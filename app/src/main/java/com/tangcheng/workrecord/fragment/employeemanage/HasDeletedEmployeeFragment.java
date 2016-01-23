package com.tangcheng.workrecord.fragment.employeemanage;

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

import java.util.List;

/**
 * Created by tc on 2016/1/15.
 */
public class HasDeletedEmployeeFragment extends Fragment implements AdapterView.OnItemClickListener{
    private SwipeMenuListView mListView;
    private List<String> mDate;
    private SwipeMeauAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employeemanege_hasdeletedemployee_fragment,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (SwipeMenuListView) view.findViewById(R.id.hasDeletedEmployee_listview);
        mDate = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeNameList("1");
        mAdapter = new SwipeMeauAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_action_favorite);
                // add to menu
                menu.addMenuItem(deleteItem);
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
                        // delete
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setIcon(R.drawable.ic_delete).
                                setTitle("注意").
                                setMessage("确定重新雇佣该员工吗？").
                                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).
                                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        WorkRecordDBUtils.getInstance(getActivity()).reHiredEmployee(mDate.get(position));
                                        mDate = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeNameList("1");
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
        Fragment fragment = new EmployeeInfoShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("employeeName", mDate.get(position));
        bundle.putBoolean("fromdeleted",true);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainactivity_maincontent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    class SwipeMeauAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDate.size();
        }

        @Override
        public String  getItem(int position) {
            return mDate.get(position);
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
}
