package com.apptech.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.apptech.myapplication.R;
import com.apptech.myapplication.bottomsheet.BrandBottomSheetFragment;
import com.apptech.myapplication.databinding.ActivityMainBinding;
import com.apptech.myapplication.fragment.check_entries.CheckEntriesFragment;
import com.apptech.myapplication.fragment.check_entries.sellout.CheckEntriesSellOutFragment;
import com.apptech.myapplication.fragment.check_entries_price_drop_valid_imei.CheckEntriesPriceDropValidImeiFragment;
import com.apptech.myapplication.fragment.language.LanguageChangeFragment;
import com.apptech.myapplication.fragment.message_centre.MessageCentreFragment;
import com.apptech.myapplication.fragment.passbook.PassbookFragment;
import com.apptech.myapplication.fragment.price_drop.PriceDropFragment;
import com.apptech.myapplication.fragment.product_details.ProductDetailsFragment;
import com.apptech.myapplication.fragment.purchase_request_now.PurchaseRequestNowFragment;
import com.apptech.myapplication.fragment.sellOut_PendingVerification.SellOut_PendingVerificationFragment;
import com.apptech.myapplication.fragment.sellout.SellOutFragment;
import com.apptech.myapplication.fragment.trade.loyalty_scheme.LoyaltySchemeTradeFragment;
import com.apptech.myapplication.fragment.trade.priceList.PriceListTradeFragment;
import com.apptech.myapplication.fragment.trade.selling_program.SellingProgramTradeFragment;
import com.apptech.myapplication.fragment.trade.sellout_program.SelloutProgramTradeFragment;
import com.apptech.myapplication.fragment.warranty.ReportsSellOutReportFragment;
import com.apptech.myapplication.fragment.warrantys.ocr_warranty.OCR_WarrantyFragment;
import com.apptech.myapplication.fragment.warrantys.warranty2.Warranty2Fragment;
import com.apptech.myapplication.modal.MenuModel;
import com.apptech.myapplication.modal.product.ProductList;
import com.apptech.myapplication.other.LanguageChange;
import com.apptech.myapplication.other.SessionManage;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BrandBottomSheetFragment.BrandClick,
        PurchaseRequestNowFragment.onbackFragmentLoad {


    private ActivityMainBinding binding;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    ExpandableListAdapter expandableListAdapter;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private static final String TAG = "MainActivity";
    FragmentManager fragmentManager;
    CheckEntriesPriceDropValidImeiFragment checkEntriesPriceDropValidImeiFragment;
    SessionManage sessionManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManage = SessionManage.getInstance(this);
        if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            new LanguageChange(this, "ar");
        } else {
            new LanguageChange(this, "en");
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        fragmentManager = getSupportFragmentManager();


        binding.appBarMain.sideBarIcon.setOnClickListener(v -> {
            openDrawer();
        });


        prepareMenuData();
        populateExpandableList();

        navigationView.setNavigationItemSelectedListener(this);
        binding.appBarMain.brandName.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BrandActivity.class));
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

            FragmentManager fm = getSupportFragmentManager();
            Fragment f = fm.findFragmentById(R.id.framelayout);
//            Log.e(TAG, "onBackPressed: " + fm.getBackStackEntryCount());
            if (fm.getBackStackEntryCount() > 1) {
//                Log.e(TAG, "onBackPressed: " +  checkEntriesPriceDropValidImeiFragment.getChildFragmentManager().getBackStackEntryCount());
                try {


                    if (checkEntriesPriceDropValidImeiFragment != null) {
                        if (checkEntriesPriceDropValidImeiFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                            checkEntriesPriceDropValidImeiFragment.getChildFragmentManager().popBackStackImmediate();
                            return;
                        }
                        fm.popBackStack();
                        return;
                    }
                    fm.popBackStack();
                } catch (IllegalStateException e) {
                    Log.e(TAG, "onBackPressed: " + e.getMessage());
                    fm.popBackStack();
                }
            } else {
                super.onBackPressed();
            }

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

//        Message Centre
        MenuModel menuModel = new MenuModel(getResources().getString(R.string.message_center), true, false, "MESSAGE_CENTRE");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

//      Sell Out
        menuModel = new MenuModel(getResources().getString(R.string.sell_outs), true, true, ""); //Menu of Java Tutorials
        headerList.add(menuModel);

        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel(getResources().getString(R.string.report_sell_out_entries), false, false, "SELL_OUT");
        childModelsList.add(childModel);

        childModel = new MenuModel("Pending Verification", false, false, "PRICE_DROP");
        childModelsList.add(childModel);

        childModel = new MenuModel("Reports sell out report", false, false, "WARRANTY");
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


//        Price Drop
        menuModel = new MenuModel(getResources().getString(R.string.price_drop1), true, true, "");  // CHECK_ENTRIES
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel("Price Drop Entry", false, false, "CHECK_ENTRIES_PRICE_DROP");
        childModelsList.add(childModel);

        childModel = new MenuModel("Entery pending verification", false, false, "PRICE_DROP_PENDING_VERIFICATION");
        childModelsList.add(childModel);

        childModel = new MenuModel("Reports", false, false, "");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

//       Passbook
        menuModel = new MenuModel(getResources().getString(R.string.passbook), true, false, "PASSBOOK");
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


//       Order
        menuModel = new MenuModel(getResources().getString(R.string.order), true, true, "");  // PURCHASE_REQUEST
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel("Place Order", false, false, "PURCHASE_REQUEST");
        childModelsList.add(childModel);

        childModel = new MenuModel("Order status", false, false, "ORDER_ORDER_STATUS");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


//       Trade  Schemes
        menuModel = new MenuModel(getResources().getString(R.string.trade_program), true, true, "TRADE_PROGRAME");  //SCHEMES
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel("Price List", false, false, "TRADE_PRICE_LIST");
        childModelsList.add(childModel);

        childModel = new MenuModel("Selling Program", false, false, "TRADE_SEELING_PROGRAM");
        childModelsList.add(childModel);

        childModel = new MenuModel("Sellout Program", false, false, "SELLOUT_PROGRAM");
        childModelsList.add(childModel);

        childModel = new MenuModel("Loyalty Scheme", false, false, "TRADE_LOYALTY_SCHEME");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

//        Warranty
        menuModel = new MenuModel("Warranty", true, true, "WARRANTY");  //SCHEMES
        headerList.add(menuModel);

        childModelsList = new ArrayList<>();
        childModel = new MenuModel("OCR", false, false, "OCR_WARRANTY");
        childModelsList.add(childModel);

        childModel = new MenuModel("Warranty 2", false, false, "WARRANTY_2");
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
                        case "MESSAGE_CENTRE":
                            loadfragment(new MessageCentreFragment());
                            binding.appBarMain.Actiontitle.setText("Message Centre");
                            break;
                        case "PASSBOOK":
                            loadfragment(new PassbookFragment());
                            binding.appBarMain.Actiontitle.setText("Passbook");
                            break;
                        case "CHANGE_LANGUAGE":
                            loadfragment(new LanguageChangeFragment());
                            binding.appBarMain.Actiontitle.setText("Language");
                            break;
                        case "LOGOUT":
                            sessionManage.logout();
                            Intent intent = new Intent(this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
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
                            binding.appBarMain.Actiontitle.setText("Report Sell out Entries");
                            break;
                        case "PRICE_DROP":
                            loadfragment(new PriceDropFragment());
                            binding.appBarMain.Actiontitle.setText("Pending Verification");
                            break;
                        case "WARRANTY":
                            loadfragment(new ReportsSellOutReportFragment());
                            binding.appBarMain.Actiontitle.setText("Reports sell out report");
                            break;
                        case "CHECK_ENTRIES_SELL_OUT":
                            loadfragment(new CheckEntriesSellOutFragment());
                            binding.appBarMain.Actiontitle.setText("Price Drop");
                            break;
                        case "CHECK_ENTRIES_PRICE_DROP":
//                            loadfragment(new CheckEntriesPriceDrop());
                            checkEntriesPriceDropValidImeiFragment = new CheckEntriesPriceDropValidImeiFragment();
                            loadfragment(checkEntriesPriceDropValidImeiFragment);
                            binding.appBarMain.Actiontitle.setText("Price Drop Entry");
                            break;
                        case "PURCHASE_REQUEST":
//                            loadfragment(new PurchaseRequestFragment());
                            loadfragment(new PurchaseRequestNowFragment(this));
                            binding.appBarMain.Actiontitle.setText("Place Order");
                            break;
                        case "ORDER_ORDER_STATUS":
//                            loadfragment(new OrderStatusFragment());
                            binding.appBarMain.Actiontitle.setText("Order Status");
                            break;
                        case "TRADE_PRICE_LIST":
                            loadfragment(new PriceListTradeFragment());
                            binding.appBarMain.Actiontitle.setText("Price List");
                            break;
                        case "TRADE_SEELING_PROGRAM":
                            loadfragment(new SellingProgramTradeFragment());
                            binding.appBarMain.Actiontitle.setText("Selling Program");
                            break;
                        case "SELLOUT_PROGRAM":
                            loadfragment(new SelloutProgramTradeFragment());
                            binding.appBarMain.Actiontitle.setText("Sellout Program");
                            break;
                        case "TRADE_LOYALTY_SCHEME":
                            loadfragment(new LoyaltySchemeTradeFragment());
                            binding.appBarMain.Actiontitle.setText("Loyalty scheme");
                            break;
                        case "OCR_WARRANTY":
                            loadfragment(new OCR_WarrantyFragment());
                            binding.appBarMain.Actiontitle.setText("OCR Warranty");
                            break;
                        case "WARRANTY_2":
                            loadfragment(new Warranty2Fragment());
                            binding.appBarMain.Actiontitle.setText("Warranty");
                            break;
                        case "PRICE_DROP_PENDING_VERIFICATION":
                            loadfragment(new SellOut_PendingVerificationFragment());
                            binding.appBarMain.Actiontitle.setText("Entery pending verification");
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
            fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).addToBackStack(null).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadfragment(new MessageCentreFragment());
        binding.appBarMain.Actiontitle.setText("Message Centre");
//        binding.drawerLayout.openDrawer(GravityCompat.START);

    }

    @Override
    public void sendMainBrand(com.apptech.myapplication.modal.brand.List list) {
        binding.appBarMain.brandName.setText(list.getName());
        bottomSheetDialogFragment.dismiss();
    }


    @Override
    public void onBack(ProductList list) {
        loadfragment(new ProductDetailsFragment(list));
    }


}




















