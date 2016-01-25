/*
 * Créé le 9 janv. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr
 * 
 * 
 * 
 */
public class RERenteAccordeeJoinInfoComptaJoinPrstDues extends RERenteAccordee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);

        // jointure entre table des prestations accordées et table rentes
        // accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        // jointure entre table des prestations accordées et table info compta
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        // jointure entre table des prestations accordées et table prestations
        // dues
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);

        return fromClauseBuffer.toString();
    }

    private String csTypeMontant = "";
    private String debutPaiement = "";
    private String finPaiement = "";
    private transient String fromClause = null;
    private String idCompteAnnexe = "";
    private String idDomaineApplication = "";
    private String idInfoCompta = "";
    private String idPrestationDue = "";
    private String idTiersAdressePmt = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String montantPrestDue = "";

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = RERenteAccordeeJoinInfoComptaJoinPrstDues.createFromClause(_getCollection());
        }

        return fromClause;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idInfoCompta = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);
        idTiersAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idDomaineApplication = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION);
        idCompteAnnexe = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
        csTypeMontant = statement.dbReadNumeric(REPrestationDue.FIELDNAME_CS_TYPE);
        debutPaiement = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT));
        finPaiement = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT));
        montantPrestDue = statement.dbReadNumeric(REPrestationDue.FIELDNAME_MONTANT);
        idPrestationDue = statement.dbReadNumeric(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);

    }

    @Override
    public String getCollection() {
        return _getCollection();
    }

    /**
     * @return the csTypeMontant
     */
    public String getCsTypeMontant() {
        return csTypeMontant;
    }

    /**
     * @return the debutPaiement
     */
    public String getDebutPaiement() {
        return debutPaiement;
    }

    /**
     * @return the finPaiement
     */
    public String getFinPaiement() {
        return finPaiement;
    }

    /**
     * @return the fromClause
     */
    public String getFromClause() {
        return fromClause;
    }

    /**
     * @return the idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return the idDomaineApplication
     */
    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    /**
     * @return the idInfoCompta
     */
    @Override
    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    /**
     * @return the idPrestationDue
     */
    public String getIdPrestationDue() {
        return idPrestationDue;
    }

    /**
     * @return the idTiersAdressePmt
     */
    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    /**
     * @return the montantPrestDue
     */
    public String getMontantPrestDue() {
        return montantPrestDue;
    }

    /**
     * @param csTypeMontant
     *            the csTypeMontant to set
     */
    public void setCsTypeMontant(String csTypeMontant) {
        this.csTypeMontant = csTypeMontant;
    }

    /**
     * @param debutPaiement
     *            the debutPaiement to set
     */
    public void setDebutPaiement(String debutPaiement) {
        this.debutPaiement = debutPaiement;
    }

    /**
     * @param finPaiement
     *            the finPaiement to set
     */
    public void setFinPaiement(String finPaiement) {
        this.finPaiement = finPaiement;
    }

    /**
     * @param fromClause
     *            the fromClause to set
     */
    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    /**
     * @param idCompteAnnexe
     *            the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idDomaineApplication
     *            the idDomaineApplication to set
     */
    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    /**
     * @param idInfoCompta
     *            the idInfoCompta to set
     */
    @Override
    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    /**
     * @param idPrestationDue
     *            the idPrestationDue to set
     */
    public void setIdPrestationDue(String idPrestationDue) {
        this.idPrestationDue = idPrestationDue;
    }

    /**
     * @param idTiersAdressePmt
     *            the idTiersAdressePmt to set
     */
    public void setIdTiersAdressePmt(String idTiersAdressePmt) {
        this.idTiersAdressePmt = idTiersAdressePmt;
    }

    /**
     * @param montantPrestDue
     *            the montantPrestDue to set
     */
    public void setMontantPrestDue(String montantPrestDue) {
        this.montantPrestDue = montantPrestDue;
    }

}
