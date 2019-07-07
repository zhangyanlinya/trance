package com.trance.empire.modules.replay.model;

import io.protostuff.Tag;

public class ReqReport {
	
	@Tag(1)
	private String reportId;

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
}
