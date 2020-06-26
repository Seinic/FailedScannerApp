package com.test.testscannerapp.ui.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.testscannerapp.R
import com.test.testscannerapp.databinding.FragmentScannerBinding
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_scanner.*


class ScannerFragment : Fragment() {
    private val viewModel: ScannerViewModel by viewModels()
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentScannerBinding.inflate(inflater, container, false)
        lifecycle.addObserver(viewModel)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        disposable.add(viewModel.publishSubject.subscribe {
            when (it) {
                is ScannerViewModel.Event.OnScanSuccess -> handleScanResult(it.scanResult)
                is ScannerViewModel.Event.OnScanError -> handleScanError()
            }
        })
    }

    fun handleScanResult(scanResult: String) {
        println("Scan result $scanResult") //TODO navigation to result page
    }

    fun handleScanError() {
        Toast.makeText(activity, getString(R.string.scan_error_message), Toast.LENGTH_SHORT).show()
    }
}


