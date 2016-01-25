package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * <p>
 * Une classe de base pour les actions qui ont un délai de paiement (exemple les rappels et les sommations.
 * </p>
 * 
 * @author vre
 */
public abstract class COAbstractDelaiPaiementAction extends COAbstractEnvoyerDocument {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    protected String dateDelaiPaiement;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param contentieux
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @throws COTransitionException
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        super._validate(contentieux, transaction);
        if (isEnvoyerDocument() && JadeStringUtil.isEmpty(dateDelaiPaiement)) {
            throw new COTransitionException("AQUILA_DATE_DELAI_PAIEMENT", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_DATE_DELAI_PAIEMENT"));
        }
    }

    /**
     * getter pour l'attribut date delai paiement.
     * 
     * @return la valeur courante de l'attribut date delai paiement
     */
    public String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    /**
     * setter pour l'attribut date delai paiement.
     * 
     * @param dateDelaiPaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDelaiPaiement(String dateDelaiPaiement) {
        this.dateDelaiPaiement = dateDelaiPaiement;
    }
}
