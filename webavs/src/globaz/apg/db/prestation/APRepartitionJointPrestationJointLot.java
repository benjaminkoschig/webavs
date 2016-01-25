package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.lots.APLot;
import globaz.globall.db.BStatement;

public class APRepartitionJointPrestationJointLot extends APRepartitionJointPrestation {

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
        fromClause.append(APPrestation.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(schema);
        fromClause.append(APRepartitionPaiements.TABLE_NAME);
        fromClause.append(".");
        fromClause.append(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        fromClause.append("=");
        fromClause.append(schema);
        fromClause.append(APPrestation.TABLE_NAME);
        fromClause.append(".");
        fromClause.append(APPrestation.FIELDNAME_IDPRESTATIONAPG);

        // jointure avec la table des droits
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClause.append(" ON ");
        fromClause.append(schema);
        fromClause.append(APPrestation.TABLE_NAME);
        fromClause.append(".");
        fromClause.append(APPrestation.FIELDNAME_IDDROIT);
        fromClause.append("=");
        fromClause.append(schema);
        fromClause.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClause.append(".");
        fromClause.append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);

        // jointure entre tables prestation et lot
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(APLot.TABLE_NAME);
        fromClause.append(" ON ");
        fromClause.append(schema);
        fromClause.append(APPrestation.TABLE_NAME);
        fromClause.append(".");
        fromClause.append(APPrestation.FIELDNAME_IDLOT);
        fromClause.append("=");
        fromClause.append(schema);
        fromClause.append(APLot.TABLE_NAME);
        fromClause.append(".");
        fromClause.append(APLot.FIELDNAME_IDLOT);

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

        idLot = statement.dbReadNumeric(APLot.FIELDNAME_IDLOT);
        csEtatLot = statement.dbReadNumeric(APLot.FIELDNAME_ETAT);
        dateComptaLot = statement.dbReadDateAMJ(APLot.FIELDNAME_DATECOMPTABLE);
        dateCreationLot = statement.dbReadDateAMJ(APLot.FIELDNAME_DATECREATION);

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