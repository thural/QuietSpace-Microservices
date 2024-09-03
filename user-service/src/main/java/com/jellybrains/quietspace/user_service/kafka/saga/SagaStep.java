package com.jellybrains.quietspace.user_service.kafka.saga;

public interface SagaStep<T, U> {
    void process(T event);

    void rollback(U event);
}
