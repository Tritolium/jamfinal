package games.Catan;

public class OfferToPlayerException extends Exception{
	public OfferToPlayerException(){
		super("Der Handel ist mit der Bank zu diesen Konditionen nicht möglich"
				+ " und wird den anderen Spielern angeboten");
	}
}
