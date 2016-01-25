package globaz.hermes.test;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersSystemCodeManager;

/**
 * Insérez la description du type ici. Date de création : (03.02.2003 13:43:53)
 * 
 * @author: Administrator
 */
public class HEAnnoncesTestListViewBean extends BManager implements FWListViewBeanInterface {
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
        new HEAnnoncesTestListViewBean().go();
    }

    /** (RNDDAN) */
    private String forDate = new String();
    /** Fichier HEANNOP */
    /** (RNIANN) */
    private String forIdAnnonce = new String();
    /** (RMILOT) */
    private String forIdLot = new String();

    /** (RNDDAN) */
    private String fromDate = new String();

    /**
     * Commentaire relatif au constructeur HEAnnoncesTestListViewBean.
     */
    public HEAnnoncesTestListViewBean() {
        super();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HEANNOP";
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return "RNDDAN,RNTPRO";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
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
            sqlWhere += "RMILOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        // traitement du positionnement
        if (getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN=" + _dbWriteNumeric(statement.getTransaction(), getForDate());
        }
        // traitement du positionnement
        if (getFromDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNDDAN>=" + _dbWriteNumeric(statement.getTransaction(), getFromDate());
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

        return new HEAnnoncesTestViewBean();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:49:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDate() {
        return forDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:49:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:49:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdLot() {
        return forIdLot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:49:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDate() {
        return fromDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:05:18)
     * 
     * @return java.lang.String
     */
    public void go() {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            session.setIdLangue("D");
            FWParametersSystemCodeManager motifsListe = new FWParametersSystemCodeManager();
            motifsListe.setSession(session);
            motifsListe.setForIdGroupe("HEMOTIFS");
            motifsListe.setForIdTypeCode("11100002");
            motifsListe.find();
            // System.out.println(motifsListe.size());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        System.exit(-1);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:49:47)
     * 
     * @param newForDate
     *            java.lang.String
     */
    public void setForDate(java.lang.String newForDate) {
        forDate = newForDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:49:47)
     * 
     * @param newForIdAnnonce
     *            java.lang.String
     */
    public void setForIdAnnonce(java.lang.String newForIdAnnonce) {
        forIdAnnonce = newForIdAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:49:47)
     * 
     * @param newForIdLot
     *            java.lang.String
     */
    public void setForIdLot(java.lang.String newForIdLot) {
        forIdLot = newForIdLot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.02.2003 13:49:47)
     * 
     * @param newFromDate
     *            java.lang.String
     */
    public void setFromDate(java.lang.String newFromDate) {
        fromDate = newFromDate;
    }
}
