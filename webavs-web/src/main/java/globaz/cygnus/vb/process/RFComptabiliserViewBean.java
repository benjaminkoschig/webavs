/*
 * Créé le 03 sept. 07
 */
package globaz.cygnus.vb.process;

import globaz.corvus.properties.REProperties;
import globaz.osiris.db.ordres.CAOrganeExecution;
import globaz.osiris.db.ordres.CAOrganeExecutionManager;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Vector;
import ch.globaz.common.properties.PropertiesException;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public class RFComptabiliserViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateComptable = "";
    private String dateEcheancePaiement = "";
    // private String dateSurDocument = "";
    private String descriptionLot = null;
    private String eMailAddress = "";
    private String idDecision = "";

    private String idGestionnaire = "";
    private String idLot = "";
    private String idOrganeExecution = "";
    private boolean isSimulation = false;
    private String numeroDecision = "";
    private String numeroOG = "";
    private String radioCreationDecision = "";
    private String radioSimulationCreationDecision = "";
    private String typeValidation = "";

    // SEPA iso20002
    private String isoGestionnaire = "";
    private String isoHighPriority = "";

    public RFComptabiliserViewBean() throws PropertiesException {
        setIdOrganeExecution(getOrganeExecProperty());
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDateComptable() {
        return dateComptable;
    }

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }

    public String getDescriptionLot() {
        return descriptionLot;
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

    public String getIdLot() {
        return idLot;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {idOrganeExecution, description} défini pour ce view bean.
     * 
     * 
     * @return Vecteur de tableau à 2 entrées {idOrganeExecution, description}
     */
    public Vector<String[]> getOrganesExecution() {

        Vector<String[]> result = new Vector<String[]>();
        CAOrganeExecution organeExecution = null;
        CAOrganeExecutionManager mgr = new CAOrganeExecutionManager();
        mgr.setSession(getSession());
        mgr.setForIdTypeTraitementOG(true);
        mgr.changeManagerSize(0);
        try {
            mgr.find();
        } catch (Exception e) {
            return result;
        }

        for (int i = 0; i < mgr.size(); i++) {
            organeExecution = (CAOrganeExecution) mgr.getEntity(i);

            result.add(new String[] { organeExecution.getIdOrganeExecution(), organeExecution.getNom(),
                    organeExecution.getCSTypeTraitementOG() });
        }
        return result;
    }

    public String getRadioCreationDecision() {
        return radioCreationDecision;
    }

    public String getRadioSimulationCreationDecision() {
        return radioSimulationCreationDecision;
    }

    public String getTypeValidation() {
        return typeValidation;
    }

    public boolean isSimulation() {
        return isSimulation;
    }

    @Override
    public void retrieve() throws Exception {
    }

    public void setDateComptable(String dateComptable) {
        this.dateComptable = dateComptable;
    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    public void setDescriptionLot(String descriptionLot) {
        this.descriptionLot = descriptionLot;
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

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public void setRadioCreationDecision(String radioCreationDecision) {
        this.radioCreationDecision = radioCreationDecision;
    }

    public void setRadioSimulationCreationDecision(String radioSimulationCreationDecision) {
        this.radioSimulationCreationDecision = radioSimulationCreationDecision;
    }

    public void setSimulation(boolean isSimulation) {
        this.isSimulation = isSimulation;
    }

    public void setTypeValidation(String typeValidation) {
        this.typeValidation = typeValidation;
    }

    public String getIsoGestionnaire() {
        if (isoGestionnaire.isEmpty()) {
            return getSession().getUserName();
        }
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHighPriority() {
        return isoHighPriority;
    }

    public void setIsoHighPriority(String isoHighPriority) {
        this.isoHighPriority = isoHighPriority;
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

    private String getOrganeExecProperty() throws PropertiesException {
        return REProperties.ORGANE_EXECUTION_PAIEMENT.getValue();
    }

}
