package gameClasses;

import java.util.HashMap;

/**
 * Class GameManager to manage the gameinstances
 * @author Anton Vetter
 *
 */
public class GameManager {
	private HashMap<Integer, Game> runningGames;
	private static GameManager instance;
	
	/**
	 * Constructor
	 */
	private GameManager(){
		runningGames = new HashMap<Integer, Game>();
	}
	
	/**
	 * Singleton pattern
	 * @return
	 */
	public static GameManager getInstance(){
		if(instance==null){
			instance=new GameManager();
		}
		return instance;
	}
	
	/**
	 * returns the running game instances
	 * @return HashMap
	 */
	public HashMap<Integer, Game> getRunningGames() {
		return runningGames;
	}
	
	/**
	 * Adds a game to the HashMap
	 * @param id - int
	 * @param g - Game object
	 */
	private void addGame(int id, Game g){
		runningGames.put(id, g);
	}
	
	/**
	 * Creates a game instance by calling the create game method of GameFactory
	 * @param id - int
	 * @param gameName - name of the game
	 */
	public void createGame(int id, String gameName){
		if(!(runningGames.containsKey(id))) {
			int gameInstanceId = id;
			Game g=GameFactory.createGame(gameInstanceId, gameName);
			addGame(id, g);
		}
	}

}
