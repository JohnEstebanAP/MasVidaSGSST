package com.johnestebanap.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.johnestebanap.myapplication.R;

public class HomeFragment extends Fragment {

    Button btnDucmentosList;
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

        btnDucmentosList.setOnClickListener(v -> showListDocuments());
        return view;
    }

    public void showListDocuments() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new ListDocumentFragment());
        fragmentTransaction.commit();
    }
}