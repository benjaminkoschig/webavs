package ch.globaz.orion.business.models.inscription;

/**
 * @author bjo Classe représentant une inscription Ebusiness en ligne pour Orion
 * 
 */
public class InscriptionEbusiness {
    public static final String MODE_DECL_DAN = "1";
    public static final String MODE_DECL_PUCS = "2";
    public static final String STATUT_ERREUR = "3";
    public static final String STATUT_NOUVELLE = "1";
    public static final String STATUT_TERMINEE = "2";

    private String idInscription = null;
    private String idTiers = null;// Attribut supplémentaire pour Orion
    private String mail = null;
    private String modeDeclSalaire = null;
    private String nom = null;
    private String numAffilie = null;
    private String owner = null;
    private String password = null;// Attribut supplémentaire pour Orion
    private String prenom = null;
    private String raisonSociale = null;
    private String remarque = null;
    private String statut = null;
    private String tel = null;

    /**
     * permet de cloner une inscription
     * 
     * @return un clone de l'inscription
     */
    public InscriptionEbusiness cloneInscription() {
        InscriptionEbusiness cloneInscriptionEbusiness = new InscriptionEbusiness();
        cloneInscriptionEbusiness.setIdInscription(getIdInscription());
        cloneInscriptionEbusiness.setIdTiers(getIdTiers());
        cloneInscriptionEbusiness.setMail(getMail());
        cloneInscriptionEbusiness.setModeDeclSalaire(getModeDeclSalaire());
        cloneInscriptionEbusiness.setNom(getNom());
        cloneInscriptionEbusiness.setNumAffilie(getNumAffilie());
        cloneInscriptionEbusiness.setOwner(getOwner());
        cloneInscriptionEbusiness.setPassword(getPassword());
        cloneInscriptionEbusiness.setPrenom(getPrenom());
        cloneInscriptionEbusiness.setRaisonSociale(getRaisonSociale());
        cloneInscriptionEbusiness.setRemarque(getRemarque());
        cloneInscriptionEbusiness.setStatut(getStatut());
        cloneInscriptionEbusiness.setTel(getTel());

        return cloneInscriptionEbusiness;
    }

    public String getIdInscription() {
        return idInscription;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMail() {
        return mail;
    }

    public String getModeDeclSalaire() {
        return modeDeclSalaire;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getOwner() {
        return owner;
    }

    public String getPassword() {
        return password;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public String getRemarque() {
        return remarque;
    }

    public String getStatut() {
        return statut;
    }

    public String getTel() {
        return tel;
    }

    public void setIdInscription(String idInscription) {
        this.idInscription = idInscription;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setModeDeclSalaire(String modeDeclSalaire) {
        this.modeDeclSalaire = modeDeclSalaire;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
