package com.johnestebanap.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.johnestebanap.myapplication.db.Document;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;


public class DocumentRVAdapter extends RecyclerView.Adapter<DocumentRVAdapter.ViewHolder> {

    // variable for our array list and context

    private ArrayList<Document> documentArrayList,busquedaEnLista;
    private Context context;
    private View view;

    // constructor
    public DocumentRVAdapter(ArrayList<Document> documentArrayList, Context context) {
        this.documentArrayList = documentArrayList;
        this.context = context;

        busquedaEnLista=new ArrayList<>();
        busquedaEnLista.addAll(documentArrayList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout
        // file for our recycler view items.
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_document_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // on below line we are setting data
        // to our views of recycler view item.
        Document modal = documentArrayList.get(position);
        holder.tvDocumentType.setText(modal.getTipoDoc());
        holder.tvDocumentUrl.setText(modal.getUrl());
        holder.tvDocumentDescription.setText(modal.getDescription());
        holder.tvDocumentStatus.setText(modal.getEstado().toUpperCase());


        //Log.e("ErrorFG", "onBindViewHolder: " + holder.tvDocumentType.getText().toString() );

        if (holder.tvDocumentStatus.getText().toString().equals("aprobado") || holder.tvDocumentStatus.getText().toString().equals("APROBADO")   ){
             holder.cardViewRv.setCardBackgroundColor(ContextCompat.getColor(holder.cardViewRv.getContext(), R.color.color_green_card_view));
        }else{
         //    holder.cardViewRv.setCardBackgroundColor(ContextCompat.getColor(holder.cardViewRv.getContext(), R.color.white));
        }


        holder.btnURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Presiono boton: "+position+" y la url es: "+ holder.tvDocumentUrl.getText(), Toast.LENGTH_SHORT).show();

               /*     Intent intent = new Intent(context, WebViewDocumentoActivity.class);
                    intent.putExtra("url", holder.tvDocumentUrl.getText());
                    context.startActivity(intent);*/
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Mensaje posicion: "+position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        // returning the size of our array list
        return documentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        public TextView tvDocumentType, tvDocumentUrl, tvDocumentDescription, tvDocumentStatus;
        public LinearLayout lyPpal;
        public Button btnURL;
        public CardView cardViewRv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            tvDocumentType = itemView.findViewById(R.id.tvDocumentType);
            tvDocumentUrl = itemView.findViewById(R.id.tvDocumentUrl);
            tvDocumentDescription = itemView.findViewById(R.id.tvDocumentDescription);
            tvDocumentStatus = itemView.findViewById(R.id.tvDocumentStatus);
            btnURL = itemView.findViewById(R.id.btnURL);

            lyPpal = itemView.findViewById(R.id.lyPpal);
            cardViewRv= itemView.findViewById(R.id.card_view_rv);
        }
    }

    public void filtrado (String txtBuscar){
        int longitud=txtBuscar.length();
        if (longitud==0){
            documentArrayList.clear();
            documentArrayList.addAll(busquedaEnLista);
        }
        else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Document> collection=documentArrayList.stream().filter(i -> i.getTipoDoc().toLowerCase().contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
                documentArrayList.clear();
                documentArrayList.addAll(collection);
            }
            else {
                for (Document document:busquedaEnLista){
                    if (document.getTipoDoc().toLowerCase().contains(txtBuscar.toLowerCase())){
                        documentArrayList.add(document);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }
}

