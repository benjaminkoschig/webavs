package globaz.ccvd.services.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CCVDEvtRegFGISrc extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static String COL_NIP = "M$IDTIE";
    private static String COL_VALUE = "M$LIREG";
    private static String TABLE_NAME = "MIRE$P";
    private String NIP = new String();
    private String value = new String();

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setNIP(statement.dbReadNumeric(COL_NIP));
        setValue(statement.dbReadString(COL_VALUE));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // On lit uniquement la table
        throw new Exception("Fonction de validation non implémentée");
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(COL_NIP, _dbWriteNumeric(statement.getTransaction(), getNIP(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // On écrit jamais dans la table, on la lit uniquement
        throw new Exception("Fonction d'écriture non implémentée");
    }

    public String getNIP() {
        return NIP;
    }

    public String getValue() {
        return value;
    }

    public void setNIP(String nip) {
        NIP = nip;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
