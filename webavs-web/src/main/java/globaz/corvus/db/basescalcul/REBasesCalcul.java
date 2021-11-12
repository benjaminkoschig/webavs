/*
 * Créé le 6 févr. 07
 */
package globaz.corvus.db.basescalcul;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteManager;
import globaz.corvus.utils.RENumberFormatter;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRDateValidator;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */

public class REBasesCalcul extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANNEE_BONIF_TACHE_EDUCATION = "YIDABE";
    public static final String FIELDNAME_ANNEE_BONIF_TACHES_ASSISTANCE = "YIDABA";

    public static final String FIELDNAME_ANNEE_BONIF_TRANSITOIRE = "YIDABT";

    public static final String FIELDNAME_ANNEE_COTI_CLASSE_AGE = "YIDACC";
    public static final String FIELDNAME_ANNEE_DE_NIVEAU = "YIDANN";
    public static final String FIELDNAME_ANNEE_TRAITEMENT = "YIDATR";
    public static final String FIELDNAME_BONIF_TACHE_EDUCATION_1 = "YIDBE1";
    public static final String FIELDNAME_BONIF_TACHE_EDUCATION_2 = "YIDBE2";
    public static final String FIELDNAME_BONIF_TACHE_EDUCATION_4 = "YIDBE4";
    public static final String FIELDNAME_CLE_INFIRMITE_AYANT_DROIT = "YILCIA";
    public static final String FIELDNAME_CODE_OFFICE_AI = "YINOAI";
    public static final String FIELDNAME_CS_ETAT = "YITETA";
    public static final String FIELDNAME_DEGRE_INVALIDITE = "YINDIN";
    public static final String FIELDNAME_QUOTITE_RENTE = "QUOTITE_RENTE";
    public static final String FIELDNAME_DROIT_APPLIQUE = "YILDAP";
    public static final String FIELDNAME_DUREE_COTI_AV_73 = "YIDDCA";
    public static final String FIELDNAME_DUREE_COTI_DES_73 = "YIDDCD";
    public static final String FIELDNAME_DUREE_RAM = "YIDDCR";
    public static final String FIELDNAME_ECHELLE_RENTE = "YINECR";
    public static final String FIELDNAME_FACTEUR_REVALORISATION = "YILFAR";
    public static final String FIELDNAME_ID_BASES_DE_CALCUL = "YIIBCA";
    public static final String FIELDNAME_ID_RENTE_CALCULEE = "YIIRCA";
    public static final String FIELDNAME_ID_TIERS_BASE_CALCUL = "YIITBC";
    public static final String FIELDNAME_IS_INVALIDITE_PRECOCE = "YIBIPR";
    public static final String FIELDNAME_IS_LIMITE_REVENU = "YIBIRL";
    public static final String FIELDNAME_IS_MINIMU_GARANTI = "YIBIRG";
    public static final String FIELDNAME_IS_PARTAGE_REVENU_CALCUL = "YIBPRC";
    public static final String FIELDNAME_IS_REVENU_SPLITTE = "YIBIRS";
    public static final String FIELDNAME_MOIS_APPOINTS_AV_73 = "YIDMAA";
    public static final String FIELDNAME_MOIS_APPOINTS_DES_73 = "YIDMAD";
    public static final String FIELDNAME_MOIS_COT_ANNEE_OUVERT_DROIT = "YIDMCA";

    public static final String FIELDNAME_MONTANT_MAX_R10_ECHELLE_44 = "YIMMME";
    public static final String FIELDNAME_PERIODE_ASS_ETRANGERE_DES_73 = "YIDPAS";
    public static final String FIELDNAME_PERIODE_ASSUR_ETRANGERE_AV_73 = "YIDPAE";

    public static final String FIELDNAME_PERIODE_JEUNESSE = "YIDPJE";
    public static final String FIELDNAME_PERIODE_MARIAGE = "YIDPMA";
    public static final String FIELDNAME_REFERENCE_DECISION = "YINRDE";
    public static final String FIELDNAME_RESULTAT_COMPARAISON = "YILRCO";
    public static final String FIELDNAME_REVENU_ANNUEL_MOYEN = "YIMRAM";
    public static final String FIELDNAME_REVENU_JEUNESSE = "YIMRJE";
    public static final String FIELDNAME_REVENU_PRIS_EN_COMPTE = "YIMREV";
    public static final String FIELDNAME_SUPPLEMENT_CARRIERE = "YINSPC";
    public static final String FIELDNAME_SURVENANCE_EVT_ASS_AYANT_DROIT = "YIDSEA";
    public static final String FIELDNAME_TYPE_CALCUL_COMPARATIF = "YILTCC";
    public static final String REVISION_NO_09 = "09";
    public static final String REVISION_NO_10 = "10";
    public static final String TABLE_NAME_BASES_CALCUL = "REBACAL";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static REBasesCalcul isBCExisteDeja(REBasesCalcul bcParametres, BSession session) throws Exception {

        REBasesCalculManager mgr = new REBasesCalculManager();
        mgr.setSession(session);
        mgr.setForIdRenteCalculee(bcParametres.getIdRenteCalculee());
        mgr.find();

        for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
            REBasesCalcul bc = (REBasesCalcul) iterator.next();

            String cleInfirmiteAyantDroit = bc.getCleInfirmiteAyantDroit();

            int lengthCle = cleInfirmiteAyantDroit.length();
            int nbZeroAAjouter = 5 - lengthCle;

            for (int i = 0; i < nbZeroAAjouter; i++) {
                cleInfirmiteAyantDroit = "0" + cleInfirmiteAyantDroit;
            }

            if (bc.getAnneeBonifTacheAssistance().equals(bcParametres.getAnneeBonifTacheAssistance())
                    && bc.getAnneeBonifTacheEduc().equals(bcParametres.getAnneeBonifTacheEduc())
                    && bc.getAnneeBonifTransitoire().equals(bcParametres.getAnneeBonifTransitoire())
                    && bc.getAnneeCotiClasseAge().equals(bcParametres.getAnneeCotiClasseAge())
                    && bc.getAnneeDeNiveau().equals(bcParametres.getAnneeDeNiveau())
                    && bc.getAnneeTraitement().equals(
                            String.valueOf(Integer.parseInt(bcParametres.getAnneeTraitement())))
                    && cleInfirmiteAyantDroit.equals(bcParametres.getCleInfirmiteAyantDroit())
                    && bc.getCodeOfficeAi().equals(bcParametres.getCodeOfficeAi())
                    && bc.getCsEtat().equals(bcParametres.getCsEtat())
                    && bc.getDegreInvalidite().equals(
                            String.valueOf(Integer.parseInt(bcParametres.getDegreInvalidite())))
                    && bc.getDroitApplique().equals(bcParametres.getDroitApplique())
                    && bc.getDureeRevenuAnnuelMoyen().equals(bcParametres.getDureeRevenuAnnuelMoyen())
                    && bc.getEchelleRente().equals(bcParametres.getEchelleRente())
                    && bc.getFacteurRevalorisation().equals(bcParametres.getFacteurRevalorisation())
                    && bc.getIdRenteCalculee().equals(bcParametres.getIdRenteCalculee())
                    && bc.getIsPartageRevenuCalcul().equals(bcParametres.getIsPartageRevenuCalcul())
                    && bc.getMoisAppointsAvant73().equals(
                            String.valueOf(Integer.parseInt(bcParametres.getMoisAppointsAvant73())))
                    && bc.getMoisAppointsDes73().equals(
                            String.valueOf(Integer.parseInt(bcParametres.getMoisAppointsDes73())))
                    && bc.getMoisCotiAnneeOuvertDroit().equals(
                            String.valueOf(Integer.parseInt(bcParametres.getMoisCotiAnneeOuvertDroit())))
                    && bc.getPeriodeAssEtrangerAv73().equals(
                            RENumberFormatter.fmt(
                                    PRDateFormater.convertDate_AAMM_to_AAxMM(bcParametres.getPeriodeAssEtrangerAv73()),
                                    false, false, true, 2, 2))
                    && bc.getPeriodeAssEtrangerDes73()
                            .equals(RENumberFormatter.fmt(
                                    PRDateFormater.convertDate_AAMM_to_AAxMM(bcParametres.getPeriodeAssEtrangerDes73()),
                                    false, false, true, 2, 2))
                    && bc.getPeriodeJeunesse().equals(
                            RENumberFormatter.fmt(
                                    PRDateFormater.convertDate_AAMM_to_AAxMM(bcParametres.getPeriodeJeunesse()), false,
                                    false, true, 2, 2))
                    && bc.getPeriodeMariage().equals(bcParametres.getPeriodeMariage())
                    && bc.getResultatComparatif().equals(bcParametres.getResultatComparatif())
                    && bc.getRevenuAnnuelMoyen().equals(new FWCurrency(bcParametres.getRevenuAnnuelMoyen()).toString())
                    && bc.getRevenuPrisEnCompte().equals(bcParametres.getRevenuPrisEnCompte())
                    && bc.getSupplementCarriere().equals(
                            String.valueOf(Integer.parseInt(bcParametres.getSupplementCarriere())))
                    && bc.getTypeCalculComparatif().equals(bcParametres.getTypeCalculComparatif())

            ) {

                // Si existe déjà, on retourne la bc existante
                return bc;

            } else {

                // Sinon, on retourne la base de calcul passée en paramètre
                return bcParametres;

            }

        }

        // Sinon, on retourne la base de calcul passée en paramètre
        return bcParametres;

    }

    private String anneeBonifTacheAssistance = "";
    private String anneeBonifTacheEduc = "";
    private String anneeBonifTransitoire = "";
    private String anneeCotiClasseAge = "";
    private String anneeDeNiveau = "";
    private String anneeTraitement = "";
    private String cleInfirmiteAyantDroit = "";
    private String codeOfficeAi = "";
    private String csEtat = "";
    private String degreInvalidite = "";
    private String quotiteRente = "";
    private String droitApplique = "";
    private String dureeCotiAvant73 = "";
    private String dureeCotiDes73 = "";
    private String dureeRevenuAnnuelMoyen = "";
    private String echelleRente = "";
    private String facteurRevalorisation = "";
    protected String idBasesCalcul = "";
    private String idRenteCalculee = "";
    private String idTiersBaseCalcul = "";
    // pour la validation lors de la saisie RABCPD
    private Boolean isDemandeRenteAPI = Boolean.FALSE;
    private Boolean isInvaliditePrecoce = Boolean.FALSE;
    private Boolean isLimiteRevenu = Boolean.FALSE;
    private Boolean isMinimuGaranti = Boolean.FALSE;
    private Boolean isPartageRevenuCalcul = Boolean.FALSE;
    private Boolean isRevenuSplitte = Boolean.FALSE;
    private String moisAppointsAvant73 = "";
    private String moisAppointsDes73 = "";
    private String moisCotiAnneeOuvertDroit = "";
    private String montantMaxR10Ech44 = "";
    private String nombreAnneeBTE1 = "";
    private String nombreAnneeBTE2 = "";
    private String nombreAnneeBTE4 = "";
    private String periodeAssEtrangerAv73 = "";
    private String periodeAssEtrangerDes73 = "";
    private String periodeJeunesse = "";
    private String periodeMariage = "";
    private String referenceDecision = "";
    private String resultatComparatif = "";

    private String revenuAnnuelMoyen = "";
    private String revenuJeunesse = "";
    private String revenuPrisEnCompte = "";

    private String supplementCarriere = "";
    private String survenanceEvtAssAyantDroit = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String typeCalculComparatif = "";

    /**
     * Crée une nouvelle instance de la classe REBasesCalcul.
     */
    public REBasesCalcul() {
        super();
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idBasesCalcul = this._incCounter(transaction, idBasesCalcul, REBasesCalcul.TABLE_NAME_BASES_CALCUL);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Copie les pointeurs sur d'autres DAB contenus dans l'entité à partir d'une autre entité. <i>
     * <p>
     * A surcharger pour copier les pointeurs sur les autres DAB. </i>
     * 
     * @param entity
     *            l'entité à copier
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    @Override
    protected void _copyPointersFromEntity(BEntity entity) throws Exception {

        if (entity instanceof REBasesCalcul) {
            setIsDemandeRenteAPI(((REBasesCalcul) entity).isDemandeRenteAPI());
        }
    }

    /**
     * getter pour le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return REBasesCalcul.TABLE_NAME_BASES_CALCUL;
    }

    /**
     * Lecture des propriétés dans les champs de la table
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idBasesCalcul = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        idRenteCalculee = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);
        revenuPrisEnCompte = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_REVENU_PRIS_EN_COMPTE);
        revenuAnnuelMoyen = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_REVENU_ANNUEL_MOYEN);
        dureeCotiAvant73 = RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(statement
                .dbReadString(REBasesCalcul.FIELDNAME_DUREE_COTI_AV_73)), false, false, true, 2, 2);
        dureeCotiDes73 = RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(statement
                .dbReadString(REBasesCalcul.FIELDNAME_DUREE_COTI_DES_73)), false, false, true, 2, 2);
        dureeRevenuAnnuelMoyen = RENumberFormatter.fmt(
                PRDateFormater.convertDate_AAMM_to_AAxMM(statement.dbReadString(REBasesCalcul.FIELDNAME_DUREE_RAM)),
                false, false, true, 2, 2);
        anneeCotiClasseAge = statement.dbReadString(REBasesCalcul.FIELDNAME_ANNEE_COTI_CLASSE_AGE);
        anneeDeNiveau = statement.dbReadString(REBasesCalcul.FIELDNAME_ANNEE_DE_NIVEAU);
        echelleRente = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ECHELLE_RENTE);
        moisAppointsAvant73 = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_MOIS_APPOINTS_AV_73);
        moisAppointsDes73 = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_MOIS_APPOINTS_DES_73);
        isLimiteRevenu = statement.dbReadBoolean(REBasesCalcul.FIELDNAME_IS_LIMITE_REVENU);
        isMinimuGaranti = statement.dbReadBoolean(REBasesCalcul.FIELDNAME_IS_MINIMU_GARANTI);
        codeOfficeAi = statement.dbReadString(REBasesCalcul.FIELDNAME_CODE_OFFICE_AI);
        degreInvalidite = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE);
        quotiteRente = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_QUOTITE_RENTE);
        cleInfirmiteAyantDroit = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_CLE_INFIRMITE_AYANT_DROIT);
        survenanceEvtAssAyantDroit = RENumberFormatter.fmt(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REBasesCalcul.FIELDNAME_SURVENANCE_EVT_ASS_AYANT_DROIT)), false, false, true, 4, 2);
        isInvaliditePrecoce = statement.dbReadBoolean(REBasesCalcul.FIELDNAME_IS_INVALIDITE_PRECOCE);
        periodeJeunesse = RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(statement
                .dbReadString(REBasesCalcul.FIELDNAME_PERIODE_JEUNESSE)), false, false, true, 2, 2);
        revenuJeunesse = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_REVENU_JEUNESSE);
        periodeMariage = RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(statement
                .dbReadString(REBasesCalcul.FIELDNAME_PERIODE_MARIAGE)), false, false, true, 2, 2);
        periodeAssEtrangerAv73 = RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(statement
                .dbReadString(REBasesCalcul.FIELDNAME_PERIODE_ASSUR_ETRANGERE_AV_73)), false, false, true, 2, 2);
        moisCotiAnneeOuvertDroit = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_MOIS_COT_ANNEE_OUVERT_DROIT);
        droitApplique = statement.dbReadString(REBasesCalcul.FIELDNAME_DROIT_APPLIQUE);
        isRevenuSplitte = statement.dbReadBoolean(REBasesCalcul.FIELDNAME_IS_REVENU_SPLITTE);
        anneeBonifTacheEduc = RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(statement
                .dbReadString(REBasesCalcul.FIELDNAME_ANNEE_BONIF_TACHE_EDUCATION)), false, false, true, 2, 2);

        nombreAnneeBTE1 = statement.dbReadString(REBasesCalcul.FIELDNAME_BONIF_TACHE_EDUCATION_1);
        nombreAnneeBTE2 = statement.dbReadString(REBasesCalcul.FIELDNAME_BONIF_TACHE_EDUCATION_2);
        nombreAnneeBTE4 = statement.dbReadString(REBasesCalcul.FIELDNAME_BONIF_TACHE_EDUCATION_4);

        anneeBonifTacheAssistance = RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(statement
                .dbReadString(REBasesCalcul.FIELDNAME_ANNEE_BONIF_TACHES_ASSISTANCE)), false, false, true, 2, 2);
        anneeBonifTransitoire = statement.dbReadString(REBasesCalcul.FIELDNAME_ANNEE_BONIF_TRANSITOIRE);
        supplementCarriere = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_SUPPLEMENT_CARRIERE);
        typeCalculComparatif = statement.dbReadString(REBasesCalcul.FIELDNAME_TYPE_CALCUL_COMPARATIF);
        resultatComparatif = statement.dbReadString(REBasesCalcul.FIELDNAME_RESULTAT_COMPARAISON);
        montantMaxR10Ech44 = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_MONTANT_MAX_R10_ECHELLE_44);
        anneeTraitement = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ANNEE_TRAITEMENT);
        periodeAssEtrangerDes73 = RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(statement
                .dbReadString(REBasesCalcul.FIELDNAME_PERIODE_ASS_ETRANGERE_DES_73)), false, false, true, 2, 2);
        facteurRevalorisation = statement.dbReadString(REBasesCalcul.FIELDNAME_FACTEUR_REVALORISATION);
        csEtat = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_CS_ETAT);
        isPartageRevenuCalcul = statement.dbReadBoolean(REBasesCalcul.FIELDNAME_IS_PARTAGE_REVENU_CALCUL);
        idTiersBaseCalcul = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL);
        referenceDecision = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_REFERENCE_DECISION);

    }

    /**
     * Méthode de validation
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        if (JadeStringUtil.isBlankOrZero(getDroitApplique())) {
            _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DROIT_APP_PAS_RENSEIGNE"));
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("ERREUR_DROIT_APP_PAS_RENSEIGNE"));
        }

        if (!JadeStringUtil.isBlankOrZero(dureeCotiAvant73)) {
            if (!PRDateValidator.isDateFormat_AAxMM_ACOR(getDureeCotiAvant73())) {
                if (PRDateValidator.isDateFormat_AAMM_ACOR(getDureeCotiAvant73())) {
                    setDureeCotiAvant73(PRDateFormater.convertDate_AAMM_to_AAxMM(getDureeCotiAvant73()));
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DUREE_COT_AV_73"));
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("ERREUR_DUREE_COT_AV_73"));
                }
            }
        }

        if (!JadeStringUtil.isBlankOrZero(dureeCotiDes73)) {
            if (!PRDateValidator.isDateFormat_AAxMM_ACOR(getDureeCotiDes73())) {
                if (PRDateValidator.isDateFormat_AAMM_ACOR(getDureeCotiDes73())) {
                    setDureeCotiDes73(PRDateFormater.convertDate_AAMM_to_AAxMM(getDureeCotiDes73()));
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("ERREUR_DUREE_COT_AV_73"));
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("ERREUR_DUREE_COT_AV_73"));
                }
            }
        }

        if (!JadeNumericUtil.isEmptyOrZero(anneeBonifTacheEduc)) {
            if (!PRDateValidator.isDateFormat_AAxDD(getAnneeBonifTacheEduc())) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_PERIODE_BTE_NON_VALIDE"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ERREUR_PERIODE_BTE_NON_VALIDE"));
            }
        }

        if (!JadeNumericUtil.isEmptyOrZero(anneeBonifTacheAssistance)) {
            if (!PRDateValidator.isDateFormat_AAxDD(getAnneeBonifTacheAssistance())) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_PERIODE_BTA_NON_VALIDE"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ERREUR_PERIODE_BTA_NON_VALIDE"));
            }
        }

        if (!JadeNumericUtil.isEmptyOrZero(periodeJeunesse)) {
            if (!PRDateValidator.isDateFormat_AAxMM_ACOR(getPeriodeJeunesse())) {
                if (PRDateValidator.isDateFormat_AAMM_ACOR(getPeriodeJeunesse())) {
                    setPeriodeJeunesse(PRDateFormater.convertDate_AAMM_to_AAxMM(getPeriodeJeunesse()));
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("ERREUR_PERIODE_JEUNESSE_NON_VALIDE"));
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("ERREUR_PERIODE_JEUNESSE_NON_VALIDE"));
                }
            }
        }
        if (!JadeNumericUtil.isEmptyOrZero(periodeMariage)) {
            if (!PRDateValidator.isDateFormat_AAxMM_ACOR(getPeriodeMariage())) {
                if (PRDateValidator.isDateFormat_AAMM_ACOR(getPeriodeMariage())) {
                    setPeriodeMariage(PRDateFormater.convertDate_AAMM_to_AAxMM(getPeriodeMariage()));
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("ERREUR_PERIODE_MARIAGE_NON_VALIDE"));
                    setMsgType(FWViewBeanInterface.ERROR);
                    setMessage(getSession().getLabel("ERREUR_PERIODE_MARIAGE_NON_VALIDE"));
                }
            }
        }
        if (!JadeNumericUtil.isEmptyOrZero(anneeCotiClasseAge)) {
            FWCurrency currency = new FWCurrency(anneeCotiClasseAge);
            if (currency.compareTo(new FWCurrency("46")) == -1) {
                setAnneeCotiClasseAge(getAnneeCotiClasseAge());
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_ANNEE_COT_PLUSGRANDE_44_ANS"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ERREUR_ANNEE_COT_PLUSGRANDE_44_ANS"));
            }
        }
        if (!JadeNumericUtil.isEmptyOrZero(degreInvalidite)) {
            FWCurrency currency = new FWCurrency(degreInvalidite);
            if (currency.compareTo(new FWCurrency("101")) == -1) {
                setDegreInvalidite(getDegreInvalidite());
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_TAUX_INV_SUP_100"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ERREUR_TAUX_INV_SUP_100"));
            }
        }

        // champs obligatoires pour les elements d'invalidite
        // si un des champs est rempli, les autres doivent l'etre egalement

        // La survenance n'est pas testée car la période d'invalidité n'est
        // créée qu'au moment des créations de la ra

        if ((isDemandeRenteAPI() != null) && isDemandeRenteAPI().booleanValue()) {

            if ((!JadeStringUtil.isDecimalEmpty(cleInfirmiteAyantDroit) || !JadeStringUtil.isDecimalEmpty(codeOfficeAi))
                    && (JadeStringUtil.isDecimalEmpty(cleInfirmiteAyantDroit) || JadeStringUtil
                            .isDecimalEmpty(codeOfficeAi))) {

                _addError(statement.getTransaction(), getSession().getLabel("ELEM_INVALIDITE_INC"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ELEM_INVALIDITE_INC"));
            }

        } else {
            if ((!JadeStringUtil.isDecimalEmpty(degreInvalidite)
                    || !JadeStringUtil.isDecimalEmpty(cleInfirmiteAyantDroit) || !JadeStringUtil
                        .isDecimalEmpty(codeOfficeAi))
                    && (JadeStringUtil.isDecimalEmpty(degreInvalidite)
                            || JadeStringUtil.isDecimalEmpty(cleInfirmiteAyantDroit) || JadeStringUtil
                                .isDecimalEmpty(codeOfficeAi))) {

                _addError(statement.getTransaction(), getSession().getLabel("ELEM_INVALIDITE_INC"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ELEM_INVALIDITE_INC"));
            }

        }

        if (!JadeStringUtil.isBlankOrZero(getReferenceDecision())) {
            int i = Integer.parseInt(getReferenceDecision());
            if (i > 99) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_REF_DEC_BC_VALUE_EXCEED"));
                setMsgType(FWViewBeanInterface.ERROR);
                setMessage(getSession().getLabel("ERREUR_REF_DEC_BC_VALUE_EXCEED"));
            }
        }

    }

    /**
     * Définition de la clé primaire de la table
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL,
                this._dbWriteNumeric(statement.getTransaction(), idBasesCalcul, "idBasesCalcul"));

    }

    /**
     * Méthode d'écriture des champs dans la table
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL,
                this._dbWriteNumeric(statement.getTransaction(), idBasesCalcul, "idBasesCalcul"));
        statement.writeField(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE,
                this._dbWriteNumeric(statement.getTransaction(), idRenteCalculee, "idRenteCalculee"));
        statement.writeField(REBasesCalcul.FIELDNAME_REVENU_PRIS_EN_COMPTE,
                this._dbWriteNumeric(statement.getTransaction(), revenuPrisEnCompte, "revenuPrisEnCompte"));
        statement.writeField(REBasesCalcul.FIELDNAME_REVENU_ANNUEL_MOYEN,
                this._dbWriteNumeric(statement.getTransaction(), revenuAnnuelMoyen, "revenuAnnuelMoyen"));
        statement.writeField(
                REBasesCalcul.FIELDNAME_DUREE_COTI_AV_73,
                this._dbWriteString(statement.getTransaction(),
                        PRDateFormater.convertDate_AAxMM_to_AAMM(dureeCotiAvant73), "dureeCotiAvant73"));
        statement.writeField(
                REBasesCalcul.FIELDNAME_DUREE_COTI_DES_73,
                this._dbWriteString(statement.getTransaction(),
                        PRDateFormater.convertDate_AAxMM_to_AAMM(dureeCotiDes73), "dureeCotiDes73"));
        statement.writeField(
                REBasesCalcul.FIELDNAME_DUREE_RAM,
                this._dbWriteString(statement.getTransaction(),
                        PRDateFormater.convertDate_AAxMM_to_AAMM(dureeRevenuAnnuelMoyen), "dureeRevenuAnnuelMoyen"));
        statement.writeField(REBasesCalcul.FIELDNAME_ANNEE_COTI_CLASSE_AGE,
                this._dbWriteString(statement.getTransaction(), anneeCotiClasseAge, "anneeCotiClasseAge"));
        statement.writeField(REBasesCalcul.FIELDNAME_ANNEE_DE_NIVEAU,
                this._dbWriteString(statement.getTransaction(), anneeDeNiveau, "anneeDeNiveau"));
        statement.writeField(REBasesCalcul.FIELDNAME_ECHELLE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), echelleRente, "echelleRente"));
        statement.writeField(REBasesCalcul.FIELDNAME_MOIS_APPOINTS_AV_73,
                this._dbWriteNumeric(statement.getTransaction(), moisAppointsAvant73, "moisAppointsAvant73"));
        statement.writeField(REBasesCalcul.FIELDNAME_MOIS_APPOINTS_DES_73,
                this._dbWriteNumeric(statement.getTransaction(), moisAppointsDes73, "moisAppointsDes73"));

        if (isLimiteRevenu != null) {
            statement.writeField(REBasesCalcul.FIELDNAME_IS_LIMITE_REVENU, this._dbWriteBoolean(
                    statement.getTransaction(), isLimiteRevenu, BConstants.DB_TYPE_BOOLEAN_CHAR, "isLimiteRevenu"));
        }

        if (isMinimuGaranti != null) {
            statement.writeField(REBasesCalcul.FIELDNAME_IS_MINIMU_GARANTI, this._dbWriteBoolean(
                    statement.getTransaction(), isMinimuGaranti, BConstants.DB_TYPE_BOOLEAN_CHAR, "isMinimuGaranti"));
        }

        statement.writeField(REBasesCalcul.FIELDNAME_CODE_OFFICE_AI,
                this._dbWriteString(statement.getTransaction(), codeOfficeAi, "officeAi"));
        statement.writeField(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE,
                this._dbWriteNumeric(statement.getTransaction(), degreInvalidite, "degreInvalidite"));
        statement.writeField(REBasesCalcul.FIELDNAME_QUOTITE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), quotiteRente, "quotiteRente"));
        statement.writeField(REBasesCalcul.FIELDNAME_CLE_INFIRMITE_AYANT_DROIT,
                this._dbWriteNumeric(statement.getTransaction(), cleInfirmiteAyantDroit, "cleInfirmiteAyantDroit"));
        statement.writeField(REBasesCalcul.FIELDNAME_SURVENANCE_EVT_ASS_AYANT_DROIT, this._dbWriteNumeric(
                statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(survenanceEvtAssAyantDroit),
                "survenanceEvtAssAyantDroit"));

        if (isInvaliditePrecoce != null) {
            statement.writeField(REBasesCalcul.FIELDNAME_IS_INVALIDITE_PRECOCE, this._dbWriteBoolean(
                    statement.getTransaction(), isInvaliditePrecoce, BConstants.DB_TYPE_BOOLEAN_CHAR,
                    "isInvaliditePrecoce"));
        }

        statement.writeField(
                REBasesCalcul.FIELDNAME_PERIODE_JEUNESSE,
                this._dbWriteString(statement.getTransaction(),
                        PRDateFormater.convertDate_AAxMM_to_AAMM(periodeJeunesse), "periodeJeunesse"));
        statement.writeField(REBasesCalcul.FIELDNAME_REVENU_JEUNESSE,
                this._dbWriteNumeric(statement.getTransaction(), revenuJeunesse, "revenuJeunesse"));
        statement.writeField(
                REBasesCalcul.FIELDNAME_PERIODE_MARIAGE,
                this._dbWriteString(statement.getTransaction(),
                        PRDateFormater.convertDate_AAxMM_to_AAMM(periodeMariage), "periodeMariage"));
        statement.writeField(
                REBasesCalcul.FIELDNAME_PERIODE_ASSUR_ETRANGERE_AV_73,
                this._dbWriteString(statement.getTransaction(),
                        PRDateFormater.convertDate_AAxMM_to_AAMM(periodeAssEtrangerAv73), "periodeAssEtrangerAv73"));
        statement.writeField(REBasesCalcul.FIELDNAME_MOIS_COT_ANNEE_OUVERT_DROIT,
                this._dbWriteNumeric(statement.getTransaction(), moisCotiAnneeOuvertDroit, "moisCotiAnneeOuvertDroit"));
        statement.writeField(REBasesCalcul.FIELDNAME_DROIT_APPLIQUE,
                this._dbWriteString(statement.getTransaction(), droitApplique, "droitApplique"));

        if (isRevenuSplitte != null) {
            statement.writeField(REBasesCalcul.FIELDNAME_IS_REVENU_SPLITTE, this._dbWriteBoolean(
                    statement.getTransaction(), isRevenuSplitte, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRevenuSplitte"));
        }

        statement.writeField(
                REBasesCalcul.FIELDNAME_ANNEE_BONIF_TACHE_EDUCATION,
                this._dbWriteString(statement.getTransaction(),
                        PRDateFormater.convertDate_AAxMM_to_AAMM(anneeBonifTacheEduc), "anneeBonifTacheEduc"));

        statement.writeField(REBasesCalcul.FIELDNAME_BONIF_TACHE_EDUCATION_1,
                this._dbWriteString(statement.getTransaction(), nombreAnneeBTE1, "nombreAnneeBTE1"));
        statement.writeField(REBasesCalcul.FIELDNAME_BONIF_TACHE_EDUCATION_2,
                this._dbWriteString(statement.getTransaction(), nombreAnneeBTE2, "nombreAnneeBTE2"));
        statement.writeField(REBasesCalcul.FIELDNAME_BONIF_TACHE_EDUCATION_4,
                this._dbWriteString(statement.getTransaction(), nombreAnneeBTE4, "nombreAnneeBTE4"));

        statement.writeField(REBasesCalcul.FIELDNAME_ANNEE_BONIF_TACHES_ASSISTANCE, this._dbWriteString(
                statement.getTransaction(), PRDateFormater.convertDate_AAxMM_to_AAMM(anneeBonifTacheAssistance),
                "anneeBonifTacheAssistance"));
        statement.writeField(REBasesCalcul.FIELDNAME_ANNEE_BONIF_TRANSITOIRE,
                this._dbWriteString(statement.getTransaction(), anneeBonifTransitoire, "anneeBonifTransitoire"));
        statement.writeField(REBasesCalcul.FIELDNAME_SUPPLEMENT_CARRIERE,
                this._dbWriteNumeric(statement.getTransaction(), supplementCarriere, "supplementCarriere"));
        statement.writeField(REBasesCalcul.FIELDNAME_TYPE_CALCUL_COMPARATIF,
                this._dbWriteString(statement.getTransaction(), typeCalculComparatif, "typeCalculComparatif"));
        statement.writeField(REBasesCalcul.FIELDNAME_RESULTAT_COMPARAISON,
                this._dbWriteString(statement.getTransaction(), resultatComparatif, "resultatComparatif"));
        statement.writeField(REBasesCalcul.FIELDNAME_MONTANT_MAX_R10_ECHELLE_44,
                this._dbWriteNumeric(statement.getTransaction(), montantMaxR10Ech44, "montantMaxR10Ech44"));
        statement.writeField(REBasesCalcul.FIELDNAME_ANNEE_TRAITEMENT,
                this._dbWriteNumeric(statement.getTransaction(), anneeTraitement, "anneeTraitement"));
        statement.writeField(
                REBasesCalcul.FIELDNAME_PERIODE_ASS_ETRANGERE_DES_73,
                this._dbWriteString(statement.getTransaction(),
                        PRDateFormater.convertDate_AAxMM_to_AAMM(periodeAssEtrangerDes73), "periodeAssEtrangerDes73"));
        statement.writeField(REBasesCalcul.FIELDNAME_FACTEUR_REVALORISATION,
                this._dbWriteString(statement.getTransaction(), facteurRevalorisation, "facteurRevalorisation"));
        statement.writeField(REBasesCalcul.FIELDNAME_CS_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), csEtat, "etat"));

        if (isPartageRevenuCalcul != null) {
            statement.writeField(REBasesCalcul.FIELDNAME_IS_PARTAGE_REVENU_CALCUL, this._dbWriteBoolean(
                    statement.getTransaction(), isPartageRevenuCalcul, BConstants.DB_TYPE_BOOLEAN_CHAR,
                    "isPartageRevenuCalcul"));
        }

        statement.writeField(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL,
                this._dbWriteNumeric(statement.getTransaction(), idTiersBaseCalcul, "idTiersBaseCalcul"));

        statement.writeField(REBasesCalcul.FIELDNAME_REFERENCE_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), referenceDecision, "referenceDecision"));

    }

    /**
     * @return
     */
    public String getAnneeBonifTacheAssistance() {
        return anneeBonifTacheAssistance;
    }

    /**
     * @return
     */
    public String getAnneeBonifTacheEduc() {
        return anneeBonifTacheEduc;
    }

    /**
     * @return
     */
    public String getAnneeBonifTransitoire() {
        return anneeBonifTransitoire;
    }

    /**
     * @return
     */
    public String getAnneeCotiClasseAge() {
        return anneeCotiClasseAge;
    }

    /**
     * @return
     */
    public String getAnneeDeNiveau() {
        return anneeDeNiveau;
    }

    /**
     * @return
     */
    public String getAnneeTraitement() {
        return anneeTraitement;
    }

    /**
     * @return
     */
    public String getCleInfirmiteAyantDroit() {
        return cleInfirmiteAyantDroit;
    }

    /**
     * @return
     */
    public String getCodeOfficeAi() {
        return codeOfficeAi;
    }

    /**
     * @return the etat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return
     */
    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    /**
     * @return
     */
    public String getQuotiteRente() {
        return quotiteRente;
    }

    /**
     * @return
     */
    public String getDroitApplique() {
        return droitApplique;
    }

    /**
     * @return
     */
    public String getDureeCotiAvant73() {
        return dureeCotiAvant73;
    }

    /**
     * @return
     */
    public String getDureeCotiDes73() {
        return dureeCotiDes73;
    }

    /**
     * @return
     */
    public String getDureeRevenuAnnuelMoyen() {
        return dureeRevenuAnnuelMoyen;
    }

    /**
     * @return
     */
    public String getEchelleRente() {
        return echelleRente;
    }

    /**
     * @return
     */
    public String getFacteurRevalorisation() {
        return facteurRevalorisation;
    }

    /**
     * @return
     */
    public String getIdBasesCalcul() {
        return idBasesCalcul;
    }

    /**
     * @return
     */
    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    /**
     * @return the isPartageRevenuActuel
     */
    public Boolean getIsPartageRevenuCalcul() {
        return isPartageRevenuCalcul;
    }

    /**
     * @return
     */
    public String getMoisAppointsAvant73() {
        return moisAppointsAvant73;
    }

    /**
     * @return
     */
    public String getMoisAppointsDes73() {
        return moisAppointsDes73;
    }

    /**
     * @return
     */
    public String getMoisCotiAnneeOuvertDroit() {
        return moisCotiAnneeOuvertDroit;
    }

    /**
     * @return
     */
    public String getMontantMaxR10Ech44() {
        return montantMaxR10Ech44;
    }

    public String getNombreAnneeBTE1() {
        return nombreAnneeBTE1;
    }

    public String getNombreAnneeBTE2() {
        return nombreAnneeBTE2;
    }

    public String getNombreAnneeBTE4() {
        return nombreAnneeBTE4;
    }

    /**
     * @return
     */
    public String getPeriodeAssEtrangerAv73() {
        return periodeAssEtrangerAv73;
    }

    /**
     * @return
     */
    public String getPeriodeAssEtrangerDes73() {
        return periodeAssEtrangerDes73;
    }

    /**
     * @return
     */
    public String getPeriodeJeunesse() {
        return periodeJeunesse;
    }

    /**
     * @return
     */
    public String getPeriodeMariage() {
        return periodeMariage;
    }

    public String getReferenceDecision() {
        return referenceDecision;
    }

    /**
     * @return
     */
    public String getResultatComparatif() {
        return resultatComparatif;
    }

    /**
     * @return
     */
    public String getRevenuAnnuelMoyen() {

        FWCurrency currency = new FWCurrency(revenuAnnuelMoyen);
        int i = currency.intValue();

        return String.valueOf(i);
    }

    /**
     * @return
     */
    public String getRevenuJeunesse() {
        return revenuJeunesse;
    }

    /**
     * @return
     */
    public String getRevenuPrisEnCompte() {
        return revenuPrisEnCompte;
    }

    /**
     * @return
     */
    public String getSupplementCarriere() {
        return supplementCarriere;
    }

    /**
     * @return
     */
    public String getSurvenanceEvtAssAyantDroit() {
        return survenanceEvtAssAyantDroit;
    }

    /**
     * @return
     */
    public String getTypeCalculComparatif() {
        return typeCalculComparatif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasSpy()
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    public Boolean isDemandeRenteAPI() {
        return isDemandeRenteAPI;
    }

    public boolean isDemandeRenteCsEtatNonValide() {

        REDemandeRenteManager drManager = new REDemandeRenteManager();
        drManager.setSession(getSession());
        drManager.setForIdRenteCalculee(getIdRenteCalculee());
        try {
            drManager.find();
        } catch (Exception e) {
            return true;
        }

        if (drManager.getSize() > 0) {

            REDemandeRente dr = (REDemandeRente) drManager.getFirstEntity();

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(dr.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL.equals(dr.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(dr.getCsEtat())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return
     */
    public Boolean isInvaliditePrecoce() {
        return isInvaliditePrecoce;
    }

    /**
     * @return
     */
    public Boolean isLimiteRevenu() {
        return isLimiteRevenu;
    }

    /**
     * @return
     */
    public Boolean isMinimuGaranti() {
        return isMinimuGaranti;
    }

    /**
     * @return
     */
    public Boolean isRevenuSplitte() {
        return isRevenuSplitte;
    }

    /**
     * @param string
     */
    public void setAnneeBonifTacheAssistance(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        anneeBonifTacheAssistance = string;
    }

    /**
     * @param string
     */
    public void setAnneeBonifTacheEduc(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        anneeBonifTacheEduc = string;
    }

    /**
     * @param string
     */
    public void setAnneeBonifTransitoire(String string) {
        anneeBonifTransitoire = string;
    }

    /**
     * @param string
     */
    public void setAnneeCotiClasseAge(String string) {
        anneeCotiClasseAge = string;
    }

    /**
     * @param string
     */
    public void setAnneeDeNiveau(String string) {
        anneeDeNiveau = string;
    }

    /**
     * @param string
     */
    public void setAnneeTraitement(String string) {
        anneeTraitement = string;
    }

    /**
     * @param string
     */
    public void setCleInfirmiteAyantDroit(String string) {
        cleInfirmiteAyantDroit = string;
    }

    /**
     * @param string
     */
    public void setCodeOfficeAi(String string) {
        codeOfficeAi = string;
    }

    /**
     * @param etat
     *            the etat to set
     */
    public void setCsEtat(String etat) {
        csEtat = etat;
    }

    /**
     * @param string
     */
    public void setDegreInvalidite(String string) {
        degreInvalidite = string;
    }

    /**
     * @param string
     */
    public void setQuotiteRente(String string) {
        quotiteRente = string;
    }

    /**
     * @param string
     */
    public void setDroitApplique(String string) {
        final String numRevisionXML = "09";
        if (string.equals(numRevisionXML)) {
            droitApplique = "9";
        } else {
            droitApplique = string;
        }
    }

    /**
     * @param string
     */
    public void setDureeCotiAvant73(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        dureeCotiAvant73 = string;
    }

    /**
     * @param string
     */
    public void setDureeCotiDes73(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        dureeCotiDes73 = string;
    }

    /**
     * @param string
     */
    public void setDureeRevenuAnnuelMoyen(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        dureeRevenuAnnuelMoyen = string;
    }

    /**
     * @param string
     */
    public void setEchelleRente(String string) {
        echelleRente = string;
    }

    /**
     * @param string
     */
    public void setFacteurRevalorisation(String string) {
        facteurRevalorisation = string;
    }

    /**
     * @param string
     */
    public void setIdBasesCalcul(String string) {
        idBasesCalcul = string;
    }

    /**
     * @param string
     */
    public void setIdRenteCalculee(String string) {
        idRenteCalculee = string;
    }

    public void setIdTiersBaseCalcul(String idTiersBaseCalcul) {
        this.idTiersBaseCalcul = idTiersBaseCalcul;
    }

    /**
     * @param b
     */
    public void setInvaliditePrecoce(Boolean b) {
        isInvaliditePrecoce = b;
    }

    public void setIsDemandeRenteAPI(Boolean isDemandeRenteAPI) {
        this.isDemandeRenteAPI = isDemandeRenteAPI;
    }

    /**
     * @param isPartageRevenuCalcul
     *            the isPartageRevenuCalcul to set
     */
    public void setIsPartageRevenuActuel(Boolean isPartageRevenuCalcul) {
        this.isPartageRevenuCalcul = isPartageRevenuCalcul;
    }

    /**
     * @param b
     */
    public void setLimiteRevenu(Boolean b) {
        isLimiteRevenu = b;
    }

    /**
     * @param b
     */
    public void setMinimuGaranti(Boolean b) {
        isMinimuGaranti = b;
    }

    /**
     * @param string
     */
    public void setMoisAppointsAvant73(String string) {
        moisAppointsAvant73 = string;
    }

    /**
     * @param string
     */
    public void setMoisAppointsDes73(String string) {
        moisAppointsDes73 = string;
    }

    /**
     * @param string
     */
    public void setMoisCotiAnneeOuvertDroit(String string) {
        moisCotiAnneeOuvertDroit = string;
    }

    /**
     * @param string
     */
    public void setMontantMaxR10Ech44(String string) {
        montantMaxR10Ech44 = string;
    }

    public void setNombreAnneeBTE1(String nombreAnneeBTE1) {
        this.nombreAnneeBTE1 = nombreAnneeBTE1;
    }

    public void setNombreAnneeBTE2(String nombreAnneeBTE2) {
        this.nombreAnneeBTE2 = nombreAnneeBTE2;
    }

    public void setNombreAnneeBTE4(String nombreAnneeBTE4) {
        this.nombreAnneeBTE4 = nombreAnneeBTE4;
    }

    /**
     * @param string
     */
    public void setPeriodeAssEtrangerAv73(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        periodeAssEtrangerAv73 = string;
    }

    /**
     * @param string
     */
    public void setPeriodeAssEtrangerDes73(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        periodeAssEtrangerDes73 = string;
    }

    /**
     * @param string
     */
    public void setPeriodeJeunesse(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        periodeJeunesse = string;
    }

    /**
     * @param string
     */
    public void setPeriodeMariage(String string) {

        if (string.length() == 4) {
            string = PRDateFormater.convertDate_AAMM_to_AAxMM(string);
        }

        periodeMariage = string;
    }

    public void setReferenceDecision(String referenceDecision) {
        this.referenceDecision = referenceDecision;
    }

    /**
     * @param string
     */
    public void setResultatComparatif(String string) {
        resultatComparatif = string;
    }

    /**
     * @param string
     */
    public void setRevenuAnnuelMoyen(String string) {
        revenuAnnuelMoyen = string;
    }

    /**
     * @param string
     */
    public void setRevenuJeunesse(String string) {
        revenuJeunesse = string;
    }

    /**
     * @param string
     */
    public void setRevenuPrisEnCompte(String string) {
        revenuPrisEnCompte = string;
    }

    /**
     * @param b
     */
    public void setRevenuSplitte(Boolean b) {
        isRevenuSplitte = b;
    }

    /**
     * @param string
     */
    public void setSupplementCarriere(String string) {
        supplementCarriere = string;
    }

    /**
     * @param string
     */
    public void setSurvenanceEvtAssAyantDroit(String string) {

        if (string.length() == 6) {
            string = PRDateFormater.convertDate_MMAAAA_to_MMxAAAA(string);
        }

        survenanceEvtAssAyantDroit = string;
    }

    /**
     * @param string
     */
    public void setTypeCalculComparatif(String string) {
        typeCalculComparatif = string;
    }
}
