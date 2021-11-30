package com.johnestebanap.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        String email = getIntent().getExtras().getString("email");
        //Guardar datos
        SharedPreferences prefs = (SharedPreferences) getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email",email);
        editor.commit();
        Toast.makeText(this, "home: "+email, Toast.LENGTH_SHORT).show();
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