package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apptech.lava_retailer.modal.MenuModel;
import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.other.SessionManage;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;
    private static final String TAG = "ExpandableListAdapter";
    SessionManage sessionManage;

    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        sessionManage = SessionManage.getInstance(context);
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        ImageView imageView = convertView.findViewById(R.id.icon);
        ImageView dot = convertView.findViewById(R.id.dot);
        ImageView down = convertView.findViewById(R.id.Down);
        TextView txtListChild = convertView.findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        if(groupPosition==7) {
            switch (childPosition) {
                case 0:
                    down.setVisibility(View.VISIBLE);
                    Log.e(TAG, "getChildView: 1");
                    break;
                case 1:
                case 2:
                case 3:
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_brightness_1_24));
                    Log.e(TAG, "getChildView: 2");
                    break;
                case 4:
                    down.setVisibility(View.VISIBLE);
                    Log.e(TAG, "getChildView: 2");
                    break;
                case 5:
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_brightness_1_24));
                    Log.e(TAG, "getChildView: 2");
                    break;

            }
        }else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_arrow_forward_24));
            dot.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }



        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        ImageView imageView = convertView.findViewById(R.id.icon);
        ImageView dot = convertView.findViewById(R.id.dot);
        ImageView down = convertView.findViewById(R.id.Down);
        ProgressBar ProfileProgress = convertView.findViewById(R.id.ProfileProgress);
        MaterialTextView percent = convertView.findViewById(R.id.percent);
        LinearLayout percentview = convertView.findViewById(R.id.per_view);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);


        switch (groupPosition){
            case 0:
                imageView.setImageResource(R.drawable.ic_user__2_);
                percentview.setVisibility(View.VISIBLE);
                ProfileProgress.setProgress(sessionManage.GetProfilePercent());
                percent.setText(String.valueOf(sessionManage.GetProfilePercent())+"%");
                int a = 0;
                Log.e(TAG, "getGroupView: " + a );
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
                imageView.setImageResource(R.drawable.ic_envelope);
                break;
            case 5:
                imageView.setImageResource(R.drawable.ic_shopping_bags);
                break;
            case 7:
                imageView.setImageResource(R.drawable.ic_secure);
                break;
            case 8:
                imageView.setImageResource(R.drawable.ic_language);
                break;
            case 9:
                imageView.setImageResource(R.drawable.ic_logout);
                break;
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        switch (childPosition){
            case 0:

        }
        return true;

    }

}
