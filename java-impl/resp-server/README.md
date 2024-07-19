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

Arrays can hold any data types inside, also other arrays.

And use \r\n as delimiter, that means you can open a `telnet/netcat/nc` against
the server port and send whatever you want.

So, what provide this project in addition to the protocol implementation? Well,
it defines a framework to implement command oriented protocols.

You only need to define a set of commands and implement them.

Some commands are built-in, `ping`, `echo`, `time` and `quit`.

## What is a command?

A command is an array of bulk string in RESP, first element in array is the command,
and the additional elements are the parameters of the command. This is how a command
looks like:

```
    *2\r\n
    $4\r\n
    ECHO\r\n
    $13\r\n
    Hello World!\r\n
```

In this sample, `ECHO` is the command and `Hello World` is the parameter.

## How is implemented?

The protocol is implemented in Java8, using asynchronous IO (netty), and using the 
reactive programming paradigm (rxjava). What that means? It means that is single
thread, every request is managed inside the same thread, so there's no concurrency
issues at all, the same way as REDIS works.

## How can I use it?

It's very easy, you only need 2 lines of code to start the server

```java
    RedisServer redisServer = new RedisServer("localhost", 12345, new CommandSuite());
    redisServer.start();
```

CommandSuite is the default commands suite but you can extend and add your own,
well, that's the point :)

What a command looks like?

```java
    @Command("ping")
    public class PingCommand implements ICommand {
        @Override
        public void execute(IRequest request, IResponse response) {
			response.addSimpleStr("PONG");
        }
    }
```

A command must implement the interface ICommand. This interface only defines 
the method execute, who receives a request object and a response object. It's
looks very familiar, isn't it? Is very similar to Servlet API.

You can get the parameter of the command like this

```java
    SafeString param0 = request.getParam(0);
```

Every parameter is a `SafeString`, and what the hell is a `SafeString`? Previously,
we that RESP is binary-safe, so, it means that you can receive anything. SafeString
wraps the bytes received, but, don't worry, it's not going to be a problem, trust me.

And you can response to a request this way:

```java
    response.addBulkStr(request.getParam(0));
```

You have similar methods like `addSimpleStr`, `addInteger`, `addArray`, `addError`...

Annotations are used to define some metadata to the commands, `@Command` annotation
defines the command name, also there's another annotation, `@ParamLength` to define
the number of the parameter accepted for this command

```java
    @Command("echo")
    @ParamLength(1)
    public class EchoCommand implements ICommand {
    
        @Override
        public void execute(IRequest request, IResponse response) {
            response.addBulkStr(request.getParam(0));
        }
    }
```

If the number of parameters is less than the specified value, the command
is rejected with an error.