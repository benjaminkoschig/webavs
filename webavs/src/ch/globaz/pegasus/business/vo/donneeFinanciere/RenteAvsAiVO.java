package ch.globaz.pegasus.business.vo.donneeFinanciere;

public class RenteAvsAiVO {

    private String csNationalite = null;
    private String csRoleFamillePc = null;
    private String csSexe = null;
    private String csTypePc = null;
    private String csTypeRente = null; // type de la rente
    private String dateDebut = null;
    private String dateDecision = null; // date décision
    private String dateDepot = null; // date de depot
    private String dateEcheance = null; // date échéance
    private String dateFin = null;
    private String dateNaissance = null;
    private String idRenteAvsAi = null; // clé primaire
    /*
     * Les service doit retourné, pour une PCA donnée, toutes les rentes AVS/AI comprise dans les données financière
     * pour la version du droit de la PCA. Pour chacune des rentes donné aussi l'idTiers et le csRoleFamillePC du membre
     * de famille auquel elle est liées.
     */
    private String idTiers = null;
    /*
     * private String idDroitMembreFamille = null; private String idEntity = null; private String idEntityGroup = null;
     * private String idVersionDroit = null; private Boolean isSupprime = null;
     */
    private String montant = null; // montant
    private String nom = null;

    private String nss = null;

    private String Prenom = null;

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsRoleFamillePc() {
        return csRoleFamillePc;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsTypePc() {
        return csTypePc;
    }

    public String getCsTypeRente() {
        return csTypeRente;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateDepot() {
        return dateDepot;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdRenteAvsAi() {
        return idRenteAvsAi;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsRoleFamillePc(String csRoleFamillePc) {
        this.csRoleFamillePc = csRoleFamillePc;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setCsTypePc(String csTypePc) {
        this.csTypePc = csTypePc;
    }

    public void setCsTypeRente(String csTypeRente) {
        this.csTypeRente = csTypeRente;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdRenteAvsAi(String idRenteAvsAi) {
        this.idRenteAvsAi = idRenteAvsAi;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }
}
