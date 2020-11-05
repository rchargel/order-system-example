package com.github.rchargel.orders

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

@Component
class Inventory(
    @Autowired private val kafkaTemplate: KafkaTemplate<String, String>?,
    @Autowired private val salesSystem: SalesSystem?
) {

    private val inventory = ConcurrentHashMap<String, Int>(mapOf("orange" to 5, "apple" to 10))

    private fun publishSale(purchase: PurchaseResult) {
        kafkaTemplate?.send(Topics.PURCHASE_PROCESSED, mapper.writeValueAsString(purchase))
    }

    @KafkaListener(topics = [Topics.PURCHASE_REQUEST])
    fun purchaseFromInventory(record: ConsumerRecord<String, String>) {
        val value = mapper.readValue(record.value(), PurchaseRequest::class.java)

        if (!inventory.containsKey(value.update.item) || inventory[value.update.item]!! < value.update.quantity)
            publishSale(PurchaseResult(value.customer, 0.0, false, "Not enough in stock"))
        else {
            updateInventory(value.update.item, -value.update.quantity)
            publishSale(
                PurchaseResult(
                    value.customer,
                    salesSystem?.calculatePrice(value.update.item, value.update.quantity) ?: 0.0
                )
            )
        }
    }

    @KafkaListener(topics = [Topics.INVENTORY_INSERT])
    fun inventoryAddRequest(record: ConsumerRecord<String, String>) {
        val value = mapper.readValue(record.value(), ItemUpdate::class.java)
        updateInventory(value.item, value.quantity)
    }

    fun updateInventory(item: String, quantity: Int): Int {
        inventory[item] = max(0, inventory[item]?.plus(quantity) ?: quantity)
        log.info("Available Inventory: $inventory")
        return inventory[item]!!
    }

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())!!
        val log = LoggerFactory.getLogger(Inventory::class.java)!!
    }
}