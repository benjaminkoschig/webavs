package globaz.musca.db.facturation;

import globaz.globall.db.BManager;
import java.io.Serializable;

public class FAOrdreRegroupementManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdOrdreRegroupement = new String();
    private String forNature = new String();
    private String forNumCaisse = new String();
    private String forOrdreRegroupement = new String();
    private String fromNumRubrique = new String();
    private String likeOrdreRegroupement = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "FAORDIP AS FAORDIP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "EHNORD ASC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForOrdreRegroupement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EHNORD=" + this._dbWriteNumeric(statement.getTransaction(), getForOrdreRegroupement());
        }

        if (getForIdOrdreRegroupement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAORDIP.EHIDOR="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOrdreRegroupement());
        }
        if (getLikeOrdreRegroupement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EHNORD >=" + getLikeOrdreRegroupement();
        }
        if (getForNumCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "EHNCAI =" + getForNumCaisse();
        }
        if (getForNature().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NATURE =" + getForNature();
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAOrdreRegroupement();
    }

    /**
     * @return
     */
    public String getForIdOrdreRegroupement() {
        return forIdOrdreRegroupement;
    }

    public String getForNature() {
        return forNature;
    }

    /**
     * @return
     */
    public String getForNumCaisse() {
        return forNumCaisse;
    }

    /**
     * @return
     */
    public java.lang.String getForOrdreRegroupement() {
        return forOrdreRegroupement;
    }

    /**
     * @return
     */
    public java.lang.String getFromNumRubrique() {
        return fromNumRubrique;
    }

    /**
     * @return
     */
    public String getLikeOrdreRegroupement() {
        return likeOrdreRegroupement;
    }

    /**
     * @param string
     */
    public void setForIdOrdreRegroupement(String string) {
        forIdOrdreRegroupement = string;
    }

    public void setForNature(String forNature) {
        this.forNature = forNature;
    }

    /**
     * @param string
     */
    public void setForNumCaisse(String string) {
        forNumCaisse = string;
    }

    /**
     * @param string
     */
    public void setForOrdreRegroupement(java.lang.String string) {
        forOrdreRegroupement = string;
    }

    /**
     * @param string
     */
    public void setFromNumRubrique(java.lang.String string) {
        fromNumRubrique = string;
    }

    /**
     * @param string
     */
    public void setLikeOrdreRegroupement(String string) {
        likeOrdreRegroupement = string;
    }

}
