package com.vgdc.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.vgdc.spooky.Spooky;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
/**
 * Abstraction of a game object that lets us move things,
 * and whatnot.
 * @author Evan S.
 *
 */
public abstract class AbstractGameObject {
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;

	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction; // It's more realistic if our player
	// doesn't just stop in their tracks.

	public Vector2 acceleration;
	public Rectangle bounds;

	public AbstractGameObject() {
		position = new Vector2();
		dimension = new Vector2(1,1);
		origin = new Vector2();
		scale = new Vector2(1,1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1,1);
		friction = new Vector2();
		acceleration = new Vector2();
		bounds = new Rectangle();

	}

	/**
	 * Updates motion along the X axis.
	 */
	protected void updateMotionX(float deltaTime) {
		if (velocity.x!=0) {
			// Apply friction
			if (velocity.x > 0)
				velocity.x = Math.max(velocity.x - friction.x*deltaTime, 0);
			else
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
		}
		// Apply acceleration
		velocity.x += acceleration.x * deltaTime;
		// Make sure the object's velocity does not exceed the
		// Positive or negative terminal velocity.
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
	}

	/**
	 * Updates motion along Y axis
	 */
	protected void updateMotionY (float deltaTime) {
		if (velocity.y!=0) {
			// Apply friction
			if (velocity.y > 0)
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
			else
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
		}
		// Apply acceleration
		velocity.y += acceleration.y * deltaTime;
		// Make sure the object's velocity does not exceed the
		// positive or negative terminal velocity
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
	}

	/**
	 * Updates position of object wrt delta time.
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		// Move to new position
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
	}

	public abstract void render (SpriteBatch batch);

	//Lis made this for box2d
	//collision bounds
//	public Body createBox(float x, float y, float width, float height, boolean isStatic){
//		Body pBody;
//
//		//physical properties of the body
//		BodyDef def = new BodyDef();
//		if(isStatic)
//			def.type = BodyType.StaticBody;
//		else
//			def.type = BodyType.DynamicBody;
//
//		def.position.set(x, y);
//		def.fixedRotation = true;
//		//places in the world
//		pBody = Spooky.b2dWorld.createBody(def);
//
//		PolygonShape shape = new PolygonShape();
//		//hit box i guess
//		shape.setAsBox (width, height);
//
//		pBody.createFixture(shape, 1.0f);
//		shape.dispose();
//
//		return pBody;
//	}

	public abstract float getWidth();
	public abstract float getHeight();
}
