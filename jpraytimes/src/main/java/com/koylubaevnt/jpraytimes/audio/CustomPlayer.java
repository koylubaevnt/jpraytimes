package com.koylubaevnt.jpraytimes.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

import com.koylubaevnt.jpraytimes.preferences.AppConfig.SoundEnum;

/*
import javax.swing.JOptionPane;
import com.koylubaevnt.jpraytimes.i18n.I18n;
*/
import javazoom.jl.player.advanced.AdvancedPlayer;

public class CustomPlayer {
	
	private AdvancedPlayer player;
	private InputStream FIS;
	private BufferedInputStream BIS;
	private boolean canResume;
	private URL pathURL;
	private int total;
	private int stopped;
	private boolean valid;
	private SoundEnum soundEnum;
	
	public CustomPlayer(){
	    player = null;
	    FIS = null;
	    valid = false;
	    BIS = null;
	    pathURL = null;
	    total = 0;
	    stopped = 0;
	    canResume = false;
	}
	
	public boolean canResume(){
	    return canResume;
	}
	
	public void setPath(URL pathURL){
	    this.pathURL = pathURL;
	}
	
	public URL getPath(){
	    return pathURL;
	}
	
	public AdvancedPlayer getPlayer() {
		return player;
	}
	
	public void setSoundEnum(SoundEnum soundEnum){
	    this.soundEnum = soundEnum;
	}
	
	public SoundEnum getSoundEnum(){
	    return soundEnum;
	}
	
	
	public void pause(){
	    try{
		    stopped = FIS.available();
		    player.close();
		    FIS = null;
		    BIS = null;
		    player = null;
		    if(valid) canResume = true;
	    }catch(Exception e){
	
	    }
	}
	
	public void resume(){
	    if(!canResume) return;
	    play(total-stopped);
	}
	
	public boolean play(int pos){
	    valid = true;
	    canResume = false;
	    try{
		    FIS = pathURL.openStream();
		    total = FIS.available();
		    if(pos > -1) FIS.skip(pos);
		    BIS = new BufferedInputStream(FIS);
		    player = new AdvancedPlayer(BIS);
		    new Thread(
		            new Runnable(){
		                public void run(){
		                    try{
		                        player.play();
		                    }catch(Exception e){
		                        valid = false;
		                    }
		                }
		            }
		    ).start();
	    }catch(Exception e){
	        valid = false;
	    }
	    return valid;
	}
}