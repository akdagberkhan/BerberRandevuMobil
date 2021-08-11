package com.berkhanakdag.onlineberber.FragmentActivty;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.berkhanakdag.onlineberber.Adapter.BerberlerAdapter;
import com.berkhanakdag.onlineberber.R;

import java.util.ArrayList;

public class FragmentBerberler extends Fragment {

    BerberlerAdapter berberlerAdapter;
    private ArrayList<String> berberId;
    private ArrayList<String> berberAdi;
    private ArrayList<String> berberTelefon;
    private ArrayList<String> berberAdres;
    private ArrayList<String> berberFotoIlk;
    private ArrayList<String> berberKullaniciAdi;
    private Context mcontext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berberler,container,false);

        berberId=new ArrayList<String>();
        berberAdi=new ArrayList<String>();
        berberTelefon=new ArrayList<String>();
        berberAdres=new ArrayList<String>();
        berberFotoIlk=new ArrayList<String>();
        berberKullaniciAdi=new ArrayList<String>();

        berberId.add("1");
        berberAdi.add("Deneme Ad");
        berberTelefon.add("054682347525");
        berberAdres.add("bunlar mahallesi deneme sokak no:14");
        berberFotoIlk.add("http://192.168.43.37/berberApi/berberFoto/xxx0.jpg");
        berberKullaniciAdi.add("berkhan");

        RecyclerView recyclerView=view.findViewById(R.id.rViewBerberler);
        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        berberlerAdapter=new BerberlerAdapter(berberId,berberAdi,berberTelefon,berberAdres,berberFotoIlk,berberKullaniciAdi);
        recyclerView.setAdapter(berberlerAdapter);
        return view;
    }
}
