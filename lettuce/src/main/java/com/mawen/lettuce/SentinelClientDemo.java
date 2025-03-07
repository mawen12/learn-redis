package com.mawen.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SentinelClientDemo {

    public static void main(String[] args) {
        try (RedisClient redisClient = RedisClient.create("redis://redis.local:6379")) {
            StatefulRedisConnection<String, String> connect = redisClient.connect();
            RedisCommands<String, String> commands = connect.sync();

            Path path = Path.of(ClassLoader.getSystemResource("sentinel").toURI());;
            List<File> files = sentinelFiles(path);
            for (File file : files) {
                set(commands, file);
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<File> sentinelFiles(Path path) throws IOException {
        return Files.list(path).map(Path::toFile).collect(Collectors.toList());
    }

    public static void set(RedisCommands<String, String> commands, File file) throws IOException {
        String name = file.getName();
        commands.set("sentinel-" + name.substring(0, name.indexOf('.')), Files.readString(file.toPath()));
    }
}
