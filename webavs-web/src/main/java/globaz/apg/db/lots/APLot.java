/*
 * Créé le 6 juin 05
 */
package globaz.apg.db.lots;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;

/**
 * <H1>BEntity représentant un lot</H1>
 * 
 * <p>
 * ATTENTION : le numero du lot = id du lot.
 * </p>
 * 
 * @author dvh
 */
public class APLot extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_DATECOMPTABLE = "VMDCOM";

    /**
     */
    public static final String FIELDNAME_DATECREATION = "VMDCRE";

    /**
     */
    public static final String FIELDNAME_DATEIMPRESSIONCOMM = "VMDIMC";

    /**
     */
    public static final String FIELDNAME_DESCRIPTION = "VMLDES";

    /**
     */
    public static final String FIELDNAME_ETAT = "VMTETA";

    /**
     */
    public static final String FIELDNAME_ID_JOURNAL_CA = "VMIJCA";

    /**
     */
    public static final String FIELDNAME_IDLOT = "VMILOT";

    /**
     */
    public static final String FIELDNAME_NOLOT = "VMNLOT";

    /**
     */
    public static final String FIELDNAME_TYPELOT = "VMTLOT";

    /**
     */
    public static final String TABLE_NAME = "APLOTSP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateComptable = "";
    private String dateCreation = "";
    private String dateImpressionComm = "";
    private String description = "";
    private String etat = "";
    private String idJournalCA = "";
    private String idLot = "";
    private String noLot = "";
    private String typeLot = "";

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
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // mise en etat contrôlé des prestations APG qui étaient dans ce lot et
        // valide des prestations maternités
        APPrestationManager mgr = new APPrestationManager();
        mgr.setSession(getSession());
        mgr.setForIdLot(idLot);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        APPrestation prestation;

        for (int i = 0; i < mgr.size(); i++) {
            prestation = (APPrestation) (mgr.getEntity(i));

            if (prestation.getNoRevision().equals(IAPDroitMaternite.CS_REVISION_MATERNITE_2005)) {
                prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            } else {
                prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);
            }

            prestation.setIdLot("");
            prestation.setDateControle("");
            prestation.update(transaction);
        }

        // suppression des compensations de ce lot
        APCompensationManager compensationManager = new APCompensationManager();
        compensationManager.setSession(getSession());
        compensationManager.setForIdLot(idLot);
        compensationManager.find(transaction, BManager.SIZE_NOLIMIT);

        APCompensation compensation = null;

        for (int i = 0; i < compensationManager.size(); i++) {
            compensation = (APCompensation) compensationManager.getEntity(i);
            compensation.delete(transaction);
        }

        compensation = null;
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
        setNoLot(idLot);
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
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric(FIELDNAME_IDLOT);
        noLot = statement.dbReadNumeric(FIELDNAME_NOLOT);
        dateCreation = statement.dbReadDateAMJ(FIELDNAME_DATECREATION);
        dateImpressionComm = statement.dbReadDateAMJ(FIELDNAME_DATEIMPRESSIONCOMM);
        etat = statement.dbReadNumeric(FIELDNAME_ETAT);
        description = statement.dbReadString(FIELDNAME_DESCRIPTION);
        dateComptable = statement.dbReadDateAMJ(FIELDNAME_DATECOMPTABLE);
        typeLot = statement.dbReadNumeric(FIELDNAME_TYPELOT);
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
        statement.writeField(FIELDNAME_NOLOT, _dbWriteNumeric(statement.getTransaction(), noLot, "noLot"));
        statement.writeField(FIELDNAME_DATECREATION,
                _dbWriteDateAMJ(statement.getTransaction(), dateCreation, "dateCreation"));
        statement.writeField(FIELDNAME_DATEIMPRESSIONCOMM,
                _dbWriteDateAMJ(statement.getTransaction(), dateImpressionComm, "dateImpressionComm"));
        statement.writeField(FIELDNAME_ETAT, _dbWriteNumeric(statement.getTransaction(), etat, "etat"));
        statement.writeField(FIELDNAME_DESCRIPTION,
                _dbWriteString(statement.getTransaction(), description, "description"));
        statement.writeField(FIELDNAME_DATECOMPTABLE,
                _dbWriteDateAMJ(statement.getTransaction(), dateComptable, "dateComptable"));
        statement.writeField(FIELDNAME_TYPELOT, _dbWriteNumeric(statement.getTransaction(), typeLot, "typeLot"));
        statement.writeField(FIELDNAME_ID_JOURNAL_CA,
                _dbWriteNumeric(statement.getTransaction(), idJournalCA, "idJournalCA"));
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
     * getter pour l'attribut date impression comm
     * 
     * @return la valeur courante de l'attribut date impression comm
     */
    public String getDateImpressionComm() {
        return dateImpressionComm;
    }

    /**
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter pour l'attribut etat
     * 
     * @return la valeur courante de l'attribut etat
     */
    public String getEtat() {
        return etat;
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
     * getter pour l'attribut type lot
     * 
     * @return la valeur courante de l'attribut type lot
     */
    public String getTypeLot() {
        return typeLot;
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
     * setter pour l'attribut date impression comm
     * 
     * @param dateImpressionComm
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateImpressionComm(String dateImpressionComm) {
        this.dateImpressionComm = dateImpressionComm;
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

    /**
     * setter pour l'attribut etat
     * 
     * @param etat
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtat(String etat) {
        this.etat = etat;
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

    /**
     * setter pour l'attribut type lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeLot(String string) {
        typeLot = string;
    }
}
