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

	public Dating(int maxpart, int maxmatch, String name, int g1, int g2) {
		this.eventname = name;
		this.max_part = maxpart;
		this.max_match = maxmatch;
		this.ptable = new HashMap<>(maxpart);
		this.gender1 = g1;
		this.gender2 = g2;
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
	
	// Adds a single like to a participant
	
	public void addLikes(int key, int like) throws MatchException{
		this.ptable.get(key).addMatch(like);
	}
	
	// Same as above, but adds All likes at once
	
	public void addLikes(int key, int[] likearr) throws MatchException{
		this.ptable.get(key).addMatch(likearr);
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
				sb.append("Event Name: " + this.eventname +nl+nl+"Participant");
				sb.append(this.ptable.get(counter-1).toString());
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

				String p= this.ptable.get(counter-1).toString();
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
	
	
	// Get-Functions for private variables
	
	public HashMap<Integer, Participant> getParticipants(){
		return this.ptable;
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

	public String toString(){
		String nl = Processing.getLineSep();
		return "Event Name: " + this.eventname +nl+nl+"Participant\t\t\tMatched with"+nl;
	}

}
