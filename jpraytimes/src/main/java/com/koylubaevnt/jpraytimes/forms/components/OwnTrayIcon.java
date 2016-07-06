package com.koylubaevnt.jpraytimes.forms.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;


/*TODO need some code:
 * 1. closeLabel must be enabled only when mouse entered on frame
 * 2. closeLabel must change color when mouse enter on label
 * 3. need set Color like statusBar if it's posible*/
public class OwnTrayIcon {
	private Image image;
	private final int SLEEP_MILLISECONDS = 5000;
	private final double WIDTH_RATIO = 0.2; 

	public OwnTrayIcon() {
		this(null);
	}

	public OwnTrayIcon(Image image) {
		this.image = image;
	}
	
	public void displayMessage(String caption, String text, MessageType messageType) {
		if (caption == null && text == null) {
            return;
        }
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		final JFrame frame = new JFrame();
		final JPanel panel = new JPanel();
		frame.setAlwaysOnTop(true);
		frame.setUndecorated(true);
		
		GroupLayout frameLayout = new GroupLayout(panel);
		panel.setLayout(frameLayout);
		
		JLabel imageLabel = new JLabel();
		JLabel captionLabel = new JLabel(caption);
		JLabel textLabel = new JLabel();
		JLabel exitLabel = new JLabel("x");
		captionLabel.setFont(captionLabel.getFont().deriveFont(Font.BOLD, captionLabel.getFont().getSize() + 2));
		exitLabel.setFont(captionLabel.getFont().deriveFont(Font.BOLD, captionLabel.getFont().getSize() + 2));
		
		if (messageType.equals(MessageType.NONE)) {
			if (image != null) 
				imageLabel = new JLabel(new ImageIcon(image));
		} else if (messageType.equals(MessageType.ERROR)) {
			imageLabel = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
		} else if (messageType.equals(MessageType.INFO)) {
			imageLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
		} else if (messageType.equals(MessageType.WARNING)) {
			imageLabel = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
		}
			
		textLabel.setVerticalTextPosition(SwingConstants.TOP);
		textLabel.setText("<html>" + text + "</html>");
		/*
		imageLabel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		captionLabel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		textLabel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		exitLabel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		*/
		int widthPanel = (int) Math.ceil(scrSize.getWidth() * WIDTH_RATIO - imageLabel.getPreferredSize().getHeight());
		
		int height = captionLabel.getPreferredSize().getHeight() > exitLabel.getPreferredSize().getHeight() ? 
				(int) Math.ceil(captionLabel.getPreferredSize().getHeight()) : (int) Math.ceil(exitLabel.getPreferredSize().getHeight()); 
		
		FontMetrics fontMetrics = textLabel.getFontMetrics(textLabel.getFont());
		int heightFont = fontMetrics.getHeight();
		int widthText = fontMetrics.stringWidth(textLabel.getText());
		
		int heightTextLabel = (int) Math.ceil((double) widthText / widthPanel * heightFont);
		height += heightTextLabel;
		height = height > (int) Math.ceil(imageLabel.getPreferredSize().getHeight()) ? height : (int) Math.ceil(imageLabel.getPreferredSize().getHeight());
		height = height * 2;
		panel.setSize((int) Math.ceil(scrSize.getWidth() * WIDTH_RATIO), height);
		frame.setSize((int) Math.ceil(scrSize.getWidth() * WIDTH_RATIO), height);
		
		frameLayout.setHorizontalGroup(
            frameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(frameLayout.createSequentialGroup()
                .addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(textLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(captionLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        frameLayout.setVerticalGroup(
            frameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(frameLayout.createSequentialGroup()
                .addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(frameLayout.createSequentialGroup()
                .addGroup(frameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(captionLabel, GroupLayout.Alignment.TRAILING)
                    .addComponent(exitLabel, GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
		frame.add(panel);
		
		exitLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					frame.dispose();
				}
			}

		});
		
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
		frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight() - 20);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		/*hide tooltip after SLEEP_MILLISECONDS */
		new Thread(){
		      @Override
		      public void run() {
		           try {
		                  Thread.sleep(SLEEP_MILLISECONDS); 
		                  frame.dispose();
		           } catch (InterruptedException e) {
		                  e.printStackTrace();
		           }
		      };
		}.start();
	}
	

}
