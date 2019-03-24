package games.Catan;

public class NoMoreCardsException extends Exception{

	public NoMoreCardsException(){
		super("Es sind keine Karten mehr auf dem Stapel");
	}
}
