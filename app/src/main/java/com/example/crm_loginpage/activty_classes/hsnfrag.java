package com.example.crm_loginpage.activty_classes;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.crm_loginpage.Adapters.hsn_adapter;
import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.GstDb_firebase;
import com.example.crm_loginpage.class_structure_files.hsn_firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class hsnfrag extends Fragment {

    Spinner spinner;
    Dialog dialog2;
    EditText hsn_code;
    ListView lv_hsn;
    ProgressBar loadingProgressBar;
    androidx.appcompat.widget.AppCompatButton addnew,search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_hsnfrag, container, false);
        addnew= v.findViewById(R.id.addnew_hsn);
        hsn_code= v.findViewById(R.id.hsncode);
        lv_hsn= v.findViewById(R.id.lv_hsn);
        search= v.findViewById(R.id.search_hsn);
        spinner = v.findViewById(R.id.spinner_hsn);
        set_list_view();
        set_hsn_options();


        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.loading_page);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.show();

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sel= spinner.getSelectedItem().toString();
                    String newhsn = hsn_code.getText().toString().toUpperCase();
                    try {
                        boolean[] fin = new boolean[1];
                        if (!sel.matches("All") && !newhsn.matches("")) {
                            FirebaseDatabase.getInstance().getReference().child("gstdb").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.hasChild("HSN")) {
                                            for (DataSnapshot snapshot1 : snapshot.child("HSN").getChildren()) {
                                                Object s = snapshot1.getValue();
                                                if ( s.toString().matches(newhsn) ) {
                                                    fin[0] = true;
                                                    Toast.makeText(getActivity(), "HSN Already Exists", Toast.LENGTH_SHORT).show();
                                                    break;
                                                }
                                            }
                                        }
                                        if(fin[0])  break;
                                    }
                                    if(!fin[0]){
                                        FirebaseDatabase.getInstance().getReference("gstdb").child(sel).child("HSN").child(newhsn).setValue(newhsn);
                                        Toast.makeText(getActivity(), "HSN Added", Toast.LENGTH_SHORT).show();
                                        hsn_code.setText("");
                                        set_list_view();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else {
                            Toast.makeText(getActivity(), "Cannot Add HSN", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e){
                        Toast.makeText(getActivity(),"Error1", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getActivity(),"Error2", Toast.LENGTH_SHORT).show();
                }
                hsn_code.setText("");
            }
        });

        //Will autmatically display only values based on the gst number selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().matches("All")) set_list_view();
                else{
                    FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<hsn_firebase> temp = new ArrayList<>();
                            int i=0;
                            for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                                if( snapshot.getKey().matches( spinner.getSelectedItem().toString() )  ){
                                    GstDb_firebase gg= snapshot.getValue(GstDb_firebase.class);
                                    if(snapshot.hasChild("HSN") ){
                                        for(DataSnapshot snapshot1: snapshot.child("HSN").getChildren() ){
                                            Object s=snapshot1.getValue();
                                            i++;
                                            temp.add ( new hsn_firebase( (i)+"" , s.toString(), gg.getGstper() ) );
                                        }
                                    }
                                }
                            }
                            if (getActivity()!=null){
                                hsn_adapter arrayAdapter1 = new hsn_adapter(getActivity(), R.layout.listviewhsn, temp);
                                lv_hsn.setAdapter(arrayAdapter1);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(),"Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sel= spinner.getSelectedItem().toString();
                if( hsn_code.getText().toString().matches("") ) set_list_view();
                else{
//                    so basically now we use the same lines of code, even if all is selected and some value is to be
//                    searcehd it will search. because we have added a finalDropdown_condition condition whihc will mkae the if
//                    condition true  by defaul
                    boolean dropdown_condition= sel.matches("All");
                    boolean finalDropdown_condition = dropdown_condition;
                    FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<hsn_firebase> temp = new ArrayList<>();
                            int i=0;
                            for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                                if( finalDropdown_condition || snapshot.getKey().matches( spinner.getSelectedItem().toString() )  ){
                                    GstDb_firebase gg= snapshot.getValue(GstDb_firebase.class);
                                    if(snapshot.hasChild("HSN") ){
                                        for(DataSnapshot snapshot1: snapshot.child("HSN").getChildren() ){
                                            Object s=snapshot1.getValue();
                                            if(s.toString().matches( hsn_code.getText().toString().toUpperCase()) ){
                                                i++;
                                                temp.add ( new hsn_firebase( (i)+"" , s.toString(), gg.getGstper() ) );
                                            }
                                        }
                                    }
                                }
                            }
                            if (getActivity()!=null){
                                hsn_adapter arrayAdapter1 = new hsn_adapter(getActivity(), R.layout.listviewhsn, temp);
                                lv_hsn.setAdapter(arrayAdapter1);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(),"Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        lv_hsn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // WILL CALL THE POP UP AND THAT WILL DEL OR EDIT DATA
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                try{
                    Dialog dialog;
                    dialog = new Dialog(getActivity());
                    hsn_firebase hsnold = (hsn_firebase) parent.getItemAtPosition(i);
                    dialog.setContentView(R.layout.packu_prompt);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.addpacku_prompt);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                    EditText newval= dialog.findViewById(R.id.new_qty);
                    newval.setText(  hsnold.getHSN()  );
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
                            DatabaseReference delref=FirebaseDatabase.getInstance().getReference("gstdb").child( hsnold.getGst() +"").child("HSN").child(hsnold.getHSN());
                            delref.removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(),"Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(),"Could not delete", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            dialog.cancel();
                            set_list_view();
                        }
                    });

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                boolean fin[]= {false};
                                DatabaseReference delref=FirebaseDatabase.getInstance().getReference("gstdb").child( hsnold.getGst() +"").child("HSN").child(hsnold.getHSN());
                                FirebaseDatabase.getInstance().getReference().child("gstdb").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            if (snapshot.hasChild("HSN")) {
                                                for (DataSnapshot snapshot1 : snapshot.child("HSN").getChildren()) {
                                                    Object s = snapshot1.getValue();
                                                    if ( s.toString().matches(newval.getText().toString()) ) {
                                                        fin[0] = true;
                                                        Toast.makeText(getActivity(), "HSN Already Exists", Toast.LENGTH_SHORT).show();
                                                        break;
                                                    }
                                                }
                                            }
                                            if(fin[0])  break;
                                        }
                                        if(!fin[0]){
                                            delref.removeValue();
                                            String newhsncode= newval.getText().toString();
                                            FirebaseDatabase.getInstance().getReference("gstdb").child( hsnold.getGst() +"").child("HSN").child(  newhsncode ).setValue(newhsncode);
                                            Toast.makeText(getActivity(), "HSN Edited", Toast.LENGTH_SHORT).show();
                                            hsn_code.setText("");
                                            set_list_view();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

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


    public void set_list_view(){
        FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<hsn_firebase> temp = new ArrayList<>();
                int i=0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
//                    GstDb_firebase gg= snapshot.getValue(GstDb_firebase.class);
//                    String cur_gst = snapshot.
                    GstDb_firebase gg= snapshot.getValue(GstDb_firebase.class);
                    if(snapshot.hasChild("HSN") ){
                        for(DataSnapshot snapshot1: snapshot.child("HSN").getChildren() ){
                            Object s=snapshot1.getValue();
                            i++;
                            temp.add ( new hsn_firebase( (i)+"" , s.toString(), gg.getGstper() ) );
                        }
                    }
                }

                if (getActivity()!=null){
                    hsn_adapter arrayAdapter1 = new hsn_adapter(getActivity(), R.layout.listviewhsn, temp);
                    lv_hsn.setAdapter(arrayAdapter1);
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

    public void set_hsn_options(){
        FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> optionlist = new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                    GstDb_firebase gg= snapshot.getValue(GstDb_firebase.class);
                    optionlist.add( gg.getGstper()+"" );
                }
                optionlist.add("All");

                if (getActivity()!=null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, optionlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    int position = adapter.getPosition("All");
                    spinner.setSelection(position);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Database Error", Toast.LENGTH_SHORT).show();
            }

        });
    }
}