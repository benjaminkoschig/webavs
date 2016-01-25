package globaz.phenix.db.taxation.definitive;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * sql request :
 * 
 * -- prestation APG + MAT
 * 
 * 
 * 
 * @author scr
 * @see globaz.apg.db.alfa.CPTaxationDefinitiveManager
 * 
 */
public class CPTaxationDefinitive extends BEntity {

    public static final String FIELDNAME_DATE_DEBUT_DECISION = "IADDEB";

    public static final String FIELDNAME_DATE_DEBUT_PRST = "VHDDEB";
    public static final String FIELDNAME_DATE_FIN_DECISION = "IADFIN";
    public static final String FIELDNAME_DATE_FIN_PRST = "VHDFIN";
    public static final String FIELDNAME_ETAT_PRST = "VHTETA";
    public static final String FIELDNAME_ID_AFFILIATION = "MAIAFF";
    public static final String FIELDNAME_ID_DECISION = "IAIDEC";
    public static final String FIELDNAME_ID_DEMANDE = "WAIDEM";
    public static final String FIELDNAME_ID_DEMANDE_DROIT = "VAIDEM";
    public static final String FIELDNAME_ID_DROIT_DROIT = "VAIDRO";
    public static final String FIELDNAME_ID_DROIT_PRST = "VHIDRO";
    public static final String FIELDNAME_ID_DROIT_SITPROF = "VFIDRO";
    public static final String FIELDNAME_ID_PASSAGE_DECISION = "EBIPAS";
    public static final String FIELDNAME_ID_TIERS = "HTITIE";
    public static final String FIELDNAME_ID_TIERS_DEMANDE = "WAITIE";
    public static final String FIELDNAME_IS_DECISION_ACTIVE = "IAACTI";
    public static final String FIELDNAME_IS_INDEPENDANT = "VFBIND";
    public static final String FIELDNAME_NO_AFFILIE = "MALNAF";
    public static final String FIELDNAME_NSS = "HXNAVS";
    public static final String FIELDNAME_REVENU_DETERMINANT_DOCAP = "IHMDCA";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_REVENU_INDEPENDANT_SITPROF = "VFMRIN";
    public static final String FIELDNAME_TYPE_AFFILIATION = "MATTAF";
    public static final String FIELDNAME_TYPE_CALCUL_DOCAP = "IHIDCA";
    public static final String FIELDNAME_TYPE_DECISION_DECISON = "IATTDE";
    /**
	 * 
	 */
    private static final long serialVersionUID = -3048532792237202090L;

    public static final String TABLE_AFFILIATIONS = "AFAFFIP";
    public static final String TABLE_DECISIONS = "CPDECIP";
    public static final String TABLE_DEMANDES = "PRDEMAP";
    public static final String TABLE_DOCAP = "CPDOCAP";
    public static final String TABLE_DROITS = "APDROIP";
    public static final String TABLE_PERSAVS = "TIPAVSP";
    public static final String TABLE_PREST_APG = "APPRESP";
    public static final String TABLE_SIT_PROF = "APSIPRP";
    public static final String TABLE_TIERS = "TITIERP";

    public static final String createFields(String schema) {

        StringBuffer sb = new StringBuffer();

        sb.append(schema).append(TABLE_PERSAVS).append(".").append(FIELDNAME_NSS).append(", ");
        sb.append(schema).append(TABLE_AFFILIATIONS).append(".").append(FIELDNAME_NO_AFFILIE).append(", ");
        sb.append(schema).append(TABLE_PREST_APG).append(".").append(FIELDNAME_DATE_DEBUT_PRST).append(", ");
        sb.append(schema).append(TABLE_PREST_APG).append(".").append(FIELDNAME_DATE_FIN_PRST).append(", ");
        sb.append(schema).append(TABLE_DOCAP).append(".").append(FIELDNAME_REVENU_DETERMINANT_DOCAP).append(", ");
        sb.append(schema).append(TABLE_SIT_PROF).append(".").append(FIELDNAME_REVENU_INDEPENDANT_SITPROF).append(" ");
        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT OUTER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        // jointure entre table des prestations et des droits
        fromClauseBuffer.append(schema).append(TABLE_DECISIONS);

        // INNER JOIN WEBAVS.AFAFFIP on
        // WEBAVS.CPDECIP.MAIAFF=WEBAVS.AFAFFIP.MAIAFF
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AFFILIATIONS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DECISIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_AFFILIATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AFFILIATIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_AFFILIATION);

        // INNER JOIN WEBAVS.CPDOCAP on WEBAVS.CPDOCAP.IAIDEC =
        // WEBAVS.CPDECIP.IAIDEC
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DOCAP);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DOCAP);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DECISIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DECISION);

        // INNER JOIN WEBAVS.TITIERP ON
        // WEBAVS.TITIERP.HTITIE=WEBAVS.AFAFFIP.HTITIE
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AFFILIATIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS);

        // INNER JOIN WEBAVS.TIPAVSP ON
        // WEBAVS.TIPAVSP.HTITIE=WEBAVS.TITIERP.HTITIE
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSAVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSAVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS);

        // LEFT OUTER JOIN WEBAVS.PRDEMAP ON
        // WEBAVS.PRDEMAP.WAITIE=WEBAVS.AFAFFIP.HTITIE
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DEMANDES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DEMANDES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AFFILIATIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS);

        // LEFT OUTER JOIN WEBAVS.APDROIP ON
        // WEBAVS.APDROIP.VAIDEM=WEBAVS.PRDEMAP.WAIDEM
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DROITS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DROITS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DEMANDE_DROIT);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DEMANDES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DEMANDE);

        // LEFT OUTER JOIN WEBAVS.APPRESP ON
        // WEBAVS.APPRESP.VHIDRO=WEBAVS.APDROIP.VAIDRO
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PREST_APG);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PREST_APG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DROIT_PRST);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DROITS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DROIT_DROIT);

        // LEFT OUTER JOIN WEBAVS.APSIPRP ON
        // WEBAVS.APSIPRP.VFIDRO=WEBAVS.APDROIP.VAIDRO
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_SIT_PROF);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_SIT_PROF);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DROIT_SITPROF);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_DROITS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DROIT_DROIT);

        return fromClauseBuffer.toString();
    }

    private String dateDebut = "";
    private String dateFin = "";
    private transient String fields = null;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    //
    private transient String fromClause = null;
    private String noAffilie = "";
    private String nss = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String revenuDeterminant = "";

    private String revenuIndependant = "";

    /**
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = createFields(_getCollection());
        }

        return fields;
    }

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

    @Override
    protected String _getTableName() {
        return _getCollection() + "." + TABLE_DROITS;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        revenuIndependant = statement.dbReadNumeric(FIELDNAME_REVENU_INDEPENDANT_SITPROF);
        revenuDeterminant = statement.dbReadNumeric(FIELDNAME_REVENU_DETERMINANT_DOCAP);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT_PRST);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN_PRST);
        nss = statement.dbReadString(FIELDNAME_NSS);
        noAffilie = statement.dbReadString(FIELDNAME_NO_AFFILIE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getFields() {
        return fields;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getNoAffilie() {
        return noAffilie;
    }

    public String getNss() {
        return nss;
    }

    public String getRevenuDeterminant() {
        return revenuDeterminant;
    }

    public String getRevenuIndependant() {
        return revenuIndependant;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setRevenuDeterminant(String revenuDeterminant) {
        this.revenuDeterminant = revenuDeterminant;
    }

    public void setRevenuIndependant(String revenuIndependant) {
        this.revenuIndependant = revenuIndependant;
    }
}
