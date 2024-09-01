package com.intuitcraft.leaderboard.services.client;

import com.intuitcraft.leaderboard.exceptions.MessageQueueFailureException;

public interface NewDataProducerService<T> {
	public void addDataToQueue(T newData) throws MessageQueueFailureException;
}
