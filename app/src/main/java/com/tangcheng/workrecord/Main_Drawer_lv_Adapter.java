package com.tangcheng.workrecord;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * 左侧侧滑菜单的adpter
 * Created by tc on 2016/1/13.
 */
public class Main_Drawer_lv_Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MainDrawerMenu> list_menu;                   //菜单名称与图标的list,采用了一个类

    public Main_Drawer_lv_Adapter(Context context,List<MainDrawerMenu> list_menu) {
        inflater = LayoutInflater.from(context);
        this.list_menu = list_menu;
    }

    @Override
    public int getCount() {
        return list_menu.size();
    }

    @Override
    public Object getItem(int position) {
        return list_menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        menuItem mItem;
        if(convertView==null)
        {
            convertView = inflater.inflate(R.layout.meau_item,null);
            mItem = new menuItem();
            mItem.icon = (ImageView)convertView.findViewById(R.id.item_icon);
            mItem.title = (TextView)convertView.findViewById(R.id.item_title);
            convertView.setTag(mItem);
        }else{
            mItem = (menuItem)convertView.getTag();
        }

        mItem.icon.setImageResource(list_menu.get(position).getMainDrawer_icon());
        mItem.title.setText(list_menu.get(position).getMainDrawer_menuName());

        return convertView;
    }

    public class menuItem
    {
        ImageView icon;
        TextView title;
    }
}

