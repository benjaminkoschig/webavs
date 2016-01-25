package globaz.hermes.test;

import globaz.globall.db.BEntity;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (16.01.2003 15:30:34)
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
        // Ins�rez ici le code de d�marrage de l'application
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
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la lecture des propri�t�s a �chou�e
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
     * Valide le contenu de l'entit� (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant la cl� primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), ""));
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� dans la base de donn�es
     * 
     * @param statement
     *            l'instruction � utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getChampEnregistrement() {
        return champEnregistrement;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLot() {
        return idLot;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdMessage() {
        return idMessage;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdProgramme() {
        return idProgramme;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRefUnique() {
        return refUnique;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getStatut() {
        return statut;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUtilisateur() {
        return utilisateur;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @param newChampEnregistrement
     *            java.lang.String
     */
    public void setChampEnregistrement(java.lang.String newChampEnregistrement) {
        champEnregistrement = newChampEnregistrement;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @param newDateAnnonce
     *            java.lang.String
     */
    public void setDateAnnonce(java.lang.String newDateAnnonce) {
        dateAnnonce = newDateAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @param newIdAnnonce
     *            java.lang.String
     */
    public void setIdAnnonce(java.lang.String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @param newIdLot
     *            java.lang.String
     */
    public void setIdLot(java.lang.String newIdLot) {
        idLot = newIdLot;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @param newIdMessage
     *            java.lang.String
     */
    public void setIdMessage(java.lang.String newIdMessage) {
        idMessage = newIdMessage;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @param newIdProgramme
     *            java.lang.String
     */
    public void setIdProgramme(java.lang.String newIdProgramme) {
        idProgramme = newIdProgramme;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @param newRefUnique
     *            java.lang.String
     */
    public void setRefUnique(java.lang.String newRefUnique) {
        refUnique = newRefUnique;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @param newStatut
     *            java.lang.String
     */
    public void setStatut(java.lang.String newStatut) {
        statut = newStatut;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @param newUtilisateur
     *            java.lang.String
     */
    public void setUtilisateur(java.lang.String newUtilisateur) {
        utilisateur = newUtilisateur;
    }
}
