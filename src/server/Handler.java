package server;

import global.FileHelper;
import global.Parser;
import global.SendClass;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sessionHandling.SessionHandler;
import userManagement.User;
import userManagement.UserManager;
import website.Website;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dataStorage.Data;

/**
 * Handler class operating on the '/' context, responsible for common user
 * interaction such as login, logout and register;
 * 
 * @author Marius Müller
 * 
 *         12-07-2013 : cleaned the code, exported parse and send methods
 * 
 */
public class Handler implements HttpHandler {

	private UserManager um;
	private SessionHandler sh;
	private String uriContext ="";

	static final int EVENT_ERROR = 0;
	static final int EVENT_LOGIN = 1;
	static final int EVENT_REGISTER = 2;
	static final int EVENT_LOGOUT = 3;

	private String toShow;

	public Handler() {
		this.um = UserManager.getInstance();
		this.sh = SessionHandler.getInstance();
	}

	/**
	 * handle method for http requests, called when a request arrives the opened
	 * port
	 */
	public void handle(HttpExchange httpExchange) throws IOException {
		
		String requestMethod = httpExchange.getRequestMethod();
		URI requestedUri = httpExchange.getRequestURI();
		
		String query = requestedUri.getRawQuery();
		HashMap<String, String> receivedParameters = null;
		Headers responseHead = httpExchange.getResponseHeaders();
		Headers requestHead = httpExchange.getRequestHeaders();
		String ref = "";
		if (requestHead.containsKey("Referer")) {
			ref = httpExchange.getRequestHeaders().get("Referer").get(0);
		}

		String uri = httpExchange.getRequestURI() + "";

		if (FileHelper.validFile(uri)) {
			String filename = uri.substring(1);

			SendClass.sendFile(httpExchange, filename);
			return;
		}

		InetAddress inet = httpExchange.getRemoteAddress().getAddress();

		// initial html page to view, gets modified according to incoming request
		showIndex();

		ArrayList<String> components = Parser.getPathComponents(requestedUri);

		// parse request for cookies
		String id = SessionHandler.getSessionId(requestHead);

		// restore session if able
		if (id.length() > 0  && sh.userLoggedIn(id)) {
			if (sh.getUserById(id).getGameInstanceId() == 0) {
				showStart(sh.getUserById(id).getName());
			} else {
				User u = sh.getUserById(id);
				String gamePath = Server.gameContextMap.get(
						u.getGameInstanceId()).getPath();
				SendClass.redirect(302, httpExchange, gamePath);
				return;
			}
		}

		if (requestMethod.equals("GET")) {
			if (uri.contains("/createGame/")) {
				/*
				 * a new game has been started, go for it!
				 */
				int gameInstanceId = SessionHandler.getInstance()
						.getUserById(id).getGameInstanceId();
				String[] uriArr = uri.split("/");
				ArrayList<String> gamesList = Data.getInstalledGames();

				if (gamesList.contains(uriArr[1]) && gameInstanceId == 0) {

					GameHandler gameHandler = new GameHandler();
					HttpContext context = null;

					String newGameUri = "/" + uriArr[1] + "/"
							+ Server.GLOBAL_ID + "/";

					context = Server.server.createContext(newGameUri,
							gameHandler);
					gameClasses.GameManager.getInstance().createGame(
							Server.GLOBAL_ID, uriArr[1]);
					Server.gameHandlerMap.put(Server.GLOBAL_ID, gameHandler);
					Server.gameContextMap.put(Server.GLOBAL_ID, context);
					SendClass.redirect(302, httpExchange, newGameUri);
					
					Server.GLOBAL_ID++;

					return;
				}
				showError("Das Spiel existiert nicht!", "create");
				SendClass.sendResponse(httpExchange, toShow);
				return;
			}
			// no game requested, go on
			if (query != null && !query.isEmpty()) {
				try {
					// get get request parameters
					receivedParameters = Parser.parseGetRequest(query);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (components.size() != 0) {
				// parse request uri and show requested page
				String s = components.get(0);
				switch (s) {
				case "login":
					if (!sh.userLoggedIn(id)) {
						showLogin();
					}
					break;
				case "register":
					if (!sh.userLoggedIn(id)) {
						showRegister();
					}
					break;
				case "create":
					if (sh.userLoggedIn(id)) {
						showCreate(sh.getUserById(id).getName());
					}
					break;
				case "join":
					if (sh.userLoggedIn(id)) {
						showJoin(sh.getUserById(id).getName());
					}
					break;
					/**
					 * To do: Chat
					 */
				//case "chat":
				//	if (sh.userLoggedIn(id)) {
				//		showChat(sh.getUserById(id).getName());
				//	}
				//	break;
				case "logout":
					if (sh.userLoggedIn(id)) {
						String name = sh.getUserById(id).getName();
						// try logout and write event to logfile
						long out = logout(id);
						if (out >= 0) {
							String diff = "" + out;
							if (out < 1) {
								diff = "< 1";
							}
							writeToLogfile(EVENT_LOGOUT, inet,
									"Logging out user \"" + name
											+ "\" succeeded; Session length: "
											+ diff + " " + " Minutes");
							Gui.getInstance().print("Logging out user \"" + name
											+ "\" succeeded; Session length: "
											+ diff + " " + " Minutes");
						} else {
							writeToLogfile(EVENT_LOGOUT, inet,
									"logging out user \"" + name + "\" failed");
							Gui.getInstance().print("logging out user \"" + name + "\" failed");
						}
						Website.blankContent();
						showIndex();
					}
					break;
				default:
					break;
				}
			}
		} else if (requestMethod.equals("POST")) {
			try {
				// get post request parameters
				receivedParameters = Parser.parsePostRequest(httpExchange
						.getRequestBody());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (components.size() != 0) {
				if (components.get(0).equals("login")
						|| components.get(0).equals("index")) {
					if (receivedParameters != null && !sh.userLoggedIn(id)) {
						// search name in HashMap
						String name = "";
						String pw = "";
						for (String key : receivedParameters.keySet()) {
							switch (key) {
							case "name":
								name = receivedParameters.get(key);
								break;
							case "password":
								pw = receivedParameters.get(key);
								break;
							default:
								break;
							}
						}
						if (Parser.parseReferer(ref).equals("login")) {
							if (um.userExists(name)) {
								// try login and write event to logfile
								if (login(name, pw, responseHead, inet)) {
									writeToLogfile(EVENT_LOGIN, inet,
											"Logging in user \"" + name
													+ "\" succeeded");
									Gui.getInstance().print("Logging in user \"" + name
													+ "\" succeeded");
								} else {
									writeToLogfile(EVENT_LOGIN, inet,
											"Logging in user \"" + name
													+ "\" failed");
									Gui.getInstance().print("Logging in user \"" + name
											+ "\" failed");
								}
							} else {
								showFeedback("Benutzer nicht gefunden!", true);
								showLogin();
							}
						} else if (Parser.parseReferer(ref).equals("register")) {
							// check for white spaces
							Pattern pattern = Pattern.compile("\\s");
							Matcher matcher = pattern.matcher(name);
							boolean spacesFound = matcher.find();

							if (!um.userExists(name) && !spacesFound) {
								// try register and write event to logfile
								if (register(name, pw)) {
									writeToLogfile(EVENT_REGISTER, inet,
											"Registering user \"" + name
													+ "\" succeeded");
									Gui.getInstance().print("Registering user \"" + name
													+ "\" succeeded");
									showFeedback("Registrierung erfolgreich!", false);
									showLogin();
								} else {
									writeToLogfile(EVENT_REGISTER, inet,
											"Registering user \"" + name
													+ "\" failed");
									Gui.getInstance().print("Registering user \"" + name
											+ "\" failed");
									showFeedback("UNKNOWN_ERROR", true);
									showRegister();
								}

							} else if (spacesFound) {
								showFeedback("Ungueltiger Benutzername!",true);
								showRegister();
							} else {
								showFeedback("Benutzername bereits vergeben!", true);
								showRegister();
							}
						}
					} else {
						// at least one text field was left blank, could not process request
						Website.setFeedback("Ungueltiger Wert, bitte alle Felder ausfuellen!",  true);
						switch(Parser.parseReferer(ref)) {
						case "login":
							showLogin();
							break;
						case "register":
							showRegister();
							break;
						default:
							showIndex();
							break;
						}
						Gui.getInstance().print("ERROR, key not found");
					}
				}
			}
		}

		// send response to client
		SendClass.sendResponse(httpExchange, toShow);

	}

	/**
	 * Method to perform login, checks whether login successful or not
	 * 
	 * @param name
	 * @param pw
	 * @param head
	 * @param inet
	 * @return
	 */
	private boolean login(String name, String pw, Headers head, InetAddress inet) {
		if (name.length() >= 1 && pw.length() >= 1) {
			User user = um.getUserByName(name);
			if (user != null) {
				if (user.checkPassword(pw)) {
					// Password OK
					showFeedback("Login", false);
					showStart(name);
					String cookie = sh.createNewCookie();
					head.set("Set-Cookie", cookie);
					// create session, login successful
					user.setGameInstanceId(0);
					sh.addSession(user, inet, sh.getCurrentId(),
							new Date().getTime());
					return true;
				} else {
					// wrong password
					showFeedback("Falsches Passwort!", true);
					showLogin();
					return false;
				}
			} else {
				// user not registered yet
				showFeedback("Benutzer noch nicht registriert!", true);
				showLogin();
				return false;
			}
		} else {
			// received empty strings, could not process login
			showFeedback("Invalid parameter, fill all fields", true);
			showLogin();
			return false;
		}
	}

	/**
	 * Perform register, returns true if successful
	 * 
	 * @param name
	 * @param pw
	 * @return
	 */
	private boolean register(String name, String pw) {
		if (name.length() >= 1 && pw.length() >= 1) {
			um.addUser(name, pw);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Perform logout and end session, returns session length if successful
	 * 
	 * @param id
	 * @return
	 */
	private long logout(String id) {
		long start = sh.getStampById(id);
		long end = sh.endSession(id);
		if (end > 0) {
			long ret = end - start;
			ret /= 1000;
			ret /= 60;
			return ret;
		} else {
			return -1;
		}
	}


	/**
	 * Writes an event handled by this class into a text file
	 * 
	 * @param event
	 * @param inet
	 * @param msg
	 */
	private void writeToLogfile(int event, InetAddress inet, String msg) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"logfile.txt", true));
			Date date = new Date();
			String str = "[" + new Timestamp(date.getTime()) + "] [IP: " + inet
					+ "] ";
			switch (event) {
			case EVENT_ERROR:
				out.write(str + "[ERROR] " + msg);
				break;
			case EVENT_LOGIN:
				out.write(str + "[LOGIN] " + msg);
				break;
			case EVENT_REGISTER:
				out.write(str + "[REGISTER] " + msg);
				break;
			case EVENT_LOGOUT:
				out.write(str + "[LOGOUT] " + msg);
				break;
			default:
				break;
			}
			out.newLine();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * The following methods modify the website html code according to handled
	 * requests
	 * 
	 */
	private void showIndex() {
		Website.blankContent();
		toShow = Website.getPage(false, "", false, false, uriContext);
	}

	private void showStart(String name) {
		Website.setStart(Gui.getInstance().getStartTime());
		toShow = Website.getPage(true, name, false, false, uriContext);
	}

	private void showLogin() {
		Website.setLogin();
		toShow = Website.getPage(false, "", false, false, uriContext);
	}

	private void showRegister() {
		Website.setRegister();
		toShow = Website.getPage(false, "", false, false, uriContext);
	}

	private void showError(String error, String referer) {
		Website.setError(error, referer);
		toShow = Website.getPage(false, "", false, false, uriContext);
	}

	private void showFeedback(String msg, boolean error) {
		Website.setFeedback(msg, error);
		toShow = Website.getPage(false, "", false, false, uriContext);
	}

	private void showCreate(String name) {
		Website.setCreate();
		toShow = Website.getPage(true, name, false, false, uriContext);
	}

	private void showJoin(String name) {
		Website.setJoin();
		toShow = Website.getPage(true, name, false, false, uriContext);
	}

	/**
	 * To do: Chat
	 */
	//private void showChat(String name) {
	//	Website.setGlobalChat();
	//	toShow = Website.getPage(true, name, false, true);
	//}

}
