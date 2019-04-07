package gameClasses;

import java.util.ArrayList;

import userManagement.User;

/**
 * Interface that has to be implemented by all games running on this server
 * @author Julian, Vetter
 */
public interface GameInterface {

	/**
	 * This method should be used and implemented to get the html data of an game. It can be stored in an external file 
	 * that must be read or only in a java String. There is a div-container just for the game, it is called id="main" and must be used in the
	 * html code. For further information, see the documentation.
	 * @return
	 */
	public String getSite();
	
	/**
	 * Just for loading the CSS Code for the certain game. Can be stored in a css file that must be read or only in a java String.
	 * @return
	 */
	public String getCSS();
	
	/**
	 * For loading the Javascript-Code for a certain game, can be stored in a js file or in a java String
	 * @return
	 */
	public String getJavaScript();
	
	/**
	 * This method is for returning the maximal amount of players that can be play this game at the same time.
	 * @return
	 */
	public int getMaxPlayerAmount();
	
	/**
	 * This method is for returning the current amount of players that play a game at the same tim.
	 * @return
	 */
	public int getCurrentPlayerAmount();
	
	/**
	 * Excecute shall change the Gamedata. It need a User object and a String s, that contains the game data.
	 * @param user
	 * @param gsonString
	 */
	public void execute(User user, String s);
	
	/**
	 * Returns the playerList of a game. This playerlist should be used as an ArrayList containing User objects.
	 * @return
	 */
	public ArrayList<User> getPlayerList();
	
	/**
	 * Returns the spectatorlist of a game. It is an arrayList that caontains spectators as User objects
	 * @return
	 */
	public ArrayList<User> getSpectatorList();
	
	
	/**
	 * Returns the gamedata of a specific user
	 * @param eventName
	 * @param user
	 * @return String
	 */
	public String getGameData(String eventName, User user);
	
	/**
	 * Adds a user to the game
	 * @param user
	 */
	public void addUser(User user);
	
	/**
	 * Adds a spectator to the game
	 * @param user
	 */
	public void addSpectator(User user);
	
	/**
	 * Method for checking if a game is joinable or not
	 * @return boolean
	 */
	public boolean isJoinable();
	
	/**
	 * When a user klicks on quit game or logout, this method is called and the game gets an information that a player has left the game.
	 * @param user
	 */
	public void playerLeft(User user);
	
	/**
	 * Returns the gamestate of a game as an enumerator.
	 * @return
	 */
	public GameState getGameState();

}
