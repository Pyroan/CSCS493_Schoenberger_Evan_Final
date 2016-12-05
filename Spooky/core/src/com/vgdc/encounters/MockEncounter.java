package com.vgdc.encounters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vgdc.utils.Constants;

/**
 * A test encounter to make sure this isn't entirely stupid.
 * @author Evan S.
 *
 */
public class MockEncounter extends AbstractEncounter {
	private static final String TAG = AbstractEncounter.class.getName();

	/**
	 * Pretty sure my implementation is hot garbage.
	 */
	public MockEncounter() {
		super("Test Encounter");
	}

	@Override
	public void trigger() {
		isTriggered = true;
		Gdx.app.log(TAG, "Was triggered.");
		if (currentText == null) {
		currentText = "This is a test.";
		} else {
			currentText = "You failed";
		}
	}
}
