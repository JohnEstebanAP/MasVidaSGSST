package com.johnestebanap.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }
        super.finish();//para finalizar la actividad y no quede en segundo plano avierta por detras luego de avirla otra actividad.
    }
}
