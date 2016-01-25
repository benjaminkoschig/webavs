package globaz.helios.db.modeles;

import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CGModeleEcritureManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdMandat = "";
    private String forIdParametreBouclement = "";
    private String forLibelle = "";
    private String fromNumero = "";

    private String orderBy = "";

    /**
     * Commentaire relatif au constructeur CGModeleEcritureManager.
     */
    public CGModeleEcritureManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CGModeleEcriture.TABLE_NAME;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return orderBy;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdMandat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGModeleEcriture.FIELD_IDMANDAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdParametreBouclement())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGModeleEcriture.FIELD_IDPARAMETREBOUCL + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdParametreBouclement());
        }

        if (!JadeStringUtil.isBlank(getForLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if ("FR".equalsIgnoreCase(getSession().getIdLangueISO())) {
                sqlWhere += CGModeleEcriture.FIELD_LIBELLEFR + " like '%" + getForLibelle() + "%'";
            } else if ("DE".equalsIgnoreCase(getSession().getIdLangueISO())) {
                sqlWhere += CGModeleEcriture.FIELD_LIBELLEDE + " like '%" + getForLibelle() + "%'";
            } else if ("IT".equalsIgnoreCase(getSession().getIdLangueISO())) {
                sqlWhere += CGModeleEcriture.FIELD_LIBELLEIT + " like '%" + getForLibelle() + "%'";
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(getFromNumero())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGModeleEcriture.FIELD_IDMODELEECRITURE + ">="
                    + _dbWriteNumeric(statement.getTransaction(), getFromNumero());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGModeleEcriture();
    }

    /**
     * Returns the forIdMandat.
     * 
     * @return String
     */
    public String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Returns the forIdParametreBouclement.
     * 
     * @return String
     */
    public String getForIdParametreBouclement() {
        return forIdParametreBouclement;
    }

    /**
     * Returns the forLibelle.
     * 
     * @return String
     */
    public String getForLibelle() {
        return forLibelle;
    }

    /**
     * Returns the fromNumero.
     * 
     * @return String
     */
    public String getFromNumero() {
        return fromNumero;
    }

    /**
     * Returns the orderBy.
     * 
     * @return String
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the forIdMandat.
     * 
     * @param forIdMandat
     *            The forIdMandat to set
     */
    public void setForIdMandat(String forIdMandat) {
        this.forIdMandat = forIdMandat;
    }

    /**
     * Sets the forIdParametreBouclement.
     * 
     * @param forIdParametreBouclement
     *            The forIdParametreBouclement to set
     */
    public void setForIdParametreBouclement(String forIdParametreBouclement) {
        this.forIdParametreBouclement = forIdParametreBouclement;
    }

    /**
     * Sets the forLibelle.
     * 
     * @param forLibelle
     *            The forLibelle to set
     */
    public void setForLibelle(String forLibelle) {
        this.forLibelle = forLibelle;
    }

    /**
     * Sets the fromNumero.
     * 
     * @param fromNumero
     *            The fromNumero to set
     */
    public void setFromNumero(String fromNumero) {
        this.fromNumero = fromNumero;
    }

    public void setOrderby(String newOrderby) {
        orderBy = newOrderby;
    }

    /**
     * Sets the orderBy.
     * 
     * @param orderBy
     *            The orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
