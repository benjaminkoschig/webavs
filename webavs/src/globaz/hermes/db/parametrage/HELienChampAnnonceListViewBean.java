package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (12.03.2003 08:48:30)
 * 
 * @author: Administrator
 */
public class HELienChampAnnonceListViewBean extends BManager implements FWListViewBeanInterface {
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
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HELienChampAnnonceListViewBean lienList = new HELienChampAnnonceListViewBean();
            lienList.setSession(session);
            lienList.find();
            for (int i = 0; i < lienList.size(); i++) {
                HELienChampAnnonceViewBean lienC = (HELienChampAnnonceViewBean) lienList.getEntity(i);
                System.out.println(lienC.getIdLienChampAnnonce());
                lienC.delete();
            }
            if (lienList.hasErrors()) {
                throw new Exception(lienList.getErrors().toString());
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    /** (RDICHA) */
    private String forIdChampAnnonceDepart = new String();
    /** (HEC_RDICHA) */
    private String forIdChampAnnonceRetour = new String();
    /** (RIILIA) */
    private String forIdLienAnnonce = new String();

    /** Fichier HELICAP */
    /** (RJILCA) */
    private String forIdLienChampAnnonce = new String();

    /**
     * Commentaire relatif au constructeur HELienChampAnnonceListViewBean.
     */
    public HELienChampAnnonceListViewBean() {
        super();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HELICAP";
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
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdLienChampAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RJILCA=" + _dbWriteNumeric(statement.getTransaction(), getForIdLienChampAnnonce());
        }

        // traitement du positionnement
        if (getForIdLienAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RIILIA=" + _dbWriteNumeric(statement.getTransaction(), getForIdLienAnnonce());
        }

        // traitement du positionnement
        if (getForIdChampAnnonceDepart().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDICHA=" + _dbWriteNumeric(statement.getTransaction(), getForIdChampAnnonceDepart());
        }

        // traitement du positionnement
        if (getForIdChampAnnonceRetour().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEC_RDICHA=" + _dbWriteNumeric(statement.getTransaction(), getForIdChampAnnonceRetour());
        }

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
        return new HELienChampAnnonceViewBean();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2003 08:50:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdChampAnnonceDepart() {
        return forIdChampAnnonceDepart;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2003 08:50:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdChampAnnonceRetour() {
        return forIdChampAnnonceRetour;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2003 08:50:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdLienAnnonce() {
        return forIdLienAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2003 08:50:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdLienChampAnnonce() {
        return forIdLienChampAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2003 08:50:34)
     * 
     * @param newForIdChampAnnonceDepart
     *            java.lang.String
     */
    public void setForIdChampAnnonceDepart(java.lang.String newForIdChampAnnonceDepart) {
        forIdChampAnnonceDepart = newForIdChampAnnonceDepart;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2003 08:50:34)
     * 
     * @param newForIdChampAnnonceRetour
     *            java.lang.String
     */
    public void setForIdChampAnnonceRetour(java.lang.String newForIdChampAnnonceRetour) {
        forIdChampAnnonceRetour = newForIdChampAnnonceRetour;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2003 08:50:34)
     * 
     * @param newForIdLienAnnonce
     *            java.lang.String
     */
    public void setForIdLienAnnonce(java.lang.String newForIdLienAnnonce) {
        forIdLienAnnonce = newForIdLienAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2003 08:50:34)
     * 
     * @param newForIdLienChampAnnonce
     *            java.lang.String
     */
    public void setForIdLienChampAnnonce(java.lang.String newForIdLienChampAnnonce) {
        forIdLienChampAnnonce = newForIdLienChampAnnonce;
    }
}
