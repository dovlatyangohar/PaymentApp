package com.example.paymentapp.di

import com.example.paymentapp.fragment.payment.PaymentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PaymentViewModel() }
}