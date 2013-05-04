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
  		ByteArrayOutputStream baos = new ByteArrayOutputStream();
  		ObjectOutputStream oos = new ObjectOutputStream(baos);
  		oos.writeObject(d1);
  		byte[] buf = baos.toByteArray();
		return buf;
	} 
	
	public boolean checkValidity(byte[] buffer) throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer));
		Date d2 = (Date) ois.readObject();
      	ois.close();
		
      	Date dateToday = new Date();
      	long todaySeconds = dateToday.getTime()/1000;
      	
      	// 
      	long date2 = d2.getTime()/1000;
      	
      	long diference = todaySeconds-date2;
      	
      	if(diference<30){
      		return true;
      	}else{
      		return false;
      	}
	}
}
