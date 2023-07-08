package com.example.crm_loginpage.activty_classes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crm_loginpage.Adapters.CustomAutoCompleteAdapter;
import com.example.crm_loginpage.Adapters.op_adapter;
import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.customer_firebase;
import com.example.crm_loginpage.class_structure_files.cus_contact_firebase;
import com.example.crm_loginpage.class_structure_files.opportunity_firebase;
import com.example.crm_loginpage.class_structure_files.users_firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class opportunity extends Fragment {

    androidx.appcompat.widget.AppCompatButton open_new_opp,opp_search_button;
    ListView lv_opp;
    EditText opp_search_cus,opp_search_nm;
    Spinner opp_own_search;

    ImageView cancel_filters;

    String temp_id;

    Dialog dialog,dialog2;
    Spinner opp_new_own, opp_new_stage,opp_ct_name;
    EditText opp_follow_up_dt, opp_expected_close_dt;
    AutoCompleteTextView opp_new_cus_name;
    TextView  opp_new_email,opp_new_ph;
    EditText  opp_new_nm,  opp_new_comment;
    androidx.appcompat.widget.AppCompatButton cancel_opp, add_opp, edit_opp, delete_opp;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Main page initialization
        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.loading_page);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.show();

        View v = inflater.inflate(R.layout.fragment_opportunity, container, false);
        open_new_opp = v.findViewById(R.id.open_new_lead);
        opp_own_search= v.findViewById(R.id.opp_own_search);
        opp_search_cus = v.findViewById(R.id.opp_search_cus);
        opp_search_button= v.findViewById(R.id.opp_search_button);
        opp_search_nm = v.findViewById(R.id.opp_search_nm);
        cancel_filters = v.findViewById(R.id.cancel_filters);
        opp_owners_search("Select");
        lv_opp= v.findViewById(R.id.lv_opp);
        lv_with_filter();
//        set_up_lv();


        //Dialog Initalization
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.opportunity_prompt);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.new_prod);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        opp_expected_close_dt = dialog.findViewById(R.id.opp_expected_close_dt);
        opp_follow_up_dt = dialog.findViewById(R.id.opp_follow_up_dt);
        cancel_opp = dialog.findViewById(R.id.cancel_opp);
        add_opp = dialog.findViewById(R.id.add_opp);
        edit_opp = dialog.findViewById(R.id.edit_opp);
        delete_opp = dialog.findViewById(R.id.delete_opp);
        opp_new_own = dialog.findViewById(R.id.opp_new_own);
        opp_new_stage = dialog.findViewById(R.id.opp_new_stage);
        opp_new_cus_name = dialog.findViewById(R.id.opp_new_cus_name);
        opp_new_nm = dialog.findViewById(R.id.opp_new_nm);
        opp_ct_name = dialog.findViewById(R.id.opp_ct_name);
        opp_new_ph = dialog.findViewById(R.id.opp_new_ph);
        opp_new_email = dialog.findViewById(R.id.opp_new_email);
        opp_new_comment = dialog.findViewById(R.id.opp_new_comment);

        cancel_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opp_search_nm.setText("");
                opp_search_cus.setText("");
                opp_owners_search("Select");
            }
        });

        opp_own_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lv_with_filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        opp_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_with_filter();
            }
        });

        open_new_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cus_contact_firebase ct=new cus_contact_firebase("Select","Select","Select","Select","Select","Select");
                get_cus_details(ct,"");
                opp_new_cus_name.setEnabled(true);
                set_up_cus_autocomplete();

                set_up_opp_owners("Select");
                set_up_stages("Select");
                opp_new_cus_name.setText("");
                opp_new_nm.setText("");
                opp_new_ph.setText("");
                opp_new_email.setText("");
                opp_new_comment.setText("");
                opp_expected_close_dt.setText("");
                opp_follow_up_dt.setText("");


                dialog.show();
                edit_opp.setVisibility(View.INVISIBLE);
                delete_opp.setVisibility(View.GONE);
                add_opp.setVisibility(View.VISIBLE);
                opp_follow_up_dt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popdatepicker(opp_follow_up_dt);
                    }
                });
                opp_expected_close_dt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popdatepicker(opp_expected_close_dt);
                    }
                });

                cancel_opp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                opp_new_cus_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {
                        String sel_val = (String) parent.getItemAtPosition(position);
                        String ids = sel_val.substring(sel_val.length()-9, sel_val.length());
                        temp_id = ids;
                        get_cus_details(ct,ids);
                    }
                });

                opp_ct_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cus_contact_firebase sel= (cus_contact_firebase) parent.getItemAtPosition(position);
                        opp_new_ph.setText(sel.getPh());
                        opp_new_email.setText(sel.getEmail());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                opp_new_cus_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        temp_id = null;
                    }
                });


                add_opp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( temp_id!=null && validation_inp() ) {
                            String op_nm = opp_new_cus_name.getText().toString().toUpperCase();
                            String cus_nm = opp_new_nm.getText().toString().toUpperCase();
                            String ct_nm = opp_ct_name.getSelectedItem().toString();
                            String ph = opp_new_ph.getText().toString().toUpperCase();
                            String email = opp_new_email.getText().toString().toUpperCase();
                            String comment = opp_new_comment.getText().toString().toUpperCase();
                            String opp_own = opp_new_own.getSelectedItem().toString();
                            String opp_st = opp_new_stage.getSelectedItem().toString();
                            String fdt = opp_follow_up_dt.getText().toString();
                            String exdt = opp_expected_close_dt.getText().toString();


                            FirebaseDatabase.getInstance().getReference("opportunity_db").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        FirebaseDatabase.getInstance().getReference("opportunity_db").child("op_count").setValue(0);
                                    }
                                    DataSnapshot countSnapshot = snapshot.child("op_count");
                                    Integer countValue = countSnapshot.getValue(Integer.class);
                                    int count = (countValue != null) ? countValue : 0;
                                    count++;
                                    String new_id = "01OP" + String.format("%04d", count);
                                    String cus_id = op_nm.substring(op_nm.length()-9, op_nm.length() );
                                    int finalCount = count;
                                    FirebaseDatabase.getInstance().getReference("customers_db").child(cus_id).child("cus_name").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String cusnm = snapshot.getValue().toString();
                                            opportunity_firebase opdt = new opportunity_firebase(new_id,  cus_nm,cus_id, ct_nm, ph, email, fdt, exdt, opp_own, opp_st, comment,cusnm);
                                            FirebaseDatabase.getInstance().getReference("opportunity_db").child(new_id).setValue(opdt);
                                            FirebaseDatabase.getInstance().getReference("opportunity_db").child("op_count").setValue(finalCount);
                                            Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {

                            Toast.makeText(getActivity(), "Enter all Fields", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


        lv_opp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                opp_new_cus_name.setEnabled(false);
                cus_contact_firebase ct=new cus_contact_firebase("Select","Select","Select","Select","Select","Select");
                opportunity_firebase sel_op = (opportunity_firebase) parent.getItemAtPosition(position);
                add_opp.setVisibility(View.GONE);
                edit_opp.setVisibility(View.VISIBLE);
                delete_opp.setVisibility(View.VISIBLE);
                set_up_cus_autocomplete();

                get_cus_details(sel_op.getCt_name(), sel_op.getCus_id());

                opp_new_cus_name.setText(sel_op.getCus_id());
                opp_new_nm.setText(sel_op.getOpp_name());
                opp_new_ph.setText(sel_op.getPh());
                opp_new_email.setText(sel_op.getEmail());
                opp_new_comment.setText(sel_op.getComment());
                opp_expected_close_dt.setText(sel_op.getExp_dt());
                opp_follow_up_dt.setText(sel_op.getF_dt());
                set_up_stages(sel_op.getSale_stage());
                set_up_opp_owners(sel_op.getOpp_own());



                dialog.show();

                opp_ct_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cus_contact_firebase sel= (cus_contact_firebase) parent.getItemAtPosition(position);
                        opp_new_ph.setText(sel.getPh());
                        opp_new_email.setText(sel.getEmail());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                edit_opp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validation_inp() ) {

                            String op_nm = opp_new_nm.getText().toString().toUpperCase();
                            String cus_nm = opp_new_cus_name.getText().toString().toUpperCase();
                            String ct_nm = opp_ct_name.getSelectedItem().toString();
                            String ph = opp_new_ph.getText().toString().toUpperCase();
                            String email = opp_new_email.getText().toString().toUpperCase();
                            String comment = opp_new_comment.getText().toString().toUpperCase();
                            String opp_own = opp_new_own.getSelectedItem().toString();
                            String opp_st = opp_new_stage.getSelectedItem().toString();
                            String fdt = opp_follow_up_dt.getText().toString();
                            String exdt = opp_expected_close_dt.getText().toString();


                            FirebaseDatabase.getInstance().getReference("opportunity_db").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    opportunity_firebase opdt = new opportunity_firebase(sel_op.getId(), op_nm, cus_nm, ct_nm, ph, email, fdt, exdt, opp_own, opp_st, comment, sel_op.getCus_name());
                                    FirebaseDatabase.getInstance().getReference("opportunity_db").child(sel_op.getId()).setValue(opdt);
                                    Toast.makeText(getActivity(), "Edited", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {

                            Toast.makeText(getActivity(), "Enter all Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                delete_opp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("opportunity_db").child(sel_op.getId()).removeValue();
                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                cancel_opp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

        return v;
    }

    //put if conditions such that it will always take all suitable conditions
    public void lv_with_filter(){
        FirebaseDatabase.getInstance().getReference().child("opportunity_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String owner=opp_own_search.getSelectedItem().toString();
                String srch_cus_nm= opp_search_cus.getText().toString().toUpperCase();
                String opp_nm= opp_search_nm.getText().toString().toUpperCase();
                List<opportunity_firebase> temp = new ArrayList<>();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("op_count")) {
                            opportunity_firebase gg = snapshot.getValue(opportunity_firebase.class);
                        if( (owner.equalsIgnoreCase("select") || gg.getOpp_own().equalsIgnoreCase(owner)) &&
                         ( opp_nm.equalsIgnoreCase("") || gg.getOpp_name().contains(opp_nm) ) &&
                                (srch_cus_nm.equalsIgnoreCase("") || gg.getPh().contains(srch_cus_nm) || gg.getCus_name().contains(srch_cus_nm)  )
                        ) {
                            i++;
                            gg.setS_no(i + "");
                            temp.add(gg);
                        }
                    }
                }

                if (getActivity() != null) {
                    op_adapter arrayAdapter1 = new op_adapter(getActivity(), R.layout.listview_lead, temp);
                    lv_opp.setAdapter(arrayAdapter1);
                }
                dialog2.cancel();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                dialog2.cancel();
            }
        });
    }

    public void get_cus_details(cus_contact_firebase x,String cus_id){
        FirebaseDatabase.getInstance().getReference().child("customers_db").child(cus_id).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<cus_contact_firebase> tempct_nm = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("ct_count")) {
                        cus_contact_firebase gg = snapshot.getValue(cus_contact_firebase.class);
                        tempct_nm.add(gg);
                    }
                }

                cus_contact_firebase ct=new cus_contact_firebase("Select","Select","Select","Select","Select","Select");
                tempct_nm.add(ct);
                ArrayAdapter<cus_contact_firebase> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tempct_nm);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                opp_ct_name.setAdapter(adapter);
                int position = adapter.getPosition(x);
                opp_ct_name.setSelection(position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void get_cus_details(String x,String cus_id){
        FirebaseDatabase.getInstance().getReference().child("customers_db").child(cus_id).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<cus_contact_firebase> tempct_nm = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("ct_count")) {
                        cus_contact_firebase gg = snapshot.getValue(cus_contact_firebase.class);
                        tempct_nm.add(gg);
                    }
                }

                cus_contact_firebase ct=new cus_contact_firebase("Select","Select","Select","Select","Select","Select");
                tempct_nm.add(ct);
                ArrayAdapter<cus_contact_firebase> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tempct_nm);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                opp_ct_name.setAdapter(adapter);
//                Toast.makeText(getActivity(), opp_ct_name.getCount()+"-", Toast.LENGTH_SHORT).show();
                for(int i=0;i<opp_ct_name.getCount();i++){
                    cus_contact_firebase temp = (cus_contact_firebase) opp_ct_name.getItemAtPosition(i);
                    if(temp.getName().equalsIgnoreCase(x) ){
                        opp_ct_name.setSelection(i);
                        opp_new_ph.setText(temp.getPh());
                        opp_new_email.setText(temp.getEmail());
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void set_up_cus_autocomplete() {

        FirebaseDatabase.getInstance().getReference().child("customers_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> temp = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("cus_count")) {
                        customer_firebase gg = snapshot.getValue(customer_firebase.class);
                        temp.add(gg.getLeadno() + "|" + gg.getCus_name()+ "|" + gg.getId());
                    }
                }

                if (getActivity() != null) {
                    CustomAutoCompleteAdapter adaptertemp = new CustomAutoCompleteAdapter(getContext(), temp);
                    opp_new_cus_name.setAdapter(adaptertemp);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validation_inp(){

        if ( opp_ct_name.getSelectedItem().toString().equalsIgnoreCase("Select") || opp_new_nm.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Enter All Values", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( opp_expected_close_dt.getText().toString().equalsIgnoreCase("") || opp_follow_up_dt.getText().toString().equalsIgnoreCase("") ){
            Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( opp_new_own.getSelectedItem().toString().equalsIgnoreCase("select") ){
            Toast.makeText(getActivity(), "Select Owner", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( opp_new_stage.getSelectedItem().toString().equalsIgnoreCase("select") ){
            Toast.makeText(getActivity(), "Select Sales Stage", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    public void set_up_opp_owners(String x) {
        FirebaseDatabase.getInstance().getReference().child("users_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> temp = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("id_count")) {
                        users_firebase gg = snapshot.getValue(users_firebase.class);
                        temp.add(gg.getName());
                    }
                }

                temp.add("Select");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, temp);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                opp_new_own.setAdapter(adapter);
                int position = adapter.getPosition(x);
                opp_new_own.setSelection(position);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void opp_owners_search(String x) {
        FirebaseDatabase.getInstance().getReference().child("users_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> temp = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("id_count")) {
                        users_firebase gg = snapshot.getValue(users_firebase.class);
                        temp.add(gg.getName());
                    }
                }

                temp.add("Select");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, temp);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                opp_own_search.setAdapter(adapter);
                int position = adapter.getPosition(x);
                opp_own_search.setSelection(position);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void set_up_stages(String x) {
        List<String> temp = new ArrayList<>();
        temp.add("Prospecting ");
        temp.add("Qualification");
        temp.add("Needs Analysis");
        temp.add("Value Proposition");
        temp.add("Decision Makers");
        temp.add("Perception Analysis");
        temp.add("Proposal/Price Quote");
        temp.add("Negotiation/Review");
        temp.add("Lead qualification");
        temp.add("Demo or Meeting");
        temp.add("Proposal");
        temp.add("Negotiation and Commitment");
        temp.add("Opportunity Won");
        temp.add("Post-Purchase");
        temp.add("Opportunity Lost");
        temp.add("Select");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, temp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opp_new_stage.setAdapter(adapter);
        int position = adapter.getPosition(x);
        opp_new_stage.setSelection(position);
    }

    public void popdatepicker(EditText et) {

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker da, int year, int month, int dayOfMonth) {
                String dates = dayOfMonth + "/" + month + "/" + year;
                et.setText(dates);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        DatePickerDialog datepicker = new DatePickerDialog(getContext(), onDateSetListener, year, month, day);
        datepicker.show();

    }

//    public void set_up_lv(){
//        FirebaseDatabase.getInstance().getReference().child("opportunity_db").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                List<opportunity_firebase> temp = new ArrayList<>();
//                int i = 0;
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (!snapshot.getKey().equalsIgnoreCase("op_count")) {
//                        opportunity_firebase gg = snapshot.getValue(opportunity_firebase.class);
//                        i++;
//                        gg.setS_no(i + "");
//                        temp.add(gg);
//                    }
//                }
//
//                if (getActivity() != null) {
//                    op_adapter arrayAdapter1 = new op_adapter(getActivity(), R.layout.listview_lead, temp);
//                    lv_opp.setAdapter(arrayAdapter1);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}