package com.projetos.redes.utilidades;

import android.util.Log;
import com.projetos.redes.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UtilidadeData {
    public static final String DATA_SEM_HORAS = "dd/MM/yy";
    private Calendar calendar;

    private UtilidadeData(){
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    public UtilidadeData(long tempo){
        this();
        calendar.setTimeInMillis(tempo);
    }

    public UtilidadeData(int dia, int mes, int ano) {
        this();
        calendar.set(ano, mes, dia);
    }

    public UtilidadeData(String data){
        // "10/05/21 2:32 da tarde
        this();
        if(data.contains("tarde") || data.contains("manhã")){
            formataDataParaDate(data);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                calendar.setTime(sdf.parse(data));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("UtilidadeDataErro:", "Erro ao processar a data.");
            }
        }
    }

    public int dia(){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int mes(){
        return calendar.get(Calendar.MONTH);
    }

    public int ano(){
        return calendar.get(Calendar.YEAR);
    }

    public int getHora(){
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public Date getDate(){
        return calendar.getTime();
    }

    public int getMinutos(){
        return calendar.get(Calendar.MINUTE);
    }

    public long dataEmMilisegundos() {

        return  calendar.getTimeInMillis();
    }

    public String toString(){
        String data = new SimpleDateFormat(Utils.PADRAO_DATA).format(calendar.getTime());
        return data;
    }

    public String pegarDataSemHoras(){
        return new SimpleDateFormat(DATA_SEM_HORAS).format(calendar.getTime());
    }

    public String pegarHorasDaData(){ return String.format("%d:00", getHora());}

    private void formataDataParaDate(String data){
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.PADRAO_DATA);
        String[] dates = data.split(" ");
        if(dates.length > 1) {
            String d = dates[0] + " " + dates[1];
            if(dates.length > 2)
                d += (dates[3].contains("manhã") ? " AM" : " PM");
            else
                d += " AM";
            try {
                d = d.replaceAll(",","");
                calendar.setTime(sdf.parse(d));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
    }

    public static String toDateString(long timeStamp){
        return new SimpleDateFormat(Utils.PADRAO_DATA).format(new Date(timeStamp));
    }
}
