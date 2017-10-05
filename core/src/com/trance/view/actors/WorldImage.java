package com.trance.view.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.utils.FontUtil;

public class WorldImage extends Image {
	
	private FreeBitmapFont font;
	
	private PlayerDto playerDto;
	
	public WorldImage(Texture texture, PlayerDto dto) {
		super(texture);
		this.font = FontUtil.getFont();
		this.playerDto = dto;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(playerDto != null){
			font.draw(batch, playerDto.getPlayerName() +" [" + playerDto.getLevel() +"]" ,this.getX(),this.getY() + + getHeight());
		}
	}
	
	public PlayerDto getPlayerDto(){
		return playerDto;
	}

	public void setPlayerDto(PlayerDto playerDto) {
		this.playerDto = playerDto;
	}
	
}
