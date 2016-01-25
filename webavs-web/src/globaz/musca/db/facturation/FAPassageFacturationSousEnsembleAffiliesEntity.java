package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Entité liée à la gestion d'un sous ensemble d'affiliés pour un passage.
 * 
 * @author JSI
 * 
 */
public class FAPassageFacturationSousEnsembleAffiliesEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPassage;
    private String numeroAffilie;

    @Override
    protected String _getFields(BStatement statement) {
        String fields = _getCollection() + "FAFIAFP.IDPASSAGE," + _getCollection() + "AFAFFIP.MALNAF";
        return fields;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String from = _getCollection() + "FAFIAFP INNER JOIN " + _getCollection() + "AFAFFIP ON " + _getCollection()
                + "FAFIAFP.MAIAFF=" + _getCollection() + "AFAFFIP.MAIAFF";
        return from;
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdPassage(statement.dbReadNumeric("IDPASSAGE"));
        setNumeroAffilie(statement.dbReadString("MALNAF"));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

}
