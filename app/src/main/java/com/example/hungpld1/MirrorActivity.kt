package com.example.hungpld1

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import kotlinx.android.synthetic.main.mirror_activity.*
import kotlinx.android.synthetic.main.mirror_activity.adView

class MirrorActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var isLight = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeItFullScreen()
        setContentView(R.layout.mirror_activity)
        setLightParam(30F)

        initSurface()
        initFeatureZoom()
        initFeatureLight()
        initAd()
    }

    private fun initAd() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Log.e(TAG, "onAdFailedToLoad: $errorCode")
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
        if (isPermissionGranted()){
            initCamera()
            camera?.startPreview()
        }else{
            startActivity(Intent(this,RequestPerMissionActivity::class.java))
            finish()
        }
    }

    private fun initFeatureLight() {
        imgLight.setOnClickListener {
            if (isLight) {
                imgLight.setImageResource(R.drawable.light_off)
                isLight = false
                setLightParam(30F)
            } else {
                imgLight.setImageResource(R.drawable.light_on)
                isLight = true
                setLightParam(100F)
            }
        }
    }

    private fun initFeatureZoom() {
        sbZoom.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                try {
                    val params = camera?.parameters
                    params?.zoom = seekParams.progress
                    Log.e(TAG,seekParams.progress.toString())
                    camera!!.parameters = params
                    camera!!.startPreview()
                }catch (e : java.lang.Exception){
                    Toast.makeText(this@MirrorActivity,e.printStackTrace().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }
    }

    private fun initSurface() {
        surfaceHolder = mirror?.holder
        surfaceHolder?.addCallback(this)
    }

    private fun makeItFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        //Changing SurfaceView background color
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val videoLayoutParams = mirror?.layoutParams
        videoLayoutParams?.width = displayMetrics.widthPixels
        videoLayoutParams?.height = displayMetrics.heightPixels
        val videoParams = mirror?.layoutParams
        videoParams?.width = displayMetrics.widthPixels
        videoParams?.height = displayMetrics.heightPixels
    }

    private fun initCamera() {
        try {
            openFrontFacingCameraGingerbread()
        } catch (e: RuntimeException) {
            Log.e(TAG, "init_camera: $e")
            return
        }

    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera?.stopPreview()
        camera?.release()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        try {
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(holder)
        } catch (e: Exception) {
            Log.e(TAG, "init_camera: $e")
            return
        }
    }

    private fun openFrontFacingCameraGingerbread() {
        var cameraCount = 0
        camera = null
        val cameraInfo = CameraInfo()
        cameraCount = Camera.getNumberOfCameras()
        for (camIdx in 0 until cameraCount) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    camera = Camera.open(camIdx)
                } catch (e: java.lang.RuntimeException) {
                    Log.e(
                        TAG,
                        "Camera failed to open: " + e.localizedMessage
                    )
                }
            }
        }
    }

    private fun setLightParam(lightValue: Float) {
        val backLightValue = lightValue / 100

        val layoutParams = window.attributes // Get Params

        layoutParams.screenBrightness = backLightValue // Set Value

        window.attributes = layoutParams
    }

    override fun onBackPressed() {
        finish()
    }

    private fun isPermissionGranted(): Boolean {
        return RequestPerMissionActivity.permissions.all {
            ContextCompat.checkSelfPermission(
                this
                , it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        const val TAG = "LOL"
    }
}