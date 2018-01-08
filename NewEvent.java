package speedmatch;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class NewEvent extends JFrame {

	private int dirchange;
	private String dir;
	private String[] eventarr;
	public NewEvent(int dc, String dd, String[] ea) {
		dirchange = dc;
		dir = dd;
		eventarr = ea;
		initGUI();
	}

	public void initGUI() {
		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		this.getContentPane().setBackground(new Color(255, 229, 204));

		// Labels
		JLabel title = new JLabel("Create new speeddating event");
		title.setForeground(new Color(250, 92, 92));
		title.setFont(new Font("Arial", Font.BOLD, 30));

		JLabel name = new JLabel("Name of the event");
		name.setForeground(new Color(250, 92, 92));
		name.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel maxpart = new JLabel("Max. number of participants");
		maxpart.setForeground(new Color(250, 92, 92));
		maxpart.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel maxlike = new JLabel("Max. number of likes");
		maxlike.setForeground(new Color(250, 92, 92));
		maxlike.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel matches = new JLabel ("matches");
		matches.setForeground(new Color(250, 92, 92));
		matches.setFont(new Font("Arial", Font.PLAIN, 15));

		//Seperator
		JSeparator sep = new JSeparator();
		sep.setPreferredSize(new Dimension(500,20));

		// Text Fields
		JTextField namebox = new JTextField();
		namebox.setColumns(15);
		JTextField maxpartbox = new JTextField();
		JTextField maxlikebox = new JTextField();

		// Combo Boxes
		String[] genders = { "male", "female", "other","all" };
		JComboBox<String> gen1 = new JComboBox<String>(genders);
		JComboBox<String> gen2 = new JComboBox<String>(genders);

		//buttons
		JPanel actionPanel = new JPanel();
		JButton disc= new JButton("Discard");
		actionPanel.add(disc);

		JButton add= new JButton("Add");
		actionPanel.add(add);

		//Action listener

		//Discard
		disc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainDesign frame= new MainDesign();
				frame.setVisible(true);
				dispose();
			}

		});

		// add button pressed to create new event
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int g1 = gen1.getSelectedIndex();
				int g2 = gen2.getSelectedIndex();
				int replace = 1;
				String name= namebox.getText(); //textfelder
				if(Arrays.asList(eventarr).contains(name+".sdo")) {
					int uinput = JOptionPane.showConfirmDialog(NewEvent.this, "The name is already taken. Do you want to replace the file?", "Info", JOptionPane.INFORMATION_MESSAGE);
					if(uinput != JOptionPane.YES_OPTION) {
						replace = 0;
					}
				}
				if(replace==1) {
					try {
						int maxPart= Integer.parseInt( maxpartbox.getText());
						int maxLikes= Integer.parseInt( maxlikebox.getText());
						final Dating event = new Dating( maxPart, maxLikes ,name, g1, g2);//initialisiert neues event
						if(dirchange==1) {
							Processing.saveDating(event, dir);
						}
						else {
							Processing.saveDating(event);
						}
						ParticipantPage NewParticipantWindow = new ParticipantPage(event, dirchange, dir);
						NewParticipantWindow.initializeUi();
						dispose();
					}catch(MatchException e) {
						JOptionPane.showMessageDialog(NewEvent.this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					}catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(NewEvent.this, "Not a Number " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
				// NewParticipantWindow.setVisible(true);


			}
		});


		// Adding
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		//gbc.gridwidth = GridBagConstraints.RELATIVE;

		int i = 1;

		gbc.gridx = 0;
		gbc.gridy = 0;
		//gbc.weighty = 1;
		gbc.ipady = 20;
		gbc.anchor = GridBagConstraints.PAGE_START;
		panel.add(title, gbc);

		/*gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(sep, gbc);*/

		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(name, gbc);

		gbc.gridx = 2;
		gbc.gridy = i;
		panel.add(namebox, gbc);

		i++;

		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(maxlike, gbc);

		gbc.gridx = 2;
		gbc.gridy = i;
		panel.add(maxlikebox, gbc);

		i++;

		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add(maxpart,gbc);

		gbc. gridx = 2;
		gbc.gridy = i;
		panel.add(maxpartbox, gbc);

		i++;

		gbc.gridx = 0;
		gbc.gridy = i;
		panel.add (gen1, gbc);

		gbc.gridx = 1;
		gbc.gridy = i;
		panel.add(matches, gbc);

		gbc.gridx = 2; 
		gbc.gridy = i;
		panel.add (gen2, gbc);

		i++;

		gbc.gridx = 0;
		gbc.gridy = i;
		//gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		panel.add(actionPanel, gbc);


		this.pack();

		this.setVisible(true);

	}
}
