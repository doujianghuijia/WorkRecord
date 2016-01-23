package com.tangcheng.workrecord.fragment.worksitemanage;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;
import com.tangcheng.workrecord.fragment.employeemanage.EmployeeInfoShowFragment;

import java.util.List;

/**
 * Created by tc on 2016/1/15.
 */
public class HasFinishedWorkSiteFragment extends Fragment implements AdapterView.OnItemClickListener {
    private SwipeMenuListView mListView;
    private List<String> finishedWorkSiteNameList;
    private SwipeMeauAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worksitemanage_hasfinishedworksite_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (SwipeMenuListView) view.findViewById(R.id.hasfinishedworksite_swipmeaulistview);
        finishedWorkSiteNameList = WorkRecordDBUtils.getInstance(getActivity()).getWorkSiteNameList("1");
        mAdapter = new SwipeMeauAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment = new WorkSiteInfoShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("worksiteName", finishedWorkSiteNameList.get(position));
        bundle.putBoolean("fromfinished",true);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainactivity_maincontent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class SwipeMeauAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return finishedWorkSiteNameList.size();
        }

        @Override
        public String getItem(int position) {
            return finishedWorkSiteNameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
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
    }
}
