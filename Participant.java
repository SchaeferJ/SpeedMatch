package speedmatch;

/**
 *
 * @author Jochen
 */
public class Participant implements java.io.Serializable{
	private String f_name;
	private String l_name;
	private int gender;
	private int id;
	private int[] match; // Integer-Array storing IDs of people liked by participant
	private int counter = 0;

	// Constructor
	public Participant(String fname, String lname, int gnd, int nr, int matches){
		this.f_name = fname;
		this.l_name = lname;
		this.gender = gnd;
		this.id = nr;
		this.match = new int[matches];
		for(int i=0; i<this.match.length; i++) {
			this.match[i] = -1;
		}
	}
	
	// Adds a single like. Throws an exception, if the maximum number of likes
	// is being exceeded.
	
	public boolean addMatch(int i) throws MatchException{
		if(counter<match.length){
			this.match[counter] = i;
			this.counter++;
			return true;
		}
		else{
			throw new MatchException("You exceeded the maximum number of Likes for Participant "+ this.getFullName());
		}
	}
	
	// Adds an entire Array of liked IDs at once. Throws an exception if length
	// of array is greater than the maximum number of likes.
	
	public void addMatch(int[] ia) throws MatchException{
		if(ia.length==match.length) {
			this.match = ia;
		}
		else if(ia.length<this.match.length) {
			for(int j=0; j<ia.length; j++) {
				this.match[j] = ia[j];
			}
		}
		else {
			throw new MatchException("Your Array of Likes is too long for participant "+this.getFullName());
		}
	}
	
	// Get-Methods for private Variables
	public int getID(){
		return this.id;
	}

	public String getFirstName(){
		return this.f_name;
	}

	public String getLastName(){
		return this.l_name;
	}

	public String getFullName(){
		String tmp = this.f_name + " " +this.l_name;
		return tmp;
	}

	public int[] getMatches(){
		return this.match;
	}

	public String toString(){
		return this.id+": "+this.f_name+" "+this.l_name;
	}
	
	public int getGender() {
		return this.gender;
	}


}
