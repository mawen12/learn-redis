package com.mawen.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

import java.time.Duration;

public class RedisClientDemo {

    public static void main(String[] args) {
        String host = "redis.local";
        int port = 6379;
        int database = 0;
        boolean sslEnable = false;
        long timeout = 5000L;
        String password = "redis";


        RedisURI.Builder redisUriBuilder = RedisURI.builder();
        redisUriBuilder.withHost(host)
                .withPort(port)
                .withDatabase(database)
                .withSsl(sslEnable)
                .withTimeout(Duration.ofMillis(timeout))
//                .withPassword(password.toCharArray());
        ;

        RedisClient redisClient = RedisClient.create(redisUriBuilder.build());

        StatefulRedisConnection<String, String> connect = redisClient.connect();
        String ping = connect.sync().ping();
        System.out.println(ping);

        redisClient.close();
    }
}
