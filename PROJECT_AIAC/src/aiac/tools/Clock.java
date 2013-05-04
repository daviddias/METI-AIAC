package aiac.tools;

import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Clock{
	
	public static byte[] getCurrentTime() throws IOException{
		Date d1 = new Date();
		//System.out.println(d1);
  		ByteArrayOutputStream baos = new ByteArrayOutputStream();
  		ObjectOutputStream oos = new ObjectOutputStream(baos);
  		oos.writeObject(d1);
  		byte[] buf = baos.toByteArray();
		return buf;
	} 
	
	public static boolean checkValidity(byte[] buffer) throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer));
		Date d2 = (Date) ois.readObject();
      	ois.close();
		//System.out.println(d2);
      	Date dateToday = new Date();
      	long todaySeconds = dateToday.getTime()/1000;

      	long date2 = d2.getTime()/1000;
      	long difference = todaySeconds-date2;
      	
      	if(difference<30){
      		return true;
      	}else{
      		return false;
      	}
	}
}
