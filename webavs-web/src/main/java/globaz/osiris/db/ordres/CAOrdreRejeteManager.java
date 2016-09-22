package globaz.osiris.db.ordres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CAOrdreRejeteManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = -8546248011838964175L;

    private java.lang.String forIdOG = new String();

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAOrdreRejete();
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        return _getCollection() + "CAORREJ AS ORREJ INNER JOIN " + _getCollection()
                + "CAOPOVP AS ORDRE ON ORREJ.IDORDR = ORDRE.IDORDRE ";
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
        if (!JadeStringUtil.isEmpty(getForIdOG())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ORDRE.IDORDREGROUPE  = " + this._dbWriteNumeric(statement.getTransaction(), getForIdOG());
        }

        return sqlWhere;
    }

    public java.lang.String getForIdOG() {
        return forIdOG;
    }

    public void setForIdOG(java.lang.String forIdOG) {
        this.forIdOG = forIdOG;
    }

}
