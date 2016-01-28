package globaz.prestation.db.infos;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * @author BSC
 */
public class PRInfoCompl extends BEntity implements IPRCloneable {

    private static final long serialVersionUID = -2771654631127048278L;

    public static final String FIELDNAME_ANCIEN_ETAT_DROIT_OU_PRONONCE = "WCTAET";
    public static final String FIELDNAME_CS_TIERS_RESPONSABLE = "WCTTRE";
    public static final String FIELDNAME_DATE_INFO_COMPL = "WCDICO";
    public static final String FIELDNAME_ID_INFO_COMPL = "WCIINC";
    public static final String FIELDNAME_ID_TIERS_CAISSE = "WCICAI";
    public static final String FIELDNAME_IS_CAS_PENIBLE_AI = "WCBICP";
    public static final String FIELDNAME_IS_INDEPENDANT = "WCBIIN";
    public static final String FIELDNAME_IS_PERMIS_SEJOURS = "WCBIPS";
    public static final String FIELDNAME_IS_REFUGIE = "WCBIRE";
    public static final String FIELDNAME_IS_TRANSFERE = "WCBITR";
    public static final String FIELDNAME_MOTIF = "WCTMOT";
    public static final String FIELDNAME_NO_AGENCE = "WCNAGC";
    public static final String FIELDNAME_NO_CAISSE = "WCNCAI";
    public static final String FIELDNAME_REMARQUE = "WCLREM";
    public static final String FIELDNAME_TAXE_FACTUREE_A_ASSURE = "WCMTAX";
    public static final String FIELDNAME_TYPE_DOMAINE = "WCTTYP";
    public static final String FIELDNAME_TYPE_INFO_COMPL = "WCTTIC";
    public static final String FIELDNAME_VETO_PRESTATION = "WCTVPR";
    public static final String FIELDNAME_IS_RENTE_VEUF_LIMITEE = "WCBVLI";
    public static final String FIELDNAME_IS_RENTE_AVEC_SUPPLEMENT_PERSONNE_VEUVE = "WCBRSV";
    public static final String FIELDNAME_IS_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE = "WCBDDA";
    public static final String FIELDNAME_IS_RENTE_AVEC_MONTANT_MINIMUM_MAJORE_INVALIDITE = "WCBMMI";
    public static final String FIELDNAME_IS_RENTE_REDUITE_POUR_SURASSURANCE = "WCBRSU";

    public static final String TABLE_NAME = "PRINFCOM";

    private String ancienEtatDroitOuPrononce;
    private String csTiersResponsable;
    private String csVetoPrestation;
    private String dateInfoCompl;
    private String idInfoCompl;
    private String idTiersCaisse;
    private Boolean isCasPenibleAI;
    private Boolean isIndependant;
    private Boolean isPermisSejours;
    private Boolean isRefugie;
    private Boolean isTransfere;
    private Boolean isRenteLimitee;
    private Boolean isRenteAvecSupplementPourPersonneVeuve;
    private Boolean isRenteAvecDebutDroit5AnsAvantDepotDemande;
    private Boolean isRenteAvecMontantMinimumMajoreInvalidite;
    private Boolean isRenteReduitePourSurassurance;

    private String motif;
    private String noAgence;
    private String noCaisse;
    private String remarque;
    private String taxeFactureeAssure;
    private String typeDomaine;
    private String typeInfoCompl;

    public PRInfoCompl() {
        super();

        ancienEtatDroitOuPrononce = "";
        csTiersResponsable = "";
        csVetoPrestation = "";
        dateInfoCompl = "";
        idInfoCompl = "";
        idTiersCaisse = "";
        isCasPenibleAI = Boolean.FALSE;
        isIndependant = Boolean.FALSE;
        isPermisSejours = Boolean.FALSE;
        isRefugie = Boolean.FALSE;
        isTransfere = Boolean.FALSE;
        isRenteLimitee = Boolean.FALSE;
        isRenteAvecSupplementPourPersonneVeuve = Boolean.FALSE;
        isRenteAvecDebutDroit5AnsAvantDepotDemande = Boolean.FALSE;
        isRenteAvecMontantMinimumMajoreInvalidite = Boolean.FALSE;
        isRenteReduitePourSurassurance = Boolean.FALSE;
        motif = "";
        noAgence = "";
        noCaisse = "";
        remarque = "";
        taxeFactureeAssure = "";
        typeDomaine = "";
        typeInfoCompl = "";
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idInfoCompl = this._incCounter(transaction, "0");
    }

    @Override
    protected String _getTableName() {
        return PRInfoCompl.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        ancienEtatDroitOuPrononce = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_ANCIEN_ETAT_DROIT_OU_PRONONCE);
        csTiersResponsable = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_CS_TIERS_RESPONSABLE);
        csVetoPrestation = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_VETO_PRESTATION);
        dateInfoCompl = statement.dbReadDateAMJ(PRInfoCompl.FIELDNAME_DATE_INFO_COMPL);
        idInfoCompl = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_ID_INFO_COMPL);
        idTiersCaisse = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_ID_TIERS_CAISSE);
        isCasPenibleAI = statement.dbReadBoolean(PRInfoCompl.FIELDNAME_IS_CAS_PENIBLE_AI);
        isIndependant = statement.dbReadBoolean(PRInfoCompl.FIELDNAME_IS_INDEPENDANT);
        isPermisSejours = statement.dbReadBoolean(PRInfoCompl.FIELDNAME_IS_PERMIS_SEJOURS);
        isRefugie = statement.dbReadBoolean(PRInfoCompl.FIELDNAME_IS_REFUGIE);
        isTransfere = statement.dbReadBoolean(PRInfoCompl.FIELDNAME_IS_TRANSFERE);
        isRenteLimitee = statement.dbReadBoolean(PRInfoCompl.FIELDNAME_IS_RENTE_VEUF_LIMITEE);
        isRenteAvecSupplementPourPersonneVeuve = statement
                .dbReadBoolean(PRInfoCompl.FIELDNAME_IS_RENTE_AVEC_SUPPLEMENT_PERSONNE_VEUVE);
        isRenteAvecDebutDroit5AnsAvantDepotDemande = statement
                .dbReadBoolean(PRInfoCompl.FIELDNAME_IS_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE);
        isRenteAvecMontantMinimumMajoreInvalidite = statement
                .dbReadBoolean(PRInfoCompl.FIELDNAME_IS_RENTE_AVEC_MONTANT_MINIMUM_MAJORE_INVALIDITE);
        isRenteReduitePourSurassurance = statement
                .dbReadBoolean(PRInfoCompl.FIELDNAME_IS_RENTE_REDUITE_POUR_SURASSURANCE);

        motif = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_MOTIF);
        noAgence = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_NO_AGENCE);
        noCaisse = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_NO_CAISSE);
        remarque = statement.dbReadString(PRInfoCompl.FIELDNAME_REMARQUE);
        taxeFactureeAssure = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_TAXE_FACTUREE_A_ASSURE);
        typeDomaine = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_TYPE_DOMAINE);
        typeInfoCompl = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(PRInfoCompl.FIELDNAME_ID_INFO_COMPL, idInfoCompl);
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(PRInfoCompl.FIELDNAME_ANCIEN_ETAT_DROIT_OU_PRONONCE, this._dbWriteNumeric(
                statement.getTransaction(), ancienEtatDroitOuPrononce, "ancienEtatDroitOuPrononce"));
        statement.writeField(PRInfoCompl.FIELDNAME_CS_TIERS_RESPONSABLE,
                this._dbWriteNumeric(statement.getTransaction(), csTiersResponsable, "csTiersResponsable"));
        statement.writeField(PRInfoCompl.FIELDNAME_VETO_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), csVetoPrestation, "csVetoPrestation"));
        statement.writeField(PRInfoCompl.FIELDNAME_DATE_INFO_COMPL,
                this._dbWriteDateAMJ(statement.getTransaction(), dateInfoCompl, "dateInfoCompl"));
        statement.writeField(PRInfoCompl.FIELDNAME_ID_INFO_COMPL,
                this._dbWriteNumeric(statement.getTransaction(), idInfoCompl, "idInfoCompl"));
        statement.writeField(PRInfoCompl.FIELDNAME_ID_TIERS_CAISSE,
                this._dbWriteNumeric(statement.getTransaction(), idTiersCaisse, "idTiersCaisse"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_CAS_PENIBLE_AI, this._dbWriteBoolean(statement.getTransaction(),
                isCasPenibleAI, BConstants.DB_TYPE_BOOLEAN_CHAR, "isCasPenibleAI"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_INDEPENDANT, this._dbWriteBoolean(statement.getTransaction(),
                isIndependant, BConstants.DB_TYPE_BOOLEAN_CHAR, "isIndependant"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_PERMIS_SEJOURS, this._dbWriteBoolean(statement.getTransaction(),
                isPermisSejours, BConstants.DB_TYPE_BOOLEAN_CHAR, "isPermisSejours"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_REFUGIE, this._dbWriteBoolean(statement.getTransaction(),
                isRefugie, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRefugie"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_TRANSFERE, this._dbWriteBoolean(statement.getTransaction(),
                isTransfere, BConstants.DB_TYPE_BOOLEAN_CHAR, "isTransfere"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_RENTE_VEUF_LIMITEE, this._dbWriteBoolean(
                statement.getTransaction(), isRenteLimitee, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRenteVeufLimitee"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_RENTE_AVEC_SUPPLEMENT_PERSONNE_VEUVE, this._dbWriteBoolean(
                statement.getTransaction(), isRenteAvecSupplementPourPersonneVeuve, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRenteAvecSupplementPourPersonneVeuve"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE, this
                ._dbWriteBoolean(statement.getTransaction(), isRenteAvecDebutDroit5AnsAvantDepotDemande,
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "isRenteAvecDebutDroit5AnsAvantDepotDemande"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_RENTE_AVEC_MONTANT_MINIMUM_MAJORE_INVALIDITE, this
                ._dbWriteBoolean(statement.getTransaction(), isRenteAvecMontantMinimumMajoreInvalidite,
                        BConstants.DB_TYPE_BOOLEAN_CHAR, "isRenteAvecMontantMinimumMajoreInvalidite"));
        statement.writeField(PRInfoCompl.FIELDNAME_IS_RENTE_REDUITE_POUR_SURASSURANCE, this._dbWriteBoolean(
                statement.getTransaction(), isRenteReduitePourSurassurance, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isRenteReduitePourSurassurance"));
        statement.writeField(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL,
                this._dbWriteNumeric(statement.getTransaction(), typeInfoCompl, "typeInfoCompl"));
        statement.writeField(PRInfoCompl.FIELDNAME_MOTIF,
                this._dbWriteNumeric(statement.getTransaction(), motif, "motif"));
        statement.writeField(PRInfoCompl.FIELDNAME_NO_AGENCE,
                this._dbWriteNumeric(statement.getTransaction(), noAgence, "noAgence"));
        statement.writeField(PRInfoCompl.FIELDNAME_NO_CAISSE,
                this._dbWriteNumeric(statement.getTransaction(), noCaisse, "noCaisse"));
        statement.writeField(PRInfoCompl.FIELDNAME_TAXE_FACTUREE_A_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), taxeFactureeAssure, "taxeFactureeAssure"));
        statement.writeField(PRInfoCompl.FIELDNAME_TYPE_DOMAINE,
                this._dbWriteNumeric(statement.getTransaction(), typeDomaine, "typeDomaine"));
        statement.writeField(PRInfoCompl.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
    }

    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        PRInfoCompl clone = new PRInfoCompl();
        clone.setAncienEtatDroitOuPrononce(getAncienEtatDroitOuPrononce());
        clone.setCsTiersResponsable(getCsTiersResponsable());
        clone.setCsVetoPrestation(getCsVetoPrestation());
        clone.setDateInfoCompl(getDateInfoCompl());
        clone.setIdTiersCaisse(getIdTiersCaisse());
        clone.setIsCasPenibleAI(getIsCasPenibleAI());
        clone.setIsIndependant(getIsIndependant());
        clone.setIsPermisSejours(getIsPermisSejours());
        clone.setIsRefugie(getIsRefugie());
        clone.setIsTransfere(getIsTransfere());
        clone.setIsRenteLimitee(getIsRenteLimitee());
        clone.setIsRenteAvecSupplementPourPersonneVeuve(getIsRenteAvecSupplementPourPersonneVeuve());
        clone.setIsRenteAvecDebutDroit5AnsAvantDepotDemande(getIsRenteAvecDebutDroit5AnsAvantDepotDemande());
        clone.setIsRenteAvecMontantMinimumMajoreInvalidite(getIsRenteAvecMontantMinimumMajoreInvalidite());
        clone.setIsRenteReduitePourSurassurance(getIsRenteReduitePourSurassurance());
        clone.setMotif(getMotif());
        clone.setNoAgence(getNoAgence());
        clone.setNoCaisse(getNoCaisse());
        clone.setRemarque(getRemarque());
        clone.setTypeDomaine(getTypeDomaine());
        clone.setTypeInfoCompl(getTypeInfoCompl());

        clone.wantCallValidate(false);

        return clone;
    }

    public String getAncienEtatDroitOuPrononce() {
        return ancienEtatDroitOuPrononce;
    }

    public String getCsTiersResponsable() {
        return csTiersResponsable;
    }

    public String getCsVetoPrestation() {
        return csVetoPrestation;
    }

    public String getDateInfoCompl() {
        return dateInfoCompl;
    }

    public String getIdInfoCompl() {
        return idInfoCompl;
    }

    public String getIdTiersCaisse() {
        return idTiersCaisse;
    }

    public Boolean getIsCasPenibleAI() {
        return isCasPenibleAI;
    }

    public Boolean getIsIndependant() {
        return isIndependant;
    }

    public Boolean getIsPermisSejours() {
        return isPermisSejours;
    }

    public Boolean getIsRefugie() {
        return isRefugie;
    }

    public Boolean getIsTransfere() {
        return isTransfere;
    }

    public String getMotif() {
        return motif;
    }

    public String getNoAgence() {
        return noAgence;
    }

    public String getNoCaisse() {
        return noCaisse;
    }

    public String getRemarque() {
        return remarque;
    }

    public String getTaxeFactureeAssure() {
        return taxeFactureeAssure;
    }

    public String getTypeDomaine() {
        return typeDomaine;
    }

    public String getTypeInfoCompl() {
        return typeInfoCompl;
    }

    @Override
    public String getUniquePrimaryKey() {
        return getIdInfoCompl();
    }

    public void setAncienEtatDroitOuPrononce(String ancienEtatDroitOuPrononce) {
        this.ancienEtatDroitOuPrononce = ancienEtatDroitOuPrononce;
    }

    public void setCsTiersResponsable(String csTiersResponsable) {
        this.csTiersResponsable = csTiersResponsable;
    }

    public void setCsVetoPrestation(String csVetoPrestation) {
        this.csVetoPrestation = csVetoPrestation;
    }

    public void setDateInfoCompl(String dateInfoCompl) {
        this.dateInfoCompl = dateInfoCompl;
    }

    public void setIdInfoCompl(String idInfoCompl) {
        this.idInfoCompl = idInfoCompl;
    }

    public void setIdTiersCaisse(String idTiersCaisse) {
        this.idTiersCaisse = idTiersCaisse;
    }

    public void setIsCasPenibleAI(Boolean isCasPenibleAI) {
        this.isCasPenibleAI = isCasPenibleAI;
    }

    public void setIsIndependant(Boolean isIndependant) {
        this.isIndependant = isIndependant;
    }

    public void setIsPermisSejours(Boolean isPermisSejours) {
        this.isPermisSejours = isPermisSejours;
    }

    public void setIsRefugie(Boolean isRefugie) {
        this.isRefugie = isRefugie;
    }

    public void setIsTransfere(Boolean isTransfere) {
        this.isTransfere = isTransfere;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNoAgence(String noAgence) {
        this.noAgence = noAgence;
    }

    public void setNoCaisse(String noCaisse) {
        this.noCaisse = noCaisse;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setTaxeFactureeAssure(String taxeFactureeAssure) {
        this.taxeFactureeAssure = taxeFactureeAssure;
    }

    public void setTypeDomaine(String typeDomaine) {
        this.typeDomaine = typeDomaine;
    }

    public void setTypeInfoCompl(String typeInfoCompl) {
        this.typeInfoCompl = typeInfoCompl;
    }

    public Boolean getIsRenteLimitee() {
        return isRenteLimitee;
    }

    public void setIsRenteLimitee(Boolean isRenteVeufLimitee) {
        isRenteLimitee = isRenteVeufLimitee;
    }

    public final Boolean getIsRenteAvecSupplementPourPersonneVeuve() {
        return isRenteAvecSupplementPourPersonneVeuve;
    }

    public final void setIsRenteAvecSupplementPourPersonneVeuve(Boolean isRenteAvecSupplementPourPersonneVeuve) {
        this.isRenteAvecSupplementPourPersonneVeuve = isRenteAvecSupplementPourPersonneVeuve;
    }

    public final Boolean getIsRenteAvecDebutDroit5AnsAvantDepotDemande() {
        return isRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    public final void setIsRenteAvecDebutDroit5AnsAvantDepotDemande(Boolean isRenteAvecDebutDroit5AnsAvantDepotDemande) {
        this.isRenteAvecDebutDroit5AnsAvantDepotDemande = isRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    public final Boolean getIsRenteAvecMontantMinimumMajoreInvalidite() {
        return isRenteAvecMontantMinimumMajoreInvalidite;
    }

    public final void setIsRenteAvecMontantMinimumMajoreInvalidite(Boolean isRenteAvecMontantMinimumMajoreInvalidite) {
        this.isRenteAvecMontantMinimumMajoreInvalidite = isRenteAvecMontantMinimumMajoreInvalidite;
    }

    public final Boolean getIsRenteReduitePourSurassurance() {
        return isRenteReduitePourSurassurance;
    }

    public final void setIsRenteReduitePourSurassurance(Boolean isRenteReduitePourSurassurance) {
        this.isRenteReduitePourSurassurance = isRenteReduitePourSurassurance;
    }

    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdInfoCompl(pk);
    }
}
