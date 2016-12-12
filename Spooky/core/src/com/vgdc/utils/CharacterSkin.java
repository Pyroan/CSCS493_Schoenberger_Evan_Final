package com.vgdc.utils;

import com.badlogic.gdx.graphics.Color;

public enum CharacterSkin {
	WHITE ("White", 1.0f, 1.0f, 1.0f),
	RED ("Red", 1.0f, .2f, .2f),
	BLUE ("Blue", 1.0f, .2f, .2f);
	
	private String name;
	private Color color = new Color();
	
	private CharacterSkin(String name, float r, float g, float b)
	{
		this.name = name;
		color.set(r,g,b,1.0f);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public Color getColor()
	{
		return color;
	}
}
