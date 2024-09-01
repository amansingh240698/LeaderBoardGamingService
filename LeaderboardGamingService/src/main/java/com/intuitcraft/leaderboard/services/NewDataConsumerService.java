package com.intuitcraft.leaderboard.services;

public interface NewDataConsumerService<T> {
	public void consumeDataFromQueue(T newData);
}
