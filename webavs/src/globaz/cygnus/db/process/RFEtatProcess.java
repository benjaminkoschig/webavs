/*
 * Créé le 9 novembre 2010
 */
package globaz.cygnus.db.process;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFEtatProcess extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ETAT_PROCESS = "FMTETA";
    public static final String FIELDNAME_ID_PROCESS = "FMIPRO";

    public static final String TABLE_NAME = "RFETPRO";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean etatProcess = Boolean.FALSE;
    private String idProcess = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossier
     */
    public RFEtatProcess() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // setIdEtatProcess(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFEtatProcess.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idProcess = statement.dbReadString(RFEtatProcess.FIELDNAME_ID_PROCESS);
        etatProcess = statement.dbReadBoolean(RFEtatProcess.FIELDNAME_ETAT_PROCESS);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFEtatProcess.FIELDNAME_ID_PROCESS,
                this._dbWriteString(statement.getTransaction(), idProcess, "idProcess"));
    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFEtatProcess.FIELDNAME_ID_PROCESS,
                this._dbWriteString(statement.getTransaction(), idProcess, "idProcess"));
        statement.writeField(RFEtatProcess.FIELDNAME_ETAT_PROCESS, this._dbWriteBoolean(statement.getTransaction(),
                etatProcess, BConstants.DB_TYPE_BOOLEAN_CHAR, "etatProcess"));
    }

    public Boolean getEtatProcess() {
        return etatProcess;
    }

    public String getIdProcess() {
        return idProcess;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setEtatProcess(Boolean etatProcess) {
        this.etatProcess = etatProcess;
    }

    public void setIdProcess(String idProcess) {
        this.idProcess = idProcess;
    }

}