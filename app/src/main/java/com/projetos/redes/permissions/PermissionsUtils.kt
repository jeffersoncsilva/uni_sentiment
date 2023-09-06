package com.projetos.redes.permissions

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat

val REQUEST_CODE_PERMISSION = 22

fun temPermissaoAcessoDadosTelefone(context: Context?) : Boolean{
        if(context == null)
                return false
        val appOps : AppOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOp(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName)
        } else {
                appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName)
        }
        return mode == MODE_ALLOWED
}

fun temPermissaoAcessoDadosRedeMovel(context: Context?) : Boolean{
        if(context == null)
                return false
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
}

fun solicitaPermissaoAcessoRedeMovel(con: Context?){
        con?.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
}


