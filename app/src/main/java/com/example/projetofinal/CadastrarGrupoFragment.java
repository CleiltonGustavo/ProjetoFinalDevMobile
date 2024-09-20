package com.example.projetofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.projetofinal.dao.TarefaDao;
import com.example.projetofinal.database.AppDataBase;
import com.example.projetofinal.models.Tarefa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CadastrarGrupoFragment extends Fragment {

    private EditText et_Grupo, et_DescGrupo;
    Button btn_SalvarGrupos;



    public CadastrarGrupoFragment() {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_SalvarGrupos = getView().findViewById(R.id.btn_SalvarGrupo);
        et_Grupo = getView().findViewById(R.id.et_Grupo);
        et_DescGrupo = getView().findViewById(R.id.et_DescGrupo);

        Bundle bundle_grupo = new Bundle();

        btn_SalvarGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pego os valores dos EditText
                String valorParaTituloGrupo = et_Grupo.getText().toString();
                String valorParaDescGrupo = et_DescGrupo.getText().toString();

                if (valorParaDescGrupo.equals("") || valorParaTituloGrupo.equals("")){
                    Toast.makeText(v.getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    //Passo os valores para o meu bundle
                    bundle_grupo.putString("valorPassadoTituloGrupo", valorParaTituloGrupo);
                    bundle_grupo.putString("valorPassadoDescGrupo", valorParaDescGrupo);

                    //Crio uma nova instância do Fragmento Grupos, para onde os dados serão enviados
                    GruposFragment gruposFragment = new GruposFragment();
                    gruposFragment.setArguments(bundle_grupo);

                    //Substituo o fragmento CadastrarGrupo pelo Grupos
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, gruposFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*getContext().getTheme().applyStyle(com.google.android.material.R.style.Theme_AppCompat_DayNight_NoActionBar, true);*/
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cadastrar_grupo, container, false);
    }
}
