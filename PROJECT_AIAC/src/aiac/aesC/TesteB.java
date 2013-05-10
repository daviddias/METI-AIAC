package aiac.aesC;

import java.io.IOException;

import aiac.aesJAVA.FileRW;

public class TesteB {

	public static void main(String[] args) {
		try {
			byte[] plainText = FileRW.readFile("sandboxFolder/emailExample.txt");
			byte[] cypherText = AESCMiddleware.cypher(plainText);
			byte[] plainTextAgain = AESCMiddleware.decypher(cypherText);
			
			FileRW.writeFile("sandboxFolder/emailExampleAfterMagic.txt", plainTextAgain);
			
		} catch (IOException e) { e.printStackTrace(); }



	}
}
