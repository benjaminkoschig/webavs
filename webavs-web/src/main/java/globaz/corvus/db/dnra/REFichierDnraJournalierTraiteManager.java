package globaz.corvus.db.dnra;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.PRAbstractManager;

public class REFichierDnraJournalierTraiteManager extends PRAbstractManager {

    private static final long serialVersionUID = 5660404192248717774L;
    private String forIdFichierDnraJournalierTraite = new String();
    private String forNomFichierDnraJournalierTraite = new String();

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (getForIdFichierDnraJournalierTraite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DNRAID=" + _dbWriteNumeric(statement.getTransaction(), getForIdFichierDnraJournalierTraite());
        }
        if (getForNomFichierDnraJournalierTraite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DNRANOM=" + _dbWriteString(statement.getTransaction(), getForNomFichierDnraJournalierTraite());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REFichierDnraJournalierTraite();
    }

    public String getForIdFichierDnraJournalierTraite() {
        return forIdFichierDnraJournalierTraite;
    }

    public void setForIdFichierDnraJournalierTraite(String forIdFichierDnraJournalierTraite) {
        this.forIdFichierDnraJournalierTraite = forIdFichierDnraJournalierTraite;
    }

    public String getForNomFichierDnraJournalierTraite() {
        return forNomFichierDnraJournalierTraite;
    }

    public void setForNomFichierDnraJournalierTraite(String forNomFichierDnraJournalierTraite) {
        this.forNomFichierDnraJournalierTraite = forNomFichierDnraJournalierTraite;
    }

    @Override
    public String getOrderByDefaut() {
        return "DNRAID";
    }
}
