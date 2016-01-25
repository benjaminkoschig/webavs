/*
 * Créé le 3 jan. 07
 */
package globaz.corvus.db.demandes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REDemandeRenteSurvivant extends REDemandeRente implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Nom des champs de la table
    public static final String FIELDNAME_ID_DEMANDE_RENTE_SURVIVANT = "YBIDSU";

    public static final String FIELDNAME_POURCENTAGE_REDUCTION = "YBNPRE";
    // Nom de la table
    public static final String TABLE_NAME_DEMANDE_RENTE_SURVIVANT = "REDESUR";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_DEMANDE_RENTE_SURVIVANT);

        // jointure avec la table des demandes de rentes
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_DEMANDE_RENTE);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_DEMANDE_RENTE_SURVIVANT);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_DEMANDE_RENTE);

        return fromClause.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String pourcentageReduction = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

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
        return createFromClause(_getCollection());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_DEMANDE_RENTE_SURVIVANT;
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
        super._readProperties(statement);
        idDemandeRente = statement.dbReadNumeric(FIELDNAME_ID_DEMANDE_RENTE_SURVIVANT);
        pourcentageReduction = statement.dbReadNumeric(FIELDNAME_POURCENTAGE_REDUCTION);
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
        statement.writeKey(FIELDNAME_ID_DEMANDE_RENTE_SURVIVANT,
                _dbWriteNumeric(statement.getTransaction(), getIdDemandeRente()));
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
        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_DEMANDE_RENTE_SURVIVANT,
                    _dbWriteNumeric(statement.getTransaction(), getIdDemandeRente(), "idDemandeRente"));
        }

        statement.writeField(FIELDNAME_POURCENTAGE_REDUCTION,
                _dbWriteNumeric(statement.getTransaction(), getPourcentageReduction(), "pourcentageReduction"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        REDemandeRenteSurvivant clone = new REDemandeRenteSurvivant();
        duplicateDemandeRente(clone, action);

        clone.setPourcentageReduction(getPourcentageReduction());

        return clone;
    }

    /**
     * getter pour l'attribut pourcentageReduction
     * 
     * @return la valeur courante de l'attribut pourcentageReduction
     */
    public String getPourcentageReduction() {
        return pourcentageReduction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * setter pour l'attribut pourcentageReduction.
     * 
     * @param pourcentageReduction
     *            une nouvelle valeur pour cet attribut
     */
    public void setPourcentageReduction(String string) {
        pourcentageReduction = string;
    }

}
