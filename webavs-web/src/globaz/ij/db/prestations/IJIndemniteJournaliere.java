package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRCalcul;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJIndemniteJournaliere extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_CS_TYPE_INDEMNISATION = "XWTTIN";

    public static final String FIELDNAME_DEDUCTION_RENTE_AI = "XWMDRA";
    public static final String FIELDNAME_FRACTION_REDUCTION_SI_REVENU_AVANT_READAPTATION = "XWMFRR";
    public static final String FIELDNAME_ID_IJ_CALCULEE = "XWIIJC";
    public static final String FIELDNAME_ID_INDEMNITE_JOURNALIERE = "XWIIND";
    public static final String FIELDNAME_INDEMNITE_AVANT_REDUCTION = "XWMIAR";
    public static final String FIELDNAME_MONTANT_COMPLET = "XWMMCO";
    public static final String FIELDNAME_MONTANT_GARANTI_AA_NON_REDUIT = "XWMGNR";
    public static final String FIELDNAME_MONTANT_GARANTI_AA_REDUIT = "XWMGAR";
    public static final String FIELDNAME_MONTANT_JOURNALIER_INDEMNITE = "XWMMJI";
    public static final String FIELDNAME_MONTANT_PLAFONNE = "XWMMPL";
    public static final String FIELDNAME_MONTANT_PLAFONNE_MINIMUM = "XWMMPM";
    public static final String FIELDNAME_MONTANT_REDUCTION_SI_REVENU_AVANT_READAPTATION = "XWMMRR";
    public static final String FIELDNAME_MONTANT_SUPPLEMENTAIRE_READAPTATION = "XWMMSR";
    public static final String TABLE_NAME = "IJINDJRN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csTypeIndemnisation = "";
    private String deductionRenteAI = "";
    private String fractionReductionSiRevenuAvantReadaptation = "";
    private String idIJCalculee = "";
    private String idIndemniteJournaliere = "";
    private String indemniteAvantReduction = "";
    private String montantComplet = "";
    private String montantGarantiAANonReduit = "";
    private String montantGarantiAAReduit = "";
    private String montantJournalierIndemnite = "";
    private String montantPlafonne = "";
    private String montantPlafonneMinimum = "";
    private String montantReductionSiRevenuAvantReadaptation = "";
    private String montantSupplementaireReadaptation = "";

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
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idIndemniteJournaliere = _incCounter(transaction, "0");
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
    protected void _readProperties(BStatement statement) throws Exception {
        idIndemniteJournaliere = statement.dbReadNumeric(FIELDNAME_ID_INDEMNITE_JOURNALIERE);
        idIJCalculee = statement.dbReadNumeric(FIELDNAME_ID_IJ_CALCULEE);
        csTypeIndemnisation = statement.dbReadNumeric(FIELDNAME_CS_TYPE_INDEMNISATION);
        montantSupplementaireReadaptation = statement.dbReadNumeric(FIELDNAME_MONTANT_SUPPLEMENTAIRE_READAPTATION, 2);
        montantGarantiAANonReduit = statement.dbReadNumeric(FIELDNAME_MONTANT_GARANTI_AA_NON_REDUIT, 2);
        indemniteAvantReduction = statement.dbReadNumeric(FIELDNAME_INDEMNITE_AVANT_REDUCTION, 2);
        deductionRenteAI = statement.dbReadNumeric(FIELDNAME_DEDUCTION_RENTE_AI, 2);
        fractionReductionSiRevenuAvantReadaptation = statement.dbReadNumeric(
                FIELDNAME_FRACTION_REDUCTION_SI_REVENU_AVANT_READAPTATION, 2);
        montantReductionSiRevenuAvantReadaptation = statement.dbReadNumeric(
                FIELDNAME_MONTANT_REDUCTION_SI_REVENU_AVANT_READAPTATION, 2);
        montantJournalierIndemnite = statement.dbReadNumeric(FIELDNAME_MONTANT_JOURNALIER_INDEMNITE, 2);
        montantGarantiAAReduit = statement.dbReadNumeric(FIELDNAME_MONTANT_GARANTI_AA_REDUIT, 2);
        montantComplet = statement.dbReadNumeric(FIELDNAME_MONTANT_COMPLET, 2);
        montantPlafonne = statement.dbReadNumeric(FIELDNAME_MONTANT_PLAFONNE, 2);
        montantPlafonneMinimum = statement.dbReadNumeric(FIELDNAME_MONTANT_PLAFONNE_MINIMUM, 2);
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
        statement.writeKey(FIELDNAME_ID_INDEMNITE_JOURNALIERE,
                _dbWriteNumeric(statement.getTransaction(), idIndemniteJournaliere));
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
        statement.writeField(FIELDNAME_ID_INDEMNITE_JOURNALIERE,
                _dbWriteNumeric(statement.getTransaction(), idIndemniteJournaliere, "idIndemniteJournaliere"));
        statement.writeField(FIELDNAME_ID_IJ_CALCULEE,
                _dbWriteNumeric(statement.getTransaction(), idIJCalculee, "idIJCalculee"));
        statement.writeField(FIELDNAME_CS_TYPE_INDEMNISATION,
                _dbWriteNumeric(statement.getTransaction(), csTypeIndemnisation, "csTypeIndemnisation"));
        statement.writeField(
                FIELDNAME_MONTANT_SUPPLEMENTAIRE_READAPTATION,
                _dbWriteNumeric(statement.getTransaction(), montantSupplementaireReadaptation,
                        "montantSupplementaireReadaptation"));
        statement.writeField(FIELDNAME_MONTANT_GARANTI_AA_NON_REDUIT,
                _dbWriteNumeric(statement.getTransaction(), montantGarantiAANonReduit, "montantGarantiAANonReduit"));
        statement.writeField(FIELDNAME_INDEMNITE_AVANT_REDUCTION,
                _dbWriteNumeric(statement.getTransaction(), indemniteAvantReduction, "indemniteAvantReduction"));
        statement.writeField(FIELDNAME_DEDUCTION_RENTE_AI,
                _dbWriteNumeric(statement.getTransaction(), deductionRenteAI, "deductionRenteAI"));
        statement.writeField(
                FIELDNAME_FRACTION_REDUCTION_SI_REVENU_AVANT_READAPTATION,
                _dbWriteNumeric(statement.getTransaction(), fractionReductionSiRevenuAvantReadaptation,
                        "fractionReductionSiRevenuAvantReadaptation"));
        statement.writeField(
                FIELDNAME_MONTANT_REDUCTION_SI_REVENU_AVANT_READAPTATION,
                _dbWriteNumeric(statement.getTransaction(), montantReductionSiRevenuAvantReadaptation,
                        "montantReductionSiRevenuAvantReadaptation"));
        statement.writeField(FIELDNAME_MONTANT_JOURNALIER_INDEMNITE,
                _dbWriteNumeric(statement.getTransaction(), montantJournalierIndemnite, "montantJournalierIndemnite"));
        statement.writeField(FIELDNAME_MONTANT_GARANTI_AA_REDUIT,
                _dbWriteNumeric(statement.getTransaction(), montantGarantiAAReduit, "montantGarantiAAReduit"));
        statement.writeField(FIELDNAME_MONTANT_COMPLET,
                _dbWriteNumeric(statement.getTransaction(), montantComplet, "montantComplet"));
        statement.writeField(FIELDNAME_MONTANT_PLAFONNE,
                _dbWriteNumeric(statement.getTransaction(), montantPlafonne, "montantPlafonne"));
        statement.writeField(FIELDNAME_MONTANT_PLAFONNE_MINIMUM,
                _dbWriteNumeric(statement.getTransaction(), montantPlafonneMinimum, "montantPlafonneMinimum"));
    }

    /**
     * getter pour l'attribut cs type indemnisation
     * 
     * @return la valeur courante de l'attribut cs type indemnisation
     */
    public String getCsTypeIndemnisation() {
        return csTypeIndemnisation;
    }

    /**
     * getter pour l'attribut deduction rente AI
     * 
     * @return la valeur courante de l'attribut deduction rente AI
     */
    public String getDeductionRenteAI() {
        return deductionRenteAI;
    }

    /**
     * getter pour l'attribut fraction reduction si revenu avant readaptation
     * 
     * @return la valeur courante de l'attribut fraction reduction si revenu avant readaptation
     */
    public String getFractionReductionSiRevenuAvantReadaptation() {
        return fractionReductionSiRevenuAvantReadaptation;
    }

    /**
     * getter pour l'attribut id IJIJCalculee
     * 
     * @return la valeur courante de l'attribut id IJIJCalculee
     */
    public String getIdIJCalculee() {
        return idIJCalculee;
    }

    /**
     * getter pour l'attribut id indemnite journaliere
     * 
     * @return la valeur courante de l'attribut id indemnite journaliere
     */
    public String getIdIndemniteJournaliere() {
        return idIndemniteJournaliere;
    }

    /**
     * getter pour l'attribut indemnite avant reduction
     * 
     * @return la valeur courante de l'attribut indemnite avant reduction
     */
    public String getIndemniteAvantReduction() {
        return indemniteAvantReduction;
    }

    /**
     * getter pour l'attribut montant complet
     * 
     * @return la valeur courante de l'attribut montant complet
     */
    public String getMontantComplet() {
        return montantComplet;
    }

    /**
     * getter pour l'attribut montant garanti AANon reduit
     * 
     * @return la valeur courante de l'attribut montant garanti AANon reduit
     */
    public String getMontantGarantiAANonReduit() {
        return montantGarantiAANonReduit;
    }

    /**
     * getter pour l'attribut montant garanti AAReduit
     * 
     * @return la valeur courante de l'attribut montant garanti AAReduit
     */
    public String getMontantGarantiAAReduit() {
        return montantGarantiAAReduit;
    }

    /**
     * getter pour l'attribut montant journalier indemnite
     * 
     * @return la valeur courante de l'attribut montant journalier indemnite
     */
    public String getMontantJournalierIndemnite() {
        return montantJournalierIndemnite;
    }

    /**
     * getter pour l'attribut montant plafonne
     * 
     * @return la valeur courante de l'attribut montant plafonne
     */
    public String getMontantPlafonne() {
        return montantPlafonne;
    }

    /**
     * getter pour l'attribut montant plafonne minimum
     * 
     * @return la valeur courante de l'attribut montant plafonne minimum
     */
    public String getMontantPlafonneMinimum() {
        return montantPlafonneMinimum;
    }

    /**
     * getter pour l'attribut montant reduction si revenu avant readaptation
     * 
     * @return la valeur courante de l'attribut montant reduction si revenu avant readaptation
     */
    public String getMontantReductionSiRevenuAvantReadaptation() {
        return montantReductionSiRevenuAvantReadaptation;
    }

    /**
     * getter pour l'attribut montant supplementaire readaptation
     * 
     * @return la valeur courante de l'attribut montant supplementaire readaptation
     */
    public String getMontantSupplementaireReadaptation() {
        return montantSupplementaireReadaptation;
    }

    /**
     * Pour une base d'indémnisation donnée, détermine si cette ij et l'ij 'ij' donneraient lieu à exactement les mêmes
     * montants de prestations.
     * 
     * @param ij
     *            DOCUMENT ME!
     * 
     * @return vrai si les ij sont égales au niveau du calcul.
     */
    public boolean isEgalPourCalcul(IJIndemniteJournaliere ij) {
        if (PRCalcul.isIEgaux(csTypeIndemnisation, ij.csTypeIndemnisation)) {
            return PRCalcul.isDEgaux(montantComplet, ij.montantComplet)
                    && PRCalcul.isDEgaux(montantGarantiAANonReduit, ij.montantGarantiAANonReduit)
                    && PRCalcul.isDEgaux(montantGarantiAAReduit, ij.montantGarantiAAReduit)
                    && PRCalcul.isDEgaux(montantJournalierIndemnite, ij.montantJournalierIndemnite)
                    && PRCalcul.isDEgaux(montantPlafonne, ij.montantPlafonne)
                    && PRCalcul.isDEgaux(montantPlafonneMinimum, ij.montantPlafonneMinimum)
                    && PRCalcul.isDEgaux(montantReductionSiRevenuAvantReadaptation,
                            ij.montantReductionSiRevenuAvantReadaptation)
                    && PRCalcul.isDEgaux(montantSupplementaireReadaptation, ij.montantSupplementaireReadaptation)
                    && PRCalcul.isDEgaux(indemniteAvantReduction, ij.indemniteAvantReduction)
                    && PRCalcul.isDEgaux(deductionRenteAI, ij.deductionRenteAI)
                    && PRCalcul.isDEgaux(fractionReductionSiRevenuAvantReadaptation,
                            ij.fractionReductionSiRevenuAvantReadaptation);
        }

        return false;
    }

    /**
     * setter pour l'attribut cs type indemnisation
     * 
     * @param csTypeIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIndemnisation(String csTypeIndemnisation) {
        this.csTypeIndemnisation = csTypeIndemnisation;
    }

    /**
     * setter pour l'attribut deduction rente AI
     * 
     * @param deductionRenteAI
     *            une nouvelle valeur pour cet attribut
     */
    public void setDeductionRenteAI(String deductionRenteAI) {
        this.deductionRenteAI = deductionRenteAI;
    }

    /**
     * setter pour l'attribut fraction reduction si revenu avant readaptation
     * 
     * @param fractionReductionSiRevenuAvantReadaptation
     *            une nouvelle valeur pour cet attribut
     */
    public void setFractionReductionSiRevenuAvantReadaptation(String fractionReductionSiRevenuAvantReadaptation) {
        this.fractionReductionSiRevenuAvantReadaptation = fractionReductionSiRevenuAvantReadaptation;
    }

    /**
     * setter pour l'attribut id IJIJCalculee
     * 
     * @param idMesure
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdIJCalculee(String idMesure) {
        idIJCalculee = idMesure;
    }

    /**
     * setter pour l'attribut id indemnite journaliere
     * 
     * @param idIndemniteJournaliere
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdIndemniteJournaliere(String idIndemniteJournaliere) {
        this.idIndemniteJournaliere = idIndemniteJournaliere;
    }

    /**
     * setter pour l'attribut indemnite avant reduction
     * 
     * @param indemniteAvantReduction
     *            une nouvelle valeur pour cet attribut
     */
    public void setIndemniteAvantReduction(String indemniteAvantReduction) {
        this.indemniteAvantReduction = indemniteAvantReduction;
    }

    /**
     * setter pour l'attribut montant complet
     * 
     * @param montantComplet
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantComplet(String montantComplet) {
        this.montantComplet = montantComplet;
    }

    /**
     * setter pour l'attribut montant garanti AANon reduit
     * 
     * @param montantGarantiAANonReduit
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantGarantiAANonReduit(String montantGarantiAANonReduit) {
        this.montantGarantiAANonReduit = montantGarantiAANonReduit;
    }

    /**
     * setter pour l'attribut montant garanti AAReduit
     * 
     * @param montantGarantiAAReduit
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantGarantiAAReduit(String montantGarantiAAReduit) {
        this.montantGarantiAAReduit = montantGarantiAAReduit;
    }

    /**
     * setter pour l'attribut montant journalier indemnite
     * 
     * @param montantJournalierIndemnite
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantJournalierIndemnite(String montantJournalierIndemnite) {
        this.montantJournalierIndemnite = montantJournalierIndemnite;
    }

    /**
     * setter pour l'attribut montant plafonne
     * 
     * @param montantPlafonne
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantPlafonne(String montantPlafonne) {
        this.montantPlafonne = montantPlafonne;
    }

    /**
     * setter pour l'attribut montant plafonne minimum
     * 
     * @param montantPlafonneMinimum
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantPlafonneMinimum(String montantPlafonneMinimum) {
        this.montantPlafonneMinimum = montantPlafonneMinimum;
    }

    /**
     * setter pour l'attribut montant reduction si revenu avant readaptation
     * 
     * @param montantReductionSiRevenuAvantReadaptation
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantReductionSiRevenuAvantReadaptation(String montantReductionSiRevenuAvantReadaptation) {
        this.montantReductionSiRevenuAvantReadaptation = montantReductionSiRevenuAvantReadaptation;
    }

    /**
     * setter pour l'attribut montant supplementaire readaptation
     * 
     * @param montantSupplementaireReadaptation
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantSupplementaireReadaptation(String montantSupplementaireReadaptation) {
        this.montantSupplementaireReadaptation = montantSupplementaireReadaptation;
    }
}
