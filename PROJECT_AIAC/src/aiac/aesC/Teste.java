package aiac.aesC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import aiac.tools.ReadWrite;

public class Teste {
	
	public static void main(String[] args) throws IOException{
		String line;
	    OutputStream stdin = null;
	    InputStream stderr = null;
	    InputStream stdout = null;

	      // launch EXE and grab stdin/stdout and stderr
	      //Process process = Runtime.getRuntime().exec("sandboxFolder/caixa.exe");
	      Process process = new ProcessBuilder("sandboxFolder/codigoc.exe","-c").start();
	      stdin = process.getOutputStream ();
	      stderr = process.getErrorStream ();
	      

	      // "write" the parms into stdin
	      line = ReadWrite.readFileToString("sandboxFolder/bananas.txt");
	      //System.out.println("line1 "+line);
	      stdin.write(line.getBytes());
	      //System.out.println("Stdin1 "+stdin);
	      stdin.flush();

	     
//	      line = "sandboxFolder/bananas.txt" + "\n";
//	      System.out.println("line2 "+line);
//	      stdin.write(line.getBytes() );
//	      System.out.println("Stdin2"+stdin);
//	      stdin.flush();

//	      line = "sandboxFolder/morangos.txt" + "\n";
//	      System.out.println("line3 "+line);
//	      stdin.write(line.getBytes() );
//	      System.out.println("Stdin3 "+stdin);
//	      stdin.flush();

	      stdin.close();
	      stdout = process.getInputStream ();

	      // clean up if any output in stdout
	      BufferedReader brCleanUp =
	        new BufferedReader (new InputStreamReader (stdout));
	      while ((line = brCleanUp.readLine ()) != null) {
	        System.out.println ("[Stdout] " + line);
	      }
	      brCleanUp.close();

	      // clean up if any output in stderr
	      brCleanUp =
	        new BufferedReader (new InputStreamReader (stderr));
	      while ((line = brCleanUp.readLine ()) != null) {
	        System.out.println ("[Stderr] " + line);
	      }
	      brCleanUp.close();
		
	}

}
