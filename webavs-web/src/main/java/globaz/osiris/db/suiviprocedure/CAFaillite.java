package globaz.osiris.db.suiviprocedure;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class CAFaillite extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_DATE_ANNULATION_PRODUCTION = "dateAnnulProd";
    public static final String FIELD_DATE_CLOTURE_FAILLITE = "dateCloture";
    public static final String FIELD_DATE_ETAT_COLLOCATION = "dateEtatColloc";
    public static final String FIELD_DATE_FAILLITE = "dateFaillite";
    public static final String FIELD_DATE_MODIFICATION_ETAT_COLLOCATION = "dateModifColloc";
    public static final String FIELD_DATE_PRODUCTION = "dateProduction";
    public static final String FIELD_DATE_PRODUCTION_DEFINITIVE = "dateProdDefini";
    public static final String FIELD_DATE_REVOCATION = "dateRevocation";
    public static final String FIELD_DATE_SUSPENSION_FAILLITE = "dateSuspension";
    public static final String FIELD_ID_COMPTEANNEXE = "idCompteAnnexe";
    public static final String FIELD_ID_FAILLITE = "idFaillite";
    public static final String FIELD_MONTANT_PRODUCTION = "montantProd";
    public static final String FIELD_COMMENTAIRE = "commentaire";
    private static final String LABEL_DATE_FAILLITE_DOSSIER = "DATE_FAILLITE_DOSSIER";
    private static final String LABEL_DATE_FAILLITE_NON_RENSEIGNEE = "DATE_FAILLITE_NON_RENSEIGNEE";

    private static final String TABLE_NAME = "CAFAILP";

    private String dateAnnulationProduction = new String();

    private String dateClotureFaillite = new String();
    private String dateEtatCollocation = new String();
    private String dateFaillite = new String();
    private String dateModificationEtatCollocation = new String();
    private String dateProduction = new String();
    private String dateProductionDefinitive = new String();
    private String dateRevocation = new String();
    private String dateSuspensionFaillite = new String();
    private String idCompteAnnexe = new String();
    private String idFaillite = new String();
    private String montantProduction = new String();
    private String commentaire = new String();

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (!checkForBlank(transaction)) {
            return;
        }

        if (getTestManager().getCount() != 0) {
            _addError(transaction, getSession().getLabel(CAFaillite.LABEL_DATE_FAILLITE_DOSSIER));
            return;
        }

        idFaillite = this._incCounter(transaction, idFaillite);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (getTestManager().getCount() > 1) {
            _addError(transaction, getSession().getLabel(CAFaillite.LABEL_DATE_FAILLITE_DOSSIER));
        }
    }

    @Override
    protected String _getTableName() {
        return CAFaillite.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idFaillite = statement.dbReadNumeric(CAFaillite.FIELD_ID_FAILLITE);
        dateFaillite = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_FAILLITE);
        dateProduction = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_PRODUCTION);
        dateProductionDefinitive = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_PRODUCTION_DEFINITIVE);
        dateAnnulationProduction = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_ANNULATION_PRODUCTION);
        dateRevocation = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_REVOCATION);
        dateSuspensionFaillite = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_SUSPENSION_FAILLITE);
        dateEtatCollocation = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_ETAT_COLLOCATION);
        dateModificationEtatCollocation = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_MODIFICATION_ETAT_COLLOCATION);
        dateClotureFaillite = statement.dbReadDateAMJ(CAFaillite.FIELD_DATE_CLOTURE_FAILLITE);
        montantProduction = statement.dbReadNumeric(CAFaillite.FIELD_MONTANT_PRODUCTION, 2);
        idCompteAnnexe = statement.dbReadNumeric(CAFaillite.FIELD_ID_COMPTEANNEXE);
        commentaire = statement.dbReadString(CAFaillite.FIELD_COMMENTAIRE);
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
        statement.writeKey(CAFaillite.FIELD_ID_FAILLITE, this._dbWriteNumeric(statement.getTransaction(), idFaillite));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAFaillite.FIELD_ID_FAILLITE,
                this._dbWriteNumeric(statement.getTransaction(), idFaillite, "idFaillite"));
        statement.writeField(CAFaillite.FIELD_DATE_FAILLITE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFaillite, "dateFaillite"));
        statement.writeField(CAFaillite.FIELD_DATE_PRODUCTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateProduction, "dateProduction"));
        statement.writeField(CAFaillite.FIELD_DATE_PRODUCTION_DEFINITIVE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateProductionDefinitive, "dateProductionDefinitive"));
        statement.writeField(CAFaillite.FIELD_DATE_ANNULATION_PRODUCTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateAnnulationProduction, "dateAnnulationProduction"));
        statement.writeField(CAFaillite.FIELD_DATE_REVOCATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRevocation, "dateRevocation"));
        statement.writeField(CAFaillite.FIELD_DATE_SUSPENSION_FAILLITE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateSuspensionFaillite, "dateSuspensionFaillite"));
        statement.writeField(CAFaillite.FIELD_DATE_ETAT_COLLOCATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEtatCollocation, "dateEtatCollocation"));
        statement.writeField(CAFaillite.FIELD_DATE_MODIFICATION_ETAT_COLLOCATION, this._dbWriteDateAMJ(
                statement.getTransaction(), dateModificationEtatCollocation, "dateModificationEtatCollocation"));
        statement.writeField(CAFaillite.FIELD_DATE_CLOTURE_FAILLITE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateClotureFaillite, "dateClotureFaillite"));
        statement.writeField(CAFaillite.FIELD_MONTANT_PRODUCTION,
                this._dbWriteNumeric(statement.getTransaction(), montantProduction, "montantProduction"));
        statement.writeField(CAFaillite.FIELD_ID_COMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), idCompteAnnexe, "idCompteAnnexe"));
        statement.writeField(CAFaillite.FIELD_COMMENTAIRE,
                this._dbWriteString(statement.getTransaction(), commentaire, "commentaire"));
    }

    private boolean checkForBlank(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isBlank(getDateFaillite())) {
            _addError(transaction, getSession().getLabel(CAFaillite.LABEL_DATE_FAILLITE_NON_RENSEIGNEE));
            return false;
        }

        return true;
    }

    public String getDateAnnulationProduction() {
        return dateAnnulationProduction;
    }

    public String getDateClotureFaillite() {
        return dateClotureFaillite;
    }

    public String getDateEtatCollocation() {
        return dateEtatCollocation;
    }

    public String getDateFaillite() {
        return dateFaillite;
    }

    public String getDateModificationEtatCollocation() {
        return dateModificationEtatCollocation;
    }

    public String getDateProduction() {
        return dateProduction;
    }

    public String getDateProductionDefinitive() {
        return dateProductionDefinitive;
    }

    public String getDateRevocation() {
        return dateRevocation;
    }

    public String getDateSuspensionFaillite() {
        return dateSuspensionFaillite;
    }

    /**
     * @return the idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdFaillite() {
        return idFaillite;
    }

    /**
     * @return the montantProduction
     */
    public String getMontantProduction() {
        return montantProduction;
    }

    public String getCommentaire() {
        return commentaire;
    }

    private CAFailliteManager getTestManager() throws Exception {
        CAFailliteManager manager = new CAFailliteManager();
        manager.setSession(getSession());

        manager.setForIdCompteAnnexe(getIdCompteAnnexe());
        manager.setForDateFaillite(getDateFaillite());

        return manager;
    }

    public void setDateAnnulationProduction(String dateAnnulationProduction) {
        this.dateAnnulationProduction = dateAnnulationProduction;
    }

    public void setDateClotureFaillite(String dateClotureFaillite) {
        this.dateClotureFaillite = dateClotureFaillite;
    }

    public void setDateEtatCollocation(String dateEtatCollocation) {
        this.dateEtatCollocation = dateEtatCollocation;
    }

    public void setDateFaillite(String dateFaillite) {
        this.dateFaillite = dateFaillite;
    }

    public void setDateModificationEtatCollocation(String dateModificationEtatCollocation) {
        this.dateModificationEtatCollocation = dateModificationEtatCollocation;
    }

    public void setDateProduction(String dateProduction) {
        this.dateProduction = dateProduction;
    }

    public void setDateProductionDefinitive(String dateProductionDefinitive) {
        this.dateProductionDefinitive = dateProductionDefinitive;
    }

    public void setDateRevocation(String dateRevocation) {
        this.dateRevocation = dateRevocation;
    }

    public void setDateSuspensionFaillite(String dateSuspensionFaillite) {
        this.dateSuspensionFaillite = dateSuspensionFaillite;
    }

    /**
     * @param idCompteAnnexe
     *            the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdFaillite(String idFaillite) {
        this.idFaillite = idFaillite;
    }

    /**
     * @param montantProduction
     *            the montantProduction to set
     */
    public void setMontantProduction(String montantProduction) {
        this.montantProduction = montantProduction;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

}
