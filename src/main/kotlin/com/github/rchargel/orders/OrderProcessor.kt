package com.github.rchargel.orders

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.text.NumberFormat

@Component
class OrderProcessor {

    private val emails = HashMap<String, String>()

    @KafkaListener(topics = [Topics.CUSTOM_REG])
    fun register(record: ConsumerRecord<String, String>) {
        val value = mapper.readValue(record.value(), CustomerRegistration::class.java)
        emails[value.customer] = value.email
    }

    @KafkaListener(topics = [Topics.PURCHASE_PROCESSED])
    fun processed(record: ConsumerRecord<String, String>) {
        val value = mapper.readValue(record.value(), PurchaseResult::class.java)
        log.info("Purchase Processed: $value")

        if (emails.containsKey(value.customer))
            log.info(
                """Sending email
                Dear: ${value.customer},
                
                You're request ${if (value.accepted) "has" else "has not"} been processed.
                ${value.message ?: ""}
                
                Total sale price: ${formatter.format(value.price)}
            """.trimMargin()
            )
    }

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())!!
        val formatter = NumberFormat.getCurrencyInstance()!!
        val log = LoggerFactory.getLogger(OrderProcessor::class.java)!!
    }
}