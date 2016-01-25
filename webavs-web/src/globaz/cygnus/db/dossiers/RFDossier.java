/*
 * Créé le 11 novembre 2009
 */
package globaz.cygnus.db.dossiers;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFDossier extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_ETAT_DOSSIER = "EATETA";
    public static final String FIELDNAME_CS_SOURCE = "EATSRC";
    public static final String FIELDNAME_DATE_DEBUT = "EADDEB";
    public static final String FIELDNAME_DATE_FIN = "EADFIN";
    public static final String FIELDNAME_ID_DOSSIER = "EAIDOS";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "EAIGES";
    public static final String FIELDNAME_ID_PRDEM = "EAIPRD";

    public static final String TABLE_NAME = "RFDOSSI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatDossier = "";
    private String csSource = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String idDossier = "";
    private String idGestionnaire = "";
    private String idPrDem = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossier
     */
    public RFDossier() {
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
        setIdDossier(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idDossier = statement.dbReadNumeric(FIELDNAME_ID_DOSSIER);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATE_FIN);
        csSource = statement.dbReadNumeric(FIELDNAME_CS_SOURCE);
        csEtatDossier = statement.dbReadNumeric(FIELDNAME_CS_ETAT_DOSSIER);
        idGestionnaire = statement.dbReadString(FIELDNAME_ID_GESTIONNAIRE);
        idPrDem = statement.dbReadNumeric(FIELDNAME_ID_PRDEM);

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
        statement.writeKey(FIELDNAME_ID_DOSSIER, _dbWriteNumeric(statement.getTransaction(), idDossier, "idDossier"));
    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_DOSSIER, _dbWriteNumeric(statement.getTransaction(), idDossier, "idDossier"));
        statement.writeField(FIELDNAME_DATE_DEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(FIELDNAME_DATE_FIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(FIELDNAME_CS_SOURCE, _dbWriteNumeric(statement.getTransaction(), csSource, "csSource"));
        statement.writeField(FIELDNAME_CS_ETAT_DOSSIER,
                _dbWriteNumeric(statement.getTransaction(), csEtatDossier, "csEtatDossier"));
        statement.writeField(FIELDNAME_ID_GESTIONNAIRE,
                _dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(FIELDNAME_ID_PRDEM, _dbWriteNumeric(statement.getTransaction(), idPrDem, "idPrDem"));

    }

    public String getCsEtatDossier() {
        return csEtatDossier;
    }

    public String getCsSource() {
        return csSource;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdPrDem() {
        return idPrDem;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCsEtatDossier(String etat) {
        csEtatDossier = etat;
    }

    public void setCsSource(String csSource) {
        this.csSource = csSource;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdPrDem(String idPrDem) {
        this.idPrDem = idPrDem;
    }

}