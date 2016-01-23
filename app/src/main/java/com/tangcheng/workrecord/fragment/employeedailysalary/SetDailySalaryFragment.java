package com.tangcheng.workrecord.fragment.employeedailysalary;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;
import com.tangcheng.workrecord.fragment.employeemanage.EmployeeInfoShowFragment;
import com.tangcheng.workrecord.model.EmployeeDailySalary;

import java.util.Calendar;
import java.util.List;

/**
 * Created by tc on 2016/1/17.
 */
public class SetDailySalaryFragment extends Fragment{
    private SwipeMenuListView swipeMenuListView;
    private EditText startTimeEditText;
    private Button btn_save;
    private List<String> dailySalaryList;
    private AppAdapter mAdapter;
    private EditText dailySalaryEditText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employeedailysalary_setdailysalary_fragment,null);
        String employeeName = getArguments().getString("employeeName");
        initView(view, employeeName);
        return view;
    }

    private void initView(final View view, final String name) {
        //设置日薪记录的员工名
        final EmployeeDailySalary employeeDailySalary = new EmployeeDailySalary();
        employeeDailySalary.setEmployeeName(name);
        //初始化
        swipeMenuListView = (SwipeMenuListView) view.findViewById(R.id.setdailysalary_swipemeaulistview);
        startTimeEditText = (EditText) view.findViewById(R.id.setdailysalary_startTimeEditText);
        dailySalaryEditText = (EditText) view.findViewById(R.id.setdailysalary_dailySalaryEditText);
        btn_save = (Button) view.findViewById(R.id.setdailysalary_addbtn);
        dailySalaryList = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeDailySalaryInfoStrByName(name);
        mAdapter = new AppAdapter();
        swipeMenuListView.setAdapter(mAdapter);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        // set creator
        swipeMenuListView.setMenuCreator(creator);

        // step 2. listener item click event
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setIcon(R.drawable.ic_delete).
                                setTitle("注意").
                                setMessage("确定删除该日薪规则吗？").
                                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String info = dailySalaryList.get(position);
                                        String startTime = info.substring(info.indexOf("：")+1, info.indexOf("--"));
                                        EmployeeDailySalary dailySalary = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeDailySalary(name,startTime);
                                        WorkRecordDBUtils.getInstance(getActivity()).deleteDailySalary(dailySalary);
                                        dailySalaryList = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeDailySalaryInfoStrByName(name);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                        builder.show();
                        break;
                }
                return false;
            }
        });
        startTimeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            int mYear = year;
                            int mMonth = month;
                            int mDay = day;
                            //更新EditText控件日期 小于10加0
                            startTimeEditText.setText(new StringBuilder().append(mYear).append("-")
                                    .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                                    .append("-")
                                    .append((mDay < 10) ? "0" + mDay : mDay));
                            startTimeEditText.clearFocus();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startTimeEditText.clearFocus();
                        }
                    });
                    datePickerDialog.show();
                } else {
                    HideKeyboard(v);
                }
            }
        });
        dailySalaryEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    dailySalaryEditText.clearFocus();
                    HideKeyboard(v);
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeDailySalary.setStartTime(startTimeEditText.getText().toString());
                employeeDailySalary.setPrice(Integer.parseInt(dailySalaryEditText.getText().toString()));
                WorkRecordDBUtils.getInstance(getActivity()).saveEmployeeDailySalary(employeeDailySalary);
                dailySalaryList = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeDailySalaryInfoStrByName(employeeDailySalary.getEmployeeName());
                mAdapter.notifyDataSetChanged();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view.requestFocus();
                return false;
            }
        });
    }

    public static void HideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );
        }
    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dailySalaryList.size();
        }

        @Override
        public String  getItem(int position) {
            return dailySalaryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity().getApplicationContext(),
                        R.layout.item_list, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            String item = getItem(position);
            holder.tv_name.setText(item);
            return convertView;
        }
        class ViewHolder {
            TextView tv_name;

            public ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }

        public boolean getSwipEnableByPosition(int position) {
            if(position % 2 == 0){
                return false;
            }
            return true;
        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
