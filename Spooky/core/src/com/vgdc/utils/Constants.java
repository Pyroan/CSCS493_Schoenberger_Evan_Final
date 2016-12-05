package com.vgdc.utils;

import com.badlogic.gdx.Gdx;

/**
 * Constants class: Contains constant values to be used by all the code
 * @author Evan S.
 *
 */
public final class Constants {

	//pixels per meter
	//use to convert to box2d units
	//so setting boxshape and getting object position
	//giving units -> divide
	//getting units -> multiply
	public static final float PPM = 32;

	// Enable/Disable Debug Tools
	public static final boolean DEBUGGING_MAP = false;
	public static final boolean ENABLE_COLLISION = false;
	public static final boolean ENABLE_LIGHTS = false;

	// Our default map size (in tiles)
	public static final int MAP_WIDTH = 64;
	public static final int MAP_HEIGHT = 64;

	// The default window size.
	// It's the number of units the camera can see.
	public static final float VIEWPORT_WIDTH = DEBUGGING_MAP ? MAP_WIDTH : 28;
	public static final float VIEWPORT_HEIGHT = DEBUGGING_MAP ? MAP_HEIGHT : 16;

	// The Viewport for the GUI
	public static final float VIEWPORT_GUI_WIDTH = 1280;
	public static final float VIEWPORT_GUI_HEIGHT = 720;

	// Reference to the Texture Atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "tiles/tiles.atlas";

	// Number of Candies in the game.
	public static final int NUMBER_OF_CANDIES = 30;

	// Name of our real level.
	public static final String LEVEL_NAME = "AKHC-level01.png";
}
