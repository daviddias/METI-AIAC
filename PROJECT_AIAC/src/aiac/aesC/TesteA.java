package aiac.aesC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import aiac.tools.Convert;
import aiac.tools.ReadWrite;

public class TesteA {
	
	public static void main(String[] args) throws IOException{
		String line;
	    OutputStream stdin = null;
	    InputStream stderr = null;
	    InputStream stdout = null;

	      // launch EXE and grab stdin/stdout and stderr
	      //Process process = Runtime.getRuntime().exec("sandboxFolder/caixa.exe");
	      Process processCypher = new ProcessBuilder("sandboxFolder/testaesmachine.exe","-c").start();
	      stdin = processCypher.getOutputStream ();
	      stderr = processCypher.getErrorStream ();
	      
	      
	      
	      String toWrite = "BANANAS";
	      byte[] toWriteInByte = Convert.encodeToBase64FromString(toWrite);
	      //System.out.println(toWriteInByte.length);
	      stdin.write(toWriteInByte);
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
	    
	      
	      //System.out.print(readFromBoxCypher);
	      //System.out.print("\n");
	    
	      Process processDecypher = new ProcessBuilder("sandboxFolder/testaesmachine.exe","-d").start();
	      stdin = processDecypher.getOutputStream ();
	      stderr = processDecypher.getErrorStream ();
	      
	      
	      stdin.write(readFromBoxCypher);
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
	    
	      System.out.print(Convert.decodeFromBase64ToString(readFromBoxDecypher));
	      		
	}

}
