package com.example.newlogin.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Edward on 10-Jun-16.
 */
public class ListViewCustomAdapter extends BaseAdapter {
    ArrayList<Object> itemList;

    public Activity context;
    public LayoutInflater inflater;
    public ListViewCustomAdapter(Activity context, ArrayList<Object> itemList){
        super();

        this.context = context;
        this.itemList = itemList;

        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static class ViewHolder
    {
        ImageView imgViewLogo;
        TextView txtViewTitle;
        TextView txtViewDescription;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.items, null);

            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);

            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();

        ItemBean bean = (ItemBean) itemList.get(position);

        holder.imgViewLogo.setImageResource(bean.getImage());
        holder.txtViewTitle.setText(bean.getTitle());
        holder.txtViewDescription.setText(bean.getDescription());

        return convertView;
    }
}
