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
		event=evt;
		initGUI();
		dirchange = dc;
		dir = dd;
	}

	public void initGUI() {

		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		this.getContentPane().setBackground(new Color(255, 229, 204));

		// Labels
		JLabel title = new JLabel("Add new participant");
		title.setForeground(new Color(250, 92, 92));
		title.setFont(new Font("Arial", Font.BOLD, 30));

		JLabel fname = new JLabel("First name");
		fname.setForeground(new Color(250, 92, 92));
		fname.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel lname = new JLabel("Last name");
		lname.setForeground(new Color(250, 92, 92));
		lname.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel gender = new JLabel("Gender");
		lname.setForeground(new Color(250, 92, 92));
		lname.setFont(new Font("Arial", Font.PLAIN, 15));

		// Text Fields
		
		JTextField fnamebox = new JTextField();
		fnamebox.setColumns(15);
		JTextField lnamebox = new JTextField();
		//JTextField genderbox = new JTextField();
		
		String[] genders = { "male", "female", "other" };
		JComboBox<String> gnd = new JComboBox<String>(genders);

		// buttons
		JPanel actionPanel = new JPanel();
		JButton disc= new JButton("Discard");
		actionPanel.add(disc);

		JButton add= new JButton("Add");
		actionPanel.add(add);

		//Action Listener for Discard-Button
		disc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		//Action Listener for Add-Button

		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String f= fnamebox.getText();
				String l= lnamebox.getText();
				int gindex = gnd.getSelectedIndex();
				try {
					event.addParticipant(f,l,gindex);
					if(dirchange==0) {
						Processing.saveDating(event);
						event.addToFile();
					}else {
						Processing.saveDating(event, dir);
						event.addToFile(dir);
					}
					JOptionPane.showMessageDialog(AddParticipant.this, "Participant ID:"+(event.getCurrentParts()), "ID", JOptionPane.INFORMATION_MESSAGE);

				} catch (MatchException em) {
					JOptionPane.showMessageDialog(AddParticipant.this, em.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				dispose();
			}
		});


		// Adding
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.gridwidth = GridBagConstraints.RELATIVE;

		int i = 1;

		gbc.gridx = 0;
		gbc.gridy = 0;
		// gbc.weighty = 1;
		gbc.ipady = 20;
		gbc.anchor = GridBagConstraints.PAGE_START;
		panel.add(title, gbc);

		/*
		 * gbc.gridx = 0; gbc.gridy = 1; panel.add(sep, gbc);
		 */

		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(fname, gbc);

		gbc.gridx = 2;
		gbc.gridy = i;
		panel.add(fnamebox, gbc);

		i++;

		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(lname, gbc);

		gbc.gridx = 2;
		gbc.gridy = i;
		panel.add(lnamebox, gbc);

		i++;

		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(gender, gbc);

		gbc.gridx = 2;
		gbc.gridy = i;
		panel.add(gnd, gbc);

		i++;

		//		gbc.gridx = 0;
		//		gbc.gridy = i;
		//		panel.add(id, gbc);

		//		gbc.gridx = 2;
		//		gbc.gridy = i;
		//		panel.add(idbox, gbc);

		i++;

		gbc.gridx = 0;
		gbc.gridy = i;
		// gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		panel.add(actionPanel, gbc);

		this.pack();

		this.setVisible(true);

	}

	public void initializeAddPart() {

		//AddParticipant frame = new AddParticipant();
		this.pack();
		this.setVisible(true);
		this.setTitle("SpeedDating - MatchFinder");
		this.setSize(800, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}


