package com.transfer.backend.service.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transfer.backend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class Utility {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper ;

    public void updateRedisCache(UserEntity user, String redisKey, boolean removeOnly) throws JsonProcessingException {
        if (removeOnly) {
            redisTemplate.delete(redisKey);  // Invalidate the cache by deleting the entry
        } else {
            String jsonData = objectMapper.writeValueAsString(user);
            redisTemplate.opsForValue().set(redisKey, jsonData, 30, TimeUnit.MINUTES);  // Update cache with new data
        }
    }
}
