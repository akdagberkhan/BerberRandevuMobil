package com.berkhanakdag.onlineberber;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

public class GirisActivity extends AppCompatActivity {

    int a =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        if(InternetKontrol()){
            a=0;
            giris();
        }else{
            a=1;
            new CountDownTimer(5000, 1000) {
                public void onFinish() {
                    System.exit(0);
                }
                public void onTick(long millisUntilFinished) {
                    Toast.makeText(GirisActivity.this, "İnternet bağlantısı yok!", Toast.LENGTH_LONG).show();
                }
            }.start();
        }

    }

    public boolean InternetKontrol() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null
                && manager.getActiveNetworkInfo().isAvailable()
                && manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void giris(){
        new CountDownTimer(2000, 1000) {
            public void onFinish() {
                startActivity(new Intent(GirisActivity.this,GirisYapActivity.class));
                finish();
            }
            public void onTick(long millisUntilFinished) {
                if (a==1){
                    Toast.makeText(GirisActivity.this, "İnternet bağlantısı yok!", Toast.LENGTH_LONG).show();
                }
            }
        }.start();

    }
}
