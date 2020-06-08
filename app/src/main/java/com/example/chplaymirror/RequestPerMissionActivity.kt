package com.example.chplaymirror

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.developer.kalert.KAlertDialog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.permission_activity.*


class RequestPerMissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission_activity)
        setFlagFullScreen()
        initAd()
        titleRequestPermission.setOnClickListener {
            requestPermission()
        }
    }

    /*Set full screen for waiting activity ui*/
    private fun setFlagFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }


    private fun initAd() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (isPermissionGranted()) {
            goToMirrorActivity()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@RequestPerMissionActivity,
            permissions,
            REQUEST_CODE_PERMISSION
        )
    }

    private fun isPermissionGranted(): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(
                this
                , it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                goToMirrorActivity()
            } else {
                setupPermissionGuideline(permissions.all { permission ->
                    isDeniedPermanently(permission)
                })
            }
        }
    }

    private fun setupPermissionGuideline(shouldOpenSetting: Boolean) {
        if (shouldOpenSetting) {
            val mDialog = KAlertDialog(this, KAlertDialog.WARNING_TYPE)
                .setTitleText("Request permission")
                .setContentText("Open Setting")
                .setConfirmText("Open")
                .setCancelText("Cancel")
            mDialog.setConfirmClickListener {
                openPermissionSettings()
                mDialog.dismiss()
            }
            mDialog.setCancelClickListener {
                mDialog.dismiss()
            }
            mDialog.show()
        } else {
            requestPermission()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun goToMirrorActivity() {
        startActivity(Intent(this, MirrorActivity::class.java))
    }

    companion object {
        const val REQUEST_CODE_PERMISSION = 0
        val permissions = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}
