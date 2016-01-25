package globaz.osiris.db.lettrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/*
 * Permet d'exclure une section du processus de lettrage de masse.
 */
public class CAExclusionSection extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAnnexe = "";
    private String idExculsion = "";
    private String idSection = "";

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdExculsion(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return "CAEXLEP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idExculsion = statement.dbReadNumeric("IDEXCL");
        idSection = statement.dbReadNumeric("IDSECTION");
        idCompteAnnexe = statement.dbReadNumeric("IDCOMPTEANNEXE");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
            _addError(statement.getTransaction(), "L'identifiant de section est obligatoire"); // TODO
            // :
            // translate
        }
        if (JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe())) {
            _addError(statement.getTransaction(), "L'identifiant du compte annexe est obligatoire"); // TODO
            // :
            // translate
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDEXCL", this._dbWriteNumeric(statement.getTransaction(), getIdExculsion(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IDEXCL",
                this._dbWriteNumeric(statement.getTransaction(), getIdExculsion(), "idExculsion"));
        statement.writeField("IDCOMPTEANNEXE",
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), "idCompteAnnexe"));
        statement
                .writeField("IDSECTION", this._dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /*
     * Getter and Setter
     */
    public String getIdExculsion() {
        return idExculsion;
    }

    public String getIdSection() {
        return idSection;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExculsion(String idExculsion) {
        this.idExculsion = idExculsion;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }
}
