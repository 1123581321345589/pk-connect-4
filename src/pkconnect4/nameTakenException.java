package pkconnect4;

public class nameTakenException extends Exception {
	String id;
	
	public nameTakenException(String name) {
		
		id = name;
	}
	 public String toString() {
	      return "nameTakenException[" + id + "]";
	   }
	 
}
