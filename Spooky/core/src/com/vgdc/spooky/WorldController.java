package com.vgdc.spooky;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.vgdc.audio.MusicPlayer;
import com.vgdc.objects.AbstractGameObject;
import com.vgdc.objects.Bush;
import com.vgdc.objects.Candy;
import com.vgdc.objects.Floor;
import com.vgdc.objects.Player;
import com.vgdc.objects.Rock;
import com.vgdc.objects.Tree;
import com.vgdc.screens.ScoreScreen;
import com.vgdc.ui.UIController;
import com.vgdc.utils.CameraHelper;
import com.vgdc.utils.Constants;

/**
 * I'd be lying if i said I was prepared to explain
 * what this does.
 * In class it's been functioning simultaneously
 * as an input handler and a collision detector.
 *
 * This class is getting extremely out of hand.
 * @author Evan S.
 *
 */
public class WorldController {

	public Level level;

	private Game game;

	public CameraHelper cameraHelper;

	public PlayerControls controller;

	public MusicPlayer musicPlayer;

	public UIController uiController;

	public World b2World;

	public int numberOfCandies;
	public int collectedCandies;

	public ParticleEffect snow = new ParticleEffect();

	private static final String TAG = WorldController.class.getName();

	public WorldController() {
		init();
	}

	public WorldController (Game game) {
		this.game = game;
		init();
	}

	private void init() {
		cameraHelper = new CameraHelper();
		controller = new PlayerControls();
		musicPlayer = new MusicPlayer();
		uiController = new UIController();
		snow.load(Gdx.files.internal("particles/Snow"), Gdx.files.internal("particles"));
		collectedCandies = 0;
		initLevel();
		initPhysics();
	}

	/**
	 * Initializes the level (right now with a given seed)
	 */
	private void initLevel() {
		long seed = 123456789; // Seed can be up to 9 digits long (for now).
		//		MapGenerator mg = new MapGenerator(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, seed);
		AlternativeMapGen mg = new AlternativeMapGen(Constants.MAP_WIDTH, Constants.MAP_HEIGHT, seed);
		// We're actually not gonna use Procedural generation for now but I'll leave the code for later.
		// That's disgusting how Dare I do that.
		level = new Level(Constants.LEVEL_NAME);
		//		level = new Level(mg.getPixmap());
		mg.dispose();
		if (!Constants.DEBUGGING_MAP) cameraHelper.setTarget(level.player);
		numberOfCandies = level.getNumberOfCandies();
	}

	public void update(float deltaTime)
	{
		// Input Handling
		handleDebugInput(deltaTime);
		handleCameraMovement(deltaTime);
		handlePlayerMovement(deltaTime);
		// Update UI/Level Objects
		uiController.update(deltaTime);
		level.update(deltaTime);
		b2World.step(deltaTime, 8, 3);
		//Test for collision
		if (Constants.ENABLE_COLLISION) testForCollision();
		// Move Camera
		cameraHelper.update(deltaTime);
		// Play Music
		musicPlayer.update(deltaTime);
		// Update Snow.
		snow.update(deltaTime);
	}


	/**
	 * Moves the camera to a place.
	 * @param x how far to move it.
	 * @param y how far to move it.
	 */
	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	/**
	 * Handles input for debug features,
	 * like letting the camera move freely
	 * @param deltaTime
	 */
	public void handleDebugInput(float deltaTime)
	{
		if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE))
			if (!cameraHelper.hasTarget())
				cameraHelper.setTarget(level.player);
			else
				cameraHelper.setTarget(null);
		// Switch to the High Score screen.
		if (Gdx.input.isKeyJustPressed(Keys.Y))
			game.setScreen(new ScoreScreen(game));
		// Test a mock encounter.
	}

	/**
	 * Moves the camera around if the camera doesn't have a target.
	 * @param deltaTime
	 */
	public void handleCameraMovement(float deltaTime)
	{
		if (cameraHelper.hasTarget()) return;

		float cameraMoveSpeed = 5 * deltaTime;
		if(Gdx.input.isKeyPressed(Keys.W))
		{
			moveCamera(0, cameraMoveSpeed);
		}
		if(Gdx.input.isKeyPressed(Keys.A))
		{
			moveCamera(-cameraMoveSpeed, 0 );
		}
		if (Gdx.input.isKeyPressed(Keys.S))
		{
			moveCamera(0, -cameraMoveSpeed);
		}
		if(Gdx.input.isKeyPressed(Keys.D))
		{
			moveCamera(cameraMoveSpeed, 0);
		}
	}

	/**
	 * Moves the player around
	 * @param deltaTime
	 */
	public void handlePlayerMovement(float deltaTime)
	{
		if (!cameraHelper.hasTarget(level.player)) return;

		if (Gdx.input.isKeyPressed(Keys.W)) {
			level.player.body.linVelLoc.y = level.player.terminalVelocity.y;
			level.player.setTexture(level.player.back);
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			level.player.velocity.y = -level.player.terminalVelocity.y;
			level.player.setTexture(level.player.front);
		}

		if (Gdx.input.isKeyPressed(Keys.A)) {
			level.player.body.linVelLoc.x = -level.player.terminalVelocity.x;
			level.player.setTexture(level.player.left);
		}else if (Gdx.input.isKeyPressed(Keys.D)) {
			level.player.body.linVelLoc.x = level.player.terminalVelocity.x;
			level.player.setTexture(level.player.right);
		}
	}

	private void initPhysics()
	{
		if (b2World != null) b2World.dispose();
		b2World = new World(new Vector2(0, -9.81f), true);
		// Rocks?
		Vector2 origin = new Vector2();
		for (Floor floor: level.tiles)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(floor.position);
			Body body = b2World.createBody(bodyDef);
			floor.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = floor.bounds.width / 2.0f;
			origin.y = floor.bounds.height / 2.0f;
			polygonShape.setAsBox(floor.bounds.width /2.0f,
					floor.bounds.height / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
		// Player
		origin = new Vector2();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(level.player.position);
		bodyDef.fixedRotation = true;
		Body body = b2World.createBody(bodyDef);
		level.player.body = body;
		PolygonShape polygonShape = new PolygonShape();
		origin.x = level.player.bounds.width / 2.0f;
		origin.y = level.player.bounds.height / 2.0f;
		polygonShape.setAsBox(level.player.bounds.width / 2.0f,
				level.player.bounds.height / 2.0f, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		body.createFixture(fixtureDef);
		polygonShape.dispose();
	}
	
	public void dispose()
	{
		if (b2World != null) b2World.dispose();
	}


	/*************************************
	 * Collision Detection Begins Here   *
	 * TODO: Turn this into Box2D Stuff. *
	 *************************************/

	/**
	 * Really important note: I'm very tempted to move all this out
	 *    into its own class.
	 * However, since Lis is currently redoing how collision works
	 *    entirely, there's really not point in spending time doing
	 *    all that refactoring. I hope.
	 */
	Rectangle r1 = new Rectangle();
	Rectangle r2 = new Rectangle();

	/**
	 * How to pick up candies.
	 * @param candy the Candy the player is colliding with.
	 */
	private void onCollisionPlayerWithCandy (Candy candy) {
		candy.collected = true;
		collectedCandies++;
		Gdx.app.log(TAG, "Candy Collected");
	}

	/**
	 * How to not be on walls.
	 * @param wall
	 */
	private void onCollisionPlayerWithWall (AbstractGameObject wall) {
		Player player = level.player;
		float yDiff = Math.abs(wall.bounds.y - player.bounds.y);
		float yGoal = wall.bounds.height/2 + player.bounds.height/2;

		// If  they're too close together, move them apart.
		if (yDiff < yGoal){

			if (player.position.y > wall.position.y) {
				player.position.y += (1 - yGoal - yDiff);
			}
			if (player.position.y < wall.position.y){
				player.position.y -= (1 - yGoal - yDiff);
			}
		}

		float xDiff = Math.abs(wall.bounds.x - player.bounds.x);
		float xGoal = wall.bounds.width/2 + player.bounds.width/2;

		if (xDiff < xGoal) {
			if (player.position.x > wall.position.x) {
				player.position.x += (1 - xGoal - xDiff);
			}
			if (player.position.x < wall.position.x){
				player.position.x -= (1 -xGoal - xDiff);
			}
		}

		float y = 1-yGoal - yDiff;
		float x = 1-xDiff-xGoal;
		Gdx.app.log(TAG, "U HIT DA WALL. " + y + " " + x );
	}

	/**
	 * ... Collision Detection (For real this time)
	 */
	private void testForCollision () {
		r1.set(level.player.position.x, level.player.position.y,
				level.player.bounds.width, level.player.bounds.height);

		for (Candy candy : level.candies) {
			r2.set(candy.position.x, candy.position.y, candy.bounds.width, candy.bounds.height);
			if (!r1.overlaps(r2)) continue;
			if (!candy.collected)
				onCollisionPlayerWithCandy(candy);
			break;
		}

		// I don't wanna test all these walls :\
		for (Tree tree: level.trees) {
			r2.set(tree.position.x, tree.position.y, tree.bounds.width, tree.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionPlayerWithWall(tree);
		}

		for (Bush bush: level.bushes) {
			r2.set(bush.position.x, bush.position.y, bush.bounds.width, bush.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionPlayerWithWall(bush);
		}

		for (Rock rock: level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionPlayerWithWall(rock);
		}
	}
}
