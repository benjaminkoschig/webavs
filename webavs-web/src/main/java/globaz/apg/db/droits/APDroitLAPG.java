package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;

/**
 * Le BEntity représentant un droit APG non typé APG ou MATERNITE.
 * 
 * @author VRE
 */
public class APDroitLAPG extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CS_PROVENANCE_DROIT_ACQUIS = "VATPDO";
    public static final String FIELDNAME_DATEDEBUTDROIT = "VADDDR";
    public static final String FIELDNAME_DATEDEPOT = "VADDEP";
    public static final String FIELDNAME_DATEFINDROIT = "VADDFD";
    public static final String FIELDNAME_DATERECEPTION = "VADREC";
    public static final String FIELDNAME_DROIT_ACQUIS = "VAMDRA";
    public static final String FIELDNAME_ETAT = "VATETA";
    public static final String FIELDNAME_GENRESERVICE = "VATGSE";
    public static final String FIELDNAME_ID_INFO_COMPL = "VAIINF";
    public static final String FIELDNAME_IDCAISSE = "VAICAI";
    public static final String FIELDNAME_IDDEMANDE = "VAIDEM";
    public static final String FIELDNAME_IDDROIT_LAPG = "VAIDRO";
    public static final String FIELDNAME_IDDROIT_LAPG_PARENT = "VAIPAR";
    public static final String FIELDNAME_IDGESTIONNAIRE = "VAIGES";
    public static final String FIELDNAME_NODROIT = "VANDRO";
    public static final String FIELDNAME_NPA = "VANPOS";
    public static final String FIELDNAME_PAYS = "VATPAY";
    public static final String FIELDNAME_CANTON = "VACANT";
    public static final String FIELDNAME_REFERENCE = "VALREF";
    public static final String FIELDNAME_REMARQUE = "VALREM";
    public static final String FIELDNAME_SOUMIS_IMPOT_SOURCE = "VABSIM";
    public static final String FIELDNAME_TAUX_IMPOT_SOURCE = "VAMTAU";
    public static final String TABLE_NAME_LAPG = "APDROIP";

    protected static final String SQL_DOT = ".";
    protected static final String SQL_EQUALS = "=";
    protected static final String SQL_INNER_JOIN = " INNER JOIN ";
    protected static final String SQL_ON = "  ON ";

    private transient JadeUser gestionnaire = null; // transient pour ne pas surcharger la session http
    private transient PRDemande demande = null; // transient pour ne pas surcharger la session http
    private boolean deletePrestationRequis = false;
    private String csProvenanceDroitAcquis = "";
    private String dateDebutDroit = "";
    private String dateDepot = "";
    private String dateFinDroit = "";
    private String dateReception = "";
    private String droitAcquis = "";
    private String etat = "";
    private String genreService = "";
    private String idCaisse = "";
    private String idDemande = "";
    private String idDroit = "";
    private String idDroitParent = "";
    private String idGestionnaire = "";
    private String idInfoCompl = "";
    private Boolean isSoumisImpotSource = Boolean.FALSE;
    private String noDroit = ""; // TODO voir a quoi et ou est utilisé ce machin....
    private String npa = "";
    private String pays = "";
    private String reference = "";
    private String remarque = "";
    private String tauxImpotSource = "";
    private String csCantonDomicile = "";

    public APDroitLAPG() {
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        deletePrestationRequis = false;
        super._afterRetrieve(transaction);
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        // quand un droit est mis à jour, les prestations qui ont pu être
        // calculée pour ce droit n'ont plus aucun sens,
        // on les efface.

        if (deletePrestationRequis) {
            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(getSession());
            prestationManager.setForIdDroit(idDroit);
            prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < prestationManager.size(); i++) {
                APPrestation prestation = (APPrestation) prestationManager.getEntity(i);
                prestation.delete(transaction);
            }
            deletePrestationRequis = false;
        }
    }

    /**
     * <p>
     * Initialise la valeur de Id Droit.<br/>
     * On utilise uniquement le compteur de la table APDroitLAPG pour toutes les classes descendantes de celle-ci.
     * </p>
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDroit(this._incCounter(transaction, idDroit, APDroitLAPG.TABLE_NAME_LAPG));
        noDroit = idDroit;
        idCaisse = CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication())
                + CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication());
    }

    @Override
    protected String _getTableName() {
        return APDroitLAPG.TABLE_NAME_LAPG;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        csProvenanceDroitAcquis = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_CS_PROVENANCE_DROIT_ACQUIS);
        dateDebutDroit = statement.dbReadDateAMJ(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
        dateDepot = statement.dbReadDateAMJ(APDroitLAPG.FIELDNAME_DATEDEPOT);
        dateFinDroit = statement.dbReadDateAMJ(APDroitLAPG.FIELDNAME_DATEFINDROIT);
        dateReception = statement.dbReadDateAMJ(APDroitLAPG.FIELDNAME_DATERECEPTION);
        droitAcquis = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_DROIT_ACQUIS, 2);
        etat = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_ETAT);
        genreService = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_GENRESERVICE);
        idCaisse = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDCAISSE);
        idDemande = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDEMANDE);
        idDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        idDroitParent = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        idGestionnaire = statement.dbReadString(APDroitLAPG.FIELDNAME_IDGESTIONNAIRE);
        idInfoCompl = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_ID_INFO_COMPL);
        isSoumisImpotSource = statement.dbReadBoolean(APDroitLAPG.FIELDNAME_SOUMIS_IMPOT_SOURCE);
        noDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_NODROIT);
        npa = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_NPA);
        pays = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_PAYS);
        reference = statement.dbReadString(APDroitLAPG.FIELDNAME_REFERENCE);
        remarque = statement.dbReadString(APDroitLAPG.FIELDNAME_REMARQUE);
        tauxImpotSource = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_TAUX_IMPOT_SOURCE);
        csCantonDomicile = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_CANTON);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APDroitLAPG.FIELDNAME_IDDROIT_LAPG,
                this._dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(APDroitLAPG.FIELDNAME_CS_PROVENANCE_DROIT_ACQUIS,
                this._dbWriteNumeric(statement.getTransaction(), csProvenanceDroitAcquis, "csProvenanceDroitAcquis"));
        statement.writeField(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutDroit, "7"));
        statement.writeField(APDroitLAPG.FIELDNAME_DATEDEPOT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDepot, "dateDepot"));
        statement.writeField(APDroitLAPG.FIELDNAME_DATEFINDROIT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinDroit, "dateFinDroit"));
        statement.writeField(APDroitLAPG.FIELDNAME_DATERECEPTION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateReception, "dateReception"));
        statement.writeField(APDroitLAPG.FIELDNAME_DROIT_ACQUIS,
                this._dbWriteNumeric(statement.getTransaction(), droitAcquis, "droitAcquis"));
        statement
                .writeField(APDroitLAPG.FIELDNAME_ETAT, this._dbWriteNumeric(statement.getTransaction(), etat, "etat"));
        statement.writeField(APDroitLAPG.FIELDNAME_GENRESERVICE,
                this._dbWriteNumeric(statement.getTransaction(), genreService, "genreService"));
        statement.writeField(APDroitLAPG.FIELDNAME_IDCAISSE,
                this._dbWriteNumeric(statement.getTransaction(), idCaisse, "idCaisse"));
        statement.writeField(APDroitLAPG.FIELDNAME_IDDEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
        statement.writeField(APDroitLAPG.FIELDNAME_IDDROIT_LAPG,
                this._dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));
        statement.writeField(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT,
                this._dbWriteNumeric(statement.getTransaction(), idDroitParent, "idDroitParent"));
        statement.writeField(APDroitLAPG.FIELDNAME_IDGESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(APDroitLAPG.FIELDNAME_ID_INFO_COMPL,
                this._dbWriteNumeric(statement.getTransaction(), idInfoCompl, "idInfoCompl"));
        statement.writeField(APDroitLAPG.FIELDNAME_NODROIT,
                this._dbWriteNumeric(statement.getTransaction(), noDroit, "noDroit"));
        statement.writeField(APDroitLAPG.FIELDNAME_NPA, this._dbWriteNumeric(statement.getTransaction(), npa, "npa"));
        statement
                .writeField(APDroitLAPG.FIELDNAME_PAYS, this._dbWriteNumeric(statement.getTransaction(), pays, "pays"));
        statement.writeField(APDroitLAPG.FIELDNAME_REFERENCE,
                this._dbWriteString(statement.getTransaction(), reference, "reference"));
        statement.writeField(APDroitLAPG.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(APDroitLAPG.FIELDNAME_SOUMIS_IMPOT_SOURCE,
                this._dbWriteBoolean(statement.getTransaction(), isSoumisImpotSource, BConstants.DB_TYPE_BOOLEAN_CHAR));
        statement.writeField(APDroitLAPG.FIELDNAME_TAUX_IMPOT_SOURCE,
                this._dbWriteNumeric(statement.getTransaction(), tauxImpotSource));
        statement.writeField(APDroitLAPG.FIELDNAME_CANTON,
                this._dbWriteNumeric(statement.getTransaction(), csCantonDomicile, "csCantonDomicile"));
    }

    public String getCsProvenanceDroitAcquis() {
        return csProvenanceDroitAcquis;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateDepot() {
        return dateDepot;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getDroitAcquis() {
        return droitAcquis;
    }

    public String getEtat() {
        return etat;
    }

    public String getGenreService() {
        return genreService;
    }

    public String getIdCaisse() {
        return idCaisse;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdDroitParent() {
        return idDroitParent;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdInfoCompl() {
        return idInfoCompl;
    }

    public Boolean getIsSoumisImpotSource() {
        return isSoumisImpotSource;
    }

    public String getNoDroit() {
        return noDroit;
    }

    public String getNpa() {
        return npa;
    }

    public String getPays() {
        return pays;
    }

    public String getReference() {
        return reference;
    }

    public String getRemarque() {
        return remarque;
    }

    public String getTauxImpotSource() {
        return tauxImpotSource;
    }

    /**
     * Désactive le pspy dans la table des droits LAPG.
     * <p>
     * Il FAUT réactiver le pspy dans les classes filles !!!
     * </p>
     * 
     * @return false
     * @see globaz.globall.db.BEntity#hasSpy()
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    public boolean isModifiable() {
        if (JadeStringUtil.isEmpty(etat)) {
            throw new IllegalArgumentException(
                    "Unable to know is the APDroitLAPG can be edited because his 'etat' is empty....");
        }
        return IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(etat);
    }

    /**
     * Charge la demande avec laquelle ce droit est lié. Cette méthode recharge automatiquement la demande si (et
     * seulement si) la valeur de Id Demande de ce bean a été modifiée.
     * 
     * @return la demande liée à ce droit (jamais nul).
     * @throws Exception
     *             si la demande ne peut être chargée.
     */
    public PRDemande loadDemande() throws Exception {
        // si la demande est nulle, instancier
        if (demande == null) {
            demande = new PRDemande();
        }

        // on s'assure que la session est la bonne (pour les cas où on
        // chargerait le tiers...)
        demande.setSession(getSession());

        // si la demande est différente, charger la demande
        if (!idDemande.equals(demande.getIdDemande())) {
            demande.setIdDemande(idDemande);
            demande.retrieve();
        }

        return demande;
    }

    /**
     * Charge le gestionnaire associé avec cette demande.
     * <p>
     * Le gestionnaire est automatiquement rechargé si le champ idGestionnaire de ce bean est modifié.
     * </p>
     * 
     * @return Une instance de JadeUser ou null si l'idGestionnaire est vide.
     * @throws Exception
     *             s'il n'existe pas de gestionnaire avec cet identifiant.
     */
    public JadeUser loadGestionnaire() throws Exception {
        if (!JadeStringUtil.isEmpty(idGestionnaire)
                && ((gestionnaire == null) || !idGestionnaire.equals(gestionnaire.getVisa()))) {
            gestionnaire = PRGestionnaireHelper.getGestionnaire(idGestionnaire);
        }
        return gestionnaire;
    }

    public void setCsProvenanceDroitAcquis(String csProvenanceDroitAcquis) {
        this.csProvenanceDroitAcquis = csProvenanceDroitAcquis;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setDeletePrestationRequis(boolean b) {
        deletePrestationRequis = b;
    }

    public void setDemande(PRDemande demande) {
        this.demande = demande;
        idDemande = demande.getIdDemande();
    }

    public void setDroitAcquis(String droitAcquis) {
        this.droitAcquis = droitAcquis;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public void setIdCaisse(String idCaisse) {
        this.idCaisse = idCaisse;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdDroitParent(String idDroitParent) {
        this.idDroitParent = idDroitParent;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdInfoCompl(String idInfoCompl) {
        this.idInfoCompl = idInfoCompl;
    }

    public void setIsSoumisImpotSource(Boolean isSoumisImpotSource) {
        this.isSoumisImpotSource = isSoumisImpotSource;
    }

    public void setNoDroit(String noDroit) {
        this.noDroit = noDroit;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setTauxImpotSource(String tauxImpotSource) {
        this.tauxImpotSource = tauxImpotSource;
    }

    /**
     * Redéfinir cette méthode pour effectuer des tests spécifiques juste avant le calcul des prestations.
     * 
     * @return vrai si le droit et valide et que le calcul des prestations peut commencer.
     */
    public boolean validateBeforeCalcul(BTransaction transaction) throws Exception {
        return true;
    }

    /**
     * @return the csCantonDomicile
     */
    public String getCsCantonDomicile() {
        return csCantonDomicile;
    }

    /**
     * @param csCantonDomicile the csCantonDomicile to set
     */
    public void setCsCantonDomicile(String csCantonDomicile) {
        this.csCantonDomicile = csCantonDomicile;
    }
}
