package io.github.cres95.rs2world.util;

class ConcurrentArrayWrapper<E> extends StandardArrayWrapper<E> {

    private final Object mutex;

    protected ConcurrentArrayWrapper(E[] elements) {
        super(elements);
        this.mutex = new Object();
    }

    @Override
    public int add(E e) {
        synchronized (mutex) {
            return super.add(e);
        }
    }

    @Override
    public void set(int index, E e) {
        synchronized (mutex) {
            super.set(index, e);
        }
    }

    @Override
    public void clear() {
        synchronized (mutex) {
            super.clear();
        }
    }

    @Override
    public E replace(int index, E e) {
        synchronized (mutex) {
            return super.replace(index, e);
        }
    }
}
