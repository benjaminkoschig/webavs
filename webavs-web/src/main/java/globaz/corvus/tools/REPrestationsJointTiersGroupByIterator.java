package globaz.corvus.tools;

import globaz.corvus.db.prestations.IREPrestatationsDecisionsRCListIterator;
import globaz.corvus.db.prestations.IREPrestatationsDecisionsRCListViewBean;
import globaz.corvus.db.prestations.REPrestationsJointTiers;
import globaz.corvus.vb.prestations.REPrestationsJointTiersViewBean;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class REPrestationsJointTiersGroupByIterator implements IREPrestatationsDecisionsRCListIterator {

    private REPrestationsJointTiersViewBean courant = null;
    private boolean hasNext = false;
    private Iterator<REPrestationsJointTiersViewBean> iterator;
    private String precendantId = "";

    public REPrestationsJointTiersGroupByIterator(Iterator<REPrestationsJointTiersViewBean> iterator)
            throws NullPointerException {
        reset(iterator);
    }

    /**
     * Vrai si l'it�rateur a encore des �l�ments.
     * 
     * @return vrai si l'it�rateur a encore des �l�ments.
     */
    @Override
    public boolean hasNext() {
        if (!hasNext && iterator.hasNext()) {
            if (courant != null) {
                precendantId = ((REPrestationsJointTiers) courant).getIdDecision();
            }

            courant = iterator.next();
            hasNext = true;
        }

        return hasNext;
    }

    /**
     * @return vrai si l'element suivant repr�sente le m�me tiers
     */
    @Override
    public boolean isNextSameEntity() {
        if (hasNext()) {
            return precendantId.equals(((REPrestationsJointTiers) courant).getIdDecision());
        }

        return false;
    }

    @Override
    public IREPrestatationsDecisionsRCListViewBean next() {
        if (hasNext()) {
            hasNext = false;

            return courant;
        } else {
            throw new NoSuchElementException("il n'y a plus d'elements disponible avec cet iterateur");
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("cet it�rateur ne permet pas d'enlever des �l�ments");
    }

    /**
     * Reset l'�tat de cet iterateur avec les nouveaux arguments.
     * 
     * @param iterator
     *            un nouvel iterateur
     * 
     * @throws NullPointerException
     *             si l'argument iterator est null
     */
    public void reset(Iterator<REPrestationsJointTiersViewBean> iterator) {
        if ((iterator == null)) {
            throw new NullPointerException("l'argument iterator est null");
        }

        this.iterator = iterator;
    }
}
