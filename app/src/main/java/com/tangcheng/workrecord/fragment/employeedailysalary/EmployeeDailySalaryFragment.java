package com.tangcheng.workrecord.fragment.employeedailysalary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;

import java.util.List;

/**
 * Created by tc on 2016/1/11.
 */
public class EmployeeDailySalaryFragment extends Fragment {
    private ListView mListView;
    private List<String> mEmployeeNameList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employeedailyeesalary_fragment,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.dailysalary_listview);
        mEmployeeNameList = WorkRecordDBUtils.getInstance(getActivity()).getEmployeeNameList("0");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,mEmployeeNameList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new SetDailySalaryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("employeeName",mEmployeeNameList.get(position));
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainactivity_maincontent,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
