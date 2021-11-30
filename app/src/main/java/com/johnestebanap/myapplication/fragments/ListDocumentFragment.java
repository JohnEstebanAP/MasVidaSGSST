package com.johnestebanap.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnestebanap.myapplication.DocumentRVAdapter;
import com.johnestebanap.myapplication.HomeActivity;
import com.johnestebanap.myapplication.R;
import com.johnestebanap.myapplication.db.ControllerDocument;
import com.johnestebanap.myapplication.db.DbHelper;
import com.johnestebanap.myapplication.db.Document;

import java.util.ArrayList;

public class ListDocumentFragment extends Fragment {
    // creating variables for our array list,
    // dbhandler, adapter and recycler view.
    private ArrayList<Document> documentArrayList;
    private DbHelper dbHandler;
    private DocumentRVAdapter documentRVAdapter;
    private RecyclerView documentRV;
    private Button btnUrl;
    private TextView tvUrl;
    public String prueba;
    private SearchView searchView;
   private FloatingActionButton floatingActionButton;
    public ListDocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_document, container, false);

        prueba = "probando";
       // tvUrl = (TextView) view.findViewById(R.id.tvU);
        btnUrl = (Button) view.findViewById(R.id.btnURL);
        searchView=(SearchView) view.findViewById(R.id.xSearchView);
        floatingActionButton = view.findViewById(R.id.floating_action_button);

        floatingActionButton.setOnClickListener(v -> pCreateNovedades(view));
        Log.e("LOG", "logueo de boton");

        // initializing our all variables.
        documentArrayList = new ArrayList<>();
        dbHandler = new DbHelper(getContext());
        ControllerDocument controller = new ControllerDocument(getContext());
        // getting our course array
        // list from db handler class.
        documentArrayList = controller.getListDocuments();
        // on below line passing our array lost to our adapter class.
        documentRVAdapter = new DocumentRVAdapter(documentArrayList, getContext());
        documentRV = view.findViewById(R.id.idRVCourses);
        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        documentRV.setLayoutManager(linearLayoutManager);
        // setting our adapter to recycler view.
        documentRV.setAdapter(documentRVAdapter);
        //buuscador del texto
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                documentRVAdapter.filtrado(s);
                return false;
            }
        });

        return view;
    }
    //Cabio el Fragmen dentro del mismo Fragmen esto lo ago go getActivity par aseder a losrecursosd e la actividad y con getSupportFragmentManager
    public void pCreateNovedades (View view) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new DocumentosFragment());
        fragmentTransaction.commit();

    }

}