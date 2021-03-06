package com.johnestebanap.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.johnestebanap.myapplication.R;

public class HomeFragment extends Fragment {

    Button btnDucmentosList,btnQr,btnOcr;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnDucmentosList = view.findViewById(R.id.btn_documentos);
        btnQr = view.findViewById(R.id.btn_qr);
        btnOcr = view.findViewById(R.id.btn_ocr);
        btnDucmentosList.setOnClickListener(v -> showListDocuments());
        btnQr.setOnClickListener(v -> showQr());
        btnOcr.setOnClickListener(v -> openOcr());
        return view;
    }

    public void showListDocuments() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new ListDocumentFragment());
        fragmentTransaction.commit();
    }

    public void showQr() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
       // intentIntegrator.initiateScan();
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Selecciona el QR.");
        intentIntegrator.setTorchEnabled(true);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
            if (result != null && !TextUtils.isEmpty(result.getContents().toString())) {
                String url = result.getContents().toString();
                showWebView(url);
            } else {
                //Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        }finally {

        }

    }

    public void showWebView(String url) {
        Bundle args = new Bundle();
        args.putString("url",url);
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, webViewFragment);
        fragmentTransaction.commit();
    }

    public void openOcr() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new OcrFragment()).setReorderingAllowed(true).addToBackStack(null).commit();
    }
}