package aiac.aesC;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import aiac.tools.Convert;


public class AESCMiddleware {


	public static void main(String[] args) throws IOException{
		String toWrite = "O TESTE DA BANANA";
		byte[] toWriteInByte = Convert.encodeToBase64FromString(toWrite);
		byte[] cypherText = cypher(toWriteInByte);
		byte[] plainText = decypher(cypherText);

		System.out.print(Convert.decodeFromBase64ToString(plainText));
		System.out.println(toWriteInByte.length);
	}


	static public byte[] cypher(byte[] plainText) throws IOException{
		System.out.println("CYPHER START ");
		OutputStream stdin = null;
		//InputStream stderr = null;
		InputStream stdout = null;
		Process processCypher = new ProcessBuilder("sandboxFolder/testaesmachine.exe","-c").start();
		stdin = processCypher.getOutputStream ();
		//stderr = processCypher.getErrorStream ();


		//String toWrite = "abcdefghij";
		//byte[] toWriteInByte = Convert.encodeToBase64(toWrite);
		//System.out.println(toWriteInByte.length);

		int leftToSend = plainText.length;
		final int block = 256;//Enviamos 256 de cada vez
		byte[] bufferToSend = new byte[block];
		int i = 0;
		for(i = 0; leftToSend >= block; i++){ 
			leftToSend = leftToSend - block;
			System.arraycopy(plainText, i*block, bufferToSend, 0, block);
			stdin.write(bufferToSend);
			stdin.flush();
			bufferToSend = new byte[block];
		}
		System.arraycopy(plainText, i*block, bufferToSend, 0, leftToSend);		
		stdin.write(bufferToSend);
		stdin.flush();
		stdin.close();

	
		stdout = processCypher.getInputStream ();
		int bytesAvailableCypher = stdout.available();
		while (bytesAvailableCypher <= 0){
			bytesAvailableCypher = stdout.available();
		}
		//já há bytes para ler, vamos lá..
		byte[] bufferToReceive = new byte[block];
		byte[] bufferToReturn = new byte[block];

		int nBytesRead = stdout.read(bufferToReceive, 0, block);
		int j = 0;
		while(true){
			byte[] bufferTmp = new byte[j*block + nBytesRead + 1];
			if(j!=0){ //Se for só um bloco é que não havia nada no return
				if(nBytesRead < block){ //Last block
					System.arraycopy(bufferToReturn, 0, bufferTmp, 0, (j-1)*block);
					if(nBytesRead>0){
						System.arraycopy(bufferToReceive, 0, bufferTmp, (j-1)*block, nBytesRead);
					}
					bufferToReturn = bufferTmp; // =)
					break;
				}
				System.arraycopy(bufferToReturn, 0, bufferTmp, 0, j*block);
			}
			System.arraycopy(bufferToReceive, 0, bufferTmp, j*block, nBytesRead);
			bufferToReturn = bufferTmp; // =)
			j++;
			if(nBytesRead < block){ //Final block
				break;
			}
			bufferToReceive = new byte[block];
			nBytesRead = stdout.read(bufferToReceive, 0, block);
		}
		stdout.close();

		System.out.println("CYPHER TEXT SIZE: " + bufferToReturn.length);
		System.out.println("CYPHER END ");	
		return bufferToReturn;
	}


	static public byte[] decypher(byte[] cypherText) throws IOException{
		System.out.println("DECYPHER START ");
		System.out.println("CYPHER TEXT SIZE: " + cypherText.length);

		OutputStream stdin = null;
		//InputStream stderr = null;
		InputStream stdout = null;

		Process processDecypher = new ProcessBuilder("sandboxFolder/testaesmachine.exe","-d").start();
		stdin = processDecypher.getOutputStream ();
		//stderr = processDecypher.getErrorStream ();

		
		int leftToSend = cypherText.length;
		final int block = 256;//Enviamos 256 de cada vez
		byte[] bufferToSend = new byte[block];
		int i = 0;
		for(i = 0; leftToSend >= block; i++){ 
			leftToSend = leftToSend - block;
			System.arraycopy(cypherText, i*block, bufferToSend, 0, block);
			stdin.write(bufferToSend);
			stdin.flush();
			bufferToSend = new byte[block];
		}
		System.arraycopy(cypherText, i*block, bufferToSend, 0, leftToSend);		
		stdin.write(bufferToSend);
		stdin.flush();
		stdin.close();
		
		stdout = processDecypher.getInputStream ();

		int bytesAvailableCypher = stdout.available();
		while (bytesAvailableCypher <= 0){
			bytesAvailableCypher = stdout.available();
		}
		//já há bytes para ler, vamos lá..

		byte[] bufferToReceive = new byte[block];
		byte[] bufferToReturn = new byte[block];
		
		int nBytesRead = stdout.read(bufferToReceive, 0, block);
		int j = 0;
		while(true){
			byte[] bufferTmp = new byte[j*block + nBytesRead + 1];
			if(j!=0){ //Se for só um bloco é que não havia nada no return
				if(nBytesRead < block){
					System.arraycopy(bufferToReturn, 0, bufferTmp, 0, (j-1)*block);
					if(nBytesRead>0){
						System.arraycopy(bufferToReceive, 0, bufferTmp, (j-1)*block, nBytesRead);
					}
					bufferToReturn = bufferTmp; // =)
					break;
				}
				System.arraycopy(bufferToReturn, 0, bufferTmp, 0, j*block);
			}
			System.arraycopy(bufferToReceive, 0, bufferTmp, j*block, nBytesRead);
			bufferToReturn = bufferTmp; // =)
			j++;
			if(nBytesRead < block){ //Final block
				break;
			}
			bufferToReceive = new byte[block];
			nBytesRead = stdout.read(bufferToReceive, 0, block);
		}
		stdout.close();
		
		System.out.println("I've decifered:" + bufferToReturn);
		System.out.println("DECYPHER END ");

		return bufferToReturn;
	}
}
