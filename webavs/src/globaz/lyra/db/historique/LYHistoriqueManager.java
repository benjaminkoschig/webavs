/*
 * Crée le 31 octobre 2006
 */
package globaz.lyra.db.historique;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author hpe
 * 
 *         Manager de la classe LYHistorique
 */
public class LYHistoriqueManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static final long serialVersionUID = 231007857112891543L;

    private String forDate = "";
    private String forEtat = "";
    private String forIdEcheance = "";
    private String forIdHistorique = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LYHistoriqueManager.
     */
    public LYHistoriqueManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return LYHistorique.FIELDNAME_IDHISTORIQUE + " DESC";
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdHistorique)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LYHistorique.FIELDNAME_IDHISTORIQUE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdHistorique));

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdEcheance)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LYHistorique.FIELDNAME_IDECHEANCE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdEcheance));
        }

        if (!JadeStringUtil.isIntegerEmpty(forDate)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LYHistorique.FIELDNAME_DATEEXECUTION);
            sqlWhere.append(" >= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDate));
        }

        if (!JadeStringUtil.isIntegerEmpty(forEtat)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LYHistorique.FIELDNAME_ETAT);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forEtat));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LYEcheances)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LYHistorique();
    }

    /**
     * getter pour l'attribut forDate
     * 
     * @return la valeur courante de l'attribut forDate
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * getter pour l'attribut forEtat
     * 
     * @return la valeur courante de l'attribut forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * getter pour l'attribut forIdEcheance
     * 
     * @return la valeur courante de l'attribut forIdEcheance
     */
    public String getForIdEcheance() {
        return forIdEcheance;
    }

    /**
     * getter pour l'attribut forIdHistorique
     * 
     * @return la valeur courante de l'attribut forIdHistorique
     */
    public String getForIdHistorique() {
        return forIdHistorique;
    }

    /**
     * setter pour l'attribut forDate
     * 
     * @param forDate
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    /**
     * setter pour l'attribut forEtat
     * 
     * @param forEtat
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * setter pour l'attribut forIdEcheance
     * 
     * @param forIdEcheance
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdEcheance(String forIdEcheance) {
        this.forIdEcheance = forIdEcheance;
    }

    /**
     * setter pour l'attribut forIdHistorique
     * 
     * @param forIdHistorique
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdHistorique(String forIdHistorique) {
        this.forIdHistorique = forIdHistorique;
    }

}