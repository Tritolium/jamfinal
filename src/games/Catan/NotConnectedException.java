package games.Catan;

public class NotConnectedException extends Exception {
	public NotConnectedException() {
		super("Du kannst Straßen oder Kreuzungen nur bauen, wenn du sie an dein bestehendes System anbaust");
	}
}
