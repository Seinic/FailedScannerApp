package com.test.testscannerapp.ui.scanner

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.test.testscannerapp.util.ScanResult
import io.reactivex.subjects.PublishSubject

class ScannerViewModel: ViewModel(), LifecycleObserver {
    val publishSubject: PublishSubject<Event> = PublishSubject.create()
    val isScannerRunning = ObservableBoolean()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        resumeScanner()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        pauseScanner()
    }

    fun onScanned(scanResult: ScanResult){
        pauseScanner()
        when (scanResult){
            is ScanResult.Success -> handleScanSuccess(scanResult.resultValue)
            is ScanResult.Error -> handleScanError()
        }
    }

    fun handleScanSuccess(scanResult: String){
        publishSubject.onNext(Event.OnScanSuccess(scanResult))
    }

    fun handleScanError(){
        publishSubject.onNext(Event.OnScanError)
    }

    fun resumeScanner(){
        isScannerRunning.set(true)
    }

    fun pauseScanner(){
        isScannerRunning.set(false)
    }

    sealed class Event{
        data class OnScanSuccess(val scanResult: String) : Event()
        object OnScanError: Event()
    }
}