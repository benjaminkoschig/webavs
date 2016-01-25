package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author bjo Classe représentant une décompte aux impôts LTN. Contient l'id du blob relatif (fichier pdf)
 */
public class DSDecompteLtnBlob extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee;
    private String canton;
    private String dateImpression;
    private String fileExtension;
    private String idBlob;
    private String idDecompteLtnBlob;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de 1 l'id de l'incrément
        if (JadeStringUtil.isBlank(getIdDecompteLtnBlob())) {
            setIdDecompteLtnBlob((this._incCounter(transaction, "0")));
        }
    }

    @Override
    protected String _getTableName() {
        return "DSDLTNB";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDecompteLtnBlob = statement.dbReadNumeric("DLTNID");
        idBlob = statement.dbReadString("IDBLOB");
        dateImpression = statement.dbReadDateAMJ("DLTNDA");
        annee = statement.dbReadNumeric("DLTNAN");
        canton = statement.dbReadString("DLTNCA");
        fileExtension = statement.dbReadString("DLTNFE");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".DLTNID",
                this._dbWriteNumeric(statement.getTransaction(), getIdDecompteLtnBlob(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("DLTNID",
                this._dbWriteNumeric(statement.getTransaction(), getIdDecompteLtnBlob(), "idDecompteLtnBlob"));
        statement.writeField("IDBLOB", this._dbWriteString(statement.getTransaction(), getIdBlob(), "idBlob"));
        statement.writeField("DLTNDA",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateImpression(), "dateImpression"));
        statement.writeField("DLTNAN", this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField("DLTNCA", this._dbWriteString(statement.getTransaction(), getCanton(), "canton"));
        statement.writeField("DLTNFE",
                this._dbWriteString(statement.getTransaction(), getFileExtension(), "fileExtension"));
    }

    public String getAnnee() {
        return annee;
    }

    public String getCanton() {
        return canton;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getIdBlob() {
        return idBlob;
    }

    public String getIdDecompteLtnBlob() {
        return idDecompteLtnBlob;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void setIdBlob(String idBlob) {
        this.idBlob = idBlob;
    }

    public void setIdDecompteLtnBlob(String idDecompteLtnBlob) {
        this.idDecompteLtnBlob = idDecompteLtnBlob;
    }
}