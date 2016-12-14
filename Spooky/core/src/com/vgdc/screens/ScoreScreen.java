package com.vgdc.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.vgdc.scoreboard.Scoreboard;

/**
 * Displays the Scoreboard
 * @author Evan S.
 *
 */
public class ScoreScreen extends AbstractGameScreen {

	private Scoreboard scoreBoard;
	private float playerScore = -1;
	/**
	 * Constructor. Initializes an ArrayList.
	 */
	public ScoreScreen(Game game)
	{
		super(game);
	}
	
	public ScoreScreen(Game game, float playerScore)
	{
		this(game);
		this.playerScore = playerScore;
		
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(.1f,  .1f, .1f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		scoreBoard.render();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show(){
		if (playerScore < 0)
			scoreBoard = new Scoreboard(game);
		else 
			scoreBoard = new Scoreboard(game, playerScore);
	}

	@Override
	public void hide() {
		scoreBoard.dispose();
	}

}
