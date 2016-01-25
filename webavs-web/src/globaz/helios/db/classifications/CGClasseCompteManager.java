package globaz.helios.db.classifications;

import globaz.globall.db.BManager;

public class CGClasseCompteManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.util.Enumeration forClassesCompteList = null;
    private java.lang.String forIdClasseCompte = "";
    private java.lang.String forIdClassification = new String();
    private java.lang.String forIdSuperClasse = "";
    private java.lang.String forNoClasse = new String();
    private java.lang.String likeNoClasse = new String();
    private java.lang.String order = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGCLCOP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order + " " + _getCollection() + "CGCLCOP.NUMEROORDRE, " + _getCollection() + "CGCLCOP.NOCLASSE ";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdClassification().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCLASSIFICATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdClassification());
        }

        if (getForClassesCompteList() != null) {
            String temp = "";
            java.util.Enumeration enumerate = getForClassesCompteList();
            while (enumerate.hasMoreElements()) {
                temp += this._dbWriteNumeric(statement.getTransaction(), ((String) enumerate.nextElement()))
                        + ((enumerate.hasMoreElements()) ? " ," : "");
            }
            if (temp.length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IDCLASSECOMPTE IN (" + temp + ")";
            }
        }

        // traitement du positionnement
        if (getForIdSuperClasse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSUPERCLASSE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSuperClasse());
        }

        if (getForNoClasse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NOCLASSE=" + this._dbWriteString(statement.getTransaction(), getForNoClasse());
        }

        if ((getLikeNoClasse() != null) && (getLikeNoClasse().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NOCLASSE like'" + getLikeNoClasse() + "%'";
        }

        if (getForNoClasse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NOCLASSE=" + this._dbWriteString(statement.getTransaction(), getForNoClasse());
        }

        if (getForIdClasseCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCLASSECOMPTE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdClasseCompte());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGClasseCompte();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 14:38:11)
     * 
     * @return java.util.Enumeration
     */
    public java.util.Enumeration getForClassesCompteList() {
        return forClassesCompteList;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 19:07:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdClasseCompte() {
        return forIdClasseCompte;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdClassification() {
        return forIdClassification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 18:15:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdSuperClasse() {
        return forIdSuperClasse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:50:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNoClasse() {
        return forNoClasse;
    }

    public int getLevelNumber(String idClassification) {

        int maxLevelNumber = 0;
        try {
            CGClasseCompteManager classeCompteManager = new CGClasseCompteManager();
            classeCompteManager.setForIdClassification(idClassification);
            classeCompteManager.setSession(getSession());
            classeCompteManager.find(null, BManager.SIZE_NOLIMIT);

            if (classeCompteManager.size() > 0) {
                maxLevelNumber = 1;
            }

            for (int i = 0; i < classeCompteManager.size(); i++) {
                int levelNumber = 1;
                CGClasseCompte entity = (CGClasseCompte) classeCompteManager.getEntity(i);

                if ((maxLevelNumber == 1) || ((maxLevelNumber > 1) && !entity.isLeaf())) {
                    CGClasseCompte parent = getParent(entity);
                    while (parent != null) {
                        levelNumber++;
                        parent = getParent(parent);
                    }
                    if (maxLevelNumber < levelNumber) {
                        maxLevelNumber = levelNumber;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxLevelNumber;
    }

    /**
     * Returns the likeNoClasse.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLikeNoClasse() {
        return likeNoClasse;
    }

    private CGClasseCompte getParent(CGClasseCompte fils) throws Exception {
        CGClasseCompte parent = new CGClasseCompte();
        parent.setSession(getSession());
        parent.setIdClasseCompte(fils.getIdSuperClasse());

        parent.retrieve();
        if (parent.isNew()) {
            return null;
        } else {
            return parent;
        }
    }

    /**
     * @param field
     */
    public void orderByIdSuperClasse(String field) {
        setOrder(_getCollection() + "CGCLCOP." + field + ", ");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 14:38:11)
     * 
     * @param newForClassesCompteList
     *            java.util.Enumeration
     */
    public void setForClassesCompteList(java.util.Enumeration newForClassesCompteList) {
        forClassesCompteList = newForClassesCompteList;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 19:07:30)
     * 
     * @param newForIdClasseCompte
     *            java.lang.String
     */
    public void setForIdClasseCompte(java.lang.String newForIdClasseCompte) {
        forIdClasseCompte = newForIdClasseCompte;
    }

    /**
     * Setter
     */
    public void setForIdClassification(java.lang.String newForIdClassification) {
        forIdClassification = newForIdClassification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 18:15:53)
     * 
     * @param newForIdSuperClasse
     *            java.lang.String
     */
    public void setForIdSuperClasse(java.lang.String newForIdSuperClasse) {
        forIdSuperClasse = newForIdSuperClasse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:50:59)
     * 
     * @param newForNoClasse
     *            java.lang.String
     */
    public void setForNoClasse(java.lang.String newForNoClasse) {
        forNoClasse = newForNoClasse;
    }

    /**
     * Sets the likeNoClasse.
     * 
     * @param likeNoClasse
     *            The likeNoClasse to set
     */
    public void setLikeNoClasse(java.lang.String likeNoClasse) {
        this.likeNoClasse = likeNoClasse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 14:57:36)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

}
