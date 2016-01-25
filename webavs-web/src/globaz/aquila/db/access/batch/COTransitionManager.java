package globaz.aquila.db.access.batch;

import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import java.util.Iterator;

/**
 * Représente un container de type Transition.
 * 
 * @author Arnaud Dostes, 11-oct-2004
 */
public class COTransitionManager extends COBManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 462432952419626402L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean forAuto = null;
    private String forDuree = "";
    private String forFormSnippet = "";
    private String forIdEtape = "";
    private String forIdEtapeSuivante = "";
    private Collection forIdEtapeSuivanteIn;
    private String forIdTransition = "";
    private Boolean forManuel = null;
    private String forPriorite = "";
    private String forTransitionAction = "";
    private Boolean fromAuto = null;
    private String fromDuree = "";
    private String fromFormSnippet = "";
    private String fromIdEtape = "";
    private String fromIdEtapeSuivante = "";
    private String fromIdTransition = "";
    private Boolean fromManuel = null;
    private String fromPriorite = "";
    private String fromTransitionAction = "";
    private String orderBy = COTransition.FNAME_PRIORITE + " desc";

    private Boolean orderByLibEtapeCSOrder = Boolean.FALSE;
    private String orIdEtapePrecedent = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer buffer = new StringBuffer(_getCollection() + COTransition.TABLE_NAME);

        if (orderByLibEtapeCSOrder.booleanValue()) {
            // jointure avec les étapes suivantes
            buffer.append(" INNER JOIN ");
            buffer.append(_getCollection());
            buffer.append(ICOEtapeConstante.TABLE_NAME);
            buffer.append(" ON ");
            buffer.append(_getCollection());
            buffer.append(ICOEtapeConstante.TABLE_NAME);
            buffer.append(".");
            buffer.append(ICOEtapeConstante.FNAME_ID_ETAPE);
            buffer.append("=");
            buffer.append(COTransition.FNAME_ID_ETAPE_SUIVANTE);

            // jointure avec les codes systèmes
            buffer.append(" INNER JOIN ");
            buffer.append(_getCollection());
            buffer.append("FWCOSP ON ");
            buffer.append(ICOEtapeConstante.FNAME_LIBETAPE);
            buffer.append("=");
            buffer.append(_getCollection());
            buffer.append("FWCOSP.PCOSID");
        }

        return buffer.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForIdTransition())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_ID_TRANSITION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTransition());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForIdEtape())) {
            String prefix = "";
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if ((orderByLibEtapeCSOrder != null) && orderByLibEtapeCSOrder.booleanValue()) {
                // sinon ambiguité sur le nom de colonne ODIETA
                prefix += _getCollection() + COTransition.TABLE_NAME + ".";
            }

            if (!JadeStringUtil.isEmpty(getOrIdEtapePrecedent())) {
                sqlWhere += "(";
            }

            sqlWhere += prefix + COTransition.FNAME_ID_ETAPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdEtape());

            if (!JadeStringUtil.isEmpty(getOrIdEtapePrecedent())) {
                sqlWhere += " OR ";
                sqlWhere += prefix;
                sqlWhere += COTransition.FNAME_ID_ETAPE + "="
                        + this._dbWriteNumeric(statement.getTransaction(), getOrIdEtapePrecedent());
                sqlWhere += ")";
                sqlWhere += " AND NOT (";
                sqlWhere += prefix;
                sqlWhere += COTransition.FNAME_ID_ETAPE_SUIVANTE + "="
                        + this._dbWriteNumeric(statement.getTransaction(), getForIdEtape());
                sqlWhere += " AND ";
                sqlWhere += prefix;
                sqlWhere += COTransition.FNAME_ID_ETAPE + "="
                        + this._dbWriteNumeric(statement.getTransaction(), getOrIdEtapePrecedent());
                sqlWhere += ")";
            }
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForIdEtapeSuivante())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_ID_ETAPE_SUIVANTE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdEtapeSuivante());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForFormSnippet())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_FORM_SNIPPET + "="
                    + this._dbWriteString(statement.getTransaction(), getForFormSnippet());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForDuree())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_DUREE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForDuree());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForTransitionAction())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_TRANSITION_ACTION + "="
                    + this._dbWriteString(statement.getTransaction(), getForTransitionAction());
        }

        // traitement du positionnement
        if (getForManuel() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_MANUEL + "="
                    + this._dbWriteBoolean(statement.getTransaction(), getForManuel(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        // traitement du positionnement
        if (getForAuto() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_AUTO + "="
                    + this._dbWriteBoolean(statement.getTransaction(), getForAuto(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForPriorite())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_PRIORITE + "="
                    + this._dbWriteString(statement.getTransaction(), getForPriorite());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getFromIdTransition())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_ID_TRANSITION + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdTransition());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getFromIdEtape())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_ID_ETAPE + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdEtape());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getFromIdEtapeSuivante())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_ID_ETAPE_SUIVANTE + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdEtapeSuivante());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getFromFormSnippet())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_FORM_SNIPPET + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromFormSnippet());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getFromDuree())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_DUREE + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromDuree());
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getFromTransitionAction())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_TRANSITION_ACTION + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromTransitionAction());
        }

        // traitement du positionnement
        if (getFromManuel() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_MANUEL
                    + ">="
                    + this._dbWriteBoolean(statement.getTransaction(), getFromManuel(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        // traitement du positionnement
        if (getFromAuto() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_AUTO + ">="
                    + this._dbWriteBoolean(statement.getTransaction(), getFromAuto(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getFromPriorite())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += COTransition.FNAME_PRIORITE + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromPriorite());
        }

        if ((forIdEtapeSuivanteIn != null) && !forIdEtapeSuivanteIn.isEmpty()) {
            StringBuffer buffer = new StringBuffer(sqlWhere);

            if (buffer.length() != 0) {
                buffer.append(" AND ");
            }

            buffer.append(COTransition.FNAME_ID_ETAPE_SUIVANTE);
            buffer.append(" IN (");

            for (Iterator idIter = forIdEtapeSuivanteIn.iterator(); idIter.hasNext();) {
                buffer.append((String) idIter.next());

                if (idIter.hasNext()) {
                    buffer.append(",");
                }
            }

            buffer.append(")");
            sqlWhere += buffer.toString();
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COTransition();
    }

    /**
     * @return La valeur courante de la propriété
     */
    public Boolean getForAuto() {
        return forAuto;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForDuree() {
        return forDuree;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForFormSnippet() {
        return forFormSnippet;
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
    public String getForIdEtapeSuivante() {
        return forIdEtapeSuivante;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public Collection getForIdEtapeSuivanteIn() {
        return forIdEtapeSuivanteIn;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdTransition() {
        return forIdTransition;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public Boolean getForManuel() {
        return forManuel;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForPriorite() {
        return forPriorite;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForTransitionAction() {
        return forTransitionAction;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public Boolean getFromAuto() {
        return fromAuto;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromDuree() {
        return fromDuree;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromFormSnippet() {
        return fromFormSnippet;
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
    public String getFromIdEtapeSuivante() {
        return fromIdEtapeSuivante;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdTransition() {
        return fromIdTransition;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public Boolean getFromManuel() {
        return fromManuel;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromPriorite() {
        return fromPriorite;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromTransitionAction() {
        return fromTransitionAction;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @see #setOrderByLibEtapeCSOrder(Boolean)
     */
    public String getOrderByLibEtapeCSOrder() {
        return orderByLibEtapeCSOrder.toString();
    }

    /**
     * A utiliser conjointement avec "getForIdEtape()". <br/>
     * 
     * @return the orIdEtapePrecedent
     */
    public String getOrIdEtapePrecedent() {
        return orIdEtapePrecedent;
    }

    /**
     * @param boolean1
     *            La nouvelle valeur de la propriété
     */
    public void setForAuto(Boolean boolean1) {
        forAuto = boolean1;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForDuree(String string) {
        forDuree = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForFormSnippet(String string) {
        forFormSnippet = string;
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
    public void setForIdEtapeSuivante(String string) {
        forIdEtapeSuivante = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param forIdEtapeSuivanteIn
     *            DOCUMENT ME!
     */
    public void setForIdEtapeSuivanteIn(Collection forIdEtapeSuivanteIn) {
        this.forIdEtapeSuivanteIn = forIdEtapeSuivanteIn;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdTransition(String string) {
        forIdTransition = string;
    }

    /**
     * @param boolean1
     *            La nouvelle valeur de la propriété
     */
    public void setForManuel(Boolean boolean1) {
        forManuel = boolean1;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForPriorite(String string) {
        forPriorite = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForTransitionAction(String string) {
        forTransitionAction = string;
    }

    /**
     * @param boolean1
     *            La nouvelle valeur de la propriété
     */
    public void setFromAuto(Boolean boolean1) {
        fromAuto = boolean1;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromDuree(String string) {
        fromDuree = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromFormSnippet(String string) {
        fromFormSnippet = string;
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
    public void setFromIdEtapeSuivante(String string) {
        fromIdEtapeSuivante = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdTransition(String string) {
        fromIdTransition = string;
    }

    /**
     * @param boolean1
     *            La nouvelle valeur de la propriété
     */
    public void setFromManuel(Boolean boolean1) {
        fromManuel = boolean1;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromPriorite(String string) {
        fromPriorite = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromTransitionAction(String string) {
        fromTransitionAction = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     *            DOCUMENT ME!
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

    /**
     * "true" pour que la liste des transitions sorte dans l'ordre des codes systèmes de l'étape suivante.
     * 
     * @param orderByLibEtapeCSOrder
     *            "true" pour vrai, "" ou null pour indifférent et n'importe quoi d'autre pour faux
     */
    public void setOrderByLibEtapeCSOrder(String orderByLibEtapeCSOrder) {
        this.orderByLibEtapeCSOrder = new Boolean(orderByLibEtapeCSOrder);

        if (this.orderByLibEtapeCSOrder.booleanValue()) {
            orderBy = "PCONCS";
        } else if ("PCONCS".equals(orderBy)) {
            orderBy = "";
        }
    }

    /**
     * A utiliser conjointement avec "getForIdEtape()". <br/>
     * 
     * @param orIdEtapePrecedent
     *            the orIdEtapePrecedent to set
     */
    public void setOrIdEtapePrecedent(String orIdEtapePrecedent) {
        this.orIdEtapePrecedent = orIdEtapePrecedent;
    }

}
