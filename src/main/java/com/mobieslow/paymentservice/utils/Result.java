package com.mobieslow.paymentservice.utils;

import java.util.NoSuchElementException;
import java.util.function.Function;

public record Result<U, E>(U data, E error) {

    public static <U, E> Result<U, E> ok(U data) {
        return new Result<>(data, null);
    }

    public static <U, E> Result<U, E> err(E error) {
        return new Result<>(null, error);
    }

    public boolean isOk() {
        return data != null;
    }

    public <X extends Throwable> U orElseThrow(Function<? super E, ? extends RuntimeException> exceptionSupplier) throws X {
        if (data != null) {
            return data;
        } else {
            throw exceptionSupplier.apply(error);
        }
    }

    public U orElseThrow() {
        if (data == null) {
            throw new NoSuchElementException("No value present");
        }
        return data;
    }
}
