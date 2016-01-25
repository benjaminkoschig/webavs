package globaz.hermes.db.gestion;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.naos.application.AFApplication;
import globaz.pavo.application.CIApplication;

/**
 * Insérez la description du type ici. Date de création : (13.01.2003 16:47:57)
 * 
 * @author: Administrator
 */
public class HEOutputAnnonceListViewBean extends HEAnnoncesListViewBean implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String GROUP_BY_RNREFU = "RNREFU";
    // ******************CONSTANTES
    public static final String ORDER_BY_RNIANN = "RNIANN";

    public static String getSqlForSetDateCopy(String listeReference) throws Exception {
        if (JadeStringUtil.isEmpty(listeReference)) {
            throw new Exception("Erreur critique, on ne peut utiliser getSqlForSetDateCopy sans liste de référence ");
        }
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".HEANNOP SET RNDECP=");
        sql.append(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD));
        sql.append(" WHERE RNREFU IN(");
        sql.append(listeReference);
        sql.append(")");
        return sql.toString();
    }

    public static String getSqlForTerminateSeries(String reference) {
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".HEANNOP SET RNTSTA=117003 WHERE RNREFU ='");
        sql.append(reference);
        sql.append("'");
        // adaptation car la requête ci-dessous n'est pas correct sur certain
        // système
        // notamment sur AS400
        // sql.append(".HEANNOP SET RNTSTA=117003 WHERE RNREFU IN(");
        // sql.append("SELECT RNREFU FROM ");
        // sql.append(Jade.getInstance().getDefaultJdbcSchema());
        // sql.append(".HEANNOP h  where RNTSTA=117002 AND NOT EXISTS(SELECT * FROM ");
        // sql.append(Jade.getInstance().getDefaultJdbcSchema());
        // sql.append(".HEAREAP WHERE HEA_RNIANN=0 AND h.RNREFU=ROLRUN))");
        return sql.toString();
    }

    private String andForRefUnique2 = "";
    private String forCaOnly = "";
    private boolean forCIAdditionnel = false;
    private boolean forClotureOnly = false;
    private String forCodeApplication = "";
    private String forCodeApplicationLike = "";
    private String forCodeAppOr1 = "";
    private String forCodeAppOr2 = "";
    private boolean forCodeEnr01Or001 = false;
    private String forCodesApps = "";
    private String forDate = "";
    private boolean forDateReceptionVide = false;
    private boolean forImpressionCAOnly = false;
    private boolean forInProgress = false;
    private String forMotif = "";
    private String forMotifLettreAcc = "";
    private String forMotifLettreCA = "";
    private String[] forMotifs = null;
    private boolean forNotCiaddtionnel = false;
    private String forNotLikeCodeAppl = "";
    private String forNotLikeCodeAppl2 = "";
    private String forNotRefUnique = "";
    private String forNumCaisse = "";
    private String forNumeroAVS = "";
    private String forNumeroAvsNNSS = "";
    private boolean forOuvertureOnly = false;
    private String forRCIOnly = "false";
    private String forReferenceInterne = "";
    private String forService = "";
    private boolean forVingtOnly = false;
    private String fromDate = "";
    private String fromNumeroAVS = "";
    private boolean isNotMotifPavo = false;
    private boolean isProducteurRentes = false;
    private String likeNumeroAVS = "";
    private String likeNumeroAvsNNSS = "";

    private String order = "";

    private String untilDate = "";

    /**
     * Commentaire relatif au constructeur HEOutputAnnonceListViewBean.
     */
    public HEOutputAnnonceListViewBean() {
        super();
    }

    /**
     * Commentaire relatif au constructeur HEOutputAnnonceListViewBean.
     */
    public HEOutputAnnonceListViewBean(BSession session) {
        super();
        setSession(session);
    }

    /**
     * Renvoie la valeur de la propriété order
     * 
     * @return la valeur de la propriété order
     * @param statement
     *            statement
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Renvoie la valeur de la propriété where
     * 
     * @return la valeur de la propriété where
     * @param statement
     *            statement
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // -- composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForRefUnique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNREFU=" + _dbWriteString(statement.getTransaction(), getForRefUnique());
            if (getAndForRefUnique2().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " OR ";
                }
                sqlWhere += "RNREFU=" + _dbWriteString(statement.getTransaction(), getAndForRefUnique2());
            }
        }
        // traitement du positionnement
        if (getForNotRefUnique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNREFU<>" + _dbWriteString(statement.getTransaction(), getForNotRefUnique());
        }
        // traitement du positionnement
        if (getForIdAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNIANN=" + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonce());
        }
        // traitement du positionnement
        if (getForIdLot().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RMILOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }

        // traitement du positionnement
        if (getForCodeApplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SUBSTR(RNLENR,1,2)='" + getForCodeApplication() + "'";
        }
        // traitement du positionnement
        if (forCodeAppOr1.trim().length() != 0 && forCodeAppOr2.trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += getForCodeApplicationOR();
        }
        /*
         * if (getForCodeApplication().length() != 0) { if (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere +=
         * "RNREFU=" + _dbWriteString(statement.getTransaction(), getForRefUnique()); }
         */
        if (getLikeEnregistrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNLENR LIKE '" + getLikeEnregistrement() + "%'";
        }

        if (getForCodeEnregistrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (!isCodeApplication3839()) {
                sqlWhere += " SUBSTR(HEANNOP.RNLENR,3,2)='"
                        + JadeStringUtil.rightJustify(getForCodeEnregistrement(), 2, '0') + "'";
            } else {
                sqlWhere += " SUBSTR(HEANNOP.RNLENR,3,2)='"
                        + JadeStringUtil.rightJustify(getForCodeEnregistrement(), 3, '0') + "'";
            }
            // /* sqlWhere += "(SUBSTR(RNLENR,3,2)='0" +
            // getForCodeEnregistrement() + "' OR SUBSTR(RNLENR,3,3)='00" +
            // getForCodeEnregistrement() + "')";*/
            // sqlWhere +=
            // " (((SUBSTR(RNLENR,1,2)='38' OR SUBSTR(RNLENR,1,2)='39') AND SUBSTR(RNLENR,3,3)='"
            // +
            // globaz.hermes.utils.StringUtils.padBeforeString(getForCodeEnregistrement(),
            // "0", 3)
            // +
            // "') OR ((SUBSTR(RNLENR,1,2)<>'38' AND SUBSTR(RNLENR,1,2)<>'39') AND SUBSTR(RNLENR,3,2)='"
            // +
            // globaz.hermes.utils.StringUtils.padBeforeString(getForCodeEnregistrement(),
            // "0", 2)
            // + "')) ";
        }
        if (!JadeStringUtil.isBlank(getForService()) && getForService().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if ("2101".equals(getLikeEnregistrement())) {
                sqlWhere += " SUBSTR(RNLENR,80,4) LIKE '" + getForService() + "%'";
            } else {
                sqlWhere += " SUBSTR(RNLENR,11,4) LIKE '" + getForService() + "%'";
            }
        }
        // 98-99
        if (getForCaOnly().equals("true")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            /*
             * sqlWhere +=
             * "(SUBSTR(RNLENR,98,2)='11' OR SUBSTR(RNLENR,98,2)='21' OR SUBSTR(RNLENR,98,2)='13' OR SUBSTR(RNLENR,98,2)='15' OR SUBSTR(RNLENR,98,2)='25' OR SUBSTR(RNLENR, 98, 2) = '19' OR SUBSTR(RNLENR, 98, 2)='31' OR SUBSTR(RNLENR, 98, 2) = '41' OR SUBSTR(RNLENR, 98, 2) = '33' OR SUBSTR(RNLENR, 98, 2) = '43' OR SUBSTR(RNLENR, 98, 2) = '35')"
             * ;
             */
            /*
             * 11 21 13 15 25 19 31 41 33 43 35 71 75 81 85
             */
            sqlWhere += "( RNMOT='11' OR RNMOT='21' OR RNMOT='13' OR RNMOT='15' OR RNMOT='25' OR RNMOT='19' OR RNMOT='31' OR RNMOT='41' OR RNMOT='33' OR RNMOT='43' OR RNMOT='35')"; // OR
            // RNMOT='71'
            // OR
            // RNMOT='75'
            // OR
            // RNMOT='81'
            // OR
            // RNMOT='85')";

        }
        // Motif qui ne doive pas être imprimer dans le cas ou le producteur est
        // PAVO
        if (isNotMotifPavo()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ((RNTPRO NOT IN ('" + CIApplication.DEFAULT_APPLICATION_PAVO + "', '"
                    + AFApplication.DEFAULT_APPLICATION_NAOS + "')) OR (RNMOT NOT IN ('36', '46'))) ";
        }
        if (isProducteurRentes()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (RNTPRO IN ('HERMES', 'CORVUS')) ";
        }
        if (getForMotifLettreCA().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += getForMotifLettreCA();
        }
        if (getForMotifLettreAcc().equals("true")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "( RNMOT='11' OR RNMOT='21' OR RNMOT='13' OR RNMOT='15' OR RNMOT='25' OR RNMOT='19' OR RNMOT='31' OR RNMOT='41' OR RNMOT='33' OR RNMOT='43' OR RNMOT='35' OR RNMOT='61')"; // OR
            // RNMOT='71'
            // OR
            // RNMOT='75'
            // OR
            // RNMOT='81'
            // OR
            // RNMOT='85')";
        }

        if (isForImpressionCAOnly()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "( RNMOT='36' OR RNMOT='46' )";
        }
        if (getForNumCaisse().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNCAIS=" + _dbWriteString(statement.getTransaction(), getForNumCaisse());
        }
        if (getForMotif().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNMOT=" + _dbWriteString(statement.getTransaction(), getForMotif());
        }
        if (getForNumeroAVS().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNAVS=" + _dbWriteString(statement.getTransaction(), getForNumeroAVS());
        }
        if (getForNotLikeCodeAppl().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNLENR NOT LIKE " + _dbWriteString(statement.getTransaction(), getForNotLikeCodeAppl() + "%");
        }
        if (getForNotLikeCodeAppl2().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNLENR NOT LIKE " + _dbWriteString(statement.getTransaction(), getForNotLikeCodeAppl2() + "%");
        }
        if (getForCodeApplicationLike().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNLENR LIKE " + _dbWriteString(statement.getTransaction(), getForCodeApplicationLike() + "%");
        }
        if (getForDate().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN = " + _dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }
        if (getFromDate().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN >= " + _dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        }
        if (getUntilDate().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN <= " + _dbWriteDateAMJ(statement.getTransaction(), getUntilDate());
        }
        // traitement du positionnement
        if (getForStatut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNTSTA=" + _dbWriteNumeric(statement.getTransaction(), getForStatut());
        }
        if (getForNotStatut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEANNOP.RNTSTA<>" + _dbWriteNumeric(statement.getTransaction(), getForNotStatut());
        }
        //
        if (forInProgress) {
            // on regarde si c'est en cours
            // donc statut 117001 ou 117002
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(RNTSTA=" + _dbWriteNumeric(statement.getTransaction(), IHEAnnoncesViewBean.CS_EN_ATTENTE);
            sqlWhere += " OR RNTSTA=" + _dbWriteNumeric(statement.getTransaction(), IHEAnnoncesViewBean.CS_A_TRAITER)
                    + ") ";
        }
        if (getFromNumeroAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNAVS >="
                    + _dbWriteString(statement.getTransaction(), JadeStringUtil.change(getFromNumeroAVS(), ".", ""),
                            "Depuis le numéro AVS");
        }
        if (getLikeNumeroAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNAVS LIKE "
                    + _dbWriteString(statement.getTransaction(), JadeStringUtil.change(getLikeNumeroAVS(), ".", "")
                            + "%");
        }
        if (getForRCIOnly().equals("true")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " RNMOT >= '71' AND RNMOT <> '96' AND RNMOT <> '99'";
        }
        if (getForOuvertureOnly()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (RNMOT='21' OR RNMOT='25' OR RNMOT='41' OR RNMOT='43' OR RNMOT='61' OR RNMOT='63' OR RNMOT='65' OR RNMOT='67' OR RNMOT='81' OR RNMOT='85' OR RNMOT='68' )";
        }
        if (getForClotureOnly()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (RNMOT='71' OR RNMOT='81' OR RNMOT='75' OR RNMOT='85' OR RNMOT='79')";
        }
        if (getForVingtOnly()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SUBSTR(RNLENR,1,2) like '2%'";
        }
        if (isForCodeEnr01Or001()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(SUBSTR(RNLENR,3,3)='001' OR SUBSTR(RNLENR,3,2)='01')";
        }
        if (isForCIAdditionnel()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(SUBSTR(RNLENR,92,1)='1')";
        }
        if (isForNotCiaddtionnel()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(SUBSTR(RNLENR,92,1)='0')";
        }
        if (isForDateReceptionVide()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(HEANNOP.RNDECP = 0 OR HEANNOP.RNDECP IS NULL)";
        }
        if (getForMotifs() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (";
            for (int i = 0; i < getForMotifs().length; i++) {
                if (i > 0) {
                    sqlWhere += " OR ";
                }
                sqlWhere += "RNMOT='" + getForMotifs()[i] + "'";
            }
            sqlWhere += ")";
        }
        /**
         * ***********************************modifNNSS : suffixer le setter() ***************************
         */
        if (!JadeStringUtil.isBlankOrZero(forNumeroAVS)) {
            if ("true".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "RNBNNS ='1'";
            }
            if ("false".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "RNBNNS ='2'";
            }

        }
        if (!JadeStringUtil.isBlankOrZero(likeNumeroAVS)) {
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "RNBNNS ='1'";
            }
            if ("false".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "RNBNNS ='2'";
            }
        }

        if (!JadeStringUtil.isBlankOrZero(getForCodesApps())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (SUBSTR(RNLENR,1,2) in (" + getForCodesApps() + "))";
        }

        if (getForReferenceInterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " SUBSTR(RNLENR,11," + getForReferenceInterne().length() + ") LIKE '"
                    + getForReferenceInterne() + "%'";
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new HEOutputAnnonceViewBean(isArchivage());
    }

    /**
     * Returns the andforRefUnique2.
     * 
     * @return String
     */
    public String getAndForRefUnique2() {
        return andForRefUnique2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 10:09:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForCaOnly() {
        return forCaOnly;
    }

    /**
     * Returns the forClotureOnly.
     * 
     * @return boolean
     */
    public boolean getForClotureOnly() {
        return forClotureOnly;
    }

    /**
     * Returns the forCodeApplication.
     * 
     * @return String
     */
    public String getForCodeApplication() {
        return forCodeApplication;
    }

    /**
     * Returns the forCodeApplicationLike.
     * 
     * @return String
     */
    public String getForCodeApplicationLike() {
        return forCodeApplicationLike;
    }

    /**
     * Method setForCodeApplicationOR.
     * 
     * @param string
     * @param string1
     */
    public String getForCodeApplicationOR() {
        return "(SUBSTR(RNLENR,1,2)='" + forCodeAppOr1 + "' OR SUBSTR(RNLENR,1,2)='" + forCodeAppOr2 + "')";
    }

    public String getForCodesApps() {
        return forCodesApps;
    }

    /*
     * public String toString() { return ""+size(); }
     */
    /**
     * Returns the forDate (AAAAMMJJ).
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * Returns the forMotif.
     * 
     * @return String
     */
    @Override
    public String getForMotif() {
        return forMotif;
    }

    /**
     * @return
     */
    public String getForMotifLettreAcc() {
        return forMotifLettreAcc;
    }

    public String getForMotifLettreCA() {
        return forMotifLettreCA;
    }

    public String[] getForMotifs() {
        return forMotifs;
    }

    /**
     * Returns the forNotLikeCodeAppl.
     * 
     * @return String
     */
    public String getForNotLikeCodeAppl() {
        return forNotLikeCodeAppl;
    }

    /**
     * Returns the forNotLikeCodeAppl2.
     * 
     * @return String
     */
    public String getForNotLikeCodeAppl2() {
        return forNotLikeCodeAppl2;
    }

    /**
     * Returns the forNotRefUnique.
     * 
     * @return String
     */
    public String getForNotRefUnique() {
        return forNotRefUnique;
    }

    /**
     * Returns the forNumCaisse.
     * 
     * @return String
     */
    public String getForNumCaisse() {
        return forNumCaisse;
    }

    /**
     * Returns the forNumeroAVS.
     * 
     * @return String
     */
    public String getForNumeroAVS() {
        return forNumeroAVS;
    }

    public String getForNumeroAvsNNSS() {
        return forNumeroAvsNNSS;
    }

    /**
     * Returns the forRCIOuverture.
     * 
     * @return String
     */
    public boolean getForOuvertureOnly() {
        return forOuvertureOnly;
    }

    /**
     * Returns the forRCIOnly.
     * 
     * @return String
     */
    public String getForRCIOnly() {
        return forRCIOnly;
    }

    public String getForReferenceInterne() {
        return forReferenceInterne;
    }

    public String getForService() {
        return forService;
    }

    /**
     * Returns the forVingtOnly.
     * 
     * @return boolean
     */
    public boolean getForVingtOnly() {
        return forVingtOnly;
    }

    /**
     * Returns the fromDate.
     * 
     * @return String
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Returns the fromNumeroAVS.
     * 
     * @return String
     */
    public String getFromNumeroAVS() {
        return fromNumeroAVS;
    }

    /**
     * Returns the likeNumeroAVS.
     * 
     * @return String
     */
    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2003 15:59:32)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getOrder() {
        return order;
    }

    public String getRefUniqueCI() {
        for (int i = 0; i < size(); i++) {
            HEOutputAnnonceViewBean crt = (HEOutputAnnonceViewBean) getEntity(i);
            if (crt.getChampEnregistrement().startsWith("39001")) {
                return crt.getIdAnnonce();
            }
        }
        return "";
    }

    public String getSqlForSetCodeOrdreRevoque(String reference) throws Exception {
        if (JadeStringUtil.isEmpty(reference)) {
            throw new Exception(
                    "Erreur critique, on ne peut pas utiliser getSqlForSetCodeOrdreRevoque sans une référence ");
        }
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(_getCollection());
        sql.append("HEANNOP SET RNBREV=");
        sql.append('1');
        sql.append(" WHERE RNREFU ='");
        sql.append(reference);
        sql.append("')");
        return sql.toString();
    }

    /**
     * Returns the untilDate.
     * 
     * @return String
     */
    @Override
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * @return
     */
    public boolean isForCIAdditionnel() {
        return forCIAdditionnel;
    }

    /**
     * @return
     */
    public boolean isForCodeEnr01Or001() {
        return forCodeEnr01Or001;
    }

    public boolean isForDateReceptionVide() {
        return forDateReceptionVide;
    }

    public boolean isForImpressionCAOnly() {
        return forImpressionCAOnly;
    }

    /**
     * Returns the forInProgress.
     * 
     * @return boolean
     */
    public boolean isForInProgress() {
        return forInProgress;
    }

    /**
     * @return
     */
    public boolean isForNotCiaddtionnel() {
        return forNotCiaddtionnel;
    }

    private boolean isNotMotifPavo() {
        return isNotMotifPavo;
    }

    public boolean isProducteurRentes() {
        return isProducteurRentes;
    }

    /**
     * Sets the andforRefUnique2.
     * 
     * @param andforRefUnique2
     *            The andforRefUnique2 to set
     */
    public void setAndForRefUnique2(String andForRefUnique2) {
        this.andForRefUnique2 = andForRefUnique2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 10:09:24)
     * 
     * @param newForCaOnly
     *            java.lang.String
     */
    public void setForCaOnly(java.lang.String newForCaOnly) {
        if (newForCaOnly == null) {
            forCaOnly = "false";
        } else {
            forCaOnly = newForCaOnly;
        }
    }

    /**
     * @param b
     */
    public void setForCIAdditionnel(boolean b) {
        forCIAdditionnel = b;
    }

    /**
     * Sets the forClotureOnly.
     * 
     * @param forClotureOnly
     *            The forClotureOnly to set
     */
    public void setForClotureOnly() {
        forClotureOnly = true;
    }

    /**
     * Sets the forCodeApplication.
     * 
     * @param forCodeApplication
     *            The forCodeApplication to set
     */
    public void setForCodeApplication(String forCodeApplication) {
        this.forCodeApplication = forCodeApplication;
    }

    /**
     * Method setForCodeApplicationLike.
     * 
     * @param string
     */
    public void setForCodeApplicationLike(String codeApp) {
        forCodeApplicationLike = codeApp;
    }

    /**
     * Method setForCodeApplicationOR.
     * 
     * @param string
     * @param string1
     */
    public void setForCodeApplicationOR(String codeApp1, String codeApp2) {
        forCodeAppOr1 = codeApp1;
        forCodeAppOr2 = codeApp2;
    }

    /**
     * @param b
     */
    public void setForCodeEnr01Or001(boolean b) {
        forCodeEnr01Or001 = b;
    }

    public void setForCodesApps(String forCodesApps) {
        this.forCodesApps = forCodesApps;
    }

    /**
     * Sets the forDate (AAAAMMJJ).
     * 
     * @param forDate
     *            The forDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForDateReceptionVide(boolean forDateReceptionVide) {
        this.forDateReceptionVide = forDateReceptionVide;
    }

    public void setForImpressionCAOnly(boolean forImpressionCAOnly) {
        this.forImpressionCAOnly = forImpressionCAOnly;
    }

    /**
     * Sets the forInProgress.
     * 
     * @param forInProgress
     *            The forInProgress to set
     */
    public void setForInProgress(boolean forInProgress) {
        this.forInProgress = forInProgress;
    }

    /**
     * Sets the forMotif.
     * 
     * @param forMotif
     *            The forMotif to set
     */
    @Override
    public void setForMotif(String forMotif) {
        this.forMotif = forMotif;
    }

    /**
     * @param string
     */
    public void setForMotifLettreAcc(String string) {
        forMotifLettreAcc = string;
    }

    public void setForMotifLettreCA(String forMotifLettreCA) {
        if (forMotifLettreCA.length() != 0) {
            String[] temp = forMotifLettreCA.split(",");
            StringBuffer query = new StringBuffer();

            query.append("( ");
            for (int i = 0; i < temp.length; i++) {
                query.append("RNMOT='");
                query.append(temp[i] + "'");
                if (!(i == temp.length - 1)) {
                    query.append(" OR ");
                }
            }
            query.append(" )");

            this.forMotifLettreCA = query.toString();
        }
    }

    public void setForMotifs(String[] forMotifs) {
        this.forMotifs = forMotifs;
    }

    /**
     * @param b
     */
    public void setForNotCiaddtionnel(boolean b) {
        forNotCiaddtionnel = b;
    }

    /**
     * Sets the forNotLikeCodeAppl.
     * 
     * @param forNotLikeCodeAppl
     *            The forNotLikeCodeAppl to set
     */
    public void setForNotLikeCodeAppl(String forNotLikeCodeAppl) {
        this.forNotLikeCodeAppl = forNotLikeCodeAppl;
    }

    /**
     * Sets the forNotLikeCodeAppl2.
     * 
     * @param forNotLikeCodeAppl2
     *            The forNotLikeCodeAppl2 to set
     */
    public void setForNotLikeCodeAppl2(String forNotLikeCodeAppl2) {
        this.forNotLikeCodeAppl2 = forNotLikeCodeAppl2;
    }

    /**
     * Method setForNotRefUnique.
     * 
     * @param string
     */
    public void setForNotRefUnique(String notRefUnique) {
        forNotRefUnique = notRefUnique;
    }

    /**
     * Method setForNumCaisse.
     * 
     * @param string
     */
    public void setForNumCaisse(String caisse) {
        forNumCaisse = caisse;
    }

    /**
     * Sets the forNumeroAVS.
     * 
     * @param forNumeroAVS
     *            The forNumeroAVS to set
     */
    public void setForNumeroAVS(String forNumeroAVS) {
        this.forNumeroAVS = StringUtils.padAfterString(forNumeroAVS.trim(), "0", 11);
    }

    public void setForNumeroAvsNNSS(String forNumeroAvsNNSS) {
        this.forNumeroAvsNNSS = forNumeroAvsNNSS;
    }

    /**
     * Method setForOuvertureOnly. Renvoit que les ouvertures
     */
    public void setForOuvertureOnly() {
        forOuvertureOnly = true;
    }

    /**
     * Method setForOuvertureOnly. true: que les ouvertures false : toutes
     */
    public void setForOuvertureOnly(boolean ouverturesOnly) {
        forOuvertureOnly = ouverturesOnly;
    }

    public void setForRCIOnly() {
        forRCIOnly = "true";
    }

    public void setForReferenceInterne(String forReferenceInterne) {
        this.forReferenceInterne = forReferenceInterne;
    }

    public void setForService(String newForService) {
        forService = newForService;
    }

    /**
     * Sets the forVingtOnly.
     * 
     * @param forVingtOnly
     *            The forVingtOnly to set
     */
    public void setForVingtOnly(boolean forVingtOnly) {
        this.forVingtOnly = forVingtOnly;
    }

    /**
     * Sets the fromDate.
     * 
     * @param fromDate
     *            The fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Sets the fromNumeroAVS.
     * 
     * @param fromNumeroAVS
     *            The fromNumeroAVS to set
     */
    public void setFromNumeroAVS(String fromNumeroAVS) {
        this.fromNumeroAVS = fromNumeroAVS;
    }

    /**
     * Sets the likeNumeroAVS.
     * 
     * @param likeNumeroAVS
     *            The likeNumeroAVS to set
     */
    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAvsNNSS(String likeNumeroAvsNNSS) {
        this.likeNumeroAvsNNSS = likeNumeroAvsNNSS;
    }

    public void setNotMotifPavo(boolean isNotMotifPavo) {
        this.isNotMotifPavo = isNotMotifPavo;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2003 15:59:32)
     * 
     * @param newOrder
     *            java.lang.String
     */
    @Override
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    public void setProducteurRentes(boolean isProducteurRentes) {
        this.isProducteurRentes = isProducteurRentes;
    }

    /**
     * Sets the untilDate.
     * 
     * @param untilDate
     *            The untilDate to set
     */
    @Override
    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }
}
