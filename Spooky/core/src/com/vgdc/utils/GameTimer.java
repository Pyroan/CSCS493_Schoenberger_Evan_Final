package com.vgdc.utils;

/**
 * Keeps track of time in both a raw format and a more
 * user-friendly string format.
 * @author Evan S.
 */
public class GameTimer {

	// Time since initialization (in seconds)
	private float rawTime;
	// String representation of time, in form HH:MM:SS
	private String time = "";


	/**
	 * Creates a new Timer starting at 0 seconds.
	 */
	public GameTimer()
	{
		this(0);
	}

	/**
	 * If you want to initialize this with a specific time for some reason.
	 * @param startTime Time (in seconds) to start the timer from.
	 */
	public GameTimer(int startTime)
	{
		rawTime = startTime;
		translateTime();
	}

	/**
	 * Turns raw time into actual real time that we can
	 * look at and not throw up.
	 */
	private void translateTime()
	{
		// Turns seconds into minutes and seconds.
		int minutes = (int)rawTime / 60;
		int seconds = (int)rawTime % 60;
		int hours = 0; // I'll be amazed if this is needed.

		// Turns minutes bigger than 59 into hours.
		hours = minutes / 60;
		minutes = minutes % 60;

		// Calculations done. Time to convert to a pretty string.

		// Only bother with hours if they've actually passed.
		time = "";
		if (hours > 0) time = hours + ":";
		// If minutes and seconds < 10, need to add a 0
		if (minutes < 10) time+= "0";
		time+= minutes + ":";
		if (seconds < 10) time+="0";
		time+= seconds + "";
	}

	/**
	 * Adds the time since last frame to the current time.
	 * @param deltaTime
	 */
	public void update(float deltaTime)
	{
		rawTime+=deltaTime;
		translateTime();
	}

	/**
	 * @return String representation of time, in form (HH:)MM:SS
	 */
	public String getTime()
	{
		return time;
	}

	/**
	 * @return Raw time in seconds (float).
	 */
	public float getRawTime()
	{
		return rawTime;
	}

}
