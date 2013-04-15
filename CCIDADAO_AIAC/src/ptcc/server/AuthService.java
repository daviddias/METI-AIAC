package ptcc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.apache.log4j.Logger;

import ptcc.Operations;
import ptcc.utils.CCAPI;
import ptcc.utils.Utils;

public class AuthService {

	static Logger logger = Logger.getLogger(AuthService.class);

	static PublicKey publicKey;
	static String port;

	private  ServerSocket serverSocket;
	private  Socket clientSocket;
	private  InputStream inputStream;
	private  OutputStream outputStream;

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
				String operation = new String(buffer);
				logger.debug("Received Something: " + operation);
				
				
				if(operation.startsWith(Operations.AUTH_REQUEST.toString())){ 
					logger.debug("Received Auth Request");
					buffer = new byte[512];
					buffer[0] = Operations.NOUNCE.toString().getBytes()[0];
					System.arraycopy(Utils.Generate128bitNounce(), 0, buffer, 1, 36); //nounce lenght 36 bytes (128/8)
					outputStream.write(buffer);					
				}
				
				if(operation.startsWith(Operations.NOUNCE_SIGNED)){ //128 bit Nounce(36 Bytes) + 128 Byte Signature					
					logger.debug("Received Nounce Signed");
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
	}
}


