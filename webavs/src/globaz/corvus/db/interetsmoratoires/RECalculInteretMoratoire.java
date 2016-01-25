/*
 * Créé le 2 août 07
 */
package globaz.corvus.db.interetsmoratoires;

import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author BSC
 * 
 */
public class RECalculInteretMoratoire extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "ZHDDEB";
    public static final String FIELDNAME_DATE_FIN = "ZHDFIN";
    public static final String FIELDNAME_ID_CALCUL_INTERET_MORATOIRE = "ZHICIM";
    public static final String FIELDNAME_ID_INTERET_MORATOIRE = "ZHIIMO";
    public static final String FIELDNAME_ID_TIERS_BENEFICIAIRE = "ZHITBE";
    public static final String FIELDNAME_MONTANT_DETTE = "ZHMDET";
    public static final String FIELDNAME_MONTANT_RETRO = "ZHMRET";
    public static final String TABLE_NAME_CALCUL_INTERET_MORATOIRE = "RECALIM";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebut = "";
    private String dateFin = "";
    private String idCalculInteretMoratoire = "";
    private String idInteretMoratoire = "";
    private String idTiers = "";
    private String idTiersAdrPmt = "";
    private String montantDette = "";

    private String montantRetro = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCalculInteretMoratoire(_incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        // on supprimer la ref. de la RA sur ce calcul d'interet moratoire
        RERenteAccordeeManager raMgr = new RERenteAccordeeManager();
        raMgr.setSession(getSession());
        raMgr.setForIdCalulInteretMoratoire(getIdCalculInteretMoratoire());
        raMgr.find(transaction);

        if (raMgr.getSize() > 0) {
            RERenteAccordee ra = (RERenteAccordee) raMgr.getFirstEntity();
            ra.setIdCalculInteretMoratoire("");
            ra.update(transaction);
        }

        super._beforeDelete(transaction);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_CALCUL_INTERET_MORATOIRE;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idCalculInteretMoratoire = statement.dbReadNumeric(FIELDNAME_ID_CALCUL_INTERET_MORATOIRE);
        montantDette = statement.dbReadNumeric(FIELDNAME_MONTANT_DETTE);
        montantRetro = statement.dbReadNumeric(FIELDNAME_MONTANT_RETRO);
        idTiers = statement.dbReadNumeric(FIELDNAME_ID_TIERS_BENEFICIAIRE);
        dateDebut = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(FIELDNAME_DATE_DEBUT));
        dateFin = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadNumeric(FIELDNAME_DATE_FIN));
        idInteretMoratoire = statement.dbReadNumeric(FIELDNAME_ID_INTERET_MORATOIRE);

        idTiersAdrPmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
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
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_CALCUL_INTERET_MORATOIRE,
                _dbWriteNumeric(statement.getTransaction(), idCalculInteretMoratoire, "idCalculInteretMoratoire"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_CALCUL_INTERET_MORATOIRE,
                _dbWriteNumeric(statement.getTransaction(), idCalculInteretMoratoire, "idCalculInteretMoratoire"));
        statement.writeField(FIELDNAME_MONTANT_DETTE,
                _dbWriteNumeric(statement.getTransaction(), montantDette, "dette"));
        statement.writeField(FIELDNAME_MONTANT_RETRO,
                _dbWriteNumeric(statement.getTransaction(), montantRetro, "montantRetro"));
        statement.writeField(FIELDNAME_ID_TIERS_BENEFICIAIRE,
                _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(
                FIELDNAME_DATE_DEBUT,
                _dbWriteNumeric(statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebut),
                        "dateDebut"));
        statement.writeField(
                FIELDNAME_DATE_FIN,
                _dbWriteNumeric(statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFin),
                        "dateFin"));
        statement.writeField(FIELDNAME_ID_INTERET_MORATOIRE,
                _dbWriteNumeric(statement.getTransaction(), idInteretMoratoire, "idInteretMoratoire"));
    }

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public String getIdCalculInteretMoratoire() {
        return idCalculInteretMoratoire;
    }

    /**
     * @return
     */
    public String getIdInteretMoratoire() {
        return idInteretMoratoire;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    /**
     * @return
     */
    public String getMontantDette() {
        return montantDette;
    }

    /**
     * @return
     */
    public String getMontantRetro() {
        return montantRetro;
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
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setIdCalculInteretMoratoire(String string) {
        idCalculInteretMoratoire = string;
    }

    /**
     * @param string
     */
    public void setIdInteretMoratoire(String string) {
        idInteretMoratoire = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setMontantDette(String string) {
        montantDette = string;
    }

    /**
     * @param string
     */
    public void setMontantRetro(String string) {
        montantRetro = string;
    }

}
