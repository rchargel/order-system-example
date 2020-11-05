package com.github.rchargel.orders

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@Component
open class SalesSystem(
    @field:Value("\${app.prices}") private val prices: Array<String>
) {
    private val priceMap: Map<String, Double>
        get() {
            return prices.map {
                val spl = it.split(":")
                spl[0] to spl[1].toDouble()
            }.toMap()
        }

    fun calculatePrice(item: String, quantity: Int) =
        priceMap[item]?.times(calculateSalesQuantity(item, quantity)) ?: Double.NaN

    private fun calculateSalesQuantity(item: String, quantity: Int) = when (item) {
        "apple" -> Math.ceil(quantity / 2.0).toInt()
        "orange" -> Math.ceil(quantity / 3.0 * 2.0).toInt()
        else -> quantity
    }
}