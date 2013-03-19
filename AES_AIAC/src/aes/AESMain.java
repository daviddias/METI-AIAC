package aes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class AESMain{

	// How to run:
	// AES [–e/d] [–ecb/cbc/ctr] [–f Keyfile] [–k Key] [–i inputfile] [–o outputfile]

	/*
	 * Testes a fazer
	 * 
	 * Verificar que a cifra é feita bem com exemplos de código já cifrado da net
	 * 
	 * Medir velocidade para vários tamanhos
	 * 
	 * Comparar velocidade com outras libs de java 
	 */
	
	
	
	public static void main(String[] args){
		
		BlockCypherMode blockCypherMode = null;
		CypherMode cypherMode = null;
		String keyFilePath = null;
		byte[] key = null;
		String inputFilePath = null;
		String outputFilePath = null;

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
				key = convertToByteArray(readFile(keyFilePath));	
			}
			
			// Key
			if(args[i].equals("-k")){key = convertToByteArray(args[i+1]);}

			//Input file
			if(args[i].equals("-i")){inputFilePath = args[i+1];}

			//Output file
			if(args[i].equals("-o")){outputFilePath = args[i+1];}
		}
		
		if (key == null || inputFilePath == null || outputFilePath == null || cypherMode == null){
			System.out.println("Missing key || inputFilePath || outputFilePath || cypherMode");
			System.out.println("key: " + key);
			System.out.println("inputFilePath: " + inputFilePath);
			System.out.println("outputFilePath: " + outputFilePath);
			System.out.println("cypherMode: " + cypherMode);
			
			return;
		}
		if (blockCypherMode == null){
			System.out.println("No Block Cypher Mode Selected, using ECB as default");
			blockCypherMode = BlockCypherMode.ECB;
		}
		
		
		AES aes = new AES();
		aes.init(cypherMode, blockCypherMode, key, convertToByteArray(readFile(inputFilePath)), outputFilePath);
		aes.test();
	}

	private static String readFile(String FilePath) {
		//String line = null;
		String sCurrentLine = null;
		String text = "";
		try {
			FileInputStream fstream = new FileInputStream(FilePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));		
			while ((sCurrentLine = br.readLine()) != null) {
				text = text + sCurrentLine;
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}		
		return text; 
	}


	private static byte[] convertToByteArray(String text){
		byte[] convertme = text.getBytes();
		byte[] result = new byte[convertme.length];
		for(int i=0; i<convertme.length; i++){    
		    result[i] = convertme[i];    
		}      
		return result;
	}


}









