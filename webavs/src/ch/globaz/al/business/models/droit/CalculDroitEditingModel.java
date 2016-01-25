/**
 * 
 */
package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Classe modèle d'un droit calculé
 * 
 * @author pta
 * 
 */
public class CalculDroitEditingModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * identifiant du calcul
     */
    private String idCalcul = null;
    /**
     * identifiant du droit
     */
    private String idDroit = null;
    /**
	 * 
	 */
    private Boolean isHide = null;
    /**
     * montant de l'allocataire cas supplétif cantonal
     */
    private String montantAllocataire = null;
    /**
     * montant de l'autre parent (cas suppléetif cantonal)
     */
    private String montantAutreParent = null;

    /**
     * montant Effectif attribuer pour le droit
     */
    private String montantResultEffectif = null;
    /**
     * montant Total pour le dossier
     */
    private String montantTotal = null;
    /**
     * montant totale debut
     */
    private String montantTotalDebut = null;
    /**
     * montant fin
     */
    private String montantTotalFin = null;
    /**
     * montant total unité
     */
    private String montantTotalUnite = null;
    /**
     * tarif de la caisse
     */
    private String tarifCaisse = null;
    /**
     * tarifEffectif
     */
    private String tarifEffectif = null;
    /**
     * typePrestation
     */
    private String typePrestation = null;
    /**
     * nom utilisateur
     */
    private String user = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {

        return idCalcul;
    }

    public String getIdCalcul() {
        return idCalcul;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public Boolean getIsHide() {
        return isHide;
    }

    public String getMontantAllocataire() {
        return montantAllocataire;
    }

    public String getMontantAutreParent() {
        return montantAutreParent;
    }

    public String getMontantResultEffectif() {
        return montantResultEffectif;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public String getMontantTotalDebut() {
        return montantTotalDebut;
    }

    public String getMontantTotalFin() {
        return montantTotalFin;
    }

    public String getMontantTotalUnite() {
        return montantTotalUnite;
    }

    public String getTarifCaisse() {
        return tarifCaisse;
    }

    public String getTarifEffectif() {
        return tarifEffectif;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public String getUser() {
        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idCalcul = id;

    }

    public void setIdCalcul(String idCalcul) {
        this.idCalcul = idCalcul;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIsHide(Boolean isHide) {
        this.isHide = isHide;
    }

    public void setMontantAllocataire(String montantAllocataire) {
        this.montantAllocataire = montantAllocataire;
    }

    public void setMontantAutreParent(String montantAutreParent) {
        this.montantAutreParent = montantAutreParent;
    }

    public void setMontantResultEffectif(String montantResultEffectif) {
        this.montantResultEffectif = montantResultEffectif;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setMontantTotalDebut(String montantTotalDebut) {
        this.montantTotalDebut = montantTotalDebut;
    }

    public void setMontantTotalFin(String montantTotalFin) {
        this.montantTotalFin = montantTotalFin;
    }

    public void setMontantTotalUnite(String montantTotalUnite) {
        this.montantTotalUnite = montantTotalUnite;
    }

    public void setTarifCaisse(String tarifCaisse) {
        this.tarifCaisse = tarifCaisse;
    }

    public void setTarifEffectif(String tarifEffectif) {
        this.tarifEffectif = tarifEffectif;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
