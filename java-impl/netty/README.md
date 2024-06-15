TinyDB
======

TinyDB is a REDIS implementation in Java. At this moment is in the first stage of developments
and only implements a small subset of commands. The objetive is implement a full functional 
one-to-one replacement for REDIS.

Implemented commands
--------------------

- Server
  - FLUSHDB
  - SELECT
  - SYNC
  - SLAVEOF
- Connection
  - ECHO
  - PING
  - TIME
- Key
  - DEL
  - EXISTS
  - KEYS
  - RENAME
  - TYPE
- String
  - APPEND
  - DECRBY
  - DECR
  - GET
  - GETSET
  - INCRBY
  - INCR
  - MGET
  - MSET
  - SET
  - STRLEN
- Hash
  - HDEL
  - HEXISTS
  - HGETALL
  - HGET
  - HKEYS
  - HSET
  - HVALS
- List
  - LPOP
  - LPUSH
  - LINDEX
  - LLEN
  - LRANGE
  - LSET
  - RPOP
  - RPUSH
- Set
  - SADD
  - SCARD
  - SDIFF
  - SINTER
  - SISMEMBER
  - SMEMBERS
  - SPOP
  - SRANDOMMEMBER
  - SREM
  - SUNION
- Sorted Set
  - ZADD
  - ZCARD
  - ZRANGEBYSCORE
  - ZRANGE
  - ZREM
  - ZREVRANGE
- Pub/Sub
  - SUBSCRIBE
  - UNSUBSCRIBE
  - PUBLISH
- Transactions
  - MULTI
  - EXEC

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

TODO
----

- ~~Pipelining.~~ **Done!**
- Key expiration.
- Transaction (MULTI & EXEC)_(Done!)_.
- Persistence to disk. _(Working on it!)_
  - 80% RDB file format implemented (strings, lists, sets, sorted set and hashes).
  - Ziplist and Maplist encoding not implemented yes.
- Publish/Subscribe._(Partially implemented)_
  - PSUBSCRIBE and PUNSUBSCRIBE commands not implemented yet.
- Master - Slave Replication. _(Working on it!)_
  - Initial implementation finished, with only one slave per master
- Scripting with Javascript/Lua.
- Partitioning?
- Clustering?