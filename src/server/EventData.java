package server;

/**
 * Class to create data Strings for the ServerSentEvents
 * only the data part of an ServerSentEvent must be parsed to JSON,
 * so it is necesseray to separate this class from Class Event
 * @author Julian Hupertz
 * 
 */
public class EventData {
	private String data = "";
	
	public EventData() {
		this.data = "";
	}
	
	public EventData(String data) {
		this.data = data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getData() {
		return this.data;
	}

}
