package globaz.aquila.db.access.batch;

import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Représente un container de type Sequence.
 * 
 * @author Arnaud Dostes, 11-oct-2004
 */
public class COSequenceManager extends COBManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 7422350166897286211L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forIdSequence = "";
    private String forLibSequence = "";
    private String fromIdSequence = "";
    private String fromLibSequence = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + ICOSequenceConstante.TABLE_NAME;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return ICOSequenceConstante.FNAME_ID_SEQUENCE;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdSequence().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += ICOSequenceConstante.FNAME_ID_SEQUENCE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSequence());
        }

        // traitement du positionnement
        if (getForLibSequence().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += ICOSequenceConstante.FNAME_LIB_SEQUENCE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForLibSequence());
        }

        // traitement du positionnement
        if (getFromIdSequence().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += ICOSequenceConstante.FNAME_ID_SEQUENCE + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdSequence());
        }

        // traitement du positionnement
        if (getFromLibSequence().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += ICOSequenceConstante.FNAME_LIB_SEQUENCE + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromLibSequence());
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COSequence();
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForIdSequence() {
        return forIdSequence;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getForLibSequence() {
        return forLibSequence;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromIdSequence() {
        return fromIdSequence;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFromLibSequence() {
        return fromLibSequence;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForIdSequence(String string) {
        forIdSequence = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setForLibSequence(String string) {
        forLibSequence = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromIdSequence(String string) {
        fromIdSequence = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFromLibSequence(String string) {
        fromLibSequence = string;
    }

}
