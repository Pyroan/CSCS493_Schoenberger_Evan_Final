package com.vgdc.spooky;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.vgdc.screens.GameScreen;
import com.vgdc.screens.ScoreScreen;
import com.vgdc.ui.Fonts;

/**
 * TFW It's the most important class in the game
 * but you can't explain what it does.
 * TODO add an actual comment.
 * @author Evan S.
 */
public class Spooky extends Game {

	// The player controller.
	// Is in progress.
	PlayerControls controller;

	// Draws the whole world.
	WorldRenderer worldRenderer;
	// Updates the world, including the level and
	// the player and the music and everything.
	WorldController worldController;

	@Override
	public void create () {
		// Initialize assets and fonts
		Assets.instance.init(new AssetManager());
		Fonts.instance.init();
		initializeThings();

		setScreen(new GameScreen(this));
	}

	public void initializeThings()
	{
		controller = new PlayerControls();
	}
}
