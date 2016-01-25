package globaz.corvus.db.adaptation;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BStatement;

/**
 * @author HPE
 */
public class RERentesAdapteesJointRATiers extends RERentesAdaptees {

    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_TIERS = "TITIERP";

    public static String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERentesAdaptees.TABLE_NAME_RENTES_ADAPTEES);

        // jointure entre table des prestations accordées et table rentes
        // adaptées
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
        fromClauseBuffer.append(RERentesAdaptees.TABLE_NAME_RENTES_ADAPTEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERentesAdaptees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre table des prestations accordées et table des nss
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des nss et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient String fromClause = null;
    private String nomRA = "";
    private String nssRA = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String prenomRA = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = RERentesAdapteesJointRATiers.createFromClause(_getCollection());
        }

        return fromClause;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        nssRA = NSUtil.formatAVSUnknown(statement.dbReadString(FIELDNAME_NUM_AVS));
        nomRA = statement.dbReadString(FIELDNAME_NOM);
        prenomRA = statement.dbReadString(FIELDNAME_PRENOM);
    }

    public String getNomRA() {
        return nomRA;
    }

    public String getNssRA() {
        return nssRA;
    }

    public String getPrenomRA() {
        return prenomRA;
    }

    public void setNomRA(String nomRA) {
        this.nomRA = nomRA;
    }

    public void setNssRA(String nssRA) {
        this.nssRA = nssRA;
    }

    public void setPrenomRA(String prenomRA) {
        this.prenomRA = prenomRA;
    }

}
