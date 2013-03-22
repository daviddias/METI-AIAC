package aes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


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
				key = FileRW.readFile(keyFilePath);	
			}
			
			// Key
			if(args[i].equals("-k")){key = FileRW.convertToByteArray(args[i+1]);}

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
		
		/*
		AES_API aes_api = new AES_API();
		aes_api.init(cypherMode, blockCypherMode, key);
		//aes.test();
		byte[] input = FileRW.readFile(inputFilePath);
		//aes_api.update(input);
		byte[] output = aes_api.doFinal(input);
		*&
		
		/**
		 * Testing
		 */
		
		
		AES_API aes_api_encrypt = new AES_API();
		aes_api_encrypt.init(CypherMode.ENCRYPT, BlockCypherMode.ECB, key);
		byte[] input = FileRW.readFile(inputFilePath);
		byte[] CypherText = aes_api_encrypt.doFinal(input);
		
		try {
			System.out.println("Input PlainText was:");
			System.out.println(new String(input, "UTF-8"));
			System.out.println("CypherText was:");
			System.out.println(new String(CypherText, "UTF-8"));
		} catch (UnsupportedEncodingException e) {System.out.println("FUCK");}

		
		System.out.println("Going to decrypt");
		
		AES_API aes_api_decrypt = new AES_API();
		aes_api_decrypt.init(CypherMode.DECRYPT, BlockCypherMode.ECB, key);
		
		byte[] outputPlainText = aes_api_decrypt.doFinal(CypherText);
		
		try {
			System.out.println("CypherText was:");
			System.out.println(new String(CypherText, "UTF-8"));
			System.out.println("Output PlainText was:");
			System.out.println(new String(outputPlainText, "UTF-8"));
		} catch (UnsupportedEncodingException e) {System.out.println("FUCK");}
		
		System.out.println("\n Finito \n");
		
		
		/*
		byte[] input = FileRW.readFile(inputFilePath);
		
		System.out.println("Plaintext is:");
		System.out.println(input);
		try {
			System.out.println(new String(input, "UTF-8"));
		} catch (UnsupportedEncodingException e) {System.out.println("FUCK");}

		System.out.println("Testing ECB Encryption");
		byte[] cypheredText = AESTAKENFROMTHEWEB.encrypt(input,key);
		System.out.println(cypheredText);
		System.out.println("Encryption Complete");

		System.out.println("Testing ECB Decryption");
		byte[] plainText = AESTAKENFROMTHEWEB.decrypt(cypheredText,key);
		System.out.println(plainText);
		try {
			System.out.println(new String(plainText, "UTF-8"));
		} catch (UnsupportedEncodingException e) {System.out.println("FUCK");}
		System.out.println("Decryption Complete");
		
		*/
		
	}

	
	



}









