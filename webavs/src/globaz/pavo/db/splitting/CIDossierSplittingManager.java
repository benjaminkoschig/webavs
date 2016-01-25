package globaz.pavo.db.splitting;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.pavo.util.CIUtil;

/**
 * Manager des dossiers de splitting. Date de création : (15.10.2002 10:38:19)
 * 
 * @author: dgi
 */
public class CIDossierSplittingManager extends BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String apartir;
    private String forIdDossierInterneSplitting = new String();
    private String forIdDossierSplitting = new String();
    private java.lang.String forIdEtat = new String();
    private java.lang.String forIdTiersAssure = new String();
    private java.lang.String forIdTiersAssureNNSS = "";
    private java.lang.String forIdTiersConjoint = new String();
    private java.lang.String forIdTiersConjointNNSS = "";
    private String forMotif = new String();
    private String forNotIdEtat = new String();
    private java.lang.String fromDateOuvertureDossier = new String();
    private java.lang.String fromIdDossierInterneSplitting = new String();
    private java.lang.String fromIdDossierSplitting = new String();
    private java.lang.String order = new String();

    // recherche
    private String tri;
    private String untilDateOuverture = new String();

    /**
     * Constructeur. Force l'appel de beforeFind afin de pouvoir générer la clause orderby Date de création :
     * (06.11.2002 08:06:53)
     */
    public CIDossierSplittingManager() {
        wantCallMethodBeforeFind(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.10.2002 08:31:46)
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {

        if ((getFromDateOuvertureDossier() != null) && (getFromDateOuvertureDossier().trim().length() > 0)) {
            // order by
            setOrderBy("KDDATE");
        } else if ((getFromIdDossierInterneSplitting() != null)
                && (getFromIdDossierInterneSplitting().trim().length() > 0)) {
            setOrderBy("KDIDIN");
        } else if ((forNotIdEtat != null) && (getForNotIdEtat().trim().length() > 0)) {
            // Pour ordrer l'impression des extraits dans le batch
            setOrderBy("KDUSER");
        }
        /*
         * else if (getFromIdTiersConjoint()!=null && getFromIdTiersConjoint().trim().length()>0) {
         * setOrderBy("KDITI2"); } else { setOrderBy("KDITI1"); }
         */
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CISPDSP";
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

        // traitement du positionnement
        if (getForIdEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIETA=" + this._dbWriteNumeric(statement.getTransaction(), getForIdEtat());
        }
        // traitement du positionnement
        if (getForIdTiersAssure().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if ("true".equalsIgnoreCase(forIdTiersAssureNNSS)) {
                sqlWhere += "KDITI1 >= "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CIUtil.fillWith(getForIdTiersAssure(), '0', 13))
                        + " AND KDITI1 <= "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CIUtil.fillWith(getForIdTiersAssure(), '9', 13));
            } else {
                sqlWhere += "KDITI1 >= "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CIUtil.fillWith(getForIdTiersAssure(), '0', 11))
                        + " AND KDITI1 <= "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CIUtil.fillWith(getForIdTiersAssure(), '9', 11));
            }
        }

        // traitement du positionnement
        if (getFromIdDossierSplitting().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDID>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdDossierSplitting());
        }
        // traitement du positionnement
        if (getForIdDossierSplitting().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDID=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDossierSplitting());
        }
        if (getFromIdDossierInterneSplitting().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIDIN>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdDossierInterneSplitting());
        }
        if (getForIdDossierInterneSplitting().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIDIN=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDossierInterneSplitting());
        }

        // traitement du positionnement
        if (getForIdTiersConjoint().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if ("true".equalsIgnoreCase(forIdTiersConjointNNSS)) {
                sqlWhere += "KDITI2 >= "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CIUtil.fillWith(getForIdTiersConjoint(), '0', 13))
                        + " AND KDITI2 <= "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CIUtil.fillWith(getForIdTiersConjoint(), '9', 13));
            } else {
                sqlWhere += "KDITI2 >= "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CIUtil.fillWith(getForIdTiersConjoint(), '0', 11))
                        + " AND KDITI2 <= "
                        + this._dbWriteNumeric(statement.getTransaction(),
                                CIUtil.fillWith(getForIdTiersConjoint(), '9', 11));
            }
        }

        // traitement du positionnement
        if (getFromDateOuvertureDossier().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATE>="
                    + this._dbWriteNumeric(statement.getTransaction(),
                            CIUtil.formatDateAMJ(getFromDateOuvertureDossier()));
        }
        if (getUntilDateOuverture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDDATE<="
                    + this._dbWriteNumeric(statement.getTransaction(), CIUtil.formatDateAMJ(getUntilDateOuverture()));
        }
        if (getForMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " KDIMSP = " + this._dbWriteNumeric(statement.getTransaction(), getForMotif());
        }
        if (getForNotIdEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDIETA<> " + this._dbWriteNumeric(statement.getTransaction(), getForNotIdEtat());

        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIDossierSplitting();
    }

    /**
     * Returns the forIdDossierInterneSplitting.
     * 
     * @return String
     */
    public String getForIdDossierInterneSplitting() {
        return forIdDossierInterneSplitting;
    }

    public String getForIdDossierSplitting() {
        return forIdDossierSplitting;
    }

    public java.lang.String getForIdEtat() {
        return forIdEtat;
    }

    public java.lang.String getForIdTiersAssure() {
        return forIdTiersAssure;
    }

    /**
     * @return
     */
    public java.lang.String getForIdTiersAssureNNSS() {
        return forIdTiersAssureNNSS;
    }

    public java.lang.String getForIdTiersConjoint() {
        return forIdTiersConjoint;
    }

    /**
     * @return
     */
    public java.lang.String getForIdTiersConjointNNSS() {
        return forIdTiersConjointNNSS;
    }

    /**
     * Returns the forMotif.
     * 
     * @return String
     */
    public String getForMotif() {
        return forMotif;
    }

    /**
     * @return
     */
    public String getForNotIdEtat() {
        return forNotIdEtat;
    }

    public java.lang.String getFromDateOuvertureDossier() {
        return fromDateOuvertureDossier;
    }

    /**
     * Returns the fromIdDossierInterneSplitting.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdDossierInterneSplitting() {
        return fromIdDossierInterneSplitting;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 11:21:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdDossierSplitting() {
        return fromIdDossierSplitting;
    }

    /**
     * Returns the untilDateOuverture.
     * 
     * @return String
     */
    public String getUntilDateOuverture() {
        return untilDateOuverture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.10.2002 08:27:27)
     * 
     * @param newApartir
     *            java.lang.String
     */
    public void setApartir(java.lang.String newApartir) {
        apartir = newApartir;
    }

    /**
     * Sets the forIdDossierInterneSplitting.
     * 
     * @param forIdDossierInterneSplitting
     *            The forIdDossierInterneSplitting to set
     */
    public void setForIdDossierInterneSplitting(String forIdDossierInterneSplitting) {
        this.forIdDossierInterneSplitting = forIdDossierInterneSplitting;
    }

    public void setForIdDossierSplitting(String forIdDossierSplitting) {
        this.forIdDossierSplitting = forIdDossierSplitting;
    }

    public void setForIdEtat(java.lang.String newForIdEtat) {
        forIdEtat = newForIdEtat;
    }

    public void setForIdTiersAssure(java.lang.String newForIdTiersAssure) {

        forIdTiersAssure = CIUtil.unFormatAVS(newForIdTiersAssure);

    }

    /**
     * @param string
     */
    public void setForIdTiersAssureNNSS(java.lang.String string) {
        forIdTiersAssureNNSS = string;
    }

    public void setForIdTiersConjoint(java.lang.String newForIdTiersConjoint) {
        forIdTiersConjoint = CIUtil.unFormatAVS(newForIdTiersConjoint);
    }

    /**
     * @param string
     */
    public void setForIdTiersConjointNNSS(java.lang.String string) {
        forIdTiersConjointNNSS = string;
    }

    /**
     * Sets the forMotif.
     * 
     * @param forMotif
     *            The forMotif to set
     */
    public void setForMotif(String forMotif) {
        this.forMotif = forMotif;
    }

    /**
     * @param string
     */
    public void setForNotIdEtat(String string) {
        forNotIdEtat = string;
    }

    public void setFromDateOuvertureDossier(java.lang.String newFromDateOuvertureDossier) {
        fromDateOuvertureDossier = newFromDateOuvertureDossier;
    }

    /**
     * Sets the fromIdDossierInterneSplitting.
     * 
     * @param fromIdDossierInterneSplitting
     *            The fromIdDossierInterneSplitting to set
     */
    public void setFromIdDossierInterneSplitting(java.lang.String fromIdDossierInterneSplitting) {
        this.fromIdDossierInterneSplitting = fromIdDossierInterneSplitting;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.02.2003 11:21:58)
     * 
     * @param newFromIdDossierSplitting
     *            java.lang.String
     */
    public void setFromIdDossierSplitting(java.lang.String newFromIdDossierSplitting) {
        fromIdDossierSplitting = newFromIdDossierSplitting;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.10.2002 10:03:23)
     * 
     * @param order
     *            java.lang.String
     */
    public void setOrderBy(String order) {
        this.order = order;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.10.2002 08:27:59)
     * 
     * @param newTri
     *            java.lang.String
     */
    public void setTri(java.lang.String newTri) {
        tri = newTri;
    }

    /**
     * Sets the untilDateOuverture.
     * 
     * @param untilDateOuverture
     *            The untilDateOuverture to set
     */
    public void setUntilDateOuverture(String untilDateOuverture) {
        this.untilDateOuverture = untilDateOuverture;
    }

}
