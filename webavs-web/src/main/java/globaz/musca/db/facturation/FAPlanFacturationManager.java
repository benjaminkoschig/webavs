package globaz.musca.db.facturation;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.util.FARightHelper;

public class FAPlanFacturationManager extends globaz.globall.db.BManager implements java.io.Serializable {

    private static final long serialVersionUID = -498936311573033366L;
    private java.lang.String forIdPlanFacturation = new String();
    private java.lang.String forIdTypeFacturation = new String();
    private java.lang.String fromLibelleDe = new String();
    private java.lang.String fromLibelleFr = new String();
    private java.lang.String fromLibelleIt = new String();
    protected java.lang.String order = "";

    /** Permet de filtrer les plans selon les droits accordés à l'utilisateur */
    private Boolean wantFilterByPlanFacturation = new Boolean(false);

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAPlanFacturation.TABLE_FIELDS;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "FAPLFAP AS FAPLFAP ";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdPlanFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPLFAP.IDPLANFACTURATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanFacturation());
        }

        if (getForIdTypeFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPLFAP.IDTYPEFACTURATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeFacturation());
        }

        if (getFromLibelleFr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPLFAP.LIBELLEFR>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleFr());
        }

        if (getFromLibelleDe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPLFAP.LIBELLEDE>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleDe());
        }

        if (getFromLibelleIt().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPLFAP.LIBELLEIT>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleIt());
        }

        if ((getWantFilterByPlanFacturation() != null) && getWantFilterByPlanFacturation().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPLFAP.IDPLANFACTURATION in ("
                    + FARightHelper.getIdPlanFacturationRightsInString(getSession(), FWSecureConstants.READ) + ")";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAPlanFacturation();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdPlanFacturation() {
        return forIdPlanFacturation;
    }

    public java.lang.String getForIdTypeFacturation() {
        return forIdTypeFacturation;
    }

    public java.lang.String getFromLibelleDe() {
        return fromLibelleDe;
    }

    public java.lang.String getFromLibelleFr() {
        return fromLibelleFr;
    }

    public java.lang.String getFromLibelleIt() {
        return fromLibelleIt;
    }

    /**
     * Permet de filtrer les plans selon les droits accordés à l'utilisateur
     */
    public Boolean getWantFilterByPlanFacturation() {
        return wantFilterByPlanFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleDe() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "LIBELLEDE";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleFr() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "LIBELLEFR";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            String
     */
    public void orderByLibelleIt() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "LIBELLEIT";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            String
     */
    public void orderByLibelleLangueSession() {
        if (getSession().getIdLangueISO().equalsIgnoreCase("FR")) {
            orderByLibelleFr();
        } else if (getSession().getIdLangueISO().equalsIgnoreCase("DE")) {
            orderByLibelleDe();
        } else if (getSession().getIdLangueISO().equalsIgnoreCase("IT")) {
            orderByLibelleIt();
        } else { // par défaut
            orderByLibelleFr();
        }
    }

    /**
     * Setter
     */
    public void setForIdPlanFacturation(java.lang.String newForIdPlanFacturation) {
        forIdPlanFacturation = newForIdPlanFacturation;
    }

    public void setForIdTypeFacturation(java.lang.String newForIdTypeFacturation) {
        forIdTypeFacturation = newForIdTypeFacturation;
    }

    public void setFromLibelleDe(java.lang.String newFromLibelleDe) {
        fromLibelleDe = newFromLibelleDe;
    }

    public void setFromLibelleFr(java.lang.String newFromLibelleFr) {
        fromLibelleFr = newFromLibelleFr;
    }

    public void setFromLibelleIt(java.lang.String newFromLibelleIt) {
        fromLibelleIt = newFromLibelleIt;
    }

    /**
     * Permet de filtrer les plans selon les droits accordés à l'utilisateur
     */
    public void setWantFilterByPlanFacturation(Boolean boolean1) {
        wantFilterByPlanFacturation = boolean1;
    }

}
