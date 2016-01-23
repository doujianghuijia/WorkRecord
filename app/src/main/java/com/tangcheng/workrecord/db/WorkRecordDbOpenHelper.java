package com.tangcheng.workrecord.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tc on 2016/1/13.
 */
public class WorkRecordDbOpenHelper extends SQLiteOpenHelper {
    //职工基础信息表
    public static final String CREATE_EMPLOYEEINFO = "create table employeeinfo(" +
            "id integer primary key autoincrement," +           //主键id
            "name text," +                                        //职工姓名
            "phoneNumber text," +                                 //职工电话
            "isHired integer," +                                  //职工是否在职（0-在职  1-离职）
            "totalWorkTime real," +                                //职工当前总共时
            "totalSalary real," +                                  //职工当前总薪资
            "restSalary real," +                                  //职工剩余总薪资
            "hasDone integer," +                                  //是否发放工资完毕（0-没有  1-发放完毕）
             "startTime text," +                                  //入职时间
             "endTime text)";                                    //离职时间

    //施工地点基础信息表
    public static final String CREATE_WORKSITEINFO = "create table worksiteinfo(" +
            "id integer primary key autoincrement," +           //主键id
            "name text," +                                        //工地名称
            "remark text," +                                        //工地备注
            "isBuilded integer," +                                //工地是否在建（0-在建  1-不在建）
            "startTime text," +                                  //开工时间
            "endTime text)";                                    //完工时间

    //每天工作记录详情信息表
    public static final String CREATE_DAILYRECORDINFO = "create table dailyrecordinfo(" +
            "id integer primary key autoincrement," +           //主键id
            "employeeName text," +                               //职工姓名
            "timeLength real," +                                     //工作时长
            "remark text," +                                        //备注
            "currentTime text," +                                  //当日日期
            "workSite text)";                                    //施工地点

    //职工日薪信息表
    public static final String CREATE_EMPLOYEEDAILYSALARY = "create table employeedailysalary(" +
            "id integer primary key autoincrement," +           //主键id
            "employeeName text," +                               //职工姓名
            "startTime text," +                                     //该工资的起止时间
            "endTime text," +                                       //该工资的结束时间
            "price integer)";                                         //职工的日工资

    //职工发放薪水信息表
    public static final String CREATE_EMPLOYEEGIVENSALARY = "create table employeegivensalary(" +
            "id integer primary key autoincrement," +           //主键id
            "employeeName text," +                               //职工姓名
            "time text," +                                       //薪水发放日期
            "remark text," +                                       //薪水发放备注
            "price integer)";                                         //薪水发放数额



    public WorkRecordDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DAILYRECORDINFO);             //建立每日上工情况表
        db.execSQL(CREATE_EMPLOYEEDAILYSALARY);         //建立职工日薪信息表
        db.execSQL(CREATE_EMPLOYEEGIVENSALARY);         //建立职工发放薪资表
        db.execSQL(CREATE_EMPLOYEEINFO);                    //建立职工基础信息表
        db.execSQL(CREATE_WORKSITEINFO);                //建立工地基础信息表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
