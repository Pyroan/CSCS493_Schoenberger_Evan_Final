package com.vgdc.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.vgdc.utils.Constants;

/**
 * Handles our music
 * @author Evan S.
 */
public class MusicPlayer
{
	private Music backgroundSong;
	private Music drugSong;
	private Music nathanielSnoring;
	private Music wind;
	
	private Sound pickup;
	private Sound lsdPickup;

	/**
	 * Initialize all our Music.
	 */
	public MusicPlayer()
	{
		// BG sound
		backgroundSong =
				Gdx.audio.newMusic(Gdx.files.internal("dead guy.mp3"));
//				Gdx.audio.newMusic(Gdx.files.internal("Snow in October.mp3"));
		backgroundSong.setVolume(.7f);
		backgroundSong.play();
		backgroundSong.setLooping(true);
		// Drug version of BG sound
		drugSong = Gdx.audio.newMusic(Gdx.files.internal("Snow in October Reverse.mp3"));
		drugSong.setVolume(.0f);
		drugSong.setLooping(true);
		drugSong.play();
		// NathanielSnoring
		nathanielSnoring =
				Gdx.audio.newMusic(Gdx.files.internal("Nathaniel Snoring.mp3"));
		// Wind
		wind = Gdx.audio.newMusic(Gdx.files.internal("wind.mp3"));
		wind.setLooping(true);
		wind.setVolume(0.2f);
		wind.play();
		
		// Initialize Sounds
		pickup = Gdx.audio.newSound(Gdx.files.internal("Pickup_Coin.wav"));
		lsdPickup = Gdx.audio.newSound(Gdx.files.internal("everyTen.wav"));
	}

	/**
	 * Updates music.
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		if (Math.random() > .98)
		{
			nathanielSnoring.play();
		}
		if (nathanielSnoring.getPosition() >= 7.0f)
		{
			nathanielSnoring.stop();
		}
		// Set the background music based on whether drugs are happening.
		if (Constants.LSD_MODE)
		{
			backgroundSong.setVolume(0);
			drugSong.setVolume(1.0f);
		} else 
		{
			drugSong.setVolume(0f);
			backgroundSong.setVolume(.7f);
		}
	}
	
	public void playPickup()
	{
		pickup.play();
	}
	public void playLSDPickup()
	{
		lsdPickup.play();
	}
	
	public void dispose()
	{
		lsdPickup.dispose();
		pickup.dispose();
		wind.dispose();
		nathanielSnoring.dispose();
		backgroundSong.dispose();
		drugSong.dispose();
	}
}
