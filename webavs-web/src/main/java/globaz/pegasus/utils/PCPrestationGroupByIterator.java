package globaz.pegasus.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ch.globaz.pegasus.business.models.lot.Prestation;

public class PCPrestationGroupByIterator implements Iterator {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Object courant = null;
    private boolean hasNext = false;
    private Iterator iterator;
    private String precendantId = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REPrestationGroupByIterator.
     * 
     * @param iterator
     *            DOCUMENT ME!
     * 
     * @throws NullPointerException
     *             si un des arguments est null
     */
    public PCPrestationGroupByIterator(Iterator iterator) throws NullPointerException {
        reset(iterator);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Vrai si l'itérateur a encore des éléments.
     * 
     * @return vrai si l'itérateur a encore des éléments.
     */
    @Override
    public boolean hasNext() {
        if (!hasNext && iterator.hasNext()) {
            if (courant != null) {
                precendantId = ((Prestation) courant).getId();
            }

            courant = iterator.next();
            hasNext = true;
        }

        return hasNext;
    }

    /**
     * 
     * @return vrai si l'element suivant représente le meme tiers
     */
    public boolean isNextSameEntity() {
        if (hasNext()) {
            return precendantId.equals(((Prestation) courant).getId());
        }

        return false;
    }

    /**
     * l'élément suivant
     * 
     * @return DOCUMENT ME!
     * 
     * @throws NoSuchElementException
     *             S'il n'y a plus d'élément suivant
     */
    @Override
    public Object next() {
        if (hasNext()) {
            hasNext = false;

            return courant;
        } else {
            throw new NoSuchElementException("il n'y a plus d'elements disponible avec cet iterateur");
        }
    }

    /**
     * @throws UnsupportedOperationException
     *             DOCUMENT ME!
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("cet itérateur ne permet pas d'enlever des éléments");
    }

    /**
     * Reset l'etat de cet iterateur avec les nouveaux arguments.
     * 
     * @param iterator
     *            un nouvel iterateur
     * 
     * @throws NullPointerException
     *             si l'argument iterator est null
     */
    public void reset(Iterator iterator) {
        if ((iterator == null)) {
            throw new NullPointerException("l'argument iterator est null");
        }

        this.iterator = iterator;
    }

}
