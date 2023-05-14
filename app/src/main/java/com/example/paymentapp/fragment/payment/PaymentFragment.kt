package com.example.paymentapp.fragment.payment

import androidx.core.view.isInvisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.example.paymentapp.R
import com.example.paymentapp.base.BaseFragment
import com.example.paymentapp.databinding.FragmentPaymentBinding
import com.example.paymentapp.utils.settings.CardNumberTextWatcher
import com.example.paymentapp.utils.viewBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentFragment : BaseFragment<PaymentViewModel, FragmentPaymentBinding>() {
    override val viewModel: PaymentViewModel by viewModel()
    override val binding: FragmentPaymentBinding by viewBinding()

    override fun onView() {
        super.onView()
        with(binding) {
            paymentAmountTv.text =
                String.format("%s %d$", getString(R.string.amount), viewModel.generateRandomInt())

            cardNumberEt.addTextChangedListener(CardNumberTextWatcher(cardNumberEt))
            cardNumberEt.doAfterTextChanged {
                cardNumberErrorMessage.isInvisible = true
            }
            nameEt.doAfterTextChanged {
                nameErrorMessage.isInvisible = true
            }
            expirationDateEt.doAfterTextChanged {
                expirationDateErrorMessage.isInvisible = true
            }
            cardCvcEt.doAfterTextChanged {
                cvcCodeErrorMessage.isInvisible = true
            }


        }
    }


    override fun onViewClick() {
        super.onViewClick()
        with(binding) {
            expirationDateEt.apply {
                doOnTextChanged { text, start, before, _ ->
                    if (text?.length == MONTH_DATE_DIGITS_COUNT) {
                        if (start == EXPIRE_DATE_START_POSITION && before == EXPIRE_DATE_BEFORE_POSITION && !text.toString()
                                .contains("/")
                        ) {
                            setText(
                                text.toString()
                                    .substring(MONTH_DATE_START_POSITION, MONTH_DATE_END_POSITION)
                            )
                            setSelection(EXPIRE_DATE_SET_SELECTION_CASE_1)
                        } else {
                            setText("$text/")
                            setSelection(EXPIRE_DATE_SET_SELECTION_CASE_3)
                        }
                    }
                }
            }

            payButton.setOnClickListener {
                with(viewModel) {
                    if (cardNumberEt.text.toString().isNotEmpty()) {
                        validateCardNumber(cardNumberEt.text.toString().replace(" ", "").toLong())
                    } else {
                        cardNumberErrorMessage.isInvisible = false
                    }
                    validateName(nameEt.text.toString())
                    validateExpDate(expirationDateEt.text.toString())
                    if (cardCvcEt.text.toString().isNotEmpty()) {
                        validateCVC(cardCvcEt.text.toString().toInt())
                    } else {
                        cvcCodeErrorMessage.isInvisible = false
                    }
                    if (cardNumberEt.text.toString().isNotEmpty() && cardCvcEt.text.toString()
                            .isNotEmpty()
                    ) {
                        validateFullCard(
                            cardNumberEt.text.toString().replace(" ", "").toLong(),
                            cardCvcEt.text.toString().toInt(),
                            expirationDateEt.toString()
                        )
                    }

                }
            }
        }
    }

    override fun onEach() {
        super.onEach()
        with(viewModel) {
            with(binding) {
                onEach(cardNumberValidation) {
                    cardNumberErrorMessage.isInvisible = it
                }
                onEach(cardNameValidation) {
                    nameErrorMessage.isInvisible = it
                }
                onEach(cardExpDateValidation) {
                    expirationDateErrorMessage.isInvisible = it
                }
                onEach(cardCVCValidation) {
                    cvcCodeErrorMessage.isInvisible = it
                }
                onEach(cardFullValidation) {
                    Snackbar.make(binding.root, "PAID", Snackbar.LENGTH_SHORT)
                        .setAction("DONE") {
                        }
                        .show()
                }
            }

        }
    }

    companion object {
        const val EXPIRE_DATE_START_POSITION = 2
        const val EXPIRE_DATE_BEFORE_POSITION = 1
        const val EXPIRE_DATE_SET_SELECTION_CASE_1 = 1
        const val EXPIRE_DATE_SET_SELECTION_CASE_3 = 3
        const val MONTH_DATE_DIGITS_COUNT = 2
        const val MONTH_DATE_START_POSITION = 0
        const val MONTH_DATE_END_POSITION = 1
    }

}