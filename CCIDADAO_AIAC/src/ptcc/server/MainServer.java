package ptcc.server;

import java.io.IOException;

import javax.security.cert.CertificateEncodingException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

import ptcc.utils.CCAPI;
import ptcc.utils.Utils;
import pteidlib.PteidException;

/**
 * Server main Class
 * @author DavidDias
 *
 */
public class MainServer {
	static Logger logger = Logger.getLogger(MainServer.class);

	public static String port = "8090";
	public static String keyPubFilePath; //= "pubKey"; // default path

	public static void main(String[] args){
		PropertyConfigurator.configure("log4j.properties");
		
		if (args.length > 5){
			System.out.println("Incorrect flags");
			System.out.println("[-p <port>] [-kpub filepath]");
			return;
		}
		for (int i=0; i < args.length ; i++){
			if(args[i].equals("-p")){port = args[i+1];}
			if(args[i].equals("-kpub")){keyPubFilePath = args[i+1];}
		}
		if(keyPubFilePath == null){
			//Read Key and save to Disk
			try { Utils.WriteKeytoFile(CCAPI.getCertificateX509().getPublicKey(), "pubKey");
			} catch (IOException e) { e.printStackTrace();
			} catch (PteidException e) { e.printStackTrace(); }
			keyPubFilePath= "pubKey";
		}
		
		AuthService as = new AuthService(keyPubFilePath, port);
		as.run();
	}
}
