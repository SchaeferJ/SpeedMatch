package speedmatch;

import java.util.HashMap;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;

import java.awt.Desktop;
import java.io.*;

/**
 *
 * @author Jochen
 */
public class Matching {

	// Get matches by iterating over all participants and performing matchCheck()
	// Returns a two-dimensional array of Integers, where rows represent
	// participants and columns represent matches persons by using participant IDs

	public static int[][] match(Dating d) {
		HashMap<Integer, Participant> matchtable = d.getParticipants();
		int maxlike = d.getMaxMatch();
		int npart = d.getCurrentParts();
		int[] gndarr = d.getGender();
		int[][] amor = new int[npart][maxlike];
		for(int i=1; i<=npart;i++){
			Participant p1 = matchtable.get(i);
			amor[(p1.getID()-1)] = matchCheck(p1, matchtable, maxlike, gndarr);
		}
		return amor;
	}

	// Performs the actual matching by iterating over all persons liked by
	// a participant and checking and checking if the likes were given
	// mutually. If so, the respective ID is added to an array of integers
	// which is returned. If there is no respective like back, -1 is added
	// to indicate a non-match.

	public static int[] matchCheck(Participant p1, HashMap<Integer, Participant> h, int maxlike, int[]gndarr) {
		int[] marr = p1.getMatches();
		int[] matches = new int[maxlike];
		int pid = p1.getID();
		int matchcount = 0;
		int p1g = p1.getGender();
		for (int i = 0; i < marr.length; i++) {
			if(marr[i]>0) {
				Participant p2 = h.get(marr[i]);
				int[] backlikes = p2.getMatches();
				int p2g = p2.getGender();
				if(gndarr[0]!=3&&gndarr[1]!=3) {
					if ((IntStream.of(backlikes).anyMatch(x -> x == pid))&&((gndarr[0]==p1g&&gndarr[1]==p2g)||(gndarr[1]==p1g&&gndarr[0]==p2g))) {
						matches[matchcount] = marr[i];
					} 
					else {
						matches[matchcount] = -1;
					}
				}
				else {
					if (IntStream.of(backlikes).anyMatch(x -> x == pid)) {
						matches[matchcount] = marr[i];
					} 
					else {
						matches[matchcount] = -1;
					}
				}
			}
			else {
				matches[matchcount] = -1;
			}
			matchcount++;

		}
		return matches;
	}

	// Produces a nicely formatted table containing all participants 
	// and their matches

	public static String matchToText(int[][] arr, HashMap<Integer, Participant> h) {
		String nl = Processing.getLineSep();
		StringBuffer sb = new StringBuffer();
		sb.append("------------------------------------------------------------"+nl);
		for (int i=0; i<arr.length; i++) {
			Participant pt = h.get(i+1);
			String st = pt.toString();
			if(st.length()<30) {
				st = String.format("%-30s", st);
			}
			else {
				st = st.substring(0, 28);
				st += ".";
			}
			sb.append(st);
			sb.append("\t");
			for (int j=0; j < arr[i].length; j++) {
				if (j==0) {
					if (arr[i][j] >= 0) {
						sb.append(h.get(arr[i][j]).toString());
						sb.append(nl);
					}
					else {
						sb.append("No match :( "+nl);
					}
				} else {
					if (arr[i][j] >= 0) {
						sb.append("\t\t\t\t");
						sb.append(h.get(arr[i][j]).toString());
						sb.append(nl);
					}
				}

			}
			sb.append("------------------------------------------------------------"+nl);
		}
		return sb.toString();
	}

	// Returns same table as above, but also including information on the event

	public static String printDating(Dating sd){
		StringBuffer sb2 = new StringBuffer();
		int[][] ma = match(sd);
		sb2.append(sd.toString());
		HashMap<Integer, Participant> hm = sd.getParticipants();
		sb2.append(matchToText(ma, hm));
		return sb2.toString();
	}

	// Saves table above in a text file within a given directory

	public static boolean outputMatches(String path, Dating sd){
		String filename= path+sd.getName()+ "_matches.txt";
		final File matches= new File(filename);
		int replace = 1;

		if (matches.exists()){
			int uin = JOptionPane.showConfirmDialog(null, "A matching file already exists. Do you want to replace it?", "Info", JOptionPane.INFORMATION_MESSAGE);
			if(uin != JOptionPane.YES_OPTION) {
				replace = 0;
			}
		}
		if(replace==1) {
			try{
				matches.createNewFile();
				FileWriter mfw= new FileWriter(filename);
				mfw.write(printDating(sd));
				mfw.close();
				Desktop.getDesktop().open(matches);
			}
			catch(IOException i){
				JOptionPane.showMessageDialog(null, "Error: "+i.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			}


		}
		else{ 
			//System.out.println("Matches file already exists.");
		}return true;
	}

	// OVERRIDE: Same as above, but using default directory

	public static boolean outputMatches(Dating sd) {
		return outputMatches(Processing.getHomeDir(), sd);
	}

}
