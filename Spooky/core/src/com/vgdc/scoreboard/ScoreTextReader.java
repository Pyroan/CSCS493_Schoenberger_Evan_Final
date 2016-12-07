package com.vgdc.scoreboard;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Following actual OOP Principles and
 * moving the code for reading our score.txt file
 * to its own class.
 * @author Evan S.
 *
 */
public class ScoreTextReader
{
	public static final String TAG = ScoreTextReader.class.getName();

	private FileHandle fileHandle;
	private String rawScores;
	public String[] scores;
	private Scanner scoreReader;

	/**
	 * Initialize ALL the things!
	 * except for scores!
	 * @param filepath
	 */
	public ScoreTextReader(FileHandle filepath)
	{
		fileHandle = filepath;
		// Read the whole thing into rawScores.
		rawScores = fileHandle.readString();
		// Set up a scanner to read it line by line
		scoreReader = new Scanner(rawScores);
		processRawScores();
		scoreReader.close();
	}

	/**
	 * Turns our raw string input
	 * into a series of scores,
	 * stores data in scores.
	 */
	private void processRawScores()
	{
		// God forgive this crappy code.
		// Using dynamic because who knows how long
		// the scores doc might be.
		// I kinda wish I could do this in Lua tbh.
		ArrayList<String> scoreList = new ArrayList<String>();
		// For every line in the string...
		while (scoreReader.hasNext())
		{
			String s = scoreReader.nextLine();
			// Ignore lines where the first
			// character is #
			if (s.charAt(0) != '#')
			{
				// If it's a valid entry add it to the list
				if (s.matches("(.+ )+\\d+"))
					scoreList.add(s);
				else
					Gdx.app.debug(TAG, "Invalid Entry: " + s);
			}
		}
		scoreList.toArray(scores);
		// So we end up with an array of strings that look like
		// "<name> <number>".
	}
}
