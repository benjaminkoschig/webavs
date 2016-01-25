package globaz.hermes.test;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (20.01.2003 12:56:16)
 * 
 * @author: Administrator
 */
public class HERetourAnnonce_AnnonceListViewBean extends BManager implements FWListViewBeanInterface {
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
        // Ins�rez ici le code de d�marrage de l'application
        new HERetourAnnonce_AnnonceListViewBean().go();
    }

    private String forIdAnnonceRetour = "";

    /**
     * Commentaire relatif au constructeur HERetourAnnonce_AnnonceListViewBean.
     */
    public HERetourAnnonce_AnnonceListViewBean() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return "SELECT HEPAREP.*, HECHANP.*, R2.* "
                + "FROM HECHANP INNER JOIN ((SELECT FWCOSP.PPTYGR, FWCOUP.PCOUID, FWCOUP.PCOLUT, R1.*, FWCOSP.PCOSID  "
                + "FROM (SELECT HEAREAP.ROIARA, HEANNOP.*, Left(HEANNOP.RNLENR,2) AS CODEAPPLICATION  "
                + "FROM HEAREAP,HEANNOP WHERE HEAREAP.HEA_RNIANN=0 AND HEAREAP.RNIANN=HEANNOP.RNIANN) AS R1,FWCOSP,FWCOUP "
                + "WHERE FWCOSP.PPTYGR='HECODAPP' AND FWCOUP.PCOSID=FWCOSP.PCOSID AND R1.CODEAPPLICATION=AJPPCOU.PCOUID) AS R2 INNER JOIN HEPAREP ON R2.PCOSID=HEPAREP.RETLIB) ON HECHANP.REIPAE=HEPAREP.REIPAE "
                + "WHERE HECHANP.RDICHA=2";
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
        return "ROLRUN";
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
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = " WHERE STR_CODE_ENR='01' AND PCOSID=111001 ";
        return sqlWhere;
    }

    /**
     * Cr�e une nouvelle entit�
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception
     *                si la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new HERetourAnnonce_AnnonceViewBean();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (20.01.2003 13:02:51)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdAnnonceRetour() {
        return forIdAnnonceRetour;
    }

    public void go() {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            setSession(session);
            find();
            System.out.println(size());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        System.exit(-1);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (20.01.2003 13:02:51)
     * 
     * @param newForIdAnnonceRetour
     *            java.lang.String
     */
    public void setForIdAnnonceRetour(java.lang.String newForIdAnnonceRetour) {
        forIdAnnonceRetour = newForIdAnnonceRetour;
    }
}
