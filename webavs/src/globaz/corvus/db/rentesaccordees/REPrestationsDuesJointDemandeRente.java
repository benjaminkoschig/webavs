/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BStatement;

/**
 * @author bsc
 * 
 *         Jointure entre les tables des montants a verser, les bases de calcul et les demandes de rentes
 * 
 */
public class REPrestationsDuesJointDemandeRente extends REPrestationDue {

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
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);

        // jointure entre table des demandes de rentes et table des rentes
        // calculees
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des rentes calculees et table des bases de
        // calculs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des bases de calculs et table des rentes
        // accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        // jointure entre table des rentes accordées et prestations accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre table des prestations accordees et tables des montants
        // a verser
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

        // jointure entre tables des prestations accordées et table des infos
        // compta
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

        return fromClauseBuffer.toString();
    }

    private String codePrestation = "";
    private String csTypeDemandeRente = "";
    private String idTiersBeneficiaire = "";
    // Autres champs nécessaires
    private String noDemandeRente = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String noRenteAccordee = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

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
        noDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        noRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        csTypeDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien du tout... c'est en lecture seule.
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    /**
     * @return
     */
    public String getCsTypeDemandeRente() {
        return csTypeDemandeRente;
    }

    /**
     * @return
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return
     */
    public String getNoDemandeRente() {
        return noDemandeRente;
    }

    /**
     * @return
     */
    public String getNoRenteAccordee() {
        return noRenteAccordee;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    /**
     * @param string
     */
    public void setCsTypeDemandeRente(String string) {
        csTypeDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setIdTiersBeneficiaire(String string) {
        idTiersBeneficiaire = string;
    }

    /**
     * @param string
     */
    public void setNoDemandeRente(String string) {
        noDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setNoRenteAccordee(String string) {
        noRenteAccordee = string;
    }

}
