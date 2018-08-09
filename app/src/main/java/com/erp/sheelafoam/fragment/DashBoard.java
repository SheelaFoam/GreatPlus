package com.erp.sheelafoam.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erp.sheelafoam.BE.HomeBE;
import com.erp.sheelafoam.BE.HomeSecondBE;
import com.erp.sheelafoam.BE.MenuItems;
import com.erp.sheelafoam.BE.OrderMenuItems;
import com.erp.sheelafoam.HeaderMenuDesc_Web;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.Picasso;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.UploadActivity;
import com.erp.sheelafoam.UserLogAPI;
import com.erp.sheelafoam.adapter.DashboardListingAdaper;
import com.erp.sheelafoam.adapter.DashboardMenuAdapter;
import com.erp.sheelafoam.adapter.InboxAdapter;
import com.erp.sheelafoam.adapter.MyAppointment_Adapter;
import com.erp.sheelafoam.adapter.MyPerformance_home_Adapter;
import com.erp.sheelafoam.adapter.OtherRequestAdapter;
import com.erp.sheelafoam.adapter.TaskAdapter;
import com.erp.sheelafoam.adapter.UpcomingHoliday_Adapter;
import com.erp.sheelafoam.adapter.YourOpinionAdapter;
import com.erp.sheelafoam.asynctask.NetworkTask;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.interfaces.ItemClickListener;
import com.erp.sheelafoam.model.HeaderModel;
import com.erp.sheelafoam.model.SubmitPollsModel;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderFragment;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.AppConstant;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyCustomAsyncTask;
import com.erp.sheelafoam.utils.AlertDialogManager;
import com.erp.sheelafoam.utils.CommonClass;
import com.erp.sheelafoam.utils.ConnectionDetector;
import com.erp.sheelafoam.utils.CustomDialog;
import com.erp.sheelafoam.utils.RecyclerItemClickListener;
import com.erp.sheelafoam.utils.URLS;
import com.erp.sheelafoam.utils.Util;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.TRANSPARENT;

public class DashBoard extends Fragment implements OnClickListener, NetworkTask.Result, YouTubePlayer.OnInitializedListener, ItemClickListener {
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int RECOVERY_REQUEST = 1;
    public static String TAG = "MainActivity";
    public static int scrollX = 0;
    public static int scrollY = -1;
    public CardView card_view_dashBord;
      String os_type,update_status,version_code,version_name,ver_update,date_time;
    JSONObject jsonObjectRequest, jsonObjectResponse;
    SlidingUpPanelLayout mLayout;
    ArrayList<MenuItems> menuItemLists = new ArrayList<>();
    ArrayList<OrderMenuItems> orderMenuItemLists = new ArrayList<>();
    LinearLayoutManager layoutManager, layoutManagerDashboard, menuLayout;
    RecyclerView facility_recycle, request_recycle, dashboard_listing, inbox_listing, task_listing, my_appointment_listing,
            opinion_listing, myperformance_listing, upcoming_holiday_listing, menu_grid;
    Handler handler = new Handler();
    ProgressBar Progressbar;
    TextView remaining_leave_txt;
    RelativeLayout nxt_img, previous_img;
    ConnectionDetector cDetector;
    AlertDialogManager alert;
    TextView txt_see_all, txt_approved, tv_welcome, tv_versionName, tv_versionNo;
    String versionNo;
    int versionCode;
    LinearLayout see_all_holiday;
    RelativeLayout dashboard_listing_layout, your_opinion_layout, inbox_layout, my_appointment_layout, dashboard_layout,
            my_performance_layout, my_task_layout, sankalp_stories_layout, latest_events_layout, company_performance_layout,
            upcoming_holidays_layout, menu_layout, dashboard_video_layout, relativeLayout_welcome;
    LinearLayout performance_layout, more_event, more_sankalp,
            holiday_layout, main_leave_layout, main_facilitylayout, main_request_layout;
    NestedScrollView scroll_view;
    HomeSecondBE base_;
    String UserID, UserToken, UserAuthType, UserDisplayName, opRoleNAme, OP_USER_ROLENAME, User_empGroupCode,
            UserEmail, GrtPlusUSerID, UserProfilePic, user_id, dealerID;
    Button btnSubmitVote;
    DashboardMenuAdapter dashBoardMenuadapter;
    YourOpinionAdapter yourOpinionAdapter;
    TextView question, txt_apply, events_name, event_date, event_matter, sankalp_story_date, sankalp_story_matter,
            sankalp_story_name, view_inbox;
    ImageView home_event_image, sankalp_image;
    float leave;
    float remaining_leave;
    LinearLayout facilities_request_layout, dragView;
    YouTubePlayerView youTubePlayerView;
    int stopPosition;
    YouTubePlayerFragment playerFragment;
    private SharedPreferences loginPref;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String mCurrentPhotoPath, marqueeValue;
    private PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            openCamera();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard
                , container, false);
        new UserLogAPI("Home Page", getActivity());
        MenuItems mNitms = new MenuItems();
        mNitms.setTxt_menu("Camera");
        mNitms.setTxt_img_url(null);
        mNitms.setWeb_url(null);
        //menuItemLists.add(mNitms);
        init(view);
        facility_recycle.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent in = new Intent(getActivity(), HeaderMenuDesc_Web.class);
                        in.putExtra("WebUrl", base_.getRequest_center_inner_menu().get(0).getFacilities().get(position).getLINK());
                        startActivity(in);
                    }
                }));

        request_recycle.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent in = new Intent(getActivity(), HeaderMenuDesc_Web.class);
                        in.putExtra("WebUrl", base_.getRequest_center_inner_menu().get(2).getOther_request().get(position).getLINK());
                        startActivity(in);
                    }
                }));


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean("op_update_yn", false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("A new update is available, kindly update the application!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }

                            dialog.dismiss();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }


        return view;
    }

    private void init(View view) {
        cDetector = new ConnectionDetector(getActivity());
        alert = new AlertDialogManager();
        getSharedPreferenceValues();
        YouTubeBaseActivity youTubeBaseActivity = new YouTubeBaseActivity();
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        facility_recycle = (RecyclerView) view.findViewById(R.id.facility_recycle);
        inbox_listing = (RecyclerView) view.findViewById(R.id.inbox_listing);
        dragView = (LinearLayout) view.findViewById(R.id.dragView);
        task_listing = (RecyclerView) view.findViewById(R.id.task_listing);
        my_appointment_listing = (RecyclerView) view.findViewById(R.id.my_appointment_listing);
        opinion_listing = (RecyclerView) view.findViewById(R.id.opinion_listing);
        myperformance_listing = (RecyclerView) view.findViewById(R.id.myperformance_listing);
        upcoming_holiday_listing = (RecyclerView) view.findViewById(R.id.upcoming_holiday_listing);
        request_recycle = (RecyclerView) view.findViewById(R.id.request_recycle);
        dashboard_listing = (RecyclerView) view.findViewById(R.id.dashboard_listing);
        nxt_img = (RelativeLayout) view.findViewById(R.id.nxt_img);
        question = (TextView) view.findViewById(R.id.question);
        txt_apply = (TextView) view.findViewById(R.id.txt_apply);
        previous_img = (RelativeLayout) view.findViewById(R.id.previous_img);
        scroll_view = (NestedScrollView) view.findViewById(R.id.scroll_view);
        //mLayout.setScrollableView(scroll_view);
        holiday_layout = (LinearLayout) view.findViewById(R.id.holiday_layout);
        performance_layout = (LinearLayout) view.findViewById(R.id.performance_layout);
        dashboard_listing_layout = (RelativeLayout) view.findViewById(R.id.dashboard_listing_layout);
        your_opinion_layout = (RelativeLayout) view.findViewById(R.id.your_opinion_layout);
        latest_events_layout = (RelativeLayout) view.findViewById(R.id.latest_events_layout);
        sankalp_stories_layout = (RelativeLayout) view.findViewById(R.id.sankalp_stories_layout);
        inbox_layout = (RelativeLayout) view.findViewById(R.id.inbox_layout);
        my_task_layout = (RelativeLayout) view.findViewById(R.id.my_task_layout);
        my_appointment_layout = (RelativeLayout) view.findViewById(R.id.my_appointment_layout);
        dashboard_layout = (RelativeLayout) view.findViewById(R.id.dashboard_layout);
        company_performance_layout = (RelativeLayout) view.findViewById(R.id.company_performance_layout);
        my_performance_layout = (RelativeLayout) view.findViewById(R.id.my_performance_layout);
        upcoming_holidays_layout = (RelativeLayout) view.findViewById(R.id.upcoming_holidays_layout);
        main_leave_layout = (LinearLayout) view.findViewById(R.id.main_leave_layout);
        main_facilitylayout = (LinearLayout) view.findViewById(R.id.main_facilitylayout);
        main_request_layout = (LinearLayout) view.findViewById(R.id.main_request_layout);
        more_sankalp = (LinearLayout) view.findViewById(R.id.more_sankalp);
        more_event = (LinearLayout) view.findViewById(R.id.more_event);
        txt_approved = (TextView) view.findViewById(R.id.txt_approved);
        txt_see_all = (TextView) view.findViewById(R.id.txt_see_all);
        see_all_holiday = (LinearLayout) view.findViewById(R.id.see_all_holiday);
        menu_layout = (RelativeLayout) view.findViewById(R.id.dashboard_menu);
        //menu_layout_bottom = (RelativeLayout) view.findViewById(R.id.dashboard_menu_bottom);
        menu_grid = (RecyclerView) view.findViewById(R.id.menu_listing);
        dashboard_video_layout = (RelativeLayout) view.findViewById(R.id.layout_video);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.frame_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Constant.YOU_TUBE_API_KEY, this);
        ((HomeScreen) getActivity()).txt_title.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DisplayName));
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_User_Type).equalsIgnoreCase("EMPLOYEE")) {
            System.out.println("userType");
            dashboard_video_layout.setVisibility(View.GONE);
        } else {
            dashboard_video_layout.setVisibility(View.VISIBLE);

        }
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionNo = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        card_view_dashBord = (CardView) view.findViewById(R.id.card_view_dashBord);
        /*tv_versionName = (TextView) view.findViewById(R.id.tv_versionName);
        tv_versionName.setText("V ");*/
        tv_versionNo = (TextView) view.findViewById(R.id.tv_versionNo);
        tv_versionNo.setText("V " + versionNo);
        tv_welcome = (TextView) view.findViewById(R.id.tv_welcome);

        tv_welcome.setSelected(true);
        more_event.setOnClickListener(this);
        txt_apply.setOnClickListener(this);
        more_sankalp.setOnClickListener(this);
        txt_see_all.setOnClickListener(this);
        txt_approved.setOnClickListener(this);
        see_all_holiday.setOnClickListener(this);
        home_event_image = (ImageView) view.findViewById(R.id.home_event_image);
        events_name = (TextView) view.findViewById(R.id.events_name);
        event_date = (TextView) view.findViewById(R.id.event_date);
        event_matter = (TextView) view.findViewById(R.id.event_matter);
        sankalp_image = (ImageView) view.findViewById(R.id.home_sankalp_image);
        sankalp_story_name = (TextView) view.findViewById(R.id.home_sankalp_story_name);
        sankalp_story_date = (TextView) view.findViewById(R.id.home_sankalp_date);
        sankalp_story_matter = (TextView) view.findViewById(R.id.home_sankalp_story_matter);
        view_inbox = (TextView) view.findViewById(R.id.view_inbox);
        dashboard_listing_layout.requestFocus();
        ((HomeScreen) getActivity()).txt_title.setVisibility(View.VISIBLE);
        btnSubmitVote = (Button) view.findViewById(R.id.submit_vote);
        btnSubmitVote.setOnClickListener(this);
        view_inbox.setOnClickListener(this);
        previous_img.setOnClickListener(this);
        nxt_img.setOnClickListener(this);


        callWSStepFirst();
        callWSStepSecond();

        try {
           /* callViewMethod1();
            callViewMethod2();*/
            callNotificationMethod();
            callService();
        } catch (Exception e) {
        e.printStackTrace();
        }


        layoutManagerDashboard = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        dashboard_listing.setHasFixedSize(true);
        dashboard_listing.setLayoutManager(layoutManagerDashboard);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        facility_recycle.setHasFixedSize(true);
        facility_recycle.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        opinion_listing.setHasFixedSize(true);
        opinion_listing.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        upcoming_holiday_listing.setHasFixedSize(true);
        upcoming_holiday_listing.setLayoutManager(layoutManager);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager_home);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs_home);
        tabLayout.setupWithViewPager(viewPager);
        createViewPager(viewPager);
        createTabIcons();

        viewPager.setCurrentItem(1);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        myperformance_listing.setHasFixedSize(true);
        myperformance_listing.setLayoutManager(layoutManager);

        inbox_listing.setNestedScrollingEnabled(false);
        dashboard_listing.setNestedScrollingEnabled(false);
        task_listing.setNestedScrollingEnabled(false);
        my_appointment_listing.setNestedScrollingEnabled(false);
        opinion_listing.setNestedScrollingEnabled(false);
        my_appointment_listing.setNestedScrollingEnabled(false);
        upcoming_holiday_listing.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        inbox_listing.setHasFixedSize(true);
        inbox_listing.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        my_appointment_listing.setHasFixedSize(true);
        my_appointment_listing.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        task_listing.setHasFixedSize(true);
        task_listing.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        request_recycle.setHasFixedSize(true);
        request_recycle.setLayoutManager(layoutManager);

        menuLayout = new GridLayoutManager(getActivity(), 3);
        menu_grid.setNestedScrollingEnabled(true);
        menu_grid.setHasFixedSize(true);
        menu_grid.setLayoutManager(menuLayout);
        //ArrayList<MenuItems> menuItemLists = prepareData();
        dashBoardMenuadapter = new DashboardMenuAdapter(getActivity(), menuItemLists, orderMenuItemLists, getActivity().getSupportFragmentManager());
        dashBoardMenuadapter.setClickListener(this);
        menu_grid.setAdapter(dashBoardMenuadapter);
        Progressbar = (ProgressBar) view.findViewById(R.id.progressBar1);
        remaining_leave_txt = (TextView) view.findViewById(R.id.remaining_leave_txt);
        mLayout.addPanelSlideListener(new PanelSlideListener() {
                                          @Override
                                          public void onPanelSlide(View panel, float slideOffset) {
                                              Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                                          }

                                          @Override
                                          public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                                              Log.i(TAG, "onPanelStateChanged " + newState);
                                          }
                                      }

        );
       /* mLayout.setFadeOnClickListener(new

                                               OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       mLayout.setPanelState(PanelState.COLLAPSED);
                                                   }
                                               }

        );*/

        // callViewMethod();


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo("N9vfsHq1ZdU");
            youTubePlayer.setShowFullscreenButton(false);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
//            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("ActivityFragment");
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(Constant.YOU_TUBE_API_KEY, this);
        }
    }

    public void notifyUploadActivity(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            CommonClass.goToNextScreen(getActivity(), UploadActivity.class, imageUri.getPath());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }

    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerView;
    }

    private void callWSStepSecond() {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("OTP_SESSION", MODE_PRIVATE);
                JSONObject obj = new JSONObject();
                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("auth_type", UserAuthType);
                obj.put("mode", "step2");
                obj.put("displayname", UserDisplayName);
                obj.put("op_role_name", opRoleNAme);
                obj.put("op_user_emp_group_code", User_empGroupCode);
                obj.put("user_email", UserEmail);
                obj.put("op_user_zone", sharedPreferences.getString("zone", ""));
                obj.put("op_user_role_name", OP_USER_ROLENAME);
                obj.put("op_greatplus_user_id", GrtPlusUSerID);

                String url = Constant.WS_URL + Constant.WS_HOME;
                NetworkTask networkTask = new NetworkTask(getActivity(), 2, obj.toString());
                //  networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("THIS WEEK");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("YTD");
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new CompanyPerformance_ThisWeek_HOME(), "THIS WEEK");
        adapter.addFrag(new CompanyPerformance_YTD_HOME(), "YTD");
        viewPager.setAdapter(adapter);
    }

    private void getSharedPreferenceValues() {
        UserID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserID);
        UserToken = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_UserToken);
        UserAuthType = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType);
        UserDisplayName = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DisplayName);
        opRoleNAme = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_RoleName);
        User_empGroupCode = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_emp_grpCode);
        UserEmail = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_USerEmail);
        GrtPlusUSerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_GrtPlusUserID);
        UserProfilePic = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_userProfileImage);
        OP_USER_ROLENAME = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_op_user_role_name);
        user_id = Util.getSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME);
        dealerID = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_DealerID);
    }

    private void callWSStepFirst() {
        if (!cDetector.isConnectingToInternet()) {
            mLayout.setVisibility(View.GONE);
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        } else {
            mLayout.setVisibility(View.VISIBLE);
            try {

             /*   {"uid":"ROSHNI.SHRESHTHA","token":"TkVXQEluZGlh","mode":"step1","auth_type":"EMAIL",
                        "displayname":"ROSHNI SHRESHTHA","op_role_name":"WEB ADMIN","op_user_role_name":"WEB ADMIN",
                        "op_user_emp_group_code":"SG00016503","op_greatplus_user_id":"ROSHNIS"}*/

                JSONObject obj = new JSONObject();
                obj.put("uid", UserID);
                obj.put("token", UserToken);
                obj.put("mode", "step1");
                obj.put("auth_type", UserAuthType);
                obj.put("displayname", UserDisplayName);
                obj.put("op_role_name", opRoleNAme);
                obj.put("op_user_role_name", OP_USER_ROLENAME);
                obj.put("op_greatplus_user_id", GrtPlusUSerID);
                obj.put("op_user_emp_group_code", User_empGroupCode);

                String url = Constant.WS_URL + Constant.WS_HOME;
                NetworkTask networkTask = new NetworkTask(getActivity(), 1, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] _permission = {Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (TedPermission.isGranted(getActivity(), _permission)) {
                openCamera();
            } else {
                TedPermission.with(getActivity())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        } else {
            openCamera();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nxt_img:
                dashboard_listing.getLayoutManager().scrollToPosition(layoutManagerDashboard.findLastVisibleItemPosition() + 1);
                break;
            case R.id.previous_img:
                dashboard_listing.getLayoutManager().scrollToPosition(layoutManagerDashboard.findFirstVisibleItemPosition() - 1);
                break;
            case R.id.view_inbox:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WS_URL + "inbox.login.php?uid=" + UserID + "&token=" + UserToken));
                startActivity(browserIntent);
                break;
            case R.id.see_all_holiday:
                ((HomeScreen) getActivity()).commonFragmentMethod(new UpcomingHolidaysFragment(), null, null);
                break;
            case R.id.submit_vote:
                if (yourOpinionAdapter.mSelectedItem != -1) {
                    String id = yourOpinionAdapter.optionID.get("ID");
                    callWSSubmitVote(id);
                }
                break;
            case R.id.more_event:
                ((HomeScreen) getActivity()).commonFragmentMethod(new LatestEventsFragment(), null, null);
                break;
            case R.id.more_sankalp:
                ((HomeScreen) getActivity()).commonFragmentMethod(new SankalpStoriesFragment(), null, null);
                break;
            case R.id.txt_approved:
                ((HomeScreen) getActivity()).commonFragmentMethod(new ApproveMyTask(), null, null);
                break;
            case R.id.txt_see_all:
                ((HomeScreen) getActivity()).commonFragmentMethod(new MyTask(), null, null);
                break;
            case R.id.txt_apply:
                ((HomeScreen) getActivity()).commonFragmentMethod(new LeaveFragment(), null, null);
                break;

        }
    }

    private void callWSSubmitVote(String id) {
        if (!cDetector.isConnectingToInternet())
            alert.showAlertDialog(getActivity(), "Network Connection", "Enable Your Internet Connection", false);
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("user_id", UserID);
                obj.put("email", UserEmail);
                obj.put("auth_type", UserAuthType);
                obj.put("action", "submit_poll");
                obj.put("id_polls", base_.getPolls().get(0).getId_polls());
                obj.put("id_poll_options", id);
                obj.put("op_greatplus_user_id", GrtPlusUSerID);
                obj.put("token", UserToken);
                obj.put("op_user_role_name", OP_USER_ROLENAME);

                String url = Constant.WS_URL + Constant.WS_OPTION_SUBMIT;
                NetworkTask networkTask = new NetworkTask(getActivity(), 3, obj.toString());
                networkTask.setDialogMessage("Loading...");
                networkTask.exposePostExecute(this);
                networkTask.execute(url);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        switch (id) {
            case 1:
                Log.d("check", "case 1 id " + object.toString());
                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("Object", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("Data2", object);
                editor1.commit();
                Gson gson = new Gson();
                HomeBE base = gson.fromJson(object, HomeBE.class);
                HeaderModel headerbase;
                headerbase = gson.fromJson(object, HeaderModel.class);
                Log.e("MENU HEADER", "==>" + headerbase.getQuick_access().size());

                if (headerbase.getQuick_access().size() > 0) {
                    menu_layout.setVisibility(View.VISIBLE);
                    String userRole = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_RoleName);
                    try {
                        if (userRole.equalsIgnoreCase("DLR MTS") || userRole.equalsIgnoreCase("DST ADMIN") || userRole.equalsIgnoreCase("DLR ADMIN") || userRole.equalsIgnoreCase("DLR USER") || userRole.equalsIgnoreCase("SALES") || userRole.equalsIgnoreCase("SALES USER")) {
                            MenuItems mItmOrder = new MenuItems();
                            mItmOrder.setTxt_img_url(headerbase.getQuick_access().get(0).getICON());
                            mItmOrder.setTxt_menu("Order");
                            mItmOrder.setWeb_url(null);
                            menuItemLists.add(mItmOrder);
                        }

                        for (int i = 0; i < headerbase.getQuick_access().size(); i++) {


                            MenuItems mItm = new MenuItems();
                            OrderMenuItems oItems = new OrderMenuItems();
                            Log.d("DeshboardMenu->", "-->" + i + headerbase.getQuick_access().get(i).getCHILD_MENU());
                            Log.d("DeshboardIcon->", "-->" + headerbase.getQuick_access().get(i).getICON());
                            if (headerbase.getQuick_access().get(i).getCHILD_MENU().toLowerCase().contains("order")) {
                                oItems.setTxt_img_url(headerbase.getQuick_access().get(i).getICON());
                                oItems.setTxt_menu(headerbase.getQuick_access().get(i).getCHILD_MENU());
                                oItems.setWeb_url(headerbase.getQuick_access().get(i).getLINK());
                                orderMenuItemLists.add(oItems);
                            } else {
                                mItm.setTxt_img_url(headerbase.getQuick_access().get(i).getICON());
                                mItm.setTxt_menu(headerbase.getQuick_access().get(i).getCHILD_MENU());
                                mItm.setWeb_url(headerbase.getQuick_access().get(i).getLINK());
                                menuItemLists.add(mItm);
                                //String test[] =  headerbase.getQuick_access().get(m).getCHILD_MENU();

                            }

                        }
//                        //ADDED HERE
//                        menuItemLists.add(new MenuItems("Guarantee Log", "", null));
//                        //

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    menu_layout.setVisibility(View.GONE);
                }
                dashBoardMenuadapter.notifyDataSetChanged();

                if (base.getSlider().size() > 0) {
                    dashboard_listing_layout.setVisibility(View.VISIBLE);
                    DashboardListingAdaper adapter = new DashboardListingAdaper(getActivity(), base.getSlider());
                    dashboard_listing.setAdapter(adapter);
                } else {
                    dashboard_listing_layout.setVisibility(View.GONE);
                }

                if (base.getInbox().size() > 0) {
                    inbox_layout.setVisibility(View.VISIBLE);
                    InboxAdapter adapter = new InboxAdapter(getActivity(), base.getInbox());
                    inbox_listing.setAdapter(adapter);
                } else {
                    inbox_layout.setVisibility(View.GONE);
                }
                if (base.getTask().size() > 0) {
                    my_task_layout.setVisibility(View.VISIBLE);
                    TaskAdapter adapter = new TaskAdapter(getActivity(), base.getTask());
                    task_listing.setAdapter(adapter);
                } else {
                    my_task_layout.setVisibility(View.GONE);
                }
                if (base.getAppointment().size() > 0) {
                    my_appointment_layout.setVisibility(View.VISIBLE);
                    MyAppointment_Adapter adapter = new MyAppointment_Adapter(getActivity(), base.getAppointment());
                    my_appointment_listing.setAdapter(adapter);
                } else {
                    my_appointment_layout.setVisibility(View.GONE);
                }

                if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType).equals("OTP")) {
                    ((HomeScreen) getActivity()).profile_image.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_userProfileImage)).into(((HomeScreen) getActivity()).profile_image);
                } else {
                    if (base.getProfile_image().size() > 0)
                        ((HomeScreen) getActivity()).profile_image.setVisibility(View.VISIBLE);

                    Picasso.with(getActivity()).load(base.getProfile_image().get(0).getOp_user_profile_image().replaceAll(" ", "%20")).into(((HomeScreen) getActivity()).profile_image);
                    Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.Sp_userProfileImage, base.getProfile_image().get(0).getOp_user_profile_image().replaceAll(" ", "%20"));
                }

                break;
            case 2: {
                Log.d("check", "Case 2 id " + object.toString());
                try {

                    Gson gson_ = new Gson();
                    base_ = gson_.fromJson(object, HomeSecondBE.class);
                    SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("Object", MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                    editor2.putString("Data3", object);
                    editor2.commit();
                    if (base_.getCompany_performance().size() > 0) {
                        company_performance_layout.setVisibility(View.VISIBLE);
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_TP_WEEK_TARGET, base_.getCompany_performance().get(0).getTP_WEEK_TARGET());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_TP_WEEK_ACH, base_.getCompany_performance().get(0).getTP_WEEK_ACH());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_RCV_WEEK_TARGET, base_.getCompany_performance().get(0).getRCV_WEEK_ACH());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_RCV_WEEK_ACH, base_.getCompany_performance().get(0).getRCV_WEEK_TARGET());

                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_TP_YTD_TARGET, base_.getCompany_performance().get(0).getTP_YTD_TARGET());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_TP_YTD_ACH, base_.getCompany_performance().get(0).getTP_YTD_ACH());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_RCV_YTD_TARGET, base_.getCompany_performance().get(0).getRCV_YTD_TARGET());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_RCV_YTD_ACH, base_.getCompany_performance().get(0).getRCV_YTD_ACH());

                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_tp_name, base_.getCompany_performance_heading().get(0).getTP());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_rcv_name, base_.getCompany_performance_heading().get(0).getRCV());
                    } else {
                        company_performance_layout.setVisibility(View.GONE);
                    }

                    if (base_.getMy_performance().size() > 0) {
                        my_performance_layout.setVisibility(View.VISIBLE);
                        MyPerformance_home_Adapter myPerformance_home_adapter = new MyPerformance_home_Adapter(getActivity(), base_.getMy_performance());
                        myperformance_listing.setAdapter(myPerformance_home_adapter);
                    } else {
                        my_performance_layout.setVisibility(View.GONE);
                    }

                    if (base_.getUpcoming_holiday().size() > 0) {
                        upcoming_holidays_layout.setVisibility(View.VISIBLE);
                        UpcomingHoliday_Adapter upcomingHoliday_adapter = new UpcomingHoliday_Adapter(getActivity(), base_.getUpcoming_holiday());
                        upcoming_holiday_listing.setAdapter(upcomingHoliday_adapter);
                    } else {
                        upcoming_holidays_layout.setVisibility(View.GONE);
                    }


                /*    if (base_.getRequest_center_inner_menu().get(0).getLeave().size() > 0 ||
                            base_.getRequest_center_inner_menu().get(0).getFacilities() != null ||
                            base_.getRequest_center_inner_menu().get(0).getOther_request() != null) {

                        for (int i = 0; i < base_.getRequest_center_inner_menu().size(); i++) {
                            if (base_.getRequest_center_inner_menu().get(i).getFacilities() != null) {
                                if (base_.getRequest_center_inner_menu().get(i).getFacilities().size() > 0) {
                                    main_facilitylayout.setVisibility(View.VISIBLE);
                                    FacilityAdapter facilityAdapter = new FacilityAdapter(getActivity(), base_.getRequest_center_inner_menu().get(i).getFacilities());
                                    facility_recycle.setAdapter(facilityAdapter);
                                }
                            } else if (base_.getRequest_center_inner_menu().get(i).getOther_request() != null) {
                                if (base_.getRequest_center_inner_menu().get(i).getOther_request().size() > 0) {
                                    main_request_layout.setVisibility(View.VISIBLE);
                                    OtherRequestAdapter facilityAdapter = new OtherRequestAdapter(getActivity(), base_.getRequest_center_inner_menu().get(i).getOther_request());
                                    request_recycle.setAdapter(facilityAdapter);
                                }
                            } else if (base_.getRequest_center_inner_menu().get(i).getLeave() != null) {
                                if (base_.getRequest_center_inner_menu().get(i).
                                        getLeave().size() > 0) {
                                    main_leave_layout.setVisibility(View.VISIBLE);
                                    leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(i).getLeave().get(0).getOp_leave_rec());
                                    remaining_leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(i).getLeave().get(0).getOp_leave_bal());

                                    new Thread(new Runnable() {

                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            while (remaining_leave < leave) {

                                                handler.post(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        Progressbar.setProgress((int) remaining_leave);
                                                        if (remaining_leave <= 25.0) {
                                                            Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                        } else if (remaining_leave > 25 && remaining_leave <= 50) {
                                                            Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                        } else if (remaining_leave > 50) {
                                                            Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                        }
                                                        if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                            remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                        } else {
                                                            remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                        }
                                                    }
                                                });
                                                try {
                                                    Thread.sleep(300);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            while (remaining_leave == leave) {

                                                handler.post(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        Progressbar.setProgress((int) (leave / remaining_leave) * 100);
                                                        if ((leave / remaining_leave) * 100 <= 25.0) {
                                                            Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                        } else if ((leave / remaining_leave) * 100 > 25 && (leave / remaining_leave) * 100 <= 50) {
                                                            Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                        } else if ((leave / remaining_leave) * 100 > 50) {
                                                            Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                        }
                                                        if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                            remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                        } else {
                                                            remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                        }
                                                    }
                                                });
                                                try {
                                                    Thread.sleep(300);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).start();
                                }
                            } else {
                                main_leave_layout.setVisibility(View.GONE);
                                main_facilitylayout.setVisibility(View.GONE);
                                main_request_layout.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        dragView.setOnClickListener(null);
                    }*/

                    if ((base_.getRequest_center_inner_menu().get(0).getLeave() != null ||
                            base_.getRequest_center_inner_menu().get(0).getFacilities().size() > 0 ||
                            base_.getRequest_center_inner_menu().get(0).getOther_request() != null) ||
                            (base_.getRequest_center_inner_menu().get(1).getLeave().size() > 0 ||
                                    base_.getRequest_center_inner_menu().get(1).getFacilities() != null ||
                                    base_.getRequest_center_inner_menu().get(1).getOther_request() != null) ||
                            (base_.getRequest_center_inner_menu().get(2).getLeave() != null ||
                                    base_.getRequest_center_inner_menu().get(2).getFacilities() != null ||
                                    base_.getRequest_center_inner_menu().get(2).getOther_request().size() > 0)) {

                        if (base_.getRequest_center_inner_menu().get(2).getOther_request() != null) {
                            if (base_.getRequest_center_inner_menu().get(2).getOther_request().size() > 0) {
                                main_request_layout.setVisibility(View.VISIBLE);
                                OtherRequestAdapter facilityAdapter = new OtherRequestAdapter(getActivity(), base_.getRequest_center_inner_menu().get(2).getOther_request());
                                request_recycle.setAdapter(facilityAdapter);
                            }
                        } else if (base_.getRequest_center_inner_menu().get(0).getOther_request() != null) {
                            if (base_.getRequest_center_inner_menu().get(0).getOther_request().size() > 0) {
                                main_request_layout.setVisibility(View.VISIBLE);
                                OtherRequestAdapter facilityAdapter = new OtherRequestAdapter(getActivity(), base_.getRequest_center_inner_menu().get(0).getOther_request());
                                request_recycle.setAdapter(facilityAdapter);
                            }
                        } else if (base_.getRequest_center_inner_menu().get(1).getOther_request() != null) {
                            if (base_.getRequest_center_inner_menu().get(1).getOther_request().size() > 0) {
                                main_request_layout.setVisibility(View.VISIBLE);
                                OtherRequestAdapter facilityAdapter = new OtherRequestAdapter(getActivity(), base_.getRequest_center_inner_menu().get(1).getOther_request());
                                request_recycle.setAdapter(facilityAdapter);
                            }
                        } else {
                            main_request_layout.setVisibility(View.GONE);
                        }

                        if (base_.getRequest_center_inner_menu().get(1).getLeave() != null) {
                            if (base_.getRequest_center_inner_menu().get(1).
                                    getLeave().size() > 0) {
                                main_leave_layout.setVisibility(View.VISIBLE);
                                leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(1).getLeave().get(0).getOp_leave_rec());
                                remaining_leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(1).getLeave().get(0).getOp_leave_bal());

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        while (remaining_leave < leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) remaining_leave);
                                                    if (remaining_leave <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 25 && remaining_leave <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        while (remaining_leave == leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) (leave / remaining_leave) * 100);
                                                    if ((leave / remaining_leave) * 100 <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 25 && (leave / remaining_leave) * 100 <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                            }
                        } else if (base_.getRequest_center_inner_menu().get(2).getLeave() != null) {
                            if (base_.getRequest_center_inner_menu()
                                    .get(2).getLeave().size() > 0) {
                                main_leave_layout.setVisibility(View.VISIBLE);
                                leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(2).getLeave().get(0).getOp_leave_rec());
                                remaining_leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(2).getLeave().get(0).getOp_leave_bal());

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        while (remaining_leave < leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) remaining_leave);
                                                    if (remaining_leave <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 25 && remaining_leave <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }

                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        while (remaining_leave == leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) (leave / remaining_leave) * 100);
                                                    if ((leave / remaining_leave) * 100 <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 25 && (leave / remaining_leave) * 100 <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                            }
                        } else if (base_.getRequest_center_inner_menu().get(0).getLeave() != null)
                            if (base_.getRequest_center_inner_menu().get(0).getLeave().size() > 0) {
                                main_leave_layout.setVisibility(View.VISIBLE);
                                leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(0).getLeave().get(0).getOp_leave_rec());
                                remaining_leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(0).getLeave().get(0).getOp_leave_bal());

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        while (remaining_leave < leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) remaining_leave);
                                                    if (remaining_leave <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 25 && remaining_leave <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }

                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        while (remaining_leave == leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) (leave / remaining_leave) * 100);
                                                    if ((leave / remaining_leave) * 100 <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 25 && (leave / remaining_leave) * 100 <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                            } else {
                                main_leave_layout.setVisibility(View.GONE);
                            }
                    } else {

                        dragView.setOnClickListener(null);
                    }

                    if (base_.getPolls().size() > 0) {
                        your_opinion_layout.setVisibility(View.VISIBLE);
                        question.setText(base_.getPolls().get(0).getName());
                        yourOpinionAdapter = new YourOpinionAdapter(getActivity(), base_.getPolls());
                        opinion_listing.setAdapter(yourOpinionAdapter);

                        if (base_.getPolls().get(0).getPolls_option_answer().size() > 0) {
                            btnSubmitVote.setVisibility(View.GONE);
                        }
                    } else {
                        your_opinion_layout.setVisibility(View.GONE);
                    }

                    if (base_.getEvent_list().size() > 0) {
                        latest_events_layout.setVisibility(View.VISIBLE);
                        Picasso.with(getActivity()).load(base_.getEvent_list().get(0).getImage()).into(home_event_image);
                        events_name.setText(base_.getEvent_list().get(0).getTitle());
                        event_date.setText(base_.getEvent_list().get(0).getEvent_date());
                        event_matter.setText(base_.getEvent_list().get(0).getShort_desc());
                    } else {
                        latest_events_layout.setVisibility(View.GONE);
                    }
                    if (base_.getSankalp_story().size() > 0) {
                        sankalp_stories_layout.setVisibility(View.VISIBLE);
                        sankalp_story_date.setText(base_.getSankalp_story().get(0).getDate());
                        sankalp_story_name.setText(base_.getSankalp_story().get(0).getTitle());
                        sankalp_story_matter.setText(base_.getSankalp_story().get(0).getDesc());
                        Picasso.with(getActivity()).load(base_.getSankalp_story().get(0).getImage()).into(sankalp_image);
                    } else {
                        sankalp_stories_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 3:

                Log.d("check", "case 3 id " + object.toString());
                Gson gson_ = new Gson();
                SubmitPollsModel base_ = gson_.fromJson(object, SubmitPollsModel.class);
                if (base_.getInfo().get(0).getStatus() == 1) {
                    Toast.makeText(getActivity(), base_.getInfo().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                    ((HomeScreen) getActivity()).commonFragmentMethod(new DashBoard(), null, null);
                } else {
                    Toast.makeText(getActivity(), base_.getInfo().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollX = scroll_view.getScrollX();
        scrollY = scroll_view.getScrollY();
        //stopPosition = videoView.getCurrentPosition();
        //videoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
//this is important. scrollTo doesn't work in main thread.
        scroll_view.post(new Runnable() {
            @Override
            public void run() {
                scroll_view.scrollTo(scrollX, scrollY);
            }
        });

        //videoView.seekTo(stopPosition);
        //videoView.start();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void openCamera() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    return;
                }
                if (photoFile != null) {
                    Uri photoURI = Uri.fromFile(createImageFile());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    getActivity().startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                }

            }
        } catch (Exception e) {

        }
    }

    private void callViewMethod1() {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Object", MODE_PRIVATE);
            String dashBord1 = sharedPreferences.getString("Data2", "hello");
            Log.d("Hello DashBord", dashBord1.toString());
            if (dashBord1.equalsIgnoreCase("hello")) {
                callWSStepFirst();
                System.out.println(" DashBord1 service call");
            } else {
                mLayout.setVisibility(View.VISIBLE);
                System.out.println("DashBord1 service Not call case 1");
                Gson gson = new Gson();
                HomeBE base = gson.fromJson(dashBord1, HomeBE.class);
                HeaderModel headerbase;
                headerbase = gson.fromJson(dashBord1, HeaderModel.class);
                Log.e("MENU HEADER", "==>" + headerbase.getQuick_access().size());
                if (headerbase.getQuick_access().size() > 0) {
                    menu_layout.setVisibility(View.VISIBLE);
                    String userRole = Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_RoleName);
                    try {
                        if (userRole.equalsIgnoreCase("DLR MTS") || userRole.equalsIgnoreCase("DST ADMIN") || userRole.equalsIgnoreCase("DLR ADMIN") || userRole.equalsIgnoreCase("DLR USER") || userRole.equalsIgnoreCase("SALES") || userRole.equalsIgnoreCase("SALES USER")) {
                            MenuItems mItmOrder = new MenuItems();
                            mItmOrder.setTxt_img_url(headerbase.getQuick_access().get(0).getICON());
                            mItmOrder.setTxt_menu("Order");
                            mItmOrder.setWeb_url(null);
                            menuItemLists.add(mItmOrder);
                        }

                        for (int i = 0; i < headerbase.getQuick_access().size(); i++) {
                            MenuItems mItm = new MenuItems();
                            OrderMenuItems oItems = new OrderMenuItems();
                            Log.d("DeshboardMenu->", "-->" + i + headerbase.getQuick_access().get(i).getCHILD_MENU());
                            Log.d("DeshboardIcon->", "-->" + headerbase.getQuick_access().get(i).getICON());
                            if (headerbase.getQuick_access().get(i).getCHILD_MENU().toLowerCase().contains("order")) {
                                oItems.setTxt_img_url(headerbase.getQuick_access().get(i).getICON());
                                oItems.setTxt_menu(headerbase.getQuick_access().get(i).getCHILD_MENU());
                                oItems.setWeb_url(headerbase.getQuick_access().get(i).getLINK());
                                orderMenuItemLists.add(oItems);
                                Log.d("count", orderMenuItemLists.size() + " orderMenuItemLists");
                            } else {
                                mItm.setTxt_img_url(headerbase.getQuick_access().get(i).getICON());
                                mItm.setTxt_menu(headerbase.getQuick_access().get(i).getCHILD_MENU());
                                mItm.setWeb_url(headerbase.getQuick_access().get(i).getLINK());
                                menuItemLists.add(mItm);
                                //String test[] =  headerbase.getQuick_access().get(m).getCHILD_MENU();

                            }

                        }
                        //                        //ADDED HERE
                        //                        menuItemLists.add(new MenuItems("Guarantee Log", "", null));
                        //                        //

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    menu_layout.setVisibility(View.GONE);
                }
                //            dashBoardMenuadapter.notifyDataSetChanged();

                if (base.getSlider().size() > 0) {
                    dashboard_listing_layout.setVisibility(View.VISIBLE);
                    DashboardListingAdaper adapter = new DashboardListingAdaper(getActivity(), base.getSlider());
                    dashboard_listing.setAdapter(adapter);
                } else {
                    dashboard_listing_layout.setVisibility(View.GONE);
                }

                if (base.getInbox().size() > 0) {
                    inbox_layout.setVisibility(View.VISIBLE);
                    InboxAdapter adapter = new InboxAdapter(getActivity(), base.getInbox());
                    inbox_listing.setAdapter(adapter);
                } else {
                    inbox_layout.setVisibility(View.GONE);
                }
                if (base.getTask().size() > 0) {
                    my_task_layout.setVisibility(View.VISIBLE);
                    TaskAdapter adapter = new TaskAdapter(getActivity(), base.getTask());
                    task_listing.setAdapter(adapter);
                } else {
                    my_task_layout.setVisibility(View.GONE);
                }
                if (base.getAppointment().size() > 0) {
                    my_appointment_layout.setVisibility(View.VISIBLE);
                    MyAppointment_Adapter adapter = new MyAppointment_Adapter(getActivity(), base.getAppointment());
                    my_appointment_listing.setAdapter(adapter);
                } else {
                    my_appointment_layout.setVisibility(View.GONE);
                }

                if (Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_AuthType).equals("OTP")) {
                    ((HomeScreen) getActivity()).profile_image.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(Util.getSharedPrefrenceValue(getActivity(), Constant.Sp_userProfileImage)).into(((HomeScreen) getActivity()).profile_image);
                } else {
                    if (base.getProfile_image() != null && base.getProfile_image().size() > 0)
                        ((HomeScreen) getActivity()).profile_image.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(base.getProfile_image().get(0).getOp_user_profile_image().replaceAll(" ", "%20")).into(((HomeScreen) getActivity()).profile_image);
                    Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.Sp_userProfileImage, base.getProfile_image().get(0).getOp_user_profile_image().replaceAll(" ", "%20"));
                }

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void callViewMethod2() {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Object", MODE_PRIVATE);
            String dashBord2 = sharedPreferences.getString("Data3", "hello");
            Log.d("Hello DashBord", dashBord2.toString());
            if (dashBord2.equalsIgnoreCase("hello")) {
                callWSStepSecond();
                System.out.println(" DashBord2 service call");
            } else {
                try {
                    Gson gson_ = new Gson();
                    base_ = gson_.fromJson(dashBord2, HomeSecondBE.class);
                    if (base_.getCompany_performance().size() > 0) {
                        company_performance_layout.setVisibility(View.VISIBLE);
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_TP_WEEK_TARGET, base_.getCompany_performance().get(0).getTP_WEEK_TARGET());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_TP_WEEK_ACH, base_.getCompany_performance().get(0).getTP_WEEK_ACH());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_RCV_WEEK_TARGET, base_.getCompany_performance().get(0).getRCV_WEEK_ACH());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_RCV_WEEK_ACH, base_.getCompany_performance().get(0).getRCV_WEEK_TARGET());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_TP_YTD_TARGET, base_.getCompany_performance().get(0).getTP_YTD_TARGET());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_TP_YTD_ACH, base_.getCompany_performance().get(0).getTP_YTD_ACH());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_RCV_YTD_TARGET, base_.getCompany_performance().get(0).getRCV_YTD_TARGET());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_RCV_YTD_ACH, base_.getCompany_performance().get(0).getRCV_YTD_ACH());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_tp_name, base_.getCompany_performance_heading().get(0).getTP());
                        Util.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SP_rcv_name, base_.getCompany_performance_heading().get(0).getRCV());
                    } else {
                        company_performance_layout.setVisibility(View.GONE);
                    }

                    if (base_.getMy_performance().size() > 0) {
                        my_performance_layout.setVisibility(View.VISIBLE);
                        MyPerformance_home_Adapter myPerformance_home_adapter = new MyPerformance_home_Adapter(getActivity(), base_.getMy_performance());
                        myperformance_listing.setAdapter(myPerformance_home_adapter);
                    } else {
                        my_performance_layout.setVisibility(View.GONE);
                    }

                    if (base_.getUpcoming_holiday().size() > 0) {
                        upcoming_holidays_layout.setVisibility(View.VISIBLE);
                        UpcomingHoliday_Adapter upcomingHoliday_adapter = new UpcomingHoliday_Adapter(getActivity(), base_.getUpcoming_holiday());
                        upcoming_holiday_listing.setAdapter(upcomingHoliday_adapter);
                    } else {
                        upcoming_holidays_layout.setVisibility(View.GONE);
                    }
                    if ((base_.getRequest_center_inner_menu().get(0).getLeave() != null ||
                            base_.getRequest_center_inner_menu().get(0).getFacilities().size() > 0 ||
                            base_.getRequest_center_inner_menu().get(0).getOther_request() != null) ||
                            (base_.getRequest_center_inner_menu().get(1).getLeave().size() > 0 ||
                                    base_.getRequest_center_inner_menu().get(1).getFacilities() != null ||
                                    base_.getRequest_center_inner_menu().get(1).getOther_request() != null) ||
                            (base_.getRequest_center_inner_menu().get(2).getLeave() != null ||
                                    base_.getRequest_center_inner_menu().get(2).getFacilities() != null ||
                                    base_.getRequest_center_inner_menu().get(2).getOther_request().size() > 0)) {

                        if (base_.getRequest_center_inner_menu().get(2).getOther_request() != null) {
                            if (base_.getRequest_center_inner_menu().get(2).getOther_request().size() > 0) {
                                main_request_layout.setVisibility(View.VISIBLE);
                                OtherRequestAdapter facilityAdapter = new OtherRequestAdapter(getActivity(), base_.getRequest_center_inner_menu().get(2).getOther_request());
                                request_recycle.setAdapter(facilityAdapter);
                            }
                        } else if (base_.getRequest_center_inner_menu().get(0).getOther_request() != null) {
                            if (base_.getRequest_center_inner_menu().get(0).getOther_request().size() > 0) {
                                main_request_layout.setVisibility(View.VISIBLE);
                                OtherRequestAdapter facilityAdapter = new OtherRequestAdapter(getActivity(), base_.getRequest_center_inner_menu().get(0).getOther_request());
                                request_recycle.setAdapter(facilityAdapter);
                            }
                        } else if (base_.getRequest_center_inner_menu().get(1).getOther_request() != null) {
                            if (base_.getRequest_center_inner_menu().get(1).getOther_request().size() > 0) {
                                main_request_layout.setVisibility(View.VISIBLE);
                                OtherRequestAdapter facilityAdapter = new OtherRequestAdapter(getActivity(), base_.getRequest_center_inner_menu().get(1).getOther_request());
                                request_recycle.setAdapter(facilityAdapter);
                            }
                        } else {
                            main_request_layout.setVisibility(View.GONE);
                        }

                        if (base_.getRequest_center_inner_menu().get(1).getLeave() != null) {
                            if (base_.getRequest_center_inner_menu().get(1).
                                    getLeave().size() > 0) {
                                main_leave_layout.setVisibility(View.VISIBLE);
                                leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(1).getLeave().get(0).getOp_leave_rec());
                                remaining_leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(1).getLeave().get(0).getOp_leave_bal());

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        while (remaining_leave < leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) remaining_leave);
                                                    if (remaining_leave <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 25 && remaining_leave <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        while (remaining_leave == leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) (leave / remaining_leave) * 100);
                                                    if ((leave / remaining_leave) * 100 <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 25 && (leave / remaining_leave) * 100 <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                            }
                        } else if (base_.getRequest_center_inner_menu().get(2).getLeave() != null) {
                            if (base_.getRequest_center_inner_menu()
                                    .get(2).getLeave().size() > 0) {
                                main_leave_layout.setVisibility(View.VISIBLE);
                                leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(2).getLeave().get(0).getOp_leave_rec());
                                remaining_leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(2).getLeave().get(0).getOp_leave_bal());

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        while (remaining_leave < leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) remaining_leave);
                                                    if (remaining_leave <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 25 && remaining_leave <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }

                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        while (remaining_leave == leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) (leave / remaining_leave) * 100);
                                                    if ((leave / remaining_leave) * 100 <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 25 && (leave / remaining_leave) * 100 <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                            }
                        } else if (base_.getRequest_center_inner_menu().get(0).getLeave() != null)
                            if (base_.getRequest_center_inner_menu().get(0).getLeave().size() > 0) {
                                main_leave_layout.setVisibility(View.VISIBLE);
                                leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(0).getLeave().get(0).getOp_leave_rec());
                                remaining_leave = Float.parseFloat(base_.getRequest_center_inner_menu().get(0).getLeave().get(0).getOp_leave_bal());

                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        while (remaining_leave < leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) remaining_leave);
                                                    if (remaining_leave <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 25 && remaining_leave <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if (remaining_leave > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }

                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        while (remaining_leave == leave) {

                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {

                                                    Progressbar.setProgress((int) (leave / remaining_leave) * 100);
                                                    if ((leave / remaining_leave) * 100 <= 25.0) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#ec6464"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 25 && (leave / remaining_leave) * 100 <= 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#FF7F50"), PorterDuff.Mode.SRC_IN);
                                                    } else if ((leave / remaining_leave) * 100 > 50) {
                                                        Progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#33cc66"), PorterDuff.Mode.SRC_IN);

                                                    }
                                                    if (remaining_leave - Math.floor(remaining_leave) == 0.0) {
                                                        remaining_leave_txt.setText(Math.round(remaining_leave) + "/" + Math.round(leave) + " leaves remaining");
                                                    } else {
                                                        remaining_leave_txt.setText(remaining_leave + "/" + leave + " leaves remaining");
                                                    }
                                                }
                                            });
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                            } else {
                                main_leave_layout.setVisibility(View.GONE);
                            }
                    } else {

                        dragView.setOnClickListener(null);
                    }

                    if (base_.getPolls().size() > 0) {
                        your_opinion_layout.setVisibility(View.VISIBLE);
                        question.setText(base_.getPolls().get(0).getName());
                        yourOpinionAdapter = new YourOpinionAdapter(getActivity(), base_.getPolls());
                        opinion_listing.setAdapter(yourOpinionAdapter);

                        if (base_.getPolls().get(0).getPolls_option_answer().size() > 0) {
                            btnSubmitVote.setVisibility(View.GONE);
                        }
                    } else {
                        your_opinion_layout.setVisibility(View.GONE);
                    }

                    if (base_.getEvent_list().size() > 0) {
                        latest_events_layout.setVisibility(View.VISIBLE);
                        Picasso.with(getActivity()).load(base_.getEvent_list().get(0).getImage()).into(home_event_image);
                        events_name.setText(base_.getEvent_list().get(0).getTitle());
                        event_date.setText(base_.getEvent_list().get(0).getEvent_date());
                        event_matter.setText(base_.getEvent_list().get(0).getShort_desc());
                    } else {
                        latest_events_layout.setVisibility(View.GONE);
                    }
                    if (base_.getSankalp_story().size() > 0) {
                        sankalp_stories_layout.setVisibility(View.VISIBLE);
                        sankalp_story_date.setText(base_.getSankalp_story().get(0).getDate());
                        sankalp_story_name.setText(base_.getSankalp_story().get(0).getTitle());
                        sankalp_story_matter.setText(base_.getSankalp_story().get(0).getDesc());
                        Picasso.with(getActivity()).load(base_.getSankalp_story().get(0).getImage()).into(sankalp_image);
                    } else {
                        sankalp_stories_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callNotificationMethod() throws JSONException {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getActivity());
        JSONObject _req = new JSONObject();
        _req.put("p_greatplus_user_id", user_id);
        _req.put("p_dealer_id", dealerID);
        JsonArrayRequest _arrayReq = new JsonArrayRequest(Request.Method.POST, URLS.NOTIFICATION_URL, _req,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("Noti response is", jsonArray + "");
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(jsonArray.getJSONObject(0)));
                            marqueeValue = jsonObject.getString("message");
                            if (marqueeValue.equalsIgnoreCase("null")) {
                                card_view_dashBord.setVisibility(View.GONE);
                            } else
                                tv_welcome.setText(marqueeValue);
                            Log.d("value response is", marqueeValue + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue1.add(_arrayReq);
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void callService() throws  JSONException{
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("os_type", "A");
        postParam.put("version_code",String.valueOf(versionCode));// String.valueOf(versionCode)"
        postParam.put("version_name", versionNo);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    URLS.UPDATE_VERSION, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("response"+response);
                            try {
                                JSONObject jsonObject=response;
                                if(jsonObject.getString("update_status").equals("true")){
                                    os_type= jsonObject.getString("os_type");
                                    version_code= jsonObject.getString("version_code");
                                    version_name= jsonObject.getString("version_name");
                                    ver_update= jsonObject.getString("ver_update");
                                    date_time=jsonObject.getString("date_time");
                                    customDialog();
                                }else {

                                }

                            }catch (Exception e){

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };

            jsonObjReq.setTag(TAG);
            // Adding request to request queue
            queue.add(jsonObjReq);

    }

    private void customDialog() {
        final CustomDialog cd = new CustomDialog(getActivity(), R.layout.custome_dialog);
        cd.setCancelable(true);
        cd.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        TextView dialog_title = (TextView) cd.findViewById(R.id.dialog_title);
        dialog_title.setText("New version available" +" -V "+"("+ version_name+")");
        TextView txt_address_value = (TextView) cd.findViewById(R.id.txt_address_value);
        txt_address_value.setText(ver_update);
        TextView btn_download = (TextView) cd.findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.erp.sheelafoam&hl=en_IN"));
                startActivity(i);
            }
        });
        cd.show();
    }

}
