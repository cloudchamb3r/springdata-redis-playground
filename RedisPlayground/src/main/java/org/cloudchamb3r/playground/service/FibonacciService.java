package org.cloudchamb3r.playground.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FibonacciService implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Lazy
    @Autowired
    private FibonacciService self;

    /**
     * WARNING: In this recursive call, only fibonacci::n will be saved
     * Because Spring internally uses Proxy object when applying @Cacheable
     * but in this recursive call, it uses FibonacciService directly
     */
    @Cacheable("fibonacci")
    public BigDecimal getFibonacci(int n) {
        if (n < 0) throw new IllegalArgumentException();
        if (n <= 1) return BigDecimal.ONE;
        return getFibonacci(n - 1).add(getFibonacci(n - 2));
    }

    /**
     * This method, make recursive call using proxy,
     * so when call getFibonacci(n), fibonacci::1 ~ fibonacci::n will be saved into redis cache ;)
     */
    @Cacheable("fibonacci")
    public BigDecimal getFibonacci2(int n) {
        if (n < 0) throw new IllegalArgumentException();
        if (n <= 1) return BigDecimal.ONE;
        FibonacciService fiboProxy = applicationContext.getBean(FibonacciService.class);
        return fiboProxy.getFibonacci2(n - 1).add(fiboProxy.getFibonacci2(n - 2));
    }

    /**
     * using Proxy object, via `@Lazy annotated Self`
     */
    @Cacheable("fibonacci")
    public BigDecimal getFibonacci3(int n) {
        if (n < 0) throw new IllegalArgumentException();
        if (n <= 1) return BigDecimal.ONE;
        return self.getFibonacci3(n - 1).add(self.getFibonacci3(n - 2));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
