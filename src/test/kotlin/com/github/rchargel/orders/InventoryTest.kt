package com.github.rchargel.orders

import org.junit.Assert.assertEquals
import org.junit.Test

class InventoryTest() {
    private var inventory = Inventory(null, null)

    @Test
    fun testUpdateInventory() {
        assertEquals(14, inventory.updateInventory("apple", 4))
        assertEquals(5, inventory.updateInventory("apple", -9))
        assertEquals(0, inventory.updateInventory("apple", -9))
        assertEquals(10, inventory.updateInventory("orange", 5))
        assertEquals(10, inventory.updateInventory("banana", 10))
        assertEquals(0, inventory.updateInventory("peach", -100))
    }
}