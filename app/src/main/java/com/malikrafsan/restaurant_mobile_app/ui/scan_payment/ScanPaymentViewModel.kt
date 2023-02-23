package com.malikrafsan.restaurant_mobile_app.ui.scan_payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.event.CartEvent
import com.malikrafsan.restaurant_mobile_app.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanPaymentViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {
    val carts = repository.getAll()

    suspend fun clearCart() {
        repository.deleteAll()
    }
}