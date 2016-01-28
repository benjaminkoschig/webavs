package globaz.ij.regles;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.db.lots.IJCompensation;
import globaz.ij.db.lots.IJCompensationManager;
import globaz.ij.db.lots.IJLot;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public final class IJLotsRegles {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Détermine si on peut générer les compensations sur le lot transmis en paramètre.
     * 
     * @param lot
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isCompensable(IJLot lot) throws Exception {
        return !lot.getCsEtat().equals(IIJLot.CS_VALIDE);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Détermine si les décisions peuvent être générées sur le lot transmis en parametre.
     * 
     * @param lot
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final boolean isGenererDecisionPermis(IJLot lot) throws Exception {
        return lot.getCsEtat().equals(IIJLot.CS_COMPENSE);
    }

    /**
     * Remet un lot dans l'etat ouvert.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param lot
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final void setEtatOuvert(BSession session, BITransaction transaction, IJLot lot) throws Exception {
        IJCompensationManager compensationManager = new IJCompensationManager();

        // Mieux vaut prévenir que guérir....
        if (IIJLot.CS_VALIDE.equals(lot.getCsEtat())) {
            throw new Exception(session.getLabel("MAJ_LOT_VALIDE_ERR"));
        }

        compensationManager.setSession(session);
        compensationManager.setForIdLot(lot.getIdLot());
        compensationManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < compensationManager.size(); i++) {
            ((IJCompensation) compensationManager.getEntity(i)).delete(transaction);
        }

        lot.setCsEtat(IIJLot.CS_OUVERT);
        lot.update(transaction);
    }

    private IJLotsRegles() {
    }
}
