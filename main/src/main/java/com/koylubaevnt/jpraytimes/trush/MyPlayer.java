package com.koylubaevnt.jpraytimes.trush;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MyPlayer implements Runnable {
    
    private AdvancedPlayer currentPlayer;
    private String gPath;
    //frames
    private int intFrames = 0;
    private boolean playerCreatedStatus = false;
    
 
    public MyPlayer(String path){
        this.gPath = path;
        //Set and start THREAD
        Thread playerThread = new Thread(this);
        playerThread.start();       
    }
    
    public void run() {
        try {
            play();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while(!Thread.currentThread().isInterrupted()){}
    }
    
    private void createPlayer() throws IOException {
        //Set audio file
        URL urlToFile = new URL("file:///" + gPath);
        InputStream audioStream = urlToFile.openStream();
        //create player
        try {
            this.currentPlayer = new AdvancedPlayer(audioStream);
            this.currentPlayer.setPlayBackListener(new PlaybackListener() {
            @Override
                public void playbackFinished(PlaybackEvent arg0) {
                System.out.println("plFinished");
                    arg0.setSource(currentPlayer);
                    intFrames = arg0.getFrame();
                    super.playbackFinished(arg0);
                System.out.println("get frame = " + intFrames);
                    
                }   
            
            @Override
            public void playbackStarted(PlaybackEvent arg0) {
                System.out.println("plStarted");
                arg0.setSource(currentPlayer);
            }
            });         
            this.playerCreatedStatus = true;
        } catch (JavaLayerException e) {
            this.playerCreatedStatus = false;
            e.printStackTrace();    
        }   
    }
    
    public void play() throws IOException {     
        //
        System.out.println(this.playerCreatedStatus);
        //
        if(this.playerCreatedStatus == false){
            try {
                createPlayer();
                //this.currentPlayer.play(intFrames,Integer.MAX_VALUE);
                this.currentPlayer.play(intFrames,Integer.MAX_VALUE);
            } catch (JavaLayerException e) {
                System.out.println("Player not Started!");
                e.printStackTrace();
            }       
        }else{
            //pause();
        }
    }
    
    
    public void pause() {
        playerCreatedStatus = false;
        currentPlayer.stop();
    }
    
    
    public void resume() throws IOException {
            play();
    }
    
            
 
}