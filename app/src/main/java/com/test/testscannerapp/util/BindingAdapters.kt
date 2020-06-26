package com.test.testscannerapp.util

import android.widget.Button
import androidx.databinding.BindingAdapter
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView


class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("bind:isScanRunning")
        fun setScanRunning(barcodeView: DecoratedBarcodeView, isRunning: Boolean) {
            if (isRunning) {
                barcodeView.resume()
            } else barcodeView.pause()
        }

        @JvmStatic
        @BindingAdapter("bind:scan", "bind:onScanned", requireAll = false)
        fun scan(
            scanButton: Button,
            barcodeView: DecoratedBarcodeView,
            scanBindingCallback: ScanBindingCallback
        ) {
            scanButton.setOnClickListener {
                barcodeView.resume()
                barcodeView.decodeSingle(object : BarcodeCallback {
                    override fun barcodeResult(result: BarcodeResult?) {

                        /*
                        there is no way to get a scan error callback :(
                         */

                        if (result != null) {
                            scanBindingCallback.onScanned(ScanResult.Success(result.text))
                        } else scanBindingCallback.onScanned(ScanResult.Error)
                    }
                    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
                })
            }
        }

        interface ScanBindingCallback {
            fun onScanned(result: ScanResult)
        }
    }
}
