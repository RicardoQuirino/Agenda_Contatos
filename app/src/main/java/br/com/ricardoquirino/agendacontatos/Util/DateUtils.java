package br.com.ricardoquirino.agendacontatos.Util;

import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ricardo on 05/04/2016.
 */
public class DateUtils {

    public static Date getDate(int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date data = calendar.getTime();
        return data;
    }

    public static String dateToString(int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date data = calendar.getTime();

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format(data);

        return dt;
    }

    public static String dateToString(Date date) {

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format(date);
        return dt;
    }
}