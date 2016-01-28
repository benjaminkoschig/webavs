package globaz.naos.db.parametreAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Le Manager pour l'entité ParametreAssurance.
 * 
 * @author sau
 */
public class AFParametreAssuranceManager extends BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forDate;
    private java.lang.String forGenre;
    private java.lang.String forIdAssurance;
    private java.lang.String forSexe;

    protected java.lang.String order = "";

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFPARAS";
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

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForIdAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAssurance());
        }

        if (!JadeStringUtil.isEmpty(getForGenre())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MXTGEN=" + this._dbWriteNumeric(statement.getTransaction(), getForGenre());
        }

        if (!JadeStringUtil.isEmpty(getForDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MXDDEB <=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }

        if (!JadeStringUtil.isEmpty(getForSexe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MXTSEX=" + this._dbWriteNumeric(statement.getTransaction(), getForSexe()) + " or "
                    + " MXTSEX=0)";
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFParametreAssurance();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForDate() {
        return forDate;
    }

    public java.lang.String getForGenre() {
        return forGenre;
    }

    public java.lang.String getForIdAssurance() {
        return forIdAssurance;
    }

    public java.lang.String getForSexe() {
        return forSexe;
    }

    public java.lang.String getOrder() {
        return order;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForDate(java.lang.String string) {
        forDate = string;
    }

    public void setForGenre(java.lang.String string) {
        forGenre = string;
    }

    public void setForIdAssurance(java.lang.String string) {
        forIdAssurance = string;
    }

    public void setForSexe(java.lang.String string) {
        forSexe = string;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }

    public void setOrderByDateDebut() {
        setOrder("MXDDEB");
    }

    public void setOrderByDateDebutDesc() {
        setOrder("MXDDEB DESC");
    }

    public void setOrderByGenreDateDebutDesc() {
        setOrder("MXTGEN, MXDDEB");
    }
}
