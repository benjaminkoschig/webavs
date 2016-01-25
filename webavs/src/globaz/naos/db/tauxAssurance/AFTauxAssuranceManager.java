package globaz.naos.db.tauxAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

/**
 * Le Manager pour l'entité TauxAssurance.
 * 
 * @author administrator
 */
public class AFTauxAssuranceManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnnee;
    private java.lang.String forCategorieId;
    private java.lang.String forDate;
    private java.lang.String forGenreValeur;
    private java.lang.String forIdAssurance;
    private String forSelectionTri = new String();
    private java.lang.String forSexe;
    private java.lang.String forTranche;
    private java.lang.String forTypeId;
    private java.lang.String[] forTypeIdIn = new String[] {};
    private java.lang.String fromRang;
    private String fromTranche;
    private java.lang.String order;

    private java.lang.String toRang;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFTAUXP";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // setOrder("MCTGEN, MCDDEB"); // Genre, Date Début

        if (!JadeStringUtil.isEmpty(getForIdAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAssurance());
        }

        if (!JadeStringUtil.isEmpty(getForTypeId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCTTYP=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeId());
        }

        if (getForTypeIdIn().length != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MCTTYP in(";
            for (int i = 0; i < getForTypeIdIn().length; i++) {
                sqlWhere += this._dbWriteNumeric(statement.getTransaction(), getForTypeIdIn()[i]);
                if (i + 1 < getForTypeIdIn().length) {
                    sqlWhere += ",";
                }
            }
            sqlWhere += " ) ";
        }

        if (!JadeStringUtil.isEmpty(getForCategorieId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCTCAT=" + this._dbWriteNumeric(statement.getTransaction(), getForCategorieId());
        }

        if (!JadeStringUtil.isEmpty(getForGenreValeur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCTGEN=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreValeur());
            // order = "MCDDEB";
        }

        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String debutAnnee = getForAnnee() + "0101";
            String finAnnee = getForAnnee() + "1231";
            sqlWhere += "(" + "MCDDEB >= " + debutAnnee + " AND " + "MCDDEB <= " + finAnnee + ")";
            /*
             * sqlWhere += "((" + "MCDDEB <= " + finAnnee + " AND " + "MCDFIN <= " + finAnnee + " AND " + "MCDFIN >= " +
             * debutAnnee + ") OR " + "(" + "MCDDEB <= " + finAnnee + " AND " + "MCDFIN = 0))";
             */
        }

        if (!JadeStringUtil.isEmpty(getForDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCDDEB <=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDate());
            /*
             * + " AND (MCDFIN = 0 OR MCDFIN >=" + _dbWriteDateAMJ(statement.getTransaction(), getForDate()) + ")";
             */
            // setOrder("MCTGEN, MCDDEB DESC");
        }

        if (!JadeStringUtil.isEmpty(getToRang())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCNRAN <" + this._dbWriteNumeric(statement.getTransaction(), getToRang());
        }

        if (!JadeStringUtil.isEmpty(getFromRang())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCNRAN >" + this._dbWriteNumeric(statement.getTransaction(), getFromRang());
        }

        if (!JadeStringUtil.isEmpty(getForTranche())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCMTRA =" + this._dbWriteNumeric(statement.getTransaction(), getForTranche());
        }

        if (!JadeStringUtil.isEmpty(getFromTranche())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MCMTRA >=" + this._dbWriteNumeric(statement.getTransaction(), getFromTranche());
        }
        if (!JadeStringUtil.isEmpty(getForSexe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MCTSEX=" + this._dbWriteNumeric(statement.getTransaction(), getForSexe()) + " or "
                    + " MCTSEX=0)";
        }
        // ne pas prendre en compte les taux de type moyen
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "MCTTYP<>" + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_TAUX_MOYEN);

        if (JadeStringUtil.isEmpty(order)) {
            setOrderByGenreAndDateDebutDesc();
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFTauxAssurance();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForAnnee() {
        return forAnnee;
    }

    public java.lang.String getForCategorieId() {
        return forCategorieId;
    }

    public java.lang.String getForDate() {
        return forDate;
    }

    public java.lang.String getForGenreValeur() {
        return forGenreValeur;
    }

    public java.lang.String getForIdAssurance() {
        return forIdAssurance;
    }

    public String getForSelectionTri() {
        return forSelectionTri;
    }

    public java.lang.String getForSexe() {
        return forSexe;
    }

    public java.lang.String getForTranche() {
        return forTranche;
    }

    public java.lang.String getForTypeId() {
        return forTypeId;
    }

    public java.lang.String[] getForTypeIdIn() {
        return forTypeIdIn;
    }

    public java.lang.String getFromRang() {
        return fromRang;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    /**
     * @return
     */
    public String getFromTranche() {
        return fromTranche;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public java.lang.String getToRang() {
        return toRang;
    }

    public void setForAnnee(java.lang.String newforAnnee) {
        forAnnee = newforAnnee;
    }

    public void setForCategorieId(java.lang.String newForCategorieId) {
        forCategorieId = newForCategorieId;
    }

    public void setForDate(java.lang.String string) {
        forDate = string;
    }

    public void setForGenreValeur(java.lang.String string) {
        forGenreValeur = string;
    }

    public void setForIdAssurance(java.lang.String newForIdAssurance) {
        forIdAssurance = newForIdAssurance;
    }

    public void setForSelectionTri(String forSelectionTri) {
        this.forSelectionTri = forSelectionTri;
    }

    public void setForSexe(java.lang.String string) {
        forSexe = string;
    }

    public void setForTranche(java.lang.String string) {
        forTranche = string;
    }

    public void setForTypeId(java.lang.String newForTypeId) {
        forTypeId = newForTypeId;
    }

    public void setForTypeIdIn(java.lang.String[] forTypeIdIn) {
        this.forTypeIdIn = forTypeIdIn;
    }

    public void setFromRang(java.lang.String string) {
        fromRang = string;
    }

    /**
     * @param string
     */
    public void setFromTranche(String string) {
        fromTranche = string;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }

    public void setOrderByDateDebut() {
        setOrder("MCDDEB");
    }

    public void setOrderByDebutDescRang() {
        setOrder("MCDDEB DESC, MCNRAN");
    }

    public void setOrderByGenreAndDateDebut() {
        setOrder("MCTGEN, MCDDEB");
    }

    public void setOrderByGenreAndDateDebutDesc() {
        setOrder("MCTGEN, MCDDEB DESC, MCNRAN");
    }

    public void setOrderByRangDebutDesc() {
        setOrder("MCNRAN , MCDDEB DESC");
    }

    public void setToRang(java.lang.String string) {
        toRang = string;
    }

}
