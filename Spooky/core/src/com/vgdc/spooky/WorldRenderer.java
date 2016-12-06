package com.vgdc.spooky;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.vgdc.utils.Constants;

/**
 * Draws our world
 * @author Evan S.
 * also
 */
public class WorldRenderer implements Disposable {

	private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
	private Box2DDebugRenderer b2debugRenderer;

	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;


	public WorldRenderer(WorldController worldController) {
		super();
		this.worldController = worldController;
		init();
	}

	/*
	 *  Sets up our camera and our SpriteBatch
	 */
	private void init() {
		batch = new SpriteBatch();
//		camera = new OrthographicCamera (Gdx.graphics.getWidth() / Constants.PPM, Gdx.graphics.getHeight() / Constants.PPM);
	    camera = new OrthographicCamera (Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		b2debugRenderer = new Box2DDebugRenderer();
	}

	public void render() {
		renderWorld(batch);
		renderGui(batch);
	}

	/**
	 * Renders our GUI, including text boxes?
	 * @param batch
	 */
	private void renderGui(SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		worldController.snow.setPosition(0, Constants.VIEWPORT_GUI_HEIGHT);
		worldController.snow.draw(batch);
		worldController.uiController.render(batch);
		batch.end();
	}

	/**
	 * Renders the world, applying the cameraHelper to our world camera,
	 * and renders the level.
	 * @param batch
	 */
	private void renderWorld(SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		if (DEBUG_DRAW_BOX2D_WORLD)
		{
			b2debugRenderer.render(worldController.b2World, camera.combined);
		}
	}

	/**
	 * Triggered on window resize.
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		// Update the World camera.
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/ height) * width;

		camera.update();
		// Update the GUI camera.
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_HEIGHT / (float) height * (float) width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2,
				cameraGUI.viewportHeight /2, 0);
		cameraGUI.update();
	}


	/**
	 * Frees resources used in this class.
	 */
	@Override
	public void dispose() {
		batch.dispose();
	}

}
