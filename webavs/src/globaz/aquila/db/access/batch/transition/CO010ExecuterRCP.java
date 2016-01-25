package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO10RCPSaisie;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 17-nov-2004
 */
public class CO010ExecuterRCP extends COAbstractEnvoyerDocument {
    private String dateDecision = "";
    private String dateJugement = "";
    private String decisionMainlevee = "";
    private boolean decisionMainleveeChecked = false;
    private String jugementMainlevee = "";
    private boolean jugementMainleveeChecked = false;
    private String observationTexteLibre = "";

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {

        StringBuffer observation = new StringBuffer("");
        // Génération et envoi du document
        try {
            CO10RCPSaisie rcp = new CO10RCPSaisie(transaction.getSession());

            if (isJugementMainleveeChecked() && !JadeStringUtil.isEmpty(getDateJugement())) {
                rcp.setJugementMainLevee(getJugementMainlevee());
                rcp.setDateJugementMainLevee(getDateJugement());

            }
            if (isDecisionMainleveeChecked() && !JadeStringUtil.isEmpty(getDateDecision())) {
                rcp.setDecisionMainLevee(getDecisionMainlevee());
                rcp.setDateDecisionMainLevee(getDateDecision());
            }
            if (!JadeStringUtil.isEmpty(getObservationTexteLibre())) {
                observation.append(getObservationTexteLibre() + "\n");
            }

            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rcp.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rcp.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }

            rcp.setObservation(observation.toString());
            rcp.addContentieux(contentieux);
            rcp.setTaxes(taxes);
            this._envoyerDocument(contentieux, rcp);
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
