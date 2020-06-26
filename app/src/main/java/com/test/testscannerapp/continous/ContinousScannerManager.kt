package com.test.testscannerapp.continous

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.webkit.URLUtil
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import java.util.*

class ContinousScannerManager(
    activity: Activity,
    val barcodeView: DecoratedBarcodeView,
    val scanResultTextView: TextView
) : LifecycleObserver {

    private var lastText = ""

    private val barcodeCallback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            result.let { barcodeResult ->
                if (barcodeResult.text == lastText) return
                lastText = barcodeResult.text
                scanResultTextView.text = barcodeResult.text
                beepManager?.playBeepSoundAndVibrate()
                if (URLUtil.isValidUrl(barcodeResult.text)) {
                    scanResultTextView.setTextColor(Color.BLUE)
                    scanResultTextView.setOnClickListener {
                        activity.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(barcodeResult.text)
                            )
                        )
                    }
                } else {
                    scanResultTextView.setTextColor(Color.BLACK)
                    scanResultTextView.setOnClickListener(null)
                }
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    private var beepManager: BeepManager? = null

    init {
        beepManager = BeepManager(activity)
        barcodeView.setStatusText("")
        val formats: Collection<BarcodeFormat> =
            Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.initializeFromIntent(activity.intent)
        barcodeView.decodeContinuous(barcodeCallback)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        resumeScanner()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        pauseScanner()
    }

    fun resumeScanner() {
        barcodeView.visibility = View.VISIBLE
        barcodeView.resume()
    }

    fun pauseScanner() {
        barcodeView.pause()
    }
}