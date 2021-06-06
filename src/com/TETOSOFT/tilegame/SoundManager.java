package com.TETOSOFT.tilegame;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
	
	Clip clip;

	
	public void playMusic(String filepath) {
		
		try {
			
			File music = new File(filepath);
			
			if(music.exists()) {
				AudioInputStream ais = AudioSystem.getAudioInputStream(music);
				clip = AudioSystem.getClip();
				clip.open(ais);
				clip.start();
			}else {
				System.out.println("Can't find file");
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void stopMusic() {
		clip.stop();
	}
	
	public void loopMusic(String filepath) {
		
		playMusic(filepath);
		
		 try {
             if (clip != null) {
                 new Thread() {
                     public void run() {
                         synchronized (clip) {
                             clip.stop();
                             clip.setFramePosition(0);
                             clip.loop(Clip.LOOP_CONTINUOUSLY);
 
                         }
                     }
 
                 }.start();
 
             }
 
         } catch (Exception e) {
 
             e.printStackTrace();
 
         }
 
	}

}
