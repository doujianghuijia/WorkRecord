package com.tangcheng.workrecord.model;

/**
 * 每日上工情况记录信息表
 * Created by tc on 2016/1/14.
 */
public class DailyRecordInfo {
    private int id;                         //ID
    private String employeeName;           //职工姓名
    private float timeLength;               //工作时长
    private String remark;                  //备注
    private String currentTime;             //上工日期
    private String workSite;                //上工地点

    public DailyRecordInfo() {
    }

    public DailyRecordInfo(String employeeName, float timeLength, String remark, String currentTime, String workSite) {
        this.employeeName = employeeName;
        this.timeLength = timeLength;
        this.remark = remark;
        this.currentTime = currentTime;
        this.workSite = workSite;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public float getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(float timeLength) {
        this.timeLength = timeLength;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getWorkSite() {
        return workSite;
    }

    public void setWorkSite(String workSite) {
        this.workSite = workSite;
    }
}
