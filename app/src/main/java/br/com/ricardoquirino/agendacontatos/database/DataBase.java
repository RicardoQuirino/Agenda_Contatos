package br.com.ricardoquirino.agendacontatos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ricardo on 29/03/2016.
 */
public class DataBase extends SQLiteOpenHelper {
    public DataBase(Context context) {
        super(context, "AGENDA",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ScriptSQL.getCreateContato());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
