package cberg.kst

import java.math.BigDecimal

fun Iterable<BigDecimal>.sum(): BigDecimal {
    var sum = BigDecimal.ZERO
    for (element in this) {
        sum += element
    }
    return sum
}

