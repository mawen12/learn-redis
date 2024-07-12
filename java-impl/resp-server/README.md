# resp-server

Netty implementation of Redis Serialization Protocol, and a simple framework to implement command base protocols.

## Why?

The important thing of REDIS is the RESP protocol.
I think that it would be nice to to build a library to implement services using this protocol.

## What?

[RESP](https://redis.io/docs/latest/develop/reference/protocol-spec) is the protocol used by REDIS, but it
can be used to implement other client-server protocols.

It's nice because:

- Is easy
- Is fast
- Also is human readable

RESP can serialize some data types

- simple strings: `+PONG`
- errors: `-ERROR`
- integers: `:1`
- bulk strings (binary-safe): `$4\r\nPING`
- arrays: `*3\r\n:1\r\n:2\r\n:3`

What binary safe means? It means that can be what ever you want, a UTF-8 String 
or compressed data, a picture, etc...



## What is a command?


## How is implemented?


## How can I use it?