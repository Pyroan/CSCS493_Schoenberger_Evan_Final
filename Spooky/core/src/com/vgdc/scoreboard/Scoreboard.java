package com.vgdc.scoreboard;

import java.util.Scanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.vgdc.screens.GameScreen;
import com.vgdc.screens.MenuScreen;
import com.vgdc.ui.Fonts;
import com.vgdc.utils.Constants;
import com.vgdc.utils.GameTimer;

public class Scoreboard
{
	private Array<Entry> scores;
	private String title;

	Game game;
	private SpriteBatch batch;

	public Scoreboard(Game game)
	{
		this.game = game;
		title = "LOCAL HIGH SCORES";
		scores = new Array<Entry>(10);
		batch = new SpriteBatch();
		loadScores();
	}

	public Scoreboard(Game game, float playerScore)
	{
		this(game);
		scores.add(new Entry ("You", (int)playerScore));
		scores.sort();
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
		for (int i = 0; i < 10; i++)
		{
			String entry = "";
			if ((i+1) / 10 == 0) entry = " ";
			entry += i + 1 + ". ";
			if (scores.get(i) != null) 
			{
				entry += scores.get(i).name;
			}
			else
			{
				entry += "--";
			}
			
			// Translate the Score version into readable time.
			GameTimer gt = new GameTimer(scores.get(i).score);
			String time = gt.getTime();
			
			for (int j = 0; j < 30 - scores.get(i).name.length(); j++)
			{
				entry+= " ";
			}
			
			entry +=time;
			//		scores[i].name + "/t" + scores[i].score;
			// who doesn't like magic numbers?
			if (scores.get(i).name == "You")
			{
				Fonts.instance.gamer_fixed.setColor(Color.MAGENTA);
			} else
			{
				Fonts.instance.gamer_fixed.setColor(Color.WHITE);
			}
	
			Fonts.instance.gamer_fixed.draw(batch, entry, 
					Constants.VIEWPORT_GUI_WIDTH/2,
					Constants.VIEWPORT_GUI_HEIGHT - 150 - (30 * i), 
					0, Align.center, false);
		}
		batch.end();
		handleInput();
	}

	/**
	 * Loads previous high scores from scores.txt
	 */
	public void loadScores()
	{
		ScoreTextReader str = 
				new ScoreTextReader(Gdx.files.internal("scores.txt"));
		scores = str.getScores();
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
