package globaz.hermes.test;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (17.01.2003 09:11:04)
 * 
 * @author: Administrator
 */
public class TestRetourManager extends BManager implements FWListViewBeanInterface {
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
        // Insérez ici le code de démarrage de l'application
        new TestRetourManager().go();
    }

    /** (RNLENR) */
    private String forChampEnregistrement = new String();
    /** (RNDDAN) */
    private String forDateAnnonce = new String();
    /** Fichier HEANNOP */
    /** (RNIANN) */
    private String forIdAnnonce = new String();
    /** (RNIANN) */
    private String forIdAnnonceAttente = new String();
    /** (HEA_RNIANN) */
    private String forIdAnnonceRetour = new String();
    /** (ROTATT) */
    private String forIdAnnonceRetourAttendue = new String();
    /** Fichier HEAREAP */
    /** (ROIARA) */
    private String forIdAttenteRetour = new String();
    /** (RMILOT) */
    private String forIdLot = new String();
    /** (RNTMES) */
    private String forIdMessage = new String();
    /** (RNTPRO) */
    private String forIdProgramme = new String();
    /** (ROLRUN) */
    private String forReferenceUnique = new String();
    /** (RNREFU) */
    private String forRefUnique = new String();
    /** (RNTSTA) */
    private String forStatut = new String();

    /** (RNLUTI) */
    private String forUtilisateur = new String();

    /**
     * Commentaire relatif au constructeur TestRetourManager.
     */
    public TestRetourManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        // return _getCollection() + "HEAREAP";
        return _getCollection() + "heareap inner join " + _getCollection() + "heannop on " + _getCollection()
                + "heannop.rnrefu=" + _getCollection() + "heareap.rolrun";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "ROIARA";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdAttenteRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROIARA=" + _dbWriteNumeric(statement.getTransaction(), getForIdAttenteRetour());
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new TestRetour();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForChampEnregistrement() {
        return forChampEnregistrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDateAnnonce() {
        return forDateAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAnnonceAttente() {
        return forIdAnnonceAttente;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAnnonceRetour() {
        return forIdAnnonceRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAnnonceRetourAttendue() {
        return forIdAnnonceRetourAttendue;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAttenteRetour() {
        return forIdAttenteRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdLot() {
        return forIdLot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMessage() {
        return forIdMessage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdProgramme() {
        return forIdProgramme;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForReferenceUnique() {
        return forReferenceUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForRefUnique() {
        return forRefUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForStatut() {
        return forStatut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForUtilisateur() {
        return forUtilisateur;
    }

    public void go() {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            setSession(session);
            setForIdAttenteRetour("50");
            find();
            System.out.println(size());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        System.exit(-1);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForChampEnregistrement
     *            java.lang.String
     */
    public void setForChampEnregistrement(java.lang.String newForChampEnregistrement) {
        forChampEnregistrement = newForChampEnregistrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForDateAnnonce
     *            java.lang.String
     */
    public void setForDateAnnonce(java.lang.String newForDateAnnonce) {
        forDateAnnonce = newForDateAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForIdAnnonce
     *            java.lang.String
     */
    public void setForIdAnnonce(java.lang.String newForIdAnnonce) {
        forIdAnnonce = newForIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForIdAnnonceAttente
     *            java.lang.String
     */
    public void setForIdAnnonceAttente(java.lang.String newForIdAnnonceAttente) {
        forIdAnnonceAttente = newForIdAnnonceAttente;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForIdAnnonceRetour
     *            java.lang.String
     */
    public void setForIdAnnonceRetour(java.lang.String newForIdAnnonceRetour) {
        forIdAnnonceRetour = newForIdAnnonceRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForIdAnnonceRetourAttendue
     *            java.lang.String
     */
    public void setForIdAnnonceRetourAttendue(java.lang.String newForIdAnnonceRetourAttendue) {
        forIdAnnonceRetourAttendue = newForIdAnnonceRetourAttendue;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForIdAttenteRetour
     *            java.lang.String
     */
    public void setForIdAttenteRetour(java.lang.String newForIdAttenteRetour) {
        forIdAttenteRetour = newForIdAttenteRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForIdLot
     *            java.lang.String
     */
    public void setForIdLot(java.lang.String newForIdLot) {
        forIdLot = newForIdLot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForIdMessage
     *            java.lang.String
     */
    public void setForIdMessage(java.lang.String newForIdMessage) {
        forIdMessage = newForIdMessage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForIdProgramme
     *            java.lang.String
     */
    public void setForIdProgramme(java.lang.String newForIdProgramme) {
        forIdProgramme = newForIdProgramme;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForReferenceUnique
     *            java.lang.String
     */
    public void setForReferenceUnique(java.lang.String newForReferenceUnique) {
        forReferenceUnique = newForReferenceUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForRefUnique
     *            java.lang.String
     */
    public void setForRefUnique(java.lang.String newForRefUnique) {
        forRefUnique = newForRefUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForStatut
     *            java.lang.String
     */
    public void setForStatut(java.lang.String newForStatut) {
        forStatut = newForStatut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2003 09:17:21)
     * 
     * @param newForUtilisateur
     *            java.lang.String
     */
    public void setForUtilisateur(java.lang.String newForUtilisateur) {
        forUtilisateur = newForUtilisateur;
    }
}
