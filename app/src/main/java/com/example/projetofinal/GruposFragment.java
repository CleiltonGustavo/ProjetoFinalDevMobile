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

import com.example.projetofinal.dao.GrupoDao;
import com.example.projetofinal.dao.TarefaDao;
import com.example.projetofinal.database.AppDataBase;
import com.example.projetofinal.models.Grupo;
import com.example.projetofinal.models.Tarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class GruposFragment extends Fragment {

    //Neste fragment serão mostrados os grupos de tarefas

    private RecyclerView recyclerViewGrupos; //RecyclerView que ira receber a lista de tarefas ainda ativas
    private List<Grupo> gruposList; //Lista de grupos
    ItemAdapterGrupo itensAdapterGrupo;
    String valorTituloGrupo, valorDescGrupo;
    private AppDataBase appDatabase;

    Button btAddGrupo;

    public GruposFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewGrupos = getView().findViewById(R.id.rv_itens_grupos);
        btAddGrupo = getView().findViewById(R.id.btAddGrupo);
        gruposList = new ArrayList<>();

        //Obtenho os dados enviados pelo CadastrarTarefa por meio de bundle
        Bundle bundle_grupo = getArguments();
        if (bundle_grupo != null) {
            valorTituloGrupo = bundle_grupo.getString("valorPassadoTituloGrupo");
            valorDescGrupo = bundle_grupo.getString("valorPassadoDescGrupo");
        }

        appDatabase = Room.databaseBuilder(getContext(), AppDataBase.class, "db_grupos")
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        /*TarefaDao tarefaDao = appDatabase.tarefaDao();*/
        GrupoDao grupoDao = appDatabase.grupoDao();



        //Limpar os dados do banco:
/*        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.clearAllTables();
            }
        });*/

        List<Grupo> gruposDoBd = grupoDao.getAllGrupos();
        for(Grupo g : gruposDoBd){
            Log.d("sid-tag", g.toString());
        }

        gruposList = gruposDoBd;

        itensAdapterGrupo = new ItemAdapterGrupo(gruposList);

        recyclerViewGrupos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewGrupos.setAdapter(itensAdapterGrupo);


        btAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new CadastrarGrupoFragment()).commit();
            }
        });

        if (valorTituloGrupo != null && valorDescGrupo != null){
            Grupo ga = new Grupo(valorTituloGrupo, valorDescGrupo);
            gruposList.add(ga);
            grupoDao.insertAll(ga);
            itensAdapterGrupo.notifyDataSetChanged();
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grupos, container, false);
    }
}