package com.koylubaevnt.jpraytimes.trush;

import java.io.FileInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class AudioPlayer {
	
	FileInputStream fileInputStream;
	AdvancedPlayer advancedPlayer;
	
	public AudioPlayer() {
		
	}
	
	public AudioPlayer(FileInputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
	
	public void play() {
		try {
			advancedPlayer = new AdvancedPlayer(fileInputStream);
			advancedPlayer.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
			
	}
	
	public void stop() {
	//	if (advancedPlayer != null && advancedPlayer.getPlayBackListener().playbackFinished(evt);)
		try {
			advancedPlayer = new AdvancedPlayer(fileInputStream);
			advancedPlayer.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
			
	}
	
}
