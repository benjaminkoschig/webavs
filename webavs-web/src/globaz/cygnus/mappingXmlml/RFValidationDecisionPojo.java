package globaz.cygnus.mappingXmlml;

/**
 * Pojo utilisé pour la génération de la liste ListeDeControleValidationDecision
 * 
 */
public class RFValidationDecisionPojo implements Comparable<RFValidationDecisionPojo> {
    private String idTiers;
    private String nss;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String csSexe;
    private String csNationalite;
    private String montantPrestation;
    private String idPrestation;
    private String idAdressePaiement;
    private String communePolitique;
    private boolean ajouterCommunePolitique;

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
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

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    public boolean getAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

    @Override
    public int compareTo(RFValidationDecisionPojo o) {
        if (getAjouterCommunePolitique()) {
            int result1 = getCommunePolitique().compareTo(o.getCommunePolitique());
            if (result1 != 0) {
                return result1;
            }
        }

        int result2 = getNom().compareTo(o.getNom());
        if (result2 != 0) {
            return result2;
        }

        return getPrenom().compareTo(o.getPrenom());
    }
}