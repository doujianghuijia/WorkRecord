package com.tangcheng.workrecord.model;

/**
 * 施工地点基础信息表
 * Created by tc on 2016/1/14.
 */
public class WorkSiteInfo {
    private int id;                     //ID
    private String name;                //工地名称
    private String remark;              //备注
    private int isBuilded;          //是否完工（0-否  1-是）
    private String startTime;           //工地开始日期
    private String endTime;             //工地完工日期

    public WorkSiteInfo() {
    }

    public WorkSiteInfo(String name, String remark, int isBuilded, String startTime, String endTime) {
        this.name = name;
        this.remark = remark;
        this.isBuilded = isBuilded;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsBuilded() {
        return isBuilded;
    }

    public void setIsBuilded(int isBuilded) {
        this.isBuilded = isBuilded;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
