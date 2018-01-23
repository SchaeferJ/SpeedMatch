package speedmatch;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.*;

public class MainDesign extends JFrame {

	public MainDesign() {
		initGUI();
	}

	private String[] eventarr = new String[0];
	private int lselect = -1; // Selected Item Index
	private int dirchange = 0; // Dummy, indicates if user specified directory
	private String dir = ""; // Dummy for directory

	public void setArr(String dir) {
		try {
			if (dirchange == 0) {
				this.eventarr = Processing.getDatingsDir();
			} else {
				this.eventarr = Processing.getDatingsDir(dir);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void initGUI() {

		// Creating Panel
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		this.getContentPane().add(panel);
		this.getContentPane().setBackground(new Color(255, 229, 204));
	    setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Title and Subtitle
		JLabel title = new JLabel("Welcome to SpeedMatch!");
		title.setForeground(new Color(250, 92, 92));
		title.setFont(new Font("Arial", Font.BOLD, 40));

		JLabel subtitle = new JLabel("To begin, please select an existing event or create a new one");
		subtitle.setForeground(new Color(250, 92, 92));
		subtitle.setFont(new Font("Arial", Font.BOLD, 25));

		// Seperator
		JSeparator sep = new JSeparator();
		sep.setPreferredSize(new Dimension(500, 20));

		// Labels
		JLabel label = new JLabel("Saved Speeddating Events");
		label.setForeground(new Color(250, 92, 92));
		label.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 15));

		// Buttons
		JPanel pagePanel = new JPanel();
		pagePanel.setOpaque(false);
		JButton next = new JButton("Next >");
		pagePanel.add(next);
		next.setEnabled(false);
		JButton exit = new JButton("Exit");
		pagePanel.add(exit);

		// List of Events
		setArr("");
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		for (int i = 0; i < eventarr.length; i++) {
			dlm.addElement(eventarr[i].substring(0, (eventarr[i].length() - 4)));
		}
		JList<String> list = new JList<String>(dlm);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Buttons
		JPanel eventPanel = new JPanel();
		eventPanel.setOpaque(false);
		JButton newEvent = new JButton("Create new event");
		eventPanel.add(newEvent);
		JButton changeDir = new JButton("Change Directory");
		eventPanel.add(changeDir);
		JButton edit = new JButton("Edit");
		eventPanel.add(edit);
		edit.setEnabled(false);
		JButton delete = new JButton("Delete");
		eventPanel.add(delete);
		delete.setEnabled(false);
		JButton servermode = new JButton("Change to Server Mode");
		eventPanel.add(servermode);

		// Action Listener for List
		ListSelectionModel lsm = list.getSelectionModel();
		lsm.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					lselect = list.getSelectedIndex();
					next.setEnabled(true);
					delete.setEnabled(true);
					edit.setEnabled(true);
				}
			}
		});

		// Action Listener for Change Directory
		changeDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// if (e.getSource() == changeDir) {
				int returnVal = fc.showOpenDialog(MainDesign.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File dirselect = fc.getSelectedFile();
					dir = dirselect.getAbsolutePath();
					char sep = '/';
					if (Processing.detectOS().equals("Windows")) {
						sep = '\\';
					}
					dir += sep;
					dirchange = 1;
					setArr(dir);
					dlm.removeAllElements();
					for (int i = 0; i < eventarr.length; i++) {
						dlm.addElement(eventarr[i].substring(0, (eventarr[i].length() - 4)));
					}
				}
			}
		});

		// Action Listener for new Event
		newEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewEvent NewEventWindow = new NewEvent(dirchange, dir, eventarr);
				NewEventWindow.setVisible(true);
				dispose();
			}
		});

		// Action Listener for Exit
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// Action Listener for Next
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dating d;
				try {
					if (dirchange == 0) {
						d = (Dating) Processing.readDatingFromWD(eventarr[lselect]);
					} else {
						d = (Dating) Processing.readDating(dir + eventarr[lselect]);
					}
					ParticipantPage pp = new ParticipantPage(d, dirchange, dir);
					pp.setVisible(true);
					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(MainDesign.this, ex.getMessage(), "ERROR while opening file",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// ActionListener for Delete
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int askdelete = JOptionPane.showConfirmDialog(MainDesign.this,
						"Are you sure you want to permanently delete " + eventarr[lselect] + "? This cannot be undone.",
						"Delete Speeddating", JOptionPane.INFORMATION_MESSAGE);
				if (askdelete == JOptionPane.YES_OPTION) {
					if (dirchange == 0) {
						Processing.deleteFile(eventarr[lselect]);
					} else {
						Processing.deleteFile(dir, eventarr[lselect]);

					}
					setArr(dir);
					dlm.removeAllElements();
					for (int i = 0; i < eventarr.length; i++) {
						dlm.addElement(eventarr[i].substring(0, (eventarr[i].length() - 4)));
					}
				}
			}
		});

		// ActionListener for Edit
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditWindow ew = new EditWindow(eventarr[lselect], dir, dirchange);
				ew.setVisible(true);
			}
		});

		// ActionListener for Servermode
		servermode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerConfig sc = new ServerConfig();
				sc.setVisible(true);
				dispose();
			}
		});

		// GridBagConstraints
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
		 * gbc.gridx = 0; gbc.gridy = 3; panel.add(label, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 4; panel.add(new JScrollPane(list), gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(10,0,0,0);
		 * panel.add(eventPanel, gbc);
		 * 
		 * gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 5; panel.add(pagePanel, gbc);
		 * 
		 * this.pack();
		 * 
		 * this.setVisible(true);
		 */

		panel.add(title, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.PAGE_START,
				GridBagConstraints.VERTICAL, new Insets(20, 0, 20, 0), 0, 0));

		panel.add(subtitle, new GridBagConstraints(1, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(0, 0, 10, 0), 0, 0));

		panel.add(sep, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(0, 0, 30, 0), 0, 0));

		panel.add(label, new GridBagConstraints(0, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(30, 0, 10, 0), 0, 0));

		JScrollPane sp = new JScrollPane(list);
		sp.setMinimumSize(new Dimension(300, 200));
		panel.add(sp, new GridBagConstraints(0, 4, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(10, 0, 30, 0), 0, 0));

		panel.add(eventPanel, new GridBagConstraints(0, 5, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.VERTICAL, new Insets(30, 0, 30, 0), 0, 0));

		panel.add(pagePanel, new GridBagConstraints(0, 6, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(50, 0, 20, 0), 0, 0));

		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		File f = new File(Processing.getHomeDir());
		if (!f.exists()) {
			f.mkdir();
		}

		MainDesign frame = new MainDesign();

		frame.pack();
		frame.setVisible(true);
		frame.setTitle("SpeedDating - MatchFinder");
		// frame.setSize(800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
