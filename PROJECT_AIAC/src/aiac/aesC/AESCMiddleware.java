package aiac.aesC;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import aiac.tools.Convert;


public class AESCMiddleware {


	public static void main(String[] args) throws IOException{
		String toWrite = "O TESTE DA BANANA";
		byte[] toWriteInByte = Convert.encodeToBase64(toWrite);
		byte[] cypherText = cypher(toWriteInByte);
		byte[] plainText = decypher(cypherText);
		
		System.out.print(Convert.decodeFromBase64(plainText));

		System.out.println(toWriteInByte.length);

	}
	
	
	static public byte[] cypher(byte[] plainText) throws IOException{
		OutputStream stdin = null;
		//InputStream stderr = null;
		InputStream stdout = null;
		Process processCypher = new ProcessBuilder("sandboxFolder/testaesmachine.exe","-c").start();
		stdin = processCypher.getOutputStream ();
		//stderr = processCypher.getErrorStream ();


		//String toWrite = "abcdefghij";
		//byte[] toWriteInByte = Convert.encodeToBase64(toWrite);
		//System.out.println(toWriteInByte.length);
		stdin.write(plainText);
		stdin.flush();
		stdin.close();


		stdout = processCypher.getInputStream ();
		int bytesAvailableCypher = stdout.available();
		while (bytesAvailableCypher <= 0){
			bytesAvailableCypher = stdout.available();
		}	      
		byte[] readFromBoxCypher  = new byte[bytesAvailableCypher];	      
		stdout.read(readFromBoxCypher, 0, bytesAvailableCypher);
		stdout.close();
		return readFromBoxCypher;

	}



	static public byte[] decypher(byte[] cypherText) throws IOException{
		OutputStream stdin = null;
		//InputStream stderr = null;
		InputStream stdout = null;

		Process processDecypher = new ProcessBuilder("sandboxFolder/testaesmachine.exe","-d").start();
		stdin = processDecypher.getOutputStream ();
		//stderr = processDecypher.getErrorStream ();


		stdin.write(cypherText);
		stdin.flush();
		stdin.close();

		stdout = processDecypher.getInputStream ();
		int bytesAvailableDecypher = stdout.available();
		while (bytesAvailableDecypher <= 0){
			bytesAvailableDecypher = stdout.available();
		}	      
		//System.out.println(bytesAvailableDecypher);
		byte[] readFromBoxDecypher  = new byte[bytesAvailableDecypher];	      
		stdout.read(readFromBoxDecypher, 0, bytesAvailableDecypher);
		stdout.close();

		return readFromBoxDecypher;
		//System.out.print(Convert.decodeFromBase64(readFromBoxDecypher));

	}
}
