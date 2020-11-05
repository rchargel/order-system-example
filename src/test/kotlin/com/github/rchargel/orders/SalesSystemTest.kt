package com.github.rchargel.orders

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class SalesSystemTest(private val item: String, private val quantity: Int, private val expected: Double) {

    private val salesSystem = SalesSystem(arrayOf("orange:0.25", "apple:0.60"))

    @Test
    fun evaluate() = assertEquals(expected, salesSystem.calculatePrice(item, quantity), 0.001)

    companion object {
        @JvmStatic
        @Parameters(name = "{1} {0} = \${2}")
        fun params() = arrayOf(
            arrayOf("apple", 1, 0.6),
            arrayOf("apple", 2, 0.6), // 2 for 1
            arrayOf("apple", 3, 1.2),
            arrayOf("apple", 4, 1.2),
            arrayOf("apple", 5, 1.8),
            arrayOf("orange", 1, 0.25),
            arrayOf("orange", 2, 0.5),
            arrayOf("orange", 3, 0.5), // 3 for 2
            arrayOf("orange", 4, 0.75),
            arrayOf("orange", 5, 1.0),
            arrayOf("orange", 6, 1.0),
            arrayOf("orange", 7, 1.25),
        )
    }
}