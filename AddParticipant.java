package speedmatch;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddParticipant extends JFrame {

	private Dating event;
	private int dirchange;
	private String dir;

	public AddParticipant(Dating evt, int dc, String dd) {
		event = evt;
		initGUI();
		dirchange = dc;
		dir = dd;
	}

	public void initGUI() {

		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		panel.setOpaque(false);
		this.getContentPane().setBackground(new Color(255, 229, 204));
	    setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Title
		JLabel title = new JLabel("Add new participant");
		title.setForeground(new Color(250, 92, 92));
		title.setFont(new Font("Arial", Font.BOLD, 30));

		// Seperator
		JSeparator sep = new JSeparator();
		sep.setPreferredSize(new Dimension(500, 20));

		// Labels
		JLabel fname = new JLabel("First name");
		fname.setForeground(new Color(250, 92, 92));
		fname.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel lname = new JLabel("Last name");
		lname.setForeground(new Color(250, 92, 92));
		lname.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel gender = new JLabel("Gender");
		gender.setForeground(new Color(250, 92, 92));
		gender.setFont(new Font("Arial", Font.PLAIN, 15));

		// Text Fields
		JTextField fnamebox = new JTextField();
		fnamebox.setColumns(15);
		JTextField lnamebox = new JTextField();
		// JTextField genderbox = new JTextField();

		String[] genders = { "male", "female", "other" };
		JComboBox<String> gnd = new JComboBox<String>(genders);
		gnd.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setBackground(Color.WHITE);
				super.paint(g);
			}
		});

		// Buttons
		JPanel actionPanel = new JPanel();
		actionPanel.setOpaque(false);
		JButton disc = new JButton("Discard");
		actionPanel.add(disc);

		JButton add = new JButton("Add");
		actionPanel.add(add);

		// Action Listener for Discard-Button
		disc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		// Action Listener for Add-Button
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String f = fnamebox.getText();
				String l = lnamebox.getText();
				try {
					if (f.equals("") || l.equals("")) {
						throw new MatchException("Name must not be empty");
					}
					int gindex = gnd.getSelectedIndex();
					event.addParticipant(f, l, gindex);
					if (dirchange == 0) {
						Processing.saveDating(event);
						event.addToFile();
					} else {
						Processing.saveDating(event, dir);
						event.addToFile(dir);
					}
					JOptionPane.showMessageDialog(AddParticipant.this, "Participant ID:" + (event.getCurrentParts()),
							"ID", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} catch (MatchException em) {
					JOptionPane.showMessageDialog(AddParticipant.this, em.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Adding
		/*
		 * GridBagConstraints gbc = new GridBagConstraints(); gbc.fill =
		 * GridBagConstraints.HORIZONTAL; // gbc.gridwidth =
		 * GridBagConstraints.RELATIVE;
		 * 
		 * int i = 1;
		 * 
		 * gbc.gridx = 0; gbc.gridy = 0; // gbc.weighty = 1; gbc.ipady = 20; gbc.anchor
		 * = GridBagConstraints.PAGE_START; panel.add(title, gbc);
		 * 
		 * 
		 * gbc.gridx = 0; gbc.gridy = 1; panel.add(sep, gbc);
		 * 
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(fname, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(fnamebox, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(lname, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(lnamebox, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; panel.add(gender, gbc);
		 * 
		 * gbc.gridx = 2; gbc.gridy = i; panel.add(gnd, gbc);
		 * 
		 * i++;
		 * 
		 * // gbc.gridx = 0; // gbc.gridy = i; // panel.add(id, gbc);
		 * 
		 * // gbc.gridx = 2; // gbc.gridy = i; // panel.add(idbox, gbc);
		 * 
		 * i++;
		 * 
		 * gbc.gridx = 0; gbc.gridy = i; // gbc.weighty = 1; gbc.anchor =
		 * GridBagConstraints.SOUTHEAST; panel.add(actionPanel, gbc);
		 * 
		 * this.pack();
		 * 
		 * this.setVisible(true);
		 */

		panel.add(title, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.PAGE_START,
				GridBagConstraints.CENTER, new Insets(20, 0, 10, 0), 0, 0));

		panel.add(sep, new GridBagConstraints(1, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

		panel.add(fname, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(fnamebox, new GridBagConstraints(0, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

		panel.add(lname, new GridBagConstraints(0, 4, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(lnamebox, new GridBagConstraints(0, 5, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

		panel.add(gender, new GridBagConstraints(0, 6, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

		panel.add(gnd, new GridBagConstraints(0, 7, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

		panel.add(actionPanel, new GridBagConstraints(0, 8, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(50, 0, 100, 0), 0, 0));

		this.pack();
		this.setVisible(true);

	}

	public void initializeAddPart() {

		// AddParticipant frame = new AddParticipant();
		this.pack();
		this.setVisible(true);
		this.setTitle("SpeedDating - MatchFinder");
		this.setSize(800, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
