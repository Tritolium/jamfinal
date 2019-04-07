package gameClasses;

import java.io.IOException;
import java.io.OutputStream;

import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import server.Event;
import server.EventData;
import userManagement.User;

/**
 * Abstract superclass for all running game instances during the time the server is run.
 * Implements GameInterface where wrapper-methods are given to implement general game purposes.
 * Implements Runnable because every game instance is started in a certain thread.
 * HaspMap<User, HttpExchange> userMapcontains all Users mapped to their specific HttpExchange objects to send them messages
 * Event is an attribute to create the first part of an ServerSentEvent
 * EventData is an attribute to create the second part (data) of an ServerSentEvent
 * SEPARATOR is just the OS' linefeed for short call
 * DATA_FLAG is to add the data-flag before the data-part of an ServerSentEvent
 * thread is to run each game instance in a certain thread
 * @author Anton Vetter, Julian Hupertz
 */
public abstract class Game implements GameInterface, Runnable {
	private HashMap<User, HttpExchange> userMap;
	private Event event;
	private EventData eventData;
	private String SEPARATOR = System.getProperty("line.separator");
	private String DATA_FLAG = "data: ";
	private String retry = "2000";
	public int gameInstanceId;
	protected GameState gState = GameState.SETUP;
	protected User creator = null;
	
	
	private Thread thread;
	
	/** 
	 * Constructor calls a new Thread with itself as param and calls the Thread.start() method
	 * @author Julian Hupertz
	 */
	protected Game(){
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Method sends an new Event / new data to a specific User/player of a certain gameInstance.
	 * @author Julian Hupertz
	 * @param u User - specific User
	 * @param eventName - eventName
	 */
	protected final void sendGameDataToUser (User u, String eventName) {
		this.sendGameDataToUser(u, eventName, "0");
	}
	
	/**
	 * Method sends a new Event / new data to a specific User/Player of a certain game Instance.
	 * @param u - a User
	 * @param eventName - the Name of an Event
	 * @param id - Id of an Event
	 */
	protected final void sendGameDataToUser(User u, String eventName, String id) {
		
		Event event = new Event(this.retry, eventName, id);
		EventData eventData = new EventData(getGameData(eventName, u));
		String message = createServerSentEvent(event, eventData);
		OutputStream os = userMap.get(u).getResponseBody();
		
		try {
			os.write(message.getBytes());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method sends an new Event to all users/clients/players of a certain game instance.
	 * @param retry - retry-part of an ServerSentEvent
	 * @param eventName - name of the event of an ServerSentEvent
	 * @param eventData - real data-part of an ServerSentEvent
	 * @author - Julian Hupertz
	 */
	protected final void sendGameDataToClients(String eventName){
		this.sendGameDataToClients(eventName, "0");
	}
	
	/**
	 * Method sends an new Event to all users/clients/players of a certain game instance.
	 * @param retry - retry-part of an ServerSentEvent
	 * @param eventName - name of the event of an ServerSentEvent
	 * @param eventData - real data-part of an ServerSentEvent
	 * @param id - the id of an ServerSentEvent (don't think that is will be used)
	 * @author Julian Hupertz
	 */
	protected final void sendGameDataToClients(String eventName, String id){
		
		this.event = new Event(this.retry, eventName, id);
		
		
		for(User key : userMap.keySet()) {
			
			this.eventData = new EventData(getGameData(eventName, key));
			String message = createServerSentEvent(this.event, this.eventData);
			OutputStream os = userMap.get(key).getResponseBody();
			
			try {
				os.write(message.getBytes());
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Creates an ServerSentEvent and returns it as String
	 * @param e - Event
	 * @param ed - EventData
	 * @return - String
	 * @author Julian Hupertz
	 */
	public String createServerSentEvent(Event e, EventData ed) {
		return e.toString() + returnJsonString(ed) + SEPARATOR + SEPARATOR;
	}
	
	/**
	 * Creates a json-object of an EventData object
	 * @param j
	 * @return String + json-object
	 * @author Julian
	 */
	protected String returnJsonString(EventData j) {
		//Gson gson = new GsonBuilder().create();
		return DATA_FLAG + j.getData();
	}
	
	/**
	 * Adds User to the userMap of a game instance.
	 * Additionally, it opens the Header to a new User and sends the responseHeader
	 * Third, it adds a User to the User-Map(??) of the concrete game class with an empty game state ("")
	 * (for Example see Testgame)
	 * @param user
	 * @param exchange
	 * @author Julian Hupertz
	 */
	public final void addUser(User user, HttpExchange exchange){
		userMap.put(user, exchange);
		exchange.getResponseHeaders().add("Content-type", "text/event-stream; charset=utf-8");
		try {
			exchange.sendResponseHeaders(200, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(creator==null) {
			creator = user;
		}
		
		if(this.isJoinable()) {
			addUser(user);
		}
		else {
			addSpectator(user);
		}
		
	}
	
	/**
	 * Sets the first user who connects to a game to gamecreator
	 * @return the creator, type of User
	 * @author Julian Hupertz
	 */
	public final User getGameCreator() {
		return this.creator;
	}
	
	/**
	 * run method for the thread
	 * @author Julian Hupertz
	 */
	public void run(){
		this.userMap = new HashMap<User, HttpExchange>(); 
	}
	
	/**
	 * returns the retry value of an server sent event
	 * @return retry - String
	 * @author Julian Hupertz
	 */
	protected String getRetry() {
		return retry;
	}
	
	/**
	 * Sets the retry value for the SSE's
	 * @param retry
	 * @author Julian Hupertz
	 */
	protected void setRetry(String retry) {
		this.retry = retry;
	}
	
	/**
	 * Getter for the HashMap
	 * @return HashMap<User, HttpExchange>
	 * @author Julian Hupertz
	 */
	public HashMap<User, HttpExchange> getUserMap() {
		return this.userMap;
	}
	
	/**
	 * Sets the game state = closed, that will occur when a game has implemented this functionality to close a game (by a player's action)
	 * or when a game has just one player left, WHO leaves the game by quit or logout. Gamestate will be closed
	 * @author Julian Hupertz
	 */
	public void closeGame(){
		this.gState = GameState.CLOSED;
	}
}
