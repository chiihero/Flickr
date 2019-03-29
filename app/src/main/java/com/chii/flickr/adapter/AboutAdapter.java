package com.chii.flickr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
//TODO
public class AboutAdapter extends BaseAdapter {
    public Context mContext;
    private LayoutInflater inflater;
    private List<String> mList;

    public AboutAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder = null;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.fragment_setting_item, null);
//            viewHolder.item_about_icon = (ImageView) convertView.findViewById(R.id.item_about_icon);
//            viewHolder.item_about_title = (TextView) convertView.findViewById(R.id.item_about_title);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.item_about_icon.setBackgroundResource(SETTING_ICON[position]);
//        viewHolder.item_about_title.setText(mList.get(i));
        return convertView;
    }
    class ViewHolder {
        private ImageView iv_setting_icon;
        private TextView tv_setting_content;
    }

    //更新单个item
    public void updataOneItemView(int position, ListView listView, String text) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (position >= visibleFirstPosi && position <= visibleLastPosi) {
            View view = listView.getChildAt(position - visibleFirstPosi);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.tv_setting_content.setText(text);
            mList.set(position, text);
        } else {
            mList.set(position, text);
        }
    }
}
