package speedmatch;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddLikes extends JFrame {

	Dating event;

	public AddLikes(Dating evt) {
		event= evt;
		initGUI();
	}

	public void initGUI() {

		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		this.getContentPane().setBackground(new Color(255, 229, 204));

		// Labels
		JLabel title = new JLabel("Add new likes");
		title.setForeground(new Color(250, 92, 92));
		title.setFont(new Font("Arial", Font.BOLD, 30));

		JLabel id = new JLabel("Your ID: ");
		id.setForeground(new Color(250, 92, 92));
		id.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel[] likeArray= new JLabel[event.getMaxMatch()];
		
		//Dynamically generate Labels as specified by maximum number of likes
		for (int i=0; i<event.getMaxMatch(); i++) {
			int nbr= i+1;
			likeArray[i]= new JLabel("Like "+nbr+" (ID): ");
			likeArray[i].setForeground(new Color(250, 92, 92));
			likeArray[i].setFont(new Font("Arial", Font.PLAIN, 15));

		}

		// Text Fields
		JTextField idbox = new JTextField();

		JTextField[] textArray= new JTextField[event.getMaxMatch()];
		
		//Dynamically generate Text fields
		
		for(int i=0; i< event.getMaxMatch();i++) {
			if (i==0) {
				textArray[i] = new JTextField();
			}
			else if (i==1) {
				textArray[i] = new JTextField();
			}
			else if (i==2) {
				textArray[i] = new JTextField();
			}
			else {
				int nbr= i+1;
				textArray[i] = new JTextField();
			}
			textArray[i].setColumns(10);
		}

		// buttons
		JPanel actionPanel = new JPanel();
		JButton disc= new JButton("Discard");
		actionPanel.add(disc);
		JButton addLike= new JButton("Add");
		actionPanel.add(addLike);

		//Action Listener Discard-Button

		disc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		//Action Listener Add-Button
		
		addLike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int key= Integer.parseInt(idbox.getText());
				int[] likearr= new int[textArray.length]; 
				for (int i=0; i< textArray.length; i++ ) {
					likearr[i]= Integer.parseInt(textArray[i].getText());
				}
				try {
					event.addLikes(key, likearr);
				} catch (MatchException e1) {
					JOptionPane.showMessageDialog(AddLikes.this, "ERROR: "+e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
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
		panel.add(id, gbc);

		gbc.gridx = 2;
		gbc.gridy = i;
		panel.add(idbox, gbc);

		i++;

		for(int y=0; y<event.getMaxMatch();y++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			panel.add(likeArray[y], gbc);

			gbc.gridx = 2;
			gbc.gridy = i;
			panel.add(textArray[y], gbc);

			i++;
		}

		gbc.gridx = 0;
		gbc.gridy = i;
		// gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		panel.add(actionPanel, gbc);

		this.pack();

		this.setVisible(true);

	}

	public void initializeLikes() {

		//AddLikes frame = new AddLikes();
		this.pack();
		this.setVisible(true);
		this.setTitle("SpeedDating - MatchFinder");
		this.setSize(800, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
