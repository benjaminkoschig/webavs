package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;
import globaz.musca.api.IFAPrintManageDoc;

public class FAFacturationExtManager extends globaz.globall.db.BManager implements java.io.Serializable,
        IFAPrintManageDoc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEnSuspens = new String();
    private String forIdFacturationExt = new String();
    private String forNumAffilie = new String();
    private String forNumCaisse = new String();
    private String forNumPassage = new String();
    private String forNumPeriode = new String();
    private String forNumRubrique = new String();
    private String forRole = new String();
    private String forTypeFactu = new String();
    private Boolean numPassageIsNullOrZero = new Boolean(true);

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "FANAFF ASC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        if (isNumPassageIsNullOrZero().booleanValue()) {
            sqlWhere = " (FAFAEXT.FANPAS IS NULL OR FAFAEXT.FANPAS=0) ";
        } else {
            sqlWhere = " (FAFAEXT.FANPAS IS NOT NULL OR FAFAEXT.FANPAS <> 0) ";
        }
        // Type de facture
        if (getForTypeFactu().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FALTFA=" + this._dbWriteString(statement.getTransaction(), getForTypeFactu());
        }
        // idFacturationExterne
        if (getForIdFacturationExt().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FAIDFE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdFacturationExt());
        }
        // Numéro de caisse
        if (getForNumCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FANCAI=" + this._dbWriteNumeric(statement.getTransaction(), getForNumCaisse());
        }
        // Numéro d'affilié
        if (getForNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FANAFF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie());
        }
        // Numéro de rubrique
        if (getForNumRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FANRUB=" + this._dbWriteString(statement.getTransaction(), getForNumRubrique());
        }
        // Numéro de période
        if (getForNumPeriode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FANPER=" + this._dbWriteString(statement.getTransaction(), getForNumPeriode());
        }
        // Numéro de passage
        if (getForNumPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FANPAS=" + this._dbWriteNumeric(statement.getTransaction(), getForNumPassage());
        }
        // Role
        if (getForRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FATROL=" + this._dbWriteNumeric(statement.getTransaction(), getForRole());
        }
        // En Suspens
        if (getForEnSuspens().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAFAEXT.FABSUS=" + this._dbWriteString(statement.getTransaction(), getForEnSuspens());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAFacturationExt();
    }

    /**
     * @return
     */
    public java.lang.String getForEnSuspens() {
        return forEnSuspens;
    }

    /**
     * @return
     */
    public java.lang.String getForIdFacturationExt() {
        return forIdFacturationExt;
    }

    /**
     * @return
     */
    public java.lang.String getForNumAffilie() {
        return forNumAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getForNumCaisse() {
        return forNumCaisse;
    }

    /**
     * @return
     */
    public java.lang.String getForNumPassage() {
        return forNumPassage;
    }

    /**
     * @return
     */
    public java.lang.String getForNumPeriode() {
        return forNumPeriode;
    }

    /**
     * @return
     */
    public java.lang.String getForNumRubrique() {
        return forNumRubrique;
    }

    /**
     * @return
     */
    public java.lang.String getForRole() {
        return forRole;
    }

    /**
     * @return
     */
    public java.lang.String getForTypeFactu() {
        return forTypeFactu;
    }

    /**
     * @return the forNumPassageIsNullOrZero
     */
    public Boolean isNumPassageIsNullOrZero() {
        return numPassageIsNullOrZero;
    }

    /**
     * @param string
     */
    public void setForEnSuspens(java.lang.String string) {
        forEnSuspens = string;
    }

    /**
     * @param string
     */
    public void setForIdFacturationExt(java.lang.String string) {
        forIdFacturationExt = string;
    }

    /**
     * @param string
     */
    public void setForNumAffilie(java.lang.String string) {
        forNumAffilie = string;
    }

    /**
     * @param string
     */
    public void setForNumCaisse(java.lang.String string) {
        forNumCaisse = string;
    }

    /**
     * @param string
     */
    public void setForNumPassage(java.lang.String string) {
        forNumPassage = string;
    }

    /**
     * @param string
     */
    public void setForNumPeriode(java.lang.String string) {
        forNumPeriode = string;
    }

    /**
     * @param string
     */
    public void setForNumRubrique(java.lang.String string) {
        forNumRubrique = string;
    }

    /**
     * @param string
     */
    public void setForRole(java.lang.String string) {
        forRole = string;
    }

    /**
     * @param string
     */
    public void setForTypeFactu(java.lang.String string) {
        forTypeFactu = string;
    }

    /**
     * Cette méthode permet de dire s'il faut prendre les numéros de passage suivants : <br>
     * <li>null ou zéro = true (défaut)</li> <li>not null ou différent de zéro = false</li>
     * 
     * @param numPassageIsNullOrZero
     *            the numPassageIsNullOrZero to set
     */
    public void setNumPassageIsNullOrZero(Boolean numPassageIsNullOrZero) {
        this.numPassageIsNullOrZero = numPassageIsNullOrZero;
    }

}
