package globaz.prestation.tools;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui permet de filtrer les �l�ments retourn�s par un it�rateur au moyen d'un pr�dicat.
 * </p>
 * 
 * @author vre
 */
public class PRFilterIterator implements Iterator {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Object courant = null;
    private boolean hasNext = false;

    private Iterator iterator;
    private PRPredicate predicate;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe PRFilterIterator.
     * 
     * @param iterator
     *            DOCUMENT ME!
     * @param predicate
     *            DOCUMENT ME!
     * 
     * @throws NullPointerException
     *             si un des arguments est null
     */
    public PRFilterIterator(Iterator iterator, PRPredicate predicate) throws NullPointerException {
        reset(iterator, predicate);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Vrai si l'it�rateur a encore des �l�ments qui v�rifient le pr�dicat.
     * 
     * @return vrai si l'it�rateur a encore des �l�ments qui v�rifient le pr�dicat.
     */
    @Override
    public boolean hasNext() {
        while (!hasNext && iterator.hasNext()) {
            courant = iterator.next();
            hasNext = predicate.evaluer(courant);
        }

        return hasNext;
    }

    /**
     * l'�l�ment suivant
     * 
     * @return DOCUMENT ME!
     * 
     * @throws NoSuchElementException
     *             S'il n'y a plus d'�l�ment suivant
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
        throw new UnsupportedOperationException("cet it�rateur ne permet pas d'enlever des �l�ments");
    }

    /**
     * Reset l'etat de cet iterateur avec les nouveaux arguments.
     * 
     * @param iterator
     *            un nouvel iterateur
     * @param predicate
     *            un nouveau predicat
     * 
     * @throws NullPointerException
     *             si un des arguments est null
     */
    public void reset(Iterator iterator, PRPredicate predicate) {
        if ((iterator == null) || (predicate == null)) {
            throw new NullPointerException("un des arguments est null");
        }

        this.iterator = iterator;
        this.predicate = predicate;
    }
}
