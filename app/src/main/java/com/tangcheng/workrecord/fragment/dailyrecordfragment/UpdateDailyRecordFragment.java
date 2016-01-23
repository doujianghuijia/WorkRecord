package com.tangcheng.workrecord.fragment.dailyrecordfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;
import com.tangcheng.workrecord.model.DailyRecordInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tc on 2016/1/16.
 */
public class UpdateDailyRecordFragment extends Fragment {
    private TextView text_updateEmployeeName;
    private TextView text_updateDate;
    private AppCompatSpinner spinner_timeLength;
    private AppCompatSpinner spinner_worksite;
    private Button btn_ok;
    private Button btn_cancel;
    private EditText edit_remark;
    private LinkedList<String> mTimeLengthList;
    private List<String> mWorkSiteList = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dailyrecord_updaterecord_fragment,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        String name = getArguments().getString("hasRecordedEmployeeName");
        mWorkSiteList = WorkRecordDBUtils.getInstance(getActivity()).getWorkSiteNameList("0");
        mTimeLengthList = new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.item_timeLength)));
        text_updateEmployeeName = (TextView) view.findViewById(R.id.updateDailyRecord_employeeName);
        text_updateDate = (TextView) view.findViewById(R.id.updateDailyRecord_currentDate);
        spinner_timeLength = (AppCompatSpinner) view.findViewById(R.id.updateDailyRecord_timeLength_spinner);
        spinner_worksite = (AppCompatSpinner) view.findViewById(R.id.updateDailyRecord_worksite_spinner);
        edit_remark = (EditText) view.findViewById(R.id.updateDailyRecord_remark);
        btn_ok = (Button) view.findViewById(R.id.updateDailyRecord_okbtn);
        btn_cancel = (Button) view.findViewById(R.id.updateDailyRecord_cancelbtn);
        text_updateEmployeeName.setText(name);
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf1.format(currentDate);
        String datestr = sdf.format(currentDate);
        final DailyRecordInfo dailyRecordInfo = WorkRecordDBUtils.getInstance(getActivity()).getDailyRecordInfoByNameAndDate(name,date);
        text_updateDate.setText("今天是"+datestr);
        edit_remark.setText(dailyRecordInfo.getRemark());
        //初始化工作时长下拉框的默认值,这里得到原来的数据位置
        final float timelengthfloat = dailyRecordInfo.getTimeLength();
        String timelength = timelengthfloat+"";
        int timelengthindex;
        if(!timelength.equals("0.0")){
            timelengthindex = mTimeLengthList.indexOf(timelength);
        }else{
            timelengthindex = 10;
        }
        //初始化施工地点下拉框的默认值，这里得到原来数据的位置
        String worksite = dailyRecordInfo.getWorkSite();
        int worksiteIndex = mWorkSiteList.indexOf(worksite);
        //工时控件相关设置
        ArrayAdapter timeLengthAdaapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,mTimeLengthList);
        timeLengthAdaapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_timeLength.setAdapter(timeLengthAdaapter);
        //设置spinner初始的显示选项
        spinner_timeLength.setSelection(timelengthindex, true);
        spinner_timeLength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 10){
                    dailyRecordInfo.setTimeLength(Float.parseFloat(mTimeLengthList.get(position)));
                }else{
                    dailyRecordInfo.setTimeLength(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "请选择工作时长!", Toast.LENGTH_SHORT).show();
            }
        });
        //施工地点控件相关设置
        ArrayAdapter worksiteAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,mWorkSiteList);
        worksiteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_worksite.setAdapter(worksiteAdapter);
        //设置worksite spinner的初始化显示
        spinner_worksite.setSelection(worksiteIndex, true);
        spinner_worksite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dailyRecordInfo.setWorkSite(mWorkSiteList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "请选择施工地点!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                } catch (IOException e) {
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.ic_delete).
                setTitle("注意").
                setMessage("确定更新该员工当天的工作记录吗？").
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String timelengthstr = dailyRecordInfo.getTimeLength() + "";
                        String worksiteStr = dailyRecordInfo.getWorkSite();
                        String remark = edit_remark.getText().toString();
                        WorkRecordDBUtils.getInstance(getActivity()).updateDailyRecord(dailyRecordInfo.getEmployeeName(), worksiteStr, remark, timelengthstr);
                        WorkRecordDBUtils.getInstance(getActivity()).updateTotalWorkTime(dailyRecordInfo.getEmployeeName(),dailyRecordInfo.getTimeLength(),timelengthfloat);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        manager.popBackStack();
                        Fragment fragment = new DailyRecordFragment();
                        manager.beginTransaction().replace(R.id.mainactivity_maincontent, fragment).commit();
                    }
                });
            builder.show();
            }
        });
    }
}
