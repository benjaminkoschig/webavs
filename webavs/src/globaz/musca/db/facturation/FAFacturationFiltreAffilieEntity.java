package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class FAFacturationFiltreAffilieEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idAffiliation;
    private String idFiltreAffilie;
    private String idPassage;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        setIdFiltreAffilie(this._incCounter(transaction, getIdFiltreAffilie()));
    }

    @Override
    protected String _getTableName() {
        return "FAFIAFP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idFiltreAffilie = statement.dbReadNumeric("IDUNIQ");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idFiltreAffilie = statement.dbReadNumeric("IDPASSAGE");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDUNIQ", this._dbWriteNumeric(statement.getTransaction(), getIdFiltreAffilie(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IDUNIQ", this._dbWriteNumeric(statement.getTransaction(), getIdFiltreAffilie(), ""));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), "idAffiliation"));
        statement
                .writeField("IDPASSAGE", this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), "idPassage"));
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdFiltreAffilie() {
        return idFiltreAffilie;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdFiltreAffilie(String idFiltreAffilie) {
        this.idFiltreAffilie = idFiltreAffilie;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

}
