package aes;

public class AES {

	
	
	
	CypherMode cypherMode;
	BlockCypherMode blockCypherMode;
	
	//128, 192 or 256 bit
	byte[] key;
	
	
	
	/**
	 * This method performs the required initializations, which include s the key expansion.
	 * 
	 * @param cypher
	 * @param blockCypherMode
	 * @param key
	 */
	public void init(CypherMode cypherMode, BlockCypherMode blockCypherMode, byte[] key ) {
		this.cypherMode = cypherMode;
		this.blockCypherMode = blockCypherMode;
		this.key = key;
	}


	// Cyphering Methods
	// 
	// doFinal is used after the last block is cyphered (using update), if update
	//

	/**
	 * This method performs the ciphering of the input data, using the parameters specified in the init method.
	 * Note: If the last bytes of the input data do not form a full block, the bytes are not ciphered at that instance .
	 * 
	 * @param plaintext
	 * @return
	 */
	byte[] update(byte[] plaintext){
		return plaintext;

	}


	/**
	 * This method performs the ciphering of the input data, using the parameters specified in the init method.
	 * Note: If the last bytes of the input data do not form a full block, the bytes are not ciphered at that instance .
	 * 
	 * @param plaintext
	 * @param inputlen
	 * @return
	 */
	byte[] update(byte[] plaintext, int inputlen){
		return plaintext;

	}



	/**
	 * This method performs the ciphering of the input data, using the parameters specified in the init method. 
	 * This method also performs the necessary procedures to conclude the ciphering of the data stream, including 
	 * the addition of necessary padding.
	 * @param plaintext
	 * @return
	 */
	byte[] doFinal(byte[] plaintext){
		return plaintext;

	}

	/**
	 * This method performs the ciphering of the input data, using the parameters specified in the init method. 
	 * This method also performs the necessary procedures to conclude the ciphering of the data stream, including 
	 * the addition of necessary padding.
	 * 
	 * @param plaintext
	 * @param inputlen
	 * @return
	 */
	byte[] doFinal(byte[] plaintext,int inputlen){
		return plaintext;

	}


}




/*

	Notas: 
 * A Chave tem de ser igual ao tamanho do Bloco
 * O AESAPI.java cifra 1 bloco
 * A Cifra por blocos ECB/CBC/CTR é feita por cima do algoritmo AES

 * Padding é feito adicionando ao final o número de blocos que foram adicionados (genero se adicionarmos 8 bytes, metemos 0808080808080808)
 * Tem de haver sempre um byte de Padding, nem que seja um bloco inteiro


 */

