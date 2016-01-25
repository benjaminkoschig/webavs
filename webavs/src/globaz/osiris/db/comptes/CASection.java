package globaz.osiris.db.comptes;

import globaz.aquila.api.ICOContentieux;
import globaz.aquila.api.ICOEtape;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIEvenementContentieux;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIParametreEtape;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.api.APITypeSection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.contentieux.CADateExecutionSommationAquila;
import globaz.osiris.db.contentieux.CADateExecutionSommationAquilaManager;
import globaz.osiris.db.contentieux.CADateExecutionSommationOsirisManager;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAEvenementContentieuxManager;
import globaz.osiris.db.contentieux.CAEvenementContentieuxSectionParamEtapeManager;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.db.contentieux.CAMotifContentieuxUtil;
import globaz.osiris.db.contentieux.CASectionAuxPoursuitesManager;
import globaz.osiris.db.contentieux.CASectionAuxPoursuitesNotRadieeManager;
import globaz.osiris.db.contentieux.CASectionAvecSectionsPoursuiteManager;
import globaz.osiris.db.contentieux.CASequenceContentieux;
import globaz.osiris.db.utils.CAGestionRemarque;
import globaz.osiris.db.utils.CARemarque;
import globaz.osiris.db.utils.IntRemarque;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.translation.CACodeSystem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author SCH Date de création : (11.12.2001 13:59:54)
 * @revision SCO 22 janv. 2010
 */
public class CASection extends BEntity implements Serializable, APISection, IntRemarque {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int AK_IDEXTERNE = 1;
    public final static int AK_REFERENCE_BVR = 2;

    private static final String DATE_FIN = "31.12.2999";

    public static final String FIELD_AMENDE = "AMENDE";
    public static final String FIELD_ATTENTE_LSVDD = "ATTENTELSVDD";
    public static final String FIELD_BASE = "BASE";
    public static final String FIELD_CATEGORIESECTION = "CATEGORIESECTION";
    public static final String FIELD_CONTENTIEUXESTSUS = "CONTENTIEUXESTSUS";
    public static final String FIELD_DATEDEBUTPERIODE = "DATEDEBUTPERIODE";
    public static final String FIELD_DATEECHEANCE = "DATEECHEANCE";
    public static final String FIELD_DATEFINPERIODE = "DATEFINPERIODE";
    public static final String FIELD_DATESECTION = "DATESECTION";
    public static final String FIELD_DATESUSPENDU = "DATESUSPENDU";
    public static final String FIELD_DOMAINE = "DOMAINE";
    public static final String FIELD_FRAIS = "FRAIS";
    public static final String FIELD_IDCAISSEPROFESSIONNELE = "IDCAISSEPRO";
    public static final String FIELD_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";
    public static final String FIELD_IDEXTERNE = "IDEXTERNE";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    public static final String FIELD_IDLASTETAPECTX = "IDLASTETAPECTX";
    public static final String FIELD_IDLASTETATAQUILA = "IDLASTETATAQUILA";
    public static final String FIELD_IDMODECOMPENSATION = "IDMODECOMPENS";
    public static final String FIELD_IDMOTCONSUS = "IDMOTCONSUS";
    public static final String FIELD_IDPASSAGECOMP = "IDPASSAGECOMP";
    public static final String FIELD_IDPLANRECOUVREMENT = "IDPLANRECOUVREMENT";
    public static final String FIELD_IDPOSJOU = "IDPOSJOU";
    public static final String FIELD_IDREMARQUE = "IDREMARQUE";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_IDSECTIONPRINC = "IDSECTIONPRINC";
    public static final String FIELD_IDSEQCON = "IDSEQCON";
    public static final String FIELD_IDTYPESECTION = "IDTYPESECTION";
    public static final String FIELD_INTERETS = "INTERETS";
    private static final String FIELD_NON_IMPRIMABLE = "NONIMPRIMABLE";
    public static final String FIELD_NOPOURSUITE = "NOPOURSUITE";
    public static final String FIELD_PMTCMP = "PMTCMP";
    public static final String FIELD_REFERENCE_BVR = "REFERENCEBVR";
    public static final String FIELD_SOLDE = "SOLDE";
    public static final String FIELD_STATUTBN = "STATUTBN";
    public static final String FIELD_TAXES = "TAXES";
    public static final String FIELD_TYPEADRESSE = "TYPEADRESSE";

    private static final String LABEL_CODE_REFERENCE_NON_ATTRIBUE = "CODE_REFERENCE_NON_ATTRIBUE";
    private static final String LABEL_CODE_REFERENCE_NON_AUTORISE = "CODE_REFERENCE_NON_AUTORISE";
    private static final String LABEL_MONTANT_POSITIF = "5125";

    private static final String LABEL_OPERATION_AUXILIAIRE_NON_INSEREE = "OPERATION_AUXILIAIRE_NON_INSEREE";
    public static final String LABEL_RETOURS_GERER_AILLEURS = "RETOURS_GERER_AILLEURS";
    private static final String LABEL_SECTION_NON_RENSEIGNEE = CASection.LABEL_MONTANT_POSITIF;
    private static final String SOLDE_ZERO = "0.00";
    public static final String TABLE_CASECTP = "CASECTP";

    private static final int YEAR_LENGTH = 4;
    private static final String ZERO = "0";

    public static String findIdContentieuxAquilaForIdSection(String idSection, BISession session) throws Exception {
        ICOContentieux ctx = (ICOContentieux) session.getAPIFor(ICOContentieux.class);
        return ctx.findIdContentieuxForIdSection(idSection);

    }

    private String amende = CASection.ZERO;
    private String ancienIdSequenceContentieux = null;
    private Boolean attenteLSVDD = new Boolean(false);
    private String base = CASection.SOLDE_ZERO;
    private String categorieSection = "";
    private CACompteAnnexe compteAnnexe = null;
    private Boolean contentieuxEstSuspendu = new Boolean(false);
    private FWParametersSystemCode csMotifContentieuxSuspendu = null;
    private FWParametersSystemCodeManager csMotifContentieuxSuspendus = null;
    private String dateDebutPeriode = "";
    private String dateEcheance = "";
    private String dateFinPeriode = "";
    private String dateSection = "";
    private String dateSuspendu = "";
    private String domaine;
    private CAEvenementContentieux firstEvContentieuxNonExec = null;
    private CAEvenementContentieux firstEvContentieuxNonExecIgnore = null;
    private String frais = CASection.ZERO;
    private String idCaissePro = "";
    private String idCompteAnnexe = "";
    private String idExterne = "";
    private String idJournal = "";
    private String idLastEtapeCtx = "";
    private String idLastEtatAquila = "";
    private String idModeCompensation = "";
    private String idMotifContentieuxSuspendu = "";
    private String idPassageComp = "";
    private String idPlanRecouvrement = "";
    private String idPosteJournalisation = "";
    private String idRemarque = "";
    private String idSection = "";
    private String idSectionPrincipal = null;
    private String idSequenceContentieux = "";
    private String idTypeSection = "";
    private String interets = CASection.ZERO;
    private CAJournal journal = null;
    private CAEvenementContentieux lastEvenementContentieux = null;
    private Boolean nonImprimable = new Boolean(false);
    private String numeroPoursuite = "";
    private CAPlanRecouvrement planRecouvrement = null;
    private String pmtCmp = CASection.SOLDE_ZERO;
    private String referenceBvr = "";
    private CARemarque remarque = null;
    private APIRubrique rubriqueDeCompensation = null;
    private boolean saveRemarque = false;
    private APISectionDescriptor sectionDescriptor;
    private CASequenceContentieux sequenceContentieux = null;
    private String solde = CASection.SOLDE_ZERO;
    private String statutBN = null;
    private String taxes = CASection.ZERO;
    private String texteRemarque = "";
    private String typeAdresse;
    private CATypeSection typeSection = null;

    private FWParametersUserCode ucMotifContentieuxSuspendu = null;

    /**
     * Commentaire relatif au constructeur CASection
     */
    public CASection() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        if (APISection.ID_CATEGORIE_SECTION_LTN.equals(getCategorieSection())) {
            if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                // On ajoute le motif dans la table des motifs, le blocage se
                // faite dans le _before
                CAMotifContentieux motif = new CAMotifContentieux();
                motif.setSession(transaction.getSession());
                motif.setIdSection(getIdSection());
                motif.setIdMotifBlocage(CACodeSystem.CS_DECOMPTE_LTN);
                motif.setDateDebut(JACalendar.today().toStr("."));
                motif.setDateFin(CASection.DATE_FIN);
                motif.add(transaction);
            }
        }
        super._afterAdd(transaction);
    }

    /**
     * Date de création : (26.03.2002 18:39:22)
     */
    @Override
    protected void _afterRetrieveWithResultSet(BStatement statement) throws Exception {
        // Laisser la supercalsse traiter l'événement
        super._afterRetrieveWithResultSet(statement);

        // Charger les zones pour l'écran si nécessaire
        ancienIdSequenceContentieux = idSequenceContentieux;
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception Exception en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdSection(this._incCounter(transaction, idSection));

        // Date d'échéance par défaut si null
        if (JACalendar.isNull(getDateEcheance())) {
            setDateEcheance(getSectionDescriptor().getDateEcheanceAdaptee());
        }

        // Pas de catégorie, on tente d'en obtenir une par défaut
        if (JadeStringUtil.isIntegerEmpty(getCategorieSection())) {
            setCategorieSection(getSectionDescriptor().getIdCategorie());
        }

        // Pas de date de début, on tente d'en obtenir une
        if (JadeStringUtil.isBlank(getDateDebutPeriode())) {
            setDateDebutPeriode(getSectionDescriptor().getDateDebutPeriode());
        }

        // Pas de date de fin, on tente d'en obtenir une
        if (JadeStringUtil.isBlank(getDateFinPeriode())) {
            setDateFinPeriode(getSectionDescriptor().getDateFinPeriode());
        }

        // Séquence contentieux par défaut si 0
        if (JadeStringUtil.isIntegerEmpty(getIdSequenceContentieux())) {
            if (getTypeSection() != null) {
                setIdSequenceContentieux(getTypeSection().getIdSequenceContentieux());
            }
        }
        // Contrôler les types de sections
        if ((getCategorieSection().equals(APISection.ID_CATEGORIE_SECTION_ALLOCATIONS_FAMILIALES) && !getIdTypeSection()
                .equals(APISection.ID_TYPE_SECTION_AF))
                || (getCategorieSection().equals(APISection.ID_CATEGORIE_SECTION_APG) && !getIdTypeSection().equals(
                        APISection.ID_TYPE_SECTION_APG))
                || (getCategorieSection().equals(APISection.ID_CATEGORIE_SECTION_IJAI) && !getIdTypeSection().equals(
                        APISection.ID_TYPE_SECTION_IJAI))
                || (getCategorieSection().equals(APISection.ID_CATEGORIE_SECTION_RESTITUTIONS) && !getIdTypeSection()
                        .equals(APISection.ID_TYPE_SECTION_RESTITUTION))
                || (getCategorieSection().equals(APISection.ID_CATEGORIE_SECTION_REPARATION_DOMMAGES) && !getIdTypeSection()
                        .equals(APISection.ID_TYPE_SECTION_ARD))) {
            _addError(transaction, FWMessageFormat.format(getSession().getLabel("SECTION_CONTROLE_TYPE_SECTION"),
                    getCategorieSection().substring(4), getTypeSection().getDescription()));
        }

        if (APISection.ID_CATEGORIE_SECTION_LTN.equals(getCategorieSection())) {
            // on bloque la section dans le _before et on met le motif de
            // blocage dans l'_after.
            setContentieuxEstSuspendu(Boolean.TRUE);
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                setDateSuspendu(JACalendar.today().toStr("."));
                setIdMotifContentieuxSuspendu(CACodeSystem.CS_DECOMPTE_LTN);
            }
        }

        // InfoRom169 : Pas de compensation possible sur ces décomptes.
        if (APISection.ID_CATEGORIE_SECTION_ICI.equals(getCategorieSection())
                || APISection.ID_CATEGORIE_SECTION_DIVIDENDE.equals(getCategorieSection())) {
            setIdModeCompensation(APISection.MODE_BLOQUER_COMPENSATION);
        }
    }

    /**
     * Date de création : (22.01.2002 15:18:10)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        if (hasOperations()) {
            _addError(transaction, getSession().getLabel("7099"));
        } else {
            // Supprimer la remarque
            if (!JadeStringUtil.isIntegerEmpty(getIdRemarque())) {
                CARemarque rem = new CARemarque();
                rem.setIdRemarque(getIdRemarque());
                rem.retrieve(transaction);
                rem.delete(transaction);
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("7095"));
                    return;
                }
                setIdRemarque("");
            }
        }
    }

    /**
     * Date de création : (22.01.2002 14:44:51)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        updateDateFinPeriode();
        // Mise à jour de la remarque
        _saveRemarque(transaction);
    }

    /**
     * Cette Méthode retourne la date d'éxecution. En cas d'erreur, retourne "".
     * 
     * @return String Date d'éxecution de l'étape
     * @throws Exception
     */
    public String _getDateExecutionSommation() throws Exception {
        String date = "";
        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {

            CADateExecutionSommationAquilaManager manager = new CADateExecutionSommationAquilaManager();
            manager.setSession(getSession());
            manager.setForIdSection(idSection);
            manager.setForSeqCon(idSequenceContentieux);
            manager.setForCsEtape(APIEtape.SOMMATION_AQUILA);
            manager.find();

            if (!manager.isEmpty()) {
                CADateExecutionSommationAquila ev = (CADateExecutionSommationAquila) manager.getFirstEntity();
                date = ev.getDateExecution();
            }
        } else {
            CADateExecutionSommationOsirisManager manager = new CADateExecutionSommationOsirisManager();
            manager.setSession(getSession());
            manager.setForIdSection(idSection);
            manager.setForIdSeqCon(idSequenceContentieux);
            manager.setForTypeEtape(APIEtape.SOMMATION);
            manager.find();

            if (!manager.isEmpty()) {
                CAEvenementContentieux ev = (CAEvenementContentieux) manager.getFirstEntity();
                date = ev.getDateExecution();
            }
        }

        return date;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CASection.TABLE_CASECTP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSection = statement.dbReadNumeric(CASection.FIELD_IDSECTION);
        idPlanRecouvrement = statement.dbReadNumeric(CASection.FIELD_IDPLANRECOUVREMENT);
        idCompteAnnexe = statement.dbReadNumeric(CASection.FIELD_IDCOMPTEANNEXE);
        idTypeSection = statement.dbReadNumeric(CASection.FIELD_IDTYPESECTION);
        idRemarque = statement.dbReadNumeric(CASection.FIELD_IDREMARQUE);
        idPosteJournalisation = statement.dbReadNumeric(CASection.FIELD_IDPOSJOU);
        idSequenceContentieux = statement.dbReadNumeric(CASection.FIELD_IDSEQCON);
        idExterne = statement.dbReadString(CASection.FIELD_IDEXTERNE);
        dateSection = statement.dbReadDateAMJ(CASection.FIELD_DATESECTION);
        dateEcheance = statement.dbReadDateAMJ(CASection.FIELD_DATEECHEANCE);
        solde = statement.dbReadNumeric(CASection.FIELD_SOLDE, 2);
        base = statement.dbReadNumeric(CASection.FIELD_BASE, 2);
        pmtCmp = statement.dbReadNumeric(CASection.FIELD_PMTCMP, 2);
        interets = statement.dbReadNumeric(CASection.FIELD_INTERETS, 2);
        taxes = statement.dbReadNumeric(CASection.FIELD_TAXES, 2);
        frais = statement.dbReadNumeric(CASection.FIELD_FRAIS, 2);
        amende = statement.dbReadNumeric(CASection.FIELD_AMENDE, 2);
        contentieuxEstSuspendu = statement.dbReadBoolean(CASection.FIELD_CONTENTIEUXESTSUS);
        dateDebutPeriode = statement.dbReadDateAMJ(CASection.FIELD_DATEDEBUTPERIODE);
        dateFinPeriode = statement.dbReadDateAMJ(CASection.FIELD_DATEFINPERIODE);
        idMotifContentieuxSuspendu = statement.dbReadNumeric(CASection.FIELD_IDMOTCONSUS);
        categorieSection = statement.dbReadNumeric(CASection.FIELD_CATEGORIESECTION);
        idJournal = statement.dbReadNumeric(CASection.FIELD_IDJOURNAL);
        idLastEtapeCtx = statement.dbReadNumeric(CASection.FIELD_IDLASTETAPECTX);
        dateSuspendu = statement.dbReadDateAMJ(CASection.FIELD_DATESUSPENDU);
        numeroPoursuite = statement.dbReadString(CASection.FIELD_NOPOURSUITE);
        idSectionPrincipal = statement.dbReadNumeric(CASection.FIELD_IDSECTIONPRINC);
        idLastEtatAquila = statement.dbReadNumeric(CASection.FIELD_IDLASTETATAQUILA);
        idModeCompensation = statement.dbReadNumeric(CASection.FIELD_IDMODECOMPENSATION);
        idCaissePro = statement.dbReadNumeric(CASection.FIELD_IDCAISSEPROFESSIONNELE);
        referenceBvr = statement.dbReadString(CASection.FIELD_REFERENCE_BVR);
        idPassageComp = statement.dbReadNumeric(CASection.FIELD_IDPASSAGECOMP);
        domaine = statement.dbReadNumeric(CASection.FIELD_DOMAINE);
        typeAdresse = statement.dbReadNumeric(CASection.FIELD_TYPEADRESSE);
        nonImprimable = statement.dbReadBoolean(CASection.FIELD_NON_IMPRIMABLE);
        attenteLSVDD = statement.dbReadBoolean(CASection.FIELD_ATTENTE_LSVDD);
        statutBN = statement.dbReadNumeric(CASection.FIELD_STATUTBN);
    }

    /**
     * Date de création : (14.03.2002 16:32:32)
     */
    private void _saveRemarque(BTransaction transaction) {
        try {
            if (saveRemarque) {
                CAGestionRemarque gestionRemarque = new CAGestionRemarque(this);
                gestionRemarque.add(transaction);
                remarque = gestionRemarque.getRemarque();
            }
            saveRemarque = false;
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdCompteAnnexe(), getSession().getLabel("5106"));
        _propertyMandatory(statement.getTransaction(), getIdSection(), getSession().getLabel("7080"));
        _propertyMandatory(statement.getTransaction(), getIdExterne(), getSession().getLabel("7081"));
        _propertyMandatory(statement.getTransaction(), getIdTypeSection(), getSession().getLabel("7082"));
        _propertyMandatory(statement.getTransaction(), getDateSection(), getSession().getLabel("7083"));
        _checkDate(statement.getTransaction(), getDateSection(), getSession().getLabel("7084"));

        if (!JadeStringUtil.isIntegerEmpty(getDateDebutPeriode())) {
            _checkDate(statement.getTransaction(), getDateDebutPeriode(), getSession().getLabel("7085"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getDateFinPeriode())) {
            _checkDate(statement.getTransaction(), getDateFinPeriode(), getSession().getLabel("7086"));
        }

        if (getTypeSection() == null) {
            _addError(statement.getTransaction(), getSession().getLabel("5015"));
        }

        // Vérifier le solde de la section
        FWCurrency dSolde = new FWCurrency(getSolde());
        FWCurrency dTotal = new FWCurrency(getBase());
        dTotal.add(getPmtCmp());
        dTotal.add(getFrais());
        dTotal.add(getTaxes());
        dTotal.add(getAmende());
        dTotal.add(getInterets());

        if (!dSolde.equals(dTotal)) {
            _addError(statement.getTransaction(), getSession().getLabel("7087"));
        }

        // Vérification de la suspension
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            if (getContentieuxEstSuspendu().booleanValue()) {
                if (JadeStringUtil.isBlank(getDateSuspendu())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7088"));
                } else {
                    _checkDate(statement.getTransaction(), getDateSuspendu(), getSession().getLabel("7089"));
                }
                if (JadeStringUtil.isBlank(getIdMotifContentieuxSuspendu())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7090"));
                }
            } else {
                if (!JAUtil.isDateEmpty(getDateSuspendu())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7091"));
                }

                if (!JadeStringUtil.isIntegerEmpty(getIdMotifContentieuxSuspendu())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7092"));
                }
            }
        }

        // Vérification de la séquence
        if ((ancienIdSequenceContentieux != null)
                && !ancienIdSequenceContentieux.equalsIgnoreCase(getIdSequenceContentieux())) {
            if (hasEvenementContentieux()) {
                _addError(statement.getTransaction(), getSession().getLabel("7093"));
            }
        }
    }

    /**
     * Date de création : (11.04.2002 10:33:12)
     * 
     * @param alternateKey int
     * @exception Exception La description de l'exception.
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Clé alternée numéro 1 : idCompteAnnexe, idTypeSection, idExterne
        switch (alternateKey) {
            case AK_IDEXTERNE:
                statement.writeKey(CASection.FIELD_IDCOMPTEANNEXE,
                        this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), ""));
                statement.writeKey(CASection.FIELD_IDTYPESECTION,
                        this._dbWriteNumeric(statement.getTransaction(), getIdTypeSection(), ""));
                statement.writeKey(CASection.FIELD_IDEXTERNE,
                        this._dbWriteString(statement.getTransaction(), getIdExterne(), ""));
                break;
            case AK_REFERENCE_BVR:
                statement.writeKey(CASection.FIELD_REFERENCE_BVR,
                        this._dbWriteString(statement.getTransaction(), getReferenceBvr(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CASection.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdSection(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CASection.FIELD_IDSECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        statement.writeField(CASection.FIELD_IDPLANRECOUVREMENT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanRecouvrement(), "idPlanRecouvrement"));
        statement.writeField(CASection.FIELD_IDCOMPTEANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteAnnexe(), "idCompteAnnexe"));
        statement.writeField(CASection.FIELD_IDTYPESECTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeSection(), "idTypeSection"));
        statement.writeField(CASection.FIELD_IDREMARQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField(CASection.FIELD_IDPOSJOU,
                this._dbWriteNumeric(statement.getTransaction(), getIdPosteJournalisation(), "idPosJou"));
        statement.writeField(CASection.FIELD_IDSEQCON,
                this._dbWriteNumeric(statement.getTransaction(), getIdSequenceContentieux(), "idSeqCon"));
        statement.writeField(CASection.FIELD_IDEXTERNE,
                this._dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
        statement.writeField(CASection.FIELD_DATESECTION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateSection(), "dateSection"));
        statement.writeField(CASection.FIELD_DATEECHEANCE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEcheance(), "dateEcheance"));
        statement.writeField(CASection.FIELD_SOLDE,
                this._dbWriteNumeric(statement.getTransaction(), getSolde(), "solde"));
        statement.writeField(CASection.FIELD_BASE, this._dbWriteNumeric(statement.getTransaction(), getBase(), "base"));
        statement.writeField(CASection.FIELD_PMTCMP,
                this._dbWriteNumeric(statement.getTransaction(), getPmtCmp(), "pmtCmp"));
        statement.writeField(CASection.FIELD_INTERETS,
                this._dbWriteNumeric(statement.getTransaction(), getInterets(), "interets"));
        statement.writeField(CASection.FIELD_TAXES,
                this._dbWriteNumeric(statement.getTransaction(), getTaxes(), "taxes"));
        statement.writeField(CASection.FIELD_FRAIS,
                this._dbWriteNumeric(statement.getTransaction(), getFrais(), "frais"));
        statement.writeField(CASection.FIELD_AMENDE,
                this._dbWriteNumeric(statement.getTransaction(), getAmende(), "amende"));
        statement.writeField(CASection.FIELD_CONTENTIEUXESTSUS, this._dbWriteBoolean(statement.getTransaction(),
                getContentieuxEstSuspendu(), BConstants.DB_TYPE_BOOLEAN_CHAR, "contentieuxEstSus"));
        statement.writeField(CASection.FIELD_DATEDEBUTPERIODE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDebutPeriode(), "dateDebutPeriode"));
        statement.writeField(CASection.FIELD_DATEFINPERIODE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFinPeriode(), "dateFinPeriode"));
        statement.writeField(CASection.FIELD_IDMOTCONSUS,
                this._dbWriteNumeric(statement.getTransaction(), getIdMotifContentieuxSuspendu(), "idMotConSus"));
        statement.writeField(CASection.FIELD_CATEGORIESECTION,
                this._dbWriteNumeric(statement.getTransaction(), getCategorieSection(), "categorieSection"));
        statement.writeField(CASection.FIELD_IDJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(CASection.FIELD_IDLASTETAPECTX,
                this._dbWriteNumeric(statement.getTransaction(), getIdLastEtapeCtx(), "idLastEtapeCtx"));
        statement.writeField(CASection.FIELD_DATESUSPENDU,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateSuspendu(), "dateSuspendu"));
        statement.writeField(CASection.FIELD_NOPOURSUITE,
                this._dbWriteString(statement.getTransaction(), getNumeroPoursuite(), "numeroPoursuite"));
        statement.writeField(CASection.FIELD_IDSECTIONPRINC,
                this._dbWriteNumeric(statement.getTransaction(), getIdSectionPrincipal(), "idSectionPrinc"));
        statement.writeField(CASection.FIELD_IDLASTETATAQUILA,
                this._dbWriteNumeric(statement.getTransaction(), getIdLastEtatAquila(), "idLastEtatAquila"));
        statement.writeField(CASection.FIELD_IDMODECOMPENSATION,
                this._dbWriteNumeric(statement.getTransaction(), getIdModeCompensation(), "idModeCompens"));
        statement.writeField(CASection.FIELD_IDCAISSEPROFESSIONNELE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCaisseProfessionnelle(), "idCaissePro"));
        statement.writeField(CASection.FIELD_REFERENCE_BVR,
                this._dbWriteString(statement.getTransaction(), getReferenceBvr(), "referenceBvr"));
        statement.writeField(CASection.FIELD_IDPASSAGECOMP,
                this._dbWriteNumeric(statement.getTransaction(), getIdPassageComp(), "idPassageComp"));
        statement.writeField(CASection.FIELD_DOMAINE,
                this._dbWriteNumeric(statement.getTransaction(), getDomaine(), "domaine"));
        statement.writeField(CASection.FIELD_TYPEADRESSE,
                this._dbWriteNumeric(statement.getTransaction(), getTypeAdresse(), "typeAdresse"));
        statement.writeField(CASection.FIELD_NON_IMPRIMABLE, this._dbWriteBoolean(statement.getTransaction(),
                getNonImprimable(), BConstants.DB_TYPE_BOOLEAN_CHAR, "nonImprimable"));
        statement.writeField(CASection.FIELD_ATTENTE_LSVDD, this._dbWriteBoolean(statement.getTransaction(),
                getAttenteLSVDD(), BConstants.DB_TYPE_BOOLEAN_CHAR, "attenteLSVDD"));
        statement.writeField(CASection.FIELD_STATUTBN,
                this._dbWriteNumeric(statement.getTransaction(), getStatutBN(), "statutBN"));
    }

    /**
     * Ajout d'une opération auxiliaire en fonction d'une écriture de base.
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param section
     * @param ecritureAuxiliaire
     * @param sourceEcriture
     * @return IdOperation de l'écriture auxiliaire créée.
     * @throws Exception
     */
    private String addAuxiliaire(BTransaction transaction, CACompteAnnexe compteAnnexe, CAJournal journal,
            CASection section, CAAuxiliaire ecritureAuxiliaire, CAEcriture sourceEcriture) throws Exception {
        ecritureAuxiliaire.setSession(getSession());
        ecritureAuxiliaire.setIdJournal(journal.getIdJournal());
        ecritureAuxiliaire.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        ecritureAuxiliaire.setIdSection(section.getIdSection());

        ecritureAuxiliaire.setDate(sourceEcriture.getDate());

        ecritureAuxiliaire.setMontant(sourceEcriture.getMontantToCurrency().toStringFormat());
        ecritureAuxiliaire.setCodeDebitCredit(sourceEcriture.getCodeDebitCredit());

        ecritureAuxiliaire.setLibelle(sourceEcriture.getLibelle());

        ecritureAuxiliaire.setIdCompte(sourceEcriture.getIdCompte());
        ecritureAuxiliaire.setProvenancePmt(sourceEcriture.getProvenancePmt());

        ecritureAuxiliaire.add(transaction);

        if (journal.getEstVisibleImmediatement().booleanValue()) {
            ecritureAuxiliaire.activer(transaction);
            ecritureAuxiliaire.update(transaction);
        }

        if ((ecritureAuxiliaire.getLog() != null) && (ecritureAuxiliaire.getLog().getHighestMessage() != null)) {
            FWMessage msg = ecritureAuxiliaire.getLog().getHighestMessage();
            throw new Exception(msg.getMessageText());
        }

        return ecritureAuxiliaire.getIdOperation();
    }

    /**
     * Ajout d'une opération auxiliaire sans libellé sur la section actuelle. Utile lors de la compensation d'un section
     * par rapport à sa section auxiliaire.
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param montant
     * @throws Exception
     */
    public void addAuxiliaire(BTransaction transaction, String idCompteAnnexe, CAJournal journal, FWCurrency montant)
            throws Exception {
        boolean useOwnTransaction = false;
        try {
            // Refuser si section pas instanciée
            if (isNew()) {
                throw new Exception(getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
            }
            // Ouvrir une transaction si nécessaire
            if (transaction == null) {
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                useOwnTransaction = true;
            }
            // Si nécessaire création d'un journal d'Ecritures journalières
            // Récupérer le journal des inscriptions journalières
            if (journal == null) {
                journal = CAJournal.fetchJournalJournalier(getSession(), transaction);
            }

            this.addAuxiliaire(transaction, idCompteAnnexe, journal, montant, this);
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        } finally {
            if (useOwnTransaction) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    JadeLogger.fatal(this, e);
                } finally {
                    try {
                        transaction.closeTransaction();
                        transaction = null;
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * Ajout d'une opération auxiliaire sans libellé sur une section.
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param montant
     * @param section
     * @param isCredit
     * @throws Exception
     */
    private void addAuxiliaire(BTransaction transaction, String idCompteAnnexe, CAJournal journal, FWCurrency montant,
            CASection section) throws Exception {
        CAAuxiliaire auxiliaire = new CAAuxiliaire();
        auxiliaire.setSession(getSession());
        auxiliaire.setIdJournal(journal.getIdJournal());
        auxiliaire.setIdCompteAnnexe(idCompteAnnexe);
        auxiliaire.setIdSection(section.getIdSection());

        auxiliaire.setDate(JACalendar.todayJJsMMsAAAA());

        auxiliaire.setMontant(montant.toStringFormat());

        if (montant.isPositive()) {
            auxiliaire.setCodeDebitCredit(APIEcriture.CREDIT);
        } else {
            auxiliaire.setCodeDebitCredit(APIEcriture.DEBIT);
        }

        auxiliaire.add(transaction);

        if (journal.getEstVisibleImmediatement().booleanValue()) {
            auxiliaire.activer(transaction);
            auxiliaire.update(transaction);
        }
        if ((auxiliaire.getLog() != null) && (auxiliaire.getLog().getHighestMessage() != null)) {
            FWMessage msg = auxiliaire.getLog().getHighestMessage();
            throw new Exception(msg.getMessageText());
        }
    }

    /**
     * Ajout d'une écriture sur une section.
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param montant
     * @param section
     * @param libelle
     * @param referenceRubriqueCompensation
     * @throws Exception
     */
    private void addEcriture(BTransaction transaction, CACompteAnnexe compteAnnexe, CAJournal journal,
            FWCurrency montant, CASection section, String libelle, String referenceRubriqueCompensation,
            CASection sectionCompensation, String sectionCompensationDeSur) throws Exception {
        this.addEcriture(transaction, compteAnnexe, journal, JACalendar.todayJJsMMsAAAA(), montant, section, libelle,
                referenceRubriqueCompensation, sectionCompensation, sectionCompensationDeSur);
    }

    /**
     * Ajout d'une écriture sur la section actuelle. Utile pour soldé une section par exemple (fonction Transfert de
     * comptes).
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param montant
     * @param libelle
     * @throws Exception
     */
    public void addEcriture(BTransaction transaction, CACompteAnnexe compteAnnexe, CAJournal journal,
            FWCurrency montant, String libelle, String referenceRubriqueCompensation) throws Exception {
        boolean useOwnTransaction = false;
        try {
            // Refuser si section pas instanciée
            if (isNew()) {
                throw new Exception(getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
            }
            // Ouvrir une transaction si nécessaire
            if (transaction == null) {
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                useOwnTransaction = true;
            }
            // Si nécessaire création d'un journal d'Ecritures journalières
            // Récupérer le journal des inscriptions journalières
            if (journal == null) {
                journal = CAJournal.fetchJournalJournalier(getSession(), transaction);
            }

            this.addEcriture(transaction, compteAnnexe, journal, montant, this, libelle, referenceRubriqueCompensation,
                    null, null);
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        } finally {
            if (useOwnTransaction) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    JadeLogger.fatal(this, e);
                } finally {
                    try {
                        transaction.closeTransaction();
                        transaction = null;
                    } catch (Exception e) {
                    }
                }
            }
        }

    }

    /**
     * Ajout d'une écriture sur une section.
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param montant
     * @param section
     * @param libelle Optionnel
     * @param referenceRubriqueCompensation
     * @throws Exception
     */
    private void addEcriture(BTransaction transaction, CACompteAnnexe compteAnnexe, CAJournal journal, String date,
            FWCurrency montant, CASection section, String libelle, String referenceRubriqueCompensation,
            CASection sectionCompensation, String sectionCompensationDeSur) throws Exception {
        CAEcriture ecriture = new CAEcriture();
        ecriture.setSession(getSession());
        ecriture.setIdJournal(journal.getIdJournal());
        ecriture.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        ecriture.setIdSection(section.getIdSection());
        if (sectionCompensation != null) {
            ecriture.setIdSectionCompensation(sectionCompensation.getIdSection());
        }
        if (sectionCompensationDeSur != null) {
            ecriture.setSectionCompensationDeSur(sectionCompensationDeSur);
        }

        ecriture.setDate(date);
        ecriture.setMontant(montant.toStringFormat());

        if (montant.isPositive()) {
            ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
        } else {
            ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
        }

        if (!JadeStringUtil.isBlank(libelle)) {
            ecriture.setLibelle(libelle);
        }

        // Chargement de la rubrique de compensation
        if (!(referenceRubriqueCompensation.equals(APIReferenceRubrique.COMPENSATION_LETTRAGE)
                || referenceRubriqueCompensation.equals(APIReferenceRubrique.COMPENSATION_LISSAGE) || referenceRubriqueCompensation
                    .equals(APIReferenceRubrique.COMPENSATION_TRANSFERT_DE_SOLDE))) {
            transaction.addErrors(journal.getSession().getLabel(CASection.LABEL_CODE_REFERENCE_NON_AUTORISE)
                    + referenceRubriqueCompensation);
        }
        if (rubriqueDeCompensation == null) {
            CAReferenceRubrique ref = new CAReferenceRubrique();
            ref.setSession(journal.getSession());
            rubriqueDeCompensation = ref.getRubriqueByCodeReference(referenceRubriqueCompensation);
            if (rubriqueDeCompensation == null) {
                transaction.addErrors(journal.getSession().getLabel(CASection.LABEL_CODE_REFERENCE_NON_ATTRIBUE)
                        + referenceRubriqueCompensation);
            }
        }

        if (rubriqueDeCompensation != null) {
            ecriture.setIdCompte(rubriqueDeCompensation.getIdRubrique());
        }

        ecriture.add(transaction);

        if (journal.getEstVisibleImmediatement().booleanValue()) {
            ecriture.activer(transaction);
            ecriture.update(transaction);
        }

        if ((ecriture.getLog() != null) && (ecriture.getLog().getHighestMessage() != null)) {
            FWMessage msg = ecriture.getLog().getHighestMessage();
            throw new Exception(msg.getMessageText());
        }
    }

    /**
     * Insérer une opération dans la section <br />
     * Date de création : (18.01.2002 13:19:50)
     * 
     * @return FWMessage null s'il n'y a pas d'erreur, sinon un message d'erreur
     * @param oper globaz.osiris.db.comptes.CAOperation l'opération à insérer
     */
    public FWMessage addOperation(CAOperation oper) {
        // Initialiser un nouveau message
        FWMessage msg = null;

        try {
            if (oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE)) {
                // S'il s'agit d'une écriture, on met à jour le solde du compte
                // annexe
                updateAddSoldeCA(oper);

                if (oper.isInstanceOrSubClassOf(APIOperation.CARECOUVREMENT)
                        || oper.isInstanceOrSubClassOf(APIOperation.CAPAIEMENTBVR)) {
                    setAttenteLSVDD(new Boolean(false));
                }
                // Gestion de la caisse professionnelle
                if ((oper.getCompte() != null) && oper.getCompte().isUseCaissesProf()) {
                    if (JadeStringUtil.isIntegerEmpty(getIdCaisseProfessionnelle())
                            && !JadeStringUtil.isIntegerEmpty(oper.getIdCaisseProfessionnelle())) {
                        // Met à jour l'ecriture avec la Section
                        setIdCaisseProfessionnelle(oper.getIdCaisseProfessionnelle());
                    }
                }
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAOPERATIONCONTENTIEUX)) {
                // S'il s'agit d'une opération contentieux, on met à jour la
                // dernière étape
                updateAddLastEtape(oper);
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAOPERATIONCONTENTIEUXAQUILA)) {
                // S'il s'agit d'une opération contentieux Aquila, on met à jour
                // la dernière étape
                // TODO sch Voir comment on met à la dernière étape qui a été
                // effectuée au niveau du contentieux
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE)) {
                // S'il s'agit d'une écriture, on met à jour le solde de la
                // section
                updateAddSoldeSection(oper);
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAOPERATIONORDRERECOUVREMENT)) {
                setAttenteLSVDD(new Boolean(true));
            }
        } catch (Exception e) {
            msg = new FWMessage();

            if (oper.isInstanceOrSubClassOf(APIOperation.CAOPERATIONCONTENTIEUX)) {
                msg.setMessageId("5189");
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE)) {
                msg.setMessageId(CASection.LABEL_OPERATION_AUXILIAIRE_NON_INSEREE);
            } else {
                msg.setMessageId("5131");
            }

            msg.setComplement(e.getMessage());
            msg.setIdSource("CASection");
            msg.setTypeMessage(FWMessage.ERREUR);
        }

        return msg;
    }

    /**
     * Exécution du lettrage automatique. La liste des sections sera retourné en fonction du compte annexe.
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     */
    public void automaticLettrage(BTransaction transaction, CACompteAnnexe compteAnnexe, CAJournal journal,
            String montantMax) {
        boolean useOwnTransaction = false;
        try {
            // Refuser si section pas instanciée
            if (isNew()) {
                throw new Exception(getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
            }
            // Ouvrir une transaction si nécessaire
            if (transaction == null) {
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                useOwnTransaction = true;
            }
            // Si nécessaire création d'un journal d'Ecritures journalières
            // Récupérer le journal des inscriptions journalières
            if (journal == null) {
                journal = CAJournal.fetchJournalJournalier(getSession(), transaction);
            }

            Collection<CASection> list = compteAnnexe.propositionCompensation(APICompteAnnexe.PC_TYPE_MONTANT,
                    APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN, getSolde());
            if (list.isEmpty()) {
                throw new Exception(getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
            }

            doLettrageSectionParDate(transaction, compteAnnexe, journal, list, montantMax);

        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        } finally {
            if (useOwnTransaction) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    JadeLogger.fatal(this, e);
                } finally {
                    try {
                        transaction.closeTransaction();
                        transaction = null;
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * Exécute le lettrage des sections.
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param it
     * @throws Exception
     */
    private void doLettrage(BTransaction transaction, CACompteAnnexe compteAnnexe, CAJournal journal,
            Collection<CASection> sectionList, String montantMax, String date) throws Exception {
        FWCurrency soldeALettrer = new FWCurrency(montantMax);
        soldeALettrer.abs();

        Iterator<CASection> it = sectionList.iterator();

        while (it.hasNext() && (soldeALettrer.compareTo(new FWCurrency(0)) > 0)) {
            CASection sectionALettrer = it.next();

            // Permet de recharger la section pour prendre en compte d'eventuels
            // modifications
            sectionALettrer.retrieve(transaction);

            FWCurrency sectionSolde = sectionALettrer.getSoldeToCurrency();
            sectionSolde.abs();

            // Si la section est a zéro, on a rien a lettrer, on passe a une
            // autre section.
            if (sectionSolde.isZero()) {
                continue;
            }

            if (soldeALettrer.compareTo(sectionSolde) < 1) {
                this.addEcriture(transaction, compteAnnexe, journal, date, soldeALettrer, sectionALettrer, null,
                        APIReferenceRubrique.COMPENSATION_LETTRAGE, this, APIOperation.SECTION_COMPENSATION_DE);
                this.addEcriture(transaction, compteAnnexe, journal, date, getCurrencyNegativeValue(soldeALettrer),
                        this, null, APIReferenceRubrique.COMPENSATION_LETTRAGE, sectionALettrer,
                        APIOperation.SECTION_COMPENSATION_SUR);
                soldeALettrer = new FWCurrency(0);
            } else {
                this.addEcriture(transaction, compteAnnexe, journal, date, sectionSolde, sectionALettrer, null,
                        APIReferenceRubrique.COMPENSATION_LETTRAGE, this, APIOperation.SECTION_COMPENSATION_DE);
                this.addEcriture(transaction, compteAnnexe, journal, date, getCurrencyNegativeValue(sectionSolde),
                        this, null, APIReferenceRubrique.COMPENSATION_LETTRAGE, sectionALettrer,
                        APIOperation.SECTION_COMPENSATION_SUR);
                soldeALettrer.sub(sectionSolde);
            }
        }
    }

    /**
     * Execute le lettrage des sections en fonction des dates
     * 
     * @revision SCO 20 avr. 2010 (mandat inforom 146)
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param it
     * @param montantMax
     * @throws Exception
     */
    private boolean doLettrageSectionParDate(BTransaction transaction, CACompteAnnexe compteAnnexe, CAJournal journal,
            Collection<CASection> sectionList, String montantMax) throws Exception {
        FWCurrency soldeALettrer = new FWCurrency(montantMax);
        soldeALettrer.abs();

        // Parcours des opérations de la section a lettrer.
        CAExtraitCompteManager manager = new CAExtraitCompteManager();
        manager.setSession(getSession());
        manager.setForIdSection(getIdSection());
        manager.setOrder(CAExtraitCompteManager.ORDER_BY_DATE_VALEUR);
        manager.init(transaction);
        manager.fillSelectVariables();
        manager.executeQuery(transaction);

        FWCurrency soldeSectionCumule = new FWCurrency();

        boolean atLeastOneLettrage = false; // il se peut qu'aucun lettrage ne
        // soit effectué...
        // dans certain processus, comme le lettrage de masse (inforom160),
        // la suite du traitement dépend du fait qu'un lettre soit
        // "effectivement fait"
        // dans le but de sortir un bulletin de solde par exemple.

        // On recherche le dernier solde a 0 pour commencer a partir de la (Si
        // il y a déjà eu passage par 0 sinon, fonctionnement normal.
        int depart = 0;

        for (int i = 0; i < manager.size(); i++) {

            CAExtraitCompte extraitCompte = (CAExtraitCompte) manager.getEntity(i);
            soldeSectionCumule.add(extraitCompte.getSum());
            if (soldeSectionCumule.isZero() && ((i + 1) < manager.size())) {
                depart = i + 1;
            }
        }

        soldeSectionCumule = new FWCurrency();

        // Pour chaque opération de la section a lettrer.
        for (int i = depart; i < manager.size(); i++) {

            CAExtraitCompte extraitCompte = (CAExtraitCompte) manager.getEntity(i);
            soldeSectionCumule.add(extraitCompte.getSum());

            if (soldeALettrer.isPositive() && soldeSectionCumule.isNegative()) {

                // On regarde l'opération suivante si on peut lettrer ou pas
                FWCurrency soldeSectionCumuleAbs = new FWCurrency(soldeSectionCumule.doubleValue());
                soldeSectionCumuleAbs.abs();

                if ((i + 1) < manager.size()) {
                    CAExtraitCompte prochainExtraitCompte = (CAExtraitCompte) manager.getEntity(i + 1);
                    FWCurrency montantProchainExtraitCompte = new FWCurrency(prochainExtraitCompte.getSum());

                    if (montantProchainExtraitCompte.isPositive()
                            && (montantProchainExtraitCompte.compareTo(soldeSectionCumuleAbs) < 0)) {

                        FWCurrency montantALettrer = new FWCurrency();
                        montantALettrer.add(soldeSectionCumuleAbs);
                        montantALettrer.sub(montantProchainExtraitCompte);

                        soldeALettrer = lettrer(transaction, compteAnnexe, journal, sectionList, montantALettrer,
                                soldeALettrer, extraitCompte.getDate());
                        atLeastOneLettrage = true;

                    } else if (montantProchainExtraitCompte.isNegative()) {

                        soldeALettrer = lettrer(transaction, compteAnnexe, journal, sectionList, soldeSectionCumuleAbs,
                                soldeALettrer, extraitCompte.getDate());
                        atLeastOneLettrage = true;
                    }
                } else {
                    soldeALettrer = lettrer(transaction, compteAnnexe, journal, sectionList, soldeSectionCumuleAbs,
                            soldeALettrer, extraitCompte.getDate());
                    atLeastOneLettrage = true;
                }
            }

            // Il n'y a plus rien a lettrer on sort de la boucle.
            if (soldeALettrer.isZero() || soldeALettrer.isNegative()) {
                break;
            }
        }
        return atLeastOneLettrage;
    }

    /**
     * Duplique l'écriture passée sur la section d'un compte annexe vers la section auxiliaire liée.
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param ecriture
     * @return IdOperation de l'écriture auxiliaire créée.
     * @throws Exception
     */
    public String duplicateEcritureToAuxiliaire(BTransaction transaction, CACompteAnnexe compteAnnexe,
            CAJournal journal, CAEcriture ecriture) throws Exception {
        String newIdOperation = "";

        boolean useOwnTransaction = false;
        try {
            // Refuser si section pas instanciée
            if (isNew()) {
                throw new Exception(getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
            }
            // Ouvrir une transaction si nécessaire
            if (transaction == null) {
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                useOwnTransaction = true;
            }
            // Si nécessaire création d'un journal d'Ecritures journalières
            // Récupérer le journal des inscriptions journalières
            if (journal == null) {
                journal = CAJournal.fetchJournalJournalier(getSession(), transaction);
            }

            if (ecriture.getIdTypeOperation().startsWith(APIOperation.CAPAIEMENT)) {
                newIdOperation = this.addAuxiliaire(transaction, compteAnnexe, journal, this,
                        new CAAuxiliairePaiement(), ecriture);
            } else {
                newIdOperation = this.addAuxiliaire(transaction, compteAnnexe, journal, this, new CAAuxiliaire(),
                        ecriture);
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        } finally {
            if (useOwnTransaction) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    JadeLogger.fatal(this, e);
                } finally {
                    try {
                        transaction.closeTransaction();
                        transaction = null;
                    } catch (Exception e) {
                    }
                }
            }
        }

        return newIdOperation;
    }

    /**
     * @param transaction
     * @param journal
     * @param text
     */
    public void extournerEcritures(BTransaction transaction, CAJournal journal, String text) {
        boolean useOwnTransaction = false;
        try {
            // Refuser si section pas instanciée
            if (isNew()) {
                throw new Exception(getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
            }
            // Ouvrir une transaction si nécessaire
            if (transaction == null) {
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                useOwnTransaction = true;
            }

            // Extourne les opérations
            extournerOperations(transaction, journal, text);

        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        } finally {
            if (useOwnTransaction) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    JadeLogger.error(this, e);
                } finally {
                    try {
                        transaction.closeTransaction();
                        transaction = null;
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * Extourne les opérations
     * 
     * @param transaction
     * @param journal
     * @param text
     * @throws Exception
     */
    private void extournerOperations(BTransaction transaction, CAJournal journal, String text) throws Exception {
        // Récupérer les opérations
        CAEcritureManager mgr = new CAEcritureManager();
        mgr.setSession(getSession());
        mgr.setForIdSection(getIdSection());
        mgr.find(transaction);

        for (int i = 0; i < mgr.size(); i++) {
            // Récupérer l'opération
            CAOperation op = (CAOperation) mgr.getEntity(i);
            // Si l'opération est active et extournable
            if (op.getEstActive().booleanValue()) {
                op = op.getOperationFromType(transaction);
                if (!transaction.hasErrors() && op.isOperationExtournable()) {
                    // L'extourner
                    op.extourner(transaction, journal, text);
                    // Sortir s'il y a des erreurs
                    if (transaction.hasErrors()) {
                        _addError(transaction, op.toString());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String getAmende() {
        return amende;
    }

    /**
     * Indique s'il y a des ordres de recouvrement(LSV/DD) en attente pour cette section
     * 
     * @return attenteLSVDD
     */
    public Boolean getAttenteLSVDD() {
        return attenteLSVDD;
    }

    @Override
    public String getBase() {
        return JANumberFormatter.deQuote(base);
    }

    /**
     * Date de création : (07.01.2002 10:10:20)
     * 
     * @return String
     */
    public String getBaseFormate() {
        return globaz.globall.util.JANumberFormatter.formatNoRound(getBase(), 2);
    }

    @Override
    public String getCategorieSection() {
        return categorieSection;
    }

    /**
     * Date de création : (21.01.2002 11:17:22)
     * 
     * @return String
     */
    @Override
    public APICompteAnnexe getCompteAnnexe() {
        if ((compteAnnexe == null) || !compteAnnexe.getIdCompteAnnexe().equals(getIdCompteAnnexe())) {
            compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setISession(getSession());
            compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
            try {
                compteAnnexe.retrieve();
                if (compteAnnexe.isNew()) {
                    compteAnnexe = null;
                }
            } catch (Exception e) {
                compteAnnexe = null;
            }
        }

        if (compteAnnexe == null) {
            _addError(null, getSession().getLabel("7096") + getIdCompteAnnexe());
        }

        return compteAnnexe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APISection#getContentieuxEstSuspendu()
     */
    @Override
    public Boolean getContentieuxEstSuspendu() {
        return contentieuxEstSuspendu;
    }

    /**
     * Date de création : (17.01.2002 13:02:45)
     * 
     * @return String
     */
    public FWParametersSystemCode getCsMotifContentieuxSuspendu() {
        if (csMotifContentieuxSuspendu == null) {
            // liste pas encore chargee, on la charge
            csMotifContentieuxSuspendu = new FWParametersSystemCode();
            csMotifContentieuxSuspendu.getCode(getIdMotifContentieuxSuspendu());
        }
        return csMotifContentieuxSuspendu;
    }

    /**
     * Date de création : (17.01.2002 13:05:28)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsMotifContentieuxSuspendus() {
        // liste déjà chargée ?
        if (csMotifContentieuxSuspendus == null) {
            // liste pas encore chargée, on la charge
            csMotifContentieuxSuspendus = new FWParametersSystemCodeManager();
            csMotifContentieuxSuspendus.setSession(getSession());
            csMotifContentieuxSuspendus.getListeCodesSup("OSIMOTCTX", getSession().getIdLangue());
        }
        return csMotifContentieuxSuspendus;
    }

    /**
     * Retourne la valeur négative dune devise.
     * 
     * @param sourceValue
     * @return
     */
    private FWCurrency getCurrencyNegativeValue(FWCurrency sourceValue) {
        if (sourceValue.isPositive()) {
            return new FWCurrency(sourceValue.getBigDecimalValue().multiply(new BigDecimal("-1")).toString());
        } else {
            return sourceValue;
        }
    }

    @Override
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    @Override
    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * Date de création : (04.06.2002 14:19:57)
     * 
     * @return String
     */
    @Override
    public String getDateReferenceContentieux(APIParametreEtape etape) {
        return null;
    }

    @Override
    public String getDateSection() {
        return dateSection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APISection#getDateSuspendu()
     */
    @Override
    public String getDateSuspendu() {
        return dateSuspendu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APISection#getDescription()
     */
    @Override
    public String getDescription() {
        return getSectionDescriptor().getDescription();
    }

    /**
     * Date de création : (11.03.2002 11:12:24)
     * 
     * @return String
     * @param codeISOLangue String
     */

    public String getDescription(String codeISOLangue) {
        return getSectionDescriptor().getDescription(codeISOLangue);
    }

    @Override
    public String getDomaine() {
        return domaine;
    }

    /**
     * Cette méthode permet de récupérer le premier identifiant de l'événement contentieux non exécuté et modifié
     * manuellement.
     * 
     * @return String idEveCon
     * @throws Exception
     */
    public String getFirstIdEvContentieuxNonExecute() throws Exception {
        if (firstEvContentieuxNonExec == null) {
            CAEvenementContentieuxSectionParamEtapeManager mgr = new CAEvenementContentieuxSectionParamEtapeManager();
            mgr.setSession(getSession());
            mgr.setForIdSection(getIdSection());
            mgr.setForEstModifie(new Boolean(true));
            mgr.setForEstDeclenche(new Boolean(false));
            mgr.setForEstIgnoree(new Boolean(false));
            mgr.find();
            if (!mgr.isEmpty()) {
                firstEvContentieuxNonExec = (CAEvenementContentieux) mgr.getFirstEntity();
            } else {
                return null;
            }
        }
        return firstEvContentieuxNonExec.getIdEvenementContentieux();
    }

    /**
     * Cette méthode permet de récupérer le premier identifiant de l'événement contentieux non exécuté et modifié
     * manuellement qui est ignoré.
     * 
     * @return String idEveCon
     * @throws Exception
     */
    public String getFirstIdEvContentieuxNonExecuteIgnore() throws Exception {
        if (firstEvContentieuxNonExecIgnore == null) {
            CAEvenementContentieuxSectionParamEtapeManager mgr = new CAEvenementContentieuxSectionParamEtapeManager();
            mgr.setSession(getSession());
            mgr.setForIdSection(getIdSection());
            mgr.setForEstModifie(new Boolean(true));
            mgr.setForEstDeclenche(new Boolean(false));
            mgr.setForEstIgnoree(new Boolean(true));
            mgr.find();
            if (!mgr.isEmpty()) {
                firstEvContentieuxNonExecIgnore = (CAEvenementContentieux) mgr.getFirstEntity();
            } else {
                return null;
            }
        }
        return firstEvContentieuxNonExecIgnore.getIdEvenementContentieux();
    }

    /**
     * Cette méthode retourne le montant des frais de la section
     * 
     * @return String frais
     */
    @Override
    public String getFrais() {
        return frais;
    }

    /**
     * Retourne la description complète de la section <br />
     * Date de création : (11.03.2002 13:39:01)
     * 
     * @return String
     */
    public String getFullDescription() {
        String s = getIdExterne() + " - " + this.getDescription();
        return s;
    }

    /**
     * Retourne la description complète de la section <br />
     * Date de création : (11.03.2002 13:39:01)
     * 
     * @param String codeIsoLangue
     * @return String
     */
    public String getFullDescription(String codeIsoLangue) {
        String s = getIdExterne() + " - " + this.getDescription(codeIsoLangue);
        return s;
    }

    /**
     * @return the idCaissePro
     */
    public String getIdCaisseProfessionnelle() {
        return idCaissePro;
    }

    @Override
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    @Override
    public String getIdExterne() {
        return idExterne;
    }

    @Override
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Cette méthode retourne l'identifiant du dernier événement contentieux effectué
     * 
     * @return String identifiant du dernier événement contentieux effectué
     */
    public String getIdLastEtapeCtx() {
        return idLastEtapeCtx;
    }

    /**
     * Cette méthode retourne le dernier état de la section dans le contentieux Aquila (Code système)
     * 
     * @return String idLastEtatAquila
     */
    @Override
    public String getIdLastEtatAquila() {
        return idLastEtatAquila;
    }

    /**
     * Cette méthode retourne le mode de compensation de la section (code Système)
     * 
     * @return String idModeCompensation
     */
    @Override
    public String getIdModeCompensation() {
        if (JadeStringUtil.isBlank(idModeCompensation)) {
            return APISection.MODE_COMPENSATION_STANDARD;
        } else {
            return idModeCompensation;
        }
    }

    @Override
    public String getIdMotifContentieuxSuspendu() {
        return idMotifContentieuxSuspendu;
    }

    /**
     * Cette méthode retourne l'identifiant du passage dans lequel le report de la section est pris en compte
     * 
     * @return String idPassageComp
     */
    @Override
    public String getIdPassageComp() {
        return idPassageComp;
    }

    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    public String getIdPosteJournalisation() {
        return idPosteJournalisation;
    }

    @Override
    public String getIdRemarque() {
        return idRemarque;
    }

    /**
     * Getter
     */
    @Override
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return
     */
    public String getIdSectionPrincipal() {
        return idSectionPrincipal;
    }

    public String getIdSequenceContentieux() {
        return idSequenceContentieux;
    }

    @Override
    public String getIdTypeSection() {
        return idTypeSection;
    }

    /**
     * Date de création : (21.02.2002 17:30:52)
     * 
     * @return String
     */
    @Override
    public String getInterets() {
        return interets;
    }

    /**
     * Date de création : (01.02.2002 11:50:43)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public CAJournal getJournal() {
        if (journal == null) {
            journal = new CAJournal();
            journal.setISession(getSession());
            journal.setIdJournal(getIdJournal());
            try {
                journal.retrieve();
                if (journal.isNew()) {
                    journal = null;
                }
            } catch (Exception e) {
                journal = null;
            }
        }
        return journal;
    }

    /**
     * Récupération de la dernière étape de contentieux Aquila
     * 
     * @param sb
     */
    private String getLastEtapeAquila() {
        String etape = "";
        try {
            if (!JadeStringUtil.isIntegerEmpty(getIdLastEtatAquila())) {

                // BZ 5156
                // etape = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                // CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionAquila(this.getSession()).getCodeLibelle(
                // this.getIdLastEtatAquila());
                etape = getSession().getCodeLibelle(getIdLastEtatAquila());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return etape;
    }

    /**
     * Date de création : (04.07.2002 11:54:10)
     * 
     * @return globaz.osiris.db.contentieux.CAEvenementContentieux
     */
    public CAEvenementContentieux getLastEvenementContentieux() {
        if (!JadeStringUtil.isIntegerEmpty(getIdLastEtapeCtx())) {
            if (lastEvenementContentieux == null) {
                lastEvenementContentieux = new CAEvenementContentieux();
                lastEvenementContentieux.setSession(getSession());
                lastEvenementContentieux.setIdEvenementContentieux(getIdLastEtapeCtx());
                try {
                    lastEvenementContentieux.retrieve();
                    if (lastEvenementContentieux.isNew() || lastEvenementContentieux.hasErrors()) {
                        _addError(null, getSession().getLabel("7098"));
                        lastEvenementContentieux = null;
                    }
                } catch (Exception e) {
                    lastEvenementContentieux = null;
                    _addError(null, e.getMessage());
                }
            }
        }
        return lastEvenementContentieux;
    }

    /**
     * @return the nonImprimable
     */
    public Boolean getNonImprimable() {
        return nonImprimable;
    }

    /**
     * Date de création : (21.06.2002 13:36:31)
     * 
     * @return String
     */
    @Override
    public String getNumeroPoursuite() {
        return numeroPoursuite;
    }

    /**
     * Retourne l'id et le libellé du plan <br />
     * Date de création : (22.01.2002 16:03:24)
     * 
     * @return String
     */
    public String getPlan() {
        String planStr = "";

        if ((getPlanRecouvrement() != null) && !JadeStringUtil.isBlankOrZero(getIdPlanRecouvrement())) {
            planStr = getPlanRecouvrement().getId() + " - " + getPlanRecouvrement().getLibelle();
        }

        return planStr;
    }

    /**
     * Getter de l'objet CAPlanRecouvrement relatif a la section
     * 
     * @return
     */
    public CAPlanRecouvrement getPlanRecouvrement() {

        if (planRecouvrement == null) {
            try {
                retrievePlanRecouvrement();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }

        return planRecouvrement;
    }

    @Override
    public String getPmtCmp() {
        return JANumberFormatter.deQuote(pmtCmp);
    }

    /**
     * Date de création : (07.01.2002 10:30:49)
     * 
     * @return String
     */
    public String getPmtCmpFormate() {
        return globaz.globall.util.JANumberFormatter.formatNoRound(getPmtCmp(), 2);
    }

    /**
     * @return the referenceBvr
     */
    public String getReferenceBvr() {
        return referenceBvr;
    }

    /**
     * Date de création : (21.01.2002 11:33:15)
     * 
     * @return globaz.osiris.db.utils.CARemarque
     */
    public CARemarque getRemarque() {
        // Instancier une nouvelle remarque
        remarque = new CARemarque();
        remarque.setSession(getSession());

        // Récupérer la remarque en question
        remarque.setIdRemarque(getIdRemarque());
        try {
            remarque.retrieve();
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return null;
        }
        // Retourner la remarque
        return remarque;
    }

    // Bug 5562
    public String getRemarqueEcran() {
        if (!JadeStringUtil.isBlank(getTexteRemarque())
                && !getTexteRemarque().equalsIgnoreCase(getRemarque().getTexte())) {
            return getTexteRemarque();
        } else {
            return getRemarque().getTexte();
        }
    }

    /**
     * Date de création : (15.01.2002 07:30:21)
     * 
     * @return String
     */
    public String getResumeContentieux() {
        StringBuffer sb = new StringBuffer();
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            getResumeContentieuxOsiris(sb);
        } else {
            getResumeContentieuxAquila(sb);
        }
        return sb.toString();
    }

    /**
     * @param sb
     */
    private void getResumeContentieuxAquila(StringBuffer sb) {
        // Si contentieux suspendu
        try {
            if (getContentieuxEstSuspendu().booleanValue()) {
                CAMotifContentieuxManager motMgr = new CAMotifContentieuxManager();
                motMgr.setSession(getSession());
                motMgr.setForIdSection(getIdSection());
                motMgr.setFromDateBetweenDebutFin(JACalendar.todayJJsMMsAAAA());
                motMgr.find();
                if (!motMgr.isEmpty()) {
                    CAMotifContentieux mot = (CAMotifContentieux) motMgr.getFirstEntity();
                    sb.append(mot.getUcMotifContentieuxSuspendu().getLibelle() + "->" + mot.getDateFin());
                } else {
                    sb.append(getLastEtapeAquila());
                }
            } else if (!JadeStringUtil.isBlankOrZero(getStatutBN())
                    && !APISection.STATUTBN_REACTIVE.equals(getStatutBN())) {
                sb.append(getSession().getCodeLibelle(getStatutBN()));
            } else {
                sb.append(getLastEtapeAquila());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * @param sb
     */
    private void getResumeContentieuxOsiris(StringBuffer sb) {
        // Si contentieux suspendu
        try {
            if (getContentieuxEstSuspendu().booleanValue()
                    && BSessionUtil.compareDateFirstGreater(getSession(), getDateSuspendu(),
                            JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY))) {
                sb.append(getUcMotifContentieuxSuspendu().getLibelle());
                if (!JAUtil.isDateEmpty(getDateSuspendu())) {
                    sb.append(" -> " + getDateSuspendu());
                }
                // S'il y a une étape en cours
            } else if (!JadeStringUtil.isIntegerEmpty(getIdLastEtapeCtx())) {
                if (getLastEvenementContentieux() != null) {
                    try {
                        sb.append(getLastEvenementContentieux().getParametreEtape().getEtape().getDescription());
                    } catch (NullPointerException e) {
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * Date de création : (11.03.2002 11:06:46)
     * 
     * @return globaz.osiris.interfaceext.comptes.APISectionDescriptor
     */
    protected APISectionDescriptor getSectionDescriptor() {
        if ((sectionDescriptor == null) || !sectionDescriptor.getIdSection().equals(getIdSection())) {
            try {
                // Récupérer le descripteur selon le type de section
                if (getTypeSection() == null) {
                    throw new Exception("Error : Type section not found !");
                }
                Class cl = Class.forName(getTypeSection().getNomClasse());
                sectionDescriptor = (APISectionDescriptor) cl.newInstance();

                // Passer la section
                sectionDescriptor.setISession(getSession());
                sectionDescriptor.setSection(this);
            } catch (Exception e) {
                _addError(null, e.toString());
                sectionDescriptor = null;
            }
        }
        // Retourne le descripteur
        return sectionDescriptor;

    }

    /**
     * Date de création : (21.01.2002 11:33:15)
     * 
     * @return globaz.osiris.db.utils.CARemarque
     */
    public CASequenceContentieux getSequenceContentieux() {
        // Si sequenceContentieux pas déjà instanciée
        if (sequenceContentieux == null) {
            // Instancier une nouvelle séquence contentieux
            sequenceContentieux = new CASequenceContentieux();
            sequenceContentieux.setSession(getSession());

            // Récupérer la sequenceContentieux en question
            sequenceContentieux.setIdSequenceContentieux(getIdSequenceContentieux());
            try {
                sequenceContentieux.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return null;
            }
        }
        // Retourner la sequenceContentieux
        return sequenceContentieux;
    }

    @Override
    public String getSolde() {
        return JANumberFormatter.deQuote(solde);
    }

    /**
     * Date de création : (07.01.2002 10:10:20)
     * 
     * @return String
     */
    @Override
    public String getSoldeFormate() {
        return globaz.globall.util.JANumberFormatter.formatNoRound(getSolde(), 2);
    }

    /**
     * Cette méthode retourne le solde de la section à une date donnée.
     * 
     * @param untilDate
     * @return String solde de la section ou null en cas de problème
     * @throws Exception
     */
    public String getSoldeSectionUntilDate(String untilDate) throws Exception {
        CASoldeSectionUntilDateManager manager = new CASoldeSectionUntilDateManager();
        manager.setSession(getSession());
        manager.setUntilDate(untilDate);
        manager.setForIdSection(getIdSection());
        manager.setForEtat(APIOperation.ETAT_COMPTABILISE);
        manager.find();
        if (!manager.hasErrors() && !manager.isEmpty()) {
            return ((CASoldeSectionUntilDate) manager.getFirstEntity()).getSoldeSection();
        } else {
            return null;
        }
    }

    /**
     * Date de création : (16.08.2002 15:53:47)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getSoldeToCurrency() {
        return new FWCurrency(getSolde());
    }

    /**
     * @return the statutBN
     */
    public String getStatutBN() {
        return statutBN;
    }

    /**
     * Date de création : (21.02.2002 17:30:28)
     * 
     * @return String
     */
    @Override
    public String getTaxes() {
        return taxes;
    }

    /**
     * Date de création : (22.01.2002 08:16:23)
     * 
     * @return String
     */
    @Override
    public String getTexteRemarque() {
        return texteRemarque;
    }

    @Override
    public String getTypeAdresse() {
        return typeAdresse;
    }

    /**
     * Date de création : (17.01.2002 12:00:16)
     * 
     * @return String le type de section ou null si pas trouvé.
     */
    @Override
    public APITypeSection getTypeSection() {
        typeSection = new CATypeSection();
        typeSection.setISession(getSession());
        typeSection.setIdTypeSection(getIdTypeSection());
        try {
            typeSection.retrieve();
            if (typeSection.isNew()) {
                typeSection = null;
            }
        } catch (Exception e) {
            typeSection = null;
        }

        return typeSection;
    }

    /**
     * Date de création : (17.01.2002 13:47:48)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    public FWParametersUserCode getUcMotifContentieuxSuspendu() {
        if (ucMotifContentieuxSuspendu == null) {
            // liste pas encore chargee, on la charge
            ucMotifContentieuxSuspendu = new FWParametersUserCode();
            ucMotifContentieuxSuspendu.setSession(getSession());
        }

        if (!JadeStringUtil.isIntegerEmpty(getIdMotifContentieuxSuspendu())) {
            // Récupérer le code système dans la langue de l'utilisateur
            ucMotifContentieuxSuspendu.setIdCodeSysteme(getIdMotifContentieuxSuspendu());
            ucMotifContentieuxSuspendu.setIdLangue(getSession().getIdLangue());
            try {
                ucMotifContentieuxSuspendu.retrieve();
                if (ucMotifContentieuxSuspendu.isNew() || ucMotifContentieuxSuspendu.hasErrors()) {
                    _addError(null, getSession().getLabel("7324"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7324"));
            }
        }

        return ucMotifContentieuxSuspendu;
    }

    public String getYear() throws Exception {
        return getIdExterne().substring(0, CASection.YEAR_LENGTH);
    }

    /**
     * Retourne vrai si EvenementContentieux contient des données <br />
     * Date de création : (27.05.2002 08:16:14)
     * 
     * @return boolean vrai si EvenementContentieux contient des données
     */
    public boolean hasEvenementContentieux() {
        boolean retour = false;

        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            // Charger un manager
            CAEvenementContentieuxManager mgr = new CAEvenementContentieuxManager();
            mgr.setSession(getSession());
            mgr.setForIdSection(getIdSection());

            // Compter le nombre de correspondances
            try {
                if (mgr.getCount() != 0) {
                    retour = true;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        } else {
            // Si contentieux Aquila
            if (!JadeStringUtil.isIntegerEmpty(getIdLastEtatAquila())) {
                retour = true;
            }
        }
        return retour;
    }

    /**
     * @param idModifBlocage
     * @return
     * @throws CATechnicalException
     */
    public boolean hasMotifContentieux(String idModifBlocage) throws CATechnicalException {
        boolean contentieux = false;

        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            contentieux = CAMotifContentieuxUtil
                    .hasMotifContentieux(getSession(), null, getIdSection(), idModifBlocage);
        } else {
            String a = "" + getCsMotifContentieuxSuspendu().getIdCode();
            if (a.equalsIgnoreCase(idModifBlocage)) {
                contentieux = true;
            }
        }
        return contentieux;
    }

    /**
     * @throws CATechnicalException
     * @see APISection.hasMotifContentieuxForYear(String idModifBlocage, String year)
     */
    @Override
    public boolean hasMotifContentieuxForYear(String idModifBlocage, String year) throws CATechnicalException {
        return CAMotifContentieuxUtil.hasMotifContentieuxForYear(getSession(), null, getIdSection(), idModifBlocage,
                year);
    }

    /**
     * Retourne vrai si la section contient des opérations Date de création : (22.03.2002 09:49:14)
     * 
     * @return boolean vrai si la section contient des opérations
     */
    @Override
    public boolean hasOperations() {
        // Charger un manager
        CAOperationManager mgr = new CAOperationManager();
        mgr.setSession(getSession());
        mgr.setForIdSection(getIdSection());

        // Compter le nombre de correspondances
        try {
            if (mgr.getCount() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APISection#isSectionAuContentieux()
     */
    @Override
    public boolean isSectionAuContentieux() {
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            // Si contentieux Osiris
            if (!JadeStringUtil.isIntegerEmpty(getIdLastEtapeCtx())) {
                return true;
            }
        } else {
            // Si contentieux Aquila
            if (!JadeStringUtil.isIntegerEmpty(getIdLastEtatAquila())
                    && !ICOEtape.CS_AUCUNE.equals(getIdLastEtatAquila())) {
                return true;
            }
        }
        return false;
    }

    /**
     * La section est-elle au poursuite ? <br/>
     * Si oui les intérêts moratoires ne doivent pas être facturé car l'office de poursuite s'en charge => état exempté.<br/>
     * Ne tient pas compte si la poursuite est radiée.
     * 
     * @param boolean soldeOuvert, true pour ne prendre que les sections qui ont un solde > 0. False, prend toutes les
     *        sections indépendamment du solde
     * @return true si la section est aux poursuites (RP et ss)
     */
    public boolean isSectionAuxPoursuites(boolean soldeOuvert) {
        boolean isSectionAvecSectionsContentieux = false;

        // ancien contentieux
        if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            CASectionAvecSectionsPoursuiteManager manager = new CASectionAvecSectionsPoursuiteManager();
            manager.setSession(getSession());
            manager.setForIdSection(getIdSection());
            manager.setTypeEtapeIn(APIEtape.ETAPE_POURSUITE_FORMAT);
            if (soldeOuvert) {
                manager.setForSelectionSections("3");
            }
            try {
                manager.changeManagerSize(BManager.SIZE_NOLIMIT);
                manager.find();
                if (!manager.isEmpty()) {
                    if (manager.size() > 0) {
                        isSectionAvecSectionsContentieux = true;
                    }
                }
            } catch (Exception e) {
                return isSectionAvecSectionsContentieux;
            }
        } else {
            // contentieux Aquila
            CASectionAuxPoursuitesManager manager = new CASectionAuxPoursuitesManager();
            manager.setSession(getSession());
            manager.setForIdSection(getIdSection());
            manager.setOnlySoldeOuvert(soldeOuvert);
            try {
                manager.changeManagerSize(BManager.SIZE_NOLIMIT);
                if (manager.getCount() > 0) {
                    isSectionAvecSectionsContentieux = true;
                }
            } catch (Exception e) {
                return isSectionAvecSectionsContentieux;
            }
        }

        return isSectionAvecSectionsContentieux;
    }

    /**
     * @param soldeOuvert (Boolean) : <br />
     *            <code>true</code> pour ne prendre que les sections qui ont un solde > 0. <br/>
     *            <code>false</code> prend toutes les sections indépendamment du solde.
     * @return <code>true</code> si la section est aux poursuites (RP et ss) et n'est pas radiée.
     */
    @Override
    public Boolean isSectionAuxPoursuitesNotRadiee(Boolean soldeOuvert) {
        Boolean isSectionAvecSectionsContentieux = false;

        // contentieux Aquila
        CASectionAuxPoursuitesNotRadieeManager manager = new CASectionAuxPoursuitesNotRadieeManager();
        manager.setSession(getSession());
        manager.setForIdSection(getIdSection());
        manager.setOnlySoldeOuvert(soldeOuvert);

        try {
            manager.changeManagerSize(BManager.SIZE_NOLIMIT);
            if (manager.getCount() > 0) {
                isSectionAvecSectionsContentieux = true;
            }
        } catch (Exception e) {
            return isSectionAvecSectionsContentieux;
        }

        return isSectionAvecSectionsContentieux;
    }

    /**
     * 
     * @param transaction
     * @param compteAnnexe
     * @param journal
     * @param sectionList
     * @param montantMax
     * @param date
     * @return
     * @throws Exception
     */
    private FWCurrency lettrer(BTransaction transaction, CACompteAnnexe compteAnnexe, CAJournal journal,
            Collection<CASection> sectionList, FWCurrency montant, FWCurrency soldeALettrer, String date)
            throws Exception {

        if (montant.compareTo(soldeALettrer) < 0) {
            doLettrage(transaction, compteAnnexe, journal, sectionList, montant.toString(), date);
            soldeALettrer.sub(montant);
        } else {
            doLettrage(transaction, compteAnnexe, journal, sectionList, soldeALettrer.toString(), date);
            soldeALettrer = new FWCurrency(0);
        }

        return soldeALettrer;
    }

    /**
     * Exécution du lettrage sur une section.
     * 
     * @param transaction
     * @param section
     * @param journal
     */
    public boolean manualLettrage(BTransaction transaction, CACompteAnnexe compteAnnexe, CASection section,
            CAJournal journal, String montantMaxALettrer) {
        boolean useOwnTransaction = false;
        boolean atLeastOneLettrage = false;
        try {
            // Refuser si section pas instanciée
            if (isNew()) {
                throw new Exception(getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
            }
            if (!section.getSoldeToCurrency().isPositive()) {
                throw new Exception(getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
            }
            if (APISection.MODE_BLOQUER_COMPENSATION.equals(section.getIdModeCompensation())) {
                throw new Exception(FWMessageFormat.format(getSession().getLabel("SECTION_MODE_COMPENSATION_BLOQUE"),
                        section.getIdExterne(), section.getCompteAnnexe().getIdExterneRole()));
            }

            // Ouvrir une transaction si nécessaire
            if (transaction == null) {
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                useOwnTransaction = true;
            }

            // Si nécessaire création d'un journal d'Ecritures journalières
            // Récupérer le journal des inscriptions journalières
            if (journal == null) {
                journal = CAJournal.fetchJournalJournalier(getSession(), transaction);
            }

            Collection<CASection> manualSection = new ArrayList<CASection>();
            manualSection.add(section);

            atLeastOneLettrage = doLettrageSectionParDate(transaction, compteAnnexe, journal, manualSection,
                    montantMaxALettrer);

        } catch (Exception e) {
            _addError(transaction, e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        } finally {
            if (useOwnTransaction) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }

                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    JadeLogger.fatal(this, e);
                } finally {
                    try {
                        transaction.closeTransaction();
                        transaction = null;
                    } catch (Exception e) {
                    }
                }
            }
        }
        return atLeastOneLettrage;
    }

    /**
     * Supprimer une opération de la section <br />
     * Date de création : (18.01.2002 13:19:50)
     * 
     * @return FWMessage null s'il n'y a pas d'erreur, sinon un message d'erreur
     * @param oper globaz.osiris.db.comptes.CAOperation l'opération à supprimer
     */
    public FWMessage removeOperation(CAOperation oper) {
        // Initialiser un nouveau message
        FWMessage msg = null;

        try {
            if (oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE)) {
                // S'il s'agit d'une écriture, on met à jour le solde du compte
                // annexe
                updateRemoveSoldeCA(oper);
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAOPERATIONCONTENTIEUX)) {
                // S'il s'agit d'une opération contentieux, on met à jour la
                // dernière étape
                updateRemoveLastEtape(oper);
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAOPERATIONCONTENTIEUXAQUILA)) {
                // TODO sch Revoir comment on met à jour lors de la suppresion
                // d'une opération de contentieux
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE)) {
                // S'il s'agit d'une écriture, on met à jour le solde de la
                // section
                updateRemoveSoldeSection(oper);
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CAOPERATIONORDRERECOUVREMENT)) {
                if (getAttenteLSVDD().booleanValue()) {
                    setAttenteLSVDD(new Boolean(false));
                }
            } else if (oper.isInstanceOrSubClassOf(APIOperation.CARECOUVREMENT)) {
                if (getAttenteLSVDD().booleanValue()) {
                    setAttenteLSVDD(new Boolean(false));
                }
            }
        } catch (Exception e) {
            msg = new FWMessage();
            if (oper.isInstanceOrSubClassOf(APIOperation.CAOPERATIONCONTENTIEUX)) {
                msg.setMessageId("5189");
            } else {
                msg.setMessageId("5168");
            }
            msg.setComplement(e.getMessage());
            msg.setIdSource("CASection");
            msg.setTypeMessage(FWMessage.ERREUR);
        }
        return msg;
    }

    /**
     * Récupération du plan de paiement suivant l'id du plan de paiement
     * 
     * @return
     */
    private CAPlanRecouvrement retrievePlanRecouvrement() throws Exception {

        if ((planRecouvrement == null) && !JadeStringUtil.isBlankOrZero(getIdPlanRecouvrement())) {
            planRecouvrement = new CAPlanRecouvrement();
            planRecouvrement.setSession(getSession());
            planRecouvrement.setIdPlanRecouvrement(getIdPlanRecouvrement());
            planRecouvrement.retrieve();
        }

        return planRecouvrement;
    }

    public void setAmende(String amende) {
        this.amende = amende;
    }

    public void setAttenteLSVDD(Boolean attenteLSVDD) {
        this.attenteLSVDD = attenteLSVDD;
    }

    public void setBase(String newBase) {
        base = newBase;
    }

    public void setCategorieSection(String newCategorieSection) {
        categorieSection = newCategorieSection;
    }

    public void setContentieuxEstSuspendu(Boolean newContentieuxEstSuspendu) {
        contentieuxEstSuspendu = newContentieuxEstSuspendu;
    }

    public void setDateDebutPeriode(String newDateDebutPeriode) {
        dateDebutPeriode = newDateDebutPeriode;
    }

    public void setDateEcheance(String newDateEcheance) {
        dateEcheance = newDateEcheance;
    }

    public void setDateFinPeriode(String newDateFinPeriode) {
        dateFinPeriode = newDateFinPeriode;
    }

    public void setDateSection(String newDate) {
        dateSection = newDate;
    }

    /**
     * Date de création : (23.05.2002 08:02:14)
     * 
     * @param newDateSuspendu String
     */
    public void setDateSuspendu(String newDateSuspendu) {
        dateSuspendu = newDateSuspendu;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    /**
     * Date de création : (21.02.2002 17:30:40)
     * 
     * @param newFrais String
     */
    public void setFrais(String newFrais) {
        frais = newFrais;
    }

    /**
     * @param idCaissePro the idCaissePro to set
     */
    public void setIdCaisseProfessionnelle(String idCaissePro) {
        this.idCaissePro = idCaissePro;
    }

    public void setIdCompteAnnexe(String newIdCompteAnnexe) {
        idCompteAnnexe = newIdCompteAnnexe;
    }

    @Override
    public void setIdExterne(String newIdExterne) {
        idExterne = newIdExterne;
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Date de création : (07.03.2002 08:42:28)
     * 
     * @param newIdLastEtapeCtx String
     */
    public void setIdLastEtapeCtx(String newIdLastEtapeCtx) {
        idLastEtapeCtx = newIdLastEtapeCtx;
        lastEvenementContentieux = null;
    }

    /**
     * Set le dernier état de la section en fonction du contentieux Aquila
     * 
     * @param String idLastEtatAquila (Code système)
     */
    public void setIdLastEtatAquila(String idLastEtatAquila) {
        this.idLastEtatAquila = idLastEtatAquila;
    }

    /**
     * Set le mode de compensation de la section
     * 
     * @param idModeCompensation (Code Système)
     */
    @Override
    public void setIdModeCompensation(String idModeCompensation) {
        this.idModeCompensation = idModeCompensation;
    }

    public void setIdMotifContentieuxSuspendu(String newIdMotifContentieuxSuspendu) {
        idMotifContentieuxSuspendu = newIdMotifContentieuxSuspendu;
        ucMotifContentieuxSuspendu = null;
        csMotifContentieuxSuspendu = null;
    }

    /**
     * Cette méthode permet de setter l'identifiant du passage dans lequel le report de la section est pris en compte
     * 
     * @param String idPassageComp
     */
    @Override
    public void setIdPassageComp(String idPassageComp) {
        this.idPassageComp = idPassageComp;
    }

    public void setIdPlanRecouvrement(String newIdPlanRecouvrement) {
        idPlanRecouvrement = newIdPlanRecouvrement;
    }

    public void setIdPosteJournalisation(String newIdPosteJournalisation) {
        idPosteJournalisation = newIdPosteJournalisation;
    }

    /**
     * Date de création : (22.01.2002 13:15:39)
     */
    @Override
    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    /**
     * Setter
     */
    @Override
    public void setIdSection(String newIdSection) {
        idSection = newIdSection;
    }

    /**
     * @param string
     */
    public void setIdSectionPrincipal(String string) {
        idSectionPrincipal = string;
    }

    public void setIdSequenceContentieux(String newIdSequenceContentieux) {
        idSequenceContentieux = newIdSequenceContentieux;
    }

    @Override
    public void setIdTypeSection(String newIdTypeSection) {
        idTypeSection = newIdTypeSection;
    }

    /**
     * Date de création : (21.02.2002 17:30:52)
     * 
     * @param newInterets String
     */
    public void setInterets(String newInterets) {
        interets = newInterets;
    }

    /**
     * @param nonImprimable
     */
    public void setNonImprimable(Boolean nonImprimable) {
        this.nonImprimable = nonImprimable;
    }

    /**
     * Date de création : (21.06.2002 13:36:31)
     * 
     * @param newNumeroPoursuite String
     */
    public void setNumeroPoursuite(String newNumeroPoursuite) {
        numeroPoursuite = newNumeroPoursuite;
    }

    /**
     * Setter de l'objet CAPlanRecouvrement
     * 
     * @param planRecouvrement
     */
    public void setPlanRecouvrement(CAPlanRecouvrement planRecouvrement) {
        this.planRecouvrement = planRecouvrement;
    }

    public void setPmtCmp(String newPmtCmp) {
        pmtCmp = newPmtCmp;
    }

    /**
     * @param referenceBvr the referenceBvr to set
     */
    public void setReferenceBvr(String referenceBvr) {
        this.referenceBvr = referenceBvr;
    }

    public void setSolde(String newSolde) {
        solde = newSolde;
    }

    /**
     * @param statutBN the statutBN to set
     */
    public void setStatutBN(String statutBN) {
        this.statutBN = statutBN;
    }

    /**
     * Date de création : (21.02.2002 17:30:28)
     * 
     * @param newTaxes String
     */
    public void setTaxes(String newTaxes) {
        taxes = newTaxes;
    }

    /**
     * Date de création : (22.01.2002 08:16:23)
     * 
     * @param newTexteRemarque String
     */
    @Override
    public void setTexteRemarque(String newTexteRemarque) {
        texteRemarque = newTexteRemarque;
        saveRemarque = true;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    /**
     * Synchroniser le compteur en fonction des opérations liées <br />
     * Date de création : (06.02.2002 13:37:51)
     */
    public void synchronizeFromOperations(BTransaction tr) {
        // Initialisation
        String lastIdOperation = CASection.ZERO;

        // Vider les variables de cumul
        setSolde(CASection.SOLDE_ZERO);
        setBase(CASection.SOLDE_ZERO);
        setPmtCmp(CASection.SOLDE_ZERO);
        setInterets(CASection.SOLDE_ZERO);
        setTaxes(CASection.SOLDE_ZERO);
        setFrais(CASection.SOLDE_ZERO);
        setAmende(CASection.SOLDE_ZERO);
        setIdLastEtapeCtx(CASection.ZERO);

        // Instancer un manager pour récupérer les types d'opération
        CAOperationManager mgr = new CAOperationManager();
        mgr.setSession(getSession());
        mgr.setForIdSection(getIdSection());

        // Récupérer les opérations
        while (true) {
            // Récupérer une série d'opérations
            try {
                mgr.clear();
                mgr.find(tr);
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return;
            }

            // Sortir s'il n'y a aucune opération trouvée
            if (mgr.size() == 0) {
                break;
            }

            // Récupérer les opérations
            for (int i = 0; i < mgr.size(); i++) {
                // Récupérer une opération et la convertir dans le type
                // d'opération
                CAOperation operX = (CAOperation) mgr.getEntity(i);
                CAOperation oper = operX.getOperationFromType(tr);

                // Si l'opération n'a pas été convertie
                if (oper == null) {
                    _addError(null, getSession().getLabel("5013") + " " + operX.getIdOperation());
                    return;
                } else {
                    // Instancier un objet en fonction du type d'opération
                    oper.setSession(getSession());
                    // Conserver le dernier ID
                    lastIdOperation = oper.getIdOperation();

                    // Si provisoire ou comptabilisée
                    if (oper.getEstActive().booleanValue()) {
                        // Mettre à jour
                        FWMessage msg = addOperation(oper);
                        if (msg != null) {
                            _addError(null, msg.getMessageText());
                            return;
                        }
                    }
                }
            }

            // Charger les opérations suivantes sauf si vide
            if (JadeStringUtil.isIntegerEmpty(lastIdOperation)) {
                break;
            }

            mgr.setAfterIdOperation(lastIdOperation);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        try {
            CACompteAnnexe ca = (CACompteAnnexe) getCompteAnnexe();
            String s = "[" + getIdSection() + "] " + getIdExterne() + " " + getDateSection() + " " + getSoldeFormate();
            if (ca != null) {
                s = s + " ; " + ca.toString();
            }
            return s;
        } catch (Exception e) {
            return super.toString();
        }
    }

    /**
     * S'il s'agit d'une opération contentieux, on met à jour la dernière étape
     * 
     * @param oper
     */
    private void updateAddLastEtape(CAOperation oper) {
        // Récupérer l'opération contentieux
        CAOperationContentieux ctx = (CAOperationContentieux) oper;

        // S'il n'y a pas de dernière étape, on utilise l'étape en cours
        if (JadeStringUtil.isIntegerEmpty(getIdLastEtapeCtx())) {
            setIdLastEtapeCtx(ctx.getIdEvenementContentieux());
        } else {
            // Récupérer la séquence
            String sOldSequence = getLastEvenementContentieux().getParametreEtape().getSequence();
            String sNewSequence = ctx.getParametreEtape().getSequence();

            // Si nouvelle séquence > ancienne, on met à jour
            if (sNewSequence.compareTo(sOldSequence) > 0) {
                setIdLastEtapeCtx(ctx.getIdEvenementContentieux());
            }
        }
    }

    /**
     * S'il s'agit d'une écriture, on met à jour le solde du compte annexe
     * 
     * @param oper
     */
    private void updateAddSoldeCA(CAOperation oper) {
        // S'il s'agit d'une écriture, on met à jour le solde du compte annexe
        CAEcriture ecr = (CAEcriture) oper;
        FWCurrency montant = new FWCurrency(ecr.getMontant());

        // Mettre à jour le solde
        FWCurrency solde = new FWCurrency(getSolde());
        solde.add(montant);
        setSolde(solde.toString());

        // Mettre à jour la base / pmtCmp en fonction de la nature du compte
        String nature = ecr.getCompte().getNatureRubrique();
        if (nature.equals(APIRubrique.COMPTE_COMPENSATION) || nature.equals(APIRubrique.COMPTE_FINANCIER)) {
            FWCurrency pmtCmp = new FWCurrency(getPmtCmp());
            pmtCmp.add(montant);
            setPmtCmp(pmtCmp.toString());
            // Taxes
        } else if (nature.equals(APIRubrique.TAXATION_OFFICE) || nature.equals(APIRubrique.TAXE_SOMMATION)) {
            FWCurrency taxes = new FWCurrency(getTaxes());
            taxes.add(montant);
            setTaxes(taxes.toString());
            // Amende
        } else if (nature.equals(APIRubrique.AMENDE)) {
            FWCurrency amende = new FWCurrency(getAmende());
            amende.add(montant);
            setAmende(amende.toString());
            // Frais
        } else if (nature.equals(APIRubrique.FRAIS_POURSUITES)) {
            FWCurrency frais = new FWCurrency(getFrais());
            frais.add(montant);
            setFrais(frais.toString());
            // Intérêts
        } else if (nature.equals(APIRubrique.INTERETS) || nature.equals(APIRubrique.INTERETS_MORATOIRES)
                || nature.equals(APIRubrique.INTERETS_REMUNERATOIRES)) {
            FWCurrency interets = new FWCurrency(getInterets());
            interets.add(montant);
            setInterets(interets.toString());
            // Base
        } else {
            FWCurrency base = new FWCurrency(getBase());
            base.add(montant);
            setBase(base.toString());
        }
    }

    /**
     * S'il s'agit d'une écriture, on met à jour le solde de la section
     * 
     * @param oper
     */
    private void updateAddSoldeSection(CAOperation oper) {
        // S'il s'agit d'une écriture, on met à jour le solde de la section

        CAAuxiliaire aux = (CAAuxiliaire) oper;
        FWCurrency montant = new FWCurrency(aux.getMontant());

        // Mettre à jour le solde
        FWCurrency solde = new FWCurrency(getSolde());
        solde.add(montant);
        setSolde(solde.toString());

        if (oper.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE_PAIEMENT)) {
            FWCurrency pmtCmp = new FWCurrency(getPmtCmp());
            pmtCmp.add(montant);
            setPmtCmp(pmtCmp.toString());
        } else {
            FWCurrency base = new FWCurrency(getBase());
            base.add(montant);
            setBase(base.toString());
        }
    }

    @Override
    public void updateCaisseProf(String idCaisseProfessionnelle) throws Exception {
        CASection sect = new CASection();
        sect.setSession(getSession());
        sect.setIdSection(getIdSection());
        sect.retrieve();
        if (!sect.isNew() && !sect.hasErrors()) {
            sect.setIdCaisseProfessionnelle(idCaisseProfessionnelle);
            sect.update();
        }
    }

    // TODO sch 30 mars 2010 Cette méthode pourra être supprimée dès que toutes
    // les dates fin de périodes seront mises à jour (version 1-5-9)
    private void updateDateFinPeriode() throws Exception {
        // Déduire la période si vide
        if (!JadeStringUtil.isBlank(dateDebutPeriode) && JadeStringUtil.isBlankOrZero(dateFinPeriode)) {
            String annee = getDateDebutPeriode().substring(6, 10);
            if ((getCategorieSection().compareTo("227001") >= 0) && (getCategorieSection().compareTo("227012") <= 0)) {
                JACalendarGregorian calendar = new JACalendarGregorian();
                JADate _dateDeb = new JADate(getDateDebutPeriode());
                dateFinPeriode = calendar.setLastInMonth(_dateDeb).toStr(".");
                // 1er Trimestre
            } else if (getCategorieSection().equals("227041")) {
                dateFinPeriode = "31.03." + annee;
                // 2è Trimestre
            } else if (getCategorieSection().equals("227042")) {
                dateFinPeriode = "30.06." + annee;
                // 3è Trimestre
            } else if (getCategorieSection().equals("227043")) {
                dateFinPeriode = "30.09." + annee;
                // 4è Trimestre
            } else if (getCategorieSection().equals("227044")) {
                dateFinPeriode = "31.12." + annee;
                // 1er semestre
            } else if (getCategorieSection().equals("227061")) {
                dateFinPeriode = "30.06." + annee;
                // 2ème semestre
            } else if (getCategorieSection().equals("227062")) {
                dateFinPeriode = "31.12." + annee;
            } else {
                dateFinPeriode = "31.12." + annee;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APISection#updateInfoCompensation(java.lang.String, java.lang.String)
     */
    @Override
    public void updateInfoCompensation(String idModeCompensation, String idPassageComp) throws Exception {
        CASection sect = new CASection();
        sect.setSession(getSession());
        sect.setIdSection(getIdSection());
        sect.retrieve();
        if (!sect.isNew() && !sect.hasErrors()) {
            sect.setIdModeCompensation(idModeCompensation);
            sect.setIdPassageComp(idPassageComp);
            sect.update();
        }
    }

    /**
     * S'il s'agit d'une opération contentieux, on met à jour la dernière étape
     * 
     * @param oper
     */
    private void updateRemoveLastEtape(CAOperation oper) {
        // Récupérer l'opération contentieux
        CAOperationContentieux ctx = (CAOperationContentieux) oper;

        // S'il y a une étape en cours
        if (!JadeStringUtil.isIntegerEmpty(getIdLastEtapeCtx())) {
            // Si l'évenement contentieux correspond au dernier
            if (ctx.getIdEvenementContentieux().equals(getIdLastEtapeCtx())) {
                // Récupérer l'événement contentieux précédent
                APIEvenementContentieux evc = ctx.getParametreEtape().getEvenementContentieuxPrecedent(this);

                // Boucler tant qu'il y a des événements
                while (evc != null) {
                    // Si l'événement a été déclenché
                    if (evc.getEstDeclenche().booleanValue()) {
                        setIdLastEtapeCtx(evc.getIdEvenementContentieux());
                        break;
                    }
                    // Evénement précédent
                    evc = evc.getParametreEtape().getEvenementContentieuxPrecedent(this);
                }

                // S'il n'y a rien, il n'y a plus d'événement
                if (evc == null) {
                    setIdLastEtapeCtx(CASection.ZERO);
                }
            }
        }
    }

    public String toOtherString() {
        return idExterne;
    }

    /**
     * S'il s'agit d'une écriture, on met à jour le solde du compte annexe
     * 
     * @param oper
     */
    private void updateRemoveSoldeCA(CAOperation oper) {
        CAEcriture ecr = (CAEcriture) oper;

        // Mettre à jour le solde
        FWCurrency montant = new FWCurrency(ecr.getMontant());
        FWCurrency solde = new FWCurrency(getSolde());
        solde.sub(montant);
        setSolde(solde.toString());

        // Mettre à jour la base / pmtCmp en fonction de la nature du compte
        String nature = ecr.getCompte().getNatureRubrique();
        if (nature.equals(APIRubrique.COMPTE_COMPENSATION) || nature.equals(APIRubrique.COMPTE_FINANCIER)) {
            FWCurrency pmtCmp = new FWCurrency(getPmtCmp());
            pmtCmp.sub(montant);
            setPmtCmp(pmtCmp.toString());
            // Taxes
        } else if (nature.equals(APIRubrique.TAXATION_OFFICE) || nature.equals(APIRubrique.TAXE_SOMMATION)) {
            FWCurrency taxes = new FWCurrency(getTaxes());
            taxes.sub(montant);
            setTaxes(taxes.toString());
            // Frais
        } else if (nature.equals(APIRubrique.FRAIS_POURSUITES)) {
            FWCurrency frais = new FWCurrency(getFrais());
            frais.sub(montant);
            setFrais(frais.toString());
            // Amende
        } else if (nature.equals(APIRubrique.AMENDE)) {
            FWCurrency amende = new FWCurrency(getAmende());
            amende.sub(montant);
            setAmende(amende.toString());
            // Intérêts
        } else if (nature.equals(APIRubrique.INTERETS) || nature.equals(APIRubrique.INTERETS_MORATOIRES)
                || nature.equals(APIRubrique.INTERETS_REMUNERATOIRES)) {
            FWCurrency interets = new FWCurrency(getInterets());
            interets.sub(montant);
            setInterets(interets.toString());
            // Base
        } else {
            FWCurrency base = new FWCurrency(getBase());
            base.sub(montant);
            setBase(base.toString());
        }
    }

    /**
     * S'il s'agit d'une écriture, on met à jour le solde de la section
     * 
     * @param oper
     */
    private void updateRemoveSoldeSection(CAOperation oper) {
        CAAuxiliaire aux = (CAAuxiliaire) oper;
        FWCurrency montant = new FWCurrency(aux.getMontant());

        // Mettre à jour le solde
        FWCurrency solde = new FWCurrency(getSolde());
        solde.sub(montant);
        setSolde(solde.toString());

        if (oper.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE_PAIEMENT)) {
            FWCurrency pmtCmp = new FWCurrency(getPmtCmp());
            pmtCmp.sub(montant);
            setPmtCmp(pmtCmp.toString());
        } else {
            FWCurrency base = new FWCurrency(getBase());
            base.sub(montant);
            setBase(base.toString());
        }
    }
}
