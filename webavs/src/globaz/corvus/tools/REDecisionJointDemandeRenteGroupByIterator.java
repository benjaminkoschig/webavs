package globaz.corvus.tools;

import globaz.corvus.db.prestations.IREPrestatationsDecisionsRCListIterator;
import globaz.corvus.db.prestations.IREPrestatationsDecisionsRCListViewBean;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class REDecisionJointDemandeRenteGroupByIterator implements IREPrestatationsDecisionsRCListIterator {

    private IREPrestatationsDecisionsRCListViewBean courant = null;
    private boolean hasNext = false;
    private Iterator<IREPrestatationsDecisionsRCListViewBean> iterator;
    private String precendantId = "";

    public REDecisionJointDemandeRenteGroupByIterator(Iterator<IREPrestatationsDecisionsRCListViewBean> iterator)
            throws NullPointerException {
        reset(iterator);
    }

    @Override
    public boolean hasNext() {
        if (!hasNext && iterator.hasNext()) {
            if (courant != null) {
                precendantId = ((REDecisionJointDemandeRente) courant).getIdDecision();
            }

            courant = iterator.next();
            hasNext = true;
        }

        return hasNext;
    }

    @Override
    public boolean isNextSameEntity() {
        if (hasNext()) {
            return precendantId.equals(((REDecisionJointDemandeRente) courant).getIdDecision());
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
        throw new UnsupportedOperationException("cet itérateur ne permet pas d'enlever des éléments");
    }

    public void reset(Iterator<IREPrestatationsDecisionsRCListViewBean> iterator) {
        if ((iterator == null)) {
            throw new NullPointerException("l'argument iterator est null");
        }

        this.iterator = iterator;
    }
}
