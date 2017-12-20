package com.example.yjp.baiduspeechdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.widget.Toast

import com.baidu.speech.VoiceRecognitionService

class SpeechRecognizerTool(private val mContext: Context) : RecognitionListener {

    private var mSpeechRecognizer: SpeechRecognizer? = null

    private var mResultsCallback: ResultsCallback? = null

    interface ResultsCallback {
        fun onResults(result: String)
    }

    @Synchronized
    fun createTool() {
        if (null == mSpeechRecognizer) {

            // 创建识别器
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext,
                    ComponentName(mContext, VoiceRecognitionService::class.java))

            // 注册监听器
            mSpeechRecognizer!!.setRecognitionListener(this)
        }
    }

    @Synchronized
    fun destroyTool() {
        mSpeechRecognizer!!.stopListening()
        mSpeechRecognizer!!.destroy()
        mSpeechRecognizer = null
    }

    // 开始识别
    fun startASR(callback: ResultsCallback) {
        mResultsCallback = callback

        val intent = Intent()
        bindParams(intent)
        mSpeechRecognizer!!.startListening(intent)
    }

    //停止识别
    fun stopASR() {
        mSpeechRecognizer!!.stopListening()
    }

    private fun bindParams(intent: Intent) {
        // 设置识别参数
    }

    override fun onReadyForSpeech(params: Bundle) {
        // 准备就绪
        Toast.makeText(mContext, "请开始说话", Toast.LENGTH_SHORT).show()
    }

    override fun onBeginningOfSpeech() {
        // 开始说话处理
    }

    override fun onRmsChanged(rmsdB: Float) {
        // 音量变化处理
    }

    override fun onBufferReceived(buffer: ByteArray) {
        // 录音数据传出处理
    }

    override fun onEndOfSpeech() {
        // 说话结束处理
    }

    override fun onError(error: Int) {}

    override fun onResults(results: Bundle) {

        // 最终结果处理
        if (mResultsCallback != null) {
            val text = results.get(SpeechRecognizer.RESULTS_RECOGNITION)!!
                    .toString().replace("]", "").replace("[", "")
            mResultsCallback!!.onResults(text)
        }
    }

    override fun onPartialResults(partialResults: Bundle) {
        // 临时结果处理
    }

    override fun onEvent(eventType: Int, params: Bundle) {}
}