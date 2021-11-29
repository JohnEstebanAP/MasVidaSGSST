package com.johnestebanap.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
    private String userId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText editTextNombreUsuario, editContraseña, editTextNombre, editTextApellido, editTextCedula, editTextTelefono, editCelular, editDireccion;
    private TextView TxvSelectRole;
    private CheckBox checkBox;
    private Button btnfinalizar;
    TextView txvRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextNombreUsuario = findViewById(R.id.editTextNombreUsuario);
        editContraseña = findViewById(R.id.editContraseña);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextCedula = findViewById(R.id.editTextCedula);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editCelular = findViewById(R.id.editCelular);
        editDireccion = findViewById(R.id.editDireccion);
        checkBox = findViewById(R.id.checkBox);
        btnfinalizar = findViewById(R.id.finalizar);

        txvRol = (TextView) findViewById(R.id.TxvSelectRole);
        txvRol.setOnClickListener(v -> showAlertDialog());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.select_roles, android.R.layout.simple_spinner_item);
        btnfinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    public void createUser() {

        String emailUser = editTextNombreUsuario.getText().toString();
        String password = editContraseña.getText().toString();
        String name = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String cedula = editTextCedula.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String celular = editCelular.getText().toString();
        String direccion = editDireccion.getText().toString();
        String rol = txvRol.getText().toString();

        if (TextUtils.isEmpty(emailUser)) {
            Toast.makeText(this, "pro favor completar el campo del correo electronico", Toast.LENGTH_SHORT).show();
            editTextNombreUsuario.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "pro favor completar el campo de la contraseña", Toast.LENGTH_SHORT).show();
            editContraseña.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "pro favor completar el campo del nombre", Toast.LENGTH_SHORT).show();
            editTextNombre.requestFocus();
        } else if (TextUtils.isEmpty(apellido)) {
            Toast.makeText(this, "pro favor completar el campo del apellido", Toast.LENGTH_SHORT).show();
            editTextApellido.requestFocus();
        } else if (TextUtils.isEmpty(cedula)) {
            Toast.makeText(this, "pro favor completar el campo de la cedula", Toast.LENGTH_SHORT).show();
            editTextCedula.requestFocus();
        } else if (TextUtils.isEmpty(telefono)) {
            Toast.makeText(this, "pro favor completar el campo del telefono", Toast.LENGTH_SHORT).show();
            editTextTelefono.requestFocus();
        } else if (TextUtils.isEmpty(celular)) {
            Toast.makeText(this, "pro favor completar el campo del celular", Toast.LENGTH_SHORT).show();
            editCelular.requestFocus();
        } else if (TextUtils.isEmpty(direccion)) {
            Toast.makeText(this, "pro favor completar el campo de la direción", Toast.LENGTH_SHORT).show();
            editDireccion.requestFocus();
        } else if (TextUtils.isEmpty(rol)) {
            Toast.makeText(this, "pro favor completar el campo del rol del usuario", Toast.LENGTH_SHORT).show();
            TxvSelectRole.requestFocus();
        } else {
//            mAuth.createUserWithEmailAndPassword(emailUser, password).addOnCompleteListener(
//                    new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                showHome(emailUser);
//                            } else {
//                                showAlertError();
//                            }
//                        }
//                    }
//            );

            mAuth.createUserWithEmailAndPassword(emailUser, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userId = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = db.collection("users").document(userId);

                                Map<String, Object> user = new HashMap<>();
                                user.put("Email", emailUser);
                                user.put("Nombre", name);
                                user.put("Apellido", apellido);
                                user.put("Cedula", cedula);
                                user.put("Telefono", telefono);
                                user.put("Celular", celular);
                                user.put("Direccion", direccion);
                                user.put("Rol", rol);

                                //   registro los datos nuevos con el metodo del set.(user)
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(@NonNull Void unused) {
                                        Toast.makeText(RegistroActivity.this, "Datos registrados", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                showAlertExito();
                                showHome(emailUser);
                            } else {
                                showAlertError();
                            }
                        }
                    });
        }
    }

    private void showHome(String email) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    private void showAlertExito() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegistroActivity.this);
        alertDialog.setTitle("¡Felicidades!");
        alertDialog.setMessage("Registro exitoso");
        alertDialog.setPositiveButton("Aceptar", null);
        alertDialog.create().show();
    }

    private void showAlertError() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegistroActivity.this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Se ha producidoun error autenticando al usuario");
        alertDialog.setPositiveButton("Aceptar", null);
        alertDialog.create().show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegistroActivity.this);
        alertDialog.setTitle("Seleccione su perfil.");
        String[] items = {"Administrador", "Empleado", "Supervisor", "Presidente COCOLA", "Presidente BE", "Presidente COPASST"};
        boolean[] checkedItems = {false, false, false, false, false, false};
        alertDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences("which", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                for (int i = 0; i < items.length; i++) {

                    Log.e("LOG", "booleano : " + checkedItems[i]);
                    Log.e("LOG", "nombre Item: " + items[i]);

                    if (checkedItems[i] == true) {
                        txvRol.setText(items[i]);
                        dialog.dismiss();
                    }

                }

                editor.putBoolean("ischeck", isChecked);
                editor.putInt("case", which);
                editor.commit();
                //ShowAdmin();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}