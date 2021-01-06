package com.projetos.redes.utilidades;

import com.projetos.redes.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {

    private Date date;

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
        String str =new SimpleDateFormat("dd/MM/yy hh:mm a").format(date);
        return str;
    }

    private Date formataDataParaDate(String data){
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.PADRAO_DATA);
        Date dt = null;
        try {
            dt = sdf.parse(data);
        } catch (ParseException pe) { }
        if(dt != null) return dt;

        String[] dates = data.split(" ");
        if(dates.length > 1) {
            String d = dates[0] + " " + dates[1];
            d += (dates[2].contains("da manha") ? " AM" : " PM") ;
            try {
                dt = sdf.parse(d);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return dt;
    }
}
