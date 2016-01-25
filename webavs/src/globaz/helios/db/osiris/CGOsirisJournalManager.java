package globaz.helios.db.osiris;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Iterator;

public class CGOsirisJournalManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateValeurFrom;
    private String dateValeurUntil;
    private ArrayList forEtatNotIn;

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CGOsirisJournal.TABLE_NAME;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlank(getDateValeurFrom())) {
            sqlWhere += CGOsirisJournal.FIELD_DATEVALEURCG + " > "
                    + _dbWriteDateAMJ(statement.getTransaction(), getDateValeurFrom());
        }

        if (!JadeStringUtil.isBlank(getDateValeurUntil())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGOsirisJournal.FIELD_DATEVALEURCG + " < "
                    + _dbWriteDateAMJ(statement.getTransaction(), getDateValeurUntil());
        }

        if (getForEtatNotIn() != null) {
            String tmp = new String();

            Iterator iter = getForEtatNotIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();

                if (tmp.length() != 0) {
                    tmp += " AND ";
                }

                tmp += CGOsirisJournal.FIELD_ETAT + " <> " + _dbWriteNumeric(statement.getTransaction(), element);
            }

            if (!JadeStringUtil.isBlank(tmp)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "(" + tmp + ")";
            }
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGOsirisJournal();
    }

    public String getDateValeurFrom() {
        return dateValeurFrom;
    }

    public String getDateValeurUntil() {
        return dateValeurUntil;
    }

    public ArrayList getForEtatNotIn() {
        return forEtatNotIn;
    }

    public void setDateValeurFrom(String dateValeurFrom) {
        this.dateValeurFrom = dateValeurFrom;
    }

    public void setDateValeurUntil(String dateValeurUntil) {
        this.dateValeurUntil = dateValeurUntil;
    }

    public void setForEtatNotIn(ArrayList forEtatNotIn) {
        this.forEtatNotIn = forEtatNotIn;
    }

}
