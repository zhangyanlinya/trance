package com.trance.empire.modules.reward.service;

import com.trance.empire.constant.RewardType;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.reward.result.RewardResult;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.view.utils.MsgUtil;

import java.util.List;

public class RewardService {
	
	
	public static void executeRewards(ValueResultSet valueResultSet){
		List<RewardResult<?>> list = valueResultSet.getResults();
		if(list == null || list.isEmpty()){
			return;
		}
		
		PlayerDto player = Player.player;
		StringBuilder sb = new StringBuilder("");
		for(RewardResult<?> rewardResult : list){
			int count = rewardResult.getActualCount();
			RewardType type = RewardType.valueOf(rewardResult.getType());
			switch(type){
			case GOLD:
				long gold = player.getGold();
				player.setGold(gold + count);
				sb.append("金币 : ");
				break;
			case SILVER:
				long silver = player.getSilver();
				player.setSilver(silver + count);
				sb.append("银币 : ");
				break;
			case FOODS:
				long foods = player.getFoods();
				player.setFoods(foods + count);
				sb.append("粮食 : ");
				break;
			case PLAYER_EXP:
				long exp = player.getExperience();
				player.setExperience(exp + count);
				sb.append("经验 : ");
			default:
				break;
			}
			if(count > 0){
				sb.append("+");
			}
			sb.append(count + " ");
		}
		
		MsgUtil.showMsg(sb.toString());
		//TODO 
	}
}
