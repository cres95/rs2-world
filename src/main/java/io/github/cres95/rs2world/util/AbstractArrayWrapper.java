package io.github.cres95.rs2world.util;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

abstract class AbstractArrayWrapper<E> implements ArrayWrapper<E> {

    final E[] elements;

    protected AbstractArrayWrapper(E[] elements) {
        this.elements = elements;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (E element : elements) if (element != null) action.accept(element);

    }

    @Override
    public int add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int find(E e) {
        Assert.notNull(e, "Use findEmpty() instead of find(null)");
        for (int i = 0; i < elements.length; i++) {
            if (e.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public int findEmpty() {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) return i;
        }
        return -1;
    }

    @Override
    public E find(Predicate<? super E> predicate) {
        Assert.notNull(predicate, "predicate may not be null");
        for (E element : elements) {
            if (element != null && predicate.test(element)) return element;
        }
        return null;
    }

    @Override
    public List<E> findAll(Predicate<? super E> predicate) {
        Assert.notNull(predicate, "predicate may not be null");
        List<E> result = new ArrayList<>(elements.length);
        for (E element : elements) {
            if (element != null && predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }

    @Override
    public void set(int index, E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int index) {
        return elements[index];
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E replace(int index, E e) {
        throw new UnsupportedOperationException();
    }
}
