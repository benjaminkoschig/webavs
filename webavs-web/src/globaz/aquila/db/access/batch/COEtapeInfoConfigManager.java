package globaz.aquila.db.access.batch;

import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOSequenceConstante;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COEtapeInfoConfigManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 7442754042437377045L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forCsLibelle;
    private String forIdEtape;
    private Collection forIdEtapeIn;
    private String forLibAction;
    private String forLibEtape;
    private String forLibSequence;

    private String orderBy = COEtapeInfoConfig.FNAME_IDETAPE + "," + COEtapeInfoConfig.FNAME_ORDRE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer();

        from.append(_getCollection());
        from.append(COEtapeInfoConfig.TABLE_NAME_CONFIG);

        // jointure avec la table des étapes si nécessaire
        if (!JadeStringUtil.isEmpty(forLibEtape) || !JadeStringUtil.isEmpty(forLibAction)
                || !JadeStringUtil.isEmpty(forLibSequence)) {
            from.append(" INNER JOIN ");
            from.append(_getCollection());
            from.append(ICOEtapeConstante.TABLE_NAME);
            from.append(" ON ");
            from.append(_getCollection());
            from.append(COEtapeInfoConfig.TABLE_NAME_CONFIG);
            from.append(".");
            from.append(COEtapeInfoConfig.FNAME_IDETAPE);
            from.append("=");
            from.append(_getCollection());
            from.append(ICOEtapeConstante.TABLE_NAME);
            from.append(".");
            from.append(ICOEtapeConstante.FNAME_ID_ETAPE);

            // jointure avec la table des séquences si nécessaire
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
        }

        return from.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();

        if (!JadeStringUtil.isEmpty(forIdEtape)) {
            if (where.length() > 0) {
                where.append(" AND ");
            }

            where.append(COEtapeInfoConfig.FNAME_IDETAPE);
            where.append("=");
            where.append(this._dbWriteNumeric(statement.getTransaction(), forIdEtape));
        }

        if (!JadeStringUtil.isEmpty(forLibEtape)) {
            if (where.length() > 0) {
                where.append(" AND ");
            }

            where.append(_getCollection());
            where.append(ICOEtapeConstante.TABLE_NAME);
            where.append(".");
            where.append(ICOEtapeConstante.FNAME_LIBETAPE);
            where.append("=");
            where.append(this._dbWriteNumeric(statement.getTransaction(), forLibEtape));
        }

        if (!JadeStringUtil.isEmpty(forLibAction)) {
            if (where.length() > 0) {
                where.append(" AND ");
            }

            where.append(_getCollection());
            where.append(ICOEtapeConstante.TABLE_NAME);
            where.append(".");
            where.append(ICOEtapeConstante.FNAME_LIBACTION);
            where.append("=");
            where.append(this._dbWriteNumeric(statement.getTransaction(), forLibAction));
        }

        if (!JadeStringUtil.isEmpty(forLibSequence)) {
            if (where.length() > 0) {
                where.append(" AND ");
            }

            where.append(_getCollection());
            where.append(ICOSequenceConstante.TABLE_NAME);
            where.append(".");
            where.append(ICOSequenceConstante.FNAME_LIB_SEQUENCE);
            where.append("=");
            where.append(this._dbWriteNumeric(statement.getTransaction(), forLibSequence));
        }

        if (!JadeStringUtil.isEmpty(forCsLibelle)) {
            if (where.length() > 0) {
                where.append(" AND ");
            }

            where.append(_getCollection());
            where.append(COEtapeInfoConfig.TABLE_NAME_CONFIG);
            where.append(".");
            where.append(COEtapeInfoConfig.FNAME_CS_LIBELLE);
            where.append("=");
            where.append(this._dbWriteNumeric(statement.getTransaction(), forCsLibelle));
        }

        if ((forIdEtapeIn != null) && !forIdEtapeIn.isEmpty()) {
            if (where.length() != 0) {
                where.append(" AND ");
            }

            where.append(COEtapeInfoConfig.FNAME_IDETAPE);
            where.append(" IN (");

            for (Iterator idIter = forIdEtapeIn.iterator(); idIter.hasNext();) {
                where.append((String) idIter.next());

                if (idIter.hasNext()) {
                    where.append(",");
                }
            }

            where.append(")");
        }

        return where.toString();
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COEtapeInfoConfig();
    }

    /**
     * getter pour l'attribut for cs libelle.
     * 
     * @return la valeur courante de l'attribut for cs libelle
     */
    public String getForCsLibelle() {
        return forCsLibelle;
    }

    /**
     * getter pour l'attribut for id etape.
     * 
     * @return la valeur courante de l'attribut for id etape
     */
    public String getForIdEtape() {
        return forIdEtape;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public Collection getForIdEtapeIn() {
        return forIdEtapeIn;
    }

    public String getForLibAction() {
        return forLibAction;
    }

    /**
     * getter pour l'attribut for lib etape.
     * 
     * @return la valeur courante de l'attribut for lib etape
     */
    public String getForLibEtape() {
        return forLibEtape;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getForLibSequence() {
        return forLibSequence;
    }

    /**
     * getter pour l'attribut order by.
     * 
     * @return la valeur courante de l'attribut order by
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * setter pour l'attribut for cs libelle.
     * 
     * @param forCsLibelle
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsLibelle(String forCsLibelle) {
        this.forCsLibelle = forCsLibelle;
    }

    /**
     * setter pour l'attribut for id etape.
     * 
     * @param forIdEtape
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdEtape(String forIdEtape) {
        this.forIdEtape = forIdEtape;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param forIdEtapeIn
     *            DOCUMENT ME!
     */
    public void setForIdEtapeIn(Collection forIdEtapeIn) {
        this.forIdEtapeIn = forIdEtapeIn;
    }

    public void setForLibAction(String forLibAction) {
        this.forLibAction = forLibAction;
    }

    /**
     * setter pour l'attribut for lib etape.
     * 
     * @param forLibEtape
     *            une nouvelle valeur pour cet attribut
     */
    public void setForLibEtape(String forLibEtape) {
        this.forLibEtape = forLibEtape;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param forLibSequence
     *            DOCUMENT ME!
     */
    public void setForLibSequence(String forLibSequence) {
        this.forLibSequence = forLibSequence;
    }

    /**
     * setter pour l'attribut order by.
     * 
     * @param orderBy
     *            une nouvelle valeur pour cet attribut
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
