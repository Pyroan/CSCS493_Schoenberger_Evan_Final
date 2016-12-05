package com.vgdc.encounters;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
/**
 * An encounter to be triggered by events that occur in the world.
 * Maybe have an EncounterHandler that knows what triggers every encounter
 * And then triggers them when appropriate.
 * Yeah that's a good idea.
 * Oh boy I hope I figure out how this works.
 * @author Evan S.
 *
 */
public abstract class AbstractEncounter {

	String title;
	String currentText;

	boolean isTriggered;

	public AbstractEncounter (String title) {
		this.title = title;
	}

	public abstract void trigger();


}
