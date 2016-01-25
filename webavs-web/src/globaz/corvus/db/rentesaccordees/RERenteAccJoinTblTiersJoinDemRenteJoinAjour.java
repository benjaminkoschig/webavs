/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.RESituationFamiliale;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BStatement;

/**
 * @author JJE
 * 
 *         Jointure entre les tables des bases de calcul et des demandes de rentes
 * 
 */
public class RERenteAccJoinTblTiersJoinDemRenteJoinAjour extends RERenteAccJoinTblTiersJoinDemandeRente {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDeces = "";
    private String dateNaissance = "";
    private String dateRevoquationAjournement = "";
    private String genreDePrestation = "";
    private String idGestionnaire = "";
    private String idMembreFamille = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String isAjournementRequerant = "";
    // Autres champs nécessaires
    private String motif = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";

    private String sexe = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        dateDeces = statement.dbReadDateAMJ(RESituationFamiliale.FIELD_DATEDECES);
        genreDePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        dateNaissance = statement.dbReadDateAMJ(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_DATENAISSANCE);
        nss = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_AVS_NUM_AVS);
        nom = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_NOM);
        prenom = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_PRENOM);
        sexe = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_SEXE);
        isAjournementRequerant = statement.dbReadString(REDemandeRenteVieillesse.FIELDNAME_IS_AJOURNEMENT_REQUERANT);
        dateRevoquationAjournement = statement
                .dbReadString(REDemandeRenteVieillesse.FIELDNAME_DATE_REVOCATION_REQUERANT);
        idMembreFamille = statement.dbReadNumeric(RESituationFamiliale.FIELD_IDMEMBREFAMILLE);
        idGestionnaire = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_IDGESTIONNAIRE);
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateRevoquationAjournement() {
        return dateRevoquationAjournement;
    }

    public String getGenreDePrestation() {
        return genreDePrestation;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIsAjournementRequerant() {
        return isAjournementRequerant;
    }

    public String getMotif() {
        return motif;
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

    public String getSexe() {
        return sexe;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateRevoquationAjournement(String dateRevoquationAjournement) {
        this.dateRevoquationAjournement = dateRevoquationAjournement;
    }

    public void setGenreDePrestation(String genreDePrestation) {
        this.genreDePrestation = genreDePrestation;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    public void setIsAjournementRequerant(String isAjournementRequerant) {
        this.isAjournementRequerant = isAjournementRequerant;
    }

    public void setMotif(String motif) {
        this.motif = motif;
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

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}
