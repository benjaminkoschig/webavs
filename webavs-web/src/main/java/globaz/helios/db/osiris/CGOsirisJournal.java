package globaz.helios.db.osiris;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CGOsirisJournal extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ETAT_ANNULE = "202005";
    public final static String ETAT_COMPTABILISE = "202002";

    public static final String FIELD_DATEVALEURCG = "DATEVALEURCG";
    public static final String FIELD_ETAT = "ETAT";

    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    public static final String TABLE_NAME = "CAJOURP";
    private String etat;

    private String idJournal;

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(FIELD_ETAT, _dbWriteNumeric(statement.getTransaction(), getEtat(), "etat"));
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

    public String getEtat() {
        return etat;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

}
