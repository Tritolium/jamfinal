package sessionHandling;

import java.net.HttpCookie;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.net.InetAddress;

import userManagement.User;

import com.sun.net.httpserver.Headers;

/**
 * Class providing the session handling, manages active sessions and user
 * actions such as login and logout
 * 
 * @author marius
 *
 */
public class SessionHandler {

	private static HashMap<User, Session> activeSessions;

	// recently used id, gets overwritten everytime a new user logs in
	private String currentId;

	private static SessionHandler instance;

	public SessionHandler() {
		currentId = "";
		activeSessions = new HashMap<User, Session>();
	}

	/**
	 * Singleton
	 * 
	 * @return
	 */
	public static SessionHandler getInstance() {
		if (instance == null) {
			instance = new SessionHandler();
		}
		return instance;
	}

	/**
	 * Returns the recently user id
	 * 
	 * @return
	 */
	public String getCurrentId() {
		return currentId;
	}

	/**
	 * Adds a session with the given user and session data
	 * 
	 * @param user
	 * @param inet
	 * @param id
	 * @param stamp
	 */
	public void addSession(User user, InetAddress inet, String id, long stamp) {
		activeSessions.put(user, new Session(id, stamp, inet));
	}

	/**
	 * Ends a session identified by the given id
	 * 
	 * @param id
	 * @return
	 */
	public long endSession(String id) {
		Set<User> set = activeSessions.keySet();
		for (User key : set) {
			Session value = activeSessions.get(key);
			if (value.getSessionId().equals(id)) {
				// id found
				activeSessions.remove(key);
				return new Date().getTime();
			}
		}
		// id not found, couldn't end session
		return 0;
	}

	/**
	 * Lists up all active sessions and returns a list
	 * 
	 * @return
	 */
	public static String showSessions() {
		String list = "";
		if (activeSessions != null) {
			Set<User> set = activeSessions.keySet();
			for (User key : set) {
				list += "Name: " + key.getName() + "\n" + "Session-ID: " + activeSessions.get(key).getSessionId() + "\n"
						+ "Inet Address: " + activeSessions.get(key).getInetAddress().toString() + "\n";
			}
		}
		return list;
	}

	/**
	 * Method extracting the session id from the cookie within the given request
	 * header
	 * 
	 * @param request
	 * @return
	 */
	public static String getSessionId(Headers request) {
		Set<Entry<String, List<String>>> set = request.entrySet();
		Iterator<Entry<String, List<String>>> it = set.iterator();
		String id = "";
		while (it.hasNext()) {
			Entry<String, List<String>> entry = it.next();
			if (entry.getKey().equals("Cookie")) {
				for (String value : entry.getValue()) {
					if (value.startsWith("id")) {
						String[] comp = value.split("=");
						String s = comp[1];
						int first = s.indexOf("\"");
						int last = s.lastIndexOf("\"");
						s = s.substring(first + 1, last);
						id = s;
					}
				}
			}
		}
		return id;
	}

	/**
	 * Checks whether the user with the given id is logged in
	 * 
	 * @param id
	 * @return
	 */
	public boolean userLoggedIn(String id) {
		Set<User> set = activeSessions.keySet();
		for (User key : set) {
			if (activeSessions.get(key).getSessionId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates a new cookie with a fresh id
	 * 
	 * @return
	 */
	public String createNewCookie() {
		currentId = createNewId();
		HttpCookie cookie = new HttpCookie("id", "" + currentId);
		cookie.setPath("/");
		cookie.setVersion(1);
		// expires when explorer is closed
		cookie.setMaxAge(-1);
		return cookie.toString();
	}

	/**
	 * Returns the session timestamp of the session with the given id
	 * 
	 * @param id
	 * @return
	 */
	public long getStampById(String id) {
		Set<User> set = activeSessions.keySet();
		for (User key : set) {
			if (activeSessions.get(key).getSessionId().equals(id)) {
				return activeSessions.get(key).getSessionStart();
			}
		}
		return 0;
	}

	/**
	 * Returns the session ip address of the session with the given id
	 * 
	 * @param id
	 * @return
	 */
	public InetAddress getInetById(String id) {
		Set<User> set = activeSessions.keySet();
		for (User key : set) {
			if (activeSessions.get(key).getSessionId().equals(id)) {
				return activeSessions.get(key).getInetAddress();
			}
		}
		return null;
	}

	/**
	 * Returns the user of the session with the given id
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(String id) {
		Set<User> set = activeSessions.keySet();
		for (User key : set) {
			if (activeSessions.get(key).getSessionId().equals(id)) {
				return key;
			}
		}
		return null;
	}

	/**
	 * Generates a fresh and unique session id
	 * 
	 * @return
	 */
	private String createNewId() {
		// takes the current timestamp and generates its hexcode
		Date date = new Date();
		long id = date.getTime();
		return Integer.toHexString((int) id);
	}

}
