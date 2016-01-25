package globaz.osiris.db.suiviprocedure;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class CASursisConcordataire extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_DATE_ASSEMBLEE_CREANCIERS = "dateAssCreanciers";
    public static final String FIELD_DATE_ECHEANCE_PROLONGATION = "dateEchProlong";
    public static final String FIELD_DATE_ECHEANCE_SURSIS = "dateEcheance";

    public static final String FIELD_DATE_FIN_SURSIS = "dateFinSursis";
    public static final String FIELD_DATE_HOMOLOGATION_SURSIS = "dateHomologation";
    public static final String FIELD_DATE_PRODUCTION = "dateProduction";
    public static final String FIELD_DATE_RECTIFICATION_PRODUCTION = "dateRectifProd";
    public static final String FIELD_DATE_REVOCATION_SURSIS = "dateRevocSursis";
    public static final String FIELD_DATE_SURSIS_CONCORDATAIRE = "dateSursisConcord";
    public static final String FIELD_ID_COMPTEANNEXE = "idCompteAnnexe";
    public static final String FIELD_ID_SURSIS_CONCORDATAIRE = "idSursisConcordata";
    public static final String FIELD_MONTANT_PRODUCTION = "montantProd";
    private static final String LABEL_CONTENTIEUX_NON_RENSEIGNE = "CONTENTIEUX_NON_RENSEIGNE";
    private static final String LABEL_DATE_SURSIS_CONCORDATAIRE_DOSSIER = "DATE_SURSIS_CONCORDATAIRE_DOSSIER";
    private static final String LABEL_DATE_SURSIS_CONCORDATAIRE_NON_RENSEIGNEE = "DATE_SURSIS_CONCORDATAIRE_NON_RENSEIGNEE";

    private static final String TABLE_NAME = "CASURSP";

    private String dateAssembleeCreanciers = new String();

    private String dateEcheanceProlongation = new String();
    private String dateEcheanceSursis = new String();
    private String dateFinSursis = new String();
    private String dateHomologationSursis = new String();
    private String dateProduction = new String();
    private String dateRectificationProduction = new String();
    private String dateRevocationSursis = new String();
    private String dateSursisConcordataire = new String();
    private String idCompteAnnexe = new String();
    private String idSursisConcordataire = new String();
    private String montantProduction = new String();

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (!checkForBlank(transaction)) {
            return;
        }

        if (getTestManager().getCount() != 0) {
            _addError(transaction, getSession().getLabel(CASursisConcordataire.LABEL_DATE_SURSIS_CONCORDATAIRE_DOSSIER));
            return;
        }

        idSursisConcordataire = this._incCounter(transaction, idSursisConcordataire);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (getTestManager().getCount() > 1) {
            _addError(transaction, getSession().getLabel(CASursisConcordataire.LABEL_DATE_SURSIS_CONCORDATAIRE_DOSSIER));
        }
    }

    @Override
    protected String _getTableName() {
        return CASursisConcordataire.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSursisConcordataire = statement.dbReadNumeric(CASursisConcordataire.FIELD_ID_SURSIS_CONCORDATAIRE);

        dateSursisConcordataire = statement.dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_SURSIS_CONCORDATAIRE);
        dateEcheanceSursis = statement.dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_ECHEANCE_SURSIS);
        dateProduction = statement.dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_PRODUCTION);
        dateRectificationProduction = statement
                .dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_RECTIFICATION_PRODUCTION);
        dateEcheanceProlongation = statement.dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_ECHEANCE_PROLONGATION);
        dateRevocationSursis = statement.dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_REVOCATION_SURSIS);
        dateAssembleeCreanciers = statement.dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_ASSEMBLEE_CREANCIERS);
        dateHomologationSursis = statement.dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_HOMOLOGATION_SURSIS);
        dateFinSursis = statement.dbReadDateAMJ(CASursisConcordataire.FIELD_DATE_FIN_SURSIS);
        idCompteAnnexe = statement.dbReadNumeric(CASursisConcordataire.FIELD_ID_COMPTEANNEXE);

        montantProduction = statement.dbReadNumeric(CASursisConcordataire.FIELD_MONTANT_PRODUCTION, 2);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        checkForBlank(statement.getTransaction());
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CASursisConcordataire.FIELD_ID_SURSIS_CONCORDATAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idSursisConcordataire));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CASursisConcordataire.FIELD_ID_SURSIS_CONCORDATAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idSursisConcordataire, "idSursisConcordataire"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_SURSIS_CONCORDATAIRE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateSursisConcordataire, "dateSursisConcordataire"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_ECHEANCE_SURSIS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEcheanceSursis, "dateEcheanceSursis"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_PRODUCTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateProduction, "dateProduction"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_RECTIFICATION_PRODUCTION, this._dbWriteDateAMJ(
                statement.getTransaction(), dateRectificationProduction, "dateRectificationProduction"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_ECHEANCE_PROLONGATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEcheanceProlongation, "dateEcheanceProlongation"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_REVOCATION_SURSIS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRevocationSursis, "dateRevocationSursis"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_ASSEMBLEE_CREANCIERS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateAssembleeCreanciers, "dateAssembleeCreanciers"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_HOMOLOGATION_SURSIS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateHomologationSursis, "dateHomologationSursis"));
        statement.writeField(CASursisConcordataire.FIELD_DATE_FIN_SURSIS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinSursis, "dateFinSursis"));
        statement.writeField(CASursisConcordataire.FIELD_MONTANT_PRODUCTION,
                this._dbWriteNumeric(statement.getTransaction(), montantProduction, "montantProduction"));
        statement.writeField(CASursisConcordataire.FIELD_ID_COMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), idCompteAnnexe, "idCompteAnnexe"));
    }

    private boolean checkForBlank(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe())) {
            _addError(transaction, getSession().getLabel(CASursisConcordataire.LABEL_CONTENTIEUX_NON_RENSEIGNE));
            return false;
        }

        if (JadeStringUtil.isBlank(getDateSursisConcordataire())) {
            _addError(transaction,
                    getSession().getLabel(CASursisConcordataire.LABEL_DATE_SURSIS_CONCORDATAIRE_NON_RENSEIGNEE));
            return false;
        }

        return true;
    }

    public String getDateAssembleeCreanciers() {
        return dateAssembleeCreanciers;
    }

    public String getDateEcheanceProlongation() {
        return dateEcheanceProlongation;
    }

    public String getDateEcheanceSursis() {
        return dateEcheanceSursis;
    }

    public String getDateFinSursis() {
        return dateFinSursis;
    }

    public String getDateHomologationSursis() {
        return dateHomologationSursis;
    }

    public String getDateProduction() {
        return dateProduction;
    }

    public String getDateRectificationProduction() {
        return dateRectificationProduction;
    }

    public String getDateRevocationSursis() {
        return dateRevocationSursis;
    }

    public String getDateSursisConcordataire() {
        return dateSursisConcordataire;
    }

    /**
     * @return the idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdSursisConcordataire() {
        return idSursisConcordataire;
    }

    /**
     * @return the montantProduction
     */
    public String getMontantProduction() {
        return montantProduction;
    }

    private CASursisConcordataireManager getTestManager() throws Exception {
        CASursisConcordataireManager manager = new CASursisConcordataireManager();
        manager.setSession(getSession());

        manager.setForIdCompteAnnexe(getIdCompteAnnexe());
        manager.setForDateSursisConcordataire(getDateSursisConcordataire());

        return manager;
    }

    public void setDateAssembleeCreanciers(String dateAssembleeCreanciers) {
        this.dateAssembleeCreanciers = dateAssembleeCreanciers;
    }

    public void setDateEcheanceProlongation(String dateEcheanceProlongation) {
        this.dateEcheanceProlongation = dateEcheanceProlongation;
    }

    public void setDateEcheanceSursis(String dateEcheanceSursis) {
        this.dateEcheanceSursis = dateEcheanceSursis;
    }

    public void setDateFinSursis(String dateFinSursis) {
        this.dateFinSursis = dateFinSursis;
    }

    public void setDateHomologationSursis(String dateHomologationSursis) {
        this.dateHomologationSursis = dateHomologationSursis;
    }

    public void setDateProduction(String dateProduction) {
        this.dateProduction = dateProduction;
    }

    public void setDateRectificationProduction(String dateRectificationProduction) {
        this.dateRectificationProduction = dateRectificationProduction;
    }

    public void setDateRevocationSursis(String dateRevocationSursis) {
        this.dateRevocationSursis = dateRevocationSursis;
    }

    public void setDateSursisConcordataire(String dateSursisConcordataire) {
        this.dateSursisConcordataire = dateSursisConcordataire;
    }

    /**
     * @param idCompteAnnexe
     *            the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdSursisConcordataire(String idSursisConcordataire) {
        this.idSursisConcordataire = idSursisConcordataire;
    }

    /**
     * @param montantProduction
     *            the montantProduction to set
     */
    public void setMontantProduction(String montantProduction) {
        this.montantProduction = montantProduction;
    }

}
