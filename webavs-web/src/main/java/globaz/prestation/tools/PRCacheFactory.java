/*
 * Cr�� le 9 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une factory pour les caches simples, fonctionnant en interne avec une map.
 * </p>
 * 
 * <p>
 * Les caches sont synchronis�s pour l'ecriture mais pas pour la lecture (plus rapide). Le cache maintient les �l�ments
 * dans l'ordre de leur derni�r acc�s. Dans le cas d'un d�passement de la taille du cache, l'element qui n'a pas ete lu
 * depuis le plus longtemps est ejecte.
 * </p>
 * 
 * <p>
 * Le risque d'erreur en cas d'acces concurrentiel est th�oriquement infime, pratiquement nul.
 * </p>
 * 
 * <p>
 * Apr�s le d�passement du time-out, la valeur est egalement ejectee du cache.
 * </p>
 * 
 * @author vre
 */
public class PRCacheFactory {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * impl�mentation de base de IPRCache bas�e en interne sur une map. pour acc�l�rer le cache, seuls les �critures
     * dans la map sont synchronis�s. le risque d'acces concurrent est faible
     * 
     * lors du d�passement de la taille du cache, la valeur utilis�e la moins r�cemment est enlev�e du cache (Least
     * Recently Used).
     * 
     * @author vre
     */
    private static class IPRCacheImpl implements IPRCache {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private class ValueWrapper {

            // ~ Instance fields
            // ----------------------------------------------------------------------------------------

            Object key;

            boolean moving = false;
            ValueWrapper next = this;

            ValueWrapper previous = this;
            boolean timedOut = false;

            private long timeStamp;
            Object value;

            // ~ Constructors
            // -------------------------------------------------------------------------------------------

            /**
             * Cr�e une nouvelle instance de la classe ValueWrapper.
             * 
             * @param key
             *            DOCUMENT ME!
             * @param value
             *            DOCUMENT ME!
             */
            public ValueWrapper(Object key, Object value) {
                reset(key, value);
            }

            // ~ Methods
            // ------------------------------------------------------------------------------------------------

            /**
             * getter pour l'attribut value et met a jour le time stamp.
             * 
             * @param key
             *            DOCUMENT ME!
             * 
             * @return la valeur courante de l'attribut value
             */
            public Object getValue(Object key) {
                Object retValue = value;

                if (key.equals(this.key)) {
                    if (timeStamp != 0) {
                        timeStamp = System.currentTimeMillis();
                    }

                    return retValue;
                } else {
                    return null;
                }
            }

            /**
             * getter pour l'attribut timed out
             * 
             * @return la valeur courante de l'attribut timed out
             */
            public boolean isTimedOut() {
                if (timedOut) {
                    return true;
                }

                if (timeOut != 0) {
                    if ((System.currentTimeMillis() - timeStamp) > timeOut) {
                        synchronized (cache) {
                            key = null;
                            value = null;
                            timedOut = true;

                            cache.remove(key);

                            return true;
                        }
                    }
                }

                return false;
            }

            /**
             * @throws ConcurrentModificationException
             *             DOCUMENT ME!
             */
            public void moveToMRU() {
                if (next != header) {
                    if (moving) {
                        throw new ConcurrentModificationException();
                    }

                    // si le processus perd le moniteur ICI, il existe un risque
                    // d'erreur en cas d'acces concurrentiel
                    moving = true;

                    previous.next = next;
                    next.previous = previous;

                    next = header;
                    previous = header.previous;
                    header.previous.next = this;
                    header.previous = this;

                    moving = false;
                }
            }

            /**
             * @param key
             *            DOCUMENT ME!
             * @param value
             *            DOCUMENT ME!
             * 
             * @return DOCUMENT ME!
             */
            public ValueWrapper reset(Object key, Object value) {
                this.key = key;
                this.value = value;

                if (timeOut != 0) {
                    timeStamp = System.currentTimeMillis();
                    timedOut = false;
                }

                return this;
            }
        }

        HashMap cache = null;

        private int capacity;
        private int count;
        private boolean filled;
        ValueWrapper header = new ValueWrapper(null, null); // premier et
        // dernier element

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        long timeOut;

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * Cr�e une nouvelle instance de la classe IPRCacheImpl.
         * 
         * @param capacity
         *            DOCUMENT ME!
         * @param timeOut
         *            DOCUMENT ME!
         */
        public IPRCacheImpl(int capacity, long timeOut) {
            this.capacity = capacity;
            count = capacity;
            cache = new HashMap(capacity);
            this.timeOut = timeOut;
        }

        /**
         * @see globaz.prestation.tools.IPRCache#fetch(java.lang.Object)
         */
        @Override
        public Object fetch(Object key) throws ConcurrentModificationException {
            if (key != null) {
                ValueWrapper wrapper = (ValueWrapper) cache.get(key);

                if (wrapper != null) {
                    if (!wrapper.isTimedOut()) {
                        wrapper.moveToMRU();

                        return wrapper.getValue(key);
                    }
                }
            }

            return null;
        }

        /**
         * @see globaz.prestation.tools.IPRCache#getCapacity()
         */
        @Override
        public int getCapacity() {
            return capacity;
        }

        /**
         * @see globaz.prestation.tools.IPRCache#getTimeOut()
         */
        @Override
        public long getTimeOut() {
            return timeOut;
        }

        private ValueWrapper getWrapperInstance(Object key, Object value) {
            if (filled) {
                // cache filled, reuse old wrapper
                cache.remove(header.next.key); // remove old wrapped value;

                return header.next.reset(key, value); // return reused wrapper
            } else {
                // space left in cache, create a new wrapper
                --count;
                filled = !(count > 0);

                return new ValueWrapper(key, value);
            }
        }

        /**
         * @see globaz.prestation.tools.IPRCache#setTimeOut(long)
         */
        @Override
        public void setTimeOut(long timeOut) {
            this.timeOut = timeOut;
        }

        // ~ Inner Classes
        // ----------------------------------------------------------------------------------------------

        /**
         * @see globaz.prestation.tools.IPRCache#store(java.lang.Object, java.lang.Object)
         */
        @Override
        public void store(Object key, Object value) throws ConcurrentModificationException, NullPointerException {
            if (key != null) {
                synchronized (cache) {
                    ValueWrapper wrapper = getWrapperInstance(key, value);

                    synchronized (wrapper) {
                        wrapper.moveToMRU();
                        cache.put(key, wrapper);
                    }
                }
            } else {
                throw new NullPointerException("cle du cache nulle");
            }
        }
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * @param capacity
     *            DOCUMENT ME!
     * @param timeOut
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final IPRCache createCache(int capacity, long timeOut) {
        return new IPRCacheImpl(capacity, timeOut);
    }
}
