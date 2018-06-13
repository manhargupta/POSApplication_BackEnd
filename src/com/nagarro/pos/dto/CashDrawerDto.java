package com.nagarro.pos.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nagarro.pos.model.CashDrawer;

public class CashDrawerDto {

	@JsonProperty("cashdrawers")
	Map<String, List<CashDrawer>> list;
	boolean status;

	public Map<String, List<CashDrawer>> getList() {
		return list;
	}

	public void setList(Map<String, List<CashDrawer>> list) {
		this.list = list;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public CashDrawerDto(Map<String, List<CashDrawer>> list, boolean status) {
		super();
		this.list = list;
		this.status = status;
	}

}
