package globaz.corvus.db.ordresversements;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.prestations.REPrestations;
import globaz.globall.db.BStatement;

public class REOVJointDemandeJointDecisionJointTiers extends REOrdresVersements {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DEB_ADR_PMT = "HEDDAD";
    public static final String FIELDNAME_DOM_ADR_PMT = "HFIAPP";
    public static final String FIELDNAME_FIN_ADR_PMT = "HEDFAD";
    public static final String FIELDNAME_ID_ADR_PMT = "HAIADR";

    public static final String FIELDNAME_ID_CANTON = "HJICAN";
    public static final String FIELDNAME_ID_LOCALITE = "HJILOC";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_TYPE_ADR_PMT = "HETTAD";

    public static final String TABLE_ADRESSE = "TIAADRP";
    public static final String TABLE_ADRESSE_2 = "TIADREP";

    public static final String TABLE_LOCALITE = "TILOCAP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS);

        // jointure entre table des OV et table des prestations
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(REOrdresVersements.FIELDNAME_ID_PRESTATION);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(REPrestations.FIELDNAME_ID_PRESTATION);

        // jointure entre table des demandes des prestations et table des
        // demande de rente
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(REPrestations.FIELDNAME_ID_DEMANDE_RENTE);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        // jointure entre table des demandes des prestations et table des
        // décisions
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(REPrestations.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(REDecisionEntity.FIELDNAME_ID_DECISION);

        // jointure entre table des décisions et titierp
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table titierp et tiaadrp
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ADRESSE);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(" (");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ADRESSE);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(" ");
        fromClauseBuffer.append(" AND " + FIELDNAME_TYPE_ADR_PMT + " = 508008 ");
        fromClauseBuffer.append(" AND " + FIELDNAME_DOM_ADR_PMT + " = 519004");
        fromClauseBuffer.append(")");

        // jointure entre TIAADRP et TIADREP
        fromClauseBuffer.append(" LEFT OUTER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ADRESSE_2);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(" (");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ADRESSE);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(FIELDNAME_ID_ADR_PMT);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ADRESSE_2);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(FIELDNAME_ID_ADR_PMT);
        fromClauseBuffer.append(")");

        // jointure entre TIADREP et TILOCAP
        fromClauseBuffer.append(" LEFT OUTER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_LOCALITE);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(" (");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ADRESSE_2);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(FIELDNAME_ID_LOCALITE);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_LOCALITE);
        fromClauseBuffer.append(".");
        fromClauseBuffer.append(FIELDNAME_ID_LOCALITE);
        fromClauseBuffer.append(")");

        return fromClauseBuffer.toString();
    }

    private String dateValidationDecision = new String();
    private String idCanton = new String();
    private String idDecision = new String();
    private String idDemandeRente = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idTiersBeneficiaire = new String();

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
        idDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        idTiersBeneficiaire = statement.dbReadNumeric(FIELDNAME_ID_TIERS_TI);
        idCanton = statement.dbReadNumeric(FIELDNAME_ID_CANTON);
        idDecision = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ID_DECISION);
        dateValidationDecision = statement.dbReadDateAMJ(REDecisionEntity.FIELDNAME_DATE_VALIDATION);
    }

    public String getDateValidationDecision() {
        return dateValidationDecision;
    }

    public String getIdCanton() {
        return idCanton;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public void setDateValidationDecision(String dateValidationDecision) {
        this.dateValidationDecision = dateValidationDecision;
    }

    public void setIdCanton(String idCanton) {
        this.idCanton = idCanton;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

}
