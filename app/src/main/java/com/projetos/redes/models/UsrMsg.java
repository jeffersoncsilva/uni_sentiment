package com.projetos.redes.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UsrMsg extends Data {
    public static final String TB_USR_MSG = "usr_msg";
    public static final String CL_DATE = "date";
    public static final String CL_MSG = "msg";
    public static final String CL_ID = "id";
    public static final String SQL_CREATE_TB_USR = String.format("CREATE TABLE IF NOT EXISTS %s (%s TEXT, %s TEXT, %s INTEGER PRIMARY KEY AUTOINCREMENT);", TB_USR_MSG, CL_DATE, CL_MSG, CL_ID);
    public static final String SQL_SELECT_MGS = String.format("SELECT %s, %s FROM %s", CL_MSG, CL_DATE, TB_USR_MSG);


    private String date, msg;
    private int id;

    public UsrMsg(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsrMsg(String msg){
        this.msg = msg;
        this.date = getDay() + getHour();
    }

    /**
     * @author Jefferson C. Silva
     * @return retorna o data (dia-mes-ano) atual no formato especificado.
     */
    private static  String getDay(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    /**
     * @author Jefferson C. Silva
     * @return retorna a hora atual no formato especificado.
     */
    private static String getHour(){
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}
