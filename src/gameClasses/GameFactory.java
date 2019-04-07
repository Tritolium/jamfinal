package gameClasses;

/**
 * Factory class to create an game Instance
 * @author Anton Vetter
 */

public class GameFactory {
	
	
	/**
	 * This Method creates and returns a game instance
	 * @param id - game id
	 * @param gameName - the name of the conrete game
	 * @return - returns the game instance (object)
	 * @author Anton Vetter
	 */
	public static Game createGame(int id, String gameName){
		Game game = null;
		try {
			game = (Game) Class.forName("games."+gameName+"."+gameName).newInstance();
			game.gameInstanceId = id;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		}		
		return game;
	}	

}
