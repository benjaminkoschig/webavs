package globaz.hermes.test;

import globaz.globall.db.BEntity;

/**
 * Insérez la description du type ici. Date de création : (16.01.2003 15:30:34)
 * 
 * @author: Administrator
 */
public class TestAnnonce extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        // Insérez ici le code de démarrage de l'application
        new TestRetour().go();
    }

    /** (RNLENR) */
    private String champEnregistrement = new String();
    /** (RNDDAN) */
    private String dateAnnonce = new String();
    /** Fichier HEANNOP */
    /** (RNIANN) */
    private String idAnnonce = new String();
    /** (RMILOT) */
    private String idLot = new String();
    /** (RNTMES) */
    private String idMessage = new String();
    /** (RNTPRO) */
    private String idProgramme = new String();
    /** (RNREFU) */
    private String refUnique = new String();
    /** (RNTSTA) */
    private String statut = new String();

    /** (RNLUTI) */
    private String utilisateur = new String();

    /**
     * Commentaire relatif au constructeur TestRetour.
     */
    public TestAnnonce() {
        super();
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "heannop";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        //
        idAnnonce = statement.dbReadNumeric("RNIANN");
        idLot = statement.dbReadNumeric("RMILOT");
        dateAnnonce = statement.dbReadNumeric("RNDDAN");
        utilisateur = statement.dbReadString("RNLUTI");
        idProgramme = statement.dbReadString("RNTPRO");
        champEnregistrement = statement.dbReadString("RNLENR");
        refUnique = statement.dbReadString("RNREFU");
        statut = statement.dbReadNumeric("RNTSTA");
        idMessage = statement.dbReadNumeric("RNTMES");
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données
     * 
     * @param statement
     *            l'instruction à utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getChampEnregistrement() {
        return champEnregistrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLot() {
        return idLot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdMessage() {
        return idMessage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdProgramme() {
        return idProgramme;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRefUnique() {
        return refUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getStatut() {
        return statut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUtilisateur() {
        return utilisateur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @param newChampEnregistrement
     *            java.lang.String
     */
    public void setChampEnregistrement(java.lang.String newChampEnregistrement) {
        champEnregistrement = newChampEnregistrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @param newDateAnnonce
     *            java.lang.String
     */
    public void setDateAnnonce(java.lang.String newDateAnnonce) {
        dateAnnonce = newDateAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @param newIdAnnonce
     *            java.lang.String
     */
    public void setIdAnnonce(java.lang.String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:26)
     * 
     * @param newIdLot
     *            java.lang.String
     */
    public void setIdLot(java.lang.String newIdLot) {
        idLot = newIdLot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @param newIdMessage
     *            java.lang.String
     */
    public void setIdMessage(java.lang.String newIdMessage) {
        idMessage = newIdMessage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @param newIdProgramme
     *            java.lang.String
     */
    public void setIdProgramme(java.lang.String newIdProgramme) {
        idProgramme = newIdProgramme;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @param newRefUnique
     *            java.lang.String
     */
    public void setRefUnique(java.lang.String newRefUnique) {
        refUnique = newRefUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @param newStatut
     *            java.lang.String
     */
    public void setStatut(java.lang.String newStatut) {
        statut = newStatut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2003 15:45:27)
     * 
     * @param newUtilisateur
     *            java.lang.String
     */
    public void setUtilisateur(java.lang.String newUtilisateur) {
        utilisateur = newUtilisateur;
    }
}
