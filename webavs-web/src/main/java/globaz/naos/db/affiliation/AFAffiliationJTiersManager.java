package globaz.naos.db.affiliation;

import java.io.Serializable;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class AFAffiliationJTiersManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int ORDER_AFFILIDATIONID = 2;
    public static final int ORDER_AFFILIENUMERO = 1;
    public static final java.lang.String PARITAIRE = "par";
    public static final java.lang.String PERSONNEL = "per";


    private String fromId;
    private String fromNom;
    private String likeAffilieNumero;
    private String likeAncienNumero;
    private String forTypeFacturation;
    //
    private String order;


    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return AFAffiliationJTiers.createFromClause(_getCollection());
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


        if (!JadeStringUtil.isEmpty(getFromId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MAIAFF>=" + this._dbWriteNumeric(statement.getTransaction(), getFromId());
        }


        if (!JadeStringUtil.isEmpty(getLikeAncienNumero())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "AFAFFIP.MALNAA";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAA like '" + getLikeAncienNumero() + "%'";
        }
        if (!JadeStringUtil.isEmpty(getFromNom())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "TITIERP.HTLDU1";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "TITIERP.HTLDU1 like "
                    + this._dbWriteString(statement.getTransaction(), getFromNom().toUpperCase() + "%");
        }

        if (!JadeStringUtil.isEmpty(getLikeAffilieNumero())) {
            if (JadeStringUtil.isEmpty(order)) {
                order = _getCollection() + "AFAFFIP.MALNAF";
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF like '" + getLikeAffilieNumero() + "%'";
        }
        
        if (!JadeStringUtil.isEmpty(getForTypeFacturation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForTypeFacturation().equals(AFAffiliationJTiersManager.PARITAIRE)) {
                sqlWhere += "MATTAF in(804002,804005,804010,804012)";
            } else {
                sqlWhere += "MATTAF in(804001,804004) AND MATPER not in(802005)";
            }
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
        return new AFAffiliationJTiers();
    }

    public java.lang.String getFromId() {
        return fromId;
    }

    public java.lang.String getFromNom() {
        return fromNom;
    }

    public java.lang.String getLikeAffilieNumero() {
        return likeAffilieNumero;
    }

    public java.lang.String getLikeAncienNumero() {
        return likeAncienNumero;
    }

    public java.lang.String getOrder() {
        return order;
    }
    
    public java.lang.String getForTypeFacturation() {
        return forTypeFacturation;
    }

    public void setFromId(java.lang.String newFromId) {
        fromId = newFromId;
    }

    public void setFromNom(java.lang.String newFromNom) {
        fromNom = newFromNom;
    }

    public void setLikeAffilieNumero(java.lang.String newLikeAffilieNumero) {
        likeAffilieNumero = newLikeAffilieNumero;
    }

    public void setLikeAncienNumero(java.lang.String likeAncienNumero) {
        this.likeAncienNumero = likeAncienNumero;
    }
    
    public void setForTypeFacturation(java.lang.String newForTypeFacturation) {
        forTypeFacturation = newForTypeFacturation;
    }

    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    public void setOrderBy(int orderBy) {
        if (orderBy == AFAffiliationJTiersManager.ORDER_AFFILIENUMERO) {
            order = "MALNAF";
        } else if (orderBy == AFAffiliationJTiersManager.ORDER_AFFILIDATIONID) {
            order = "MAIAFF";
        }
    }


}
