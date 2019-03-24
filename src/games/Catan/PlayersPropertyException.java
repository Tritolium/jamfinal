package games.Catan;

public class PlayersPropertyException extends Exception{
	public PlayersPropertyException(){
		super("Dieses Objekt gehört bereits einem Spieler");
	}
}
