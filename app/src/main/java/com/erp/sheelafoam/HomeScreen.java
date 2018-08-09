package com.erp.sheelafoam;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.erp.sheelafoam.adapter.NavigationViewTop;
import com.erp.sheelafoam.adapter.NavigationViewTopEMAIL;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.fragment.ChangePassword;
import com.erp.sheelafoam.fragment.CompanyPerformanceFragmnt;
import com.erp.sheelafoam.fragment.DashBoard;
import com.erp.sheelafoam.fragment.LatestEventsFragment;
import com.erp.sheelafoam.fragment.LearningRef_Fragment;
import com.erp.sheelafoam.fragment.MyPerformanceFragment;
import com.erp.sheelafoam.fragment.MyTask;
import com.erp.sheelafoam.fragment.PollsFragment;
import com.erp.sheelafoam.fragment.SankalpStoriesFragment;
import com.erp.sheelafoam.fragment.TelephoneDirectoryFragment;
import com.erp.sheelafoam.fragment.UpcomingHolidaysFragment;
import com.erp.sheelafoam.fragment.UpdateProfileFragment;
import com.erp.sheelafoam.interfaces.VolleyCallback;
import com.erp.sheelafoam.model.HeaderModel;
import com.erp.sheelafoam.model.NavigationViewModel;
import com.erp.sheelafoam.model.NavigationViewModel_EMAIL;
import com.erp.sheelafoam.model.SubmitPollsModel;
import com.erp.sheelafoam.sheelafoam.activity.CheckoutActivity;
import com.erp.sheelafoam.sheelafoam.activity.DocumentUploadNewActivity;
import com.erp.sheelafoam.sheelafoam.activity.MTSReportActivity;
import com.erp.sheelafoam.sheelafoam.activity.SupportActivity;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ConsumerOredrActivity;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ExchangeSchameActivity;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ProductMainActivity;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderFragment;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderView;
import com.erp.sheelafoam.sheelafoam.qrcode.ScanQrCodeActivity;
import com.erp.sheelafoam.sheelafoam.report.ReportOrderStatusFragment;
import com.erp.sheelafoam.sheelafoam.services.BumperOfferService;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ComplaintNew;
import com.erp.sheelafoam.sheelafoam.xperia.activity.MrpCalculation;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.RecyclerItemClickListener;
import com.erp.sheelafoam.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.erp.sheelafoam.R.id.etMobileNo;
import static com.erp.sheelafoam.R.id.tv_consumerOrder;


public class HomeScreen extends AppCompatActivity implements NetworkTask.Result, View.OnClickListener, VolleyCallback {
    private final String drawer_list_text[] = {"Home"};
    private final String drawer_list_textfor_EMAIL[] = {"Home"};
    private final int drawer_list_icons[] = {R.drawable.home_icon};
    private final int drawer_list_iconsfor_EMAIL[] = {R.drawable.home_icon};
    public NavigationView navigationView;
    public TextView txt_title;
    public CircularImageView profile_image;
    RequestQueue queue;
    RecyclerView navigation_view_top;
    ImageView expand_list, collapse_list;
    String UserID, UserProfileImage, GreatPlusUserID, OP_USER_ROLENAME, UserToken, UserAuthType,
            UserDisplayName, User_opRoleNAme, User_empGroupCode, UserEmail, opRoleNAme, userType;
    Toolbar toolbar;
    ConnectionDetector cDetector;
    CoordinatorLayout home_crl;
    AlertDialogManager alert;
    String screenType;
    LinearLayoutManager layoutManager;
    LinearLayout log_out_linear, layoutSathiMenu;
    HeaderModel base;
    List<NLevelItem> list;
    ListView listView;
    RelativeLayout expandlayout, collapselayout;
    Context mContext;
    TextView helpSupport,tv_consumerOrder;
    String bName, bMobile, bUid, bId, sUid;
    String left_name, left_mobile, left_uid, left_id, short_left_uid;
    String right_name, right_mobile, right_uid, right_id, short_right_uid;
    String main_name = "", main_mobile = "", main_uid = "", main_id, short_main_uid;
    //    TextView perfectMatch;
    String olaAppLink = "com.olacabs.customer";
    String uberAppLink = "com.ubercab";
    String printAppLink = "jp.co.canon.oip.android.opal";
    String matrixAppLink = "com.matrixcomsec.varta.adr.activities";
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            ((NLevelAdapter) listView.getAdapter()).toggle(arg2);
            ((NLevelAdapter) listView.getAdapter()).getFilter().filter();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    setListViewHeightBasedOnChildren(listView);

                }
            }, 100);

        }
    };
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DashBoard dashBoard;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        StrictMode.VmPolicy policy = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(policy);
        try {
            getSharedPreferenceValues();
            init();
            navigationWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            //String temp_post_id = uri.getQueryParameter("left_data");
            String left_data = uri.getQueryParameter("left_data");
            String right_data = uri.getQueryParameter("right_data");
            String main_data = uri.getQueryParameter("main_data");
            Log.d("POST DATA", " " + left_data + "->" + right_data + "->" + main_data);
            try {
                String[] left = left_data.split("_");
                left_id = left[1];
                left_name = left[2];
                left_mobile = left[3];
                left_uid = left[4];
                String[] short_l = left_uid.split("-");
                if (short_l[0].equalsIgnoreCase("UPMM")) {
                    short_left_uid = "L-" + short_l[1] + " (S)";
                } else if (short_l[0].equalsIgnoreCase("SCUPMM")) {
                    short_left_uid = "L-" + short_l[1] + " (C)";
                }

            } catch (Exception lE) {
                lE.printStackTrace();
            }


            try {
                String[] right = right_data.split("_");
                right_id = right[1];
                right_name = right[2];
                right_mobile = right[3];
                Log.i("Right", right_mobile.toString());
                right_uid = right[4];
                String[] short_r = right_uid.split("-");
                if (short_r[0].equalsIgnoreCase("UPMM")) {
                    short_right_uid = "R-" + short_r[1] + " (S)";
                } else if (short_r[0].equalsIgnoreCase("SCUPMM")) {
                    short_right_uid = "R-" + short_r[1] + " (C)";
                }
            } catch (Exception rE) {
                rE.printStackTrace();
            }
            try {
                String[] main = main_data.split("_");
                main_id = main[1];
                main_name = main[2];
                main_mobile = main[3];
                main_uid = main[4];
                String[] short_m = main_uid.split("-");
                if (short_m[0].equalsIgnoreCase("UPMM")) {
                    short_main_uid = short_m[1] + " (S)";
                } else if (short_m[0].equalsIgnoreCase("SCUPMM")) {
                    short_main_uid = short_m[1] + " (C)";
                }


            } catch (Exception mE) {
                mE.printStackTrace();
            }
            if (main_name != "") {
                bId = main_id;
                bMobile = main_mobile;
                bName = main_name;
                bUid = main_uid;
                sUid = short_main_uid;
            } else if (left_id.equalsIgnoreCase(right_id)) {
                bMobile = left_mobile;
                bId = left_id;
                bName = left_name;
                bUid = left_uid;
                String[] short_name = left_uid.split("-");
                if (short_name[0].equalsIgnoreCase("UPMM")) {
                    sUid = short_name[1] + " (S)";
                } else if (short_name[0].equalsIgnoreCase("SCUPMM")) {
                    sUid = short_name[1] + " (C)";
                }

            } else {
                bId = left_id + "&" + right_id;
                bMobile = left_mobile + "&" + right_mobile;
                bName = left_name + "(L) & " + right_name + " (R)";
                bUid = left_uid + "(L) & (R)" + right_uid;
                sUid = short_left_uid + " & " + short_right_uid;
            }

            Bundle bundle = new Bundle();
//            bundle.putString("name", uri.getQueryParameter("name"));
//            bundle.putString("mobile", uri.getQueryParameter("mobile"));
//            bundle.putString("unique_code", uri.getQueryParameter("unique_code"));

            bundle.putString("b_id", bId);
            bundle.putString("name", bName);
            bundle.putString("mobile", bMobile);
            bundle.putString("unique_code", bUid);
            bundle.putString("short_unique_code", sUid);
            System.out.println("Anand bId for Web   "+bId+","+ bName +"," +bMobile+","+bUid);
            commonFragmentMethod(new ProductOrderFragment(), bundle, null);

        } else {
            dashBoard = new DashBoard();
            commonFragmentMethod(dashBoard, null, "DashBoard");
        }

        navigation_view_top.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (UserAuthType.equalsIgnoreCase("EMAIL")) {

                            switch (position) {
                                case 0:
                                    dashBoard = new DashBoard();
                                    commonFragmentMethod(dashBoard, null, "DashBoard");
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 1:
                                    commonFragmentMethod(new CompanyPerformanceFragmnt(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 2:
                                    commonFragmentMethod(new MyPerformanceFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 3:
                                    commonFragmentMethod(new PollsFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 4:
                                    commonFragmentMethod(new UpcomingHolidaysFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 5:
                                    commonFragmentMethod(new SankalpStoriesFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 6:
                                    commonFragmentMethod(new LatestEventsFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                            }
                        } else {
                            switch (position) {
                                case 0:
                                    dashBoard = new DashBoard();
                                    commonFragmentMethod(dashBoard, null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 1:
                                    commonFragmentMethod(new MyTask(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 2:
                                    commonFragmentMethod(new CompanyPerformanceFragmnt(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 3:
                                    commonFragmentMethod(new MyPerformanceFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 4:
                                    commonFragmentMethod(new PollsFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 5:
                                    commonFragmentMethod(new UpcomingHolidaysFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 6:
                                    commonFragmentMethod(new SankalpStoriesFragment(), null, "SankalpStoriesFragment");
                                    drawerLayout.closeDrawer(navigationView);
                                    break;
                                case 7:
                                    commonFragmentMethod(new LatestEventsFragment(), null, null);
                                    drawerLayout.closeDrawer(navigationView);
                                    break;

                            }
                        }
                    }

                }));

        if(!isMyServiceRunning(BumperOfferService.class)){
            startService(new Intent(HomeScreen.this,BumperOfferService.class));
        }
    }

    private void getSharedPreferenceValues() {
        UserID = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_AuthType);
        UserDisplayName = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_DisplayName);
        User_opRoleNAme = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_RoleName);
        User_empGroupCode = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_emp_grpCode);
        UserEmail = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_USerEmail);
        GreatPlusUserID = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_GrtPlusUserID);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_op_user_role_name);
        opRoleNAme = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_RoleName);
        userType = Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_User_Type);
        System.out.println("AnandToken" + UserToken +"," +User_opRoleNAme+ "," +opRoleNAme+","+userType);
    }

    private void callWSStepFirst() {
        /*if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(HomeScreen.this, "Network Connection", "Enable Your Internet Connection", false);
        else {*/
        try {
            JSONObject obj = new JSONObject();
            obj.put("uid", UserID);
            obj.put("token", UserToken);
            obj.put("mode", "step1");
            obj.put("auth_type", UserAuthType);
            obj.put("displayname", UserDisplayName);
            obj.put("op_role_name", opRoleNAme);
            obj.put("op_user_role_name", OP_USER_ROLENAME);
            obj.put("op_greatplus_user_id", GreatPlusUserID);
            obj.put("op_user_emp_group_code", User_empGroupCode);
            String url = Constant.WS_URL + Constant.WS_HOME;
            NetworkTask networkTask = new NetworkTask(HomeScreen.this, 1, obj.toString());
            networkTask.exposePostExecute(this);
            networkTask.execute(url);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //}
    }

    private ArrayList<NavigationViewModel> prepareData() {

        ArrayList<NavigationViewModel> topNavigationList = new ArrayList<>();
        if (drawer_list_icons != null && drawer_list_icons.length > 0)
            for (int i = 0; i < drawer_list_icons.length; i++) {
                NavigationViewModel navigationViewModel = new NavigationViewModel();
                navigationViewModel.setDrawer_list_text(drawer_list_text[i]);
                //navigationViewModel.setDrawer_list_icons(drawer_list_icons[i]);
                topNavigationList.add(navigationViewModel);
            }

        return topNavigationList;
    }

    private void init() {
        cDetector = new ConnectionDetector(HomeScreen.this);
        alert = new AlertDialogManager();
        toolbar = (Toolbar) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_left);
        listView = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<NLevelItem>();
        mContext = getApplicationContext();
        navigation_view_top = (RecyclerView) findViewById(R.id.navigation_view_top);
        toolbar = (Toolbar) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar);
        home_crl = (CoordinatorLayout) findViewById(R.id.home_crl);
        profile_image = (CircularImageView) findViewById(R.id.common_toolbar).findViewById(R.id.profile_image);
        txt_title = (TextView) findViewById(R.id.common_toolbar).findViewById(R.id.txt_title);

        log_out_linear = (LinearLayout) findViewById(R.id.log_out_linear);
        helpSupport = (TextView) findViewById(R.id.help_support);
       // tv_consumerOrder= (TextView) findViewById(R.id.tv_consumerOrder);
//        perfectMatch = (TextView) findViewById(R.id.perfetc_match);

        layoutSathiMenu = (LinearLayout) findViewById(R.id.layout_sathi_menu);
        if (userType.equalsIgnoreCase("SAATHI")) {
            layoutSathiMenu.setVisibility(View.VISIBLE);
        }
        //expand_list = (ImageView) findViewById(R.idxcc.expand_list);
        //collapse_list = (ImageView) findViewById(R.id.collapse_list);
        expandlayout = (RelativeLayout) findViewById(R.id.expandlayout);
        expandlayout.setVisibility(View.VISIBLE);
        //collapselayout = (RelativeLayout) findViewById(R.id.collapselayout);
        //collapselayout.setVisibility(View.VISIBLE);
        //collapselayout.setOnClickListener(this);
        //expandlayout.setOnClickListener(this);
        log_out_linear.setOnClickListener(this);

        helpSupport.setOnClickListener(this);
   // tv_consumerOrder.setOnClickListener(this);
        layoutSathiMenu.setOnClickListener(this);
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_DisplayName));
        // txt_title.setSelected(true);
        profile_image.setVisibility(View.VISIBLE);
        profile_image.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        navigation_view_top.setHasFixedSize(true);
        navigation_view_top.setLayoutManager(layoutManager);
        screenType = getIntent().getStringExtra("ScreenType");
        callWSStepFirst();
        try {
           // callViewMethod();
            callNotificationMethod();
        } catch (Exception e) {

        }

    }

    private void navigationWork() {
        if (UserAuthType.equalsIgnoreCase("EMAIL")) {
            ArrayList<NavigationViewModel_EMAIL> navigationViewModels_email = prepareData_EMAIL();
            NavigationViewTopEMAIL adapter = new NavigationViewTopEMAIL(getApplicationContext(), navigationViewModels_email);
            navigation_view_top.setAdapter(adapter);

        } else {
            ArrayList<NavigationViewModel> navigationViewModels = prepareData();
            NavigationViewTop adapter = new NavigationViewTop(getApplicationContext(), navigationViewModels);
            navigation_view_top.setAdapter(adapter);
        }


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(final View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
              /*  toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.close));
                // Navigation onClickLister
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawerLayout.closeDrawer(navigationView);
                    }
                });*/
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //  View headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        //   navigationView.addHeaderView(headerView);

        actionBarDrawerToggle.syncState();


        /*------------------------------------------------------------------------------------------------*/

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                Bundle bundle = new Bundle();
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                }
                return false;
            }
        });
    }

    private ArrayList<NavigationViewModel_EMAIL> prepareData_EMAIL() {
        ArrayList<NavigationViewModel_EMAIL> topNavigationList_email = new ArrayList<>();
        for (int i = 0; i < drawer_list_iconsfor_EMAIL.length; i++) {
            NavigationViewModel_EMAIL navigationViewModel_email = new NavigationViewModel_EMAIL();
            navigationViewModel_email.setDrawer_list_textfor_EMAIL((drawer_list_textfor_EMAIL[i]));
            //navigationViewModel_email.setDrawer_list_iconsfor_EMAIL(drawer_list_iconsfor_EMAIL[i]);
            topNavigationList_email.add(navigationViewModel_email);
        }
        return topNavigationList_email;
    }

    public void commonFragmentMethod(Fragment fragment, Bundle data, String TAG) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (data != null)
            fragment.setArguments(data);

        fragmentTransaction.replace(R.id.fragment_container, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        if (fragmentTransaction != null)
            fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fr = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fr instanceof DashBoard) {
            dialogWindow();
        } else if (fr instanceof ChangePassword) {
            commonFragmentMethod(new UpdateProfileFragment(), null, null);
        } else {
            dashBoard = new DashBoard();
            commonFragmentMethod(dashBoard, null, null);
        }
    }

    /* @Override
     public void onBackPressed() {
         Log.d("TAG",fragmentManager.getClass().getName().toString());
         if(fragmentManager.getClass().getName().equals("DashBoard")) { // here HomeFragment.class.getName() means from which faragment you actually want to exit
             finish();
         } else{
             dialogWindow();
         }
       *//*  FrameLayout fl = (FrameLayout) findViewById(R.id.fragment_container);
        if (fl.getChildCount() == 1) {
            dialogWindow();
            if (fl.getChildCount() == 0) {
                dialogWindow();
            }
        } else if (fl.getChildCount() == 0) {

        } else {
            super.onBackPressed();
        }*//*
    }
*/
    public void dialogWindow() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                HomeScreen.this);

        alertDialogBuilder
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isMyServiceRunning(BumperOfferService.class)){
                            stopService(new Intent(HomeScreen.this,BumperOfferService.class));
                        }
                        finish();
                        //  Animation RightSwipe = AnimationUtils.loadAnimation(HomeScreen.this, R.anim.right_slide);
                        // ScreenAnimation.startAnimation(RightSwipe);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        switch (id) {
            case 1:
                Gson gson = new Gson();
                base = gson.fromJson(object, HeaderModel.class);

                SharedPreferences sharedPreferences = HomeScreen.this.getSharedPreferences("Object", MODE_PRIVATE);
                Editor editor = sharedPreferences.edit();
                editor.putString("Data", object);
                editor.commit();


//                preference.setSaveObject(object.toString());

                if (base.getHeader_menu().size() > 0) {
                    try {

                        final LayoutInflater inflater = LayoutInflater.from(this);
                        for (int i = 0; i < base.getHeader_menu().size(); i++) {

                            final int m = i;
                            Log.d("NEW MENU", base.getHeader_menu().get(m).getCHILD_MENU());

                            final NLevelItem grandParent = new NLevelItem(new SomeObject(base.getHeader_menu().get(i).getCHILD_MENU(), base.getHeader_menu().get(i).getICON(), base.getHeader_menu().get(i).getSUB_MENU().size()), null, new NLevelView() {

                                @Override
                                public View getView(NLevelItem item) {
                                    View view = inflater.inflate(R.layout.header_list, null);
                                    TextView tv = (TextView) view.findViewById(R.id.textView);
                                    TextView home_divider = (TextView) view.findViewById(R.id.home_divider);

                                    final String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                                    ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);
                                    String image = (String) ((SomeObject) item.getWrappedObject()).getImage();
                                    if (image.length() > 0)
                                        Picasso.with(mContext).load(image).into(iv);
                                    tv.setText(name);
                                    //tv.setTypeface(null, Typeface.BOLD);

                                    if (base.getHeader_menu().size() - 1 == m) {
                                        home_divider.setVisibility(View.GONE);
                                    } else {
                                        home_divider.setVisibility(View.VISIBLE);
                                    }

                                    ImageView ivDropDownHeader = (ImageView) view.findViewById(R.id.dropdown_list);
                                    int size = (int) ((SomeObject) item.getWrappedObject()).getSize();
                                    Log.d("Sizevv", size + "");
                                    if (size > 0) {
                                        ivDropDownHeader.setVisibility(View.VISIBLE);
                                        tv.setClickable(false);
                                        tv.setFocusable(false);
                                    } else {
                                        ivDropDownHeader.setVisibility(View.GONE);
                                        tv.setTypeface(null, Typeface.NORMAL);
                                        tv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (base.getHeader_menu().get(m).getCHILD_MENU_VALUE() == null) {
                                                    if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("Learning References")) {
                                                        Bundle in = new Bundle();
                                                        in.putString("Flow_from", "Learning");
                                                        commonFragmentMethod(new LearningRef_Fragment(), in, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("Policies And Procedures")) {
                                                        Bundle in = new Bundle();
                                                        in.putString("Flow_from", "Policies");
                                                        commonFragmentMethod(new LearningRef_Fragment(), in, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("Document Box")) {
                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WS_URL + "document-box-login.php?uid=" + UserID + "&token=" + UserToken));
                                                        startActivity(browserIntent);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("Telephone Directory")) {
                                                        commonFragmentMethod(new TelephoneDirectoryFragment(), null, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    }
                                                } else {
                                                    if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("sankalp blogs")) {
                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(base.getHeader_menu().get(m).getCHILD_MENU_VALUE()));
                                                        startActivity(browserIntent);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getCHILD_MENU_VALUE().equalsIgnoreCase("inbox.login.php")) {
//                                                        Intent intent = new Intent(HomeScreen.this, NewWebViewActivity.class);
//                                                        intent.putExtra("WebUrl", Constant.WS_URL + base.getHeader_menu().get(m).getCHILD_MENU_VALUE() + "?uid=" + UserID + "&token=" + UserToken);
//                                                        intent.putExtra("title", base.getHeader_menu().get(m).getCHILD_MENU());
//                                                        startActivity(intent);

                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WS_URL + base.getHeader_menu().get(m).getCHILD_MENU_VALUE() + "?uid=" + UserID + "&token=" + UserToken));
                                                        startActivity(browserIntent);
                                                    } else {
                                                        Log.d("URLs==>", base.getHeader_menu().get(m).getCHILD_MENU_VALUE());
                                                        Intent in = new Intent(HomeScreen.this, HeaderMenuDesc_Web.class);
                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getCHILD_MENU_VALUE());
                                                        startActivity(in);
                                                    }
                                                }

                                            }
                                        });
                                    }
                                    return view;
                                }
                            });
                            list.add(grandParent);

                            int numChildren = base.getHeader_menu().get(i).getSUB_MENU().size();
                            if (numChildren > 0) {
                                // ivDropDownHeader.setVisibility(View.VISIBLE);
                            }
                            for (int j = 0; j < numChildren; j++) {
                                final int n = j;
                                Log.d("MENUSSS->", base.getHeader_menu().get(i).getSUB_MENU().get(j).getCHILD_MENU());
                                NLevelItem parent = new NLevelItem(new SomeObject(base.getHeader_menu().get(i).getSUB_MENU().get(j).getCHILD_MENU(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getICON(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().size()), grandParent, new NLevelView() {

                                    @Override
                                    public View getView(NLevelItem item) {
                                        View view = inflater.inflate(R.layout.parent_list, null);
                                        TextView tv = (TextView) view.findViewById(R.id.textView);

                                        final String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                                        tv.setText(name);
                                        ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);
                                        String image = (String) ((SomeObject) item.getWrappedObject()).getImage();
                                        Log.d("Image Icon", image);
                                        if (image.length() > 0)
                                            Picasso.with(mContext).load(image).into(iv);

                                        ImageView ivDropDownHeader = (ImageView) view.findViewById(R.id.dropdown_list);
                                        int size = (int) ((SomeObject) item.getWrappedObject()).getSize();
                                        Log.d("Sizexx", size + "");
                                        if (size > 0) {
                                            ivDropDownHeader.setVisibility(View.VISIBLE);
                                            tv.setTypeface(null, Typeface.BOLD);
                                        } else {
                                            ivDropDownHeader.setVisibility(View.GONE);
                                            tv.setTypeface(null, Typeface.NORMAL);
                                            tv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Toast.makeText(getApplicationContext(),name+n+"",Toast.LENGTH_SHORT).show();

                                                    /*Intent in = new Intent(HomeScreen.this, HeagitderMenuDesc_Web.class);
                                                    in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getLINK());
                                                    startActivity(in);*/

                                                    //TODO: Fragment if else required
//                                                    commonFragmentMethod(new frag(), null, null);
//                                                    drawerLayout.closeDrawer(navigationView);

                                                    if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Order Report")) {
                                                        commonFragmentMethod(new ReportOrderStatusFragment(), null, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("place order")) {
                                                        commonFragmentMethod(new ProductOrderFragment(), null, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("order status")) {
                                                        commonFragmentMethod(new ProductOrderView(), null, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Mrp Calculation")) {
                                                        Intent in = new Intent(HomeScreen.this, MrpCalculation.class);
                                                        startActivity(in);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Upload Document")) {
                                                        Intent inDocs = new Intent(HomeScreen.this, DocumentUploadNewActivity.class);
                                                        startActivity(inDocs);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Digital Payment")) {
                                                        Intent inDigital = new Intent(HomeScreen.this, CheckoutActivity.class);
                                                        startActivity(inDigital);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Exchange Offer")) {
                                                        Intent inDigital = new Intent(HomeScreen.this, ExchangeSchameActivity.class);
                                                        startActivity(inDigital);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Guarantee Registration")) {
                                                        Intent inGuarantee = new Intent(HomeScreen.this, ExchangeSchameActivity.class);
                                                        startActivity(inGuarantee);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Complaint")) {
                                                        Intent inComplaint = new Intent(HomeScreen.this, ComplaintNew.class);
                                                        startActivity(inComplaint);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Ola")) {

                                                        Intent intent = getPackageManager().getLaunchIntentForPackage(olaAppLink);
                                                        if (intent != null) {
                                                            // We found the activity now start the activity
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            // Bring user to the market or let them choose an app?
                                                            intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.setData(Uri.parse("market://details?id=" + olaAppLink));
                                                            startActivity(intent);
                                                        }
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Uber")) {
                                                        Intent intent = getPackageManager().getLaunchIntentForPackage(uberAppLink);
                                                        if (intent != null) {
                                                            // We found the activity now start the activity
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            // Bring user to the market or let them choose an app?
                                                            intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.setData(Uri.parse("market://details?id=" + uberAppLink));
                                                            startActivity(intent);
                                                        }
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Print")) {
                                                        Intent intent = getPackageManager().getLaunchIntentForPackage(printAppLink);
                                                        if (intent != null) {
                                                            // We found the activity now start the activity
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            // Bring user to the market or let them choose an app?
                                                            intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.setData(Uri.parse("market://details?id=" + printAppLink));
                                                            startActivity(intent);
                                                        }
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Matrix")) {
                                                        Intent intent = getPackageManager().getLaunchIntentForPackage(matrixAppLink);
                                                        if (intent != null) {
                                                            // We found the activity now start the activity
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            // Bring user to the market or let them choose an app?
                                                            intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.setData(Uri.parse("market://details?id=" + matrixAppLink));
                                                            startActivity(intent);
                                                        }
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Perfect Match")) {
                                                        drawerLayout.closeDrawer(navigationView);
                                                        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
                                                        String id = sharedPreferences.getString(Constant.Sp_DealerID, "");
//                                                        id= "1234";

                                                        try {
                                                            Log.d("AnandPerfect match", "https://greatplus.com/task_manager/perfect_match/form/" + id);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://greatplus.com/task_manager/perfect_match/form/" + id));
                                                        startActivity(intent);

                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Mts Report")) {
                                                        // String url = base.getHeader_menu().get(m).getSUB_MENU().get(n).getLINK()+"?uid="+UserID.toLowerCase()+"&token="+UserToken+"&auth_type="+UserAuthType;
                                                       /* Intent in = new Intent(HomeScreen.this, NewWebViewActivity.class);
                                                        in.putExtra("WebUrl", url);*/
                                                        Intent in = new Intent(HomeScreen.this, MTSReportActivity.class);
                                                        startActivity(in);

                                                    } else {

                                                        Intent in = new Intent(HomeScreen.this, HeaderMenuDesc_Web.class);
                                                        // Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.greatplus.com/"+base.getHeader_menu().get(m).getSUB_MENU().get(n).getLINK()));
                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getLINK());
                                                        startActivity(in);
                                                    }
                                                }
                                            });
                                        }


                                        return view;
                                    }
                                });

                                list.add(parent);
                                int grandChildren = base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().size();
                                for (int k = 0; k < grandChildren; k++) {
                                    final int o = k;
                                    Log.e("Child Menu", base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getCHILD_MENU());
                                    NLevelItem child = new NLevelItem(new SomeObject(base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getCHILD_MENU(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getICON(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().size()), parent, new NLevelView() {

                                        @Override
                                        public View getView(NLevelItem item) {
                                            View view = inflater.inflate(R.layout.child_list, null);
                                            TextView tv = (TextView) view.findViewById(R.id.textView);

                                            String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                                            tv.setText(name);
                                            ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);
                                            String image = (String) ((SomeObject) item.getWrappedObject()).getImage();
                                            if (image.length() > 0)
                                                Picasso.with(mContext).load(image).into(iv);

                                            ImageView ivDropDownHeader = (ImageView) view.findViewById(R.id.dropdown_list);
                                            int size = (int) ((SomeObject) item.getWrappedObject()).getSize();
                                            Log.d("Sizezz", size + "");
                                            if (size > 0) {
                                                ivDropDownHeader.setVisibility(View.VISIBLE);
                                                tv.setTypeface(null, Typeface.BOLD);
                                            } else {
                                                ivDropDownHeader.setVisibility(View.GONE);
                                                tv.setTypeface(null, Typeface.NORMAL);

                                                tv.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //----open--webView---------
                                                        System.out.println("HederName" + base.getHeader_menu().get(m).getSUB_MENU().get(n).getSUB_MENU().get(o).getLINK());
                                                        Intent in = new Intent(HomeScreen.this, NewWebViewActivity.class);
                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getSUB_MENU().get(o).getLINK() + "?email=" + UserEmail + "&token=" + UserToken);
                                                        startActivity(in);

                                                        //Toast.makeText(getApplicationContext(),name+n+"",Toast.LENGTH_SHORT).show();

//                                                        Intent in = new Intent(HomeScreen.this, HeaderMenuDesc_Web.class);
//                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getSUB_MENU().get(o).getLINK());
//                                                        startActivity(in);
                                                    }
                                                });
                                            }
                                            return view;
                                        }
                                    });

                                    list.add(child);

                                    int grandGrandChildren = base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().size();
                                    for (int l = 0; l < grandGrandChildren; l++) {
                                        final int p = l;
                                        NLevelItem childGrand = new NLevelItem(new SomeObject(base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().get(l).getCHILD_MENU(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().get(l).getICON(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().get(l).getSUB_MENU().size()), child, new NLevelView() {

                                            @Override
                                            public View getView(NLevelItem item) {
                                                View view = inflater.inflate(R.layout.grand_child_list, null);
                                                TextView tv = (TextView) view.findViewById(R.id.textView);

                                                String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                                                tv.setText(name);
                                                ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);

                                                String image = (String) ((SomeObject) item.getWrappedObject()).getImage();
                                                if (image.length() > 0)
                                                    Picasso.with(mContext).load(image).into(iv);

                                                ImageView ivDropDownHeader = (ImageView) view.findViewById(R.id.dropdown_list);
                                                int size = (int) ((SomeObject) item.getWrappedObject()).getSize();
                                                Log.d("Size//", size + "");
                                                if (size > 0) {
                                                    ivDropDownHeader.setVisibility(View.VISIBLE);
                                                    tv.setTypeface(null, Typeface.BOLD);
                                                } else {
                                                    ivDropDownHeader.setVisibility(View.GONE);
                                                    tv.setTypeface(null, Typeface.NORMAL);

                                                    tv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            //Toast.makeText(getApplicationContext(),name+n+"",Toast.LENGTH_SHORT).show();

                                                            Intent in = new Intent(HomeScreen.this, HeaderMenuDesc_Web.class);
                                                            in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getSUB_MENU().get(o).getSUB_MENU().get(p).getLINK());
                                                            startActivity(in);
                                                        }
                                                    });
                                                }
                                                return view;
                                            }
                                        });

                                        list.add(childGrand);
                                    }
                                }
                            }
                        }

                        NLevelAdapter adapter = new NLevelAdapter(HomeScreen.this, list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                    long arg3) {
                                //  Log.d("Clicked at : ",arg2+"");
                                //Toast.makeText(getApplicationContext(),arg2+"",Toast.LENGTH_SHORT).show();
                                ((NLevelAdapter) listView.getAdapter()).toggle(arg2);
                                ((NLevelAdapter) listView.getAdapter()).getFilter().filter();


                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        setListViewHeightBasedOnChildren(listView);

                                    }
                                }, 100);

                            }

                        });

                        setListViewHeightBasedOnChildren(listView);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //   Util.showSnackbar(Login.this, login_crl, base.getInfo().get(0).getMsg());
                }

                if (Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_AuthType).equals("OTP")) {
                    Picasso.with(HomeScreen.this).load(Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_userProfileImage)).into(profile_image);
                    Util.setSharedPrefrenceValue(HomeScreen.this, Constant.PREFS_NAME, Constant.Sp_userProfileImage, base.getProfile_image().get(0).getOp_user_profile_image().replaceAll(" ", "%20"));
                } else {
                    if (base.getProfile_image().size() > 0) {
                        Picasso.with(HomeScreen.this).load(base.getProfile_image().get(0).getOp_user_profile_image()).into(profile_image);
                        Util.setSharedPrefrenceValue(HomeScreen.this, Constant.PREFS_NAME, Constant.Sp_userProfileImage, base.getProfile_image().get(0).getOp_user_profile_image().replaceAll(" ", "%20"));
                    }
                }

                break;

            case 2:
                Gson gson1 = new Gson();
                SubmitPollsModel base = gson1.fromJson(object, SubmitPollsModel.class);
                if (base.getInfo().get(0).getStatus() == 1) {
                    Util.showSnackbar(HomeScreen.this, home_crl, base.getInfo().get(0).getMsg());

                    Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_USerEmail, null);
                    Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_UserID, null);
                    Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_UserToken, null);
                    Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_USerName, null);
                    Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.Sp_UserArea, null);

                    Intent login = new Intent(HomeScreen.this, Login.class);
                    startActivity(login);
                    finish();
                } else {
                    Util.showSnackbar(HomeScreen.this, home_crl, base.getInfo().get(0).getMsg());
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                commonFragmentMethod(new UpdateProfileFragment(), null, null);
                break;
            case R.id.log_out_linear:
                drawerLayout.closeDrawer(navigationView);
                callWSLogOut();
                break;
            case R.id.expandlayout:
                collapselayout.setVisibility(View.VISIBLE);
                expandlayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                break;
            case R.id.collapselayout:
                expandlayout.setVisibility(View.VISIBLE);
                collapselayout.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                break;
            case R.id.help_support:
                callSupportAcivity();
                drawerLayout.closeDrawer(navigationView);
                break;

       /*case R.id.tv_consumerOrder:

                drawerLayout.closeDrawer(navigationView);
              break;*/
            case R.id.layout_sathi_menu:
                startActivity(new Intent(HomeScreen.this, ScanQrCodeActivity.class));
                drawerLayout.closeDrawer(navigationView);
                break;
        }
    }

    private void callSupportAcivity() {
        startActivity(new Intent(HomeScreen.this, SupportActivity.class));
    }

    private void callWSLogOut() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(HomeScreen.this, "Network Connection", "Enable Your Internet Connection", false);
        else {
            SharedPreferences prefs = mContext.getSharedPreferences("Location",
                    Context.MODE_PRIVATE);
            Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            SharedPreferences sharedPreferences = HomeScreen.this.getSharedPreferences("Object", MODE_PRIVATE);
            Editor editor1 = sharedPreferences.edit();
            editor1.clear();
            editor1.commit();
            //String s = sharedPreferences.getString("Data", "hello");
            try {

                JSONObject obj = new JSONObject();
                obj.put("uid", UserID);
                obj.put("action", "logout");
                obj.put("auth_type", UserAuthType);
                obj.put("op_greatplus_user_id", GreatPlusUserID);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url = Constant.WS_URL + Constant.WS_LOG_OUT;
                NetworkTask networkTask = new NetworkTask(HomeScreen.this, 2, obj.toString());
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (dashBoard != null)
            dashBoard.notifyUploadActivity(requestCode, resultCode, data);
    }

    private void callViewMethod() {
        try {
            SharedPreferences sharedPreferences = HomeScreen.this.getSharedPreferences("Object", MODE_PRIVATE);
            String s = sharedPreferences.getString("Data", "hello");
            Log.d("Hello", s.toString());
            if (s.equalsIgnoreCase("hello")) {
                callWSStepFirst();
                System.out.println("service call");
            } else {
                System.out.println("service Not call");
                Gson gson = new Gson();
                base = gson.fromJson(s, HeaderModel.class);
                //  preference.setUsername(object);
                if (base.getHeader_menu().size() > 0) {
                    try {

                        final LayoutInflater inflater = LayoutInflater.from(this);
                        for (int i = 0; i < base.getHeader_menu().size(); i++) {

                            final int m = i;
                            Log.d("NEW MENU", base.getHeader_menu().get(m).getCHILD_MENU());

                            final NLevelItem grandParent = new NLevelItem(new SomeObject(base.getHeader_menu().get(i).getCHILD_MENU(), base.getHeader_menu().get(i).getICON(), base.getHeader_menu().get(i).getSUB_MENU().size()), null, new NLevelView() {

                                @Override
                                public View getView(NLevelItem item) {
                                    View view = inflater.inflate(R.layout.header_list, null);
                                    TextView tv = (TextView) view.findViewById(R.id.textView);
                                    TextView home_divider = (TextView) view.findViewById(R.id.home_divider);

                                    final String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                                    ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);
                                    String image = (String) ((SomeObject) item.getWrappedObject()).getImage();
                                    if (image.length() > 0)
                                        Picasso.with(mContext).load(image).into(iv);
                                    tv.setText(name);
                                    //tv.setTypeface(null, Typeface.BOLD);

                                    if (base.getHeader_menu().size() - 1 == m) {
                                        home_divider.setVisibility(View.GONE);
                                    } else {
                                        home_divider.setVisibility(View.VISIBLE);
                                    }

                                    ImageView ivDropDownHeader = (ImageView) view.findViewById(R.id.dropdown_list);
                                    int size = (int) ((SomeObject) item.getWrappedObject()).getSize();
                                    Log.d("Sizevv", size + "");
                                    if (size > 0) {
                                        ivDropDownHeader.setVisibility(View.VISIBLE);
                                        tv.setClickable(false);
                                        tv.setFocusable(false);
                                    } else {
                                        ivDropDownHeader.setVisibility(View.GONE);
                                        tv.setTypeface(null, Typeface.NORMAL);
                                        tv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (base.getHeader_menu().get(m).getCHILD_MENU_VALUE() == null) {
                                                    if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("Learning References")) {
                                                        Bundle in = new Bundle();
                                                        in.putString("Flow_from", "Learning");
                                                        commonFragmentMethod(new LearningRef_Fragment(), in, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("Policies And Procedures")) {
                                                        Bundle in = new Bundle();
                                                        in.putString("Flow_from", "Policies");
                                                        commonFragmentMethod(new LearningRef_Fragment(), in, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("Document Box")) {
                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WS_URL + "document-box-login.php?uid=" + UserID + "&token=" + UserToken));
                                                        startActivity(browserIntent);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("Telephone Directory")) {
                                                        commonFragmentMethod(new TelephoneDirectoryFragment(), null, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    }
                                                } else {
                                                    if (base.getHeader_menu().get(m).getCHILD_MENU().equalsIgnoreCase("sankalp blogs")) {
                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(base.getHeader_menu().get(m).getCHILD_MENU_VALUE()));
                                                        startActivity(browserIntent);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getCHILD_MENU_VALUE().equalsIgnoreCase("inbox.login.php")) {
                                                        //                                                        Intent intent = new Intent(HomeScreen.this, NewWebViewActivity.class);
                                                        //                                                        intent.putExtra("WebUrl", Constant.WS_URL + base.getHeader_menu().get(m).getCHILD_MENU_VALUE() + "?uid=" + UserID + "&token=" + UserToken);
                                                        //                                                        intent.putExtra("title", base.getHeader_menu().get(m).getCHILD_MENU());
                                                        //                                                        startActivity(intent);

                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WS_URL + base.getHeader_menu().get(m).getCHILD_MENU_VALUE() + "?uid=" + UserID + "&token=" + UserToken));
                                                        startActivity(browserIntent);
                                                    } else {
                                                        Log.d("URLs==>", base.getHeader_menu().get(m).getCHILD_MENU_VALUE());
                                                        Intent in = new Intent(HomeScreen.this, HeaderMenuDesc_Web.class);
                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getCHILD_MENU_VALUE());
                                                        startActivity(in);
                                                    }
                                                }

                                            }
                                        });
                                    }
                                    return view;
                                }
                            });
                            list.add(grandParent);

                            int numChildren = base.getHeader_menu().get(i).getSUB_MENU().size();
                            if (numChildren > 0) {
                                // ivDropDownHeader.setVisibility(View.VISIBLE);
                            }
                            for (int j = 0; j < numChildren; j++) {
                                final int n = j;
                                Log.d("MENUSSS->", base.getHeader_menu().get(i).getSUB_MENU().get(j).getCHILD_MENU());
                                NLevelItem parent = new NLevelItem(new SomeObject(base.getHeader_menu().get(i).getSUB_MENU().get(j).getCHILD_MENU(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getICON(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().size()), grandParent, new NLevelView() {

                                    @Override
                                    public View getView(NLevelItem item) {
                                        View view = inflater.inflate(R.layout.parent_list, null);
                                        TextView tv = (TextView) view.findViewById(R.id.textView);

                                        final String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                                        tv.setText(name);
                                        ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);
                                        String image = (String) ((SomeObject) item.getWrappedObject()).getImage();
                                        Log.d("Image Icon", image);
                                        if (image.length() > 0)
                                            Picasso.with(mContext).load(image).into(iv);

                                        ImageView ivDropDownHeader = (ImageView) view.findViewById(R.id.dropdown_list);
                                        int size = (int) ((SomeObject) item.getWrappedObject()).getSize();
                                        Log.d("Sizexx", size + "");
                                        if (size > 0) {
                                            ivDropDownHeader.setVisibility(View.VISIBLE);
                                            tv.setTypeface(null, Typeface.BOLD);
                                        } else {
                                            ivDropDownHeader.setVisibility(View.GONE);
                                            tv.setTypeface(null, Typeface.NORMAL);
                                            tv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //Toast.makeText(getApplicationContext(),name+n+"",Toast.LENGTH_SHORT).show();

                                                        /*Intent in = new Intent(HomeScreen.this, HeagitderMenuDesc_Web.class);
                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getLINK());
                                                        startActivity(in);*/

                                                    //TODO: Fragment if else required
                                                    //                                                    commonFragmentMethod(new ProductOrderFragment(), null, null);
                                                    //                                                    drawerLayout.closeDrawer(navigationView);

                                                    if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Order Report")) {
                                                        commonFragmentMethod(new ReportOrderStatusFragment(), null, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("place order")) {
                                                        commonFragmentMethod(new ProductOrderFragment(), null, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("order status")) {
                                                        commonFragmentMethod(new ProductOrderView(), null, null);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Mrp Calculation")) {
                                                        Intent in = new Intent(HomeScreen.this, MrpCalculation.class);
                                                        startActivity(in);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Upload Document")) {
                                                        Intent inDocs = new Intent(HomeScreen.this, DocumentUploadNewActivity.class);
                                                        startActivity(inDocs);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Digital Payment")) {
                                                        Intent inDigital = new Intent(HomeScreen.this, CheckoutActivity.class);
                                                        startActivity(inDigital);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Exchange Offer")) {
                                                        Intent inDigital = new Intent(HomeScreen.this, ExchangeSchameActivity.class);
                                                        startActivity(inDigital);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Guarantee Registration")) {
                                                        Intent inGuarantee = new Intent(HomeScreen.this, ExchangeSchameActivity.class);
                                                        startActivity(inGuarantee);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Complaint")) {
                                                        Intent inComplaint = new Intent(HomeScreen.this, ComplaintNew.class);
                                                        startActivity(inComplaint);
                                                        drawerLayout.closeDrawer(navigationView);
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Ola")) {

                                                        Intent intent = getPackageManager().getLaunchIntentForPackage(olaAppLink);
                                                        if (intent != null) {
                                                            // We found the activity now start the activity
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            // Bring user to the market or let them choose an app?
                                                            intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.setData(Uri.parse("market://details?id=" + olaAppLink));
                                                            startActivity(intent);
                                                        }
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Uber")) {
                                                        Intent intent = getPackageManager().getLaunchIntentForPackage(uberAppLink);
                                                        if (intent != null) {
                                                            // We found the activity now start the activity
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            // Bring user to the market or let them choose an app?
                                                            intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.setData(Uri.parse("market://details?id=" + uberAppLink));
                                                            startActivity(intent);
                                                        }
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Print")) {
                                                        Intent intent = getPackageManager().getLaunchIntentForPackage(printAppLink);
                                                        if (intent != null) {
                                                            // We found the activity now start the activity
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            // Bring user to the market or let them choose an app?
                                                            intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.setData(Uri.parse("market://details?id=" + printAppLink));
                                                            startActivity(intent);
                                                        }
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Matrix")) {
                                                        Intent intent = getPackageManager().getLaunchIntentForPackage(matrixAppLink);
                                                        if (intent != null) {
                                                            // We found the activity now start the activity
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            // Bring user to the market or let them choose an app?
                                                            intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.setData(Uri.parse("market://details?id=" + matrixAppLink));
                                                            startActivity(intent);
                                                        }
                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Perfect Match")) {
                                                        drawerLayout.closeDrawer(navigationView);
                                                        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
                                                        String id = sharedPreferences.getString(Constant.Sp_DealerID, "");
                                                        //                                                        id= "1234";
                                                        try {
                                                            Log.d("Anand_Perfect_match", "https://greatplus.com/task_manager/perfect_match/form/" + id);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://greatplus.com/task_manager/perfect_match/form/" + id));
                                                        startActivity(intent);

                                                    } else if (base.getHeader_menu().get(m).getSUB_MENU().get(n).getCHILD_MENU().equalsIgnoreCase("Mts Report")) {
                                                        // String url = base.getHeader_menu().get(m).getSUB_MENU().get(n).getLINK()+"?uid="+UserID.toLowerCase()+"&token="+UserToken+"&auth_type="+UserAuthType;
                                                           /* Intent in = new Intent(HomeScreen.this, NewWebViewActivity.class);
                                                            in.putExtra("WebUrl", url);*/
                                                        Intent in = new Intent(HomeScreen.this, MTSReportActivity.class);
                                                        startActivity(in);

                                                    } else {
                                                        Intent in = new Intent(HomeScreen.this, HeaderMenuDesc_Web.class);
                                                        // Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.greatplus.com/"+base.getHeader_menu().get(m).getSUB_MENU().get(n).getLINK()));
                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getLINK());
                                                        startActivity(in);
                                                    }
                                                }
                                            });
                                        }


                                        return view;
                                    }
                                });

                                list.add(parent);
                                int grandChildren = base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().size();
                                for (int k = 0; k < grandChildren; k++) {
                                    final int o = k;
                                    Log.e("Child Menu", base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getCHILD_MENU());
                                    NLevelItem child = new NLevelItem(new SomeObject(base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getCHILD_MENU(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getICON(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().size()), parent, new NLevelView() {

                                        @Override
                                        public View getView(NLevelItem item) {
                                            View view = inflater.inflate(R.layout.child_list, null);
                                            TextView tv = (TextView) view.findViewById(R.id.textView);

                                            String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                                            tv.setText(name);
                                            ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);
                                            String image = (String) ((SomeObject) item.getWrappedObject()).getImage();
                                            if (image.length() > 0)
                                                Picasso.with(mContext).load(image).into(iv);

                                            ImageView ivDropDownHeader = (ImageView) view.findViewById(R.id.dropdown_list);
                                            int size = (int) ((SomeObject) item.getWrappedObject()).getSize();
                                            Log.d("Sizezz", size + "");
                                            if (size > 0) {
                                                ivDropDownHeader.setVisibility(View.VISIBLE);
                                                tv.setTypeface(null, Typeface.BOLD);
                                            } else {
                                                ivDropDownHeader.setVisibility(View.GONE);
                                                tv.setTypeface(null, Typeface.NORMAL);

                                                tv.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //----open--webView-----------
                                                        Intent in = new Intent(HomeScreen.this, NewWebViewActivity.class);
                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getSUB_MENU().get(o).getLINK() + "?email=" + UserEmail + "&token=" + UserToken);
                                                        startActivity(in);

                                                        //Toast.makeText(getApplicationContext(),name+n+"",Toast.LENGTH_SHORT).show();

                                                        //                                                        Intent in = new Intent(HomeScreen.this, HeaderMenuDesc_Web.class);
                                                        //                                                        in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getSUB_MENU().get(o).getLINK());
                                                        //                                                        startActivity(in);
                                                    }
                                                });
                                            }
                                            return view;
                                        }
                                    });

                                    list.add(child);

                                    int grandGrandChildren = base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().size();
                                    for (int l = 0; l < grandGrandChildren; l++) {
                                        final int p = l;
                                        NLevelItem childGrand = new NLevelItem(new SomeObject(base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().get(l).getCHILD_MENU(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().get(l).getICON(), base.getHeader_menu().get(i).getSUB_MENU().get(j).getSUB_MENU().get(k).getSUB_MENU().get(l).getSUB_MENU().size()), child, new NLevelView() {

                                            @Override
                                            public View getView(NLevelItem item) {
                                                View view = inflater.inflate(R.layout.grand_child_list, null);
                                                TextView tv = (TextView) view.findViewById(R.id.textView);

                                                String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                                                tv.setText(name);
                                                ImageView iv = (ImageView) view.findViewById(R.id.image_indicator);

                                                String image = (String) ((SomeObject) item.getWrappedObject()).getImage();
                                                if (image.length() > 0)
                                                    Picasso.with(mContext).load(image).into(iv);

                                                ImageView ivDropDownHeader = (ImageView) view.findViewById(R.id.dropdown_list);
                                                int size = (int) ((SomeObject) item.getWrappedObject()).getSize();
                                                Log.d("Size//", size + "");
                                                if (size > 0) {
                                                    ivDropDownHeader.setVisibility(View.VISIBLE);
                                                    tv.setTypeface(null, Typeface.BOLD);
                                                } else {
                                                    ivDropDownHeader.setVisibility(View.GONE);
                                                    tv.setTypeface(null, Typeface.NORMAL);

                                                    tv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            //Toast.makeText(getApplicationContext(),name+n+"",Toast.LENGTH_SHORT).show();

                                                            Intent in = new Intent(HomeScreen.this, HeaderMenuDesc_Web.class);
                                                            in.putExtra("WebUrl", base.getHeader_menu().get(m).getSUB_MENU().get(n).getSUB_MENU().get(o).getSUB_MENU().get(p).getLINK());
                                                            startActivity(in);
                                                        }
                                                    });
                                                }
                                                return view;
                                            }
                                        });

                                        list.add(childGrand);
                                    }
                                }
                            }
                        }

                        NLevelAdapter adapter = new NLevelAdapter(HomeScreen.this, list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                    long arg3) {
                                //  Log.d("Clicked at : ",arg2+"");
                                //Toast.makeText(getApplicationContext(),arg2+"",Toast.LENGTH_SHORT).show();
                                ((NLevelAdapter) listView.getAdapter()).toggle(arg2);
                                ((NLevelAdapter) listView.getAdapter()).getFilter().filter();


                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        setListViewHeightBasedOnChildren(listView);

                                    }
                                }, 100);

                            }

                        });

                        setListViewHeightBasedOnChildren(listView);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //   Util.showSnackbar(Login.this, login_crl, base.getInfo().get(0).getMsg());
                }

                if (Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_AuthType).equals("OTP")) {
                    Picasso.with(HomeScreen.this).load(Util.getSharedPrefrenceValue(HomeScreen.this, Constant.Sp_userProfileImage)).into(profile_image);
                    Util.setSharedPrefrenceValue(HomeScreen.this, Constant.PREFS_NAME, Constant.Sp_userProfileImage, base.getProfile_image().get(0).getOp_user_profile_image().replaceAll(" ", "%20"));
                } else {
                    if (base.getProfile_image() != null && base.getProfile_image().size() > 0) {
                        Picasso.with(HomeScreen.this).load(base.getProfile_image().get(0).getOp_user_profile_image()).into(profile_image);
                        Util.setSharedPrefrenceValue(HomeScreen.this, Constant.PREFS_NAME, Constant.Sp_userProfileImage, base.getProfile_image().get(0).getOp_user_profile_image().replaceAll(" ", "%20"));
                    }
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void callNotificationMethod() throws JSONException {

    }

    @Override
    public void onSuccessResponse(String result) {

    }


    class SomeObject {
        public String name, image;
        public int size;

        public SomeObject(String name, String image, int size) {
            this.name = name;
            this.image = image;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public int getSize() {
            return size;
        }
    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
