package com.example.caloriestracker.ui.user.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }

    val text: LiveData<String> = _text

    // Anda dapat menambahkan properti LiveData lainnya sesuai kebutuhan
    private val _targetKalori = MutableLiveData<Double>()
    val targetKalori: LiveData<Double> = _targetKalori

    // Fungsi untuk mengubah nilai _targetKalori
    fun setTargetKalori(value: Double) {
        _targetKalori.value = value
    }

    private val _sisaKalori = MutableLiveData<String>()
    val sisaKalori: LiveData<String> get() = _sisaKalori

    private val _konsumsiKalori = MutableLiveData<String>()
    val konsumsiKalori: LiveData<String> get() = _konsumsiKalori

    fun setSisaKalori(value: String) {
        _sisaKalori.value = value
    }

    // Fungsi untuk mengatur nilai konsumsi kalori
    fun setKonsumsiKalori(value: String) {
        _konsumsiKalori.value = value
    }

    // Fungsi untuk mengatur nilai target kalori
    fun setTargetKalori(value: Double?) {
        _targetKalori.value = value
    }
}
