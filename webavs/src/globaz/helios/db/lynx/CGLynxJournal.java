package globaz.helios.db.lynx;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CGLynxJournal extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ETAT_ANNULE = "7400003";
    public final static String ETAT_COMPTABILISE = "7400004";

    public static final String FIELD_CSETAT = "CSETAT";
    public static final String FIELD_DATEVALEURCG = "DATEVALEURCG";

    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    public static final String TABLE_NAME = "LXJOURP";
    private String csEtat;

    private String idJournal;

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(FIELD_CSETAT, _dbWriteNumeric(statement.getTransaction(), getCsEtat(), "etat"));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Do nothing here !
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Do nothing here !
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

}
