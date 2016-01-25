package globaz.pavo.util;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;

public class CIAffilieManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String INDEPENDANT = "ind";
    public final static java.lang.String PARITAIRE = "par";
    public final static java.lang.String PERSONNEL = "per";
    private boolean forActif = false;
    private java.lang.String forAffiliationId = new String();
    private java.lang.String forAffilieNumero = new String();
    public String forCategorie = "";
    private java.lang.String forIndependant = new String();
    private boolean forParitaire = false;
    private java.lang.String forPeriodicite = new String();
    private java.lang.String[] forPeriodiciteIn = new String[] {};
    private java.lang.String forTiersId = new String();
    private java.lang.String forTypeFacturation = new String();
    private java.lang.String likeAffilieNumero = new String();
    private java.lang.String order = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "AFAFFIP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForTiersId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "HTITIE=" + _dbWriteNumeric(statement.getTransaction(), getForTiersId());
        }
        if (getForAffiliationId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MAIAFF=" + _dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }

        if (getForPeriodicite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MATPER=" + _dbWriteNumeric(statement.getTransaction(), getForPeriodicite());
        }
        if (getForPeriodiciteIn().length != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MATPER in(";
            for (int i = 0; i < getForPeriodiciteIn().length; i++) {
                sqlWhere += _dbWriteNumeric(statement.getTransaction(), getForPeriodiciteIn()[i]);
                if (i + 1 < getForPeriodiciteIn().length) {
                    sqlWhere += ",";
                }
            }
            sqlWhere += " ) ";
        }
        if (getForAffilieNumero().length() != 0) {
            order = "MADFIN";
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF=" + _dbWriteString(statement.getTransaction(), getForAffilieNumero());
        }
        if (getLikeAffilieNumero().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MALNAF like '" + getLikeAffilieNumero() + "%'";
        }
        if (getForTypeFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForTypeFacturation().equals(PARITAIRE)) {
                sqlWhere += "MATTAF in(804002,804005)";
            } else {
                sqlWhere += "MATTAF in(804001,804004)";
            }
        }
        if (getForActif()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MADFIN=0";
        }
        if (isForParitaire()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MATTAF in(" + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + "," + CodeSystem.TYPE_AFFILI_EMPLOY + ","
                    + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + "," + CodeSystem.TYPE_AFFILI_LTN + ","
                    + CodeSystem.TYPE_AFFILI_BENEF_AF + ")";
        }

        if (getForIndependant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (getForIndependant().equals(INDEPENDANT)) {
                sqlWhere += "MATTAF in(" + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + "," + CodeSystem.TYPE_AFFILI_INDEP
                        + "," + CodeSystem.TYPE_AFFILI_NON_ACTIF + "," + CodeSystem.TYPE_AFFILI_TSE + ","
                        + CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE + ")";
            } else {
                sqlWhere += "MATTAF in(" + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + "," + CodeSystem.TYPE_AFFILI_EMPLOY
                        + "," + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + "," + CodeSystem.TYPE_AFFILI_LTN + ","
                        + CodeSystem.TYPE_AFFILI_BENEF_AF + ")";
            }
        }
        if (!JadeStringUtil.isEmpty(getForCategorie())) {
            if (PARITAIRE.equals(getForCategorie())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "MATTAF in(" + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + "," + CodeSystem.TYPE_AFFILI_EMPLOY
                        + "," + CodeSystem.TYPE_AFFILI_LTN + "," + CodeSystem.TYPE_AFFILI_BENEF_AF + ")";
            }
            if (PERSONNEL.equals(getForCategorie())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "MATTAF in(" + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + "," + CodeSystem.TYPE_AFFILI_INDEP
                        + "," + CodeSystem.TYPE_AFFILI_NON_ACTIF + "," + CodeSystem.TYPE_AFFILI_TSE + ","
                        + CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE + ")";
            }
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CIAffilie();
    }

    /**
     * @return
     */
    public boolean getForActif() {
        return forActif;
    }

    /**
     * Getter
     */
    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2003 10:10:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForAffilieNumero() {
        return forAffilieNumero;
    }

    public String getForCategorie() {
        return forCategorie;
    }

    public java.lang.String getForIndependant() {
        return forIndependant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.05.2003 14:48:33)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForPeriodicite() {
        return forPeriodicite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2003 14:14:52)
     * 
     * @return java.lang.String
     */
    public java.lang.String[] getForPeriodiciteIn() {
        return forPeriodiciteIn;
    }

    /**
     * Getter
     */
    public java.lang.String getForTiersId() {
        return forTiersId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.06.2003 11:38:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForTypeFacturation() {
        return forTypeFacturation;
    }

    /**
     * Returns the likeAffilieNumero.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLikeAffilieNumero() {
        return likeAffilieNumero;
    }

    /**
     * @return
     */
    public boolean isForParitaire() {
        return forParitaire;
    }

    /**
     * @param string
     */
    public void setForActif(boolean valeur) {
        forActif = valeur;
    }

    /**
     * Setter
     */
    public void setForAffiliationId(java.lang.String newForAffiliationId) {
        forAffiliationId = newForAffiliationId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2003 10:10:50)
     * 
     * @param newForAffilieNumero
     *            java.lang.String
     */
    public void setForAffilieNumero(java.lang.String newForAffilieNumero) {
        forAffilieNumero = newForAffilieNumero;
    }

    public void setForCategorie(String forCategorie) {
        this.forCategorie = forCategorie;
    }

    public void setForIndependant(java.lang.String forIndependant) {
        this.forIndependant = forIndependant;
    }

    /**
     * @param b
     */
    public void setForParitaire(boolean b) {
        forParitaire = b;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.05.2003 14:48:33)
     * 
     * @param newForPeriodicite
     *            java.lang.String
     */
    public void setForPeriodicite(java.lang.String newForPeriodicite) {
        forPeriodicite = newForPeriodicite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2003 14:14:52)
     * 
     * @param newForPeriodiciteIn
     *            java.lang.String
     */
    public void setForPeriodiciteIn(java.lang.String[] newForPeriodiciteIn) {
        forPeriodiciteIn = newForPeriodiciteIn;
    }

    /**
     * Setter
     */
    public void setForTiersId(java.lang.String newForTiersId) {
        forTiersId = newForTiersId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.06.2003 11:38:43)
     * 
     * @param newForTypeFacturation
     *            java.lang.String
     */
    public void setForTypeFacturation(java.lang.String newForTypeFacturation) {
        forTypeFacturation = newForTypeFacturation;
    }

    /**
     * Sets the likeAffilieNumero.
     * 
     * @param likeAffilieNumero
     *            The likeAffilieNumero to set
     */
    public void setLikeAffilieNumero(java.lang.String likeAffilieNumero) {
        this.likeAffilieNumero = likeAffilieNumero;
    }

}
