package com.erp.sheelafoam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erp.sheelafoam.R;
import com.erp.sheelafoam.constant.Constant;
import com.erp.sheelafoam.utils.Util;

/**
 * Created by priyanka.sharma on 12/2/2016.
 */

public class CompanyPerformance_ThisWeek_HOME extends Fragment {
    TextView tp_achieved, tp_target, rcv_achieved, rcv_target, rcv_txt, txt_tp_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_performance_home
                , container, false);

        init(view);

        return view;
    }

    private void init(View view) {

        tp_achieved = (TextView) view.findViewById(R.id.tp_achieved);
        tp_target = (TextView) view.findViewById(R.id.tp_target);
        rcv_achieved = (TextView) view.findViewById(R.id.rcv_achieved);
        rcv_target = (TextView) view.findViewById(R.id.rcv_target);
        txt_tp_name = (TextView) view.findViewById(R.id.txt_tp_name);
        rcv_txt = (TextView) view.findViewById(R.id.rcv_txt);

        tp_achieved.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.SP_TP_WEEK_ACH));
        tp_target.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.SP_TP_WEEK_TARGET));
        rcv_achieved.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.SP_RCV_WEEK_TARGET));
        rcv_target.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.SP_RCV_WEEK_ACH));

        txt_tp_name.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.SP_tp_name));
        rcv_txt.setText(Util.getSharedPrefrenceValue(getActivity(), Constant.SP_rcv_name));

    }

}
