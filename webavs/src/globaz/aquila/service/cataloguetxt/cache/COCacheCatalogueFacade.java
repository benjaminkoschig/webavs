package globaz.aquila.service.cataloguetxt.cache;

import java.util.HashMap;

public class COCacheCatalogueFacade {
    private static COCacheCatalogueFacade instance = null;

    /**
     * @return l'instance unique de COCacheCatalogueFacade
     */
    public static COCacheCatalogueFacade getInstance() {
        if (instance == null) {
            instance = new COCacheCatalogueFacade();
        }
        return instance;
    }

    private HashMap container = null;

    /**
     * Singleton
     */
    private COCacheCatalogueFacade() {
        super();
        container = new HashMap();
    }

    /**
     * @param key
     * @return le catalogue correspondant à la clé.
     * @throws Exception
     */
    public COCacheCatalogue getCache(Object key) throws Exception {
        if (!container.containsKey(key)) {
            throw new Exception(getClass().getName() + " - Unable to get cache, the cache is not started for uuid : "
                    + key);
        }
        return (COCacheCatalogue) container.get(key);
    }

    /**
     * @param key
     * @throws Exception
     */
    public void releaseCache(Object key) throws Exception {
        if (key == null) {
            throw new Exception(getClass().getName() + " - Unable to release cache, the uuid passed is null!");
        }
        if (!container.containsKey(key)) {
            throw new Exception(getClass().getName()
                    + " - Unable to release cache, the cache is not started for uuid : " + key);
        }
        container.remove(key);
    }

    /**
     * @param key
     *            uuid
     * @throws Exception
     */
    public void startCache(Object key) throws Exception {
        if (key == null) {
            throw new Exception(getClass().getName() + " - Unable to start cache, the uuid passed is null!");
        }
        if (container.containsKey(key)) {
            throw new Exception(getClass().getName()
                    + " - Unable to start cache, the cache is already started for uuid : " + key);
        }
        container.put(key, COCacheCatalogue.newInstance());
    }
}
