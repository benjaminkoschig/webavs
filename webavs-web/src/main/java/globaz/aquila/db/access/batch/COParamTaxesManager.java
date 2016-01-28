package globaz.aquila.db.access.batch;

import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class COParamTaxesManager extends COBManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CODEISOLANGUE = "CODEISOLANGUE";
    public static final String FIELD_IDTRADUCTION = "IDTRADUCTION";

    // field table PMTRADP
    public static final String FIELD_LIBELLE = "LIBELLE";
    // field table COCTPEP
    public static final String FIELD_ODIETA = "ODIETA"; // ID ETAPE
    // field table COETAPP
    public static final String FIELD_ODTETA = "ODTETA";
    public static final String FIELD_OFISEQ = "OFISEQ";

    // COCTPEP et COTXCTP ->
    // ID TAXE
    public static final String FIELD_OHBIMP = "OHBIMP"; // IMPUTER TAXES
    public static final String FIELD_OIICTX = "OIICTX"; // champs identique sur
    // field table COTXCTP
    public static final String FIELD_OIIRUB = "OIIRUB"; // ID RUBRIQUE

    public static final String FIELD_OIITRA = "OIITRA"; // ID TRADUCTION
    public static final String FIELD_OIMFIX = "OIMFIX"; // MONTANT FIXE
    public static final String FIELD_OITBTX = "OITBTX"; // BASE TAXE
    public static final String FIELD_OITTTE = "OITTTE"; // TYPE TAXE ETAPE
    public static final String FIELD_OITTYP = "OITTYP"; // TYPE TAXE
    public static final String TABLE_COCTPEP = "COCTPEP";
    public static final String TABLE_COETAPP = "COETAPP";

    public static final String TABLE_COTXCTP = "COTXCTP";
    public static final String TABLE_PMTRADP = "PMTRADP";

    private String forEtape = new String();
    private String forIdSequence = new String();

    @Override
    protected String _getFields(BStatement statement) {
        return "B." + FIELD_OIITRA + ", " + "A." + FIELD_ODIETA + ", " + "A." + FIELD_OHBIMP + ", " + "B."
                + FIELD_OITTTE + ", " + "B." + FIELD_OIIRUB + ", " + "B." + FIELD_OITTYP + ", " + "B." + FIELD_OIMFIX
                + ", " + "B." + FIELD_OITBTX + ", " + "B." + FIELD_OIICTX + ", " + "C." + FIELD_LIBELLE + ", " + "D."
                + FIELD_ODTETA + ", " + "D." + FIELD_OFISEQ;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String fromClause = _getCollection() + TABLE_COCTPEP + " A";

        return fromClause + addJointure();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (getForEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "D." + FIELD_ODTETA + EGAL + _dbWriteNumeric(statement.getTransaction(), getForEtape());
        }
        return sqlWhere;
    }

    // protected String _getOrder(BStatement statement){
    //
    // }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COParamTaxes();
    }

    protected String addJointure() {
        String jointure = "";
        jointure = INNER_JOIN + _getCollection() + TABLE_COETAPP + " D" + ON + "A." + FIELD_ODIETA + EGAL + "D."
                + FIELD_ODIETA + AND + "D." + FIELD_OFISEQ + EGAL + getForIdSequence() +

                INNER_JOIN + _getCollection() + TABLE_COTXCTP + " B" + ON + "A." + FIELD_OIICTX + EGAL + "B."
                + FIELD_OIICTX + LEFT_OUTER_JOIN + _getCollection() + TABLE_PMTRADP + " C" + ON + "B." + FIELD_OIITRA
                + EGAL + "C." + FIELD_IDTRADUCTION + AND + FIELD_CODEISOLANGUE + EGAL + "'"
                + getSession().getIdLangueISO().toUpperCase() + "'";
        return jointure;
    }

    public String getForEtape() {
        return forEtape;
    }

    public String getForIdSequence() {
        return forIdSequence;
    }

    public void setForEtape(String forEtape) {
        this.forEtape = forEtape;
    }

    public void setForIdSequence(String forIdSequence) {
        this.forIdSequence = forIdSequence;
    }
}
