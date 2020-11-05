package com.github.rchargel.orders

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.stereotype.Component

@SpringBootApplication
open class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(arrayOf(Application::class.java), args).close()
        }
    }

    @Bean
    open fun kafkaBroker() = EmbeddedKafkaBroker(1)
        .kafkaPorts(9092)
        .brokerListProperty("spring.kafka.bootstrap-servers")
}

@Component
class Runner(@Autowired val kafkaTemplate: KafkaTemplate<String, String>) : CommandLineRunner {
    init {
        println("Options:")
        println("  BUY customer-name item-name quantity")
        println("  ADD item-name quantity")
        println("  REG customer-name email")
    }

    override fun run(args: Array<String>) {

        println("Start entering requests now..")
        while (true) {
            val input = readLine()!!.split(" ")
            if (input.isNotEmpty() && input.size >= 3) {
                when (input[0]) {
                    "BUY" -> buyItem(input[1], input[2].toLowerCase(), input[3].toInt())
                    "ADD" -> addItem(input[1].toLowerCase(), input[2].toInt())
                    "REG" -> registerCustomer(input[1], input[2])
                    else -> println("Not a valid request")
                }
            }
        }
    }

    fun registerCustomer(customer: String, email: String) {
        kafkaTemplate.send(Topics.CUSTOM_REG, mapper.writeValueAsString(CustomerRegistration(customer, email)))
    }

    fun addItem(item: String, quantity: Int) {
        kafkaTemplate.send(Topics.INVENTORY_INSERT, mapper.writeValueAsString(ItemUpdate(item, quantity)))
    }

    fun buyItem(customer: String, item: String, quantity: Int) {
        kafkaTemplate.send(
            Topics.PURCHASE_REQUEST,
            mapper.writeValueAsString(PurchaseRequest(customer, ItemUpdate(item, quantity)))
        )
    }

    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())
    }
}

class Topics {
    companion object {
        const val INVENTORY_INSERT = "inventory-insert"
        const val PURCHASE_REQUEST = "purchase-request"
        const val PURCHASE_PROCESSED = "purchase-processed"
        const val CUSTOM_REG = "customer-registration"
    }
}

