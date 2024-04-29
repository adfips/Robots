package log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс для хранения логов с ограниченным размером.
 */
public class BoundedLogQueue {
    /**
     * Максимальный размер очереди
     */
    private final int capacity;
    /**
     * Массив для хранения записей протокола
     */
    private final LogEntry[] queue;
    /**
     * Индекс начала очереди
     */
    private int head;
    /**
     * Индекс конца очереди
     */
    private int tail;
    /**
     * Блокировка для потокобезопасности
     */
    private final Lock lock = new ReentrantLock();

    /**
     * Создает новую очередь логов с заданным размером.
     *
     * @param capacity емкость очереди
     */
    public BoundedLogQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LogEntry[capacity];
        this.head = 0;
        this.tail = 0;
    }

    /**
     * Добавляет новый лог в очередь.
     *
     * @param entry новый лог для добавления
     */
    public void add(LogEntry entry) {
        lock.lock();
        try {
            queue[tail] = entry;
            tail = (tail + 1) % capacity;
            if ((tail + 1) % capacity == head)
                head = (head + 1) % capacity;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает диапазон логов из очереди.
     *
     * @param startFrom начальный индекс диапазона
     * @param count     количество логов в диапазоне
     * @return диапазон логов
     */
    public Iterable<LogEntry> range(int startFrom, int count) {
        lock.lock();
        try {
            List<LogEntry> result = new ArrayList<>();
            int index = (head + startFrom) % capacity;
            int endIndex = (index + count) % capacity;
            for (int i = 0; i < count && index != endIndex; i++) {
                result.add(queue[index]);
                index = (index + 1) % capacity;
            }
            return result;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает все логи из очереди.
     */
    public Iterable<LogEntry> all() {
        lock.lock();
        try {
            return range(0, size());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает текущее количество логов в очереди.
     */
    public int size() {
        return (tail - head + capacity) % capacity;
    }
}
