package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projetofinal.dao.TarefaDao;
import com.example.projetofinal.database.AppDataBase;
import com.example.projetofinal.models.Tarefa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ExibirTarefaExpandidaFragment extends Fragment {

    //Neste fragment será mostrada uma visão expandida da tarefa

    private AppDataBase appDatabase;

    Button btVoltar;

    TextView tvTituloTarefa, tvDescricaoTarefa, tvPrazoTarefa;

    int idDaTarefa;

    Tarefa tarefaSelecionada;

    public ExibirTarefaExpandidaFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTituloTarefa = getView().findViewById(R.id.tvTituloTarefa);
        tvDescricaoTarefa = getView().findViewById(R.id.tvDescricaoTarefa);
        tvPrazoTarefa = getView().findViewById(R.id.tvPrazoTarefa);
        btVoltar = getView().findViewById(R.id.btVoltar);

        Bundle bundle_id_tarefa = getArguments();
        if (bundle_id_tarefa != null){
            idDaTarefa = bundle_id_tarefa.getInt("idTarefa");
        }
        Log.d("checa-id", "O id da tareafa que foi clicado é: " + idDaTarefa);

        appDatabase = Room.databaseBuilder(getContext(), AppDataBase.class, "db_tarefas")
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        TarefaDao tarefaDao = appDatabase.tarefaDao();


        List<Tarefa> tarefasDoBd = tarefaDao.getAllTarefas();
        for(Tarefa t : tarefasDoBd){
            Log.d("sid-tag", t.toString());
        }


        for(Tarefa t : tarefasDoBd){
            if (t.uid == idDaTarefa) {
                tarefaSelecionada = t;
            }
        }

        tvTituloTarefa.setText(tarefaSelecionada.titulo);
        tvDescricaoTarefa.setText(tarefaSelecionada.descricao);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); //Variável sdf criada para converter de Long para String a data
        tvPrazoTarefa.setText(sdf.format(tarefaSelecionada.prazo));


        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exibir_tarefa_expandida, container, false);
    }
}