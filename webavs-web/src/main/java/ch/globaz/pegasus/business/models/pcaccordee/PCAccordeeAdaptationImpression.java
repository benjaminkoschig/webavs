package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;

public class PCAccordeeAdaptationImpression extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csGenrePC = null;
    private String idDecisionHeader = null;
    private String idDroit = null;
    private String idPCAccordee = null;
    private String idTiers = null;
    private String idTiersConjoint = null;
    private String idVersionDroit = null;
    private boolean isPrecedent = false; // champ non persisté, utilisé uniquement pendant l'impression de l'adaptation
    private String montantPrestationAyantDroit = null;
    private String montantPrestationConjoint = null;
    private String nomAyantDroit = null;
    private String nomConjoint = null;
    private String noVersionDroit = null;
    private String nssAyantDroit = null;
    private String nssConjoint = null;
    private ArrayList<SimplePlanDeCalcul> planCalculs = null;
    private String prenomAyantDroit = null;
    private String prenomConjoint = null;
    private String idDecision = null;

    public PCAccordeeAdaptationImpression() {
        super();
    }

    public String getCsGenrePC() {
        return csGenrePC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdDecisionHeader();
    }

    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getMontantPrestationAyantDroit() {
        return montantPrestationAyantDroit;
    }

    public String getMontantPrestationConjoint() {
        return montantPrestationConjoint;
    }

    public String getNomAyantDroit() {
        return nomAyantDroit;
    }

    public String getNomConjoint() {
        return nomConjoint;
    }

    public String getNoVersionDroit() {
        return noVersionDroit;
    }

    public String getNssAyantDroit() {
        return nssAyantDroit;
    }

    public String getNssConjoint() {
        return nssConjoint;
    }

    public ArrayList<SimplePlanDeCalcul> getPlanCalculs() {
        return planCalculs;
    }

    public SimplePlanDeCalcul getPlanRetenu() throws PCAccordeeException {
        if (planCalculs == null) {
            throw new PCAccordeeException("No plan calcul found!");
        }
        for (SimplePlanDeCalcul plan : planCalculs) {
            if (plan.getIsPlanRetenu()) {
                return plan;
            }
        }
        return null;

    }

    public String getPrenomAyantDroit() {
        return prenomAyantDroit;
    }

    public String getPrenomConjoint() {
        return prenomConjoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return null;
    }

    public boolean isPrecedent() {
        return isPrecedent;
    }

    public void setCsGenrePC(String csGenrePC) {
        this.csGenrePC = csGenrePC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        setIdDecisionHeader(id);
    }

    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setMontantPrestationAyantDroit(String montantPrestationAyantDroit) {
        this.montantPrestationAyantDroit = montantPrestationAyantDroit;
    }

    public void setMontantPrestationConjoint(String montantPrestationConjoint) {
        this.montantPrestationConjoint = montantPrestationConjoint;
    }

    public void setNomAyantDroit(String nomAyantDroit) {
        this.nomAyantDroit = nomAyantDroit;
    }

    public void setNomConjoint(String nomConjoint) {
        this.nomConjoint = nomConjoint;
    }

    public void setNoVersionDroit(String noVersionDroit) {
        this.noVersionDroit = noVersionDroit;
    }

    public void setNssAyantDroit(String nssAyantDroit) {
        this.nssAyantDroit = nssAyantDroit;
    }

    public void setNssConjoint(String nssConjoint) {
        this.nssConjoint = nssConjoint;
    }

    public void setPlanCalculs(ArrayList<SimplePlanDeCalcul> planCalculs) {
        this.planCalculs = planCalculs;
    }

    public void setPrecedent(boolean isPrecedent) {
        this.isPrecedent = isPrecedent;
    }

    public void setPrenomAyantDroit(String prenomAyantDroit) {
        this.prenomAyantDroit = prenomAyantDroit;
    }

    public void setPrenomConjoint(String prenomConjoint) {
        this.prenomConjoint = prenomConjoint;
    }
    public String getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }
    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
    }
}
