/*
 * Cr?? le 11 oct. 06
 * 
 * Pour changer le mod?le de ce fichier g?n?r?, allez ? : Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.tools;

import globaz.cepheus.db.demande.DODemandePrestations;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author bsc
 * 
 *         Pour changer le mod?le de ce commentaire de type g?n?r?, allez ? :
 *         Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code et commentaires
 */
public class DODemandePrestationGroupByIterator implements Iterator {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Object courant = null;
    private boolean hasNext = false;
    private Iterator iterator;
    private String precendantTiersId = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr?e une nouvelle instance de la classe DODemandePrestationGroupByIterator.
     * 
     * @param iterator
     *            DOCUMENT ME!
     * 
     * @throws NullPointerException
     *             si un des arguments est null
     */
    public DODemandePrestationGroupByIterator(Iterator iterator) throws NullPointerException {
        reset(iterator);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Vrai si l'it?rateur a encore des ?l?ments.
     * 
     * @return vrai si l'it?rateur a encore des ?l?ments.
     */
    @Override
    public boolean hasNext() {
        if (!hasNext && iterator.hasNext()) {
            if (courant != null) {
                precendantTiersId = ((DODemandePrestations) courant).getIdTiers();
            }

            courant = iterator.next();
            hasNext = true;
        }

        return hasNext;
    }

    /**
     * 
     * @return vrai si l'element suivant repr?sente le meme tiers
     */
    public boolean isNextSameEntity() {
        if (hasNext()) {
            return precendantTiersId.equals(((DODemandePrestations) courant).getIdTiers());
        }

        return false;
    }

    /**
     * l'?l?ment suivant
     * 
     * @return DOCUMENT ME!
     * 
     * @throws NoSuchElementException
     *             S'il n'y a plus d'?l?ment suivant
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
        throw new UnsupportedOperationException("cet it?rateur ne permet pas d'enlever des ?l?ments");
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