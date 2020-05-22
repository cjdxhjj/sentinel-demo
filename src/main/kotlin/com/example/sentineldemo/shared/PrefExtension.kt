package com.example.sentineldemo.shared

import com.alibaba.csp.sentinel.SphU

suspend fun <T, R> sentinel(key: String, ctx: T, block: suspend (T) -> R, ex: suspend (T, Exception) -> R): R = try {
    SphU.entry(key).use { block(ctx) }
} catch (e: Exception) {
    ex(ctx, e)
}

suspend fun <T1, T2, R> sentinel(key: String, t1: T1, t2: T2, def: R, block: suspend (T1, T2) -> R): R = try {
    SphU.entry(key).use { block(t1, t2) }
} catch (e: Exception) {
    def
}

suspend fun <T1, T2, R> sentinel(key: String, t1: T1, t2: T2, block: suspend (T1, T2) -> R, ex: suspend (T1, T2, Exception) -> R): R = try {
    SphU.entry(key).use { block(t1, t2) }
} catch (e: Exception) {
    ex(t1, t2, e)
}

suspend fun <T, R> sentinelAsync(key: String, ctx: T, block: suspend (T) -> R, ex: suspend (T, Exception) -> R): R = try {
    val entry = SphU.asyncEntry(key)
    block(ctx).apply {
        entry.close()
    }
} catch (e: Exception) {
    ex(ctx, e)
}