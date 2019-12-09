package Client;

public class nameException extends Exception{
	String id;
	
	public nameException(String name) {
		
		id = name;
	}
	 public String toString() {
	      return "nameException[" + id + "]";
	   }
	 
}



