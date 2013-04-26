package aiac.tools;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;


import org.apache.commons.codec.binary.Base64;

import aiac.ptcc.CCAPI;

public class Convert {

	static Logger logger = Logger.getLogger(Convert.class);
	
	//To Unicode & To Base64

	
	
	public static byte[] toUTF8Bytes(String s){
		try { return s.getBytes("UTF8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		logger.debug("could not convert to UTF8 Bytes");
		return null;
	}
	
	public static String toUTF8String(byte[] b){
		try { return new String(b, "UTF8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		logger.debug("could not convert to UTF8 String");
		return null;
	}
	
	public static byte[] encodeToBase64(byte[] b){
		return Base64.encodeBase64(b);
	}
	
	public static byte[] decodeFromBase64(byte[] b){
		return Base64.decodeBase64(b); 
	}
	
}







// Just as guidance =)
//
//import java.io.UnsupportedEncodingException;
//
//public class StringConverter {
//
//  public static void printBytes(byte[] array, String name) {
//    for (int k = 0; k < array.length; k++) {
//      System.out.println(name + "[" + k + "] = " + "0x"
//          + UnicodeFormatter.byteToHex(array[k]));
//    }
//  }
//
//  public static void main(String[] args) {
//
//    System.out.println(System.getProperty("file.encoding"));
//    String original = new String("A" + "\u00ea" + "\u00f1" + "\u00fc" + "C");
//
//    System.out.println("original = " + original);
//    System.out.println();
//
//    try {
//      byte[] utf8Bytes = original.getBytes("UTF8");
//      byte[] defaultBytes = original.getBytes();
//
//      String roundTrip = new String(utf8Bytes, "UTF8");
//      System.out.println("roundTrip = " + roundTrip);
//
//      System.out.println();
//      printBytes(utf8Bytes, "utf8Bytes");
//      System.out.println();
//      printBytes(defaultBytes, "defaultBytes");
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
//
//  } // main
//
//}
//
//class UnicodeFormatter {
//
//  static public String byteToHex(byte b) {
//    // Returns hex String representation of byte b
//    char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//        'a', 'b', 'c', 'd', 'e', 'f' };
//    char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
//    return new String(array);
//  }
//
//  static public String charToHex(char c) {
//    // Returns hex String representation of char c
//    byte hi = (byte) (c >>> 8);
//    byte lo = (byte) (c & 0xff);
//    return byteToHex(hi) + byteToHex(lo);
//  }
//
//} // class