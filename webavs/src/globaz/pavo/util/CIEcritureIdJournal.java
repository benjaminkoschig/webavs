package globaz.pavo.util;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIEcritureIdJournal extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String ecritureId = new String();
    private String idJournal = new String();

    /**
     * Constructor for CIEcritureIdJournal.
     */
    public CIEcritureIdJournal() {
        super();
    }

    /**
     * Method _getTableName.
     * 
     * @return String
     */
    @Override
    protected String _getTableName() {
        return "CIECRIP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idJournal = statement.dbReadNumeric("KCID");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".KBIECR",
                _dbWriteNumeric(statement.getTransaction(), getEcritureId(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    /**
     * Returns the ecritueId.
     * 
     * @return String
     */
    public String getEcritureId() {
        return ecritureId;
    }

    /**
     * Returns the idJournal.
     * 
     * @return String
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Sets the ecritueId.
     * 
     * @param ecritueId
     *            The ecritueId to set
     */
    public void setEcritureId(String ecritueId) {
        ecritureId = ecritueId;
    }

    /**
     * Sets the idJournal.
     * 
     * @param idJournal
     *            The idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

}
