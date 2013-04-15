package ptcc.client;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

/**
 * Client Main Class
 * @author DavidDias
 *
 */
public class MainClient {


	public static String port = "8080";

	public static void main(String[] args){
		
		PropertyConfigurator.configure("log4j.properties");
		
		if (args.length > 3){
			System.out.println("Incorrect flags");
			System.out.println("[-p <port>]");
			return;
		}
		for (int i=0; i < args.length ; i++){ if(args[i].equals("-p")){port = args[i+1];}}

		AuthAgent aa = new AuthAgent(port);
		aa.run();

		
		


	}
}
