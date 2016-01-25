/*
 * Créé le 1er septembre 2006
 */

package globaz.ij.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;

/**
 * <H1>Description</H1>
 * 
 * @author hpe
 */

public class IJRecapitulationAnnonce extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 
     */
    public static final String FIELDNAME_COMPTECARTES = "XXNCOA";

    /** 
     */
    public static final String FIELDNAME_SOMMENBJOURS = "XXNSNJ";

    /** 
     */
    public static final String FIELDNAME_SOMMETOTALIJ = "XXMSTA";

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
    public static final String createFields(String schema) {
        StringBuffer fields = new StringBuffer();

        fields.append("COUNT(");
        fields.append(IJAnnonce.FIELDNAME_CONTENUANNONCE);
        fields.append(") AS ");
        fields.append(FIELDNAME_COMPTECARTES);
        fields.append(", ");
        fields.append(IJAnnonce.FIELDNAME_CONTENUANNONCE);
        fields.append(", ");
        fields.append(IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE);
        fields.append(", SUM(");
        fields.append(IJPeriodeAnnonce.FIELDNAME_NOMBREJOURS);
        fields.append(") AS ");
        fields.append(FIELDNAME_SOMMENBJOURS);
        fields.append(", SUM(");
        fields.append(IJPeriodeAnnonce.FIELDNAME_TOTALIJ);
        fields.append(") AS ");
        fields.append(FIELDNAME_SOMMETOTALIJ);

        return fields.toString();
    }

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJAnnonce.TABLE_NAME);

        // Jointure entre annonce (IJANNONC) et période annonce (IJPERANN)
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPeriodeAnnonce.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJAnnonce.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJAnnonce.FIELDNAME_IDANNONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPeriodeAnnonce.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPeriodeAnnonce.FIELDNAME_IDANNONCE);

        return fromClauseBuffer.toString();
    }

    private String compteCartes = "";
    private String contenuAnnonce = "";
    private transient String fromClause = null;

    private String moisAnnee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String sommeNbJoursService = "";

    private String sommeTotalIJAI = "";

    /**
     * opération interdite.
     * 
     * @return faux
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * opération interdite.
     * 
     * @return faux
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * opération interdite.
     * 
     * @return faux
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return createFields(_getCollection());
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

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return IJAnnonce.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        compteCartes = statement.dbReadNumeric(FIELDNAME_COMPTECARTES);
        contenuAnnonce = statement.dbReadNumeric(IJAnnonce.FIELDNAME_CONTENUANNONCE);
        moisAnnee = formatDateFromDB(statement.dbReadNumeric(IJAnnonce.FIELDNAME_MOISANNEECOMPTABLE));
        sommeNbJoursService = statement.dbReadString(FIELDNAME_SOMMENBJOURS);
        sommeTotalIJAI = statement.dbReadString(FIELDNAME_SOMMETOTALIJ);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    private String formatDateFromDB(String date) {
        try {
            String ret = JACalendar.format(date.substring(4, 6) + date.substring(0, 4), JACalendar.FORMAT_MMsYYYY);

            return ret;
        } catch (Exception e) {
            return date;
        }
    }

    public String getCompteCartes() {
        return compteCartes;
    }

    public String getContenuAnnonce() {
        return contenuAnnonce;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String getSommeNbJoursService() {
        return sommeNbJoursService;
    }

    public String getSommeTotalIJAI() {
        return sommeTotalIJAI;
    }

    public void setCompteCartes(String compteCartes) {
        this.compteCartes = compteCartes;
    }

    public void setContenuAnnonce(String contenuAnnonce) {
        this.contenuAnnonce = contenuAnnonce;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setSommeNbJoursService(String sommeNbJoursService) {
        this.sommeNbJoursService = sommeNbJoursService;
    }

    public void setSommeTotalIJAI(String sommeTotalIJAI) {
        this.sommeTotalIJAI = sommeTotalIJAI;
    }
}
