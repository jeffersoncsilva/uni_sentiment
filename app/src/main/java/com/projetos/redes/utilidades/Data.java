package com.projetos.redes.utilidades;

import com.projetos.redes.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    public static final String DATA_SEM_HORAS = "dd/MM/yy";
    private final Date date;

    public Data(long tempo){
        this.date = new Date(tempo);
    }

    public Data(Date d){
        this.date = d;
    }

    public Data(String data){
        this.date = formataDataParaDate(data);
    }

    public long dataEmMilisegundos() {
        return  date.getTime();
    }

    public String toString(){
        return new SimpleDateFormat("dd/MM/yy hh:mm a").format(date);
    }

    public String pegarDataSemHoras(){
        return new SimpleDateFormat(DATA_SEM_HORAS).format(date);
    }

    public String pegarHorasDaData(){ return date.getHours() + "";}

    private Date formataDataParaDate(String data){
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.PADRAO_DATA);
        Date dt = null;
        try {
            dt = sdf.parse(data);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        if(dt != null) return dt;

        String[] dates = data.split(" ");
        if(dates.length > 1) {
            String d = dates[0] + " " + dates[1];
            if(dates.length < 2)
                d += (dates[2].contains("da manha") ? " AM" : " PM") ;
            else
                d += " AM";
            try {
                d = d.replaceAll(",","");
                dt = sdf.parse(d);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return dt;
    }
}
