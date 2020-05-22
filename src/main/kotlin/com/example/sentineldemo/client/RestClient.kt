package com.example.sentineldemo.client

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException
import com.example.sentineldemo.shared.sentinel
import com.example.sentineldemo.shared.sentinelAsync
import kotlinx.coroutines.reactive.awaitFirst
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient

open class RestClient(private val host: String, private val webClient: WebClient) {
    private val logger = LoggerFactory.getLogger(javaClass)
    suspend fun test1(id: Int): String = sentinel("test1", id, {
        webClient.get().uri("$host/test1/$it").retrieve().bodyToMono(String::class.java).awaitFirst()
    }) { i, e ->
        when(e) {
            is DegradeException -> logger.info("request 1 err: DegradeException")
            else -> logger.error("request 1 err: ", e)
        }
        "one $i"
    }

    suspend fun test2(id: Int): String = sentinel("test2", id, {
        webClient.get().uri("$host/test2/$it").retrieve().bodyToMono(String::class.java).awaitFirst()
    }) { i, e ->
        when(e) {
            is DegradeException -> logger.info("request 2 err: DegradeException")
            else -> logger.error("request 2 err: ", e)
        }
        "two $i"
    }

    suspend fun test3(id: Int): String = sentinel("test3", id, {
        webClient.get().uri("$host/test3/$it").retrieve().bodyToMono(String::class.java).awaitFirst()
    }) { i, e ->
        when(e) {
            is DegradeException -> logger.info("request 3 err: DegradeException")
            else -> logger.error("request 3 err: ", e)
        }
        "three $i"
    }

    suspend fun test4(id: Int): String = sentinelAsync("test1", id, {
        webClient.get().uri("$host/test1/$it").retrieve().bodyToMono(String::class.java).awaitFirst()
    }) { i, e ->
        when(e) {
            is DegradeException -> logger.info("request 1 err: DegradeException")
            else -> logger.error("request 1 err: ", e)
        }
        "four $i"
    }

    suspend fun test5(id: Int): String = sentinelAsync("test2", id, {
        webClient.get().uri("$host/test2/$it").retrieve().bodyToMono(String::class.java).awaitFirst()
    }) { i, e ->
        when(e) {
            is DegradeException -> logger.info("request 1 err: DegradeException")
            else -> logger.error("request 1 err: ", e)
        }
        "five $i"
    }
}