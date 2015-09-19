package appLauncher.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class CommonUtils {

	private static final Logger logger = Logger.getLogger(CommonUtils.class);

	public static boolean isEmpty(String s) {
		if (s == null || s.length() == 0)
			return true;
		return false;
	}

	/**
	 * Encrypts given string with given privateKey with using 128 bit AES
	 * algorithm. If key length is low ( ie very short string ) encryption fails
	 * and returns original string. After encryption is completed, returns
	 * Base64Encoded String as encrypted data.
	 * 
	 * @param stringToEncrypt
	 *            Any type of string. Turkish characters can be user. Encoded
	 *            with UTF-8.
	 * @param privateKeyAsString
	 *            Must be larger than 128 bit.
	 * @return Base64 encoded String as encrypted data.
	 */
	public static String encryptStringAES(String stringToEncrypt, String privateKeyAsString) {
		if (isEmpty(stringToEncrypt) || isEmpty(privateKeyAsString))
			return null;
		try {
			String predefinedKey = privateKeyAsString;
			SecretKey SecKey = new SecretKeySpec(predefinedKey.getBytes(), 0, 16, "AES");
			Cipher AesCipher = Cipher.getInstance("AES");

			byte[] byteText = stringToEncrypt.getBytes();
			AesCipher.init(Cipher.ENCRYPT_MODE, SecKey);
			byte[] byteCipherText = AesCipher.doFinal(byteText);
			String encodedEncryption = new String(Base64.encodeBase64(byteCipherText));

			return encodedEncryption;
		} catch (Exception e) {
			logger.error("Error encrypting String", e);
		}
		return stringToEncrypt;
	}

	/**
	 * Decrypts given encrypted string. Encrypted string is assumed to be
	 * encoded with Base64. Uses AES with 128 bit key, key must be given as a
	 * parameter and must be larger than 128 bit to function. Uses first 128 bit
	 * of the key, even if you give 1024 bit key, first 128 bit is used for
	 * encryption.
	 * 
	 * @param stringToDecrypt
	 *            base64 encrypted string.
	 * @param privateKeyAsString
	 *            minimum 128 bit key
	 * @return decrypted text if it is successful, returns encrypted string if
	 *         it fails.
	 */
	public static String decryptStringAES(String stringToDecrypt, String privateKeyAsString) {
		if (isEmpty(stringToDecrypt) || isEmpty(privateKeyAsString))
			return null;
		try {
			String predefinedKey = privateKeyAsString;
			SecretKey SecKey = new SecretKeySpec(predefinedKey.getBytes(), 0, 16, "AES");
			Cipher AesCipher = Cipher.getInstance("AES");

			byte[] cipherText = Base64.decodeBase64(stringToDecrypt.getBytes());

			AesCipher.init(Cipher.DECRYPT_MODE, SecKey);
			byte[] decryptedByte = AesCipher.doFinal(cipherText);

			return new String(decryptedByte, "UTF-8");
		} catch (Exception e) {
			logger.error("Error encrypting String", e);
		}
		return stringToDecrypt;
	}

}
