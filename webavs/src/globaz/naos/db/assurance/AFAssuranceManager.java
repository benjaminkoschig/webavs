package globaz.naos.db.assurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

public class AFAssuranceManager extends BManager implements java.io.Serializable {

    private static final long serialVersionUID = -4100004306230435605L;
    private java.lang.String forCanton;
    private java.lang.String forGenreAssurance;
    private java.lang.String forIdAssurance;
    private java.lang.String forIdAssuranceReference;
    private java.lang.String forInTypeAssurance;
    private String forLibelleCourtOuLongDELike;
    private String forLibelleCourtOuLongFRLike;
    private String forLibelleCourtOuLongITLike;
    private String forLibelleCourtOuLongLike;
    private String forRubriqueComptableId;

    private java.lang.String forTypeAssurance;
    private java.lang.String fromLibelle;
    private java.lang.String fromLibelleDE;
    private java.lang.String fromLibelleFR;
    private java.lang.String fromLibelleIT;

    protected java.lang.String order = "";

    /**
     * Constructeur de AFAssuranceManager
     */
    public AFAssuranceManager() {
        wantCallMethodBeforeFind(true);
    }

    /**
     * Effectue des traitements avant une recherche dans la BD.
     * 
     * @see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        String langue = getSession().getIdLangueISO();
        if (JACalendar.LANGUAGE_DE.equalsIgnoreCase(langue)) {
            if (!JadeStringUtil.isEmpty(fromLibelle)) {
                setFromLibelleDE(fromLibelle);
            }
            if (!JadeStringUtil.isEmpty(forLibelleCourtOuLongLike)) {
                setForLibelleCourtOuLongDELike(forLibelleCourtOuLongLike);
            }
            orderByLibelleDE();
        } else if (JACalendar.LANGUAGE_IT.equalsIgnoreCase(langue)) {
            if (!JadeStringUtil.isEmpty(fromLibelle)) {
                setFromLibelleIT(fromLibelle);
            }
            if (!JadeStringUtil.isEmpty(forLibelleCourtOuLongLike)) {
                setForLibelleCourtOuLongITLike(forLibelleCourtOuLongLike);
            }
            orderByLibelleIT();
        } else {
            if (!JadeStringUtil.isEmpty(fromLibelle)) {
                setFromLibelleFR(fromLibelle);
            }
            if (!JadeStringUtil.isEmpty(forLibelleCourtOuLongLike)) {
                setForLibelleCourtOuLongFRLike(forLibelleCourtOuLongLike);
            }
            orderByLibelleFR();
        }
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFASSUP";
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

        if (!JadeStringUtil.isEmpty(getForIdAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAssurance());
        }

        if (!JadeStringUtil.isEmpty(getForIdAssuranceReference())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIREA=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAssuranceReference());
        }

        if (!JadeStringUtil.isEmpty(getForGenreAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBTGEN=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAssurance());
        }

        if (!JadeStringUtil.isEmpty(getForTypeAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBTTYP=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeAssurance());
        }
        if (!JadeStringUtil.isEmpty(getForInTypeAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBTTYP in (" + getForInTypeAssurance() + ") ";
        }

        if (!JadeStringUtil.isEmpty(getFromLibelleFR())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBLLIF>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleFR());
        }

        if (!JadeStringUtil.isEmpty(getFromLibelleDE())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBLLID>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleDE());
        }

        if (!JadeStringUtil.isEmpty(getFromLibelleIT())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBLLII>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleIT());
        }

        if (!JadeStringUtil.isEmpty(getForLibelleCourtOuLongFRLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(upper(MBLLCF) LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%"
                            + getForLibelleCourtOuLongFRLike().toUpperCase() + "%");
            sqlWhere += " OR upper(MBLLIF) LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%"
                            + getForLibelleCourtOuLongFRLike().toUpperCase() + "%") + ")";
        }

        if (!JadeStringUtil.isEmpty(getForLibelleCourtOuLongDELike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(upper(MBLLCD) LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%"
                            + getForLibelleCourtOuLongDELike().toUpperCase() + "%");
            sqlWhere += " OR upper(MBLLID) LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%"
                            + getForLibelleCourtOuLongDELike().toUpperCase() + "%") + ")";
        }

        if (!JadeStringUtil.isEmpty(getForLibelleCourtOuLongITLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(upper(MBLLCI) LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%"
                            + getForLibelleCourtOuLongITLike().toUpperCase() + "%");
            sqlWhere += " OR upper(MBLLII) LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%"
                            + getForLibelleCourtOuLongITLike().toUpperCase() + "%") + ")";
        }

        if (!JadeStringUtil.isEmpty(getForRubriqueComptableId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIRUB=" + this._dbWriteNumeric(statement.getTransaction(), getForRubriqueComptableId());
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
        return new AFAssurance();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForCanton() {
        return forCanton;
    }

    public java.lang.String getForGenreAssurance() {
        return forGenreAssurance;
    }

    public java.lang.String getForIdAssurance() {
        return forIdAssurance;
    }

    public java.lang.String getForIdAssuranceReference() {
        return forIdAssuranceReference;
    }

    public java.lang.String getForInTypeAssurance() {
        return forInTypeAssurance;
    }

    public String getForLibelleCourtOuLongDELike() {
        return forLibelleCourtOuLongDELike;
    }

    public String getForLibelleCourtOuLongFRLike() {
        return forLibelleCourtOuLongFRLike;
    }

    public String getForLibelleCourtOuLongITLike() {
        return forLibelleCourtOuLongITLike;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getForLibelleCourtOuLongLike() {
        return forLibelleCourtOuLongLike;
    }

    public String getForRubriqueComptableId() {
        return forRubriqueComptableId;
    }

    public java.lang.String getForTypeAssurance() {
        return forTypeAssurance;
    }

    public java.lang.String getFromLibelle() {
        return fromLibelle;
    }

    private java.lang.String getFromLibelleDE() {
        return fromLibelleDE;
    }

    private java.lang.String getFromLibelleFR() {
        return fromLibelleFR;
    }

    private java.lang.String getFromLibelleIT() {
        return fromLibelleIT;
    }

    private void orderByLibelleDE() {
        order = "MBLLID";
    }

    private void orderByLibelleFR() {
        order = "MBLLIF";
    }

    private void orderByLibelleIT() {
        order = "MBLLII";
    }

    public void setForCanton(java.lang.String forCanton) {
        this.forCanton = forCanton;
    }

    public void setForGenreAssurance(java.lang.String newForGenreAssurance) {
        forGenreAssurance = newForGenreAssurance;
    }

    public void setForIdAssurance(java.lang.String newForIdAssurance) {
        forIdAssurance = newForIdAssurance;
    }

    public void setForIdAssuranceReference(java.lang.String string) {
        forIdAssuranceReference = string;
    }

    public void setForInTypeAssurance(java.lang.String forInTypeAssurance) {
        this.forInTypeAssurance = forInTypeAssurance;
    }

    public void setForLibelleCourtOuLongDELike(String string) {
        forLibelleCourtOuLongDELike = string;
    }

    public void setForLibelleCourtOuLongFRLike(String string) {
        forLibelleCourtOuLongFRLike = string;
    }

    public void setForLibelleCourtOuLongITLike(String string) {
        forLibelleCourtOuLongITLike = string;
    }

    public void setForLibelleCourtOuLongLike(String string) {
        forLibelleCourtOuLongLike = string;
    }

    public void setForRubriqueComptableId(String string) {
        forRubriqueComptableId = string;
    }

    public void setForTypeAssurance(java.lang.String newForTypeAssurance) {
        forTypeAssurance = newForTypeAssurance;
    }

    public void setFromLibelle(java.lang.String string) {
        fromLibelle = string;
    }

    public void setFromLibelleDE(java.lang.String string) {
        fromLibelleDE = string;
    }

    public void setFromLibelleFR(java.lang.String string) {
        fromLibelleFR = string;
    }

    public void setFromLibelleIT(java.lang.String string) {
        fromLibelleIT = string;
    }

}
