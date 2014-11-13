package no.uninett.adc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

	private static MessageDigest MESSAGE_DIGEST = null;

	public static String hash(final String in) {
		try {
			return new String(getDigest().digest(in.getBytes("UTF-8")));
		} catch (final Exception e) {
			// that is clear not clean code, but hm, uf there is no MD5 and no
			// UTF-8, then we are in trouble anyway ;-)
			throw new RuntimeException(e);
		}
	}

	private static MessageDigest getDigest() throws NoSuchAlgorithmException {
		if (MESSAGE_DIGEST == null) {
			synchronized (Hasher.class) {
				if (MESSAGE_DIGEST == null) {
					MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
				}
			}
		}
		return MESSAGE_DIGEST;
	}

}
