package com.example.sentineldemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SentinelDemoApplication

fun main(args: Array<String>) {
	runApplication<SentinelDemoApplication>(*args)
}
