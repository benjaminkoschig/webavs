package globaz.corvus.db.recap.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

/**
 * Manager sur fichier reinfrec
 * 
 * @author fgo
 * @version 1.0 Created on Fri Nov 30 11:51:36 CET 2007
 */
public class RERecapInfoManager extends BManager {
    /** Table : REINFREC */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Pour codeRecap - code récap est = à ... (ZQNCOD) */
    private String forCodeRecap = new String();

    private String forCodeRecapIn = new String();

    /** Pour datePmt - date paiement MMxAAAA est = à ... (ZQDDAT) */
    private String forDatePmt = new String();

    /** Pour idRecapInfo - id récap info (pk) est = à ... (ZQIIFR) */
    private String forIdRecapInfo = new String();

    /** Pour idTiers - id tiers (fk) est = à ... (ZQITIE) */
    private String forIdTiers = new String();

    /** Définition de l'instruction ORDER BY */
    private String forOrderBy = new String();

    private String forRestoreTag = new String();

    /** Pour montant - montant est >= à ... (ZQMMON) */
    private String fromMontant = new String();

    /** Définition si total par code */
    private boolean totalByCode = false;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (getTotalByCode()) {
            StringBuffer fields = new StringBuffer();
            // if (!JadeNumericUtil.isEmptyOrZero(getForCodeRecap())) {
            fields.append(IRERecapInfoDefTable.CODE_RECAP).append(",");
            // }
            fields.append(" SUM(").append(IRERecapInfoDefTable.MONTANT).append(") AS ")
                    .append(IRERecapInfoDefTable.TOTAL_MONTANT);

            fields.append(",").append(" COUNT(1) AS ").append(IRERecapInfoDefTable.CAS);

            return fields.toString();
        } else {
            return super._getFields(statement);
        }
    }

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "REINFREC" (Model : RERecapInfo)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(IRERecapInfoDefTable.TABLE_NAME).toString();
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param statement
     *            de type BStatement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (getTotalByCode()) {
            if (JadeNumericUtil.isEmptyOrZero(getForCodeRecap())) {
                return "";
            } else {
                return (IRERecapInfoDefTable.CODE_RECAP);
            }
        } else if (JadeStringUtil.isEmpty(getForOrderBy())) {
            return super._getOrder(statement);
        } else {
            return getForOrderBy();
        }
    }

    /**
     * Retourne la clause WHERE de la requete SQL
     * 
     * @param statement
     *            BStatement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        /* composant de la requete initialises avec les options par defaut */
        StringBuffer sqlWhere = new StringBuffer();
        // traitement du positionnement
        if (getForIdRecapInfo().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapInfoDefTable.ID_RECAP_INFO).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdRecapInfo()));
        }
        // traitement du positionnement
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapInfoDefTable.ID_TIERS).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiers()));
        }

        // traitement du positionnement
        if (getForRestoreTag().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapInfoDefTable.RESTORE_TAG).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForRestoreTag()));
        }

        // traitement du positionnement
        if (getForCodeRecap().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapInfoDefTable.CODE_RECAP).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCodeRecap()));
        }
        // traitement du positionnement
        if (getForCodeRecapIn().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapInfoDefTable.CODE_RECAP).append(" IN (" + getForCodeRecapIn() + ")");
        }
        // traitement du positionnement
        if (getForDatePmt().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapInfoDefTable.DATE_PMT)
                    .append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(),
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePmt())));
        }

        // traitement du positionnement
        if (getFromMontant().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapInfoDefTable.MONTANT).append(">=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getFromMontant()));
        }
        groupBy(sqlWhere);
        return sqlWhere.toString();
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERecapInfo();
    }

    /**
     * Récupération de la requête SQL
     * 
     * @return
     */
    public String getCurrentSqlQuery() {
        return _getCurrentSqlQuery();
    }

    /**
     * Renvoie forCodeRecap;
     * 
     * @return String codeRecap - code récap;
     */
    public String getForCodeRecap() {
        return forCodeRecap;
    }

    public String getForCodeRecapIn() {
        return forCodeRecapIn;
    }

    /**
     * Renvoie forDatePmt;
     * 
     * @return String datePmt - date paiement MMxAAAA;
     */
    public String getForDatePmt() {
        return forDatePmt;
    }

    /**
     * Renvoie forIdRecapInfo;
     * 
     * @return String idRecapInfo - id récap info (pk);
     */
    public String getForIdRecapInfo() {
        return forIdRecapInfo;
    }

    /**
     * Renvoie forIdTiers;
     * 
     * @return String idTiers - id tiers (fk);
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * Récupération de l'instruction ORDER BY
     * 
     * @return the forOrderBy
     */
    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getForRestoreTag() {
        return forRestoreTag;
    }

    /**
     * Sélection par fromMontant;
     * 
     * @return String montant - montant;
     */
    public String getFromMontant() {
        return fromMontant;
    }

    /**
     * Récupération si total par code
     * 
     * @return the forOrderBy
     */
    public boolean getTotalByCode() {
        return totalByCode;
    }

    /**
     * Définition du group by
     * 
     * @param sqlWhere
     */
    private void groupBy(StringBuffer sqlWhere) {
        if (getTotalByCode()) {
            sqlWhere.append(" GROUP BY ").append(IRERecapInfoDefTable.CODE_RECAP);
        }
    }

    /**
     * Sélection par forCodeRecap
     * 
     * @param newForCodeRecap
     *            String - code récap
     */
    public void setForCodeRecap(String newForCodeRecap) {
        forCodeRecap = newForCodeRecap;
    }

    public void setForCodeRecapIn(String forCodeRecapIn) {
        this.forCodeRecapIn = forCodeRecapIn;
    }

    /**
     * Sélection par forDatePmt
     * 
     * @param newForDatePmt
     *            String - date paiement MMxAAAA
     */
    public void setForDatePmt(String newForDatePmt) {
        forDatePmt = newForDatePmt;
    }

    /**
     * Sélection par forIdRecapInfo
     * 
     * @param newForIdRecapInfo
     *            String - id récap info (pk)
     */
    public void setForIdRecapInfo(String newForIdRecapInfo) {
        forIdRecapInfo = newForIdRecapInfo;
    }

    /**
     * Sélection par forIdTiers
     * 
     * @param newForIdTiers
     *            String - id tiers (fk)
     */
    public void setForIdTiers(String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    /**
     * Définition de l'instruction ORDER By
     * 
     * @param newForOrderBy
     *            the newForOrderBy to set
     */
    public void setForOrderBy(String newForOrderBy) {
        forOrderBy = newForOrderBy;
    }

    public void setForRestoreTag(String forRestoreTag) {
        this.forRestoreTag = forRestoreTag;
    }

    /**
     * Définition affichage normal ou total par code
     * 
     * @param newForOrderBy
     *            the newForOrderBy to set
     */
    public void setForTotalByCode(boolean newTotalByCode) {
        totalByCode = newTotalByCode;
    }

    /**
     * Sélection par fromMontant
     * 
     * @param newFromMontant
     *            String - montant
     */
    public void setFromMontant(String newFromMontant) {
        fromMontant = newFromMontant;
    }
}
