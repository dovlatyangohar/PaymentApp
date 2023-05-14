package com.example.paymentapp.utils.settings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CardNumberTextWatcher(private val editText: EditText) : TextWatcher {

    private var isFormatting: Boolean = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (isFormatting) {
            return
        }

        isFormatting = true

        val cardNumber = editable.toString()
        val formattedCardNumber = formatCardNumber(cardNumber)

        editText.setText(formattedCardNumber)
        editText.setSelection(formattedCardNumber.length)

        isFormatting = false
    }

    private fun formatCardNumber(cardNumber: String): String {
        val digitsOnly = cardNumber.replace("\\D+".toRegex(), "")
        val formattedCardNumber = StringBuilder()

        var templateIndex = 0
        var digitIndex = 0

        while (templateIndex < CARD_NUMBER_TEMPLATE.length && digitIndex < digitsOnly.length) {
            val templateChar = CARD_NUMBER_TEMPLATE[templateIndex]
            if (templateChar == '#') {
                formattedCardNumber.append(digitsOnly[digitIndex])
                digitIndex++
            } else {
                formattedCardNumber.append(templateChar)
            }
            templateIndex++
        }

        return formattedCardNumber.toString()
    }

    companion object {
        private const val CARD_NUMBER_TEMPLATE = "#### #### #### ####"
    }
}