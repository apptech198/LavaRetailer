package com.apptech.lava_retailer.ui.filter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.CategoryAdapter;
import com.apptech.lava_retailer.adapter.SellOutReportCategoryFilterAdapter;
import com.apptech.lava_retailer.adapter.SellOutReportModalFilterAdapter;
import com.apptech.lava_retailer.databinding.FragmentSellOutReportCategoryFilterBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.gms.common.api.Api;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellOutReportCategoryFilter extends BottomSheetDialogFragment implements SellOutReportCategoryFilterAdapter.OnItemClickCategoryInterface {


    FragmentSellOutReportCategoryFilterBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    ProgressDialog progressDialog;
    OnClickBackPress comodityLists;
    List<ComodityLists> categoryLists;
    JSONObject MainObject = new JSONObject();
    private static final String TAG = "SellOutReportCategoryFi";
    SellOutReportCategoryFilterAdapter sellOutReportCategoryFilterAdapter;
    SellOutReportCategoryFilterAdapter.OnItemClickCategoryInterface onItemClickInterface;
    boolean AllBTN_CLICK = false;


    public SellOutReportCategoryFilter(OnClickBackPress comodityLists , List<ComodityLists> categoryLists) {
        this.comodityLists = comodityLists;
        this.categoryLists = categoryLists;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentSellOutReportCategoryFilterBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManage = SessionManage.getInstance(getContext());
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

//        onItemClickInterface = new SellOutReportCategoryFilterAdapter.OnItemClickCategoryInterface() {
//            @Override
//            public void OnItemClick() {
//
//            }
//
//            @Override
//            public void AddItem(ComodityLists lists) {
//
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put(lists.getId() , lists.getId());
//                    jsonObject.put("name" , lists.getName());
//                    MainObject.put(lists.getId() , jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Log.e(TAG, "AddItem: " + MainObject);
//
//            }
//
//            @Override
//            public void RemoveItem(ComodityLists lists) {
//                MainObject.remove(lists.getId());
//                Log.e(TAG, "RemoveItem: " + MainObject.toString() );
//            }
//        };

//        if (new NetworkCheck().haveNetworkConnection(getActivity())){
//                getCategory();
//        }else {
//            Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
//        }


        sellOutReportCategoryFilterAdapter = new SellOutReportCategoryFilterAdapter(this , categoryLists , AllBTN_CLICK);
        binding.categoryRecyclerView.setAdapter(sellOutReportCategoryFilterAdapter);



        binding.checkBoxtAllClick.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(categoryLists.size() > 0){
                for (int i=0; i< categoryLists.size(); i++){
                    SellOutReportCategoryFilterAdapter.Viewholder  viewholder = (SellOutReportCategoryFilterAdapter.Viewholder) binding.categoryRecyclerView.findViewHolderForAdapterPosition(i);
                    CheckBox checkBox = viewholder.itemView.findViewById(R.id.CheckBtn);
                    checkBox.setChecked(isChecked);
                    ComodityLists lists = categoryLists.get(i);
                    if(isChecked){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(lists.getId() , lists.getId());
                            jsonObject.put("name" , lists.getName());
                            MainObject.put(lists.getId() , jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        MainObject.remove(lists.getId());
                    }
                }
            }
            binding.checkBoxtAllClick.setEnabled(true);
        });


        binding.Filterbtn.setOnClickListener(v -> comodityLists.OnClickItem(MainObject));

    }

    private void getCategory(){

        lavaInterface.SELL_OUT_CATEGORY_MODAL_FILTER().enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if(error.equalsIgnoreCase("FALSE")){

                        JSONArray comodity_list = jsonObject.getJSONArray("comodity_list");
                        for (int i=0; i<comodity_list.length(); i++){
                            JSONObject op = comodity_list.getJSONObject(i);
                            categoryLists.add(new ComodityLists(
                                    op.optString("id")
                                    ,op.optString("name")
                                    ,op.optString("name_ar")
                                    ,op.optString("name_fr")
                                    ,op.optString("brand_id")
                                    ,op.optString("brand_name")
                                    ,op.optString("form_type")
                                    ,op.optString("time")
                            ));
                        }
                        sellOutReportCategoryFilterAdapter = new SellOutReportCategoryFilterAdapter(onItemClickInterface , categoryLists , AllBTN_CLICK);
                        binding.categoryRecyclerView.setAdapter(sellOutReportCategoryFilterAdapter);

                        progressDialog.dismiss();
                        return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void AddItem(ComodityLists lists) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(lists.getId() , lists.getId());
            jsonObject.put("name" , lists.getName());
            MainObject.put(lists.getId() , jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RemoveItem(ComodityLists lists) {
        MainObject.remove(lists.getId());
    }


    public interface OnClickBackPress {
        void OnClickItem(JSONObject jsonObject);
    }


    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight - 400;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }



/*
    public class RecentItems {



        // This four methods are used for maintaining favorites.
        public void saveFavorites(Context context, List<Datum> favorites) {
            SharedPreferences settings;
            SharedPreferences.Editor editor;

            settings = context.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);
            editor = settings.edit();

            Gson gson = new Gson();
            String jsonFavorites = gson.toJson(favorites);

            editor.putString(FAVORITES, jsonFavorites);

            editor.commit();
        }

        public void addFavorite(Context context, Datum product) {
            if(!checkFavoriteItem(product, context)){
                List<Datum> favorites = getFavorites(context);
                if (favorites == null)
                    favorites = new ArrayList<Datum>();
                favorites.add(product);
                saveFavorites(context, favorites);
            }
        }

        public void removeFavorite(Context context, Datum product) {
            ArrayList<Datum> favorites = getFavorites(context);
            if (favorites != null) {
                favorites.remove(product);
                saveFavorites(context, favorites);
            }
        }

        public ArrayList<Datum> getFavorites(Context context) {
            SharedPreferences settings;
            List<Datum> favorites = new ArrayList<>();

            settings = context.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);

            if (settings.contains(FAVORITES)) {
                String jsonFavorites = settings.getString(FAVORITES, null);
                Gson gson = new Gson();
                Datum[] favoriteItems = gson.fromJson(jsonFavorites,
                        Datum[].class);

                favorites = Arrays.asList(favoriteItems);
                favorites = new ArrayList<Datum>(favorites);
            } else
                return (ArrayList<Datum>) favorites;

            return (ArrayList<Datum>) favorites;
        }




        public boolean checkFavoriteItem(Datum checkProduct,Context context) {
            SharedPreferences settings;
            SharedPreferences.Editor editor;
            settings = context.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);
            editor = settings.edit();
            boolean check = false;
            List<Datum> favorites = getFavorites(context);
            if (favorites != null) {
                for (Datum product : favorites) {
                    if (product.getId().equals(checkProduct.getId())) {
                        check = true;
                        break;
                    }
                }
                if(favorites.size()>4){
                    favorites.remove(0);
                    Gson gson = new Gson();
                    String jsonFavorites = gson.toJson(favorites);
                    editor.putString(FAVORITES, jsonFavorites);
                    editor.commit();
                }
            }
            return check;
        }

    }

 */

}




































