package chat;

import global.FileHelper;

public class GlobalChat {

	public static String getSite() {
		try {
			FileHelper.getFile("globalchat/globalchat.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getCSS() {
		try {
			FileHelper.getFile("globalchat/globalchat.css");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getJS() {
		try {
			FileHelper.getFile("globalchat/globalchat.js");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
