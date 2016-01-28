package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class AFReviseurManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdReviseur;
    private java.lang.String forVisa;
    private java.lang.String likeVisa;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFREVIP";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
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

        if (!JadeStringUtil.isEmpty(getForIdReviseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MIIREV=" + this._dbWriteString(statement.getTransaction(), getForIdReviseur());
        }
        if (!JadeStringUtil.isEmpty(getForVisa())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MILVIS=" + this._dbWriteString(statement.getTransaction(), getForVisa());
        }
        if (!JadeStringUtil.isEmpty(getLikeVisa())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MILVIS LIKE" + this._dbWriteString(statement.getTransaction(), getLikeVisa() + "%");
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
        return new AFReviseur();
    }

    /**
     * @return
     */
    public java.lang.String getForIdReviseur() {
        return forIdReviseur;
    }

    /**
     * @return
     */
    public java.lang.String getForVisa() {
        return forVisa;
    }

    /**
     * @return
     */
    public java.lang.String getLikeVisa() {
        return likeVisa;
    }

    /**
     * @param string
     */
    public void setForIdReviseur(java.lang.String string) {
        forIdReviseur = string;
    }

    /**
     * @param string
     */
    public void setForVisa(java.lang.String string) {
        forVisa = string;
    }

    /**
     * @param string
     */
    public void setLikeVisa(java.lang.String string) {
        likeVisa = string;
    }

}
