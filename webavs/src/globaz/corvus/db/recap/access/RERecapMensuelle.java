package globaz.corvus.db.recap.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:34:32 CET 2007
 */
public class RERecapMensuelle extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Table : RERECMEN */
    /** clé alterné par date de rapport */
    public static final int DATE_RAPPORT_KEY = 1;
    /** csEtat - cs état récap mensuelle (ZRTETA) */
    private String csEtat = new String();
    /** dateRapportMensuel - date rapport mensuel (MMxAAAA) (ZRDREC) */
    private String dateRapportMensuel = new String();
    /** idRecapMensuelle - id récap mensuelle (ZRIRM) */
    private String idRecapMensuelle = new String();

    /**
     * Constructeur de la classe RERecapMensuelle
     */
    public RERecapMensuelle() {
        super();
    }

    /**
     * Méthode qui incrémente la clé primaire
     * 
     * @param transaction
     *            BTransaction transaction
     * @throws Exception
     *             exception
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRecapMensuelle(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table RERECMEN
     * 
     * @return String RERECMEN
     */
    @Override
    protected String _getTableName() {
        return IRERecapMensuelleDefTable.TABLE_NAME;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRecapMensuelle = statement.dbReadNumeric(IRERecapMensuelleDefTable.ID_RECAP_MENSUELLE);
        dateRapportMensuel = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(statement
                .dbReadNumeric(IRERecapMensuelleDefTable.DATE_RAPPORT_MENSUEL));
        csEtat = statement.dbReadNumeric(IRERecapMensuelleDefTable.CS_ETAT);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case RERecapMensuelle.DATE_RAPPORT_KEY:
                // Clé alternée numéro 1 : idType et idValeur
                statement.writeKey(IRERecapMensuelleDefTable.DATE_RAPPORT_MENSUEL,
                        _dbWriteDateAMJ(statement.getTransaction(), getDateRapportMensuel(), "date rapport mensuel"));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");

        }
    }

    /**
     * Indique la clé principale ERecapMensuelle() du fichier RERECMEN
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écriture de la clé
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                IRERecapMensuelleDefTable.ID_RECAP_MENSUELLE,
                _dbWriteNumeric(statement.getTransaction(), getIdRecapMensuelle(),
                        "idRecapMensuelle - id récap mensuelle"));
    }

    /**
     * Ecriture des propriétés
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écritrues des propriétés
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(
                IRERecapMensuelleDefTable.ID_RECAP_MENSUELLE,
                _dbWriteNumeric(statement.getTransaction(), getIdRecapMensuelle(),
                        "idRecapMensuelle - id récap mensuelle"));
        statement.writeField(
                IRERecapMensuelleDefTable.DATE_RAPPORT_MENSUEL,
                _dbWriteDateAMJ(statement.getTransaction(), getDateRapportMensuel(),
                        "dateRapportMensuel - date rapport mensuel (MMxAAAA)"));
        statement.writeField(IRERecapMensuelleDefTable.CS_ETAT,
                _dbWriteNumeric(statement.getTransaction(), getCsEtat(), "csEtat - cs état récap mensuelle"));
    }

    /**
     * Renvoie la zone csEtat - cs état récap mensuelle (ZRTETA)
     * 
     * @return String csEtat - cs état récap mensuelle
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * Renvoie la zone dateRapportMensuel - date rapport mensuel (ZRDREC)
     * 
     * @return String dateRapportMensuel - date du rapport mensuel (MMxAAAA)
     */
    public String getDateRapportMensuel() {
        return dateRapportMensuel;
    }

    /**
     * Renvoie la zone idRecapMensuelle - id récap mensuelle (ZRIRM)
     * 
     * @return String idRecapMensuelle - id récap mensuelle
     */
    public String getIdRecapMensuelle() {
        return idRecapMensuelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * Modifie la zone csEtat - cs état récap mensuelle (ZRTETA)
     * 
     * @param newCsEtat
     *            - cs état récap mensuelle String
     */
    public void setCsEtat(String newCsEtat) {
        csEtat = newCsEtat;
    }

    /**
     * Modifie la zone dateRapportMensuel - date rapport mensuel(ZRDREC)
     * 
     * @param newIdRecap
     *            - date rapport mensuel (MMxAAAA) String
     */
    public void setDateRapportMensuel(String newDateRapportMensuel) {
        dateRapportMensuel = newDateRapportMensuel;
    }

    /**
     * Modifie la zone idRecapMensuelle - id récap mensuelle (ZRIRM)
     * 
     * @param newIdRecapMensuelle
     *            - id récap mensuelle String
     */
    public void setIdRecapMensuelle(String newIdRecapMensuelle) {
        idRecapMensuelle = newIdRecapMensuelle;
    }
}
