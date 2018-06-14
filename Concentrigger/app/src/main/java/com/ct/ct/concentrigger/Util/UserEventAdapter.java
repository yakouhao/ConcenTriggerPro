package com.ct.ct.concentrigger.Util;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ct.ct.concentrigger.Model.Event;
import com.ct.ct.concentrigger.R;



import java.util.List;

/**
 * Created by holic on 2018/5/28.
 */

public class UserEventAdapter extends BaseAdapter {

    private List<Event> eventList;


    public UserEventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }


    //返回活动数
    public int getCount() {

        return eventList == null? 0: eventList.size();

    }


    public Object getItem(int position) {
        return eventList.get(position);
    }


    public long getItemId(int position) {
        //return eventList.get(position).getActivity_id();
        return 1;
    }


    //可以用了的单个列表页getView
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (null == view) {
            view = View.inflate(viewGroup.getContext(), R.layout.listitem_my_event, null);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Event eventItem = eventList.get(i);

//        if (actItem.getActivity_icon() != null) {
//            Picasso.with(view.getContext()).load(ServiceURL.getServerUrl(viewGroup.getContext())
//                    + actItem.getActivity_icon()).into(viewHolder.actHomeListImg);
//        }

        viewHolder.eventCdt.setText("condition");
        viewHolder.eventRst.setText("result");


        return view;
    }


    static class ViewHolder {

        ImageView eventCImg;
        ImageView eventTImg;
        TextView eventCdt;
        TextView eventRst;

        ViewHolder(View view) {

            eventCImg = (ImageView) view.findViewById(R.id.ic_c);
            eventTImg = (ImageView) view.findViewById(R.id.ic_t);
            eventCdt = (TextView) view.findViewById(R.id.txt_condition);
            eventRst = (TextView) view.findViewById(R.id.txt_result);


        }
    }

}
