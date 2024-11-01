package com.yuvrajsinghgmx.shopsmart.profilefeatures

import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.screens.PaymentMethodInfo

object SavedCardsManager {
    private val _savedCards = mutableListOf(
        PaymentMethodInfo(
            "•••• •••• •••• 1234",
            "Expires 12/25",
            R.drawable.credit_card_24px,
            "saved_cards"
        ),
        PaymentMethodInfo(
            "•••• •••• •••• 5678",
            "Expires 09/24",
            R.drawable.credit_card_24px,
            "saved_cards"
        )
    )

    var savedCards: List<PaymentMethodInfo> = _savedCards

    fun addCard(card: PaymentMethodInfo) {
        _savedCards.add(card)
        savedCards = _savedCards.toList()
    }
}