package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.hermes.utils.StringUtils;

/**
 * Insérez la description du type ici. Date de création : (19.12.2002 11:33:22)
 * 
 * @author: Administrator
 */
public class HEAttenteRetourOptimizedListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 10:46:21)
     * 
     * @param args
     *            String[]
     */
    public static void main(String[] args) {
        new HEAttenteRetourOptimizedListViewBean().go();
    }

    protected String forDate = "";
    protected String forIdAnnonce = "";
    protected String forIdLot = "";
    private String forIdParamAnnonce = "";
    protected String forMotif = "";
    protected String forNom = "";
    protected String forNotCodeApplication = "";
    private String forNumeroAVS = "";
    private String forNumeroCaisse = "";
    protected String forReferenceUnique = "";
    protected String forStatut = "";
    protected String forTypeRetour = "";
    protected String forUserId = "";
    private final String HEANNOP_ARCHIVE = "HEANNOR";
    private final String HEANNOP_EN_COURS = "HEANNOP";
    private final String HEAREAP_ARCHIVE = "HEAREAR";
    private final String HEAREAP_EN_COURS = "HEAREAP";
    private boolean isArchivage = false;

    protected String likeNumeroAvs = "";

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 10:18:54)
     */
    public HEAttenteRetourOptimizedListViewBean() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        /*
         * return "(SELECT R3.*, SUBSTR(R3.RNLENR,CAST(R3.RDNDEB AS INTEGER),CAST(R3.RDNLON AS INTEGER)) AS NUMAVS " +
         * "FROM (SELECT DISTINCT R2.ROTATT AS RETOUR, AJPPCOU2.PCOUID AS IDRETOUR,AJPPCOU2.PCOLUT AS LIBRETOUR, " +
         * _getCollection() + "HEPAREP.*, " + _getCollection() + "HECHANP.*, R2.* " + "FROM " + _getCollection() +
         * "HEPAREP AS HEPAREP2, " + _getCollection() + "AJPPCOU AS AJPPCOU2, " + _getCollection() +
         * "HECHANP INNER JOIN ((SELECT " + _getCollection() + "AJPPCOS.PPTYGR, " + _getCollection() +
         * "AJPPCOU.PCOUID, " + _getCollection() + "AJPPCOU.PCOLUT, R1.*, " + _getCollection() + "AJPPCOS.PCOSID " +
         * "FROM (SELECT " + _getCollection() + "HEAREAP.*, " + _getCollection() + "HEANNOP.*, Left(" + _getCollection()
         * + "HEANNOP.RNLENR,2) AS CODEAPPLICATION,CAST(SUBSTR(" + _getCollection() +
         * "HEANNOP.RNLENR,3,2) AS INTEGER) AS CODEENR " + "FROM " + _getCollection() + "HEAREAP," + _getCollection() +
         * "HEANNOP WHERE " + _getCollection() /* + "HEAREAP.HEA_RNIANN=0 AND " + _getCollection()
         */
        /*
         * +"HEAREAP.RNIANN=" + _getCollection() + "HEANNOP.RNIANN) AS R1," + _getCollection() + "AJPPCOS," +
         * _getCollection() + "AJPPCOU " + "WHERE " + _getCollection() + "AJPPCOS.PPTYGR='HECODAPP' AND " +
         * _getCollection() + "AJPPCOU.PCOSID=" + _getCollection() + "AJPPCOS.PCOSID AND R1.CODEAPPLICATION=" +
         * _getCollection() + "AJPPCOU.PCOUID AND " + _getCollection() + "AJPPCOU.PLAIDE='" + getSession().getIdLangue()
         * + "') AS R2 INNER JOIN " + _getCollection() + "HEPAREP ON R2.PCOSID=" + _getCollection() +
         * "HEPAREP.RETLIB) ON " + _getCollection() + "HECHANP.REIPAE=" + _getCollection() + "HEPAREP.REIPAE " +
         * "WHERE " + _getCollection() + "HECHANP.RDTCHA=118007 AND AJPPCOU2.PLAIDE='" + getSession().getIdLangue() +
         * "' AND (R2.ROTATT=HEPAREP2.REIPAE AND HEPAREP2.RETLIB=AJPPCOU2.PCOSID) AND " + _getCollection() +
         * "HEPAREP.RENCED=CODEENR) AS R3 ) AS MAINQUERY ";
         */
        // +
        // "' AND (R2.ROTATT=HEPAREP2.REIPAE AND HEPAREP2.RETLIB=AJPPCOU2.PCOSID)) AS R3 WHERE PCOSID=111001) AS MAINQUERY ";
        if (isArchivage()) {
            return _getCollection() + HEANNOP_ARCHIVE + " AS HEANNOP," + _getCollection() + HEAREAP_ARCHIVE
                    + " AS HEAREAP";
        } else {
            return _getCollection() + HEANNOP_EN_COURS + " AS HEANNOP," + _getCollection() + HEAREAP_EN_COURS
                    + " AS HEAREAP";
        }
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
        // return "NUMAVS,SUBSTR(RNLENR,48,20),ROLRUN ";
        // return "IDRETOUR,NUMAVS,ROLRUN ";
        return "ROIARA";
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
        String sqlWhere = "";
        // traitement du positionnement
        /*
         * if (getLikeNumeroAvs().length() != 0) { if (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere +=
         * "ROAVS like " + _dbWriteString(statement.getTransaction(), getLikeNumeroAvs() + "%"); }
         */
        if (getForNotCodeApplication().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNLENR NOT LIKE "
                    + _dbWriteString(statement.getTransaction(), getForNotCodeApplication() + "%");
        }
        if (getForMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROMOT=" + _dbWriteString(statement.getTransaction(), getForMotif());
        }
        if (getForIdAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += /* _getCollection() + */"HEANNOP.RNIANN="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonce());
        }
        if (getForStatut().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNTSTA=" + _dbWriteNumeric(statement.getTransaction(), getForStatut());
        } /*
           * else { // tous sauf ceux terminés if (sqlWhere.length() != 0) { sqlWhere += " AND "; } sqlWhere +=
           * "RNTSTA<>"
           * + _dbWriteNumeric(statement.getTransaction(), HEAnnoncesViewBean.CS_TERMINE); }
           */
        if (getForUserId().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UCASE(RNLUTI)=" + _dbWriteString(statement.getTransaction(), getForUserId().toUpperCase());
        }
        if (!JAUtil.isIntegerEmpty(getForDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN=" + _dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }
        /*
         * if (!JAUtil.isStringEmpty(getForReferenceUnique())) { if (sqlWhere.length() != 0) { sqlWhere += " AND "; }
         * sqlWhere += "ROLRUN=" + _dbWriteString(statement.getTransaction(), getForReferenceUnique()); }
         */
        if (!JAUtil.isStringEmpty(getForTypeRetour())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROTATT=" + _dbWriteNumeric(statement.getTransaction(), getForTypeRetour());
        }
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ROTATT<>8 ";
        } else {
            sqlWhere += " ROTATT<>8 ";
        }
        //
        // CFCP.HEANNOP.RNIANN=13331 AND RNLENR LIKE '1101%';
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND "
            /* + _getCollection() */
            + "HEAREAP.ROLRUN='" + getForReferenceUnique() + "'  AND "
            /* + _getCollection() */
            + "HEANNOP.RNREFU='" + getForReferenceUnique() + "' AND RNLENR LIKE '1101%'";
        } else {
            sqlWhere += " "
            // + _getCollection()
                    + "HEAREAP.ROLRUN='" + getForReferenceUnique() + "'  AND "
                    // + _getCollection()
                    + "HEANNOP.RNREFU='" + getForReferenceUnique() + "' AND RNLENR LIKE '1101%'";
        }
        if (getForNumeroAVS().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNAVS=" + _dbWriteString(statement.getTransaction(), getForNumeroAVS());
        }
        if (getForNumeroCaisse().trim().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNCAIS=" + _dbWriteString(statement.getTransaction(), getForNumeroCaisse());
        }
        return sqlWhere;
    }

    @Override
    public BEntity _newEntity() {
        HEAttenteRetourOptimizedViewBean n = new HEAttenteRetourOptimizedViewBean();
        n.setIsArchivage(isArchivage());
        return n;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.01.2003 09:42:08)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDate() {
        return forDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.07.2003 13:50:52)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2003 15:05:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdLot() {
        return forIdLot;
    }

    /**
     * Returns the forIdParamAnnonce.
     * 
     * @return String
     */
    public String getForIdParamAnnonce() {
        return forIdParamAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 17:34:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForMotif() {
        return forMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.04.2003 17:21:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNom() {
        return forNom;
    }

    /**
     * Returns the forNotCodeApplication.
     * 
     * @return String
     */
    public String getForNotCodeApplication() {
        return forNotCodeApplication;
    }

    /**
     * Returns the forNumeroAVS.
     * 
     * @return String
     */
    public String getForNumeroAVS() {
        return forNumeroAVS;
    }

    /**
     * Returns the forNumeroCaisse.
     * 
     * @return String
     */
    public String getForNumeroCaisse() {
        return forNumeroCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2003 11:34:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForReferenceUnique() {
        return forReferenceUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.01.2003 09:40:54)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForStatut() {
        return forStatut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 16:08:46)
     * 
     * @return java.lang.String
     */
    public final String getForTypeRetour() {
        return forTypeRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 17:34:30)
     * 
     * @return String
     */
    public String getForUserId() {
        return forUserId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 17:34:30)
     * 
     * @return String
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    public void go() {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            setSession(session);
            find();
            for (int i = 0; i < size(); i++) {
                HEAttenteRetourOptimizedViewBean view = (HEAttenteRetourOptimizedViewBean) getEntity(i);
                System.out.println(view);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        System.exit(-1);
    }

    /**
     * Returns the isArchivage.
     * 
     * @return boolean
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.01.2003 09:42:08)
     * 
     * @param newForDate
     *            String
     */
    public void setForDate(String newForDate) {
        forDate = newForDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.07.2003 13:50:52)
     * 
     * @param newForIdAnnonce
     *            String
     */
    public void setForIdAnnonce(String newForIdAnnonce) {
        forIdAnnonce = newForIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2003 15:05:07)
     * 
     * @param newForIdLot
     *            String
     */
    public void setForIdLot(String newForIdLot) {
        forIdLot = newForIdLot;
    }

    /**
     * Method setForIdParamAnnonce.
     * 
     * @param string
     */
    public void setForIdParamAnnonce(String idParamAnnonce) {
        forIdParamAnnonce = idParamAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 17:34:30)
     * 
     * @param newForMotif
     *            String
     */
    public void setForMotif(String newForMotif) {
        forMotif = newForMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.04.2003 17:21:03)
     * 
     * @param newForNom
     *            String
     */
    public void setForNom(String newForNom) {
        forNom = newForNom;
    }

    /**
     * Method setForNotCodeApplication.
     * 
     * @param string
     */
    public void setForNotCodeApplication(String codeApplication) {
        forNotCodeApplication = codeApplication;
    }

    /**
     * Method setForNumeroAVS.
     * 
     * @param string
     */
    public void setForNumeroAVS(String numAVS) {
        forNumeroAVS = StringUtils.padAfterString(numAVS, "0", 11);
    }

    /**
     * Method setForNumeroCaisse.
     * 
     * @param string
     */
    public void setForNumeroCaisse(String numCaisse) {
        forNumeroCaisse = numCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2003 11:34:59)
     * 
     * @param newForReferenceUnique
     *            String
     */
    public void setForReferenceUnique(String newForReferenceUnique) {
        forReferenceUnique = newForReferenceUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.01.2003 09:40:54)
     * 
     * @param newForStatut
     *            String
     */
    public void setForStatut(String newForStatut) {
        forStatut = newForStatut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 16:08:46)
     * 
     * @param newForTypeRetour
     *            String
     */
    public void setForTypeRetour(String newForTypeRetour) {
        forTypeRetour = newForTypeRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 17:34:30)
     * 
     * @param newForUserId
     *            String
     */
    public void setForUserId(String newForUserId) {
        forUserId = newForUserId;
    }

    /**
     * Sets the isArchivage.
     * 
     * @param isArchivage
     *            The isArchivage to set
     */
    public void setIsArchivage(boolean isArchivage) {
        this.isArchivage = isArchivage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 17:34:30)
     * 
     * @param newLikeNumeroAvs
     *            String
     */
    public void setLikeNumeroAvs(String newLikeNumeroAvs) {
        likeNumeroAvs = newLikeNumeroAvs;
    }

}
