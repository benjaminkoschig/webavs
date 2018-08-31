package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSimpleModel;

public class EnteteFactureAssociationProfessionnelleSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -4476704137892710557L;

    private String id;
    private String idEmployeur;
    private String taux;
    private String anneeFacture;
    private String reductionFacture;
    private String etat;
    private String montantTotal;
    private String masseSalariale;
    private String idModeleEntete;
    private String idAssociation;
    private String idPassageFacturation;
    private String numeroSection;

    public String getNumeroSection() {
        return numeroSection;
    }

    public void setNumeroSection(String numeroSection) {
        this.numeroSection = numeroSection;
    }

    public String getIdAssociation() {
        return idAssociation;
    }

    public void setIdAssociation(String idAssociation) {
        this.idAssociation = idAssociation;
    }

    public String getIdModeleEntete() {
        return idModeleEntete;
    }

    public void setIdModeleEntete(String idModeleEntete) {
        this.idModeleEntete = idModeleEntete;
    }

    public EnteteFactureAssociationProfessionnelleSimpleModel() {
        super();
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public String getAnneeFacture() {
        return anneeFacture;
    }

    public void setAnneeFacture(String anneeFacture) {
        this.anneeFacture = anneeFacture;
    }

    public String getReductionFacture() {
        return reductionFacture;
    }

    public void setReductionFacture(String reductionFacture) {
        this.reductionFacture = reductionFacture;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(String masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the idPassageFacturation
     */
    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    /**
     * @param idPassageFacturation the idPassageFacturation to set
     */
    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

}
