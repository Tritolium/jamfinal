package games.Catan;

public class OtherPlayersPropertyException extends Exception{
	public OtherPlayersPropertyException(){
		super("Dieses Objekt gehört bereits einem anderen Spieler");
	}
}
