package com.vgdc.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.vgdc.spooky.Assets;
import com.vgdc.utils.Constants;

public class SpookyBackground extends AbstractGameObject
{
	private TextureRegion regSpooky;

	private int length;
	private int height;

	public SpookyBackground (int length, int height)
	{
		this.length = length;
		this.height = height;
		init();
	}

	public void init()
	{
		dimension.set(4, 1);

		setAnimation (Assets.instance.spooky.animSpooky);
		//		stateTime = MathUtils.random(0.0f, 1.0f);
		regSpooky = Assets.instance.spooky.spooky;
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
		height += dimension.y * 2;
	}

	private void drawSpooky (SpriteBatch batch, float offsetX, float offsetY)
	{
		TextureRegion reg = null;
		reg = animation.getKeyFrame(stateTime, true);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		int spookyLength = 0;
		spookyLength += MathUtils.ceil(length / (2 * dimension.x));
		spookyLength += MathUtils.ceil(0.5f + offsetX);
		int spookyHeight = 0;
		spookyHeight += MathUtils.ceil(height / (2 * dimension.y));
		spookyHeight += MathUtils.ceil(0.5f + offsetY);
		for (int i = 0; i < spookyLength; i++)
		{
			for (int j = 0; j < spookyHeight; j++)
			{
				if (Constants.LSD_MODE)
				{
					float r = (float)Math.random();
					float g = (float)Math.random();
					float b = (float)Math.random();
					batch.setColor(r, g, b, 1);
				} else
				{
					batch.setColor(i/10.0f, j/10.0f, .5f, 1);
				}
				batch.draw(reg.getTexture(), origin.x + xRel, position.y + origin.y + yRel,
						origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
						reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
				yRel += dimension.y;
			}
			xRel += dimension.x;
			yRel = dimension.y * offsetY;
		}
		batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void render(SpriteBatch batch) {
		drawSpooky(batch, 0, 0);
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
