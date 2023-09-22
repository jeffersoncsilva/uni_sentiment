package com.projetos.redes.task;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SendMailTask extends AsyncTask<Void, Void, Void> {
    private String body;
    private Context mContext;

    public SendMailTask(Context context, String body){
        this.mContext = context;
        this.body = body;
    }

    @Override
    protected Void doInBackground(Void... voids) {
//        if(isOnline()) {
//            try {
//                AssetManager am = mContext.getAssets();
//                InputStream input = am.open("email.config.json");
//                JSONObject obj = new JSONObject(convertStream(input));
//                Mail m = new Mail(toStringArray(obj.getJSONArray("toArr")), // emails de destino
//                        getAssuntoEmail(),               // assunto do email
//                        body,                                         // corpo do email
//                        obj.getString("fromEmail"),             // email que esta sendo usado para envio
//                        obj.getString("passwordEmail"),          // senha do email
//                        obj.getString("host"),                  // host padrao do email
//                        obj.getString("port"), //porta que sera usada para envio email
//                        obj.getString("sport") // porta socket padrao
//                );
//                m.send();
//            } catch (RuntimeException rex) {
//                rex.printStackTrace();
//            } catch (JSONException jsonEx) {
//                jsonEx.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(mContext, "Mensagem enviada!", Toast.LENGTH_SHORT).show();
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
        return new String(output.toByteArray(), "UTF-8");
    }

    private String getAssuntoEmail(){
        // Vira das preferencias do usuario onde será salvo o identificador do usuario.
        return "Assunto do Email";
    }

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        catch(Exception ex){
            Toast.makeText(mContext, "Erro ao verificar se estava online! (" + ex.getMessage() + ")", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
