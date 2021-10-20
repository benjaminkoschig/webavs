package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPetiteIJCalculee extends IJIJCalculee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_CS_MODE_CALCUL = "XTTMCA";

    public static final String FIELDNAME_ID_IJ_CALCULEE_PETITE = "XTIIJC";
    public static final String TABLE_NAME_PETITE_IJ_CALCULEE = "IJPIJCAL";

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
        fromClause.append(TABLE_NAME_PETITE_IJ_CALCULEE);

        // jointure avec la table des ij calculees
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_IJ_CALCULEE);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_IJ_CALCULEE_PETITE);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_IJ_CALCULEE);

        return fromClause.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String csModeCalcul = "";

    /**
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
        return TABLE_NAME_PETITE_IJ_CALCULEE;
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
        csModeCalcul = statement.dbReadNumeric(FIELDNAME_CS_MODE_CALCUL);
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
        // TODO Auto-generated method stub
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
        statement.writeKey(FIELDNAME_ID_IJ_CALCULEE_PETITE,
                _dbWriteNumeric(statement.getTransaction(), getIdIJCalculee()));
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
            statement.writeField(FIELDNAME_ID_IJ_CALCULEE_PETITE,
                    _dbWriteNumeric(statement.getTransaction(), getIdIJCalculee(), "idIJCalculee"));
        }

        statement.writeField(FIELDNAME_CS_MODE_CALCUL,
                _dbWriteNumeric(statement.getTransaction(), csModeCalcul, "csModeCalcul"));
    }

    /**
     * getter pour l'attribut cs mode calcul
     * 
     * @return la valeur courante de l'attribut cs mode calcul
     */
    public String getCsModeCalcul() {
        return csModeCalcul;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
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
     * getter pour l'attribut egal pour calcul
     * 
     * @param ij
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut egal pour calcul
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public boolean isEgalPourCalcul(IJIJCalculee ij) throws Exception {
        if(ij instanceof IJPetiteIJCalculee) {
            return csModeCalcul.equals(((IJPetiteIJCalculee) ij).csModeCalcul) && super.isEgalPourCalcul(ij);
        }
        return false;
    }

    /**
     * setter pour l'attribut cs mode calcul
     * 
     * @param csModeCalcul
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsModeCalcul(String csModeCalcul) {
        this.csModeCalcul = csModeCalcul;
    }
}
