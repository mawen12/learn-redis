package com.mawen.learn.redis.basic;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/6/6
 */
public class TinyDBException extends RuntimeException{

	private static final long serialVersionUID = -1L;

	public TinyDBException() {
		super();
	}

	public TinyDBException(String message) {
		super(message);
	}

	public TinyDBException(String message, Throwable cause) {
		super(message, cause);
	}

	public TinyDBException(Throwable cause) {
		super(cause);
	}
}
