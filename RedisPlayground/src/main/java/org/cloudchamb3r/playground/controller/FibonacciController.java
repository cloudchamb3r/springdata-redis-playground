package org.cloudchamb3r.playground.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloudchamb3r.playground.service.FibonacciService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/fibo")
@RequiredArgsConstructor
public class FibonacciController {
    private final FibonacciService fibonacciService;

    @GetMapping("/{n}")
    public BigDecimal computeFibonacci(@PathVariable int n) {
        BigDecimal value = fibonacciService.getFibonacci(n);
        log.info("fibonacci({}) = {}", n, value);
        return value;
    }

    @GetMapping("/recursive-cache/{n}")
    public BigDecimal computeFibonacci2(@PathVariable int n) {
        BigDecimal value = fibonacciService.getFibonacci2(n);
        log.info("fibonacci({}) = {}", n, value);
        return value;
    }
}
