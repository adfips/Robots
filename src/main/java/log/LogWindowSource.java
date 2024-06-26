package log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource
{
    private int m_iQueueLength;

    private BoundedLogQueue m_messages;
    private final List<WeakReference<LogChangeListener>> m_listeners;
    private volatile List<WeakReference<LogChangeListener>> m_activeListeners;


    public LogWindowSource(int iQueueLength)
    {
        m_iQueueLength = iQueueLength;
        m_messages = new BoundedLogQueue(m_iQueueLength);
        m_listeners = new ArrayList<>();
    }
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(new WeakReference<>(listener));
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            for (WeakReference<LogChangeListener> cur : m_listeners) {
                if (cur.get() == listener) {
                    m_listeners.remove(cur);
                    break;
                }
            }
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        if (m_activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    m_activeListeners = new ArrayList<>();
                    for (WeakReference<LogChangeListener> ref : m_listeners) {
                        LogChangeListener listener = ref.get();
                        if (listener != null) {
                            m_activeListeners.add(ref);
                        }
                    }
                }
            }
        }
        for (WeakReference<LogChangeListener> ref : m_activeListeners) {
            LogChangeListener listener = ref.get();
            if (listener != null) {
                listener.onLogChanged();
            }
        }
    }


    public int size()
    {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return m_messages.range(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return m_messages.all();
    }
}
