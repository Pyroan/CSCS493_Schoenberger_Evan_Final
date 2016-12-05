package com.vgdc.spooky;

import java.awt.Point; // using AWT like a scrub.
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.vgdc.utils.Constants;

/*******************************************
 *                                         *
 *             MapGenerator                *
 *                                         *
 * Pseudo-Randomly generates a map for us. *
 * @author Evan S.                         *
 *                                         *
 *******************************************/
/**
 * Generally I've been testing it with the seed 12345789, but I need to find one
 * that generates blocked off areas on me map.
 * or, I could have it draw more trees which would lead to more messed-up stuff.
 */
public class AlternativeMapGen implements Disposable {
	private static final String TAG = AlternativeMapGen.class.getName();

	// Defines the colors we're generating.
	// Much cleaner than the disgusting thing we were doing before.
	private enum OBJECT {
		GROUND (0, 0, 0, 1, 1),
		EXIT   (0, 0, 1, 1, 1),
		TREE   (0, 1, 0, 2, 2),
		HOUSE  (0, 1, 1, 2, 4),
		ROCK   (1, 0, 0, 2, 2),
		CANDY  (1, 0, 1, 1, 1), // Since candy is almost more of a particle might not be worth spawning.
		BUSH   (1, 1, 0, 1, 1),
		PLAYER (1, 1, 1, 1, 1);

		private float r;
		private float g;
		private float b;

		private int colWidth; // Collision dimensions
		private int colHeight;// So we can draw things without bad overlap.

		OBJECT (float r, float g, float b, int width, int height) {
			this.r = r;
			this.g = g;
			this.b = b;

			this.colWidth = width;
			this.colHeight = height;
		}

		public int getColWidth() {
			return colWidth;
		}

		public int getColHeight() {
			return colHeight;
		}
	}

	// Array of all the ground area.
	private Array<Point> groundPixels;

	// Array of pixels that are still available to drop things.
	private Array<Point> availableArea;

	// Our actual map.
	private Pixmap map;
	// Random number generator
	private Random rng;

	// Whether the map has spawned the player yet.
	boolean spawnedPlayer = false;

	public AlternativeMapGen(int width, int height, long seed) {
		rng = new Random(seed);
		map = new Pixmap(width, height, Format.RGB888);
		groundPixels = new Array<Point>();
		initAvailableArea();
		generate();
	}

	/**
	 * This feels extremely dirty. EXTREMELY.
	 */
	private void initAvailableArea() {
		availableArea = new Array<Point>();

		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				availableArea.add(new Point(i, j));
			}
		}
	}

	/**
	 * Allegedly is supposed to somehow generate a map.
	 */
	// TODO: Encapsulate each step into it's own method(?)
	private Pixmap generate() {
		map.drawRectangle(0, 0, map.getWidth(), map.getHeight());
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				// Step -1: Make sure we can draw here.
				if (!availableArea.contains(new Point(x,y), false)) continue; // OH GOD IT'S A DIRTY WORD.

				// Step 0: Generate an Integer.
				int object = rng.nextInt(100); // Basically we're gonna pretend we're working with percentages.

				// Step 1: Randomly decide if the current thing is a wall.
				if (object < 40) {
					drawObject(OBJECT.BUSH, x, y);
				}
			}
		}
		// Step 4: add the player.
		randomSpawn(OBJECT.PLAYER);
		// Step 5: add the candy.
		spawnCandy();
		return map;
	}

	/**
	 * Step 2: Spawn interior
	 */
	private void spawnInterior(int x, int y, int ran) {
			OBJECT obj = OBJECT.GROUND;
		if (ran<60) {
			obj = OBJECT.GROUND;
			// Add location of the ground to the array.
			// This comes in handy later setting up paths.
			groundPixels.add(new Point(x, y));
		}
		else if (ran>=60 && ran<90)
			obj = OBJECT.TREE;
		else if (ran>=90 && ran<95)
			obj = OBJECT.BUSH;
		else if (ran>=95 && ran<100)
			obj = OBJECT.ROCK;
		drawObject(obj, x, y);
	}

	/**
	 * Step 3: Ensure all ground tiles are reachable.
	 */
	private void fixPathing() {

		/**
		 * Goal: Create an algorithm that makes sure the entirety of the ground tiles are connected.
		 * Step 0: Somehow translate the groundPixels array into something I can use (?????)
		 * Step 1: Use a depth first search to determine connected components of the graph (Easy?)
		 * Step 2: Somehow choose some pixels to turn into ground pixels to connect unconnected components,
		 *    or turn a small unconnected component into more trees (Medium?)
		 *
		 * This may help: http://www.java2blog.com/2015/12/depth-first-search-in-java.html
		 */
	}

	/**
	 * Step 4: Actual-Randomly selects a ground tile
	 * and adds an object there.
	 */
	private void randomSpawn(OBJECT obj) {
		// Select a position.
		Point pos;
		int i;
		i = (int)(Math.random() * (availableArea.size-1));
		pos = availableArea.get(i);
		drawObject(obj, pos.x, pos.y);
		// Add the object.
		Gdx.app.log(TAG, "Chose a place to spawn" + obj + ": (" +pos.x+ ", " + pos.y+ ")");
		// Removes the location of the object from the groundPixels array.
		// may mess us up later when we're trying to fix paths,but as of right now
		// it seems the best way to prevent things spawning on top of one another.
		availableArea.removeIndex(i);
	}

	/**
	 * Step 5: Actual-Randomly generate some candy.
	 */
	private void spawnCandy() {
		for (int i = 0; i < Constants.NUMBER_OF_CANDIES; i++)
			randomSpawn(OBJECT.CANDY);
	}

	/**
	 * Draws an object at the given location and removes that
	 * object's collision space from available spawning tiles.
	 * @return true if the object was successfully drawn
	 * (i.e. its collision size didn't interfere with another object's)
	 */
	private boolean drawObject(OBJECT obj, int x, int y) {
		// Draw the object.
		setObject(obj);
		map.drawPixel(x,y);
		return true;
	}

	/**
	 * Sets the color the pixmap is drawing with to one of our
	 * predetermined values by letting you specify one of our objects.
	 * @param object the object
	 */
	private void setObject(OBJECT object) {
//		map.setColor(colorVals[object][0], colorVals[object][1], colorVals[object][2], 1);
		map.setColor(object.r, object.g, object.b, 1);
	}


	/****************************************************************
	 * UNIMPORTANT METHODS (Getters, Setters, Disposes) BEGIN HERE. *
	 ****************************************************************/

	/**
	 * @return the pixmap.
	 */
	public Pixmap getPixmap() {
		return map;
	}

	/**
	 * Frees memory
	 */
	@Override
	public void dispose() {
		map.dispose();
	}
}
