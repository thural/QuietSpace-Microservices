package com.jellybrains.quietspace.auth_service.kafka.saga;

public interface SagaStep<T, U> {
    void process(T event);

    void rollback(U event);
}
