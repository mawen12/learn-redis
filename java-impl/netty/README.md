TinyDB
======

TinyDB is a REDIS implementation in Java. At this moment is in the first stage of developments
and only implements a small subset of commands. The objetive is implement a full functional 
one-to-one replacement for REDIS.

Implemented commands
--------------------

- SET
- GET
- DEL
- GETSET
- EXISTS
- MGET
- MSET
- INCR
- DECR
- INCRBY
- DECRBY
- HGET
- HSET
- HGETALL
- PING
- ECHO
- TIME
- FLUSHDB
- STRLEN
- TYPE
- RENAME
- KEYS
- HEXISTS
- HDEL
- HKEYS
- HLEN
- APPEND
- HVALS
- LPUSH
- LPOP
- RPUSH
- RPOP
- LLEN
- SADD
- SMEMBERS
- SCARD
- SISMEMBER
- ZADD
- ZCARD
- SREM
- ZREM
- ZRANGE
- ZRANGEBYSCORE
- ZREVRANGE

Design
------

TinyDB is implemented using asynchronous IO with netty, and at this moment with no persistence,
only work like an on-memory cache.

Performance
-----------

Performance is quite good, not as good as REDIS, but it's good enough for Java.

This is TinyDB

```shell
redis-benchmark -t set,get -h localhost -p 7081 -n 100000 -q

```

And this is REDIS

```shell
redis-benchmark -t set,get -h localhost -p 6379 -n 100000 -q

SET: 149476.83 requests per second, p50=0.167 msec                    
GET: 159235.66 requests per second, p50=0.159 msec
```