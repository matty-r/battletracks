package com.mattyr.battletracks.desktop;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mattyr.battletracks.BattleTracks;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		config.title = "Battle Tracks";
		config.width = (int) gd.getDefaultConfiguration().getBounds().getWidth();
		config.height = (int) gd.getDefaultConfiguration().getBounds().getHeight();
		config.fullscreen = true;
		new LwjglApplication(new BattleTracks(), config);
	}
}
