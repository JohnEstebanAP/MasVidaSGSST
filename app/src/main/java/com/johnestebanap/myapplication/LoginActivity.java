package com.johnestebanap.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private int GOOGLE_SING_IN = 100;
    Button btnEntrar, btnRegistro, btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        btnEntrar = findViewById(R.id.btn_entrar);
        btnRegistro = findViewById(R.id.registrarse);
        btnGoogle = findViewById(R.id.btn_google);

        btnEntrar.setOnClickListener(view -> loginUser());
        btnRegistro.setOnClickListener(view -> registroUser());
        btnGoogle.setOnClickListener(view -> loginUserGoogle());
    }

    public void loginUserGoogle() {
        //Configuracion
        GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient googleClient = GoogleSignIn.getClient(this, googleConf);
        googleClient.signOut();
        startActivityForResult(googleClient.getSignInIntent(), GOOGLE_SING_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SING_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Bienvenid@", Toast.LENGTH_SHORT).show();
                                        showHome(account.getEmail());
                                    } else {
                                        showAlertError();
                                        Log.w("TAG", "Error", task.getException());
                                    }
                                }
                            }
                    );
                }
            } catch (ApiException e) {
                e.printStackTrace();
                showAlertError();
            }


        }
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

    public void registroUser() {
        startActivity(new Intent(this, RegistroActivity.class));
    }
}