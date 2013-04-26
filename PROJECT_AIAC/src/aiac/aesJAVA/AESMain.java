package aiac.aesJAVA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;


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


		System.out.println("Start");
		try {
			Long startTime = new Date().getTime();

			AES_API aes_api = new AES_API();
			aes_api.init(cypherMode, blockCypherMode, key);


			InputStream is = new FileInputStream(inputFilePath);
			OutputStream os = new FileOutputStream(outputFilePath);

			//byte[] b = new byte[is.available()];
			byte[] input = new byte[1048576]; // Read 1 Mb at a time
			//is.read(b);
			byte[] result;
			byte[] tmp;
			int nBytesRead;
			while(true){
				nBytesRead = is.read(input);
				if (nBytesRead > 0){
					//System.out.println("Number of Bytes Read: " + nBytesRead);
					tmp = new byte[nBytesRead];
					System.arraycopy(input, 0, tmp, 0, nBytesRead);
					result = aes_api.update(tmp);
					
					//System.out.println("Size of result: "+ result.length + " VS nBytesRead:  " + nBytesRead );
					
					os.write(result, 0, result.length);
					
					//os.write(result, result.length); //Escrever;
					result = null;
					input = new byte[1048576];
				}else{
					is.close();
					input = null;
					break;
				}
			}
			result = aes_api.doFinal(null);
			//System.out.println("Size of result: " + result.length );
			os.write(result, 0, result.length); //Escrever;
			result = null;

			Long endTime = new Date().getTime();
			System.out.println("Total time: " + (endTime - startTime));

		}
		catch (Exception e) {e.printStackTrace();}
	}






}









