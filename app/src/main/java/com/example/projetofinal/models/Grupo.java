package com.example.projetofinal.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "grupo")
public class Grupo {
    @PrimaryKey(autoGenerate = true)
    public int gid;

    @ColumnInfo(name = "nomeGrupo")
    public String nomeGrupo;

    @ColumnInfo(name = "descricaoGrupo")
    public String descricaoGrupo;

    @ColumnInfo(name = "tamanhoGrupo")
    public int tamanhoGrupo;

    public Grupo(String nomeGrupo, String descricaoGrupo) {
        this.nomeGrupo = nomeGrupo;
        this.descricaoGrupo = descricaoGrupo;
        this.tamanhoGrupo = 0;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public String getDescricaoGrupo() {
        return descricaoGrupo;
    }

    public void setDescricaoGrupo(String descricaoGrupo) {
        this.descricaoGrupo = descricaoGrupo;
    }

    public int getTamanhoGrupo() {
        return tamanhoGrupo;
    }

    public void setTamanhoGrupo(int tamanhoGrupo) {
        this.tamanhoGrupo = tamanhoGrupo;
    }


    @NonNull
    @Override
    public String toString() {
        String grupoRetorno = this.nomeGrupo + " | " + this.tamanhoGrupo + " | " + this.gid;
        return grupoRetorno;
    }
}
