package userManagement;

import java.io.Serializable;

/**
 * User class containing all necessary user information
 * @author Marius Müller
 *
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String password;
	private int gameInstanceId=0;

	public User(String name, String pw) {
		this.name=name;
		this.password=pw;
	}

	/**
	 * Checks whether the given password is correct
	 * @param s
	 * @return
	 */
	public boolean checkPassword(String s) {
		if(s.equals(password))
			return true;
		return false;
	}
	
	public String toString() {
		return "";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getGameInstanceId() {
		return gameInstanceId;
	}

	public void setGameInstanceId(int gameInstanceId) {
		if(gameInstanceId != this.gameInstanceId) {
			this.gameInstanceId = gameInstanceId;
		}
	}
	
}
