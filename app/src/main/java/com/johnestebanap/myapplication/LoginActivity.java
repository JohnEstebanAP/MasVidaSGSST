package com.johnestebanap.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Button btnEntrar, btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btnEntrar = findViewById(R.id.btn_entrar);
        btnRegistro = findViewById(R.id.registrarse);

        btnEntrar.setOnClickListener(view -> loginUser());
        btnRegistro.setOnClickListener(view -> registroUser());
    }

    public void loginUser() {
        EditText editTxtUser, ediTxtPassword;
        editTxtUser = (EditText) findViewById(R.id.txtUser);
        ediTxtPassword = (EditText) findViewById(R.id.txtPassword);

        preferences = getSharedPreferences("usuarios", MODE_PRIVATE);
        String user1 = "sg-sst"; // Nombre Shared
        String user2 = "empleado";
        String pass = "123456789"; //Pass Shared
        String rolUser1 = "1";
        String rolUser2 = "8";
        String email = editTxtUser.getText().toString();
        String password = ediTxtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Ingrese el Correo electronico", Toast.LENGTH_SHORT).show();
            editTxtUser.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese una Contraseña", Toast.LENGTH_SHORT).show();
            ediTxtPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Bienvenid@", Toast.LENGTH_SHORT).show();
                                showHome(email);
                            } else {
                                showAlertError();
                                Log.w("TAG", "Error", task.getException());
                            }
                        }
                    }
            );

        }

        if (user1.equals(email) && pass.equals(password)) {
            //Toast.makeText(MainActivity.this, "SG SST", Toast.LENGTH_SHORT).show();
            preferences = getSharedPreferences("guest", MODE_PRIVATE);
            editor = preferences.edit();
            editor.putString("Name", "sg-sst");
            editor.putString("Pass", "123456789");
            editor.putString("rol", "1");
            editor.commit();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            if (user2.equals(email) && pass.equals(password)) {
                //Toast.makeText(MainActivity.this, "empleado", Toast.LENGTH_SHORT).show();
                preferences = getSharedPreferences("guest", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("Name", "empleado");
                editor.putString("Pass", "123456789");
                editor.putString("rol", "8");
                editor.commit();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }
    private void showHome(String email) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    private void showAlertError() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Se ha producidoun error autenticando al usuario");
        alertDialog.setPositiveButton("Aceptar", null);
        alertDialog.create().show();
    }

    public void registroUser(){
        startActivity(new Intent(this, RegistroActivity.class));
    }
}