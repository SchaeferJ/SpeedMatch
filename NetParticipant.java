package speedmatch;

public class NetParticipant extends Participant {

	//Additional matching attributes
	private int[][] discuss;
	private String fach;
	private String diktator;
	private String ereignis;
	private String feierbuddy;
	private String revolution;
	private int n = 0;
	
	//Constructor
	public NetParticipant(String fname, String lname, int gnd, int nr, int matches, int nbrParticipants,
			String fa, String dik,String ereig, String feier, String rev){
		super(fname,lname,gnd,nr, matches);
		this.fach=fa;
		this.diktator = dik;
		this.ereignis = ereig;
		this.feierbuddy = feier;
		this.revolution = rev;
		this.discuss = new int[nbrParticipants][2];
		for(int i=0; i<nbrParticipants; i++) {
			this.discuss[i][0] = -2;
			this.discuss[i][1] = 0;
			
		}
	}
	
	// Add discussion partners to array in sorted order
	
	public boolean addPartner(int id, int score) {
		int k=0;
		if(n<discuss.length){
			if(n==0) {
				this.discuss[0][0] = id;
				this.discuss[0][1] = score;
				n++;
				return true;
			}
			else {
				for (int i=0; i<=n; i++) {
					if (this.discuss[i][1]< score) {
						k=i;
						break;
					}	
				}
				for (int i=n-1; i>=k; i--) {
					this.discuss[i+1][0]=this.discuss[i][0];
					this.discuss[i+1][1]=this.discuss[i][1];
				}
				
				this.discuss[k][0] = id;
				this.discuss[k][1] = score;
				this.n++;
				return true;
			}
		}
		else return false;
	}
	
	//Get methods for private variables

	public int[][] getDiscuss() {
		return this.discuss;
	}
	
	public String getFach() {
		return this.fach;
	}
	
	public String getDiktator() {
		return this.diktator;
	}
	
	public String getEreignis() {
		return this.ereignis;
	}
	
	public String getFeierbuddy() {
		return this.feierbuddy;
	}
	
	public String getRevolution() {
		return this.revolution;
	}
	
// Main Methode zum testen
	
//	public static void main(String[] args) {
//		NetParticipant p=new NetParticipant("hanna", "arent",  1, 3, 4, 4,
//			"la", "dik","ereig","feier", "rev");
//		p.addPartner(3, 3);
//		p.addPartner(4, 30);
//		p.addPartner(5, 15);
//		p.addPartner(6, 25);
//		
//		for (int i=0; i<4; i++) {
//			for(int j=0; j<2; j++) {
//				System.out.println(p.getDiscuss()[i][j]);
//			}			
//			System.out.println();
//		}
//		
//	}
}
