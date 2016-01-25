package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPCommentaireRemarqueType extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ACT_ACCESSOIRE = "607015";
    public final static String CS_ASSISTANCE = "607003";
    public final static String CS_BVR = "607016";
    public final static String CS_CONJOINT = "607014";
    public final static String CS_COPIE_ATTESTATION = "607001";
    public final static String CS_DOUBLE_REV_AF = "607004";
    public final static String CS_EXEMPTE_FRANCHISE = "607073";
    public final static String CS_OPPOSITION = "607002";
    public final static String CS_PRORATA_REVN = "607010";
    public final static String CS_RENTIER = "607074";
    /** (IOIDRE) */
    private String idCommentaire = "";
    /** Fichier CPCORTP */
    private String idCommentaireRemarque = "";
    /** (ISICRE) */
    private String idRemarqueType = "";

    // code systeme
    /**
     * Commentaire relatif au constructeur CPCommentaireRemarqueType
     */
    public CPCommentaireRemarqueType() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdCommentaireRemarque(this._incCounter(transaction, idCommentaireRemarque));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPCORTP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCommentaireRemarque = statement.dbReadNumeric("ISICRE");
        idRemarqueType = statement.dbReadNumeric("IOIDRE");
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

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("ISICRE", this._dbWriteNumeric(statement.getTransaction(), getIdCommentaireRemarque(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("ISICRE",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommentaireRemarque(), "idCommentaireRemarque"));
        statement.writeField("IOIDRE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarqueType(), "idRemarqueType"));
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
    public String getIdCommentaireRemarque() {
        return idCommentaireRemarque;
    }

    public String getIdRemarqueType() {
        return idRemarqueType;
    }

    public void setIdCommentaire(String newIdCommentaire) {
        idCommentaire = newIdCommentaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdCommentaireRemarque(String newIdCommentaireRemarque) {
        idCommentaireRemarque = newIdCommentaireRemarque;
    }

    public void setIdRemarqueType(String newIdRemarqueType) {
        idRemarqueType = newIdRemarqueType;
    }
}
