
/**
 * Short description for file: Used for display address book data
 * <p>
 * <p>
 * Used Android  minSdkVersion 11
 * Used Android  targetSdkVersion 19
 *
 * @package com.sheela.employeeportal.erp.sheelafoam
 * @author Vinay Kumar Gupta
 * Class name  AddressBookAdapter
 * Short description for class:  Adapter class used for set address book  data in list
 * Creation Date  06-02-2015
 * Last Modified  06-02-2015
 * Modified By    Vinay Kumar Gupta
 */

package com.erp.sheelafoam.sheelafoam.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.entry.ProductOrderListBean;
import com.erp.sheelafoam.sheelafoam.fragments.OrderApprovalFragment;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderView;
import com.erp.sheelafoam.sheelafoam.utility.ApiList;
import com.erp.sheelafoam.sheelafoam.utility.HelperMethods;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ApprovedOrderAdapter extends BaseAdapter {


    SharedPreferences mPrefs;
    ArrayList<ProductOrderListBean> _data;
    Activity mActivity;


    Fragment fragment;
    FragmentManager mFragmentManager;

    /*constructor*/
    public ApprovedOrderAdapter(ArrayList<ProductOrderListBean> data, Activity c, Fragment fragment, FragmentManager fragmentManager) {
        _data = data;
        mActivity = c;

        this.fragment = fragment;
        this.mFragmentManager = fragmentManager;

        mPrefs = mActivity.getSharedPreferences("Location", Context.MODE_PRIVATE);

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return _data.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return _data.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /*override method for inflate view start*/
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_list_approved_order, null);
        }


        final ProductOrderListBean entry = _data.get(position);


        TextView textview_product_name = (TextView) v.findViewById(R.id.textview_item_name);
        TextView textview_order_no = (TextView) v.findViewById(R.id.textview_order_no);
        //      TextView textview_delivery_date = (TextView) v.findViewById(R.id.textview_delivery_date);
        TextView textview_delivery_date = (TextView) v.findViewById(R.id.textview_delivery_date);
        TextView textview_product_l_w_t_text = (TextView) v.findViewById(R.id.textview_product_l_w_t_text);

        TextView textview_product_quantity = (TextView) v.findViewById(R.id.textview_product_quantity);

        TextView textview_customer_name = (TextView) v.findViewById(R.id.textview_customer_name);
        TextView textview_dealer_name = (TextView) v.findViewById(R.id.textview_dealer_name);
        TextView textview_outstanding_amount = (TextView) v.findViewById(R.id.textview_outstanding_amount);

	       
	        
	     /*  LinearLayout llayout_edit_order = (LinearLayout) v.findViewById(R.id.llayout_edit_order);
	       LinearLayout llayout_cancel_order = (LinearLayout) v.findViewById(R.id.llayout_cancel_order);
	  */


        /**
         *This adapter and ProductOrderView fragment calling from two places
         *first from order managment module and second is from reporting mudule.
         * **/

        String length = (entry.getLength().length() > 0) ? entry.getLength() : "0";
        String breadth = (entry.getBreadth().length() > 0) ? entry.getBreadth() : "0";
        String thick = (entry.getThick().length() > 0) ? entry.getThick() : "0";


        textview_product_name.setText("Item Name: " + entry.getProduct_display_name());
        textview_order_no.setText("Order No: " + entry.getOrder_number());
        textview_delivery_date.setText("Date: " + entry.getOrder_date());
//		textview_product_l_w_t_text.setText("LxWxT: "+entry.getLength()+"x"+entry.getBreadth()+"x"+entry.getThick());
        textview_product_l_w_t_text.setText("LxWxT: " + length + "x" + breadth + "x" + thick);
        textview_product_quantity.setText("Qty: " + entry.getQty() + " " + entry.getUom());

        textview_customer_name.setText("Customer Name :" + entry.getCustomer_name());
        textview_dealer_name.setText("Dealer Name: " + entry.getDealer_name());
        textview_outstanding_amount.setText("Outstanding Amount: " + entry.getOutstanding_balance());

        v.findViewById(R.id.llayout_approved_order).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                final ProductOrderListBean entry = _data.get(position);
                defaultTwoButtonDialog_approved(mActivity, "Are you sure want to approve this order", entry, position);


            }


        });


        v.findViewById(R.id.llayout_reject_order).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //deleteOrder(entry.getOrder_number(),position);


                defaultTwoButtonDialog_reject(mActivity, "Are you sure want to reject this order", entry, position);

            }


        });


        return v;
    }

    /*override method for inflate view start*/
    private void approvedOrder(String order_number, int position) {


        if (HelperMethods.isNetworkAvailable(mActivity)) {
            try {
                OrderApprovalFragment.getInstance().requset_no = "2";

                OrderApprovalFragment.getInstance().deleted_position = position;
                JSONObject jsonObjectRequest = new JSONObject();
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_APPROVED_ORDER);
                jsonObjectRequest.put("p_order_number", order_number);
                jsonObjectRequest.put("p_status", "1");

                //ProductOrderView.getInstance().requset_no = "2";


                Log.e("###request###", "" + jsonObjectRequest);


                new MyAsyncTask(mActivity, fragment,
                        jsonObjectRequest,
                        ApiList.URL_PLACE_ORDER).execute();


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mActivity, "Network Error", Toast.LENGTH_LONG).show();
        }


    }


    private void rejectOrder(String order_number, int position) {



        if (HelperMethods.isNetworkAvailable(mActivity)) {
            try {
                OrderApprovalFragment.getInstance().requset_no = "2";

                OrderApprovalFragment.getInstance().deleted_position = position;
                JSONObject jsonObjectRequest = new JSONObject();
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_APPROVED_ORDER);
                jsonObjectRequest.put("p_order_number", order_number);
                jsonObjectRequest.put("p_status", "2");

                //ProductOrderView.getInstance().requset_no = "2";


                Log.e("###request###", "" + jsonObjectRequest);


                new MyAsyncTask(mActivity, fragment,
                        jsonObjectRequest,
                        ApiList.URL_PLACE_ORDER).execute();


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mActivity, "Network Error", Toast.LENGTH_LONG).show();
        }
    }


    private void saveDatainPrefences(ProductOrderListBean entry) {
        // TODO Auto-generated method stub

        Log.e("Entry", "" + entry);

        Editor editor = mPrefs.edit();
        editor.putString("p_order_number", entry.getOrder_number());
        editor.putString("p_dealer_name", entry.getDealer_name());
        editor.putString("p_product_display_name", entry.getProduct_display_name());
        editor.putString("p_length", entry.getLength());
        editor.putString("p_breadth", entry.getBreadth());
        editor.putString("p_thick", entry.getThick());
        editor.putString("p_colour", entry.getColour());
        editor.putString("p_uom", entry.getUom());
        editor.putString("p_qty", entry.getQty());

        editor.putString("p_remark", entry.getRemark());
        editor.putString("p_customer_name", entry.getCustomer_name());
        editor.putString("p_customer_mobile", entry.getCustomer_mobile());
        editor.putString("p_delivery_date", entry.getDelivery_date());
        editor.putString("p_captured_image_url", entry.getCaptured_image_url());
        editor.putString("p_captured_length", entry.getCaptured_length());
        editor.putString("p_captured_bredth", entry.getCaptured_bredth());
        editor.putString("p_order_date", entry.getOrder_date());

        editor.putString("p_ext", entry.getExt());
        editor.putString("p_old_image", entry.getCaptured_image());


        editor.commit();


    }


    private void editOrder(String order_number) {


        if (HelperMethods.isNetworkAvailable(mActivity)) {
            try {
                ProductOrderView.getInstance().requset_no = "3";


                JSONObject jsonObjectRequest = new JSONObject();
                jsonObjectRequest.put("request", ApiList.API_PRODUCT_EDIT_ORDER);
                jsonObjectRequest.put("p_order_number", order_number);

                jsonObjectRequest.put("p_dealer_name", order_number);
                jsonObjectRequest.put("p_product_display_name", order_number);
                jsonObjectRequest.put("p_length", order_number);
                jsonObjectRequest.put("p_breadth", order_number);
                jsonObjectRequest.put("p_thick", order_number);

                jsonObjectRequest.put("p_colour", order_number);
                jsonObjectRequest.put("p_uom", order_number);
                jsonObjectRequest.put("p_qty", order_number);
                jsonObjectRequest.put("p_remark", order_number);
                jsonObjectRequest.put("p_customer_name", order_number);
                jsonObjectRequest.put("p_customer_mobile", order_number);

                jsonObjectRequest.put("p_delivery_date", order_number);
                jsonObjectRequest.put("p_captured_image", order_number);
                jsonObjectRequest.put("p_captured_length", order_number);
                jsonObjectRequest.put("p_captured_bredth", order_number);
                jsonObjectRequest.put("p_order_date", order_number);

                jsonObjectRequest.put("p_ext", order_number);
                jsonObjectRequest.put("p_captured_image_binary", order_number);
                jsonObjectRequest.put("p_old_image", order_number);


                Log.e("###request###", "" + jsonObjectRequest);


                new MyAsyncTask(mActivity, fragment,
                        jsonObjectRequest,
                        ApiList.URL_PLACE_ORDER).execute();


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mActivity, "Network Error", Toast.LENGTH_LONG).show();
        }


    }


    public void defaultTwoButtonDialog_approved(Activity activity, String msg, final ProductOrderListBean entry, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        approvedOrder(entry.getOrder_number(), position);
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void defaultTwoButtonDialog_reject(Activity activity, String msg, final ProductOrderListBean entry, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        rejectOrder(entry.getOrder_number(), position);
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}