package ptcc;

public class Operations {
	public static final String AUTH_REQUEST = "0"; 
	public static final String NOUNCE = "1"; 
	public static final String NOUNCE_SIGNED = "2"; 
	public static final String AUTH_APPROVED = "3"; 
	public static final String AUTH_DENIED = "4"; 
}


//The messages has the first byte to define the type and the rest is the stuff needed