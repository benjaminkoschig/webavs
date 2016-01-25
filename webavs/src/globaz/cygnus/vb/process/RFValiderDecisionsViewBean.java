/*
 * Créé le 03 sept. 07
 */
package globaz.cygnus.vb.process;

import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFValiderDecisionsViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateSurDocument = "";
    private Boolean displaySendToGed = false;
    private String eMailAddress = "";
    private String idDecision = "";
    private String idGestionnaire = "";
    private Boolean miseEnGed = null;
    private String numeroDecision = "";
    private String typeValidation = "";

    private void afficherCaseEnvoiGed() throws Exception {
        if (JadeGedFacade.isInstalled()) {
            List l = null;

            if (getSession() != null) {
                String targetName = RFPropertiesUtils.gedTargetName();

                l = JadeGedFacade.getDocumentNamesList(targetName);
            } else {
                l = JadeGedFacade.getDocumentNamesList();
            }

            List<String> listeDecisionsRfm = new ArrayList<String>();
            listeDecisionsRfm.add(IPRConstantesExternes.RFM_DECISION_DE_RESTITUTION);
            listeDecisionsRfm.add(IPRConstantesExternes.RFM_DECISION_PONCTUELLE);
            listeDecisionsRfm.add(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME);
            listeDecisionsRfm.add(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI);
            listeDecisionsRfm.add(IPRConstantesExternes.RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS);
            for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                String s = (String) iterator.next();

                if (listeDecisionsRfm.contains(s)) {
                    setDisplaySendToGed(true);
                    break;
                } else {
                    setDisplaySendToGed(false);
                }
            }
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public Boolean getDisplaySendToGed() {
        return displaySendToGed;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public Boolean getMiseEnGed() {
        return miseEnGed;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getTypeValidation() {
        return typeValidation;
    }

    @Override
    public void retrieve() throws Exception {
        afficherCaseEnvoiGed();

    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDisplaySendToGed(Boolean displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setMiseEnGed(Boolean miseEnGed) {
        this.miseEnGed = miseEnGed;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setTypeValidation(String typeValidation) {
        this.typeValidation = typeValidation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }

    // Methode controlant la propriété utilisé
    public boolean validerDecisionGestionnaire() throws Exception {
        return RFPropertiesUtils.utiliserGestionnaireViewBean();
    }

}
