package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * Descpription
 * 
 * @author scr Date de création 23 mai 05
 */
public class APDroitLAPGManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande = null;
    private String forIdDroit = null;
    private String forIdDroitParent = null;
    /**
     */
    protected String orderBy = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDroitLAPGManager.
     */
    public APDroitLAPGManager() {
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
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdDroit())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APDroitLAPG.FIELDNAME_IDDROIT_LAPG + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDroit());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdDroitParent())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDroitParent());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdDemande())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APDroitLAPG.FIELDNAME_IDDEMANDE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDemande());
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APDroitLAPG();
    }

    /**
     * getter pour l'attribut for id demande
     * 
     * @return la valeur courante de l'attribut for id demande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * getter pour l'attribut for id droit
     * 
     * @return la valeur courante de l'attribut for id droit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * getter pour l'attribut for id droit parent
     * 
     * @return la valeur courante de l'attribut for id droit parent
     */
    public String getForIdDroitParent() {
        return forIdDroitParent;
    }

    /**
     * getter pour l'attribut order by
     * 
     * @return la valeur courante de l'attribut order by
     */
    @Override
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APDroitLAPG.FIELDNAME_IDDROIT_LAPG;
    }

    /**
     * setter pour l'attribut for id demande
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDemande(String string) {
        forIdDemande = string;
    }

    /**
     * setter pour l'attribut for id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String string) {
        forIdDroit = string;
    }

    /**
     * setter pour l'attribut for id droit parent
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroitParent(String string) {
        forIdDroitParent = string;
    }

    /**
     * setter pour l'attribut order by
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setOrderBy(String string) {
        orderBy = string;
    }
}
