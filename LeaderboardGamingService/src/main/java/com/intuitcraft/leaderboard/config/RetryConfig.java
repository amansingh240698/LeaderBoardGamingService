package com.intuitcraft.leaderboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    // Define the retry policy
    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(3);
    retryTemplate.setRetryPolicy(retryPolicy);

    // Define backoff policy
    FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
    backOffPolicy.setBackOffPeriod(2000);
    retryTemplate.setBackOffPolicy(backOffPolicy);

    // Optionally, you can add a retry listener
    retryTemplate.setListeners(new RetryListener[] {
        new RetryListener() {
          @Override
          public <T, E extends Throwable> boolean open(RetryContext retryContext,
              RetryCallback<T, E> retryCallback) {
            return false;
          }

          @Override
          public <T, E extends Throwable> void close(RetryContext retryContext,
              RetryCallback<T, E> retryCallback, Throwable throwable) {

          }

          @Override
          public <T, E extends Throwable> void onError(RetryContext retryContext,
              RetryCallback<T, E> retryCallback, Throwable throwable) {

          }
        }
    });

    return retryTemplate;
  }
}
