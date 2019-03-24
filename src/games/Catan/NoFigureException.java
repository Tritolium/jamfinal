package games.Catan;

public class NoFigureException extends Exception{

	public NoFigureException(){
		super("Du hast keine von diesen Spielfiguren mehr.");
	}
}
