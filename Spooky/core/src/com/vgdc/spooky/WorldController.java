package com.vgdc.spooky;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.vgdc.audio.MusicPlayer;
import com.vgdc.objects.Candy;
import com.vgdc.objects.Floor;
import com.vgdc.screens.MenuScreen;
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

	public World b2world;
	
	public PhysicsHandler physicsHandler;

	public static int numberOfCandies;
	public static int collectedCandies;

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
		physicsHandler = new PhysicsHandler();
		snow.load(Gdx.files.internal("particles/Snow"), Gdx.files.internal("particles"));
		collectedCandies = 0;
		initLevel();
		initPhysics();
		b2world.setContactListener(physicsHandler);
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

	/**
	 * Sets up Box2D Bodies. Allegedly.
	 */
	private void initPhysics()
	{
		if (b2world != null) b2world.dispose();
		b2world = new World(new Vector2(0, -9.81f), true);
		// Floor
		Vector2 origin = new Vector2();
		for (Floor floor: level.tiles)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(floor.position);
			Body body = b2world.createBody(bodyDef);
			floor.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = floor.dimension.x / 2.0f;
			origin.y = floor.dimension.y / 2.0f;
			polygonShape.setAsBox(floor.dimension.x /2.0f,
					floor.dimension.y / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.friction = .9f;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
		// Player
		origin = new Vector2();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(level.player.position);
		bodyDef.fixedRotation = false;
		Body body = b2world.createBody(bodyDef);
		level.player.body = body;
		PolygonShape polygonShape = new PolygonShape();
		origin.x = level.player.dimension.x / 2.0f;
		origin.y = level.player.dimension.y / 2.0f - .05f;
		polygonShape.setAsBox(level.player.dimension.x / 2.0f - .1f,
				level.player.dimension.y / 2.0f - .05f, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		body.createFixture(fixtureDef);
		body.setUserData(level.player);
		polygonShape.dispose();
		// Candies
		origin = new Vector2();
		for (Candy candy: level.candies)
		{
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(candy.position);
			bodyDef.fixedRotation = false;
			body = b2world.createBody(bodyDef);
			candy.body = body;
			polygonShape = new PolygonShape();
			origin.x = candy.dimension.x /2.0f;
			origin.y = candy.dimension.y / 2.0f;
			polygonShape.setAsBox(candy.dimension.x / 4.0f,
					candy.dimension.y / 4.0f, origin, 0);
			fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.friction = 0;
			body.createFixture(fixtureDef);
			body.setUserData(candy);
			polygonShape.dispose();
		}
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
		b2world.step(1/60f, 8, 3);
		// Move Camera
		cameraHelper.update(deltaTime);
		// Play Music
		musicPlayer.update(deltaTime);
		// Update Snow.
		snow.update(deltaTime);
		
		collectedCandies = 0;
		for (Candy candy : level.candies)
		{
			if (candy.collected)
			{
				collectedCandies++;
			}
			if (candy.justCollected)
			{
				musicPlayer.playPickup();
				candy.justCollected = false;
			}
		}
		
		if (collectedCandies == numberOfCandies)
		{
//			die inside?
			String time = uiController.getTimer().getTime();
		}
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
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.setScreen(new MenuScreen(game));
		if (Gdx.input.isKeyJustPressed(Keys.L))
			Constants.LSD_MODE = Constants.LSD_MODE ? false : true;
		// Test a mock encounter.
	}

	/**
	 * Moves the camera around if the camera doesn't have a target.
	 * @param deltaTime
	 */
	public void handleCameraMovement(float deltaTime)
	{
		if (cameraHelper.hasTarget()) return;

		float cameraMoveSpeed = 20 * deltaTime;
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

		// Not quite right.
		Vector2 moveVector = new Vector2();

		if (Gdx.input.isKeyPressed(Keys.W)) {
			moveVector.y = level.player.terminalVelocity.y;
			level.player.setTexture(level.player.back);
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			moveVector.y = -level.player.terminalVelocity.y;
			level.player.setTexture(level.player.front);
		}

		if (Gdx.input.isKeyPressed(Keys.A)) {
			moveVector.x = -level.player.terminalVelocity.x;
			level.player.setTexture(level.player.left);
		}else if (Gdx.input.isKeyPressed(Keys.D)) {
			moveVector.x = level.player.terminalVelocity.x;
			level.player.setTexture(level.player.right);
		}

		level.player.body.applyLinearImpulse(moveVector, level.player.position, true);

//		// Rotate the player?
//		if (Gdx.input.isKeyJustPressed(Keys.Q))
//		{
//			float rotation = level.player.body.getAngle() / 90;
//			if (level.player.body.getAngle() > rotation - 90)
//			{
//				level.player.body.setAngularVelocity(20);
//			} else
//			{
//				level.player.body.setAngularVelocity(0);
//			}
//		}
	}


	public void dispose()
	{
		level.dispose();
		musicPlayer.dispose();
		snow.dispose();
	}
}