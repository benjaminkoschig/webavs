package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (29.11.2002 13:44:50)
 * 
 * @author: ado
 */
public class HEChampobligatoireListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (RDICHA) */
    private String forIdChampAnnonce = new String();
    /** Fichier HECHOBP */
    /** (RCICHO) */
    private String forIdChampObligatoire = new String();
    /** (RBICRM) */
    private String forIdCritereMotif = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HECHOBP";
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
        return "RCICHO";
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
        if (getForIdChampObligatoire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RCICHO=" + _dbWriteNumeric(statement.getTransaction(), getForIdChampObligatoire());
        }
        // traitement du positionnement
        if (getForIdCritereMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RBICRM=" + _dbWriteNumeric(statement.getTransaction(), getForIdCritereMotif());
        }
        // traitement du positionnement
        if (getForIdChampAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RDICHA=" + _dbWriteNumeric(statement.getTransaction(), getForIdChampAnnonce());
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
        return new HEChampobligatoireViewBean();
    }

    public String getForIdChampAnnonce() {
        return forIdChampAnnonce;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdChampObligatoire() {
        return forIdChampObligatoire;
    }

    public String getForIdCritereMotif() {
        return forIdCritereMotif;
    }

    public void setForIdChampAnnonce(String newForIdChampAnnonce) {
        forIdChampAnnonce = newForIdChampAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdChampObligatoire(String newForIdChampObligatoire) {
        forIdChampObligatoire = newForIdChampObligatoire;
    }

    public void setForIdCritereMotif(String newForIdCritereMotif) {
        forIdCritereMotif = newForIdCritereMotif;
    }
}
