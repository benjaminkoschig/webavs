/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.annonces;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPeriodeAnnonce extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_CODEVALEURTOTALIJ = "XYACVA";

    /**
     */
    public static final String FIELDNAME_DEDUCTIONNOURRITURELOGEMENT = "XYBDNL";

    /**
     */
    public static final String FIELDNAME_DROIT_PRESTATION_POUR_ENFANT = "XYADPE";

    /**
     */
    public static final String FIELDNAME_GARANTIE_DROIT_ACQUIS_5_EME_REVISION = "XYAGDA";

    /**
     */
    public static final String FIELDNAME_IDANNONCE = "XYIANN";

    /**
     */
    public static final String FIELDNAME_IDPERIODEANNONCE = "XYIPEA";

    /**
     */
    public static final String FIELDNAME_MONTANT_AIT = "XYMAIT";

    /**
     */
    public static final String FIELDNAME_MONTANT_ALLOC_ASSISTANCE = "XYMALA";

    /**
     */
    public static final String FIELDNAME_MOTIFINTERRUPTION = "XYAMOI";

    /**
     */
    public static final String FIELDNAME_NOMBREJOURS = "XYNNJR";

    /**
     */
    public static final String FIELDNAME_NOMBREJOURSINTERRUPTION = "XYNNJI";

    /**
     */
    public static final String FIELDNAME_PERIODEA = "XYDPEA";

    /**
     */
    public static final String FIELDNAME_PERIODEDE = "XYDPED";

    /**
     */
    public static final String FIELDNAME_TAUXJOURNALIER = "XYMTJR";

    /**
     */
    public static final String FIELDNAME_TOTALIJ = "XYMTIJ";

    /**
     */
    public static final String FIELDNAME_VERSEMENTIJ = "XYAVIJ";

    /**
     */
    public static final String TABLE_NAME = "IJPERANN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeMotifInterruption = "";
    private String codeValeurTotalIJ = "";
    private String deductionNourritureLogement = "";
    private String droitPrestationPourEnfant = "";
    private String garantieDroitAcquis5emeRevision = "";
    private String idAnnonce = "";
    private String idPeriodeAnnonce = "";
    private String montantAit = "";
    private String montantAllocAssistance = "";
    private String nombreJours = "";
    private String nombreJoursInterruption = "";
    private String periodeA = "";

    private String periodeDe = "";
    private String tauxJournalier = "";
    private String totalIJ = "";
    private String versementIJ = "";

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
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdPeriodeAnnonce(_incCounter(transaction, "0"));
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
        idPeriodeAnnonce = statement.dbReadNumeric(FIELDNAME_IDPERIODEANNONCE);
        idAnnonce = statement.dbReadNumeric(FIELDNAME_IDANNONCE);
        nombreJours = statement.dbReadNumeric(FIELDNAME_NOMBREJOURS);
        tauxJournalier = statement.dbReadNumeric(FIELDNAME_TAUXJOURNALIER);
        deductionNourritureLogement = statement.dbReadString(FIELDNAME_DEDUCTIONNOURRITURELOGEMENT);
        nombreJoursInterruption = statement.dbReadNumeric(FIELDNAME_NOMBREJOURSINTERRUPTION);
        codeMotifInterruption = statement.dbReadString(FIELDNAME_MOTIFINTERRUPTION);
        versementIJ = statement.dbReadString(FIELDNAME_VERSEMENTIJ);
        totalIJ = statement.dbReadNumeric(FIELDNAME_TOTALIJ);
        codeValeurTotalIJ = statement.dbReadString(FIELDNAME_CODEVALEURTOTALIJ);
        periodeDe = statement.dbReadDateAMJ(FIELDNAME_PERIODEDE);
        periodeA = statement.dbReadDateAMJ(FIELDNAME_PERIODEA);
        droitPrestationPourEnfant = statement.dbReadString(FIELDNAME_DROIT_PRESTATION_POUR_ENFANT);
        garantieDroitAcquis5emeRevision = statement.dbReadString(FIELDNAME_GARANTIE_DROIT_ACQUIS_5_EME_REVISION);
        montantAllocAssistance = statement.dbReadNumeric(FIELDNAME_MONTANT_ALLOC_ASSISTANCE);
        montantAit = statement.dbReadNumeric(FIELDNAME_MONTANT_AIT);
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

        // Lors de la saisie manuelle d'une annonce, ce champ n'est pas saisi.
        if (JadeStringUtil.isBlank(getCodeValeurTotalIJ())) {
            FWCurrency txJrn = null;
            try {
                txJrn = new FWCurrency(getTauxJournalier());
            } catch (Exception e) {
                txJrn = new FWCurrency(0);
            }

            if (txJrn.isNegative()) {
                setCodeValeurTotalIJ("1");
            } else {
                setCodeValeurTotalIJ("0");
            }

            // MAJ du montant total IJ, qui ne peut pas être négatif.
            // C'est le codeValeur qui identifie le signe...
            FWCurrency mntTotalIJ = new FWCurrency(0);
            try {
                mntTotalIJ = new FWCurrency(totalIJ);
            } catch (Exception e) {
                ;
            }
            mntTotalIJ.abs();
            totalIJ = mntTotalIJ.toString();

        }
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
        statement.writeKey(FIELDNAME_IDPERIODEANNONCE,
                _dbWriteNumeric(statement.getTransaction(), idPeriodeAnnonce, "idPeriodeAnnonce"));
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
        statement.writeField(FIELDNAME_IDPERIODEANNONCE,
                _dbWriteNumeric(statement.getTransaction(), idPeriodeAnnonce, "idPeriodeAnnonce"));
        statement.writeField(FIELDNAME_IDANNONCE, _dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));

        if (nombreJours.equals("")) {
            statement.writeField(FIELDNAME_NOMBREJOURS, null);
        } else {
            statement.writeField(FIELDNAME_NOMBREJOURS,
                    _dbWriteNumeric(statement.getTransaction(), nombreJours, "nombreJours"));
        }

        if (tauxJournalier.equals("")) {
            statement.writeField(FIELDNAME_TAUXJOURNALIER, null);
        } else {
            statement.writeField(FIELDNAME_TAUXJOURNALIER,
                    _dbWriteNumeric(statement.getTransaction(), tauxJournalier, "tauxJournalier"));
        }

        statement.writeField(FIELDNAME_DEDUCTIONNOURRITURELOGEMENT,
                _dbWriteString(statement.getTransaction(), deductionNourritureLogement, "deductionNourritureLogement"));

        if (nombreJoursInterruption.equals("")) {
            statement.writeField(FIELDNAME_NOMBREJOURSINTERRUPTION, null);
        } else {
            statement.writeField(FIELDNAME_NOMBREJOURSINTERRUPTION,
                    _dbWriteNumeric(statement.getTransaction(), nombreJoursInterruption, "nombreJoursInterruption"));
        }

        statement.writeField(FIELDNAME_MOTIFINTERRUPTION,
                _dbWriteString(statement.getTransaction(), codeMotifInterruption, "codeMotifInterruption"));
        statement.writeField(FIELDNAME_VERSEMENTIJ,
                _dbWriteString(statement.getTransaction(), versementIJ, "versementIJ"));

        if (totalIJ.equals("")) {
            statement.writeField(FIELDNAME_TOTALIJ, null);
        } else {
            statement.writeField(FIELDNAME_TOTALIJ, _dbWriteNumeric(statement.getTransaction(), totalIJ, "totalIJ"));
        }

        statement.writeField(FIELDNAME_CODEVALEURTOTALIJ,
                _dbWriteString(statement.getTransaction(), codeValeurTotalIJ, "codeValeurTotalIJ"));
        statement.writeField(FIELDNAME_PERIODEDE, _dbWriteDateAMJ(statement.getTransaction(), periodeDe, "periodeDe"));
        statement.writeField(FIELDNAME_PERIODEA, _dbWriteDateAMJ(statement.getTransaction(), periodeA, "periodeA"));

        statement.writeField(FIELDNAME_DROIT_PRESTATION_POUR_ENFANT,
                _dbWriteString(statement.getTransaction(), droitPrestationPourEnfant, "droitPrestationPourEnfant"));

        statement.writeField(
                FIELDNAME_GARANTIE_DROIT_ACQUIS_5_EME_REVISION,
                _dbWriteString(statement.getTransaction(), garantieDroitAcquis5emeRevision,
                        "garantieDroitAcquis5emeRevision"));

        if (montantAllocAssistance.equals("")) {
            statement.writeField(FIELDNAME_MONTANT_ALLOC_ASSISTANCE, null);
        } else {
            statement.writeField(FIELDNAME_MONTANT_ALLOC_ASSISTANCE,
                    _dbWriteNumeric(statement.getTransaction(), montantAllocAssistance, "montantAllocAssistance"));
        }

        if (montantAit.equals("")) {
            statement.writeField(FIELDNAME_MONTANT_AIT, null);
        } else {
            statement.writeField(FIELDNAME_MONTANT_AIT,
                    _dbWriteNumeric(statement.getTransaction(), montantAit, "montantAit"));
        }

    }

    /**
     * copie tous les champs excepté la clef primaire dans un nouvel entity, et donne la session du parent
     * 
     * @return DOCUMENT ME!
     */
    public IJPeriodeAnnonce createClone() {
        IJPeriodeAnnonce periodeAnnonce = new IJPeriodeAnnonce();
        periodeAnnonce.setSession(getSession());
        periodeAnnonce.setIdAnnonce(idAnnonce);
        periodeAnnonce.setNombreJours(nombreJours);
        periodeAnnonce.setTauxJournalier(tauxJournalier);
        periodeAnnonce.setDeductionNourritureLogement(deductionNourritureLogement);
        periodeAnnonce.setNombreJoursInterruption(nombreJoursInterruption);
        periodeAnnonce.setCodeMotifInterruption(codeMotifInterruption);
        periodeAnnonce.setVersementIJ(versementIJ);
        periodeAnnonce.setTotalIJ(totalIJ);
        periodeAnnonce.setCodeValeurTotalIJ(codeValeurTotalIJ);
        periodeAnnonce.setPeriodeDe(periodeDe);
        periodeAnnonce.setPeriodeA(periodeA);

        periodeAnnonce.setDroitPrestationPourEnfant(droitPrestationPourEnfant);
        periodeAnnonce.setGarantieDroitAcquis5emeRevision(garantieDroitAcquis5emeRevision);
        periodeAnnonce.setMontantAllocAssistance(montantAllocAssistance);
        periodeAnnonce.setMontantAit(montantAit);

        return periodeAnnonce;
    }

    /**
     * getter pour l'attribut motif interruption
     * 
     * @return la valeur courante de l'attribut motif interruption
     */
    public String getCodeMotifInterruption() {
        return codeMotifInterruption;
    }

    /**
     * getter pour l'attribut code valeur total IJ
     * 
     * @return la valeur courante de l'attribut code valeur total IJ
     */
    public String getCodeValeurTotalIJ() {
        return codeValeurTotalIJ;
    }

    /**
     * getter pour l'attribut deduction nourriture logement
     * 
     * @return la valeur courante de l'attribut deduction nourriture logement
     */
    public String getDeductionNourritureLogement() {
        return deductionNourritureLogement;
    }

    public String getDroitPrestationPourEnfant() {
        return droitPrestationPourEnfant;
    }

    public String getGarantieDroitAcquis5emeRevision() {
        return garantieDroitAcquis5emeRevision;
    }

    /**
     * getter pour l'attribut id annonce
     * 
     * @return la valeur courante de l'attribut id annonce
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * getter pour l'attribut id periode annonce
     * 
     * @return la valeur courante de l'attribut id periode annonce
     */
    public String getIdPeriodeAnnonce() {
        return idPeriodeAnnonce;
    }

    public String getMontantAit() {
        return montantAit;
    }

    public String getMontantAllocAssistance() {
        return montantAllocAssistance;
    }

    /**
     * getter pour l'attribut nombre jours
     * 
     * @return la valeur courante de l'attribut nombre jours
     */
    public String getNombreJours() {
        return nombreJours;
    }

    /**
     * getter pour l'attribut nombre jours interruption
     * 
     * @return la valeur courante de l'attribut nombre jours interruption
     */
    public String getNombreJoursInterruption() {
        return nombreJoursInterruption;
    }

    /**
     * getter pour l'attribut periode a
     * 
     * @return la valeur courante de l'attribut periode a
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * getter pour l'attribut periode de
     * 
     * @return la valeur courante de l'attribut periode de
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /**
     * getter pour l'attribut taux journalier
     * 
     * @return la valeur courante de l'attribut taux journalier
     */
    public String getTauxJournalier() {
        return tauxJournalier;
    }

    /**
     * getter pour l'attribut total IJ
     * 
     * @return la valeur courante de l'attribut total IJ
     */
    public String getTotalIJ() {
        return totalIJ;
    }

    /**
     * getter pour l'attribut versement IJ
     * 
     * @return la valeur courante de l'attribut versement IJ
     */
    public String getVersementIJ() {
        return versementIJ;
    }

    /**
     * setter pour l'attribut motif interruption
     * 
     * @param motifInterruption
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeMotifInterruption(String motifInterruption) {
        codeMotifInterruption = motifInterruption;
    }

    /**
     * setter pour l'attribut code valeur total IJ
     * 
     * @param codeValeurTotalIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeValeurTotalIJ(String codeValeurTotalIJ) {
        this.codeValeurTotalIJ = codeValeurTotalIJ;
    }

    /**
     * setter pour l'attribut deduction nourriture logement
     * 
     * @param deductionNourritureLogement
     *            une nouvelle valeur pour cet attribut
     */
    public void setDeductionNourritureLogement(String deductionNourritureLogement) {
        this.deductionNourritureLogement = deductionNourritureLogement;
    }

    public void setDroitPrestationPourEnfant(String droitPrestationPourEnfant) {
        this.droitPrestationPourEnfant = droitPrestationPourEnfant;
    }

    public void setGarantieDroitAcquis5emeRevision(String garantieDroitAcquis5emeRevision) {
        this.garantieDroitAcquis5emeRevision = garantieDroitAcquis5emeRevision;
    }

    /**
     * setter pour l'attribut id annonce
     * 
     * @param idAnnonce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    /**
     * setter pour l'attribut id periode annonce
     * 
     * @param idPeriodeAnnonce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPeriodeAnnonce(String idPeriodeAnnonce) {
        this.idPeriodeAnnonce = idPeriodeAnnonce;
    }

    public void setMontantAit(String montantAit) {
        this.montantAit = montantAit;
    }

    public void setMontantAllocAssistance(String montantAllocAssistance) {
        this.montantAllocAssistance = montantAllocAssistance;
    }

    /**
     * setter pour l'attribut nombre jours
     * 
     * @param nombreJours
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJours(String nombreJours) {
        this.nombreJours = nombreJours;
    }

    /**
     * setter pour l'attribut nombre jours interruption
     * 
     * @param nombreJoursInterruption
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursInterruption(String nombreJoursInterruption) {
        this.nombreJoursInterruption = nombreJoursInterruption;
    }

    /**
     * setter pour l'attribut periode a
     * 
     * @param periodeA
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    /**
     * setter pour l'attribut periode de
     * 
     * @param periodeDe
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
    }

    /**
     * setter pour l'attribut taux journalier
     * 
     * @param tauxJournalier
     *            une nouvelle valeur pour cet attribut
     */
    public void setTauxJournalier(String tauxJournalier) {
        this.tauxJournalier = tauxJournalier;
    }

    /**
     * setter pour l'attribut total IJ
     * 
     * @param totalIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setTotalIJ(String totalIJ) {
        this.totalIJ = totalIJ;
    }

    /**
     * setter pour l'attribut versement IJ
     * 
     * @param versementIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setVersementIJ(String versementIJ) {
        this.versementIJ = versementIJ;
    }
}
