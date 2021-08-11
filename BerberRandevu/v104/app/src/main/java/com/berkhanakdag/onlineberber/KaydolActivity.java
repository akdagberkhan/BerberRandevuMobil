package com.berkhanakdag.onlineberber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class KaydolActivity extends AppCompatActivity {



    private EditText edtAdSoyadKaydol,edtMailKaydol,edtKulAdiKaydol,edtSifreKaydol,edtTelefonKaydol;
    private Button btnfotoSecKaydol,btnKaydol;
    private ImageView secilenFotoKaydol;
    private TextView txtgirisGitKaydol;
    Bitmap secilenBitMap;
    String url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);

        SunucuAdres sunucuAdres=new SunucuAdres();
        url = "http://"+sunucuAdres.sunucuIp+"/berberApi/kaydol.php";


        secilenFotoKaydol=findViewById(R.id.imgSecilenKaydol);
        btnfotoSecKaydol=findViewById(R.id.btnFotoSecKaydol);
        edtAdSoyadKaydol=findViewById(R.id.edtTxtAdSoyadKaydol);
        edtKulAdiKaydol=findViewById(R.id.edtTxtKuladKaydol);
        edtMailKaydol=findViewById(R.id.edtTxtEmailKaydol);
        edtSifreKaydol=findViewById(R.id.edtTxtSifreKaydol);
        edtTelefonKaydol=findViewById(R.id.edtTxtTelefonKaydol);
        txtgirisGitKaydol=findViewById(R.id.txtGirisYap);
        btnKaydol=findViewById(R.id.btn_KayitOl);
        btnKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtMailKaydol.getText().toString().equals("") || edtAdSoyadKaydol.getText().toString().equals("") || edtKulAdiKaydol.getText().toString().equals("") || edtSifreKaydol.getText().toString().equals("") || edtTelefonKaydol.getText().toString().equals("")||secilenFotoKaydol.getDrawable()==null){
                    Toast.makeText(KaydolActivity.this, "Alanlar boş bırkılamaz.", Toast.LENGTH_SHORT).show();
                }
                else {
                    kaydol();
                }


            }
        });

        txtgirisGitKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(KaydolActivity.this,GirisYapActivity.class));
                finish();
            }
        });

    }
    private void kaydol(){
        RequestQueue requestQueue=Volley.newRequestQueue(KaydolActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String basarili = jsonObject.getJSONObject("Deger").getString("basarili");
                    String kulAdi = jsonObject.getJSONObject("Deger").getString("kuladi");

                    if(basarili.equals("01")){
                        Toast.makeText(getApplicationContext(), "Böyle bir kullanıcı adı mevcut!", Toast.LENGTH_SHORT).show();
                    }
                    if(basarili.equals("02")){
                        Toast.makeText(getApplicationContext(), "Tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                    }
                    if(basarili.equals("03")){
                        Toast.makeText(getApplicationContext(), "Fotoğraf hatası, Lütfen tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                    }
                    if(basarili.equals("04")){
                        Toast.makeText(getApplicationContext(), "Bilgileri kontrol edip tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                    }
                    if(basarili.equals("10")){
                        Toast.makeText(getApplicationContext(), "Kayıt başarılı", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(KaydolActivity.this,AnaSayfaActivity.class);
                        intent.putExtra("kulAdi",kulAdi);
                        startActivity(intent);
                        finish();
                    }

                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("foto",fotoStringCevir(secilenBitMap));
                params.put("adSoyad",edtAdSoyadKaydol.getText().toString());
                params.put("mail",edtMailKaydol.getText().toString().trim());
                params.put("kuladi",edtKulAdiKaydol.getText().toString().trim());
                params.put("sifre",edtSifreKaydol.getText().toString().trim());
                params.put("telefon",edtTelefonKaydol.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //FOTOĞRAF SEÇME VE İZİN -------------------------------------------------------------------------------------------------------------------------------------
    public void resimSecTikla(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            Intent intentGaleri=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGaleri,2);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intentGaleri=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGaleri,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==2 && resultCode==RESULT_OK&&data!=null){

            Uri uri =data.getData();
            try {
                if(Build.VERSION.SDK_INT>=28){

                    ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),uri);
                    Bitmap bitmap=ImageDecoder.decodeBitmap(source);
                    secilenBitMap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    secilenFotoKaydol.setImageBitmap(bitmap);
                }
                else{
                    secilenBitMap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    secilenFotoKaydol.setImageBitmap(secilenBitMap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String fotoStringCevir(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] fotoByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(fotoByte,Base64.DEFAULT);
    }
}