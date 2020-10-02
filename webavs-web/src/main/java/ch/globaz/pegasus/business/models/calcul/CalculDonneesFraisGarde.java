package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;

public class CalculDonneesFraisGarde extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csRoleFamille = null;
    private String csTypeDonneeFinanciere = null;
    private String dateDebutDonneeFinanciere = null;
    private String dateFinDonneeFinanciere = null;
    private String idDonneeFinanciereHeader = null;
    private String idDroitMembreFamille = null;
    private String idMembreFamilleSF = null;
    private String revenuActiviteLucrativeDependanteMontantFraisDeGarde = null;
    private String revenuActiviteLucrativeIndependanteMontantFraisDeGarde = null;
    private String revenuHypothetiqueMontantFraisGarde = null;
    private String depenseMontantFraisDeGarde = null;

    @Override
    public String getId() {
        return idMembreFamilleSF;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(String id) {
        idMembreFamilleSF = id;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return CalculDonneesFraisGarde.class.getSimpleName() + "(" + csTypeDonneeFinanciere + ")@" + hashCode();
    }

    public String getCsRoleFamille() {
        return csRoleFamille;
    }

    public void setCsRoleFamille(String csRoleFamille) {
        this.csRoleFamille = csRoleFamille;
    }

    public String getCsTypeDonneeFinanciere() {
        return csTypeDonneeFinanciere;
    }

    public void setCsTypeDonneeFinanciere(String csTypeDonneeFinanciere) {
        this.csTypeDonneeFinanciere = csTypeDonneeFinanciere;
    }

    public String getDateDebutDonneeFinanciere() {
        return dateDebutDonneeFinanciere;
    }

    public void setDateDebutDonneeFinanciere(String dateDebutDonneeFinanciere) {
        this.dateDebutDonneeFinanciere = dateDebutDonneeFinanciere;
    }

    public String getDateFinDonneeFinanciere() {
        return dateFinDonneeFinanciere;
    }

    public void setDateFinDonneeFinanciere(String dateFinDonneeFinanciere) {
        this.dateFinDonneeFinanciere = dateFinDonneeFinanciere;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    public String getIdMembreFamilleSF() {
        return idMembreFamilleSF;
    }

    public void setIdMembreFamilleSF(String idMembreFamilleSF) {
        this.idMembreFamilleSF = idMembreFamilleSF;
    }

    public String getRevenuActiviteLucrativeDependanteMontantFraisDeGarde() {
        return revenuActiviteLucrativeDependanteMontantFraisDeGarde;
    }

    public void setRevenuActiviteLucrativeDependanteMontantFraisDeGarde(String revenuActiviteLucrativeDependanteMontantFraisDeGarde) {
        this.revenuActiviteLucrativeDependanteMontantFraisDeGarde = revenuActiviteLucrativeDependanteMontantFraisDeGarde;
    }

    public String getRevenuActiviteLucrativeIndependanteMontantFraisDeGarde() {
        return revenuActiviteLucrativeIndependanteMontantFraisDeGarde;
    }

    public void setRevenuActiviteLucrativeIndependanteMontantFraisDeGarde(String revenuActiviteLucrativeIndependanteMontantFraisDeGarde) {
        this.revenuActiviteLucrativeIndependanteMontantFraisDeGarde = revenuActiviteLucrativeIndependanteMontantFraisDeGarde;
    }

    public String getRevenuHypothetiqueMontantFraisGarde() {
        return revenuHypothetiqueMontantFraisGarde;
    }

    public void setRevenuHypothetiqueMontantFraisGarde(String revenuHypothetiqueMontantFraisGarde) {
        this.revenuHypothetiqueMontantFraisGarde = revenuHypothetiqueMontantFraisGarde;
    }
    public String getDepenseMontantFraisDeGarde() {
        return depenseMontantFraisDeGarde;
    }

    public void setDepenseMontantFraisDeGarde(String depenseMontantFraisDeGarde) {
        this.depenseMontantFraisDeGarde = depenseMontantFraisDeGarde;
    }


}
