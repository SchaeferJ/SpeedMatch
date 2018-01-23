package speedmatch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Test {

	public static void main(String[] args) {
		try {
			/*//InetAddress ia = InetAddress.getByName("speedmatch.ctdikeavu3ue.eu-central-1.rds.amazonaws.com");
			InetAddress ia = InetAddress.getByName("192.168.2.106");
			Dating d = new Dating(5,5,"DBtest4",0,1,3);
			DBConnect test = new DBConnect(d);
			test.initConnection(ia);
			test.createEventTable();
			int id = test.insertParticipant("Max", "Mustermann",1 , "A", "B", "C", "D", "E");
			System.out.println(id);
			id = test.insertParticipant("Theo", "Tester", 0 , "B", "A", "E", "F", "E");
			System.out.println(id);
			id = test.insertParticipant("Berta", "Beispiel",1 , "D", "E", "B", "C", "A");
			System.out.println(id);
			test.dbToEvent(d);
			String[] earr = test.getEventsFromDB();
			for(int i=0; i<earr.length; i++) {
				System.out.println(earr[i]);
			}
			test.close();		

			 */
			int maxpart = 6;
			int maxlike = 2;
			String name = "test";
			int g1 = 1;
			int g2 = 1;
			int disc = 3;

			Dating d = new Dating(maxpart, maxlike, name, g1, g2, disc);
			d.addParticipant(1, "AAA", "AAA", 1, "B", "A", "C", "D", "E");
			d.addParticipant(2, "BBB", "BBB", 1, "B", "C", "D", "C", "E");
			d.addParticipant(3, "CCC", "CCC", 1, "C", "D", "A", "C", "C");
			d.addParticipant(4, "DDD", "DDD", 1, "E", "B", "C", "D", "A");
			d.addParticipant(5, "EEE", "EEE", 1, "A", "A", "F", "D", "A");
			d.addParticipant(6, "FFF", "FFF", 1, "A", "B", "E", "F", "A");

			d.computeScores();
			System.out.println(DiscussionMatching.discussionMatchText(d));			

			HashMap<Integer, NetParticipant> hm = d.getNetParticipants();
			for(int u=1; u<=6; u++) {
				for (int i=0; i<6; i++) {
					for(int j=0; j<2; j++) {
						System.out.println(hm.get(u).getDiscuss()[i][j]);
					}			
					System.out.println();
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
