package userManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


import dataStorage.Data;

/**
 * General class for user management, contains all registered users
 * @author Marius Mueller
 *
 */
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<User> userList;
	private static UserManager instance;
	
	private UserManager() {
		userList = new ArrayList<User>();		
	}
	
	/**
	 * Singleton
	 * @return
	 */
	public static UserManager getInstance() {
		if(instance==null) {
			instance = new UserManager();
			
			try {
				// try to deserialize userlist from saved file
				instance = Data.loadUsers();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	/**
	 * Add a just registered user
	 * @param name
	 * @param pw
	 */
	public void addUser(String name, String pw) {
		userList.add(new User(name, pw));
	}
	
	/**
	 * Return the user with the given name
	 * @param name
	 * @return
	 */
	public User getUserByName(String name) {
		Iterator<User> i = userList.iterator();
		while(i.hasNext()) {
			User u = i.next();
			if(u.getName().equals(name)) {
				return u;
			}
		}
		return null;
	}
	
	/**
	 * Returns a String listing all registered users
	 * @return
	 */
	public String showAllUser() {
		String list = "";
		Iterator<User> i = userList.iterator();
		while(i.hasNext()) {
			list += "Name: " + i.next().getName() + "\n";
		}
		return list;
	}
	
	/**
	 * Returns the ArrayList containing all registered users
	 * @return
	 */
	public ArrayList<User> getUsers() {
		return userList;
	}
	
	/**
	 * Checks whether the user with the given name is registered
	 * @param name
	 * @return
	 */
	public boolean userExists(String name) {
		Iterator<User> i = userList.iterator();
		while(i.hasNext()) {
			if(i.next().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
