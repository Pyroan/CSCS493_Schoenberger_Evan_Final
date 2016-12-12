package com.vgdc.spooky;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.vgdc.objects.AbstractGameObject;
import com.vgdc.objects.Bush;
import com.vgdc.objects.Candy;
import com.vgdc.objects.Floor;
import com.vgdc.objects.HorizontalHouse;
import com.vgdc.objects.SpookyBackground;
import com.vgdc.objects.VerticalHouse;
import com.vgdc.objects.Player;
import com.vgdc.objects.Rock;
import com.vgdc.objects.Tree;
import com.vgdc.utils.Constants;

/**
 * Our Level within the game.
 * @author Evan S.
 *
 */
public class Level implements Disposable {
	public static final String TAG = Level.class.getName();

	// Display the Pixmap for debugging
	private boolean showPixmap = Constants.DEBUGGING_MAP;
	Pixmap pixmap;
	Texture tex;

	/**
	 * All our potential tiles and what they'll be
	 * represented with.
	 *
	 * You may be wondering why we don't just use
	 * the OBJECT enum from our Map Generator. TBH
	 * I don't really know, but I do know that that one
	 * uses floats for color instead of ints.
	 * @author Evan S.
	 */
	protected enum TILE {
		TREE   (0, 255, 0),	// Green
		BUSH   (255, 255, 0),	// Yellow
		GROUND (0, 0, 0),	// Black
		ROCK   (255, 0, 0),	// Red
		PLAYER (255, 255, 255), // White
		CANDY  (255, 0, 255), // Magenta
		VERTICAL_HOUSE (0, 255, 255),	// Cyan
		HORIZONTAL_HOUSE(0, 0, 255);	// Blue

		private int color;

		TILE(int r, int g, int b) {
			// Converts three color values to
			// an int. There's some scary bitwise
			// stuff going on and I don't like it.
			color = r<<24|g<<16|b<<8|0xff;
		}

		/**
		 * @param color
		 * @return true if the pixel's color matches.
		 */
		public boolean sameColor (int color) {
			return this.color == color;
		}

		public int getColor () {
			return color;
		}
	}

	/**
	 * Takes in a Pixmap made by a MapGenerator.
	 * @param pixmap
	 */
	public Level(Pixmap pixmap) {
		init(pixmap);
	}

	/**
	 * Takes an image file and converts it into a
	 * pixmap.
	 * @param filename
	 */
	public Level (String filename) {
		Pixmap tempMap = new Pixmap(Gdx.files.internal(filename));
		init(tempMap);
	}

	// Objects
	public Array<Tree> trees;
	public Array<Bush> bushes;
	public Array<Rock> rocks;
	public Array<Floor> tiles;
	public Array<Candy> candies;
	public Array<VerticalHouse> verticalHouses;
	public Array<HorizontalHouse> horizontalHouses;

	public Player player;
	public SpookyBackground spooky;

	/**
	 * Reads in a pixmap, filling our Arrays based on the colors
	 * of each pixel.
	 * @param pixmap
	 */
	private void init(Pixmap pixmap) {
		this.pixmap = pixmap;
		tex = new Texture(pixmap);
		// objects
		trees = new Array<Tree>();
		bushes = new Array<Bush>();
		rocks = new Array<Rock>();
		tiles = new Array<Floor>();
		candies = new Array<Candy>();
		verticalHouses = new Array<VerticalHouse>();
		horizontalHouses = new Array<HorizontalHouse>();
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// Height goes from bottom to top.
				float baseHeight = pixmap.getHeight() - pixelY;
				// get color of current pixel as 32 bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);

				/**
				 * Find matching color value to identify block type at (x,y)
				 * point and create the corresponding game object if there
				 * is a match.
				 */

				// Tree
				if (TILE.TREE.sameColor(currentPixel)) {
					obj = new Tree();
					obj.position.set(pixelX, baseHeight);
					//					obj.createBox(obj.position.x, obj.position.y, obj.getWidth(), obj.getHeight(), true);
					trees.add((Tree)obj);

				}

				// Bush (did 9/11)
				else if (TILE.BUSH.sameColor(currentPixel)) {
					obj = new Bush();
					obj.position.set(pixelX, baseHeight);
					//					obj.createBox(obj.position.x, obj.position.y, obj.getWidth(), obj.getHeight(), true);
					bushes.add((Bush)obj);
				}

				// Rock
				else if (TILE.ROCK.sameColor(currentPixel)) {
					obj = new Rock();
					obj.position.set(pixelX, baseHeight);
					//					obj.createBox(obj.position.x, obj.position.y, obj.getWidth(), obj.getHeight(), true);
					rocks.add((Rock)obj);
				}

				// Player
				else if (TILE.PLAYER.sameColor(currentPixel)) {
					obj = new Player();
					obj.position.set(pixelX, baseHeight);
					player = (Player)obj;
					Gdx.app.log(TAG, "Found a player spawn: (" + pixelX + ", " + pixelY + ")");
				}

				// Candy
				else if (TILE.CANDY.sameColor(currentPixel)) {
					obj = new Candy();
					obj.position.set(pixelX, baseHeight);
					candies.add((Candy)obj);
				}

				// Vertical House
				else if (TILE.VERTICAL_HOUSE.sameColor(currentPixel)) {
					obj = new VerticalHouse();
					obj.position.set(pixelX, baseHeight);
					verticalHouses.add((VerticalHouse)obj);
				}

				// Horizontal House
				else if (TILE.HORIZONTAL_HOUSE.sameColor(currentPixel)) {
					obj = new HorizontalHouse();
					obj.position.set(pixelX, baseHeight);
					horizontalHouses.add((HorizontalHouse)obj);
				}

				else if (TILE.GROUND.sameColor(currentPixel)) {
					obj = new Floor();
					obj.position.set(pixelX, baseHeight);
					tiles.add((Floor)obj);
				}

				// Unknown pixel color.
				else {
					int r = 0xff & (currentPixel >>> 24); // red color channel
					int g = 0xff & (currentPixel >>> 16); // green color channel
					int b = 0xff & (currentPixel >>> 8);  // blue color channel
					int a = 0xff & (currentPixel);
					//					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
					//							+ pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
			}
		}

		spooky = new SpookyBackground(pixmap.getWidth() * 2, pixmap.getHeight() * 2);
		spooky.position.set(0, 0);

		// Tell me something.
		Gdx.app.log(TAG, candies.size + " Candies spawned.");
		if (player == null) Gdx.app.error(TAG, "Player not spawned!");
		Gdx.app.debug(TAG,"Level Loaded");
	}

	/**
	 * Draws all our objects in the Level.
	 * @param batch
	 */
	public void render (SpriteBatch batch) {
		if (showPixmap) {
			batch.draw(tex, 0, 0, Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
		} else{

			spooky.render(batch);
			if (Constants.LSD_MODE)
			{
				float r = (float)Math.random();
				float g = (float)Math.random();
				float b = (float)Math.random();
				batch.setColor(r, g, b, 1);
			}
			// Draw Floor
			for (Floor floor: tiles)
				floor.render(batch);

			// Draw Rocks
			for (Rock rock: rocks)
				rock.render(batch);

			// Draw Bushes
			for (Bush /*GHW*/ bush /*GW*/: bushes /*Jeb*/)
				bush.render(batch);

			// Draw trees
			for (Tree tree: trees)
				tree.render(batch);

			// Draw Candies
			for (Candy candy: candies)
				candy.render(batch);

			player.render(batch);
			// Draw houses
			for (VerticalHouse house: verticalHouses)
				house.render(batch);
			// Draw horizontal houses
			for (HorizontalHouse house: horizontalHouses)
				house.render(batch);
			batch.setColor(1, 1, 1, 1);
		}
	}

	/**
	 * Updates all objects in the level.
	 */
	public void update (float deltaTime) {
		spooky.update(deltaTime);
		player.update(deltaTime);
		// Floor
		for (Floor floor: tiles)
			floor.update(deltaTime);
		// Rocks
		for (Rock rock: rocks)
			rock.update(deltaTime);

		// Houses
		for (VerticalHouse house: verticalHouses)
			house.update(deltaTime);
		for (HorizontalHouse house: horizontalHouses)
			house.update(deltaTime);
		// Bushes
		for (Bush bush: bushes)
			bush.update(deltaTime);
		// Trees
		for (Tree tree: trees)
			tree.update(deltaTime);
		// Candies
		for (Candy candy: candies)
			candy.update(deltaTime);
	}

	/**
	 * @return how many candies are in the map.
	 */
	public int getNumberOfCandies() {
		return candies.size;
	}

	/**
	 * Frees memory.
	 */
	public void dispose() {
		// Free memory from pixmap
		pixmap.dispose();
	}
}
