package com.example.algoitso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WrinkleAnalysis extends AppCompatActivity {
    int serverResponseCode = 0;
    String WrinkleselectServerUri = null;
    String WrinkleselectServerIndex = null;
    String WrinkleselectServerName = null;
    Bitmap bmImg;
    String bmName;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrinkle_analysis);

        ImageButton btn_back = (ImageButton) findViewById(R.id.back_btn);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent_back = new Intent(getApplicationContext(), Selectanalysis.class);
                startActivity(intent_back);
                finish();
            }
        });


        String User_ID = SharedPreference.getAttribute(getApplicationContext(),"useremail");;
        WrinkleselectServerIndex = "http://ec2-18-223-44-123.us-east-2.compute.amazonaws.com:3000/select/" + User_ID + "/Wrinkleindex";



        Thread th = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
                WrinkleselectIndex(WrinkleselectServerIndex);

            }
        };
        th.start();
        try{
            th.join();
            for (i = 0; i < Integer.parseInt(WrinkleselectServerIndex); i++){
                WrinkleselectServerUri = "http://ec2-18-223-44-123.us-east-2.compute.amazonaws.com:3000/select/" + User_ID + "/Wrinkle/" + i;
                WrinkleselectServerName = "http://ec2-18-223-44-123.us-east-2.compute.amazonaws.com:3000/select/" + User_ID + "/Wrinklename/" + i;
                Thread thdata = new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                            }
                        });
                        WrinkleselectFile(WrinkleselectServerUri);
                        WrinkleselectName(WrinkleselectServerName);

                    }
                };
                thdata.start();
                try{
                    thdata.join();
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
                    ImageView imageView = new ImageView(getBaseContext());
                    imageView.setImageBitmap(bmImg);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(390,390);
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    linearLayout.addView(imageView);
                    TextView textView = new TextView(getBaseContext());

                    String subtext = "분석일시\n";

                    int idx = bmName.indexOf("_");
                    String sxt1 = bmName.substring(idx+1);
                    idx = sxt1.indexOf("_");
                    String sxt2 = sxt1.substring(idx+1);
                    String Year = sxt2.substring(0,4);
                    String Month = sxt2.substring(4,6);
                    String Day = sxt2.substring(6,8);
                    String Hour = sxt2.substring(9,11);
                    String Min = sxt2.substring(11,13);
                    String YMDHM = Year + "-" + Month + "-" + Day + "   " + Hour + ":" + Min;
                    textView.setText("분석일시\n"+YMDHM);
                    textView.setTextSize(30);
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    linearLayout.addView(textView);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            if(i == 0){
                Toast.makeText(getApplicationContext(), "주름 분석 기록이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                Intent mintent = new Intent(this, MainActivity.class);
                startActivity(mintent);
                finish();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }






    //------------------------------------------------------------------------------
    public int WrinkleselectIndex(String sourceUri) {
        HttpURLConnection conn = null;
        try {
            // open a URL connection to the Servlet
            URL url = new URL(sourceUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if (serverResponseCode == 200) {
                StringBuffer sb = new StringBuffer();
                byte[] b = new byte[4096];
                for (int n; (n = conn.getInputStream().read(b)) != -1;) {
                    sb.append(new String(b, 0, n));
                }
                WrinkleselectServerIndex = sb.toString();
                System.out.println("################   Index   ######################");
                System.out.println(WrinkleselectServerIndex);
                System.out.println("############################################");
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(WrinkleAnalysis.this, "MalformedURLException",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(WrinkleAnalysis.this, "Got Exception : see logcat ",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
        }
        return serverResponseCode;
    } // End else block
    //------------------------------------------------------------------------------

    public int WrinkleselectFile(String sourceUri) {
        HttpURLConnection conn = null;
        try {
            // open a URL connection to the Servlet
            URL url = new URL(sourceUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if (serverResponseCode == 200) {
                bmImg = BitmapFactory.decodeStream(conn.getInputStream());
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(WrinkleAnalysis.this, "MalformedURLException",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(WrinkleAnalysis.this, "Got Exception : see logcat ",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
        }
        return serverResponseCode;
    } // End else block

    //------------------------------------------------------------------------------------------
    public int WrinkleselectName(String sourceUri) {
        HttpURLConnection conn = null;
        try {
            // open a URL connection to the Servlet
            URL url = new URL(sourceUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if (serverResponseCode == 200) {
                StringBuffer sb = new StringBuffer();
                byte[] b = new byte[4096];
                for (int n; (n = conn.getInputStream().read(b)) != -1;) {
                    sb.append(new String(b, 0, n));
                }
                bmName = sb.toString();

            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(WrinkleAnalysis.this, "MalformedURLException",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(WrinkleAnalysis.this, "Got Exception : see logcat ",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
        }
        return serverResponseCode;
    } // End else block
}