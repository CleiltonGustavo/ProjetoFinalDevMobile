package com.example.projetofinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projetofinal.dao.TarefaDao;
import com.example.projetofinal.database.AppDataBase;
import com.example.projetofinal.models.Tarefa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    //Neste fragment serão mostradas as tarefas ainda não concluidas

    private RecyclerView recyclerView; //RecyclerView que ira receber a lista de tarefas ainda ativas
    private List<Tarefa> tarefasList; //Lista de tarefas ainda ativas
    ItemAdapter itensAdapter;
    String valorTitulo, valorDesc;

    private long valorData;
    private AppDataBase appDatabase;

    int valorTrivialGrupo = 0; //Este valor é pra ser enviado para o ItemAdapter quando não estivermos na tela de adicionar ao grupo, o que significa que ele não terá utilidade

    public HomeFragment() {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.rv_itens);
        tarefasList = new ArrayList<>();

        //Obtenho os dados enviados pelo CadastrarTarefa por meio de bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            valorTitulo = bundle.getString("valorPassadoTitulo");
            valorDesc = bundle.getString("valorPassadoDesc");
            valorData = bundle.getLong("valorPassadoData");
        }


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
            if(t.status){ //Verifico se o status da tarefa está true ou false, se estiver true ele entra para minha lista de tarefas a fazer
                tarefasList.add(t);
            }
        }

        itensAdapter = new ItemAdapter(tarefasList, true, false, valorTrivialGrupo, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itensAdapter);



/*        if (valorTitulo != null && valorDesc != null){
            Tarefa ta = new Tarefa(valorTitulo, valorDesc, valorData);
            tarefasList.add(ta);
            tarefaDao.insertAll(ta);
            itensAdapter.notifyDataSetChanged();
        }*/

        if (valorTitulo != null && valorDesc != null){
            Tarefa ta = new Tarefa(valorTitulo, valorDesc, valorData);

            tarefasList.add(ta);
            itensAdapter.notifyDataSetChanged();

            new InsertTarefaAsyncTask(tarefaDao).execute(ta);
        }


    }

    private static class InsertTarefaAsyncTask extends AsyncTask<Tarefa, Void, Void> {
        private TarefaDao tarefaDao;

        InsertTarefaAsyncTask(TarefaDao dao) {
            this.tarefaDao = dao;
        }

        @Override
        protected Void doInBackground(Tarefa... tarefas) {
            tarefaDao.insertAll(tarefas[0]);
            return null;
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}