package globaz.babel.api.helper;

import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class ICTListeTextesImpl implements ICTListeTextes {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private class EntrySetIterator implements Iterator {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private Iterator entries;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private EntrySetIterator(Iterator entries) {
            this.entries = entries;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public boolean hasNext() {
            return entries.hasNext();
        }

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public Object next() {
            return ((Map.Entry) entries.next()).getValue();
        }

        /**
         * @throws UnsupportedOperationException
         *             DOCUMENT ME!
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("opération non supportée par cet iterator");
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private TreeMap textes = new TreeMap();

    /**
     * @param texte
     *            DOCUMENT ME!
     * 
     * @throws NumberFormatException
     *             DOCUMENT ME!
     */
    public void addTexte(ICTTexte texte) throws NumberFormatException {
        textes.put(new Integer(texte.getPosition()), texte);
    }

    /**
     * getter pour l'attribut texte
     * 
     * @param idPosition
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut texte
     * 
     * @throws IndexOutOfBoundsException
     *             s'il n'y a pas d'elements a la position idPosition
     */
    @Override
    public ICTTexte getTexte(int idPosition) {
        ICTTexte retValue = (ICTTexte) textes.get(new Integer(idPosition));

        if (retValue == null) {
            throw new IndexOutOfBoundsException("pas de texte a la position: " + idPosition);
        }

        return retValue;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public Iterator iterator() {
        return new EntrySetIterator(textes.entrySet().iterator());
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public int size() {
        return textes.size();
    }
}
