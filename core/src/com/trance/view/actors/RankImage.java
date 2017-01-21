package com.trance.view.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trance.empire.modules.player.model.PlayerDto;

public class RankImage extends Image {

	private BitmapFont font;

	private PlayerDto playerDto;

	public RankImage(Texture texture, BitmapFont font, PlayerDto dto) {
		super(texture);
		this.font = font;
		this.playerDto = dto;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(playerDto != null){
			String name = playerDto.getPlayerName() + "           ";
			font.draw(batch, name ,this.getX() + getWidth(),this.getY() + getHeight()/2);
			font.draw(batch, playerDto.getLevel()+"" ,this.getX() + getWidth() * 2 + getWidth()/2, this.getY() + getHeight()/2);
		}
	}
	
	public PlayerDto getPlayerDto(){
		return playerDto;
	}

	public void setPlayerDto(PlayerDto playerDto) {
		this.playerDto = playerDto;
	}
	
}
