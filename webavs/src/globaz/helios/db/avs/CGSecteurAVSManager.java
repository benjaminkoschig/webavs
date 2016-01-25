package globaz.helios.db.avs;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;

public class CGSecteurAVSManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.Boolean existCompteBilan = null;
    private java.lang.String forIdMandat = "";
    private java.lang.String forIdSecteurAVS = new String();
    private java.lang.String forIdTypeTache = "";
    private java.lang.String forNotIdTypeTache = "";
    private java.lang.Boolean forTauxVentilationDefini = null;
    private java.lang.String fromSecteur = "";
    private String orderBy = null;
    private java.lang.String untilSecteur = "";

    /**
     * Commentaire relatif au constructeur CGSecteurAVSManager.
     */
    public CGSecteurAVSManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGSECTP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (orderBy == null || orderBy.trim().length() == 0) {
            setOrderBy("CGSECTP.IDSECTEURAVS ");
        }

        return " " + _getCollection() + getOrderBy();

    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdSecteurAVS() != null && getForIdSecteurAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTEURAVS=" + _dbWriteNumeric(statement.getTransaction(), getForIdSecteurAVS());
        }
        if (getForIdTypeTache() != null && getForIdTypeTache().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPETACHE=" + _dbWriteNumeric(statement.getTransaction(), getForIdTypeTache());
        }

        if (getForNotIdTypeTache() != null && getForNotIdTypeTache().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPETACHE <>" + _dbWriteNumeric(statement.getTransaction(), getForIdTypeTache());
        }

        if (getForIdMandat() != null && getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }
        if (getForTauxVentilationDefini() != null && getForTauxVentilationDefini() != null
                && getForTauxVentilationDefini().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAUXVENTILATION>0";
        }
        if (getFromSecteur() != null && getFromSecteur().length() != 0 && getUntilSecteur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTEURAVS BETWEEN " + _dbWriteNumeric(statement.getTransaction(), getFromSecteur())
                    + " AND " + _dbWriteNumeric(statement.getTransaction(), getUntilSecteur());
        } else {
            if (getFromSecteur().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IDSECTEURAVS >= " + _dbWriteNumeric(statement.getTransaction(), getFromSecteur());
            }
            if (getUntilSecteur().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IDSECTEURAVS <= " + _dbWriteNumeric(statement.getTransaction(), getUntilSecteur());
            }
        }

        if (getExistCompteBilan() != null && getExistCompteBilan().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "COMPTEBILAN="
                    + _dbWriteBoolean(statement.getTransaction(), getExistCompteBilan(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGSecteurAVS();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2003 11:30:25)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getExistCompteBilan() {
        return existCompteBilan;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 18:00:33)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 16:51:38)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdSecteurAVS() {
        return forIdSecteurAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.06.2003 10:51:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeTache() {
        return forIdTypeTache;
    }

    /**
     * Returns the forNotIdTypeTache.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNotIdTypeTache() {
        return forNotIdTypeTache;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 16:53:51)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForTauxVentilationDefini() {
        return forTauxVentilationDefini;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 08:56:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromSecteur() {
        return fromSecteur;
    }

    /**
     * Returns the orderBy.
     * 
     * @return String
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 08:56:19)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilSecteur() {
        return untilSecteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.06.2003 11:30:25)
     * 
     * @param newForBilan
     *            java.lang.Boolean
     */
    public void setExistCompteBilan(java.lang.Boolean newForBilan) {
        existCompteBilan = newForBilan;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 18:00:33)
     * 
     * @param newForIdMandat
     *            java.lang.String
     */
    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 16:51:38)
     * 
     * @param newForIdSecteurAVS
     *            java.lang.String
     */
    public void setForIdSecteurAVS(java.lang.String newForIdSecteurAVS) {
        forIdSecteurAVS = newForIdSecteurAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.06.2003 10:51:58)
     * 
     * @param newForIdTypeTache
     *            java.lang.String
     */
    public void setForIdTypeTache(java.lang.String newForIdTypeTache) {
        forIdTypeTache = newForIdTypeTache;
    }

    /**
     * Sets the forNotIdTypeTache.
     * 
     * @param forNotIdTypeTache
     *            The forNotIdTypeTache to set
     */
    public void setForNotIdTypeTache(java.lang.String forNotIdTypeTache) {
        this.forNotIdTypeTache = forNotIdTypeTache;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 16:53:51)
     * 
     * @param newForTauxVentilationDefini
     *            java.lang.Boolean
     */
    public void setForTauxVentilationDefini(java.lang.Boolean newForTauxVentilationDefini) {
        forTauxVentilationDefini = newForTauxVentilationDefini;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 08:56:02)
     * 
     * @param newFromSecteur
     *            java.lang.String
     */
    public void setFromSecteur(java.lang.String newFromSecteur) {
        fromSecteur = newFromSecteur;
    }

    /**
     * Sets the orderBy.
     * 
     * @param orderBy
     *            The orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 08:56:19)
     * 
     * @param newUntilSecteur
     *            java.lang.String
     */
    public void setUntilSecteur(java.lang.String newUntilSecteur) {
        untilSecteur = newUntilSecteur;
    }

}
