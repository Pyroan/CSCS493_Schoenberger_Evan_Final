package com.vgdc.scoreboard;
/**
 * Holds a name and a score.
 */
public class Entry implements Comparable<Entry>
{
	String name;
	int score;
	public Entry(String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
	/**
	 * Handles comparisons so I can call Array.sort()
	 * instead of actually doing work.
	 */
	@Override
	public int compareTo(Entry arg0) {
		// Rememeber that "Score" is time, so
		// Smaller is better.
		if (this.score < arg0.score)
			return -1;
		if (this.score > arg0.score)
			return 1;
		// Default: They're equal.
		return 0;
	}
}