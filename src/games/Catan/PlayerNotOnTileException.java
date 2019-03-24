package games.Catan;

public class PlayerNotOnTileException extends Exception{
	public PlayerNotOnTileException(){
		super("Der Spieler hat Keine Siedlung an diesem Feld oder auf dieser kreuzung steht keine Siedlung");
	}
}
