package com.example.mycashcalc;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MyAsyncTask extends AsyncTask<String, Void, Integer> {

    private static final int BUFFER_SIZE = 4096;
//    private static final String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
    private static final String host = "kurdt982.asuscomm.com:60001";
    private static final String user = "test";
    private static final String pass = "testtest1";
    private static final String fileDir = "tmp";
//    private static final String fileName = "111";

    @Override
    protected Integer doInBackground(String... opers) {
        int i = 0;
        switch (opers[0]) {
            case "1": i = uploadToFTP(opers[1], user, pass, host, fileDir); break;
            case "2": i = downloadfromFTP(opers[1], user, pass, host, fileDir); break;
            default: break;
        }
        return i;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
    }

    private int uploadToFTP(String db_name, String un, String pw, String ip, String dir) {

        if (db_name.isEmpty()) {
            Log.d(Main.LOG_TAG, "Database not created");
            return 0;
        }
        int res = 1;
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        String fname = db_name.substring(db_name.lastIndexOf("/") + 1) + Build.MODEL;
        try {
            URL url = new URL("ftp://"+un+":"+pw+"@"+ip+"/"+dir+"/"+fname+";type=i");
            URLConnection conn = url.openConnection();
            outputStream = conn.getOutputStream();

            inputStream = new FileInputStream(db_name);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            Log.e(Main.LOG_TAG, e.getMessage());
            res = 0;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(Main.LOG_TAG, e.getMessage());
                    res = 0;
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(Main.LOG_TAG, e.getMessage());
                    res = 0;
                }
            }
        }
        return res;
    }

    private int downloadfromFTP(String db_name, String un, String pw, String ip, String dir) {
        int res = 1;
        URLConnection con;
        BufferedInputStream in = null;
        FileOutputStream out = null;
        String fname = db_name.substring(db_name.lastIndexOf("/") + 1)+ Build.MODEL;
        try{
            URL url = new URL("ftp://"+un+":"+pw+"@"+ip+"/"+dir+"/"+fname+";type=i");
            con = url.openConnection();
            in = new BufferedInputStream(con.getInputStream());
            out = new FileOutputStream(db_name);
            int i = 0;
            byte[] bytesIn = new byte[1024];

            while ((i = in.read(bytesIn)) >= 0) {
                out.write(bytesIn, 0, i);
            }

        }catch(Exception e){
            Log.e(Main.LOG_TAG, "Error with read file on FTP server "+ e.getMessage());
            res = 0;
        }finally{
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(Main.LOG_TAG, e.getMessage());
                    res = 0;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(Main.LOG_TAG, e.getMessage());
                    res = 0;
                }
            }
            return res;
        }
    }

}
