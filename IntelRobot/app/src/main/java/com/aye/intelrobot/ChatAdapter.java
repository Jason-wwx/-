package com.aye.intelrobot;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<ChatBean> chatBeanList;

    public ChatAdapter(Context context, List<ChatBean> chatBeanList) {
        this.context = context;
        this.chatBeanList = chatBeanList;
    }

    @Override
    public int getCount() {
        return chatBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        ChatBean chatBean = chatBeanList.get(position);
            if (chatBean.getState()==ChatBean.SEND){
                convertView=View.inflate(context,R.layout.list_item_left,null);
            }else {
                convertView=View.inflate(context,R.layout.list_item_right,null);
            }
            viewHolder.tvContent=convertView.findViewById(R.id.tv_chat_content);
            viewHolder.tvContent.setText(chatBean.getMessage());
            return convertView;
    }
    class ViewHolder{
        TextView tvContent;
    }
}
