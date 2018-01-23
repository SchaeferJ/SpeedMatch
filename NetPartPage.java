package speedmatch;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NetPartPage extends JFrame {

	private Dating event;
	private String name;
	private DBConnect data;
	private String dir = System.getProperty("java.io.tmpdir") + Processing.getPathSep();
	private boolean loaded = false;
	private boolean discload = false;

	public NetPartPage(String n, DBConnect con) {
		this.name = n;
		this.data = con;
		this.event = data.readDatingFromDB(n);
		initGUI();
	}

	public void initGUI() {

		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		panel.setOpaque(false);
		this.getContentPane().setBackground(new Color(255, 229, 204));
	    setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Title and Subtitle
		JLabel title = new JLabel("SpeedMatch - Main Menu");
		title.setForeground(new Color(250, 92, 92));
		title.setFont(new Font("Arial", Font.BOLD, 40));

		JLabel subtitle = new JLabel("Add Paricipants, add likes and perform matching");
		subtitle.setForeground(new Color(250, 92, 92));
		subtitle.setFont(new Font("Arial", Font.BOLD, 25));

		// Separator
		JSeparator sep = new JSeparator();
		sep.setPreferredSize(new Dimension(500, 20));

		// Labels
		JLabel info;
		if (!(event == null)) {
			info = new JLabel("Successfully loaded " + event.getName() + ".");
		} else {
			info = new JLabel("ERROR! Please try again!");
		}

		info.setForeground(new Color(0, 0, 0));
		info.setFont(new Font("Arial", Font.BOLD, 12));

		// Buttons to add participants or likes
		JPanel actionPanel = new JPanel();
		actionPanel.setOpaque(false);
		JButton addP = new JButton("Load Participants");
		actionPanel.add(addP);
		JButton lexport = new JButton("Open Participant List");
		actionPanel.add(lexport);
		JButton discmatch = new JButton("Show Discussion Groups");
		actionPanel.add(discmatch);
		JButton likes = new JButton("Add Likes");
		actionPanel.add(likes);
		JButton match = new JButton("Match!");
		actionPanel.add(match);

		// Buttons to change page or exit
		JPanel pagePanel = new JPanel();
		pagePanel.setOpaque(false);
		JButton back = new JButton("Back");
		pagePanel.add(back);
		JButton serversave = new JButton("Save to Server");
		pagePanel.add(serversave);
		JButton save = new JButton("Save to Computer");
		pagePanel.add(save);
		JButton exit = new JButton("Exit");
		pagePanel.add(exit);

		// Action Listener
		// add Participant
		addP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.dbToEvent(event);
				loaded = true;
			}
		});

		// likes
		likes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddLikes newLikePage = new AddLikes(event);
				newLikePage.setVisible(true);
				// newLikePage.initializeLikes();
			}
		});

		// Discussion Groups
		discmatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!loaded || discload) {
					data.dbToEvent(event);
					loaded = true;
					discload = true;
				}
				event.computeScores();
				DiscussionMatching.outputDiscussionMatches(dir, event);
			}
		});

		// match
		ActionListener lm = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Matching.outputMatches(dir, event);
			}
		};
		match.addActionListener(lm);

		// Export List
		lexport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					File f;
					f = new File(dir + event.getName() + "_list.txt");
					Desktop.getDesktop().open(f);
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(NetPartPage.this, "Error: You have to add Participants first",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(NetPartPage.this, "Error: You have to add Participants first",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Action Listener for exit
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int exs = JOptionPane.showConfirmDialog(NetPartPage.this,
						"Do you want to save your changes before exiting? Unsaved changes will be lost!", "Save",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (exs == JOptionPane.YES_OPTION) {
					data.updateFile(event);
					data.close();
					dispose();
				}
				if (exs == JOptionPane.NO_OPTION) {
					data.close();
					dispose();
				}
			}
		});

		// Action Listener for back

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerConfig sc = new ServerConfig(data);
				sc.setVisible(true);
				dispose();
			}
		});

		// Action Listener for Save to Server

		serversave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					data.updateFile(event);
					JOptionPane.showMessageDialog(NetPartPage.this, "Saved successfully", "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(NetPartPage.this, ex.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Action Listener for Save

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(Processing.getHomeDir());
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(NetPartPage.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File dirselect = fc.getSelectedFile();
					dir = dirselect.getAbsolutePath();
					dir += Processing.getPathSep();
				}
				try {
					Processing.saveDating(event, dir);
					JOptionPane.showMessageDialog(NetPartPage.this, "Saved successfully", "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(NetPartPage.this, ex.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Adding
		/*
		 * GridBagConstraints gbc = new GridBagConstraints(); gbc.fill =
		 * GridBagConstraints.HORIZONTAL; gbc.gridwidth = GridBagConstraints.RELATIVE;
		 * 
		 * gbc.gridx = 0; gbc.gridy = 0; gbc.ipady = 20; gbc.anchor =
		 * GridBagConstraints.PAGE_START; panel.add(title, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 1; panel.add(subtitle, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 2; panel.add(sep, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 3; panel.add(info, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 4; gbc.insets = new Insets(10,0,0,0);
		 * panel.add(actionPanel, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 5; //gbc.anchor =
		 * GridBagConstraints.LAST_LINE_END; panel.add(pagePanel, gbc);
		 * 
		 * this.pack();
		 * 
		 * this.setVisible(true);
		 */

		panel.add(title, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.PAGE_START,
				GridBagConstraints.CENTER, new Insets(20, 0, 10, 0), 0, 0));

		panel.add(subtitle, new GridBagConstraints(1, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(sep, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(0, 0, 30, 0), 0, 0));

		panel.add(actionPanel, new GridBagConstraints(0, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(200, 0, 100, 0), 0, 0));

		panel.add(pagePanel, new GridBagConstraints(0, 4, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(100, 0, 50, 0), 0, 0));

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

}