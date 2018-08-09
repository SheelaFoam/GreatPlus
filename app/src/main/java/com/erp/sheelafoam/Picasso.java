package com.erp.sheelafoam;

import android.content.Context;
import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Picasso {

    private static com.squareup.picasso.Picasso mInstance = null;

    private Picasso(Context context) {
        OkHttpClient client = new OkHttpClient();
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        } };
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            client.setSslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mInstance = new com.squareup.picasso.Picasso.Builder(context)
                .downloader(new OkHttpDownloader(client))
                .listener(new com.squareup.picasso.Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(com.squareup.picasso.Picasso picasso, Uri uri, Exception exception) {
                       // Log.e("PICASSO", exception);
                    }
                }).build();

    }

    public static com.squareup.picasso.Picasso with(Context context) {
        if (mInstance == null) {
             new Picasso(context);
        }
        return mInstance;
    }
}