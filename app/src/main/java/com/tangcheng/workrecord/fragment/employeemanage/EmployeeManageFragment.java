package com.tangcheng.workrecord.fragment.employeemanage;

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
import com.tangcheng.workrecord.fragment.givesalary.GiveSalaryFragment;

import java.util.List;

/**
 * Created by tc on 2016/1/11.
 */
public class EmployeeManageFragment extends Fragment implements AdapterView.OnItemClickListener{
    private List<String> mEmployeeList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employeemanege_fragment,null);
        TextView title = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title.setText("员工管理");
        iniView(view);
        return view;
    }

    public void iniView(View view){
        mEmployeeList = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeNameList("0");
        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem giveItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                giveItem.setBackground(new ColorDrawable(Color.rgb(0xEE, 0xC5,
                        0x91)));
                // set item width
                giveItem.setWidth(dp2px(90));
                // set item title
                giveItem.setTitle("发薪");
                // set item title fontsize
                giveItem.setTitleSize(18);
                // set item title font color
                giveItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(giveItem);


                // create "open" item
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

                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getActivity().getApplicationContext());
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                // set item width
//                deleteItem.setWidth(dp2px(90));
//                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete);
//                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        GiveSalaryFragment fragment = new GiveSalaryFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("employeeName",mEmployeeList.get(position));
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainactivity_maincontent,fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case 1:
                        // delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setIcon(R.drawable.ic_delete).
                                setTitle("注意").
                                setMessage("确定删除该员工吗？").
                                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        WorkRecordDBUtils.getInstance(getActivity()).setEmployeeInfoNotHired(mEmployeeList.get(position));
                                        mEmployeeList = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeNameList("0");
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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("employeeName", mEmployeeList.get(position));
        EmployeeInfoShowFragment fragment = new EmployeeInfoShowFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.mainactivity_maincontent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mEmployeeList.size();
        }

        @Override
        public String  getItem(int position) {
            return mEmployeeList.get(position);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.employeemanage_fragment_meau,menu);
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
            Fragment addFragment = new AddemployeeFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainactivity_maincontent,addFragment);
            transaction.addToBackStack("");
            transaction.commit();
            return true;
        }
        if(id == R.id.employeemanage_meau_hasDeleted){
            Fragment hasDeletedfragment = new HasDeletedEmployeeFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainactivity_maincontent,hasDeletedfragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
