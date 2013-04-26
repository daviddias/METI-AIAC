package aiac;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import aiac.ptcc.*;
import aiac.tools.Convert;
import aiac.tools.ReadWrite;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		boolean send = false;
		boolean receive = false;
		boolean timestamp = false;
		boolean zip = false;
		boolean useAES = false; //if it's send it's cypher, if it's receive it's decypher
		String pathOfFile = "null";
		
		if (args.length > 3){
			System.out.println("Incorrect flags");
			System.out.println("-s/r -p <pathOfFile> [-t] [-z] [-c]");
			return;
		}
		for (int i=0; i < args.length ; i++){
			if(args[i].equals("-s")){send = true;}
			if(args[i].equals("-r")){receive = true;}
			if(args[i].equals("-p")){pathOfFile = args[i+1];}
			if(args[i].equals("-t")){timestamp = true;}
			if(args[i].equals("-z")){zip = true;}
			if(args[i].equals("-c")){useAES = true;}
		}


		
		
		if(send){
			// 1st Cycle - Cause you can't put all the thing into heap
			// To Unicode
			// To Base64
			String text = ReadWrite.readFileToString(pathOfFile);
			//System.out.println(text);
			byte[] textInUnicode = Convert.toUTF8Bytes(text);
			byte[] textInBase64 = Convert.encodeToBase64(textInUnicode);
			
			// 2nd Cycle - Cause you can't put all the thing into heap
			// Hash //the PKCS11 Library already hashes the content , so no need
			
			// Sign	
			
			
			// Generate TimeStamp
			if(timestamp){
				//TODO
			}
			
			// Concat everything
			
			
			// Cypher - Cause you can't put all the thing into heap
			if(useAES){
				//TODO
			}
			
			
			// Write to File - "OUTPUT-SEND-<filepath>"
			
			

		}
		
		
		if(receive){
			
			
			// Write to File - "OUTPUT-RECEIVE-<filepath>"
			
		}
		
	}

}
