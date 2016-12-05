package com.vgdc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * Holds BitmapFonts for that
 * easy access.
 *
 * TODO move this to Assets.java
 * @author Evan S.
 *
 */
public class Fonts {
	public static final String TAG = Fonts.class.getName();

	public static final Fonts instance = new Fonts();

	public BitmapFont gamer;

	FreeTypeFontGenerator generator;

	public void Fonts()
	{
	}

	public void init()
	{
		generator = new FreeTypeFontGenerator(Gdx.files.internal("Gamer.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 24;
		param.borderColor = Color.BLACK;
		param.borderWidth = 2;
		gamer = generator.generateFont(param);
	}

}
