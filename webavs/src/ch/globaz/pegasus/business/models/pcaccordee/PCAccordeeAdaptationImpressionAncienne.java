package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;

public class PCAccordeeAdaptationImpressionAncienne extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csGenrePC = null;
    private String idDroit = null;
    private String idPCAccordee = null;
    private String idVersionDroit = null;
    private boolean isPrecedent = false; // champ non persisté, utilisé uniquement pendant l'impression de l'adaptation
    private String montantPrestationAyantDroit = null;
    private String montantPrestationConjoint = null;
    private String nomAyantDroit = null;
    private String nomConjoint = null;

    private String nssAyantDroit = null;
    private String nssConjoint = null;
    private ArrayList<SimplePlanDeCalcul> planCalculs = null;
    private String prenomAyantDroit = null;
    private String prenomConjoint = null;

    public PCAccordeeAdaptationImpressionAncienne() {
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
        return getIdVersionDroit();
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
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
        setIdVersionDroit(id);
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdVersionDroit(String idversiondroit) {
        idVersionDroit = idversiondroit;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
    }
}
