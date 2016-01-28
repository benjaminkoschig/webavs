package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class FARemarque extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static java.lang.String TABLE_FIELDS = "	FAREMAP.IDREMARQUE, FAREMAP.TEXTE, FAREMAP.PSPY";

    /**
     * Retourne le texte de la remarque par rapport à un id.
     */
    public static java.lang.String getRemarque(BStatement statement) {
        FARemarque oRem = new FARemarque();
        try {
            oRem._readProperties(statement);
            return oRem.getTexte();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le texte de la remarque par rapport à un id.
     */
    public static java.lang.String getRemarque(String id, BTransaction trans) {
        if (!JadeStringUtil.isIntegerEmpty(id)) {
            FARemarque rem = new FARemarque();
            rem.setSession(trans.getSession());
            rem.setIdRemarque(id);
            try {
                rem.retrieve(trans);
                return rem.getTexte();
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    private java.lang.String idRemarque = new String();

    private java.lang.String texte = new String();

    /**
     * Commentaire relatif au constructeur FARemarque
     */
    public FARemarque() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdRemarque(this._incCounter(transaction, idRemarque));
        // setIdRemarque(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FARemarque.TABLE_FIELDS;
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + _getTableName() + " AS " + _getTableName();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "FAREMAP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRemarque = statement.dbReadNumeric("IDREMARQUE");
        texte = statement.dbReadString("TEXTE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDREMARQUE", this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDREMARQUE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField("TEXTE", this._dbWriteString(statement.getTransaction(), getTexte(), "texte"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdRemarque() {
        return idRemarque;
    }

    public java.lang.String getTexte() {
        return texte;
    }

    /**
     * Setter
     */
    public void setIdRemarque(java.lang.String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setTexte(java.lang.String newTexte) {
        texte = newTexte;
    }
}
