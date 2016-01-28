package ch.globaz.amal.business.models.primesassurance;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePrimesAssurance extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeHistorique = null;
    private String idPrimeMoyenne = null;
    private String idTiers = null;
    private String montantPrimeAdulte = null;
    private String montantPrimeEnfant = null;
    private String montantPrimeFormation = null;
    private String noCaisseMaladie = null;

    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    @Override
    public String getId() {
        return idPrimeMoyenne;
    }

    public String getIdPrimeMoyenne() {
        return idPrimeMoyenne;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantPrimeAdulte() {
        return montantPrimeAdulte;
    }

    public String getMontantPrimeEnfant() {
        return montantPrimeEnfant;
    }

    public String getMontantPrimeFormation() {
        return montantPrimeFormation;
    }

    public String getNoCaisseMaladie() {
        return noCaisseMaladie;
    }

    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    @Override
    public void setId(String id) {
        idPrimeMoyenne = id;
    }

    public void setIdPrimeMoyenne(String idPrimeMoyenne) {
        this.idPrimeMoyenne = idPrimeMoyenne;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantPrimeAdulte(String montantPrimeAdulte) {
        this.montantPrimeAdulte = montantPrimeAdulte;
    }

    public void setMontantPrimeEnfant(String montantPrimeEnfant) {
        this.montantPrimeEnfant = montantPrimeEnfant;
    }

    public void setMontantPrimeFormation(String montantPrimeFormation) {
        this.montantPrimeFormation = montantPrimeFormation;
    }

    public void setNoCaisseMaladie(String noCaisseMaladie) {
        this.noCaisseMaladie = noCaisseMaladie;
    }

}
