import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyHashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private List<List<Entry<K, V>>> buckets;
    private int size;
    private int capacity;
    private double loadFactor;

    public MyHashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashTable(int capacity, double loadFactor) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        if (loadFactor <= 0 || loadFactor >= 1) {
            throw new IllegalArgumentException("Load factor must be between 0 and 1");
        }
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            this.buckets.add(new LinkedList<>());
        }
        this.size = 0;
    }

    public void put(K key, V value) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = buckets.get(index);
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;
        if ((double) size / capacity > loadFactor) {
            resize();
        }
    }

    public V get(K key) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = buckets.get(index);
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public void remove(K key) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = buckets.get(index);
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return;
            }
        }
    }

    public int size() {
        return size;
    }

    private int getIndex(K key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode % capacity);
    }

    private void resize() {
        int newCapacity = capacity * 2;
        List<List<Entry<K, V>>> newBuckets = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newBuckets.add(new LinkedList<>());
        }
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = Math.abs(entry.key.hashCode() % newCapacity);
                newBuckets.get(newIndex).add(entry);
            }
        }
        capacity = newCapacity;
        buckets = newBuckets;
    }

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
