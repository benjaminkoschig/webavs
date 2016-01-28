package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPSortieMontantManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String exceptIdMontantSortie = "";
    private String field = "";
    private String forAssurance = "";

    /**
     * Fichier CPMOSOP
     */
    /** (IZISOR) */
    private String forIdSortie = "";
    private String order = "";

    /**
	 * 
	 */
    public CPSortieMontantManager() {
        super();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPMOSOP";
    }

    // protected String _getFields(BStatement statement) {
    // if (JadeStringUtil.isEmpty(field)) {
    // return "IKIMSO , IZISOR , IKMONT , ISTGCO";
    //
    // } else {
    // return field;
    // }
    // }
    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */

    @Override
    protected String _getOrder(BStatement statement) {
        return getOrder();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */

    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdSortie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IZISOR=" + _dbWriteNumeric(statement.getTransaction(), getForIdSortie());
        }
        if (getExceptIdMontantSortie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IKIMSO <>" + _dbWriteNumeric(statement.getTransaction(), getExceptIdMontantSortie());
        }
        if (getForAssurance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MEICOT=" + _dbWriteNumeric(statement.getTransaction(), getForAssurance());
        }
        return sqlWhere;
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
        return new CPSortieMontant();
    }

    /**
     * @return
     */
    public String getExceptIdMontantSortie() {
        return exceptIdMontantSortie;
    }

    /**
     * @return
     */
    public String getField() {
        return field;
    }

    /**
     * @return
     */
    public String getForAssurance() {
        return forAssurance;
    }

    /**
     * @return
     */
    public String getForIdSortie() {
        return forIdSortie;
    }

    /**
     * Returns the order.
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * Tri par No Affiliés Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdSortieMontant() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("IKIMSO ASC");
        } else {
            setOrder(getOrder() + ", IKIMSO ASC");
        }
    }

    /**
     * @param string
     */
    public void setExceptIdMontantSortie(String string) {
        exceptIdMontantSortie = string;
    }

    /**
     * @param string
     */
    public void setField(String string) {
        field = string;
    }

    /**
     * @param string
     */
    public void setForAssurance(String string) {
        forAssurance = string;
    }

    /**
     * @param string
     */
    public void setForIdSortie(String string) {
        forIdSortie = string;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(java.lang.String order) {
        this.order = order;
    }

}