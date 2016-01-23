package com.tangcheng.workrecord.model;

import java.io.Serializable;

/**
 * 职工基础信息表
 * Created by tc on 2016/1/14.
 */
public class EmployeeInfo implements Serializable{
    private int id;                      //ID
    private String name;                 //职工姓名
    private String phoneNumber;         //职工联系电话
    private int isHired;            //职工是否在职（0-在职  1-离职）
    private float totalWorkTime;        //职工总工作时长
    private float totalSalary;          //职工总薪资
    private float restSalary;           //职工剩余薪资
    private int hasDone;            //职工薪资是否发放完毕（0-否 1-是）
    private String startTime;           //职工入职日期
    private String endTime;             //职工离职日期

    public EmployeeInfo() {
    }

    public EmployeeInfo(String name, String phoneNumber, int isHired, float totalWorkTime, float totalSalary, float restSalary, int hasDone, String startTime, String endTime) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isHired = isHired;
        this.totalWorkTime = totalWorkTime;
        this.totalSalary = totalSalary;
        this.restSalary = restSalary;
        this.hasDone = hasDone;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIsHired() {
        return isHired;
    }

    public void setIsHired(int isHired) {
        this.isHired = isHired;
    }

    public int getHasDone() {
        return hasDone;
    }

    public void setHasDone(int hasDone) {
        this.hasDone = hasDone;
    }

    public float getTotalWorkTime() {
        return totalWorkTime;
    }

    public void setTotalWorkTime(float totalWorkTime) {
        this.totalWorkTime = totalWorkTime;
    }

    public float getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(float totalSalary) {
        this.totalSalary = totalSalary;
    }

    public float getRestSalary() {
        return restSalary;
    }

    public void setRestSalary(float restSalary) {
        this.restSalary = restSalary;
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
