package com.vgdc.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.vgdc.spooky.PlayerControls;
import com.vgdc.spooky.WorldController;
import com.vgdc.spooky.WorldRenderer;
public class GameScreen extends AbstractGameScreen 
{
	private static final String TAG = GameScreen.class.getName();
	
	// PlayerController (in progress)
	PlayerControls controller;
	
	// Updates the world, including the level and
	// player and music and whatnot.
	private WorldController worldController;
	
	// Draws the whole world.
	private WorldRenderer worldRenderer;
	
	public GameScreen (Game game)
	{
		super(game);
	}
	
	@Override
	public void render(float deltaTime)
	{

		// Update game world by the time that has passed
		// since last rendered frame.
		worldController.update(deltaTime);

		// Sets the clear screen color to: Not what the book wanted.
		Gdx.gl.glClearColor(.1f, .1f, .1f, 0f);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Render game world to screen
		worldRenderer.render();
	}

	
	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width, height);
	}
	
	@Override
	public void show ()
	{
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}
	
	@Override
	public void hide()
	{
		worldController.dispose();
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}
	
}
