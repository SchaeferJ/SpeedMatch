package speedmatch;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ParticipantPage extends JFrame {

	protected Dating event;
	private int dirchange;
	private String dir;

	public ParticipantPage(Dating evt, int dc, String dd) {

		event=evt;
		dirchange = dc;
		dir = dd;
		initGUI();
	}

	public void initGUI() {

		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		this.getContentPane().setBackground(new Color(255, 229, 204));

		//Title and Subtitle
		JLabel title = new JLabel("SpeedMatch - Main Menu");
		title.setForeground(new Color (250, 92,92));
		title.setFont(new Font("Arial", Font.BOLD, 40));

		JLabel subtitle = new JLabel ("Add Paricipants, add likes and perform matching");
		subtitle.setForeground(new Color(250,92,92));
		subtitle.setFont(new Font("Arial", Font.BOLD, 25));

		//Seperator
		JSeparator sep = new JSeparator();
		sep.setPreferredSize(new Dimension(500,20));

		// Buttons to add participants or likes
		JPanel actionPanel = new JPanel();
		JButton addP =new JButton("Add Participant");
		actionPanel.add(addP);
		JButton likes= new JButton("Add Likes");
		actionPanel.add(likes);
		JButton lexport = new JButton("Open Participant List");
		actionPanel.add(lexport);
		JButton match= new JButton("Match!");
		actionPanel.add(match);

		//Action Listener

		//add Participant
		addP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddParticipant newPartPage= new AddParticipant(event, dirchange, dir);
				newPartPage.initializeAddPart();
			}
		});

		//likes
		likes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddLikes newLikePage= new AddLikes(event);
				newLikePage.initializeLikes();
			}
		});

		//match	
		ActionListener lm = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(dirchange == 1) {
					Matching.outputMatches(dir, event);
				}
				else {
					Matching.outputMatches(event);
				}
			}
		};
		match.addActionListener(lm);

		// Export List
		lexport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					File f;
					if(dirchange == 1) {
						f = new File(dir+event.getName()+"_list.txt");
					}
					else {
						f = new File(Processing.getHomeDir()+event.getName()+"_list.txt");
					}
					Desktop.getDesktop().open(f);
				}catch(FileNotFoundException e) {
					JOptionPane.showMessageDialog(ParticipantPage.this, "Error: You have to add Participants first","ERROR",JOptionPane.ERROR_MESSAGE);
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(ParticipantPage.this, "Error: You have to add Participants first","ERROR",JOptionPane.ERROR_MESSAGE);
				}
			}
		});



		// Buttons to change page or exit
		JPanel pagePanel = new JPanel();
		JButton back= new JButton("Back");
		pagePanel.add(back);
		JButton save = new JButton("Save");
		pagePanel.add(save);
		JButton exit =new JButton("Exit");
		pagePanel.add(exit);


		//Action Listener for exit

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setVisible(false);
				dispose();

			}
		});

		// Action Listener for back

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainDesign frame = new MainDesign();
			}
		});

		// Action Listener for Save

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(dirchange==0) {
						Processing.saveDating(event);
					}
					else {
						Processing.saveDating(event, dir);
					}
					JOptionPane.showMessageDialog(ParticipantPage.this, "Saved successfully","Success", JOptionPane.INFORMATION_MESSAGE);
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(ParticipantPage.this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Adding
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.RELATIVE;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipady = 20;
		gbc.anchor = GridBagConstraints.PAGE_START;
		panel.add(title, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(subtitle, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(sep, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.insets = new Insets(10,0,0,0);
		panel.add(actionPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;		
		gbc.gridwidth = 5;
		//gbc.anchor = GridBagConstraints.LAST_LINE_END;
		panel.add(pagePanel, gbc);

		this.pack();

		this.setVisible(true);
	}

	public void initializeUi() {
		this.pack();
		this.setVisible(true);
		this.setTitle("SpeedDating - MatchFinder");
		this.setSize(1000, 900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	//	public static void main(String[] args) {
	//		
	//		ParticipantPage ppage = new ParticipantPage();
	//		ppage.pack();
	//		ppage.setVisible(true);
	//		ppage.setTitle("SpeedDating - MatchFinder");
	//		ppage.setSize(1000, 900);
	//		ppage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	//	}

}