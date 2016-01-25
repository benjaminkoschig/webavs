/*
 * Créé le 24.07.2009
 */
package globaz.libra.db.journalisations;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author HPE
 * 
 */
public class LIEcheancesMultiple extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ALTERNATE_KEY_ID_JOURNALISATION = 1;
    public static final String FIELDNAME_ANNEE = "ACNANN";
    public static final String FIELDNAME_DATE_RECEPTION = "ACDREC";
    public static final String FIELDNAME_ID_ECHEANCES_MULTIPLE = "ACIEMU";
    public static final String FIELDNAME_ID_JOURNALISATION = "ACIJOU";
    public static final String FIELDNAME_IS_RECU = "ACBREC";

    public static final String FIELDNAME_LIBELLE = "ACLLIB";

    public static final String TABLE_NAME = "LIECHMU";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annee = new String();
    private String dateReception = new String();
    private String idEcheanceMultiple = new String();
    private String idJournalisation = new String();
    private Boolean isRecu = Boolean.FALSE;
    private String libelle = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIEcheancesMultiple.
     */
    public LIEcheancesMultiple() {
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
        setIdEcheanceMultiple(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table pour les échéances multiples
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

        idEcheanceMultiple = statement.dbReadNumeric(FIELDNAME_ID_ECHEANCES_MULTIPLE);
        idJournalisation = statement.dbReadNumeric(FIELDNAME_ID_JOURNALISATION);
        isRecu = statement.dbReadBoolean(FIELDNAME_IS_RECU);
        dateReception = statement.dbReadDateAMJ(FIELDNAME_DATE_RECEPTION);
        libelle = statement.dbReadString(FIELDNAME_LIBELLE);
        annee = statement.dbReadNumeric(FIELDNAME_ANNEE);

    }

    /**
     * Méthode de validation des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * Définition de la clé primaire de la table des échéances multiples
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {

        if (alternateKey == ALTERNATE_KEY_ID_JOURNALISATION) {
            statement.writeKey(FIELDNAME_ID_JOURNALISATION,
                    _dbWriteNumeric(statement.getTransaction(), idJournalisation, "idJournalisation"));
        } else {
            statement.writeKey(FIELDNAME_ID_ECHEANCES_MULTIPLE,
                    _dbWriteNumeric(statement.getTransaction(), idEcheanceMultiple, "idEcheanceMultiple"));
        }

    }

    /**
     * Définition de la clé primaire de la table des échéances multiples
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(FIELDNAME_ID_ECHEANCES_MULTIPLE,
                _dbWriteNumeric(statement.getTransaction(), idEcheanceMultiple, "idEcheanceMultiple"));

    }

    /**
     * Méthode d'écriture des champs dans la table des échéances
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_ECHEANCES_MULTIPLE,
                _dbWriteNumeric(statement.getTransaction(), idEcheanceMultiple, "idEcheanceMultiple"));
        statement.writeField(FIELDNAME_ID_JOURNALISATION,
                _dbWriteNumeric(statement.getTransaction(), idJournalisation, "idJournalisation"));
        statement.writeField(FIELDNAME_IS_RECU,
                _dbWriteBoolean(statement.getTransaction(), isRecu, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRecu"));
        statement.writeField(FIELDNAME_DATE_RECEPTION,
                _dbWriteDateAMJ(statement.getTransaction(), dateReception, "dateReception"));
        statement.writeField(FIELDNAME_LIBELLE, _dbWriteString(statement.getTransaction(), libelle, "libelle"));
        statement.writeField(FIELDNAME_ANNEE, _dbWriteNumeric(statement.getTransaction(), annee, "annee"));

    }

    public String getAnnee() {
        return annee;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getIdEcheanceMultiple() {
        return idEcheanceMultiple;
    }

    public String getIdJournalisation() {
        return idJournalisation;
    }

    public Boolean getIsRecu() {
        return isRecu;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setIdEcheanceMultiple(String idEcheanceMultiple) {
        this.idEcheanceMultiple = idEcheanceMultiple;
    }

    public void setIdJournalisation(String idJournalisation) {
        this.idJournalisation = idJournalisation;
    }

    public void setIsRecu(Boolean isRecu) {
        this.isRecu = isRecu;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}