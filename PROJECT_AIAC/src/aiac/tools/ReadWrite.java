package aiac.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadWrite {



	public static String readFileToString(String filePath){
		String result = "";
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(filePath));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				result = result + sCurrentLine;
			}
			br.readLine();
			br.close();
		} catch (FileNotFoundException e) { e.printStackTrace(); 
		} catch (IOException e) { e.printStackTrace(); }
		return result;
	}

	public static byte[] ReadFileToByteArray(String filename) throws IOException {
		File file = new File(filename);
		FileInputStream file_input = new FileInputStream (file);
		DataInputStream data_in    = new DataInputStream (file_input);
		byte[] data = new byte[(int)file.length()];
		data_in.read(data);
		data_in.close();
		return data;
	}

}
