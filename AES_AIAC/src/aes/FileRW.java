package aes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileRW {

	
	public static byte[] readFile(String FilePath) {
		//String line = null;
		String sCurrentLine = null;
		String text = "";
		try {
			FileInputStream fstream = new FileInputStream(FilePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));		
			while ((sCurrentLine = br.readLine()) != null) {
				text = text + sCurrentLine + "\n";
			}
			br.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}		
		return convertToByteArray(text); 
	}

	
	
	public static void writeFile(String FilePath, byte[] output) {
	
		
		try {
			String outText  = new String(output, "UTF-8");
			File outFile = new File(FilePath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			writer.write(outText);
            //writer.newLine();   // Write system dependent end of line.
            writer.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println("writeFileErrorA");
		} catch (IOException e) {
			System.out.println("writeFileErrorB");
		}
	}
	
	
	
	


	public static byte[] convertToByteArray(String text){
		byte[] convertme = text.getBytes();
		byte[] result = new byte[convertme.length];
		for(int i=0; i<convertme.length; i++){    
		    result[i] = convertme[i];    
		}      
		return result;
	}
}
