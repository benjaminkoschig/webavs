/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.paiement;

import globaz.corvus.db.lots.RELot;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFLot extends RELot {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_GESTIONNAIRE = "FWIGES";
    public static final String FIELDNAME_ID_LOT_RFM = "FWILOT";

    public static final String TABLE_NAME = "RFLOTS";

    public static final RFLot loadLot(BSession session, BITransaction transaction, String idLot) throws Exception {
        RFLot retValue;

        retValue = new RFLot();
        retValue.setIdLotRFM(idLot);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    private String idGestionnaire = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String idLotRFM = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFLot.
     */
    public RFLot() {
        super();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFLot.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RELot.TABLE_NAME_LOT;
        getFrom += " ON ";
        getFrom += RELot.FIELDNAME_ID_LOT;
        getFrom += "=";
        getFrom += RFLot.FIELDNAME_ID_LOT_RFM;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFLot.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idLotRFM = statement.dbReadNumeric(RFLot.FIELDNAME_ID_LOT_RFM);
        idGestionnaire = statement.dbReadString(RFLot.FIELDNAME_ID_GESTIONNAIRE);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demande maintien à dom.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFLot.FIELDNAME_ID_LOT_RFM,
                this._dbWriteNumeric(statement.getTransaction(), idLotRFM, "idLotRFM"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFLot.FIELDNAME_ID_LOT_RFM,
                this._dbWriteNumeric(statement.getTransaction(), idLotRFM, "idLotRFM"));
        statement.writeField(RFLot.FIELDNAME_ID_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdLotRFM() {
        return idLotRFM;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdLotRFM(String idLotRFM) {
        this.idLotRFM = idLotRFM;
    }
}
