package com.kejunyao.arch.algorithm;

import java.io.UnsupportedEncodingException;

/**
 *   该类提供了Base64加解密功能。 
 */
public class Base64Util {
    /** 未指定字符集时，默认的String转换的编解码字符集 */
    private static final String DEFAULT_CHARSET = "UTF-8";
	
	private Base64Util() {
	}

    /**
     * Encode a byte array with Base64 algorithm
     * @param data Byte array
     * @return Encoded string
     */
	public static String encode(byte[] data) {
		return encode(data, DEFAULT_CHARSET);
	}
	
    /**
     * Encode a byte array with Base64 algorithm
     * @param data Byte array
     * @param charset Charset name for encoded string
     * @return Encoded string
     */
	public static String encode(byte[] data, String charset) {
        return encode(data, charset, Base64.NO_WRAP);
	}
	
	/**
	 * Encode a byte array with Base64 algorithm
	 * @param data Byte array
	 * @param charset Charset name for encoded string
	 * @param flags Encode flags, see {@link Base64#encode(byte[], int)}
	 * @return Encoded string
	 */
	private static String encode(byte[] data, String charset, int flags) {
	    byte[] encoded = Base64.encode(data, flags);
	    String encodedStr = null;
	    if (charset != null) {
	        try {
	            encodedStr = new String(encoded, charset);
	        } catch (UnsupportedEncodingException e) {
	        }
	    }
	    if (encodedStr == null) {
	        encodedStr = new String(encoded); // Using the system's default charset
	    }
	    return encodedStr;
	}
	
	/**
	 * Encode a string with Base64 algorithm
	 * @param data Original string
	 * @return Encoded string
	 */
	public static String encode(String data) {
		if (data == null) {
			return null;
		}
		
		byte[] bytes = null;
		bytes = data.getBytes();
		return encode(bytes);
	}
	
	/**
	 * Encode a string with Base64 algorithm
	 * @param data Original string
	 * @param charset Charset name for encoded string
	 * @return Encoded string
	 */
	public static String encode(String data, String charset) {
	    if (data == null) {
	        return null;
	    }
	    
	    byte[] bytes = null;
	    bytes = data.getBytes();
	    return encode(bytes, charset);
	}

	/**
	 * 解密一个特定字符串
	 * @param str
	 * @return
	 */
	public static byte[] decode(String str) {
		return Base64.decode(str, Base64.NO_WRAP);
	}
	
	/**
	 * Decode a Base64 string to a string
	 * @param str String to be decoded
	 * @return Decoded string
	 */
	public static String decodeToString(String str) {
		return decodeToString(str, DEFAULT_CHARSET);
	}
	
	/**
	 * Decode a Base64 string to a string
	 * @param str String to be decoded
	 * @param charset Charset name
	 * @return Decoded string
	 */
	public static String decodeToString(String str, String charset) {
	    byte[] decoded = decode(str);
	    String decodedStr;
	    try {
            decodedStr = new String(decoded, charset);
        } catch (UnsupportedEncodingException e) {
            decodedStr = new String(decoded);
        }
	    return decodedStr;
	}
    
    /**
     * URL安全的Base64编码
     * @param data
     * @param charset
     * @return
     */
    public static String encodeUrlSafe(String data, String charset) {
        if (data == null) {
            return null;
        }
        
        byte[] bytes = data.getBytes();
        return encode(bytes, charset, Base64.NO_WRAP | Base64.URL_SAFE);
    }
    
    /**
     * URL安全的Base64解码
     * @param str
     * @param charset
     * @return
     */
    public static String decodeToStringUrlSafe(String str, String charset) {
        byte[] decoded = Base64.decode(str, Base64.NO_WRAP | Base64.URL_SAFE);
        String decodedStr;
        try {
            decodedStr = new String(decoded, charset);
        } catch (UnsupportedEncodingException e) {
            decodedStr = new String(decoded);
        }
        return decodedStr;
    }
}