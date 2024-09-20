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

import com.example.projetofinal.dao.TarefaDao;
import com.example.projetofinal.database.AppDataBase;
import com.example.projetofinal.models.Tarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ExibirTarefasGrupoFragment extends Fragment {


    private RecyclerView recyclerView;
    private List<Tarefa> tarefasList;
    ItemAdapter itensAdapter;
    private AppDataBase appDatabase;

    int valorGroupId;

    Button btVoltar;

    public ExibirTarefasGrupoFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.rv_itens);
        tarefasList = new ArrayList<>();
        btVoltar = getView().findViewById(R.id.btVoltar);

        //Recuperando o id do grupo que foi clicado
        Bundle bundle_id_grupo = getArguments();
        if (bundle_id_grupo != null){
            valorGroupId = bundle_id_grupo.getInt("idGrupo");
        }
        /*Log.d("checa-id", "O id do grupo que foi clicado é: " + valorGroupId);*/

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
            if(t.status && (t.groupId == valorGroupId)){ //Verifico se o status da tarefa está true ou false, se estiver true ele entra para minha lista de tarefas a fazer
                tarefasList.add(t);
            }
        }

        itensAdapter = new ItemAdapter(tarefasList, false, false, valorGroupId, true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itensAdapter);

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new GruposFragment()).commit();
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exibir_tarefas_grupo, container, false);
    }
}