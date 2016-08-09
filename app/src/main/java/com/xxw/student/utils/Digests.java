package com.xxw.student.utils;

import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


/**
 * 支持SHA-1/MD5消息摘要的工具类.
 * 
 * 返回ByteSource，可进一步被编码为Hex, Base64或UrlSafeBase64
 * 
 */
public class Digests {

	/** SHA-1 **/
	private static final String SHA1 = "SHA-1";
	/** MD5 **/
	private static final String MD5 = "MD5";
	/** SALT **/
	private static final String SALT = "sh_itgoing";

	private static SecureRandom random = new SecureRandom();

	/**
	 * 对输入字符串进行sha1散列.
	 */
	public static byte[] sha1(byte[] input) {
		return digest(input, SHA1, SALT.getBytes(), 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt) {
		return digest(input, SHA1, salt, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return digest(input, SHA1, salt, iterations);
	}

	/**
	 * 对字符串进行散列, 支持md5与sha1算法.
	 */
	private static byte[] digest(byte[] input, String algorithm, byte[] salt,
			int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(input);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 生成随机的Byte[]作为salt.
	 * 
	 * @param numBytes
	 *            byte数组的大小
	 */
	public static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0,
				"numBytes argument must be a positive integer (1 or larger)",
				numBytes);

		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}

	/**
	 * 对文件进行md5散列.
	 */
	public static byte[] md5(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * 对文件进行sha1散列.
	 */
	public static byte[] sha1(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	private static byte[] digest(InputStream input, String algorithm)
			throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = 8 * 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return messageDigest.digest();
		} catch (GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String e(String inputText) {
		return md5(inputText);
	}

	/**
	 * 二次加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5AndSha(String inputText) {
		return sha(md5(inputText));
	}

	/**
	 * md5加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5(String inputText) {
		return encrypt(inputText, "md5");
	}

	/**
	 * sha加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String sha(String inputText) {
		return encrypt(inputText, "sha-1");
	}

	/**
	 * md5或者sha-1加密
	 * 
	 * @param inputText
	 *            要加密的内容
	 * @param algorithmName
	 *            加密算法名称：md5或者sha-1，不区分大小写
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	/**
	 * 返回十六进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
					3));
		}
		return sb.toString();
	}

	// 密钥是16位长度的byte[]进行Base64转换后得到的字符串
	public static String key = "LkMGStGoOpF4xNyvYt55EQ==";

	/**
	 * <li>
	 * 方法名称:encrypt</li> <li>
	 * 加密方法
	 * 
	 * @param xmlStr
	 *            需要加密的消息字符串
	 * @return 加密后的字符串
	 */
	public static String encrypt(String xmlStr) {
		byte[] encrypt = null;

		try {
			// 取需要加密内容的utf-8编码。
			encrypt = xmlStr.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 取MD5Hash码，并组合加密数组
		byte[] md5Hasn = null;
		try {
			md5Hasn = Digests.MD5Hash(encrypt, 0, encrypt.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 组合消息体
		byte[] totalByte = Digests.addMD5(md5Hasn, encrypt);

		// 取密钥和偏转向量
		byte[] key = new byte[8];
		byte[] iv = new byte[8];
		getKeyIV(Digests.key, key, iv);
		SecretKeySpec deskey = new SecretKeySpec(key, "DES");
		IvParameterSpec ivParam = new IvParameterSpec(iv);

		// 使用DES算法使用加密消息体
		byte[] temp = null;
		try {
			temp = Digests.DES_CBC_Encrypt(totalByte, deskey, ivParam);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 使用Base64加密后返回
		return new BASE64Encoder().encode(temp);
	}

	/**
	 * <li>
	 * 方法名称:encrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 解密方法
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param xmlStr
	 *            需要解密的消息字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decrypt(String xmlStr) throws Exception {
		// base64解码
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] encBuf = null;
		try {
			encBuf = decoder.decodeBuffer(xmlStr);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 取密钥和偏转向量
		byte[] key = new byte[8];
		byte[] iv = new byte[8];
		getKeyIV(Digests.key, key, iv);

		SecretKeySpec deskey = new SecretKeySpec(key, "DES");
		IvParameterSpec ivParam = new IvParameterSpec(iv);

		// 使用DES算法解密
		byte[] temp = null;
		try {
			temp = Digests.DES_CBC_Decrypt(encBuf, deskey, ivParam);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 进行解密后的md5Hash校验
		byte[] md5Hash = null;
		try {
			md5Hash = Digests.MD5Hash(temp, 16, temp.length - 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 进行解密校检
		for (int i = 0; i < md5Hash.length; i++) {
			if (md5Hash[i] != temp[i]) {
				// System.out.println(md5Hash[i] + "MD5校验错误。" + temp[i]);
				throw new Exception("MD5校验错误。");
			}
		}

		// 返回解密后的数组，其中前16位MD5Hash码要除去。
		return new String(temp, 16, temp.length - 16, "utf-8");
	}

	/**
	 * <li>
	 * 方法名称:TripleDES_CBC_Encrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 经过封装的三重DES/CBC加密算法，如果包含中文，请注意编码。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param sourceBuf
	 *            需要加密内容的字节数组。
	 * @param deskey
	 *            KEY 由24位字节数组通过SecretKeySpec类转换而成。
	 * @param ivParam
	 *            IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成。
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public static byte[] TripleDES_CBC_Encrypt(byte[] sourceBuf,
			SecretKeySpec deskey, IvParameterSpec ivParam) throws Exception {
		byte[] cipherByte;
		// 使用DES对称加密算法的CBC模式加密
		Cipher encrypt = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");

		encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);

		cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
		// 返回加密后的字节数组
		return cipherByte;
	}

	/**
	 * <li>
	 * 方法名称:TripleDES_CBC_Decrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 经过封装的三重DES / CBC解密算法
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param sourceBuf
	 *            需要解密内容的字节数组
	 * @param deskey
	 *            KEY 由24位字节数组通过SecretKeySpec类转换而成。
	 * @param ivParam
	 *            IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成。
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public static byte[] TripleDES_CBC_Decrypt(byte[] sourceBuf,
			SecretKeySpec deskey, IvParameterSpec ivParam) throws Exception {

		byte[] cipherByte;
		// 获得Cipher实例，使用CBC模式。
		Cipher decrypt = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
		// 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
		decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);

		cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
		// 返回解密后的字节数组
		return cipherByte;
	}

	/**
	 * <li>
	 * 方法名称:DES_CBC_Encrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 经过封装的DES/CBC加密算法，如果包含中文，请注意编码。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param sourceBuf
	 *            需要加密内容的字节数组。
	 * @param deskey
	 *            KEY 由8位字节数组通过SecretKeySpec类转换而成。
	 * @param ivParam
	 *            IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成。
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public static byte[] DES_CBC_Encrypt(byte[] sourceBuf,
			SecretKeySpec deskey, IvParameterSpec ivParam) throws Exception {
		byte[] cipherByte;
		// 使用DES对称加密算法的CBC模式加密
		Cipher encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");

		encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);

		cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
		// 返回加密后的字节数组
		return cipherByte;
	}

	/**
	 * <li>
	 * 方法名称:DES_CBC_Decrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 经过封装的DES/CBC解密算法。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param sourceBuf
	 *            需要解密内容的字节数组
	 * @param deskey
	 *            KEY 由8位字节数组通过SecretKeySpec类转换而成。
	 * @param ivParam
	 *            IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成。
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public static byte[] DES_CBC_Decrypt(byte[] sourceBuf,
			SecretKeySpec deskey, IvParameterSpec ivParam) throws Exception {

		byte[] cipherByte;
		// 获得Cipher实例，使用CBC模式。
		Cipher decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
		decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);

		cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
		// 返回解密后的字节数组
		return cipherByte;
	}

	/**
	 * <li>
	 * 方法名称:MD5Hash</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * MD5，进行了简单的封装，以适用于加，解密字符串的校验。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param buf
	 *            需要MD5加密字节数组。
	 * @param offset
	 *            加密数据起始位置。
	 * @param length
	 *            需要加密的数组长度。
	 * @return
	 * @throws Exception
	 */
	public static byte[] MD5Hash(byte[] buf, int offset, int length)
			throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(buf, offset, length);
		return md.digest();
	}

	/**
	 * <li>
	 * 方法名称:byte2hex</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 字节数组转换为二行制表示
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param inStr
	 *            需要转换字节数组。
	 * @return 字节数组的二进制表示。
	 */
	public static String byte2hex(byte[] inStr) {
		String stmp;
		StringBuffer out = new StringBuffer(inStr.length * 2);

		for (int n = 0; n < inStr.length; n++) {
			// 字节做"与"运算，去除高位置字节 11111111
			stmp = Integer.toHexString(inStr[n] & 0xFF);
			if (stmp.length() == 1) {
				// 如果是0至F的单位字符串，则添加0
				out.append("0" + stmp);
			} else {
				out.append(stmp);
			}
		}
		return out.toString();
	}

	/**
	 * <li>
	 * 方法名称:addMD5</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * MD校验码 组合方法，前16位放MD5Hash码。 把MD5验证码byte[]，加密内容byte[]组合的方法。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param md5Byte
	 *            加密内容的MD5Hash字节数组。
	 * @param bodyByte
	 *            加密内容字节数组
	 * @return 组合后的字节数组，比加密内容长16个字节。
	 */
	public static byte[] addMD5(byte[] md5Byte, byte[] bodyByte) {
		int length = bodyByte.length + md5Byte.length;
		byte[] resutlByte = new byte[length];

		// 前16位放MD5Hash码
		for (int i = 0; i < length; i++) {
			if (i < md5Byte.length) {
				resutlByte[i] = md5Byte[i];
			} else {
				resutlByte[i] = bodyByte[i - md5Byte.length];
			}
		}

		return resutlByte;
	}

	/**
	 * <li>
	 * 方法名称:getKeyIV</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * </li>
	 * 
	 * @param encryptKey
	 * @param key
	 * @param iv
	 */
	public static void getKeyIV(String encryptKey, byte[] key, byte[] iv) {
		// 密钥Base64解密
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = null;
		try {
			buf = decoder.decodeBuffer(encryptKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 前8位为key
		int i;
		for (i = 0; i < key.length; i++) {
			key[i] = buf[i];
		}
		// 后8位为iv向量
		for (i = 0; i < iv.length; i++) {
			iv[i] = buf[i + 8];
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(encrypt("123456"));
		System.out.println(decrypt("2g0IUQRBZDJ/2Qal7tyLcfkKf/Kv7fRR"));
	}

}
