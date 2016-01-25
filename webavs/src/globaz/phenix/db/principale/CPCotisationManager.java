package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPCotisationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (MEICOT) */
    private String forGenreCotisation = "";
    /** (IAIDEC) */
    private String forIdCotiAffiliation = "";
    /** Fichier CPCOTIP */
    private String forIdCotisation = "";
    /** (ISICOT) */
    private String forIdDecision = "";
    /** (ISTGCO) */
    private String notInGenreCotisation = "";
    /** (ISTGCO) */
    // order
    private String order = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPCOTIP";
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
        if (JadeStringUtil.isEmpty(getOrder())) {
            return "ISICOT";
        } else {
            return getOrder();
        }
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
        if (getForIdCotisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ISICOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdCotisation());
        }

        // traitement du positionnement
        if (getForIdDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAIDEC=" + _dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }

        // traitement du positionnement
        if (getForIdCotiAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MEICOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdCotiAffiliation());
        }
        // Genre de cotisation
        if (getForGenreCotisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ISTGCO=" + _dbWriteNumeric(statement.getTransaction(), getForGenreCotisation());
        }
        // Non inclus dans une s�lection
        if (getNotInGenreCotisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ISTGCO not in (" + getNotInGenreCotisation() + ")";
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
        return new CPCotisation();
    }

    /**
     * Returns the forGenreCotisation.
     * 
     * @return String
     */
    public String getForGenreCotisation() {
        return forGenreCotisation;
    }

    public String getForIdCotiAffiliation() {
        return forIdCotiAffiliation;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getForIdCotisation() {
        return forIdCotisation;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getNotInGenreCotisation() {
        return notInGenreCotisation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.06.2003 11:25:32)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * Sets the forGenreCotisation.
     * 
     * @param forGenreCotisation
     *            The forGenreCotisation to set
     */
    public void setForGenreCotisation(String forGenreCotisation) {
        this.forGenreCotisation = forGenreCotisation;
    }

    public void setForIdCotiAffiliation(String newForIdCotiAffiliation) {
        forIdCotiAffiliation = newForIdCotiAffiliation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdCotisation(String newForIdCotisation) {
        forIdCotisation = newForIdCotisation;
    }

    public void setForIdDecision(String newForIdDecision) {
        forIdDecision = newForIdDecision;
    }

    public void setNotInGenreCotisation(String notInGenreCotisation) {
        this.notInGenreCotisation = notInGenreCotisation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.06.2003 11:25:32)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

}
