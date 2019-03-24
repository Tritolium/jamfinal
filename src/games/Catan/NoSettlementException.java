package games.Catan;

public class NoSettlementException extends Exception{
	public NoSettlementException(){
		super("Um eine Stadt auf einer Kreuzung zu bauen muss dort deine Siedlung stehen.");
	}
}
