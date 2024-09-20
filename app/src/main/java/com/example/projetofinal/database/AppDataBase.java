package com.example.projetofinal.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.projetofinal.dao.GrupoDao;
import com.example.projetofinal.dao.TarefaDao;
import com.example.projetofinal.models.Grupo;
import com.example.projetofinal.models.Tarefa;

@Database(entities = {Tarefa.class, Grupo.class}, version = 6)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TarefaDao tarefaDao();
    public abstract GrupoDao grupoDao();
}
