package globaz.naos.db.acompte;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliationManager;
import java.io.Serializable;

public class AFAcompteManager extends AFAffiliationManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String ANNUEL30JUIN = "ann";
    public static int ORDER_AFFILIDATIONID = 2;
    //
    public static int ORDER_AFFILIENUMERO = 1;
    //
    // private Boolean forAssurance = new Boolean(false);
    //
    public final static java.lang.String PARITAIRE = "par";

    public final static java.lang.String PERSONNEL = "per";

    private java.lang.String forIdPlanAffiliation = new String();
    private java.lang.String order = new String();

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        return _getCollection() + "AFAFFIP AS AFAFFIP INNER JOIN " + _getCollection()
                + "AFPLAFP AS AFPLAFP ON (AFPLAFP.MAIAFF=AFAFFIP.MAIAFF AND AFPLAFP.MUBINA="
                + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + ")" + " INNER JOIN " + _getCollection() + "AFCOTIP AS AFCOTIP ON AFCOTIP.MUIPLA=AFPLAFP.MUIPLA"
                + " INNER JOIN " + _getCollection() + "AFASSUP AS AFASSUP ON AFASSUP.MBIASS=AFCOTIP.MBIASS ";

    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        if (!JadeStringUtil.isEmpty(getForIdPlanAffiliation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "AFPLAFP.MUIPLA=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanAffiliation());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAcompteViewBean();
    }

    // ***********************************************
    // Getter
    // ***********************************************

    /**
     * @return
     */
    public java.lang.String getForIdPlanAffiliation() {
        return forIdPlanAffiliation;
    }

    // ***********************************************
    // Setter
    // ***********************************************

    @Override
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * @param string
     */
    public void setForIdPlanAffiliation(java.lang.String string) {
        forIdPlanAffiliation = string;
    }

    @Override
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    @Override
    public void setOrderBy(int orderBy) {
        if (orderBy == AFAcompteManager.ORDER_AFFILIENUMERO) {
            order = "MALNAF";
        } else if (orderBy == AFAcompteManager.ORDER_AFFILIDATIONID) {
            order = "MAIAFF";
        }
    }

}
