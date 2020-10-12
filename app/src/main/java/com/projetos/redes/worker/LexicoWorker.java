package com.projetos.redes.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.projetos.redes.Lexico;
import com.projetos.redes.Utils;
import com.projetos.redes.bd.LexicoDb;

import java.text.DateFormat;
import java.util.Date;

public class LexicoWorker extends Worker {
    public static final String WORK_NAME = "lexico_work_execution";
    private final String tag = "LexicoWorker";
    private Context mContext;
    private LexicoDb db;
    private Lexico lexico;

    public LexicoWorker(@NonNull Context context, @NonNull WorkerParameters params){
        super(context, params);
        this.mContext = context;

        db = new LexicoDb(this.mContext);
        lexico= new Lexico(this.mContext);
        Log.d(tag, "LexicoWorker foi criado. ");
    }

    @NonNull
    @Override
    public Result doWork() {
        DateFormat df = Utils.getDateFormatter();
        Date d = new Date();
        Log.d(tag, "Executando o worker as: " + df.format(d));
        //long dRede = lexico.pegarDadosRede();
        //if(dRede < 0){
        //    Log.d(tag, "Dados de rede não existe. Não e possivel utilizar analizador lexico no momento.");
        //    return Result.failure();
       // }
        //Log.d(tag, "Executanto algoritmo lexico pelo worker manager.");
        lexico.executarLexico();
        Log.d(tag, "Classificando sentimentos do usuario pelo worker manager.");
        //lexico.classificaSentimentos(dRede);

        return Result.success();
    }
}
