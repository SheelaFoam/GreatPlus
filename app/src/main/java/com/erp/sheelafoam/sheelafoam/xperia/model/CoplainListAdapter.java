package com.erp.sheelafoam.sheelafoam.xperia.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.sheelafoam.xperia.activity.ComplaintNew;
import com.erp.sheelafoam.sheelafoam.xperia.dataholders.GetSetComplaint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sudhirharit on 22/11/17.
 */

public class CoplainListAdapter extends BaseAdapter
{
    Context c;
    ArrayList<GetSetComplaint> list;
    LayoutInflater inflater;

    ArrayList<String> color=new ArrayList<String>();

    ArrayList<Drawable> drawable=new ArrayList<Drawable>();

    Random rnd=new Random();
    int max=4;
    int min=0;
    ComplaintNew complaint;
    String userType;
    public CoplainListAdapter(Context c, ArrayList<GetSetComplaint> list)
    {
        this.c=c;
        this.list=list;
        this.inflater=(LayoutInflater.from(c));
        this.complaint=(ComplaintNew)c;
        color.add("#36acb0");
        color.add("#fe5653");
        color.add("#194445");
        color.add("#7b7b7b");
        color.add("#7d962c");

        SharedPreferences sharedPreferences = c.getSharedPreferences("OTP_SESSION", Context.MODE_PRIVATE);
        userType = sharedPreferences.getString("type_user","");
//       drawable.add(c.getResources().getDrawable(R.drawable.xperia_complaint_layout_bdr));
//       drawable.add(c.getResources().getDrawable(R.drawable.xperia_complaint_layout_bdr2));
//       drawable.add(c.getResources().getDrawable(R.drawable.xperia_complaint_layout_bdr3));
//       drawable.add(c.getResources().getDrawable(R.drawable.xperia_complaint_layout_bdr4));
//       drawable.add(c.getResources().getDrawable(R.drawable.xperia_complaint_layout_bdr5));

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup parent)
    {
        itemView=inflater.inflate(R.layout.custom_complaint_layout,null);
        TextView no, cmpDate, city, name, mobile, address, product, billDate, dealer, problem, pending;
        Button btnAddDetails, btnMore;
        no=(TextView)itemView.findViewById(R.id.txtNo);
        cmpDate=(TextView)itemView.findViewById(R.id.txtDate);
        city=(TextView)itemView.findViewById(R.id.txtCity);
        name=(TextView)itemView.findViewById(R.id.txtName);
        mobile=(TextView)itemView.findViewById(R.id.txtMobile);
        address=(TextView)itemView.findViewById(R.id.txtAddress);
        product=(TextView)itemView.findViewById(R.id.txtProduct);
        billDate=(TextView)itemView.findViewById(R.id.txtBillDate);
        dealer=(TextView)itemView.findViewById(R.id.txtDealer);
        problem=(TextView)itemView.findViewById(R.id.txtProblem);
        pending=(TextView)itemView.findViewById(R.id.txtPending);
        btnAddDetails=(Button)itemView.findViewById(R.id.btnAddDetails);
        btnMore = (Button) itemView.findViewById(R.id.btnMore);

        GetSetComplaint gss=list.get(position);
        no.setText("No. "+gss.getNo());
        cmpDate.setText(gss.getCmpDate());
        city.setText(gss.getCity());
        name.setText(gss.getName());
        mobile.setText(gss.getMobile());
        address.setText(gss.getAddress());
        product.setText(gss.getProduct());
        billDate.setText(gss.getBillDate());
        dealer.setText(gss.getDealer());
        problem.setText(gss.getProblem());
        pending.setText(gss.getPending());

        if(userType.equalsIgnoreCase("AGENT")){
            btnMore.setVisibility(View.GONE);
        }else {
            btnMore.setVisibility(View.VISIBLE);
        }


        btnAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaint.addetails(position);
            }
        });
        btnMore = (Button) itemView.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaint.viewMoreDetails(position);
            }
        });
        //Collections.shuffle(color);
        itemView.setBackgroundColor(Color.parseColor(color.get(rnd.nextInt(max-min+1)+min)));
        // itemView.setBackground(drawable.get(rnd.nextInt(max-min+1)+min));
        return itemView;
    }
}