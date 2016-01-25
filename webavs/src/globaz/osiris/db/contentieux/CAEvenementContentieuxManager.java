package globaz.osiris.db.contentieux;

/**
 * Insérez la description du type ici. Date de création : (13.12.2001 15:50:03)
 * 
 * @author: Administrator
 */
public class CAEvenementContentieuxManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Getter
     */

    private java.lang.String forIdParametreEtape = new String();
    /**
     * Setter
     */

    private java.lang.String forIdSection = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAEVCTP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement depuis la section
        if (getForIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }
        // traitement du positionnement depuis un numéro
        if (getForIdParametreEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPARAMETREETAPE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdParametreEtape());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAEvenementContentieux();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.03.2003 10:36:19)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdParametreEtape() {
        return forIdParametreEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2002 08:24:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdSection() {
        return forIdSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.03.2003 10:36:19)
     * 
     * @param newForIdParametreEtape
     *            java.lang.String
     */
    public void setForIdParametreEtape(java.lang.String newForIdParametreEtape) {
        forIdParametreEtape = newForIdParametreEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2002 08:24:59)
     * 
     * @param newForIdSection
     *            java.lang.String
     */
    public void setForIdSection(java.lang.String newForIdSection) {
        forIdSection = newForIdSection;
    }
}
