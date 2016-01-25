package globaz.apg.db.droits;

import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.db.employeurs.PRAbstractEmployeur;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Note: il y a une relation 1-1 entre la table des employeurs et celle des situations pro.
 * </p>
 * 
 * @author vre
 * @see globaz.apg.db.droits.APSituationProfessionnelle
 */
public class APEmployeur extends PRAbstractEmployeur implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_AFFILIE = "VGIAFF";
    public static final String FIELDNAME_ID_EMPLOYEUR = "VGIEMP";
    public static final String FIELDNAME_ID_PARTICULARITE = "VGIPAR";
    public static final String FIELDNAME_ID_TIERS = "VGITIE";
    public static final String TABLE_NAME = "APEMPLP";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
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
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        setIdTiers(statement.dbReadNumeric(FIELDNAME_ID_TIERS));
        setIdAffilie(statement.dbReadNumeric(FIELDNAME_ID_AFFILIE));
        setIdParticularite(statement.dbReadNumeric(FIELDNAME_ID_PARTICULARITE));
        setIdEmployeur(statement.dbReadNumeric(FIELDNAME_ID_EMPLOYEUR));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getIdTiers(), getSession().getLabel("TIERS_INTROUVABLE"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_EMPLOYEUR,
                _dbWriteNumeric(statement.getTransaction(), getIdEmployeur(), "idEmployeur"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_TIERS, _dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField(FIELDNAME_ID_AFFILIE,
                _dbWriteNumeric(statement.getTransaction(), getIdAffilie(), "idAffilie"));
        statement.writeField(FIELDNAME_ID_PARTICULARITE,
                _dbWriteNumeric(statement.getTransaction(), getIdParticularite(), "idParticularite"));
        statement.writeField(FIELDNAME_ID_EMPLOYEUR,
                _dbWriteNumeric(statement.getTransaction(), getIdEmployeur(), "idEmployeur"));
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
        APEmployeur clone = new APEmployeur();

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
