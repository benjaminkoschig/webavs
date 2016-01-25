/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.lots;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJLot extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_CS_ETAT = "XOTETA";

    /**
     */
    public static final String FIELDNAME_DATECOMPTABLE = "XODCOM";

    /**
     */
    public static final String FIELDNAME_DATECREATION = "XODCRE";

    /**
     */
    public static final String FIELDNAME_DATEIMPRESSIONCOMMUNICATION = "XODIMC";

    /**
     */
    public static final String FIELDNAME_DESCRIPTION = "XOLDES";

    /**
     */
    public static final String FIELDNAME_ID_JOURNAL_CA = "XOIJCA";

    /**
     */
    public static final String FIELDNAME_IDLOT = "XOILOT";

    /**
     */
    public static final String FIELDNAME_NOLOT = "XONNLO";

    /**
     */
    public static final String TABLE_NAME = "IJLOTS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtat = "";
    private String dateComptable = "";
    private String dateCreation = "";
    private String dateImpressionCommunication = "";
    private String description = "";
    private String idJournalCA = "";
    private String idLot = "";
    private String noLot = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        // effacement des compensations
        IJCompensationManager compensationManager = new IJCompensationManager();
        compensationManager.setSession(getSession());
        compensationManager.setForIdLot(idLot);
        compensationManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < compensationManager.size(); i++) {
            IJCompensation compensation = (IJCompensation) compensationManager.getEntity(i);
            compensation.delete(transaction);
        }

        // remise des prestations en etat valide
        IJPrestationManager prestationManager = new IJPrestationManager();
        prestationManager.setSession(getSession());
        prestationManager.setForIdLot(idLot);
        prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

        IJPrestation prestation = null;

        for (int i = 0; i < prestationManager.size(); i++) {
            prestation = (IJPrestation) prestationManager.getEntity(i);
            prestation.setCsEtat(IIJPrestation.CS_VALIDE);
            prestation.setIdLot("");
            prestation.update(transaction);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdLot(_incCounter(transaction, "0"));

        // TODO mettre un numero de lot correct
        noLot = idLot;
    }

    /**
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
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric(FIELDNAME_IDLOT);
        dateCreation = statement.dbReadDateAMJ(FIELDNAME_DATECREATION);
        noLot = statement.dbReadNumeric(FIELDNAME_NOLOT);
        dateImpressionCommunication = statement.dbReadDateAMJ(FIELDNAME_DATEIMPRESSIONCOMMUNICATION);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        description = statement.dbReadString(FIELDNAME_DESCRIPTION);
        dateComptable = statement.dbReadDateAMJ(FIELDNAME_DATECOMPTABLE);
        idJournalCA = statement.dbReadNumeric(FIELDNAME_ID_JOURNAL_CA);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDLOT, _dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_IDLOT, _dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(FIELDNAME_DATECREATION,
                _dbWriteDateAMJ(statement.getTransaction(), dateCreation, "dateCreation"));
        statement.writeField(FIELDNAME_NOLOT, _dbWriteNumeric(statement.getTransaction(), noLot, "noLot"));
        statement
                .writeField(
                        FIELDNAME_DATEIMPRESSIONCOMMUNICATION,
                        _dbWriteDateAMJ(statement.getTransaction(), dateImpressionCommunication,
                                "dateImpressionCommunication"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_DESCRIPTION,
                _dbWriteString(statement.getTransaction(), description, "description"));
        statement.writeField(FIELDNAME_DATECOMPTABLE,
                _dbWriteDateAMJ(statement.getTransaction(), dateComptable, "dateComptable"));
        statement.writeField(FIELDNAME_ID_JOURNAL_CA,
                _dbWriteNumeric(statement.getTransaction(), idJournalCA, "idJournalCA"));
    }

    /**
     * getter pour l'attribut csEtat
     * 
     * @return la valeur courante de l'attribut csEtat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * getter pour l'attribut date comptable
     * 
     * @return la valeur courante de l'attribut date comptable
     */
    public String getDateComptable() {
        return dateComptable;
    }

    /**
     * getter pour l'attribut date creation
     * 
     * @return la valeur courante de l'attribut date creation
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * getter pour l'attribut date impression communication
     * 
     * @return la valeur courante de l'attribut date impression communication
     */
    public String getDateImpressionCommunication() {
        return dateImpressionCommunication;
    }

    /**
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescription() {
        return description;
    }

    public String getIdJournalCA() {
        return idJournalCA;
    }

    /**
     * getter pour l'attribut id lot
     * 
     * @return la valeur courante de l'attribut id lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * getter pour l'attribut no lot
     * 
     * @return la valeur courante de l'attribut no lot
     */
    public String getNoLot() {
        return noLot;
    }

    /**
     * setter pour l'attribut csEtat
     * 
     * @param etat
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtat(String etat) {
        csEtat = etat;
    }

    /**
     * setter pour l'attribut date comptable
     * 
     * @param dateComptable
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateComptable(String dateComptable) {
        this.dateComptable = dateComptable;
    }

    /**
     * setter pour l'attribut date creation
     * 
     * @param dateCreation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * setter pour l'attribut date impression communication
     * 
     * @param dateImpressionCommunication
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateImpressionCommunication(String dateImpressionCommunication) {
        this.dateImpressionCommunication = dateImpressionCommunication;
    }

    /**
     * setter pour l'attribut description
     * 
     * @param description
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdJournalCA(String idJournalCA) {
        this.idJournalCA = idJournalCA;
    }

    /**
     * setter pour l'attribut id lot
     * 
     * @param idLot
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    /**
     * setter pour l'attribut no lot
     * 
     * @param noLot
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoLot(String noLot) {
        this.noLot = noLot;
    }
}
