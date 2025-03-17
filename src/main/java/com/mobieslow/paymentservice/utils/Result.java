package com.mobieslow.paymentservice.utils;

public class Result<U, E> {
    private final U data;
    private final E error;

    private Result(U data, E error) {
        this.data = data;
        this.error = error;
    }

    public static <U, E> Result<U, E> ok(U data) {
        return new Result<>(data, null);
    }

    public static <U, E> Result<U, E> err(E error) {
        return new Result<>(null, error);
    }

    public U getData() {
        assert data != null;
        return data;
    }

    public E getError() {
        assert error != null;
        return error;
    }

    public boolean isOk() {
        return data != null;
    }

    public String toString() {
        if (isOk()) {
            return String.format("Result(ok=%s)", data.toString());
        } else {
            return String.format("Result(err=%s)", error.toString());
        }
    }
}
