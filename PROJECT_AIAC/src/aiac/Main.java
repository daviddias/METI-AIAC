package aiac;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sun.org.apache.bcel.internal.classfile.Signature;

import pteidlib.PteidException;
import pteidlib.pteid;
import aiac.ptcc.*;
import aiac.tools.Convert;
import aiac.tools.ReadWrite;

public class Main {

	static Logger logger = Logger.getLogger(Main.class);

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
			try {
				// 1. 1st Cycle - Cause you can't put all the thing into heap
				// To Base64
				String text = ReadWrite.readFileToString(pathOfFile);
				byte[] textInBase64 = Convert.encodeToBase64(text);

				
				// 2nd Cycle - Cause you can't put all the thing into heap
				// Hash //the PKCS11 Library already hashes the content , so no need

				// Generate TimeStamp //has to be part of signature as well
				if(timestamp){
					//TODO
				}

				// 2. Sign	

				CCAPI.initialize();
				//byte[] nounce = Utils.Generate128bitNounce();
				long p11_session = CCAPI.PKCS11SessionInit();
				byte[] signature = CCAPI.SignNounce(p11_session,textInBase64);
				//boolean valide = CCAPI.validateSig(CCAPI.getCertificateX509().getPublicKey(),signature,textInBase64);
				//logger.debug("\n Assinatura Validada com sucesso? : " + valide + "\n");

				pteid.Exit(pteid.PTEID_EXIT_LEAVE_CARD);


				// 3. Concat everything [signature + timestamp + base64]
				byte[] result = new byte[signature.length + textInBase64.length];
				System.arraycopy(signature, 0, result, 0, 128);
				System.arraycopy(textInBase64, 0, result, 128, textInBase64.length);
				
				// 4. Cypher - Cause you can't put all the thing into heap
				if(useAES){
					//TODO
				}

				
				// 5. ZIP
				if (zip){
					//TODO
				}
				
				// 6. Write to File - "OUTPUT-SEND"
				File file = new File ("sandboxFolder/OUTPUT-TO-SEND");
				FileOutputStream file_output = new FileOutputStream (file);
				DataOutputStream data_out = new DataOutputStream (file_output);
				data_out.write(result);
				file_output.close();
				
				
				
				//Test to check if still validating 
				/*
				byte[] sign = new byte[128];
				byte[] base64 = new byte[result.length-128];
				System.arraycopy(result, 0, sign, 0, 128);
				System.arraycopy(result, 128, base64, 0, result.length-128);
				
				boolean valide = CCAPI.validateSig(CCAPI.getCertificateX509().getPublicKey(),sign,base64);
				logger.debug("\n Assinatura Validada com sucesso? : " + valide + "\n");
				*/
				

			} catch (Exception e) { e.printStackTrace();}
		}


		if(receive){
			try {
			// 1. Open the file
			File file = new File(pathOfFile);
			FileInputStream file_input;
			file_input = new FileInputStream (file);
			DataInputStream data_in    = new DataInputStream (file_input);
			byte[] data = new byte[(int)file.length()];
			data_in.read(data);
			data_in.close();
			
			// 2. Unzip
			if (zip){
				//TODO
			}
			
			// 3. Decypher
			if(useAES){
				//TODO
			}
			
			// 4. Validate Signature
			byte[] sign = new byte[128];
			byte[] base64 = new byte[data.length-128];
			
			System.arraycopy(data, 0, sign, 0, 128);
			System.arraycopy(data, 128, base64, 0, data.length-128);
			boolean valide = CCAPI.validateSig(CCAPI.getCertificateX509().getPublicKey(),sign,base64);
			logger.debug("\n Assinatura Validada com sucesso? : " + valide + "\n");
		
			
			
			// 5. Save to file "OUTPUT-RECEIVED"

			
			
			
			} catch (FileNotFoundException e) { e.printStackTrace();
			} catch (IOException e) { e.printStackTrace();
			} catch (PteidException e) { e.printStackTrace(); }	
		}

	}

}
