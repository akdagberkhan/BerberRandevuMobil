package com.berkhanakdag.onlineberber;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.berkhanakdag.onlineberber.Adapter.SunucuAdres;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GirisYapActivity extends AppCompatActivity {


    private Button btnGirisYap;
    private EditText kuladiGirisYap,sifreGirisYap;
    private TextView kaydolbtnGirisYap;
    private CheckBox benihatirlaGirisYap;
    String url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);

        SunucuAdres sunucuAdres=new SunucuAdres();
        url = "http://"+sunucuAdres.sunucuIp+"/berberApi/girisYap.php";

        btnGirisYap=findViewById(R.id.btnGiris);
        kuladiGirisYap=findViewById(R.id.edtkuladi_giris);
        sifreGirisYap=findViewById(R.id.edtSifre_giris);
        kaydolbtnGirisYap=findViewById(R.id.txtKaydolGiris);
        benihatirlaGirisYap=findViewById(R.id.cboxGirisHatirla);

        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!kuladiGirisYap.getText().toString().equals("") && !sifreGirisYap.getText().toString().equals("")){
                    girisYap(kuladiGirisYap.getText().toString(),sifreGirisYap.getText().toString());
                }
                else{
                    Toast.makeText(GirisYapActivity.this, "Kullanıcı adı veya Şifre alanı boş bırakılamaz!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        kaydolbtnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GirisYapActivity.this,KaydolActivity.class));
            }
        });
    }

    public void girisYap(String kuladi,String sifre){


        RequestQueue requestQueue= Volley.newRequestQueue(GirisYapActivity.this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);


                    String basarili = jsonObject.getJSONObject("Deger").getString("basarili");
                    String kuladi = jsonObject.getJSONObject("Deger").getString("kuladi");

                    if(basarili.equals("1")){

                        Intent intent = new Intent(GirisYapActivity.this,AnaSayfaActivity.class);
                        intent.putExtra("kulAdi",kuladi);
                        startActivity(intent);
                        finish();
                    }
                    else if(basarili.equals("0")){
                        Toast.makeText(GirisYapActivity.this, "Kullanıcı adı veya Şifre hatalı!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {


                    Toast.makeText(GirisYapActivity.this,e.toString(),Toast.LENGTH_SHORT).show();

                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GirisYapActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String ,String> veri = new HashMap<>();
                veri.put("kuladi",kuladi);
                veri.put("sifre",sifre);
                return veri;
            }

        };

        requestQueue.add(stringRequest);

    }
}