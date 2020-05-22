package com.example.sentineldemo.controller

import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class HomeController {
    @GetMapping("/test1/{id}")
    suspend fun test(@PathVariable("id") id: Int): String {
        val i = Random.nextLong(2000)
        delay(i)
        return "hello world 1 delay $i"
    }

    @GetMapping("/test2/{id}")
    suspend fun test2(@PathVariable("id") id: Int): String {
        val i = Random.nextLong(1500)
        delay(i)
        return "hello world 2 delay $i"
    }

    @GetMapping("/test3/{id}")
    suspend fun test3(@PathVariable("id") id: Int): String {
        val i = Random.nextLong(1000)
        delay(i)
        return "hello world 3 delay $i"
    }
}