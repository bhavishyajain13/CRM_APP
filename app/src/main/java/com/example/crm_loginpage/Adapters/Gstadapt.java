package com.example.crm_loginpage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.crm_loginpage.class_structure_files.GstDb_firebase;
import com.example.crm_loginpage.R;

import java.util.List;

public class Gstadapt extends ArrayAdapter<GstDb_firebase> {
    private static final String TAG = "Gstadapt";
    private final Context mcontext;
    int mresource,i;

    public Gstadapt(@NonNull Context context, int resource, List<GstDb_firebase> ans) {
        super(context, resource, ans);
        i=0;
        mcontext = context;
        mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        int gst= getItem(pos).getGstper();
        String id = getItem(pos).getId();

        GstDb_firebase pdb= new GstDb_firebase(gst,id);

        LayoutInflater inflater= LayoutInflater.from(mcontext);
        convertView= inflater.inflate(mresource,parent,false);

        TextView qtyl= convertView.findViewById(R.id.glv1);
        TextView descl= convertView.findViewById(R.id.glv2);

        qtyl.setText(id+"");
        descl.setText(gst+"%");
        return convertView;
    }

}
