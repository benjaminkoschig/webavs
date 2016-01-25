package globaz.osiris.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.ordres.CAOrdreVersement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author sch
 */
public class CAOperationOrdreVersementLettrageManager extends CAOperationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public Boolean forEstBloque = null;
    private List forEtatJournalNotIn = new ArrayList();

    private Boolean groupByDate = new Boolean(false);

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (getGroupByDate().booleanValue()) {
            return CAOperation.FIELD_DATE;
        }
        return super._getFields(statement);
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " LEFT OUTER JOIN " + _getCollection()
                + CAOrdreVersement.TABLE_CAOPOVP + " ON " + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                + CAOperation.FIELD_IDOPERATION + "=" + _getCollection() + CAOrdreVersement.TABLE_CAOPOVP + "."
                + CAOrdreVersement.FIELD_IDORDRE + " LEFT OUTER JOIN " + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " ON " + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                + CAOperation.FIELD_IDCOMPTEANNEXE + "=" + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " LEFT OUTER JOIN " + _getCollection()
                + CASection.TABLE_CASECTP + " ON " + _getCollection() + CASection.TABLE_CASECTP + "."
                + CASection.FIELD_IDSECTION + "=" + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                + CAOperation.FIELD_IDSECTION + " INNER JOIN " + _getCollection() + CAJournal.TABLE_CAJOURP + " ON "
                + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDJOURNAL + "="
                + _getCollection() + CAJournal.TABLE_CAJOURP + "." + CAJournal.FIELD_IDJOURNAL;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        String order = "";
        if (!getGroupByDate().booleanValue()) {
            order = super._getOrder(statement);
            if (JadeStringUtil.isBlank(order)) {
                order = CAOrdreVersement.FIELD_NOMCACHE;
            }
        }
        return order;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperationManager#_getWhere(globaz.globall. db.BStatement)
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // Récupérer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }

        sqlWhere += _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION + " = "
                + this._dbWriteString(statement.getTransaction(), APIOperation.CAOPERATIONORDREVERSEMENT);
        // traitement du positionnement selon le numero du journal
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDJOURNAL + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        // traitement du positionnement selon le montant absolu
        if (getForMontantABS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ABS(" + CAOperation.FIELD_MONTANT + ")=" + "ABS("
                    + this._dbWriteNumeric(statement.getTransaction(), getForMontantABS()) + ")";
        }

        // traitement du positionnement selon que l'ordre de versement soit
        // bloqué ou non
        if (getForEstBloque() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection()
                    + CAOrdreVersement.TABLE_CAOPOVP
                    + "."
                    + CAOrdreVersement.FIELD_ESTBLOQUE
                    + "="
                    + this._dbWriteBoolean(statement.getTransaction(), getForEstBloque(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDROLE
                        + " IN (";

                for (int idRole = 0; idRole < roles.length; ++idRole) {
                    if (idRole > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[idRole]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDROLE + "="
                        + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
            }
        }

        if (!JadeStringUtil.isBlank(getForIdExterneRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                    + "=" + this._dbWriteString(statement.getTransaction(), getForIdExterneRole());
        }
        if ((getForEtatJournalNotIn() != null) && (getForEtatJournalNotIn().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CAJournal.TABLE_CAJOURP + "." + CAJournal.FIELD_ETAT + " NOT IN (";
            Iterator iter = getForEtatJournalNotIn().iterator();
            StringBuffer bSql = new StringBuffer();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                bSql.append(element + ",");
            }
            bSql.replace(bSql.length() - 1, bSql.length(), ")");
            sqlWhere += bSql.toString();
        }

        sqlWhere += getGroupBy();
        return sqlWhere;
    }

    /**
     * Returns the forEstBloque.
     * 
     * @return Boolean
     */
    public Boolean getForEstBloque() {
        return forEstBloque;
    }

    /**
     * @return the forEtatJournalNotIn
     */
    public List getForEtatJournalNotIn() {
        return forEtatJournalNotIn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     */
    protected String getGroupBy() {
        String groupBy = "";
        if (getGroupByDate().booleanValue()) {
            groupBy = " GROUP BY (" + CAOperation.FIELD_DATE + ")";
        }
        return groupBy;
    }

    /**
     * @return the groupByDate
     */
    public Boolean getGroupByDate() {
        return groupByDate;
    }

    /**
     * @param forEtatJournalNotIn
     *            the forEtatJournalNotIn to set
     */
    public void setForEtatJournalNotIn(List forEtatJournalNotIn) {
        this.forEtatJournalNotIn = forEtatJournalNotIn;
    }

    /**
     * @param groupByDate
     *            the groupByDate to set
     */
    public void setGroupByDate(Boolean groupByDate) {
        this.groupByDate = groupByDate;
    }

    /**
     * Sélection (for) des opérations selon l'état bloqué Date de création : (23.01.2003 08:26:37)
     * 
     * @param newForEstBloque
     *            boolean
     */
    public void wantForEstBloque(boolean newForEstBloque) {
        forEstBloque = new Boolean(newForEstBloque);
    }

    /**
     * Sélection (for) des opérations selon l'état bloqué Date de création : (23.01.2003 09:33:47)
     * 
     * @param newForEstBloque
     *            Boolean
     */
    void wantForEstBloque(Boolean newForEstBloque) {
        forEstBloque = newForEstBloque;
    }

}
