package speedmatch;
import java.util.HashMap;

import javax.swing.JOptionPane;

import java.awt.Desktop;
import java.io.*;

/**
 *
 * @author Jochen
 * 
 */

public class Dating implements java.io.Serializable{
	private int max_part;
	private int counter=1;
	private int max_match;
	private int gender1;
	private int gender2;
	private String eventname;
	private HashMap<Integer, Participant> ptable;
	private HashMap<Integer, NetParticipant> nptable;
	private int disc_rounds;
	private boolean netevent = false;

	//Constructor Likes Matching
	public Dating(int maxpart, int maxmatch, String name, int g1, int g2) {
		this.eventname = name;
		this.max_part = maxpart;
		this.max_match = maxmatch;
		this.ptable = new HashMap<>(maxpart);
		this.gender1 = g1;
		this.gender2 = g2;
	}

	//Constructor Discussion Matching
	public Dating(int maxpart, int maxmatch, String name, int g1, int g2, int disc) {
		this.eventname = name;
		this.max_part = maxpart;
		this.max_match = maxmatch;
		this.nptable = new HashMap<>(maxpart);
		this.gender1 = g1;
		this.gender2 = g2;
		this.disc_rounds= disc;
		netevent = true;
	}

	//Adds Participant to Dating (if a free place is left)

	public int addParticipant(String firstname, String lastname, int gender) throws MatchException{
		if(counter <= max_part){
			Participant p = new Participant(firstname, lastname, gender, counter, max_match);
			this.ptable.put(this.counter, p);
			this.counter++;
			return this.counter-1;
		}
		throw new MatchException("You exceeded the maximum number of participants");
	}

	//Adds NetParticipant to Dating (if a free place is left)

	public int addParticipant(int id, String firstname, String lastname, int gender,
			String fa, String dik,String ereig, String feier, String rev) throws MatchException{
		if(counter <= max_part){
			NetParticipant p = new NetParticipant(firstname, lastname, gender, id, max_match, max_part, fa, dik, ereig, feier, rev);
			this.nptable.put(this.counter, p);
			this.counter++;
			return this.counter-1;
		}
		throw new MatchException("You exceeded the maximum number of participants");
	}

	// Adds a single like to a participant

	public void addLikes(int key, int like) throws MatchException{
		this.ptable.get(key).addMatch(like);
	}


	//Add Methoden f�r NetParticipants?
	// Same as above, but adds All likes at once

	public void addLikes(int key, int[] likearr) throws MatchException{
		if(netevent) {
			if(nptable.containsKey(key)) {
				this.nptable.get(key).addMatch(likearr);
			}
			else {
				throw new MatchException("Invalid ID "+key);
			}
		}
		else {
			if(ptable.containsKey(key)) {
				this.ptable.get(key).addMatch(likearr);
			}
			else {
				throw new MatchException("Invalid ID: "+key);
			}
		}
	}


	//Creates scores for Participants

	public void computeScores() {
		int sc=50;
		// reset discussion array
		for (int j=1; j<counter; j++)
			for (int i=0; i<this.max_part; i++) {
				this.nptable.get(j).getDiscuss()[i][0]=-2;
				this.nptable.get(j).getDiscuss()[i][1] = 0;
				}
		// i = Participant whose scores are added
		for(int i=1; i<counter; i++) {
			//j = Participant whose matching score to i is computed
			for(int j=1; j<counter; j++) {
				if(i!=j) { //no match with itself
					if(this.nptable.get(i).getFach()!= this.nptable.get(j).getFach()) {
						sc+=5;
					}
					else {sc-=10;}
					if(this.nptable.get(i).getDiktator()== this.nptable.get(j).getDiktator()) {
						sc+=10;
					}
					else {sc-=5;}
					if(this.nptable.get(i).getEreignis()!= this.nptable.get(j).getEreignis()) {
						sc+=5;
					}
					else {sc-=10 ;}
					if(this.nptable.get(i).getFeierbuddy()== this.nptable.get(j).getFeierbuddy()) {
						sc+=10;
					}
					else {sc-=5;}
					if(this.nptable.get(i).getRevolution()== this.nptable.get(j).getRevolution()) {
						sc+=10;
					}
					else {sc-=5;}
					// Adds Discussion Partners with corresponding scores to Participant
					this.nptable.get(i).addPartner(this.nptable.get(j).getID(), sc);
					sc=50;

				}				
			}
		}	
	}
	



	// Adds the most recently added Participant to a list of participants in
	// a specified directory

	public void addToFile(String path){
		String filename = path+this.eventname +"_list.txt";
		File participants= new File(filename);
		String nl = Processing.getLineSep();

		if (!participants.exists()){
			try{
				participants.createNewFile();  

				FileWriter fw= new FileWriter(participants);
				BufferedWriter bw= new BufferedWriter(fw);    
				StringBuilder sb = new StringBuilder();
				sb.append("Event Name: " + this.eventname +nl+nl+"Participant"+nl);
				if(!netevent) {
					sb.append(this.ptable.get(counter-1).toString());
				}
				else {
					sb.append(this.nptable.get(counter-1).toString());
				}
				String p = sb.toString();
				bw.write(p);
				bw.flush();
				bw.close();
			}
			catch (FileNotFoundException ex) {	
				JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage(), "ERROR: File not Found", JOptionPane.ERROR_MESSAGE);
			}
			catch(IOException o){
				JOptionPane.showMessageDialog(null, "Error: "+o.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			}   

		}
		else{          
			FileReader fr;
			try {
				fr = new FileReader(participants);
				BufferedReader br= new BufferedReader(fr); 

				StringBuilder sb= new StringBuilder();
				String s;
				while ((s = br.readLine())!= null){
					sb.append(s+nl);
				}
				FileWriter fw= new FileWriter(participants);
				BufferedWriter bw= new BufferedWriter(fw);    
				String p;
				if(!netevent) {
					p = this.ptable.get(counter-1).toString();
				}
				else {
					p= this.nptable.get(counter-1).toString();
				}
				sb.append(p);

				bw.write(sb.toString());
				bw.close();
				br.close();
				fr.close();
				fw.close();
			}
			catch (FileNotFoundException ex) {
				JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage(), "ERROR: File not Found", JOptionPane.ERROR_MESSAGE);
			}
			catch(IOException o){
				JOptionPane.showMessageDialog(null, "Error: "+o.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			} 
		}
	} 

	// Same as above, but with default directory

	public void addToFile() {
		addToFile(Processing.getHomeDir());
	}



	//Clear hashtables (Caution: Nuclear option)

	public void clearParticipants() {
	this.counter = 1;
		if(netevent) {
			nptable.clear();
			try {
				String td = System.getProperty("java.io.tmpdir")+Processing.getPathSep();
				File f = new File(td+eventname+"_list.txt");
				if(f.exists()) {
					if(f.canWrite()) {
						f.delete();
					}
				}
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			ptable.clear();
		}
	}


	// Get-Functions for private variables

	public HashMap<Integer, Participant> getParticipants(){
		return this.ptable;
	}

	public HashMap<Integer, NetParticipant> getNetParticipants(){
		return this.nptable;
	} 

	public int getCurrentParts(){
		return this.counter-1;
	}

	public int getCounter() {
		return this.counter;
	}

	public int getMaxMatch(){
		return this.max_match;
	}

	public int getMaxPart(){
		return this.max_part;
	}

	public String getName() {
		return this.eventname;
	}

	public int[] getGender() {
		int[] garr = new int[2];
		garr[0] = this.gender1;
		garr[1] = this.gender2;
		return garr;
	}

	public int getDiscRounds() {
		return this.disc_rounds;
	}

	public boolean getNet() {
		return netevent;
	}

	public String toString(){
		String nl = Processing.getLineSep();
		return "Event Name: " + this.eventname +nl+nl+"Participant\t\t\tMatched with"+nl;
	}


}
