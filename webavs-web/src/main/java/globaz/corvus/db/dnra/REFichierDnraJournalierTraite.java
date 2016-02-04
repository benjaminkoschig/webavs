package globaz.corvus.db.dnra;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author bjo
 * 
 */
public class REFichierDnraJournalierTraite extends BEntity {

    private static final long serialVersionUID = 1738761316025565423L;
    private String idFichierDnraJournalierTraite = new String();
    private String nomFichierDnraJournalierTraite = new String();

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 la primary key
        if (JadeStringUtil.isBlank(getIdFichierDnraJournalierTraite())) {
            setIdFichierDnraJournalierTraite(_incCounter(transaction, "0"));
        }
    }

    @Override
    protected String _getTableName() {
        return "REDNRAJ";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idFichierDnraJournalierTraite = statement.dbReadNumeric("DNRAID");
        nomFichierDnraJournalierTraite = statement.dbReadString("DNRANOM");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".DNRAID",
                _dbWriteNumeric(statement.getTransaction(), getIdFichierDnraJournalierTraite(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(
                "DNRAID",
                _dbWriteNumeric(statement.getTransaction(), getIdFichierDnraJournalierTraite(),
                        "idFichierDnraJournalierTraite"));
        statement.writeField(
                "DNRANOM",
                _dbWriteString(statement.getTransaction(), getNomFichierDnraJournalierTraite(),
                        "nomFichierDnraJournalierTraite"));
    }

    public String getIdFichierDnraJournalierTraite() {
        return idFichierDnraJournalierTraite;
    }

    public void setIdFichierDnraJournalierTraite(String idFichierDnraJournalierTraite) {
        this.idFichierDnraJournalierTraite = idFichierDnraJournalierTraite;
    }

    public String getNomFichierDnraJournalierTraite() {
        return nomFichierDnraJournalierTraite;
    }

    public void setNomFichierDnraJournalierTraite(String nomFichierDnraJournalierTraite) {
        this.nomFichierDnraJournalierTraite = nomFichierDnraJournalierTraite;
    }

}
