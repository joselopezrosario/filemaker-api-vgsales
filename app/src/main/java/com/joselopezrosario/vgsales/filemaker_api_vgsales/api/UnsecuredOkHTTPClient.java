package com.joselopezrosario.vgsales.filemaker_api_vgsales.api;

import android.annotation.SuppressLint;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * DevOkHTTPClient
 * Use this Class to run API calls in development using the default SSL certificate
 * provided by FileMaker Server or other self-signed certificates
 * Source: https://www.stubbornjava.com/posts/okhttpclient-trust-all-ssl-certificates
 */
final class UnsecuredOkHTTPClient {
    public static OkHttpClient trustAllSslClient(OkHttpClient client) {
        OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
            @SuppressLint("BadHostnameVerifier")
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return builder.build();
    }

    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };
    private static final SSLContext trustAllSslContext;

    static {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();
}
