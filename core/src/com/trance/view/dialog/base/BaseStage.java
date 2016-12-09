package com.trance.view.dialog.base;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.trance.view.TranceGame;

public abstract class BaseStage extends Stage {
	
	private TranceGame tranceGame;
	
	private boolean visible;
	
	public BaseStage(TranceGame tranceGame) {
		this.tranceGame = tranceGame;
	}

	public TranceGame getTranceGame() {
		return tranceGame;
	}

	public void setTranceGame(TranceGame tranceGame) {
		this.tranceGame = tranceGame;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public abstract void show();

	public void hide(){
		this.visible = false;
	}

}
