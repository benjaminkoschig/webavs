package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.utils.RENumberFormatter;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRDateValidator;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class RERenteAccordee extends REPrestationsAccordees {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANNEE_ANTICIPATION = "YLLAAN";
    public static final String FIELDNAME_ANNEE_MONTANT_RAM = "YLDAMR";
    public static final String FIELDNAME_CLE_REGROUPEMENT_DECISION = "YLLKEY";
    public static final String FIELDNAME_CODE_AUXILLIAIRE = "YLLCAU";
    public static final String FIELDNAME_CODE_CAS_SPECIAUX_1 = "YLLCS1";
    public static final String FIELDNAME_CODE_CAS_SPECIAUX_2 = "YLLCS2";
    public static final String FIELDNAME_CODE_CAS_SPECIAUX_3 = "YLLCS3";
    public static final String FIELDNAME_CODE_CAS_SPECIAUX_4 = "YLLCS4";
    public static final String FIELDNAME_CODE_CAS_SPECIAUX_5 = "YLLCS5";
    public static final String FIELDNAME_CODE_MUTATION = "YLLCMU";
    public static final String FIELDNAME_CODE_REFUGIE = "YLLREF";
    public static final String FIELDNAME_CODE_SURVIVANT_INVALIDE = "YLLCSI";
    public static final String FIELDNAME_CS_ETAT_CIVIL = "YLTECI";
    public static final String FIELDNAME_CS_RELATION_AU_REQUERANT = "YLTRRE";
    public static final String FIELDNAME_DATE_DEBUT_ANTICIPATION = "YLDDAN";
    public static final String FIELDNAME_DATE_FIN_DROIT_PREVUE_ECHEANCE = "YLDFDE";
    public static final String FIELDNAME_DATE_REVOCATION_AJOURNEMENT = "YLDRAJ";
    public static final String FIELDNAME_DUREE_AJOURNEMENT = "YLNDAJ";
    public static final String FIELDNAME_ID_BASE_CALCUL = "YLIBAC";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE = "YLIRAC";
    public static final String FIELDNAME_ID_TIERS_BASE_CALCUL = "YLITBC";
    public static final String FIELDNAME_ID_TIERS_COMPLEMENTAIRE_1 = "YLIPTC";
    public static final String FIELDNAME_ID_TIERS_COMPLEMENTAIRE_2 = "YLIDTC";
    public static final String FIELDNAME_IDGESTIONNAIRE = "YAIGES";
    public static final String FIELDNAME_IS_TRAITEMENT_MANUEL = "YLBTMA";
    public static final String FIELDNAME_MONTANT_REDUCTION_ANTICIPATION = "YLMRAN";
    public static final String FIELDNAME_MONTANT_RENTE_ORDI_REMPLACEE = "YLMROR";
    public static final String FIELDNAME_PRESCRIPTION_APPLIQUEE = "YLLPAP";
    public static final String FIELDNAME_REDUCTION_FAUTE_GRAVE = "YLNRFG";
    public static final String FIELDNAME_REMARQUES = "YLLREM";
    public static final String FIELDNAME_SUPPLEMENT_AJOURNEMENT = "YLMSAJ";
    public static final String FIELDNAME_SUPPLEMENT_VEUVAGE = "YLLSVE";
    public static final String FIELDNAME_TAUX_REDUCTION_ANTICIPATION = "YLMTRA";
    public static final String FIELDNAME_CS_GENRE_DROIT_API = "YLGAPI";

    public static final String TABLE_NAME_RENTE_ACCORDEE = "REREACC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String anneeAnticipation = "";
    private String anneeMontantRAM = "";
    private String cleRegroupementDecision = "";
    private String codeAuxilliaire = "";
    private String codeCasSpeciaux1 = "";
    private String codeCasSpeciaux2 = "";
    private String codeCasSpeciaux3 = "";
    private String codeCasSpeciaux4 = "";
    private String codeCasSpeciaux5 = "";
    private String codeMutation = "";
    private String codeRefugie = "";
    private String codeSurvivantInvalide = "";
    private String csEtatCivil = "";
    private String csRelationAuRequerant = "";
    private String dateDebutAnticipation = "";
    private String dateFinDroitPrevueEcheance = "";
    private String dateRevocationAjournement = "";
    private String dureeAjournement = "";
    private String idBaseCalcul = "";

    private String idTiersBaseCalcul = "";
    private String idTiersComplementaire1 = "";
    private String idTiersComplementaire2 = "";
    private Boolean isTraitementManuel = Boolean.FALSE;
    private String montantReducationAnticipation = "";
    private String montantRenteOrdiRemplacee = "";

    private String prescriptionAppliquee = "";
    private String reductionFauteGrave = "";
    private String remarques = "";

    public String getCsGenreDroitApi() {
        return csGenreDroitApi;
    }

    public void setCsGenreDroitApi(String csGenreDroitApi) {
        this.csGenreDroitApi = csGenreDroitApi;
    }

    private String supplementAjournement = "";
    private String supplementVeuvage = "";
    private String tauxReductionAnticipation = "";
    private String csGenreDroitApi = "";

    /**
     * initialise la valeur de Id période api
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(final BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);

        setCsGenre(IREPrestationAccordee.CS_GENRE_RENTES);

        // Génération de la clé de regroupement des décision
        setCleRegroupementDecision(generateKeyDecisionGroup());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeUpdate(final BTransaction transaction) throws Exception {

        // Génération de la clé de regroupement de décision
        setCleRegroupementDecision(generateKeyDecisionGroup());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(final BStatement statement) {

        String getFrom = "";

        getFrom += super._getFrom(statement);

        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        getFrom += " ON ";
        getFrom += RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE;
        getFrom += "=";
        getFrom += REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;

        return getFrom;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
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
    protected void _readProperties(final BStatement statement) throws Exception {
        super._readProperties(statement);

        idPrestationAccordee = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        codeAuxilliaire = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_AUXILLIAIRE);

        csEtatCivil = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_CS_ETAT_CIVIL);
        codeRefugie = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_REFUGIE);
        idTiersComplementaire1 = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_TIERS_COMPLEMENTAIRE_1);
        idTiersComplementaire2 = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_TIERS_COMPLEMENTAIRE_2);

        montantRenteOrdiRemplacee = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_MONTANT_RENTE_ORDI_REMPLACEE);
        codeCasSpeciaux1 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1);
        codeCasSpeciaux2 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2);
        codeCasSpeciaux3 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3);
        codeCasSpeciaux4 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4);
        codeCasSpeciaux5 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5);
        reductionFauteGrave = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_REDUCTION_FAUTE_GRAVE);
        codeMutation = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_MUTATION);
        dureeAjournement = statement.dbReadString(RERenteAccordee.FIELDNAME_DUREE_AJOURNEMENT);
        supplementAjournement = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_SUPPLEMENT_AJOURNEMENT);
        dateRevocationAjournement = RENumberFormatter.fmt(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERenteAccordee.FIELDNAME_DATE_REVOCATION_AJOURNEMENT)), false, false, true, 4, 2);
        anneeAnticipation = statement.dbReadString(RERenteAccordee.FIELDNAME_ANNEE_ANTICIPATION);
        montantReducationAnticipation = statement
                .dbReadNumeric(RERenteAccordee.FIELDNAME_MONTANT_REDUCTION_ANTICIPATION);
        tauxReductionAnticipation = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_TAUX_REDUCTION_ANTICIPATION);
        dateDebutAnticipation = RENumberFormatter.fmt(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERenteAccordee.FIELDNAME_DATE_DEBUT_ANTICIPATION)), false, false, true, 4, 2);
        codeSurvivantInvalide = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_SURVIVANT_INVALIDE);
        dateFinDroitPrevueEcheance = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERenteAccordee.FIELDNAME_DATE_FIN_DROIT_PREVUE_ECHEANCE));
        supplementVeuvage = statement.dbReadString(RERenteAccordee.FIELDNAME_SUPPLEMENT_VEUVAGE);
        prescriptionAppliquee = statement.dbReadString(RERenteAccordee.FIELDNAME_PRESCRIPTION_APPLIQUEE);
        csRelationAuRequerant = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_CS_RELATION_AU_REQUERANT);
        isTraitementManuel = statement.dbReadBoolean(RERenteAccordee.FIELDNAME_IS_TRAITEMENT_MANUEL);
        idBaseCalcul = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);
        anneeMontantRAM = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ANNEE_MONTANT_RAM);
        cleRegroupementDecision = statement.dbReadString(RERenteAccordee.FIELDNAME_CLE_REGROUPEMENT_DECISION);
        idTiersBaseCalcul = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_TIERS_BASE_CALCUL);
        remarques = statement.dbReadString(RERenteAccordee.FIELDNAME_REMARQUES);
        csGenreDroitApi = statement.dbReadNumeric(FIELDNAME_CS_GENRE_DROIT_API);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(final BStatement statement) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(dureeAjournement)) {
            if (!PRDateValidator.isDateFormat_AxMM(getDureeAjournement())) {
                if (PRDateValidator.isDateFormat_AMM(getDureeAjournement())
                        || (Float.parseFloat(getDureeAjournement()) == 0)) {
                    setDureeAjournement(PRDateFormater.convertDate_AMM_to_AxMM(getDureeAjournement()));
                } else {
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("ERREUR_DUREE_AJOUR_NON_VALIDE"));
                    _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DUREE_AJOUR_NON_VALIDE"));
                }
            }
        }

        if (!JadeStringUtil.isBlankOrZero(dateRevocationAjournement)) {
            if (!PRDateValidator.isDateFormat_MMxAAAA(getDateRevocationAjournement())) {
                if (PRDateValidator.isDateFormat_MMAAAA(getDateRevocationAjournement())) {
                    setDateRevocationAjournement(PRDateFormater
                            .convertDate_MMAAAA_to_AAAAMM(getDateRevocationAjournement()));
                } else {
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("ERREUR_DATE_REVO_AJOUR_NON_VALIDE"));
                    _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_REVO_AJOUR_NON_VALIDE"));
                }
            }
        }
        if (!JadeStringUtil.isBlankOrZero(dateDebutAnticipation)) {
            if (!PRDateValidator.isDateFormat_MMxAAAA(getDateDebutAnticipation())) {
                if (PRDateValidator.isDateFormat_MMAAAA(getDateDebutAnticipation())) {
                    setDateDebutAnticipation(PRDateFormater.convertDate_MMAAAA_to_AAAAMM(getDateDebutAnticipation()));
                } else {
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("ERREUR_DATE_DEB_ANTICIPATION_NON_VALIDE"));
                    _addError(statement.getTransaction(),
                            getSession().getLabel("ERREUR_DATE_DEB_ANTICIPATION_NON_VALIDE"));
                }
            }
        }

        if (!JadeStringUtil.isBlankOrZero(dateFinDroitPrevueEcheance)) {
            if (!PRDateValidator.isDateFormat_MMxAAAA(getDateFinDroitPrevueEcheance())) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATE_FIN_DROIT_ECHEANCE_NON_VALIDE"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ERREUR_DATE_FIN_DROIT_ECHEANCE_NON_VALIDE"));
            }
        }

        // champs obligatoires pour la retraite flexible (anticipation)
        // si un des champs est rempli, les autres doivent l'etre egalement
        if ((!JadeStringUtil.isBlankOrZero(anneeAnticipation)
                || !JadeStringUtil.isBlankOrZero(montantReducationAnticipation) || !JadeStringUtil
                    .isBlankOrZero(dateDebutAnticipation))
                && (JadeStringUtil.isBlankOrZero(anneeAnticipation)
                        || JadeStringUtil.isBlankOrZero(montantReducationAnticipation) || JadeStringUtil
                            .isBlankOrZero(dateDebutAnticipation))) {

            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DONNEES_RETRAITE_ANT_INC"));
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("ERREUR_DONNEES_RETRAITE_ANT_INC"));
        }

        // champs obligatoires pour la retraite flexible (ajournement)
        // si un des champs est rempli, les autres doivent l'etre egalement
        if ((!JadeStringUtil.isBlankOrZero(dureeAjournement) || !JadeStringUtil.isBlankOrZero(supplementAjournement) || !JadeStringUtil
                .isBlankOrZero(dateRevocationAjournement))
                && (JadeStringUtil.isBlankOrZero(dureeAjournement)
                        || JadeStringUtil.isBlankOrZero(supplementAjournement) || JadeStringUtil
                            .isBlankOrZero(dateRevocationAjournement))) {

            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DONNEES_AJOUR_RENTE_VIEILLESSE_INC"));
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("ERREUR_DONNEES_AJOUR_RENTE_VIEILLESSE_INC"));
        }

        // controle si la date de fin est plus petite ou égale à la date du mois de paiement
        JADate dateFinDroit = new JADate(getDateFinDroit());
        JADate dateMoisPaiement = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

        JACalendar cal = new JACalendarGregorian();

        if (cal.compare(dateFinDroit, dateMoisPaiement) == JACalendar.COMPARE_FIRSTUPPER) {
            // On permet 1 mois de différence entre la date de fin et le mois de paiement
            if (PRDateFormater.nbrMoisEntreDates(dateFinDroit, dateMoisPaiement) > 1) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DATEFIN_DATEMOISPAIEMENT"));
            }
        }

        if (JadeStringUtil.isBlankOrZero(anneeMontantRAM)) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("ERREUR_ANNEE_MONTANT_RAM_OBLIGATOIRE"));
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_ANNEE_MONTANT_RAM_OBLIGATOIRE"));
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        /**
         * BZ 9872
         * Il arrive très rarement et chez différents clients que l'entité REREACC soit supprimé et non la REPRACC
         * associée.
         * De plus, les REREACC supprimées sont en état non modifiable (VALIDE ou autre) d'ou l'ajout de ce contrôle
         * Une rente accordée (l'entitée) ne peu être supprimée que si elle est en état calculée
         * 
         */
        if (!JadeStringUtil.isEmpty(getCsEtat())) {
            if (!IREPrestationAccordee.CS_ETAT_CALCULE.equals(getCsEtat())) {
                StringBuilder message = new StringBuilder();
                message.append("SUPPRESSION D'ENTITE INTERDITE : Impossible de supprimer la rente accordée avec l'id ["
                        + getIdPrestationAccordee() + "] car elle n'est pas en état calculé");

                Exception e = new Exception(message.toString());
                JadeLogger.error(this, e);
                throw e;
            }
        }
        // Génération de la clé de regroupement des décision
        setCleRegroupementDecision(generateKeyDecisionGroup());
        super._beforeDelete(transaction);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(final globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), getIdPrestationAccordee(), "idPrestationAccordee"));
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
    protected void _writeProperties(final BStatement statement) throws Exception {

        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement
                    .writeField(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE, this._dbWriteNumeric(
                            statement.getTransaction(), getIdPrestationAccordee(), "idPrestationAccordee"));
        }

        statement.writeField(RERenteAccordee.FIELDNAME_CODE_AUXILLIAIRE,
                this._dbWriteString(statement.getTransaction(), codeAuxilliaire, "codeAuxilliaire"));
        statement.writeField(RERenteAccordee.FIELDNAME_CS_ETAT_CIVIL,
                this._dbWriteNumeric(statement.getTransaction(), csEtatCivil, "csEtatCivil"));
        statement.writeField(RERenteAccordee.FIELDNAME_CODE_REFUGIE,
                this._dbWriteString(statement.getTransaction(), codeRefugie, "codeRefugie"));
        statement.writeField(RERenteAccordee.FIELDNAME_ID_TIERS_COMPLEMENTAIRE_1,
                this._dbWriteNumeric(statement.getTransaction(), idTiersComplementaire1, "idTiersComplementaire1"));
        statement.writeField(RERenteAccordee.FIELDNAME_ID_TIERS_COMPLEMENTAIRE_2,
                this._dbWriteNumeric(statement.getTransaction(), idTiersComplementaire2, "idTiersComplementaire2"));
        statement.writeField(RERenteAccordee.FIELDNAME_MONTANT_RENTE_ORDI_REMPLACEE, this._dbWriteNumeric(
                statement.getTransaction(), montantRenteOrdiRemplacee, "montantRenteOrdiRemplacee"));
        statement.writeField(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1,
                this._dbWriteString(statement.getTransaction(), codeCasSpeciaux1, "codeCasSpeciaux1"));
        statement.writeField(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2,
                this._dbWriteString(statement.getTransaction(), codeCasSpeciaux2, "codeCasSpeciaux2"));
        statement.writeField(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3,
                this._dbWriteString(statement.getTransaction(), codeCasSpeciaux3, "codeCasSpeciaux3"));
        statement.writeField(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4,
                this._dbWriteString(statement.getTransaction(), codeCasSpeciaux4, "codeCasSpeciaux4"));
        statement.writeField(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5,
                this._dbWriteString(statement.getTransaction(), codeCasSpeciaux5, "codeCasSpeciaux5"));
        statement.writeField(RERenteAccordee.FIELDNAME_REDUCTION_FAUTE_GRAVE,
                this._dbWriteNumeric(statement.getTransaction(), reductionFauteGrave, "reductionFauteGrave"));

        statement.writeField(RERenteAccordee.FIELDNAME_CODE_MUTATION,
                this._dbWriteString(statement.getTransaction(), codeMutation, "codeMutation"));
        statement.writeField(RERenteAccordee.FIELDNAME_DUREE_AJOURNEMENT,
                this._dbWriteString(statement.getTransaction(), dureeAjournement, "dureeAjournement"));
        statement.writeField(RERenteAccordee.FIELDNAME_SUPPLEMENT_AJOURNEMENT,
                this._dbWriteNumeric(statement.getTransaction(), supplementAjournement, "supplementAjournement"));
        statement.writeField(RERenteAccordee.FIELDNAME_DATE_REVOCATION_AJOURNEMENT, this._dbWriteNumeric(
                statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateRevocationAjournement),
                "dateRevocationAjournement"));
        statement.writeField(RERenteAccordee.FIELDNAME_ANNEE_ANTICIPATION,
                this._dbWriteString(statement.getTransaction(), anneeAnticipation, "anneeAnticipation"));
        statement.writeField(RERenteAccordee.FIELDNAME_MONTANT_REDUCTION_ANTICIPATION, this._dbWriteNumeric(
                statement.getTransaction(), montantReducationAnticipation, "montantReducationAnticipation"));
        statement.writeField(RERenteAccordee.FIELDNAME_TAUX_REDUCTION_ANTICIPATION, this._dbWriteNumeric(
                statement.getTransaction(), tauxReductionAnticipation, "tauxReductionAnticipation"));
        statement.writeField(
                RERenteAccordee.FIELDNAME_DATE_DEBUT_ANTICIPATION,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebutAnticipation), "dateDebutAnticipation"));
        statement.writeField(RERenteAccordee.FIELDNAME_CODE_SURVIVANT_INVALIDE,
                this._dbWriteString(statement.getTransaction(), codeSurvivantInvalide, "codeSurvivantInvalide"));
        statement.writeField(RERenteAccordee.FIELDNAME_DATE_FIN_DROIT_PREVUE_ECHEANCE, this._dbWriteNumeric(
                statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFinDroitPrevueEcheance),
                "dateFinDroitEcheance"));
        statement.writeField(RERenteAccordee.FIELDNAME_SUPPLEMENT_VEUVAGE,
                this._dbWriteString(statement.getTransaction(), supplementVeuvage, "supplementVeuvage"));
        statement.writeField(RERenteAccordee.FIELDNAME_PRESCRIPTION_APPLIQUEE,
                this._dbWriteString(statement.getTransaction(), prescriptionAppliquee, "prescriptionAppliquee"));
        statement.writeField(RERenteAccordee.FIELDNAME_CS_RELATION_AU_REQUERANT,
                this._dbWriteNumeric(statement.getTransaction(), csRelationAuRequerant, "csRelationAuRequerant"));
        statement.writeField(RERenteAccordee.FIELDNAME_IS_TRAITEMENT_MANUEL, this._dbWriteBoolean(
                statement.getTransaction(), isTraitementManuel, BConstants.DB_TYPE_BOOLEAN_CHAR, "isTraitementManuel"));
        statement.writeField(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL,
                this._dbWriteNumeric(statement.getTransaction(), idBaseCalcul, "idBaseCalcul"));
        statement.writeField(RERenteAccordee.FIELDNAME_ANNEE_MONTANT_RAM,
                this._dbWriteNumeric(statement.getTransaction(), anneeMontantRAM, "anneeMontantRAM"));
        statement.writeField(RERenteAccordee.FIELDNAME_CLE_REGROUPEMENT_DECISION,
                this._dbWriteString(statement.getTransaction(), cleRegroupementDecision, "cleRegroupementDecision"));
        statement.writeField(RERenteAccordee.FIELDNAME_ID_TIERS_BASE_CALCUL,
                this._dbWriteNumeric(statement.getTransaction(), idTiersBaseCalcul, "idTiersBaseCalcul"));
        statement.writeField(RERenteAccordee.FIELDNAME_REMARQUES,
                this._dbWriteString(statement.getTransaction(), remarques, "remarques"));

        statement.writeField(FIELDNAME_CS_GENRE_DROIT_API,
                _dbWriteNumeric(statement.getTransaction(), csGenreDroitApi, "csGenreDroitApi"));

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Auto-generated method stub
        return super.clone();
    }

    /**
     * Test si la rente accordée possède le code cas spécial passé en paramètre
     * 
     * @param codeCasSpecial
     * @return
     */
    public boolean contientCodeCasSpecial(final String codeCasSpecial) {
        if (!JadeStringUtil.isBlankOrZero(codeCasSpecial)) {
            if (codeCasSpecial.equals(getCodeCasSpeciaux1()) || codeCasSpecial.equals(getCodeCasSpeciaux2())
                    || codeCasSpecial.equals(getCodeCasSpeciaux3()) || codeCasSpecial.equals(getCodeCasSpeciaux4())
                    || codeCasSpecial.equals(getCodeCasSpeciaux5())) {
                return true;
            }
        }
        return false;
    }

    private String generateKeyDecisionGroup() throws Exception {

        String result = "";

        // Serait plus propre en récupérant les code utilisateurs des code système correspondant,
        // mais bon un peu lourd comme traitement...

        /*
         * AVS Ordianire
         */
        if (REGenresPrestations.GENRE_10.equals(getCodePrestation())
                || REGenresPrestations.GENRE_13.equals(getCodePrestation())
                || REGenresPrestations.GENRE_14.equals(getCodePrestation())
                || REGenresPrestations.GENRE_15.equals(getCodePrestation())
                || REGenresPrestations.GENRE_16.equals(getCodePrestation())
                || REGenresPrestations.GENRE_33.equals(getCodePrestation())
                || REGenresPrestations.GENRE_34.equals(getCodePrestation())
                || REGenresPrestations.GENRE_35.equals(getCodePrestation())
                || REGenresPrestations.GENRE_36.equals(getCodePrestation())) {
            result = "AVS-ordi";
        }
        /*
         * AVS extra ordianire
         */
        else if (REGenresPrestations.GENRE_20.equals(getCodePrestation())
                || REGenresPrestations.GENRE_23.equals(getCodePrestation())
                || REGenresPrestations.GENRE_24.equals(getCodePrestation())
                || REGenresPrestations.GENRE_25.equals(getCodePrestation())
                || REGenresPrestations.GENRE_26.equals(getCodePrestation())
                || REGenresPrestations.GENRE_45.equals(getCodePrestation())) {
            result = "AVS-extra";
        }

        /*
         * API
         */
        if (REGenresPrestations.GENRE_81.equals(getCodePrestation())
                || REGenresPrestations.GENRE_91.equals(getCodePrestation())
                || REGenresPrestations.GENRE_84.equals(getCodePrestation())
                || REGenresPrestations.GENRE_82.equals(getCodePrestation())
                || REGenresPrestations.GENRE_88.equals(getCodePrestation())
                || REGenresPrestations.GENRE_92.equals(getCodePrestation())
                || REGenresPrestations.GENRE_83.equals(getCodePrestation())
                || REGenresPrestations.GENRE_93.equals(getCodePrestation())) {

            result = "API-AI";

        } else if (REGenresPrestations.GENRE_85.equals(getCodePrestation())
                || REGenresPrestations.GENRE_94.equals(getCodePrestation())
                || REGenresPrestations.GENRE_95.equals(getCodePrestation())
                || REGenresPrestations.GENRE_86.equals(getCodePrestation())
                || REGenresPrestations.GENRE_89.equals(getCodePrestation())
                || REGenresPrestations.GENRE_96.equals(getCodePrestation())
                || REGenresPrestations.GENRE_87.equals(getCodePrestation())
                || REGenresPrestations.GENRE_97.equals(getCodePrestation())) {

            result = "API-AVS";
        }

        /*
         * AI Ordinaire
         */
        else if (REGenresPrestations.GENRE_50.equals(getCodePrestation())
                || REGenresPrestations.GENRE_53.equals(getCodePrestation())
                || REGenresPrestations.GENRE_54.equals(getCodePrestation())
                || REGenresPrestations.GENRE_55.equals(getCodePrestation())
                || REGenresPrestations.GENRE_56.equals(getCodePrestation())) {
            result = "AI-ordi";// + getFractionRente();
        }
        /*
         * AI Extra ordinaire
         */
        else if (REGenresPrestations.GENRE_70.equals(getCodePrestation())
                || REGenresPrestations.GENRE_72.equals(getCodePrestation())
                || REGenresPrestations.GENRE_73.equals(getCodePrestation())
                || REGenresPrestations.GENRE_74.equals(getCodePrestation())
                || REGenresPrestations.GENRE_75.equals(getCodePrestation())
                || REGenresPrestations.GENRE_76.equals(getCodePrestation())) {
            result = "AI-extra";// + getFractionRente();
        } else {
            result = "";
        }

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(getSession());
        bc.setIdBasesCalcul(getIdBaseCalcul());
        bc.retrieve();

        PRAssert.notIsNew(bc, null);

        if (!JadeStringUtil.isBlankOrZero(bc.getReferenceDecision())) {
            result += "-REF_DEC-" + bc.getReferenceDecision();
        } else {
            Long lRam = new Long(bc.getRevenuAnnuelMoyen());
            Long lEchelle = new Long(bc.getEchelleRente());
            Float lDureeCotiRam = new Float(bc.getDureeRevenuAnnuelMoyen());

            result += "-" + lRam.toString() + "-" + lEchelle.toString() + "-" + lDureeCotiRam.toString();
        }
        return result;
    }

    /**
     * @return
     */
    public String getAnneeAnticipation() {
        return anneeAnticipation;
    }

    /**
     * @return the anneeMontantRAM
     */
    public String getAnneeMontantRAM() {
        return anneeMontantRAM;
    }

    /**
     * @return the cleRegroupementDecision
     */
    public String getCleRegroupementDecision() {
        return cleRegroupementDecision;
    }

    /**
     * @return
     */
    public String getCodeAuxilliaire() {
        return codeAuxilliaire;
    }

    public String getCodeCasSpeciaux1() {
        return codeCasSpeciaux1;
    }

    public String getCodeCasSpeciaux2() {
        return codeCasSpeciaux2;
    }

    public String getCodeCasSpeciaux3() {
        return codeCasSpeciaux3;
    }

    public String getCodeCasSpeciaux4() {
        return codeCasSpeciaux4;
    }

    public String getCodeCasSpeciaux5() {
        return codeCasSpeciaux5;
    }

    /**
     * @return
     */
    public String getCodeMutation() {
        return codeMutation;
    }

    /**
     * @return
     */
    public String getCodeRefugie() {
        return codeRefugie;
    }

    /**
     * Ce n'est pas un code système
     * 
     * @return 0, 1 ou une chaîne vide
     */
    public String getCodeSurvivantInvalide() {
        return codeSurvivantInvalide;
    }

    /**
     * @return
     */
    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    /**
     * @return
     */
    public String getCsRelationAuRequerant() {
        return csRelationAuRequerant;
    }

    /**
     * @return
     */
    public String getDateDebutAnticipation() {
        return dateDebutAnticipation;
    }

    /**
     * @return
     */
    public String getDateFinDroitPrevueEcheance() {
        return dateFinDroitPrevueEcheance;
    }

    /**
     * @return
     */
    public String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    /**
     * @return
     */
    public String getDureeAjournement() {
        return dureeAjournement;
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    /**
     * @return
     */
    public String getIdTiersComplementaire1() {
        return idTiersComplementaire1;
    }

    /**
     * @return
     */
    public String getIdTiersComplementaire2() {
        return idTiersComplementaire2;
    }

    /**
     * @return
     */
    public String getMontantReducationAnticipation() {
        return montantReducationAnticipation;
    }

    /**
     * @return
     */
    public String getMontantRenteOrdiRemplacee() {
        return montantRenteOrdiRemplacee;
    }

    /**
     * @return
     */
    public String getPrescriptionAppliquee() {
        return prescriptionAppliquee;
    }

    /**
     * @return
     */
    public String getReductionFauteGrave() {
        return reductionFauteGrave;
    }

    public String getRemarques() {
        return remarques;
    }

    /**
     * @return
     */
    public String getSupplementAjournement() {
        return supplementAjournement;
    }

    /**
     * @return
     */
    public String getSupplementVeuvage() {
        return supplementVeuvage;
    }

    public String getTauxReductionAnticipation() {
        return tauxReductionAnticipation;
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
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    public Boolean isRAAPI() {
        if (REGenresPrestations.GENRE_85.equals(getCodePrestation())
                || REGenresPrestations.GENRE_86.equals(getCodePrestation())
                || REGenresPrestations.GENRE_87.equals(getCodePrestation())
                || REGenresPrestations.GENRE_89.equals(getCodePrestation())
                || REGenresPrestations.GENRE_94.equals(getCodePrestation())
                || REGenresPrestations.GENRE_95.equals(getCodePrestation())
                || REGenresPrestations.GENRE_96.equals(getCodePrestation())
                || REGenresPrestations.GENRE_97.equals(getCodePrestation())
                || REGenresPrestations.GENRE_81.equals(getCodePrestation())
                || REGenresPrestations.GENRE_82.equals(getCodePrestation())
                || REGenresPrestations.GENRE_83.equals(getCodePrestation())
                || REGenresPrestations.GENRE_84.equals(getCodePrestation())
                || REGenresPrestations.GENRE_88.equals(getCodePrestation())
                || REGenresPrestations.GENRE_91.equals(getCodePrestation())
                || REGenresPrestations.GENRE_92.equals(getCodePrestation())
                || REGenresPrestations.GENRE_93.equals(getCodePrestation())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean isRAInvalidite() {
        if (REGenresPrestations.GENRE_50.equals(getCodePrestation())
                || REGenresPrestations.GENRE_53.equals(getCodePrestation())
                || REGenresPrestations.GENRE_54.equals(getCodePrestation())
                || REGenresPrestations.GENRE_55.equals(getCodePrestation())
                || REGenresPrestations.GENRE_56.equals(getCodePrestation())
                || REGenresPrestations.GENRE_70.equals(getCodePrestation())
                || REGenresPrestations.GENRE_72.equals(getCodePrestation())
                || REGenresPrestations.GENRE_73.equals(getCodePrestation())
                || REGenresPrestations.GENRE_74.equals(getCodePrestation())
                || REGenresPrestations.GENRE_75.equals(getCodePrestation())
                || REGenresPrestations.GENRE_76.equals(getCodePrestation())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean isRASurvivant() {
        if (REGenresPrestations.GENRE_13.equals(getCodePrestation())
                || REGenresPrestations.GENRE_14.equals(getCodePrestation())
                || REGenresPrestations.GENRE_15.equals(getCodePrestation())
                || REGenresPrestations.GENRE_16.equals(getCodePrestation())
                || REGenresPrestations.GENRE_23.equals(getCodePrestation())
                || REGenresPrestations.GENRE_24.equals(getCodePrestation())
                || REGenresPrestations.GENRE_25.equals(getCodePrestation())
                || REGenresPrestations.GENRE_26.equals(getCodePrestation())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean isRAVieillesse() {
        if (REGenresPrestations.GENRE_10.equals(getCodePrestation())
                || REGenresPrestations.GENRE_33.equals(getCodePrestation())
                || REGenresPrestations.GENRE_34.equals(getCodePrestation())
                || REGenresPrestations.GENRE_35.equals(getCodePrestation())
                || REGenresPrestations.GENRE_36.equals(getCodePrestation())
                || REGenresPrestations.GENRE_20.equals(getCodePrestation())
                || REGenresPrestations.GENRE_45.equals(getCodePrestation())) {

            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @return
     */
    public Boolean isTraitementManuel() {
        return isTraitementManuel;
    }

    /**
     * @param string
     */
    public void setAnneeAnticipation(final String string) {
        anneeAnticipation = string;
    }

    /**
     * @param anneeMontantRAM
     *            the anneeMontantRAM to set
     */
    public void setAnneeMontantRAM(final String anneeMontantRAM) {
        this.anneeMontantRAM = anneeMontantRAM;
    }

    /**
     * @param cleRegroupementDecision
     *            the cleRegroupementDecision to set
     */
    public void setCleRegroupementDecision(final String cleRegroupementDecision) {
        this.cleRegroupementDecision = cleRegroupementDecision;
    }

    /**
     * @param string
     */
    public void setCodeAuxilliaire(final String string) {
        codeAuxilliaire = string;
    }

    public void setCodeCasSpeciaux1(final String codeCasSpeciaux1) {
        this.codeCasSpeciaux1 = codeCasSpeciaux1;
    }

    public void setCodeCasSpeciaux2(final String codeCasSpeciaux2) {
        this.codeCasSpeciaux2 = codeCasSpeciaux2;
    }

    public void setCodeCasSpeciaux3(final String codeCasSpeciaux3) {
        this.codeCasSpeciaux3 = codeCasSpeciaux3;
    }

    public void setCodeCasSpeciaux4(final String codeCasSpeciaux4) {
        this.codeCasSpeciaux4 = codeCasSpeciaux4;
    }

    public void setCodeCasSpeciaux5(final String codeCasSpeciaux5) {
        this.codeCasSpeciaux5 = codeCasSpeciaux5;
    }

    /**
     * @param string
     */
    public void setCodeMutation(final String string) {
        codeMutation = string;
    }

    /**
     * @param newCodeRefugie
     */
    public void setCodeRefugie(final String newCodeRefugie) {
        codeRefugie = newCodeRefugie;
    }

    /**
     * @param string
     */
    public void setCodeSurvivantInvalide(final String string) {
        codeSurvivantInvalide = string;
    }

    /**
     * @param string
     */
    public void setCsEtatCivil(final String string) {
        csEtatCivil = string;
    }

    /**
     * @param string
     */
    public void setCsRelationAuRequerant(final String string) {
        csRelationAuRequerant = string;
    }

    /**
     * @param string
     */
    public void setDateDebutAnticipation(String string) {

        if (string.length() == 6) {
            string = PRDateFormater.convertDate_MMAAAA_to_MMxAAAA(string);
        }

        dateDebutAnticipation = string;
    }

    /**
     * @param string
     */
    public void setDateFinDroitPrevueEcheance(final String string) {
        dateFinDroitPrevueEcheance = string;
    }

    /**
     * @param string
     */
    public void setDateRevocationAjournement(String string) {

        if (string.length() == 6) {
            string = PRDateFormater.convertDate_MMAAAA_to_MMxAAAA(string);
        }

        dateRevocationAjournement = string;
    }

    /**
     * @param string
     */
    public void setDureeAjournement(String string) {

        if (string.length() == 3) {
            string = PRDateFormater.convertDate_AMM_to_AxMM(string);
        }

        dureeAjournement = string;
    }

    public void setIdBaseCalcul(final String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setIdTiersBaseCalcul(final String idTiersBaseCalcul) {
        this.idTiersBaseCalcul = idTiersBaseCalcul;
    }

    /**
     * @param string
     */
    public void setIdTiersComplementaire1(final String string) {
        idTiersComplementaire1 = string;
    }

    /**
     * @param string
     */
    public void setIdTiersComplementaire2(final String string) {
        idTiersComplementaire2 = string;
    }

    /**
     * @param boolean1
     */
    public void setIsTraitementManuel(final Boolean boolean1) {
        isTraitementManuel = boolean1;
    }

    /**
     * @param string
     */
    public void setMontantReducationAnticipation(final String string) {
        montantReducationAnticipation = string;
    }

    /**
     * @param string
     */
    public void setMontantRenteOrdiRemplacee(final String string) {
        montantRenteOrdiRemplacee = string;
    }

    /**
     * @param string
     */
    public void setPrescriptionAppliquee(final String string) {
        prescriptionAppliquee = string;
    }

    /**
     * @param string
     */
    public void setReductionFauteGrave(final String string) {
        reductionFauteGrave = string;
    }

    public void setRemarques(final String remarques) {
        this.remarques = remarques;
    }

    /**
     * @param string
     */
    public void setSupplementAjournement(final String string) {
        supplementAjournement = string;
    }

    /**
     * @param newSupplementVeuvage
     */
    public void setSupplementVeuvage(final String newSupplementVeuvage) {
        supplementVeuvage = newSupplementVeuvage;
    }

    public void setTauxReductionAnticipation(final String tauxReductionAnticipation) {
        this.tauxReductionAnticipation = tauxReductionAnticipation;
    }

}
