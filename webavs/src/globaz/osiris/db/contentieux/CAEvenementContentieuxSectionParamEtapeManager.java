package globaz.osiris.db.contentieux;

import globaz.osiris.db.comptes.CASection;

/**
 * Ce manager effectue un inner join entre CAEVCTP,CASECTP et CAPECTP
 * 
 * @author: sch
 */
public class CAEvenementContentieuxSectionParamEtapeManager extends globaz.globall.db.BManager implements
        java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Getter
     */

    private String forEstDeclenche = new String();
    private String forEstIgnoree = new String();
    private String forEstModifie = new String();
    private String forIdParametreEtape = new String();
    /**
     * Setter
     */

    private String forIdSection = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAEVCTP ev" + " inner join " + _getCollection() + CASection.TABLE_CASECTP
                + " se on " + "ev.idsection = se." + CASection.FIELD_IDSECTION + " inner join " + _getCollection()
                + "CAPECTP pe on " + "ev.idparametreetape = pe.idparametreetape";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "pe.sequence ASC";
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
            sqlWhere += "ev." + CASection.FIELD_IDSECTION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }
        // traitement du positionnement depuis un numéro
        if (getForIdParametreEtape().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ev.idparametreetape="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdParametreEtape());
        }
        if (getForEstModifie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ev.estmodifie=" + this._dbWriteString(statement.getTransaction(), getForEstModifie());
        }
        if (getForEstDeclenche().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ev.estdeclenche=" + this._dbWriteString(statement.getTransaction(), getForEstDeclenche());
        }
        if (getForEstIgnoree().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ev.estignoree=" + this._dbWriteString(statement.getTransaction(), getForEstIgnoree());
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
     * @return the forEstDeclenche
     */
    public String getForEstDeclenche() {
        return forEstDeclenche;
    }

    /**
     * @return the forEstIgnoree
     */
    public String getForEstIgnoree() {
        return forEstIgnoree;
    }

    /**
     * @return the forEstModifie
     */
    public String getForEstModifie() {
        return forEstModifie;
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
     * @param forEstDeclenche
     *            the forEstDeclenche to set
     */
    public void setForEstDeclenche(Boolean forEstDeclenche) {
        if (forEstDeclenche.booleanValue()) {
            this.forEstDeclenche = "1";
        } else {
            this.forEstDeclenche = "2";
        }
    }

    /**
     * @param forEstIgnoree
     *            the forEstIgnoree to set
     */
    public void setForEstIgnoree(Boolean forEstIgnoree) {
        if (forEstIgnoree.booleanValue()) {
            this.forEstIgnoree = "1";
        } else {
            this.forEstIgnoree = "2";
        }
    }

    /**
     * @param Boolean
     *            forEstModifier
     */
    public void setForEstModifie(Boolean forEstModifie) {
        if (forEstModifie.booleanValue()) {
            this.forEstModifie = "1";
        } else {
            this.forEstModifie = "2";
        }
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
