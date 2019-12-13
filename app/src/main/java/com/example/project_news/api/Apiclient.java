package com.example.project_news.api;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Apiclient {

    public  static final String BASE_URL="https://newsapi.org/v2/";
    public static Retrofit retrofit;

    public  static  Retrofit getApiClient(){

        if(retrofit==null){

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(getUseOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }

    public static OkHttpClient.Builder getUseOkHttpClient(){
        try {
            final TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext =SSLContext.getInstance("SSL");
            sslContext.init(null,trustManagers,new SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();//@

            OkHttpClient.Builder builder= new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory,(X509TrustManager) trustManagers[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder;

        }catch (Exception ex){
            throw  new RuntimeException(ex);
        }


    }

}
