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

import com.example.crm_loginpage.Adapters.Packuadapt;
import com.example.crm_loginpage.unused.DbHelper;
import com.example.crm_loginpage.unused.PackDB;
import com.example.crm_loginpage.R;
import com.example.crm_loginpage.class_structure_files.PackDb_firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class packfrag extends Fragment {
    public static PackDB pdel;

    EditText packusize;
    private Dialog dialog,dialog2;
    androidx.appcompat.widget.AppCompatButton packu_newsize;
    androidx.appcompat.widget.AppCompatButton packuview;
    ListView lv_packu;

    Dialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.packu_frag, container, false);

        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.loading_page);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.show();

        //DATABSE SET UP
        DatabaseReference databaseReference;
        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
        databaseReference= rootnode.getReference("packuz");

        packusize= v.findViewById(R.id.packusize);
        packuview= v.findViewById(R.id.packuview);
        packu_newsize = v.findViewById(R.id.packu_newsize);
        mDialog= new Dialog(getActivity());
        lv_packu= v.findViewById(R.id.lv_packu);
        DbHelper dbHelper = new DbHelper(  getActivity(),"packu.db",null,3);
        setview();

        packu_newsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int qty_temp= Integer.parseInt( packusize.getText().toString() );

//                    Firebase Database
                    String fb_uid = FirebaseAuth.getInstance().getUid();
                    PackDb_firebase packDb_firebase = new PackDb_firebase( fb_uid,qty_temp, 1+"*"+qty_temp   );
                    databaseReference.child(qty_temp+"").setValue(packDb_firebase);
                    packusize.setText("");

                }
                catch (Exception e){
                    Toast.makeText(getActivity(),"Enter Value First", Toast.LENGTH_SHORT).show();
                }
                setview();
            }
        });

        packuview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int val = Integer.parseInt( packusize.getText().toString() );
//                    LOCAL DATABSE
//                    List<PackDB> temp= dbHelper.search_packu(val);
//                    Packuadapt arrayAdapter = new Packuadapt(getActivity(), R.layout.listview, temp );
//                    lv_packu.setAdapter(arrayAdapter);

//                    FIREBASE DATABASE
                    FirebaseDatabase.getInstance().getReference().child("packuz").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int ii=0;
                            List<PackDB> temp1 = new ArrayList<>();
                            for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                                PackDb_firebase packDb_firebase1= snapshot.getValue(PackDb_firebase.class);
                                if(packDb_firebase1.getQty()>=val) temp1.add( new PackDB(ii++,packDb_firebase1.getQty(),packDb_firebase1.getDesc() ) );
                            }

                            Packuadapt arrayAdapter = new Packuadapt(getActivity(), R.layout.listview, temp1 );
                            lv_packu.setAdapter(arrayAdapter);

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(),"major error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getActivity(),"SUCCESS" , Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    setview();
                    Toast.makeText(getActivity(),"SEARCH FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lv_packu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // WILL CALL THE POP UP AND THAT WILL DEL OR EDIT DATA
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                try{
                    dialog = new Dialog(getActivity());
//                    EditText newqty= dialog.findViewById(R.id.new_qty);
                    pdel = (PackDB) parent.getItemAtPosition(i);
                    dialog.setContentView(R.layout.packu_prompt);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.addpacku_prompt);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                    EditText newqty= dialog.findViewById(R.id.new_qty);
                   newqty.setText( Integer.toString( pdel.getQty() ) );
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
//                            LOCAL DATABASE
//                            dbHelper.delone(pdel);
                            FirebaseDatabase.getInstance().getReference("packuz").child(""+ pdel.getQty() ).removeValue();
//                            String fb_uid = FirebaseAuth.getInstance().getUid();
//                            PackDb_firebase packDb_firebase = new PackDb_firebase( fb_uid,0, 1+"*"+0   );
//                            databaseReference.child(0+"").setValue(packDb_firebase);
//                            FirebaseDatabase.getInstance().getReference("packuz").child(""+ 0 ).removeValue();

                            dialog.cancel();

                        }
                    });

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
//                                LOCAL DATABASE
//                                dbHelper.editone(pdel, Integer.parseInt(newqty.getText().toString())  );

                                HashMap hashMap = new HashMap();
                                hashMap.put( "qty", Integer.parseInt(newqty.getText().toString())  );
//                                hashMap.put("id", FirebaseAuth.getInstance().getUid().toString() );
                                hashMap.put("desc", "1"+"*"+newqty.getText().toString() );
                                FirebaseDatabase.getInstance().getReference("packuz").child(""+ pdel.getQty() ).removeValue();
                                String fb_uid = FirebaseAuth.getInstance().getUid();
                                PackDb_firebase packDb_firebase = new PackDb_firebase( fb_uid,  Integer.parseInt(newqty.getText().toString()) , 1+"*"+ Integer.parseInt(newqty.getText().toString())   );
                                databaseReference.child(Integer.parseInt(newqty.getText().toString())+"").setValue(packDb_firebase);

                                dialog.cancel();
                                setview();
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

    private void setview() {
//        List<PackDB> temp= dbHelper.getalldetails();
//        Packuadapt arrayAdapter = new Packuadapt(getActivity(), R.layout.listview, temp );
//        lv_packu.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("packuz").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<PackDB> temp1 = new ArrayList<>();
                int i=1;
                for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                    PackDb_firebase packDb_firebase1= snapshot.getValue(PackDb_firebase.class);
                    temp1.add( new PackDB(i++,packDb_firebase1.getQty(),packDb_firebase1.getDesc() ) );
                }

                Packuadapt arrayAdapter = new Packuadapt(getActivity(), R.layout.listview, temp1 );
                lv_packu.setAdapter(arrayAdapter);
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