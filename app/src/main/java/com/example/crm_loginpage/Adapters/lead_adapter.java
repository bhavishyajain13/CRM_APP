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
import com.example.crm_loginpage.class_structure_files.lead_Firebase;

import java.util.List;

public class lead_adapter extends ArrayAdapter<lead_Firebase> {
    private static final String TAG ="lead_adapter";
    private final Context mcontext;
    int mresource;

    public lead_adapter(@NonNull Context context, int resource, List<lead_Firebase> ans){
        super(context, resource, ans);
        mcontext = context;
        mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        String sno1 = getItem(pos).getSno();
        String leadid= getItem(pos).getId();
        String cmp= getItem(pos).getCmp_nm();
        String stat= getItem(pos).getL_status();

        LayoutInflater inflater= LayoutInflater.from(mcontext);
        convertView= inflater.inflate(mresource,parent,false);

        TextView sno= convertView.findViewById(R.id.leadlv1);
        TextView lead_no= convertView.findViewById(R.id.leadlv2);
        TextView compa = convertView.findViewById(R.id.leadlv3);
        TextView status= convertView.findViewById(R.id.leadlv4);

        sno.setText(sno1);
        lead_no.setText(leadid);
        compa.setText(cmp);
        status.setText(stat);

        return convertView;
    }


}
