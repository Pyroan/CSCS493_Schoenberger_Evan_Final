package com.vgdc.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Handles our music
 * @author Evan S.
 */
public class MusicPlayer
{
	private Music backgroundSong;
	private Music nathanielSnoring;
	private Music wind;

	/**
	 * Initialize all our Music.
	 */
	public MusicPlayer()
	{
		backgroundSong =
				Gdx.audio.newMusic(Gdx.files.internal("Snow in October.mp3"));
		nathanielSnoring =
				Gdx.audio.newMusic(Gdx.files.internal("Nathaniel Snoring.mp3"));
		wind = Gdx.audio.newMusic(Gdx.files.internal("wind.mp3"));
	}

	/**
	 * Updates music.
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		backgroundSong.setVolume(.5f);
		backgroundSong.play();
		if (Math.random() > .98)
		{
			nathanielSnoring.play();
		}
		if (nathanielSnoring.getPosition() >= 7.0f)
		{
			nathanielSnoring.stop();
		}
		wind.setVolume(0.2f);
		wind.play();
	}
}
