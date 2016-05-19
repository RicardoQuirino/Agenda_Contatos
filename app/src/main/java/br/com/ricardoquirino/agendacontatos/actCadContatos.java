package br.com.ricardoquirino.agendacontatos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.ricardoquirino.agendacontatos.Util.DateUtils;
import br.com.ricardoquirino.agendacontatos.app.MessageBox;
import br.com.ricardoquirino.agendacontatos.app.ViewHelper;
import br.com.ricardoquirino.agendacontatos.database.DataBase;
import br.com.ricardoquirino.agendacontatos.dominio.RepositorioContato;
import br.com.ricardoquirino.agendacontatos.dominio.entidades.Contato;

public class actCadContatos extends AppCompatActivity  {
    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private EditText edtEndereco;
    private EditText edtDatasEspeciais;
    private EditText edtGrupos;

    private Spinner spnTelefone;
    private Spinner spnEmail;
    private Spinner spnEndereco;
    private Spinner spnDatasEspeciais;

    private ArrayAdapter<String> adpSpnTelefone;
    private ArrayAdapter<String> adpSpnEmail;
    private ArrayAdapter<String> adpSpnEndereco;
    private ArrayAdapter<String> adpSpnDatasEspeciais;

    private DataBase dataBase;
    private SQLiteDatabase conexao;
    private RepositorioContato repositorioContato;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_contatos);

        edtNome           = (EditText)findViewById(R.id.edtNome);
        edtTelefone       = (EditText)findViewById(R.id.edtTelefone);
        edtEmail          = (EditText)findViewById(R.id.edtEmail);
        edtEndereco       = (EditText)findViewById(R.id.edtEndereco);
        edtDatasEspeciais = (EditText)findViewById(R.id.edtDatasEspeciais);
        edtGrupos         = (EditText)findViewById(R.id.edtGrupos);

        spnTelefone       = (Spinner)findViewById(R.id.spnTelefone);
        spnEmail          = (Spinner)findViewById(R.id.spnEmail);
        spnEndereco       = (Spinner)findViewById(R.id.spnEndereco);
        spnDatasEspeciais = (Spinner)findViewById(R.id.spnDatasEspeciais);

        adpSpnEmail = ViewHelper.createArrayAdapter(this,spnEmail);
        adpSpnTelefone = ViewHelper.createArrayAdapter(this,spnTelefone);
        adpSpnEndereco = ViewHelper.createArrayAdapter(this,spnEndereco);
        adpSpnDatasEspeciais = ViewHelper.createArrayAdapter(this,spnDatasEspeciais);

     /*   adpSpnTelefone = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adpSpnTelefone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpSpnEmail = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adpSpnEmail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpSpnEndereco = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpSpnTelefone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpSpnDatasEspeciais = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adpSpnDatasEspeciais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTelefone.setAdapter(adpSpnTelefone);
        spnEmail.setAdapter(adpSpnEmail);
        spnEndereco.setAdapter(adpSpnEndereco);
        spnDatasEspeciais.setAdapter(adpSpnDatasEspeciais); */

        adpSpnTelefone.add("Celular");
        adpSpnTelefone.add("Trabalho");
        adpSpnTelefone.add("casa");
        adpSpnTelefone.add("Recado");

        adpSpnEmail.add("Casa");
        adpSpnEmail.add("Trabalho");
        adpSpnEmail.add("Outros");

        adpSpnEndereco.add("Casa");
        adpSpnEndereco.add("Trabalho");
        adpSpnEndereco.add("Outros");

        adpSpnDatasEspeciais.add("Aniversario");
        adpSpnDatasEspeciais.add("Data Comemorativa");
        adpSpnDatasEspeciais.add("Outros");

        ExibeDataListener listener = new ExibeDataListener();

        edtDatasEspeciais.setOnClickListener(listener);
        edtDatasEspeciais.setOnFocusChangeListener(listener);
        edtDatasEspeciais.setKeyListener(null);

        Bundle bundle = getIntent().getExtras();

        if ((bundle != null)&& (bundle.containsKey(actContatos.PAR_CONTATO))){

            contato = (Contato)bundle.getSerializable(actContatos.PAR_CONTATO);
            preencheDados();

        }
        else
        contato = new Contato();


        try {
            dataBase = new DataBase(this);
            conexao = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(conexao);

        }catch (SQLException ex){

            MessageBox.show(this, "Erro", "Erro ao criar o banco: " + ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conexao != null){
            conexao.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_cad_contatos, menu);



        if (contato.getId()!= 0)
            menu.getItem(1).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case  R.id.mni_acao1:
                    salvar();
                    finish();
                break;


            case R.id.mni_acao2:
                    excluir();
                    finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void preencheDados(){

        edtNome.setText(contato.getNome());
        edtTelefone.setText(contato.getTelefone());
        edtEmail.setText(contato.getEmail());
        edtEndereco.setText(contato.getEndereco());

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format(contato.getDatasEspeciais());
        edtDatasEspeciais.setText(dt);

        edtGrupos.setText(contato.getGrupos());

        spnTelefone.setSelection(Integer.parseInt(contato.getTipoTelefone()));
        spnEmail.setSelection(Integer.parseInt(contato.getTipoEmail()));
        spnEndereco.setSelection(Integer.parseInt(contato.getTipoEndereco()));
        spnDatasEspeciais.setSelection(Integer.parseInt(contato.getTipoDatasEspeciais()));
    }

    private void excluir(){

        try {

            repositorioContato.excluir(contato.getId());

        }catch (Exception ex){

            MessageBox.show(this,"Erro","Erro ao excluir dados: " + ex.getMessage());
        }
    }

    private void salvar (){

        try{

        contato.setNome(edtNome.getText().toString());
        contato.setTelefone(edtTelefone.getText().toString());
        contato.setEmail(edtEmail.getText().toString());
        contato.setEndereco(edtEndereco.getText().toString());

        contato.setGrupos(edtGrupos.getText().toString());

        contato.setTipoTelefone(String.valueOf(spnTelefone.getSelectedItemPosition()));
        contato.setTipoEmail(String.valueOf(spnEmail.getSelectedItemPosition()));
        contato.setTipoEndereco(String.valueOf(spnEndereco.getSelectedItemPosition()));
        contato.setTipoDatasEspeciais(String.valueOf(spnDatasEspeciais.getSelectedItemPosition()));

            if (contato.getId()== 0 ){
                repositorioContato.inserir(contato);

            }else{
                repositorioContato.alterar(contato);

            }

        }catch (Exception ex){

            MessageBox.show(this,"Erro","Erro ao salvar os dados: " + ex.getMessage());

        }
    }

    private void exibeData(){

        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this,new SelecionaDataListener(),ano,mes,dia);
        dlg.show();

    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener{

        @Override
        public void onClick(View v) {
            exibeData();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
            exibeData();
        }
    }
    public class SelecionaDataListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String dt = DateUtils.dateToString(year, monthOfYear, dayOfMonth);
            Date data = DateUtils.getDate(year,monthOfYear,dayOfMonth);

            edtDatasEspeciais.setText(dt);

            contato.setDatasEspeciais(data);

        }
    }
}
