package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (27.11.2002 09:51:45)
 * 
 * @author: ado
 */
public class HEChampannonceListViewBean extends BManager implements FWListViewBeanInterface {
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
    }

    /** (RDTCHA) */
    private String forIdChamp = new String();
    /** Fichier HECHANP */
    /** (REIPAE) */
    private String forIdParametrageAnnonce = new String();

    private String forNotIdChamp = new String();

    /**
     * Commentaire relatif au constructeur HEChampannonceListViewBean.
     */
    public HEChampannonceListViewBean() {
        super();
    }

    /**
     * Constructor HEChampannonceListViewBean.
     * 
     * @param bSession
     */
    public HEChampannonceListViewBean(BSession bSession) {
        this();
        setSession(bSession);
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HECHANP";
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
        return "REIPAE,RDNDEB";
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
        if (getForIdParametrageAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "REIPAE=" + _dbWriteNumeric(statement.getTransaction(), getForIdParametrageAnnonce());
        }
        // traitement du positionnement
        if (getForIdChamp().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDTCHA=" + _dbWriteNumeric(statement.getTransaction(), getForIdChamp());
        }
        // traitement du positionnement
        if (getForNotIdChamp().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDTCHA<>" + _dbWriteNumeric(statement.getTransaction(), getForNotIdChamp());
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
        return new HEChampannonceViewBean();
    }

    public String getForIdChamp() {
        return forIdChamp;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdParametrageAnnonce() {
        return forIdParametrageAnnonce;
    }

    /**
     * Returns the forNotIdChamp.
     * 
     * @return String
     */
    public String getForNotIdChamp() {
        return forNotIdChamp;
    }

    public void setForIdChamp(String newForIdChamp) {
        forIdChamp = newForIdChamp;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdParametrageAnnonce(String newForIdParametrageAnnonce) {
        forIdParametrageAnnonce = newForIdParametrageAnnonce;
    }

    /**
     * Method setForNotIdChamp.
     * 
     * @param NUMERO_ASSURE
     */
    public void setForNotIdChamp(String idChamp) {
        forNotIdChamp = idChamp;
    }
}
