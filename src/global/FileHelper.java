package global;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * This class provides methods in order to parse and request filenames and files
 * @author Marius Müller, Anton Vetter
 *
 */
public class FileHelper {
	
	private static HashMap<String, byte []> images = new HashMap<String, byte[]>();
	
	/**
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static String getFile(String filename) throws IOException{
		File file = new File(filename);		
		String page = "";
		page = Files.toString(file, Charsets.UTF_8);
		return page;
	}
	
	/**
	 * Returns a byte array containing the requested file
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] getImage(String filename) throws IOException {
		if(images.containsKey(filename)) return images.get(filename);
		File file = new File(filename);
		
		byte[] bytes = null;
		
		bytes = Files.toByteArray(file);
		images.put(filename, bytes);
		return bytes;
	}

	/**
	 * Returns the mime type of the input file extension from the given URI
	 * @param uri
	 * @return
	 */
	public static String getMimeType(String uri){
		String ext = Files.getFileExtension(uri);
		switch(validFiles.valueOf(ext)){
		case bmp:
			return "image/bmp";
		case html:
			return"text/html";
		case css:
			return"text/css";
		case gif:
			return"image/gif";
		case ico:
			return"image/x-icon";
		case jpg:case jpeg:
			return"image/jpeg";
		case png:
			return"image/x-png";
		case js:
			return"text/javascript";
		default:
			return"text/html";
		}
	}
	
	/**
	 * Checks whether the given URI contains a valid file ending
	 * @param uri
	 * @return
	 */
	public static boolean validFile(String uri){
		if(uri.contains(".")){
			String ext = Files.getFileExtension(uri);
			for(validFiles file : validFiles.values()) {
				if(ext.equals(file.name())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the given filename represents an image
	 * @param filename
	 * @return
	 */
	public static boolean isImage(String filename){
		String ext = Files.getFileExtension(filename);
		if(validFiles.valueOf(ext).ordinal()>2){
			return true;
		}
		return false;
	}
	
	/**
	 * Enum if valid file endings
	 *
	 */
	enum validFiles{
		html,
		css,
		js,
		bmp,
		jpg,
		jpeg,
		gif,
		ico,
		png
	}

}
