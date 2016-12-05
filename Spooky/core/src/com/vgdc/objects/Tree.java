package com.vgdc.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vgdc.spooky.Assets;

public class Tree extends AbstractGameObject{

	// Texture Region of the tree asset
	private TextureRegion reg;

	public Tree() {
		init();
	}

	// Sets some stuff, finds reg.
	private void init() {
		dimension.set(2, 3);
		bounds.set(0,0,dimension.x,dimension.y);
		origin.set(dimension.x/2, dimension.y/2);
		reg = Assets.instance.tree.tree1;
	}

	@Override
	public void render(SpriteBatch batch) {
		boolean flipX = Math.random() > .9999 ? true : false;
		boolean flipY = Math.random() > .9999 ? true : false;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), flipX, flipY);
	}

	public float getWidth() {
		return reg.getRegionWidth();
	}

	public float getHeight() {
		return reg.getRegionHeight();
	}

}
