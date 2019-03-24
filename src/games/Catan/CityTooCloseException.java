package games.Catan;

public class CityTooCloseException extends Exception{
	public CityTooCloseException(){
		super("Es Steht bereits eine Siedlung auf einer benachbarten Kreuzung");
	}
}
