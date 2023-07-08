package com.example.crm_loginpage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.address_firebase;

import java.util.List;

public class new_cus_add extends ArrayAdapter<address_firebase> {
    private static final String TAG ="new_cus_add";
    private final Context mcontext;
    int mresource;

    public new_cus_add(@NonNull Context context, int resource, List<address_firebase> ans){
        super(context, resource, ans);
        mcontext = context;
        mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        String adr = getItem(pos).getAddress();
        String pinc= getItem(pos).getPincode();
        String state= getItem(pos).getState();

        LayoutInflater inflater= LayoutInflater.from(mcontext);
        convertView= inflater.inflate(mresource,parent,false);

        TextView idl= convertView.findViewById(R.id.hsnlv1);
        TextView hsnl= convertView.findViewById(R.id.hsnlv2);
        TextView gstl= convertView.findViewById(R.id.hsnlv3);

        idl.setText(adr);
        hsnl.setText(state);
        gstl.setText(pinc);
        return convertView;
    }


}
