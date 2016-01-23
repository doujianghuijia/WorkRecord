package com.tangcheng.workrecord.fragment.worksitemanage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;
import com.tangcheng.workrecord.model.WorkSiteInfo;

/**
 * Created by tc on 2016/1/15.
 */
public class WorkSiteInfoShowFragment extends Fragment {
    private TextView text_worksiteName;
    private TextView text_remark;
    private TextView text_isBuilded;
    private TextView text_startTime;
    private TextView text_endTime;
    private Boolean fromfinished = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fromfinished = getArguments().getBoolean("fromfinished");
        if(!fromfinished){
            setHasOptionsMenu(true);
        }
        View view = inflater.inflate(R.layout.worksitemanage_worksiteinfoshow_fragment,null);
        String worksiteName = getArguments().getString("worksiteName");
        initView(view,worksiteName);
        return view;
    }

    private void initView(View view,String worksiteName) {
        text_worksiteName = (TextView) view.findViewById(R.id.worksiteinfoshow_name);
        text_remark = (TextView) view.findViewById(R.id.worksiteinfoshow_remark);
        text_isBuilded = (TextView) view.findViewById(R.id.worksiteinfoshow_isBuilded);
        text_startTime = (TextView) view.findViewById(R.id.worksiteinfoshow_starttime);
        text_endTime = (TextView) view.findViewById(R.id.worksiteinfoshow_endtime);
        if(fromfinished){
            text_remark.setEnabled(false);
        }
        WorkSiteInfo workSiteInfo = WorkRecordDBUtils.getInstance(getActivity()).getWorkSiteInfoByName(worksiteName);
        text_worksiteName.setText(workSiteInfo.getName());
        text_remark.setText(workSiteInfo.getRemark());
        if(workSiteInfo.getIsBuilded() == 0){
            text_isBuilded.setText("在建");
        }else{
            text_isBuilded.setText("完工");
        }
        text_startTime.setText(workSiteInfo.getStartTime());
        text_endTime.setText(workSiteInfo.getEndTime());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.worksiteinfoshow_fragment_meau,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resId = item.getItemId();
        final String name = text_worksiteName.getText().toString();
        final String remark = text_remark.getText().toString();
        if(resId == R.id.worksiteshow_meau_update){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.ic_delete).
                    setTitle("注意").
                    setMessage("确定更新工地备注并退出吗？").
                    setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WorkRecordDBUtils.getInstance(getActivity()).updateWorkSiteRemark(name,remark);
                            Fragment worksiteManageFragment = new WorkSiteManageFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_maincontent, worksiteManageFragment).commit();
                        }
                    });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
