package globaz.naos.db.taxeCo2;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class AFTaxeCo2Manager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAffiliationId = new String();
    private String forAnneeMasse = new String();
    private String forAnneeRedistri = new String();
    private String forEtatTaxe = new String();

    private String forIdPassage = new String();

    private String forIdRubrique = new String();
    private String forMasse = new String();
    private String forMotifFin = new String();
    private String forNotTaxeCo2Id = new String();
    private String forNumAffilie = new String();

    private String forTauxForce = new String();

    private String forTaxeCo2Id = new String();
    private String fromMasse = new String();
    private String fromNumAffilie = new String();
    private Boolean hasMotifFin = new Boolean(false);
    private Boolean hasTauxForce = null;
    private String order = new String();

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return AFTaxeCo2.TABLE_FIELDS + ", AFAFFIP.MALNAF, AFAFFIP.MATPER ";
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFTACOP AS AFTACOP INNER JOIN " + _getCollection()
                + "AFAFFIP AS AFAFFIP ON (AFTACOP.MAIAFF=AFAFFIP.MAIAFF) ";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isEmpty(getOrder())) {
            return "MWDANN ASC, MALNAF ASC";
        } else {
            return getOrder();
        }

    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        if (getForTauxForce().equals("force")) {
            setHasTauxForce(new Boolean(true));
        } else if (getForTauxForce().equals("nonForce")) {
            setHasTauxForce(new Boolean(false));
        } else {
            setHasTauxForce(null);
        }

        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        if (getFromNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFAFFIP.MALNAF>=" + this._dbWriteString(statement.getTransaction(), getFromNumAffilie());
        }
        if (getForNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFAFFIP.MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie());
        }
        if (getForAnneeMasse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MWDANN=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeMasse());
        }
        if (getForAnneeRedistri().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MWDANR=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeRedistri());
        }
        if (getFromMasse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MWMMAS>=" + this._dbWriteNumeric(statement.getTransaction(), getFromMasse());
        }
        if (getForMotifFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MATMOT=" + this._dbWriteNumeric(statement.getTransaction(), getForMotifFin());
        }
        if (hasMotifFin.booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MATMOT <> 0";
        }
        if (getForEtatTaxe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MWTFAC=" + this._dbWriteNumeric(statement.getTransaction(), getForEtatTaxe());
        }
        if (getHasTauxForce() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getHasTauxForce().booleanValue()) {
                sqlWhere += "AFTACOP.MWMTAU<>0 AND AFTACOP.MWMTAU IS NOT NULL";
            } else {
                sqlWhere += "(AFTACOP.MWMTAU=0 OR AFTACOP.MWMTAU IS NULL)";
            }
        }
        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MWIPAS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }
        if (getForAffiliationId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }
        if (getForNotTaxeCo2Id().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MWIDTC<>" + this._dbWriteNumeric(statement.getTransaction(), getForNotTaxeCo2Id());
        }
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MWIRUB=" + this._dbWriteNumeric(statement.getTransaction(), getForNotTaxeCo2Id());
        }
        if (getForTaxeCo2Id().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AFTACOP.MWIDTC=" + this._dbWriteNumeric(statement.getTransaction(), getForTaxeCo2Id());
        }

        // if(isForFacturation.booleanValue()){
        // sqlWhere += _getGroupBy(statement);
        // }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFTaxeCo2();
    }

    public String getForAffiliationId() {
        return forAffiliationId;
    }

    public String getForAnneeMasse() {
        return forAnneeMasse;
    }

    public String getForAnneeRedistri() {
        return forAnneeRedistri;
    }

    public String getForEtatTaxe() {
        return forEtatTaxe;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getForIdRubrique() {
        return forIdRubrique;
    }

    public String getForMasse() {
        return forMasse;
    }

    public String getForMotifFin() {
        return forMotifFin;
    }

    public String getForNotTaxeCo2Id() {
        return forNotTaxeCo2Id;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public String getForTauxForce() {
        return forTauxForce;
    }

    public String getForTaxeCo2Id() {
        return forTaxeCo2Id;
    }

    public String getFromMasse() {
        return fromMasse;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public Boolean getHasMotifFin() {
        return hasMotifFin;
    }

    public Boolean getHasTauxForce() {
        return hasTauxForce;
    }

    public String getOrder() {
        return order;
    }

    public void setForAffiliationId(java.lang.String forAffiliationId) {
        this.forAffiliationId = forAffiliationId;
    }

    public void setForAnneeMasse(String forAnnee) {
        forAnneeMasse = forAnnee;
    }

    public void setForAnneeRedistri(String forAnneeRedistri) {
        this.forAnneeRedistri = forAnneeRedistri;
    }

    public void setForEtatTaxe(String forEtatTaxe) {
        this.forEtatTaxe = forEtatTaxe;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public void setForIdRubrique(String forIdRubrique) {
        this.forIdRubrique = forIdRubrique;
    }

    public void setForMasse(String forMasse) {
        this.forMasse = forMasse;
    }

    public void setForMotifFin(String forMotifFin) {
        this.forMotifFin = forMotifFin;
    }

    public void setForNotTaxeCo2Id(String forNotTaxeCo2Id) {
        this.forNotTaxeCo2Id = forNotTaxeCo2Id;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setForTauxForce(String forTauxForce) {
        this.forTauxForce = forTauxForce;
    }

    public void setForTaxeCo2Id(String forTaxeCo2Id) {
        this.forTaxeCo2Id = forTaxeCo2Id;
    }

    public void setFromMasse(String fromMasse) {
        this.fromMasse = fromMasse;
    }

    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setHasMotifFin(Boolean hasMotifFin) {
        this.hasMotifFin = hasMotifFin;
    }

    public void setHasTauxForce(Boolean hasTauxForce) {
        this.hasTauxForce = hasTauxForce;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
