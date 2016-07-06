package com.koylubaevnt.jpraytimes.forms;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.koylubaevnt.jpraytimes.i18n.I18n;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -4616202206945399657L;

	public AboutDialog(Image appImage) {
		
		initComponents(appImage);
		
	}
	
	private void initComponents (Image appImage) {
		
		setTitle(I18n.resourceBundle.getString("about"));
		setModal(true);
		setSize(400, 350);
		setResizable(false);
		
		JPanel mainPanel = new JPanel();
		ImageIcon imageIcon = new ImageIcon(appImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		JLabel imageLabel = new JLabel(imageIcon);
		JLabel textLabel = new JLabel("<html>" + I18n.resourceBundle.getString("about_note") + "</html>");
		JLabel copyrightLabel = new JLabel(I18n.resourceBundle.getString("copyright"));
		
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.TOP);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        textLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
					try {
						if (((JLabel)e.getSource()).getCursor().equals(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))) {
							Desktop.getDesktop().browse(new URI("mailto:koylubaevnt@gmail.com?subject=JPrayTimes%20Desktop"));
	        			}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			
			
		});
        textLabel.addMouseMotionListener(new MouseMotionAdapter() {

        	JLabel label;
        	@Override
			public void mouseMoved(MouseEvent e) {
				label = (JLabel) e.getSource();
				if (e.getX() >= 110 && e.getX() <= 255
						&& e.getY() >= 175 && e.getY() <= 190) {
        			if (!label.getCursor().equals(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))) {
        				label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        			}
				} else if (label.getCursor().equals(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))) {
					label.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
        	
		});
		GroupLayout mainLayout =  new GroupLayout(mainPanel);
		mainPanel.setLayout(mainLayout);
		mainLayout.setAutoCreateContainerGaps(true);
		mainLayout.setAutoCreateGaps(true);
		mainLayout.setHorizontalGroup(
				mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	            .addGroup(mainLayout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                    .addComponent(copyrightLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                    .addGroup(mainLayout.createSequentialGroup()
	                        .addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(textLabel, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))
	                .addContainerGap())
	        );
		mainLayout.setVerticalGroup(
				mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	            .addGroup(mainLayout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                    .addComponent(textLabel, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
	                    .addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
	                .addGap(8, 8, 8)
	                .addComponent(copyrightLabel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
	        );
		
		
		add(mainPanel);
		pack();
	}
	
}
