package com.vgdc.spooky;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.vgdc.audio.MusicPlayer;
import com.vgdc.objects.AbstractGameObject;
import com.vgdc.objects.Candy;
import com.vgdc.objects.Floor;
import com.vgdc.objects.Meds;
import com.vgdc.objects.Player;
import com.vgdc.screens.MenuScreen;
import com.vgdc.screens.ScoreScreen;
import com.vgdc.ui.UIController;
import com.vgdc.utils.CameraHelper;
import com.vgdc.utils.Constants;
import com.vgdc.utils.GameTimer;

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

	private boolean debugging = true;

	LinkedList<String> levels = new LinkedList<String>();

	public WorldController() {
		init();
	}

	public WorldController (Game game) {
		this.game = game;

		// Doing this thing instead of something smart
		levels.add(Constants.LEVEL_01);
		levels.add(Constants.LEVEL_02);
		init();
		uiController = new UIController();
	}

	private void init() {
		dispose();
		cameraHelper = new CameraHelper();
		controller = new PlayerControls();
		musicPlayer = new MusicPlayer();
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

		String name = levels.poll();
		level = new Level(name);

		if (!Constants.DEBUGGING_MAP) cameraHelper.setTarget(level.player);
		numberOfCandies = 30;
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
			polygonShape.setAsBox(floor.dimension.x / 2.0f,
					floor.dimension.y / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.friction = .9f;
			body.createFixture(fixtureDef);
			body.setUserData("Floor");
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
		// Candies and Meds
		origin = new Vector2();
		for (Candy candy: level.candies)
		{
			makeCandyBox(candy);
		}
	}

	public void makeCandyBox(AbstractGameObject object)
	{
		BodyDef bodyDef = new BodyDef();
		Vector2 origin = new Vector2();
		Body body = b2world.createBody(bodyDef);
		PolygonShape polygonShape;
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(object.position);
		bodyDef.fixedRotation = false;
		body = b2world.createBody(bodyDef);
		object.body = body;
		polygonShape = new PolygonShape();
		origin.x = object.dimension.x /2.0f;
		origin.y = object.dimension.y / 2.0f;
		polygonShape.setAsBox(object.dimension.x / 4.0f,
				object.dimension.y / 4.0f, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.friction = 0;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef);
		body.setUserData(object);
		polygonShape.dispose();
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
			if (candy instanceof Meds)
			{
				if (candy.justCollected)
				{
					musicPlayer.playLSDPickup();
					handleLSDmode(deltaTime);
					candy.justCollected = false;
				}
			} else
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
		}

		if (collectedCandies == numberOfCandies)
		{
			nextLevel();
		}

		if (Constants.LSD_MODE)
			handleLSDmode(deltaTime);
	}

	/**
	 * Advances the game to the next level.
	 * If we're on the last level, takes us to the
	 * score screen.
	 */
	private void nextLevel()
	{
		//		die inside?
		int time = (int)uiController.getTimer().getRawTime();
		if (levels.peek() == null)
		{
			game.setScreen(new ScoreScreen(game, time));
		}
		else 
		{
			collectedCandies = 0;
			init();
		}
	}

	/**
	 * Makes Crazy Drug Mode Happen for 6 seconds
	 * @param deltaTime
	 */
	GameTimer timer = new GameTimer();
	private void handleLSDmode(float deltaTime)
	{
		float drugTime = 10.0f;
		if (Constants.LSD_MODE)
		{
			timer.update(deltaTime);

			// Can't end if player is in wall.
			if (level.player.wallsTouching == 0)
			{
				// If it's time for the effect to end...
				if (timer.getRawTime() > drugTime)
				{
					// re-enable wall collision
					for (Floor floor: level.tiles)
					{
						for(Fixture fixture: floor.body.getFixtureList())
						{
							fixture.setSensor(false);
						}
					}
					// Disable LSD mode
					Constants.LSD_MODE = false;

					cameraHelper.zoomGoal = 1.0f;
				}
			}
		} else
			// When drug mode first starts.
		{
			timer = new GameTimer();
			Constants.LSD_MODE = true;
			cameraHelper.zoomGoal = 1.5f;
			// disable wall collision
			for (Floor floor: level.tiles)
			{
				if (!floor.isBorder)
				{
					for(Fixture fixture: floor.body.getFixtureList())
					{
						fixture.setSensor(true);
					}
				}
			}
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
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
			game.setScreen(new MenuScreen(game));
		if (debugging)
		{
			if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE))
				if (!cameraHelper.hasTarget())
					cameraHelper.setTarget(level.player);
				else
					cameraHelper.setTarget(null);
			// Switch to the High Score screen.
			if (Gdx.input.isKeyJustPressed(Keys.Y))
				game.setScreen(new ScoreScreen(game));
			if (Gdx.input.isKeyJustPressed(Keys.L))
				handleLSDmode(deltaTime);
			if (Gdx.input.isKeyJustPressed(Keys.K))
				nextLevel();
			// Test a mock encounter.
		}
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
	}


	public void dispose()
	{
		if (level != null) level.dispose();
		if (musicPlayer != null) musicPlayer.dispose();
		if (snow != null) snow.dispose();
	}
}