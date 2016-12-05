package com.vgdc.encounters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.vgdc.utils.Constants;

/**
 * Keeps track of all our encounters
 * and watches for their triggers,
 * setting them off when appropriate.
 * @author Evan S.
 *
 */
public class EncounterHandler {

	private Pixmap textBox;
	private Texture textBoxTex;
	
	BitmapFont font = new BitmapFont(Gdx.files.internal("arial-15.fnt"), Gdx.files.internal("arial-15.png"), false);
	
	// List of all the encounters we can use.
	public Array<AbstractEncounter> encounters;

	/**
	 * Initializes AbstractEncounter array
	 */
	public EncounterHandler() {
		encounters = new Array<AbstractEncounter>();
		textBox = new Pixmap(600, 200, Format.RGB888);
		textBox.fillRectangle(0, 0, textBox.getWidth(), textBox.getHeight());
		textBoxTex = new Texture(textBox);
		initEncounters();
	}

	/**
	 * For now, just makes a mock encounter.
	 * Eventually will make all the encounters?
	 */
	private void initEncounters() {
		encounters.add(new MockEncounter());
	}

	/**
	 * Checks to see if any events have been triggered.
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		for (int i = 0; i < encounters.size; i++) {
			if (encounters.get(i).isTriggered) {
				renderTextBox(batch, encounters.get(i));				
			}
		}
	}
	
	/**
	 * Sees checks all encounter triggers.
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		if (Gdx.input.isKeyJustPressed(Keys.T)) {
			int ind = encounters.indexOf(new MockEncounter(), false);
//			if (ind >=0) 
				encounters.get(0).trigger();
		}
	}

	private void renderTextBox(SpriteBatch batch, AbstractEncounter encounter) {
		batch.draw(textBoxTex, Constants.VIEWPORT_GUI_WIDTH/2 - textBoxTex.getWidth()/2,
				10);

		font.setColor(Color.WHITE);
		font.draw(batch, encounter.title + ": " + encounter.currentText,
				Constants.VIEWPORT_GUI_WIDTH/2 - textBoxTex.getWidth()/2 + 10, textBoxTex.getHeight(),
				textBox.getWidth(), Align.left, true);
	}

}
