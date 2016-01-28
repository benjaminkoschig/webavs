package globaz.osiris.db.utils;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author sel Créé le : 22 nov. 06
 */
public class CACumulOperationCotisation extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CUMUL_OPERATION = "CUMULOPERATION";
    public static final String DATEREFERENCE = "DATEREFERENCE";
    public static final String IDRUBRIQUE = "IDRUBRIQUE";

    private String cumulOperation = "0";
    private String dateDebut = new String();
    private String dateFin = new String();
    private String dateReference = new String();
    private String idCompteAnnexe = new String();
    private String idJournal = new String();

    private String idRubrique = new String();
    private String idTypeOperation = new String();

    /**
     * Surcharge :
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * @return null
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * Surcharge :
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCompteAnnexe(statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTEANNEXE));
        setIdRubrique(statement.dbReadNumeric(CACumulOperationCotisation.IDRUBRIQUE));
        setIdTypeOperation(statement.dbReadString(CAOperation.FIELD_IDTYPEOPERATION));
        setIdJournal(statement.dbReadNumeric(CAOperation.FIELD_IDJOURNAL));
        setCumulOperation(statement.dbReadDateAMJ(CACumulOperationCotisation.DATEREFERENCE));
        setCumulOperation(statement.dbReadNumeric(CACumulOperationCotisation.CUMUL_OPERATION));
    }

    /**
     * Surcharge :
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Do nothing
    }

    /**
     * Surcharge :
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Do nothing
    }

    /**
     * Surcharge :
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Do nothing
    }

    /**
     * @return the cumulOperation
     */
    public String getCumulOperation() {
        return cumulOperation;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the dateReference
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * @return the idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return the idJournal
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * @return the idRubrique
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return the idTypeOperation
     */
    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    /**
     * @param cumulOperation
     *            the cumulOperation to set
     */
    public void setCumulOperation(String cumulOperation) {
        this.cumulOperation = cumulOperation;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param dateReference
     *            the dateReference to set
     */
    public void setDateReference(String dateRefernece) {
        dateReference = dateRefernece;
    }

    /**
     * @param idCompteAnnexe
     *            the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idJournal
     *            the idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * @param idRubrique
     *            the idRubrique to set
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /**
     * @param idTypeOperation
     *            the idTypeOperation to set
     */
    public void setIdTypeOperation(String idTypeOperation) {
        this.idTypeOperation = idTypeOperation;
    }

}
