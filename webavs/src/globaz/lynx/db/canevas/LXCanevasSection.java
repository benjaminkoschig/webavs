package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class LXCanevasSection extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_IDEXTERNE = "IDEXTERNE";

    public static final String FIELD_IDFOURNISSEUR = "IDFOURNISSEUR";
    public static final String FIELD_IDSECTIONCANEVAS = "IDSECTIONCANEVAS";
    public static final String FIELD_IDSOCIETE = "IDSOCIETE";
    public static final String TABLE_LXCANSP = "LXCANSP";

    private String idExterne = "";
    private String idFournisseur = "";
    private String idSectionCanevas = "";
    private String idSociete = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSectionCanevas(this._incCounter(transaction, idSectionCanevas));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return LXCanevasSection.TABLE_LXCANSP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSectionCanevas(statement.dbReadNumeric(LXCanevasSection.FIELD_IDSECTIONCANEVAS));
        setIdSociete(statement.dbReadNumeric(LXCanevasSection.FIELD_IDSOCIETE));
        setIdFournisseur(statement.dbReadNumeric(LXCanevasSection.FIELD_IDFOURNISSEUR));
        setIdExterne(statement.dbReadString(LXCanevasSection.FIELD_IDEXTERNE));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(LXCanevasSection.FIELD_IDSECTIONCANEVAS,
                this._dbWriteNumeric(statement.getTransaction(), getIdSectionCanevas(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(LXCanevasSection.FIELD_IDSECTIONCANEVAS,
                this._dbWriteNumeric(statement.getTransaction(), getIdSectionCanevas(), "idSectionCanevas"));
        statement.writeField(LXCanevasSection.FIELD_IDSOCIETE,
                this._dbWriteNumeric(statement.getTransaction(), getIdSociete(), "idSociete"));
        statement.writeField(LXCanevasSection.FIELD_IDFOURNISSEUR,
                this._dbWriteNumeric(statement.getTransaction(), getIdFournisseur(), "idFournisseur"));
        statement.writeField(LXCanevasSection.FIELD_IDEXTERNE,
                this._dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdSectionCanevas() {
        return idSectionCanevas;
    }

    public String getIdSociete() {
        return idSociete;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdSectionCanevas(String idSectionCanevas) {
        this.idSectionCanevas = idSectionCanevas;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

}
