package com.projetos.redes.worker;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.projetos.redes.Lexico;
import com.projetos.redes.Utils;
import java.text.DateFormat;
import java.util.Date;

public class LexicoWorker extends Worker {
    public static final String WORK_NAME = "lexico_work_execution";
    public static final String TAG_WORKER = "WRK_APP_R";
    private final String tag = "LexicoWorker";
    private Context mContext;
    private Lexico lexico;

    public LexicoWorker(@NonNull Context context, @NonNull WorkerParameters params){
        super(context, params);
        this.mContext = context;
        lexico = new Lexico(this.mContext);
    }

    /**
     * Quando o worker e criado, ele realiza as operações referentes ao lexico. Sendo:
     * Pega os dados da rede
     * Executa o lexico, classificando os sentimentos quanto cada frase digitada.
     * Classifica o sentimento e relaciona os dados que foram usados no intervalo de tempo.
     * @return
     */
    @NonNull
    @Override
    public Result doWork() {
        /*E retornado um vetor de 3 posições, onde:
          dado[0]: hora de inicio do intervalo.
          dado[1]: Hora de fim do intervalo.
          dado[2]: dados wifi.
          dado[3]: dado mobile.
         */
        long[] dados = lexico.pegarDadosRede();
        lexico.executarLexico();
        lexico.classificaSentimentos(dados);
        Log.d(tag, "Coleta de dados, algoritmo lexico e classficador executado.");
        return Result.success();
    }

    public static boolean WorkActive(Context con){
        return true;
    }

    public static void stopWorker(Context con){
        WorkManager.getInstance(con).cancelAllWorkByTag(TAG_WORKER);
    }
}
