package com.nagarro.pos.dto;

public class ReportMessage {

	private String file;
	private boolean flag;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ReportMessage(String file, boolean flag) {
		super();
		this.file = file;
		this.flag = flag;
	}

}
