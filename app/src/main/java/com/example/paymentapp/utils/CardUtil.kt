package com.example.paymentapp.utils

import com.example.paymentapp.utils.settings.CARD_NUMBER_LENGTH_MAX
import com.example.paymentapp.utils.settings.CARD_NUMBER_LENGTH_MIN
import com.example.paymentapp.utils.settings.CVC_LENGTH
import com.example.paymentapp.utils.settings.CardType
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CardUtil {
    companion object {
        private val LOG_TAG = CardUtil::class.java.simpleName
        private val SUPPORTED_CARDS: MutableList<CardType> = getAllCardTypes()

        private var isLoggingEnabled: Boolean = true

        private fun getAllCardTypes() = mutableListOf(
            CardType.Visa,
            CardType.MasterCard,
            CardType.AmericanExpress,
            CardType.DinersClub,
            CardType.Discover,
            CardType.Jcb
        )

        private fun isValidCardNumberByLuhn(stringInputCardNumber: String): Boolean {
            var sum = 0
            var isSecondDigit = false

            for (i in stringInputCardNumber.length - 1 downTo 0) {
                var d = stringInputCardNumber[i] - '0'

                if (isSecondDigit) {
                    d *= 2
                }

                sum += d / 10
                sum += d % 10

                isSecondDigit = !isSecondDigit
            }

            val result: Boolean = ((sum % 10) == 0)
            printLog("isValidCardNumber By Luhn () = $result")
            return result
        }

        private fun isValidCardNumberByTypeSupport(stringInputCardNumber: String): Boolean {
            for (supportedType in SUPPORTED_CARDS) {
                if (stringInputCardNumber.matches(supportedType.pattern.toRegex())) {
                    printLog("isValidCardNumber By Type Support() = true")
                    return true
                }
            }

            printLog("isValidCardNumber By Type Support () = false")
            return false
        }

        private fun isValidCardNumberLength(inputCardNumber: String): Boolean {
            val result: Boolean =
                ((inputCardNumber.length >= CARD_NUMBER_LENGTH_MIN) &&
                        (inputCardNumber.length <= CARD_NUMBER_LENGTH_MAX))

            printLog("isValidCardNumber Length () = $result")
            return result
        }

        private fun isValidCardNumberValue(inputCardNumber: Long): Boolean =
            (inputCardNumber > 0)

        private fun printLog(logMessage: String) {
            if (isLoggingEnabled) println("$LOG_TAG.$logMessage")
        }

        private fun setLoggingEnabled(isEnabled: Boolean) {
            isLoggingEnabled = isEnabled
        }

        private fun setSupportedCardTypes(listSupportedCardTypes: List<CardType>) {
            if (listSupportedCardTypes.isEmpty()) {
                printLog("setSupportedCardTypes() : Cannot set empty selection of CardTypes.")
            } else {
                SUPPORTED_CARDS.clear()
                SUPPORTED_CARDS.addAll(listSupportedCardTypes)
            }
        }

        fun setUp(isLoggingEnabled: Boolean, listSupportedCardTypes: List<CardType>) {
            setLoggingEnabled(isLoggingEnabled)
            setSupportedCardTypes(listSupportedCardTypes)
        }

        fun isValidCard(
            inputCardNumber: Long, inputCVC: Int,
            expDate: String
        ): Boolean =
            (isValidCardNumber(inputCardNumber) && isValidCVC(inputCVC) &&
                    validateCardExpirationDate(expDate))

        /*
        * @param inputCardNumber : the 12-19 digit number
        * */
        fun isValidCardNumber(inputCardNumber: Long): Boolean {
            printLog("isValidCardNumber() : input = $inputCardNumber")
            return isValidCardNumberValue(inputCardNumber) &&
                    isValidCardNumberLength(inputCardNumber.toString()) &&
                    isValidCardNumberByTypeSupport(inputCardNumber.toString()) &&
                    isValidCardNumberByLuhn(inputCardNumber.toString())
        }

        /*
        * @param inputCVC : 3-4 digit CVC/CVV value
        * */
        fun isValidCVC(inputCVC: Int): Boolean {
            val stringInputCVC = inputCVC.toString()
            val result: Boolean = (stringInputCVC.length == CVC_LENGTH)
            printLog("isValidCVC() : $stringInputCVC = $result")
            return result
        }


        fun validateCardExpirationDate(expirationDate: String): Boolean {
            val regex = Regex("^(0[1-9]|1[0-2])/(2[2-9]|[3-9][0-9])$")
            if (!expirationDate.matches(regex)) {
                return false
            }

            val currentDate = YearMonth.now()
            val formatter = DateTimeFormatter.ofPattern("MM/yy")
            val parsedExpirationDate = YearMonth.parse(expirationDate, formatter)

            return parsedExpirationDate.isAfter(currentDate)
        }


        fun isCardNameValid(name: String): Boolean {
            val regex = "^[a-zA-Z\\s'-]+$"
            return name.matches(regex.toRegex())
        }
    }

}