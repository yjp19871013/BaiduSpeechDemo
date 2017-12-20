package com.example.yjp.baiduspeechdemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SpeechRecognizerTool.ResultsCallback {

    private val mSpeechRecognizerTool = SpeechRecognizerTool(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermission()

        startSpeechButton.setOnTouchListener {
            view, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    mSpeechRecognizerTool.startASR(this@MainActivity)
                }
                MotionEvent.ACTION_UP -> {
                    mSpeechRecognizerTool.stopASR()
                }
                else -> return@setOnTouchListener false
            }

            return@setOnTouchListener true
        }
    }

    override fun onStart() {
        super.onStart()
        mSpeechRecognizerTool.createTool()
    }

    override fun onStop() {
        super.onStop()
        mSpeechRecognizerTool.destroyTool()
    }

    override fun onResults(result: String) {
        this@MainActivity.runOnUiThread { speechTextView!!.text = result }
    }

    private fun initPermission() {
        val permissions = arrayOf<String>(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val toApplyList = ArrayList<String>()

        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm)
                //进入到这里代表没有权限.

            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123)
        }
    }
}
