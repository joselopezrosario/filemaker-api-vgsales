package com.joselopezrosario.vgsales.filemaker_api_vgsales;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public final class Utilities {

    public Utilities() { throw new AssertionError("No Utilities instances for you!"); }

    /**
     * encodeFileMakerCredentials
     * @param accountName the FileMaker account with fmrest privilegess
     * @param password the FileMaker account's password
     * @return the Base54 encoded credentials
     */
    static String encodeFileMakerCredentials(String accountName, String password){
        if ( accountName == null || password == null){
            return null;
        }
        String credentials = accountName + ":" + password;
        String encodedCredentials;
        byte[] credentialBytes;
        try {
            credentialBytes = credentials.getBytes("UTF-8");
            encodedCredentials = Base64.encodeToString(credentialBytes, Base64.DEFAULT).trim();
            return encodedCredentials;
        } catch (UnsupportedEncodingException e) {
            System.out.print("Could not encode credentials: " + e.toString());
            return null;
        }
    }
}