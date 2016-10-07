/**
 * 
 */
package globaz.pegasus.vb.droit;

// globaz.pegasus.vb.droit.PCCorrigerDroitViewBean

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author DMA
 * 
 */
public class PCCorrigerDroitViewBean extends BJadePersistentObjectViewBean {
    private String dateSuppression = null;
    private String csMotif = null;
    private String dateAnnonce = null;
    private Droit droit = null;
    private String idDroit = null;
    private String dateDecision = null;
    private String mailProcessCompta = getSession().getUserEMail();
    private boolean isComptabilisationAuto = false;

    public PCCorrigerDroitViewBean() {
        super();
        droit = new Droit();
        dateAnnonce = "";
        dateSuppression = "";
        dateDecision = "";
    }

    /**
     * @param droit
     */
    public PCCorrigerDroitViewBean(Droit droit) {
        super();
        this.droit = droit;
    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {
    }

    public String getCsMotif() {
        if (!JadeStringUtil.isBlankOrZero(csMotif)) {
            return csMotif;
        } else {
            return IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES;
        }

    }

    /**
     * @return the dateAnnonce
     */
    public String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * @return the droit
     */
    public Droit getDroit() {
        return droit;
    }

    @Override
    public String getId() {
        return droit.getId();
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(droit.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        droit = PegasusServiceLocator.getDroitService().getCurrentVersionDroit(idDroit);
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    /**
     * @param dateAnnonce
     *            the dateAnnonce to set
     */
    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setDroit(Droit droit) {
        this.droit = droit;
    }

    @Override
    public void setId(String newId) {
        idDroit = newId;
        droit.setId(newId);
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public String getDateSuppression() {
        return dateSuppression;
    }

    public void setDateSuppression(String dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public String getCurrentUserId() {
        return getSession().getUserId();
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public void update() throws Exception {
        retrieve();
        if (IPCDroits.CS_MOTIF_DROIT_DECES.equals(csMotif)) {
            droit = PegasusServiceLocator.getDroitService().corrigerDroitEnCasDeDeces(droit, dateAnnonce, csMotif,
                    dateSuppression, dateDecision, getCurrentUserId(), isComptabilisationAuto(), mailProcessCompta);
        } else {
            droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, dateAnnonce, csMotif);
        }

    }

    public boolean isComptabilisationAuto() {
        return isComptabilisationAuto;
    }

    public boolean getIsComptabilisationAuto() {
        return isComptabilisationAuto;
    }

    public void setIsComptabilisationAuto(boolean isComptabilisationAuto) {
        this.isComptabilisationAuto = isComptabilisationAuto;
    }

    public String getMailProcessCompta() {
        return mailProcessCompta;
    }

    public void setMailProcessCompta(String mailProcessCompta) {
        this.mailProcessCompta = mailProcessCompta;
    }

}
