package com.johnestebanap.myapplication.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.johnestebanap.myapplication.R;
import com.johnestebanap.myapplication.db.ControllerDocument;
import com.johnestebanap.myapplication.db.Document;

public class DocumentosFragment extends Fragment {


    public String userId;
    private EditText etDescription, etURL, etTipoDoc, etEstado;
    private String strTipoDoc, strDescription, strURL, strEstado;
    String[] items;
    boolean[] checkedItems;
    private Button btnTipoDoc, btnEstado, btnCreate;

    public DocumentosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_documentos, container, false);

        btnTipoDoc = view.findViewById(R.id.btnTipodoc);
        etTipoDoc = view.findViewById(R.id.etTipodoc);
        etDescription = view.findViewById(R.id.editTextDescripcion);
        etURL = view.findViewById(R.id.editTextURL);
        etEstado = view.findViewById(R.id.etEstado);
        btnEstado = view.findViewById(R.id.btnEstado);
        btnCreate = view.findViewById(R.id.btnCrearNovedad);

        Log.e("LOG", "oncreate OK");


        btnTipoDoc.setOnClickListener(v -> selectTipoDoc());
        btnEstado.setOnClickListener(v -> selectEstado());
        btnCreate.setOnClickListener(v -> validator());
        return view;
    }

    private void validator() {
        Log.e("LOG", "Validator OK");
        getData();
        insertData();
        showListDocuments();
    }
/*
    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this).
                setTitle("Exit")
                .setMessage("Are you sure to exit?")
                .setCancelable(false)
                .setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.cancel()))
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    super.finish();
                })
                .show();
    }*/

    private void getData() {
        strTipoDoc = etTipoDoc.getText().toString();
        strDescription = etDescription.getText().toString();
        strURL = etURL.getText().toString();
        strEstado = etEstado.getText().toString();
    }

    private void clearDocumentScreen() {
        etDescription.setText("");
        etURL.setText("");

    }

    private void insertData() {

        //Document document = new Document(strType, strDescription, strUrl, strSeveridad);

        Log.e("LOG", "en insertData URL: " + strURL);

        Document document = new Document(strTipoDoc, strDescription, strURL, strEstado);
        ControllerDocument controller = new ControllerDocument(getContext());

        long db = controller.insertDocument(document);

        if (db > 0) {
            Toast.makeText(getContext(), "Successfully!", Toast.LENGTH_SHORT).show();
            clearDocumentScreen();
        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectTipoDoc() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Seleccione el tipo de documento: ");
        String[] items = {"Reglamentos", "Procedimientos", "Instructivos", "Formatos", "Registros", "Programas", "Planes", "Guías", "Manuales", "Políticas", "Lineamientos"};
        String[] urlItems = {
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link",
                "https://docs.google.com/forms/d/e/1FAIpQLSflz8mrviuiyyKahWYui0abjRLVpUWNct2v_izDkoKt7QUi1g/viewform?usp=sf_link"
        };
        boolean[] checkedItems = {false, false, false, false, false, false, false, false, false, false, false};
        alertDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                SharedPreferences preferences = getActivity().getSharedPreferences("which", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                //Log.e("LOG", "length : " + items.length.toString());

                for (int i = 0; i < items.length; i++) {

                    Log.e("LOG", "booleano : " + checkedItems[i]);
                    Log.e("LOG", "nombre Item: " + items[i]);

                    if (checkedItems[i] == true) {
                        etTipoDoc.setText(items[i]);
                        etURL.setText(urlItems[i]);
                        dialog.dismiss();
                    }

                }

            }

            private void closeDialog() {
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);
                alert.hide();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();

    }

    private void selectEstado() {
        Log.e("Log", "Entro a selectEstado");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        SharedPreferences preferences = getActivity().getSharedPreferences("guest", MODE_PRIVATE);
        String rol = preferences.getString("rol", "");


        alertDialog.setTitle("Seleccione el estado de documento: ");
        String[] itemsS = {"votacion", "en revision", "falta por votar", "aprobado", "publicado", "en proceso", "propuesta", "agendacion", "agendado"};
        boolean[] checkedItemsS = {false, false, false, false, false, false, false, false, false};

        String[] itemsE = {"en revision", "en proceso"};
        boolean[] checkedItemsE = {false, false};

        if (rol.equals("8")) {

            items = itemsE;
            checkedItems = checkedItemsE;
        }

        if (rol.equals("1")) {

            items = itemsS;
            checkedItems = checkedItemsS;
        }


        alertDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                SharedPreferences preferences = getActivity().getSharedPreferences("which", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                for (int i = 0; i < items.length; i++) {

                    Log.e("LOG", "booleano : " + checkedItems[i]);
                    Log.e("LOG", "nombre Item: " + items[i]);

                    if (checkedItems[i] == true) {
                        etEstado.setText(items[i]);
                        dialog.dismiss();
                    }

                }

            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void showListDocuments() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new ListDocumentFragment());
        fragmentTransaction.commit();
    }

}