package com.tangcheng.workrecord.fragment.dailyrecordfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;
import com.tangcheng.workrecord.fragment.employeemanage.HasDeletedEmployeeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tc on 2016/1/11.
 */
public class DailyRecordFragment extends Fragment implements AdapterView.OnItemClickListener{
    private List<String> allHiredEmployeeList = new ArrayList<String>();
    private List<String> hasRecordedEmployeeList = new ArrayList<String>();
    private List<String> toShowEmployeeList = new ArrayList<String>();
    private ListView mListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView toolbar_title = (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("每日工时记录");
        View view = inflater.inflate(R.layout.dailyrecord_fragment,null);
        setHasOptionsMenu(true);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.dailyrecord_listview);
        allHiredEmployeeList = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeNameList("0");
        hasRecordedEmployeeList = WorkRecordDBUtils.getInstance(getActivity()).getHasRecordedEmployeeList();
        allHiredEmployeeList.removeAll(hasRecordedEmployeeList);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,allHiredEmployeeList);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dailyrecord_fragment_meau,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_2:
                Fragment fragment = new HasRecordedEmployeeFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainactivity_maincontent,fragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment addDailyRecordFragment = new AddDailyRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("addRecordEmployeeName",allHiredEmployeeList.get(position));
        addDailyRecordFragment.setArguments(bundle);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainactivity_maincontent,addDailyRecordFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
