package com.vgdc.screens;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vgdc.ui.Fonts;
import com.vgdc.utils.Constants;

/**
 * Displays the Scoreboard
 * @author Evan S.
 *
 */
public class ScoreScreen {

	private Entry[] scores;
	private String title;

	/**
	 * Constructor. Initializes an ArrayList.
	 */
	public ScoreScreen()
	{
		title = "High Scores";
		scores = new Entry[10];
		loadScores();
	}

	/**
	 * Adds a new player/score to the score board.
	 */
	public void addEntry(String name, int score)
	{
		// Add logic to have the scores actually be sorted.
	}

	/**
	 * Draws the score board.
	 */
	public void render(SpriteBatch batch)
	{
		// First, THE TITLE.
		Fonts.instance.gamer_big.draw(batch, title, Constants.VIEWPORT_WIDTH/2, 10);
		// Next, THE SCORES
		for (int i = 0; i < scores.length; i++)
		{
			String entry = scores[i].name + "/t" + scores[i].score;
			// who doesn't like magic numbers?
			Fonts.instance.gamer.draw(batch, entry, Constants.VIEWPORT_WIDTH/2, 50 + (30 * i));
		}
	}

	/**
	 * Loads previous high scores to scores.txt
	 */
	public void loadScores()
	{
		FileHandle fh = Gdx.files.internal("scores.txt");
		String rawScores = fh.readString();
		// This feels a bit dirty
		Scanner scoreReader = new Scanner(rawScores);
		// Reads each line, separates it, uses data to make new entry.
		int count=0;
		while (scoreReader.hasNext())
		{
			// Get dat input
			String s = scoreReader.next();
			String[] data = s.split(" ");
			// Process dat input
			String name = data[0];
			int score = Integer.parseInt(data[1]);
			// Add dat data.
			Entry e = new Entry(name, score);
			scores[count] = e;
			count++;
		}
		scoreReader.close();
	}

	/**
	 * Saves new high scores to scores.txt
	 */
	public void saveScores()
	{
		FileHandle fh = Gdx.files.internal("scores.txt");
		// Gotta write all the current scores to scores.txt
	}

	/**
	 * Holds a name and a score.
	 */
	public class Entry
	{
		String name;
		int score;
		public Entry(String name, int score)
		{
			this.name = name;
			this.score = score;
		}
	}
}
