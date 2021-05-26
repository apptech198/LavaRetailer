package com.apptech.lava_retailer.Utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;


import com.apptech.lava_retailer.modal.notification_list.NotificationListShow;

import java.util.List;

public class MessageShowUtils extends DiffUtil.Callback {

    List<NotificationListShow> oldList;
    List<NotificationListShow> newList;



    public MessageShowUtils(List<NotificationListShow> newlist , List<NotificationListShow> oldlist) {
        this.oldList = oldlist;
        this.newList = newlist;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition) == oldList.get(oldItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
        return result == 0;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

//        com.apptech.myapplication.modal.notification_list.List newModel = newList.get(newItemPosition);
//        com.apptech.myapplication.modal.notification_list.List oldModel = oldList.get(oldItemPosition);
//
//
//        Bundle diff = new Bundle();
//
//        if(newModel.getHeading() != oldModel.getHeading()){
//            diff.putString("title" , newModel.getHeading());
//        }
//
//        if(newModel.get() != oldModel.getMsg()){
//            diff.putString("msg" , newModel.getMsg());
//        }

//        if (diff.size() == 0) {
//            return null;
//        }
//        return diff;
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}



































