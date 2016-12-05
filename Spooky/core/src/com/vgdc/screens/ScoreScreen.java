package com.vgdc.screens;

import java.util.Scanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.vgdc.ui.Fonts;
import com.vgdc.utils.Constants;

/**
 * Displays the Scoreboard
 * @author Evan S.
 *
 */
public class ScoreScreen extends AbstractGameScreen {

	private Entry[] scores;
	private String title;
	
	private SpriteBatch batch;

	/**
	 * Constructor. Initializes an ArrayList.
	 */
	public ScoreScreen(Game game)
	{
		super(game);
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
	 * Draws the score board.
	 */
	public void render(SpriteBatch batch)
	{
		handleInput();
		batch.begin();
		// First, THE TITLE.
		Fonts.instance.gamer_big.draw(batch, title, 
				Constants.VIEWPORT_GUI_WIDTH/2, Constants.VIEWPORT_GUI_HEIGHT - 50,
				0, Align.center, false);
		// Next, THE SCORES
		for (int i = 0; i < scores.length; i++)
		{
			String entry = "";
			entry += i + 1 + ". " + "TEST"; 
			//		scores[i].name + "/t" + scores[i].score;
			// who doesn't like magic numbers?
			Fonts.instance.gamer.draw(batch, entry, Constants.VIEWPORT_GUI_WIDTH/2 - 300, 
					Constants.VIEWPORT_GUI_HEIGHT - 150 - (30 * i), 0, Align.left, false);
		}
		batch.end();
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

	@Override
	public void render(float deltaTime) {
		render(batch);
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		Gdx.input.setCatchBackKey(false);
	}
	
	public void handleInput() 
	{
		if (Gdx.input.isKeyJustPressed(Keys.Y))
			game.setScreen(new GameScreen(game));
	}
}
