package com.example.crm_loginpage.activty_classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crm_loginpage.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class navcon extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    FragmentManager fm;

    TextView disp;
    FirebaseAuth mAuth;
    FirebaseUser user;
    NavigationView navigationView;
    Menu menu;
    MenuItem hsnMasterItem;
    MenuItem packuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_flow);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.draw_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new homefrag()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
        }


        navigationView.getMenu().findItem(R.id.nav_hsn).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_gst).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_packu).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_prod).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_lead).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_customer).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_opp).setVisible(false);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mAuth = FirebaseAuth.getInstance();

        if (R.id.nav_hsn == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new hsnfrag()).commit();
        } else if (R.id.master_opt == item.getItemId() ) {
            MenuItem cur= navigationView.getMenu().findItem(R.id.master_opt);
            MenuItem hs = navigationView.getMenu().findItem(R.id.nav_hsn);
            MenuItem gs = navigationView.getMenu().findItem(R.id.nav_gst);
            MenuItem pu = navigationView.getMenu().findItem(R.id.nav_packu);
            MenuItem pd = navigationView.getMenu().findItem(R.id.nav_prod);

            hs.setVisible( !hs.isVisible() );
            gs.setVisible( !gs.isVisible() );
            pu.setVisible( !pu.isVisible() );
            pd.setVisible( !pd.isVisible() );
            return true;
        } else if (R.id.sales == item.getItemId()) {
            MenuItem ld = navigationView.getMenu().findItem(R.id.nav_lead);
            MenuItem cus = navigationView.getMenu().findItem(R.id.nav_customer);
            MenuItem op = navigationView.getMenu().findItem(R.id.nav_opp);
            ld.setVisible( !ld.isVisible() );
            cus.setVisible( !cus.isVisible() );
            op.setVisible( !op.isVisible() );
            return true;
        } else if (R.id.nav_home == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new homefrag(),null).addToBackStack(null).commit();
        } else if (R.id.nav_abt == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new aboutfrag(),null).addToBackStack(null).commit();
        } else if (R.id.nav_packu == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new packfrag(),null).addToBackStack(null).commit();
        } else if (R.id.nav_gst == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new gstfrag(),null).addToBackStack(null).commit();
        } else if (R.id.nav_prod == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new prodfrag(),null).addToBackStack(null).commit();
        }else if (R.id.nav_opp == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new opportunity(),null).addToBackStack(null).commit();
        }
        else if (R.id.nav_lead == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new leadfrag(),null).addToBackStack(null).commit();
        } else if (R.id.nav_customer == item.getItemId()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new customerfrag(),null).addToBackStack(null).commit();
        } else if (R.id.nav_logout == item.getItemId()) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(navcon.this, MainActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}