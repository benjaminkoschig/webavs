package globaz.naos.db.particulariteAffiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import com.google.common.base.Joiner;

/**
 * Le Manager de l'entité ParticulariteAffiliation.
 * 
 * @author sau
 */
public class AFParticulariteAffiliationManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Collection<String> inAffiliationIds;
    private List<String> inParticularites;
    private java.lang.String dateDebutLessOrEqual;
    private java.lang.String dateFinGreatOrEqual;
    private java.lang.String forAffiliationId;
    private java.lang.String forDate;
    private java.lang.String forDateDebut;
    private java.lang.String forDateFin;
    private java.lang.String forParticularite;
    private java.lang.String forParticulariteAffiliationId;
    private java.lang.String fromDateDebut;
    private java.lang.String exceptParaticulariteId;

    private java.lang.String order = "MFDDEB";

    public Collection<String> getInAffiliationIds() {
        return inAffiliationIds;
    }

    public void setInAffiliationIds(Collection<String> inAffiliationIds) {
        this.inAffiliationIds = inAffiliationIds;
    }

    public List<String> getInParticularites() {
        return inParticularites;
    }

    public void setInParticularites(List<String> inParticularites) {
        this.inParticularites = inParticularites;
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFPARTP";
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

        if (!JadeStringUtil.isEmpty(getDateDebutLessOrEqual())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MFDDEB<=" + this._dbWriteDateAMJ(statement.getTransaction(), getDateDebutLessOrEqual());
        }

        if (!JadeStringUtil.isEmpty(getExceptParaticulariteId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MFIPAR<>" + this._dbWriteNumeric(statement.getTransaction(), getExceptParaticulariteId());
        }

        if (!JadeStringUtil.isEmpty(getDateFinGreatOrEqual())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MFDFIN=0 OR MFDFIN is null OR MFDFIN>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getDateFinGreatOrEqual()) + ")";
        }

        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }

        if (getInAffiliationIds() != null && !getInAffiliationIds().isEmpty()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF in (" + Joiner.on(",").join(getInAffiliationIds()) + ")";
        }

        if (!JadeStringUtil.isEmpty(getForParticularite())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MFTPAR=" + this._dbWriteNumeric(statement.getTransaction(), getForParticularite());
        }

        if (getInParticularites() != null && !getInParticularites().isEmpty()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MFTPAR in (" + Joiner.on(",").join(inParticularites) + ")";
        }

        if (!JadeStringUtil.isEmpty(getFromDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MFDDEB>=" + this._dbWriteNumeric(statement.getTransaction(), getFromDateDebut());
        }

        if (!JadeStringUtil.isEmpty(getForDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MFDDEB=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
        }

        if (!JadeStringUtil.isEmpty(getForDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MFDFIN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin());
        }

        if (!JadeStringUtil.isEmpty(forDate)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MFDDEB<=" + this._dbWriteDateAMJ(statement.getTransaction(), forDate)
                    + " AND (MFDFIN=0 OR MFDFIN>=" + this._dbWriteDateAMJ(statement.getTransaction(), forDate) + ")";
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
        AFParticulariteAffiliation part = new AFParticulariteAffiliation();
        return part;
    }

    public java.lang.String getDateDebutLessOrEqual() {
        return dateDebutLessOrEqual;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getDateFinGreatOrEqual() {
        return dateFinGreatOrEqual;
    }

    public java.lang.String getExceptParaticulariteId() {
        return exceptParaticulariteId;
    }

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    public java.lang.String getForDate() {
        return forDate;
    }

    public java.lang.String getForDateDebut() {
        return forDateDebut;
    }

    public java.lang.String getForDateFin() {
        return forDateFin;
    }

    public java.lang.String getForParticularite() {
        return forParticularite;
    }

    public java.lang.String getForParticulariteAffiliationId() {
        return forParticulariteAffiliationId;
    }

    public java.lang.String getFromDateDebut() {
        return fromDateDebut;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public String idAffCotPersAutreAgenceOuverte() throws Exception {
        setForParticularite(CodeSystem.PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE);
        this.find(BManager.SIZE_NOLIMIT);

        String idAffCotPersAutreAgence = "";
        if (size() >= 1) {
            for (int i = 1; i <= size(); i++) {
                idAffCotPersAutreAgence += ((AFParticulariteAffiliation) getEntity(i - 1)).getAffiliationId() + ",";
            }
            idAffCotPersAutreAgence = JadeStringUtil.remove(idAffCotPersAutreAgence,
                    idAffCotPersAutreAgence.length() - 1, 1);
        }
        return idAffCotPersAutreAgence;

    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setDateDebutLessOrEqual(java.lang.String newDateDebutLessOrEqual) {
        dateDebutLessOrEqual = newDateDebutLessOrEqual;
    }

    public void setDateFinGreatOrEqual(java.lang.String newDateFinGreatOrEqual) {
        dateFinGreatOrEqual = newDateFinGreatOrEqual;
    }

    public void setExceptParaticulariteId(java.lang.String exceptParaticulariteId) {
        this.exceptParaticulariteId = exceptParaticulariteId;
    }

    public void setForAffiliationId(java.lang.String newForAffiliationId) {
        forAffiliationId = newForAffiliationId;
    }

    public void setForDate(java.lang.String newforDate) {
        forDate = newforDate;
    }

    public void setForDateDebut(java.lang.String string) {
        forDateDebut = string;
    }

    public void setForDateFin(java.lang.String string) {
        forDateFin = string;
    }

    public void setForParticularite(java.lang.String string) {
        forParticularite = string;
    }

    public void setForParticulariteAffiliationId(java.lang.String newForParticulariteAffiliationId) {
        forParticulariteAffiliationId = newForParticulariteAffiliationId;
    }

    public void setFromDateDebut(java.lang.String newFromDateDebut) {
        fromDateDebut = newFromDateDebut;
    }

    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

}
