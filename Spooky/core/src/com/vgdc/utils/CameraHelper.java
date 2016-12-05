package com.vgdc.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.vgdc.objects.AbstractGameObject;

/**
 * Used basically as a surrogate controller for our camera.
 * Moves it about and such.
 * @author Evan S.
 *
 */
public class CameraHelper {
	// TAGs are used for debug logs.
	private static final String TAG = CameraHelper.class.getName();

	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;

	private Vector2 position;
	private float zoom;
	private AbstractGameObject target;

	public CameraHelper () {
		position = new Vector2();

		if (Constants.DEBUGGING_MAP) {
			// Centers camera on the Pixmap for debugging
			position = new Vector2(Constants.MAP_WIDTH/2, Constants.MAP_HEIGHT/2);
		}

		zoom = 1.0f;
	}

	/**
	 * Updates position and whatnot,
	 * clamping the camera to the map (hopefully) (eventually)
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		if (!hasTarget()) return;

		// Move to where the target is.
		position.x = target.position.x + target.origin.x;
		position.y = target.position.y + target.origin.y;

		// Clamp the camera's position to within the map's range.
		// (We don't want to be able to see anything outside the map.)
		position.x = MathUtils.clamp(position.x, Constants.VIEWPORT_WIDTH/2 + 0.3f, Constants.MAP_WIDTH - Constants.VIEWPORT_WIDTH /2 - 0.22f);
		position.y = MathUtils.clamp(position.y, Constants.VIEWPORT_HEIGHT/2 + 1, Constants.MAP_HEIGHT - Constants.VIEWPORT_HEIGHT / 2 + 1);
	}

	/**
	 * Sets the position.
	 * @param x, y : the coordinates
	 */
	public void setPosition (float x, float y) {
		this.position.set(x,y);
	}

	/**
	 * Returns the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Zooms in the camera (NOT USED)
	 * @param amount
	 */
	public void addZoom(float amount) {
		setZoom(zoom + amount);
	}

	/**
	 * Sets the zoom of the camera (NOT USED)
	 * @param zoom what factor to zoom in by
	 */
	public void setZoom(float zoom) {
		this.zoom = MathUtils.clamp(zoom,  MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}

	/**
	 * @return current zoom
	 */
	public float getZoom() {
		return zoom;
	}

	/**
	 * Sets the Target, the object that the camera follows.
	 * @param target : the object to follow
	 */
	public void setTarget (AbstractGameObject target) {
		this.target = target;
	}

	/**
	 * @return current target
	 */
	public AbstractGameObject getTarget() {
		return target;
	}

	/**
	 * @return True if we currently have a target
	 */
	public boolean hasTarget() {
		return target != null;
	}

	/**
	 * @param target : the object our target is being compared to.
	 * @return true if the current target is an instance of the
	 * object passed in.
	 */
	public boolean hasTarget(AbstractGameObject target) {
		return hasTarget() && this.target.equals(target);
	}

	/**
	 * Updates the passed-in camera's position and zoom,
	 * and updates it.
	 * @param camera
	 */
	public void applyTo(OrthographicCamera camera) {
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
}
