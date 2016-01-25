package globaz.ij.db.prononces;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.db.employeurs.PRAbstractEmployeur;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJEmployeur extends PRAbstractEmployeur implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 
     */
    public static final String FIELDNAME_ID_AFFILIE = "XIIAFF";

    /** 
     */
    public static final String FIELDNAME_ID_EMPLOYEUR = "XIIEMP";

    /** 
     */
    public static final String FIELDNAME_ID_PARTICULARITE = "XIIAPA";

    /** 
     */
    public static final String FIELDNAME_ID_TIERS = "XIITIE";

    /** 
     */
    public static final String TABLE_NAME = "IJEMPLOY";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdEmployeur(_incCounter(transaction, "0"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdEmployeur(statement.dbReadNumeric(FIELDNAME_ID_EMPLOYEUR));
        setIdTiers(statement.dbReadNumeric(FIELDNAME_ID_TIERS));
        setIdAffilie(statement.dbReadNumeric(FIELDNAME_ID_AFFILIE));
        setIdParticularite(statement.dbReadNumeric(FIELDNAME_ID_PARTICULARITE));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getIdTiers(), getSession().getLabel("ERREUR_CHARGEMENT_TIERS"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_EMPLOYEUR, getIdEmployeur());
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_EMPLOYEUR,
                _dbWriteNumeric(statement.getTransaction(), getIdEmployeur(), "idEmployeur"));
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField(FIELDNAME_ID_AFFILIE,
                _dbWriteNumeric(statement.getTransaction(), getIdAffilie(), "idAffilie"));
        statement.writeField(FIELDNAME_ID_PARTICULARITE,
                _dbWriteNumeric(statement.getTransaction(), getIdParticularite(), "idParticularite"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param actionType
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int actionType) {
        IJEmployeur clone = new IJEmployeur();

        clone.setIdAffilie(getIdAffilie());
        clone.setIdTiers(getIdTiers());
        clone.setIdParticularite(getIdParticularite());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdEmployeur();
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdEmployeur(pk);
    }
}
