package com.apptech.lava_retailer.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.BrandsTopAdapter;
import com.apptech.lava_retailer.databinding.ActivityMainBinding;
import com.apptech.lava_retailer.list.brand.Brandlist;
import com.apptech.lava_retailer.list.country.Country_list;
import com.apptech.lava_retailer.ui.cart.CartFragment;
import com.apptech.lava_retailer.ui.language.LanguageChangeFragment;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.message_centre.MessageCentreFragment;
import com.apptech.lava_retailer.ui.order.order_status.OrderStatusFragment;
import com.apptech.lava_retailer.ui.order.place_order.PlaceOrderFragment;
import com.apptech.lava_retailer.ui.order.product_details.ProductDetailsFragment;
import com.apptech.lava_retailer.ui.passbook.PassbookFragment;
import com.apptech.lava_retailer.ui.price_drop.entry_pending_verification.EntryPendingVerificationFragment;
import com.apptech.lava_retailer.ui.price_drop.price_drop_entry.PriceDropEntryFragment;
import com.apptech.lava_retailer.ui.price_drop.reports.ReportsFragment;
import com.apptech.lava_retailer.ui.profile.ProfileFragment;
import com.apptech.lava_retailer.ui.sell_out.pending_verification.PendingVerificationFragment;
import com.apptech.lava_retailer.ui.sell_out.report_sell_out_entries.ReportSellOutEntriesFragment;
import com.apptech.lava_retailer.ui.sell_out.report_sell_out_report.ReportSellOutReportFragment;
import com.apptech.lava_retailer.ui.trade_program.TradeProgramFragment;
import com.apptech.lava_retailer.ui.trade_program.loyalty_scheme.LoyaltySchemeFragment;
import com.apptech.lava_retailer.ui.trade_program.price_list.PricelistFragment;
import com.apptech.lava_retailer.ui.trade_program.selling_program.SellingProgramFragment;
import com.apptech.lava_retailer.ui.trade_program.sellout_program.SellOutProgramFragment;
import com.apptech.lava_retailer.ui.warranty.ocr_warranty.OCR_WarrantyFragment;
import com.apptech.lava_retailer.modal.MenuModel;
import com.apptech.lava_retailer.other.LanguageChange;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.ui.warranty.pending_replacement_request.PendingReplacementRequestFragment;
import com.apptech.lava_retailer.ui.warranty.serialize.SerializeFragment;
import com.apptech.lava_retailer.ui.warranty.unserialize.UnSerializeFragment;
import com.apptech.lava_retailer.ui.warranty.warrenty_check.WarrentyCheckFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private ActivityMainBinding binding;
    ExpandableListAdapter expandableListAdapter;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private static final String TAG = "MainActivity";
    FragmentManager fragmentManager;
    SessionManage sessionManage;
    NavController navController;
    LavaInterface lavaInterface;
    BrandsTopAdapter.BrandTopInterfaces brandInterfaces;
    Fragment loadFragment;
    Dialog dialog;
    List<Brandlist> brandlists = new ArrayList<>();
    BrandsTopAdapter brandsTopAdapter;
    private boolean isFirstBackPressed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManage = SessionManage.getInstance(this);
        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            new LanguageChange(this, "en");
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            new LanguageChange(this, "fr");
        } else {
            new LanguageChange(this, "ar");
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_controller);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        TextView brand_name = findViewById(R.id.brand_name);
        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            brand_name.setText(sessionManage.getUserDetails().get("BRAND_NAME"));
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            if(sessionManage.getUserDetails().get(SessionManage.BRAND_NAME_FR)!=null){
                if(sessionManage.getUserDetails().get(SessionManage.BRAND_NAME_FR).isEmpty()){
                    brand_name.setText(sessionManage.getUserDetails().get("BRAND_NAME"));
                }else {
                    brand_name.setText(sessionManage.getUserDetails().get(SessionManage.BRAND_NAME_FR));
                }
            }else {
                brand_name.setText(sessionManage.getUserDetails().get("BRAND_NAME"));
            }


        } else {
            brand_name.setText(sessionManage.getUserDetails().get("BRAND_NAME_AR"));
        }

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);

        TextView name = headerView.findViewById(R.id.name);
        TextView mobile = headerView.findViewById(R.id.mobile);
        TextView Email = headerView.findViewById(R.id.Email);
        ImageView profile_image = headerView.findViewById(R.id.profile_image);
        ImageView check = headerView.findViewById(R.id.check);

        name.setText(sessionManage.getUserDetails().get("NAME"));
        mobile.setText(sessionManage.getUserDetails().get("MOBILE"));
        Email.setText(sessionManage.getUserDetails().get("EMAIL"));

        if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){
              check.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_yellow));
        }else {
            check.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green));
        }


        Glide.with(this).load(ApiClient.Image_URL + sessionManage.getUserDetails().get("USER_IMG")).placeholder(R.drawable.ic_user__1_).into(profile_image);

        fragmentManager = getSupportFragmentManager();

        binding.appBarMain.sideBarIcon.setOnClickListener(v -> {
            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){
                check.setImageDrawable(getResources().getDrawable(R.drawable.ic_circle_yellow));
            }else {
                check.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_green));
            }
            openDrawer();

        });


        prepareMenuData1();
        populateExpandableList1();

        navigationView.setNavigationItemSelectedListener(this);

        brandInterfaces = (list , text  , text_ar) -> {
            sessionManage.brandSelect(list.getId() , text , text_ar,list.getName_fr());


            if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
                binding.appBarMain.brandName.setText(list.getName());
            }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){

                if(sessionManage.getUserDetails().get(SessionManage.BRAND_NAME_FR)!=null){
                    if(sessionManage.getUserDetails().get(SessionManage.BRAND_NAME_FR).isEmpty()){
                        binding.appBarMain.brandName.setText(sessionManage.getUserDetails().get("BRAND_NAME"));
                    }else {
                        binding.appBarMain.brandName.setText(sessionManage.getUserDetails().get(SessionManage.BRAND_NAME_FR));
                    }
                }else {
                    binding.appBarMain.brandName.setText(sessionManage.getUserDetails().get("BRAND_NAME"));
                }
//                binding.appBarMain.brandName.setText(list.getName_fr());
            } else {
                binding.appBarMain.brandName.setText(list.getName_ar());
            }





            getCurrentVisibleFragment();

        };


        binding.appBarMain.brandName.setOnClickListener(v -> {
            BrandSelect();
        });

        versionControl();
        getCountry();


//        Log.e(TAG, "onCreate: " + sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK"));


        printHashKey(this);


//        sessionManage.clearaddcard();

        Log.e(TAG, "onCreate: " + "\u00a3" );
        Log.e(TAG, "onCreate: " + "\u20B9" );

    }


     public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void getCurrentVisibleFragment() {

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().getPrimaryNavigationFragment();
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
        loadFragment = fragmentManager.getPrimaryNavigationFragment();


        navController.popBackStack();




        if(loadFragment instanceof  MessageCentreFragment){
            navController.navigate(R.id.messageCentreFragment);
        }

        if(loadFragment instanceof ProfileFragment){
            navController.navigate(R.id.profileFragment);
        }

        if(loadFragment instanceof LanguageChangeFragment){
            navController.navigate(R.id.languageChangeFragment);
        }

        if(loadFragment instanceof ReportSellOutEntriesFragment){
            navController.navigate(R.id.reportSellOutEntriesFragment);
        }

        if(loadFragment instanceof PendingVerificationFragment){
            navController.navigate(R.id.pendingVerificationFragment);
        }


        if(loadFragment instanceof ReportSellOutReportFragment){
            navController.navigate(R.id.reportSellOutReportFragment);
        }

        if(loadFragment instanceof PriceDropEntryFragment){
            navController.navigate(R.id.priceDropEntryFragment);
        }

        if(loadFragment instanceof EntryPendingVerificationFragment){
            navController.navigate(R.id.entryPendingVerificationFragment);
        }

        if(loadFragment instanceof ReportsFragment){
            navController.navigate(R.id.reportsFragment);
        }

        if(loadFragment instanceof PassbookFragment){
            navController.navigate(R.id.passbookFragment);
        }

        if(loadFragment instanceof PlaceOrderFragment){
            navController.navigate(R.id.placeOrderFragment);
        }

        if(loadFragment instanceof OrderStatusFragment){
            navController.navigate(R.id.orderStatusFragment);
        }

        if(loadFragment instanceof PricelistFragment){
            navController.navigate(R.id.pricelistFragment);
        }

        if(loadFragment instanceof SellingProgramFragment){
            navController.navigate(R.id.sellingProgramFragment);
        }

        if(loadFragment instanceof SellOutProgramFragment){
            navController.navigate(R.id.sellOutProgramFragment);
        }

        if(loadFragment instanceof LoyaltySchemeFragment){
            navController.navigate(R.id.loyaltySchemeFragment);
        }

        if(loadFragment instanceof SerializeFragment){
            navController.navigate(R.id.serializeFragment);
        }
        if(loadFragment instanceof UnSerializeFragment){
            navController.navigate(R.id.unSerializeFragment);
        }

        if(loadFragment instanceof ProductDetailsFragment){
            navController.navigate(R.id.productDetailsFragment);
        }

        if(loadFragment instanceof TradeProgramFragment){
            navController.navigate(R.id.tradeProgramFragment);
        }

        if(loadFragment instanceof CartFragment){
            navController.navigate(R.id.cartFragment);
        }
        if(loadFragment instanceof PendingReplacementRequestFragment){
            navController.navigate(R.id.pendingreplacementFragmnet);
        }
//        if(loadFragment instanceof SerializeFragment){
//            navController.navigate(R.id.serializeFragment);
//        }
//        if(loadFragment instanceof UnSerializeFragment){
//            navController.navigate(R.id.unSerializeFragment);
//        }
        if(loadFragment instanceof WarrentyCheckFragment){
            navController.navigate(R.id.warrentycheckFragment);
        }


        Log.e(TAG, "getCurrentVisibleFragment: " + loadFragment.toString() );
        dialog.dismiss();





    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e(TAG, "onWindowFocusChanged: " + "sdcnsdjbcd" );
    }

    private void BrandSelect(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.brand_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        Typeface face = Typeface.createFromAsset(getAssets(), "font/lato_semi_bold.ttf");
//        LinearLayout linearLayout= dialog.findViewById(R.id.mainview);
        ImageView close= dialog.findViewById(R.id.close);
        RecyclerView BrandRecyclerView = dialog.findViewById(R.id.BrandRecyclerView);


            if (!brandlists.isEmpty()){
                brandsTopAdapter = new BrandsTopAdapter(brandlists , brandInterfaces);
                BrandRecyclerView.setAdapter(brandsTopAdapter);
                dialog.show();
            }   else {

                String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
                String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

                lavaInterface.Brand(sessionManage.getUserDetails().get(SessionManage.COUNTRY_NAME) , country_id , country_name).enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");

                            if(error.equalsIgnoreCase("false")){

                                JSONArray array = jsonObject.getJSONArray("list");

                                brandlists.clear();

                                for (int i=0 ; i < array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);
                                    brandlists.add(new Brandlist(object.optString("id")
                                            , object.optString("name")
                                            ,object.optString("time")
                                            ,object.optString("name_ar")
                                            , object.optString("img")
                                            , object.optString("name_fr")
                                    ));

                                    try {
                                        brandsTopAdapter = new BrandsTopAdapter(brandlists , brandInterfaces);
                                        BrandRecyclerView.setAdapter(brandsTopAdapter);
                                        dialog.show();
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                        Log.e(TAG, "onResponse: " + e.getMessage() );
                                    }

                                }
                                return;

                            }
                            Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                        Toast.makeText(MainActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
            }


        close.setOnClickListener(v -> {dialog.cancel();});


    }


    private void openDrawer() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void prepareMenuData1() {



//        profile

        MenuModel menuModel = new MenuModel(getResources().getString(R.string.Profile), true, false, "PROFILE_UPDATE");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


//        Message Centre
        menuModel = new MenuModel(getResources().getString(R.string.message_center), true, false, "MESSAGE_CENTRE");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


//      Sell Out
        menuModel = new MenuModel(getResources().getString(R.string.Sell_Through), true, true, "SELL_OUT"); //Menu of Java Tutorials
        headerList.add(menuModel);

        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel(getResources().getString(R.string.Sell_Through), false, false, "SELL_OUT_REPORT_SELL_OUT_ENTRIES");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Pending_Verification), false, false, "SELL_OUT_PENDING_VERIFICATION");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Dashboard), false, false, "SELL_OUT_REPORT_SELL_OUT_REPORT");
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel(getResources().getString(R.string.Price_Protection), true, true, "");  // CHECK_ENTRIES
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel(getResources().getString(R.string.Price_Drop_Entry), false, false, "PRICE_DROP_PRICE_DROP_ENTRY");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Entery_pending_verification), false, false, "PRICE_DROP_ENTERY_PENDING_VERIFICATION");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Report), false, false, "PRICE_DROP_REPORTS");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

//       Passbook
        menuModel = new MenuModel(getResources().getString(R.string.Claim_Statement), true, false, "PASSBOOK_PASSBOOK");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


//       Order
        menuModel = new MenuModel(getResources().getString(R.string.Buy_Product), true, true, "");  // PURCHASE_REQUEST
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel(getResources().getString(R.string.Place_Order), false, false, "ORDER_PLACE_ORDER");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.My_Orders), false, false, "ORDER_ORDER_STATUS");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


//       Trade  Schemes


        menuModel = new MenuModel(getResources().getString(R.string.Trade_Program), true, false, "TRADE_PROGRAM");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }



//        menuModel = new MenuModel(getResources().getString(R.string.trade_program), true, true, "");  //SCHEMES
//        headerList.add(menuModel);
//
//        childModelsList = new ArrayList<>();
//        childModel = new MenuModel("Price List", false, false, "TRADE_PROGRAM_PRICE_LIST");
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Selling Program", false, false, "TRADE_PROGRAM_SEELING_PROGRAM");
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Sellout Program", false, false, "TRADE_PROGRAM_SELLOUT_PROGRAM");
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Loyalty Scheme", false, false, "TRADE_PROGRAM_LOYALTY_SCHEME");
//        childModelsList.add(childModel);
//
//        if (menuModel.hasChildren) {
//            childList.put(menuModel, childModelsList);
//        }

 //        Warranty
        menuModel = new MenuModel(getResources().getString(R.string.Warranty), true, true, "WARRANTY");  //SCHEMES
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel(getResources().getString(R.string.Mobile_Phone), false, false, "");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Replacement_process), false, false, "SERIALIZE");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Pending_replace_process), false, false, "PENDING_REPLACEMENT_REQUEST");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Warranty), false, false, "WARRENTY_CHECK");
        childModelsList.add(childModel);


        childModel = new MenuModel(getResources().getString(R.string.Accessories), false, false, "");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Replacement_process), false, false, "UN_SERIALIZE");
        childModelsList.add(childModel);

        childModel = new MenuModel(getResources().getString(R.string.Pending_replace_process), false, false, "PENDING_REPLACEMENT_ACCESSORIE");
        childModelsList.add(childModel);




        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

//       Language
        menuModel = new MenuModel(getResources().getString(R.string.change_language), true, false, "CHANGE_LANGUAGE");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }



//       LOGOUT
        menuModel = new MenuModel(getResources().getString(R.string.logout), true, false, "LOGOUT");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().getPrimaryNavigationFragment();
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
        Fragment loginFragment = fragmentManager.getPrimaryNavigationFragment();
        loginFragment.onActivityResult(requestCode, resultCode, data);

    }



    private void populateExpandableList1() {

        expandableListAdapter = new com.apptech.lava_retailer.adapter.ExpandableListAdapter(this, headerList, childList);
        binding.expandableListView.setAdapter(expandableListAdapter);

        binding.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {

            if (headerList.get(groupPosition).isGroup) {


                Log.e("aa1", headerList.get(groupPosition).fragmentname);

                if (!headerList.get(groupPosition).hasChildren) {

                    switch (headerList.get(groupPosition).fragmentname) {
                        case "MESSAGE_CENTRE":
                            navController.navigate(R.id.messageCentreFragment);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "PASSBOOK_PASSBOOK":
                            navController.navigate(R.id.passbookFragment);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "TRADE_PROGRAM":
                            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("YES")) {
                                navController.navigate(R.id.tradeProgramFragment);
                                binding.drawerLayout.closeDrawer(GravityCompat.START);
                            }else {
                                int msg = R.string.profile_not_verify;
                                AlertDialogfailure(msg);
                            }
                            break;
                        case "CHANGE_LANGUAGE":
                            navController.navigate(R.id.languageChangeFragment);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "PROFILE_UPDATE":
                            navController.navigate(R.id.profileFragment);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "LOGOUT":
                            sessionManage.clearaddcard();;
                            sessionManage.BrandClear();
                            sessionManage.RemoveNotificationStore();
                            sessionManage.logout();
                            Intent intent = new Intent(this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                    }
//                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
            return false;
        });

        binding.expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            binding.expandableListView.setClickable(false);
            binding.expandableListView.setEnabled(false);

            if (childList.get(headerList.get(groupPosition)) != null) {
                MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);

                if (!model.fragmentname.isEmpty()) {
                    switch (model.fragmentname) {
                        case "SELL_OUT_REPORT_SELL_OUT_ENTRIES":
                            navController.navigate(R.id.reportSellOutEntriesFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "SELL_OUT_PENDING_VERIFICATION":
                            navController.navigate(R.id.pendingVerificationFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "SELL_OUT_REPORT_SELL_OUT_REPORT":
                            navController.navigate(R.id.reportSellOutReportFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "PRICE_DROP_PRICE_DROP_ENTRY":
                            navController.navigate(R.id.priceDropEntryFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "PRICE_DROP_ENTERY_PENDING_VERIFICATION":
                            navController.navigate(R.id.entryPendingVerificationFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "PRICE_DROP_REPORTS":
                            navController.navigate(R.id.reportsFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "PASSBOOK_PASSBOOK":
                            navController.navigate(R.id.passbookFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "ORDER_PLACE_ORDER":
                            navController.navigate(R.id.placeOrderFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "ORDER_ORDER_STATUS":
                            navController.navigate(R.id.orderStatusFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "TRADE_PROGRAM_PRICE_LIST":
                            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("YES")) {
                                navController.navigate(R.id.pricelistFragment);
                                binding.expandableListView.setClickable(true);
                                binding.expandableListView.setEnabled(true);
                                binding.drawerLayout.closeDrawer(GravityCompat.START);
                            }else {
                                int msg = R.string.profile_not_verify;
                                AlertDialogfailure(msg);

                            }
                            break;
                        case "TRADE_PROGRAM_SEELING_PROGRAM":
                            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("YES")) {
                                navController.navigate(R.id.sellingProgramFragment);
                                binding.expandableListView.setClickable(true);
                                binding.expandableListView.setEnabled(true);
                                binding.drawerLayout.closeDrawer(GravityCompat.START);
                            }else {
                                int msg = R.string.profile_not_verify;
                                AlertDialogfailure(msg);
                            }
                            break;
                        case "TRADE_PROGRAM_SELLOUT_PROGRAM":
                            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("YES")) {
                                navController.navigate(R.id.sellOutProgramFragment);
                                binding.expandableListView.setClickable(true);
                                binding.expandableListView.setEnabled(true);
                                binding.drawerLayout.closeDrawer(GravityCompat.START);
                            }else {
                                int msg = R.string.profile_not_verify;
                                AlertDialogfailure(msg);
                            }
                            break;
                        case "TRADE_PROGRAM_LOYALTY_SCHEME":
                            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("YES")) {
                                navController.navigate(R.id.loyaltySchemeFragment);
                                binding.expandableListView.setClickable(true);
                                binding.expandableListView.setEnabled(true);
                                binding.drawerLayout.closeDrawer(GravityCompat.START);
                            }else {
                                int msg = R.string.profile_not_verify;
                                AlertDialogfailure(msg);
                            }
                            break;
                        case "SERIALIZE":
                            navController.navigate(R.id.serializeFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "UN_SERIALIZE":
                            navController.navigate(R.id.unSerializeFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "WARRENTY_CHECK":
                            navController.navigate(R.id.warrentycheckFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "PENDING_REPLACEMENT_REQUEST":
                            navController.navigate(R.id.pendingreplacementFragmnet);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case "PENDING_REPLACEMENT_ACCESSORIE":
                            navController.navigate(R.id.pendingRequestAccesoriesFragment);
                            binding.expandableListView.setClickable(true);
                            binding.expandableListView.setEnabled(true);
                            binding.drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                    }
                    binding.expandableListView.setClickable(true);
                    binding.expandableListView.setEnabled(true);
                }
                binding.expandableListView.setClickable(true);
                binding.expandableListView.setEnabled(true);
            }
            binding.expandableListView.setClickable(true);
            binding.expandableListView.setEnabled(true);
            return false;
        });
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() != 0){
            super.onBackPressed();
        }else{
            if (isFirstBackPressed) {
//                super.onBackPressed();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
                finish();
                finishAffinity();
                System.exit(0);
            } else {
                if(navController.getBackStack().size() == 2){
                    isFirstBackPressed = true;
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isFirstBackPressed = false;
                        }
                    }, 1500);
                    return;
                }
                super.onBackPressed();
            }
        }

    }



    private void AlertDialogfailure(int msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_imei_not_exits , null );
        builder.setView(v);

        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);
        MaterialTextView des = v.findViewById(R.id.des);
        MaterialTextView Title = v.findViewById(R.id.Title);
        Title.setText("Alert");
        des.setText(msg);


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();
        });
        no.setOnClickListener(view -> {alertDialog.dismiss();});

    }


    private void getCountry(){


        lavaInterface.Country().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equalsIgnoreCase("false")) {

                        JSONArray array = jsonObject.getJSONArray("country_list");

                        String Countryname = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

                        for (int i=0; i < array.length(); i++){
                            JSONObject object = array.getJSONObject(i);

                            if(Countryname.trim().equalsIgnoreCase(object.getString("name").trim())){

                                String CountryActive = object.optString("active");

                                if(CountryActive.trim().equalsIgnoreCase("NO")){
                                    AlertDialog("We are not operating in this country right now");
                                    break;
                                }

                            }
                        }
                        return;
                    }
                    Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }



    private void AlertDialog(String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View v = LayoutInflater.from(this).inflate(R.layout.row_custom_alert_dialog , null );
        builder.setView(v);
        TextView Title = v.findViewById(R.id.Title);
        TextView des = v.findViewById(R.id.des);
        TextView Btn = v.findViewById(R.id.Btn);
        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);

        no.setVisibility(View.GONE);

        Title.setText("Alert");
        des.setText(msg);
        Btn.setText("Okey");

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();

            sessionManage.clearaddcard();;
            sessionManage.BrandClear();
            sessionManage.RemoveNotificationStore();
            sessionManage.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        });
        no.setOnClickListener(view -> alertDialog.dismiss());

    }



    private void versionControl() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int Androidversion = pInfo.versionCode;
            Log.e(TAG, "versionControl: " + Androidversion);

            lavaInterface.fetch_version().enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if(response.isSuccessful()) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String error = jsonObject.getString("error");
//                            String message = jsonObject.getString("message");

                            if (error.equalsIgnoreCase("false")) {

                                String data = jsonObject.getString("data");

                                JSONArray jsonArray = new JSONArray(data);
                                String version = jsonArray.getJSONObject(0).getString("current_version");
                                String type = jsonArray.getJSONObject(0).getString("type");

                                int ver = Integer.parseInt(version);

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                if (Androidversion < ver) {

                                    if (type.equalsIgnoreCase("FORCE")) {
                                        builder.setMessage("Your are using older version");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Open Playstore",
                                                (arg0, arg1) -> {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                                    finish();
                                                });

                                    } else {
                                        builder.setMessage("Newer version is available");
                                        builder.setNegativeButton("Close", (dialog, which) -> {
                                            builder.setMessage("Your are under Varification");
                                            dialog.dismiss();
                                        });
                                    }

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.show();

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }



}




























































