package com.ct.ct.concentrigger.Util;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ct.ct.concentrigger.R;
import com.ct.ct.concentrigger.Model.Event;

import java.util.List;

/**
 * Created by holic on 2018/5/28.
 */

public class SquareEventAdapter extends BaseAdapter {

    private List<Event> squareEventList;


    public SquareEventAdapter(List<Event> squareEventList) {
        this.squareEventList = squareEventList;
    }


    //返回活动数
    public int getCount() {

        return squareEventList == null? 0: squareEventList.size();

    }


    public Object getItem(int position) {
        return squareEventList.get(position);
    }


    public long getItemId(int position) {
        //return eventList.get(position).getActivity_id();
        return 1;
    }


    //可以用了的单个列表页getView
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (null == view) {
            view = View.inflate(viewGroup.getContext(), R.layout.listitem_square_event, null);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Event suqareEventItem = squareEventList.get(i);

//        if (suqareEventItem.getOwnerAvatar() != null) {
//            Picasso.with(view.getContext()).load(ServiceURL.getServerUrl(viewGroup.getContext())
//                    + actItem.getActivity_icon()).into(viewHolder.actHomeListImg);
//        }


        viewHolder.eventCdt.setText("condition");
        viewHolder.eventRst.setText("result");
        viewHolder.eventOwnerName.setText("holic");
        viewHolder.eventPull.setText("100");
        viewHolder.eventLike.setText("200");


        return view;
    }


    static class ViewHolder {

        ImageView eventOwnerAvatar;
        ImageView eventCImg;
        ImageView eventTImg;
        TextView eventOwnerName;
        TextView eventCdt;
        TextView eventRst;
        TextView eventPull;
        TextView eventLike;

        ViewHolder(View view) {

            eventOwnerAvatar = (ImageView) view.findViewById(R.id.img_owner_avatar);
            eventCImg = (ImageView) view.findViewById(R.id.ic_c);
            eventTImg = (ImageView) view.findViewById(R.id.ic_t);
            eventOwnerName = (TextView) view.findViewById(R.id.txt_owner_name);
            eventCdt = (TextView) view.findViewById(R.id.txt_condition);
            eventRst = (TextView) view.findViewById(R.id.txt_result);
            eventPull = (TextView) view.findViewById(R.id.txt_pull_num);
            eventLike = (TextView) view.findViewById(R.id.txt_like_num);


        }
    }

}
