/*
 * Créé le 2 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.planCaisse;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFPlanCaisseJCouvertureManager extends AFPlanCaisseManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAssuranceId;
    private String forDateDebutBeforeDate;
    private String forDateFinAfterDate;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return AFPlanCaisseJCouverture.createFromClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isIntegerEmpty(forAssuranceId)) {
            if (!JadeStringUtil.isEmpty(sqlWhere)) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), forAssuranceId);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateDebutBeforeDate)) {
            if (!JadeStringUtil.isEmpty(sqlWhere)) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MTDDEB<=" + this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutBeforeDate);
        }

        if (!JadeStringUtil.isIntegerEmpty(forDateFinAfterDate)) {
            if (!JadeStringUtil.isEmpty(sqlWhere)) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(MTDFIN=" + this._dbWriteDateAMJ(statement.getTransaction(), "") + " OR MTDFIN>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), forDateFinAfterDate) + ")";
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFPlanCaisseJCouverture();
    }

    /**
     * getter pour l'attribut for assurance id
     * 
     * @return la valeur courante de l'attribut for assurance id
     */
    public String getForAssuranceId() {
        return forAssuranceId;
    }

    /**
     * getter pour l'attribut for date debut before date
     * 
     * @return la valeur courante de l'attribut for date debut before date
     */
    public String getForDateDebutBeforeDate() {
        return forDateDebutBeforeDate;
    }

    /**
     * getter pour l'attribut for date fin after date
     * 
     * @return la valeur courante de l'attribut for date fin after date
     */
    public String getForDateFinAfterDate() {
        return forDateFinAfterDate;
    }

    /**
     * setter pour l'attribut for assurance id
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForAssuranceId(String string) {
        forAssuranceId = string;
    }

    /**
     * setter pour l'attribut for date debut before date
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateDebutBeforeDate(String date) {
        forDateDebutBeforeDate = date;
    }

    /**
     * setter pour l'attribut for date fin after date
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateFinAfterDate(String date) {
        forDateFinAfterDate = date;
    }
}
