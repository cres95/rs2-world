package io.github.cres95.rs2world.util;

import java.util.Arrays;

class StandardArrayWrapper<E> extends AbstractArrayWrapper<E> {

    StandardArrayWrapper(E[] elements) {
        super(elements);
    }

    @Override
    public int add(E e) {
        int emptySlot = findEmpty();
        if (emptySlot > -1) {
            elements[emptySlot] = e;
        }
        return emptySlot;
    }

    @Override
    public void set(int index, E e) {
        elements[index] = e;
    }

    @Override
    public void clear() {
        Arrays.fill(elements, null);
    }

    @Override
    public E replace(int index, E e) {
        E previous = elements[index];
        elements[index] = e;
        return previous;
    }
}
