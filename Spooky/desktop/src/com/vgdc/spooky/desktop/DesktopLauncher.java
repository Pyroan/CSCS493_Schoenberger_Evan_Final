package com.vgdc.spooky.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.vgdc.spooky.Spooky;

public class DesktopLauncher {
	// Builds the Sprite Sheet (Texture Atlas)
	private static boolean rebuildAtlas  = false;
	private static boolean rebuildMenuAtlas = false;
	private static boolean drawDebugOutline = false;
	public static void main (String[] arg) {
		if(rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "../core/assets/tiles", "../core/assets/tiles", "tiles");
		}
		if (rebuildMenuAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 2048;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "../core/assets/ui", "../core/assets/ui", "uiskin");
		}
	// Sets up the whole shebang (i.e. the game/window).
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		new LwjglApplication(new Spooky(), config);
	}
}
