package com.github.rchargel.orders

data class ItemUpdate(
    val item: String,
    val quantity: Int
)

data class PurchaseRequest(
    val customer: String,
    val update: ItemUpdate
)

data class PurchaseResult(
    val customer: String,
    val price: Double,
    val accepted: Boolean = true,
    val message: String? = null
)

data class CustomerRegistration(
    val customer: String,
    val email: String
)