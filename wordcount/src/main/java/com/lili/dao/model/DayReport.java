package com.lili.dao.model;

/**
 * Created by lili on 2015/3/9.
 */
public class DayReport {

	//----查询需要的字段
	private String fromDate;
	private String toDate;
	private String  areaIds;

	/*服务器ID*/
	private Integer areaId;
	/*举报操作次数*/
	private Integer complaintCount;
	/*投诉操作角色GUID数*/
	private Integer playerCount;
	//被投诉角色GUID数
	private Integer destCount;
	private String  reportDate;


	@Override
	public String toString() {
		return "DayReport{" +
				"areaId=" + areaId +
				", fromDate='" + fromDate + '\'' +
				", toDate='" + toDate + '\'' +
				", complaintCount=" + complaintCount +
				", playerCount=" + playerCount +
				", destCount=" + destCount +
				", reportDate='" + reportDate + '\'' +
				'}';
	}

	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getComplaintCount() {
		return complaintCount;
	}

	public void setComplaintCount(Integer complaintCount) {
		this.complaintCount = complaintCount;
	}

	public Integer getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(Integer playerCount) {
		this.playerCount = playerCount;
	}

	public Integer getDestCount() {
		return destCount;
	}

	public void setDestCount(Integer destCount) {
		this.destCount = destCount;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
}
