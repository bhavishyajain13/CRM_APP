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
import com.example.crm_loginpage.class_structure_files.opportunity_firebase;

import java.util.List;

public class op_adapter extends ArrayAdapter<opportunity_firebase> {
    private static final String TAG ="op_adapter";
    private final Context mcontext;
    int mresource;

    public op_adapter(@NonNull Context context, int resource, List<opportunity_firebase> ans){
        super(context, resource, ans);
        mcontext = context;
        mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        String sno1 = getItem(pos).getS_no();
        String op_id= getItem(pos).getId();
        String op_nm= getItem(pos).getOpp_name();
        String sale_stage= getItem(pos).getSale_stage();

        LayoutInflater inflater= LayoutInflater.from(mcontext);
        convertView= inflater.inflate(mresource,parent,false);

        TextView sno= convertView.findViewById(R.id.leadlv1);
        TextView lead_no= convertView.findViewById(R.id.leadlv2);
        TextView compa = convertView.findViewById(R.id.leadlv3);
        TextView status= convertView.findViewById(R.id.leadlv4);

        sno.setText(sno1);
        lead_no.setText(op_id);
        compa.setText(op_nm);
        status.setText(sale_stage);

        return convertView;
    }


}
