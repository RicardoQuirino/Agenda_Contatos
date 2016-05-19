package br.com.ricardoquirino.agendacontatos.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.Date;

import br.com.ricardoquirino.agendacontatos.ContatoArrayAdapter;
import br.com.ricardoquirino.agendacontatos.R;
import br.com.ricardoquirino.agendacontatos.dominio.entidades.Contato;

/**
 * Created by Ricardo on 30/03/2016.
 */
public class RepositorioContato {

    private SQLiteDatabase conexao;

    public RepositorioContato(SQLiteDatabase conexao){

        this.conexao = conexao;
    }

    private ContentValues preencheContentValues(Contato contato){

        ContentValues values = new ContentValues();
        values.put(Contato.NOME,contato.getNome());
        values.put(Contato.TELEFONE, contato.getTelefone());
        values.put(Contato.TIPOTELEFONE, contato.getTipoTelefone());
        values.put(Contato.EMAIL, contato.getEmail());
        values.put(Contato.TIPOEMAIL, contato.getTipoEmail());
        values.put(Contato.ENDERECO, contato.getEndereco());
        values.put(Contato.TIPOENDERECO, contato.getTipoEndereco());
        values.put(Contato.DATASESPECIAIS, contato.getDatasEspeciais().getTime());
        values.put(Contato.TIPODATASESPECIAIS, contato.getTipoDatasEspeciais());
        values.put(Contato.GRUPOS,contato.getGrupos());

        return values;


    }

    public void excluir(long id){

        conexao.delete(Contato.TABELA,"_id = ? ", new String[]{String.valueOf(id)});
    }

    public void alterar(Contato contato){

        ContentValues values = preencheContentValues(contato);
        conexao.update(Contato.TABELA, values, "_id = ? ", new String[]{String.valueOf(contato.getId())});
    }


    public void inserir(Contato contato){


        ContentValues values = preencheContentValues(contato);
        conexao.insertOrThrow(Contato.TABELA, null, values);
    }

    public ContatoArrayAdapter buscaContatos(Context context){

        ContatoArrayAdapter adpContatos = new ContatoArrayAdapter(context, R.layout.item_contato);

        Cursor cursor = conexao.query(Contato.TABELA, null, null, null, null, null, null);

        if (cursor.getCount()> 0){

                cursor.moveToFirst();
           do {

               Contato contato = new Contato();

               contato.setId(cursor.getLong(cursor.getColumnIndex(contato.ID)));
               contato.setNome(cursor.getString(cursor.getColumnIndex(contato.NOME)));
               contato.setTelefone(cursor.getString(cursor.getColumnIndex(contato.TELEFONE)));
               contato.setTipoTelefone(cursor.getString(cursor.getColumnIndex(contato.TIPOTELEFONE)));
               contato.setEmail(cursor.getString(cursor.getColumnIndex(contato.EMAIL)));
               contato.setTipoEmail(cursor.getString(cursor.getColumnIndex(contato.TIPOEMAIL)));
               contato.setEndereco(cursor.getString(cursor.getColumnIndex(contato.ENDERECO)));
               contato.setTipoEndereco(cursor.getString(cursor.getColumnIndex(contato.TIPOENDERECO)));
               contato.setDatasEspeciais(new Date(cursor.getLong(cursor.getColumnIndex(contato.DATASESPECIAIS))));
               contato.setTipoDatasEspeciais(cursor.getString(cursor.getColumnIndex(contato.TIPODATASESPECIAIS)));
               contato.setGrupos(cursor.getString(cursor.getColumnIndex(contato.GRUPOS)));
               adpContatos.add(contato);

           }while (cursor.moveToNext());

            }

        return adpContatos;
    }
}
