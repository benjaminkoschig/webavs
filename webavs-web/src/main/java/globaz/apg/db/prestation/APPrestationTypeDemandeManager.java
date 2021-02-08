package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.db.demandes.PRDemande;

/**
 */
public class APPrestationTypeDemandeManager extends APPrestationJointDroitManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String POINT = ".";
    public static final String EGAL = "=";
    public static final String ON = " ON ";
    public static final String AND = " AND ";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    private String forTypeDemande;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APPrestationManager.
     */
    public APPrestationTypeDemandeManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String tablePrestation = _getCollection() + APPrestation.TABLE_NAME;
        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;
        String tableDemande = _getCollection() + PRDemande.TABLE_NAME;

        StringBuilder sql = new StringBuilder(tablePrestation);

        // jointure entre tables prestation droits et demande
        sql.append(INNER_JOIN);
        sql.append(tableDroitLAPG);
        sql.append(ON);
        sql.append(tablePrestation).append(POINT).append(APPrestation.FIELDNAME_IDDROIT);
        sql.append(EGAL);
        sql.append(tableDroitLAPG).append(POINT).append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        sql.append(INNER_JOIN);
        sql.append(tableDemande);
        sql.append(ON);
        sql.append(tableDroitLAPG).append(POINT).append(APDroitLAPG.FIELDNAME_IDDEMANDE);
        sql.append(EGAL);
        sql.append(tableDemande).append(POINT).append(PRDemande.FIELDNAME_IDDEMANDE);

        return sql.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder superWhere = new StringBuilder(super._getWhere(statement));

        if (!JadeStringUtil.isEmpty(getForTypeDemande())) {
            if (superWhere.length() != 0) {
                superWhere.append(AND);
            }

            superWhere.append(_getCollection()).append(PRDemande.TABLE_NAME).append(".")
                    .append(PRDemande.FIELDNAME_TYPE_DEMANDE);
            superWhere.append(EGAL);
            superWhere.append(this._dbWriteNumeric(statement.getTransaction(), getForTypeDemande()));
        }

        return superWhere.toString();
    }

    /**
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BManager#_newEntity()
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPrestation();
    }

    /**
     * (non-Javadoc)
     *
     * @see PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APPrestation.FIELDNAME_IDPRESTATIONAPG;
    }

    public String getForTypeDemande() {
        return forTypeDemande;
    }

    public void setForTypeDemande(String forTypeDemande) {
        this.forTypeDemande = forTypeDemande;
    }
}
