package com.trance.empire.modules.reward.service;

import com.alibaba.fastjson.JSON;
import com.trance.empire.constant.RewardType;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.reward.model.SimpleReward;
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
				sb.append(MsgUtil.getInstance().getLocalMsg("gold") + " : ");
				break;
			case SILVER:
				long silver = player.getSilver();
				player.setSilver(silver + count);
				sb.append(MsgUtil.getInstance().getLocalMsg("silver") + ": ");
				break;
			case FOODS:
				long foods = player.getFoods();
				player.setFoods(foods + count);
				sb.append(MsgUtil.getInstance().getLocalMsg("foods") + " : ");
				break;
			case PLAYER_EXP:
				long experience = player.getExperience();
				player.setExperience(experience + count);
				sb.append(MsgUtil.getInstance().getLocalMsg("experience") + " : ");
			default:
				break;
			}
			if(count > 0){
				sb.append("+");
			}
			sb.append(count + " ");
		}
		
		MsgUtil.getInstance().showMsg(sb.toString());
		//TODO 
	}


	public static String getRewardMsg(String rewardString){
		if(rewardString == null || rewardString.equals("")){
			return null;
		}
		StringBuilder sb = new StringBuilder("");
		List<SimpleReward> results = JSON.parseArray(rewardString, SimpleReward.class);
		for(SimpleReward reward : results){
			int count = reward.getCount();
			RewardType type = reward.getType();
			switch(type){
				case GOLD:
					sb.append(MsgUtil.getInstance().getLocalMsg("gold") + " : ");
					break;
				case SILVER:
					sb.append(MsgUtil.getInstance().getLocalMsg("silver") + ": ");
					break;
				case FOODS:
					sb.append(MsgUtil.getInstance().getLocalMsg("foods") + " : ");
					break;
				case PLAYER_EXP:
					sb.append(MsgUtil.getInstance().getLocalMsg("experience") + " : ");
				default:
					break;
			}
			if(count > 0){
				sb.append("+");
			}
			sb.append(count + " ");
		}
		return sb.toString();
	}
}
