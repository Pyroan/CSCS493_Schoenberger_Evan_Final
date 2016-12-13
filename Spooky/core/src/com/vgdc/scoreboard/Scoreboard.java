package com.vgdc.scoreboard;

import java.util.Scanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.vgdc.screens.GameScreen;
import com.vgdc.screens.MenuScreen;
import com.vgdc.ui.Fonts;
import com.vgdc.utils.Constants;

public class Scoreboard
{
	private Entry[] scores;
	private String title;

	Game game;
	private SpriteBatch batch;

	public Scoreboard(Game game)
	{
		this.game = game;
		title = "HIGH SCORES";
		scores = new Entry[10];
		batch = new SpriteBatch();
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
	 * Draws the scoreboard
	 * @param batch
	 */
	public void render()
	{
		batch.begin();
		// First, THE TITLE.
		Fonts.instance.gamer_big.draw(batch, title,
				Constants.VIEWPORT_GUI_WIDTH/2, Constants.VIEWPORT_GUI_HEIGHT - 50,
				0, Align.center, false);
		// Next, THE SCORES
		for (int i = 0; i < scores.length; i++)
		{
			String entry = "";
			entry += i + 1 + ". ";
			if (scores[i] != null) 
			{
				entry += scores[i].name;
			}
			else
			{
				entry += "--";
			}
			//		scores[i].name + "/t" + scores[i].score;
			// who doesn't like magic numbers?
			Fonts.instance.gamer.setColor(Color.WHITE);
			Fonts.instance.gamer.draw(batch, entry, Constants.VIEWPORT_GUI_WIDTH/2 - 300,
					Constants.VIEWPORT_GUI_HEIGHT - 150 - (30 * i), 0, Align.left, false);
		}
		batch.end();
		handleInput();
	}

	/**
	 * Loads previous high scores from scores.txt
	 */
	public void loadScores()
	{
		FileHandle fh = Gdx.files.internal("scores.txt");
		// Reads in all the scores from scores.txt as a long string.
		String rawScores = fh.readString();
		// This feels a bit dirty
		Scanner scoreReader = new Scanner(rawScores);
		// Reads each line, separates it, uses data to make new entry.
		int count=0;
		while (scoreReader.hasNext())
		{
			// Get dat input
			String s = scoreReader.nextLine();
			System.out.println("S: " + s);
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

	/**
	 * Handles any input we got hurr
	 */
	public void handleInput()
	{
		if (Gdx.input.isKeyJustPressed(Keys.Y))
			game.setScreen(new GameScreen(game));
		if (Gdx.input.isKeyJustPressed(Keys.K))
			game.setScreen(new MenuScreen(game));
	}	

	/**
	 * Free memory
	 */
	public void dispose()
	{
		batch.dispose();		
	}
}
