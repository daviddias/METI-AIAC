package ptcc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import ptcc.Operations;
import ptcc.utils.CCAPI;
import ptcc.utils.Utils;
import pteidlib.PteidException;
import sun.security.pkcs11.wrapper.PKCS11Exception;

public class AuthAgent {

	static Logger logger = Logger.getLogger(AuthAgent.class);

	String port;
	Socket clientSocket;
	InputStream inputStream;
	OutputStream outputStream;

	public AuthAgent(String _port){ port = _port; }

	public void run(){
		try { clientSocket = new Socket("localhost",Integer.parseInt("8090"));//Aqui metemos quem queremos contactar 
		} catch (IOException e) { logger.debug("Could not create a Socket with Server on port: 8090 "); }
		logger.debug("Client Started at port " + port);

		try {
			// Auth Request
			inputStream = clientSocket.getInputStream();
			outputStream = clientSocket.getOutputStream();
			byte[] buffer  = new byte[512]; // Should be enough

			logger.debug("Sending first message: " + Operations.AUTH_REQUEST.toString());
			logger.debug(new String(Operations.AUTH_REQUEST.toString().getBytes()));
			outputStream.write(Operations.AUTH_REQUEST.toString().getBytes());

			// Nounce Receive
			while(inputStream.available() == 0){
				logger.debug("Still Waiting");
			}
			inputStream.read(buffer);
			logger.debug("got something: " + new String(buffer));


			byte[] nounce = new byte[36];
			System.arraycopy(buffer, 1, nounce, 0, 36);
			CCAPI.initialize();
			long p11_session = CCAPI.PKCS11SessionInit();
			byte[] signature = CCAPI.SignNounce(p11_session,nounce);

			// ---------------------------------------


			// Nounce Signed
			buffer = new byte[512];
			buffer[0] = Operations.NOUNCE_SIGNED.toString().getBytes()[0];
			System.arraycopy(nounce, 0, buffer, 1, 36);
			System.arraycopy(signature, 0, buffer, 37, 128);
			
			try { clientSocket = new Socket("localhost",Integer.parseInt("8090"));//Aqui metemos quem queremos contactar 
			} catch (IOException e) { logger.debug("Could not create a Socket with Server on port: 8090 "); }
			logger.debug("New Socket Open for Nounce Signed " + port);

			// Auth Request 
			inputStream = clientSocket.getInputStream();
			outputStream = clientSocket.getOutputStream();
		
			logger.debug("Sending Nounce message: " + Operations.NOUNCE_SIGNED.toString());
			logger.debug(new String(Operations.NOUNCE_SIGNED.toString().getBytes()));
			outputStream.write(buffer);

			// Confirmation Receive
			buffer = new byte[512];
			inputStream.read(buffer);
			System.out.println(new String(buffer, "UTF-8"));
		} 
		catch (IOException e) { e.printStackTrace(); } 
		catch (PKCS11Exception e) { e.printStackTrace(); } 
		catch (PteidException e) { e.printStackTrace(); }
	}
}