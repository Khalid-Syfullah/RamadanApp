package com.qubitech.ramadanapp.ui.dashboard;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHandler {
    private static final String TAG= com.qubitech.ramadanapp.ui.dashboard.HttpHandler.class.getSimpleName();
    public HttpHandler(){

    }
    public String makeServiceCall(String reqUrl){
        String response=null;
        try {
            URL url=new URL(reqUrl);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAE5cNgEAAAAABrBpiLV5aXk1nFMCl3i5Ro8ptsA%3D9KHayZbLfOHVakpFPV1nxwt4U3JfVY6vNo5opretW6frHpchjU");

//            if(socialMedia.equals("twitter")) {
//                connection.setRequestProperty("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAE5cNgEAAAAABrBpiLV5aXk1nFMCl3i5Ro8ptsA%3D9KHayZbLfOHVakpFPV1nxwt4U3JfVY6vNo5opretW6frHpchjU");
//            }
            InputStream inputStream=new BufferedInputStream(connection.getInputStream());
            response= convertStreamToString(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    private String convertStreamToString(InputStream inputStream) {
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder=new StringBuilder();
        String line;
        try{
            while ((line=reader.readLine()) !=null){
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
