package com.xxw.student.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName: ValidateHelper
 * @Description: 判断对象、字符串、集合是否为空、不为空
 * @author peter Jia
 * @date 2016-5-20 下午8:16:21
 * 
 */
public final class ValidateHelper {

	public static final String EMPTY = "";
	public static final String SEPARATOR_MULTI = ";";
	public static final String SEPARATOR_SINGLE = "#";
	public static final String SQL_REPLACE = "_";

	/**
	 * 判断数组是否为空
	 * 
	 * @author peter Jia
	 * @param array
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private static <T> boolean isEmptyArray(T[] array) {
		if (array == null || array.length == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @author peter Jia
	 * @param string
	 * @return boolean
	 */
	public static boolean isEmptyString(String string) {
		if (string == null || string.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断集合是否为空
	 * 
	 * @author peter Jia
	 * @date Dec 26, 2013
	 * @param collection
	 * @return boolean
	 */
	public static boolean isEmptyCollection(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断map集合是否为空
	 * 
	 * @author ming.chen
	 * @date Dec 26, 2013
	 * @param map
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmptyMap(Map map) {
		if (map == null || map.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检验对象是否为空,String 中只有空格在对象中也算空.
	 * 
	 * @param object
	 * @return 为空返回true,否则false.
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object object) {
		if (null == object) {
			return true;
		} else if (object instanceof String) {
			return "".equals(object.toString().trim());
		} else if (object instanceof Iterable) {
			return !((Iterable) object).iterator().hasNext();
		} else if (object.getClass().isArray()) {
			return Array.getLength(object) == 0;
		} else if (object instanceof Map) {
			return ((Map) object).size() == 0;
		} else if (Number.class.isAssignableFrom(object.getClass())) {
			return false;
		} else if (Date.class.isAssignableFrom(object.getClass())) {
			return false;
		} else {
			return false;
		}
	}

	/**
	 * 清除字符串两边的空格，null不处理
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? str : str.trim();
	}

	/**
	 * 清除字符串中的回车和换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String ignoreEnter(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		return str.replaceAll("\r|\n", "");
	}

	/**
	 * 判断两个字符串是否equals
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return false;
		}
		return str1.equals(str2);
	}

	/**
	 * 清除下划线，把下划线后面字母转换成大写字母
	 * 
	 * @param str
	 * @return
	 */
	public static String underline2Uppercase(String str) {
		if (ValidateHelper.isEmpty(str)) {
			return str;
		}

		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '_' && i < charArray.length - 1) {
				charArray[i + 1] = Character.toUpperCase(charArray[i + 1]);
			}
		}

		return new String(charArray).replaceAll("_", "");
	}

	/**
	 * 将以ASCII字符表示的16进制字符串以每两个字符分割转换为16进制表示的byte数组.<br>
	 * e.g. "e024c854" --> byte[]{0xe0, 0x24, 0xc8, 0x54}
	 * 
	 * @param str
	 *            原16进制字符串
	 * @return 16进制表示的byte数组
	 */
	public static byte[] hexString2Bytes(String str) {
		if (ValidateHelper.isEmpty(str)) {
			return null;
		}

		if (str.length() % 2 != 0) {
			str = "0" + str;
		}

		byte[] result = new byte[str.length() / 2];
		for (int i = 0; i < result.length; i++) {
			// High bit
			byte high = (byte) (Byte.decode("0x" + str.charAt(i * 2))
					.byteValue() << 4);
			// Low bit
			byte low = Byte.decode("0x" + str.charAt(i * 2 + 1)).byteValue();
			result[i] = (byte) (high ^ low);
		}
		return result;
	}

	/**
	 * 把16进制表达的字符串转换为整数
	 * 
	 * @param hexString
	 * @return
	 */
	public static int hexString2Int(String hexString) {
		return Integer.valueOf(hexString, 16).intValue();
	}

	/**
	 * 把一个字节数组转换为16进制表达的字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();

		for (int i = 0; i < bytes.length; i++) {
			hexString
					.append(enoughZero(Integer.toHexString(bytes[i] & 0xff), 2));
		}
		return hexString.toString();
	}

	/**
	 * 在字符串str左边补齐0直到长度等于length
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String enoughZero(String str, int len) {
		while (str.length() < len) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 把字符串按照指定的字符集进行编码
	 * 
	 * @param str
	 * @param charSetName
	 * @return
	 */
	public static String toCharSet(String str, String charSetName) {
		try {
			return new String(str.getBytes(), charSetName);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 替换sql like的字段中的通配符，包括[]%_
	 * 
	 * @param str
	 * @return
	 */
	public static String sqlWildcardFilter(String str) {
		if (ValidateHelper.isEmpty(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '[') {
				sb.append("[[]");
			} else if (c == ']') {
				sb.append("[]]");
			} else if (c == '%') {
				sb.append("[%]");
			} else if (c == '_') {
				sb.append("[_]");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 把字符串按照规则分割，比如str为"id=123&name=test"，rule为"id=#&name=#"，分隔后为["123",
	 * "test"];
	 * 
	 * @param str
	 * @param rule
	 * @return
	 */
	public static String[] split(String str, String rule) {
		if (rule.indexOf(SEPARATOR_SINGLE) == -1
				|| rule.indexOf(SEPARATOR_SINGLE + SEPARATOR_SINGLE) != -1) {
			throw new IllegalArgumentException("Could not parse rule");
		}

		String[] rules = rule.split(SEPARATOR_SINGLE);

		if (str == null || str.length() < rules[0].length()) {
			return new String[0];
		}

		boolean endsWithSeparator = rule.endsWith(SEPARATOR_SINGLE);

		String[] strs = new String[endsWithSeparator ? rules.length
				: rules.length - 1];
		if (rules[0].length() > 0 && !str.startsWith(rules[0])) {
			return new String[0];
		}

		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < strs.length; i++) {
			startIndex = str.indexOf(rules[i], endIndex) + rules[i].length();
			if (i + 1 == strs.length && endsWithSeparator) {
				endIndex = str.length();
			} else {
				endIndex = str.indexOf(rules[i + 1], startIndex);
			}

			if (startIndex == -1 || endIndex == -1) {
				return new String[0];
			}
			strs[i] = str.substring(startIndex, endIndex);
		}

		return strs;
	}

	/**
	 * 清除字符串左侧的指定字符串
	 * 
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String ltrim(String str, String remove) {
		if (str == null || str.length() == 0 || remove == null
				|| remove.length() == 0) {
			return str;
		}
		while (str.startsWith(remove)) {
			str = str.substring(remove.length());
		}
		return str;
	}

	/**
	 * 清除字符串右侧的指定字符串
	 * 
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String rtrim(String str, String remove) {
		if (str == null || str.length() == 0 || remove == null
				|| remove.length() == 0) {
			return str;
		}
		while (str.endsWith(remove) && (str.length() - remove.length()) >= 0) {
			str = str.substring(0, str.length() - remove.length());
		}
		return str;
	}
	
}