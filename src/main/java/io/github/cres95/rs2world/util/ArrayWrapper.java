package io.github.cres95.rs2world.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ArrayWrapper<E> {

    static <X> ArrayWrapper<X> wrapStandard(X[] array) {
        return new StandardArrayWrapper<>(array);
    }

    static <X> ArrayWrapper<X> wrapReadonly(X[] array) {
        return new ReadonlyArrayWrapper<>(array);
    }

    static <X> ArrayWrapper<X> wrapConcurrent(X[] array) {
        return new ConcurrentArrayWrapper<>(array);
    }

    void forEach(Consumer<? super E> action);

    int add(E e);

    int find(E e);

    int findEmpty();

    E find(Predicate<? super E> predicate);

    List<E> findAll(Predicate<? super E> predicate);

    void set(int index, E e);

    E get(int index);

    void clear();

    E replace(int index, E e);

}
