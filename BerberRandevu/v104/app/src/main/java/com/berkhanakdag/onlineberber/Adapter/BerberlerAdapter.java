package com.berkhanakdag.onlineberber.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.berkhanakdag.onlineberber.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BerberlerAdapter extends RecyclerView.Adapter<BerberlerAdapter.BerberHolder>
{
    private ArrayList<String> berberId;
    private ArrayList<String> berberAdi;
    private ArrayList<String> berberTelefon;
    private ArrayList<String> berberAdres;
    private ArrayList<String> berberFotoIlk;
    private ArrayList<String> berberKullaniciAdi;

    public BerberlerAdapter(ArrayList<String> berberId, ArrayList<String> berberAdi, ArrayList<String> berberTelefon, ArrayList<String> berberAdres, ArrayList<String> berberFotoIlk, ArrayList<String> berberKullaniciAdi) {
        this.berberId = berberId;
        this.berberAdi = berberAdi;
        this.berberTelefon = berberTelefon;
        this.berberAdres = berberAdres;
        this.berberFotoIlk = berberFotoIlk;
        this.berberKullaniciAdi = berberKullaniciAdi;
    }

    @NonNull
    @Override
    public BerberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.berberler_recyclerow,parent,false);

        return new BerberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BerberHolder holder, int position) {

        holder.berberAd.setText(berberAdi.get(position));
        holder.berberKullaniciAd.setText(berberKullaniciAdi.get(position));
        holder.berberTelefon.setText(berberTelefon.get(position));
        holder.berberAdres.setText(berberAdres.get(position));
        Picasso.get().load(berberFotoIlk.get(position)).resize(100,100).into(holder.berberFoto);
    }

    @Override
    public int getItemCount() {
        return berberId.size();
    }

    class BerberHolder extends RecyclerView.ViewHolder
    {
        TextView berberAd,berberKullaniciAd,berberTelefon,berberAdres;
        ImageView berberFoto;
        Button randevuBtn;
        public BerberHolder(@NonNull View itemView)
        {
            super(itemView);
            berberAd=itemView.findViewById(R.id.berberAdiTxt);
            berberKullaniciAd=itemView.findViewById(R.id.berberKullaniciAdiTxt);
            berberTelefon=itemView.findViewById(R.id.berberTelefonTxt);
            berberAdres=itemView.findViewById(R.id.berberAdresTxt);
            berberFoto=itemView.findViewById(R.id.berberImg);
            randevuBtn=itemView.findViewById(R.id.berberRandevuBtn);

        }
    }
}
