package com.trance.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.trance.empire.modules.player.model.Player;
import com.trance.event.BsuEvent;
import com.trance.view.TranceGame;

import var3d.net.freefont.desktop.DesktopFreeFont;

public class DesktopLauncher {
	/**
	 * @param arg
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		DesktopFreeFont.Strat();
		Player.userName ="eee";
		new LwjglApplication(new TranceGame(new BsuEvent() {
			@Override
			public void notify(int what, Object obj) {
				System.out.println(what);
			}
		},"en"), config);
	}
}
