package com.tangcheng.workrecord.fragment.dailyrecordfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.tangcheng.workrecord.R;
import com.tangcheng.workrecord.db.WorkRecordDBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tc on 2016/1/16.
 */
public class HasRecordedEmployeeFragment extends Fragment {
    private SwipeMenuListView mListView;
    private List<String> hasRecordedList = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dailyrecord_hasrecordedemployee_fragment,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (SwipeMenuListView) view.findViewById(R.id.hasRecordedEmployee_listview);
        hasRecordedList = WorkRecordDBUtils.getInstance(getActivity()).getHasRecordedEmployeeList();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,hasRecordedList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new UpdateDailyRecordFragment();
                Bundle bundle = new Bundle();
                bundle.putString("hasRecordedEmployeeName", hasRecordedList.get(position));
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().popBackStack();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainactivity_maincontent,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
