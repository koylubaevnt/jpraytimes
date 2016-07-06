package com.koylubaevnt.jpraytimes.trush;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;

import javax.swing.ImageIcon;

import com.koylubaevnt.jpraytimes.forms.components.OwnTrayIcon;
import com.koylubaevnt.jpraytimes.i18n.I18n;

public class Tooltips {

	
	public static void showTooltip(String text) {
		if (!SystemTray.isSupported()) {
	        OwnTrayIcon tooltip = new OwnTrayIcon(Tooltips.createImage("/com/koylubaevnt/jpraytimes/images/tray_icon.gif", I18n.resourceBundle.getString("tray_icon")));
			tooltip.displayMessage(I18n.resourceBundle.getString("application_name"), text, TrayIcon.MessageType.NONE);
			return;
	    }
		
		final TrayIcon trayIcon =
                new TrayIcon(createImage("/com/koylubaevnt/jpraytimes/images/tray_icon.gif", I18n.resourceBundle.getString("tray_icon")));
		final SystemTray tray = SystemTray.getSystemTray();
		trayIcon.setImageAutoSize(true);
		try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
        
		TrayIcon[] ti = tray.getTrayIcons();
		for (int i = 0; i < ti.length; i++) {
			TrayIcon trayIcon2 = ti[i];
			System.out.println(trayIcon2.toString());
		}
		
		trayIcon.displayMessage(I18n.resourceBundle.getString("application_name"), text, TrayIcon.MessageType.NONE);
		tray.remove(trayIcon);
	}
	
	//Obtain the image URL
    public static Image createImage(String path, String description) {
        URL imageURL = Tooltips.class.getResource(path);
        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
    
    
    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				Tooltips.showTooltip("test message. Very long text. How will this show in system Tray. I need check to create own tooltip.");
				
			}
		});
	}
}
