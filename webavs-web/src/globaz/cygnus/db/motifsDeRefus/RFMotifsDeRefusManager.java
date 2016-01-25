/*
 * Créé le 26 janvier 2010
 */
package globaz.cygnus.db.motifsDeRefus;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Set;

/**
 * @author jje
 */
public class RFMotifsDeRefusManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private transient String forDescriptionDE = "";
    private transient String forDescriptionFR = "";
    private transient String forDescriptionIT = "";
    private transient String forIdMotifRefusSysteme = "";
    private transient Set<String> forIdsMotifRefus = null;
    private transient Set<String> forIdsMotifRefusSysteme = null;
    private transient boolean forIsMotifRefusSysteme = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFMotifsDeRefusManager.
     */
    public RFMotifsDeRefusManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(RFMotifsDeRefusManager.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (null != forIdsMotifRefus) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);
            sqlWhere.append(" IN (");
            int j = 0;
            for (String id : forIdsMotifRefus) {
                if (!JadeStringUtil.isEmpty(id)) {
                    j++;
                    if (forIdsMotifRefus.size() != j) {
                        sqlWhere.append(id + ",");
                    } else {
                        sqlWhere.append(id + ") ");
                    }
                }
            }
        }

        if (!JadeStringUtil.isEmpty(forDescriptionFR)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_FR + " LIKE "
                    + this._dbWriteString(statement.getTransaction(), forDescriptionFR + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDescriptionIT)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_IT + " LIKE "
                    + this._dbWriteString(statement.getTransaction(), forDescriptionIT + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDescriptionDE)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_DE + " LIKE "
                    + this._dbWriteString(statement.getTransaction(), forDescriptionDE + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdMotifRefusSysteme)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdMotifRefusSysteme));
        }

        if (null != forIdsMotifRefusSysteme) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME);
            sqlWhere.append(" IN (");
            int j = 0;
            for (String id : forIdsMotifRefusSysteme) {
                if (!JadeStringUtil.isEmpty(id)) {
                    j++;
                    if (forIdsMotifRefusSysteme.size() != j) {
                        sqlWhere.append("'" + id + "',");
                    } else {
                        sqlWhere.append("'" + id + "') ");
                    }
                }
            }
        }

        if (forIsMotifRefusSysteme) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFMotifsDeRefus.FIELDNAME_IS_MOTIF_SYSTEME);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), forIsMotifRefusSysteme,
                    BConstants.DB_TYPE_BOOLEAN_CHAR));

        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFMotifsDeRefus)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFMotifsDeRefus();
    }

    public String getForDescriptionDE() {
        return forDescriptionDE;
    }

    public String getForDescriptionFR() {
        return forDescriptionFR;
    }

    public String getForDescriptionIT() {
        return forDescriptionIT;
    }

    public String getForIdMotifRefusSysteme() {
        return forIdMotifRefusSysteme;
    }

    public Set<String> getForIdsMotifRefus() {
        return forIdsMotifRefus;
    }

    public Set<String> getForIdsMotifRefusSysteme() {
        return forIdsMotifRefusSysteme;
    }

    public String getFromClause() {
        return fromClause;
    }

    public boolean isForIsMotifRefusSysteme() {
        return forIsMotifRefusSysteme;
    }

    public void setForDescriptionDE(String forDescriptionDE) {
        this.forDescriptionDE = forDescriptionDE;
    }

    public void setForDescriptionFR(String forDescriptionFR) {
        this.forDescriptionFR = forDescriptionFR;
    }

    public void setForDescriptionIT(String forDescriptionIT) {
        this.forDescriptionIT = forDescriptionIT;
    }

    public void setForIdMotifRefusSysteme(String forIdMotifRefusSysteme) {
        this.forIdMotifRefusSysteme = forIdMotifRefusSysteme;
    }

    public void setForIdsMotifRefus(Set<String> forIdsMotifRefus) {
        this.forIdsMotifRefus = forIdsMotifRefus;
    }

    public void setForIdsMotifRefusSysteme(Set<String> forIdsMotifRefusSysteme) {
        this.forIdsMotifRefusSysteme = forIdsMotifRefusSysteme;
    }

    public void setForIsMotifRefusSysteme(boolean forIsMotifRefusSysteme) {
        this.forIsMotifRefusSysteme = forIsMotifRefusSysteme;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}