package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPCommentaireCommunication extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IMTICO) */
    // code systeme
    public final static String CS_DOSS_EN_RECLAMATION = "607063";
    public final static String CS_DOSS_INCOMPLET = "607064";
    public final static String CS_DOSS_NON_NOTIFIE = "607060";
    public final static String CS_DOSS_NON_TROUVE = "607058";
    public final static String CS_DOSS_NOTIFIE = "607061";
    public final static String CS_DOSS_STOPPE = "607062";
    public final static String CS_DOSS_TROUVE = "607059";
    public final static String CS_GENRE_TD = "607051";
    public final static String CS_GENRE_TDD = "607065";
    public final static String CS_GENRE_TDR = "607052";
    public final static String CS_GENRE_TO = "607053";
    public final static String CS_GENRE_TOR = "607054";
    public final static String CS_GENRE_TP = "607055";
    public final static String CS_GENRE_TPD = "607066";
    public final static String CS_GENRE_TPR = "607056";
    public final static String CS_GENRE_TRD = "607057";
    public final static String CS_GENRE_TRP = "607067";
    public final static String CS_IMPOSITION_SOURCE = "607068";
    public final static String CS_NON_ASSUJETTI_IBO = "607079";
    public final static String CS_NON_ASSUJETTI_IFD = "607070";
    public final static String CS_PRESENCE_COMMENTAIRE = "607071";
    /** (IRICCF) */
    private String idCommentaire = "";
    /** (IKIRET) */
    /** Fichier CPCOCFP */
    private String idCommentaireCf = "";
    /** Fichier CPCOCFP */
    private String idCommunicationRetour = "";

    /**
     * Commentaire relatif au constructeur CPCommentaireRemarqueType
     */
    public CPCommentaireCommunication() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdCommentaireCf(this._incCounter(transaction, idCommentaireCf));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPCOCFP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCommentaireCf = statement.dbReadNumeric("IRICCF");
        idCommunicationRetour = statement.dbReadNumeric("IKIRET");
        idCommentaire = statement.dbReadNumeric("IMTICO");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par défaut : pas de clé alternée
        // Recherche par décisoin et genre de cotisation
        if (alternateKey == 1) {
            statement.writeKey(_getBaseTable() + "IKIRET",
                    this._dbWriteNumeric(statement.getTransaction(), getIdCommunicationRetour(), ""));
            statement.writeKey(_getBaseTable() + "IMTICO",
                    this._dbWriteNumeric(statement.getTransaction(), getIdCommentaire(), ""));
        }
    }

    /**

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IRICCF", this._dbWriteNumeric(statement.getTransaction(), getIdCommentaireCf(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("IRICCF",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommentaireCf(), "idCommentaireCf"));
        statement.writeField("IKIRET",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunicationRetour(), "idCommunicationRetour"));
        statement.writeField("IMTICO",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommentaire(), "idCommentaire"));
    }

    public String getIdCommentaire() {
        return idCommentaire;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdCommentaireCf() {
        return idCommentaireCf;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdCommunicationRetour() {
        return idCommunicationRetour;
    }

    public void setIdCommentaire(String newIdCommentaire) {
        idCommentaire = newIdCommentaire;
    }

    public void setIdCommentaireCf(String newIdCommentaireCf) {
        idCommentaireCf = newIdCommentaireCf;
    }

    public void setIdCommunicationRetour(String newIdCommunication) {
        idCommunicationRetour = newIdCommunication;
    }
}
