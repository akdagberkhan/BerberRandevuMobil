package com.berkhanakdag.onlineberber;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.berkhanakdag.onlineberber.Adapter.SunucuAdres;
import com.berkhanakdag.onlineberber.FragmentActivty.FragmentProfil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanelActivity extends AppCompatActivity {



    private ArrayList<String> il;
    private ArrayList<String> ilce;
    private ArrayList<String> il_no;
    private ArrayList<String> ilce_no;

    private boolean berberDurum;
    private CheckBox panelAktif;
    private GridLayout panelBilgi;
    private String gelenId,gelenKullanici,gelenBerbermi,gelenFotoYol;
    int kont;
    private ImageView geriBtnPanel,profilfotoPanel,berberFotoPanel,islemEklePanel;
    private TextView kullaniciAdiPanel;
    private TimePicker acilisTpPanel,kapanisPanel;
    private Spinner ilPanel,ilcePanel;
    private ArrayAdapter<String> dataAdapterForIller;
    private ArrayAdapter<String> dataAdapterForIlceler;
    private EditText berberAdiPanel,periyotPanel,telefonPanel,detayPanel;
    private Button kaydetPanel;

    String urlPanelBerber = "";
    SunucuAdres sunucuAdres=new SunucuAdres();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        urlPanelBerber = "http://"+sunucuAdres.getSunucuIp()+"/berberApi/panelBerber.php";


        panelAktif = findViewById(R.id.checkBox);
        panelBilgi = findViewById(R.id.gridPanel);
        Intent intent = getIntent();
        gelenId=intent.getStringExtra("kId");
        gelenKullanici=intent.getStringExtra("kuladi");
        gelenBerbermi=intent.getStringExtra("berbermi");
        gelenFotoYol=intent.getStringExtra("fYol");

        kaydetPanel=findViewById(R.id.panelBtnKaydet);
        geriBtnPanel=findViewById(R.id.panelGeribtn);
        profilfotoPanel=findViewById(R.id.panelProfilFoto);
        kullaniciAdiPanel=findViewById(R.id.panelKullaniciAdi);
        islemEklePanel=findViewById(R.id.panelImgİslemEkle);
        berberAdiPanel=findViewById(R.id.panelEdtBerberAdi);
        periyotPanel=findViewById(R.id.panelEdtPeriyot);
        telefonPanel=findViewById(R.id.panelEdtTelefon);
        detayPanel=findViewById(R.id.panelEdtDetay);
        acilisTpPanel= findViewById(R.id.panelTpAcilis);
        kapanisPanel=findViewById(R.id.panelTpKapanis);
        acilisTpPanel.setIs24HourView(true);
        kapanisPanel.setIs24HourView(true);
        //İL İLÇE
        //Spinner
        ilPanel = findViewById(R.id.panelSpnrIl);
        ilcePanel=findViewById(R.id.panelSpnrIlce);
        //Spinner SON

        illerCek();
        berberCek();
        /*
        String b = acilisTpPanel.getHour()+"."+acilisTpPanel.getMinute();
        double a =  Double.valueOf(b);
        Toast.makeText(this, "Saat :"+a, Toast.LENGTH_SHORT).show();*/

        ilPanel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ilcelerCek(String.valueOf(position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //İL İLÇE SON
        Picasso.get().load(gelenFotoYol).resize(110,90).centerCrop().into(profilfotoPanel);
        kullaniciAdiPanel.setText(gelenKullanici);

        //GERİ BUTON
        geriBtnPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //GERİ BUTON SON

        //BERBER KONTROL
        if(gelenBerbermi.equals("0"))
        {
            kont=0;
            panelAktif.setChecked(false);
            for (int i =0 ; i <panelBilgi.getChildCount() ; i++){
                View eleman = panelBilgi.getChildAt(i);
                eleman.setEnabled(false);
            }
        }
        else{
            kont=1;
            panelAktif.setChecked(true);
            for (int i =0 ; i <panelBilgi.getChildCount() ; i++){
                View eleman = panelBilgi.getChildAt(i);
                eleman.setEnabled(true);
            }

        }
        //BERBER KONTROL SON

        //PANEL AKTİFLEŞTİRME
        panelAktif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(kont%2!=0)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(PanelActivity.this);
                    builder.setMessage("Panel'i devre dışı bıraktın.").setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                                berberDurum =false;
                                panelAktif.setChecked(false);
                                for (int i =0 ; i <panelBilgi.getChildCount() ; i++){
                                    View eleman = panelBilgi.getChildAt(i);
                                    eleman.setEnabled(false);
                                }
                                kont=0;
                            }

                    }).show();

                }
                else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(PanelActivity.this);
                    builder.setMessage("Panel'i aktif hale getirdin.").setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                                berberDurum = true;
                                panelAktif.setChecked(true);
                                for (int i = 0; i < panelBilgi.getChildCount(); i++) {
                                    View eleman = panelBilgi.getChildAt(i);
                                    eleman.setEnabled(true);
                                }
                                kont = 1;

                        }
                    }).show();

                }


            }
        });
        //PANEL AKTİFLEŞTİRME SON
        islemEklePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(PanelActivity.this,PanelEkleIslemActivity.class);
                intent1.putExtra("berbermi",gelenBerbermi);
                intent1.putExtra("kulAd",gelenKullanici);
                intent1.putExtra("berberId",berberId);
                intent1.putExtra("fotoYol",gelenFotoYol);
                startActivity(intent1);
            }
        });
        kaydetPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                berberKaydet();
            }
        });

    }
    private String berberId;
    boolean berberX=false;
    private void berberCek()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(PanelActivity.this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlPanelBerber, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String kontrol=jsonObject.getJSONObject("Deger").getString("basarili");
                    if(kontrol.equals("1"))
                    {
                        String durum =jsonObject.getJSONObject("Deger").getString("durum");
                        if(durum.equals("1"))
                        {
                            berberX=false;
                            berberId=jsonObject.getJSONObject("Deger").getString("berberId");

                            berberAdiPanel.setText(jsonObject.getJSONObject("Deger").getString("berberAd"));
                            telefonPanel.setText(jsonObject.getJSONObject("Deger").getString("berberTel"));
                            ilcePanel.setSelection(Integer.valueOf(jsonObject.getJSONObject("Deger").getString("berberIlce"))-1);
                            ilPanel.setSelection(Integer.valueOf(jsonObject.getJSONObject("Deger").getString("berberIl"))-1);
                            detayPanel.setText(jsonObject.getJSONObject("Deger").getString("berberDetay"));
                            String acilis= jsonObject.getJSONObject("Deger").getString("acilis");
                            String kapanis= jsonObject.getJSONObject("Deger").getString("kapanis");
                            String periyot= jsonObject.getJSONObject("Deger").getString("periyot");

                            String[] periyotzaman = periyot.split("\\.");   //Gelen String değerdeki "." karakterinden öncesi ve sonrasını diziye aktarıyor bölüyor
                            if (Integer.valueOf(periyotzaman[1]) < 10)
                            {
                                periyotzaman[1] = periyotzaman[1]+"0";
                                periyotPanel.setText(periyotzaman[1].toString());
                            }
                            else{ periyotPanel.setText(periyotzaman[1].toString());}

                            String[] aciliszaman = acilis.split("\\.");
                            String[] kapaniszaman = kapanis.split("\\.");
                            if(aciliszaman.length<2)
                            {

                                acilisTpPanel.setHour(Integer.valueOf(aciliszaman[0]));
                                acilisTpPanel.setMinute(00);

                            }
                            else
                            {
                                acilisTpPanel.setHour(Integer.valueOf(aciliszaman[0]));
                                if (Integer.valueOf(aciliszaman[1]) < 10)
                                {
                                    aciliszaman[1] = aciliszaman[1]+"0";
                                    acilisTpPanel.setMinute(Integer.valueOf(aciliszaman[1]));
                                }
                                else{acilisTpPanel.setMinute(Integer.valueOf(aciliszaman[1]));}
                            }

                            if(kapaniszaman.length<2)
                            {

                                kapanisPanel.setHour(Integer.valueOf(kapaniszaman[0]));
                                kapanisPanel.setMinute(00);

                            }
                            else
                            {
                                kapanisPanel.setHour(Integer.valueOf(kapaniszaman[0]));
                                if (Integer.valueOf(kapaniszaman[1]) < 10) {
                                    kapaniszaman[1] = kapaniszaman[1] + "0";
                                    kapanisPanel.setMinute(Integer.valueOf(kapaniszaman[1]));
                                } else {
                                    kapanisPanel.setMinute(Integer.valueOf(kapaniszaman[1]));
                                }
                            }
                        }
                        else
                            {
                                Toast.makeText(PanelActivity.this, "Berber Aktif Değil!", Toast.LENGTH_SHORT).show();
                            }


                    }
                    else {
                        berberX=true;
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

                params.put("islem","3");
                params.put("kulId",gelenId);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void berberKaydet()
    {


            String acilis = acilisTpPanel.getHour()+"."+acilisTpPanel.getMinute();
            String kapanis = kapanisPanel.getHour()+"."+kapanisPanel.getMinute();
            String berberAdi = berberAdiPanel.getText().toString();
            String periyot = "0."+periyotPanel.getText().toString();
            String telefon = telefonPanel.getText().toString();
            String ilceFk = ilce_no.get(ilcePanel.getSelectedItemPosition());
            String detay= detayPanel.getText().toString();

            if(berberAdi.isEmpty() || periyot.isEmpty() || telefon.isEmpty() || detay.isEmpty() )
            {
                Toast.makeText(this, "Alanlar boş bırakılamaz!", Toast.LENGTH_SHORT).show();
            }
            else
                {
                    RequestQueue requestQueue= Volley.newRequestQueue(PanelActivity.this);

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, urlPanelBerber, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try
                            {
                                JSONObject jsonObject=new JSONObject(response);
                                Toast.makeText(PanelActivity.this, ""+jsonObject.toString(), Toast.LENGTH_SHORT).show();
                                String basarili = jsonObject.getJSONObject("Deger").getString("basarili");

                                if (basarili.equals("1"))
                                {
                                    Toast.makeText(PanelActivity.this, "Başarılı, Fotoğraf ve İşlem Eklemek için İşlem Ekle Tuşuna Basınız.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(PanelActivity.this, "Başarısız, Lütfen tekrara deneyiniz.", Toast.LENGTH_SHORT).show();
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
                            if(berberDurum == true)
                            {
                                params.put("acilis",acilis);
                                params.put("kapanis",kapanis);
                                params.put("berberAdi",berberAdi);
                                params.put("periyot",periyot);
                                params.put("telefon",telefon);
                                params.put("ilceFk",ilceFk);
                                params.put("detay",detay);
                                params.put("islem","4");
                                params.put("kullaniciFk",gelenId);
                            }
                            else {
                                params.put("islem","0");
                                params.put("kullaniciFk",gelenKullanici);
                            }
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

    }



    private void illerCek()
    {

        il = new ArrayList<String>();
        il_no = new ArrayList<String>();
        RequestQueue requestQueue = Volley.newRequestQueue(PanelActivity.this);


        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlPanelBerber, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        il.add(jsonObject.getString("il_isim"));
                        il_no.add(jsonObject.getString("il_no"));

                    }
                    dataAdapterForIller = new ArrayAdapter<String>(PanelActivity.this, android.R.layout.simple_spinner_item, il);
                    //görünüm belirle
                    dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ilPanel.setAdapter(dataAdapterForIller);

                }
                catch (Exception w)
                {
                    Toast.makeText(PanelActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
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
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
    private void ilcelerCek(String plaka)
    {
        ilce = new ArrayList<String>();
        ilce_no = new ArrayList<String>();
        RequestQueue requestQueue = Volley.newRequestQueue(PanelActivity.this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlPanelBerber, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        ilce.add(jsonObject.getString("ilce_isim"));
                        ilce_no.add(jsonObject.getString("ilce_no"));

                    }
                    dataAdapterForIlceler= new ArrayAdapter<String>(PanelActivity.this, android.R.layout.simple_spinner_item,ilce);
                    dataAdapterForIlceler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ilcePanel.setAdapter(dataAdapterForIlceler);
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

                Map<String,String > params = new HashMap<>();
                params.put("islem","2");
                params.put("ilNo",plaka);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}