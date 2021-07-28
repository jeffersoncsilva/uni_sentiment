package com.projetos.redes.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.projetos.redes.Utils;
import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.mailto.Mail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;

public class SendMailTask extends AsyncTask<Void, Void, Void> {
    private final String body;
    private final WeakReference<Context> mContext;

    public SendMailTask(Context context, String body){
        this.mContext = new WeakReference<>(context);
        this.body = body;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(isOnline()) {
            try {
                AssetManager am = mContext.get().getAssets();
                InputStream input = am.open("email.config.json");
                JSONObject obj = new JSONObject(convertStream(input));
                Mail m = new Mail(toStringArray(obj.getJSONArray("toArr")), getAssuntoEmail(),body, obj.getString("fromEmail"),
                        obj.getString("passwordEmail"),obj.getString("host"),obj.getString("port"),obj.getString("sport"));
                // emails de destino, assunto do email, corpo do email, email que esta sendo usado para envio, senha do email, host padrao do email,
                //porta que sera usada para envio email, porta socket padrao
                m.send();
            } catch (JSONException | RuntimeException | IOException  rex) {
                rex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        new BancoDeDados(mContext.get()).limparDadosUsuario();
        Toast.makeText(mContext.get(), "Mensagem enviada!", Toast.LENGTH_SHORT).show();
    }

    private String[] toStringArray(JSONArray array) throws JSONException{
        String[] arr = new String[array.length()];
        for(int i = 0; i < array.length(); i++)
            arr[i] = array.getString(i);
        return arr;
    }

    private String convertStream(InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bytesLidos;
        while((bytesLidos = in.read(buffer))!= -1){
            output.write(buffer, 0, bytesLidos);
        }
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private String getAssuntoEmail(){
        // Vira das preferencias do usuario onde será salvo o identificador do usuario.
        String assunto = "Lexico";
        try{
            SharedPreferences p = mContext.get().getSharedPreferences(Utils.CONFIG, Context.MODE_PRIVATE);
            assunto = p.getString(Utils.ID_USUARIO, "Lexico");
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return assunto;
    }

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.get().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        catch(Exception ex){
            Toast.makeText(mContext.get(), "Erro ao verificar se estava online! (" + ex.getMessage() + ")", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
