package globaz.osiris.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ordres.CAOrdreVersement;

/**
 * @author jts 10 ao�t 05 10:17:05
 * @revision SCO 18 mars 2010
 */
public class CAOperationOrdreManager extends CAOperationManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ORDER_IDORDREGROUPE_NOMCACHE = "1002";
    public static final String ORDER_IDORDREGROUPE_NUMTRANSACTION = "1003";
    public static final String ORDRE_IDORDREGROUPE_IDOPERATION = "1004";

    private String afterNumTransaction = "";
    public Boolean forEstBloque = null;
    private String forIdJournal = "";
    private String forIdNatureOrdre = "";
    private String forIdOrdreGroupe = "";
    private String forMontant = "";
    private String forMontantABS = "";
    private String forNomCacheLike;
    private String forNumTransaction = "";
    private String fromNomCache = "";
    private String fromNumTransaction = "";
    private String inIdOperation = "";

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
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE;
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

        // R�cup�rer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement selon le num�ro d'ordre group�
        if (getForIdOrdreGroupe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreVersement.FIELD_IDORDREGROUPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOrdreGroupe());
        }
        // list d'idOrdre
        if (!JadeStringUtil.isBlank(getInIdOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CAOrdreVersement.TABLE_CAOPOVP + "." + CAOrdreVersement.FIELD_IDORDRE
                    + " IN (" + getInIdOperation() + " ) ";
        }

        // traitement du positionnement � partir du nom de cache
        if (getFromNomCache().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreVersement.FIELD_NOMCACHE + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromNomCache());
        }

        if (!JadeStringUtil.isBlank(getForNomCacheLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreVersement.FIELD_NOMCACHE + " like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getForNomCacheLike() + "%");
        }

        // traitement du positionnement selon la nature de l'ordre
        if (getForIdNatureOrdre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreVersement.FIELD_NATUREORDRE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdNatureOrdre());
        }

        // traitement du positionnement � partir du num�ro de transaction
        if (getFromNumTransaction().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreVersement.FIELD_NUMTRANSACTION + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromNumTransaction());
        }

        // traitement du positionnement pour le num�ro de transaction
        if (getForNumTransaction().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreVersement.FIELD_NUMTRANSACTION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForNumTransaction());
        }

        // traitement du positionnement � partir du num�ro de transaction
        if (getAfterNumTransaction().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreVersement.FIELD_NUMTRANSACTION + ">"
                    + this._dbWriteNumeric(statement.getTransaction(), getAfterNumTransaction());
        }

        // traitement du positionnement selon le montant
        if (getForMontant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOperation.FIELD_MONTANT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForMontant());
        }

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
        // bloqu� ou non
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

        sqlWhere += getGroupBy();
        return sqlWhere;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 13:24:20)
     * 
     * @return String
     */
    public String getAfterNumTransaction() {
        return afterNumTransaction;
    }

    /**
     * Returns the forEstBloque.
     * 
     * @return Boolean
     */
    public Boolean getForEstBloque() {
        return forEstBloque;
    }

    @Override
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.02.2002 13:01:43)
     * 
     * @return String
     */
    public String getForIdNatureOrdre() {
        return forIdNatureOrdre;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 12:49:50)
     * 
     * @return String
     */
    public String getForIdOrdreGroupe() {
        return forIdOrdreGroupe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.03.2002 09:39:17)
     * 
     * @return String
     */
    public String getForMontant() {
        return forMontant;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.05.2002 13:46:22)
     * 
     * @return String
     */
    @Override
    public String getForMontantABS() {
        return forMontantABS;
    }

    public String getForNomCacheLike() {
        return forNomCacheLike;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.03.2002 15:55:56)
     * 
     * @return String
     */
    public String getForNumTransaction() {
        return forNumTransaction;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.05.2002 10:52:39)
     * 
     * @return String
     */
    public String getFromNomCache() {
        return fromNomCache;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 13:09:08)
     * 
     * @return String
     */
    public String getFromNumTransaction() {
        return fromNumTransaction;
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 13:24:20)
     * 
     * @param newAfterNumTransaction
     *            String
     */
    public void setAfterNumTransaction(String newAfterNumTransaction) {
        afterNumTransaction = newAfterNumTransaction;
    }

    @Override
    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.02.2002 13:01:43)
     * 
     * @param newForIdNatureOrdre
     *            String
     */
    public void setForIdNatureOrdre(String newForIdNatureOrdre) {
        forIdNatureOrdre = newForIdNatureOrdre;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 12:49:50)
     * 
     * @param newForIdOrdreGroupe
     *            String
     */
    public void setForIdOrdreGroupe(String newForIdOrdreGroupe) {
        forIdOrdreGroupe = newForIdOrdreGroupe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.03.2002 09:39:17)
     * 
     * @param newForMontant
     *            String
     */
    public void setForMontant(String newForMontant) {
        forMontant = newForMontant;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.05.2002 13:46:22)
     * 
     * @param newForMontantABS
     *            String
     */
    @Override
    public void setForMontantABS(String newForMontantABS) {
        forMontantABS = newForMontantABS;
    }

    public void setForNomCacheLike(String forNomCacheLike) {
        this.forNomCacheLike = forNomCacheLike;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.03.2002 15:55:56)
     * 
     * @param newForNumTransaction
     *            String
     */
    public void setForNumTransaction(String newForNumTransaction) {
        forNumTransaction = newForNumTransaction;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.05.2002 10:52:39)
     * 
     * @param newFromNomCache
     *            String
     */
    public void setFromNomCache(String newFromNomCache) {
        fromNomCache = newFromNomCache;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 13:09:08)
     * 
     * @param newFromNumTransaction
     *            String
     */
    public void setFromNumTransaction(String newFromNumTransaction) {
        fromNumTransaction = newFromNumTransaction;
    }

    /**
     * @param groupByDate
     *            the groupByDate to set
     */
    public void setGroupByDate(Boolean groupByDate) {
        this.groupByDate = groupByDate;
    }

    /**
     * S�lection (for) des op�rations selon l'�tat bloqu� Date de cr�ation : (23.01.2003 08:26:37)
     * 
     * @param newForEstBloque
     *            boolean
     */
    public void wantForEstBloque(boolean newForEstBloque) {
        forEstBloque = new Boolean(newForEstBloque);
    }

    /**
     * S�lection (for) des op�rations selon l'�tat bloqu� Date de cr�ation : (23.01.2003 09:33:47)
     * 
     * @param newForEstBloque
     *            Boolean
     */
    void wantForEstBloque(Boolean newForEstBloque) {
        forEstBloque = newForEstBloque;
    }

    public String getInIdOperation() {
        return inIdOperation;
    }

    public void setInIdOperation(String inIdOperation) {
        this.inIdOperation = inIdOperation;
    }

}
