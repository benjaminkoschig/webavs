package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 08:58:11)
 * 
 * @author: Administrator
 */
public class CASecteurTypeSectionManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * Getter
     */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Setter
     */

    private java.lang.String forIdSecteur = new String();
    private java.lang.String forIdTypeSection = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CASETSP";
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

        // traitement du positionnement
        if (getForIdSecteur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTEUR=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSecteur());
        }

        // traitement du positionnement
        if (getForIdTypeSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPESECTION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeSection());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CASecteurTypeSection();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 15:25:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdSecteur() {
        return forIdSecteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 15:25:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 15:25:20)
     * 
     * @param newForIdSecteur
     *            java.lang.String
     */
    public void setForIdSecteur(java.lang.String newForIdSecteur) {
        forIdSecteur = newForIdSecteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 15:25:40)
     * 
     * @param newForIdTypeSection
     *            java.lang.String
     */
    public void setForIdTypeSection(java.lang.String newForIdTypeSection) {
        forIdTypeSection = newForIdTypeSection;
    }
}
