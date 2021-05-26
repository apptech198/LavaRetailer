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
import com.apptech.lava_retailer.list.country.Country_list;
import com.apptech.lava_retailer.other.SessionManage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CountryAdapter  extends RecyclerView.Adapter<CountryAdapter.Viewholder> implements Filterable {

    List<Country_list> countryLists;
    List<Country_list> AllcountryLists;

    CountryInterface countryInterface;
    SessionManage sessionManage;
    Context context;
    private static final String TAG = "CountryAdapter";

    public CountryAdapter(List<Country_list> countryLists , CountryInterface countryInterface) {
        this.countryLists = countryLists;
        this.countryInterface = countryInterface;
        AllcountryLists = new ArrayList<>(countryLists);
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

        Country_list l = countryLists.get(position);


        switch (sessionManage.getUserDetails().get("LANGUAGE")){
            case "en":
                holder.textView.setText(l.getName());
                break;
            case "fr":
                if(countryLists.get(0).getName_fr().isEmpty()){
                    holder.textView.setText(l.getName());
                }else {
                    holder.textView.setText(l.getName_fr());
                }
                break;
            case "ar":
                holder.textView.setText(l.getName_ar());
                break;
        }



//        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//            holder.textView.setText(l.getName());
//        } else if(sessionManage.getUserDetails().get("LANGUAGE").equals("en")){
//            holder.textView.setText(l.getName());
//        }else {
//            holder.textView.setText(l.getName_ar());
//        }

        holder.mainLayout.setOnClickListener(v -> countryInterface.OnItemClick(holder.textView.getText().toString() , l));
    }

    @Override
    public int getItemCount() {
        return countryLists.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Country_list> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(AllcountryLists);
            } else {

                try {
                    if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
                        for (Country_list movie: AllcountryLists) {
                            if (movie.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                filteredList.add(movie);
                            }
                        }
                    } else {
                        for (Country_list movie: AllcountryLists) {
                            if (movie.getName_ar().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                filteredList.add(movie);
                            }
                        }
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(TAG, "performFiltering: " + e.getMessage() );
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countryLists.clear();
            countryLists.addAll((Collection<? extends Country_list>) results.values);
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

    public interface CountryInterface{
        void OnItemClick(String text , Country_list list);
    }




}








































