package globaz.pavo.db.bta;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CIDossierBtaManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateFinDossier = new String();
    private String forDateReceptionDossier = new String();
    private String forEtatDossier = new String();
    // attributs de la table CIBTADP
    private String forIdDossierBta = new String();
    private String forIdTiersImpotent = new String();
    // autres variables
    private String forIdTiersImpotentNNSS = "";
    private String forIdTiersRequerantNNSS = "";
    private String forMotifFin = new String();
    private String forNomImpotent = "";

    private String forNomRequerant = "";
    private String forNumeroInterneDossier = new String();
    private String fromDateReceptionDossier = new String();
    private String orderBy = "tiers.HTLDE1";// par defaut on tri sur le nom de

    private String untilDateReceptionDossier = new String();

    // l'impotent

    @Override
    protected String _getFields(BStatement statement) {
        String fields = "*";
        if (!JadeStringUtil.isBlank(forIdTiersRequerantNNSS) || !JadeStringUtil.isBlank(forNomRequerant)) {
            fields = "bta.KDIDD,bta.HTITIE,bta.KDIDIN,bta.KDDATR,bta.KDDATF,bta.KDMOTF,bta.KDETAP,bta.PSPY,avs.HXNAVS,tiers.HTLDE1,tiers.HTLDE2,pers.HPTSEX,pers.HPDNAI,tiers2.HTLDE1 as HTLDE11,tiers2.HTLDE2 as HTLDE21";
        }
        return fields;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        // jointure pour avoir également accès aux informations du tiers
        String from = _getCollection() + "CIBTADP bta";
        from += " inner join " + _getCollection() + "TIPAVSP avs on bta.HTITIE=avs.HTITIE";
        from += " inner join " + _getCollection() + "TITIERP tiers on bta.HTITIE=tiers.HTITIE";
        from += " inner join " + _getCollection() + "TIPERSP pers on bta.HTITIE=pers.HTITIE";

        if (!JadeStringUtil.isBlank(forIdTiersRequerantNNSS) || !JadeStringUtil.isBlank(forNomRequerant)) {
            from += " inner join " + _getCollection() + "CIBTARP req on bta.KDIDD=req.KDIDD";
            from += " inner join " + _getCollection() + "TIPAVSP avs2 on req.HTITIE=avs2.HTITIE";
            from += " inner join " + _getCollection() + "TITIERP tiers2 on req.HTITIE=tiers2.HTITIE";
            from += " inner join " + _getCollection() + "TIPERSP pers2 on req.HTITIE=pers2.HTITIE";
        }

        return from;
    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return orderBy;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdDossierBta().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIDD=" + _dbWriteNumeric(statement.getTransaction(), getForIdDossierBta());
        }
        // traitement du positionnement
        if (getForIdTiersImpotent().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE=" + _dbWriteNumeric(statement.getTransaction(), getForIdTiersImpotent());
        }
        // traitement du positionnement
        if (getForNumeroInterneDossier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIDIN=" + _dbWriteNumeric(statement.getTransaction(), getForNumeroInterneDossier());
        }
        // traitement du positionnement
        if (getForDateReceptionDossier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATR=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateReceptionDossier());
        }
        // traitement du positionnement
        if (getFromDateReceptionDossier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATR>=" + _dbWriteDateAMJ(statement.getTransaction(), getFromDateReceptionDossier());
        }
        // traitement du positionnement
        if (getUntilDateReceptionDossier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATR<=" + _dbWriteDateAMJ(statement.getTransaction(), getUntilDateReceptionDossier());
        }
        // traitement du positionnement
        if (getForDateFinDossier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATF=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateFinDossier());
        }
        // traitement du positionnement
        if (getForMotifFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDMOTF=" + _dbWriteNumeric(statement.getTransaction(), getForMotifFin());
        }
        // traitement du positionnement
        if (getForEtatDossier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDETAP=" + _dbWriteNumeric(statement.getTransaction(), getForEtatDossier());
        }
        // traitement du positionnement
        if (getForIdTiersImpotentNNSS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "avs.HXNAVS=" + _dbWriteString(statement.getTransaction(), getForIdTiersImpotentNNSS());
        }
        // traitement du positionnement
        if (getForNomImpotent().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "tiers.HTLDE1 like " + _dbWriteString(statement.getTransaction(), getForNomImpotent() + "%");
        }
        // traitement du positionnement
        if (getForIdTiersRequerantNNSS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "avs2.HXNAVS=" + _dbWriteString(statement.getTransaction(), getForIdTiersRequerantNNSS());
        }
        // traitement du positionnement
        if (getForNomRequerant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "tiers2.HTLDE1 like " + _dbWriteString(statement.getTransaction(), getForNomRequerant() + "%");
        }

        return sqlWhere;

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIDossierBta();
    }

    public String getForDateFinDossier() {
        return forDateFinDossier;
    }

    public String getForDateReceptionDossier() {
        return forDateReceptionDossier;
    }

    public String getForEtatDossier() {
        return forEtatDossier;
    }

    public String getForIdDossierBta() {
        return forIdDossierBta;
    }

    public String getForIdTiersImpotent() {
        return forIdTiersImpotent;
    }

    public String getForIdTiersImpotentNNSS() {
        return forIdTiersImpotentNNSS;
    }

    public String getForIdTiersRequerantNNSS() {
        return forIdTiersRequerantNNSS;
    }

    public String getForMotifFin() {
        return forMotifFin;
    }

    public String getForNomImpotent() {
        return forNomImpotent;
    }

    public String getForNomRequerant() {
        return forNomRequerant;
    }

    public String getForNumeroInterneDossier() {
        return forNumeroInterneDossier;
    }

    public String getFromDateReceptionDossier() {
        return fromDateReceptionDossier;
    }

    public String getUntilDateReceptionDossier() {
        return untilDateReceptionDossier;
    }

    public void setForDateFinDossier(String forDateFinDossier) {
        this.forDateFinDossier = forDateFinDossier;
    }

    public void setForDateReceptionDossier(String forDateReceptionDossier) {
        this.forDateReceptionDossier = forDateReceptionDossier;
    }

    public void setForEtatDossier(String forEtatDossier) {
        this.forEtatDossier = forEtatDossier;
    }

    public void setForIdDossierBta(String forIdDossierBta) {
        this.forIdDossierBta = forIdDossierBta;
    }

    public void setForIdTiersImpotent(String forIdTiersImpotent) {
        this.forIdTiersImpotent = forIdTiersImpotent;
    }

    public void setForIdTiersImpotentNNSS(String forIdTiersImpotentNNSS) {
        this.forIdTiersImpotentNNSS = forIdTiersImpotentNNSS;
    }

    public void setForIdTiersRequerantNNSS(String forIdTiersRequerantNNSS) {
        this.forIdTiersRequerantNNSS = forIdTiersRequerantNNSS;
    }

    public void setForMotifFin(String forMotifFin) {
        this.forMotifFin = forMotifFin;
    }

    public void setForNomImpotent(String forNomImpotent) {
        this.forNomImpotent = forNomImpotent;
    }

    public void setForNomRequerant(String forNomRequerant) {
        this.forNomRequerant = forNomRequerant;
    }

    public void setForNumeroInterneDossier(String forNumeroInterneDossier) {
        this.forNumeroInterneDossier = forNumeroInterneDossier;
    }

    public void setFromDateReceptionDossier(String fromDateReceptionDossier) {
        this.fromDateReceptionDossier = fromDateReceptionDossier;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setUntilDateReceptionDossier(String untilDateReceptionDossier) {
        this.untilDateReceptionDossier = untilDateReceptionDossier;
    }
}
