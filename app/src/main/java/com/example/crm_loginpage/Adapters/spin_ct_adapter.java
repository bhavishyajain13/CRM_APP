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
        import com.example.crm_loginpage.class_structure_files.cus_contact_firebase;

        import java.util.List;

public class spin_ct_adapter extends ArrayAdapter<cus_contact_firebase> {
    private static final String TAG ="spin_ct_adapter";
    private final Context mcontext;
    int mresource;

    public spin_ct_adapter(@NonNull Context context, int resource, List<cus_contact_firebase> ans){
        super(context, resource, ans);
        mcontext = context;
        mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        String sno1 = getItem(pos).getId();

        LayoutInflater inflater= LayoutInflater.from(mcontext);
        convertView= inflater.inflate(mresource,parent,false);

        TextView sno= convertView.findViewById(R.id.slv1);


        sno.setText(sno1);

        return convertView;
    }


}
