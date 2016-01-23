package com.tangcheng.workrecord.fragment.dailyrecordfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
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
public class AddDailyRecordFragment extends Fragment {
    private TextView text_addEmployeeName;
    private TextView text_addDate;
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
        View view = inflater.inflate(R.layout.dailyrecord_adddailyrecord_fragment,null);
        String employeeName = getArguments().getString("addRecordEmployeeName");
        mTimeLengthList = new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.item_timeLength)));
        initView(view,employeeName);
        return view;
    }

    private void initView(View view,String employeeName) {
        final DailyRecordInfo dailyRecordInfo = new DailyRecordInfo();
        text_addEmployeeName = (TextView) view.findViewById(R.id.addDailyRecord_employeeName);
        text_addDate = (TextView) view.findViewById(R.id.addDailyRecord_currentDate);
        spinner_timeLength = (AppCompatSpinner) view.findViewById(R.id.addDailyRecord_timeLength_spinner);
        spinner_worksite = (AppCompatSpinner) view.findViewById(R.id.addDailyRecord_worksite_spinner);
        edit_remark = (EditText) view.findViewById(R.id.addDailyRecord_remark);
        btn_ok = (Button) view.findViewById(R.id.addDailyRecord_okbtn);
        btn_cancel = (Button) view.findViewById(R.id.addDailyRecord_cancelbtn);
        text_addEmployeeName.setText(employeeName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date currentDate = new Date(System.currentTimeMillis());
        String date = sdf.format(currentDate);
        text_addDate.setText("今天是"+date);
        //初始化工地List
        mWorkSiteList = WorkRecordDBUtils.getInstance(getActivity()).getWorkSiteNameList("0");
        //工时控件相关设置
        ArrayAdapter timeLengthAdaapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,mTimeLengthList);
        timeLengthAdaapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_timeLength.setAdapter(timeLengthAdaapter);
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
                Toast.makeText(getActivity(),"请选择工作时长!",Toast.LENGTH_SHORT).show();
            }
        });
        //施工地点控件相关设置
        ArrayAdapter worksiteAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,mWorkSiteList);
        worksiteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_worksite.setAdapter(worksiteAdapter);
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
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = text_addEmployeeName.getText().toString();
                dailyRecordInfo.setEmployeeName(name);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date(System.currentTimeMillis());
                String datestr = sdf.format(currentDate);
                dailyRecordInfo.setCurrentTime(datestr);
                dailyRecordInfo.setRemark(edit_remark.getText().toString());
                WorkRecordDBUtils.getInstance(getActivity()).saveDailyRecordInfo(dailyRecordInfo);
                WorkRecordDBUtils.getInstance(getActivity()).updateTotalWorkTime(name,dailyRecordInfo.getTimeLength(),0);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
                Fragment fragment = new DailyRecordFragment();
                manager.beginTransaction().replace(R.id.mainactivity_maincontent, fragment).commit();
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
    }
}
