package com.vgdc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.vgdc.spooky.Assets;
import com.vgdc.utils.Constants;
import com.vgdc.utils.GameTimer;

/**
 * Controls/renders all of our UI objects.
 * @author Evan S.
 *
 */
public class UIController {
	// I'm very familiar with the feeling of having no earthly
	// idea what I'm doing.

	private Minimap minimap; // -shrug-
	private GameTimer timer; // -another, slightly bigger shrug
	private Counter counter; // -a third, even bigger shrug-
	// Look, it's shrugs all the way down, ok?

	public UIController()
	{
		// Set up the timer.
		timer = new GameTimer();
//		minimap = new Minimap();
		counter = new Counter(30);
	}

	public void update(float deltaTime)
	{
		timer.update(deltaTime);
//		minimap.update(deltaTime);
	}

	public void render(SpriteBatch batch)
	{
		drawBase();
		drawTime(batch, 10, (int)Constants.VIEWPORT_GUI_HEIGHT-10);
		Fonts.instance.gamer.setColor(Color.MAGENTA);
		counter.render(batch, 10, (int)Constants.VIEWPORT_GUI_HEIGHT-10-(int)Fonts.instance.gamer.getLineHeight());
//		minimap.render(batch);
	}

	/**
	 * Draws all the boxes things should be put in.
	 */
	public void drawBase()
	{

	}

	/**
	 * Draws the GameTimer's time, in a box.
	 * @param batch
	 */
	public void drawTime(SpriteBatch batch, int x, int y)
	{
		// Draw the text
		String time = timer.getTime();
		Fonts.instance.gamer.setColor(Color.WHITE);
		Fonts.instance.gamer.draw(batch, time, x, y);
	}
}
