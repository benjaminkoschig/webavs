/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFAssDemandeDev19Ftd15 extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_ASSOCIATION_DEV19_FTD15 = "FBIADE";
    public static final String FIELDNAME_ID_DEMANDE_DEV19 = "FBIDEV";
    public static final String FIELDNAME_ID_DEMANDE_FTD15 = "FBIFTD";
    public static final String FIELDNAME_MONTANT_ASSOCIE_AU_DEVIS = "FBMDEM";

    public static final String TABLE_NAME = "RFADEFT";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDemandeDev19Ftd15.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String idAssociationDev19Ft15 = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String idDemandeDevis19 = "";
    private String idDemandeFtd15 = "";
    private String montantAssocieAuDevis = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeDev19
     */
    public RFAssDemandeDev19Ftd15() {
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
        setIdAssociationDev19Ft15(this._incCounter(transaction, idAssociationDev19Ft15,
                RFAssDemandeDev19Ftd15.TABLE_NAME));
    }

    /**
     * getter pour le nom de la table des demandes de devis
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFAssDemandeDev19Ftd15.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table association dev19 ftd15
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAssociationDev19Ft15 = statement.dbReadNumeric(RFAssDemandeDev19Ftd15.FIELDNAME_ID_ASSOCIATION_DEV19_FTD15);
        idDemandeDevis19 = statement.dbReadNumeric(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_DEV19);
        idDemandeFtd15 = statement.dbReadNumeric(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_FTD15);
        montantAssocieAuDevis = statement.dbReadNumeric(RFAssDemandeDev19Ftd15.FIELDNAME_MONTANT_ASSOCIE_AU_DEVIS);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demandes de devis
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFAssDemandeDev19Ftd15.FIELDNAME_ID_ASSOCIATION_DEV19_FTD15,
                this._dbWriteNumeric(statement.getTransaction(), idAssociationDev19Ft15, "idAssociationDev19Ft15"));
    }

    /**
     * Méthode d'écriture des champs dans la table des demandes de devis
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFAssDemandeDev19Ftd15.FIELDNAME_ID_ASSOCIATION_DEV19_FTD15,
                this._dbWriteNumeric(statement.getTransaction(), idAssociationDev19Ft15, "idAssociationDev19Ft15"));

        statement.writeField(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_DEV19,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeDevis19, "idDemandeDevis19"));

        statement.writeField(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_FTD15,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeFtd15, "idDemandeFtd15"));

        statement.writeField(RFAssDemandeDev19Ftd15.FIELDNAME_MONTANT_ASSOCIE_AU_DEVIS,
                this._dbWriteNumeric(statement.getTransaction(), montantAssocieAuDevis, "montantAssocieAuDevis"));
    }

    public String getIdAssociationDev19Ft15() {
        return idAssociationDev19Ft15;
    }

    public String getIdDemandeDevis19() {
        return idDemandeDevis19;
    }

    public String getIdDemandeFtd15() {
        return idDemandeFtd15;
    }

    public String getMontantAssocieAuDevis() {
        return montantAssocieAuDevis;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setIdAssociationDev19Ft15(String idAssociationDev19Ft15) {
        this.idAssociationDev19Ft15 = idAssociationDev19Ft15;
    }

    public void setIdDemandeDevis19(String idDemandeDevis19) {
        this.idDemandeDevis19 = idDemandeDevis19;
    }

    public void setIdDemandeFtd15(String idDemandeFtd15) {
        this.idDemandeFtd15 = idDemandeFtd15;
    }

    public void setMontantAssocieAuDevis(String montantAssocieAuDevis) {
        this.montantAssocieAuDevis = montantAssocieAuDevis;
    }

}