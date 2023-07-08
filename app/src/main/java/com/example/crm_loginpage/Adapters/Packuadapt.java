package com.example.crm_loginpage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.crm_loginpage.unused.PackDB;
import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.PackDb_firebase;

import java.util.List;

public class Packuadapt extends ArrayAdapter<PackDB> {
    private static final String TAG = "Packuadapt";
    private final Context mcontext;
    int mresource;

    public Packuadapt(@NonNull Context context, int resource, List<PackDB> ans) {
        super(context, resource, ans);
        mcontext = context;
        mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        int id_t= getItem(pos).getId();
        int qty_t= getItem(pos).getQty();
        String desc_t = getItem(pos).getDescr();

        PackDb_firebase pdb= new PackDb_firebase(id_t+"",qty_t,desc_t);

        LayoutInflater inflater= LayoutInflater.from(mcontext);
        convertView= inflater.inflate(mresource,parent,false);

        TextView idl= convertView.findViewById(R.id.lv1);
        TextView qtyl= convertView.findViewById(R.id.lv2);
        TextView descl= convertView.findViewById(R.id.lv3);

        idl.setText(id_t+"");
        qtyl.setText(qty_t+"");
        descl.setText(desc_t);

        return convertView;
    }
}
