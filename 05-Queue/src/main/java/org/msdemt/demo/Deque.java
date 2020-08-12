package org.msdemt.demo;

import org.msdemt.demo.list.LinkedList;
import org.msdemt.demo.list.List;

/**
 * 使用双向链表实现双端队列
 */
public class Deque<E> {

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

    /**
     * 队尾入队列
     *
     * @param element
     */
    public void enQueueRear(E element) {
        list.add(element);
    }

    /**
     * 队头出队列
     *
     * @return
     */
    public E deQueueFront() {
        return list.remove(0);
    }

    /**
     * 队头入队列
     *
     * @param element
     */
    public void enQueueFront(E element) {
        list.add(0, element);
    }

    /**
     * 队尾出队列
     *
     * @return
     */
    public E deQueueRear() {
        return list.remove(list.size() - 1);
    }

    public E front() {
        return list.get(0);
    }

    public E rear() {
        return list.get(list.size() - 1);
    }

}
