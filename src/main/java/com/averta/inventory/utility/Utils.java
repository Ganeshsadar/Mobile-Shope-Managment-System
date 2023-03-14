package com.averta.inventory.utility;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

public class Utils {

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public static String getAlphaNumString(int lenalfa, int numlen) {
		String randomString = "";
		randomString = genarateRandom(lenalfa, "abcdefghijklmnpqrstvwxyz");
		randomString = randomString + genarateRandom(numlen, "123456789");
		return randomString;
	}

	public static String genarateRandom(int len, String chars) {
		final int PW_LENGTH = len;
		Random rnd = new SecureRandom();
		StringBuilder randomString = new StringBuilder();
		for (int i = 0; i < PW_LENGTH; i++)
			randomString.append(chars.charAt(rnd.nextInt(chars.length())));
		return randomString.toString();
	}

	/**
	 * Generate random number.
	 */
	public static String generateRandomNumber(int len) {
		String chars = "123456789";
		final int PW_LENGTH = len;
		Random rnd = new SecureRandom();
		StringBuilder pass = new StringBuilder();
		for (int i = 0; i < PW_LENGTH; i++)
			pass.append(chars.charAt(rnd.nextInt(chars.length())));
		return pass.toString();
	}

	/**
	 * Generate random string of given length
	 *
	 * @param length
	 * @return
	 */
	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

	public static Date addDays(Date date, Long days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days.intValue());
		return c.getTime();
	}

	public static String getUserNo(String header) throws UnsupportedEncodingException {
		if (null != header) {
			final String encodedUserPassword = header.replaceFirst("Bearer" + " ", "");
			byte[] decodedBytes = Base64.decodeBase64(encodedUserPassword);
			String usernameAndPassword = new String(decodedBytes, "UTF-8");
			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			return tokenizer.nextToken();
		} else {
			return "";
		}
	}

	private static long get64LeastSignificantBitsForVersion1() {
		Random random = new Random();
		long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
		long variant3BitFlag = 0x8000000000000000L;
		return random63BitLong + variant3BitFlag;
	}

	private static long get64MostSignificantBitsForVersion1() {
		LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
		Duration duration = Duration.between(start, LocalDateTime.now());
		long seconds = duration.getSeconds();
		long nanos = duration.getNano();
		long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
		long least12SignificatBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
		long version = 1 << 12;
		return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificatBitOfTime;
	}

	public static String getUniqueUUID() {
		return new UUID(get64MostSignificantBitsForVersion1(), get64LeastSignificantBitsForVersion1()).toString();
	}

}