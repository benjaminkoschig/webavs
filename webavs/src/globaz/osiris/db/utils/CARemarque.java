package globaz.osiris.db.utils;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIRemarque;

public class CARemarque extends BEntity implements APIRemarque {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String FIELD_IDREMARQUE = "IDREMARQUE";

    private static final String FIELD_TEXTE = "TEXTE";
    public static final String TABLE_CAREMAP = "CAREMAP";

    private static final int TEXTE_MAX_LENGTH = 1000;

    private String idRemarque = new String();
    private String texte = new String();

    /**
     * Commentaire relatif au constructeur CARemarque
     */
    public CARemarque() {
        super();
    }

    /**
     * @see BEntity#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdRemarque(this._incCounter(transaction, idRemarque));
    }

    /**
     * @see BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CARemarque.TABLE_CAREMAP;
    }

    /**
     * @see BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRemarque = statement.dbReadNumeric(CARemarque.FIELD_IDREMARQUE);
        texte = statement.dbReadString(CARemarque.FIELD_TEXTE);
    }

    /**
     * @see BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        if (!JadeStringUtil.isBlank(getTexte()) && (getTexte().length() > CARemarque.TEXTE_MAX_LENGTH)) {
            _addError(statement.getTransaction(), getSession().getLabel("REMARQUE_TROP_LONGUE"));
        }
    }

    /**
     * @see BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CARemarque.FIELD_IDREMARQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), ""));
    }

    /**
     * @see BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CARemarque.FIELD_IDREMARQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField(CARemarque.FIELD_TEXTE,
                this._dbWriteString(statement.getTransaction(), getTexte(), "texte"));
    }

    /**
     * Getter
     */
    @Override
    public String getIdRemarque() {
        return idRemarque;
    }

    @Override
    public String getTexte() {
        return texte;
    }

    /**
     * Setter
     */
    @Override
    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    @Override
    public void setTexte(String newTexte) {
        texte = newTexte;
    }
}
