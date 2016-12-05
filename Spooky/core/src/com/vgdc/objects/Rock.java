package com.vgdc.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vgdc.spooky.Assets;

public class Rock extends AbstractGameObject{

	// Texture Region of the tree asset
	private TextureRegion reg;

	public Rock() {
		init();
	}

	// Sets some stuff, finds reg.
	private void init() {
		dimension.set(2,2);
		reg = Assets.instance.rock.rock;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

	public float getWidth() {
		return reg.getRegionWidth();
	}

	public float getHeight() {
		return reg.getRegionHeight();
	}

}
