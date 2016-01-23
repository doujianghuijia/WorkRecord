package com.tangcheng.workrecord.fragment.employeemanage;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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

import static com.tangcheng.workrecord.R.id.modifyemployeemanage_cancelbtn;

/**
 * Created by tc on 2016/1/18.
 */
public class ModifyEmployeeInfoFragment extends Fragment {
    private EditText edit_name;
    private EditText edit_phone;
    private Button btn_ok;
    private Button btn_cancel;
    private TextView text_nameerr;
    private TextView text_phoneerr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employeemanage_modifyemployeeinfo,null);
        EmployeeInfo employeeInfo = (EmployeeInfo) getArguments().getSerializable("employeeInfo");
        initView(view,employeeInfo);
        return view;
    }

    private void initView(final View view, final EmployeeInfo employeeInfo) {
        edit_name = (EditText) view.findViewById(R.id.modifyemployeemanage_editname);
        edit_phone = (EditText) view.findViewById(R.id.modifyemployeemanage_editphone);
        text_nameerr = (TextView) view.findViewById(R.id.modifyemployeemanage_textnameerr);
        text_phoneerr = (TextView) view.findViewById(R.id.modifyemployeemanage_textphoneerr);
        btn_ok = (Button) view.findViewById(R.id.modifyemployeemanage_okbtn);
        btn_cancel = (Button) view.findViewById(modifyemployeemanage_cancelbtn);
        edit_phone.setText(employeeInfo.getPhoneNumber());
        edit_name.setText(employeeInfo.getName());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_delete).
                        setTitle("注意").
                        setMessage("确定修改该员工信息吗？").
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = edit_name.getText().toString();
                                String phonenumber = edit_phone.getText().toString();
                                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(phonenumber)){
                                    WorkRecordDBUtils.getInstance(getActivity()).updateEmployeeInfoNameAndPhone(employeeInfo.getName(),phonenumber,name);
                                    EmployeeInfoShowFragment fragment = new EmployeeInfoShowFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("employeeName",name);
                                    fragment.setArguments(bundle);
                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                    manager.popBackStack();
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
                builder.show();
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
                if(hasFocus){
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
