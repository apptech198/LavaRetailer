package com.apptech.lava_retailer.adapter;

import android.content.Context;
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
import com.apptech.lava_retailer.list.governate.GovernateList;
import com.apptech.lava_retailer.other.SessionManage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GovernateAdapter extends RecyclerView.Adapter<GovernateAdapter.Viewholder> implements Filterable {


    GovernateInterface governateInterface;
    List<GovernateList> governatelist;
    List<GovernateList> ListAll = new ArrayList<>();
    SessionManage sessionManage;
    Context context;

    public GovernateAdapter(GovernateInterface governateInterface, List<GovernateList> governatelist) {
        this.governateInterface = governateInterface;
        this.governatelist = governatelist;
        ListAll.addAll(governatelist);
    }

    @NonNull
    @Override
    public GovernateAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_smart_select , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull GovernateAdapter.Viewholder holder, int position) {
        GovernateList l = governatelist.get(position);

//        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//            holder.textView.setText(l.getName());
//        } else {
//            holder.textView.setText(l.getName_ar());
//        }

        switch (sessionManage.getUserDetails().get("LANGUAGE")){
            case "en":
                holder.textView.setText(l.getName());
                break;
            case "fr":
                if(l.getName_fr().isEmpty()){
                    holder.textView.setText(l.getName());
                }else {
                    holder.textView.setText(l.getName_fr());
                }
                break;
            case "ar":
                holder.textView.setText(l.getName_ar());
                break;
        }



        holder.mainLayout.setOnClickListener(v -> governateInterface.OnItemClick(l));
    }

    @Override
    public int getItemCount() {
        return governatelist.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<GovernateList> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(ListAll);
            } else {

                    switch (sessionManage.getUserDetails().get("LANGUAGE")){
                        case "en":
                        case "fr":
                            for (GovernateList movie: ListAll) {
                                if (movie.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    filteredList.add(movie);
                                }
                            }
                            break;
//                        case "fr":
//                            if(l.getName_fr().isEmpty()){
//                                holder.textView.setText(l.getName());
//                            }else {
//                                holder.textView.setText(l.getName_fr());
//                            }
//                            break;
                        case "ar":
                            for (GovernateList movie: ListAll) {
                                if (movie.getName_ar().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                    filteredList.add(movie);
                                }
                            }
                            break;


//                    if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//                        if (movie.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
//                            filteredList.add(movie);
//                        }
//                    } else {
//                        if (movie.getName_ar().toLowerCase().contains(charSequence.toString().toLowerCase())) {
//                            filteredList.add(movie);
//                        }
//                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                governatelist.clear();
                governatelist.addAll((Collection<? extends GovernateList>) results.values);
                notifyDataSetChanged();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
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

    public interface  GovernateInterface {
        void OnItemClick(GovernateList list);
    }

}
