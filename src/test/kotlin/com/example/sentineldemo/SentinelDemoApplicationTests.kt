package com.example.sentineldemo

import com.alibaba.csp.sentinel.SphU
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager
import com.example.sentineldemo.client.RestClient
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient

@SpringBootTest
class SentinelDemoApplicationTests {
	private val logger = LoggerFactory.getLogger(javaClass)
	@Test
	fun test1() = runBlocking {
		val tcp = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).doOnConnected {
			it.addHandlerLast(ReadTimeoutHandler(300))
			it.addHandlerLast(WriteTimeoutHandler(300))
		}
		val client = WebClient.builder().clientConnector(ReactorClientHttpConnector(HttpClient.from(tcp))).build()
		DegradeRuleManager.loadRules(listOf(DegradeRule().apply {
			count = 350.0
			timeWindow = 2
			resource = "test1"
		}, DegradeRule().apply {
			count = 200.0
			timeWindow = 2
			resource = "test2"
		}, DegradeRule().apply {
			count = 150.0
			timeWindow = 2
			resource = "test3"
		}))
		val restClient = RestClient("http://127.0.0.1:8080", client)
		IntRange(1, 500).forEach {
			try {
				val s = withContext(Dispatchers.IO) {
					val s1 = async { restClient.test1(it) }
					val s2 = async { restClient.test2(it) }
					s1.await() + "---------" + s2.await()
				}
				val s3 = restClient.test3(it)
				logger.info("$s----$s3")
			} catch (e: Exception) {
				logger.error("other error: ${e.javaClass.name}: ${e.message}")
			}
		}
	}

	@Test
	fun test2() = runBlocking {
		val tcp = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).doOnConnected {
			it.addHandlerLast(ReadTimeoutHandler(300))
			it.addHandlerLast(WriteTimeoutHandler(300))
		}
		val client = WebClient.builder().clientConnector(ReactorClientHttpConnector(HttpClient.from(tcp))).build()
		DegradeRuleManager.loadRules(listOf(DegradeRule().apply {
			count = 350.0
			timeWindow = 2
			resource = "test1"
		}, DegradeRule().apply {
			count = 200.0
			timeWindow = 2
			resource = "test2"
		}, DegradeRule().apply {
			count = 150.0
			timeWindow = 2
			resource = "test3"
		}))
		val restClient = RestClient("http://127.0.0.1:8080", client)
		IntRange(1, 500).forEach {
			try {
				val s = withContext(Dispatchers.IO) {
					val s1 = async { restClient.test1(it) }
					val s2 = async { restClient.test2(it) }
					s1.await() + "---------" + s2.await()
				}
				logger.info(s)
			} catch (e: Exception) {
				logger.error("other error: ${e.javaClass.name}: ${e.message}")
			}
		}
	}

	@Test
	fun test3() = runBlocking {
		val tcp = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).doOnConnected {
			it.addHandlerLast(ReadTimeoutHandler(300))
			it.addHandlerLast(WriteTimeoutHandler(300))
		}
		val client = WebClient.builder().clientConnector(ReactorClientHttpConnector(HttpClient.from(tcp))).build()
		DegradeRuleManager.loadRules(listOf(DegradeRule().apply {
			count = 350.0
			timeWindow = 2
			resource = "test1"
		}, DegradeRule().apply {
			count = 200.0
			timeWindow = 2
			resource = "test2"
		}, DegradeRule().apply {
			count = 150.0
			timeWindow = 2
			resource = "test3"
		}))
		val restClient = RestClient("http://127.0.0.1:8080", client)
		IntRange(1, 500).forEach {
			try {
				val s = restClient.test4(it)
				logger.info(s)
			} catch (e: Exception) {
				logger.error("other error: ${e.javaClass.name}: ${e.message}")
			}
		}
	}

	@Test
	fun test4() {
		val tcp = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).doOnConnected {
			it.addHandlerLast(ReadTimeoutHandler(300))
			it.addHandlerLast(WriteTimeoutHandler(300))
		}
		val client = WebClient.builder().clientConnector(ReactorClientHttpConnector(HttpClient.from(tcp))).build()
		DegradeRuleManager.loadRules(listOf(DegradeRule().apply {
			count = 350.0
			timeWindow = 2
			resource = "test1"
		}, DegradeRule().apply {
			count = 200.0
			timeWindow = 2
			resource = "test2"
		}, DegradeRule().apply {
			count = 150.0
			timeWindow = 2
			resource = "test3"
		}))
		IntRange(1, 500).forEach {
			try {
				val entry = SphU.asyncEntry("test1")
				client.get().uri("http://127.0.0.1:8080/test1/$it").retrieve().bodyToMono(String::class.java).subscribe ({
					entry.close()
					logger.info(it)
				}) {
					when(it) {
						is DegradeException -> logger.info("request 1 err: DegradeException")
						else -> logger.error("request 1 err: ", it)
					}
				}
			} catch (e: Exception) {
				logger.error("other error: ${e.javaClass.name}: ${e.message}")
			}
		}
		Thread.sleep(2000000)
	}

	@Test
	fun test5() = runBlocking {
		val tcp = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).doOnConnected {
			it.addHandlerLast(ReadTimeoutHandler(300))
			it.addHandlerLast(WriteTimeoutHandler(300))
		}
		val client = WebClient.builder().clientConnector(ReactorClientHttpConnector(HttpClient.from(tcp))).build()
		DegradeRuleManager.loadRules(listOf(DegradeRule().apply {
			count = 350.0
			timeWindow = 2
			resource = "test1"
		}, DegradeRule().apply {
			count = 200.0
			timeWindow = 2
			resource = "test2"
		}, DegradeRule().apply {
			count = 150.0
			timeWindow = 2
			resource = "test3"
		}))
		val restClient = RestClient("http://127.0.0.1:8080", client)
		IntRange(1, 500).forEach {
			try {
				val s = withContext(Dispatchers.IO) {
					val s1 = async { restClient.test4(it) }
					val s2 = async { restClient.test5(it) }
					s1.await() + "------" + s2.await()
				}
				logger.info(s)
			} catch (e: Exception) {
				logger.error("other error: ${e.javaClass.name}: ${e.message}")
			}
		}
	}
}
