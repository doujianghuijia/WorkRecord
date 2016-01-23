package com.tangcheng.workrecord.fragment.employeemanage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;
import com.tangcheng.workrecord.model.EmployeeDailySalary;
import com.tangcheng.workrecord.model.EmployeeInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tc on 2016/1/15.
 */
public class EmployeeInfoShowFragment extends Fragment {
    private TextView showtext_name;
    private TextView showtext_phoneNumber;
    private TextView showtext_startTime;
    private TextView showtext_endTime;
    private TextView showtext_isHired;
    private TextView showtext_totalWorkTime;
    private TextView showtext_totalsalary;
    private TextView showtext_restSalary;
    private Button btn_dial;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employeemanege_employeeinfoshow_fragment, null);
        String employeeName = getArguments().getString("employeeName");
        setHasOptionsMenu(true);
        initView(view,employeeName);
        return  view;
    }

    public void initView(View view,String employeeName){
        showtext_endTime = (TextView) view.findViewById(R.id.employeeinfoshow_endtime);
        showtext_name = (TextView) view.findViewById(R.id.employeeinfoshow_name);
        showtext_phoneNumber = (TextView) view.findViewById(R.id.employeeinfoshow_phonenumber);
        showtext_isHired = (TextView) view.findViewById(R.id.employeeinfoshow_isHired);
        showtext_restSalary = (TextView) view.findViewById(R.id.employeeinfoshow_restsalary);
        showtext_startTime = (TextView) view.findViewById(R.id.employeeinfoshow_starttime);
        showtext_totalsalary = (TextView) view.findViewById(R.id.employeeinfoshow_totalsalary);
        showtext_totalWorkTime = (TextView) view.findViewById(R.id.employeeinfoshow_totalworktime);
        btn_dial = (Button) view.findViewById(R.id.employeeinfoshow_dial);
        final EmployeeInfo employeeInfo = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeInfoByName(employeeName);
        showtext_name.setText(employeeInfo.getName());
        showtext_phoneNumber.setText(employeeInfo.getPhoneNumber());
        showtext_totalWorkTime.setText(employeeInfo.getTotalWorkTime()+" 个工时");
        //得到总薪资
        List<EmployeeDailySalary> list = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeDailySalaryByName(employeeName);
        float totalSalary = 0;
        if(list != null){
            for(EmployeeDailySalary dailySalary:list){
                String endTime = dailySalary.getEndTime();
                if(endTime.equals("0")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentDate = new Date(System.currentTimeMillis());
                    endTime = sdf.format(currentDate);
                }
                float workLengthSum = WorkRecordDBUtils.getInstance(getActivity()).getTotalWorkTimeDuringSpecificTime(dailySalary.getEmployeeName(),dailySalary.getStartTime(),endTime);
                totalSalary = totalSalary+(workLengthSum*dailySalary.getPrice());
            }
        }else{
            totalSalary = 0;
        }
        showtext_totalsalary.setText(totalSalary+"");
        //得到剩余薪资
        int gievnSalary = WorkRecordDBUtils.getInstance(getActivity()).getTotalGivenSalary(employeeName);
        float restSalary = totalSalary - gievnSalary;
        showtext_restSalary.setText(restSalary+"元");

        showtext_startTime.setText(employeeInfo.getStartTime());
        showtext_endTime.setText(employeeInfo.getEndTime());
        if(employeeInfo.getIsHired() == 0){
            showtext_isHired.setText("在职");
        }else{
            showtext_isHired.setText("离职");
        }
        btn_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+employeeInfo.getPhoneNumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.worksiteinfoshow_fragment_meau,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resId = item.getItemId();
        if(resId == R.id.worksiteshow_meau_update){
            Bundle bundle = new Bundle();
            EmployeeInfo employeeInfo = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeInfoByName(getArguments().getString("employeeName"));
            bundle.putSerializable("employeeInfo",employeeInfo);
            ModifyEmployeeInfoFragment fragment = new ModifyEmployeeInfoFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainactivity_maincontent, fragment);
            ft.addToBackStack(null);
            ft.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
