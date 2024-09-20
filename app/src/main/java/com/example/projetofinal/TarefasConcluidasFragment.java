package com.example.projetofinal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class TarefasConcluidasFragment extends Fragment {

    //Neste Fragment serão mostradas as tarefas concluidas

    private RecyclerView recyclerView; //RecyclerView que ira receber a lista de tarefas ainda ativas
    private List<Tarefa> tarefasConcluidasList; //Lista de tarefas ainda ativas
    ItemAdapter itensAdapter;
    String valorTitulo, valorDesc;
    private AppDataBase appDatabase;

    int valorTrivialGrupo = 0; //Este valor é pra ser enviado para o ItemAdapter quando não estivermos na tela de adicionar ao grupo, o que significa que ele não terá utilidade

    public TarefasConcluidasFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.rv_itens);
        tarefasConcluidasList = new ArrayList<>();

        //Obtenho os dados enviados pelo CadastrarTarefa por meio de bundle
/*        Bundle bundle = getArguments();
        if (bundle != null) {
            valorTitulo = bundle.getString("valorPassadoTitulo");
            valorDesc = bundle.getString("valorPassadoDesc");
        }*/


/*        Intent dado_enviado = getActivity().getIntent();
        valorTitulo = dado_enviado.getStringExtra("valorPassadoTitulo");
        valorDesc = dado_enviado.getStringExtra("valorPassadoDesc");*/


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
            if(!t.status){ //Verifico se o status da tarefa está true ou false, se estiver false ele entra para minha lista de tarefas concluidas
                tarefasConcluidasList.add(t);
            }
        }

        itensAdapter = new ItemAdapter(tarefasConcluidasList, false, false, valorTrivialGrupo, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itensAdapter);

/*        if (valorTitulo != null && valorDesc != null){
            Tarefa ta = new Tarefa(valorTitulo, valorDesc);
            tarefasConcluidasList.add(ta);
            tarefaDao.insertAll(ta);
            itensAdapter.notifyDataSetChanged();
        }*/

        swipeToDelete();
    }

    private void swipeToDelete(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Tarefa tarefaParaDeletar = tarefasConcluidasList.get(position);

                Log.d("sid-tag", "Tarefa para deletar: " + tarefaParaDeletar.toString() + " Posição do item: " + position);
                appDatabase.tarefaDao().delete(tarefaParaDeletar);

                Log.d("sid-tag", "Tarefas concluidas antes: " + tarefasConcluidasList.size());

                tarefasConcluidasList.remove(position);

                Log.d("sid-tag", "Tarefas concluidas depois: " + tarefasConcluidasList.size());

                itensAdapter.notifyItemRemoved(position);
            }
        }).attachToRecyclerView(recyclerView);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
