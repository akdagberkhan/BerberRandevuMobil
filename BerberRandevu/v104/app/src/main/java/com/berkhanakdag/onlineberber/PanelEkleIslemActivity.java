package com.berkhanakdag.onlineberber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanelEkleIslemActivity extends AppCompatActivity {

    private String gelenBerbermi,gelenKulAdi,gelenFotoYol,gelenBerberId;
    private EditText panelEkleIslemAdi,panelEkleIslemUcret;
    private Button panelEkleISlemBtn,panelEkleISlemFotoBtn,panelEkleISlemSilBtn;
    private ConstraintLayout panelEkleCons;
    private ImageView panelEkleProfilFoto,panelEkleFoto1;
    private TextView panelEkleKulAdTxt;
    private ArrayList<ImageView> ImgListe;
    private Spinner panelEkleSp;
    private ImageView panelEkleGeriBtn;
    private ArrayList<String> tarifeAdi;
    private ArrayList<String> tarifeId;
    private ArrayAdapter<String> dataAdapterTarifeAdi;
    private ArrayAdapter<String> dataAdapterTarifeId;

    String urlPanelIslemFoto = "";
    SunucuAdres sunucuAdres=new SunucuAdres();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_ekle_islem);
        Intent intent=getIntent();
        urlPanelIslemFoto = "http://"+sunucuAdres.sunucuIp+"/berberApi/panelislemfoto.php";

        gelenBerberId= intent.getStringExtra("berberId");
        gelenBerbermi= intent.getStringExtra("berbermi");
        gelenKulAdi= intent.getStringExtra("kulAd");
        gelenFotoYol= intent.getStringExtra("fotoYol");

        panelEkleKulAdTxt = findViewById(R.id.panelEkleTxtKul);
        panelEkleProfilFoto=findViewById(R.id.panelEkleImgProfil);
        panelEkleCons = findViewById(R.id.panelEkleGrid2);
        panelEkleIslemAdi = findViewById(R.id.panelEkleIslemAdi);
        panelEkleIslemUcret=findViewById(R.id.panelEkleIslemUcret);
        panelEkleISlemBtn = findViewById(R.id.panelEkleIslemBtn);
        panelEkleISlemFotoBtn=findViewById(R.id.panelEkleIslemFotoBtn);
        panelEkleFoto1 = findViewById(R.id.panelEkleImg1);
        panelEkleSp = findViewById(R.id.panelEkleIslemSpinner);
        panelEkleISlemSilBtn = findViewById(R.id.panelEkleIslemSilBtn);
        panelEkleGeriBtn = findViewById(R.id.panelEkleImgGeri);
        Picasso.get().load(gelenFotoYol).centerCrop().resize(100,100).into(panelEkleProfilFoto);
        panelEkleKulAdTxt.setText(gelenKulAdi);

        if(gelenBerbermi.equals("1"))
        {
            islemFotoCek();
        }

        panelEkleGeriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        panelEkleFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(Intent.ACTION_GET_CONTENT);
                intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent1.setType("image/*");
                startActivityForResult(intent1, 2);
            }
        });
        panelEkleISlemFotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotolariEkle();
            }
        });

        panelEkleISlemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                islemEkle();
            }
        });
        panelEkleISlemSilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                islemSil();
            }
        });

    }


    private void fotolariEkle()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(PanelEkleIslemActivity.this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlPanelIslemFoto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String basari= jsonObject.getJSONObject("Deger").getString("basarili");
                    if(basari.equals("1"))
                    {
                        Toast.makeText(PanelEkleIslemActivity.this, "Fotoğraflar Kaydedildi", Toast.LENGTH_SHORT).show();
                    }
                    else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                fotoStringCevir();
                for (int i = 0 ; i< FotoSecilenler.size();i++)
                {
                   params.put("foto"+i,FotoSecilenler.get(i));
                }
                params.put("FotoSayisi",String.valueOf(FotoSecilenler.size()));
                params.put("islem","2");
                params.put("kulAdi",gelenKulAdi);
                params.put("berberId",gelenBerberId);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private int silinecekEleman;
    private void islemSil()
    {
        silinecekEleman = panelEkleSp.getSelectedItemPosition();
        RequestQueue requestQueue = Volley.newRequestQueue(PanelEkleIslemActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlPanelIslemFoto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject=new JSONObject(response);
                    String basarili= jsonObject.getJSONObject("Deger").getString("basarili");

                    if(basarili.equals("1"))
                    {
                        Toast.makeText(PanelEkleIslemActivity.this, "İşlem Silme Başarılı!", Toast.LENGTH_SHORT).show();
                        islemFotoCek();

                    }
                    else
                    {
                        Toast.makeText(PanelEkleIslemActivity.this, "Silme İşlemi Başasrısız, Lütfen Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("silinecekId",tarifeId.get(silinecekEleman));
                params.put("berberId",gelenBerberId);
                params.put("islem","3");
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void islemEkle()
    {
        if(panelEkleIslemAdi.getText().toString().equals("") || panelEkleIslemUcret.getText().toString().equals(""))
        {
            Toast.makeText(this, "Boş bırakılamaz!", Toast.LENGTH_SHORT).show();
        }
        else
            {
                String islemAdi= panelEkleIslemAdi.getText().toString();
                int islemUcret = Integer.valueOf(panelEkleIslemUcret.getText().toString());

                RequestQueue requestQueue= Volley.newRequestQueue(PanelEkleIslemActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPanelIslemFoto, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            String basarili= jsonObject.getJSONObject("Deger").getString("basarili");

                            if(basarili.equals("1"))
                            {
                                Toast.makeText(PanelEkleIslemActivity.this, "İşlem Ekleme Başarılı!", Toast.LENGTH_SHORT).show();
                                islemFotoCek();
                                panelEkleIslemAdi.setText("");
                                panelEkleIslemUcret.setText("");
                            }
                            else
                                {
                                    Toast.makeText(PanelEkleIslemActivity.this, "Ekleme İşlemi Başasrısız, Lütfen Tekrar Deneyiniz.", Toast.LENGTH_SHORT).show();
                                }
                        } 
                        catch (JSONException e) 
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> params = new HashMap<>();
                        params.put("islem","1");
                        params.put("islemUcret",String.valueOf(islemUcret));
                        params.put("islemAdi",islemAdi);
                        params.put("berberId",gelenBerberId);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }

    }
    ArrayList<String> FotoList = new ArrayList<>();
    ArrayList<String> tarifeList = new ArrayList<>();
    private void islemFotoCek()
    {
        tarifeAdi = new ArrayList<>();
        tarifeId = new ArrayList<>();
        tarifeId.clear();
        tarifeAdi.clear();
        FotoList.clear();
        tarifeList.clear();
        RequestQueue requestQueue= Volley.newRequestQueue(PanelEkleIslemActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlPanelIslemFoto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String basarili = jsonObject.getJSONObject("Deger").getString("basarili");
                    String islemTarife = jsonObject.getJSONObject("Deger").getString("islemTarife");
                    String islemFoto = jsonObject.getJSONObject("Deger").getString("islemFoto");
                    if(basarili.equals("1"))
                    {

                        if(islemTarife.equals("var"))
                        {
                            String tarifeSayi = jsonObject.getJSONObject("Deger").getString("tarifeAdet");
                            for(int x=1 ; x <= Integer.valueOf(tarifeSayi) ; x++)
                            {
                                tarifeAdi.add(jsonObject.getJSONObject("Deger").getString("tarifeAdi"+x));
                                tarifeId.add(jsonObject.getJSONObject("Deger").getString("tarifeId"+x));
                            }
                            dataAdapterTarifeAdi =  new ArrayAdapter<String>(PanelEkleIslemActivity.this, android.R.layout.simple_spinner_item, tarifeAdi);
                            dataAdapterTarifeAdi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            panelEkleSp.setAdapter(dataAdapterTarifeAdi);
                            if(islemFoto.equals("var"))
                            {
                                String fotoSayi = jsonObject.getJSONObject("Deger").getString("fotoAdet");
                                for(int y=1 ; y <= Integer.valueOf(fotoSayi) ; y++)
                                {
                                    View v = panelEkleCons.getChildAt(y);
                                    if(v instanceof ImageView)
                                    {

                                        Picasso.get().load("http://"+sunucuAdres.sunucuIp+"/berberApi/"+jsonObject.getJSONObject("Deger").getString("fotoSrc"+y)).resize(175,130).into((ImageView) v);
                                    }
                                }
                            }
                        }
                        else {

                            if(islemFoto.equals("var"))
                            {

                                String fotoSayi = jsonObject.getJSONObject("Deger").getString("fotoAdet");

                                for(int y=1 ; y <= Integer.valueOf(fotoSayi) ; y++)
                                {
                                    View v = panelEkleCons.getChildAt(y);
                                    if(v instanceof ImageView)
                                    {

                                        Picasso.get().load("http://"+sunucuAdres.sunucuIp+"/berberApi/"+jsonObject.getJSONObject("Deger").getString("fotoSrc"+y)).resize(175,130).into((ImageView) v);
                                    }

                                }
                            }

                        }
                    }
                    else
                        {
                            Toast.makeText(PanelEkleIslemActivity.this, "Bir Hata Oluştu!", Toast.LENGTH_SHORT).show();
                        }


                } catch (Exception w) {
                    Toast.makeText(PanelEkleIslemActivity.this, w.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("islem","0");
                params.put("berberId",gelenBerberId);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {       //Fotoğraf İzin
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent1 =new Intent(Intent.ACTION_GET_CONTENT);
                    intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);             //Çoklu seçim
                    intent1.setType("image/*");                                           //Tür
                    startActivityForResult(intent1, 2);

                }
                return;
            }
        }
    }
    ArrayList<Bitmap> FotoBitmapList =new ArrayList<Bitmap>();
    ArrayList<Uri> FotoUriList =new ArrayList<Uri>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FotoUriList.clear();
        FotoBitmapList.clear();
        for (int i =0; i<panelEkleCons.getChildCount();i++)         //For döngüsünü layoutumda ki eleman sayısı döndürüyor ve içlerinden ImageView olanların source unu sıfırlıyor
        {
            View v = panelEkleCons.getChildAt(i+1);
            if(v instanceof ImageView)
            {
                ((ImageView) v).setImageBitmap(null);
            }
        }                                                           //SON**

        if (requestCode == 2 && resultCode == RESULT_OK)            //Fotoğraflar seçilip onaylanırsa
        {
            ClipData clipData = data.getClipData();                 //Seçilen fotoğrafları ClipData(Birden fazla medya olduğunda kullanıyoruz) in içine atıyor
            if (clipData != null)
            {

                for (int i = 0; i < clipData.getItemCount(); i++)
                {
                    FotoUriList.add(clipData.getItemAt(i).getUri());
                    try {
                        FotoBitmapList.add(MediaStore.Images.Media.getBitmap(this.getContentResolver(),FotoUriList.get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                for (int i =0; i<clipData.getItemCount();i++)
                {
                    View v = panelEkleCons.getChildAt(i+1);
                    if(v instanceof ImageView)
                    {
                        ((ImageView) v).setImageBitmap(FotoBitmapList.get(i));
                    }
                }

            }
        }                                                             //SON**
    }

    ArrayList<String> FotoSecilenler = new ArrayList<>();
    private void fotoStringCevir(){

        for (int i = 0; i< FotoBitmapList.size();i++)
        {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            FotoBitmapList.get(i).compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] fotoByte=byteArrayOutputStream.toByteArray();
            FotoSecilenler.add(Base64.encodeToString(fotoByte,Base64.DEFAULT));
        }

    }
}