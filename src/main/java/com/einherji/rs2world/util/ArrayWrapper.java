package com.einherji.rs2world.util;

import java.util.Objects;

public class ArrayWrapper<E> {

    private final E[] elements;

    ArrayWrapper(E[] elements) {
        this.elements = elements;
    }

    public static <E> ArrayWrapper<E> wrap(E[] array) {
        Objects.requireNonNull(array);
        return new ArrayWrapper<>(array);
    }

    public int place(E e) {
        int emptyIndex = indexOf(null);
        if (emptyIndex >= 0) {
            elements[emptyIndex] = e;
        }
        return emptyIndex;
    }

    public int indexOf(E e) {
        if (e != null) {
            for (int i = 0; i < elements.length; i++) if (elements[i].equals(e)) return i;
        } else {
            for (int i = 0; i < elements.length; i++) if (elements[i] == null) return i;
        }
        return -1;
    }
}
