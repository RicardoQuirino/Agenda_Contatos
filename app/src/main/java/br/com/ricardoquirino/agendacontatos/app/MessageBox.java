package br.com.ricardoquirino.agendacontatos.app;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Ricardo on 01/04/2016.
 */
public class MessageBox {

    public static void showInfo(Context ctx, String title, String msg){
        show(ctx, title, msg, android.R.drawable.ic_dialog_info);
    }

    public static void showAlert(Context ctx, String title, String msg){
        show(ctx, title, msg, android.R.drawable.ic_dialog_alert);
    }

    public static void show(Context ctx,String title,String msg){
        show(ctx, title, msg,0);
    }


    public static void show(Context ctx, String title, String msg, int iconId){

        AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
        dlg.setIcon(iconId);
        dlg.setMessage(title);
        dlg.setMessage(msg);
        dlg.setNeutralButton("ok",null);
        dlg.show();

    }
}
