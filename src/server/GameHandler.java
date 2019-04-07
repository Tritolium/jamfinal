package server;

import gameClasses.Game;
import gameClasses.GameManager;
import global.FileHelper;
import global.SendClass;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import sessionHandling.SessionHandler;
import userManagement.User;
import website.Website;
import global.Parser;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Fetches the game data that is created by the clients.
 * @author Julian Hupertz, Anton Vetter
 *
 */
public class GameHandler implements HttpHandler {

	private GameManager gm = GameManager.getInstance();
	private SessionHandler sh = SessionHandler.getInstance();
	
	/**
	 * Handle method of this Handler first redirects the game data that is created by the users / clients to the certain game instances.
	 * If a http request contains a query with gamedata, the query information are sent to the game-instance
	 * If a http request contains a query with that is created by the "Spiel verlassen" - Button, the user is deleted by the game instance and all
	 * else where he or his information are saved (except the UserManagement)
	 * If a http request contains logout information, the same happens as by using the quit button but a user is also logged out
	 * Additionally, this method presents the game instance when it is created/called
	 * @author Julian, Anton
	 */
	@Override
	public void handle(HttpExchange h) throws IOException {
		String uri = h.getRequestURI() + "";
		String context = h.getHttpContext().getPath();
		String uriArr[] = uri.split("/");
		int gameId = Integer.parseInt(uriArr[2]);
		String userId = SessionHandler.getSessionId(h.getRequestHeaders());
		User user = sh.getUserById(userId);
		Game g = gm.getRunningGames().get(gameId);
		
		String query = h.getRequestURI().getQuery();
		
		if (query == null || query.isEmpty()) {
			if (FileHelper.validFile(uri)) {
				
				String filename = this.getFilePath(h);

				SendClass.sendFile(h, filename);
				return;
			}

		}
		
		if(query != null && query.equals("logout")){

			HashMap<Integer, Game> map = gm.getRunningGames();
			
			for(int i : map.keySet()) {
				Event e = new Event("LOGOUT"+System.getProperty("line.separator"));
				EventData ed = new EventData("logout");
				String message = map.get(i).createServerSentEvent(e, ed);
				

				OutputStream os = map.get(i).getUserMap().get(user).getResponseBody();
				
				try {
					os.write(message.getBytes());
					os.flush();
					map.get(i).getUserMap().get(user).getResponseHeaders().clear();
					map.get(i).getUserMap().get(user).close();
					os.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				if(map.get(i).getCurrentPlayerAmount()>1) {
					map.get(i).playerLeft(user);
					user.setGameInstanceId(0);
				} else {
					map.get(i).closeGame();
					user.setGameInstanceId(0);
				}
				
			}
			return;
		}
		
		if(query != null && query.equals("quit")) {
			
			int gameInstanceId = user.getGameInstanceId();

			HashMap<Integer, Game> map = gm.getRunningGames();
			
			for(int i : map.keySet()) {
				if(i==gameInstanceId) {
					Event e = new Event("QUIT"+System.getProperty("line.separator"));
					EventData ed = new EventData("quit");
					String message = map.get(i).createServerSentEvent(e, ed);
					Website.setFeedback("You have left the Game!", true);

					OutputStream os = map.get(i).getUserMap().get(user).getResponseBody();
					
					try {
						os.write(message.getBytes());
						os.flush();
						map.get(i).getUserMap().get(user).getResponseHeaders().clear();
						map.get(i).getUserMap().get(user).close();
						os.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					if(map.get(i).getCurrentPlayerAmount()>1) {
						map.get(i).getUserMap().remove(user);
						map.get(i).playerLeft(user);
						user.setGameInstanceId(0);
					} else {
						map.get(i).closeGame();
						map.get(i).getUserMap().remove(user);
						user.setGameInstanceId(0);
					}
				}
			}
			
			h.close();
			return;
		}

		if (query != null && uri.contains("gameData")) {	
			g.execute(user, query);
			h.sendResponseHeaders(204, -1);
			h.close();
			return;
		}
		
		Website.setGamePage(gm.getRunningGames().get(gameId));
		global.SendClass.sendResponse(h, website.Website.getPage(true, user.getName(), true, false, context));

	}

	/**
	 * Returns the path of the requested file
	 * @param h
	 * @return
	 */
	private String getFilePath(HttpExchange h) {
		ArrayList<String> uC = Parser.getPathComponents(h.getRequestURI());
		String filename = "";
		for(int i = 0; i < uC.size(); i++) {
			if(i != 1) {
				filename += uC.get(i);
				if(!(i == uC.size()-1)) {
					filename += "/";
				}
			}
		}
		return filename;
	}

}
