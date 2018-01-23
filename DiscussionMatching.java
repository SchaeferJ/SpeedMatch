package speedmatch;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class DiscussionMatching {

	//	Matches Participants for Discussion, 
	// depending on specified discussion rounds 
	// by using the matching score 



	//Kreiert eine Runde
	public static int[][] turn(Dating d, int c){
		int parts= d.getCurrentParts();
		int [][] ar= new int[parts/2][2];
		HashMap<Integer, NetParticipant> matchtable = d.getNetParticipants();
		if(parts%2!=0) {
			parts-=1;
		}
		if(c%2!=0) {
			int k=1; //Participants hashindex
			int l=0;

			for (int j=0; j<(parts/2); j++) {
				//New turn
				if (j==0) {
					//ID of the first participant
					ar[j][0]=matchtable.get(k).getID();

					//find never used top match of participant , indicator used -2, help variable l
					for(int i=0; i<parts; i++) {
						if (matchtable.get(k).getDiscuss()[l][0]==-2) {
							l++;
						}
					}
					// ID of matching participant
					ar[j][1]=matchtable.get(k).getDiscuss()[l][0];

					//Hilfsvariablen IDs des ersten Pärchens
					int idPart=matchtable.get(k).getDiscuss()[l][0];
					int idAkt=matchtable.get(k).getID();				

					//set indicators for used match 
					matchtable.get(k).getDiscuss()[l][0]=-2;

					for(int i=0; i<parts; i++) {
						if (matchtable.get(idPart).getDiscuss()[i][0]==idAkt) {
							matchtable.get(idPart).getDiscuss()[i][0] = -2;
						}
					}
					l=0;
					k++;
				}

				//check wether participant k already exists in this round

				else {
					for (int i=0; i<(parts/2); i++) {
						for (int m=0; m<2; m++) {
							if (matchtable.get(k).getID()== ar[i][m]) {
								m=2;
								i=-1;
								k++;
							}
						}
					}
					//ID of next not already existing participant
					ar[j][0]=matchtable.get(k).getID();

					//Checks

					boolean check=false;

					while (!check) {
						//Not used top match of participant , indicator used -2, helpvariable l
						for(int q=0; q<parts; q++) {
							if (matchtable.get(k).getDiscuss()[l][0]==-2) {
								l++;
								if(l>=parts) {
									break;
								}
							}

						}
						if(l>=parts) {
							k++;
							break;
						}
						//helpvar ID of first not used match
						int idMatch= matchtable.get(k).getDiscuss()[l][0];

						//Check ob MatchPartner schon in Runde enthalten
						for (int o=0; o<(parts/2); o++) {

							if (idMatch== ar[o][0]) {
								check=false;
								l++;
								if(l>=parts) {
									break;
								}
								break;
							}

							else if (idMatch== ar[o][1]) {
								check=false;
								l++;
								if(l>=parts) {
									break;
								}
								break;
							}

							else {
								check=true;

							}
						}
						if(l>=parts) {
							k++;
							break;
						}
					}
					if(l<parts) {
						// ID 
						ar[j][1]=matchtable.get(k).getDiscuss()[l][0];

						//helpvar IDs des ersten Pärchens
						int idPart=matchtable.get(k).getDiscuss()[l][0];
						int idAkt=matchtable.get(k).getID();

						//set used indicator
						matchtable.get(k).getDiscuss()[l][0]=-2;

						for(int p=0; p<parts; p++) {
							if (matchtable.get(idPart).getDiscuss()[p][0]==idAkt) {
								matchtable.get(idPart).getDiscuss()[p][0] = -2;
							}
						}
						k++;			
					}
					l=0;

				}
			}
		}
		if(c%2==0) {
			int k=parts;
			int l=0;

			for (int j=0; j<(parts/2); j++) {
				//New turn
				if (j==0) {
					//ID of the first participant
					ar[j][0]=matchtable.get(k).getID();

					//find never used top match of participant , indicator used -2, help variable l
					for(int i=0; i<parts; i++) {
						if (matchtable.get(k).getDiscuss()[l][0]==-2) {
							l++;
						}
					}
					// ID of matching participant
					ar[j][1]=matchtable.get(k).getDiscuss()[l][0];

					//Hilfsvariablen IDs des ersten Pärchens
					int idPart=matchtable.get(k).getDiscuss()[l][0];
					int idAkt=matchtable.get(k).getID();				

					//set indicators for used match 
					matchtable.get(k).getDiscuss()[l][0]=-2;

					for(int i=0; i<parts; i++) {
						if (matchtable.get(idPart).getDiscuss()[i][0]==idAkt) {
							matchtable.get(idPart).getDiscuss()[i][0] = -2;
						}
					}
					l=0;
					k--;
				}

				//check wether participant k already exists in this round

				else {
					for (int i=0; i<(parts/2); i++) {
						for (int m=0; m<2; m++) {
							if (matchtable.get(k).getID()== ar[i][m]) {
								m=2;
								i=-1;
								k--;
							}
						}
					}
					//ID of next not already existing participant
					ar[j][0]=matchtable.get(k).getID();

					//Checks

					boolean check=false;

					while (!check) {
						//Not used top match of participant , indicator used -2, helpvariable l
						for(int q=0; q<parts; q++) {
							if (matchtable.get(k).getDiscuss()[l][0]==-2) {
								l++;
								if(l>=parts) {
									break;
								}
							}

						}
						if(l>=parts) {
							k--;
							break;
						}
						//helpvar ID of first not used match
						int idMatch= matchtable.get(k).getDiscuss()[l][0];

						//Check ob MatchPartner schon in Runde enthalten
						for (int o=0; o<(parts/2); o++) {

							if (idMatch== ar[o][0]) {
								check=false;
								l++;
								if(l>=parts) {
									break;
								}
								break;
							}

							else if (idMatch== ar[o][1]) {
								check=false;
								l++;
								if(l>=parts) {
									break;
								}
								break;
							}

							else {
								check=true;

							}
						}
						if(l>=parts) {
							k--;
							break;
						}
					}
					if(l<parts) {
						// ID 
						ar[j][1]=matchtable.get(k).getDiscuss()[l][0];

						//helpvar IDs des ersten Pärchens
						int idPart=matchtable.get(k).getDiscuss()[l][0];
						int idAkt=matchtable.get(k).getID();

						//set used indicator
						matchtable.get(k).getDiscuss()[l][0]=-2;

						for(int p=0; p<parts; p++) {
							if (matchtable.get(idPart).getDiscuss()[p][0]==idAkt) {
								matchtable.get(idPart).getDiscuss()[p][0] = -2;
							}
						}
						k--;			
					}
					l=0;

				}
			}
		}
		return ar; //Should never be reached
	}


	//Dreidimensionaler Array mit n Runden
	public static int[][][] discussionMatchArray(Dating d) {
		int rounds= d.getDiscRounds();
		int parts= d.getCurrentParts();
		if(parts%2!=0) {
			parts-=1;
		}
		int [][][] matchArray= new int[rounds][parts/2][2];

		for (int i=0; i<rounds; i++) {

			int[][] t= turn(d,i+1);
			for(int j=0; j<parts/2; j++) {
				for(int o=0; o<2; o++) {
					matchArray[i][j][o]= t[j][o];
				}
			}

		}
		return matchArray;
	}




	//Text Output Discussion Matching

	public static String discussionMatchText(Dating d) {
		int[][][] matchAR= discussionMatchArray(d);
		HashMap<Integer, NetParticipant> nametable = d.getNetParticipants();
		int id=0;

		String nl = Processing.getLineSep();
		StringBuffer sb = new StringBuffer();
		sb.append(d.getName()+" Discussion Matching"+nl);
		sb.append("---------------------------------------------------------------"+nl);

		for(int i=0; i<d.getDiscRounds();i++) {
			sb.append("Runde:" +(i+1)+nl+nl);
			for(int j=0; j< d.getCurrentParts()/2; j++) {
				for( int n=0; n<2; n++) {
					id= matchAR[i][j][n];
					if(id!=0) {
						sb.append((j+1)+". ");
						System.out.println(id);
						String st = nametable.get(id).getFullName();
						if(st.length()<30) {
							st = String.format("%-30s", st);
						}
						else {
							st = st.substring(0, 28);
							st += ".";
						}
						sb.append(st+"\t");
					}
				}
				sb.append(nl);
			}
			sb.append(nl+nl);
		}

		return sb.toString();
	}



	// Saves table above in a text file within a given directory

	public static boolean outputDiscussionMatches(String path, Dating sd){
		String filename= path+sd.getName()+ "_discussion_matches.txt";
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
				mfw.write(discussionMatchText(sd));
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
		return outputDiscussionMatches(Processing.getHomeDir(), sd);
	}





}











