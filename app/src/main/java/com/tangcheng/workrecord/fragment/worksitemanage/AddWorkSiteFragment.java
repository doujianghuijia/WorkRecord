package com.tangcheng.workrecord.fragment.worksitemanage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.tangcheng.workrecord.model.WorkSiteInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tc on 2016/1/15.
 */
public class AddWorkSiteFragment extends Fragment {
    private EditText edit_WorksiteName;
    private EditText edit_WorkSiteRemark;
    private TextView text_WorksiteNameErr;
    private Button btn_ok;
    private Button btn_cancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worksitemanage_addworksiteinfo_fragment,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        edit_WorksiteName = (EditText) view.findViewById(R.id.addworksite_name);
        edit_WorkSiteRemark = (EditText) view.findViewById(R.id.addworksite_remark);
        text_WorksiteNameErr = (TextView) view.findViewById(R.id.addworksite_nameerrtext);
        btn_ok = (Button) view.findViewById(R.id.addworksite_okbtn);
        btn_cancel = (Button) view.findViewById(R.id.addworksite_cancelbtn);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String worksiteName = edit_WorksiteName.getText().toString();
                String worksiteRemark = edit_WorkSiteRemark.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date(System.currentTimeMillis());
                String date = sdf.format(currentDate);
                if(!TextUtils.isEmpty(worksiteName)){
                    WorkSiteInfo workSiteInfo = new WorkSiteInfo(worksiteName,worksiteRemark,0,date,"");
                    WorkRecordDBUtils.getInstance(getActivity()).saveWorkSiteInfo(workSiteInfo);
                    Fragment worksiteManageFragment = new WorkSiteManageFragment();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_maincontent,worksiteManageFragment).commit();
                }else{
                    text_WorksiteNameErr.setVisibility(View.VISIBLE);
                }
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
        edit_WorksiteName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    text_WorksiteNameErr.setVisibility(View.INVISIBLE);
                } else {
                    HideKeyboard(v);
                }
            }
        });
        edit_WorkSiteRemark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
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
