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

public class CadastrarTarefaFragment extends Fragment {

    private EditText et_Tarefa, et_Desc;

    private int dia, mes, ano;

    private int diaAgora, mesAgora, anoAgora;
    Button btn_Salvar, btn_Date;

    String valorParaTitulo, valorParaDesc;

    long valorParaData;

    Calendar calendario = Calendar.getInstance();


    public CadastrarTarefaFragment() {

    }

    private void abrirDialog()  {
        diaAgora = calendario.get(Calendar.DAY_OF_MONTH);
        mesAgora = calendario.get(Calendar.MONTH);
        anoAgora = calendario.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(getView().getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                String data_selecionada = (dayOfMonth + "/" + (month+1) + "/" + year);
                dia = dayOfMonth;
                mes = month;
                ano = year;
                calendario.set(ano, mes, dia); //Convertendo a data recebida do calendário para long
                btn_Date.setText(data_selecionada);

            }
        }, anoAgora, mesAgora, diaAgora);

        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_Salvar = getView().findViewById(R.id.btn_Salvar);
        btn_Date = getView().findViewById(R.id.btnDate);
        et_Tarefa = getView().findViewById(R.id.et_Tarefa);
        et_Desc = getView().findViewById(R.id.et_Desc);

        //Preciso alterar daqui pra baixo para enviar os dados para outro fragment ao invés de para uma activity
        //Vou utilizar Bundle para enviar os dados ao invés de Intent

        Bundle bundle = new Bundle();





        btn_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialog();
            }
        });

        btn_Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pego os valores dos EditText
                valorParaTitulo = et_Tarefa.getText().toString();
                valorParaDesc = et_Desc.getText().toString();
                valorParaData = calendario.getTimeInMillis(); //Esse valor será pegado da conversão da data no calendário para millisegundos


                //Verifica se algum dos campos está nulo
                if(valorParaDesc.equals("") || valorParaTitulo.equals("") || valorParaData == 0){
                    Toast.makeText(v.getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    //Verifica se algum dos campos está nulo
                    bundle.putString("valorPassadoTitulo", valorParaTitulo);
                    bundle.putString("valorPassadoDesc", valorParaDesc);
                    bundle.putLong("valorPassadoData", valorParaData);

                    //Crio uma nova instância do Fragmento Home, para onde os dados serão enviados
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setArguments(bundle);

                    //Substituo o fragmento CadastrarTarefa pelo Home
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, homeFragment);
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
        return inflater.inflate(R.layout.activity_cadastrar_tarefa, container, false);
    }
}
