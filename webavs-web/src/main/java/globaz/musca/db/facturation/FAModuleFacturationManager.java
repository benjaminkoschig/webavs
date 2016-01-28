package globaz.musca.db.facturation;

import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class FAModuleFacturationManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdModuleFacturation = new String();
    private String forIdTypeModule = new String();
    private String fromLibelleDe = new String();
    private String fromLibelleFr = new String();
    private String fromLibelleIt = new String();

    private String order = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + FAModuleFacturation.TABLE_FAMODUP + " AS " + FAModuleFacturation.TABLE_FAMODUP;
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

        if (getForIdModuleFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += FAModuleFacturation.TABLE_FAMODUP + "." + FAModuleFacturation.FIELD_IDMODFAC + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleFacturation());
        }

        if (getForIdTypeModule().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += FAModuleFacturation.TABLE_FAMODUP + "." + FAModuleFacturation.FIELD_IDTYPEMODULE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeModule());
        }

        if (getFromLibelleFr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += FAModuleFacturation.TABLE_FAMODUP + "." + FAModuleFacturation.FIELD_LIBELLEFR + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromLibelleFr());
        }

        if (getFromLibelleDe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += FAModuleFacturation.TABLE_FAMODUP + "." + FAModuleFacturation.FIELD_LIBELLEDE + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromLibelleDe());
        }

        if (getFromLibelleIt().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += FAModuleFacturation.TABLE_FAMODUP + "." + FAModuleFacturation.FIELD_LIBELLEIT + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromLibelleIt());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAModuleFacturation();
    }

    /**
     * Getter
     */
    public String getForIdModuleFacturation() {
        return forIdModuleFacturation;
    }

    public String getForIdTypeModule() {
        return forIdTypeModule;
    }

    public String getFromLibelleDe() {
        return fromLibelleDe;
    }

    public String getFromLibelleFr() {
        return fromLibelleFr;
    }

    public String getFromLibelleIt() {
        return fromLibelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            String
     */
    public void orderByLibelleDe() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += FAModuleFacturation.FIELD_LIBELLEDE;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            String
     */
    public void orderByLibelleFr() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += FAModuleFacturation.FIELD_LIBELLEFR;
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
        order += FAModuleFacturation.FIELD_LIBELLEIT;
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
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            String
     */
    public void orderByNiveauAppel() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += FAModuleFacturation.FIELD_NIVEAUAPPEL + " ASC";

    }

    /**
     * Setter
     */
    public void setForIdModuleFacturation(String newForIdModuleFacturation) {
        forIdModuleFacturation = newForIdModuleFacturation;
    }

    public void setForIdTypeModule(String newForIdTypeModule) {
        forIdTypeModule = newForIdTypeModule;
    }

    public void setFromLibelleDe(String newFromLibelleDe) {
        fromLibelleDe = newFromLibelleDe;
    }

    public void setFromLibelleFr(String newFromLibelleFr) {
        fromLibelleFr = newFromLibelleFr;
    }

    public void setFromLibelleIt(String newFromLibelleIt) {
        fromLibelleIt = newFromLibelleIt;
    }
}
