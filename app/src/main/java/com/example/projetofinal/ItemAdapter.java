package com.example.projetofinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.projetofinal.database.AppDataBase;
import com.example.projetofinal.models.Tarefa;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItensViewHolder> {
    private List<Tarefa> tarefasList;

    private AppDataBase appDatabase;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); //Variável para converter de Long para String a data do prazo

    private boolean checarFragmento; //Booleano para checar se estou na tela de tarefas ativou ou na de concluídas

    private boolean checarFragmentoAddTarefa;

    private boolean checarFragmentoExibir;

    private int valorRecebidoIdGrupo;

    public ItemAdapter(List<Tarefa> tarefasList, boolean checarFragmento, boolean checarFragmentoAddTarefa, int valorRecebidoIdGrupo, boolean checarFragmentoExibir) { //Construtor do Adapter
        this.tarefasList = tarefasList;
        this.checarFragmento = checarFragmento;
        this.checarFragmentoAddTarefa = checarFragmentoAddTarefa;
        this.valorRecebidoIdGrupo = valorRecebidoIdGrupo;
        this.checarFragmentoExibir = checarFragmentoExibir;
    }

    public ItensViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItensViewHolder(itemView);
    }

    public void onBindViewHolder(ItensViewHolder holder, int position){
        Tarefa tarefas = tarefasList.get(position); //Pego um item da minha lista de entradas, e abaixo associarei valores aos campos de texto da tela de itens com a ajuda dos gets
        int tamanhoMaxTextoTitulo = 18;
        int tamanhoMaxTextoDesc = 18;

        //Exibir prévias dos textos ao invés dos textos completos
        if(tarefas.getTitulo().length() > tamanhoMaxTextoTitulo) {
            String textoTruncadoTitulo = tarefas.getTitulo().substring(0, tamanhoMaxTextoTitulo) + "...";
            holder.tv_titulo.setText(textoTruncadoTitulo);
        } else {
            holder.tv_titulo.setText(tarefas.getTitulo());
        }

        if(tarefas.getDescricao().length() > tamanhoMaxTextoDesc) {
            String textoTruncado = tarefas.getDescricao().substring(0, tamanhoMaxTextoDesc) + "...";
            holder.tv_desc.setText(textoTruncado);
        } else {
            holder.tv_desc.setText(tarefas.getDescricao());
        }

        holder.tvPrazo.setText(sdf.format(tarefas.getPrazo()));

        //Abaixo faço o manuseio da visibilidade dos botões dos itens
        //Se eu estiver no AddTarefaFragment
        if(checarFragmentoAddTarefa == true && checarFragmentoExibir == false && checarFragmento == false){
            holder.tvPrazo.setVisibility(View.GONE);
            holder.btnAddGrupo.setVisibility(View.VISIBLE);
            holder.btConcluir.setVisibility(View.GONE);
        }

        //Se eu estiver no HomeFragment
        if(checarFragmentoAddTarefa == false && checarFragmentoExibir == false && checarFragmento == true){
            holder.tvPrazo.setVisibility(View.VISIBLE);
            holder.btnAddGrupo.setVisibility(View.GONE);
            holder.btConcluir.setVisibility(View.VISIBLE);
        }

        //Se eu estiver no ExibirTarefasGrupoFragment
        if(checarFragmentoAddTarefa == false && checarFragmentoExibir == true && checarFragmento == false){
            holder.tvPrazo.setVisibility(View.VISIBLE);
            holder.btnAddGrupo.setVisibility(View.GONE);
            holder.btConcluir.setVisibility(View.VISIBLE);
        }

        //Se eu estiver no TarefasConcluidasFragment
        if(checarFragmentoAddTarefa == false && checarFragmentoExibir == false && checarFragmento == false){
            holder.tvPrazo.setVisibility(View.GONE);
            holder.btnAddGrupo.setVisibility(View.GONE);
            holder.btConcluir.setVisibility(View.GONE);
        }



        //Clico no botão e mudo o status do item de true para false, o que faz o item ser concluido
        holder.btConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarefas.setStatus(false);

                appDatabase = Room.databaseBuilder(v.getContext(), AppDataBase.class, "db_tarefas")
                        .enableMultiInstanceInvalidation()
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();

                appDatabase.tarefaDao().updateTarefa(tarefas);
                Toast.makeText(v.getContext(), "Você concluiu a tarefa: " + tarefas.getTitulo(), Toast.LENGTH_SHORT).show();
                tarefasList.remove(holder.getAdapterPosition()); //Removo a tarefa concluida da lista
                notifyItemRemoved(holder.getAdapterPosition()); //Notifico meu RecyclerView para que ele atualize
            }
        });

        //Ao clicar no botão AddGrupo o groupId da tarefa será igual ao id do grupo que foi recebido
        holder.btnAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarefas.setGroupId(valorRecebidoIdGrupo); //Seta o groupId da tarefa para o valor de id do grupo recebido

                appDatabase = Room.databaseBuilder(v.getContext(), AppDataBase.class, "db_tarefas")
                        .enableMultiInstanceInvalidation()
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();

                appDatabase.tarefaDao().updateTarefa(tarefas);

                Toast.makeText(v.getContext(), "Você adicionou a tarefa: " + tarefas.getTitulo() + " ao grupo.", Toast.LENGTH_SHORT).show();

                holder.btnAddGrupo.setBackgroundTintList(ContextCompat.getColorStateList(v.getContext(), R.color.my_green)); //Mudo a cor do meu botão para verde
            }
        });

        //Ir para a visão expandida da tarefa ao clicar nela
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManagerVE = ((FragmentActivity) v.getContext()).getSupportFragmentManager();

                ExibirTarefaExpandidaFragment exibirTarefaExpandidaFragment = new ExibirTarefaExpandidaFragment();

                Bundle bundle_id_tarefa = new Bundle();
                bundle_id_tarefa.putInt("idTarefa", tarefas.getUid());
                exibirTarefaExpandidaFragment.setArguments(bundle_id_tarefa);

                FragmentTransaction transaction = fragmentManagerVE.beginTransaction();

                transaction.replace(R.id.fragment_container, exibirTarefaExpandidaFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    public int getItemCount() { return tarefasList.size(); }

    public static class ItensViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_titulo, tv_desc, tvPrazo;
        Button btConcluir, btnAddGrupo;

        public ItensViewHolder(View itemView) {
            super(itemView);

            tv_titulo = itemView.findViewById(R.id.tv_titulo);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tvPrazo = itemView.findViewById(R.id.tvPrazo);
            btConcluir = itemView.findViewById(R.id.btConcluir);
            btnAddGrupo = itemView.findViewById(R.id.btnAddGrupo);




            tv_titulo.setOnClickListener(this);
            tv_desc.setOnClickListener(this);
        }

        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Você clicou no item: " + tv_titulo.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}