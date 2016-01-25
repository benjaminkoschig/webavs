package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJLot;

public class IJRepartitionJointPrestationJointLot extends IJRepartitionJointPrestation {

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
    public static String createFromClause(String schema) {

        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME);

        // jointure avec la table des prestations
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(IJPrestation.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_IDPRESTATION);
        fromClause.append("=");
        fromClause.append(IJPrestation.FIELDNAME_IDPRESTATION);

        // jointure avec la table des bases d'indemnisation
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(IJBaseIndemnisation.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(IJPrestation.FIELDNAME_ID_BASEINDEMNISATION);
        fromClause.append("=");
        fromClause.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);

        // jointure entre tables prestation et lot
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(IJLot.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(schema);
        fromClause.append(IJPrestation.TABLE_NAME);
        fromClause.append(".");
        fromClause.append(IJPrestation.FIELDNAME_IDLOT);
        fromClause.append("=");
        fromClause.append(schema);
        fromClause.append(IJLot.TABLE_NAME);
        fromClause.append(".");
        fromClause.append(IJLot.FIELDNAME_IDLOT);

        return fromClause.toString();

    }

    private String csEtatLot = "";
    private String dateComptaLot = "";
    private String dateCreationLot = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idLot = "";

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

        idLot = statement.dbReadNumeric(IJLot.FIELDNAME_IDLOT);
        csEtatLot = statement.dbReadNumeric(IJLot.FIELDNAME_CS_ETAT);
        dateComptaLot = statement.dbReadDateAMJ(IJLot.FIELDNAME_DATECOMPTABLE);
        dateCreationLot = statement.dbReadDateAMJ(IJLot.FIELDNAME_DATECREATION);

    }

    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getDateComptaLot() {
        return dateComptaLot;
    }

    public String getDateCreationLot() {
        return dateCreationLot;
    }

    public String getIdLot() {
        return idLot;
    }

    public void setCsEtatLot(String csEtatLot) {
        this.csEtatLot = csEtatLot;
    }

    public void setDateComptaLot(String dateComptaLot) {
        this.dateComptaLot = dateComptaLot;
    }

    public void setDateCreationLot(String dateCreationLot) {
        this.dateCreationLot = dateCreationLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

}
