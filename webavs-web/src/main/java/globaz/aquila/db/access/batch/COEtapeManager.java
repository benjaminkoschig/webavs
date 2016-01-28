package globaz.aquila.db.access.batch;

import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <h1>Description</h1>
 * <p>
 * Manager de recherche des étapes.
 * </p>
 * 
 * @author Arnaud Dostes, 11-oct-2004
 */
public class COEtapeManager extends COBManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -3945162541056916572L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List forAlwaysLibEtapeIn;
    private Boolean forExisteTransitionAutoVers;
    private String forIdEtape = "";
    private String forIdSequence = "";
    private String forLibAction = "";
    private String forLibEtape = "";
    private String forLibSequence = "";
    private List forNeverLibEtapeIn;
    private String order = "";
    private Boolean orderByLibEtapeCSOrder = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer();

        from.append(_getCollection());
        from.append(ICOEtapeConstante.TABLE_NAME);

        if (!JadeStringUtil.isEmpty(forLibSequence)) {
            from.append(" INNER JOIN ");
            from.append(_getCollection());
            from.append(ICOSequenceConstante.TABLE_NAME);
            from.append(" ON ");
            from.append(_getCollection());
            from.append(ICOEtapeConstante.TABLE_NAME);
            from.append(".");
            from.append(ICOEtapeConstante.FNAME_ID_SEQUENCE);
            from.append("=");
            from.append(_getCollection());
            from.append(ICOSequenceConstante.TABLE_NAME);
            from.append(".");
            from.append(ICOSequenceConstante.FNAME_ID_SEQUENCE);
        }

        if (orderByLibEtapeCSOrder.booleanValue()) {
            from.append(" INNER JOIN ");
            from.append(_getCollection());
            from.append("FWCOSP ON ");
            from.append(ICOEtapeConstante.FNAME_LIBETAPE);
            from.append("=");
            from.append(_getCollection());
            from.append("FWCOSP.PCOSID");
        }

        return from.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer buffer = new StringBuffer();

        addCritereIdSequence(statement, buffer);
        addCritereLibSequence(statement, buffer);

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForIdEtape())) {
            if (buffer.length() != 0) {
                buffer.append(" AND ");
            }

            buffer.append(ICOEtapeConstante.FNAME_ID_ETAPE);
            buffer.append("=");
            buffer.append(this._dbWriteNumeric(statement.getTransaction(), getForIdEtape()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForLibAction())) {
            if (buffer.length() != 0) {
                buffer.append(" AND ");
            }

            buffer.append(ICOEtapeConstante.FNAME_LIBACTION);
            buffer.append("=");
            buffer.append(this._dbWriteNumeric(statement.getTransaction(), getForLibAction()));
        }

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForLibEtape())) {
            if (buffer.length() != 0) {
                buffer.append(" AND ");
            }

            buffer.append(ICOEtapeConstante.FNAME_LIBETAPE);
            buffer.append("=");
            buffer.append(this._dbWriteNumeric(statement.getTransaction(), getForLibEtape()));
        }

        if (forExisteTransitionAutoVers != null) {
            if (buffer.length() != 0) {
                buffer.append(" AND ");
            }

            buffer.append(ICOEtapeConstante.FNAME_ID_ETAPE);
            buffer.append(" IN (SELECT ");
            buffer.append(COTransition.FNAME_ID_ETAPE_SUIVANTE);
            buffer.append(" FROM ");
            buffer.append(_getCollection());
            buffer.append(COTransition.TABLE_NAME);
            buffer.append(" WHERE ");
            buffer.append(COTransition.FNAME_AUTO);
            buffer.append("=");
            buffer.append(this._dbWriteBoolean(statement.getTransaction(), forExisteTransitionAutoVers,
                    BConstants.DB_TYPE_BOOLEAN_CHAR));
            buffer.append(")");
        }

        if ((forNeverLibEtapeIn != null) && !forNeverLibEtapeIn.isEmpty()) {
            if (buffer.length() != 0) {
                buffer.append(" AND ");
            }

            buffer.append(ICOEtapeConstante.FNAME_LIBETAPE);
            buffer.append(" NOT IN (");

            for (Iterator idIter = forNeverLibEtapeIn.iterator(); idIter.hasNext();) {
                buffer.append(this._dbWriteNumeric(statement.getTransaction(), (String) idIter.next()));

                if (idIter.hasNext()) {
                    buffer.append(",");
                }
            }

            buffer.append(")");
        }

        /*
         * ATTENTION !!!!!!!!!!!! ce critère DOIT TOUJOURS être le dernier de cette méthode et DOIT TOUJOURS être suivi
         * du critère de séquence
         */
        if ((forAlwaysLibEtapeIn != null) && !forAlwaysLibEtapeIn.isEmpty()) {
            if (buffer.length() != 0) {
                /*
                 * si on avait pas encore de critères ici, c'est qu'on est sûr que les étapes étaient toutes retournées
                 * par contre, si on a d'autres critères, on va forcer le retour des étapes indiquées en ajoutant un ou
                 * logique
                 */
                buffer.insert(0, "(");
                buffer.append(") OR (");
                buffer.append(ICOEtapeConstante.FNAME_LIBETAPE);
                buffer.append(" IN (");

                for (Iterator idIter = forAlwaysLibEtapeIn.iterator(); idIter.hasNext();) {
                    buffer.append(this._dbWriteNumeric(statement.getTransaction(), (String) idIter.next()));

                    if (idIter.hasNext()) {
                        buffer.append(",");
                    }
                }

                buffer.append(")");
                addCritereIdSequence(statement, buffer);
                addCritereLibSequence(statement, buffer);
                buffer.append(")");
            }
        }

        return buffer.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COEtape();
    }

    private void addCritereIdSequence(BStatement statement, StringBuffer buffer) {
        if (!JadeStringUtil.isEmpty(getForIdSequence())) {
            if (buffer.length() != 0) {
                buffer.append(" AND ");
            }

            if (!JadeStringUtil.isEmpty(forLibSequence)) {
                buffer.append(_getCollection());
                buffer.append(ICOEtapeConstante.FNAME_ID_SEQUENCE);
                buffer.append(".");
            }

            buffer.append(ICOEtapeConstante.FNAME_ID_SEQUENCE);
            buffer.append("=");
            buffer.append(this._dbWriteNumeric(statement.getTransaction(), getForIdSequence()));
        }
    }

    private void addCritereLibSequence(BStatement statement, StringBuffer buffer) {
        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForLibSequence())) {
            if (buffer.length() != 0) {
                buffer.append(" AND ");
            }

            buffer.append(ICOSequenceConstante.FNAME_LIB_SEQUENCE);
            buffer.append("=");
            buffer.append(this._dbWriteNumeric(statement.getTransaction(), getForLibSequence()));
        }
    }

    /**
     * Indique les cs d'etapes à inclure dans les résultats quels que soient les autres critères.
     * 
     * @param libEtape
     *            un code d'étape pour lequel l'étape doit toujours être retournée.
     */
    public void addForAlwaysLibEtapeIn(String libEtape) {
        if (forAlwaysLibEtapeIn == null) {
            forAlwaysLibEtapeIn = new LinkedList();
        }

        forAlwaysLibEtapeIn.add(libEtape);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param libEtape
     */
    public void addForNeverLibEtapeIn(String libEtape) {
        if (forNeverLibEtapeIn == null) {
            forNeverLibEtapeIn = new LinkedList();
        }

        forNeverLibEtapeIn.add(libEtape);
    }

    /**
     * @see #addForAlwaysLibEtapeIn(String)
     */
    public List getForAlwaysLibEtapeIn() {
        return forAlwaysLibEtapeIn;
    }

    /**
     * @see #setForExisteTransitionAutoVers(String)
     */
    public String getForExisteTransitionAutoVers() {
        return forExisteTransitionAutoVers.toString();
    }

    /**
     * @see #setForIdEtape(String)
     */
    public String getForIdEtape() {
        return forIdEtape;
    }

    /**
     * @see #setForIdSequence(String)
     */
    public String getForIdSequence() {
        return forIdSequence;
    }

    /**
     * @see #setForLibAction(String)
     */
    public String getForLibAction() {
        return forLibAction;
    }

    /**
     * @see #setForLibEtape(String)
     */
    public String getForLibEtape() {
        return forLibEtape;
    }

    /**
     * @see #setForLibSequence(String)
     */
    public String getForLibSequence() {
        return forLibSequence;
    }

    /**
     * @see #addForNeverLibEtapeIn(String)
     */
    public List getForNeverLibEtapeIn() {
        return forNeverLibEtapeIn;
    }

    /**
     * @see #setOrderByLibEtapeCSOrder(String)
     */
    public String getOrderByLibEtapeCSOrder() {
        return orderByLibEtapeCSOrder.toString();
    }

    /**
     * "true" pour ne retourner que les étapes qui sont l'étape d'arrivée d'au moins une transition automatique,
     * n'importe quelle chaîne non vide pour le contraire, null ou chaîne vide pour ne pas activer le critère.
     * 
     * @param forExisteTransitionAutoVers
     *            "true" pour vrai
     */
    public void setForExisteTransitionAutoVers(String forExisteTransitionAutoVers) {
        if (JadeStringUtil.isEmpty(forExisteTransitionAutoVers)) {
            this.forExisteTransitionAutoVers = null;
        } else {
            this.forExisteTransitionAutoVers = new Boolean(forExisteTransitionAutoVers);
        }
    }

    /**
     * @param string
     *            un identifiant d'étape
     */
    public void setForIdEtape(String string) {
        forIdEtape = string;
    }

    /**
     * @param string
     *            l'identifiant de la séquence pour laquelle retourner les étapes
     */
    public void setForIdSequence(String string) {
        forIdSequence = string;
    }

    /**
     * @param string
     *            le cs de l'action pour laquelle retourner l'étape
     */
    public void setForLibAction(String string) {
        forLibAction = string;
    }

    /**
     * @param string
     *            le cs de l'étape pour laquelle retourner l'étape
     */
    public void setForLibEtape(String string) {
        forLibEtape = string;
    }

    /**
     * @param forLibSequence
     *            le cs de la séquence pour laquelle retourner les étapes
     */
    public void setForLibSequence(String forLibSequence) {
        this.forLibSequence = forLibSequence;
    }

    /**
     * @param orderByLibEtapeCSOrder
     *            "true" pour trier d'après l'ordre des cs des étapes
     */
    public void setOrderByLibEtapeCSOrder(String orderByLibEtapeCSOrder) {
        this.orderByLibEtapeCSOrder = new Boolean(orderByLibEtapeCSOrder);

        if (this.orderByLibEtapeCSOrder.booleanValue()) {
            order = "PCONCS";
        } else if ("PCONCS".equals(order)) {
            order = "";
        }
    }

}
