package com.example.crm_loginpage.activty_classes;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.crm_loginpage.Adapters.Gstadapt;
import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.GstDb_firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class gstfrag extends Fragment {
    EditText gstper;
    private  Dialog dialog,dialog2;
    ListView lv_gst;
    androidx.appcompat.widget.AppCompatButton addnewgst;
    androidx.appcompat.widget.AppCompatButton searchnewgst;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gst, container, false);
        // Inflate the layout for this fragment
        lv_gst= v.findViewById(R.id.lv_gst);
        addnewgst= v.findViewById(R.id.addnewgst);
        searchnewgst= v.findViewById(R.id.searchnewgst);
        gstper = v.findViewById(R.id.gstper);

        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.loading_page);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.show();

        DatabaseReference databaseReference;
        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
        databaseReference= rootnode.getReference("gstdb");
        setviewgst();
        addnewgst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int ngst= Integer.parseInt( gstper.getText().toString() );

                    String fb_uid = FirebaseAuth.getInstance().getUid();
                    GstDb_firebase gstDb_firebase = new GstDb_firebase( ngst, fb_uid   );
                    databaseReference.child(ngst+"").setValue(gstDb_firebase);
                    Toast.makeText(getActivity(),"New Gst Added", Toast.LENGTH_SHORT).show();
                    gstper.setText("");
                }
                catch (Exception e){
                    Toast.makeText(getActivity(),"Enter Value First", Toast.LENGTH_SHORT).show();
                }
                setviewgst();
            }
        });

        searchnewgst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int val = Integer.parseInt( gstper.getText().toString() );

//                    FIREBASE DATABASE
                    FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            List<GstDb_firebase> temp1 = new ArrayList<>();
                            int i=1;
                            for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                                GstDb_firebase gdb1= snapshot.getValue(GstDb_firebase.class);
                                if(gdb1.getGstper()==val) {
                                    gdb1.setId( i+"");
                                    i++;
                                    temp1.add( gdb1 );
                                }
                            }

                            Gstadapt arrayAdapter1 = new Gstadapt(getActivity(), R.layout.listviewgst, temp1);
                            lv_gst.setAdapter(arrayAdapter1);

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(),"major error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (Exception e){
                    setviewgst();
                    Toast.makeText(getActivity(),"SEARCH FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lv_gst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // WILL CALL THE POP UP AND THAT WILL DEL OR EDIT DATA
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                try{
                    dialog = new Dialog(getActivity());
                    GstDb_firebase gst_cut = (GstDb_firebase) parent.getItemAtPosition(i);
                    dialog.setContentView(R.layout.packu_prompt);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.addpacku_prompt);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                    EditText newqty= dialog.findViewById(R.id.new_qty);
                    newqty.setText( Integer.toString( gst_cut.getGstper() ) );
                    androidx.appcompat.widget.AppCompatButton edit = dialog.findViewById(R.id.edit_button);
                    androidx.appcompat.widget.AppCompatButton delete = dialog.findViewById(R.id.delete_button);
                    androidx.appcompat.widget.AppCompatButton cancel = dialog.findViewById(R.id.cancel_button);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"Cancelled", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"Deleted", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("gstdb").child(""+ gst_cut.getGstper() ).removeValue();
                            dialog.cancel();
                            setviewgst();

                        }
                    });

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                HashMap hashMap = new HashMap();
                                hashMap.put( "gstper", Integer.parseInt(newqty.getText().toString())  );
//                                hashMap.put("id", FirebaseAuth.getInstance().getUid().toString() );
                                String fb_uid = FirebaseAuth.getInstance().getUid();
//                                DataSnapshot snapshot= databaseReference.child()
                                GstDb_firebase gx = new GstDb_firebase(  Integer.parseInt(newqty.getText().toString()) , fb_uid   );
                                String x=newqty.getText().toString();

//                                 databaseReference.child(  x  ).setValue(gx);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference rootRef = database.getReference("gstdb");
                                DatabaseReference xRef = rootRef.child(""+gst_cut.getGstper());
                                DatabaseReference newChildRef = rootRef.child(x);

                                // Retrieve data from child 'x' and move it to the new child
                                xRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try{
                                            Object data = dataSnapshot.getValue();
                                            if (data instanceof Map) {
                                                Map<String, Object> dataMap = (Map<String, Object>) data;
                                                if (dataMap.containsKey("gstper")) {
                                                    // Modify the 'id' value here
                                                    dataMap.put("gstper", Integer.parseInt(x) );
                                                }
                                            }

                                            // Set the data to the new child
                                            newChildRef.setValue(data, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError != null) {
                                                        System.out.println("Data could not be moved: " + databaseError.getMessage());
                                                    } else {
                                                        System.out.println("Data moved successfully.");
                                                        Toast.makeText(getActivity(), "Edited", Toast.LENGTH_SHORT).show();
                                                        xRef.removeValue();
                                                        // Remove the data from child 'x' if desired
//                                                        xRef.removeValue();
                                                    }
                                                }
                                            });
                                        }
                                        catch (Exception e) {
                                            Toast.makeText(getActivity(), "Error Editing", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("Error retrieving data: " + databaseError.getMessage());
                                    }
                                });


                                dialog.cancel();
                                setviewgst();
                            }
                            catch (Exception e){
                                Toast.makeText(getActivity(),"Enter Qty First", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
                catch(Exception e){
                    Toast.makeText(getActivity(),"ERROR", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return v;

    }
    private void setviewgst(){
        FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<GstDb_firebase> temp1 = new ArrayList<>();
                int i=1;
                for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                    GstDb_firebase gg= snapshot.getValue(GstDb_firebase.class);
                    gg.setId(i+"");
                    i++;
                    temp1.add( gg );
                }

                if (getActivity()!=null){
                    Gstadapt arrayAdapter1 = new Gstadapt(getActivity(), R.layout.listviewgst, temp1);
                    lv_gst.setAdapter(arrayAdapter1);
                }
                dialog2.cancel();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Database Error", Toast.LENGTH_SHORT).show();
                dialog2.cancel();
            }
        });
    }

}