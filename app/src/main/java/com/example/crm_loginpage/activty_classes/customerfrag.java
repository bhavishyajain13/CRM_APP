package com.example.crm_loginpage.activty_classes;

import android.app.Dialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crm_loginpage.Adapters.CustomAutoCompleteAdapter;
import com.example.crm_loginpage.Adapters.cuslv_adapter;
import com.example.crm_loginpage.Adapters.new_cus_add;
import com.example.crm_loginpage.Adapters.new_cus_ct;
import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.address_firebase;
import com.example.crm_loginpage.class_structure_files.cus_contact_firebase;
import com.example.crm_loginpage.class_structure_files.customer_firebase;
import com.example.crm_loginpage.class_structure_files.lead_Firebase;
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
import java.util.LinkedList;
import java.util.List;


public class customerfrag extends Fragment {
    androidx.appcompat.widget.AppCompatButton new_customer_button,srch_button_cus;
    LinearLayout customer_searchbox, customer_subhead;
    ScrollView customer_scrollview;
    EditText customer_name;
    ListView customer_lv;
    LinearLayout cus_lv_header;
    EditText cus_srch_val;
    Dialog dialog2;

    String temp_ld;

    //new customer fields
    Spinner cus_type;
    RelativeLayout customer_ct_form, customer_add_form, customer_details_form;

    AutoCompleteTextView lead_choose;
    LinearLayout lv1, lv2, lv3;
    ImageView lv1_img, lv2_img, lv3_img;
    EditText ct_nm, email_customer, dept_customer, desg_customer, customer_ph;
    ListView lv_cus_add, lv_cus_ct;
    EditText addr_customer, pinc_customer, city_customer, state_customer;
    androidx.appcompat.widget.AppCompatButton new_addr_button, new_ct_button;
    androidx.appcompat.widget.AppCompatButton edit_customer, add_customer, del_customer, cancel_customer;

    public void initailizations(View v) {
        customer_scrollview = v.findViewById(R.id.customer_scrollview);
        new_customer_button = v.findViewById(R.id.new_customer_button);
        customer_searchbox = v.findViewById(R.id.customer_searchbox);
        customer_subhead = v.findViewById(R.id.customer_subhead);
        customer_lv = v.findViewById(R.id.customer_lv);
        srch_button_cus = v.findViewById(R.id.srch_button_cus);
        cus_lv_header = v.findViewById(R.id.cus_lv_header);
        set_customer_lv("");

        customer_scrollview.setVisibility(View.GONE);
        customer_lv.setVisibility(View.VISIBLE);
        cus_lv_header.setVisibility(View.VISIBLE);


        // For filling the data
        cus_type = v.findViewById(R.id.cus_type);
        set_up_cus_type();
        lead_choose = v.findViewById(R.id.lead_choose);
        set_up_lead_choose();


        lv1 = v.findViewById(R.id.customer_details);
        lv1_img = v.findViewById(R.id.customer_details_img);
        lv2 = v.findViewById(R.id.customer_add_details);
        lv2_img = v.findViewById(R.id.customer_add_details_img);
        lv3 = v.findViewById(R.id.customer_ct_details);
        lv3_img = v.findViewById(R.id.customer_ct_details_img);
        customer_ct_form = v.findViewById(R.id.customer_ct_form);
        customer_add_form = v.findViewById(R.id.customer_add_form);
        customer_details_form = v.findViewById(R.id.customer_details_form);


        customer_name = v.findViewById(R.id.customer_name);
        addr_customer = v.findViewById(R.id.addr_customer);
        pinc_customer = v.findViewById(R.id.pinc_customer);
        city_customer = v.findViewById(R.id.city_customer);
        state_customer = v.findViewById(R.id.state_customer);
        new_addr_button = v.findViewById(R.id.new_addr_button);

        lv_cus_add = v.findViewById(R.id.lv_cus_add);
        lv_cus_ct = v.findViewById(R.id.lv_cus_ct);
        ct_nm = v.findViewById(R.id.ct_nm);
        customer_ph = v.findViewById(R.id.customer_ph);
        email_customer = v.findViewById(R.id.email_customer);
        dept_customer = v.findViewById(R.id.dept_customer);
        desg_customer = v.findViewById(R.id.desg_customer);
        new_ct_button = v.findViewById(R.id.new_ct_button);


        edit_customer = v.findViewById(R.id.edit_customer);
        add_customer = v.findViewById(R.id.add_customer);
        del_customer = v.findViewById(R.id.del_customer);
        cancel_customer = v.findViewById(R.id.cancel_customer);
        cus_srch_val = v.findViewById(R.id.cus_srch_val);
    }

    public void showform() {

        customer_scrollview.setVisibility(View.VISIBLE);
        customer_searchbox.setVisibility(View.GONE);
        customer_subhead.setVisibility(View.GONE);
        customer_lv.setVisibility(View.GONE);
        cus_lv_header.setVisibility(View.GONE);
    }

    public void closeform() {
        customer_scrollview.setVisibility(View.GONE);
        cus_lv_header.setVisibility(View.VISIBLE);
        customer_lv.setVisibility(View.VISIBLE);
        customer_searchbox.setVisibility(View.VISIBLE);
        customer_subhead.setVisibility(View.VISIBLE);
    }

    public void clear_fields() {
        addr_customer.setText("");
        pinc_customer.setText("");
        city_customer.setText("");
        state_customer.setText("");
        ct_nm.setText("");
        customer_ph.setText("");
        email_customer.setText("");
        dept_customer.setText("");
        desg_customer.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customerfrag, container, false);

        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.loading_page);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.show();


        initailizations(v);

        srch_button_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_customer_lv(cus_srch_val.getText().toString().trim().toUpperCase());
            }
        });


        new_customer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_fields();
                lead_choose.setEnabled(true);
                lead_choose.setText("");
                customer_name.setText("");
                set_up_cus_type();
                edit_customer.setVisibility(View.INVISIBLE);
                add_customer.setVisibility(View.VISIBLE);
                del_customer.setVisibility(View.GONE);
                showform();
                open_v(customer_details_form, lv1_img, lv1);
                open_v(customer_add_form, lv2_img, lv2);
                open_v(customer_ct_form, lv3_img, lv3);
                List<address_firebase> lt = new ArrayList<>();
                List<cus_contact_firebase> lt2 = new ArrayList<>();
                set_ct_list(lt2);
                set_add_list(lt);

                customer_ph.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString();
                        if (input.length() > 10) {
                            String trimmedInput = input.substring(0, 10);
                            customer_ph.setText(trimmedInput);
                            customer_ph.setSelection(trimmedInput.length());
                        }
                    }
                });


                lv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customer_details_form.getVisibility() == View.GONE) {
                            open_v(customer_details_form, lv1_img, lv1);
                        } else {
                            close_v(customer_details_form, lv1_img, lv1);
                        }
                    }
                });

                lv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customer_details_form.getVisibility() == View.GONE) {
                            open_v(customer_add_form, lv2_img, lv2);
                        } else {
                            close_v(customer_add_form, lv2_img, lv2);
                        }
                    }
                });

                lv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customer_details_form.getVisibility() == View.GONE) {
                            open_v(customer_ct_form, lv3_img, lv3);
                        } else {
                            close_v(customer_ct_form, lv3_img, lv3);
                        }
                    }
                });


                pinc_customer.addTextChangedListener(new TextWatcher() {
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
                            pinc_customer.setText(trimmedInput);
                            pinc_customer.setSelection(trimmedInput.length());
                        }
                    }
                });

                lead_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String sel_val = (String) parent.getItemAtPosition(position);
                        int x = sel_val.indexOf("|");
                        String ids = sel_val.substring(0, x);
                        temp_ld = ids;
                        fill_initial_details(ids);
                    }
                });

                lead_choose.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        temp_ld = null;
//                        Toast.makeText(getActivity(),"Invalid Lead",Toast.LENGTH_SHORT).show();
                    }
                });

                new_addr_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = addr_customer.getText().toString();
                        String pincode = pinc_customer.getText().toString();
                        String city = city_customer.getText().toString();
                        String state = state_customer.getText().toString();

                        if (validation_addr(address, pincode, city, state)) {
                            String id = "01AD" + String.format("%02d", lt.size() + 1);
                            address_firebase newadr = new address_firebase(id, address, city, state, pincode);
                            lt.add(newadr);
                            set_add_list(lt);

                            addr_customer.setText("");
                            pinc_customer.setText("");
                            city_customer.setText("");
                            state_customer.setText("");
                        }

                    }
                });

                new_ct_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = ct_nm.getText().toString();
                        String phone = customer_ph.getText().toString();
                        String email = email_customer.getText().toString();
                        String dept = dept_customer.getText().toString();
                        String desg = desg_customer.getText().toString();
                        if (validation_ct(name, phone, email, dept, desg)) {
                            String id = "01CT" + String.format("%02d", lt2.size() + 1);
                            cus_contact_firebase cur_cus = new cus_contact_firebase(id, name, phone, email, dept, desg);
                            lt2.add(cur_cus);
                            set_ct_list(lt2);

                            ct_nm.setText("");
                            customer_ph.setText("");
                            email_customer.setText("");
                            dept_customer.setText("");
                            desg_customer.setText("");

                        }

                    }
                });

                add_customer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (temp_ld == null)
                            Toast.makeText(getActivity(), "Invalid Lead ", Toast.LENGTH_SHORT).show();
                        else {
                            String leadno = lead_choose.getText().toString().substring(0, 8);
                            String cust = cus_type.getSelectedItem().toString();
                            String cusnm = customer_name.getText().toString();
                            if (temp_ld != null && lt.size() > 0 && lt2.size() > 0 && !leadno.equalsIgnoreCase("") && !cust.equalsIgnoreCase("select") && !cusnm.equalsIgnoreCase("")) {
                                Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference("customers_db").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            FirebaseDatabase.getInstance().getReference("customers_db").child("cus_count").setValue(0);
                                        }
                                        DataSnapshot countSnapshot = snapshot.child("cus_count");
                                        Integer countValue = countSnapshot.getValue(Integer.class);
                                        int count = (countValue != null) ? countValue : 0;
                                        count++;

                                        String new_id = "01CUS" + String.format("%04d", count);

                                        customer_firebase cusdt = new customer_firebase(new_id, leadno, cusnm, cust);

                                        FirebaseDatabase.getInstance().getReference("customers_db").child(new_id).setValue(cusdt);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customers_db").child(new_id);

                                        for (address_firebase adr : lt) {
                                            ref.child("address").child(adr.getId()).setValue(adr);
                                        }
                                        ref.child("address").child("ad_count").setValue(lt.size());

                                        for (cus_contact_firebase ct : lt2) {
                                            ref.child("contacts").child(ct.getId()).setValue(ct);
                                        }
                                        ref.child("contacts").child("ct_count").setValue(lt2.size());


                                        FirebaseDatabase.getInstance().getReference("customers_db").child("cus_count").setValue(count);
                                        FirebaseDatabase.getInstance().getReference("lead_db").child(leadno).child("l_status").setValue("Customer");
                                        set_customer_lv("");
                                        customer_scrollview.setVisibility(View.GONE);
                                        customer_searchbox.setVisibility(View.VISIBLE);
                                        customer_subhead.setVisibility(View.VISIBLE);
                                        customer_lv.setVisibility(View.VISIBLE);
                                        cus_lv_header.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), "Check Values", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                cancel_customer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeform();
                    }
                });

            }
        });

        customer_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<address_firebase> ltad = new ArrayList<>();
                ArrayList<cus_contact_firebase> ltct = new ArrayList<>();
                showform();

                clear_fields();
                customer_ph.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString();
                        if (input.length() > 10) {
                            String trimmedInput = input.substring(0, 10);
                            customer_ph.setText(trimmedInput);
                            customer_ph.setSelection(trimmedInput.length());
                        }
                    }
                });

                lead_choose.setEnabled(false);
                open_v(customer_details_form, lv1_img, lv1);
                open_v(customer_add_form, lv2_img, lv2);
                open_v(customer_ct_form, lv3_img, lv3);
                edit_customer.setVisibility(View.VISIBLE);
                add_customer.setVisibility(View.GONE);
                del_customer.setVisibility(View.VISIBLE);
                customer_firebase selected_customer = (customer_firebase) parent.getItemAtPosition(position);
                final int[] ctad = {0};
                final int[] ctcus = {0};
                customer_name.setText(selected_customer.getCus_name());
                set_up_cus_type(selected_customer.getCus_type());
                lead_choose.setText(selected_customer.getLeadno());

                pinc_customer.addTextChangedListener(new TextWatcher() {
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
                            pinc_customer.setText(trimmedInput);
                            pinc_customer.setSelection(trimmedInput.length());
                        }
                    }
                });

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customers_db").child(selected_customer.getId());
                ref.child("address").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (!dataSnapshot.getKey().equalsIgnoreCase("ad_count")) {
                                address_firebase adt = dataSnapshot.getValue(address_firebase.class);
                                ltad.add(adt);
                            }
                            ctad[0] = ltad.size();
                            set_add_list(ltad);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                ref.child("contacts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (!dataSnapshot.getKey().equalsIgnoreCase("ct_count")) {
                                cus_contact_firebase cdt = dataSnapshot.getValue(cus_contact_firebase.class);
                                ltct.add(cdt);
                            }
                            ctcus[0] = ltct.size();
                            set_ct_list(ltct);
//
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                lv_cus_add.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        address_firebase adtemp = (address_firebase) parent.getItemAtPosition(position);
                        addr_customer.setText(adtemp.getAddress());
                        pinc_customer.setText(adtemp.getPincode());
                        city_customer.setText(adtemp.getCity());
                        state_customer.setText(adtemp.getState());
                        ltad.remove(position);
                        set_add_list(ltad);
                    }
                });

                lv_cus_ct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        cus_contact_firebase cttemp = (cus_contact_firebase) parent.getItemAtPosition(position);
                        ct_nm.setText(cttemp.getName());
                        customer_ph.setText(cttemp.getPh());
                        email_customer.setText(cttemp.getEmail());
                        dept_customer.setText(cttemp.getDept());
                        desg_customer.setText(cttemp.getDesg());
                        ltct.remove(position);
                        set_ct_list(ltct);
                    }
                });

                edit_customer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String leadno = lead_choose.getText().toString().substring(0, 8);
                        String cust = cus_type.getSelectedItem().toString();
//                        Toast.makeText(getActivity(), ltad.size()+"_"+ ltct.size(), Toast.LENGTH_SHORT).show();
                        String cusnm = customer_name.getText().toString();
                        if (ltad.size() > 0 && ltct.size() > 0 && !leadno.equalsIgnoreCase("") && !cust.equalsIgnoreCase("select") && !cusnm.equalsIgnoreCase("")) {
                            Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("customers_db").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String new_id = selected_customer.getId();

                                    customer_firebase cusdt = new customer_firebase(new_id, selected_customer.getLeadno(), cusnm, cust);

                                    FirebaseDatabase.getInstance().getReference("customers_db").child(new_id).setValue(cusdt);
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customers_db").child(new_id);

                                    for (address_firebase adr : ltad) {
                                        ref.child("address").child(adr.getId()).setValue(adr);
                                    }
                                    ref.child("address").child("ad_count").setValue(ctad[0]);

                                    for (cus_contact_firebase ct : ltct) {
                                        ref.child("contacts").child(ct.getId()).setValue(ct);
                                    }
                                    ref.child("contacts").child("ct_count").setValue(ctcus[0]);


                                    FirebaseDatabase.getInstance().getReference("lead_db").child(leadno).child("l_status").setValue("Customer");
                                    set_customer_lv("");
                                    closeform();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Check Values", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                new_addr_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = addr_customer.getText().toString();
                        String pincode = pinc_customer.getText().toString();
                        String city = city_customer.getText().toString();
                        String state = state_customer.getText().toString();

                        if (validation_addr(address, pincode, city, state)) {
                            String id = "01AD" + String.format("%02d", ctad[0] + 1);
                            address_firebase newadr = new address_firebase(id, address, city, state, pincode);
                            ltad.add(newadr);
                            ctad[0]++;
                            set_add_list(ltad);

                            addr_customer.setText("");
                            pinc_customer.setText("");
                            city_customer.setText("");
                            state_customer.setText("");
                        }

                    }
                });

                new_ct_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = ct_nm.getText().toString();
                        String phone = customer_ph.getText().toString();
                        String email = email_customer.getText().toString();
                        String dept = dept_customer.getText().toString();
                        String desg = desg_customer.getText().toString();
                        if (validation_ct(name, phone, email, dept, desg)) {
                            String id = "01CT" + String.format("%02d", ctcus[0] + 1);
                            cus_contact_firebase cur_cus = new cus_contact_firebase(id, name, phone, email, dept, desg);
                            ltct.add(cur_cus);
                            set_ct_list(ltct);
                            ctcus[0]++;
                            ct_nm.setText("");
                            customer_ph.setText("");
                            email_customer.setText("");
                            dept_customer.setText("");
                            desg_customer.setText("");

                        }

                    }
                });

                del_customer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ref.removeValue();
                        closeform();
                    }
                });

                cancel_customer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeform();
                    }
                });
            }
        });


        return v;
    }

    public void set_customer_lv(String x) {

        FirebaseDatabase.getInstance().getReference().child("customers_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<customer_firebase> temp = new ArrayList<>();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("cus_count")) {
                        customer_firebase gg = snapshot.getValue(customer_firebase.class);
                        if (x.equalsIgnoreCase("") || gg.getCus_name().toUpperCase().contains(x)) {
                            i++;
                            gg.setSno(i + "");
                            temp.add(gg);
                        }
                    }
                }

                if (getActivity() != null) {
                    cuslv_adapter arrayAdapter1 = new cuslv_adapter(getActivity(), R.layout.listviewhsn, temp);
                    customer_lv.setAdapter(arrayAdapter1);
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

    public boolean validation_addr(String address, String pincode, String city, String state) {
        if (address.equalsIgnoreCase("") || pincode.equalsIgnoreCase("") || city.equalsIgnoreCase("") || state.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Enter All Values", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validation_ct(String name, String phone, String email, String dept, String desg) {
        if (name.equalsIgnoreCase("") || phone.equalsIgnoreCase("") || email.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Enter All Values", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!phone.matches("^(\\+91[\\-\\s]?)?0?(91)?[789]\\d{9}$")) {
            Toast.makeText(getActivity(), "Invalid Phone no", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void set_add_list(List<address_firebase> lt) {
        new_cus_add arrayAdapter1 = new new_cus_add(getActivity(), R.layout.listviewhsn, lt);
        lv_cus_add.setAdapter(arrayAdapter1);

        float density = getResources().getDisplayMetrics().density;
        int desiredHeightDp = 50;
        int desiredHeightPx = (int) (desiredHeightDp * density * lt.size());

        ViewGroup.LayoutParams layoutParams = lv_cus_add.getLayoutParams();
        layoutParams.height = desiredHeightPx;
        lv_cus_add.setLayoutParams(layoutParams);
    }

    public void set_ct_list(List<cus_contact_firebase> lt2) {
        new_cus_ct arrayAdapter1 = new new_cus_ct(getActivity(), R.layout.listviewhsn, lt2);
        lv_cus_ct.setAdapter(arrayAdapter1);

        float density = getResources().getDisplayMetrics().density;
        int desiredHeightDp = 50;
        int desiredHeightPx = (int) (desiredHeightDp * density * lt2.size());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lv_cus_ct.getLayoutParams();
        layoutParams.height = desiredHeightPx;
        lv_cus_ct.setLayoutParams(layoutParams);
    }

    public void set_up_lead_choose() {
        FirebaseDatabase.getInstance().getReference().child("lead_db").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<String> temp = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equalsIgnoreCase("id_count")) {
                        lead_Firebase gg = snapshot.getValue(lead_Firebase.class);
                        if (!gg.getL_status().equalsIgnoreCase("customer"))
                            temp.add(gg.getId() + "|" + gg.getCmp_nm());
                    }
                }

                if (getActivity() != null) {
                    CustomAutoCompleteAdapter adaptertemp = new CustomAutoCompleteAdapter(getContext(), temp);
                    lead_choose.setAdapter(adaptertemp);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void set_up_cus_type() {
        List<String> lt = new LinkedList<>();
        lt.add("Registered Business");
        lt.add("UnRegistered Business");
        lt.add("Special Economic Zone(SEZ)");
        lt.add("Select");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lt);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cus_type.setAdapter(adapter);
        int position = adapter.getPosition("Select");
        cus_type.setSelection(position);
    }

    public void set_up_cus_type(String x) {
        List<String> lt = new LinkedList<>();
        lt.add("Registered Business");
        lt.add("UnRegistered Business");
        lt.add("Special Economic Zone(SEZ)");
        lt.add("Select");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lt);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cus_type.setAdapter(adapter);
        int position = adapter.getPosition(x);
        cus_type.setSelection(position);
    }


    public void fill_initial_details(String idx) {
        FirebaseDatabase.getInstance().getReference().child("lead_db").child(idx).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    lead_Firebase ldt = dataSnapshot.getValue(lead_Firebase.class);
                    customer_ph.setText(ldt.getCt_ph());


                    email_customer.setText(ldt.getMail());

                    DataSnapshot addressSnapshot = dataSnapshot.child("address");
                    int id = Integer.parseInt(addressSnapshot.child("ad_count").getValue().toString());
                    String cur_id = "01AD" + String.format("%03d", id);
                    address_firebase adt = addressSnapshot.child(cur_id).getValue(address_firebase.class);

                    addr_customer.setText(adt.getAddress());
                    pinc_customer.setText(adt.getPincode());
                    city_customer.setText(adt.getCity());
                    state_customer.setText(adt.getState());

                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
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
                                    city_customer.setText(get_district);
                                    state_customer.setText(get_state);
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

    public void open_v(RelativeLayout rl, ImageView img, LinearLayout ll) {
        ll.setBackgroundColor(R.drawable.gradcav2);
        img.setBackgroundResource(R.drawable.up_icon);
        rl.setVisibility(View.VISIBLE);
    }

    public void close_v(RelativeLayout rl, ImageView img, LinearLayout ll) {
        img.setBackgroundResource(R.drawable.down_icon);
        ll.setBackgroundColor(Color.TRANSPARENT);
        rl.setVisibility(View.GONE);
    }
}