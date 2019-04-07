package sessionHandling;

import java.net.InetAddress;

/**
 * Session Class containing the session data
 * @author Marius Müller
 *
 */
public class Session {

	private String sessionId;
	private long sessionStart;
	private InetAddress inetAddress;
	
	public Session(String id, long start, InetAddress ip) {
		this.sessionId = id;
		this.sessionStart = start;
		this.inetAddress = ip;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public long getSessionStart() {
		return sessionStart;
	}

	public InetAddress getInetAddress() {
		return inetAddress;
	}
	
}
