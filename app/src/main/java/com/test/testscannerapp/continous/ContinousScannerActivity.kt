package com.test.testscannerapp.continous

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.testscannerapp.R
import kotlinx.android.synthetic.main.activity_continous_scanner.*

const val PERMISSION_REQUEST_CAMERA = 21

class AlternativeScannerActivity : AppCompatActivity() {

    lateinit var scannerManager: ContinousScannerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continous_scanner)

        requestPermission()
    }

    fun initScanner() {
        scannerManager = ContinousScannerManager(this, barcode_view, scan_result)
        lifecycle.addObserver(scannerManager)
    }

    fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            this.checkSelfPermission(Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CAMERA
            )
        } else {
            initScanner()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScanner()
            }
        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(scannerManager)
        super.onDestroy()
    }
}