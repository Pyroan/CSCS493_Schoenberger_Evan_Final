package com.vgdc.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vgdc.spooky.Assets;

public class VerticalHouse extends AbstractGameObject{

	private TextureRegion reg;
	
	public VerticalHouse() {
		init();
	}
	
	private void init() {
		dimension.set(4,8);
		bounds.set(0, 0, dimension.x, dimension.y);
		origin.set(dimension.x/2, dimension.y/2);
		reg = Assets.instance.vHouse.vHouse;
	}
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}

	@Override
	public float getWidth() {
		return reg.getRegionWidth();
	}

	@Override
	public float getHeight() {
		return reg.getRegionHeight();
	}

}
