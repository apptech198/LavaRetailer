package com.apptech.lava_retailer.Utils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.apptech.lava_retailer.modal.card.CardList;

import java.util.List;

public class CartDiffUtils extends DiffUtil.Callback {

    private List<CardList> oldList;
    private List<CardList> newList;

    public CartDiffUtils(List<CardList> oldList, List<CardList> newList) {
        this.newList = newList;
        this.oldList = oldList;
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
        return newList.get(newItemPosition).getId().equals(oldList.get(oldItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        final CardList oldPostingDetail = oldList.get(oldItemPosition);
        final CardList newPostingDetail = newList.get(newItemPosition);
        return oldPostingDetail.getMarketing_name().equals(newPostingDetail.getMarketing_name());
//        return oldPostingDetail.getName().equals(newEmployee.getName()) && newPostingDetail.getAddress().equals(newEmployee.getAddress());

    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        CardList oldItem = oldList.get(oldItemPosition);
        CardList newItem = newList.get(newItemPosition);


        Bundle diff = new Bundle();
        if (!newItem.getMarketing_name().equals(oldItem.getMarketing_name())) {
            diff.putString("name", newItem.getMarketing_name());
        }
//        if (!newItem.getAddress().equals(oldItem.getAddress())) {
//            diff.putString("address", newItem.getAddress());
//        }
        if (diff.size() == 0) {
            return null;
        }
        return diff;


//        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}































