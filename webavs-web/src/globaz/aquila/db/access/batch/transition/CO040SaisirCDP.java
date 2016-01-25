package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 17-nov-2004
 */
public class CO040SaisirCDP extends COTransitionAction {

    /** La date de reception du CDP */
    private String dateReceptionCDP = "";
    /** Le numéro de poursuite */
    private String numPoursuite = "";
    /** si il y'a opposition */
    private String opposition = "";

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // @TODO Exécuter scénario ?
        if (!JadeStringUtil.isEmpty(numPoursuite)) {
            try {
                contentieux.setNumPoursuite(numPoursuite);
            } catch (Exception e) {
                throw new COTransitionException(e);
            }
        }
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        if (JadeStringUtil.isEmpty(numPoursuite)) {
            throw new COTransitionException("AQUILA_ERR_CO040_NUM_POURSUITE_VIDE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_ERR_CO040_NUM_POURSUITE_VIDE"));
        }
        if (JadeStringUtil.isEmpty(getDateExecution())) {
            throw new COTransitionException("AQUILA_ERR_CO040_DATE_RECEPTION_VIDE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_ERR_CO040_DATE_RECEPTION_VIDE"));
        }

        JACalendar cal = new JACalendarGregorian();

        try {
            if (cal.compare(getDateExecution(), dateReceptionCDP) == JACalendar.COMPARE_FIRSTLOWER) {
                throw new COTransitionException("AQUILA_DATENOTIFICATION_AVANT_DATEEXECUTION",
                        COActionUtils.getMessage(contentieux.getSession(),
                                "AQUILA_DATENOTIFICATION_AVANT_DATEEXECUTION"));
            }
        } catch (JAException e1) {
            throw new COTransitionException(e1);
        }

        // @TODO Autres pré conditions ?
        // si opposition==on, la case est cochée.
        // si opposition=="", la case est pas cochée.
        if (!JadeStringUtil.isEmpty(opposition)) {
            // la case est cochée, on sort du scénario habituel
            // je change l'étape suivante
            COEtape etape = new COEtape();
            etape.setAlternateKey(COEtape.ALT_KEY_LIB_ETAPE);
            etape.setLibEtape(ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION);
            etape.setIdSequence(contentieux.getIdSequence());
            try {
                etape.retrieve(transaction);
            } catch (Exception e) {
                throw new COTransitionException("AQUILA_IMPOSSIBLE_CHARGER_ETAPE_CDP_OPP", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_IMPOSSIBLE_CHARGER_ETAPE_CDP_OPP"));
            }
            getTransition().setIdEtapeSuivante(etape.getIdEtape());
        }
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateReceptionCDP() {
        return dateReceptionCDP;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getNumPoursuite() {
        return numPoursuite;
    }

    public String getOpposition() {
        return opposition;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateReceptionCDP(String string) {
        dateReceptionCDP = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setNumPoursuite(String string) {
        numPoursuite = string;
    }

    public void setOpposition(String string) {
        opposition = string;
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_annuler(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.aquila.db.access.poursuite.COHistorique, globaz.globall.db.BTransaction)
     */
    // protected void _annuler(COContentieux contentieux, COHistorique
    // historique, BTransaction transaction) throws COTransitionException {
    // }

}
