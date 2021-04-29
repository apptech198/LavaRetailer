package com.apptech.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GovernateAdapter extends RecyclerView.Adapter<GovernateAdapter.Viewholder> implements Filterable {


    GovernateInterface governateInterface;
    List<String> governatelist;
    List<String>ListAll = new ArrayList<>();


    public GovernateAdapter(GovernateInterface governateInterface, List<String> governatelist) {
        this.governateInterface = governateInterface;
        this.governatelist = governatelist;
        ListAll.addAll(governatelist);
    }

    @NonNull
    @Override
    public GovernateAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_smart_select , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull GovernateAdapter.Viewholder holder, int position) {
        holder.textView.setText(governatelist.get(position));
        holder.mainLayout.setOnClickListener(v -> governateInterface.OnItemClick( holder.textView.getText().toString()));
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
            List<String> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(ListAll);
            } else {
                for (String movie: ListAll) {
                    if (movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(movie);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            governatelist.clear();
            governatelist.addAll((Collection<? extends String>) results.values);
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

    public interface  GovernateInterface {
        void OnItemClick(String text);
    }

}
