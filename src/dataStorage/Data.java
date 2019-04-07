package dataStorage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import userManagement.UserManager;

/**
 * Class for serialization (without encryption/decryption)
 * @author Marius Mueller
 *
 */
public class Data {

	/**
	 * Writes the UserManager instance to 'Users.sav'
	 * @throws IOException
	 */
	public static void saveUsers() throws IOException {
		/*// encrypt all passwords
		Iterator<User> i = UserManager.getInstance().getUsers().iterator();
		while(i.hasNext()) {
			User u = i.next();
			u.setPassword(Crypto.encrypt(u.getPassword()));
		}*/
		ObjectOutputStream oos = null;
		oos = new ObjectOutputStream(new FileOutputStream("Users.sav"));
		oos.writeObject(UserManager.getInstance());
		oos.close();
	}
	
	/**
	 * Loads the UserManager instance saved on close from 'Users.sav' and restores it
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public static UserManager loadUsers() throws IOException, FileNotFoundException, ClassNotFoundException {
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(new FileInputStream("Users.sav"));
		Object a = ois.readObject();
		ois.close();
		UserManager result = (UserManager) a;
		/*
		// decrypt passwords
		Iterator<User> i = result.getUsers().iterator();
		while(i.hasNext()) {
			User u = i.next();
			u.setPassword(Crypto.decrypt(u.getPassword()));
		}*/		
		return result;
	}
	
	/**
	 * @author Julian Hupertz
	 * Method "installs" a Game that shall be used on the server. It puts an entry into a textfile so the server knows its "installed-Games".
	 * ->next step could be that the method gets an jar file and creates the entry because of this to an xml-file
	 * @param s String - it contains the gamename that is written to the textfile "installedGames"
	 * @throws IOException
	 */
	
	public static void installGame(String s) throws IOException {
		FileWriter fw = null;
		
		try {
			fw = new FileWriter("installedGames.txt", true);
			fw.write(s);
			fw.append(System.getProperty("line.separator"));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fw != null) {
				try {
					fw.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Method reads out the file "installedGames.txt" 
	 * @author Julian Hupertz
	 * @return gamesList; ArrayList<String> - contains the installedGames
	 * @throws FileNotFoundException
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static ArrayList<String> getInstalledGames() throws FileNotFoundException, NullPointerException, IOException {
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> gamesList = new ArrayList<String>();
		String strTemp = null;
		
		try {
			fr = new FileReader("installedGames.txt");
			br = new BufferedReader(fr);
			
			while((strTemp = br.readLine()) != null) {
				gamesList.add(strTemp);
			}
		}

		finally {
			br.close();
		}
			
		return gamesList;
	}
}
