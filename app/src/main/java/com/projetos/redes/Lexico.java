package com.projetos.redes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.models.LexicoResult;
import com.projetos.redes.models.UsrMsg;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Lexico {
    private static final String tag = "AnalisadorLexico";
    @SuppressLint("StaticFieldLeak")
    private static Lexico lexico = null;
    private Context context;
    LexicoDb db;

    public static Lexico getLexico(Context c){
        if(lexico== null){
            lexico = new Lexico(c);
        }
        return  lexico;
    }

    public Lexico(Context c) {
        this.context = c;
        db = new LexicoDb(c);
        //fileManipulation = FileManipulation.getFileManipulation(c);
    }
    
    /**
     * @author Jefferson C. Silva
     * @return SOMENTE USADO PARA TESTAR A LEITURA DOS ARQUIVOS DENTRO DE ASSETS. NÃO SERA UTILIZADO NO FINAL.
     * Essa função pega as menssagens que o usuario enviou que estão salvas em arquivo, realiza o processamento das menssagens
     * e salva o restultado compilando em intervalos de tempo de 1 hora o resultado do sentimento do usuario.
     */
    public String executarLexico(){
            Cursor cur_msg = db.getUserMessages();
            cur_msg.moveToFirst();
            int saldo, saldoSentenca;
            while(cur_msg.moveToNext()){
                saldo = 0;
                String msg = cur_msg.getString(cur_msg.getColumnIndexOrThrow(UsrMsg.CL_MSG));
                saldoSentenca = getSaldoSentenca(msg);
                String[] s = msg.split(" ");
                for(String  i : s)
                    saldo += getSaldoPalavra(i);
                LexicoResult lr = new LexicoResult();
                lr.setFrase(msg);
                lr.setData(getData());
                if(saldoSentenca == 1)
                    lr.setSentimento(Sentimento.POSITIVO);
                else if(saldoSentenca == -1)
                    lr.setSentimento(Sentimento.NEGATIVO);
                else if(saldo >= 0)
                    lr.setSentimento(Sentimento.POSITIVO);
                else
                    lr.setSentimento(Sentimento.NEGATIVO);

                LexicoDb.insertDb(lr, this.context);
            }
        db.apagarTbUsrMsg();
        cur_msg.close();
        return "Lexico processado!";
    }

    public void classificaSentimentos(){
        //select count(sentimento) from tb_lexico_result where sentimento = 'Positivo';
        //select count(sentimento) from tb_lexico_result where sentimento = 'Negativo';
        
    }

    /**
     * @author Leonardo Pereira - Adapdato para java/android por Jefferson C. Silva
     * @param sentenca sentença a ser pesquisada no arquivo base.
     * @return saldo da sentença. Retorna 0 caso ela não tenha sido encotrada.
     */
    private int getSaldoSentenca(String sentenca){
        Cursor c = db.getTableSaldoSentenca(sentenca);
        if(c != null && c.moveToFirst()) {
            int i = c.getInt(0);
            c.close();
            return i;
        }

        if (c != null) {
            c.close();
        }
        return 0;
    }

    private int getSaldoPalavra(String p){
        Cursor c = db.getSaldoPalavra(p);
        if(c != null && c.moveToFirst()) {
            int i = c.getInt(0);
            c.close();
            return i;
        }
        if (c != null) {
            c.close();
        }
        return 0;
    }

    private String getData(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault());
        return  sdf.format(new Date());
    }

}
