package aiac.aesC;


public class AESCMiddleware {
    native void initFromC(int cypherMode, int blockCypherMode); 
    native void updateFromC(byte[] input);
    native void doFinalFromC(byte[] input);
    static {
        System.loadLibrary("aesC"); 
    }
    
	
	public static void main(String[] args){
		System.out.println("!");
		AESCMiddleware testObj = new AESCMiddleware();
		testObj.initFromC(1,2);
	}
    
    public void init(int cypherMode, int blockCypherMode, byte[] key ){
    	
    }
    
	public byte[] update(byte[] input){
		
		return null;
	}
	
	public byte[] doFinal(byte[] input){
		
		return null;
	}

	
}
