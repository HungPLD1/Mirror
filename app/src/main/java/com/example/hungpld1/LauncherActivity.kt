package com.example.hungpld1

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.content.ContextCompat

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcher_activity)
        setFlagFullScreen()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            if (isPermissionGranted()){
                finish()
                startActivity(Intent(this,MirrorActivity::class.java))
            }else{
                finish()
                startActivity(Intent(this,RequestPerMissionActivity::class.java))
            }
        }, 1000)
    }


    private fun isPermissionGranted(): Boolean {
        return RequestPerMissionActivity.permissions.all {
            ContextCompat.checkSelfPermission(
                this
                , it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /*Set full screen for waiting activity ui*/
    private fun setFlagFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

}
