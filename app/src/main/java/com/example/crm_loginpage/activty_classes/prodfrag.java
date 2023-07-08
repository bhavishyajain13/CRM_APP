package com.example.crm_loginpage.activty_classes;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.crm_loginpage.Adapters.product_adapter;
import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.GstDb_firebase;
import com.example.crm_loginpage.class_structure_files.PackDb_firebase;
import com.example.crm_loginpage.class_structure_files.prod_firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class prodfrag extends Fragment {

    //DIALOG INITIALIZATION
    Dialog dialog;
    androidx.appcompat.widget.AppCompatButton dcan;
    androidx.appcompat.widget.AppCompatButton edit;
    androidx.appcompat.widget.AppCompatButton addprod;
    androidx.appcompat.widget.AppCompatButton del;
    EditText prod_name;
    EditText pp_pur;
    EditText pp_mrp;
    EditText pp_sale;
    Spinner gst_spinner;
    Spinner hsn_spinner;
    Spinner packu_spinner;


    //MAIN CODE INITALIZATIONS
    androidx.appcompat.widget.AppCompatButton add_new;
    androidx.appcompat.widget.AppCompatButton srch;
    EditText srch_text;
    //    androidx.recyclerview.widget.RecyclerView rc_prod;
    ListView lv_prod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        //MAIN CODE SET UP
        add_new = v.findViewById(R.id.new_prod);
        lv_prod = v.findViewById(R.id.rc_prod);
        srch = v.findViewById(R.id.prod_search);
        srch_text = v.findViewById(R.id.prod_name);

        //PROMPT SET UP
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.prod_prompt);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.new_prod);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dcan = dialog.findViewById(R.id.cancel_pp);
        edit = dialog.findViewById(R.id.edit_pp);
        addprod = dialog.findViewById(R.id.add_pp);
        del = dialog.findViewById(R.id.del_pp);
        prod_name = dialog.findViewById(R.id.new_prod);
        pp_pur = dialog.findViewById(R.id.pp_pur);
        pp_mrp = dialog.findViewById(R.id.pp_mrp);
        pp_sale = dialog.findViewById(R.id.pp_sale);
        gst_spinner = dialog.findViewById(R.id.prod_gst_spin);
        hsn_spinner = dialog.findViewById(R.id.prod_hsn_spin);
        packu_spinner = dialog.findViewById(R.id.prod_packu_spin);

        set_prod_listview();

        srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("products").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        List<prod_firebase> temp = new ArrayList<>();
                        int i = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            prod_firebase gg = snapshot.getValue(prod_firebase.class);
                            String stmt = srch_text.getText().toString().toUpperCase();
                            if (gg.getStatus().equalsIgnoreCase("1") && gg.getP_name().contains(stmt)) {
                                gg.setSno(i + 1+"");
                                i++;
                                temp.add(gg);
                            }
                        }

                        if (getActivity() != null) {
                            product_adapter arrayAdapter1 = new product_adapter(getActivity(), R.layout.listviewhsn, temp);
                            lv_prod.setAdapter(arrayAdapter1);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        lv_prod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                prod_firebase selected_prod = (prod_firebase) parent.getItemAtPosition(i);

                pp_packu(selected_prod.getSel_packu());
                pp_gst(selected_prod.getSel_gst());

                gst_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        pp_hsn(selected_prod.getSel_hsn());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        pp_hsn();
                    }
                });

                prod_name.setText(selected_prod.getP_name());
                pp_pur.setText(selected_prod.getPur_rate());
                pp_mrp.setText(selected_prod.getMrp_rate());
                pp_sale.setText(selected_prod.getSale_rate());
                edit.setVisibility(View.VISIBLE);
                addprod.setVisibility(View.GONE);
                del.setVisibility(View.VISIBLE);
                dialog.show();

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pur_rate = pp_pur.getText().toString().toUpperCase();
                        String mrp_rate = pp_mrp.getText().toString().toUpperCase();
                        String sale_rate = pp_sale.getText().toString().toUpperCase();
                        String p_name = prod_name.getText().toString().toUpperCase();
                        String sel_packu = packu_spinner.getSelectedItem().toString().toUpperCase();
                        String sel_gst = gst_spinner.getSelectedItem().toString().toUpperCase();
                        String sel_hsn = hsn_spinner.getSelectedItem().toString().toUpperCase();

                        if (!validation_inp(pur_rate, mrp_rate, sale_rate, p_name, sel_packu, sel_gst, sel_hsn)) {
                        } else {
                            // can also jutst create a new object and add instead of changinf values 1 by 1. IDK what i was thinking lol.
                            FirebaseDatabase.getInstance().getReference().child("products").child(selected_prod.getId()).child("p_name").setValue(prod_name.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("products").child(selected_prod.getId()).child("pur_rate").setValue(pp_pur.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("products").child(selected_prod.getId()).child("sale_rate").setValue(pp_sale.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("products").child(selected_prod.getId()).child("mrp_rate").setValue(pp_mrp.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("products").child(selected_prod.getId()).child("sel_packu").setValue(packu_spinner.getSelectedItem().toString());
                            FirebaseDatabase.getInstance().getReference().child("products").child(selected_prod.getId()).child("sel_gst").setValue(gst_spinner.getSelectedItem().toString());
                            FirebaseDatabase.getInstance().getReference().child("products").child(selected_prod.getId()).child("sel_hsn").setValue(hsn_spinner.getSelectedItem().toString());
                            Toast.makeText(getActivity(), "Edited", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                });

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("products").child(selected_prod.getId()).child("status").setValue("0");
                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });


                dcan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prod_name.setText("");
                pp_pur.setText("");
                pp_mrp.setText("");
                pp_sale.setText("");
                pp_packu();
                pp_gst();
                del.setVisibility(View.INVISIBLE);
                addprod.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                dialog.show();


                gst_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        pp_hsn();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        pp_hsn();
                    }
                });

                prod_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0 && s.charAt(0) == ' ') {
                            s.delete(0, 1); // Remove the space at the beginning
                        }
                    }
                });
                pp_pur.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString();

                        if (!input.isEmpty()) {
                            int decimalIndex = input.indexOf(".");
                            if (decimalIndex != -1 && input.substring(decimalIndex + 1).length() > 2) {
                                s.delete(decimalIndex + 3, decimalIndex + 4); // Remove extra decimal places
                            }
                        }
                    }
                });
                pp_sale.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString();

                        if (!input.isEmpty()) {
                            int decimalIndex = input.indexOf(".");
                            if (decimalIndex != -1 && input.substring(decimalIndex + 1).length() > 2) {
                                s.delete(decimalIndex + 3, decimalIndex + 4); // Remove extra decimal places
                            }
                        }
                    }
                });
                pp_mrp.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Not needed for this implementation
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString();

                        if (!input.isEmpty()) {
                            int decimalIndex = input.indexOf(".");
                            if (decimalIndex != -1 && input.substring(decimalIndex + 1).length() > 2) {
                                s.delete(decimalIndex + 3, decimalIndex + 4); // Remove extra decimal places
                            }
                        }
                    }
                });


                addprod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pur_rate = pp_pur.getText().toString().toUpperCase();
                        String mrp_rate = pp_mrp.getText().toString().toUpperCase();
                        String sale_rate = pp_sale.getText().toString().toUpperCase();
                        String p_name = prod_name.getText().toString().toUpperCase();
                        String sel_packu = packu_spinner.getSelectedItem().toString().toUpperCase();
                        String sel_gst = gst_spinner.getSelectedItem().toString().toUpperCase();
                        String sel_hsn = hsn_spinner.getSelectedItem().toString().toUpperCase();
                        final int[] id_ct = {0};
                        FirebaseDatabase.getInstance().getReference("products").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                id_ct[0] = (int) snapshot.getChildrenCount() + 1;
                                String new_id = "01PRO" + String.format("%04d", id_ct[0]);
                                //doing this inside firebase code cuz its firebase codes are asynchronous
                                if (!validation_inp(pur_rate, mrp_rate, sale_rate, p_name, sel_packu, sel_gst, sel_hsn)) {
                                    Toast.makeText(getActivity(), "Enter Values Properly", Toast.LENGTH_SHORT).show();
                                } else {
                                    prod_firebase new_prod = new prod_firebase(new_id, pur_rate, mrp_rate, sale_rate, p_name, sel_packu, sel_gst, sel_hsn, 1 + "");
                                    FirebaseDatabase.getInstance().getReference("products").child(new_id).setValue(new_prod);
                                    Toast.makeText(getActivity(), "Product Added", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

                dcan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });


        return v;
    }

    public boolean validation_inp(String pur_rate, String mrp_rate, String sale_rate, String p_name, String sel_packu, String sel_gst, String sel_hsn) {
        if (sel_packu.matches("SELECT") || sel_gst.matches("SELECT") || sel_hsn.matches("SELECT")) {
            Toast.makeText(getActivity(), "Enter Values Properly", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pur_rate.matches("") || mrp_rate.matches("") || p_name.matches("") || sale_rate.matches("")) {
            Toast.makeText(getActivity(), "Enter Values Properly", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Integer.parseInt(mrp_rate) < Integer.parseInt(pur_rate)) {
            Toast.makeText(getActivity(), "MRP should be greater than Purchase Rate", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    public void set_prod_listview() {
        FirebaseDatabase.getInstance().getReference().child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<prod_firebase> temp = new ArrayList<>();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    prod_firebase gg = snapshot.getValue(prod_firebase.class);
                    if (gg.getStatus().equalsIgnoreCase("1")) {
                        gg.setSno(i + 1 + "");
                        i++;
                        temp.add(gg);
                    }
                }

                if (getActivity() != null) {
                    product_adapter arrayAdapter1 = new product_adapter(getActivity(), R.layout.listviewhsn, temp);
                    lv_prod.setAdapter(arrayAdapter1);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pp_gst() {
        FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> gstlist = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GstDb_firebase gg = snapshot.getValue(GstDb_firebase.class);
                    gstlist.add(gg.getGstper() + "");
                }
                gstlist.add("Select");

                if (getActivity() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, gstlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    gst_spinner.setAdapter(adapter);
                    int position = adapter.getPosition("Select");
                    gst_spinner.setSelection(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void pp_packu() {
        FirebaseDatabase.getInstance().getReference().child("packuz").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> optionlist = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PackDb_firebase gg = snapshot.getValue(PackDb_firebase.class);
                    optionlist.add(gg.getDesc() + "");
                }
                optionlist.add("Select");

                if (getActivity() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, optionlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    packu_spinner.setAdapter(adapter);
                    int position = adapter.getPosition("Select");
                    packu_spinner.setSelection(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pp_hsn() {
        if (gst_spinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            List<String> optionlist = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, optionlist);
            optionlist.add("Select");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hsn_spinner.setAdapter(adapter);
        } else {
            FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> temp = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equalsIgnoreCase(gst_spinner.getSelectedItem().toString())) {
//                        if (snapshot.getKey().matches(  i + "") ) {
                            if (snapshot.hasChild("HSN")) {
                                for (DataSnapshot snapshot1 : snapshot.child("HSN").getChildren()) {
                                    Object s = snapshot1.getValue();
                                    temp.add(s.toString());
                                }
                            }
                        }
                    }
                    if (getActivity() != null) {

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, temp);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        temp.add("Select");
                        hsn_spinner.setAdapter(adapter);
                        int position = adapter.getPosition("Select");
                        hsn_spinner.setSelection(position);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void pp_packu(String pos_val) {
        FirebaseDatabase.getInstance().getReference().child("packuz").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> optionlist = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PackDb_firebase gg = snapshot.getValue(PackDb_firebase.class);
                    optionlist.add(gg.getDesc() + "");
                }
                optionlist.add("Select");

                if (getActivity() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, optionlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    packu_spinner.setAdapter(adapter);
                    int position = adapter.getPosition(pos_val);
                    packu_spinner.setSelection(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pp_gst(String pos_val) {
        FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> gstlist = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GstDb_firebase gg = snapshot.getValue(GstDb_firebase.class);
                    gstlist.add(gg.getGstper() + "");
                }
                gstlist.add("Select");

                if (getActivity() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, gstlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    gst_spinner.setAdapter(adapter);
                    int position = adapter.getPosition(pos_val);
                    gst_spinner.setSelection(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void pp_hsn(String pos_val) {
//        i.matches("0") ||
        if (gst_spinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            List<String> optionlist = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, optionlist);
            optionlist.add("Select");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hsn_spinner.setAdapter(adapter);
        } else {
            FirebaseDatabase.getInstance().getReference().child("gstdb").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> temp = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equalsIgnoreCase(gst_spinner.getSelectedItem().toString())) {
//                        if (snapshot.getKey().matches(  i + "") ) {
                            if (snapshot.hasChild("HSN")) {
                                for (DataSnapshot snapshot1 : snapshot.child("HSN").getChildren()) {
                                    Object s = snapshot1.getValue();
                                    temp.add(s.toString());
                                }
                            }
                        }
                    }
                    if (getActivity() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, temp);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        temp.add("Select");
                        hsn_spinner.setAdapter(adapter);
                        int position = adapter.getPosition(pos_val);
                        hsn_spinner.setSelection(position);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}