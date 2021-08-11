package com.berkhanakdag.onlineberber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.HeaderViewListAdapter;
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
import com.berkhanakdag.onlineberber.FragmentActivty.FragmentAnasayfa;
import com.berkhanakdag.onlineberber.FragmentActivty.FragmentBerberler;
import com.berkhanakdag.onlineberber.FragmentActivty.FragmentProfil;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AnaSayfaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    public static String data;
    String UrlFotografYol,urlKullaniciBilgiler;
    SunucuAdres sunucuAdres=new SunucuAdres();
    View Baslikview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        UrlFotografYol = "http://"+sunucuAdres.sunucuIp+"/berberApi/";
        urlKullaniciBilgiler = "http://"+sunucuAdres.sunucuIp+"/berberApi/kullaniciBilgi.php";

        Intent intent = getIntent();

        data = intent.getStringExtra("kulAdi");
        // Başlangıçta anasayfamdaki frame layoutuma fragment anasayfayı commit ettim
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAna,new FragmentAnasayfa()).commit();
        // Toolbarı tanımladım ve Action bar oalrak ayarladım
        Toolbar toolbar = findViewById(R.id.toolbarAna);
        setSupportActionBar(toolbar);

        // Menü alanımı tanımladım , Header ımı tanımladım
        NavigationView navigationView=findViewById(R.id.nav_view_ana);
        navigationView.setNavigationItemSelectedListener(this);
        Baslikview = navigationView.getHeaderView(0);

        // Kayan menümü tanımladım ve toolbara bağladım
        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_ac,R.string.navigation_drawer_kapat);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        kullaniciGetir();


    }

    public static String getData() {
        return data;
    }

    public static void setData(String data) {
        AnaSayfaActivity.data = data;
    }

    //Seçilen menü elemanı
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_anasayfa:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAna,new FragmentAnasayfa()).commit();
                break;
            case R.id.nav_berberler:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAna,new FragmentBerberler()).commit();
                break;
            case R.id.nav_profil:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutAna, new FragmentProfil()).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // geri tuşuna basıldığında menü açıksa kapat
    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    //kullanıcı bilgilerini getir-------------------
    private String kulId;
    private String adSoyad;
    private String mail;
    private String kulAdi;
    private String sifre;
    private String telefon;
    private String berbermi;
    private String profilFoto;

    private void kullaniciGetir(){



        RequestQueue requestQueue= Volley.newRequestQueue(AnaSayfaActivity.this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlKullaniciBilgiler, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject=new JSONObject(response);

                    String basarili = jsonObject.getJSONObject("Deger").getString("basarili");
                    kulAdi =data;
                    kulId= jsonObject.getJSONObject("Deger").getString("kulId");
                    adSoyad= jsonObject.getJSONObject("Deger").getString("adSoyad");
                    mail= jsonObject.getJSONObject("Deger").getString("mail");
                    sifre= jsonObject.getJSONObject("Deger").getString("sifre");
                    telefon= jsonObject.getJSONObject("Deger").getString("telefon");
                    berbermi= jsonObject.getJSONObject("Deger").getString("berbermi");
                    profilFoto= jsonObject.getJSONObject("Deger").getString("profilFoto");

                    if(basarili.equals("1")){


                        TextView txtKuladBaslik = (TextView) Baslikview.findViewById(R.id.nav_baslik_text);
                        txtKuladBaslik.setText(" "+ kulAdi);

                        ImageView imgProfilFotoBaslik = (ImageView) Baslikview.findViewById(R.id.nav_baslik_image);
                        Picasso.get().load(UrlFotografYol+profilFoto).resize(100,80).into(imgProfilFotoBaslik);


                    }
                    if(basarili.equals("2")){
                        Toast.makeText(AnaSayfaActivity.this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                    }
                    if(basarili.equals("0")) {
                        Toast.makeText(AnaSayfaActivity.this, "Bir hata oluştu lütfen tekrar giriş yapın.", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AnaSayfaActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AnaSayfaActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("kuladi",data);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

}