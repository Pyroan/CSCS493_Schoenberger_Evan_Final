package com.vgdc.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Handles our music
 * @author Evan S.
 */
public class MusicPlayer
{
	private Music backgroundSong;
	private Music nathanielSnoring;
	private Music wind;
	
	private Sound pickup;
	private Sound everyTen;

	/**
	 * Initialize all our Music.
	 */
	public MusicPlayer()
	{
		// BG sound
		backgroundSong =
				Gdx.audio.newMusic(Gdx.files.internal("dead guy.mp3"));
		backgroundSong.setVolume(.5f);
		backgroundSong.play();
		backgroundSong.setLooping(true);
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
		everyTen = Gdx.audio.newSound(Gdx.files.internal("everyTen.wav"));
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
	}
	
	public void playPickup()
	{
		pickup.play();
	}
	public void playEveryTen()
	{
		everyTen.play();
	}
	
	public void dispose()
	{
		everyTen.dispose();
		pickup.dispose();
		wind.dispose();
		nathanielSnoring.dispose();
		backgroundSong.dispose();
	}
}
