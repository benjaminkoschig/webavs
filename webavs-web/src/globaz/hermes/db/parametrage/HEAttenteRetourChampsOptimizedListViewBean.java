package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 15:00:13)
 * 
 * @author: ADO, ALD
 */
public class HEAttenteRetourChampsOptimizedListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        // Insérez ici le code de démarrage de l'application
        new HEAttenteRetourChampsOptimizedListViewBean().go();
    }

    private String forIdAttenteRetour = "";
    private final String HEANNOP_ARCHIVE = "HEANNOR";
    private final String HEANNOP_EN_COURS = "HEANNOP";
    private final String HEAREAP_ARCHIVE = "HEAREAR";
    private final String HEAREAP_EN_COURS = "HEAREAP";
    private boolean isArchivage = false;

    protected String likeCodeApplication = "";

    /**
     * Commentaire relatif au constructeur HEAttenteRetourChampsOptimizedListViewBean.
     */
    public HEAttenteRetourChampsOptimizedListViewBean() {
        super();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String heannop = "";
        String heareap = "";
        if (isArchivage()) {
            heannop = HEANNOP_ARCHIVE;
            heareap = HEAREAP_ARCHIVE;
        } else {
            heannop = HEANNOP_EN_COURS;
            heareap = HEAREAP_EN_COURS;
        }
        return "(SELECT R5.RNTMES AS MESSAGE, R5.IDANNONCE3 AS IDANNONCE,SUBSTR(R5.RNLENR,CAST(R5.RDNDEB AS INTEGER),CAST(R5.RDNLON AS INTEGER)) AS VALEUR, R5.* "
                + "FROM (SELECT "
                + _getCollection()
                + "HEPAREP.REIPAE, "
                + _getCollection()
                + "HECHANP.RDTCHA, "
                + _getCollection()
                + "HECHANP.RDNDEB, "
                + _getCollection()
                + "HECHANP.RDNLON, "
                + _getCollection()
                + "FWCOUP.PCOLUT,R4.RNTMES, R4.IDANNONCE3,R4.RNLENR, R4.PCOLUT, R4.PCOUID, R4.RNDDAN,R4.RNTSTA,R4.ROIARA  "
                + "FROM (SELECT R3.IDANNONCE AS IDANNONCE3,R3.*, SUBSTR(R3.RNLENR,CAST(R3.RDNDEB AS INTEGER),CAST(R3.RDNLON AS INTEGER)) AS STR_CODE_ENR, CAST(SUBSTR(R3.RNLENR,CAST(R3.RDNDEB AS INTEGER),CAST(R3.RDNLON AS INTEGER)) AS INTEGER) AS INT_CODE_ENR "
                + "FROM (SELECT "
                + "HEPAREP2.*, "
                + "HECHANP2.*, R2.IDANNONCE2,R2.* "
                + "FROM "
                + _getCollection()
                + "HECHANP AS "
                + "HECHANP2"
                + " INNER JOIN ((SELECT "
                + _getCollection()
                + "FWCOSP.PPTYGR, "
                + _getCollection()
                + "FWCOUP.PCOUID, "
                + _getCollection()
                + "FWCOUP.PCOLUT, R1.*,R1.IDANNONCE AS IDANNONCE2, "
                + _getCollection()
                + "FWCOSP.PCOSID "
                + "FROM (SELECT "
                + "HEAREAP2.*, "
                + "HEANNOP2.*, "
                + "HEANNOP2.RNIANN AS IDANNONCE, Left("
                + "HEANNOP2.RNLENR,2) AS CODEAPPLICATION "
                + "FROM "
                + _getCollection()
                + heareap
                + " AS "
                + "HEAREAP2"
                + ","
                + _getCollection()
                + heannop
                + " AS "
                + "HEANNOP2"
                + " WHERE " /*
                             * + _getCollection() + "HEAREAP.HEA_RNIANN=0 AND "
                             */
                + "HEAREAP2.ROLRUN="
                + "HEANNOP2.RNREFU) AS R1,"
                + _getCollection()
                + "FWCOSP,"
                + _getCollection()
                + "FWCOUP "
                + "WHERE "
                + _getCollection()
                + "FWCOSP.PPTYGR='HECODAPP' AND "
                + _getCollection()
                + "FWCOUP.PCOSID="
                + _getCollection()
                + "FWCOSP.PCOSID AND R1.CODEAPPLICATION="
                + _getCollection()
                + "FWCOUP.PCOUID AND "
                + _getCollection()
                + "FWCOUP.PLAIDE='"
                + getSession().getIdLangue()
                + "') AS R2 INNER JOIN "
                + _getCollection()
                + "HEPAREP AS "
                + "HEPAREP2"
                + " ON R2.PCOSID="
                + "HEPAREP2.RETLIB) ON "
                + "HECHANP2.REIPAE="
                + "HEPAREP2.REIPAE "
                + "WHERE "
                + "HECHANP2.RDICHA=2) AS R3) AS R4 INNER JOIN (("
                + _getCollection()
                + "FWCOSP INNER JOIN "
                + _getCollection()
                + "FWCOUP ON "
                + _getCollection()
                + "FWCOSP.PCOSID = "
                + _getCollection()
                + "FWCOUP.PCOSID AND "
                + _getCollection()
                + "FWCOUP.PLAIDE='"
                + getSession().getIdLangue()
                + "') INNER JOIN ("
                + _getCollection()
                + "HEPAREP INNER JOIN "
                + _getCollection()
                + "HECHANP ON "
                + _getCollection()
                + "HEPAREP.REIPAE = "
                + _getCollection()
                + "HECHANP.REIPAE) ON "
                + _getCollection()
                + "FWCOSP.PCOSID = "
                + _getCollection()
                + "HECHANP.RDTCHA) ON (R4.RETLIB = "
                + _getCollection()
                + "HEPAREP.RETLIB) AND (R4.INT_CODE_ENR = "
                + _getCollection()
                + "HEPAREP.RENCEF) AND (R4.INT_CODE_ENR = " + _getCollection() + "HEPAREP.RENCED)) AS R5) AS R6 ";
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
        return "";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdAttenteRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ROIARA=" + _dbWriteNumeric(statement.getTransaction(), getForIdAttenteRetour());
        }
        if (getLikeCodeApplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNLENR like '" + _dbWriteNumeric(statement.getTransaction(), getLikeCodeApplication()) + "%' ";
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
    public BEntity _newEntity() throws Exception {
        return new HEAttenteRetourChampsOptimizedViewBean();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 15:33:14)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAttenteRetour() {
        return forIdAttenteRetour;
    }

    /**
     * Returns the likeCodeApplication.
     * 
     * @return String
     */
    public String getLikeCodeApplication() {
        return likeCodeApplication;
    }

    public void go() {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            setSession(session);
            setForIdAttenteRetour("70");
            find();
            // System.out.println(size());
            for (int i = 0; i < size(); i++) {
                HEAttenteRetourChampsOptimizedViewBean view = (HEAttenteRetourChampsOptimizedViewBean) getEntity(i);
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
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 15:33:14)
     * 
     * @param newForIdAttenteRetour
     *            java.lang.String
     */
    public void setForIdAttenteRetour(java.lang.String newForIdAttenteRetour) {
        forIdAttenteRetour = newForIdAttenteRetour;
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
     * Sets the likeCodeApplication.
     * 
     * @param likeCodeApplication
     *            The likeCodeApplication to set
     */
    public void setLikeCodeApplication(String likeCodeApplication) {
        this.likeCodeApplication = likeCodeApplication;
    }

}
