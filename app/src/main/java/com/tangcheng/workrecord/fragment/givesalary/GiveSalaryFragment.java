package com.tangcheng.workrecord.fragment.givesalary;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.tangcheng.workrecord.model.EmployeeDailySalary;
import com.tangcheng.workrecord.model.EmployeeGivenSalary;

import java.util.Calendar;
import java.util.List;

/**
 * Created by tc on 2016/1/18.
 */
public class GiveSalaryFragment extends Fragment {
    private SwipeMenuListView swipeMenuListView;
    private List<String> givenSalaryList;
    private EditText edit_price;
    private Button btn_save;
    private EditText edit_time;
    private EditText edit_remark;
    private AppAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.givesalary_fragment,null);
        String employeeName = getArguments().getString("employeeName");
        initView(view, employeeName);
        return view;
    }
    private void initView(final View view, final String employeeName) {
        //初始化
        swipeMenuListView = (SwipeMenuListView) view.findViewById(R.id.givesalary_swipemeaulistview);
        edit_price = (EditText) view.findViewById(R.id.givensalary_priceEditText);
        edit_remark = (EditText) view.findViewById(R.id.givensalary_remarkEditText);
        edit_time = (EditText) view.findViewById(R.id.givensalary_timeEditText);
        btn_save = (Button) view.findViewById(R.id.givensalary_addbtn);
        givenSalaryList = WorkRecordDBUtils.getInstance(getActivity()).getGivenSalaryByEmployeeName(employeeName);
        mAdapter = new AppAdapter();
        swipeMenuListView.setAdapter(mAdapter);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x00,
                        0x00)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
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
                                setMessage("确定删除该条发薪记录吗？").
                                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String info = givenSalaryList.get(position);
                                        String time = info.substring(0,info.indexOf(":  "));
                                        WorkRecordDBUtils.getInstance(getActivity()).deleteGivenSalary(employeeName,time);
                                        givenSalaryList = WorkRecordDBUtils.getInstance(getActivity()).getGivenSalaryByEmployeeName(employeeName);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                        builder.show();
                        break;
                }
                return false;
            }
        });

        edit_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar calendar = Calendar.getInstance();
                    final TimeTemp timeTemp = new TimeTemp();
                    final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            int mYear = year;
                            int mMonth = month;
                            int mDay = day;
                            timeTemp.setYear(year);
                            timeTemp.setMonth(month);
                            timeTemp.setDay(day);
                            //更新EditText控件日期 小于10加0
                            edit_time.setText(new StringBuilder().append(mYear).append("-")
                                    .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                                    .append("-")
                                    .append((mDay < 10) ? "0" + mDay : mDay));
                            edit_time.clearFocus();
                        }
                    };
                    final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),listener , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edit_time.clearFocus();
                        }
                    });
                    datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatePicker mDatePicker = datePickerDialog.getDatePicker();
                            listener.onDateSet(mDatePicker, mDatePicker.getYear(),
                                    mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                        }
                    });
                    datePickerDialog.show();
                } else {
                    HideKeyboard(v);
                }
            }
        });
        edit_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    edit_price.clearFocus();
                    HideKeyboard(v);
                }
            }
        });
        edit_remark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    edit_remark.clearFocus();
                    HideKeyboard(v);
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeGivenSalary givenSalary = new EmployeeGivenSalary();
                givenSalary.setEmployeeName(employeeName);
                givenSalary.setPrice(Integer.parseInt(edit_price.getText().toString()));
                givenSalary.setTime(edit_time.getText().toString());
                givenSalary.setRemark(edit_remark.getText().toString());
                WorkRecordDBUtils.getInstance(getActivity()).saveEmployeeGivenSalary(givenSalary);
                givenSalaryList = WorkRecordDBUtils.getInstance(getActivity()).getGivenSalaryByEmployeeName(employeeName);
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
    class TimeTemp{
        int year;
        int month;
        int day;

        public TimeTemp() {
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
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
            return givenSalaryList.size();
        }

        @Override
        public String  getItem(int position) {
            return givenSalaryList.get(position);
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
