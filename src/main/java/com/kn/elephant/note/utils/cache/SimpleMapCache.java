package com.kn.elephant.note.utils.cache;

import java.util.Set;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Kamil Nadłonek on 21-01-2017 email:kamilnadlonek@gmail.com
 */
@Log4j2
public class SimpleMapCache<K, T> {

    private long timeToLive;
    // seconds
    private static long timerInterval = 300;
    private final LRUMap<K, CacheObject> cacheMap;

    @Data
    protected class CacheObject {
        private long lastAccessed = System.currentTimeMillis();
        private T value;

        private CacheObject(T value) {
            this.value = value;
        }
    }

    public SimpleMapCache(final long timeToLiveSeconds, final int maxItems) {
        this.timeToLive = timeToLiveSeconds * 1000;
        cacheMap = new LRUMap<>(maxItems);

        if (this.timeToLive > 0 && timerInterval > 0) {

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(timerInterval * 1000);
                    } catch (InterruptedException ex) {
                    }
                    cleanup();
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    public void put(K key, T value) {
        log.debug("Add cache:" + key);
        synchronized (cacheMap) {
            cacheMap.put(key, new CacheObject(value));
        }
    }

    public T get(K key) {
        synchronized (cacheMap) {
            CacheObject c = cacheMap.get(key);

            if (c == null)
                return null;
            else {
                c.setLastAccessed(System.currentTimeMillis());
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    public K lastKey() {
        synchronized (cacheMap) {
            return cacheMap.lastKey();
        }
    }

    public Set<K> getKeys() {
        synchronized (cacheMap) {
            return  cacheMap.keySet();
        }
    }

    private void cleanup() {
        long now = System.currentTimeMillis();
        synchronized (cacheMap) {
            MapIterator<K, CacheObject> itr = cacheMap.mapIterator();
            CacheObject c;

            while (itr.hasNext()) {
                K next = itr.next();
                c = itr.getValue();
                if (c != null && (now > (timeToLive + c.getLastAccessed()))) {
                    itr.remove();
                }
            }
        }
    }

    void clearCache() {
        synchronized (cacheMap) {
            cacheMap.clear();
        }
    }
}
