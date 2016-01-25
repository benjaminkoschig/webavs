package ch.globaz.pegasus.business.models.blocage;

import globaz.jade.persistence.model.JadeComplexModel;
import globaz.jade.persistence.sql.JadeSqlConstantes;

public class PcaBloque extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatPca;
    private String csGenrPca;
    private String csTypePca;
    private String dateDebutPca;
    private String idBlocage;
    private String idDroit;
    private String idEnteteBlocage;
    private String idPca;
    private String idPrestationAccordeeConjoint;
    private String idTiersBeneficiaire;
    private String idVersionDroit;
    private boolean isPrestationBloquee = false;
    private boolean isRetenues = false;
    private String montantBloque;
    private String montantDebloque;
    private String montantPca;
    private String nom;
    private String noVersionDroit;
    private String nss;
    private String prenom;

    public String getCsEtatPca() {
        return csEtatPca;
    }

    public String getCsGenrPca() {
        return csGenrPca;
    }

    public String getCsTypePca() {
        return csTypePca;
    }

    public String getDateDebutPca() {
        return dateDebutPca;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdBlocage() {
        return idBlocage;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdEnteteBlocage() {
        return idEnteteBlocage;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getIdPrestationAccordeeConjoint() {
        return idPrestationAccordeeConjoint;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public boolean getIsPrestationBloquee() {
        return isPrestationBloquee;
    }

    public boolean getIsRetenues() {
        return isRetenues;
    }

    public String getMontantBloque() {
        return montantBloque;
    }

    public String getMontantDebloque() {
        return montantDebloque;
    }

    public String getMontantPca() {
        return montantPca;
    }

    public String getNom() {
        return nom;
    }

    public String getNoVersionDroit() {
        return noVersionDroit;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void set_isPrestationBloquee(String isPrestationBloquee) {
        this.isPrestationBloquee = toBoolean(isPrestationBloquee);
    }

    public void set_isRetenues(String isRetenues) {
        this.isRetenues = toBoolean(isRetenues);
    }

    public void setCsEtatPca(String csEtatPca) {
        this.csEtatPca = csEtatPca;
    }

    public void setCsGenrPca(String csGenrPca) {
        this.csGenrPca = csGenrPca;
    }

    public void setCsTypePca(String csTypePca) {
        this.csTypePca = csTypePca;
    }

    public void setDateDebutPca(String dateDebutPca) {
        this.dateDebutPca = dateDebutPca;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdBlocage(String idBlocage) {
        this.idBlocage = idBlocage;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdEnteteBlocage(String idEnteteBlocage) {
        this.idEnteteBlocage = idEnteteBlocage;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setIdPrestationAccordeeConjoint(String idPrestationAccordeeConjoint) {
        this.idPrestationAccordeeConjoint = idPrestationAccordeeConjoint;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsPrestationBloquee(boolean isPrestationBloquee) {
        this.isPrestationBloquee = isPrestationBloquee;
    }

    public void setMontantBloque(String montantBloque) {
        this.montantBloque = montantBloque;
    }

    public void setMontantDebloque(String montantDebloque) {
        this.montantDebloque = montantDebloque;
    }

    public void setMontantPca(String montantPca) {
        this.montantPca = montantPca;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNoVersionDroit(String noVersionDroit) {
        this.noVersionDroit = noVersionDroit;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub
    }

    private Boolean toBoolean(String value) {
        if (value.equals(JadeSqlConstantes.DB_BOOLEAN_TRUE)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public String toString() {

        return "PcaBloque [csEtatPca=" + csEtatPca + ", csGenrPca=" + csGenrPca + ", csTypePca=" + csTypePca
                + ", dateDebutPca=" + dateDebutPca + ", idBlocage=" + idBlocage + ", idDroit=" + idDroit + ", idPca="
                + idPca + ", idPrestationAccordeeConjoint=" + idPrestationAccordeeConjoint + ", idTiersBeneficiaire="
                + idTiersBeneficiaire + ", idVersionDroit=" + idVersionDroit + ", isPrestationBloquee="
                + isPrestationBloquee + ", isRetenues=" + isRetenues + ", montantBloque=" + montantBloque
                + ", montantDebloque=" + montantDebloque + ", montantPca=" + montantPca + ", nom=" + nom
                + ", noVersionDroit=" + noVersionDroit + ", nss=" + nss + ", prenom=" + prenom + "]";
    }

}
