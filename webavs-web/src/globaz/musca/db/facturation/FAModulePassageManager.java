package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class FAModulePassageManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdAction = new String();
    private java.lang.String forIdModuleFacturation = new String();
    private java.lang.String forIdPassage = new String();
    private java.lang.String forIdTypeModule = new String();
    private java.lang.String forNiveauAppel = new String();
    private java.lang.String forNomClasse = new String();
    private java.lang.String fromIdModuleFacturation = new String();
    private java.lang.String fromIdPassage = new String();
    private java.lang.String fromLibelleDe = new String();

    private java.lang.String fromLibelleFr = new String();
    private java.lang.String fromLibelleIt = new String();
    private java.lang.String fromNiveauAppel = new String();

    protected java.lang.String order = "";

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAModulePassage.TABLE_FIELDS + ", FAMODUP.NOMCLASSE, FAMODUP.NIVEAUAPPEL, FAMODUP.IDTYPEMODULE, "
                + "FAMODUP.LIBELLEFR, FAMODUP.LIBELLEDE, FAMODUP.LIBELLEIT";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = _getCollection() + "FAMOPAP AS FAMOPAP " + "INNER JOIN " + _getCollection()
                + "FAMODUP AS FAMODUP ON (FAMOPAP.IDMODFAC=FAMODUP.IDMODFAC) ";
        return from;
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

        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPAP.IDPASSAGE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }

        if (getForIdModuleFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPAP.IDMODFAC="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleFacturation());
        }

        if (getForIdAction().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPAP.IDACTION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAction());
        }

        if (getFromIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPAP.IDPASSAGE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdPassage());
        }

        if (getFromIdModuleFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPAP.IDMODFAC>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdModuleFacturation());
        }

        if (getFromLibelleFr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.LIBELLEFR>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleFr());
        }

        if (getFromLibelleDe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.LIBELLEDE>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleDe());
        }

        if (getFromLibelleIt().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.LIBELLEIT>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleIt());
        }

        if (getForNiveauAppel().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.NIVEAUAPPEL=" + this._dbWriteNumeric(statement.getTransaction(), getForNiveauAppel());
        }

        if (getForNomClasse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.NOMCLASSE=" + this._dbWriteNumeric(statement.getTransaction(), getForNomClasse());
        }

        if (getFromNiveauAppel().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.NIVEAUAPPEL>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromNiveauAppel());
        }

        if (getForIdTypeModule().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.IDTYPEMODULE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeModule());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAModulePassage();
    }

    public java.lang.String getForIdAction() {
        return forIdAction;
    }

    public java.lang.String getForIdModuleFacturation() {
        return forIdModuleFacturation;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.04.2003 09:45:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeModule() {
        return forIdTypeModule;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:54:08)
     * 
     * @return java.lang.String
     */
    public String getForNiveauAppel() {
        return forNiveauAppel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:53:10)
     * 
     * @return java.lang.String
     */
    public String getForNomClasse() {
        return forNomClasse;
    }

    public java.lang.String getFromIdModuleFacturation() {
        return fromIdModuleFacturation;
    }

    public java.lang.String getFromIdPassage() {
        return fromIdPassage;
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
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:54:08)
     * 
     * @return java.lang.String
     */
    public String getFromNiveauAppel() {
        return fromNiveauAppel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.04.2003 15:23:16)
     */
    public void orderByDateFacturation() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "DATEFACTURATION ASC";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.04.2003 15:23:16)
     */
    public void orderByIdPassageDesc() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "IDPASSAGE DESC";
    }

    public void orderByIdPlan() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "IDPLAN DESC";
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
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByNiveauAppel() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "NIVEAUAPPEL ASC";
    }

    public void setForIdAction(java.lang.String forIdAction) {
        this.forIdAction = forIdAction;
    }

    public void setForIdModuleFacturation(java.lang.String forIdModuleFacturation) {
        this.forIdModuleFacturation = forIdModuleFacturation;
    }

    /**
     * Setter
     */
    public void setForIdPassage(java.lang.String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.04.2003 09:45:23)
     * 
     * @param newForIdTypeModule
     *            java.lang.String
     */
    public void setForIdTypeModule(java.lang.String newForIdTypeModule) {
        forIdTypeModule = newForIdTypeModule;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:50:27)
     */
    public void setForNiveauAppel(String forNiveauAppel) {
        this.forNiveauAppel = forNiveauAppel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:50:27)
     */
    public void setForNomClasse(String forNomClasse) {
        this.forNomClasse = forNomClasse;
    }

    public void setFromIdModuleFacturation(java.lang.String fromIdModuleFacturation) {
        this.fromIdModuleFacturation = fromIdModuleFacturation;
    }

    public void setFromIdPassage(java.lang.String fromIdPassage) {
        this.fromIdPassage = fromIdPassage;
    }

    public void setFromLibelleDe(java.lang.String fromLibelleDe) {
        this.fromLibelleDe = fromLibelleDe;
    }

    public void setFromLibelleFr(java.lang.String fromLibelleFr) {
        this.fromLibelleFr = fromLibelleFr;
    }

    public void setFromLibelleIt(java.lang.String fromLibelleIt) {
        this.fromLibelleIt = fromLibelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:50:27)
     */
    public void setFromNiveauAppel(String fromNiveauAppel) {
        this.fromNiveauAppel = fromNiveauAppel;
    }
}
