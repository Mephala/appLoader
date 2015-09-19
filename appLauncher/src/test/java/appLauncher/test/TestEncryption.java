package appLauncher.test;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import appLauncher.util.CommonUtils;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class TestEncryption {

	@Test
	public void test128bitKey() {
		String pk = "ayghckam1948askc";
		String textToEncrypt = UUID.randomUUID().toString();
		String encrypted = CommonUtils.encryptStringAES(textToEncrypt, pk);
		String decrypted = CommonUtils.decryptStringAES(encrypted, pk);
		assertTrue(textToEncrypt.equals(decrypted));
	}

	@Test
	public void testUTF8Encryption() {
		String pk = "ayghckam1948askc";
		String textToEncrypt = UUID.randomUUID().toString() + "üğşiöçö";
		String encrypted = CommonUtils.encryptStringAES(textToEncrypt, pk);
		String decrypted = CommonUtils.decryptStringAES(encrypted, pk);
		assertTrue(textToEncrypt.equals(decrypted));
	}

}
