package dataStorage;

//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidKeyException;

import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Class for encrypting and decrypting String on base of 64bit (currently unused, needs to be fixed)
 * @author Marius Müller
 *
 */
public class Crypto {

	/**
	 * Encrypts input String
	 * @param toEncrypt
	 * @return
	 */
	public static String encrypt(String toEncrypt) {
		String encryptedString = null;
		try {
			DESKeySpec keySpec;
			keySpec = new DESKeySpec("YourSecr".getBytes("UTF8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);
			sun.misc.BASE64Encoder base64encoder = new BASE64Encoder();
			
			// encode plaintext string
			byte[] cleartext = toEncrypt.getBytes("UTF8");      
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			// create encrypted string
			encryptedString = base64encoder.encode(cipher.doFinal(cleartext));
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}
	
	/**
	 * Decrypts input String (currently returns a String form of a byte array, needs to get fixed)
	 * @param toDecrypt
	 * @return
	 */
	public static String decrypt(String toDecrypt) {
		String decryptedString = null;
		try {
			DESKeySpec keySpec = new DESKeySpec("YourSecr".getBytes("UTF8")); 
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);
			sun.misc.BASE64Decoder base64decoder = new BASE64Decoder();
			
			// decode encrypted string
			byte[] bytes = base64decoder.decodeBuffer(toDecrypt);
			Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decryptedBytes = (cipher.doFinal(bytes));
			
			// convert byte array to string
			decryptedString = Arrays.toString(decryptedBytes);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		return decryptedString;
	}
	
	
}
