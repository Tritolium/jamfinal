package games.Catan;

public class MustOwnCardException extends Exception {
	public MustOwnCardException() {
		super("Du musst diese Karte besitzen um sie spielen zu können");
	}
}
