package com.erp.sheelafoam.sheelafoam.categories;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.sheelafoam.activity.CheckoutActivity;
import com.erp.sheelafoam.sheelafoam.activity.DocumentUploadNewActivity;
import com.erp.sheelafoam.sheelafoam.adapter.CategoriesAdapter;
import com.erp.sheelafoam.sheelafoam.adapter.SubcategoriesAdapter;
import com.erp.sheelafoam.sheelafoam.entry.CategoryEntry;
import com.erp.sheelafoam.sheelafoam.entry.SubCategories;
import com.erp.sheelafoam.sheelafoam.exchangeschame.ExchangeSchameActivity;
import com.erp.sheelafoam.sheelafoam.fragments.ContactFragment;
import com.erp.sheelafoam.sheelafoam.fragments.OrderApprovalFragment;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderFragment;
import com.erp.sheelafoam.sheelafoam.fragments.ProductOrderView;
import com.erp.sheelafoam.sheelafoam.report.ReportOrderStatusFragment;
import com.erp.sheelafoam.sheelafoam.utility.AppConstant;
import com.erp.sheelafoam.sheelafoam.utility.AsyncTaskListner;
import com.erp.sheelafoam.sheelafoam.utility.ConnectionDetector;
import com.erp.sheelafoam.sheelafoam.utility.GlobalVariables;
import com.erp.sheelafoam.sheelafoam.utility.MyAsyncTask;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ComplaintNew;
import com.erp.sheelafoam.sheelafoam.xperia.activity.MrpCalculation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryFragment extends Fragment implements AsyncTaskListner {

    //http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/

    CategoriesAdapter adapter_category;
    //ExpandableListView expListView;
    GridView gridview;
    int Call = 0;
    WebView web;
    /**
     * list object for store category header.
     **/
    List<CategoryEntry> listCategoryHeader;
    /**
     * hash map for store subcategories. index name is category and data on index is
     * subcategories.
     **/

    HashMap<String, List<SubCategories>> listSubCategories;
    /**
     * Json object for process request.
     **/

    JSONObject json_obj_request;
    /**
     * Json object for get  response.
     **/
    JSONObject json_obj_response;
    /**
     * connection object for check internet connectivity.
     **/

    ConnectionDetector con;
    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    /*fragment lifecycle method onCreate end*/


    /*fragment lifecycle method onCreateView start*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        con = new ConnectionDetector(getActivity());
        web = (WebView) view.findViewById(R.id.web);
        mPrefs = getActivity().getSharedPreferences("Location",
                Context.MODE_PRIVATE);

        //Log.e("UsrType",mPrefs.getString("op_user_type", ""));

        if (mPrefs.getString("op_user_type", "").equals("DEALER")) {
            web.setVisibility(View.VISIBLE);
        } else {
            web.setVisibility(View.INVISIBLE);
        }
        // get the listview

        gridview = (GridView) view.findViewById(R.id.gridview);

        // preparing list data

        /**calling method for process json request.**/


        if (con.isConnectingToInternet()) {
            Call = 1;
            jsonRequest();
        } else {
            //Toast.makeText(getActivity(), GlobalVariables.CONNECTION_ERROR, Toast.LENGTH_LONG).show();
            GlobalVariables.defaultOneButtonDialog(getActivity(), GlobalVariables.CONNECTION_ERROR);
        }

        //prepareListData();


        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v,
                                    int position, long id) {
                // TODO Auto-generated method stubring


                String name = listCategoryHeader.get(position).getName();
                if (name.equals("Support")) {
                    // This block see Support option

                    ContactFragment fragment = new ContactFragment();

                    addFragment(fragment);
						
						/*Intent i=new Intent(getActivity(),ScanQrCodeActivity.class);
						getActivity().startActivity(i);*/

                } else if (name.equalsIgnoreCase("Mrp Calculator")) {
                    Intent i = new Intent(getActivity(), MrpCalculation.class);
                    startActivity(i);
                } else if (name.equalsIgnoreCase("Upload Document")) {
						/*Intent i=new Intent(getActivity(),MrpCalculation.class);
						startActivity(i);*/

                    Intent i = new Intent(getActivity(), DocumentUploadNewActivity.class);
                    startActivity(i);
                } else if (name.equalsIgnoreCase("DIGITAL PAYMENT")) {
						/*Intent i=new Intent(getActivity(),MrpCalculation.class);
						startActivity(i);*/

                    Intent i = new Intent(getActivity(), CheckoutActivity.class);
                    startActivity(i);
                } else {
                    List<SubCategories> list_subcategories = listSubCategories.get(name);

                    //Toast.makeText(getActivity(), "Clicked "+list_subcategories.get(0).getName(), Toast.LENGTH_LONG).show();

                    showPopup(list_subcategories, name);
                }
            }
        });


        view.setOnClickListener(null);


        return view;
    }


    /**
     * Method: This is a callback method for handle response after just request is successfully
     * executed.
     **/
    @Override
    public void onTaskComplete(String result) {
        // TODO Auto-generated method stub

        Log.e("#Response#", result.toString());

        try {
            if (Call == 1) {
                if (result != null && result.length() > 0) {
                    json_obj_response = new JSONObject(result);
                    String status = json_obj_response.getString("status");

                    if (status.equals("200")) {
                        Log.e("status", status);
                        categoryList(json_obj_response);
                        sub_categoryList(json_obj_response);
                        setAdapter();
                        //expandListByDefault(listCategoryHeader);
                    } else {
                        String msg = json_obj_request.getString("massage");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    }
                    if (mPrefs.getString("op_user_type", "").equals("DEALER")) {
                        Call = 2;
                        getCaStatus();
                    }
                }
            } else if (Call == 2) {
                JSONObject o = new JSONObject(result);
                String open = "<html><body>";
                String marquee = "<marquee style=\"color: #ffffff !important;\">" + o.getJSONObject("data").getString("op_message") + "</marquee>";
                String close = "</body></html>";
                web.setBackgroundColor(Color.TRANSPARENT);
                if (!o.getJSONObject("data").getString("op_message").equalsIgnoreCase("null")) {
                    web.loadDataWithBaseURL(null, open + marquee + close, "text/html", "UTF-8", null);
                }
                //Toast.makeText(getActivity(), o.getJSONObject("data").getString("op_message"), Toast.LENGTH_LONG).show();
                Log.e("#Ca Response#", result.toString());
            }

        } catch (Exception e) {
        }
    }


    public void getCaStatus() {
        try {
            json_obj_request = new JSONObject();
            json_obj_request.put("request", "get_ca_order_status");
            json_obj_request.put("p_dealer_id", mPrefs.getString("DEALER_ID", ""));
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        Log.e("#CA Status Request#", json_obj_request.toString());
        new MyAsyncTask(getActivity(), CategoryFragment.this, json_obj_request).execute();

    }

    /*
     * Method : This is a method used for create entry object for category and sub_category
     * after got response.
     */


    /**
     * Method: This is a method which is used form make api request.
     **/

    public void jsonRequest() {
        try {
            json_obj_request = new JSONObject();
            json_obj_request.put("request", "getMenuList");
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        Log.e("#Menu List Request#", json_obj_request.toString());
        new MyAsyncTask(getActivity(), CategoryFragment.this, json_obj_request).execute();

    }

    /**
     * Method : Method for create category list.
     **/

    public void categoryList(JSONObject jObject) {
        listCategoryHeader = new ArrayList<CategoryEntry>();
		
		/*try {
			
			if(jObject!=null){
				JSONArray data=jObject.getJSONArray("data");
				if(data!=null && data.length()>0){
					for(int i=0;i<data.length();i++)
					{
						JSONObject obj=data.getJSONObject(i);
						String parent_menu=obj.getString("PARENT_MENU");
						String url=obj.getString("PARENT_MENU_ICON");
						CategoryEntry entry=new CategoryEntry();
						entry.setName(parent_menu);
						entry.setUrl(url);
						listCategoryHeader.add(entry);
					}
					
					// entry for support
					CategoryEntry entry=new CategoryEntry();
					entry.setName("Support");
					entry.setUrl("");
					listCategoryHeader.add(entry);
				}else{
					GlobalVariables.defaultOneButtonDialog(getActivity(), "No record found");
				}
				
				
			}else{
				
				GlobalVariables.defaultOneButtonDialog(getActivity(), "No record found");
			}
			*/


        try {

            JSONArray data = jObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                String parent_menu = obj.getString("PARENT_MENU");
                String url = obj.getString("PARENT_MENU_ICON");
                CategoryEntry entry = new CategoryEntry();
                entry.setName(parent_menu);
                entry.setUrl(url);
                listCategoryHeader.add(entry);
            }


            // entry for MRP calculation
//					CategoryEntry entrymrp=new CategoryEntry();
//					entrymrp.setName("Mrp Calculator");
//					entrymrp.setUrl("");
//					if(XperiaFunctions.getUserType(getActivity()).equalsIgnoreCase("DISTRIBUTOR") || XperiaFunctions.getUserType(getActivity()).equalsIgnoreCase("DEALER") || XperiaFunctions.getUserType(getActivity()).equalsIgnoreCase("EMPLOYEE"))
//					
//						{listCategoryHeader.add(entrymrp);}

            // entry for support
            CategoryEntry entry = new CategoryEntry();
            entry.setName("Support");
            entry.setUrl("");
            listCategoryHeader.add(entry);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Method : Method for get sub_category list.
     **/

    public void sub_categoryList(JSONObject jObject) {
        listSubCategories = new HashMap<String, List<SubCategories>>();

        try {
            JSONArray data = jObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                /**
                 * sub_categories array.
                 * **/

                JSONArray array_sub_categories = obj.getJSONArray("CHILD_MENU");

                List<SubCategories> list_subcategories = new ArrayList<SubCategories>();
                for (int j = 0; j < array_sub_categories.length(); j++) {
                    String sub_categories_id = array_sub_categories.getJSONObject(j).getString("sub_cat_id");
                    String sub_categories_name = array_sub_categories.getJSONObject(j).getString("sub_cat_name");
                    String url = array_sub_categories.getJSONObject(j).getString("sub_cat_icon");
                    SubCategories entry = new SubCategories();
                    entry.setId(sub_categories_id);
                    entry.setName(sub_categories_name);
                    entry.setUrl(url);
                    list_subcategories.add(entry);
                }

                listSubCategories.put(listCategoryHeader.get(i).getName(), list_subcategories);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setAdapter() {

        Log.e("listCategoryHeader size", "" + listCategoryHeader.size());
        Log.e("listSubCategories size", "" + listSubCategories.size());
        //listAdapter = new ExpandableListAdapter(getActivity(), listCategoryHeader, listSubCategories);
        adapter_category = new CategoriesAdapter(getActivity(), listCategoryHeader, listSubCategories);

        // setting list adapter
        //expListView.setAdapter(listAdapter);

        gridview.setAdapter(adapter_category);
    }


    public void callSubCatgoryFragment(int id) {

        switch (id) {
            case 1:

                AppConstant.EDIT_ORDER = 0;
                AppConstant.isNewFoam = false;
                ProductOrderFragment fragment = new ProductOrderFragment();
                addFragment(fragment);

                break;
            case 2:
                ProductOrderView viewfragment = new ProductOrderView();

                addFragment(viewfragment);

                break;
            case 3:

                Intent i = new Intent(getActivity(), ComplaintNew.class);
                getActivity().startActivity(i);

                break;
            case 4:
                Intent exchangeSchameIntent = new Intent(getActivity(), ExchangeSchameActivity.class);
                getActivity().startActivity(exchangeSchameIntent);

                break;
            case 5:

                break;

            case 6:

                OrderApprovalFragment fragment_approval = new OrderApprovalFragment();

                addFragment(fragment_approval);
                break;

            case 7:


                break;

            case 8:

                ReportOrderStatusFragment fragment_report_order_status = new ReportOrderStatusFragment();

                addFragment(fragment_report_order_status);
                break;
        }

    }

    public void addFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.flayout, fragment);
        ft.commit();
    }

    /**
     *
     * **/


    public void showPopup(final List<SubCategories> list, String title) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_gridview);

        GridView gridView = (GridView) dialog.findViewById(R.id.gridView);
        TextView textview_header = (TextView) dialog.findViewById(R.id.textview_header);
        RelativeLayout rlayout_cross_btn = (RelativeLayout) dialog.findViewById(R.id.rlayout_cross_btn);

        rlayout_cross_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        textview_header.setText(title);

        SubcategoriesAdapter adapter = new SubcategoriesAdapter(list, getActivity());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position,
                                    long id) {
                // TODO Auto-generated method stub


                int id_subcategory = Integer.valueOf(list.get(position).getId());
                callSubCatgoryFragment(id_subcategory);
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
