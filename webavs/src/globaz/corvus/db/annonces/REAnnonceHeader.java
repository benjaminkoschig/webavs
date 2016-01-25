package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Entête de toutes les annonces de rentes.
 */
public class REAnnonceHeader extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CODE_APPLICATION = "ZAACOA";
    public static final String FIELDNAME_CODE_ENREGISTREMENT_01 = "ZAACO1";
    public static final String FIELDNAME_ETAT = "ZATETA";
    public static final String FIELDNAME_ID_ANNONCE = "ZAIANH";
    public static final String FIELDNAME_ID_LIEN_ANNONCE = "ZAILIE";
    public static final String FIELDNAME_NUMERO_AGENCE = "ZAANOA";
    public static final String FIELDNAME_NUMERO_CAISSE = "ZAANOC";
    public static final String TABLE_NAME_ANNONCE_HEADER = "REANHEA";

    private String codeApplication = "";
    private String codeEnregistrement01 = "";
    private String etat = "";
    private String idAnnonce = "";
    private String idLienAnnonce = "";
    private String numeroAgence = "";
    private String numeroCaisse = "";

    /**
     * initialise la valeur de Id annonce header
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idAnnonce = this._incCounter(transaction, idAnnonce, REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER;
    }

    @Override
    protected String _getTableName() {
        return REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        codeApplication = statement.dbReadString(REAnnonceHeader.FIELDNAME_CODE_APPLICATION);
        codeEnregistrement01 = statement.dbReadString(REAnnonceHeader.FIELDNAME_CODE_ENREGISTREMENT_01);
        numeroCaisse = statement.dbReadString(REAnnonceHeader.FIELDNAME_NUMERO_CAISSE);
        numeroAgence = statement.dbReadString(REAnnonceHeader.FIELDNAME_NUMERO_AGENCE);
        etat = statement.dbReadNumeric(REAnnonceHeader.FIELDNAME_ETAT);
        idLienAnnonce = statement.dbReadNumeric(REAnnonceHeader.FIELDNAME_ID_LIEN_ANNONCE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(REAnnonceHeader.FIELDNAME_ID_ANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(REAnnonceHeader.FIELDNAME_ID_ANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
        statement.writeField(REAnnonceHeader.FIELDNAME_CODE_APPLICATION,
                this._dbWriteString(statement.getTransaction(), codeApplication, "codeApplication"));
        statement.writeField(REAnnonceHeader.FIELDNAME_CODE_ENREGISTREMENT_01,
                this._dbWriteString(statement.getTransaction(), codeEnregistrement01, "codeEnregistrement01"));
        statement.writeField(REAnnonceHeader.FIELDNAME_NUMERO_CAISSE,
                this._dbWriteString(statement.getTransaction(), numeroCaisse, "numeroCaisse"));
        statement.writeField(REAnnonceHeader.FIELDNAME_NUMERO_AGENCE,
                this._dbWriteString(statement.getTransaction(), numeroAgence, "numeroAgence"));
        statement.writeField(REAnnonceHeader.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), etat, "etat"));
        statement.writeField(REAnnonceHeader.FIELDNAME_ID_LIEN_ANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), idLienAnnonce, "idLienAnnonce"));
    }

    public String getCodeApplication() {
        return codeApplication;
    }

    public String getCodeEnregistrement01() {
        return codeEnregistrement01;
    }

    public String getEtat() {
        return etat;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getIdLienAnnonce() {
        return idLienAnnonce;
    }

    public String getNumeroAgence() {
        return numeroAgence;
    }

    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCodeApplication(String codeApplication) {
        this.codeApplication = codeApplication;
    }

    public void setCodeEnregistrement01(String codeEnregistrement01) {
        this.codeEnregistrement01 = codeEnregistrement01;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public void setIdLienAnnonce(String idLienAnnonce) {
        this.idLienAnnonce = idLienAnnonce;
    }

    public void setNumeroAgence(String numeroAgence) {
        this.numeroAgence = numeroAgence;
    }

    public void setNumeroCaisse(String numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
    }
}
