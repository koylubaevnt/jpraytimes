package com.koylubaevnt.jpraytimes;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.koylubaevnt.jpraytimes.i18n.I18n;
import com.koylubaevnt.jpraytimes.tools.AppLock;

public class MainApplication {
	
	public static void main(String[] args) {
		
		String MY_CUSTOM_LOCK_KEY = "JPrayTimesLockKey";
		try {
		    // Try to get LOCK //
		    if (AppLock.setLock(MY_CUSTOM_LOCK_KEY)) {
		    	JFrame frame = new JWidget();
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
		    } else {
		    	JOptionPane.showMessageDialog(null, I18n.resourceBundle.getString("one_instance"), I18n.resourceBundle.getString("error"), JOptionPane.ERROR_MESSAGE);
		    }
			
		} catch (Exception e) {
			AppLock.releaseLock(); // Release lock
		}
	}
}
