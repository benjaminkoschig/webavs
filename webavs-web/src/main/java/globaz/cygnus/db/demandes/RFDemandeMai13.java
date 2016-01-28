/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.demandes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFDemandeMai13 extends RFDemande {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_DEMANDE_MAINTIEN_DOM_13 = "ELIDEM";
    public static final String FIELDNAME_NOMBRE_D_HEURE = "ELNHEU";

    public static final String TABLE_NAME = "RFDMA13";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private boolean hasCreationSpy = false;
    private boolean hasSpy = false;
    private String idDemandeMaintienDom13 = "";
    private String nombreHeure = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeMai13
     */
    public RFDemandeMai13() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
        // super._beforeAdd(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFDemande.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFDemandeMai13.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFDemandeMai13.FIELDNAME_ID_DEMANDE_MAINTIEN_DOM_13;
        getFrom += "=";
        getFrom += RFDemande.FIELDNAME_ID_DEMANDE;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des demande maintien à dom.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDemandeMai13.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des demande maintien à dom.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idDemandeMaintienDom13 = statement.dbReadNumeric(RFDemandeMai13.FIELDNAME_ID_DEMANDE_MAINTIEN_DOM_13);
        nombreHeure = statement.dbReadNumeric(RFDemandeMai13.FIELDNAME_NOMBRE_D_HEURE);

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
        statement.writeKey(RFDemandeMai13.FIELDNAME_ID_DEMANDE_MAINTIEN_DOM_13,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeMaintienDom13, "idDemandeMaintienDom13"));

    }

    /**
     * Méthode d'écriture des champs dans la table des demande maintien à dom.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // super._writeProperties(statement);
        statement.writeField(RFDemandeMai13.FIELDNAME_ID_DEMANDE_MAINTIEN_DOM_13,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeMaintienDom13, "idDemandeMaintienDom13"));
        statement.writeField(RFDemandeMai13.FIELDNAME_NOMBRE_D_HEURE,
                this._dbWriteNumeric(statement.getTransaction(), nombreHeure, "nombreHeure"));

    }

    public String getIdDemandeMaintienDom13() {
        return idDemandeMaintienDom13;
    }

    public String getNombreHeure() {
        return nombreHeure;
    }

    @Override
    public boolean hasCreationSpy() {
        return hasCreationSpy;
    }

    @Override
    public boolean hasSpy() {
        return hasSpy;
    }

    public boolean isHasCreationSpy() {
        return hasCreationSpy;
    }

    public boolean isHasSpy() {
        return hasSpy;
    }

    public void setHasCreationSpy(boolean hasCreationSpy) {
        this.hasCreationSpy = hasCreationSpy;
    }

    public void setHasSpy(boolean hasSpy) {
        this.hasSpy = hasSpy;
    }

    public void setIdDemandeMaintienDom13(String idDemandeMaintienDom13) {
        this.idDemandeMaintienDom13 = idDemandeMaintienDom13;
    }

    public void setNombreHeure(String nombreHeure) {
        this.nombreHeure = nombreHeure;
    }

}