package com.erp.sheelafoam.sheelafoam.utility;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.erp.sheelafoam.sheelafoam.webview.WebViewDemo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**
 * Created by dell on 08/01/2016.
 */
public class CallWeb extends AsyncTask<String,Void,Void> {

    private Context context = null;
    private ProgressDialog dialog = null;
    int resultMssg = 0;
    String mobileNum;
    public CallWeb(Context con)
    {
        context = con;
        dialog = new ProgressDialog(context);
    }

    protected void onPreExecute()
    {
        if(null != dialog) {
            dialog.setMessage("Please wait..");
            dialog.show();
        }
    }
    @Override
    protected Void doInBackground(String... params) {


        String SOAP_ACTION = "http://125.19.46.252/ws/index.php#getLoginStatus";
        String NAMESPACE = "http://125.19.46.252/ws/index.php";
        String METHOD_NAME = "getLoginStatus";
        String URL = "http://125.19.46.252/ws/index.php";
        String resultData = "";
        SoapSerializationEnvelope soapEnv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //SoapObject UserCredentials = new SoapObject("Types", "UserCredentials6");
        SoapObject loginReq = new SoapObject(NAMESPACE,METHOD_NAME);
        mobileNum = params[0];
        loginReq.addProperty("MOBILE", mobileNum);
        //loginReq.addProperty("MOBILE", "9428007792");
        //soapEnv.setOutputSoapObject(loginReq);
        soapEnv.setOutputSoapObject(loginReq);
        HttpTransportSE http = new HttpTransportSE(URL);
        String result = null;
        try{
            http.call(SOAP_ACTION, soapEnv);
            while(result == null) {
                result = (String)soapEnv.bodyIn;
            }
            Log.d("testing Soap", "SOAP tested");


            resultMssg = Integer.parseInt(result);

        }
        catch(Exception ex)
        {
            Log.d("we get the exception",ex.toString());
        }
        return null;
    }


    protected void onPostExecute(Void param)
    {
        dialog.dismiss();
        if(resultMssg == 1) {
            Intent intent = new Intent(context, WebViewDemo.class);
            intent.putExtra("MOBILE",mobileNum);
            context.startActivity(intent);

        }
        else
        {
            final AlertDialog.Builder builder1 =
                               new AlertDialog.Builder(new ContextThemeWrapper(context,android.R.style.Theme_DeviceDefault_Dialog));
            builder1.setMessage("Mobile is not registered with us.");
            builder1.setCancelable(true);

            builder1.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder1.show();
        }


    }
}
