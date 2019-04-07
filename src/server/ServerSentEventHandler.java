package server;

import gameClasses.Game;
import gameClasses.GameManager;

import java.io.IOException;

import sessionHandling.SessionHandler;
import userManagement.User;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


/**
 * General class for using ServerSent-Events
 * @author Julian Hupertz, Anton Vetter
 * 12-06-13 creation of class on server, some work on it
 */
public class ServerSentEventHandler implements HttpHandler {
	
	private GameManager gm = GameManager.getInstance();
	public int counter=0;
	public static int gameCounter=0;
	
	/**
	 *When a message via the ServerSentEvent context arrives the following method is called; additionally it adds Users to the GameInstances
	 */
	public void handle (HttpExchange h) throws IOException {

		String sessionId = SessionHandler.getSessionId(h.getRequestHeaders());
		User u = SessionHandler.getInstance().getUserById(sessionId);
		
		String[] str = h.getRequestURI().toString().split("/");
		int gameId;
		if(str.length>2) {
			gameId = Integer.valueOf(str[2]);
		}
		else {
			h.sendResponseHeaders(404, -1);
			h.close();
			return;
		}
		
		Game g = gm.getRunningGames().get(gameId);
		
		if(u.getGameInstanceId()==0) {
			u.setGameInstanceId(gameId);
			g.addUser(u, h);
		}
//		else {
//			g.addSpectator(u);
//		}
		
	}
	
}
