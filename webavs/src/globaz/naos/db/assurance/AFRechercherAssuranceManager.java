package globaz.naos.db.assurance;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class AFRechercherAssuranceManager extends BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAffilieNumero;
    private java.lang.String forCanton;
    private java.lang.String forDate;
    private java.lang.String forGenreAssurance;
    private java.lang.String forIdTiers;
    private java.lang.String forTypeAssurance;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFAFFIP," + _getCollection() + "AFPLAFP," + _getCollection() + "AFCOTIP,"
                + _getCollection() + "AFASSUP";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = _getCollection() + "AFAFFIP.MAIAFF = " + _getCollection() + "AFPLAFP.MAIAFF AND "
                + _getCollection() + "AFPLAFP.MUIPLA = " + _getCollection() + "AFCOTIP.MUIPLA AND " + _getCollection()
                + "AFPLAFP.MUBINA = "
                + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + " AND " + _getCollection() + "AFCOTIP.MBIASS = " + _getCollection() + "AFASSUP.MBIASS ";

        if (!JadeStringUtil.isEmpty(getForAffilieNumero())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF=" + this._dbWriteString(statement.getTransaction(), getForAffilieNumero());
        }
        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }
        if (!JadeStringUtil.isEmpty(getForTypeAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTTYP=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeAssurance());
        }
        if (!JadeStringUtil.isEmpty(getForGenreAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTGEN=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAssurance());
        }
        if (!JadeStringUtil.isEmpty(getForDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCDDEB <=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }
        if (!JadeStringUtil.isEmpty(getForCanton())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBTCAN=" + this._dbWriteNumeric(statement.getTransaction(), getForCanton());
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
        return null /* new AFAssurance() */;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForAffilieNumero() {
        return forAffilieNumero;
    }

    public java.lang.String getForCanton() {
        return forCanton;
    }

    public java.lang.String getForDate() {
        return forDate;
    }

    public java.lang.String getForGenreAssurance() {
        return forGenreAssurance;
    }

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public java.lang.String getForTypeAssurance() {
        return forTypeAssurance;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAffilieNumero(java.lang.String string) {
        forAffilieNumero = string;
    }

    public void setForCanton(java.lang.String string) {
        forCanton = string;
    }

    public void setForDate(java.lang.String string) {
        forDate = string;
    }

    public void setForGenreAssurance(java.lang.String string) {
        forGenreAssurance = string;
    }

    public void setForIdTiers(java.lang.String string) {
        forIdTiers = string;
    }

    public void setForTypeAssurance(java.lang.String string) {
        forTypeAssurance = string;
    }
}
