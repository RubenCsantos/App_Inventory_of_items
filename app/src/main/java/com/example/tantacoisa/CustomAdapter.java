package com.example.tantacoisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.Inflater;

//Uso de uma recyclerView
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {

    private Context context;
    Activity activity;
    private ArrayList id_id,nome_name, localizaocao_local, data_adquirido, ultimo_visto; //Arrays que armazenam a informação de cada parâmetro

    ArrayList<String> arrayFull_id, arrayFull_nome, arrayFull_localizacao, arrayFull_dAdquirido, arrayFull_uVistos; //Arrays usados para a SearchView, e para o filter da mesma

//Inicialização de todos os Array's List's
    CustomAdapter(Activity activity, Context context,
                  ArrayList id_id,
                  ArrayList nome_name,
                  ArrayList localizacao_local,
                  ArrayList data_adquirido,
                  ArrayList ultimo_visto){
        this.activity = activity;
        this.context = context;
        this.id_id = id_id;
        this.nome_name = nome_name;
        this.localizaocao_local = localizacao_local;
        this.data_adquirido = data_adquirido;
        this.ultimo_visto = ultimo_visto;


        arrayFull_id = new ArrayList(id_id);
        arrayFull_nome = new ArrayList(nome_name);
        arrayFull_localizacao = new ArrayList(localizacao_local);
        arrayFull_dAdquirido = new ArrayList(data_adquirido);
        arrayFull_uVistos = new ArrayList(ultimo_visto);

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override  //Apresentação do conteúdo no Inventário
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_id.setText(String.valueOf(id_id.get(position)));
        holder.nome_name_txt.setText(String.valueOf(nome_name.get(position)));
        holder.localizacao_local_txt.setText(String.valueOf(localizaocao_local.get(position)));
        holder.data_adquirido_txt.setText("Adquirido: " + String.valueOf(data_adquirido.get(position)));
        holder.ultimo_visto_txt.setText("Vizualização: " + String.valueOf(ultimo_visto.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id_id.get(position)));
                intent.putExtra("nome", String.valueOf(nome_name.get(position)));                 //Passagem dos parâmetros nos Intentos
                intent.putExtra("localizacao", String.valueOf(localizaocao_local.get(position)));
                intent.putExtra("data_adquirido", String.valueOf(data_adquirido.get(position)));
                intent.putExtra("ultima_visto", String.valueOf(ultimo_visto.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nome_name.size();
    }

    @Override
    public Filter getFilter() {
        return FilterNome;
    }

    private Filter FilterNome=new Filter() {                          //Filtro da Search Bar, que permite filtrar pelo nome/designação do objeto
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchText = constraint.toString().toLowerCase();
            ArrayList tempArray = new ArrayList();
            if(searchText.length()==0 || searchText.isEmpty())
            {
                tempArray.addAll(arrayFull_nome);
            }
            else
            {
                for (int i=0;i<arrayFull_nome.size();i++)
                {
                    if(arrayFull_nome.get(i).toLowerCase().contains(searchText.toLowerCase()))
                    {
                        tempArray.add(arrayFull_nome.get(i));
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values=tempArray;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {   //Função que apresenta os resultados referentes ao que foi filtrado na pesquisa rápida
            nome_name.clear();
            nome_name.addAll((Collection) results.values);
            notifyDataSetChanged();


        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_id, nome_name_txt, localizacao_local_txt, data_adquirido_txt, ultimo_visto_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_id = itemView.findViewById(R.id.obj_id_txt);
            nome_name_txt = itemView.findViewById(R.id.nome_view);
            localizacao_local_txt = itemView.findViewById(R.id.localizacao_view);
            data_adquirido_txt = itemView.findViewById(R.id.dAdquirido_view);
            ultimo_visto_txt = itemView.findViewById(R.id.uVisto_view);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
