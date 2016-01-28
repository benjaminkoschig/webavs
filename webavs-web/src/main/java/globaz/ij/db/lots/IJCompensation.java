/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.lots;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJCompensation extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_DETTES = "XPMDET";

    /**
     */
    public static final String FIELDNAME_IDAFFILIE = "XPIAFF";

    /**
     */
    public static final String FIELDNAME_IDCOMPENSATION = "XPICOM";

    /**
     */
    public static final String FIELDNAME_IDLOT = "XPILOT";

    /**
     */
    public static final String FIELDNAME_IDTIERS = "XPITIE";

    /**
     */
    public static final String FIELDNAME_MONTANTTOTAL = "XPMMTO";

    /**
     */
    public static final String TABLE_NAME = "IJCOMPEN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dettes = "";
    private String idAffilie = "";
    private String idCompensation = "";
    private String idLot = "";
    private String idTiers = "";
    private String montantTotal = "";

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

        // effacement de l'idCompensation des répartitions de paiement
        IJRepartitionPaiementsManager repartitionPaiementsManager = new IJRepartitionPaiementsManager();
        repartitionPaiementsManager.setSession(getSession());
        repartitionPaiementsManager.setForIdCompensation(idCompensation);
        repartitionPaiementsManager.find(transaction, BManager.SIZE_NOLIMIT);

        IJRepartitionPaiements repartitionPaiements = null;

        for (int i = 0; i < repartitionPaiementsManager.size(); i++) {
            repartitionPaiements = (IJRepartitionPaiements) repartitionPaiementsManager.getEntity(i);
            repartitionPaiements.setIdCompensation("");
            repartitionPaiements.update(transaction);
        }

        // effacement des factures à compenser
        IJFactureACompenserManager factureACompenserManager = new IJFactureACompenserManager();
        factureACompenserManager.setSession(getSession());
        factureACompenserManager.setForIdCompensation(idCompensation);
        factureACompenserManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < factureACompenserManager.size(); i++) {
            IJFactureACompenser factureACompenser = (IJFactureACompenser) factureACompenserManager.getEntity(i);
            factureACompenser.delete(transaction);
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
        setIdCompensation(_incCounter(transaction, "0"));
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
        idCompensation = statement.dbReadNumeric(FIELDNAME_IDCOMPENSATION);
        idLot = statement.dbReadNumeric(FIELDNAME_IDLOT);
        idTiers = statement.dbReadNumeric(FIELDNAME_IDTIERS);
        idAffilie = statement.dbReadNumeric(FIELDNAME_IDAFFILIE);
        montantTotal = statement.dbReadNumeric(FIELDNAME_MONTANTTOTAL, 2);
        dettes = statement.dbReadNumeric(FIELDNAME_DETTES, 2);
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
        statement.writeKey(FIELDNAME_IDCOMPENSATION,
                _dbWriteNumeric(statement.getTransaction(), idCompensation, "idCompensation"));
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
        statement.writeField(FIELDNAME_IDCOMPENSATION,
                _dbWriteNumeric(statement.getTransaction(), idCompensation, "idCompensation"));
        statement.writeField(FIELDNAME_IDLOT, _dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(FIELDNAME_IDTIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(FIELDNAME_IDAFFILIE, _dbWriteNumeric(statement.getTransaction(), idAffilie, "idAffilie"));
        statement.writeField(FIELDNAME_MONTANTTOTAL,
                _dbWriteNumeric(statement.getTransaction(), montantTotal, "montantTotal"));
        statement.writeField(FIELDNAME_DETTES, _dbWriteNumeric(statement.getTransaction(), dettes, "dettes"));
    }

    /**
     * getter pour l'attribut dettes
     * 
     * @return la valeur courante de l'attribut dettes
     */
    public String getDettes() {
        return dettes;
    }

    /**
     * getter pour l'attribut id affilie
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id compensation
     * 
     * @return la valeur courante de l'attribut id compensation
     */
    public String getIdCompensation() {
        return idCompensation;
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
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut montant total
     * 
     * @return la valeur courante de l'attribut montant total
     */
    public String getMontantTotal() {
        return montantTotal;
    }

    /**
     * setter pour l'attribut dettes
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDettes(String string) {
        dettes = string;
    }

    /**
     * setter pour l'attribut id affilie
     * 
     * @param idAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    /**
     * setter pour l'attribut id compensation
     * 
     * @param idCompensation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompensation(String idCompensation) {
        this.idCompensation = idCompensation;
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
     * setter pour l'attribut id tiers
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut montant total
     * 
     * @param montantTotal
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }
}
