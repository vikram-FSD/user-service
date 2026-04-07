package com.demo.user.security.token;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {
    private final Map<String, Instant> store = new ConcurrentHashMap<>();

    public void addToBlackList(String token) {
        store.put(token, Instant.now());
    }

    public boolean isBlackListed(String token) {
        return store.containsKey(token);
    }

}
