package com.erp.sheelafoam.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.BE.MenuItems;
import com.erp.sheelafoam.BE.OrderMenuItems;
import com.erp.sheelafoam.HomeScreen;
import com.erp.sheelafoam.NewWebViewActivity;
import com.erp.sheelafoam.OrderDashboardActivity;
import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.interfaces.ItemClickListener;
import com.erp.sheelafoam.passbook.PassbookMainActivity;
import com.erp.sheelafoam.sheelafoam.activity.CheckoutActivity;
import com.erp.sheelafoam.sheelafoam.activity.CustomerFeedbackActivity;
import com.erp.sheelafoam.sheelafoam.activity.DocumentUploadNewActivity;
import com.erp.sheelafoam.sheelafoam.activity.FootFall_Activity;
import com.erp.sheelafoam.sheelafoam.activity.PerformanceActivity;
import com.erp.sheelafoam.sheelafoam.activity.ShowroomImageActivity;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ConsumerExchangeSchemeActivity;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ConsumerOredrActivity;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ExchangeSchameActivity;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderFragment;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderView;
import com.erp.sheelafoam.sheelafoam.qrcode.ScanQrCodeActivity;
import com.erp.sheelafoam.sheelafoam.report.ReportOrderStatusFragment;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ComplaintNew;
import com.erp.sheelafoam.sheelafoam.xperia.activity.MrpCalculation;
import com.erp.sheelafoam.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dell on 18-Jul-17.
 */

public class DashboardMenuAdapter extends RecyclerView.Adapter<DashboardMenuAdapter.ViewHolder> {

    String title;
    private ArrayList<MenuItems> menu;
    private ArrayList<OrderMenuItems> orderMenu;
    private Activity context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ItemClickListener clickListener;
    private CustomTabsClient mCustomTabsClient;
    private CustomTabsSession mCustomTabsSession;

    public DashboardMenuAdapter(Activity context, ArrayList<MenuItems> menu, ArrayList<OrderMenuItems> orderMenu, FragmentManager fragmentManager) {
        this.context = context;
        this.menu = menu;
        this.orderMenu = orderMenu;
        this.fragmentManager = fragmentManager;

        String userType = Util.getSharedPrefrenceValue(context, Constant.Sp_User_Type);

       /* if(userType.equalsIgnoreCase("DEALER"))
            menu.add(new MenuItems("Scheme PassBook","http://greatplus.com/passbook/passbook_icon.png",""));*/

    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public DashboardMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    public void commonFragmentMethod(Fragment fragment, Bundle data, String TAG) {


        //fragmentManager = ((Activity) context).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (data != null)
            fragment.setArguments(data);

        fragmentTransaction.replace(R.id.fragment_container, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        if (fragmentTransaction != null)
            fragmentTransaction.commit();
    }

    @Override
    public void onBindViewHolder(DashboardMenuAdapter.ViewHolder holder, final int position) {

        holder.tv_menu.setText(menu.get(position).getTxt_menu());
        String asd = menu.get(position).getTxt_img_url();
        // Log.d("alok", asd);
        try {
            if (TextUtils.isEmpty(menu.get(position).getTxt_img_url())) {
                holder.img_menu.setImageResource(R.drawable.capture_image);
            } else
                Picasso.with(context).load(asd).resize(60, 60).centerInside().into(holder.img_menu);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String UserEmail = Util.getSharedPrefrenceValue(context, Constant.Sp_USerEmail);
        final String UserPAss = Util.getSharedPrefrenceValue(context, Constant.Sp_pass);
        final String AuthToken = Util.getSharedPrefrenceValue(context, Constant.Sp_UserToken);
        final String uId = Util.getSharedPrefrenceValue(context, Constant.Sp_UserID);
        final String authType = Util.getSharedPrefrenceValue(context, Constant.Sp_AuthType);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = menu.get(position).getTxt_menu();
                System.out.println("Title" + title);//---Footfall&Conversion--Showroom Image
                String subUrl = menu.get(position).getWeb_url();
                if (title.equalsIgnoreCase("Footfall & Conversion")) {
                    Intent gotoNext = new Intent(context, FootFall_Activity.class);
                    context.startActivity(gotoNext);
                } else if (title.equalsIgnoreCase("Showroom Image")) {
                    Intent gotoNext = new Intent(context, ShowroomImageActivity.class);
                    context.startActivity(gotoNext);
                } else if (title.equalsIgnoreCase("Customer Feedback")) {
                    Intent gotoNext = new Intent(context, CustomerFeedbackActivity.class);
                    context.startActivity(gotoNext);
                }
                else if (title.equalsIgnoreCase("Passbook")) {
                    Intent gotoNext = new Intent(context, PassbookMainActivity.class);
                    context.startActivity(gotoNext);
                }
                else if (title.equalsIgnoreCase("Pre Invoice Cash Reward")) {
                    Intent intent = new Intent(context, ConsumerOredrActivity.class);
                    context.startActivity(intent);
                }
                else if (title.equalsIgnoreCase("My Idea")) {
                    CustomTabsServiceConnection mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
                        @Override
                        public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                            mCustomTabsClient = client;
                            mCustomTabsClient.warmup(0L);
                            mCustomTabsSession = mCustomTabsClient.newSession(null);
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName componentName) {
                            mCustomTabsClient = null;
                        }
                    };
                    Uri uri = Uri.parse(subUrl + "?email=" + UserEmail + "&token=" + AuthToken);
                    CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", mCustomTabsServiceConnection);
                    CustomTabsIntent intentBuilder = new CustomTabsIntent.Builder(mCustomTabsSession)
                            .setToolbarColor(context.getResources().getColor(R.color.colorPrimary))
                            .build();
                    intentBuilder.intent.putExtra(CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING, false);
                    intentBuilder.intent.setPackage("com.android.chrome");
                    intentBuilder.launchUrl(context, uri);
System.out.println("ideaURL:-"+uri);
                } else if (subUrl != null) {
                    try {
                        if (title.contains("Attendance")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.putExtra("WebUrl", subUrl + "?email=" + UserEmail + "&token=" + AuthToken);
//                            intent.putExtra("title", title);
//                            context.startActivity(intent);
                            intent.setData(Uri.parse(subUrl + "?email=" + UserEmail + "&token=" + AuthToken));
                            context.startActivity(intent);
                        } else if (subUrl.contains("#")) {
                            Boolean cameraIcon = false;
                            if (subUrl.toLowerCase().contains("my-gallery")) {
                                cameraIcon = true;
                            }
                            Intent in = new Intent(context, NewWebViewActivity.class);
                            in.putExtra("WebUrl", subUrl + uId + "/" + AuthToken);
                            in.putExtra("title", title);
                            in.putExtra("camera", cameraIcon);
                            context.startActivity(in);
                        } else if (subUrl.contains("SheelaFoamApp")) {
                            Intent in = new Intent(context, NewWebViewActivity.class);
                            in.putExtra("WebUrl", subUrl + "?email=" + uId + "&token=" + AuthToken);
                            in.putExtra("title", title);
                            context.startActivity(in);
                        } else if (title.contains("Mts Report")) {
                      /*      Intent in = new Intent(context, NewWebViewActivity.class);
                            in.putExtra("WebUrl", subUrl + "?uid=" + uId + "&token=" + AuthToken + "&auth_type=" + authType);
                            in.putExtra("title", title);
                            context.startActivity(in);*/
                            CustomTabsServiceConnection mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
                                @Override
                                public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                                    mCustomTabsClient = client;
                                    mCustomTabsClient.warmup(0L);
                                    mCustomTabsSession = mCustomTabsClient.newSession(null);
                                }

                                @Override
                                public void onServiceDisconnected(ComponentName componentName) {
                                    mCustomTabsClient = null;
                                }
                            };
                            Uri uri = Uri.parse(subUrl + "?email=" + UserEmail + "&token=" + AuthToken);
                            CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", mCustomTabsServiceConnection);
                            CustomTabsIntent intentBuilder = new CustomTabsIntent.Builder(mCustomTabsSession)
                                    .setToolbarColor(context.getResources().getColor(R.color.colorPrimary))
                                    .build();
                            intentBuilder.intent.putExtra(CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING, false);
                            intentBuilder.intent.setPackage("com.android.chrome");
                            intentBuilder.launchUrl(context, uri);
                            System.out.println("ideaURL:-"+uri);
                        } else if (title.contains("Performance Dashboard")) {
                            Intent intent = new Intent(context, PerformanceActivity.class);
                            context.startActivity(intent);

                        } else {
                            Log.d("NEWurl-->", subUrl + "?email=" + UserEmail + "&token=" + AuthToken);
                            Intent in = new Intent(context, NewWebViewActivity.class);
                            in.putExtra("WebUrl", subUrl + "?email=" + UserEmail + "&token=" + AuthToken);
                            in.putExtra("title", title);
                            context.startActivity(in);
                        }
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(subUrl+"?email="+UserEmail+"&token="+AuthToken));
//                        context.startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    Intent in = new Intent(context, HeaderMenuDesc_Web.class);
//                    in.putExtra("WebUrl","http://10.1.1.23/sf/login"+NewUrl+"&uid="+UserID+"&password="+UserPAss+"&token="+AuthToken);
//                    context.startActivity(in);

                } /*else if (title.equalsIgnoreCase("Guarantee Log")) {
                    //open the new created page here!
                    System.out.println("Guarantee log page!!");
                    Intent intent = new Intent(context, GuaranteeLog.class);
                    context.startActivity(intent);
                }*/ else if (title.equalsIgnoreCase("Camera")) {
                    if (clickListener != null)
                        clickListener.onItemClick();
                } else if (title.equalsIgnoreCase("Order")) {
                    Intent intent = new Intent(context.getApplicationContext(), OrderDashboardActivity.class);
                    intent.putExtra("OrderMenuList", orderMenu);
                    context.startActivity(intent);
                } else if (title.equalsIgnoreCase("Order Report")) {
                    commonFragmentMethod(new ReportOrderStatusFragment(), null, null);
                } else if (title.equalsIgnoreCase("place order")) {
                    commonFragmentMethod(new ProductOrderFragment(), null, null);
                } else if (title.equalsIgnoreCase("order status")) {
                    commonFragmentMethod(new ProductOrderView(), null, null);
                } else if (title.equalsIgnoreCase("Mrp Calculation")) {
                    Intent intent = new Intent(context, MrpCalculation.class);
                    context.startActivity(intent);
                } else if (title.equalsIgnoreCase("Upload Document")) {
                    Intent intent = new Intent(context, DocumentUploadNewActivity.class);
                    context.startActivity(intent);
                } else if (title.equalsIgnoreCase("Digital Payment")) {
                    Intent intent = new Intent(context, CheckoutActivity.class);
                    context.startActivity(intent);
                } else if (title.equalsIgnoreCase("Exchange Offer")) {
                    Intent intent = new Intent(context, ExchangeSchameActivity.class);
                    context.startActivity(intent);
                } else if (title.equalsIgnoreCase("Guarantee Registration")) {
                    Intent intent = new Intent(context, ExchangeSchameActivity.class);
                    context.startActivity(intent);
                    /*Intent intent=new Intent(context, ConsumerExchangeSchemeActivity.class);
                            context.startActivity(intent);*/
                } else if (title.equalsIgnoreCase("Complaint")) {
                    Intent intent = new Intent(context, ComplaintNew.class);
                    context.startActivity(intent);
                } else if (title.equalsIgnoreCase("Scan")) {
                    Intent intent = new Intent(context, ScanQrCodeActivity.class);
                    context.startActivity(intent);
                } else if (title.equalsIgnoreCase("Quick Support")) {
                  /*  Intent intent = new Intent(context, CustomerFeedbackActivity.class);
                    context.startActivity(intent);*/
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.teamviewer.quicksupport.market&hl=en");
                    if (intent != null) {
                        // We found the activity now start the activity
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        // Bring user to the market or let them choose an app?
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("market://details?id=com.teamviewer.quicksupport.market&hl=en"));
                        context.startActivity(intent);
                    }
                } else {
                    Toast.makeText(view.getContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final String userRole = Util.getSharedPrefrenceValue(context, Constant.Sp_RoleName);
//        holder.img_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(menu.get(position).getWeb_url() != null ) {
//                    String subUrl = menu.get(position).getWeb_url();
//                    String NewUrl = subUrl.substring(0,38);
//                    Log.d("SUBSTRING---->", NewUrl);
//                    Log.d("NEWurl-->",NewUrl+"?email="+UserEmail+"&token="+AuthToken);
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NewUrl+"?email="+UserEmail+"&token="+AuthToken));
//                    context.startActivity(browserIntent);
////                    Intent in = new Intent(context, HeaderMenuDesc_Web.class);
////                    in.putExtra("WebUrl","http://10.1.1.23/sf/login"+NewUrl+"&uid="+UserID+"&password="+UserPAss+"&token="+AuthToken);
////                    context.startActivity(in);
//
//                }
//                else {
//                    Toast.makeText(view.getContext(), "No Data Available", Toast.LENGTH_SHORT).show();
//                }

//                if(menu.get(position).getTxt_menu().equalsIgnoreCase("Order Report")) {
////                    Toast.makeText(view.getContext(), "order",
////                            Toast.LENGTH_LONG).show();
//                    commonFragmentMethod(new ReportOrderStatusFragment(), null, null);
//                }
//                else if(menu.get(position).getTxt_menu().equalsIgnoreCase("place order")) {
//                    commonFragmentMethod(new ProductOrderFragment(), null, null);
//                }
//                else if(menu.get(position).getTxt_menu().equalsIgnoreCase("order status")) {
//                    commonFragmentMethod(new ProductOrderView(), null, null);
//                }
//                else if(menu.get(position).getTxt_menu().equalsIgnoreCase("Mrp Calculation")) {
//                    Intent in = new Intent(context, MrpCalculation.class);
//                    context.startActivity(in);
//                }
//                else if(menu.get(position).getTxt_menu().equalsIgnoreCase("Upload Document")) {
//                    Intent inDocs = new Intent(context, DocumentUploadNewActivity.class);
//                    context.startActivity(inDocs);
//                }
//                else if(menu.get(position).getTxt_menu().equalsIgnoreCase("Digital Payment")) {
//                    Intent inDigital = new Intent(context, CheckoutActivity.class);
//                    context.startActivity(inDigital);
//                }
//                else if(menu.get(position).getTxt_menu().equalsIgnoreCase("Exchange Offer")) {
//                    Intent inExchange = new Intent(context, ExchangeSchameActivity.class);
//                    context.startActivity(inExchange);
//                }
//                else if(menu.get(position).getTxt_menu().equalsIgnoreCase("Guarantee Registration")) {
//                    Intent inGuarantee = new Intent(context, ExchangeSchameActivity.class);
//                    context.startActivity(inGuarantee);
//                }
//                else if(menu.get(position).getTxt_menu().equalsIgnoreCase("Complaint")) {
//                    Intent inComplaint = new Intent(context, ComplaintNew.class);
//                    context.startActivity(inComplaint);
//                }
//                else {
//                    Intent in = new Intent(context, HeaderMenuDesc_Web.class);
//                    in.putExtra("WebUrl",menu.get(position).getWeb_url());
//                    context.startActivity(in);
//                }
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tv_menu;
        private ImageView img_menu;

        public ViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.linear);
            tv_menu = (TextView) view.findViewById(R.id.tv_menu);
            img_menu = (ImageView) view.findViewById(R.id.img_menu);

        }
    }


}