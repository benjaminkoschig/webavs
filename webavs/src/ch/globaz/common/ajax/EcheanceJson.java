package ch.globaz.common.ajax;

public class EcheanceJson {
    private String id;
    private String idExterne;
    private Long idTiers;
    private String domaine;
    private String etat;
    private String type;
    private String dateTraitement;
    private String dateEcheance;
    private String libelle;
    private String description;
    private String codeDomaine;
    private String codeType;
    private String codeEtat;
    private String spy;
    private String nom;
    private String prenom;

    public String getId() {
        return id;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public Long getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(Long idTiers) {
        this.idTiers = idTiers;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return domaine;
    }

    public String getEtat() {
        return etat;
    }

    public String getType() {
        return type;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }

    public String getCodeDomaine() {
        return codeDomaine;
    }

    public void setCodeDomaine(String codeDomaine) {
        this.codeDomaine = codeDomaine;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeEtat() {
        return codeEtat;
    }

    public void setCodeEtat(String codeEtat) {
        this.codeEtat = codeEtat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return "EcheanceJson [id=" + id + ", idExterne=" + idExterne + ", idTiers=" + idTiers + ", domaine=" + domaine
                + ", etat=" + etat + ", type=" + type + ", dateTraitement=" + dateTraitement + ", dateEcheance="
                + dateEcheance + ", libelle=" + libelle + ", description=" + description + ", codeDomaine="
                + codeDomaine + ", codeType=" + codeType + ", codeEtat=" + codeEtat + ", spy=" + spy + ", nom=" + nom
                + ", prenom=" + prenom + "]";
    }

    public String getSpy() {
        return spy;
    }

    public void setSpy(String spy) {
        this.spy = spy;
    }

}
