package com.tangcheng.workrecord.model;

/**
 * 职工日薪表
 * Created by tc on 2016/1/14.
 */
public class EmployeeDailySalary {
    private int id;                         //ID
    private String employeeName;           //职工名称
    private String startTime;               //该工资起始时间
    private String endTime;                 //该工资结束时间
    private int price;                      //日工资数额

    public EmployeeDailySalary() {
    }

    public EmployeeDailySalary(int price, String employeeName, String startTime, String endTime) {
        this.price = price;
        this.employeeName = employeeName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
