package com.vgdc.spooky;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.vgdc.objects.Candy;
import com.vgdc.objects.Player;

/**
 * Handles Physics, as the name suggests.
 * @author Evan S.
 *
 */
public class PhysicsHandler implements ContactListener {
	
	/**
	 * Sets a candy to collected if the player touches it. 
	 */
	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getBody().getUserData() instanceof Player)
		{
			if (contact.getFixtureB().getBody().getUserData() instanceof Candy)
			{
				Candy candy = (Candy)contact.getFixtureB().getBody().getUserData();
				candy.collected = true;
//				worldController
			}
		}
		if (contact.getFixtureB().getBody().getUserData() instanceof Player)
		{
			if (contact.getFixtureA().getBody().getUserData() instanceof Candy)
			{
				Candy candy = (Candy)contact.getFixtureB().getBody().getUserData();
				candy.collected = true;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
