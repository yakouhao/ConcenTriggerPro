package com.ct.ct.concentrigger.Util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ct.ct.concentrigger.R;
import com.ct.ct.concentrigger.Model.Event;
import com.ct.ct.concentrigger.Model.Message;

import java.util.List;

/**
 * Created by holic on 2018/6/6.
 */

public class MsgAdapter extends BaseAdapter {
    private List<Message> msgList;


    public MsgAdapter(List<Message> eventList) {
        this.msgList = msgList;
    }


    //返回活动数
    public int getCount() {

        return msgList == null? 0: msgList.size();

    }


    public Object getItem(int position) {
        return msgList.get(position);
    }


    public long getItemId(int position) {
        //return eventList.get(position).getActivity_id();
        return 1;
    }


    //可以用了的单个列表页getView
    public View getView(int i, View view, ViewGroup viewGroup) {

        MsgAdapter.ViewHolder viewHolder;

        if (null == view) {
            view = View.inflate(viewGroup.getContext(), R.layout.listitem_msg_event, null);

            viewHolder = new MsgAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MsgAdapter.ViewHolder) view.getTag();
        }

        Message msgItem = msgList.get(i);


        viewHolder.eventTxt.setText(msgItem.getMsg_content());
        if(msgItem.getIsRead())
            viewHolder.pointImg.setImageResource(R.mipmap.ic_msg_read);
        else
            viewHolder.pointImg.setImageResource(R.mipmap.ic_msg);


        return view;
    }


    static class ViewHolder {

        ImageView pointImg;
        ImageView eventImg;
        TextView eventTxt;

        ViewHolder(View view) {

            pointImg = (ImageView) view.findViewById(R.id.img_read_point);
            eventImg = (ImageView) view.findViewById(R.id.img_event);
            eventTxt = (TextView) view.findViewById(R.id.txt_msg);


        }
    }
}
