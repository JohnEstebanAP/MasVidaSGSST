package com.johnestebanap.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Este puede generar error por lo que lo adecuado es colocarlo dentro de un try catch
        try{
            //El metodo Thread nos permite utilizar hilos, este nos permite con el metodo sleep detenernos o detener nuestro porgrama en un punto espesifico por milisegundos.
            Thread.sleep(2000);//Para deternerlo durante 2 segundos
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //Abrimos conecion con firebase y solicitamos si hay un usuario logeado
        FirebaseUser user = mAuth.getCurrentUser();

        //se inicia el SharedPreferences para verificar si hay un usuario logeado por este medio.
        SharedPreferences prefs = (SharedPreferences) getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email",null);
        Toast.makeText(this, ""+email, Toast.LENGTH_SHORT).show();
        if (email != null) {
            showHome(email);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
       }
       super.finish();//para finalizar la actividad y no quede en segundo plano avierta por detras luego de avirla otra actividad.
    }

    private void showHome(String email) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
