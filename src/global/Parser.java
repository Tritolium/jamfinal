package global;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * General class for parsing requests and URIs, provides static methods in order to do so
 * @author Marius Mueller
 * 
 * 12-07-2013: Created class for improved packaging, imported all relevant methods from Handler class
 *
 */
public class Parser {

	/**
	 * Parses a given URI and splits all components of the path to the '?'
	 * @param uri
	 * @return
	 */
	public static ArrayList<String> getPathComponents(URI uri) {
		ArrayList<String> paramList = new ArrayList<String>();
		String s = uri.getPath();
		if (!s.substring(1).isEmpty()) {
			int i = s.length();
			if(s.contains("?")) {
				i = s.indexOf("?")-1;
			}
			String toSplit = s.substring(1, i);
			String[] array = toSplit.split("/");
			for(String param : array) {
				paramList.add(param);
			}
		}
		return paramList;
	}
	
	/**
	 * Parses a GET request and returns all parameters
	 * @param query
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static HashMap<String,String> parseGetRequest(String query) throws ArrayIndexOutOfBoundsException {
		HashMap<String,String> map = new HashMap<String,String>();
		if(query.length() >= 1) {
			String[] array = query.split("&");
			for(String pair : array) {
				String[] comp = pair.split("=");
				if(comp.length>=2)
					map.put(comp[0], comp[1]);
			}
		}
		return map;
	}
	
	/**
	 * Parses a POST request and returns all parameters
	 * @param body
	 * @return
	 * @throws IOException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	@SuppressWarnings("resource")
	public static HashMap<String,String> parsePostRequest(InputStream body) 
			throws IOException, ArrayIndexOutOfBoundsException {
		HashMap<String,String> map = new HashMap<String,String>();
		String toParse = new Scanner(body, "UTF-8").useDelimiter("\\A").next();
		if(toParse.length() >= 1) {
			String[] array = toParse.split("&");
			for(String pair : array) {
				String[] comp = pair.split("=");
				if(comp.length > 1)
					map.put(comp[0], comp[1]);
				else
					throw new ArrayIndexOutOfBoundsException();
			}
		}
		return map;
	}
	
	/**
	 * Parses a String and returns the referer
	 * @param ref
	 * @return
	 */
	public static String parseReferer(String ref) {
		int i = ref.lastIndexOf("/");
		if(ref.contains("?")) {
			int j = ref.indexOf("?");
			return ref.substring(i+1,j);
		}
		return ref.substring(i+1);
	}
	
}
