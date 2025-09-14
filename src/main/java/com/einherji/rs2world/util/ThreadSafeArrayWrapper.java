package com.einherji.rs2world.util;

import java.util.Objects;

public class ThreadSafeArrayWrapper<E> extends ArrayWrapper<E> {

    private final Object mutex = new Object();

    ThreadSafeArrayWrapper(E[] elements) {
        super(elements);
    }

    public static <E> ThreadSafeArrayWrapper<E> wrap(E[] array) {
        Objects.requireNonNull(array);
        return new ThreadSafeArrayWrapper<>(array);
    }

    @Override
    public int place(E e) {
        synchronized (mutex) {
            return super.place(e);
        }
    }
}
