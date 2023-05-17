package com.example.paymentapp.fragment.payment

import androidx.lifecycle.viewModelScope
import com.example.paymentapp.base.BaseViewModel
import com.example.paymentapp.utils.CardUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class PaymentViewModel : BaseViewModel() {
    private val _cardNumberValidation: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val cardNumberValidation = _cardNumberValidation.asSharedFlow()

    private val _cardNameValidation: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val cardNameValidation = _cardNameValidation.asSharedFlow()

    private val _cardCVCValidation: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val cardCVCValidation = _cardCVCValidation.asSharedFlow()

    private val _cardExpDateValidation: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val cardExpDateValidation = _cardExpDateValidation.asSharedFlow()

    private val _cardFullValidation: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val cardFullValidation = _cardFullValidation.asSharedFlow()

    var randAmount = 0

    init {
       randAmount = generateRandomInt()
    }
    fun generateRandomInt(): Int {
        return Random.nextInt(1, 1001)
    }

    fun validateCardNumber(cardNumber: Long) {
        viewModelScope.launch {
            _cardNumberValidation.emit(CardUtil.isValidCardNumber(cardNumber))
        }
    }

    fun validateName(cardName: String) {
        viewModelScope.launch {
            _cardNameValidation.emit(CardUtil.isCardNameValid(cardName))
        }
    }

    fun validateExpDate(expDate: String) {
        viewModelScope.launch {
            _cardExpDateValidation.emit(CardUtil.validateCardExpirationDate(expDate))
        }
    }

    fun validateCVC(cardCVC: Int) {
        viewModelScope.launch {
            _cardCVCValidation.emit(CardUtil.isValidCVC(cardCVC))
        }
    }

    fun validateFullCard(cardNumber: Long, cvc: Int, expDate: String) {
        viewModelScope.launch {
            _cardFullValidation.emit(CardUtil.isValidCard(cardNumber, cvc, expDate))
        }
    }

}