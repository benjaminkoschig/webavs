package globaz.naos.db.suiviCaisseAffiliation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.List;

/**
 * Le Manager pour l'entité SuiviCaisseAffiliation.
 * 
 * @author sau
 */
public class AFSuiviCaisseAffiliationManager extends BManager implements Serializable {

    private static final long serialVersionUID = -4809152534373100856L;
    private boolean exceptDateFinEgaleDateDebut = false;
    private java.lang.String exceptIdSuiviCaisse;
    private java.lang.String forAffiliationId;
    private java.lang.String forAnnee;
    private java.lang.String forAnneeActive;
    private java.lang.String forDate;
    private java.lang.String forGenreCaisse;
    private java.lang.String forIdTiersCaisse;
    private List<String> inAffiliationId;
    private java.lang.String order;
    private boolean wantAllCaisse = false; // par défaut à false pour ne pas courciciurter les process existant qui ne
    // prend que les caisses principales
    private boolean wantCaisseAccessoire = false;

    private boolean wantCaissePrincipale = true;

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFSUAFP";
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getExceptIdSuiviCaisse())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MYISUA<>" + this._dbWriteNumeric(statement.getTransaction(), getExceptIdSuiviCaisse());
        }

        if (isExceptDateFinEgaleDateDebut()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MYDDEB<>MYDFIN or MYDDEB=0)";
        }

        if (!isWantAllCaisse()) {
            /* lister les caisses accessoires */
            if (isWantCaisseAccessoire()) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += " MYBACC = "
                        + this._dbWriteBoolean(statement.getTransaction(), new Boolean(true),
                                BConstants.DB_TYPE_BOOLEAN_CHAR);
            }

            /* lister les caisses principales */
            if (isWantCaissePrincipale()) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += " MYBACC = "
                        + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                                BConstants.DB_TYPE_BOOLEAN_CHAR);
            }
        }

        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
            order = "MYTGEN, MYDDEB DESC";
        }
        if (!JadeStringUtil.isEmpty(getForIdTiersCaisse())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiersCaisse());
        }

        if (!JadeStringUtil.isEmpty(getForGenreCaisse())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MYTGEN=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreCaisse());
        }

        if (!JadeStringUtil.isEmpty(getForAnneeActive())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MYDDEB<" + getForAnneeActive() + "1231 " + " AND ( MYDFIN=0 OR MYDFIN>" + getForAnneeActive()
                    + "0101" + " )";
        }

        if (!JadeStringUtil.isEmpty(forDate)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MYDDEB<=" + this._dbWriteDateAMJ(statement.getTransaction(), forDate)
                    + " AND (MYDFIN=0 OR MYDFIN>=" + this._dbWriteDateAMJ(statement.getTransaction(), forDate) + ")";
        }

        if (!JadeStringUtil.isEmpty(forAnnee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MYDFIN=0 OR MYDFIN>=" + forAnnee + "1231)";
        }

        if (!(getInAffiliationId() == null) && (getInAffiliationId().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF IN (";
            String critereIn = "";
            int nbId = getInAffiliationId().size();
            int idx = 0;
            for (String affId : getInAffiliationId()) {
                idx++;
                critereIn += affId;

                if (idx < nbId) {
                    critereIn += ",";
                }
            }
            sqlWhere += critereIn;
            sqlWhere += ")";
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFSuiviCaisseAffiliation();
    }

    public java.lang.String getExceptIdSuiviCaisse() {
        return exceptIdSuiviCaisse;
    }

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    public java.lang.String getForAnnee() {
        return forAnnee;
    }

    public java.lang.String getForAnneeActive() {
        return forAnneeActive;
    }

    public java.lang.String getForDate() {
        return forDate;
    }

    public java.lang.String getForGenreCaisse() {
        return forGenreCaisse;
    }

    public java.lang.String getForIdTiersCaisse() {
        return forIdTiersCaisse;
    }

    public List<String> getInAffiliationId() {
        return inAffiliationId;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public boolean isExceptDateFinEgaleDateDebut() {
        return exceptDateFinEgaleDateDebut;
    }

    // ***********************************************
    // Getter
    // ***********************************************

    public boolean isWantAllCaisse() {
        return wantAllCaisse;
    }

    public boolean isWantCaisseAccessoire() {
        return wantCaisseAccessoire;
    }

    public boolean isWantCaissePrincipale() {
        return wantCaissePrincipale;
    }

    // ***********************************************
    // Setter
    // ***********************************************

    public void setExceptDateFinEgaleDateDebut(boolean exceptDateFinEgaleDateDebut) {
        this.exceptDateFinEgaleDateDebut = exceptDateFinEgaleDateDebut;
    }

    public void setExceptIdSuiviCaisse(java.lang.String exceptIdSuiviCaisse) {
        this.exceptIdSuiviCaisse = exceptIdSuiviCaisse;
    }

    public void setForAffiliationId(java.lang.String string) {
        forAffiliationId = string;
    }

    /**
     * Definit la date de fin de suivi pour une année donnée ex : annee = 2010, date de fin = 0 ou >= 31.12.2010
     * 
     * @param forAnnee
     */
    public void setForAnnee(java.lang.String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForAnneeActive(java.lang.String string) {
        forAnneeActive = string;
    }

    public void setForDate(java.lang.String newForDate) {
        forDate = newForDate;
    }

    public void setForGenreCaisse(java.lang.String string) {
        forGenreCaisse = string;
    }

    public void setForIdTiersCaisse(java.lang.String forIdTiersCaisse) {
        this.forIdTiersCaisse = forIdTiersCaisse;
    }

    public void setInAffiliationId(List<String> inAffiliationId) {
        this.inAffiliationId = inAffiliationId;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }

    public void setWantAllCaisse(boolean wantAllCaisse) {
        this.wantAllCaisse = wantAllCaisse;
    }

    public void setWantCaisseAccessoire(boolean wantCaisseAccessoire) {
        this.wantCaisseAccessoire = wantCaisseAccessoire;
    }

    public void setWantCaissePrincipale(boolean wantCaissePrincipale) {
        this.wantCaissePrincipale = wantCaissePrincipale;
    }

}
