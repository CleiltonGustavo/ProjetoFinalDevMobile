package com.example.projetofinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.projetofinal.database.AppDataBase;
import com.example.projetofinal.models.Grupo;
import com.example.projetofinal.models.Tarefa;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ItemAdapterGrupo extends RecyclerView.Adapter<ItemAdapterGrupo.ItensViewHolder> {
    private List<Grupo> gruposList;

    private AppDataBase appDatabase;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); //Variável para converter de Long para String a data do prazo

    public ItemAdapterGrupo(List<Grupo> gruposList) { //Construtor do Adapter
        this.gruposList = gruposList;
    }

    public ItensViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_grupos, parent, false);
        return new ItensViewHolder(itemView);
    }

    public void onBindViewHolder(ItensViewHolder holder, int position){
        Grupo grupos = gruposList.get(position); //Pego um item da minha lista de entradas, e abaixo associarei valores aos campos de texto da tela de itens com a ajuda dos gets
        holder.tv_tituloGrupo.setText(grupos.getNomeGrupo());
        holder.tv_descGrupo.setText(grupos.getDescricaoGrupo());

        int tamanhoMaxTextoTitulo = 18;
        int tamanhoMaxTextoDesc = 18;

        //Exibir prévias dos textos ao invés dos textos completos
        if(grupos.getNomeGrupo().length() > tamanhoMaxTextoTitulo) {
            String textoTruncadoTitulo = grupos.getNomeGrupo().substring(0, tamanhoMaxTextoTitulo) + "...";
            holder.tv_tituloGrupo.setText(textoTruncadoTitulo);
        } else {
            holder.tv_tituloGrupo.setText(grupos.getNomeGrupo());
        }

        if(grupos.getDescricaoGrupo().length() > tamanhoMaxTextoDesc) {
            String textoTruncado = grupos.getDescricaoGrupo().substring(0, tamanhoMaxTextoDesc) + "...";
            holder.tv_descGrupo.setText(textoTruncado);
        } else {
            holder.tv_descGrupo.setText(grupos.getDescricaoGrupo());
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManagerTG = ((FragmentActivity) v.getContext()).getSupportFragmentManager();

                ExibirTarefasGrupoFragment exibirTarefasGrupoFragment = new ExibirTarefasGrupoFragment();

                Bundle bundle_id_grupo = new Bundle();
                bundle_id_grupo.putInt("idGrupo", grupos.getGid());
                exibirTarefasGrupoFragment.setArguments(bundle_id_grupo);

                FragmentTransaction transaction = fragmentManagerTG.beginTransaction();

                transaction.replace(R.id.fragment_container, exibirTarefasGrupoFragment);
                transaction.commit();


            }
        });

        //Clico no botão e ele me leva para uma tela com uma lista de tarefas ainda ativas para adicionar
        holder.btAddTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();

                AddTarefaFragment addTarefaFragment = new AddTarefaFragment();

                Bundle bundle_grupo_id = new Bundle();
                bundle_grupo_id.putInt("grupoId", grupos.getGid());
                addTarefaFragment.setArguments(bundle_grupo_id);

                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.fragment_container, addTarefaFragment);
                transaction.commit();

            }
        });

        //Clico no botão deletar e excluo o grupo de tarefas
        holder.btDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDatabase = Room.databaseBuilder(v.getContext(), AppDataBase.class, "db_grupos")
                        .enableMultiInstanceInvalidation()
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();


                appDatabase.grupoDao().updateGrupo(grupos);
                Toast.makeText(v.getContext(), "Você removeu o grupo: " + grupos.getNomeGrupo(), Toast.LENGTH_SHORT).show();
                gruposList.remove(holder.getAdapterPosition()); //Removo o grupo da lista
                appDatabase.grupoDao().delete(grupos);
                notifyItemRemoved(holder.getAdapterPosition()); //Notifico meu RecyclerView para que ele atualize
            }
        });


    }

    public int getItemCount() { return gruposList.size(); }

    public static class ItensViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_tituloGrupo, tv_descGrupo;
        Button btDeletar, btAddTarefa;

        public ItensViewHolder(View itemView) {
            super(itemView);

            tv_tituloGrupo = itemView.findViewById(R.id.tv_tituloGrupo);
            tv_descGrupo = itemView.findViewById(R.id.tv_descGrupo);
            btDeletar = itemView.findViewById(R.id.btDeletar);
            btAddTarefa = itemView.findViewById(R.id.btAddTarefas);



            tv_tituloGrupo.setOnClickListener(this);
            tv_descGrupo.setOnClickListener(this);
        }

        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Você clicou no item: " + tv_tituloGrupo.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
