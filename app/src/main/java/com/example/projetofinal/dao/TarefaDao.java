package com.example.projetofinal.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetofinal.models.Tarefa;

import java.util.List;

@Dao
public interface TarefaDao {
    @Insert
    void insertAll(Tarefa... tarefas);
    @Update
    void updateTarefa(Tarefa tarefa);
    @Delete
    void delete(Tarefa tarefa);
    @Query("SELECT * FROM tarefa")
    List<Tarefa> getAllTarefas();

}