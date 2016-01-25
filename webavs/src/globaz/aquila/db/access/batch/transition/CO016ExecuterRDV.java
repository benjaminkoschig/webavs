package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.print.CO14RequisitionDeVente;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author kurkus, 30 nov. 04
 */
public class CO016ExecuterRDV extends COAbstractEnvoyerDocument {
    private String dateDecision = "";
    private String dateJugement = "";
    private String decisionMainlevee = "";
    private boolean decisionMainleveeChecked = false;
    protected COHistoriqueService historiqueService = COServiceLocator.getHistoriqueService();
    private String jugementMainlevee = "";
    private boolean jugementMainleveeChecked = false;
    private String observationTexteLibre = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {

        StringBuffer observation = new StringBuffer("");
        if (isJugementMainleveeChecked() && !JadeStringUtil.isEmpty(getDateJugement())) {
            observation.append(getJugementMainlevee() + " " + getDateJugement() + "\n");
        }
        if (isDecisionMainleveeChecked() && !JadeStringUtil.isEmpty(getDateDecision())) {
            observation.append(getDecisionMainlevee() + " " + getDateDecision() + "\n");
        }
        if (!JadeStringUtil.isEmpty(getObservationTexteLibre())) {
            observation.append(getObservationTexteLibre() + "\n");
        }

        // Génération et envoi du document
        try {
            CO14RequisitionDeVente rdv = new CO14RequisitionDeVente(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rdv.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rdv.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            rdv.setObservation(observation.toString());
            rdv.addContentieux(contentieux);
            rdv.setTaxes(taxes);
            rdv.setIdEtapeInfoConfigToValeur(etapeInfos);
            this._envoyerDocument(contentieux, rdv);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        super._validate(contentieux, transaction);
        // Test des préconditions
        _validerSolde(contentieux);
        _validerEcheance(contentieux);

        // on ne peut effectuer une réquisition de vente pour une saisie sur
        // salaire
        String typeSaisie = null;

        for (Iterator infoIter = etapeInfosParLibelle.entrySet().iterator(); infoIter.hasNext();) {
            Map.Entry entry = (Map.Entry) infoIter.next();

            if (COEtapeInfoConfig.CS_TYPE_SAISIE.equals(entry.getKey())) {
                typeSaisie = (String) entry.getValue();

                break;
            }
        }

        if (COEtapeInfoConfig.CS_TYPE_SAISIE_SALAIRE.equals(typeSaisie)) {
            throw new COTransitionException("AQUILA_RDV_SALAIRE_INTERDIT", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_RDV_SALAIRE_INTERDIT"));
        }
        try {
            COHistorique historiqueRDV = historiqueService.getHistoriqueForLibEtapeTypeSaisie(transaction.getSession(),
                    contentieux, ICOEtape.CS_PV_DE_SAISIE_SAISI, typeSaisie);
            if (historiqueRDV == null) {
                throw new COTransitionException("AQUILA_TYPE_SAISIE_NON_TROUVE", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_TYPE_SAISIE_NON_TROUVE"));
            }
        } catch (Exception e) {
            throw new COTransitionException("AQUILA_TYPE_SAISIE_NON_TROUVE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_TYPE_SAISIE_NON_TROUVE"));
        }
    }

    /**
     * @return the dateDecision
     */
    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return the dateJugement
     */
    public String getDateJugement() {
        return dateJugement;
    }

    /**
     * @return the decisionMainlevee
     */
    public String getDecisionMainlevee() {
        return decisionMainlevee;
    }

    /**
     * @return the jugementMainlevee
     */
    public String getJugementMainlevee() {
        return jugementMainlevee;
    }

    /**
     * @return the observationTexteLibre
     */
    public String getObservationTexteLibre() {
        return observationTexteLibre;
    }

    /**
     * @return the decisionMainleveeChecked
     */
    public boolean isDecisionMainleveeChecked() {
        return decisionMainleveeChecked;
    }

    /**
     * @return the jugementMainleveeChecked
     */
    public boolean isJugementMainleveeChecked() {
        return jugementMainleveeChecked;
    }

    /**
     * @param dateDecision
     *            the dateDecision to set
     */
    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    /**
     * @param dateJugement
     *            the dateJugement to set
     */
    public void setDateJugement(String dateJugement) {
        this.dateJugement = dateJugement;
    }

    /**
     * @param decisionMainlevee
     *            the decisionMainlevee to set
     */
    public void setDecisionMainlevee(String decisionMainlevee) {
        this.decisionMainlevee = decisionMainlevee;
    }

    /**
     * @param decisionMainleveeChecked
     *            the decisionMainleveeChecked to set
     */
    public void setDecisionMainleveeChecked(boolean decisionMainleveeChecked) {
        this.decisionMainleveeChecked = decisionMainleveeChecked;
    }

    /**
     * @param jugementMainlevee
     *            the jugementMainlevee to set
     */
    public void setJugementMainlevee(String jugementMainlevee) {
        this.jugementMainlevee = jugementMainlevee;
    }

    /**
     * @param jugementMainleveeChecked
     *            the jugementMainleveeChecked to set
     */
    public void setJugementMainleveeChecked(boolean jugementMainleveeChecked) {
        this.jugementMainleveeChecked = jugementMainleveeChecked;
    }

    /**
     * @param observationTexteLibre
     *            the observationTexteLibre to set
     */
    public void setObservationTexteLibre(String observationTexteLibre) {
        this.observationTexteLibre = observationTexteLibre;
    }

}
