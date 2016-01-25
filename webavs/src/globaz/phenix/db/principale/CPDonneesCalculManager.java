package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CPDonneesCalculManager extends BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Fichier CPDOCAP */
    private String forIdDecision = "";
    /** (IAIDEC) */

    private String forIdDonneesCalcul = "";

    /** (IHIDCA) */

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPDOCAP";
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
        return "IHIDCA";
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
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC=" + _dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }

        if (getForIdDonneesCalcul().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IHIDCA=" + _dbWriteNumeric(statement.getTransaction(), getForIdDonneesCalcul());
        }

        return sqlWhere;
    }

    /**
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPDonneesCalcul();
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * @return
     */
    public String getForIdDonneesCalcul() {
        return forIdDonneesCalcul;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdDecision(String newForIdDecision) {
        forIdDecision = newForIdDecision;
    }

    /**
     * @param string
     */
    public void setForIdDonneesCalcul(String string) {
        forIdDonneesCalcul = string;
    }

}
