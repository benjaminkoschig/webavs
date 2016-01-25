package globaz.ij.db.prononces;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prestations.IJGrandeIJCalculeeManager;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJPetiteIJCalculeeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPrononce extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 */
    public static final String FIELDNAME_ANNEE_RENTE_EN_COURS_ETAT = "XBNARC";

    /**
	 */
    public static final String FIELDNAME_AVEC_DECISION = "XBBADE";

    /**
	 */
    public static final String FIELDNAME_CODES_CAS_SPECIAL = "XBLCCS";

    public static final String FIELDNAME_CS_CANTON_IMPOSITION_SOURCE = "XBTCIS";

    /**
     */
    public static final String FIELDNAME_CS_ETAT = "XBTETA";

    /**
     */
    public static final String FIELDNAME_CS_GENRE = "XBTGEN";

    /**
     */
    public static final String FIELDNAME_CS_STATUT_PROFESSIONNEL = "XBTSTA";

    /**
	 */
    public static final String FIELDNAME_CS_TYPE_HEBERGEMENT = "XBTHEB";

    /**
     */
    public static final String FIELDNAME_CS_TYPE_IJ = "XBTTIJ";

    /**
     */
    public static final String FIELDNAME_DATE_DEBUT_PRONONCE = "XBDDDR";

    /**
	 */
    public static final String FIELDNAME_DATE_ECHEANCE = "XBDECH";

    /**
     */
    public static final String FIELDNAME_DATE_FIN_PRONONCE = "XBDFDR";

    /**
     */
    public static final String FIELDNAME_DATE_PRONONCE = "XBDPRO";

    /**
     */
    public static final String FIELDNAME_DEMI_IJ_AC = "XBMDAC";

    /**
	 */
    public static final String FIELDNAME_ECHELLE = "XBNECH";

    /**
     */
    public static final String FIELDNAME_ID_CORRECTION = "XBICOR";

    public static final String FIELDNAME_ID_DECISION = "XBIDEC";

    /**
     */
    public static final String FIELDNAME_ID_DEMANDE = "XBIDEM";

    /**
     */
    public static final String FIELDNAME_ID_GESTIONNAIRE = "XBIGES";

    /**
	 */
    public static final String FIELDNAME_ID_INFO_COMPL = "XBIINF";

    /**
     */
    public static final String FIELDNAME_ID_PARENT = "XBIPAR";

    /**
     */
    public static final String FIELDNAME_ID_PRONONCE = "XBIPAI";
    public static final String FIELDNAME_ID_REVENU_READAPTATION = "XBIRDR";
    public static final String FIELDNAME_ID_SITUATION_FAMILIALE = "XBISFA";

    /**
     */
    public static final String FIELDNAME_MONTANT_GARANTI_AA = "XBMGAA";

    /**
     */
    public static final String FIELDNAME_MONTANT_GARANTI_AA_REDUIT = "XBBMGR";

    /**
     */
    public static final String FIELDNAME_MONTANT_RENTE_EN_COURS = "XBMREC";

    /**
	 */
    public static final String FIELDNAME_MOTIF_ECHEANCE = "XBTMOE";

    /**
	 */
    public static final String FIELDNAME_NO_DECISION_AI = "XBNDAI";

    /**
     */
    public static final String FIELDNAME_OFFICE_AI = "XBNOAI";

    public static final String FIELDNAME_PARENT_CORRIGE_DEPUIS = "XBIANC";

    public static final String FIELDNAME_PRONONCE_SELECTIONNE = "XBSELE";

    /**
	 */
    public static final String FIELDNAME_RAM = "XBMRAM";
    /**
     */
    public static final String FIELDNAME_SOUMIS_IMPOT_SOURCE = "XBBSIM";
    public static final String FIELDNAME_TAUX_IMPOSITION_SOURCE = "XBMTIS";
    /**
     */
    public static final String TABLE_NAME_PRONONCE = "IJPRONAI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Charge une instance correcte de l'une des classes descendantes de IJPrononce suivant l'id et le type transmis.
     * <p>
     * Note: si le type est null, cette methode tente de le retrouver pour l'id correspondant.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idPrononce
     *            jamais null
     * @param csTypeIJ
     *            peut etre vide ou null
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final IJPrononce loadPrononce(BSession session, BITransaction transaction, String idPrononce,
            String csTypeIJ) throws Exception {
        IJPrononce retValue;

        // recupere le type s'il n'est pas transmit
        if (JadeStringUtil.isNull(csTypeIJ) || JadeStringUtil.isIntegerEmpty(csTypeIJ)) {
            retValue = new IJPrononce();
            retValue.setIdPrononce(idPrononce);
            retValue.setSession(session);

            if (transaction == null) {
                retValue.retrieve();
            } else {
                retValue.retrieve(transaction);
            }

            csTypeIJ = retValue.getCsTypeIJ();
        }

        // charge l'ij
        if (IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)) {
            retValue = new IJGrandeIJ();
        } else if (IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)) {
            retValue = new IJPetiteIJ();
        } else if (IIJPrononce.CS_ALLOC_ASSIST.equals(csTypeIJ)) {
            retValue = new IJPrononceAllocAssistance();
        } else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)) {
            retValue = new IJPrononceAit();
        } else {
            // oops, ben zut alors, on n'a pas le type d'IJ
            throw new Exception("Type IJ vide ou inconnu");
        }

        retValue.setSession(session);
        retValue.setIdPrononce(idPrononce);
        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    /**
     * Charge une instance correcte de l'une des classes descendantes de IJPrononce suivant l'id et le type transmis.
     * <p>
     * Note: si le type est null, cette methode tente de le retrouver pour l'id correspondant.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idPrononce
     *            jamais null
     * @param idPrononceANePasPrendre
     *            Comme il est possible qu'il existe plusieurs prononcés, cet id sert à mentionner celui qui ne doit pas
     *            être prit
     * @param csTypeIJ
     *            peut etre vide ou null
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final IJPrononceManager loadPrononceManagerCorrigerDepuis(BSession session,
            BITransaction transaction, String idParentCorrigerDepuis, String csTypeIJ) throws Exception {

        IJPrononceManager prononces;

        if (IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)) {
            prononces = new IJGrandeIJManager();
        } else if (IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)) {
            prononces = new IJPetiteIJManager();
        } else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)) {
            prononces = new IJPrononceAitManager();
        } else {
            prononces = new IJPrononceAllocAssistanceManager();
        }

        prononces.setSession(session);
        prononces.find(transaction);

        if (prononces.isEmpty()) {
            return null;
        }

        return prononces;
    }

    private String anneeRenteEnCours = "";
    // @deprecated
    private Boolean avecDecision = Boolean.FALSE;
    private String codesCasSpecial = "";
    private String csCantonImpositionSource = "";
    private String csEtat = IIJPrononce.CS_ATTENTE;
    private String csGenre = "";
    private String csMotifEcheance = "";
    private String csStatutProfessionnel = "";
    private String csTypeHebergement = "";

    private String csTypeIJ = "";

    private String dateDebutPrononce = "";
    private String dateEcheance = "";
    private String dateFinPrononce = "";
    private String datePrononce = "";
    private transient PRDemande demande;
    private String demiIJAC = "";
    private String echelle = "";
    private transient IJPrononce enfantActif;
    private transient IJPrononce enfantCorrection;
    private String idCorrection = "";
    private String idDecision = "";
    private String idDemande = "";
    private String idGestionnaire = "";
    private String idInfoCompl = "";
    private String idParent = "";
    private String idParentCorrigeDepuis = "";
    /**
     */
    protected String idPrononce = "";
    private String idRevenuReadaptation = "";
    private String idSituationFamiliale = "";
    private String isPrononceSelectionne;
    private String montantGarantiAA = "";
    private Boolean montantGarantiAAReduit = Boolean.FALSE;
    private String montantRenteEnCours = "";

    private String noDecisionAI = "";
    private String officeAI = "";

    private String ram = "";
    private transient IJRevenu revenuReadaptation;
    private transient IJSituationFamiliale situationFamiliale;

    private Boolean soumisImpotSource = Boolean.FALSE;
    private String tauxImpositionSource = "";

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        // effacement de la situation familiale
        if (!JadeStringUtil.isIntegerEmpty(idSituationFamiliale)) {
            loadSituationFamiliale(transaction).delete(transaction);
        }

        // effacement du revenu
        if (!JadeStringUtil.isIntegerEmpty(idRevenuReadaptation) && !loadRevenuReadaptation(transaction).isNew()) {
            loadRevenuReadaptation(transaction).delete(transaction);
        }

        // effacement des mesures
        IJMesureManager mesureManager = new IJMesureManager();

        mesureManager.setSession(getSession());
        mesureManager.setForIdPrononce(idPrononce);
        mesureManager.find(transaction);

        for (int i = 0; i < mesureManager.size(); i++) {
            ((IJMesure) mesureManager.getEntity(i)).delete(transaction);
        }

        mesureManager = null;

        // effacement des bases d'indemnisation
        IJBaseIndemnisationManager baseIndemnisationManager = new IJBaseIndemnisationManager();

        baseIndemnisationManager.setSession(getSession());
        baseIndemnisationManager.setForIdPrononce(idPrononce);
        baseIndemnisationManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < baseIndemnisationManager.size(); i++) {
            IJBaseIndemnisation baseIndemnisation = (IJBaseIndemnisation) baseIndemnisationManager.getEntity(i);

            baseIndemnisation.delete(transaction);
        }

        baseIndemnisationManager = null;

        // effacement des calculs
        effacerIJCalculees(transaction);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idPrononce = this._incCounter(transaction, idPrononce, IJPrononce.TABLE_NAME_PRONONCE);
        isPrononceSelectionne = "1";
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return IJPrononce.TABLE_NAME_PRONONCE;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPrononce = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_PRONONCE);
        idDemande = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_DEMANDE);
        idDecision = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_DECISION);
        datePrononce = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_PRONONCE);
        dateDebutPrononce = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
        dateFinPrononce = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
        csGenre = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_GENRE);
        officeAI = statement.dbReadNumeric(IJPrononce.FIELDNAME_OFFICE_AI);
        csTypeIJ = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_TYPE_IJ);
        montantGarantiAA = statement.dbReadNumeric(IJPrononce.FIELDNAME_MONTANT_GARANTI_AA, 2);
        montantGarantiAAReduit = statement.dbReadBoolean(IJPrononce.FIELDNAME_MONTANT_GARANTI_AA_REDUIT);
        demiIJAC = statement.dbReadNumeric(IJPrononce.FIELDNAME_DEMI_IJ_AC, 2);
        idRevenuReadaptation = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_REVENU_READAPTATION);
        csStatutProfessionnel = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_STATUT_PROFESSIONNEL);
        idParent = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_PARENT);
        idSituationFamiliale = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_SITUATION_FAMILIALE);
        csEtat = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_ETAT);
        idGestionnaire = statement.dbReadString(IJPrononce.FIELDNAME_ID_GESTIONNAIRE);
        soumisImpotSource = statement.dbReadBoolean(IJPrononce.FIELDNAME_SOUMIS_IMPOT_SOURCE);
        idCorrection = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_CORRECTION);
        montantRenteEnCours = statement.dbReadNumeric(IJPrononce.FIELDNAME_MONTANT_RENTE_EN_COURS);
        idInfoCompl = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_INFO_COMPL);
        csTypeHebergement = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_TYPE_HEBERGEMENT);
        echelle = statement.dbReadNumeric(IJPrononce.FIELDNAME_ECHELLE);
        ram = statement.dbReadNumeric(IJPrononce.FIELDNAME_RAM);
        codesCasSpecial = statement.dbReadString(IJPrononce.FIELDNAME_CODES_CAS_SPECIAL);
        csMotifEcheance = statement.dbReadNumeric(IJPrononce.FIELDNAME_MOTIF_ECHEANCE);
        dateEcheance = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_ECHEANCE);
        noDecisionAI = statement.dbReadNumeric(IJPrononce.FIELDNAME_NO_DECISION_AI);
        anneeRenteEnCours = statement.dbReadNumeric(IJPrononce.FIELDNAME_ANNEE_RENTE_EN_COURS_ETAT);
        avecDecision = statement.dbReadBoolean(IJPrononce.FIELDNAME_AVEC_DECISION);
        tauxImpositionSource = statement.dbReadNumeric(IJPrononce.FIELDNAME_TAUX_IMPOSITION_SOURCE);
        csCantonImpositionSource = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_CANTON_IMPOSITION_SOURCE);
        isPrononceSelectionne = statement.dbReadString(IJPrononce.FIELDNAME_PRONONCE_SELECTIONNE);
        idParentCorrigeDepuis = statement.dbReadNumeric(IJPrononce.FIELDNAME_PARENT_CORRIGE_DEPUIS);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IJPrononce.FIELDNAME_ID_PRONONCE,
                this._dbWriteNumeric(statement.getTransaction(), idPrononce));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(IJPrononce.FIELDNAME_ID_PRONONCE,
                this._dbWriteNumeric(statement.getTransaction(), idPrononce, "idPrononce"));
        statement.writeField(IJPrononce.FIELDNAME_ID_DEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
        statement.writeField(IJPrononce.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(IJPrononce.FIELDNAME_DATE_PRONONCE,
                this._dbWriteDateAMJ(statement.getTransaction(), datePrononce, "datePrononce"));
        statement.writeField(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutPrononce, "dateDebutPrononce"));
        statement.writeField(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinPrononce, "dateFinPrononce"));
        statement.writeField(IJPrononce.FIELDNAME_CS_GENRE,
                this._dbWriteNumeric(statement.getTransaction(), csGenre, "csGenre"));
        statement.writeField(IJPrononce.FIELDNAME_OFFICE_AI,
                this._dbWriteNumeric(statement.getTransaction(), officeAI, "officeAI"));
        statement.writeField(IJPrononce.FIELDNAME_CS_TYPE_IJ,
                this._dbWriteNumeric(statement.getTransaction(), csTypeIJ, "csTypeIJ"));
        statement.writeField(IJPrononce.FIELDNAME_MONTANT_GARANTI_AA,
                this._dbWriteNumeric(statement.getTransaction(), montantGarantiAA, "montantGarantiAA"));
        statement.writeField(IJPrononce.FIELDNAME_MONTANT_GARANTI_AA_REDUIT, this._dbWriteBoolean(
                statement.getTransaction(), montantGarantiAAReduit, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "montantGarantiAAReduit"));
        statement.writeField(IJPrononce.FIELDNAME_DEMI_IJ_AC,
                this._dbWriteNumeric(statement.getTransaction(), demiIJAC, "demiIJAC"));
        statement.writeField(IJPrononce.FIELDNAME_ID_REVENU_READAPTATION,
                this._dbWriteNumeric(statement.getTransaction(), idRevenuReadaptation, "idRevenuReadaptation"));
        statement.writeField(IJPrononce.FIELDNAME_CS_STATUT_PROFESSIONNEL,
                this._dbWriteNumeric(statement.getTransaction(), csStatutProfessionnel, "csStatutProfessionnel"));
        statement.writeField(IJPrononce.FIELDNAME_ID_PARENT,
                this._dbWriteNumeric(statement.getTransaction(), idParent, "idParent"));
        statement.writeField(IJPrononce.FIELDNAME_ID_SITUATION_FAMILIALE,
                this._dbWriteNumeric(statement.getTransaction(), idSituationFamiliale, "idSituationFamiliale"));
        statement.writeField(IJPrononce.FIELDNAME_CS_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(IJPrononce.FIELDNAME_ID_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(IJPrononce.FIELDNAME_SOUMIS_IMPOT_SOURCE, this._dbWriteBoolean(statement.getTransaction(),
                soumisImpotSource, BConstants.DB_TYPE_BOOLEAN_CHAR, "soumisImpotSource"));
        statement.writeField(IJPrononce.FIELDNAME_ID_CORRECTION,
                this._dbWriteNumeric(statement.getTransaction(), idCorrection, "idCorrection"));
        statement.writeField(IJPrononce.FIELDNAME_MONTANT_RENTE_EN_COURS,
                this._dbWriteNumeric(statement.getTransaction(), montantRenteEnCours, "montantRenteEnCours"));
        statement.writeField(IJPrononce.FIELDNAME_ID_INFO_COMPL,
                this._dbWriteNumeric(statement.getTransaction(), idInfoCompl, "idInfoCompl"));
        statement.writeField(IJPrononce.FIELDNAME_CS_TYPE_HEBERGEMENT,
                this._dbWriteNumeric(statement.getTransaction(), csTypeHebergement, "csTypeHebergement"));
        statement.writeField(IJPrononce.FIELDNAME_ECHELLE,
                this._dbWriteNumeric(statement.getTransaction(), echelle, "echelle"));
        statement.writeField(IJPrononce.FIELDNAME_RAM, this._dbWriteNumeric(statement.getTransaction(), ram, "ram"));
        statement.writeField(IJPrononce.FIELDNAME_CODES_CAS_SPECIAL,
                this._dbWriteString(statement.getTransaction(), codesCasSpecial, "codesCasSpecial"));
        statement.writeField(IJPrononce.FIELDNAME_MOTIF_ECHEANCE,
                this._dbWriteNumeric(statement.getTransaction(), csMotifEcheance, "csMotifEcheance"));
        statement.writeField(IJPrononce.FIELDNAME_DATE_ECHEANCE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEcheance, "dateEcheance"));
        statement.writeField(IJPrononce.FIELDNAME_NO_DECISION_AI,
                this._dbWriteNumeric(statement.getTransaction(), noDecisionAI, "noDecisionAI"));
        statement.writeField(IJPrononce.FIELDNAME_ANNEE_RENTE_EN_COURS_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), anneeRenteEnCours, "anneeRenteEnCours"));
        statement.writeField(IJPrononce.FIELDNAME_AVEC_DECISION, this._dbWriteBoolean(statement.getTransaction(),
                avecDecision, BConstants.DB_TYPE_BOOLEAN_CHAR, "avecDecision"));
        statement.writeField(IJPrononce.FIELDNAME_TAUX_IMPOSITION_SOURCE,
                this._dbWriteNumeric(statement.getTransaction(), tauxImpositionSource, "tauxImpositionSource"));
        statement.writeField(IJPrononce.FIELDNAME_CS_CANTON_IMPOSITION_SOURCE,
                this._dbWriteNumeric(statement.getTransaction(), csCantonImpositionSource, "csCantonImpositionSource"));
        statement.writeField(IJPrononce.FIELDNAME_PRONONCE_SELECTIONNE,
                this._dbWriteString(statement.getTransaction(), isPrononceSelectionne, "isPrononceSelectionne"));
        statement.writeField(IJPrononce.FIELDNAME_PARENT_CORRIGE_DEPUIS,
                this._dbWriteNumeric(statement.getTransaction(), idParentCorrigeDepuis, "idParentCorrigeDepuis"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJPrononce clone = new IJPrononce();

        duplicatePrononce(clone, action);

        return clone;
    }

    /**
     * copie les données de bases du prononce pour un clonage.
     * 
     * @param clone
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected void duplicatePrononce(IJPrononce clone, int action) throws Exception {
        // etat par defaut: clone.setCsEtat(getCsEtat());
        clone.setCsGenre(getCsGenre());
        clone.setOfficeAI(getOfficeAI());
        clone.setCsStatutProfessionnel(getCsStatutProfessionnel());
        clone.setCsTypeIJ(getCsTypeIJ());
        clone.setDateDebutPrononce(getDateDebutPrononce());
        clone.setDateFinPrononce(getDateFinPrononce());
        clone.setDatePrononce(getDatePrononce());
        clone.setDemiIJAC(getDemiIJAC());
        clone.setIdDemande(getIdDemande());

        if (null != getSession().getUserId()) {
            clone.setIdGestionnaire(getSession().getUserId());
        } else {
            clone.setIdGestionnaire("");
        }

        clone.setIdRevenuReadaptation(getIdRevenuReadaptation());
        clone.setIdSituationFamiliale(getIdSituationFamiliale());
        clone.setMontantGarantiAA(getMontantGarantiAA());
        clone.setMontantGarantiAAReduit(getMontantGarantiAAReduit());
        clone.setSoumisImpotSource(getSoumisImpotSource());
        clone.setMontantRenteEnCours(getMontantRenteEnCours());
        clone.setCsTypeHebergement(getCsTypeHebergement());
        clone.setEchelle(getEchelle());
        clone.setRam(getRam());
        clone.setCodesCasSpecial(getCodesCasSpecial());
        clone.setCsMotifEcheance(getCsMotifEcheance());
        clone.setDateEcheance(getDateEcheance());
        clone.setNoDecisionAI(getNoDecisionAI());
        clone.setAnneeRenteEnCours(getAnneeRenteEnCours());
        clone.setTauxImpositionSource(getTauxImpositionSource());
        clone.setCsCantonImpositionSource(getCsCantonImpositionSource());
        clone.setAvecDecision(getAvecDecision());
        clone.setIdParentCorrigeDepuis(getIdParentCorrigeDepuis());

        if (action == IIJPrononce.CLONE_FILS) {
            if (JadeStringUtil.isIntegerEmpty(getIdParent())) {
                clone.setIdParent(getIdPrononce());
            } else {
                clone.setIdParent(getIdParent());
            }
            clone.setIdCorrection(getIdPrononce());
        } else {
            clone.setIdParent("0");
            clone.setIdCorrection("0");
        }

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void effacerIJCalculees(BTransaction transaction) throws Exception {
        IJIJCalculeeManager mgr;

        if (IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)) {
            mgr = new IJGrandeIJCalculeeManager();
        } else if (IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)) {
            mgr = new IJPetiteIJCalculeeManager();
        } else {
            mgr = new IJIJCalculeeManager();
        }

        mgr.setSession(getSession());
        mgr.setForIdPrononce(idPrononce);
        mgr.find();

        for (int idIJCalcule = mgr.size(); --idIJCalcule >= 0;) {
            ((IJIJCalculee) mgr.get(idIJCalcule)).delete(transaction);
        }
    }

    public String getAnneeRenteEnCours() {
        return anneeRenteEnCours;
    }

    public Boolean getAvecDecision() {
        return avecDecision;
    }

    /**
     * @return
     */
    public String getCodesCasSpecial() {
        return codesCasSpecial;
    }

    public String getCsCantonImpositionSource() {
        return csCantonImpositionSource;
    }

    /**
     * getter pour l'attribut cs etat.
     * 
     * @return la valeur courante de l'attribut cs etat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * getter pour l'attribut cs genre.
     * 
     * @return la valeur courante de l'attribut cs genre
     */
    public String getCsGenre() {
        return csGenre;
    }

    public String getCsMotifEcheance() {
        return csMotifEcheance;
    }

    /**
     * getter pour l'attribut cs statut professionnel.
     * 
     * @return la valeur courante de l'attribut cs statut professionnel
     */
    public String getCsStatutProfessionnel() {
        return csStatutProfessionnel;
    }

    /**
     * @return
     */
    public String getCsTypeHebergement() {
        return csTypeHebergement;
    }

    /**
     * getter pour l'attribut cs type IJ.
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date debut prononce.
     * 
     * @return la valeur courante de l'attribut date debut prononce
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * getter pour l'attribut date fin prononce.
     * 
     * @return la valeur courante de l'attribut date fin prononce
     */
    public String getDateFinPrononce() {
        return dateFinPrononce;
    }

    /**
     * getter pour l'attribut date prononce.
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getDatePrononce() {
        return datePrononce;
    }

    public String getDefaultCsCantonImposition() {
        try {
            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), loadDemande(null).getIdTiers());
            if ((tiers != null) && !JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))) {
                return tiers.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);
            } else {
                return CaisseHelperFactory.getInstance().getCsDefaultCantonCaisse(getSession().getApplication());
            }
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * getter pour l'attribut demi IJAC.
     * 
     * @return la valeur courante de l'attribut demi IJAC
     */
    public String getDemiIJAC() {
        return demiIJAC;
    }

    /**
     * @return
     */
    public String getEchelle() {
        return echelle;
    }

    /**
     * getter pour l'attribut id correction.
     * 
     * @return la valeur courante de l'attribut id correction
     */
    public String getIdCorrection() {
        return idCorrection;
    }

    public String getIdDecision() {
        return idDecision;
    }

    /**
     * getter pour l'attribut id demande.
     * 
     * @return la valeur courante de l'attribut id demande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * getter pour l'attribut id gestionnaire.
     * 
     * @return la valeur courante de l'attribut id gestionnaire
     */
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    /**
     * @return
     */
    public String getIdInfoCompl() {
        return idInfoCompl;
    }

    /**
     * getter pour l'attribut id parent.
     * 
     * @return la valeur courante de l'attribut id parent
     */
    public String getIdParent() {
        return idParent;
    }

    public String getIdParentCorrigeDepuis() {
        return idParentCorrigeDepuis;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    /**
     * getter pour l'attribut id revenu readaptation.
     * 
     * @return la valeur courante de l'attribut id revenu readaptation
     */
    public String getIdRevenuReadaptation() {
        return idRevenuReadaptation;
    }

    /**
     * getter pour l'attribut id situation familiale.
     * 
     * @return la valeur courante de l'attribut id situation familiale
     */
    public String getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    /**
     * Retourne vrai/faux. Cet attribut est utilisé dans l'écran de calcul des 30/60/90 jours pour savoir si le prononcé
     * est visible ou pas
     * 
     * @return "1" si le prononcé est sélectionné sinon "0"
     */
    public final String getIsPrononceSelectionne() {
        return isPrononceSelectionne;
    }

    /**
     * getter pour l'attribut montant garanti AA.
     * 
     * @return la valeur courante de l'attribut montant garanti AA
     */
    public String getMontantGarantiAA() {
        return montantGarantiAA;
    }

    /**
     * getter pour l'attribut montant garanti AAReduit.
     * 
     * @return la valeur courante de l'attribut montant garanti AAReduit
     */
    public Boolean getMontantGarantiAAReduit() {
        return montantGarantiAAReduit;
    }

    /**
     * @return
     */
    public String getMontantRenteEnCours() {
        return montantRenteEnCours;
    }

    public String getNoDecisionAI() {
        return noDecisionAI;
    }

    /**
     * getter pour l'attribut cs office AI.
     * 
     * @return la valeur courante de l'attribut cs office AI
     */
    public String getOfficeAI() {
        return officeAI;
    }

    /**
     * @return
     */
    public String getRam() {
        return ram;
    }

    /**
     * getter pour l'attribut soumis impot source.
     * 
     * @return la valeur courante de l'attribut soumis impot source
     */
    public Boolean getSoumisImpotSource() {
        return soumisImpotSource;
    }

    public String getTauxImpositionSource() {
        return tauxImpositionSource;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdPrononce();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * retourne vrai si cette ij est une AIT (type alloc_innit_travail).
     * <p>
     * Cette méthode est fournie juste pour accelerer un peu les tests dans les autres classes.
     * </p>
     * 
     * @return la valeur courante de l'attribut grande
     */
    public boolean isAit() {
        return IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ);
    }

    /**
     * retourne vrai si cette ij est AA (type alloc_assist).
     * <p>
     * Cette méthode est fournie juste pour accelerer un peu les tests dans les autres classes.
     * </p>
     * 
     * @return la valeur courante de l'attribut grande
     */
    public boolean isAllocAssist() {
        return IIJPrononce.CS_ALLOC_ASSIST.equals(csTypeIJ);
    }

    /**
     * retourne vrai si cette ij est une grande ij (type grande_ij).
     * <p>
     * Cette méthode est fournie juste pour accelerer un peu les tests dans les autres classes.
     * </p>
     * 
     * @return la valeur courante de l'attribut grande
     */
    public boolean isGrandeIJ() {
        return IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ);
    }

    /**
     * retourne vrai si cette ij est une petite ij (type petite_ij).
     * <p>
     * Cette méthode est fournie juste pour accelerer un peu les tests dans les autres classes.
     * </p>
     * 
     * @return la valeur courante de l'attribut grande
     */
    public boolean isPetiteIJ() {
        return IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRDemande loadDemande(BITransaction transaction) throws Exception {
        if (demande == null) {
            demande = new PRDemande();
        }

        demande.setSession(getSession());

        if (!JadeStringUtil.isIntegerEmpty(idDemande) && !idDemande.equals(demande.getIdDemande())) {
            demande.setIdDemande(idDemande);
            if (transaction == null) {
                demande.retrieve();
            } else {
                demande.retrieve(transaction);
            }
        }

        return demande;
    }

    /**
     * charge le (seul possible) enfant valide ou en attente de ce prononce.
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJPrononce loadEnfantActif(BITransaction transaction) throws Exception {
        if (IIJPrononce.CS_ANNULE.equals(csEtat) && (enfantActif == null)) {
            IJPrononceManager prononces;

            if (IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)) {
                prononces = new IJGrandeIJManager();
            } else if (IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)) {
                prononces = new IJPetiteIJManager();
            } else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)) {
                prononces = new IJPrononceAitManager();
            } else {
                prononces = new IJPrononceAllocAssistanceManager();
            }

            prononces.setForIdParent(idPrononce);
            // point ouvert 00658
            // ajoute de l'etat decide
            prononces.setForCsEtats(new String[] { /* IIJPrononce.CS_VALIDE, */IIJPrononce.CS_COMMUNIQUE,
                    IIJPrononce.CS_DECIDE });
            prononces.setSession(getSession());
            prononces.find(transaction);

            if (!prononces.isEmpty()) {
                enfantActif = (IJPrononce) prononces.get(0);
            }
        }

        return enfantActif;
    }

    /**
     * charge le (seul possible) enfant de correction de ce prononce.
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJPrononce loadEnfantCorrection(BITransaction transaction) throws Exception {
        if (enfantCorrection == null) {
            IJPrononceManager prononces;

            if (IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)) {
                prononces = new IJGrandeIJManager();
            } else if (IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)) {
                prononces = new IJPetiteIJManager();
            } else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)) {
                prononces = new IJPrononceAitManager();
            } else {
                prononces = new IJPrononceAllocAssistanceManager();
            }

            prononces.setForIdCorrection(idPrononce);
            prononces.setSession(getSession());
            prononces.find(transaction);

            if (!prononces.isEmpty()) {
                enfantCorrection = (IJPrononce) prononces.get(0);
            }
        }

        return enfantCorrection;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJRevenu loadRevenuReadaptation(BITransaction transaction) throws Exception {
        if ((revenuReadaptation == null) && !JadeStringUtil.isIntegerEmpty(idRevenuReadaptation)) {
            revenuReadaptation = new IJRevenu();
            revenuReadaptation.setSession(getSession());
            revenuReadaptation.setIdRevenu(idRevenuReadaptation);
            if (transaction == null) {
                revenuReadaptation.retrieve();
            } else {
                revenuReadaptation.retrieve(transaction);
            }
        }

        return revenuReadaptation;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJSituationFamiliale loadSituationFamiliale(BITransaction transaction) throws Exception {
        if ((situationFamiliale == null) && !JadeStringUtil.isIntegerEmpty(idSituationFamiliale)) {
            situationFamiliale = new IJSituationFamiliale();
            situationFamiliale.setSession(getSession());
            situationFamiliale.setIdSituationFamiliale(idSituationFamiliale);
            situationFamiliale.retrieve(transaction);
        }

        return situationFamiliale;
    }

    public void setAnneeRenteEnCours(String anneeRenteEnCours) {
        this.anneeRenteEnCours = anneeRenteEnCours;
    }

    public void setAvecDecision(Boolean avecDecision) {
        this.avecDecision = avecDecision;
    }

    /**
     * @param string
     */
    public void setCodesCasSpecial(String string) {
        codesCasSpecial = string;
    }

    public void setCsCantonImpositionSource(String csCantonImpositionSource) {
        this.csCantonImpositionSource = csCantonImpositionSource;
    }

    /**
     * setter pour l'attribut cs etat.
     * 
     * @param csEtat
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    /**
     * setter pour l'attribut cs genre.
     * 
     * @param csGenre
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsGenre(String csGenre) {
        this.csGenre = csGenre;
    }

    public void setCsMotifEcheance(String motifEcheance) {
        csMotifEcheance = motifEcheance;
    }

    /**
     * setter pour l'attribut cs statut professionnel.
     * 
     * @param csStatutProfessionnel
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsStatutProfessionnel(String csStatutProfessionnel) {
        this.csStatutProfessionnel = csStatutProfessionnel;
    }

    /**
     * @param string
     */
    public void setCsTypeHebergement(String string) {
        csTypeHebergement = string;
    }

    /**
     * setter pour l'attribut cs type IJ.
     * 
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        this.csTypeIJ = csTypeIJ;
    }

    /**
     * setter pour l'attribut date debut prononce.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrononce(String string) {
        dateDebutPrononce = string;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /**
     * setter pour l'attribut date fin prononce.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinPrononce(String string) {
        dateFinPrononce = string;
    }

    /**
     * setter pour l'attribut date prononce.
     * 
     * @param datePrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononce(String datePrononce) {
        this.datePrononce = datePrononce;
    }

    /**
     * setter pour l'attribut demande.
     * 
     * @param demande
     *            une nouvelle valeur pour cet attribut
     */
    public void setDemande(PRDemande demande) {
        this.demande = demande;
        idDemande = demande.getIdDemande();
    }

    /**
     * setter pour l'attribut demi IJAC.
     * 
     * @param demiIJAC
     *            une nouvelle valeur pour cet attribut
     */
    public void setDemiIJAC(String demiIJAC) {
        this.demiIJAC = demiIJAC;
    }

    /**
     * @param string
     */
    public void setEchelle(String string) {
        echelle = string;
    }

    /**
     * setter pour l'attribut id correction.
     * 
     * @param idCorrection
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCorrection(String idCorrection) {
        this.idCorrection = idCorrection;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * setter pour l'attribut id demande.
     * 
     * @param idDemande
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * setter pour l'attribut id gestionnaire.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdGestionnaire(String string) {
        idGestionnaire = string;
    }

    /**
     * @param string
     */
    public void setIdInfoCompl(String string) {
        idInfoCompl = string;
    }

    /**
     * setter pour l'attribut id parent.
     * 
     * @param idParent
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    public void setIdParentCorrigeDepuis(String idParentCorrigeDepuis) {
        this.idParentCorrigeDepuis = idParentCorrigeDepuis;
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param idPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    /**
     * setter pour l'attribut id revenu readaptation.
     * 
     * @param idRevenuReadaptation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRevenuReadaptation(String idRevenuReadaptation) {
        this.idRevenuReadaptation = idRevenuReadaptation;
    }

    /**
     * setter pour l'attribut id situation familiale.
     * 
     * @param idSituationFamiliale
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFamiliale(String idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }

    /**
     * Valeur possible "1" ou "0". Cet attribut est utilisé dans l'écran de calcul des 30/60/90 jours pour savoir si le
     * prononcé est visible ou pas
     * 
     * @param isPrononceSelectionne
     *            "1" si le prononcé est sélectionné sinon "0"
     */
    public final void setIsPrononceSelectionne(String isPrononceSelectionne) {
        this.isPrononceSelectionne = isPrononceSelectionne;
    }

    /**
     * setter pour l'attribut montant garanti AA.
     * 
     * @param montantGarantiAA
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantGarantiAA(String montantGarantiAA) {
        this.montantGarantiAA = montantGarantiAA;
    }

    /**
     * setter pour l'attribut montant garanti AAReduit.
     * 
     * @param montantGarantiAAReduit
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantGarantiAAReduit(Boolean montantGarantiAAReduit) {
        this.montantGarantiAAReduit = montantGarantiAAReduit;
    }

    /**
     * @param string
     */
    public void setMontantRenteEnCours(String string) {
        montantRenteEnCours = string;
    }

    public void setNoDecisionAI(String noDecisionAI) {
        this.noDecisionAI = noDecisionAI;
    }

    /**
     * setter pour l'attribut cs office AI.
     * 
     * @param csOfficeAI
     *            une nouvelle valeur pour cet attribut
     */
    public void setOfficeAI(String csOfficeAI) {
        officeAI = csOfficeAI;
    }

    /**
     * @param string
     */
    public void setRam(String string) {
        ram = string;
    }

    /**
     * setter pour l'attribut soumis impot source.
     * 
     * @param soumisImpotSource
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisImpotSource(Boolean soumisImpotSource) {
        this.soumisImpotSource = soumisImpotSource;
    }

    public void setTauxImpositionSource(String tauxImpositionSource) {
        this.tauxImpositionSource = tauxImpositionSource;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdPrononce(pk);
    }
}
