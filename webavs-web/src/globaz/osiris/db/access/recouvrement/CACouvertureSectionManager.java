package globaz.osiris.db.access.recouvrement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Représente un container de type CouvertureSection.
 * 
 * @author Arnaud Dostes, 31-mar-2005
 */
public class CACouvertureSectionManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCouvertureSection = "";
    private String forIdPlanRecouvrement = "";
    private String forIdSection = "";
    private String forNumeroOrdre = "";
    private String fromIdCouvertureSection = "";
    private String fromIdPlanRecouvrement = "";
    private String fromIdSection = "";
    private String fromNumeroOrdre = "";
    private String order = "";

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CAPLCSP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdCouvertureSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOUVERTURESECT="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCouvertureSection());
        }
        // traitement du positionnement
        if (getForIdPlanRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPLANRECOUVREMENT="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanRecouvrement());
        }
        // traitement du positionnement
        if (getForIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }
        // traitement du positionnement
        if (getForNumeroOrdre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NUMEROORDRE=" + this._dbWriteNumeric(statement.getTransaction(), getForNumeroOrdre());
        }
        // traitement du positionnement
        if (getFromIdCouvertureSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOUVERTURESECT>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdCouvertureSection());
        }
        // traitement du positionnement
        if (getFromIdPlanRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPLANRECOUVREMENT>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdPlanRecouvrement());
        }
        // traitement du positionnement
        if (getFromIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTION>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdSection());
        }
        // traitement du positionnement
        if (getFromNumeroOrdre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NUMEROORDRE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromNumeroOrdre());
        }
        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACouvertureSection();
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdCouvertureSection() {
        return forIdCouvertureSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdPlanRecouvrement() {
        return forIdPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForNumeroOrdre() {
        return forNumeroOrdre;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdCouvertureSection() {
        return fromIdCouvertureSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdPlanRecouvrement() {
        return fromIdPlanRecouvrement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdSection() {
        return fromIdSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromNumeroOrdre() {
        return fromNumeroOrdre;
    }

    /**
     * @return
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdCouvertureSection(String string) {
        forIdCouvertureSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdPlanRecouvrement(String string) {
        forIdPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdSection(String string) {
        forIdSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForNumeroOrdre(String string) {
        forNumeroOrdre = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdCouvertureSection(String string) {
        fromIdCouvertureSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdPlanRecouvrement(String string) {
        fromIdPlanRecouvrement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdSection(String string) {
        fromIdSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromNumeroOrdre(String string) {
        fromNumeroOrdre = string;
    }

    /**
     * @param string
     */
    public void setOrder(String string) {
        order = string;
    }
}
