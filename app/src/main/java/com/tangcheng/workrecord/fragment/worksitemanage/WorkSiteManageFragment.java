package com.tangcheng.workrecord.fragment.worksitemanage;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.tangcheng.workrecord.fragment.employeemanage.AddemployeeFragment;
import com.tangcheng.workrecord.fragment.employeemanage.EmployeeInfoShowFragment;
import com.tangcheng.workrecord.fragment.employeemanage.HasDeletedEmployeeFragment;

import java.util.List;

/**
 * Created by tc on 2016/1/11.
 */
public class WorkSiteManageFragment extends Fragment implements AdapterView.OnItemClickListener{
    private SwipeMenuListView meauListView;
    private List<String> workSiteList;
    private SwipeMeauAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worksitemanage_fragment,null);
        TextView title = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title.setText("工地管理");
        initView(view);
        return view;
    }

    private void initView(View view) {
        meauListView = (SwipeMenuListView) view.findViewById(R.id.workSiteManage_SwipeMeauListView);
        workSiteList = WorkRecordDBUtils.getInstance(getActivity()).getWorkSiteNameList("0");
        mAdapter = new SwipeMeauAdapter();
        meauListView.setAdapter(mAdapter);
        meauListView.setOnItemClickListener(this);
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
                openItem.setTitle("完工");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        // set creator
        meauListView.setMenuCreator(creator);
        // step 2. listener item click event
        meauListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setIcon(R.drawable.ic_delete).
                                setTitle("注意").
                                setMessage("确定该工地完工了吗？").
                                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        WorkRecordDBUtils.getInstance(getActivity()).setWorkSiteIsBuilded(workSiteList.get(position));
                                        workSiteList = WorkRecordDBUtils.getInstance(getActivity()).getWorkSiteNameList("0");
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                        builder.show();
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment worksiteShowInfofragment = new WorkSiteInfoShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("worksiteName", workSiteList.get(position));
        worksiteShowInfofragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainactivity_maincontent, worksiteShowInfofragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    class SwipeMeauAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return workSiteList.size();
        }

        @Override
        public String  getItem(int position) {
            return workSiteList.get(position);
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.employeemanage_fragment_meau, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.employeemanage_meau_add) {
            Fragment addFragment = new AddWorkSiteFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainactivity_maincontent,addFragment);
            transaction.addToBackStack("");
            transaction.commit();
            return true;
        }
        if(id == R.id.employeemanage_meau_hasDeleted){
            Fragment hasFinishedfragment = new HasFinishedWorkSiteFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainactivity_maincontent,hasFinishedfragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
