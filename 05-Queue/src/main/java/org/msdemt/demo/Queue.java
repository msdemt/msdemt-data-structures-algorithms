package org.msdemt.demo;

import org.msdemt.demo.list.LinkedList;
import org.msdemt.demo.list.List;

/**
 * 使用双向链表实现队列
 *
 * @param <E>
 */
public class Queue<E> {
    private List<E> list = new LinkedList<>();

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
    }

    public void enQueue(E element) {
        list.add(element);
    }

    public E deQueue() {
        return list.remove(0);
    }

    public E front() {
        return list.get(0);
    }
}
