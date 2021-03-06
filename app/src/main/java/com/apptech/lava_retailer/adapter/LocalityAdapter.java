package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.list.LocalityList;
import com.apptech.lava_retailer.other.SessionManage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocalityAdapter extends RecyclerView.Adapter<LocalityAdapter.Viewholder> implements Filterable {

    LocalityInterface localityInterface;
    List<LocalityList> localityList;
    List<LocalityList>ListAll = new ArrayList<>();
    SessionManage sessionManage;
    Context context;
    private static final String TAG = "LocalityAdapter";


    public LocalityAdapter(LocalityInterface localityInterface, List<LocalityList> localityList) {
        this.localityInterface = localityInterface;
        this.localityList = localityList;
        ListAll.addAll(localityList);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_smart_select , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        LocalityList list = localityList.get(position);

//        if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//            holder.textView.setText(list.getName_ar());
//        } else {
//            holder.textView.setText(list.getName());
//        }

        switch (sessionManage.getUserDetails().get("LANGUAGE")){
            case "en":
                holder.textView.setText(list.getName());
                break;
            case "fr":
                if(list.getName_fr().isEmpty()){
                    holder.textView.setText(list.getName());
                }else {
                    holder.textView.setText(list.getName_fr());
                }
                break;
            case "ar":
                holder.textView.setText(list.getName_ar());
                break;
        }

        holder.mainLayout.setOnClickListener(v -> localityInterface.OnItemClick(list));
    }

    @Override
    public int getItemCount() {
        return localityList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<LocalityList> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(ListAll);
            } else {

                try {

                    for (LocalityList movie: ListAll) {
//                        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//                            if (movie.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
//                                filteredList.add(movie);
//                            }
//                        } else {
//                            if (movie.getName_ar().toLowerCase().contains(charSequence.toString().toLowerCase())) {
//                                filteredList.add(movie);
//                            }
//                        }

                        switch (sessionManage.getUserDetails().get("LANGUAGE")){
                            case "en":
                            case "fr":
                                if (movie.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    filteredList.add(movie);
                                }
                                break;
//                            case "fr":
//                                if(list.getName_fr().isEmpty()){
//                                    if (movie.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
//                                        filteredList.add(movie);
//                                    }
//                                }else {
//                                    holder.textView.setText(list.getName_fr());
//                                }
//                                break;
                            case "ar":
                                if (movie.getName_ar().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    filteredList.add(movie);
                                }
                                break;
                        }


                    }

                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(TAG, "performFiltering: " + e.getMessage());
                }


            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            localityList.clear();
            localityList.addAll((Collection<? extends LocalityList>) results.values);
            notifyDataSetChanged();
        }
    };

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView textView;
        LinearLayout mainLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    public interface  LocalityInterface {
        void OnItemClick(LocalityList list);
    }


}







































