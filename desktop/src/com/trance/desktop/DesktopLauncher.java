package com.trance.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.trance.desktop.font.DesktopFreeFont;
import com.trance.empire.modules.player.model.Player;
import com.trance.event.BsuEvent;
import com.trance.view.TranceGame;


public class DesktopLauncher {
	/**
	 * @param arg
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 280;
		cfg.height = 480;
		// 全屏
	//cfg.fullscreen = true;
		// 垂直同步
	//	cfg.vSyncEnabled = true;

		DesktopFreeFont.Strat();
		Player.userName ="test10";
		LwjglApplication lwjgl= new LwjglApplication(new TranceGame(new BsuEvent() {
			@Override
			public void notify(int what, Object obj) {
				System.out.println(what);
			}
		},"en"), cfg);
		lwjgl.getGraphics().setDisplayMode(480,800, false);
	}
}
