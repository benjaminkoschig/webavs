package globaz.helios.db.bouclement;

public class CGParametreBouclementManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdBouclement = new String();
    private java.lang.String forIdCompte = "";
    private java.lang.String forIdMandat = new String();
    private java.lang.String forIdSecteurAVS = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGBOPMP";
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
        if (getForIdBouclement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDBOUCLEMENT=" + _dbWriteNumeric(statement.getTransaction(), getForIdBouclement());
        }

        // traitement du positionnement
        if (getForIdSecteurAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTEURAVS=" + _dbWriteNumeric(statement.getTransaction(), getForIdSecteurAVS());
        }

        // traitement du positionnement
        if (getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        // traitement du positionnement
        if (getForIdCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTE=" + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGParametreBouclement();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdBouclement() {
        return forIdBouclement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.03.2003 11:36:16)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCompte() {
        return forIdCompte;
    }

    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    public java.lang.String getForIdSecteurAVS() {
        return forIdSecteurAVS;
    }

    /**
     * Setter
     */
    public void setForIdBouclement(java.lang.String newForIdBouclement) {
        forIdBouclement = newForIdBouclement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.03.2003 11:36:16)
     * 
     * @param newForIdCompte
     *            java.lang.String
     */
    public void setForIdCompte(java.lang.String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    public void setForIdSecteurAVS(java.lang.String newForIdSecteurAVS) {
        forIdSecteurAVS = newForIdSecteurAVS;
    }
}
