/*
 * Créé le 17 décembre 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFSousTypeDeSoin extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CODE = "EENCOD";
    public static final String FIELDNAME_ID_POT_ASSURE = "EEIPOA";
    public static final String FIELDNAME_ID_RUBRIQUE_COMPTABLE_AI = "EEIRUA";
    public static final String FIELDNAME_ID_RUBRIQUE_COMPTABLE_AVS = "EEIRUV";
    public static final String FIELDNAME_ID_RUBRIQUE_COMPTABLE_INVALIDITE = "EEIRUI";

    public static final String FIELDNAME_ID_SOUS_TYPE_SOIN = "EEISTS";
    public static final String FIELDNAME_ID_TYPE_SOIN = "EEITSO";

    public static final String TABLE_NAME = "RFSTSOI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final RFSousTypeDeSoin loadSousTypeSoin(BSession session, BITransaction transaction,
            String idSousTypeSoin) throws Exception {
        RFSousTypeDeSoin retValue;

        retValue = new RFSousTypeDeSoin();
        retValue.setIdSousTypeSoin(idSousTypeSoin);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    private String code = "";
    private String idRubriqueComptableAI = "";
    private String idRubriqueComptableAVS = "";
    private String idRubriqueComptableInvalidite = "";
    private String idSousTypeSoin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String idTypeSoin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFSousTypeSoin
     */
    public RFSousTypeDeSoin() {
        super();
    }

    /**
     * Méthode avant l'ajout, incrémentant la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSousTypeSoin(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFSousTypeDeSoin.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des sous type de soins
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSousTypeSoin = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        code = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        idTypeSoin = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        idRubriqueComptableAI = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AI);
        idRubriqueComptableAVS = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AVS);
        idRubriqueComptableInvalidite = statement
                .dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_INVALIDITE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des sous type de soins
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idSousTypeSoin, "idSousTypeSoin"));
    }

    /**
     * Méthode d'écriture des champs dans la table des sous type de soins
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idSousTypeSoin, "idSousTypeSoin"));
        statement.writeField(RFSousTypeDeSoin.FIELDNAME_CODE,
                this._dbWriteString(statement.getTransaction(), code, "code"));
        statement.writeField(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idTypeSoin, "idTypeSoin"));
        statement.writeField(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AI,
                this._dbWriteNumeric(statement.getTransaction(), idRubriqueComptableAI, "idRubriqueComptableAI"));
        statement.writeField(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AVS,
                this._dbWriteNumeric(statement.getTransaction(), idRubriqueComptableAI, "idRubriqueComptableAI"));
        statement.writeField(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_INVALIDITE, this._dbWriteNumeric(
                statement.getTransaction(), idRubriqueComptableInvalidite, "idRubriqueComptableInvalidite"));

    }

    public String getCode() {
        return code;
    }

    public String getIdRubriqueComptableAI() {
        return idRubriqueComptableAI;
    }

    public String getIdRubriqueComptableAVS() {
        return idRubriqueComptableAVS;
    }

    public String getIdRubriqueComptableInvalidite() {
        return idRubriqueComptableInvalidite;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIdRubriqueComptableAI(String idRubriqueComptableAI) {
        this.idRubriqueComptableAI = idRubriqueComptableAI;
    }

    public void setIdRubriqueComptableAVS(String idRubriqueComptableAVS) {
        this.idRubriqueComptableAVS = idRubriqueComptableAVS;
    }

    public void setIdRubriqueComptableInvalidite(String idRubriqueComptableInvalidite) {
        this.idRubriqueComptableInvalidite = idRubriqueComptableInvalidite;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

}
