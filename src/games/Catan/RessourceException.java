package games.Catan;

public class RessourceException extends Exception{
	public RessourceException(){
		super("Du hast die benötigten Ressourcen nicht vorrätig!");
	}
}
