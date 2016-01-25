package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Représente un container de type Historique
 * 
 * @author Pascal Lovy, 06-oct-2004
 */
public class COHistoriqueManager extends COBManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String DESC = " DESC";
    private Boolean afficheEtapesAnnulees = new Boolean(false);
    private Boolean cacheImputerVersement = new Boolean(false);
    /** (forDatedeclenchement) */
    private String forDateDeclenchement = "";
    /** (forDateExecution) */
    private String forDateExecution = "";
    /** (forIdContentieux) */
    private String forIdContentieux = "";
    /** (forIdEtape) */
    private String forIdEtape = "";
    /** (forIdHistorique) */
    private String forIdHistorique = "";
    private String forIdSequence = "";
    /** (forMotif) */
    private String forMotif = "";
    /** (forSolde) */
    private String forSolde = "";
    private String forTriHistorique = "";
    /** (forUser) */
    private String forUser = "";
    /** (fromDatedeclenchement) */
    private String fromDateDeclenchement = "";
    /** (fromDateExecution) */
    private String fromDateExecution = "";
    /** (fromIdContentieux) */
    private String fromIdContentieux = "";
    /** (fromIdEtape) */
    private String fromIdEtape = "";

    /** (fromIdHistorique) */
    private String fromIdHistorique = "";
    /** (fromMotif) */
    private String fromMotif = "";

    /** (fromSolde) */
    private String fromSolde = "";

    /** (fromUser) */
    private String fromUser = "";
    private Boolean ignorerDateExecution = new Boolean(false);

    /** Champs de COEtapp */
    private String notForLibEtape = "";
    private String notForTypeEtape = "";
    private String orderBy = "";

    private Boolean triDecroissant = new Boolean(false);

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String fromClause = _getCollection() + ICOHistoriqueConstante.TABLE_NAME;

        if (getIgnorerDateExecution().booleanValue() || !JadeStringUtil.isBlank(getNotForLibEtape())
                || !JadeStringUtil.isBlank(getNotForTypeEtape())) {
            fromClause += COBManager.INNER_JOIN + _getCollection() + ICOEtapeConstante.TABLE_NAME + COBManager.ON
                    + _getCollection() + ICOHistoriqueConstante.TABLE_NAME + "."
                    + ICOHistoriqueConstante.FNAME_ID_ETAPE + "=" + _getCollection() + ICOEtapeConstante.TABLE_NAME
                    + "." + ICOEtapeConstante.FNAME_ID_ETAPE;
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (getForTriHistorique().equals("1")) {
            orderBy = ICOHistoriqueConstante.FNAME_ID_HISTORIQUE;
        } else if (getForTriHistorique().equals("2")) {
            orderBy = ICOHistoriqueConstante.FNAME_DATE_EXECUTION;
        }

        if (JadeStringUtil.isBlank(orderBy)) {
            orderBy = ICOHistoriqueConstante.FNAME_ID_HISTORIQUE;
        }

        if (getTriDecroissant().booleanValue()) {
            orderBy += COHistoriqueManager.DESC;
        }

        return orderBy;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");

        // traitement du positionnement
        if (getForIdHistorique().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_ID_HISTORIQUE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdHistorique()));
        }

        // traitement du positionnement
        if (getForIdContentieux().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdContentieux()));
        }

        // traitement du positionnement
        if (getForIdEtape().length() != 0) {
            addCondition(sqlWhere,
                    _getCollection() + ICOHistoriqueConstante.TABLE_NAME + "." + ICOHistoriqueConstante.FNAME_ID_ETAPE
                            + "=" + this._dbWriteNumeric(statement.getTransaction(), getForIdEtape()));
        }

        // traitement du positionnement
        if (getForDateDeclenchement().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_DATE_DECLENCHEMENT + "="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDeclenchement()));
        }

        // traitement du positionnement
        if (getForDateExecution().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_DATE_EXECUTION + "="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getForDateExecution()));
        }

        // traitement du positionnement
        if (getForSolde().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_SOLDE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForSolde()));
        }

        // traitement du positionnement
        if (getForUser().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_USERNAME + "="
                            + this._dbWriteString(statement.getTransaction(), getForUser()));
        }

        // traitement du positionnement
        if (getForMotif().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_MOTIF + "="
                            + this._dbWriteString(statement.getTransaction(), getForMotif()));
        }

        // traitement du positionnement
        if (getFromIdHistorique().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_ID_HISTORIQUE + ">="
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdHistorique()));
        }

        // traitement du positionnement
        if (getFromIdContentieux().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX + ">="
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdContentieux()));
        }

        // traitement du positionnement
        if (getFromIdEtape().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_ID_ETAPE + ">="
                            + this._dbWriteNumeric(statement.getTransaction(), getFromIdEtape()));
        }

        // traitement du positionnement
        if (getFromDateDeclenchement().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_DATE_DECLENCHEMENT + ">="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDeclenchement()));
        }

        // traitement du positionnement
        if (getFromDateExecution().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_DATE_EXECUTION + ">="
                            + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateExecution()));
        }

        // traitement du positionnement
        if (getFromSolde().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_SOLDE + ">="
                            + this._dbWriteNumeric(statement.getTransaction(), getFromSolde()));
        }

        // traitement du positionnement
        if (getFromUser().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_USERNAME + ">="
                            + this._dbWriteString(statement.getTransaction(), getFromUser()));
        }

        // traitement du positionnement
        if (getFromMotif().length() != 0) {
            addCondition(
                    sqlWhere,
                    ICOHistoriqueConstante.FNAME_MOTIF + ">="
                            + this._dbWriteString(statement.getTransaction(), getFromMotif()));
        }

        if (getNotForLibEtape().length() != 0) {
            addCondition(
                    sqlWhere,
                    "(" + _getCollection() + ICOEtapeConstante.TABLE_NAME + "." + ICOEtapeConstante.FNAME_LIBETAPE
                            + "<>" + this._dbWriteNumeric(statement.getTransaction(), getNotForLibEtape())
                            + COBManager.OR + "(" + _getCollection() + ICOEtapeConstante.TABLE_NAME + "."
                            + ICOEtapeConstante.FNAME_LIBETAPE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getNotForLibEtape()) + COBManager.AND
                            + ICOHistoriqueConstante.FNAME_MOTIF + "<>"
                            + this._dbWriteString(statement.getTransaction(), "") + "))");
        }

        if (!JadeStringUtil.isBlank(getNotForTypeEtape())) {
            addCondition(sqlWhere,
                    _getCollection() + ICOEtapeConstante.TABLE_NAME + "." + ICOEtapeConstante.FNAME_TYPE_ETAPE + "<>"
                            + this._dbWriteNumeric(statement.getTransaction(), getNotForTypeEtape()));
        }

        if (forIdSequence.length() != 0) {
            addCondition(
                    sqlWhere,
                    _getCollection() + ICOHistoriqueConstante.TABLE_NAME + "."
                            + ICOHistoriqueConstante.FNAME_ID_SEQUENCE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), forIdSequence));
        }

        if (!getAfficheEtapesAnnulees().booleanValue()) {
            addCondition(sqlWhere, _getCollection() + ICOHistoriqueConstante.TABLE_NAME + "."
                    + ICOHistoriqueConstante.FNAME_EST_ANNULE + COBManager.LIKE + BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        }

        if (getCacheImputerVersement().booleanValue()) {
            addCondition(sqlWhere, _getCollection() + ICOHistoriqueConstante.TABLE_NAME + "."
                    + ICOHistoriqueConstante.FNAME_ETAPE_SANS_INFLUENCE + COBManager.LIKE
                    + BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        }

        if (getIgnorerDateExecution().booleanValue()) {
            addCondition(sqlWhere, _getCollection() + ICOEtapeConstante.TABLE_NAME + "."
                    + ICOEtapeConstante.FNAME_IGNORER_DATE_EXECUTION + COBManager.LIKE
                    + BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COHistorique();
    }

    /**
     * @return the forIsAnnule
     */
    public Boolean getAfficheEtapesAnnulees() {
        return afficheEtapesAnnulees;
    }

    /**
     * @return the afficheImputerVersement
     */
    public Boolean getCacheImputerVersement() {
        return cacheImputerVersement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateDeclenchement() {
        return forDateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDateExecution() {
        return forDateExecution;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdContentieux() {
        return forIdContentieux;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdEtape() {
        return forIdEtape;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdHistorique() {
        return forIdHistorique;
    }

    public String getForIdSequence() {
        return forIdSequence;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForMotif() {
        return forMotif;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForSolde() {
        return forSolde;
    }

    /**
     * @return the forTriHistorique
     */
    public String getForTriHistorique() {
        return forTriHistorique;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForUser() {
        return forUser;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDateDeclenchement() {
        return fromDateDeclenchement;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDateExecution() {
        return fromDateExecution;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdContentieux() {
        return fromIdContentieux;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdEtape() {
        return fromIdEtape;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdHistorique() {
        return fromIdHistorique;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromMotif() {
        return fromMotif;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromSolde() {
        return fromSolde;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * @return the ignorerDateExecution
     */
    public Boolean getIgnorerDateExecution() {
        return ignorerDateExecution;
    }

    public String getNotForLibEtape() {
        return notForLibEtape;
    }

    /**
     * @return the notForTypeEtape
     */
    public String getNotForTypeEtape() {
        return notForTypeEtape;
    }

    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @return the forIsAnnule
     */
    public Boolean getTriDecroissant() {
        return triDecroissant;
    }

    /**
     * @param value
     *            forIsAnnule the forIsAnnule to set
     */
    public void setAfficheEtapesAnnulees(Boolean value) {
        afficheEtapesAnnulees = value;
    }

    /**
     * @param value
     *            forIsAnnule the forIsAnnule to set
     */
    public void setAfficheEtapesAnnulees(String value) {
        try {
            this.setAfficheEtapesAnnulees(Boolean.valueOf(value));
        } catch (Exception ex) {
            this.setAfficheEtapesAnnulees(new Boolean(true));
        }
    }

    /**
     * @param afficheImputerVersement
     *            the afficheImputerVersement to set
     */
    public void setCacheImputerVersement(Boolean afficheImputerVersement) {
        cacheImputerVersement = afficheImputerVersement;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateDeclenchement(String string) {
        forDateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDateExecution(String string) {
        forDateExecution = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdContentieux(String string) {
        forIdContentieux = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdEtape(String string) {
        forIdEtape = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdHistorique(String string) {
        forIdHistorique = string;
    }

    public void setForIdSequence(String string) {
        forIdSequence = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForMotif(String string) {
        forMotif = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForSolde(String string) {
        forSolde = string;
    }

    /**
     * @param forTriHistorique
     *            the forTriHistorique to set
     */
    public void setForTriHistorique(String forTriHistorique) {
        this.forTriHistorique = forTriHistorique;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForUser(String string) {
        forUser = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDateDeclenchement(String string) {
        fromDateDeclenchement = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDateExecution(String string) {
        fromDateExecution = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdContentieux(String string) {
        fromIdContentieux = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdEtape(String string) {
        fromIdEtape = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdHistorique(String string) {
        fromIdHistorique = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromMotif(String string) {
        fromMotif = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromSolde(String string) {
        fromSolde = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromUser(String string) {
        fromUser = string;
    }

    /**
     * @param ignorerDateExecution
     *            the ignorerDateExecution to set
     */
    public void setIgnorerDateExecution(Boolean ignorerDateExecution) {
        this.ignorerDateExecution = ignorerDateExecution;
    }

    public void setNotForLibEtape(String string) {
        notForLibEtape = string;
    }

    /**
     * @param notForTypeEtape
     *            the notForTypeEtape to set
     */
    public void setNotForTypeEtape(String notForTypeEtape) {
        this.notForTypeEtape = notForTypeEtape;
    }

    public void setOrderBy(String string) {
        orderBy = string;
    }

    /**
     * @param value
     *            forIsAnnule the forIsAnnule to set
     */
    public void setTriDecroissant(String value) {
        try {
            triDecroissant = Boolean.valueOf(value);
        } catch (Exception ex) {
            triDecroissant = new Boolean(false);
        }
    }
}
