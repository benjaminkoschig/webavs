/*
 * Créé le 23 juin 05
 */
package globaz.apg.db.droits;

import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEnfantAPGJointTiers extends APEnfantAPG {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    /**
     */
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    /**
     */
    public static final String FIELDNAME_NOM = "HTLDE1";

    /**
     */
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    /**
     */
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_SEXE = "HPTSEX";

    /**
     */
    public static final String TABLE_AVS = "TIPAVSP";

    /**
     */
    public static final String TABLE_PERSONNE = "TIPERSP";

    /**
     */
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEnfantAPG.TABLE_NAME);

        // jointure entre table des enfants apg et tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEnfantAPG.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEnfantAPG.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des enfants apg et avs
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEnfantAPG.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEnfantAPG.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des enfants apg et tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEnfantAPG.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEnfantAPG.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String csNationaliteEnf = "";
    private String csSexeEnf = "";
    private String dateNaissance = "";
    private String fromClause = null;
    private String nom = "";
    private String nss = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String prenom = "";

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nss = statement.dbReadString(FIELDNAME_NUM_AVS);
        nom = statement.dbReadString(FIELDNAME_NOM);
        prenom = statement.dbReadString(FIELDNAME_PRENOM);
        dateNaissance = statement.dbReadDateAMJ(FIELDNAME_DATENAISSANCE);
        csSexeEnf = statement.dbReadNumeric(FIELDNAME_SEXE);
        csNationaliteEnf = statement.dbReadNumeric(FIELDNAME_NATIONALITE);
        super._readProperties(statement);
    }

    /**
     * (non-Javadoc)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        super._writeProperties(statement);
    }

    /**
     * @return
     */
    public String getCsNationaliteEnf() {
        return csNationaliteEnf;
    }

    /**
     * @return
     */
    public String getCsSexeEnf() {
        return csSexeEnf;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return
     */
    public String getNss() {
        return nss;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param string
     */
    public void setCsNationaliteEnf(String string) {
        csNationaliteEnf = string;
    }

    /**
     * @param string
     */
    public void setCsSexeEnf(String string) {
        csSexeEnf = string;
    }

    /**
     * @param string
     *            DOCUMENT ME!
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * @param string
     *            DOCUMENT ME!
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * @param string
     */
    public void setNss(String string) {
        nss = string;
    }

    /**
     * @param string
     *            DOCUMENT ME!
     */
    public void setPrenom(String string) {
        prenom = string;
    }

}
