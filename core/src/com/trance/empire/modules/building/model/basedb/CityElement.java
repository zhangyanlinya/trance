package com.trance.empire.modules.building.model.basedb;

import com.trance.common.basedb.Basedb;


/**
 * 主城建筑
 * 
 * @author Along
 *
 */
public class CityElement implements Basedb {

	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * 开放等级
	 */
	private int openLevel;

    /**
     * 数量
     */
    private int amount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
