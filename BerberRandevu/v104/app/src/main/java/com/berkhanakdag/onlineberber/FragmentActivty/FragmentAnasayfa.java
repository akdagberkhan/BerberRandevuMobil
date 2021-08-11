package com.berkhanakdag.onlineberber.FragmentActivty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.berkhanakdag.onlineberber.PanelActivity;
import com.berkhanakdag.onlineberber.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentAnasayfa extends Fragment {


    private Context mcontext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    private ArrayList<String> ArrayIl,ArrayIlce,ArrayIl_No,ArrayIlce_No;
    private ArrayAdapter<String> dataAdapterIller,dataAdapterIlceler;
    private Button araBtnAnasayfa;
    private Spinner spinnerIlAnasayfa,spinnerIlceAnasayfa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anasayfa,container,false);

        araBtnAnasayfa=view.findViewById(R.id.araBtnAnsayfa);
        spinnerIlAnasayfa = view.findViewById(R.id.anasayfaSpIl);
        spinnerIlceAnasayfa = view.findViewById(R.id.anasayfaSpIlce);
        illerCek();

        spinnerIlAnasayfa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ilcelerCek(String.valueOf(position+1));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        araBtnAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }


    String urlIllerIlceler;
    private void illerCek() {

        SunucuAdres sunucuAdres=new SunucuAdres();

        urlIllerIlceler = "http://"+sunucuAdres.getSunucuIp()+"/berberApi/panelBerber.php";
        ArrayIl = new ArrayList<String>();
        ArrayIl_No = new ArrayList<String>();

        RequestQueue requestQueue= Volley.newRequestQueue(mcontext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlIllerIlceler, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        ArrayIl.add(jsonObject.getString("il_isim"));
                        ArrayIl_No.add(jsonObject.getString("il_no"));

                    }
                    dataAdapterIller = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, ArrayIl);
                    dataAdapterIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerIlAnasayfa.setAdapter(dataAdapterIller);
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
                params.put("islem","1");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void ilcelerCek(String plaka)
    {
        SunucuAdres sunucuAdres=new SunucuAdres();

        urlIllerIlceler = "http://"+sunucuAdres.getSunucuIp()+"/berberApi/panelBerber.php";
        ArrayIlce = new ArrayList<String>();
        ArrayIlce_No = new ArrayList<String>();
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlIllerIlceler, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        ArrayIlce.add(jsonObject.getString("ilce_isim"));
                        ArrayIlce_No.add(jsonObject.getString("ilce_no"));


                    }
                    dataAdapterIlceler= new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item,ArrayIlce);
                    dataAdapterIlceler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerIlceAnasayfa.setAdapter(dataAdapterIlceler);
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
