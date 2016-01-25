package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class CIAnnonceCentraleBlob extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String COLUMN_NAME_ID_ANNONCE = "ANIDAN";
    public static final String COLUMN_NAME_ID_ANNONCE_BLOB = "ANIDAB";
    public static final String COLUMN_NAME_ID_BLOB = "ANIDBL";
    public static final String TABLE_NAME_ANNONCE_CENTRALE_BLOB = "CIANBLOB";

    private String idAnnonce;
    private String idAnnonceBlob;
    private String idBlob;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdAnnonceBlob(this._incCounter(transaction, getIdAnnonceBlob()));
    }

    @Override
    protected String _getTableName() {
        return CIAnnonceCentraleBlob.TABLE_NAME_ANNONCE_CENTRALE_BLOB;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnonceBlob = statement.dbReadNumeric(CIAnnonceCentraleBlob.COLUMN_NAME_ID_ANNONCE_BLOB);
        idAnnonce = statement.dbReadNumeric(CIAnnonceCentraleBlob.COLUMN_NAME_ID_ANNONCE);
        idBlob = statement.dbReadString(CIAnnonceCentraleBlob.COLUMN_NAME_ID_BLOB);

    }

    @Override
    protected void _validate(BStatement statement) {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CIAnnonceCentraleBlob.COLUMN_NAME_ID_ANNONCE_BLOB,
                this._dbWriteNumeric(statement.getTransaction(), getIdAnnonceBlob(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(CIAnnonceCentraleBlob.COLUMN_NAME_ID_ANNONCE_BLOB,
                this._dbWriteNumeric(statement.getTransaction(), getIdAnnonceBlob(), "idAnnonceBlob"));

        statement.writeField(CIAnnonceCentraleBlob.COLUMN_NAME_ID_ANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));

        statement.writeField(CIAnnonceCentraleBlob.COLUMN_NAME_ID_BLOB,
                this._dbWriteString(statement.getTransaction(), getIdBlob(), "idBlob"));
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getIdAnnonceBlob() {
        return idAnnonceBlob;
    }

    public String getIdBlob() {
        return idBlob;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public void setIdAnnonceBlob(String idAnnonceBlob) {
        this.idAnnonceBlob = idAnnonceBlob;
    }

    public void setIdBlob(String idBlob) {
        this.idBlob = idBlob;
    }

}
