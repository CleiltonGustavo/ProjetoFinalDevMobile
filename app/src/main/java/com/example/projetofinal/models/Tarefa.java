package com.example.projetofinal.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tarefa")
public class Tarefa {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "titulo")
    public String titulo;

    @ColumnInfo(name = "descricao")
    public String descricao;
    @ColumnInfo(name = "status")
    public boolean status;
    @ColumnInfo(name = "prazo")
    public long prazo;

    public int groupId;

    public Tarefa(String titulo, String descricao, long prazo){
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = true;
        this.prazo = prazo;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getPrazo () {
        return prazo;
    }

    public void setPrazo(long prazo) {
        this.prazo = prazo;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public String toString() {
        String nomeRetorno = this.titulo + " | " + this.descricao + " | " + this.status + " | " + this.prazo + " | " + this.groupId;
        return nomeRetorno;
    }
}
