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
import com.example.crm_loginpage.class_structure_files.hsn_firebase;

import java.util.List;

public class hsn_adapter extends ArrayAdapter<hsn_firebase> {
    private static final String TAG = "hsn_adapter";
    private final Context mcontext;
    int mresource;

    public hsn_adapter(@NonNull Context context, int resource, List<hsn_firebase> ans) {
        super(context, resource, ans);
        mcontext = context;
        mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        int gst= getItem(pos).getGst();
        String hsn = getItem(pos).getHSN();
        String id= getItem(pos).getId();

        LayoutInflater inflater= LayoutInflater.from(mcontext);
        convertView= inflater.inflate(mresource,parent,false);

        TextView idl= convertView.findViewById(R.id.hsnlv1);
        TextView hsnl= convertView.findViewById(R.id.hsnlv2);
        TextView gstl= convertView.findViewById(R.id.hsnlv3);

        idl.setText(id);
        hsnl.setText(hsn);
        gstl.setText(gst+"%");
        return convertView;
    }

}
