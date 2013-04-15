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

	public AuthAgent(String _port){
		port = _port;
	}

	public void run(){
		try { clientSocket = new Socket("localhost",Integer.parseInt("8090"));//Aqui metemos quem queremos contactar 
		} catch (IOException e) { logger.debug("Could not create a Socket with Server on port: 8090 "); }
		logger.debug("Client Started at port " + port);


		try {
			inputStream = clientSocket.getInputStream();
			outputStream = clientSocket.getOutputStream();
			byte[] buffer  = new byte[512]; // Should be enough

			logger.debug("Sending first message: " + Operations.AUTH_REQUEST.toString());
			logger.debug(new String(Operations.AUTH_REQUEST.toString().getBytes()));
			outputStream.write(Operations.AUTH_REQUEST.toString().getBytes());

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
			buffer = new byte[512];
			buffer[0] = Operations.NOUNCE_SIGNED.toString().getBytes()[0];
			System.arraycopy(nounce, 0, buffer, 1, 36);
			System.arraycopy(signature, 0, buffer, 37, 128);
			outputStream.write(buffer);

			buffer = new byte[512];
			inputStream.read(buffer);
			System.out.println(buffer.toString());



		} 
		catch (IOException e) { e.printStackTrace(); } 
		catch (PKCS11Exception e) { e.printStackTrace(); } 
		catch (PteidException e) { e.printStackTrace(); }



	}

}


/*

static PublicKey publicKey;
static String port;

private  ServerSocket serverSocket;



public AuthService(String _publicKeyPath, String _port){
	try { publicKey = Utils.ReadKeyFromFile(_publicKeyPath);
	} catch (NoSuchAlgorithmException e) { e.printStackTrace();
	} catch (InvalidKeySpecException e) { e.printStackTrace(); }
	port = _port;
}

public void run(){
	try { serverSocket = new ServerSocket(Integer.parseInt(port)); 
	} catch (IOException e) { logger.debug("Could not listen on port:" + port); }
	logger.debug("Server Started at port " + port);

	while (true) {
		try {
			clientSocket = serverSocket.accept();    
			inputStream = clientSocket.getInputStream();
			outputStream = clientSocket.getOutputStream();
			byte[] buffer  = new byte[512]; // Should be enough                
			inputStream.read(buffer); //Format 1st byte for Message Type
			String operation = Byte.toString(buffer[0]);


			if(operation.equals(Operations.AUTH_REQUEST)){ 
				buffer = new byte[512];
				System.arraycopy(Utils.Generate128bitNounce(), 0, buffer, 1, 36); //nounce lenght 36 bytes (128/8)
				outputStream.write(buffer);					
			}

			if(operation.equals(Operations.NOUNCE_SIGNED)){ //128 bit Nounce(36 Bytes) + 128 Byte Signature					
				//Send Back Approved or not
				byte[] nounce = new byte[36];
				byte[] signature = new byte[128];
				System.arraycopy(buffer, 1, nounce, 0, 36);
				System.arraycopy(buffer, 37, signature, 0, 128);

				if(CCAPI.validateSig(publicKey,signature,nounce)){
					outputStream.write(Operations.AUTH_APPROVED.toString().getBytes());
				}else{
					outputStream.write(Operations.AUTH_DENIED.toString().getBytes());
				}			
			}
			inputStream.close();
			outputStream.close();
			clientSocket.close();
		} catch (IOException ex) { logger.debug("Problem in message reading"); }
	}
}*/