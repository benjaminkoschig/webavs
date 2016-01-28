package globaz.helios.db.comptes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CGCentreChargeManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdMandat = "";
    private String forNumero;
    private String fromNumero = "";
    private String orderBy = "";

    /**
     * Commentaire relatif au constructeur CGCentreChargeManager.
     */
    public CGCentreChargeManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGCCHAP";
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(BStatement)
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
        String sqlWhere = "";

        if (!JadeStringUtil.isBlank(getForIdMandat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMANDAT = " + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (!JadeStringUtil.isBlank(getFromNumero())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCENTRECHARGE >= " + _dbWriteNumeric(statement.getTransaction(), getFromNumero());
        }
        if (!JadeStringUtil.isBlank(getForNumero())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCENTRECHARGE = " + _dbWriteNumeric(statement.getTransaction(), getForNumero());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGCentreCharge();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 09:04:20)
     * 
     * @return String
     */
    public String getForIdMandat() {
        return forIdMandat;
    }

    public String getForNumero() {
        return forNumero;
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
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 09:04:20)
     * 
     * @param newForIdMandat
     *            String
     */
    public void setForIdMandat(String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    public void setForNumero(String forNumero) {
        this.forNumero = forNumero;
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

    /**
     * Method setOrderby.
     * 
     * @param newOrderby
     */
    public void setOrderby(String newOrderby) {
        orderBy = newOrderby;
    }

}
