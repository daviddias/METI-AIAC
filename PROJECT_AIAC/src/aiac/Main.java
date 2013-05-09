package aiac;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import pteidlib.PteidException;
import pteidlib.pteid;
import aiac.aesC.AESCMiddleware;
import aiac.aesJAVA.AES_API;
import aiac.aesJAVA.BlockCypherMode;
import aiac.aesJAVA.CypherMode;
import aiac.ptcc.CCAPI;
import aiac.tools.Clock;
import aiac.tools.Convert;
import aiac.tools.ReadWrite;
import aiac.zip.unzip;

public class Main {

	static boolean useAESBox = false;
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

		if (args.length > 6){
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
				byte[] time = null;
				if(timestamp){
					time = Clock.getCurrentTime();
					System.out.println("Length is: " + time.length);
					byte[] buf = new byte[textInBase64.length + time.length];
					System.arraycopy(textInBase64, 0, buf, 0, textInBase64.length);
					System.arraycopy(time, 0, buf, textInBase64.length, 46); //46 bytes for time
					textInBase64 = buf;
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
				byte[] result = null; 
				if (timestamp){
					result = new byte[signature.length + (textInBase64.length-46) + time.length];
					System.arraycopy(signature, 0, result, 0, 128);
					System.arraycopy(time, 0, result, 128, 46);
					System.arraycopy(textInBase64, 0, result, 128 + 46, textInBase64.length-46); //Aqui o textInBase64 já era a concatenação do base64+time mas por ordem inversa a que queremos deixar na mensagem
				}else{
					result = new byte[signature.length + textInBase64.length];
					System.arraycopy(signature, 0, result, 0, 128);
					System.arraycopy(textInBase64, 0, result, 128, textInBase64.length);					
				}


				// 4. ZIP (Save file, Zip, Read file Again)
				if (zip){					
					File file = new File ("sandboxFolder/tempZIPING");
					FileOutputStream file_output = new FileOutputStream (file);
					DataOutputStream data_out = new DataOutputStream (file_output);
					data_out.write(result);
					file_output.close();

					FileInputStream file_input = new FileInputStream("sandboxFolder/tempZIPING");
					ZipOutputStream file_out = new ZipOutputStream(new FileOutputStream("sandboxFolder/temp.zip"));
					file_out.putNextEntry(new ZipEntry("ZIPY")); //this name is the is the name of unziped file
					byte[] buffer = new byte[4096];
					int count;
					while ((count = file_input.read(buffer)) > 0) {
						file_out.write(buffer, 0, count);
					}
					file_out.close();
					file_input.close();

					result = ReadWrite.ReadFileToByteArray("sandboxFolder/temp.zip");			
				}				


				// 5. Cypher - Cause you can't put all the thing into heap
				if(useAES){
					if (!useAESBox){
						AES_API aes_api = new AES_API();
						aes_api.init(CypherMode.ENCRYPT, BlockCypherMode.CBC, "shdyejfilke8shdc".getBytes());
						result = aes_api.doFinal(result);
					}else{
						result = AESCMiddleware.cypher(result);
					}
				}

				// 6. Write to File - "OUTPUT-SEND"
				File file = new File ("sandboxFolder/OUTPUT-TO-SEND");
				FileOutputStream file_output = new FileOutputStream (file);
				DataOutputStream data_out = new DataOutputStream (file_output);
				data_out.write(result);
				file_output.close();

			} catch (Exception e) { e.printStackTrace();}
		}


		if(receive){
			try {
				// 1. Open the file
				File file = new File(pathOfFile);
				FileInputStream file_input;
				file_input = new FileInputStream (file);
				DataInputStream data_in   = new DataInputStream (file_input);
				byte[] data = new byte[(int)file.length()];
				data_in.read(data);
				data_in.close();


				// 2. Decypher
				if(useAES){
					if(!useAESBox){
						AES_API aes_api = new AES_API();
						aes_api.init(CypherMode.DECRYPT, BlockCypherMode.CBC, "shdyejfilke8shdc".getBytes());
						data = aes_api.doFinal(data);
					}
					else{
						data = AESCMiddleware.decypher(data);
					}
				}

				// 3. Unzip (write the file, unzip, read again)
				if (zip){
					File fileFinal = new File ("sandboxFolder/tempUNZIPING");
					FileOutputStream file_output = new FileOutputStream (fileFinal);
					DataOutputStream data_out = new DataOutputStream (file_output);
					data_out.write(data);
					file_output.close();

					String INPUT_ZIP_FILE = "sandboxFolder/tempUNZIPING";
					String OUTPUT_FOLDER = "sandboxFolder/";
					String pathOfFileUnzipped = "sandboxFolder/ZIPY"; //this is the name specified by ZipEntry
					unzip unZip = new unzip();
					unZip.unZipIt(INPUT_ZIP_FILE,OUTPUT_FOLDER);
					data = ReadWrite.ReadFileToByteArray(pathOfFileUnzipped);					
				}	




				// 4. Validate Signature
				byte[] sign = new byte[128];
				byte[] base64 = new byte[data.length-128];
				byte[] time = new byte[46];

				boolean valide = false;
				if(timestamp){
					base64 = new byte[data.length-128-46];
					byte[] base64AndTime = new byte[data.length-128];
					System.arraycopy(data, 0, sign, 0, 128);
					System.arraycopy(data, 128, time, 0, 46);
					System.arraycopy(data, 128 + 46, base64, 0, data.length-128-46);	

					System.arraycopy(base64, 0, base64AndTime, 0, base64.length);
					System.arraycopy(time, 0, base64AndTime, base64.length, 46);
					try {
						if (!Clock.checkValidity(time)){
							logger.info("A mensagem já não é valida, a validade do timestamp expirou");
							return;
						}
					} catch (ClassNotFoundException e) { e.printStackTrace(); }
					valide = CCAPI.validateSig(CCAPI.getCertificateX509().getPublicKey(),sign,base64AndTime);
				}else{
					System.arraycopy(data, 0, sign, 0, 128);
					System.arraycopy(data, 128, base64, 0, data.length-128);	
					valide = CCAPI.validateSig(CCAPI.getCertificateX509().getPublicKey(),sign,base64);
				}	
				logger.debug("\n Assinatura Validada com sucesso? : " + valide + "\n");

				// 5. Save to file "OUTPUT-RECEIVED"
				File fileFinal = new File ("sandboxFolder/OUTPUT-TO-RECEIVED");
				FileOutputStream file_output = new FileOutputStream (fileFinal);
				DataOutputStream data_out = new DataOutputStream (file_output);
				String buf = Convert.decodeFromBase64(base64);// + "\n";
				data_out.write(buf.getBytes());
				file_output.close();

			} catch (FileNotFoundException e) { e.printStackTrace();
			} catch (IOException e) { e.printStackTrace();
			} catch (PteidException e) { e.printStackTrace(); }	
		}

	}

}
