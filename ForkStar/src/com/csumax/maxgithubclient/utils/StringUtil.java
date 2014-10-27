package com.csumax.maxgithubclient.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isEmpty(String input) {

		if (null == input || input.length() == 0 || "".equals(input)) {
			return true;
		}

		for (int index = 0; index < input.length(); index++) {
			char ch = input.charAt(index);
			if (ch != ' ' && ch != '\t' && ch != '\r' && ch != '\n') {
				return false;
			}
		}

		return true;

	}

	public static String inputStream2String(InputStream in) {

		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(isr);

		try {
			String line;
			line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
					reader = null;
				}
				if (null != isr) {
					isr.close();
					isr = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
}
