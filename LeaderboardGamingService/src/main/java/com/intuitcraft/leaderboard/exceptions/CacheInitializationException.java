package com.intuitcraft.leaderboard.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CacheInitializationException extends Exception {
	public CacheInitializationException(String message, JsonProcessingException e) {
		super(message);
	}
}
