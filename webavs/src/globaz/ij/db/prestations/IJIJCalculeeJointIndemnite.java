package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJIJCalculeeJointIndemnite extends IJIJCalculee {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_IJ_CALCULEE);
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(IJIndemniteJournaliere.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_IJ_CALCULEE);
        fromClause.append("=");
        fromClause.append(IJIndemniteJournaliere.FIELDNAME_ID_IJ_CALCULEE);

        return fromClause.toString();
    }

    private String csTypeIndemnisation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String montantJournalierIndemnite = "";

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
        montantJournalierIndemnite = statement
                .dbReadNumeric(IJIndemniteJournaliere.FIELDNAME_MONTANT_JOURNALIER_INDEMNITE);
        csTypeIndemnisation = statement.dbReadNumeric(IJIndemniteJournaliere.FIELDNAME_CS_TYPE_INDEMNISATION);
    }

    /**
     * getter pour l'attribut cs type indemnisation.
     * 
     * @return la valeur courante de l'attribut cs type indemnisation
     */
    public String getCsTypeIndemnisation() {
        return csTypeIndemnisation;
    }

    /**
     * getter pour l'attribut montant journalier indemnite.
     * 
     * @return la valeur courante de l'attribut montant journalier indemnite
     */
    public String getMontantJournalierIndemnite() {
        return montantJournalierIndemnite;
    }

    /**
     * setter pour l'attribut cs type indemnisation.
     * 
     * @param csTypeIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIndemnisation(String csTypeIndemnisation) {
        this.csTypeIndemnisation = csTypeIndemnisation;
    }

    /**
     * setter pour l'attribut montant journalier indemnite.
     * 
     * @param montantJournalierIndemnite
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantJournalierIndemnite(String montantJournalierIndemnite) {
        this.montantJournalierIndemnite = montantJournalierIndemnite;
    }
}
