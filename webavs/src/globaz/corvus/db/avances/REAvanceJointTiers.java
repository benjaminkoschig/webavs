package globaz.corvus.db.avances;


/**
 * Modèle de données des avances
 * Encapsule le tier et l'avance
 * 
 * @author sce
 * 
 */
public class REAvanceJointTiers implements Comparable<REAvanceJointTiers> {

    private REAvance avance = null;
    private String dateNaissance = null;
    private String nom = null;
    private String nss = null;
    private String prenom = null;
    private String typeDemande = null;
    private String communePolitique = null;

    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    public REAvanceJointTiers() {
        avance = new REAvance();
        dateNaissance = new String();
        nom = new String();
        nss = new String();
        prenom = new String();
        typeDemande = new String();
        communePolitique = "";
    }

    public REAvance getAvance() {
        return avance;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    public void setAvance(REAvance avance) {
        this.avance = avance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    @Override
    public int compareTo(REAvanceJointTiers o) {
        return getCommunePolitique().compareToIgnoreCase(o.getCommunePolitique());
    }

}
