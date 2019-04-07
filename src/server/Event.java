package server;

/**
 * Defines the control flags of a ServerSentEvent
 * For more details see JavaDoc of Class EventData
 * @author Julian Hupertz
 *
 */
public class Event {
	private String retry = "";
	private String event = "";
	private String id = "";
	private static String SEPARATOR = System.getProperty("line.separator");
	
	public Event(String event) {
		this.event = "event: " + event;
	}
	
	public Event(String retry, String event, String id) {
		this.retry = "retry: " + retry + SEPARATOR;
		this.event = "event: " + event + SEPARATOR;
		this.id = "id: " + id + SEPARATOR;
	}
	
	/**
	 * Override of the toString method
	 */
	public String toString() {
		String s = "";
		
		if(this.retry.equals("retry: " + SEPARATOR)) {
			s = this.event + this.id;
		}
		else {
			s = this.retry + this.event + this.id;
		}
		return s;
	}

	/**
	 * Returns the retry String
	 * @return
	 */
	public String getRetry() {
		return retry + SEPARATOR;
	}

	/**
	 * Sets the retry String
	 * @param retry
	 */
	public void setRetry(String retry) {
		this.retry = "retry: " + retry + SEPARATOR;
	}

	/**
	 * Returns the event String
	 * @return
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * Sets the event String
	 * @param event
	 */
	public void setEvent(String event) {
		this.event = "event: " + event + SEPARATOR;
	}

	/**
	 * Returns the id String
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id String
	 * @param id
	 */
	public void setId(String id) {
		this.id = "id : " + id + SEPARATOR;
	}
}
