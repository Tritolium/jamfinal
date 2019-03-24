package games.Catan;

public class WinnerException extends Exception {
	public WinnerException(){
		super("Spiel beendet!");
	}
}
