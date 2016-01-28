package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (21.03.2003 10:25:11)
 * 
 * @author: Administrator
 */
public class HEAttenteEnvoiListViewBean extends BManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeApplication = "";
    private String forDate = "";
    private String forIdAnnonce = "";
    private String forIdLot = "";
    private String forMotif = "";
    private String forNotStatut = "";
    private String forNumeroAffilie = "";
    private String forNumeroAVS = "";
    private String forNumeroAvsNNSS = "";
    private String forReferenceUnique = "";
    private String forService = "";
    private String forStatut = "";
    private String forUserId = "";
    private String fromNomPrenom = "";
    protected final String HEANNOP_ARCHIVE = "HEANNOR";
    protected final String HEANNOP_EN_COURS = "HEANNOP";
    protected final String HEINCOP_ARCHIVE = "HEINCOR";
    protected final String HEINCOP_EN_COURS = "HEINCOP";
    protected final String HELOTSP_ARCHIVE = "HELOTSR";
    protected final String HELOTSP_EN_COURS = "HELOTSP";
    protected String isArchivage = "false";
    private String likeNumeroAvs = "";
    private String likeNumeroAvsNNSS = "";
    private String likeRefUnique = "";

    /**
     * Commentaire relatif au constructeur HEAttenteEnvoiListViewBean.
     */
    public HEAttenteEnvoiListViewBean() {
        super();
    }

    /**
     * Commentaire relatif au constructeur HEAttenteEnvoiListViewBean.
     */
    public HEAttenteEnvoiListViewBean(BSession session) {
        super();
        setSession(session);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String from = "";
        if (getForNumeroAffilie().trim().length() != 0) {
            // from += ",";
            if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
                // from += this._getCollection() + this.HEINCOP_ARCHIVE;
                from += getFromAffilie(_getCollection(), HEINCOP_ARCHIVE, HELOTSP_ARCHIVE, HEANNOP_ARCHIVE);
            } else {
                // from += this._getCollection() + this.HEINCOP_EN_COURS;
                from += getFromAffilie(_getCollection(), HEINCOP_EN_COURS, HELOTSP_EN_COURS, HEANNOP_EN_COURS);
            }
        } else {

            if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
                from += _getCollection() + HEANNOP_ARCHIVE + " P2 INNER JOIN " + _getCollection() + HELOTSP_ARCHIVE;
                from += " ON P2.RMILOT=" + _getCollection() + HELOTSP_ARCHIVE + ".RMILOT";
            } else {
                from += _getCollection() + HEANNOP_EN_COURS + " P2 INNER JOIN " + _getCollection() + HELOTSP_EN_COURS;
                from += " ON P2.RMILOT=" + _getCollection() + HELOTSP_EN_COURS + ".RMILOT";
            }
        }
        return from;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement le statement
     * @return String ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        // return "NUMAVS,NOM";
        // if (Boolean.valueOf(this.getIsArchivage()).booleanValue()) {
        // return this._getCollection() + this.HEANNOP_ARCHIVE + ".RNAVS," + this._getCollection()
        // + this.HEANNOP_ARCHIVE + ".RNDDAN," + this._getCollection() + this.HEANNOP_ARCHIVE + ".RNIANN";
        // } else {
        // return this._getCollection() + this.HEANNOP_EN_COURS + ".RNAVS," + this._getCollection()
        // + this.HEANNOP_EN_COURS + ".RNDDAN," + this._getCollection() + this.HEANNOP_EN_COURS + ".RNIANN";
        // }
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // choisir les tables avec lesquelles ont va travailler
        String heannop = "";
        String helot = "";
        String heincp = "";
        if (Boolean.valueOf(getIsArchivage()).booleanValue()) {
            heannop = HEANNOP_ARCHIVE;
            helot = HELOTSP_ARCHIVE;
            heincp = HEINCOP_ARCHIVE;
        } else {
            heannop = HEANNOP_EN_COURS;
            helot = HELOTSP_EN_COURS;
            heincp = HEINCOP_EN_COURS;
        }
        String sqlWhere = "";
        // traitement du positionnement
        if (getLikeNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "P2.RNAVS like "
                    + this._dbWriteString(statement.getTransaction(), StringUtils.removeDots(getLikeNumeroAvs()) + "%");
        }
        if (getForMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "P2.RNMOT=" + this._dbWriteString(statement.getTransaction(), getForMotif());
        }
        if (getForStatut().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " P2.RNTSTA=" + this._dbWriteNumeric(statement.getTransaction(), getForStatut());
        }
        if (getForNotStatut().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " P2.RNTSTA<>" + this._dbWriteNumeric(statement.getTransaction(), getForNotStatut());
        }
        /*
         * else { // tous sauf ceux terminés if (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere += "RNTSTA<>"
         * + _dbWriteNumeric(statement.getTransaction(), HEAnnoncesViewBean.CS_TERMINE); }
         */
        if (getForNumeroAffilie().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " TYPEINCO=112502 AND";
            sqlWhere += " LIBINCO=" + this._dbWriteString(statement.getTransaction(), getForNumeroAffilie());
        }
        if (getForUserId().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " P2.RNLUTI=" + this._dbWriteString(statement.getTransaction(), getForUserId().toUpperCase());
        }
        if (!JAUtil.isIntegerEmpty(getForDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " P2.RNDDAN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }
        if (!JAUtil.isStringEmpty(getForReferenceUnique())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " P2.RNREFU=" + this._dbWriteString(statement.getTransaction(), getForReferenceUnique());
        }
        if (!JAUtil.isStringEmpty(getForIdLot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + helot + ".RMILOT="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }

        if (!JAUtil.isStringEmpty(getForNumeroAVS())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "P2.RNAVS="
                    + this._dbWriteString(statement.getTransaction(), StringUtils.removeDots(getForNumeroAVS()),
                            "numero avs");
        }
        if (getForIdAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "P2.RNIANN=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAnnonce());
        }
        if (getLikeRefUnique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "P2.RNREFU like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getLikeRefUnique() + "%");
        }
        // le service, sous string de l'enregistrement
        if (!JAUtil.isStringEmpty(getForService())) {
            sqlWhere = getForServiceWhere(statement, sqlWhere);
        }
        // que les envois
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += getEnregistrementLike(statement);
        // if (sqlWhere.length() != 0) {
        // sqlWhere += " AND ";
        // }
        // sqlWhere += this._getCollection() + heannop + ".RMILOT=" + this._getCollection() + helot + ".RMILOT";
        if (getTypeLot().length > 0) {
            sqlWhere += " AND (";
            for (int i = 0; i < getTypeLot().length; i++) {
                if (i > 0) {
                    sqlWhere += " OR ";
                }
                sqlWhere += " RMTTYP=" + getTypeLot()[i];
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
                sqlWhere += "P2.RNBNNS ='1'";
            }
            if ("false".equalsIgnoreCase(forNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "P2.RNBNNS ='2'";
            }

        }
        if (!JadeStringUtil.isBlankOrZero(likeNumeroAvs)) {
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "P2.RNBNNS ='1'";
            }
            if ("false".equalsIgnoreCase(likeNumeroAvsNNSS.trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "P2.RNBNNS ='2'";
            }
        }
        // décortiquer l'enregistrement 120 afin d'extraire l'état nominatif
        if (!JadeStringUtil.isBlankOrZero(fromNomPrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "P2.RNLENR like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getFromNomPrenom() + "%");
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    public BEntity _newEntity() throws Exception {
        HEAttenteEnvoiViewBean n = new HEAttenteEnvoiViewBean();
        n.setIsArchivage(Boolean.valueOf(getIsArchivage()).booleanValue());
        return n;
    }

    /**
     * Method getEnregistrementLike.
     * 
     * @return String
     */
    protected String getEnregistrementLike(BStatement statement) {
        StringBuffer s = new StringBuffer("(P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "1101%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "39001%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "7201%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "7301%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "7401%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "7501%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8101%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8F01%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8501%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8G01%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8A001%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8B001%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8C001%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8D001%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "8E001%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4101%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4102%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4201%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4301%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4302%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4401%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4402%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4501%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4601%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "4602%"));
        s.append(" OR P2.RNLENR LIKE ");
        s.append(this._dbWriteString(statement.getTransaction(), "6101%"));
        s.append(")");
        return s.toString();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.05.2003 09:58:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForCodeApplication() {
        return forCodeApplication;
    }

    /**
     * Returns the forDate.
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * Returns the forIdAnnonce.
     * 
     * @return String
     */
    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * Returns the forIdLot.
     * 
     * @return String
     */
    public String getForIdLot() {
        return forIdLot;
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
     * Insérez la description de la méthode ici. Date de création : (14.05.2003 11:11:55)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNotStatut() {
        return forNotStatut;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
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
     * Returns the forReferenceUnique.
     * 
     * @return String
     */
    public String getForReferenceUnique() {
        return forReferenceUnique;
    }

    /**
     * @return
     */
    public String getForService() {
        return forService;
    }

    /**
     * @return
     */
    protected String getForServiceWhere(BStatement statement, String sqlWhere) {
        try {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // start caractere
            sqlWhere += "SUBSTR(P2.RNLENR,11,4) like "
                    + this._dbWriteString(statement.getTransaction(), getForService().toUpperCase() + "%");
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }
        return sqlWhere;

    }

    /**
     * Returns the forStatut.
     * 
     * @return String
     */
    public String getForStatut() {
        return forStatut;
    }

    /**
     * Returns the forUserId.
     * 
     * @return String
     */
    public String getForUserId() {
        return forUserId;
    }

    private String getFromAffilie(String shema, String heincop, String helotsp, String heannop) {
        StringBuffer from = new StringBuffer();
        from.append(shema + heincop + " INC ");
        from.append(" INNER JOIN " + shema + heannop + " P1 ON (INC.RNIANN=P1.RNIANN)");
        from.append(" INNER JOIN " + shema + heannop + " P2 ON (P1.RNREFU=P2.RNREFU)");
        from.append(" INNER JOIN " + shema + helotsp + " LO ON (P2.RMILOT=LO.RMILOT)");

        return from.toString();
    }

    public String getFromNomPrenom() {
        return fromNomPrenom;
    }

    /**
     * Returns the isArchivage.
     * 
     * @return String
     */
    public String getIsArchivage() {
        return isArchivage;
    }

    /**
     * Returns the likeNumeroAvs.
     * 
     * @return String
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    public String getLikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
    }

    public String getLikeRefUnique() {

        return likeRefUnique;
    }

    /**
     * Method getTypeLot.
     * 
     * @return String
     */
    protected String[] getTypeLot() {
        return HELotViewBean.getLotEnvoi();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.05.2003 09:58:56)
     * 
     * @param newForCodeApplication java.lang.String
     */
    public void setForCodeApplication(java.lang.String newForCodeApplication) {
        forCodeApplication = newForCodeApplication;
    }

    /**
     * Sets the forDate.
     * 
     * @param forDate The forDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    /**
     * Sets the forIdAnnonce.
     * 
     * @param forIdAnnonce The forIdAnnonce to set
     */
    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    /**
     * Sets the forIdLot.
     * 
     * @param forIdLot The forIdLot to set
     */
    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    /**
     * Sets the forMotif.
     * 
     * @param forMotif The forMotif to set
     */
    public void setForMotif(String forMotif) {
        this.forMotif = forMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.05.2003 11:11:55)
     * 
     * @param newForNotStatut java.lang.String
     */
    public void setForNotStatut(java.lang.String newForNotStatut) {
        forNotStatut = newForNotStatut;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * Sets the forNumeroAVS.
     * 
     * @param forNumeroAVS The forNumeroAVS to set
     */
    public void setForNumeroAVS(String forNumeroAVS) {
        this.forNumeroAVS = forNumeroAVS;
    }

    public void setForNumeroAvsNNSS(String forNumeroAvsNNSS) {
        this.forNumeroAvsNNSS = forNumeroAvsNNSS;
    }

    /**
     * Sets the forReferenceUnique.
     * 
     * @param forReferenceUnique The forReferenceUnique to set
     */
    public void setForReferenceUnique(String forReferenceUnique) {
        this.forReferenceUnique = forReferenceUnique;
    }

    /**
     * @param string
     */
    public void setForService(String string) {
        forService = string;
    }

    /**
     * Sets the forStatut.
     * 
     * @param forStatut The forStatut to set
     */
    public void setForStatut(String forStatut) {
        this.forStatut = forStatut;
    }

    /**
     * Sets the forUserId.
     * 
     * @param forUserId The forUserId to set
     */
    public void setForUserId(String forUserId) {
        this.forUserId = forUserId;
    }

    public void setFromNomPrenom(String fromNomPrenom) {
        this.fromNomPrenom = fromNomPrenom;
    }

    /**
     * Sets the isArchivage.
     * 
     * @param isArchivage The isArchivage to set
     */
    public void setIsArchivage(String isArchivage) {
        this.isArchivage = isArchivage;
    }

    /**
     * Sets the likeNumeroAvs.
     * 
     * @param likeNumeroAvs The likeNumeroAvs to set
     */
    public void setLikeNumeroAvs(String likeNumeroAvs) {
        likeNumeroAvs = StringUtils.removeDots(likeNumeroAvs);
        likeNumeroAvs = StringUtils.removeChar(likeNumeroAvs, '?');
        this.likeNumeroAvs = likeNumeroAvs;
    }

    public void setLikeNumeroAvsNNSS(String likeNumeroAvsNNSS) {
        this.likeNumeroAvsNNSS = likeNumeroAvsNNSS;
    }

    /**
     * @param string
     */
    public void setLikeRefUnique(String string) {
        likeRefUnique = string;
    }

}
