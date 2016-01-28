package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CIEcrituresSuspens extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Noms des champs
    public static final String FIELDNAME_ID_AFFILIATION = "KBITIE";
    public static final String FIELDNAME_ID_COMPTE_INDIVIDUEL = "KAIIND";
    public static final String FIELDNAME_ID_JOURNAL = "KCID";
    public static final String FIELDNAME_ID_TYPE_COMPTE = "KBTCPT";
    public static final String FIELDNAME_REGISTRE = "KAIREG";
    // Noms des tables
    public static final String TABLE_NAME_COMPTE_INDIVIDUEL = "CIINDIP";
    public static final String TABLE_NAME_ECRITURES = "CIECRIP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idAffiliation = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getTableName() {
        return TABLE_NAME_ECRITURES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffiliation = statement.dbReadNumeric(FIELDNAME_ID_AFFILIATION);
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

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

}
