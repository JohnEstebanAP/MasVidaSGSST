package com.johnestebanap.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.johnestebanap.myapplication.fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    BottomNavigationView bottomNavigation;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar=findViewById(R.id.topAppBar);
        drawerLayout=findViewById(R.id.drawer_layaout);
        navigationView=findViewById(R.id.navigation_view);
        bottomNavigation=findViewById(R.id.bottom_navigation);

       // El Fragment que se muestra por defecto
        showHome();

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open_nav,
                R.string.close_nav);//leyendo el nav drawer
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.mn_home:
                   // showFragment(0);
                    return true;
                case R.id.mn_Documentos:
                   // showFragment(1);
                    return true;
                case R.id.mn_ocr:
                   // showCreateUser();
                    return true;
                case R.id.mn_qr:
                    return true;
                case R.id.mn_settings:
                    return true;
                case R.id.mn_info:
                    return true;
                case R.id.mn_salir:
                    return true;
                default:
                    return false;
            }
        } );

        bottomNavigation.setSelectedItemId(R.id.bnv_documentos);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bnv_documentos:
                        Toast.makeText(HomeActivity.this, "Documentos", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bnv_ocr:
                        Toast.makeText(HomeActivity.this, "ocr", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bnv_qr:
                        Toast.makeText(HomeActivity.this, "qr", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        String email = getIntent().getExtras().getString("email");
        //Guardar datos
        SharedPreferences prefs = (SharedPreferences) getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email",email);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent=new Intent(this,MainActivity.class);
//        startActivity(intent);
    }
    public void closeAndDelogearse(){
        //borrar datos de secion
        mAuth.signOut();
        //eliminamos los datos del SharedPreferences
        getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit().clear().apply();
    }

    public void showHome(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new HomeFragment()).setReorderingAllowed(true).addToBackStack(null).commit();
    }

//    public void openOcr() {
//        Intent ocr = new Intent(this, OcrLocalActivity.class);
//        startActivity(ocr);
//    }

//    public void openDocumentos() {
//        Intent documentos = new Intent(this, Documentos.class);
//        startActivity(documentos);
//    }

//    public void openNovedades () {
//        Intent novedades = new Intent(this, ListDocumentsActivity.class);
//        startActivity(novedades);
//    }

//    public void openQr(){
//        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
//        intentIntegrator.initiateScan();
//        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//        intentIntegrator.setPrompt("Selecciona el QR.");
//        intentIntegrator.setTorchEnabled(true);
//        intentIntegrator.setBeepEnabled(true);
//        intentIntegrator.initiateScan();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            Toast.makeText(this, "El valor scaneado es:" + result.getContents(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, WebViewDocumentoActivity.class);
//            intent.putExtra("url",result.getContents() );
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}