package com.example.projetofinal.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetofinal.models.Grupo;

import java.util.List;

@Dao
public interface GrupoDao {
    @Insert
    void insertAll(Grupo... grupos);
    @Update
    void updateGrupo(Grupo grupo);
    @Delete
    void delete(Grupo grupo);
    @Query("SELECT * FROM grupo")
    List<Grupo> getAllGrupos();
}
