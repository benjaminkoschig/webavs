package ch.globaz.orion.business.models.acl;

/**
 * @author BJO Classe représentant une annonce de collaborateur pour Orion
 */
public class Acl implements EBAttestationComparatorInterface {

    private final static String STATUT_EN_COURS = "1";
    private final static String STATUT_PROBLEME = "3";
    private final static String STATUT_SAISIE = "0";
    private final static String STATUT_TERMINE = "2";
    private String dateEngagement = null;
    private String dateNaissance = "?";
    private String dateSaisie = null;
    private Boolean duplicata = null;
    private String idAnnonceCollaborateur = null;
    private String nationnalite = "?";
    private String noEmploye = null;
    private String nomPrenom = "?";
    private String noSuccursale = null;
    private String nouveauNumero = null;
    private String numeroAffilie = null;
    private String numeroAssure = null;
    private String refInterne = "";// acl+idAnnonceCollaborateur dans Ebusiness
    private String statut = null;// saisie,en cours, terminé ou problème
    private String typeArc = "?";// arc générés

    public String getDateEngagement() {
        return dateEngagement;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateSaisie() {
        return dateSaisie;
    }

    public Boolean getDuplicata() {
        return duplicata;
    }

    public String getIdAnnonceCollaborateur() {
        return idAnnonceCollaborateur;
    }

    public String getNationnalite() {
        return nationnalite;
    }

    /**
     * @return the noEmploye
     */
    public String getNoEmploye() {
        return noEmploye;
    }

    @Override
    public String getNom() {
        // TODO Auto-generated method stub
        return nomPrenom;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return the noSuccursale
     */
    @Override
    public String getNoSuccursale() {
        return noSuccursale;
    }

    public String getNouveauNumero() {
        return nouveauNumero;
    }

    @Override
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getNumeroAssure() {
        return numeroAssure;
    }

    public String getRefInterne() {
        return refInterne;
    }

    public String getStatut() {
        return statut;
    }

    public String getTypeArc() {
        return typeArc;
    }

    @Override
    public String getUser() {
        // TODO Auto-generated method stub
        return "";
    }

    public void setDateEngagement(String dateEngagement) {
        this.dateEngagement = dateEngagement;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateSaisie(String dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    public void setDuplicata(Boolean duplicata) {
        this.duplicata = duplicata;
    }

    public void setIdAnnonceCollaborateur(String idAnnonceCollaborateur) {
        this.idAnnonceCollaborateur = idAnnonceCollaborateur;
    }

    public void setNationnalite(String nationnalite) {
        this.nationnalite = nationnalite;
    }

    /**
     * @param noEmploye
     *            the noEmploye to set
     */
    public void setNoEmploye(String noEmploye) {
        this.noEmploye = noEmploye;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    /**
     * @param noSuccursale
     *            the noSuccursale to set
     */
    public void setNoSuccursale(String noSuccursale) {
        this.noSuccursale = noSuccursale;
    }

    public void setNouveauNumero(String nouveauNumero) {
        this.nouveauNumero = nouveauNumero;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setNumeroAssure(String numeroAssure) {
        this.numeroAssure = numeroAssure;
    }

    public void setRefInterne(String refInterne) {
        this.refInterne = refInterne;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setTypeArc(String typeArc) {
        this.typeArc = typeArc;
    }
}
