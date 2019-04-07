package global;

import java.io.IOException;
import java.io.OutputStream;

import server.Gui;
import server.Server;

import com.sun.net.httpserver.HttpExchange;

/**
 * This class provides methods for sending responses/files to a client and for redirection
 * @author Anton Vetter, Marius Mueller
 *
 */
public class SendClass {

	/**Loads a file(when it exists) with the filepath uri to the client
	 * @param h
	 * @param uri
	 * @throws IOException
	 */
	public static void sendFile(HttpExchange h, String uri) throws IOException {
		
		String page = null;
		byte[] image = null;
		int responseCode = 200;
		byte[] toSend = null;
			
		if(uri.startsWith("/")){
			uri = uri.substring(1);
		}
		String filename = uri;
		try {
			if (FileHelper.isImage(filename)) {
				image = FileHelper.getImage(filename);
			} else
				page = FileHelper.getFile(filename);
		} catch (IOException e) {
			System.err.println("Couldn not find file: " + filename);
			responseCode = 404;
			h.sendResponseHeaders(responseCode, -1);
			return;
		}

		h.getResponseHeaders().add("Content-type", FileHelper.getMimeType(uri));
		if (page != null) {
			h.sendResponseHeaders(responseCode, page.length());

			toSend = page.getBytes();
		} else if (image != null) {
			h.sendResponseHeaders(responseCode, image.length);

			toSend = image;
		}
		OutputStream os = h.getResponseBody();
		os.write(toSend);
		os.flush();
		os.close();
		h.close();
		h.getResponseHeaders().clear();
	}
	
	/**
	 * Redirect to URI 'host+port+uri'
	 * @param h
	 * @param uri
	 * @throws IOException
	 */
	public static void redirect(int code, HttpExchange h, String uri) throws IOException{
		int responseCode = code;
		h.getResponseHeaders().set("Location", Server.host + ":" + Server.port + uri);
		h.sendResponseHeaders(responseCode, -1);
		h.close();
		
	}
	
	/**
	 * Sends a response to a client
	 * @param h
	 * @param toShow
	 */
	public static void sendResponse(HttpExchange h, String toShow) {
		try {
			OutputStream os = null;
			h.getResponseHeaders().add("Content-type", "text/html; charset=utf-8");

			h.sendResponseHeaders(200, toShow.getBytes().length);
			os = h.getResponseBody();	
			os.write(toShow.getBytes());

			os.close();
			h.close();
		} catch(IOException e) {
			e.printStackTrace();
			Gui.getInstance().print("ERROR, Unable to send response");
		}
	}
}
