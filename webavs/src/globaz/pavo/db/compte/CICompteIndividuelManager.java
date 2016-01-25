package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIAffilie;
import globaz.pavo.util.CIAffilieManager;
import globaz.pavo.util.CIUtil;

/**
 * Manager de l'objet <tt>CICompteIndividuel</tt>. Date de création : (12.11.2002 12:45:31)
 * 
 * @author: David Girardin
 */
public class CICompteIndividuelManager extends BManager {

    private static final long serialVersionUID = 8090872241156443009L;

    private String forCompteIndividuelReferenceId = new String();

    private Boolean forCompteIndividuelReferenceMaitre = new Boolean(false);
    private String forEtat = new String();

    private String forInIdAffiliation = new String();
    private String forNNSS = "";
    /** (KALNOM) */
    private String forNomPrenom = new String();
    private String forNotCompteIndividuelId = new String();
    /** (KANAVS) */
    private String forNumeroAvs = new String();
    private String forNumeroAvsNNSS = "";

    /** (KAIREG) */
    private String forRegistre = new String();
    /** (KAISEX) */
    private String forSexe = new String();
    /** (KADNAI) */
    private String fromDateNaissance = new String();
    private String fromNomAffilie = new String();
    /** (KALNOM) */
    private String fromNomPrenom = new String();
    // affilié
    private String fromNumeroAffilie = new String();
    /** (KANAVS) */
    private String fromNumeroAvs = new String();
    /** (KANAVA) */
    private String fromNumeroAvsAncien = new String();
    /** Dernier employeur **/
    private String likeInIdAffiliation = new String();
    /** afffilié */
    private String likeNumeroAffilie = new String();

    private String likeNumeroAvs = new String();
    /** NNSS */
    private String likeNumeroAvsNNSS = "";
    // private Boolean forCiOuvert = null;
    private java.lang.String order = new String();
    // flag order by avs
    public boolean orderByAvs = true;
    private boolean quickSearch = false;
    // recherche
    private String untilNumeroAvs = new String();

    /**
     * Initialisation du manager. Date de création : (22.11.2002 09:18:43)
     */
    public CICompteIndividuelManager() {
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {

        if (orderByAvs) {
            setOrderBy("KANAVS");
        } else if (getLikeNumeroAvs() != null && getLikeNumeroAvs().trim().length() > 0) {
            setOrderBy("KANAVS");
        } else if (getFromDateNaissance() != null && getFromDateNaissance().trim().length() > 0) {
            setOrderBy("KADNAI");
        }
        if (getFromNomPrenom() != null && getFromNomPrenom().trim().length() > 0) {
            // order by
            setOrderBy("KALNOM");
        }
        if (fromNumeroAffilie.length() != 0 && fromNumeroAffilie.indexOf('.') != -1) {
            try {
                CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                String in = app.getAffiliesByNo(getSession(), fromNumeroAffilie);
                if (in.length() > 0) {
                    fromNumeroAffilie = in;
                } else {
                    fromNumeroAffilie = "0";
                }
            } catch (Exception ex) {
                fromNumeroAffilie = "0";
            }
        }
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = _getCollection() + "CIINDIP AS CIINDIP";
        return from;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    @Override
    protected java.lang.String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer("SELECT ");
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(" FROM ");
            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(" WHERE ");
                sqlBuffer.append(sqlWhere);
            }
            String sqlOrder = _getOrder(statement);
            if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                sqlBuffer.append(" ORDER BY ");
                sqlBuffer.append(sqlOrder);
            }
            // sqlBuffer.append(" fetch first "+(getContainerSize() !=
            // Integer.MAX_VALUE ? getContainerSize() : 1000)+" rows only");
            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        CIApplication ciApp = null;
        try {
            ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KANAVS=" + _dbWriteString(statement.getTransaction(), getForNumeroAvs());
        }
        // traitement du positionnement
        if (getForRegistre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KAIREG=" + _dbWriteNumeric(statement.getTransaction(), getForRegistre());
        }
        if (!JadeStringUtil.isBlank(getForEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KABOUV= '" + forEtat + "' ";
        }
        // traitement du positionnement
        if (getForCompteIndividuelReferenceId().length() != 0 || getForCompteIndividuelReferenceMaitre().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForCompteIndividuelReferenceId().length() != 0
                    && getForCompteIndividuelReferenceMaitre().booleanValue()) {
                sqlWhere += "(KAIINR="
                        + _dbWriteNumeric(statement.getTransaction(), getForCompteIndividuelReferenceId())
                        + " OR KAIINR=KAIIND)";
            } else if (getForCompteIndividuelReferenceId().length() != 0) {
                sqlWhere += "KAIINR="
                        + _dbWriteNumeric(statement.getTransaction(), getForCompteIndividuelReferenceId());
            } else if (getForCompteIndividuelReferenceMaitre().booleanValue()) {
                sqlWhere += "KAIINR=KAIIND";
            }
        }
        // traitement du positionnement
        if (getForNotCompteIndividuelId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KAIIND<>" + _dbWriteNumeric(statement.getTransaction(), getForNotCompteIndividuelId());
        }
        // traitement du positionnement
        if (getFromNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KANAVS>=" + _dbWriteString(statement.getTransaction(), getFromNumeroAvs());
        }
        // traitement du positionnement
        if (getUntilNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KANAVS<=" + _dbWriteString(statement.getTransaction(), getUntilNumeroAvs());
        }
        // traitement du positionnement
        if (getFromNomPrenom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            int pos = getFromNomPrenom().indexOf(',');
            if (pos == -1) {
                sqlWhere += "KALNOM like " + _dbWriteString(statement.getTransaction(), getFromNomPrenom() + "%");
            } else {
                sqlWhere += "KALNOM like "
                        + _dbWriteString(statement.getTransaction(), getFromNomPrenom().substring(0, pos) + "%,"
                                + getFromNomPrenom().substring(pos + 1) + "%");
            }
        }
        // traitement du positionnement
        if (getForNomPrenom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KALNOM=" + _dbWriteString(statement.getTransaction(), getForNomPrenom());
        }
        // traitement du positionnement
        if (getFromDateNaissance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KADNAI>="
                    + _dbWriteNumeric(statement.getTransaction(), CIUtil.formatDateAMJ(getFromDateNaissance()))
                    + " AND KADNAI<"
                    + _dbWriteNumeric(statement.getTransaction(), CIUtil.formatDateAMJInc(getFromDateNaissance()));
        }
        // traitement du positionnement
        if (getFromNumeroAvsAncien().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KANAVP>=" + _dbWriteString(statement.getTransaction(), getFromNumeroAvsAncien());
        }
        // traitement du positionnement
        if (getForSexe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KATSEX=" + _dbWriteNumeric(statement.getTransaction(), getForSexe());
        }
        // traitement du positionnement
        if (getFromNumeroAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String numAff = getFromNumeroAffilie();
            if (numAff != null && numAff.indexOf('.') == -1) {
                try {
                    IFormatData affilieFormater = ciApp.getAffileFormater();
                    if (affilieFormater != null) {
                        numAff = affilieFormater.format(numAff);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sqlWhere += "KAIEMP in (" + numAff + ")";
        }
        if (getLikeNumeroAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF like " + _dbWriteString(statement.getTransaction(), getLikeNumeroAffilie() + "%");

        }

        if (getLikeInIdAffiliation().length() != 0) {

            if (likeInIdAffiliation != null && likeInIdAffiliation.indexOf('.') == -1) {
                try {
                    IFormatData affilieFormater = ciApp.getAffileFormater();
                    if (affilieFormater != null) {
                        likeInIdAffiliation = affilieFormater.format(likeInIdAffiliation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String chaineAtraiter = new String();
            CIAffilieManager affMgr = new CIAffilieManager();
            affMgr.setSession(getSession());
            affMgr.setLikeAffilieNumero(getLikeInIdAffiliation());
            try {
                affMgr.find();
            } catch (Exception e) {
            }
            for (int i = 0; i < affMgr.size(); i++) {
                if (JadeStringUtil.isBlank(chaineAtraiter)) {
                    chaineAtraiter = chaineAtraiter + ((CIAffilie) affMgr.getEntity(i)).getAffiliationId();
                } else {
                    chaineAtraiter = chaineAtraiter + "," + " " + ((CIAffilie) affMgr.getEntity(i)).getAffiliationId();
                }
            }
            if (!JadeStringUtil.isBlank(chaineAtraiter)) {
                if (!JadeStringUtil.isBlank(sqlWhere)) {
                    sqlWhere += " AND ";
                }
                sqlWhere += " KAIEMP IN (" + chaineAtraiter + " ) ";
            }

        }

        if (getForInIdAffiliation().length() != 0) {
            String chaineAtraiter = new String();
            CIAffilieManager affMgr = new CIAffilieManager();
            affMgr.setSession(getSession());
            affMgr.setForAffilieNumero(getForInIdAffiliation());
            try {
                affMgr.find();
            } catch (Exception e) {
            }
            for (int i = 0; i < affMgr.size(); i++) {
                if (JadeStringUtil.isBlank(chaineAtraiter)) {
                    chaineAtraiter = chaineAtraiter + ((CIAffilie) affMgr.getEntity(i)).getAffiliationId();
                } else {
                    chaineAtraiter = "," + " " + chaineAtraiter + ((CIAffilie) affMgr.getEntity(i)).getAffiliationId();
                }
            }
            if (!JadeStringUtil.isBlank(chaineAtraiter)) {
                if (!JadeStringUtil.isBlank(sqlWhere)) {
                    sqlWhere += " AND ";
                }
                sqlWhere += " KAIEMP IN (" + _dbWriteNumeric(statement.getTransaction(), chaineAtraiter) + " ) ";
            }

        }
        // traitement du positionnement

        if (getLikeNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KANAVS like '" + _dbWriteNumeric(statement.getTransaction(), getLikeNumeroAvs());
            sqlWhere += "%'";
        }

        /************************************* modifNNSS : suffixer le setter() ****************************/
        if (!JadeStringUtil.isBlankOrZero(forNumeroAvs)) {
            if ("true".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KABNNS ='1'";
            }
            if ("false".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KABNNS ='2'";
            }

        }
        if (!JadeStringUtil.isBlankOrZero(likeNumeroAvs)) {
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KABNNS ='1'";
            }
            if ("false".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KABNNS ='2'";
            }
        }
        if (!JadeStringUtil.isBlank(getForNNSS())) {
            if ("true".equalsIgnoreCase(getForNNSS().trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KABNNS ='1'";
            }
            if ("false".equalsIgnoreCase(getForNNSS().trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KABNNS ='2'";
            }
        }

        return sqlWhere;
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CICompteIndividuel();
    }

    /**
     * Retourne le CI en fonction du numéro avs donné s'il existe au registre des assurés. Date de création :
     * (09.12.2002 11:19:03)
     * 
     * @param avs
     *            le numéro avs (format sans point de séparation)
     * @param transaction
     *            la transaction à utiliser.
     * @return le CI de l'assuré ou null si non trouvé.
     */
    public CICompteIndividuel getCIRegistreAssures(String avs, BTransaction transaction) {
        if (JadeStringUtil.isBlankOrZero(avs)) {
            return null;
        }
        setForNumeroAvs(avs);
        setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        try {
            find(transaction);
            if (size() == 0) {
                // pas trouvé
                return null;
            } else {
                // trouvé
                return (CICompteIndividuel) getEntity(0);
            }
        } catch (Exception ex) {
            // si exception, retourne null
            return null;
        }
    }

    public java.lang.String getForCompteIndividuelReferenceId() {
        return forCompteIndividuelReferenceId;
    }

    /**
     * Returns the forCompteIndividuelReferenceMaitre.
     * 
     * @return Boolean
     */
    public Boolean getForCompteIndividuelReferenceMaitre() {
        return forCompteIndividuelReferenceMaitre;
    }

    /**
     * @return
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * Returns the forInIdAffiliation.
     * 
     * @return String
     */
    public String getForInIdAffiliation() {
        return forInIdAffiliation;
    }

    public String getForNNSS() {
        return forNNSS;
    }

    public java.lang.String getForNomPrenom() {
        return forNomPrenom;
    }

    public java.lang.String getForNotCompteIndividuelId() {
        return forNotCompteIndividuelId;
    }

    public String getForNumeroAvs() {
        return forNumeroAvs;
    }

    public String getForNumeroAvsNNSS() {
        return forNumeroAvsNNSS;
    }

    public String getForRegistre() {
        return forRegistre;
    }

    public java.lang.String getForSexe() {
        return forSexe;
    }

    public String getFromDateNaissance() {
        return fromDateNaissance;
    }

    public java.lang.String getFromNomAffilie() {
        return fromNomAffilie;
    }

    public String getFromNomPrenom() {
        return fromNomPrenom;
    }

    public java.lang.String getFromNumeroAffilie() {
        return fromNumeroAffilie;
    }

    public String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    public String getFromNumeroAvsAncien() {
        return fromNumeroAvsAncien;
    }

    /**
     * @return
     */
    public String getLikeInIdAffiliation() {
        return likeInIdAffiliation;
    }

    /**
     * Returns the likeNumeroAffilie.
     * 
     * @return String
     */
    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    public java.lang.String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    public String getLikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
    }

    public boolean getQuickSearch() {
        return quickSearch;
    }

    public java.lang.String getUntilNumeroAvs() {
        return untilNumeroAvs;
    }

    /**
     * Sets the orderByAvs.
     * 
     * @param orderByAvs
     *            The orderByAvs to set
     */
    public void orderByAvs(boolean orderByAvs) {
        this.orderByAvs = orderByAvs;
    }

    public void setForCompteIndividuelReferenceId(java.lang.String newForCompteIndividuelReferenceId) {
        forCompteIndividuelReferenceId = newForCompteIndividuelReferenceId;
    }

    /**
     * Sets the forCompteIndividuelReferenceMaitre.
     * 
     * @param forCompteIndividuelReferenceMaitre
     *            The forCompteIndividuelReferenceMaitre to set
     */
    public void setForCompteIndividuelReferenceMaitre(Boolean forCompteIndividuelReferenceMaitre) {
        this.forCompteIndividuelReferenceMaitre = forCompteIndividuelReferenceMaitre;
    }

    /**
     * @param string
     */
    public void setForEtat(String string) {
        forEtat = string;
    }

    /**
     * Sets the forInIdAffiliation.
     * 
     * @param forInIdAffiliation
     *            The forInIdAffiliation to set
     */
    public void setForInIdAffiliation(String forInIdAffiliation) {

        this.forInIdAffiliation = forInIdAffiliation;
    }

    public void setForNNSS(String forNNSS) {
        this.forNNSS = forNNSS;
    }

    public void setForNomPrenom(java.lang.String newForNomPrenom) {
        forNomPrenom = newForNomPrenom;
    }

    public void setForNotCompteIndividuelId(java.lang.String newForNotCompteIndividuelId) {
        forNotCompteIndividuelId = newForNotCompteIndividuelId;
    }

    public void setForNumeroAvs(String newForNumeroAvs) {
        forNumeroAvs = newForNumeroAvs;
    }

    public void setForNumeroAvsNNSS(String string) {
        forNumeroAvsNNSS = string;
    }

    public void setForRegistre(String newForRegistre) {
        forRegistre = newForRegistre;
    }

    public void setForSexe(java.lang.String newForSexe) {
        forSexe = newForSexe;
    }

    public void setFromDateNaissance(String newFromDateNaissance) {
        fromDateNaissance = newFromDateNaissance;
    }

    public void setFromNomAffilie(java.lang.String newFromNomAffilie) {
        fromNomAffilie = newFromNomAffilie;
    }

    public void setFromNomPrenom(String newFromNomPrenom) {
        fromNomPrenom = newFromNomPrenom.toUpperCase();
    }

    public void setFromNumeroAffilie(java.lang.String newFromNumeroAffilie) {
        fromNumeroAffilie = newFromNumeroAffilie;
    }

    public void setFromNumeroAvs(String newFromNumeroAvs) {
        fromNumeroAvs = CIUtil.unFormatAVS(newFromNumeroAvs);
    }

    public void setFromNumeroAvsAncien(String newFromNumeroAvsAncien) {
        fromNumeroAvsAncien = CIUtil.unFormatAVS(newFromNumeroAvsAncien);
    }

    public void setLikeInIdAffiliation(String likeInIdAffiliation) {
        this.likeInIdAffiliation = likeInIdAffiliation;
    }

    /**
     * Sets the likeNumeroAffilie.
     * 
     * @param likeNumeroAffilie
     *            The likeNumeroAffilie to set
     */
    public void setLikeNumeroAffilie(String likeNumeroAffilie) {
        this.likeNumeroAffilie = likeNumeroAffilie;
    }

    public void setLikeNumeroAvs(java.lang.String newLikeNumeroAvs) {
        likeNumeroAvs = CIUtil.unFormatAVS(newLikeNumeroAvs);
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvsNNSS(String string) {
        likeNumeroAvsNNSS = string;
    }

    /**
     * Applique une nouvelle clause order by. Date de création : (21.10.2002 10:03:23)
     * 
     * @param order
     *            la nouvelle clause
     */
    public void setOrderBy(String neworder) {
        if (order == null || order.length() == 0) {
            order = neworder;
        } else {
            order += ", " + neworder;
        }
    }

    public void setQuickSearch(boolean quick) {
        quickSearch = quick;
    }

    public void setUntilNumeroAvs(java.lang.String newUntilNumeroAvs) {
        untilNumeroAvs = newUntilNumeroAvs;
    }

}
