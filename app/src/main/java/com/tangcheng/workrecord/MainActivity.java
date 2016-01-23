package com.tangcheng.workrecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tangcheng.workrecord.fragment.dailyrecordfragment.DailyRecordFragment;
import com.tangcheng.workrecord.fragment.givesalary.GiveSalaryFragment;
import com.tangcheng.workrecord.fragment.worksitemanage.WorkSiteManageFragment;
import com.tangcheng.workrecord.fragment.employeedailysalary.EmployeeDailySalaryFragment;
import com.tangcheng.workrecord.fragment.employeemanage.EmployeeManageFragment;

/**
 * Created by tc on 2016/1/11.
 */
public class MainActivity extends AppCompatActivity {
    long firstTime = 0;
    private RadioGroup  mainactivity_tabmeau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.user);
        setSupportActionBar(toolbar);
        iniView();
    }


    private void iniView() {
        mainactivity_tabmeau = (RadioGroup) findViewById(R.id.mainactivity_tabmeau);
        Fragment fragment1 = new EmployeeManageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_maincontent,fragment1).commit();
        mainactivity_tabmeau.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int backStackCount;
                FragmentManager manager = getSupportFragmentManager();
                switch (checkedId) {
                    case R.id.button1:
                        Fragment fragment1 = new EmployeeManageFragment();
                        backStackCount = manager.getBackStackEntryCount();
                        for(int i = 0; i < backStackCount; i++) {
                            manager.popBackStack();
                        }
                        manager.beginTransaction().replace(R.id.mainactivity_maincontent, fragment1).commit();
                        break;
                    case R.id.button2:
                        Fragment fragment2 = new DailyRecordFragment();
                        backStackCount = manager.getBackStackEntryCount();
                        for(int i = 0; i < backStackCount; i++) {
                            manager.popBackStack();
                        }
                        manager.beginTransaction().replace(R.id.mainactivity_maincontent, fragment2).commit();
                        break;
                    case R.id.button3:
                        Fragment fragment3 = new WorkSiteManageFragment();
                        backStackCount = manager.getBackStackEntryCount();
                        for(int i = 0; i < backStackCount; i++) {
                            manager.popBackStack();
                        }
                        manager.beginTransaction().replace(R.id.mainactivity_maincontent, fragment3).commit();
                        break;
                    case R.id.button4:
                        Fragment fragment4 = new EmployeeDailySalaryFragment();
                        backStackCount = manager.getBackStackEntryCount();
                        for(int i = 0; i < backStackCount; i++) {
                            manager.popBackStack();
                        }
                        manager.beginTransaction().replace(R.id.mainactivity_maincontent, fragment4).commit();
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1500) {//如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
            } else {
                System.exit(0);//否则退出程序
            }
        }else{
            super.onBackPressed();
        }
    }
}
