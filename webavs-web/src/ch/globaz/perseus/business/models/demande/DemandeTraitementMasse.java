package ch.globaz.perseus.business.models.demande;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Classe pour les demandes de prestations complémentaires familles
 * 
 * @author RCO
 * 
 */
public class DemandeTraitementMasse extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDecision = null;
    private SimpleDemande simpleDemande = null;

    private String csTypeDecision = null;

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    private String nom = null;
    private String prenom = null;
    private String nss = null;

    @Override
    public String toString() {
        return "DemandeTraitementMasse [idDecision=" + idDecision + ", simpleDemande=" + simpleDemande + ", nom=" + nom
                + ", prenom=" + prenom + ", nss=" + nss + "]";
    }

    public String getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public DemandeTraitementMasse() {
        super();
        simpleDemande = new SimpleDemande();
    }

    @Override
    public String getId() {
        return simpleDemande.getId();
    }

    @Override
    public String getSpy() {
        return simpleDemande.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleDemande.setId(id);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Override
    public void setSpy(String spy) {
        simpleDemande.setSpy(spy);
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

}
