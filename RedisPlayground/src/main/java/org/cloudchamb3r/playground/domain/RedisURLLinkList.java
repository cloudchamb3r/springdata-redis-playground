package org.cloudchamb3r.playground.domain;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisURLLinkList {
    private final RedisOperations<String, String> operations;

    /**
     * Because `RedisTemplate<K, V>` implements `RedisOperation<K, V>` interface
     * It could be injected by name
     */
    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOps;

    private final StringRedisTemplate stringRedisTemplate;
    public void addLink(String userId, URL url) {
        listOps.leftPush(userId, url.toExternalForm());
    }

    /**
     * AddLink via StringRedisTemplate
     */
    public void addLink2(String userId, URL url) {
        stringRedisTemplate.opsForList().leftPush(userId, url.toExternalForm());
    }

    /**
     * AddLink via native callback
     */
    public void addLink3(String userId, URL url) {
        operations.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                ((StringRedisConnection)connection).set(userId, url.toExternalForm());
                return null;
            }
        });
    }

    public List<String> getAllUrl(String userId) {
        Long size = listOps.size(userId);
        if (size == null) {
            return List.of();
        }
        return listOps.range(userId, 0, size);
    }

    public List<String> getAllUrl2(String userId) {
        Long size = stringRedisTemplate.opsForList().size(userId);
        if (size == null) {
            return List.of();
        }
        return stringRedisTemplate.opsForList().range(userId, 0, size);
    }

    public List<String> getAllUrl3(String userId) {
        return operations.execute(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection strConn = (StringRedisConnection) connection;
                Long size = strConn.lLen(userId);
                return strConn.lRange(userId, 0, size);
            }
        });
    }
}
