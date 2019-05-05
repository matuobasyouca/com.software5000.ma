package com.software5000.ma.aliyunSms;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 固定长度队列，用来存放每次发送短信的时间戳
 * Created by wujin on 2017/1/8.
 */
public class LimitQueue<E> implements Queue<E> {
    //队列长度
    private int limit;

    Queue<E> queue = new LinkedList<E>();

    public LimitQueue(int limit){
        this.limit = limit;
    }

    /**
     * 入队
     * @param e
     */
    @Override
    public boolean offer(E e){
        if(queue.size() >= limit){
            //如果超出长度,入队时,先出队
            queue.poll();
        }
        return queue.offer(e);
    }

    /**
     * 出队
     * @return
     */
    @Override
    public E poll() {
        return queue.poll();
    }

    /**
     * 获取队列
     * @return
     */
    public Queue<E> getQueue(){
        return queue;
    }

    /**
     * 获取限制大小
     * @return
     */
    public int getLimit(){
        return limit;
    }

    @Override
    public boolean add(E e) {
        return queue.add(e);
    }

    @Override
    public E element() {
        return queue.element();
    }

    @Override
    public E peek() {
        return queue.peek();
    }

    @Override
    public boolean isEmpty() {
        return queue.size() == 0 ? true : false;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public E remove() {
        return queue.remove();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return queue.addAll(c);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return queue.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return queue.retainAll(c);
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    public E lastElement(){
        return (E)toArray()[size()-1];
    }

    public static String getLimitQueueInfo( LimitQueue lqueue){
        String info = "";
        for (Object string : lqueue.getQueue()) {
            info+=(string.toString()+",");
        }
        return info;
    }

    public static void main(String[] args) {
        LimitQueue<String> lqueue = new LimitQueue<String>(3);

        lqueue.offer("1");
        System.out.println(lqueue.peek()+":"+lqueue.lastElement());
        lqueue.offer("2");
        System.out.println(lqueue.peek()+":"+lqueue.lastElement());
        lqueue.offer("3");
        System.out.println(lqueue.peek()+":"+lqueue.lastElement());
        lqueue.offer("4");
        System.out.println(lqueue.peek()+":"+lqueue.lastElement());
        //1因超出队列大小限制已自动出队,输出结果为2,3,4
        for (String string : lqueue.getQueue()) {
            System.out.print(string+",");
        }
    }
}
