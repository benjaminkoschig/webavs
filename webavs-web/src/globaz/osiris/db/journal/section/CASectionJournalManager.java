package globaz.osiris.db.journal.section;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * 
 * Return la liste des sections touchées par un journal.<br/>
 * Spécialité : _getCount surchargé pour que le getCount fonctionne.
 * 
 * SQL :
 * 
 * select a.idsection, a.idjournal, b.categoriesection from webavs.CAOPERP a, webavs.CASECTP b where a.idJournal = 584
 * and a.idsection = b.idsection and b.idjournal <> 584 group by a.idsection, a.idjournal, b.categoriesection
 * 
 * @author DDA
 * 
 */
public class CASectionJournalManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdJournal;

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "a." + CAOperation.FIELD_IDSECTION + ", a." + CAOperation.FIELD_IDJOURNAL + ", b."
                + CASection.FIELD_CATEGORIESECTION;
    }

    /**
     * see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " a, " + _getCollection() + CASection.TABLE_CASECTP
                + " b ";
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "a." + CAOperation.FIELD_IDSECTION + ", a." + CAOperation.FIELD_IDJOURNAL;
    }

    /**
     * see globaz.globall.db.BManager#_getSqlCount(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSqlCount(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer("SELECT COUNT(DISTINCT a." + CAOperation.FIELD_IDSECTION
                    + ") FROM ");
            sqlBuffer.append(_getFrom(statement));
            sqlBuffer.append(" WHERE ");
            sqlBuffer.append(getWhere());

            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSqlCount() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();
        where.append(getWhere());
        where.append(getGroupBy());

        return where.toString();
    }

    /**
     * see globaz.globall.db.BManager#_getSqlCount(globaz.globall.db.BStatement)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASectionJournal();
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    private String getGroupBy() {
        return " group by a." + CAOperation.FIELD_IDSECTION + ", a." + CAOperation.FIELD_IDJOURNAL + ", b."
                + CASection.FIELD_CATEGORIESECTION;
    }

    private String getWhere() {
        String where = "a." + CAOperation.FIELD_IDJOURNAL + "=" + getForIdJournal() + " and a."
                + CAOperation.FIELD_IDSECTION + " = b." + CASection.FIELD_IDSECTION + " and (";
        where += "b." + CASection.FIELD_IDJOURNAL + "<>" + getForIdJournal();
        where += " or (b." + CASection.FIELD_IDJOURNAL + " = " + getForIdJournal() + " and b." + CASection.FIELD_SOLDE
                + " <= 0)";
        where += ")";

        // Pour ne pas avoir deux calculs d'IM pour les cas de pmt de l'OP soldant la section.
        // and o.provenancepmt <> 249002 and o.provenancepmt <> 249004
        where += " and a." + CAOperation.FIELD_PROVENANCEPMT + " <> " + APIOperation.PROVPMT_SOLDEOF;
        where += " and a." + CAOperation.FIELD_PROVENANCEPMT + " <> " + APIOperation.PROVPMT_SOLDEOP;

        return where;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

}
