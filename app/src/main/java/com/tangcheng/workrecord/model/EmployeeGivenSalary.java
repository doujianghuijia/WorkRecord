package com.tangcheng.workrecord.model;

/**
 * 职工发放薪水信息表
 * Created by tc on 2016/1/14.
 */
public class EmployeeGivenSalary {
    private int id;                         //ID
    private String employeeName;           //职工名称
    private String time;                    //发放时间
    private String remark;                  //备注
    private int price;                      //发放金额

    public EmployeeGivenSalary() {
    }

    public EmployeeGivenSalary(String employeeName, String time, String remark, int price) {
        this.employeeName = employeeName;
        this.time = time;
        this.remark = remark;
        this.price = price;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
