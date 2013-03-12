package aes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class AESMain{

	public static void main(String[] args){
		//AES [–e/d] [–ecb/cbc/ctr] [–f Keyfile] [–k Key] [–i inputfile] [–o outputfile]
		BlockCypherMode blockCypherMode;
		CypherMode cypherMode;
		String keyFilePath;
		Byte[] key;
		String inputFilePath;
		String outputFilePath;

		for (int i=0; i < args.length ; i++){
			// Encrypt / Decrypt
			if(args[i].equals("-e")){cypherMode = CypherMode.ENCRYPT;}
			if(args[i].equals("-d")){cypherMode = CypherMode.DECRYPT;}

			// Block Cypher mode
			if(args[i].equals("-ecb")){blockCypherMode = BlockCypherMode.ECB;}
			if(args[i].equals("-cbc")){blockCypherMode = BlockCypherMode.CBC;}
			if(args[i].equals("-ctr")){blockCypherMode = BlockCypherMode.CTR;}

			// Keyfile
			if(args[i].equals("-f")){
				keyFilePath = args[i+1];
				key = Byte.valueOf(readKeyFile(keyFilePath));
			}

			// Key
			if(args[i].equals("-k")){key = Byte.valueOf(args[i+1]);}

			//Input file
			if(args[i].equals("-i")){inputFilePath = args[i+1];}

			//Output file
			if(args[i].equals("-o")){outputFilePath = args[i+1];}
		}

		// TEMOS DE CONVERTER O STRING ARRAY PARA BYTE ARRAY!
		// E fazer o resto =)



		AES aes = new AES();
		aes.init(cypherMode, blockCypherMode, key);



	}

	private static String readKeyFile(String keyFilePath) {
		String line = null;
		try {
			FileInputStream fstream = new FileInputStream(keyFilePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			line = br.readLine();
			return line;
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return null; //never gets here
	}





}









