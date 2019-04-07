package server;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import sessionHandling.SessionHandler;
import userManagement.UserManager;

import dataStorage.Data;


/**
 * This Class provides a simple GUI to interact with the server
 * @author Marius Mueller
 *
 */
@SuppressWarnings("serial")
public class Gui extends JFrame {

	private JTextArea logtext;
	private JTextArea port;
	private JScrollPane log;
	private JButton btnShowAll;
	private JButton btnShowSessions;
	private JButton btnClearTextArea;
	private JButton btnInstallGame;
	private JButton btnShowInstalledGames;
	private JButton btnInit;
	private JLabel timer;
	private int seconds;
	
	private String startTime;
	
	public static final String SERVER_VERSION = "1.0";
	
	private static Gui instance;
	
	/**
	 * Singleton
	 * @return
	 */
	public static Gui getInstance() {
		if(instance==null) {
			instance = new Gui();
		}
		return instance;
	}
	
	public Gui() {
		super("JAM Server - Version " + SERVER_VERSION);
		Container pane = getContentPane();
		pane.setLayout(null);
		
		seconds = 0;
		
		logtext = new JTextArea(5, 30);
		port = new JTextArea();
		log = new JScrollPane(logtext);
		btnShowAll = new JButton("users");
		btnShowSessions = new JButton("sessions");
		btnClearTextArea = new JButton("clear");
		btnInstallGame = new JButton("Install Game");
		btnShowInstalledGames = new JButton("Show Installed Games");
		btnInit = new JButton("Start Server on Port:");
		timer = new JLabel("" + seconds);
		
		pane.add(log);
		pane.add(btnShowAll);
		pane.add(btnShowSessions);
		pane.add(btnClearTextArea);
		pane.add(btnInstallGame);
		pane.add(btnShowInstalledGames);
		pane.add(btnInit);
		pane.add(timer);
		pane.add(port);
		
		log.setBounds(10, 10, 580, 450);
		btnShowAll.setBounds(600, 10, 200, 40);
		btnShowSessions.setBounds(600, 60, 200, 40);
		btnClearTextArea.setBounds(600,110, 200,40);
		btnInstallGame.setBounds(600, 160, 200, 40);
		btnShowInstalledGames.setBounds(600,210,200,40);
		btnInit.setBounds(600,260,200,40);
		timer.setBounds(600, 310, 180, 80);
		port.setBounds(805,270,50,20);
		port.setText("8000");

		/**
		 * Listener for the timer
		 */
		ActionListener timerTask = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				seconds++;
				if(seconds > 60) {
					int min = seconds/60;
					int restSec = seconds - (min*60);
					if(restSec < 10) {
						timer.setText("Running: " + min + ":0" + restSec + " Min");
					} else {
						timer.setText("Running: " + min + ":" + restSec + " Min");
					}
					if(min >= 60) {
						int hours = min/60;
						int restMin = min - (hours*60);
						timer.setText("Running: " + hours + " h - " + restMin + " min - " + restSec + " sec");
					}
				} else {
					timer.setText("Running: " + seconds + " Sec");
				}
			}
		};
		
		/**
		 * Set timer for time count server is running
		 */
		new Timer(1000, timerTask).start();
		
		btnInit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int p = Integer.parseInt(port.getText());
					if(Server.init(p)) {
						Date date = new Date();
						startTime = "" + new Timestamp(date.getTime());
						print("Server initialized at " + startTime);
					} else {
						print("Failed to start server");
					}
					btnInit.setEnabled(false);
				} catch(NumberFormatException n) {
					print("ERROR, Invalid port!");
				}
			}
			}
		);
		
		/**
		 * Listener for viewing all registered users
		 */
		btnShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = UserManager.getInstance().showAllUser();
				if(!s.isEmpty()) 
					print("Registered users:\n"
							+ s);
				else 
					print("ERROR, no users found");
				}
			}
		);
		
		/**
		 * @author Julian Hupertz
		 * Listener to clear the TextArea
		 */
		btnClearTextArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logtext.setText("");
				print("Running at " + Server.host + ":" + Server.port);
			}
		});
		
		/**
		 * @author Julian Hupertz
		 * Listener to install a game
		 */
		btnInstallGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = null;
				s = JOptionPane.showInputDialog("Enter the gamename");
				try {
					if(!(s==null)) {
						Data.installGame(s);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		/**
		 * @author Julian Hupertz
		 * Listener to show all installed games
		 */
		btnShowInstalledGames.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> gamesList;
					try {
						gamesList = Data.getInstalledGames();
						for(int i=0;i<gamesList.size();i++) {
							print(gamesList.get(i));
						}
					} catch (FileNotFoundException e1) {
						print("Das System kann die angebgebene Datei nicht finden");
					} catch (NullPointerException e1) {
						print("Nicht erzeugtes Objekt");
					} catch (IOException e1) {
						print("Fehler beim Einlesen der Datei");
					}

			}
		});
		
		/**
		 * Listener for viewing all active sessions
		 */
		btnShowSessions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = SessionHandler.showSessions();
				if(!s.isEmpty()) 
					print("Logged in users:\n"
							+ s);
				else 
					print("ERROR, no user logged in");
				}
			}
		);
		
		/**
		 * Listener for onClose window event, starts serialization on shut down
		 */
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(Server.isRunning) {
					try {
						// serialize users
						Data.saveUsers();
					} catch(IOException io) {
						io.printStackTrace();
					}
				}
			}
		});
	}
	
	/*
	 * Returns the time the server was started as a String
	 */
	public String getStartTime() {
		return startTime;
	}
	
	public void print(String s) {
		Date date = new Date();		
		String str = logtext.getText();
		str = str + "[" + new Timestamp(date.getTime()) + "]    " +s;
		logtext.setText(str + "\n");
		
		// scroll to bottom dynamically
		JScrollBar vertical = log.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
}
