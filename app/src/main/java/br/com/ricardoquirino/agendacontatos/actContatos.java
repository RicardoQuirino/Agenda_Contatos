package br.com.ricardoquirino.agendacontatos;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import br.com.ricardoquirino.agendacontatos.app.MessageBox;
import br.com.ricardoquirino.agendacontatos.database.DataBase;
import br.com.ricardoquirino.agendacontatos.dominio.RepositorioContato;
import br.com.ricardoquirino.agendacontatos.dominio.entidades.Contato;

public class actContatos extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageButton btnAdicionar;
    private EditText edtPesquisa;
    private ListView lstContatos;
    private ArrayAdapter<Contato> adpContatos;
    private FiltraDados filtraDados;

    private DataBase dataBase;
    private SQLiteDatabase conexao;
    private RepositorioContato repositorioContato;

    public static final String PAR_CONTATO = "CONTATO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contatos);

        btnAdicionar = (ImageButton)findViewById(R.id.btnAdicionar);
        edtPesquisa = (EditText)findViewById(R.id.edtPesquisa);
        lstContatos = (ListView)findViewById(R.id.lstContatos);

        btnAdicionar.setOnClickListener(this);
        lstContatos.setOnItemClickListener(this);

        try {



            dataBase = new DataBase(this);
            conexao = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(conexao);

            adpContatos = repositorioContato.buscaContatos(this);

            lstContatos.setAdapter(adpContatos);

            filtraDados = new FiltraDados(adpContatos);
            edtPesquisa.addTextChangedListener(filtraDados);


        }catch (SQLException ex){

            MessageBox.show(this,"Erro","Erro ao criar o banco: " + ex.getMessage());
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
    public void onClick(View v) {

        Intent intent = new Intent(this,actCadContatos.class);
        startActivityForResult(intent,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        adpContatos = repositorioContato.buscaContatos(this);
        filtraDados.setArrayAdapter(adpContatos);

        lstContatos.setAdapter(adpContatos);

        //super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Contato contato = adpContatos.getItem(position);

        Intent intent = new Intent(this,actCadContatos.class);
        intent.putExtra(PAR_CONTATO,contato);

        startActivityForResult(intent,0);


    }

    public class  FiltraDados implements TextWatcher{
        private ArrayAdapter<Contato> arrayAdapter;

        private FiltraDados(ArrayAdapter<Contato> arrayAdapter){
        this.arrayAdapter = arrayAdapter;

        }

        public void setArrayAdapter(ArrayAdapter<Contato> arrayAdapter){
            this.arrayAdapter = arrayAdapter;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            arrayAdapter.getFilter().filter(s);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
