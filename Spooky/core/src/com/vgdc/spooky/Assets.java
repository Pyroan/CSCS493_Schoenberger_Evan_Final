package com.vgdc.spooky;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.vgdc.utils.Constants;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

/**
 * Contains references to all of our assets.
 * @author Evan S.
 *
 */
public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;

	private Assets()
	{
	}

	public AssetBush bush;
	public AssetTiles snowTiles;
	public AssetTree tree;
	public AssetRock rock;
	public AssetAbe front;
	public AssetAbeBack back;
	public AssetAbeLeft left;
	public AssetAbeRight right;
	public AssetCandy candy;
	public VerticalHouse vHouse;
	public HorizontalHouse hHouse;
	public AssetSpooky spooky;
	

	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);

		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
				TextureAtlas.class);

		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG,  "# of assets loaded: "
			+ assetManager.getAssetNames().size);
		for (String a: assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		TextureAtlas atlas =
				assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// Enable texture filtering for pixel smoothing.
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		// create game resource objects
		bush = new AssetBush(atlas);
		snowTiles = new AssetTiles(atlas);
		tree = new AssetTree(atlas);
		rock = new AssetRock(atlas);
		front = new AssetAbe(atlas);
		back = new AssetAbeBack(atlas);
		left = new AssetAbeLeft(atlas);
		right = new AssetAbeRight(atlas);
		candy = new AssetCandy(atlas);
		vHouse = new VerticalHouse(atlas);
		hHouse = new HorizontalHouse(atlas);
		spooky = new AssetSpooky(atlas);
		
	}
	@Override
	public void dispose() {
		assetManager.dispose();
	}


	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Wouldn't load asset '"
				+ asset.fileName + "'", (Exception) throwable);
	}

	public class AssetTiles
	{

		public final AtlasRegion tiles1;
		public AssetTiles(TextureAtlas atlas)
		{
			tiles1 = atlas.findRegion("snow - Copy");
		}
	}


	public class AssetTree
	{

		public final AtlasRegion tree1;

		public AssetTree(TextureAtlas atlas)
		{
			tree1 = atlas.findRegion("Big Tree");
		}

	}

	public class AssetRock
	{
		public final AtlasRegion rock;
		public AssetRock(TextureAtlas atlas)
		{
			rock = atlas.findRegion("meds");
		}
	}

	public class AssetBush
	{

		public final AtlasRegion bush1;
		public AssetBush(TextureAtlas atlas)
		{
			bush1 = atlas.findRegion("bush");
		}
	}
	
	public class AssetAbe
	{

		public final AtlasRegion front1;
		public AssetAbe(TextureAtlas atlas)
		{
			front1 = atlas.findRegion("abe");
		}
	}
	public class AssetAbeBack
	{

		public final AtlasRegion back1;
		public AssetAbeBack(TextureAtlas atlas)
		{
			back1 = atlas.findRegion("abe-back");
		}
	}
	public class AssetAbeLeft
	{

		public final AtlasRegion left1;
		public AssetAbeLeft(TextureAtlas atlas)
		{
			left1 = atlas.findRegion("abe-left");
		}
	}
	public class AssetAbeRight
	{

		public final AtlasRegion right1;
		public AssetAbeRight(TextureAtlas atlas)
		{
			right1 = atlas.findRegion("abe-right");
		}
	}
	
	public class AssetCandy
	{
		public final AtlasRegion candy;
		public AssetCandy(TextureAtlas atlas)
		{
			candy = atlas.findRegion("candy");
		}
	}
	
	public class VerticalHouse
	{
		public final AtlasRegion vHouse;
		public VerticalHouse(TextureAtlas atlas)
		{
			vHouse = atlas.findRegion("vertical-house");
		}
	}
	
	public class HorizontalHouse
	{
		public final AtlasRegion hHouse;
		public HorizontalHouse(TextureAtlas atlas)
		{
			hHouse = atlas.findRegion("horizontal-house");
		}
	}
	
	public class AssetSpooky
	{
		public final AtlasRegion spooky;
		public final Animation animSpooky;
		
		public AssetSpooky (TextureAtlas atlas)
		{
			spooky = atlas.findRegion("spooky", 1);
			
			// Animation
			Array<AtlasRegion> regions =
					atlas.findRegions("spooky");
			AtlasRegion region = regions.first();
			for (int i = 0; i < 10; i++)
				regions.insert(0,  region);
			animSpooky = new Animation(1.0f/3.0f, regions, Animation.PlayMode.LOOP_PINGPONG);
		}
	}

}

