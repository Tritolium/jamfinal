package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import com.sun.net.httpserver.*;
/**
 * Class containing the main method; initializes the server on given port and starts the GUI
 * @author Marius Mueller, Julian Hupertz, Anton Vetter
 *
 */
public class Server extends Thread {
	
	public static String host;
	public static int port;
	public static HttpServer server;
	public static int GLOBAL_ID = 1;
	public static boolean isRunning = false;
	public static HashMap<Integer, GameHandler> gameHandlerMap = new HashMap<Integer, GameHandler>();
	public static HashMap<Integer, HttpContext> gameContextMap = new HashMap<Integer, HttpContext>();

	/**
	 * The main method setting up the GUI
	 * @param args
	 * @throws IOException
	 */
	public static void main (String [] args) throws IOException {
		Gui.getInstance().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Gui.getInstance().setLocationRelativeTo(null);
		Gui.getInstance().setVisible(true);
		Gui.getInstance().setSize(900, 500);
		
	}
	
	/**
	 * Initializes the HttpServer on the given port
	 * @param port
	 * @return
	 */
	public static boolean init(int port) {
		InetAddress i;
		try {
			i = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// unknown port
			return false;
		}
		
		host = "http://" + i.getHostAddress();
		Server.port = port;
		try {
			server = HttpServer.create(new InetSocketAddress (port), 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		isRunning = true;
		
		/** Added two Handler for testing SSEs
		 * @author Julian Hupertz
		 */
		server.createContext("/", new Handler());
		server.createContext("/sse/", new ServerSentEventHandler());

		server.setExecutor(Executors.newCachedThreadPool());
		
		server.start();
		
		Gui.getInstance().print("Running at " + host + ":" + port);
		Gui.getInstance().setTitle(Gui.getInstance().getTitle() + " - Running at " + host + ":" + port);
		return true;
	}
}


