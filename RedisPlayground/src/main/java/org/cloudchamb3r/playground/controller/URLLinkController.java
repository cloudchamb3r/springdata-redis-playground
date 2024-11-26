package org.cloudchamb3r.playground.controller;

import lombok.RequiredArgsConstructor;
import org.cloudchamb3r.playground.domain.AppendURLRequest;
import org.cloudchamb3r.playground.domain.RedisURLLinkList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/url-link")
@RequiredArgsConstructor
public class URLLinkController {
    private final RedisURLLinkList redisURLLinkList;

    @GetMapping("/{userId}")
    public List<String> getAllUrl(@PathVariable String userId) {
        return redisURLLinkList.getAllUrl(userId);
    }

    @PostMapping("/{userId}")
    public void appendUrl(@PathVariable String userId, @RequestBody AppendURLRequest request) {
        redisURLLinkList.addLink(userId, request.url());
    }
}
