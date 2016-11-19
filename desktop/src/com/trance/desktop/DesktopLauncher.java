package com.trance.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.trance.empire.modules.player.model.Player;
import com.trance.view.TranceGame;

import var3d.net.freefont.desktop.DesktopFreeFont;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		DesktopFreeFont.Strat();
		Player.userName ="ddd";
		new LwjglApplication(new TranceGame(), config);
	}
}
