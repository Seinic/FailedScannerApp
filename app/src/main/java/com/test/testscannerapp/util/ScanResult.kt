package com.test.testscannerapp.util

sealed class ScanResult {
    data class Success(val resultValue: String): ScanResult()
    object Error: ScanResult()
}