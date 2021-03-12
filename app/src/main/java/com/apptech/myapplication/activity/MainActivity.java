package com.apptech.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.ActivityMainBinding;
import com.apptech.myapplication.bottomsheet.BrandBottomSheetFragment;
import com.apptech.myapplication.fragment.check_entries.CheckEntriesFragment;
import com.apptech.myapplication.fragment.message_centre.MessageCentreFragment;
import com.apptech.myapplication.fragment.passbook.PassbookFragment;
import com.apptech.myapplication.fragment.pending.PendingFragment;
import com.apptech.myapplication.fragment.price_drop.PriceDropFragment;
import com.apptech.myapplication.fragment.purchase_request.PurchaseRequestFragment;
import com.apptech.myapplication.fragment.schemes.SchemesFragment;
import com.apptech.myapplication.fragment.sellout.SellOutFragment;
import com.apptech.myapplication.fragment.warranty.WarrantyFragment;
import com.apptech.myapplication.modal.MenuModel;
import com.apptech.myapplication.modal.brand.BrandList;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  , BrandBottomSheetFragment.BrandClick {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetDialogFragment bottomSheetDialogFragment;

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        binding.appBarMain.sideBarIcon.setOnClickListener(v -> {
            openDrawer();
        });


        prepareMenuData();
        populateExpandableList();

        navigationView.setNavigationItemSelectedListener(this);
        binding.appBarMain.brandName.setOnClickListener(v -> {
//            bottomSheetDialog = new BottomSheetDialog(this, R.style.DialogStyle);
//
//            View view1 = LayoutInflater.from(this).inflate(R.layout.row_brand_bottom_sheet, null);
//            bottomSheetDialog.setContentView(view1);
//            bottomSheetDialog.show();

             bottomSheetDialogFragment = new BrandBottomSheetFragment(this);
            bottomSheetDialogFragment.show(getSupportFragmentManager() , "");

        });

    }


    private void openDrawer() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void prepareMenuData() {


        MenuModel menuModel = new MenuModel("Message Centre", true, false, "MESSAGE_CENTRE");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


        menuModel = new MenuModel("IMEI Enter", true, true, ""); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel("Sell Out", false, false, "SELL_OUT");
        childModelsList.add(childModel);

        childModel = new MenuModel("Price Drop", false, false, "PRICE_DROP");
        childModelsList.add(childModel);

        childModel = new MenuModel("Pending", false, false, "PENDING");
        childModelsList.add(childModel);

        childModel = new MenuModel("Warranty", false, false, "WARRANTY");
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

//        childModelsList = new ArrayList<>();
//        menuModel = new MenuModel("Check Entries", true, true, ""); //Menu of Python Tutorials
//        headerList.add(menuModel);
//        childModel = new MenuModel("Sell Out", false, false, "");
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Price Drop", false, false, "");
//        childModelsList.add(childModel);
//
//        if (menuModel.hasChildren) {
//            childList.put(menuModel, childModelsList);
//        }


        menuModel = new MenuModel("Check Entries", true, false, "CHECK_ENTRIES");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


        menuModel = new MenuModel("Passbook", true, false, "PASSBOOK");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel("Purchase Request", true, false, "PURCHASE_REQUEST");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel("Schemes", true, false, "SCHEMES");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


    }


    private void populateExpandableList() {

        expandableListAdapter = new com.apptech.myapplication.adapter.ExpandableListAdapter(this, headerList, childList);
        binding.expandableListView.setAdapter(expandableListAdapter);

        binding.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {

            if (headerList.get(groupPosition).isGroup) {

                Log.e("aa1", headerList.get(groupPosition).fragmentname);

                if (!headerList.get(groupPosition).hasChildren) {

                    switch (headerList.get(groupPosition).fragmentname) {
                        case "CHECK_ENTRIES":
                            loadfragment(new CheckEntriesFragment());
                            binding.appBarMain.Actiontitle.setText("Check Entry");
                            break;
                        case "PURCHASE_REQUEST":
                            loadfragment(new PurchaseRequestFragment());
                            binding.appBarMain.Actiontitle.setText("Purchase Request");
                            break;
                        case "SCHEMES":
                            loadfragment(new SchemesFragment());
                            binding.appBarMain.Actiontitle.setText("Schemes");
                            break;
                        case "MESSAGE_CENTRE":
                            loadfragment(new MessageCentreFragment());
                            binding.appBarMain.Actiontitle.setText("Message Centre");
                            break;
                        case "PASSBOOK":
                            loadfragment(new PassbookFragment());
                            binding.appBarMain.Actiontitle.setText("Passbook");
                            break;
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START);

                }
            }

            return false;
        });

        binding.expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            if (childList.get(headerList.get(groupPosition)) != null) {
                MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);

                Log.e("aa", model.fragmentname);

                if (!model.fragmentname.isEmpty()) {
                    switch (model.fragmentname) {
                        case "SELL_OUT":
                            loadfragment(new SellOutFragment());
                            binding.appBarMain.Actiontitle.setText("Sell Out");
                            break;
                        case "PRICE_DROP":
                            loadfragment(new PriceDropFragment());
                            binding.appBarMain.Actiontitle.setText("Price Drop");
                            break;
                        case "PENDING":
                            loadfragment(new PendingFragment());
                            binding.appBarMain.Actiontitle.setText("Pending");
                            break;
                        case "WARRANTY":
                            loadfragment(new WarrantyFragment());
                            binding.appBarMain.Actiontitle.setText("Warranty");
                            break;
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }

//                    if (model.url.length() > 0) {
//                        WebView webView = findViewById(R.id.webView);
//                        webView.loadUrl(model.url);
//                        onBackPressed();
//                    }
            }

            return false;
        });
    }


    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).addToBackStack(null).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadfragment(new MessageCentreFragment());
        binding.appBarMain.Actiontitle.setText("Message Centre");
        binding.drawerLayout.openDrawer(GravityCompat.START);

    }

    @Override
    public void sendMainBrand(BrandList list) {
        binding.appBarMain.brandName.setText(list.getName());
        bottomSheetDialogFragment.dismiss();
    }
}




















