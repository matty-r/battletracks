package com.mattyr.battletracks.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mattyr.battletracks.BattleTracks;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Battle Tracks";
		config.width = 1920;
		config.height = 1080;
		config.fullscreen = true;
		new LwjglApplication(new BattleTracks(), config);
	}
}
