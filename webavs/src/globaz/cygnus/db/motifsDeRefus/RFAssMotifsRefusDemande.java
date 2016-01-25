/*
 * Créé le 19 février 2010
 */
package globaz.cygnus.db.motifsDeRefus;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFAssMotifsRefusDemande extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_ASS = "EGIADM";
    public static final String FIELDNAME_ID_DEMANDE = "EGIDEM";
    public static final String FIELDNAME_ID_MOTIF_REFUS = "EGIMOT";
    public static final String FIELDNAME_MNT_MOTIF_REFUS = "EGMNTM";

    public static final String TABLE_NAME = "RFADEMO";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idAssMotifsRefus = "";
    private String idDemande = "";
    private String idMotifsRefus = "";
    private String mntMotifsDeRefus = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFAssMotifsRefusDemande
     */
    public RFAssMotifsRefusDemande() {
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
        setIdAssMotifsRefus(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table association motifs refus demandes
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table association motifs refus demandes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idAssMotifsRefus = statement.dbReadNumeric(FIELDNAME_ID_ASS);
        idDemande = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE);
        idMotifsRefus = statement.dbReadNumeric(FIELDNAME_ID_MOTIF_REFUS);
        mntMotifsDeRefus = statement.dbReadNumeric(FIELDNAME_MNT_MOTIF_REFUS);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table association motifs refus demandes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_ASS,
                _dbWriteNumeric(statement.getTransaction(), idAssMotifsRefus, "idAssMotifsRefus"));
    }

    /**
     * Méthode d'écriture des champs dans la table association motifs refus demandes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_ASS,
                _dbWriteNumeric(statement.getTransaction(), idAssMotifsRefus, "idAssMotifsRefus"));
        statement.writeField(FIELDNAME_ID_DEMANDE, _dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
        statement.writeField(FIELDNAME_ID_MOTIF_REFUS,
                _dbWriteNumeric(statement.getTransaction(), idMotifsRefus, "idMotifsRefus"));
        statement.writeField(FIELDNAME_MNT_MOTIF_REFUS,
                _dbWriteNumeric(statement.getTransaction(), mntMotifsDeRefus, "mntMotifsDeRefus"));

    }

    public String getIdAssMotifsRefus() {
        return idAssMotifsRefus;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdMotifsRefus() {
        return idMotifsRefus;
    }

    public String getMntMotifsDeRefus() {
        return mntMotifsDeRefus;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setIdAssMotifsRefus(String idAssMotifsRefus) {
        this.idAssMotifsRefus = idAssMotifsRefus;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdMotifsRefus(String idMotifsRefus) {
        this.idMotifsRefus = idMotifsRefus;
    }

    public void setMntMotifsDeRefus(String mntMotifsDeRefus) {
        this.mntMotifsDeRefus = mntMotifsDeRefus;
    }

}
