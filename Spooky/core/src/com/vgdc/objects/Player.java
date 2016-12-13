package com.vgdc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vgdc.spooky.Assets;

/**
 * Our Player: A big black square.
 * @author Derek B., Evan S.
 *
 */
public class Player extends AbstractGameObject
{
	private Texture tex;
	private TextureRegion reg;
	
	public TextureRegion front;
	public TextureRegion back;
	public TextureRegion left;
	public TextureRegion right;
	
	public ParticleEffect cloud = new ParticleEffect();

	Pixmap pixmap = new Pixmap(200, 200, Format.RGBA8888);
	public Player()
	{

//		pixmap.setColor(0f,0f,0f,1f);
//		pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, 50);
//		tex = new Texture(pixmap);
		reg = new TextureRegion(Assets.instance.front.front1);
		
		front = new TextureRegion(Assets.instance.front.front1);
		back = new TextureRegion(Assets.instance.back.back1);
		left = new TextureRegion(Assets.instance.left.left1);
		right = new TextureRegion(Assets.instance.right.right1);
		
		// Make the particles work
		cloud.load(Gdx.files.internal("particles/cloud.prt"), Gdx.files.internal("particles"));
		cloud.scaleEffect(.1f);
		
		dimension.set(1,2);
//		bounds.set(origin.x,origin.y, dimension.x * .75f, dimension.y * .75f);
		origin.set(dimension.x/2, dimension.y/2);
		terminalVelocity.set(.5f, .5f);
		friction.set(12.0f, 12.0f);

	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		cloud.update(deltaTime);
		cloud.setPosition(position.x, position.y+.2f);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		cloud.draw(batch);
	}
	
	public void setTexture(TextureRegion reg) {
		this.reg = reg;
	}

	public float getWidth() {
		return tex.getWidth();
	}

	public float getHeight() {
		return tex.getHeight();
	}
}
