package com.example.crm_loginpage.activty_classes;



import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.crm_loginpage.Adapters.lead_adapter;
import com.example.crm_loginpage.Adapters.spin_ct_adapter;
import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.address_firebase;
import com.example.crm_loginpage.class_structure_files.lead_Firebase;
import com.example.crm_loginpage.class_structure_files.users_firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class leadfrag extends Fragment {

    androidx.appcompat.widget.AppCompatButton lead_search_button, open_new_lead;
    EditText lead_search_name;
    Spinner lead_own_search, lead_stat_search;
    ListView lv_lead;

    //prompt initializations

    Dialog dialog,dialog2;
    LinearLayout new_lead_dp, new_lead_form, add_dp, add_form;
    ImageView newleadimg, addimg;
    int hour, minute;
    EditText comp_nm, contact_per, contact_ph, mail_id, addr_lead, add_city, add_state, add_pin, dt, tm;
    Spinner lead_own, lead_stat;
    androidx.appcompat.widget.AppCompatButton cancel_lead;
    androidx.appcompat.widget.AppCompatButton add_lead;
    androidx.appcompat.widget.AppCompatButton delete_lead;
    androidx.appcompat.widget.AppCompatButton edit_lead;
    DatabaseReference ref, user_ref;

    public void initailization(View v) {
        ref = FirebaseDatabase.getInstance().getReference().child("lead_db");
        user_ref = FirebaseDatabase.getInstance().getReference().child("users_db");

        open_new_lead = v.findViewById(R.id.open_new_lead);
        lead_own_search = v.findViewById(R.id.lead_own_search);
        lead_stat_search = v.findViewById(R.id.lead_stat_search);
        lv_lead = v.findViewById(R.id.lv_lead);
        lead_search_name = v.findViewById(R.id.lead_search_name);
        lead_search_button = v.findViewById(R.id.lead_search_button);
        set_lown(lead_own_search);
        set_lstat(lead_stat_search);
        set_lead_listview();


        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.lead_prompt);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.new_prod);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        new_lead_dp = dialog.findViewById(R.id.new_lead_dp);
        new_lead_form = dialog.findViewById(R.id.new_lead_form);
        add_dp = dialog.findViewById(R.id.add_dp);
        add_form = dialog.findViewById(R.id.add_form);
        newleadimg = dialog.findViewById(R.id.newleadimg);
        addimg = dialog.findViewById(R.id.add_updown);
        newleadimg.setBackgroundResource(R.drawable.down_icon);
        addimg.setBackgroundResource(R.drawable.down_icon);


        comp_nm = dialog.findViewById(R.id.comp_nm);
        contact_per = dialog.findViewById(R.id.contact_per);
        contact_ph = dialog.findViewById(R.id.contact_ph);
        mail_id = dialog.findViewById(R.id.mail_id);
        lead_own = dialog.findViewById(R.id.lead_own);
        lead_stat = dialog.findViewById(R.id.lead_stat);

        addr_lead = dialog.findViewById(R.id.addr_lead);
        add_city = dialog.findViewById(R.id.add_city);
        add_state = dialog.findViewById(R.id.add_state);
        add_pin = dialog.findViewById(R.id.add_pin);
        lead_own = dialog.findViewById(R.id.lead_own);
        lead_stat = dialog.findViewById(R.id.lead_stat);

        tm = dialog.findViewById(R.id.follow_up_tm);
        dt = dialog.findViewById(R.id.follow_up_dt);

        cancel_lead = dialog.findViewById(R.id.cancel_lead);
        delete_lead = dialog.findViewById(R.id.delete_lead);
        add_lead = dialog.findViewById(R.id.add_lead);
        edit_lead = dialog.findViewById(R.id.edit_lead);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leadfrag, container, false);

        // will initalize all the required fields
        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.loading_page);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.show();

        initailization(v);

        lead_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        List<lead_Firebase> temp = new ArrayList<>();
                        int i = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (!snapshot.getKey().equalsIgnoreCase("id_count")) {

                                lead_Firebase gg = snapshot.getValue(lead_Firebase.class);
                                if (gg.getCmp_nm().contains(lead_search_name.getText().toString().toUpperCase())) {
                                    i++;
                                    gg.setSno(i + "");
                                    temp.add(gg);
                                }

                            }
                        }

                        if (getActivity() != null) {
                            lead_adapter arrayAdapter1 = new lead_adapter(getActivity(), R.layout.listview_lead, temp);
                            lv_lead.setAdapter(arrayAdapter1);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        lead_stat_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (lead_own_search.getSelectedItem() != null && lead_stat_search.getSelectedItem() != null) {
                    set_lead_listview(lead_own_search.getSelectedItem().toString(), lead_stat_search.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lead_own_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (lead_own_search.getSelectedItem() != null && lead_stat_search.getSelectedItem() != null) {
                    set_lead_listview( lead_own_search.getSelectedItem().toString(), lead_stat_search.getSelectedItem().toString() );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        open_new_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlead();
                unlock_inputs();
                set_lown(lead_own);
                set_lstat(lead_stat);
                openadd();

                delete_lead.setVisibility(View.GONE);
                add_lead.setVisibility(View.VISIBLE);
                edit_lead.setVisibility(View.INVISIBLE);

                comp_nm.setText("");
                contact_per.setText("");
                contact_ph.setText("");
                mail_id.setText("");
                addr_lead.setText("");
                add_city.setText("");
                add_state.setText("");
                add_pin.setText("");
                dt.setText("");
                tm.setText("");
                set_lown(lead_own);
                set_lstat(lead_stat);
                dialog.show();
                add_pin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString();
                        if (input.length() == 6) autofill(input);
                        if (input.length() > 6) {
                            String trimmedInput = input.substring(0, 6);
                            add_pin.setText(trimmedInput);
                            add_pin.setSelection(trimmedInput.length());
                        }
                    }
                });


                add_lead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validation_newinp()) {
                            String cmp_nm = comp_nm.getText().toString().toUpperCase();
                            String ct_pr = contact_per.getText().toString().toUpperCase();
                            String ct_ph = contact_ph.getText().toString().toUpperCase();
                            String mail = mail_id.getText().toString().toUpperCase();
                            String f_dt = dt.getText().toString().toUpperCase();
                            String f_tm = tm.getText().toString().toUpperCase();
                            String l_add = addr_lead.getText().toString().toUpperCase();
                            String city = add_city.getText().toString().toUpperCase();
                            String state = add_state.getText().toString().toUpperCase();
                            String pinc = add_pin.getText().toString().toUpperCase();
                            String l_owner = lead_own.getSelectedItem().toString();
                            String l_status = lead_stat.getSelectedItem().toString();


                            FirebaseDatabase.getInstance().getReference("lead_db").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        FirebaseDatabase.getInstance().getReference("lead_db").child("id_count").setValue(0);
                                    }
                                    DataSnapshot countSnapshot = snapshot.child("id_count");
                                    Integer countValue = countSnapshot.getValue(Integer.class);
                                    int count = (countValue != null) ? countValue : 0;
                                    count++;

                                    String new_id = "01LE" + String.format("%04d", count);

                                    address_firebase adt = new address_firebase(l_add, city, state, pinc);
                                    lead_Firebase newdt = new lead_Firebase(cmp_nm, ct_pr, ct_ph, mail, f_dt, f_tm, l_owner, l_status, new_id);
                                    FirebaseDatabase.getInstance().getReference("lead_db").child("id_count").setValue(count);
                                    FirebaseDatabase.getInstance().getReference("lead_db").child(new_id).setValue(newdt);

                                    FirebaseDatabase.getInstance().getReference("lead_db").child(new_id).child("address").child("ad_count").setValue(1);
                                    FirebaseDatabase.getInstance().getReference("lead_db").child(new_id).child("address").child("01AD001").setValue(adt);

                                    set_lown(lead_own_search);
                                    set_lstat(lead_stat_search);
                                    lead_search_name.setText("");

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

                tm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        poptimepicker();
                    }
                });
                dt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popdatepicker();
                    }
                });
                cancel_lead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }

                });
                new_lead_dp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (new_lead_form.getVisibility() == View.GONE) {
                            openlead();
                        } else {
                            closelead();
                        }
                    }
                });
                add_dp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (add_form.getVisibility() == View.GONE) {
                            openadd();
                        } else {
                            closeadd();
                        }
                    }
                });
            }
        });


        lv_lead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lead_Firebase selected_lead = (lead_Firebase) parent.getItemAtPosition(position);

                FirebaseDatabase.getInstance().getReference("lead_db").child(selected_lead.getId()).child("address").child("01AD001").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        address_firebase adrt= snapshot.getValue(address_firebase.class);
                        addr_lead.setText(adrt.getAddress());
                        add_city.setText(adrt.getCity());
                        add_state.setText(adrt.getState());
                        add_pin.setText(adrt.getPincode());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                add_lead.setVisibility(View.GONE);
                delete_lead.setVisibility(View.VISIBLE);
                edit_lead.setVisibility(View.VISIBLE);
                unlock_inputs();
                if(selected_lead.getL_status().equalsIgnoreCase("Customer")){

                    lock_inputs();
                    delete_lead.setVisibility(View.INVISIBLE);
                    edit_lead.setVisibility(View.INVISIBLE);
                }

                comp_nm.setText(selected_lead.getCmp_nm());
                contact_per.setText(selected_lead.getCt_pr());
                contact_ph.setText(selected_lead.getCt_ph());
                mail_id.setText(selected_lead.getMail());



                dt.setText(selected_lead.getF_dt());
                tm.setText(selected_lead.getF_tm());
                set_lown(lead_own, selected_lead.getL_owner());
                set_lstat(lead_stat, selected_lead.getL_status());
                dialog.show();


                add_pin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString();
                        if (input.length() == 6) autofill(input);
                        if (input.length() > 6) {
                            String trimmedInput = input.substring(0, 6);
                            add_pin.setText(trimmedInput);
                            add_pin.setSelection(trimmedInput.length());
                        }
                    }
                });


                edit_lead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validation_newinp()) {
                            String cmp_nm = comp_nm.getText().toString().toUpperCase();
                            String ct_pr = contact_per.getText().toString().toUpperCase();
                            String ct_ph = contact_ph.getText().toString().toUpperCase();
                            String mail = mail_id.getText().toString().toUpperCase();
                            String f_dt = dt.getText().toString().toUpperCase();
                            String f_tm = tm.getText().toString().toUpperCase();
                            String l_add = addr_lead.getText().toString().toUpperCase();
                            String city = add_city.getText().toString().toUpperCase();
                            String state = add_state.getText().toString().toUpperCase();
                            String pinc = add_pin.getText().toString().toUpperCase();
                            String l_owner = lead_own.getSelectedItem().toString();
                            String l_status = lead_stat.getSelectedItem().toString();

                            FirebaseDatabase.getInstance().getReference("lead_db").child(selected_lead.getId()).removeValue();
                            address_firebase adt = new address_firebase(l_add, city, state, pinc);
                            lead_Firebase newdt = new lead_Firebase(cmp_nm, ct_pr, ct_ph, mail, f_dt, f_tm, l_owner, l_status, selected_lead.getId());
                            FirebaseDatabase.getInstance().getReference("lead_db").child(selected_lead.getId()).setValue(newdt);
                            FirebaseDatabase.getInstance().getReference("lead_db").child(selected_lead.getId()).child("address").child("ad_count").setValue(1);
                            FirebaseDatabase.getInstance().getReference("lead_db").child(selected_lead.getId()).child("address").child("01AD001").setValue(adt);



                            dialog.cancel();
                        }
                    }
                });

                cancel_lead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                delete_lead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("lead_db").child(selected_lead.getId()).removeValue();
                        dialog.cancel();
                    }
                });

                tm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( !selected_lead.getL_status().equalsIgnoreCase("customer")) poptimepicker();
                    }
                });
                dt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         popdatepicker();
                    }
                });

            }
        });

        return v;
    }
    public void lock_inputs(){
        addr_lead.setEnabled(false);
        add_pin.setEnabled(false);
        add_city.setEnabled(false);
        add_state.setEnabled(false);
        comp_nm.setEnabled(false);
        contact_per.setEnabled(false);
        dt.setEnabled(false);
        tm.setEnabled(false);
        contact_ph.setEnabled(false);
        mail_id.setEnabled(false);
        lead_own.setEnabled(false);
        lead_stat.setEnabled(false);
    }
    public void unlock_inputs(){
        addr_lead.setEnabled(true);
        add_pin.setEnabled(true);
        add_city.setEnabled(true);
        add_state.setEnabled(true);
        comp_nm.setEnabled(true);
        contact_per.setEnabled(true);
        dt.setEnabled(true);
        tm.setEnabled(true);
        contact_ph.setEnabled(true);
        mail_id.setEnabled(true);
        lead_own.setEnabled(true);
        lead_stat.setEnabled(true);
    }
    public void set_lstat(Spinner sp) {
        List<String> lt = new ArrayList<>();
        lt.add("Prospect");
        lt.add("Open");
        lt.add("Working");
        lt.add("Meeting Set");
        lt.add("Select");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lt);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        int position = adapter.getPosition("Select");
        sp.setSelection(position);
    }

    public void set_lown(Spinner sp) {

        user_ref.addValueEventListener(new ValueEventListener() {
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
                sp.setAdapter(adapter);
                int position = adapter.getPosition("Select");
                sp.setSelection(position);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void set_lstat(Spinner sp, String val) {
        List<String> lt = new ArrayList<>();
        lt.add("Prospect");
        lt.add("Open");
        lt.add("Working");
        lt.add("Meeting Set");
        lt.add("Select");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lt);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        int position = adapter.getPosition(val);
        sp.setSelection(position);
    }

    public void set_lown(Spinner sp, String val) {
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
                sp.setAdapter(adapter);
                int position = adapter.getPosition(val);
                sp.setSelection(position);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void set_lead_listview() {
        FirebaseDatabase.getInstance().getReference().child("lead_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<lead_Firebase> temp = new ArrayList<>();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("id_count")) {
                        lead_Firebase gg = snapshot.getValue(lead_Firebase.class);
                        i++;
                        gg.setSno(i + "");
                        temp.add(gg);
                    }
                }

                if (getActivity() != null) {
                    lead_adapter arrayAdapter1 = new lead_adapter(getActivity(), R.layout.listview_lead, temp);
                    lv_lead.setAdapter(arrayAdapter1);
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

    public void set_lead_listview(String a, String b) {
        boolean a_ck = false;
        boolean b_ck = false;

        if (a.equalsIgnoreCase("SELECT")) a_ck = true;
        if (b.equalsIgnoreCase("SELECT")) b_ck = true;

        boolean finalA_ck = a_ck;
        boolean finalB_ck = b_ck;
        FirebaseDatabase.getInstance().getReference().child("lead_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<lead_Firebase> temp = new ArrayList<>();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("id_count")) {
                        lead_Firebase gg = snapshot.getValue(lead_Firebase.class);
                        if ((finalA_ck || gg.getL_owner().equalsIgnoreCase(a)) && (finalB_ck || gg.getL_status().equalsIgnoreCase(b))) {
                            i++;
                            gg.setSno(i + "");
                            temp.add(gg);
                        }
                    }
                }

                if (getActivity() != null) {
                    lead_adapter arrayAdapter1 = new lead_adapter(getActivity(), R.layout.listview_lead, temp);
                    lv_lead.setAdapter(arrayAdapter1);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void autofill(String pinc) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create a URL object with the HTTPS URL
                    String pin_url = "https://api.postalpincode.in/pincode/" + pinc;
                    URL url = new URL(pin_url);

                    // Open a connection to the URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set the request method
                    connection.setRequestMethod("GET");

                    // Create a BufferedReader to read the content
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder content = new StringBuilder();

                    // Read the content line by line
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    // Close the reader
                    reader.close();

                    JSONArray jsonArray = new JSONArray(content.toString());
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray postOfficeArray = jsonObject.getJSONArray("PostOffice");
                        if (postOfficeArray.length() > 0) {
                            JSONObject firstPostOffice = postOfficeArray.getJSONObject(0);
                            String get_state = firstPostOffice.getString("State");
                            String get_district = firstPostOffice.getString("District");

                            // Update UI with the retrieved value
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    add_city.setText(get_district);
                                    add_state.setText(get_state);
//                                    Toast.makeText(getActivity(), get_state + get_district, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public boolean validation_newinp() {
        String cmp_nm = comp_nm.getText().toString().toUpperCase();
        String ct_pr = contact_per.getText().toString().toUpperCase();
        String ct_ph = contact_ph.getText().toString().toUpperCase();
        String mail = mail_id.getText().toString().toUpperCase();
        String f_dt = dt.getText().toString().toUpperCase();
        String f_tm = tm.getText().toString().toUpperCase();
        String l_add = addr_lead.getText().toString().toUpperCase();
        String city = add_city.getText().toString().toUpperCase();
        String state = add_state.getText().toString().toUpperCase();
        String pinc = add_pin.getText().toString().toUpperCase();
        String l_owner = lead_own.getSelectedItem().toString();
        String l_status = lead_stat.getSelectedItem().toString();

        if (cmp_nm.equalsIgnoreCase("") ||
                ct_pr.equalsIgnoreCase("") ||
                ct_ph.equalsIgnoreCase("") ||
                mail.equalsIgnoreCase("") ||
                f_dt.equalsIgnoreCase("") ||
                f_tm.equalsIgnoreCase("") ||
                l_add.equalsIgnoreCase("") ||
                city.equalsIgnoreCase("") ||
                state.equalsIgnoreCase("") ||
                pinc.equalsIgnoreCase("") ||
                l_owner.equalsIgnoreCase("SELECT") ||
                l_status.equalsIgnoreCase("SELECT")
        ) {
            Toast.makeText(getActivity(), "Enter all Fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ct_ph.matches("^(\\+91[\\-\\s]?)?0?(91)?[789]\\d{9}$")) {
            Toast.makeText(getActivity(), "Phone Number Wrong", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!mail.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Toast.makeText(getActivity(), "Mail ID Wrong", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pinc.length() != 6) {
            Toast.makeText(getActivity(), "Invalid Pincode", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    public void openlead() {
        new_lead_dp.setBackgroundColor(R.drawable.gradcav2);
        newleadimg.setBackgroundResource(R.drawable.up_icon);
        new_lead_form.setVisibility(View.VISIBLE);
    }

    public void closelead() {
        newleadimg.setBackgroundResource(R.drawable.down_icon);
        new_lead_dp.setBackgroundColor(Color.TRANSPARENT);
        new_lead_form.setVisibility(View.GONE);
    }

    public void openadd() {
        add_dp.setBackgroundColor(R.drawable.gradcav2);
        addimg.setBackgroundResource(R.drawable.up_icon);
        add_form.setVisibility(View.VISIBLE);
    }

    public void closeadd() {
        addimg.setBackgroundResource(R.drawable.down_icon);
        add_dp.setBackgroundColor(Color.TRANSPARENT);
        add_form.setVisibility(View.GONE);
    }

    public void poptimepicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                hour = hourOfDay;
                minute = minutes;
                tm.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("SELECT TIME");
        timePickerDialog.show();
    }

    public void popdatepicker() {

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker da, int year, int month, int dayOfMonth) {
                String dates = dayOfMonth + "/" + month + "/" + year;
                dt.setText(dates);
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

}

