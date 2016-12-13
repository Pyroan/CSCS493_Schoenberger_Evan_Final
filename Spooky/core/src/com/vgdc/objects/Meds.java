package com.vgdc.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vgdc.spooky.Assets;

public class Meds extends Candy{

	// Texture Region of the tree asset
//	private TextureRegion reg;

	public Meds() {
		init();
	}

	// Sets some stuff, finds reg.
	private void init() {
		dimension.set(1,1);
		reg = Assets.instance.rock.rock;
		scale.set(.5f, .5f);
	}

//	@Override
//	public void render(SpriteBatch batch) {
//		batch.draw(reg.getTexture(), position.x, position.y,
//				origin.x, origin.y, dimension.x, dimension.y,
//				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
//				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
//	}

	public float getWidth() {
		return reg.getRegionWidth();
	}

	public float getHeight() {
		return reg.getRegionHeight();
	}

}
