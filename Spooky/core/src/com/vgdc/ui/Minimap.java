package com.vgdc.ui;

import com.badlogic.gdx.graphics.Pixmap;
import com.vgdc.spooky.Level;
/**
 * I imagine this should work a lot like how Level
 * does, but with a different sprite sheet.
 * I hope that isn't too memory intensive.
 *
 * @author Evan S.
 * Maybe I can have it steal Level's generation,
 * but change just its render method.
 * 
 * Probably best to leave it alone for now, 
 * Since i'll have to change how Level's rendering works
 * anyway.
 */
public class Minimap extends Level{

	/**
	 * @param pixmap
	 */
	public Minimap(Pixmap pixmap) {
		super(pixmap);
	}

	public Minimap(String fileName) {
		super(fileName);
	}
}
