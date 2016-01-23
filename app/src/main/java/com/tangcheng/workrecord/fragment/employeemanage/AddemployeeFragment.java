package com.tangcheng.workrecord.fragment.employeemanage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;
import com.tangcheng.workrecord.model.EmployeeInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tc on 2016/1/15.
 */
public class AddemployeeFragment extends Fragment {
    private Button okbtn;
    private Button cancelbtn;
    private EditText edit_name;
    private EditText edit_phone;
    private TextView text_nameerr;
    private TextView text_phoneerr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employeemanege_addemployee_fragment,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        okbtn = (Button) view.findViewById(R.id.addemployee_okbtn);
        cancelbtn = (Button) view.findViewById(R.id.addemployee_cancelbtn);
        edit_name = (EditText) view.findViewById(R.id.addemployee_name);
        edit_phone = (EditText) view.findViewById(R.id.addemployee_phonenumber);
        text_nameerr = (TextView) view.findViewById(R.id.addemployee_nameerrtext);
        text_phoneerr = (TextView) view.findViewById(R.id.addemployee_phoneerrtext);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        final String date = formatter.format(curDate);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                String phonenumber = edit_phone.getText().toString();
                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(phonenumber)){
                    EmployeeInfo employeeInfo = new EmployeeInfo(name,phonenumber,0,0,0,0,0,date,"");
                    WorkRecordDBUtils.getInstance(getActivity()).saveEmployeeInfo(employeeInfo);
                    EmployeeManageFragment fragment = new EmployeeManageFragment();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.popBackStack();             //将上一个fragment从backstack栈中弹出，避免出现重叠的现象
                    manager.beginTransaction().replace(R.id.mainactivity_maincontent,fragment).commit();
                }else{
                    if(TextUtils.isEmpty(name)){
                        text_nameerr.setVisibility(View.VISIBLE);
                    }
                    if(TextUtils.isEmpty(phonenumber)){
                        text_phoneerr.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                } catch (IOException e) {
                }
            }
        });
        edit_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    text_nameerr.setVisibility(View.INVISIBLE);
                    text_phoneerr.setVisibility(View.INVISIBLE);
                } else {
                    HideKeyboard(v);
                }
            }
        });
        edit_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    text_nameerr.setVisibility(View.INVISIBLE);
                    text_phoneerr.setVisibility(View.INVISIBLE);
                }else{
                    HideKeyboard(v);
                }
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
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
}
