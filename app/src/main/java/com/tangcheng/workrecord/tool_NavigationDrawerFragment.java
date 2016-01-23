package com.tangcheng.workrecord;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

/**
 * 左侧侧滑页面
 */
public class tool_NavigationDrawerFragment extends Fragment {
    private ListView lv_main_drawer_leftmenu;                                                 //定义菜单的listView
    private List<MainDrawerMenu> list_menu;


    /**
     *  设置菜单点击接口，以方便外部Activity调用
     */
    public interface menuClickListener
    {
        void menuClick(String menuName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.navigation_drawer,container,false);

        initleftMenuContral(view);

        return view;
    }

    /**
     * 初始化左侧菜单列表listView,并为菜单，设置点击事件
     * @param view
     */
    private void initleftMenuContral(View view) {
        lv_main_drawer_leftmenu = (ListView)view.findViewById(R.id.lv_main_drawer_leftmenu);
        list_menu = getMenuItem();
        lv_main_drawer_leftmenu.setAdapter(new Main_Drawer_lv_Adapter(getActivity(),list_menu));
        lv_main_drawer_leftmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getActivity() instanceof menuClickListener)
                {
                    ((menuClickListener)getActivity()).menuClick(list_menu.get(position).getMainDrawer_menuName());
                }
            }
        });
    }

    /**
     * 从arrays.xml中取出数据，装入list<T>中
     * @return
     */
    private List<MainDrawerMenu> getMenuItem()
    {
        List<MainDrawerMenu> list_menu = new ArrayList<MainDrawerMenu>();

        String[] itemTitle = getResources().getStringArray(R.array.item_title);
        TypedArray itemIconRes = getResources().obtainTypedArray(R.array.item_icon_res);

       //这里不能使用getResources().getIntArray，经测试该方法在item为数字有效，但在item为drawer下图片引用无效，一直为0
        //int[] itemIconRes = getResources().getIntArray(R.array.item_icon_res);

        for(int i=0;i<itemTitle.length;i++)
        {

            MainDrawerMenu lmi = new MainDrawerMenu();
            //i表示获得指定位置资源的id，0表示默认值
            lmi.setMainDrawer_icon(itemIconRes.getResourceId(i,0));
            lmi.setMainDrawer_menuName(itemTitle[i]);
            list_menu.add(lmi);
        }

        return list_menu;
    }
}