package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;

public class APDroitPanSituation extends BEntity  implements IPRCloneable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "APPANDEMIE";
    public static final String FIELDNAME_ID_APG_PANDEMIE = "ID_APG_PANDEMIE";
    public static final String FIELDNAME_ID_DROIT = "ID_DROIT";
    public static final String FIELDNAME_ACTIVITE_SALARIEE = "ACTIVITE_SALARIEE";
    public static final String FIELDNAME_RECEPTION_SALAIRE = "RECEPTION_SALAIRE";
    public static final String FIELDNAME_COPIE_DECOMPTE = "COPIE_DECOMPTE";
    public static final String FIELDNAME_CATEGORIE_ENTREPRISE = "CATEGORIE_ENTREPRISE";
    public static final String FIELDNAME_CATEGORIE_ENTREPRISE_LIBELLE = "CATEGORIE_ENTREPRISE_LIBELLE";
    public static final String FIELDNAME_MOTIF_GARDE = "MOTIF_GARDE";
    public static final String FIELDNAME_MOTIF_GARDE_HANDICAP = "MOTIF_GARDE_HANDICAP";
    public static final String FIELDNAME_QUARANTAINE_ORDONNEE = "QUARANTAINE_ORDONNEE";
    public static final String FIELDNAME_QUARANTAINE_PAR_QUI = "QUARANTAINE_PAR_QUI";
    public static final String FIELDNAME_FERMETURE_ETABLISSEMENT_DEBUT = "FERMETURE_ETABLISSEMENT_DEBUT";
    public static final String FIELDNAME_FERMETURE_ETABLISSEMENT_FIN = "FERMETURE_ETABLISSEMENT_FIN";
    public static final String FIELDNAME_MANIFESTATION_ANNULEE_DEBUT = "MANIFESTATION_ANNULEE_DEBUT";
    public static final String FIELDNAME_MANIFESTATION_ANNULEE_FIN = "MANIFESTATION_ANNULEE_FIN";
    public static final String FIELDNAME_PERTE_GAINS_DEBUT = "PERTE_GAINS_DEBUT";
    public static final String FIELDNAME_PERTE_GAINS_FIN = "PERTE_GAINS_FIN";
    public static final String FIELDNAME_REMARQUE = "REMARQUE";
    public static final String FIELDNAME_GROUPE_RISQUE = "GROUPE_RISQUE";
    public static final String FIELDNAME_CAUSE_MALADIE_ = "CAUSE_MALADIE";
    public static final String FIELDNAME_SENDER_ID = "SENDER_ID";

    public static final String FIELDNAME_IS_TELETRAVAIL = "IS_TELETRAVAIL";
    public static final String FIELDNAME_VALEUR_PERTE = "VALEUR_PERTE";
    public static final String FIELDNAME_UNIT_PERTE = "UNIT_PERTE";

    // Vague 2
    public static final String FIELDNAME_REFERENCE_DATA = "REFERENCE_DATA";
    public static final String FIELDNAME_ACTIVITE_LIMITEE_DEBUT = "ACTIVITE_LIMITEE_DEBUT";
    public static final String FIELDNAME_ACTIVITE_LIMITEE_FIN = "ACTIVITE_LIMITEE_FIN";
    public static final String FIELDNAME_ACTIVITE_LIMITEE_START_DATE = "ACTIVITE_LIMITEE_START_DATE";
    public static final String FIELDNAME_ACTIVITE_LIMITEE_LOSS_VALUE = "ACTIVITE_LIMITEE_LOSS_VALUE";
    public static final String FIELDNAME_ACTIVITE_LIMITEE_UNIT = "ACTIVITE_LIMITEE_UNIT";
    public static final String FIELDNAME_ACTIVITE_LIMITEE_REASON = "ACTIVITE_LIMITEE_REASON";

    private String idApgPandemie = "";
    private String idDroit = "";
    private boolean activiteSalarie = false;
    private boolean paiementEmployeur = false;
    private boolean copieDecompteEmployeur = false;
    private boolean quarantaineOrdonnee = false;
    private String categorieEntreprise = "";
    private String categorieEntrepriseLibelle ="";
    private String motifGarde = "";
    private String motifGardeHandicap = "";
    private String quarantaineOrdonneePar = "";

    private String dateFermetureEtablissementDebut = "";
    private String dateFermetureEtablissementFin = "";
    private String dateDebutManifestationAnnulee = "";
    private String dateFinManifestationAnnulee = "";
    private String dateDebutPerteGains = "";
    private String dateFinPerteGains = "";
    private String remarque = "";
    private String groupeRisque = "";
    private String causeMaladie = "";
    private String senderId = "";

    private boolean isTeletravail = false;
    private String valeurPerte = "";
    private String unitPerte = "";

    // Vague 2
    private String referenceData = "";
    private String dateDebutActiviteLimitee = "";
    private String dateFinActiviteLimitee = "";
    private String startDateActiviteLimitee = "";
    private String lossValueActiviteLimitee = "";
    private String unitActiviteLimitee = "";
    private String reasonActiviteLimitee = "";

    /**
     * @return la chaine TABLE_NAME
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APDroitPanSituation.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idApgPandemie = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_ID_APG_PANDEMIE);
        idDroit = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_ID_DROIT);
        activiteSalarie = statement.dbReadBoolean(APDroitPanSituation.FIELDNAME_ACTIVITE_SALARIEE);
        copieDecompteEmployeur = statement.dbReadBoolean(APDroitPanSituation.FIELDNAME_COPIE_DECOMPTE);
        paiementEmployeur = statement.dbReadBoolean(APDroitPanSituation.FIELDNAME_RECEPTION_SALAIRE);
        categorieEntreprise = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_CATEGORIE_ENTREPRISE);
        categorieEntrepriseLibelle = statement.dbReadString(APDroitPanSituation.FIELDNAME_CATEGORIE_ENTREPRISE_LIBELLE);
        motifGarde = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_MOTIF_GARDE);
        motifGardeHandicap = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_MOTIF_GARDE_HANDICAP);
        quarantaineOrdonnee = statement.dbReadBoolean(APDroitPanSituation.FIELDNAME_QUARANTAINE_ORDONNEE);
        quarantaineOrdonneePar = statement.dbReadString(APDroitPanSituation.FIELDNAME_QUARANTAINE_PAR_QUI);
        dateFermetureEtablissementDebut = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_FERMETURE_ETABLISSEMENT_DEBUT);
        dateFermetureEtablissementFin = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_FERMETURE_ETABLISSEMENT_FIN);
        dateDebutManifestationAnnulee = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_MANIFESTATION_ANNULEE_DEBUT);
        dateFinManifestationAnnulee = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_MANIFESTATION_ANNULEE_FIN);
        dateDebutPerteGains = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_PERTE_GAINS_DEBUT);
        dateFinPerteGains = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_PERTE_GAINS_FIN);
        remarque = statement.dbReadString(APDroitPanSituation.FIELDNAME_REMARQUE);
        groupeRisque = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_GROUPE_RISQUE);
        causeMaladie = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_CAUSE_MALADIE_);
        senderId = statement.dbReadString(APDroitPanSituation.FIELDNAME_SENDER_ID);

        isTeletravail = statement.dbReadBoolean(APDroitPanSituation.FIELDNAME_IS_TELETRAVAIL);
        valeurPerte = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_VALEUR_PERTE);
        unitPerte = statement.dbReadString(APDroitPanSituation.FIELDNAME_UNIT_PERTE);

        referenceData = statement.dbReadString(APDroitPanSituation.FIELDNAME_REFERENCE_DATA);
        dateDebutActiviteLimitee = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_DEBUT);
        dateFinActiviteLimitee = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_FIN);
        startDateActiviteLimitee = statement.dbReadDateAMJ(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_START_DATE);
        lossValueActiviteLimitee = statement.dbReadNumeric(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_LOSS_VALUE);
        unitActiviteLimitee = statement.dbReadString(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_UNIT);
        reasonActiviteLimitee = statement.dbReadString(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_REASON);
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APDroitPanSituation.FIELDNAME_ID_APG_PANDEMIE,
                this._dbWriteNumeric(statement.getTransaction(), getIdApgPandemie(), "idApgPandemie"));
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdApgPandemie(this._incCounter(transaction, idApgPandemie, _getTableName()));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(APDroitPanSituation.FIELDNAME_ID_APG_PANDEMIE, this._dbWriteNumeric(statement.getTransaction(), idApgPandemie, "idApgPandemie"));
        statement.writeField(APDroitPanSituation.FIELDNAME_ID_DROIT, this._dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));
        statement.writeField(APDroitPanSituation.FIELDNAME_ACTIVITE_SALARIEE, this._dbWriteBoolean(statement.getTransaction(), activiteSalarie, "activiteSalarie"));
        statement.writeField(APDroitPanSituation.FIELDNAME_RECEPTION_SALAIRE, this._dbWriteBoolean(statement.getTransaction(), paiementEmployeur, "paiementEmployeur"));
        statement.writeField(APDroitPanSituation.FIELDNAME_COPIE_DECOMPTE, this._dbWriteBoolean(statement.getTransaction(), copieDecompteEmployeur, "copieDecompteEmployeur"));
        statement.writeField(APDroitPanSituation.FIELDNAME_CATEGORIE_ENTREPRISE, this._dbWriteNumeric(statement.getTransaction(), categorieEntreprise));
        statement.writeField(APDroitPanSituation.FIELDNAME_CATEGORIE_ENTREPRISE_LIBELLE, this._dbWriteString(statement.getTransaction(), categorieEntrepriseLibelle, "categorieEntrepriseLibelle"));
        statement.writeField(APDroitPanSituation.FIELDNAME_MOTIF_GARDE, this._dbWriteNumeric(statement.getTransaction(), motifGarde));
        statement.writeField(APDroitPanSituation.FIELDNAME_MOTIF_GARDE_HANDICAP, this._dbWriteNumeric(statement.getTransaction(), motifGardeHandicap));
        statement.writeField(APDroitPanSituation.FIELDNAME_QUARANTAINE_ORDONNEE, this._dbWriteBoolean(statement.getTransaction(), quarantaineOrdonnee, "quarantaineOrdonnee"));
        statement.writeField(APDroitPanSituation.FIELDNAME_QUARANTAINE_PAR_QUI, this._dbWriteString(statement.getTransaction(), quarantaineOrdonneePar, "quarantaineOrdonneePar"));
        statement.writeField(APDroitPanSituation.FIELDNAME_FERMETURE_ETABLISSEMENT_DEBUT, this._dbWriteDateAMJ(statement.getTransaction(), dateFermetureEtablissementDebut, "dateFermetureEtablissementDebut"));
        statement.writeField(APDroitPanSituation.FIELDNAME_FERMETURE_ETABLISSEMENT_FIN, this._dbWriteDateAMJ(statement.getTransaction(), dateFermetureEtablissementFin, "dateFermetureEtablissementFin"));
        statement.writeField(APDroitPanSituation.FIELDNAME_MANIFESTATION_ANNULEE_DEBUT, this._dbWriteDateAMJ(statement.getTransaction(), dateDebutManifestationAnnulee, "dateDebutManifestationAnnulee"));
        statement.writeField(APDroitPanSituation.FIELDNAME_MANIFESTATION_ANNULEE_FIN, this._dbWriteDateAMJ(statement.getTransaction(), dateFinManifestationAnnulee, "dateFinManifestationAnnulee"));
        statement.writeField(APDroitPanSituation.FIELDNAME_PERTE_GAINS_DEBUT, this._dbWriteDateAMJ(statement.getTransaction(), dateDebutPerteGains, "dateDebutPerteGains"));
        statement.writeField(APDroitPanSituation.FIELDNAME_PERTE_GAINS_FIN, this._dbWriteDateAMJ(statement.getTransaction(), dateFinPerteGains, "dateFinPerteGains"));
        statement.writeField(APDroitPanSituation.FIELDNAME_REMARQUE, this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(APDroitPanSituation.FIELDNAME_GROUPE_RISQUE, this._dbWriteNumeric(statement.getTransaction(), groupeRisque));
        statement.writeField(APDroitPanSituation.FIELDNAME_CAUSE_MALADIE_, this._dbWriteNumeric(statement.getTransaction(), causeMaladie));
        statement.writeField(APDroitPanSituation.FIELDNAME_SENDER_ID, this._dbWriteString(statement.getTransaction(), senderId));

        statement.writeField(APDroitPanSituation.FIELDNAME_IS_TELETRAVAIL,
                this._dbWriteBoolean(statement.getTransaction(), isTeletravail));
        statement.writeField(APDroitPanSituation.FIELDNAME_VALEUR_PERTE,
                this._dbWriteNumeric(statement.getTransaction(), valeurPerte));
        statement.writeField(APDroitPanSituation.FIELDNAME_UNIT_PERTE, this._dbWriteString(statement.getTransaction(), unitPerte));

        statement.writeField(APDroitPanSituation.FIELDNAME_REFERENCE_DATA, this._dbWriteString(statement.getTransaction(), referenceData));
        statement.writeField(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_DEBUT, this._dbWriteDateAMJ(statement.getTransaction(), dateDebutActiviteLimitee));
        statement.writeField(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_FIN, this._dbWriteDateAMJ(statement.getTransaction(), dateFinActiviteLimitee));
        statement.writeField(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_START_DATE, this._dbWriteDateAMJ(statement.getTransaction(), startDateActiviteLimitee));
        statement.writeField(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_LOSS_VALUE, this._dbWriteNumeric(statement.getTransaction(), lossValueActiviteLimitee));
        statement.writeField(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_UNIT, this._dbWriteString(statement.getTransaction(), unitActiviteLimitee));
        statement.writeField(APDroitPanSituation.FIELDNAME_ACTIVITE_LIMITEE_REASON, this._dbWriteString(statement.getTransaction(), reasonActiviteLimitee));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    public Boolean isActiviteSalarie() {
        return activiteSalarie;
    }

    public void setActiviteSalarie(boolean activiteSalarie) {
        this.activiteSalarie = activiteSalarie;
    }

    public Boolean isCopieDecompteEmployeur() {
        return copieDecompteEmployeur;
    }

    public void setCopieDecompteEmployeur(boolean copieDecompteEmployeur) {
        this.copieDecompteEmployeur = copieDecompteEmployeur;
    }

    public Boolean isQuarantaineOrdonnee() {
        return quarantaineOrdonnee;
    }

    public void setQuarantaineOrdonnee(boolean quarantaineOrdonnee) {
        this.quarantaineOrdonnee = quarantaineOrdonnee;
    }

    public String getCategorieEntreprise() {
        return categorieEntreprise;
    }

    public void setCategorieEntreprise(String categorieEntreprise) {
        this.categorieEntreprise = categorieEntreprise;
    }

    public String getMotifGarde() {
        return motifGarde;
    }

    public void setMotifGarde(String motifGarde) {
        this.motifGarde = motifGarde;
    }

    public String getQuarantaineOrdonneePar() {
        return quarantaineOrdonneePar;
    }

    public void setQuarantaineOrdonneePar(String quarantaineOrdonneePar) {
        this.quarantaineOrdonneePar = quarantaineOrdonneePar;
    }

    public Boolean isPaiementEmployeur() {
        return paiementEmployeur;
    }

    public void setPaiementEmployeur(boolean paiementEmployeur) {
        this.paiementEmployeur = paiementEmployeur;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public String getIdApgPandemie() {
        return idApgPandemie;
    }

    public void setIdApgPandemie(String idApgPandemie) {
        this.idApgPandemie = idApgPandemie;
    }

    public String getCategorieEntrepriseLibelle() {
        return categorieEntrepriseLibelle;
    }

    public void setCategorieEntrepriseLibelle(String categorieEntrepriseLibelle) {
        this.categorieEntrepriseLibelle = categorieEntrepriseLibelle;
    }

    public String getDateFermetureEtablissementDebut() {
        return dateFermetureEtablissementDebut;
    }

    public void setDateFermetureEtablissementDebut(String dateFermetureEtablissementDebut) {
        this.dateFermetureEtablissementDebut = dateFermetureEtablissementDebut;
    }

    public String getDateFermetureEtablissementFin() {
        return dateFermetureEtablissementFin;
    }

    public void setDateFermetureEtablissementFin(String dateFermetureEtablissementFin) {
        this.dateFermetureEtablissementFin = dateFermetureEtablissementFin;
    }

    public String getDateDebutManifestationAnnulee() {
        return dateDebutManifestationAnnulee;
    }

    public void setDateDebutManifestationAnnulee(String dateDebutManifestationAnnulee) {
        this.dateDebutManifestationAnnulee = dateDebutManifestationAnnulee;
    }

    public String getDateFinManifestationAnnulee() {
        return dateFinManifestationAnnulee;
    }

    public void setDateFinManifestationAnnulee(String dateFinManifestationAnnulee) {
        this.dateFinManifestationAnnulee = dateFinManifestationAnnulee;
    }

    public String getMotifGardeHandicap() {
        return motifGardeHandicap;
    }

    public void setMotifGardeHandicap(String motifGardeHandicap) {
        this.motifGardeHandicap = motifGardeHandicap;
    }

    public String getDateDebutPerteGains() {
        return dateDebutPerteGains;
    }

    public void setDateDebutPerteGains(String dateDebutPerteGains) {
        this.dateDebutPerteGains = dateDebutPerteGains;
    }

    public String getDateFinPerteGains() {
        return dateFinPerteGains;
    }

    public void setDateFinPerteGains(String dateFinPerteGains) {
        this.dateFinPerteGains = dateFinPerteGains;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        APDroitPanSituation clone = new APDroitPanSituation();
        clone = (APDroitPanSituation)this.clone();
        clone._setSpy(null);
        clone._setCreationSpy(null);
        clone.setId(null);
        clone.setIdApgPandemie(null);
        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);
        // on ne veut pas faire de propagation GED
        clone.wantCallExternalServices(isCallExternalServices());
        return clone;
    }

    @Override
    public String getUniquePrimaryKey() {
        return getIdApgPandemie();
    }

    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdApgPandemie(pk);
    }

    public String getGroupeRisque() {
        return groupeRisque;
    }

    public void setGroupeRisque(String groupeRisque) {
        this.groupeRisque = groupeRisque;
    }

    public String getCauseMaladie() {
        return causeMaladie;
    }

    public void setCauseMaladie(String causeMaladie) {
        this.causeMaladie = causeMaladie;
    }

    public boolean isTeletravail() {
        return isTeletravail;
    }

    public void setTeletravail(boolean teletravail) {
        isTeletravail = teletravail;
    }

    public String getValeurPerte() {
        return valeurPerte;
    }

    public void setValeurPerte(String valeurPerte) {
        this.valeurPerte = valeurPerte;
    }

    public String getUnitPerte() {
        return unitPerte;
    }

    public void setUnitPerte(String unitPerte) {
        this.unitPerte = unitPerte;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReferenceData() {
        return referenceData;
    }

    public void setReferenceData(String referenceData) {
        this.referenceData = referenceData;
    }

    public String getDateDebutActiviteLimitee() {
        return dateDebutActiviteLimitee;
    }

    public void setDateDebutActiviteLimitee(String dateDebutActiviteLimitee) {
        this.dateDebutActiviteLimitee = dateDebutActiviteLimitee;
    }

    public String getDateFinActiviteLimitee() {
        return dateFinActiviteLimitee;
    }

    public void setDateFinActiviteLimitee(String dateFinActiviteLimitee) {
        this.dateFinActiviteLimitee = dateFinActiviteLimitee;
    }

    public String getStartDateActiviteLimitee() {
        return startDateActiviteLimitee;
    }

    public void setStartDateActiviteLimitee(String startDateActiviteLimitee) {
        this.startDateActiviteLimitee = startDateActiviteLimitee;
    }

    public String getLossValueActiviteLimitee() {
        return lossValueActiviteLimitee;
    }

    public void setLossValueActiviteLimitee(String lossValueActiviteLimitee) {
        this.lossValueActiviteLimitee = lossValueActiviteLimitee;
    }

    public String getUnitActiviteLimitee() {
        return unitActiviteLimitee;
    }

    public void setUnitActiviteLimitee(String unitActiviteLimitee) {
        this.unitActiviteLimitee = unitActiviteLimitee;
    }

    public String getReasonActiviteLimitee() {
        return reasonActiviteLimitee;
    }

    public void setReasonActiviteLimitee(String reasonActiviteLimitee) {
        this.reasonActiviteLimitee = reasonActiviteLimitee;
    }
}


