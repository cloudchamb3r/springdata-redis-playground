package org.cloudchamb3r.playground.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FibonacciService {

    @Cacheable("fibonacci") // cached value will be saved `fibonacci::value`
    public BigDecimal getFibonacci(int n) {
        if (n < 0) throw new IllegalArgumentException();
        if (n <= 1) return BigDecimal.ONE;
        return getFibonacci(n - 1).add(getFibonacci(n - 2));
    }
}
