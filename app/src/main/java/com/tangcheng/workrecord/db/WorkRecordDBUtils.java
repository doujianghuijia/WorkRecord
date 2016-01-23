package com.tangcheng.workrecord.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tangcheng.workrecord.model.DailyRecordInfo;
import com.tangcheng.workrecord.model.EmployeeDailySalary;
import com.tangcheng.workrecord.model.EmployeeGivenSalary;
import com.tangcheng.workrecord.model.EmployeeInfo;
import com.tangcheng.workrecord.model.WorkSiteInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 数据库操作工具类
 * Created by tc on 2016/1/14.
 */
public class WorkRecordDBUtils {
    private static final String DATEBASE_NAME = "WorkRecordDB";     //数据库名称
    private static final int DATEBASE_VERSION = 1;                  //数据库版本
    private SQLiteDatabase db;                                         //数据库实例
    private static WorkRecordDBUtils workRecordDBUtils;                 //数据库工具类实例

    /**
     * 私有的构造方法，通过getInstance来实现实例的创建
     * @param context
     */
    private WorkRecordDBUtils(Context context) {
        WorkRecordDbOpenHelper workRecordDbOpenHelper = new WorkRecordDbOpenHelper(context,DATEBASE_NAME,null,DATEBASE_VERSION);
        db = workRecordDbOpenHelper.getWritableDatabase();
    }

    /**
     * 同步锁实现数据库操作正常，外界调用使用此接口来创建工具类实例
     * @param context
     * @return
     */
    public synchronized static WorkRecordDBUtils getInstance(Context context){
        if(workRecordDBUtils == null){
            workRecordDBUtils = new WorkRecordDBUtils(context);
        }
        return workRecordDBUtils;
    }

    public void saveEmployeeInfo(EmployeeInfo employeeInfo){
        ContentValues values = new ContentValues();
        values.put("name",employeeInfo.getName());
        values.put("phoneNumber",employeeInfo.getPhoneNumber());
        values.put("isHired",employeeInfo.getIsHired());
        values.put("totalWorkTime",employeeInfo.getTotalWorkTime());
        values.put("totalSalary",employeeInfo.getTotalSalary());
        values.put("restSalary",employeeInfo.getRestSalary());
        values.put("hasDone",employeeInfo.getHasDone());
        values.put("startTime",employeeInfo.getStartTime());
        values.put("endTime",employeeInfo.getEndTime());
        db.insert("employeeinfo",null,values);
    }

    public void saveWorkSiteInfo(WorkSiteInfo workSiteInfo){
        ContentValues values = new ContentValues();
        values.put("name",workSiteInfo.getName());
        values.put("remark",workSiteInfo.getRemark());
        values.put("isBuilded",workSiteInfo.getIsBuilded());
        values.put("startTime",workSiteInfo.getStartTime());
        values.put("endTime",workSiteInfo.getEndTime());
        db.insert("worksiteinfo", null, values);
    }


    public void saveDailyRecordInfo(DailyRecordInfo dailyRecordInfo){
        ContentValues values = new ContentValues();
        values.put("employeeName",dailyRecordInfo.getEmployeeName());
        values.put("timeLength",dailyRecordInfo.getTimeLength());
        values.put("remark",dailyRecordInfo.getRemark());
        values.put("currentTime",dailyRecordInfo.getCurrentTime());
        values.put("workSite", dailyRecordInfo.getWorkSite());
        db.insert("dailyrecordinfo", null, values);
    }

    public void saveEmployeeDailySalary(EmployeeDailySalary employeeDailySalary){
        String isFirstRecordSQL = "select * from employeedailysalary where employeeName=?";
        Cursor cursor = db.rawQuery(isFirstRecordSQL,new String[]{employeeDailySalary.getEmployeeName()});
        if(cursor != null){
            String currentStartTime = employeeDailySalary.getStartTime();
            long oneDay = 1000*24*60*60;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(currentStartTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String beforeDay = sdf.format(new Date(date.getTime()-oneDay));
            String updateFormerDailySalaryEndTimeSQL = "update employeedailysalary set endTime=? where endTime=0 and employeeName=?";
            db.execSQL(updateFormerDailySalaryEndTimeSQL, new String[]{beforeDay,employeeDailySalary.getEmployeeName()});
        }
        ContentValues values = new ContentValues();
        values.put("employeeName",employeeDailySalary.getEmployeeName());
        values.put("startTime",employeeDailySalary.getStartTime());
        values.put("endTime","0");
        values.put("price",employeeDailySalary.getPrice());
        db.insert("employeedailysalary", null, values);
    }

    public void saveEmployeeGivenSalary(EmployeeGivenSalary employeeGivenSalary){
        ContentValues values = new ContentValues();
        values.put("employeeName",employeeGivenSalary.getEmployeeName());
        values.put("time",employeeGivenSalary.getTime());
        values.put("remark",employeeGivenSalary.getRemark());
        values.put("price",employeeGivenSalary.getPrice());
        db.insert("employeegivensalary", null, values);
    }

    public List<String> getEmployeeNameList(String isHired){
        List<String> employeeNameList = new ArrayList<String>();
        String getEmployeeNameListSQL = "select t.name from employeeInfo t where t.isHired=? order by t.id";
        Cursor cursor = db.rawQuery(getEmployeeNameListSQL,new String[]{isHired});
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    employeeNameList.add(name);
                }while(cursor.moveToNext());
            }
            return employeeNameList;
        }else{
            return null;
        }
    }

    /**
     * 根据员工名得到该员工的基础信息
     * @param name
     * @return
     */
    public EmployeeInfo getEmployeeInfoByName(String name){
        EmployeeInfo employeeInfo = new EmployeeInfo();
        String getEmployeeInfoByNameSQL = "select * from employeeInfo where name=?";
        Cursor cursor = db.rawQuery(getEmployeeInfoByNameSQL, new String[]{name});
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    employeeInfo.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
                    employeeInfo.setHasDone(cursor.getInt(cursor.getColumnIndex("hasDone")));
                    employeeInfo.setIsHired(cursor.getInt(cursor.getColumnIndex("isHired")));
                    employeeInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
                    employeeInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));
                    employeeInfo.setRestSalary(cursor.getFloat(cursor.getColumnIndex("restSalary")));
                    employeeInfo.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
                    employeeInfo.setTotalSalary(cursor.getFloat(cursor.getColumnIndex("totalSalary")));
                    employeeInfo.setTotalWorkTime(cursor.getFloat(cursor.getColumnIndex("totalWorkTime")));
                }while(cursor.moveToNext());
            }
            return employeeInfo;
        }else{
            return null;
        }
    }

    /**
     * 删除员工
     * @param employeeName
     */
    public void setEmployeeInfoNotHired(String employeeName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        String date = sdf.format(currentDate);
        String setEmployeeInfoNotHiredSQL = "update employeeInfo set isHired=1,endTime=? where name=?";
        db.execSQL(setEmployeeInfoNotHiredSQL, new String[]{date,employeeName});
    }

    /**
     * 重新雇佣员工
     * @param employeeName
     */
    public void reHiredEmployee(String employeeName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        String date = sdf.format(currentDate);
        String reHiredEmployeeSQL = "update employeeInfo set isHired=0,startTime=?,totalSalary=0,totalWorkTime=0,restSalary=0,hasDone=0 where name=?";
        db.execSQL(reHiredEmployeeSQL,new String[]{date,employeeName});
    }

    /**
     * 根据是否在建得到工地列表
     * @param isBuilded
     * @return
     */
    public List<String> getWorkSiteNameList(String isBuilded){
        List<String> workSiteNameList = new ArrayList<String>();
        String getWorkSiteNameListSQL = "select name from worksiteinfo where isBuilded=? order by id";
        Cursor cursor = db.rawQuery(getWorkSiteNameListSQL, new String[]{isBuilded});
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String workSiteName = cursor.getString(cursor.getColumnIndex("name"));
                    workSiteNameList.add(workSiteName);
                }while(cursor.moveToNext());
            }
            return workSiteNameList;
        }else{
            return null;
        }
    }

    /**
     * 根据工地名得到工地详细信息
     * @param worksiteName
     * @return
     */
    public WorkSiteInfo getWorkSiteInfoByName(String worksiteName){
        WorkSiteInfo workSiteInfo = new WorkSiteInfo();
        String getWorkSiteInfoByNameSQL = "select * from worksiteinfo where name=?";
        Cursor cursor = db.rawQuery(getWorkSiteInfoByNameSQL,new String[]{worksiteName});
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    workSiteInfo.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
                    workSiteInfo.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    workSiteInfo.setIsBuilded(cursor.getInt(cursor.getColumnIndex("isBuilded")));
                    workSiteInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
                    workSiteInfo.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
                }while(cursor.moveToNext());
            }
            return workSiteInfo;
        }else{
            return null;
        }
    }

    /**
     * 更新工地的备注
     * @param worksiteName
     * @param remark
     */
    public void updateWorkSiteRemark(String worksiteName,String remark){
        String updateWorkSiteRemarkSQL = "update worksiteinfo set remark=? where name=?";
        db.execSQL(updateWorkSiteRemarkSQL,new String[]{remark,worksiteName});
    }

    /**
     * 标记工地完工
     * @param worksiteName
     */
    public void setWorkSiteIsBuilded(String worksiteName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM--dd");
        Date currentDate = new Date(System.currentTimeMillis());
        String date = sdf.format(currentDate);
        String setWorkSiteIsBuildedSQL = "update worksiteinfo set isBuilded=1,endTime=? where name=?";
        db.execSQL(setWorkSiteIsBuildedSQL,new String[]{date,worksiteName});
    }

    /**
     * 得到已经完成上工记录的员工列表
     * @return
     */
    public List<String> getHasRecordedEmployeeList(){
        List<String> hasRecordedEmployeeList = new ArrayList<String>();
        String hasRecordedEmployeeName;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        String date = sdf.format(currentDate);
        String getHasRecordedEmployeeListSQL = "select employeeName from dailyrecordinfo where currentTime=? order by id";
        Cursor cursor = db.rawQuery(getHasRecordedEmployeeListSQL,new String[]{date});
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    hasRecordedEmployeeName = cursor.getString(cursor.getColumnIndex("employeeName"));
                    hasRecordedEmployeeList.add(hasRecordedEmployeeName);
                }while(cursor.moveToNext());
            }
            return hasRecordedEmployeeList;
        }else{
            return null;
        }
    }

    /**
     * 根据日期和员工名查询当天的上工情况
     * @param name
     * @param date
     * @return
     */
    public DailyRecordInfo getDailyRecordInfoByNameAndDate(String name,String date){
        DailyRecordInfo dailyRecordInfo = new DailyRecordInfo();
        String getDailyRecordInfoByNameAndDateSQL = "select * from dailyrecordinfo where currentTime=? and employeeName=?";
        Cursor cursor = db.rawQuery(getDailyRecordInfoByNameAndDateSQL, new String[]{date, name});
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    dailyRecordInfo.setEmployeeName(cursor.getString(cursor.getColumnIndex("employeeName")));
                    dailyRecordInfo.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    dailyRecordInfo.setCurrentTime(cursor.getString(cursor.getColumnIndex("currentTime")));
                    dailyRecordInfo.setTimeLength(cursor.getFloat(cursor.getColumnIndex("timeLength")));
                    dailyRecordInfo.setWorkSite(cursor.getString(cursor.getColumnIndex("workSite")));
                }while(cursor.moveToNext());
            }
            return dailyRecordInfo;
        }else{
            return null;
        }
    }

    /**
     * 根据日期和员工更新其上工情况
     * @param name
     * @param worksite
     * @param remark
     * @param timelength
     */
    public void updateDailyRecord(String name,String worksite,String remark,String timelength){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        String date = sdf.format(currentDate);
        String updateDailyRecordSQL = "update dailyrecordinfo set workSite=?,remark=?,timeLength=? where employeeName=? and currentTime=?";
        db.execSQL(updateDailyRecordSQL,new String[]{worksite,remark,timelength,name,date});
    }

    /**
     * 更新员工表中总工时
     * @param name
     * @param worktime
     * @param backWorkTime   代表需要去除的工时，这里模拟的是当更新上工表时需要先减去之前加上的工时
     */
    public void updateTotalWorkTime(String name,float worktime,float backWorkTime){
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo = getEmployeeInfoByName(name);
        float totalWorkTime = employeeInfo.getTotalWorkTime();
        totalWorkTime = totalWorkTime-backWorkTime+worktime;
        String updateTotalWorkTimeSQL = "update employeeInfo set totalWorkTime=? where name=?";
        db.execSQL(updateTotalWorkTimeSQL,new String[]{totalWorkTime+"",name});
    }

    /**
     * 得到数据库中存储的某位员工的日薪记录，并组织成固定格式的字符串返回，便于listview的使用
     * @param name
     * @return
     */
    public List<String> getEmployeeDailySalaryInfoStrByName(String name){
        List<String> list = new ArrayList<String>();
        String getEmployeeDailySalaryByName = "select * from employeedailysalary where employeeName=? order by startTime";
        String startTime,endTime;
        int price;
        Cursor cursor = db.rawQuery(getEmployeeDailySalaryByName,new String[]{name});
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                    endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                    if(endTime.equals("0")){
                        endTime = "至今";
                    }
                    price = cursor.getInt(cursor.getColumnIndex("price"));
                    list.add("时间："+startTime+"--"+endTime+" 日薪:"+price+"/天");
                }while(cursor.moveToNext());
            }
            return list;
        }else{
            return null;
        }
    }

    /**
     * 根据员工名统计得到指定时间段内工时数目
     * @param employeeName
     * @param startTime
     * @param endTime
     * @return
     */
    public float getTotalWorkTimeDuringSpecificTime(String employeeName,String startTime,String endTime){
        float timeLengthSum = 0;
        String getTotalWorkTimeDuringSpecificTimeSQL = "select sum(timeLength) from dailyrecordinfo where employeeName=? and currentTime>=? and currentTime<=?";
        Cursor cursor = db.rawQuery(getTotalWorkTimeDuringSpecificTimeSQL, new String[]{employeeName, startTime, endTime});
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    timeLengthSum = cursor.getFloat(cursor.getColumnIndex("sum(timeLength)"));
                }while(cursor.moveToNext());
            }
        }
        return timeLengthSum;
    }

    /**
     * 根据员工姓名获得其日薪信息
     * @param name
     * @return
     */
    public List<EmployeeDailySalary> getEmployeeDailySalaryByName(String name){
        List<EmployeeDailySalary> list = new ArrayList<EmployeeDailySalary>();
        String getEmployeeDailySalaryByNameSQL = "select * from employeedailysalary where employeeName=? order by startTime";
        Cursor cursor = db.rawQuery(getEmployeeDailySalaryByNameSQL,new String[]{name});
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    EmployeeDailySalary employeeDailySalary = new EmployeeDailySalary();
                    employeeDailySalary.setEmployeeName(cursor.getString(cursor.getColumnIndex("employeeName")));
                    employeeDailySalary.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
                    employeeDailySalary.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
                    employeeDailySalary.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
                    list.add(employeeDailySalary);
                }while(cursor.moveToNext());
            }
            return list;
        }else{
            return null;
        }
    }

    /**
     * 删除日薪记录
     * @param employeeDailySalary
     */
    public void deleteDailySalary(EmployeeDailySalary employeeDailySalary){
        db.execSQL("delete from employeedailysalary where employeeName=? and startTime=?",new String[]{employeeDailySalary.getEmployeeName(),employeeDailySalary.getStartTime()});
    }

    /**
     * 通过日薪开始时间和用户名得到该条日薪记录
     * @param name
     * @param startTime
     * @return
     */
    public EmployeeDailySalary getEmployeeDailySalary(String name,String startTime){
        EmployeeDailySalary employeeDailySalary = new EmployeeDailySalary();
        String getEmployeeDailySalarySQL = "select * from employeedailysalary where employeeName=? and startTime=?";
        Cursor cursor = db.rawQuery(getEmployeeDailySalarySQL,new String[]{name,startTime});
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    employeeDailySalary.setEmployeeName(cursor.getString(cursor.getColumnIndex("employeeName")));
                    employeeDailySalary.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
                    employeeDailySalary.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
                    employeeDailySalary.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
                }while(cursor.moveToNext());
            }
            return employeeDailySalary;
        }else{
            return null;
        }
    }

    /**
     * 对员工信息的姓名和电话进行更新，这里注意要对其他含有员工名称的表进行相应的更新（利用外键是否更好？）
     * @param name
     * @param phone
     * @param newName
     */
    public void updateEmployeeInfoNameAndPhone(String name,String phone,String newName){
        String updateEmployeeInfoNameAndPhoneSQL = "update employeeinfo set name=?,phoneNumber=? where name=?";
        db.execSQL(updateEmployeeInfoNameAndPhoneSQL,new String[]{newName,phone,name});
        String updateDailyRecordNameSQL = "update dailyrecordinfo set employeeName=? where employeeName=?";
        db.execSQL(updateDailyRecordNameSQL,new String[]{newName,name});
        String updateEmployeeDailySalaryNameSQL = "update employeedailysalary set employeeName=? where employeeName=?";
        db.execSQL(updateEmployeeDailySalaryNameSQL,new String[]{newName,name});
        String updateEmployeeGivenSalaryNameSQL = "update employeegivensalary set employeeName=? where employeeName=?";
        db.execSQL(updateEmployeeDailySalaryNameSQL,new String[]{newName,name});
    }


    /**
     * 删除发放薪资记录
     * @param name
     * @param time
     */
    public void deleteGivenSalary(String name,String time){
        String deleteGivenSalarySQL = "delete from employeegivensalary where employeeName=? and time=?";
        db.execSQL(deleteGivenSalarySQL,new String[]{name,time});
    }

    /**
     * 根据员工名得到其已经发放薪资记录
     * @param employeeName
     * @return
     */
    public List<String> getGivenSalaryByEmployeeName(String employeeName){
        List<String> givenSalaryList = new ArrayList<String>();
        String getGivenSalaryByEmployeeNameSQL = "select * from employeegivensalary where employeeName=?";
        Cursor cursor = db.rawQuery(getGivenSalaryByEmployeeNameSQL,new String[]{employeeName});
        String time,remark,price;
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    time = cursor.getString(cursor.getColumnIndex("time"));
                    remark = cursor.getString(cursor.getColumnIndex("remark"));
                    price = cursor.getString(cursor.getColumnIndex("price"));
                    givenSalaryList.add(time+":  "+price+"元   "+remark);
                }while(cursor.moveToNext());
            }
            return givenSalaryList;
        }else{
            return null;
        }
    }

    public int getTotalGivenSalary(String name){
        int totalGivenSalary = 0;
        String getTotalGivenSalarySQL = "select sum(price) from employeegivensalary where employeeName=?";
        Cursor cursor= db.rawQuery(getTotalGivenSalarySQL, new String[]{name});
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    totalGivenSalary = cursor.getInt(cursor.getColumnIndex("sum(price)"));
                }while(cursor.moveToNext());
            }

        }
        return totalGivenSalary;
    }
}
