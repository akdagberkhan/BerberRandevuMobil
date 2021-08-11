package com.berkhanakdag.onlineberber.FragmentActivty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.berkhanakdag.onlineberber.Adapter.SunucuAdres;
import com.berkhanakdag.onlineberber.AnaSayfaActivity;
import com.berkhanakdag.onlineberber.GirisYapActivity;
import com.berkhanakdag.onlineberber.PanelActivity;
import com.berkhanakdag.onlineberber.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentProfil extends Fragment {


    private static int RESULT_OK = -1;
    private ImageView profilFotoProfil;
    private TextView adSoyadProfil;
    private String gelenKullanici;
    private EditText edtAdSoyadGuncel,edtMailGuncel,edtSifreGuncel,edtSifreGuncelTekrar,edtTelefonGuncel,edtKuladGuncel;
    private Button btnDuzenleProfil,btnGuncelleProfil,btnPanelProfil;
    private Context mcontext;
    private Bitmap bitmap;
    private SunucuAdres sunucuAdres=new SunucuAdres();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    Context guncelleContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profil,container,false);

        profilFotoProfil =view.findViewById(R.id.profilFotoProfilimg);
        adSoyadProfil = view.findViewById(R.id.profilAdsoyadtxt);
        edtAdSoyadGuncel =view.findViewById(R.id.edtGuncelAdSoyad);
        edtMailGuncel = view.findViewById(R.id.edtGuncelMail);
        edtSifreGuncel=view.findViewById(R.id.edtGuncelSifre);
        edtSifreGuncelTekrar = view.findViewById(R.id.edtGuncelSifreTekrar);
        edtTelefonGuncel = view.findViewById(R.id.edtGuncelTelefon);
        edtKuladGuncel=view.findViewById(R.id.edtGuncelKulAd);
        btnDuzenleProfil=view.findViewById(R.id.profilDüzenlebtn);
        btnGuncelleProfil=view.findViewById(R.id.profilGuncellebtn);
        btnPanelProfil = view.findViewById(R.id.profilPanelBtn);
        gelenKullanici = AnaSayfaActivity.getData().toString();
        bilgileriCek(container.getContext());
        guncelleContext = container.getContext();

        btnDuzenleProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentGaleri=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGaleri,2);
            }
        });

        btnGuncelleProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });

        btnPanelProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, PanelActivity.class);
                intent.putExtra("kuladi", gelenKullanici);
                intent.putExtra("kId", kId);
                intent.putExtra("fYol",fYol);
                intent.putExtra("berbermi",berbermi);
                startActivity(intent);
            }
        });

        return view;
    }

    String adSoyad,mail,sifre,telefon,profilFoto,fYol,kId,berbermi;
    private void bilgileriCek(Context context)
    {
        adSoyad ="";
        mail="";
        sifre="";
        telefon="";
        profilFoto="";
        fYol="";
        kId="";
        berbermi="";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String urll = "http://"+sunucuAdres.getSunucuIp()+"/berberApi/kullaniciBilgi.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);

                    adSoyad=jsonObject.getJSONObject("Deger").getString("adSoyad");
                    profilFoto=jsonObject.getJSONObject("Deger").getString("profilFoto");
                    mail=jsonObject.getJSONObject("Deger").getString("mail");
                    sifre=jsonObject.getJSONObject("Deger").getString("sifre");
                    telefon=jsonObject.getJSONObject("Deger").getString("telefon");
                    kId=jsonObject.getJSONObject("Deger").getString("kulId");
                    berbermi=jsonObject.getJSONObject("Deger").getString("berbermi");

                    edtAdSoyadGuncel.setText(adSoyad);
                    edtMailGuncel.setText(mail);
                    edtSifreGuncel.setText(sifre);
                    edtSifreGuncelTekrar.setText(sifre);
                    edtTelefonGuncel.setText(telefon);
                    edtKuladGuncel.setText(gelenKullanici);
                    adSoyadProfil.setText(" " + adSoyad);
                    fYol = "http://"+sunucuAdres.getSunucuIp()+"/berberApi/" + profilFoto;
                    Picasso.get().load(fYol).resize(150,150).centerCrop().into(profilFotoProfil);


                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("kuladi",gelenKullanici);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void guncelle()
    {
        String url = "http://"+sunucuAdres.getSunucuIp()+"/berberApi/kullaniciGuncelle.php";
        String GladSoyad =edtAdSoyadGuncel.getText().toString();
        String Glmail=edtMailGuncel.getText().toString();
        String GlkulAd=gelenKullanici;
        String Glsifre=edtSifreGuncel.getText().toString();
        String Glsifretekrar=edtSifreGuncelTekrar.getText().toString();
        String Gltelefon=edtTelefonGuncel.getText().toString();
        Bitmap GlprofilFoto=bitmap;
        if(GladSoyad.equals("") || Glmail.equals("") || GlkulAd.equals("") || Glsifre.equals("") || Gltelefon.equals("")){
            Toast.makeText(mcontext, "Bilgiler boş bırakılamaz", Toast.LENGTH_SHORT).show();
        }
        else {

            if(Glsifre.equals(Glsifretekrar))
            {
                RequestQueue requestQueue = Volley.newRequestQueue(mcontext);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String basarili = jsonObject.getJSONObject("Deger").getString("basarili");
                            if(basarili.equals("1"))
                            {
                                Toast.makeText(mcontext, "Güncelleme Başarılı", Toast.LENGTH_SHORT).show();

                                bilgileriCek(guncelleContext);

                            }
                            if(basarili.equals("4")){
                                Toast.makeText(mcontext, "Güncelleme Başarısız", Toast.LENGTH_SHORT).show();
                            }
                            if(basarili.equals("3")){
                                Toast.makeText(mcontext, "Fotoğraf Güncelleme Başarısız", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e) {
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
                        params.put("adSoyad",GladSoyad);
                        params.put("mail",Glmail);
                        params.put("kuladi",gelenKullanici);
                        params.put("sifre",Glsifre);
                        params.put("telefon",Gltelefon);
                        if(GlprofilFoto != null) {
                            params.put("foto", fotoStringCevir(GlprofilFoto));
                        }
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
            else{
                Toast.makeText(mcontext, "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
            }

        }

    }


    //FOTOĞRAF

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==2 && resultCode==RESULT_OK&&data!=null){

            Uri uri =data.getData();
            try {
                if(Build.VERSION.SDK_INT>=28){

                    ImageDecoder.Source source=ImageDecoder.createSource(mcontext.getContentResolver(),uri);
                    Bitmap bitmapp=ImageDecoder.decodeBitmap(source);
                    bitmap=MediaStore.Images.Media.getBitmap(mcontext.getContentResolver(),uri);
                    profilFotoProfil.setImageBitmap(bitmapp);
                }
                else{
                    bitmap=MediaStore.Images.Media.getBitmap(mcontext.getContentResolver(),uri);
                    profilFotoProfil.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            FragmentProfil.RESULT_OK = -1;
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

