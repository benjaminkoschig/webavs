package globaz.osiris.utils;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import java.util.HashMap;
import java.util.Map;

public class CACachedManager<T extends BEntity> {
    private Map<String, T> cache = null;
    private BManager manager = null;

    public CACachedManager(BManager manager) {
        this.manager = manager;
        this.cache = this._buildCacheMap();
    }

    private synchronized Map<String, T> _buildCacheMap() {
        Map<String, T> cache = new HashMap<String, T>();
        for (T e : (Iterable<T>) this.manager) {
            cache.put(e.getId(), e);
        }
        return cache;
    }

    public synchronized T findById(String id) {
        return this.cache.get(id);
    }

    public synchronized Map<String, T> getCache() {
        return this.cache;
    }

    public synchronized void setCache(Map<String, T> cache) {
        this.cache = cache;
    }

}
