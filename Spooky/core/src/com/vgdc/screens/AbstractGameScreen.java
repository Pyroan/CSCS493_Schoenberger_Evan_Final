package com.vgdc.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.vgdc.spooky.Assets;

public abstract class AbstractGameScreen extends ScreenAdapter {
	protected Game game;
	
	public AbstractGameScreen(Game game)
	{
		this.game = game;
	}
	
	public abstract void render(float deltaTime);
	public abstract void resize(int width, int height);
	public abstract void show ();
	public abstract void hide();
	public void dispose()
	{
		Assets.instance.dispose();
	}
}
