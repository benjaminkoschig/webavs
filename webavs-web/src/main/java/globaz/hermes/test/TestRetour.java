package globaz.hermes.test;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (16.01.2003 15:30:34)
 * 
 * @author: Administrator
 */
public class TestRetour extends BEntity {
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

    /** Fichier HEANNOP */
    private TestAnnonce annonce = null;
    /** (RNIANN) */
    private String idAnnonceAttente = new String();
    /** (HEA_RNIANN) */
    private String idAnnonceRetour = new String();
    /** (ROTATT) */
    private String idAnnonceRetourAttendue = new String();
    /** Fichier HEAREAP */
    /** (ROIARA) */
    private String idAttenteRetour = new String();

    /** (ROLRUN) */
    private String referenceUnique = new String();

    /**
     * Commentaire relatif au constructeur TestRetour.
     */
    public TestRetour() {
        super();
    }

    /**
     * Effectue des traitements apr�s une lecture dans la BD et apr�s avoir vid� le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la lecture de l'entit� dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // je r�cup�re le code application
        /*
         * String codeApplication = getChampEnregistrement().substring(0, 2); HECodeapplicationListViewBean codeAppList
         * = new HECodeapplicationListViewBean(); codeAppList.setSession(getSession());
         * codeAppList.setForCodeUtilisateur(codeApplication); codeAppList.find(transaction); codeApplicationViewBean =
         * (HECodeapplicationViewBean) codeAppList.getEntity(0);
         */
        // code app charg�
        // je cherche la longueur du code enregistrement pour ce code
        // application
        // premier type de parametrage avec ce code application
    }

    /**
     * Effectue des traitements apr�s une lecture dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la lecture de l'entit� dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieveWithResultSet(BStatement statement) throws java.lang.Exception {
        getAnnonce().read(statement);
    }

    /**
     * Effectue des traitements apr�s une mise � jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la mise � jour de l'entit� dans la BD
     * <p>
     * La transaction n'est pas valid�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
        boolean spyDisabled = false;
        if (transaction != null) {
            spyDisabled = transaction.isSpyDisabled();
        }
        try {
            if (transaction != null) {
                transaction.disableSpy();
            }
            getAnnonce().update(transaction);
        } finally {
            if ((transaction != null) && (!spyDisabled)) {
                transaction.enableSpy();
            }
        }
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "heareap inner join " + _getCollection() + "heannop on " + _getCollection()
                + "heannop.rnrefu=" + _getCollection() + "heareap.rolrun";
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "heareap";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la lecture des propri�t�s a �chou�e
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idAttenteRetour = statement.dbReadNumeric("ROIARA");
        idAnnonceAttente = statement.dbReadNumeric("RNIANN");
        idAnnonceRetour = statement.dbReadNumeric("HEA_RNIANN");
        idAnnonceRetourAttendue = statement.dbReadNumeric("ROTATT");
        referenceUnique = statement.dbReadString("ROLRUN");
    }

    /**
     * R�initialise les pointeurs sur d'autres DAB contenus dans l'entit� <i>
     * <p>
     * A surcharger pour remettre � <code>null</code> les pointeurs sur les autres DAB </i>
     */
    @Override
    protected void _resetPointers() {
        annonce = null;
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
        statement.writeKey("ROIARA", _dbWriteNumeric(statement.getTransaction(), getIdAttenteRetour(), ""));
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
    public TestAnnonce getAnnonce() {
        if (annonce == null) {
            annonce = new TestAnnonce();
            annonce.setSession(getSession());
        }
        return annonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAnnonceAttente() {
        return idAnnonceAttente;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAnnonceRetour() {
        return idAnnonceRetour;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAnnonceRetourAttendue() {
        return idAnnonceRetourAttendue;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAttenteRetour() {
        return idAttenteRetour;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReferenceUnique() {
        return referenceUnique;
    }

    public void go() {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            setSession(session);
            setIdAttenteRetour("50");
            retrieve();
            System.out.println("");
            update();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        System.exit(-1);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @param newIdAnnonceAttente
     *            java.lang.String
     */
    public void setIdAnnonceAttente(java.lang.String newIdAnnonceAttente) {
        idAnnonceAttente = newIdAnnonceAttente;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @param newIdAnnonceRetour
     *            java.lang.String
     */
    public void setIdAnnonceRetour(java.lang.String newIdAnnonceRetour) {
        idAnnonceRetour = newIdAnnonceRetour;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @param newIdAnnonceRetourAttendue
     *            java.lang.String
     */
    public void setIdAnnonceRetourAttendue(java.lang.String newIdAnnonceRetourAttendue) {
        idAnnonceRetourAttendue = newIdAnnonceRetourAttendue;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:26)
     * 
     * @param newIdAttenteRetour
     *            java.lang.String
     */
    public void setIdAttenteRetour(java.lang.String newIdAttenteRetour) {
        idAttenteRetour = newIdAttenteRetour;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2003 15:45:27)
     * 
     * @param newReferenceUnique
     *            java.lang.String
     */
    public void setReferenceUnique(java.lang.String newReferenceUnique) {
        referenceUnique = newReferenceUnique;
    }
}
