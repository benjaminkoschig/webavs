/*
 * Créé le 9 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.ventilation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author jmc Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAVPTypeDeProcedureOrdreManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDER_BY_ORDRE_ASC_ID_EXTERNE_RUBRIQUE_ASC = "2";
    public final static String ORDER_BY_RUBRIQUE_ASC_ORDRE_ASC = "1";
    private String forIdRubrique = "";
    private String forIdRubriqueIrrecouvrable = "";
    private String forIdRubriqueRecouvrement = "";
    private String forNotId = "";

    private String forOrdre = "";

    private String forPenal = "";

    private String forTypeOrdre = "";

    private String forTypeProcedure = "";
    private String fromIdExterne = "";
    /**
	 *
	 */
    private String order = "";
    private String orderBy = "";

    public CAVPTypeDeProcedureOrdreManager() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /*
     * Renvoie la clause FROM @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (getOrderBy().length() != 0) {
            String joinStr = " inner join " + _getCollection() + "CARUBRP on " + _getCollection()
                    + "CARUBRP.IDRUBRIQUE=" + _getCollection() + "CAOCCP.AGID";

            return _getCollection() + "CAOCCP" + joinStr;
        } else {
            return _getCollection() + "CAOCCP";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (getOrderBy().equals(CAVPTypeDeProcedureOrdreManager.ORDER_BY_RUBRIQUE_ASC_ORDRE_ASC)) {
            return _getCollection() + "CARUBRP.IDEXTERNE ASC, " + _getCollection() + "CAOCCP.BSMORD ASC";
        } else if (getOrderBy().equals(CAVPTypeDeProcedureOrdreManager.ORDER_BY_ORDRE_ASC_ID_EXTERNE_RUBRIQUE_ASC)) {
            return _getCollection() + "CAOCCP.BSMORD ASC, " + _getCollection() + "CARUBRP.IDEXTERNE ASC";
        } else {
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " AGID= " + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }
        if (getForIdRubriqueIrrecouvrable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " AGIC= " + this._dbWriteNumeric(statement.getTransaction(), getForIdRubriqueIrrecouvrable());
        }
        if (getForIdRubriqueRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " AGIR= " + this._dbWriteNumeric(statement.getTransaction(), getForIdRubriqueRecouvrement());
        }
        if (getForTypeOrdre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " BSTPOR= " + this._dbWriteNumeric(statement.getTransaction(), getForTypeOrdre());
        }
        if (getForTypeProcedure().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " BSTPRO= " + this._dbWriteNumeric(statement.getTransaction(), getForTypeProcedure());
        }
        if (getForOrdre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " BSMORD= " + this._dbWriteNumeric(statement.getTransaction(), getForOrdre());
        }
        if (getForPenal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " BSPEN= " + this._dbWriteString(statement.getTransaction(), getForPenal());
        }
        if (getForNotId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " BSID <> " + this._dbWriteNumeric(statement.getTransaction(), getForNotId());
        }
        if ((getOrderBy().length() != 0) && (getFromIdExterne().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CARUBRP.IDEXTERNE >= "
                    + this._dbWriteString(statement.getTransaction(), getFromIdExterne());
        }
        if (getOrderBy().length() != 0) {
            _getOrder(statement);
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Raccord de méthode auto-généré
        return new CAVPTypeDeProcedureOrdre();
    }

    /**
     * @return
     */
    public String getForIdRubrique() {
        return forIdRubrique;
    }

    public String getForIdRubriqueIrrecouvrable() {
        return forIdRubriqueIrrecouvrable;
    }

    public String getForIdRubriqueRecouvrement() {
        return forIdRubriqueRecouvrement;
    }

    /**
     * @return
     */
    public String getForNotId() {
        return forNotId;
    }

    /**
     * @return
     */
    public String getForOrdre() {
        return forOrdre;
    }

    /**
     * @return
     */
    public String getForPenal() {
        return forPenal;
    }

    /**
     * @return
     */
    public String getForTypeOrdre() {
        return forTypeOrdre;
    }

    /**
     * @return
     */
    public String getForTypeProcedure() {
        return forTypeProcedure;
    }

    /**
     * @return the fromIdExterne
     */
    public String getFromIdExterne() {
        return fromIdExterne;
    }

    /**
     * @return
     */
    public String getOrder() {
        return order;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @param string
     */
    public void setForIdRubrique(String string) {
        forIdRubrique = string;
    }

    public void setForIdRubriqueIrrecouvrable(String forIdRubriqueIrrecouvrable) {
        this.forIdRubriqueIrrecouvrable = forIdRubriqueIrrecouvrable;
    }

    public void setForIdRubriqueRecouvrement(String forIdRubriqueRecouvrement) {
        this.forIdRubriqueRecouvrement = forIdRubriqueRecouvrement;
    }

    /**
     * @param string
     */
    public void setForNotId(String string) {
        forNotId = string;
    }

    /**
     * @param string
     */
    public void setForOrdre(String string) {
        forOrdre = string;
    }

    /**
     * @param string
     */
    public void setForPenal(String string) {
        forPenal = string;
    }

    /**
     * @param string
     */
    public void setForTypeOrdre(String string) {
        forTypeOrdre = string;
    }

    /**
     * @param string
     */
    public void setForTypeProcedure(String string) {
        forTypeProcedure = string;
    }

    /**
     * @param fromIdExterne
     *            the fromIdExterne to set
     */
    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    /**
     * @param string
     */
    public void setOrder(String string) {
        order = string;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
