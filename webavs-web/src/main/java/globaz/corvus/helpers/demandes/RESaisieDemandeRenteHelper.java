package globaz.corvus.helpers.demandes;

import globaz.corvus.acor.parser.rev09.REACORParser;
import globaz.corvus.api.basescalcul.IREBasesCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordee;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordeeManager;
import globaz.corvus.db.demandes.REDemandeRenteSurvivant;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.REPeriodeAPI;
import globaz.corvus.db.demandes.REPeriodeAPIManager;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeFamille;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeFamilleManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.helpers.basescalcul.RESaisieManuelleRABCPDHelper;
import globaz.corvus.topaz.REAccuseDeReceptionOO;
import globaz.corvus.utils.REDemandeUtils;
import globaz.corvus.utils.REEcheancesUtils;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.demandes.REDemandeRenteViewBean;
import globaz.corvus.vb.demandes.REInfoComplViewBean;
import globaz.corvus.vb.demandes.REPeriodeAPIViewBean;
import globaz.corvus.vb.demandes.REPeriodeInvaliditeViewBean;
import globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.ged.AirsConstants;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.JadeTarget;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.api.IPRInfoCompl;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRDateValidator;
import globaz.prestation.utils.ged.PRGedUtils;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.corvus.process.echeances.analyseur.modules.REModuleAnalyseEcheanceUtils;

/**
 * @author HPE
 */

public class RESaisieDemandeRenteHelper extends PRAbstractHelper {

    private static final int AGE_AVS_FEMME = 64;
    private static final int AGE_AVS_HOMME = 65;
    protected boolean hasBeanInsertedIntoTiers = false;

    public FWViewBeanInterface actionAfficherPeriodeAPI(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;
        Set<REPeriodeAPIViewBean> listePeriodeAPI = vb.getPeriodesAPI();
        REPeriodeAPIViewBean periodeAPIDelete = new REPeriodeAPIViewBean();

        // Reprendre la bonne entité de la liste et la setter dans le viewBean
        for (REPeriodeAPIViewBean periodeAPIVB : listePeriodeAPI) {

            if (periodeAPIVB.getIdProvisoire() == vb.getIdProvisoirePeriodeAPI().intValue()) {

                vb.setDateDebutImpotence(periodeAPIVB.getDateDebutInvalidite());
                vb.setDateFinImpotence(periodeAPIVB.getDateFinInvalidite());
                vb.setCsDegreImpotence(periodeAPIVB.getCsDegreImpotence());
                vb.setIsResidenceDansHome(periodeAPIVB.getIsResidenceHome());
                vb.setIsAssistancePratique(periodeAPIVB.getIsAssistancePratique());
                vb.setCsGenreDroitApi(periodeAPIVB.getCsGenreDroitApi());
                vb.setTypePrestationHistorique(periodeAPIVB.getTypePrestationHistorique());
                vb.setIdPeriodeAPI(periodeAPIVB.getIdPeriodeAPI());

                periodeAPIDelete = periodeAPIVB;
            }
        }
        listePeriodeAPI.remove(periodeAPIDelete);
        vb.setModifie(true);
        return vb;
    }

    public FWViewBeanInterface actionAfficherPeriodeINV(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;
        Set<REPeriodeInvaliditeViewBean> listePeriodeINV = vb.getPeriodesInvalidite();
        REPeriodeInvaliditeViewBean periodeInvDelete = new REPeriodeInvaliditeViewBean();

        // Reprendre la bonne entité de la liste et la setter dans le viewBean
        for (REPeriodeInvaliditeViewBean periodeINVVB : listePeriodeINV) {

            if (periodeINVVB.getIdProvisoire() == vb.getIdProvisoirePeriodeINV().intValue()) {
                vb.setDateDebutInvalidite(periodeINVVB.getDateDebutInvalidite());
                vb.setDateFinInvalidite(periodeINVVB.getDateFinInvalidite());
                vb.setDegreInvalidite(periodeINVVB.getDegreInvalidite());
                vb.setIdPeriodeInvalidite(periodeINVVB.getIdPeriodeInvalidite());
                periodeInvDelete = periodeINVVB;
            }
        }
        listePeriodeINV.remove(periodeInvDelete);
        vb.setModifie(true);
        return vb;
    }

    private void checkAPIAI(REPeriodeAPIViewBean periodeAPIVB, BSession session) {

        String degreImpotence = periodeAPIVB.getCsDegreImpotence();
        boolean isResidenceHome = periodeAPIVB.getIsResidenceHome();
        boolean isAssistancePratique = periodeAPIVB.getIsAssistancePratique();

        String labelOuiNonResidenceHome = session.getLabel("OUI");
        if (!isResidenceHome) {
            labelOuiNonResidenceHome = session.getLabel("NON");
        }

        String labelOuiNonAssistancePratique = session.getLabel("OUI");
        if (!isAssistancePratique) {
            labelOuiNonAssistancePratique = session.getLabel("NON");
        }

        boolean isAPIAIImpossible = isResidenceHome && isAssistancePratique;
        isAPIAIImpossible = isAPIAIImpossible
                || (IREDemandeRente.CS_DEGRE_IMPOTENCE_GRAVE.equalsIgnoreCase(degreImpotence) && !isResidenceHome && isAssistancePratique);

        if (isAPIAIImpossible) {
            session.addError(FWMessageFormat.format(session.getLabel("VALID_SDR_API_PERIODE_API_AI_IMPOSSIBLE"),
                    session.getCodeLibelle(degreImpotence), labelOuiNonResidenceHome, labelOuiNonAssistancePratique));
        }

    }

    private void checkAPIAVS(REPeriodeAPIViewBean periodeAPIVB, String typePrestationAnterieur, BSession session)
            throws Exception {

        // Si période créée avec le degré d'impotence faible et avec le genre de
        // droit API (API AVS dont le droit est né après l'age
        // légal de la retraite, message d'erreur
        if (IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_RETRAITE.equals(periodeAPIVB.getCsGenreDroitApi())
                && IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equals(periodeAPIVB.getCsDegreImpotence())) {

            // Autorisé dès le 01.01.2011
            JADate dateDebut = new JADate(periodeAPIVB.getDateDebutInvalidite());
            JADate dateOK = new JADate("01.01.2011");

            if (BSessionUtil.compareDateFirstLower(session, dateDebut.toStr("."), dateOK.toStr("."))) {
                session.addError(session.getLabel("VALID_SDR_H_API_AVS_FAIBLE_INTERDITE_AGE_AVS"));
            }

        }

        String degreImpotence = periodeAPIVB.getCsDegreImpotence();
        boolean isResidenceHome = periodeAPIVB.getIsResidenceHome();
        String csGenreDroitAPI = periodeAPIVB.getCsGenreDroitApi();

        String labelOuiNonResidenceHome = session.getLabel("OUI");
        if (!isResidenceHome) {
            labelOuiNonResidenceHome = session.getLabel("NON");
        }

        boolean isAPIAVSImpossible = IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equalsIgnoreCase(degreImpotence)
                && isResidenceHome
                && IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_RETRAITE.equalsIgnoreCase(csGenreDroitAPI);

        if (isAPIAVSImpossible) {
            session.addError(FWMessageFormat.format(session.getLabel("VALID_SDR_API_PERIODE_API_AVS_IMPOSSIBLE"),
                    session.getCodeLibelle(degreImpotence)));
        }

        boolean isDegreImpotenceFaible = IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equalsIgnoreCase(degreImpotence);
        boolean isDegreImpotenceMoyen = IREDemandeRente.CS_DEGRE_IMPOTENCE_MOYEN.equalsIgnoreCase(degreImpotence);
        boolean isDegreImpotenceGrave = IREDemandeRente.CS_DEGRE_IMPOTENCE_GRAVE.equalsIgnoreCase(degreImpotence);

        boolean isGenreDroitAPI3 = IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_SUITE_API_AI
                .equalsIgnoreCase(csGenreDroitAPI);

        boolean isAPIAI81Or84Anterieur = "81".equalsIgnoreCase(typePrestationAnterieur)
                || "84".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI82Or88Anterieur = "82".equalsIgnoreCase(typePrestationAnterieur)
                || "88".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI83Anterieur = "83".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI91Anterieur = "91".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI92Anterieur = "92".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI93Anterieur = "93".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAVS95Anterieur = "95".equalsIgnoreCase(typePrestationAnterieur);

        boolean isDateDebutPeriodeGreaterEgalJuillet2014 = JadeDateUtil.isDateAfter(
                periodeAPIVB.getDateDebutInvalidite(), "30.06.2014");

        if (isGenreDroitAPI3) {

            if (isDegreImpotenceFaible && !isResidenceHome & !isAPIAI81Or84Anterieur) {
                session.addError(FWMessageFormat.format(
                        session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AVS_3_SANS_81_84"),
                        session.getCodeLibelle(degreImpotence), labelOuiNonResidenceHome));
            } else if (isDegreImpotenceMoyen && !isResidenceHome & !isAPIAI82Or88Anterieur) {
                session.addError(FWMessageFormat.format(
                        session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AVS_3_SANS_82_88"),
                        session.getCodeLibelle(degreImpotence), labelOuiNonResidenceHome));
            } else if (isDegreImpotenceGrave && !isResidenceHome & !isAPIAI83Anterieur) {
                session.addError(FWMessageFormat.format(
                        session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AVS_3_SANS_83"),
                        session.getCodeLibelle(degreImpotence), labelOuiNonResidenceHome));
            } else if (isDegreImpotenceFaible && isResidenceHome & !isAPIAI91Anterieur
                    && !isDateDebutPeriodeGreaterEgalJuillet2014) {
                session.addError(FWMessageFormat.format(
                        session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AVS_3_SANS_91_AVANT_JUILLET_14"),
                        session.getCodeLibelle(degreImpotence), labelOuiNonResidenceHome));
            } else if (isDegreImpotenceFaible && isResidenceHome & !isAPIAI91Anterieur && !isAPIAVS95Anterieur
                    && isDateDebutPeriodeGreaterEgalJuillet2014) {
                session.addError(FWMessageFormat.format(
                        session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AVS_3_SANS_91_OR_95_APRES_JUILLET_14"),
                        session.getCodeLibelle(degreImpotence), labelOuiNonResidenceHome));
            } else if (isDegreImpotenceMoyen && isResidenceHome & !isAPIAI92Anterieur) {
                session.addError(FWMessageFormat.format(
                        session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AVS_3_SANS_92"),
                        session.getCodeLibelle(degreImpotence), labelOuiNonResidenceHome));
            } else if (isDegreImpotenceGrave && isResidenceHome & !isAPIAI93Anterieur) {
                session.addError(FWMessageFormat.format(
                        session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AVS_3_SANS_93"),
                        session.getCodeLibelle(degreImpotence), labelOuiNonResidenceHome));
            }
        }

    }

    private void checkAgeAVS(BSession session, String idTiers, String dateDebutPeriode, String dateFinPeriode,
            String csGenreDroitAPI) throws Exception {

        JACalendarGregorian calendrier = new JACalendarGregorian();
        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, idTiers);
        String dateNaissance = tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
        String csSexe = tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE);
        String moisAnneeDebutPeriode = JACalendar.format(dateDebutPeriode, JACalendar.FORMAT_MMsYYYY);
        String moisAnneeDernierPaiement = REPmtMensuel.getDateDernierPmt(session);
        String dateAgeAvs = PRTiersHelper.getDateDebutDroitAVS(dateNaissance, csSexe);

        int resultCompareAgeAVSDebutPeriode = REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(csSexe,
                dateNaissance, moisAnneeDebutPeriode);

        int resultCompareAgeAVSDernierPaiement = REModuleAnalyseEcheanceUtils.compareAgeAvsAgeMoisCourant(csSexe,
                dateNaissance, moisAnneeDernierPaiement);

        boolean isPeriodeAPIAI = IREDemandeRente.CS_GENRE_DROIT_API_API_AI_PRST.equalsIgnoreCase(csGenreDroitAPI)
                || IREDemandeRente.CS_GENRE_DROIT_API_API_AI_RENTE.equalsIgnoreCase(csGenreDroitAPI);
        boolean isPeriodeAPIAVS = IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_SUITE_API_AI
                .equalsIgnoreCase(csGenreDroitAPI)
                || IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_RETRAITE.equalsIgnoreCase(csGenreDroitAPI);

        if ((resultCompareAgeAVSDebutPeriode == -1 || resultCompareAgeAVSDebutPeriode == 0) && !isPeriodeAPIAI) {
            session.addError(session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AVS_AVANT_DROIT_AVS") + " "
                    + dateAgeAvs);

        }

        if (resultCompareAgeAVSDebutPeriode == 1 && !isPeriodeAPIAVS) {
            session.addError(session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AI_APRES_DROIT_AVS") + " " + dateAgeAvs);
        }

        if (isPeriodeAPIAI
                && (calendrier.compare(dateFinPeriode, dateAgeAvs) == JACalendar.COMPARE_FIRSTUPPER || calendrier
                        .compare(dateFinPeriode, dateAgeAvs) == JACalendar.COMPARE_EQUALS)) {
            session.addError(session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AI_DATE_FIN_APRES_DROIT_AVS") + " "
                    + dateAgeAvs);
        }

        if ((resultCompareAgeAVSDernierPaiement == 0 || resultCompareAgeAVSDernierPaiement == 1) && isPeriodeAPIAI) {
            if (JadeStringUtil.isBlankOrZero(dateFinPeriode)) {
                session.addError(session
                        .getLabel("VALID_SDR_API_ERREUR_PERIODE_API_AI_SANS_DATE_FIN_DEJA_AVS_DERNIER_PAIEMENT")
                        + " "
                        + moisAnneeDernierPaiement);
            }

        }

    }

    public FWViewBeanInterface actionAjouterPeriodeAPI(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        RESaisieDemandeRenteViewBean saisieDemandeRenteVB = (RESaisieDemandeRenteViewBean) viewBean;
        Set<REPeriodeAPIViewBean> periodesAPI = saisieDemandeRenteVB.getPeriodesAPI();
        REPeriodeAPIViewBean periodeAPIVB = new REPeriodeAPIViewBean();

        // Création d'une période selon les éléments du viewBean pour stocker
        // dans la liste provisoire
        periodeAPIVB.setDateDebutInvalidite(saisieDemandeRenteVB.getDateDebutImpotence());
        periodeAPIVB.setDateFinInvalidite(saisieDemandeRenteVB.getDateFinImpotence());
        periodeAPIVB.setCsDegreImpotence(saisieDemandeRenteVB.getCsDegreImpotence());
        periodeAPIVB.setIsResidenceHome(saisieDemandeRenteVB.getIsResidenceDansHome());
        periodeAPIVB.setIsAssistancePratique(saisieDemandeRenteVB.getIsAssistancePratique());
        periodeAPIVB.setCsGenreDroitApi(saisieDemandeRenteVB.getCsGenreDroitApi());
        periodeAPIVB.setTypePrestationHistorique(saisieDemandeRenteVB.getTypePrestationHistorique());
        periodeAPIVB.setIdDemandeRente(saisieDemandeRenteVB.getIdDemandeRente());
        periodeAPIVB.setIdPeriodeAPI(saisieDemandeRenteVB.getIdPeriodeAPI());

        // Tous les tests de validation

        // 1. Voir si les champs sont tous remplis
        if (JadeStringUtil.isEmpty(periodeAPIVB.getDateDebutInvalidite())) {
            session.addError(session.getLabel("VALID_SDR_H_DATE_DEBUT_API_VIDE"));
        }

        // 2. Vérifier le format des dates
        // 2a. Date de début d'invalidité
        boolean isDateDebutInvalidite;
        try {
            BSessionUtil.checkDateGregorian(session, periodeAPIVB.getDateDebutInvalidite());
            isDateDebutInvalidite = true;

            // Vérifier que ce soit bien dans le format JJ.MM.AAAA
            if ((periodeAPIVB.getDateDebutInvalidite().length() > 10)
                    || (periodeAPIVB.getDateDebutInvalidite().length() < 10)) {
                session.addError(session.getLabel("ERREUR_DATE_DEBUT_FORMAT"));
            }

        } catch (Exception e) {
            isDateDebutInvalidite = false;
        }

        if (!isDateDebutInvalidite) {
            session.addError(session.getLabel("VALID_SDR_H_DATE_DEBUT_API_INCORRECTE"));
        }
        // 2b. Date de fin d'invalidité
        boolean isDateFinInvalidite;
        try {
            BSessionUtil.checkDateGregorian(session, periodeAPIVB.getDateFinInvalidite());
            isDateFinInvalidite = true;

            // Vérifier que ce soit bien dans le format JJ.MM.AAAA
            if (!JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateFinInvalidite())) {
                if ((periodeAPIVB.getDateFinInvalidite().length() > 10)
                        || (periodeAPIVB.getDateFinInvalidite().length() < 10)) {
                    session.addError(session.getLabel("ERREUR_DATE_FIN_DROIT_FORMAT"));
                }
            }

        } catch (Exception e) {
            isDateFinInvalidite = false;
        }

        if (!isDateFinInvalidite) {
            session.addError(session.getLabel("VALID_SDR_H_DATE_FIN_API_INCORRECTE"));
        }

        // 4. Diverses vérifications des dates

        // Si date de fin supérieur au mois du dernier paiement mensuel,
        // erreur...
        if (!JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateFinInvalidite())) {
            String datePmtMensuel = REPmtMensuel.getDateDernierPmt(session);
            String dateFinAPI = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(periodeAPIVB.getDateFinInvalidite());
            JACalendar cal = new JACalendarGregorian();

            if (cal.compare(dateFinAPI, datePmtMensuel) == JACalendar.COMPARE_FIRSTUPPER) {
                session.addError(session.getLabel("VALID_SDR_H_DATE_FIN_API_SUP_DATE_DER_PMT"));
            }

        }

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // VALIDATION DE LA PERIODE
        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (!JadeStringUtil.isEmpty(periodeAPIVB.getDateFinInvalidite())
                && !JadeStringUtil.isEmpty(periodeAPIVB.getDateDebutInvalidite())) {

            // 4b. Date de fin doit être supérieure ou égale à la date du début
            if (Double.valueOf(new JADate(periodeAPIVB.getDateFinInvalidite()).toStrAMJ()).doubleValue() < Double
                    .valueOf(new JADate(periodeAPIVB.getDateDebutInvalidite()).toStrAMJ()).doubleValue()) {

                session.addError(session.getLabel("VALID_SDR_H_DATE_FIN_API_POST_DATE_DEBUT_INV"));
            }
        }

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // VALIDATION DE LA PERIODE
        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (!JadeStringUtil.isEmpty(periodeAPIVB.getDateDebutInvalidite())) {

            // 4c. On parcours toute la liste pour savoir si la nouvelle période
            // est pendant une autre période déjà dans la liste
            for (REPeriodeAPIViewBean perAPIVb : periodesAPI) {

                /*
                 * Général : ---------
                 * 
                 * periodeAPIVB = nouvelle période = np perAPIVb = periode de la liste déjà entrée = pl
                 * 
                 * 
                 * Cas possibles pour insertion période !
                 * 
                 * 1) Si pl avec dd et df
                 * 
                 * -> a) dateFin np avant dateDebut pl -> b) dateDebut np après dateFin pl
                 * 
                 * -> c) dateDebut np après dateFin pl (sans dateFin np)
                 * 
                 * 2) Si pl avec dd et pas de df
                 * 
                 * -> a) dateFin np avant dateDebut pl
                 * 
                 * -> d) si sans DateFin = impossible
                 */

                boolean isPeriodeOK = false;

                JACalendar cal = new JACalendarGregorian();

                // 1) si pl avec dd et df
                if (!JadeStringUtil.isBlankOrZero(perAPIVb.getDateDebutInvalidite())
                        && !JadeStringUtil.isBlankOrZero(perAPIVb.getDateFinInvalidite())) {

                    // Si np avec dd et df
                    if (!JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateDebutInvalidite())
                            && !JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateFinInvalidite())) {

                        // a)
                        if (cal.compare(periodeAPIVB.getDateFinInvalidite(), perAPIVb.getDateDebutInvalidite()) == JACalendar.COMPARE_FIRSTLOWER) {
                            isPeriodeOK = true;
                        }

                        // b)
                        if (cal.compare(periodeAPIVB.getDateDebutInvalidite(), perAPIVb.getDateFinInvalidite()) == JACalendar.COMPARE_FIRSTUPPER) {
                            isPeriodeOK = true;
                        }
                    } else if (!JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateDebutInvalidite())
                            && JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateFinInvalidite())) {

                        // c)
                        if (cal.compare(periodeAPIVB.getDateDebutInvalidite(), perAPIVb.getDateFinInvalidite()) == JACalendar.COMPARE_FIRSTUPPER) {
                            isPeriodeOK = true;
                        }

                    }

                } else if (!JadeStringUtil.isBlankOrZero(perAPIVb.getDateDebutInvalidite())
                        && JadeStringUtil.isBlankOrZero(perAPIVb.getDateFinInvalidite())) {

                    // Si np avec dd et df
                    if (!JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateDebutInvalidite())
                            && !JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateFinInvalidite())) {

                        // a)
                        if (cal.compare(periodeAPIVB.getDateFinInvalidite(), perAPIVb.getDateDebutInvalidite()) == JACalendar.COMPARE_FIRSTLOWER) {
                            isPeriodeOK = true;
                        }

                    }

                }

                if (!isPeriodeOK) {
                    session.addError(session.getLabel("ERREUR_SAISIE_PERIODE"));
                }

            }
        }

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // VALIDATION
        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        boolean isBefore2004 = BSessionUtil.compareDateFirstLower(session, periodeAPIVB.getDateDebutInvalidite(),
                "01.01.2004");

        String typePrestationPrecedent = "";
        String typePrestation = "";

        if (!isBefore2004) {

            if (JadeStringUtil.isBlankOrZero(periodeAPIVB.getCsGenreDroitApi())) {
                session.addError(session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_SANS_GENRE_DROIT_API"));
            }

            if (JadeStringUtil.isBlankOrZero(saisieDemandeRenteVB.getIdTiers())) {
                session.addError(session.getLabel("VALID_SDR_API_ERREUR_PERIODE_API_SANS_TIERS"));
            }

            for (REPeriodeAPIViewBean aPeriodeAPI : periodesAPI) {
                if (BSessionUtil.compareDateFirstLowerOrEqual(session, periodeAPIVB.getDateDebutInvalidite(),
                        aPeriodeAPI.getDateDebutInvalidite())) {
                    session.addError(session.getLabel("VALID_SDR_API_ERREUR_AJOUT_PERIODE_API_ANTERIEURE"));
                    break;
                }

            }

            if (IREDemandeRente.CS_GENRE_DROIT_API_API_AI_PRST.equals(saisieDemandeRenteVB.getCsGenreDroitApi())
                    || IREDemandeRente.CS_GENRE_DROIT_API_API_AI_RENTE
                            .equals(saisieDemandeRenteVB.getCsGenreDroitApi())) {

                checkAPIAI(periodeAPIVB, session);

            } else {
                typePrestationPrecedent = getTypePrestationAPIPrecedent(saisieDemandeRenteVB.getIdTiers(), periodesAPI,
                        periodeAPIVB, session);

                boolean isFaibleHome3After91 = IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equalsIgnoreCase(periodeAPIVB
                        .getCsDegreImpotence());
                isFaibleHome3After91 = isFaibleHome3After91 && periodeAPIVB.getIsResidenceHome();
                isFaibleHome3After91 = isFaibleHome3After91
                        && IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_SUITE_API_AI.equalsIgnoreCase(periodeAPIVB
                                .getCsGenreDroitApi());
                isFaibleHome3After91 = isFaibleHome3After91 && "91".equalsIgnoreCase(typePrestationPrecedent);

                boolean isJuillet14IncludeInPeriode = JadeDateUtil.isDateBefore(periodeAPIVB.getDateDebutInvalidite(),
                        "01.07.2014");
                isJuillet14IncludeInPeriode = isJuillet14IncludeInPeriode
                        && (JadeStringUtil.isBlankOrZero(periodeAPIVB.getDateFinInvalidite()) || JadeDateUtil
                                .isDateAfter(periodeAPIVB.getDateFinInvalidite(), "30.06.2014"));

                if (isFaibleHome3After91 && isJuillet14IncludeInPeriode) {
                    session.addError(session.getLabel("VALID_SDR_API_ERREUR_PERIODE_95_AVEC_91_ENGLOBANT_JUILLET_2014"));
                }

                periodeAPIVB.setIsAssistancePratique(Boolean.FALSE);
                checkAPIAVS(periodeAPIVB, typePrestationPrecedent, session);

            }

            checkAgeAVS(session, saisieDemandeRenteVB.getIdTiers(), periodeAPIVB.getDateDebutInvalidite(),
                    periodeAPIVB.getDateFinInvalidite(), periodeAPIVB.getCsGenreDroitApi());
        }

        if (!session.hasErrors()) {

            periodeAPIVB.setTypePrestationHistorique(typePrestationPrecedent);
            typePrestation = getTypePrestationAPI(periodeAPIVB, session);
            periodeAPIVB.setTypePrestation(typePrestation);

            Integer noProv = saisieDemandeRenteVB.getNextKeyPeriodeAPI();

            if (JadeStringUtil.isEmpty(periodeAPIVB.getIdPeriodeAPI())) {
                periodeAPIVB.setIdProvisoire(noProv.intValue());
            } else {
                periodeAPIVB.setIdProvisoire(Integer.parseInt(periodeAPIVB.getIdPeriodeAPI()));
            }

            // ajout de la période en cours dans la liste provisoire
            periodesAPI.add(periodeAPIVB);

            saisieDemandeRenteVB.setDateDebutImpotence("");
            saisieDemandeRenteVB.setDateFinImpotence("");
            saisieDemandeRenteVB.setCsDegreImpotence("");
            saisieDemandeRenteVB.setIsResidenceDansHome(Boolean.FALSE);
            saisieDemandeRenteVB.setIsAssistancePratique(Boolean.FALSE);
            saisieDemandeRenteVB.setCsGenreDroitApi("");
            saisieDemandeRenteVB.setTypePrestationHistorique("");
            saisieDemandeRenteVB.setIdPeriodeAPI("");

        }

        saisieDemandeRenteVB.setModifie(true);
        return saisieDemandeRenteVB;
    }

    public FWViewBeanInterface actionAjouterPeriodeINV(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        RESaisieDemandeRenteViewBean saisieDemandeRenteVB = (RESaisieDemandeRenteViewBean) viewBean;
        Set<REPeriodeInvaliditeViewBean> periodesInvalidites = saisieDemandeRenteVB.getPeriodesInvalidite();
        REPeriodeInvaliditeViewBean periodeInvaliditeVB = new REPeriodeInvaliditeViewBean();

        // Création d'une période selon les éléments du viewBean pour stocker
        // dans la liste provisoire
        periodeInvaliditeVB.setDateDebutInvalidite(saisieDemandeRenteVB.getDateDebutInvalidite());
        periodeInvaliditeVB.setDateFinInvalidite(saisieDemandeRenteVB.getDateFinInvalidite());
        periodeInvaliditeVB.setDegreInvalidite(saisieDemandeRenteVB.getDegreInvalidite());
        periodeInvaliditeVB.setIdDemandeRente(saisieDemandeRenteVB.getIdDemandeRente());
        periodeInvaliditeVB.setIdPeriodeInvalidite(saisieDemandeRenteVB.getIdPeriodeInvalidite());

        // Tous les tests de validation

        // 1. Voir si les champs sont tous remplis
        if (JadeStringUtil.isEmpty(periodeInvaliditeVB.getDateDebutInvalidite())) {
            session.addError(session.getLabel("VALID_SDR_H_DATE_DEBUT_INV_VIDE"));
        }
        if (JadeStringUtil.isEmpty(periodeInvaliditeVB.getDegreInvalidite())) {
            session.addError(session.getLabel("VALID_SDR_H_DEGRE_INVALIDITE_VIDE"));
        } else {
            // 2. le degré doit être entre 40 et 100
            if ((Double.valueOf(periodeInvaliditeVB.getDegreInvalidite()).doubleValue() < 40)
                    || (Double.valueOf(periodeInvaliditeVB.getDegreInvalidite()).doubleValue() > 100)) {
                session.addError(session.getLabel("VALID_SDR_H_DEGRE_INVALIDITE_ENTRE_40_ET_100"));
            }
        }

        // 3. Vérifier le format des dates
        // 3a. Date de début d'invalidité
        boolean isDateDebutInvalidite;
        try {
            BSessionUtil.checkDateGregorian(session, periodeInvaliditeVB.getDateDebutInvalidite());
            isDateDebutInvalidite = true;

            // Vérifier que ce soit bien dans le format JJ.MM.AAAA
            if ((periodeInvaliditeVB.getDateDebutInvalidite().length() > 10)
                    || (periodeInvaliditeVB.getDateDebutInvalidite().length() < 10)) {
                session.addError(session.getLabel("ERREUR_DATE_DEBUT_DROIT_FORMAT"));
            }

        } catch (Exception e) {
            isDateDebutInvalidite = false;
        }

        if (!isDateDebutInvalidite) {
            session.addError(session.getLabel("VALID_SDR_H_DATE_DEBUT_INV_INCORRECTE"));
        }
        // 3b. Date de fin d'invalidité
        boolean isDateFinInvalidite;
        try {
            BSessionUtil.checkDateGregorian(session, periodeInvaliditeVB.getDateFinInvalidite());
            isDateFinInvalidite = true;

            // Vérifier que ce soit bien dans le format JJ.MM.AAAA
            if (!JadeStringUtil.isBlankOrZero(periodeInvaliditeVB.getDateFinInvalidite())) {
                if ((periodeInvaliditeVB.getDateFinInvalidite().length() > 10)
                        || (periodeInvaliditeVB.getDateFinInvalidite().length() < 10)) {
                    session.addError(session.getLabel("ERREUR_DATE_FIN_DROIT_FORMAT"));
                }
            }

        } catch (Exception e) {
            isDateFinInvalidite = false;
        }

        if (!isDateFinInvalidite) {
            session.addError(session.getLabel("VALID_SDR_H_DATE_FIN_INV_INCORRECTE"));
        }

        // 4. Diverses vérifications des dates

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // VALIDATION DE LA PERIODE
        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (!JadeStringUtil.isEmpty(periodeInvaliditeVB.getDateFinInvalidite())
                && !JadeStringUtil.isEmpty(periodeInvaliditeVB.getDateDebutInvalidite())) {

            // 4b. Date de fin doit être supérieure ou égale à la date du début
            if (Double.valueOf(new JADate(periodeInvaliditeVB.getDateFinInvalidite()).toStrAMJ()).doubleValue() < Double
                    .valueOf(new JADate(periodeInvaliditeVB.getDateDebutInvalidite()).toStrAMJ()).doubleValue()) {

                session.addError(session.getLabel("VALID_SDR_H_DATE_FIN_INV_POST_DATE_DEBUT_INV"));
            }
        }

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // VALIDATION DE LA PERIODE
        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (!JadeStringUtil.isEmpty(periodeInvaliditeVB.getDateDebutInvalidite())) {

            // 4c. On parcours toute la liste pour savoir si la nouvelle période
            // est pendant une autre période déjà dans la liste
            for (REPeriodeInvaliditeViewBean perINVVb : periodesInvalidites) {

                /*
                 * Général : ---------
                 * 
                 * periodeAPIVB = nouvelle période = np perAPIVb = période de la liste déjà entrée = pl
                 * 
                 * 
                 * Cas possibles pour insertion période !
                 * 
                 * 1) Si pl avec dd et df
                 * 
                 * -> a) dateFin np avant dateDebut pl -> b) dateDebut np après dateFin pl
                 * 
                 * -> c) dateDebut np après dateFin pl (sans dateFin np)
                 * 
                 * 2) Si pl avec dd et pas de df
                 * 
                 * -> a) dateFin np avant dateDebut pl
                 * 
                 * -> d) si sans DateFin = impossible
                 */

                boolean isPeriodeOK = false;
                JACalendar cal = new JACalendarGregorian();

                // 1) si pl avec dd et df
                if (!JadeStringUtil.isBlankOrZero(perINVVb.getDateDebutInvalidite())
                        && !JadeStringUtil.isBlankOrZero(perINVVb.getDateFinInvalidite())) {

                    // Si np avec dd et df
                    if (!JadeStringUtil.isBlankOrZero(periodeInvaliditeVB.getDateDebutInvalidite())
                            && !JadeStringUtil.isBlankOrZero(periodeInvaliditeVB.getDateFinInvalidite())) {
                        // a)
                        if (cal.compare(periodeInvaliditeVB.getDateFinInvalidite(), perINVVb.getDateDebutInvalidite()) == JACalendar.COMPARE_FIRSTLOWER) {
                            isPeriodeOK = true;
                        }
                        // b)
                        if (cal.compare(periodeInvaliditeVB.getDateDebutInvalidite(), perINVVb.getDateFinInvalidite()) == JACalendar.COMPARE_FIRSTUPPER) {
                            isPeriodeOK = true;
                        }
                    } else if (!JadeStringUtil.isBlankOrZero(periodeInvaliditeVB.getDateDebutInvalidite())
                            && JadeStringUtil.isBlankOrZero(periodeInvaliditeVB.getDateFinInvalidite())) {
                        // c)
                        if (cal.compare(periodeInvaliditeVB.getDateDebutInvalidite(), perINVVb.getDateFinInvalidite()) == JACalendar.COMPARE_FIRSTUPPER) {
                            isPeriodeOK = true;
                        }
                    }
                } else if (!JadeStringUtil.isBlankOrZero(perINVVb.getDateDebutInvalidite())
                        && JadeStringUtil.isBlankOrZero(perINVVb.getDateFinInvalidite())) {

                    // Si np avec dd et df
                    if (!JadeStringUtil.isBlankOrZero(periodeInvaliditeVB.getDateDebutInvalidite())
                            && !JadeStringUtil.isBlankOrZero(periodeInvaliditeVB.getDateFinInvalidite())) {

                        // a)
                        if (cal.compare(periodeInvaliditeVB.getDateFinInvalidite(), perINVVb.getDateDebutInvalidite()) == JACalendar.COMPARE_FIRSTLOWER) {
                            isPeriodeOK = true;
                        }
                    }
                }
                if (!isPeriodeOK) {
                    session.addError(session.getLabel("ERREUR_SAISIE_PERIODE"));
                }
            }
        }

        if (!session.hasErrors()) {
            Integer noProv = saisieDemandeRenteVB.getNextKeyPeriodeINV();
            if (JadeStringUtil.isEmpty(periodeInvaliditeVB.getIdPeriodeInvalidite())) {
                periodeInvaliditeVB.setIdProvisoire(noProv.intValue());
            } else {
                periodeInvaliditeVB.setIdProvisoire(Integer.parseInt(periodeInvaliditeVB.getIdPeriodeInvalidite()));
            }

            // ajout de la période en cours dans la liste provisoire
            periodesInvalidites.add(periodeInvaliditeVB);
            saisieDemandeRenteVB.setDateDebutInvalidite("");
            saisieDemandeRenteVB.setDateFinInvalidite("");
            saisieDemandeRenteVB.setDegreInvalidite("");
            saisieDemandeRenteVB.setIdPeriodeInvalidite("");
        }
        saisieDemandeRenteVB.setModifie(true);
        return saisieDemandeRenteVB;
    }

    public FWViewBeanInterface actionAjouterPeriodeINVPrestationTransitoire(FWViewBeanInterface vb, FWAction action,
            BSession session) throws Exception {

        // Limitation de l'ajout de période à.. une seule
        RESaisieDemandeRenteViewBean viewBean = (RESaisieDemandeRenteViewBean) vb;
        if (viewBean.getPeriodesInvalidite().size() > 0) {
            session.addError(session
                    .getLabel("AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_UNE_SEULE_PERIODE_DOIT_ETRE_DECLAREE"));
            return viewBean;
        } else {
            return actionAjouterPeriodeINV(viewBean, action, session);
        }
    }

    public FWViewBeanInterface actionSupprimerPeriodeAPI(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;
        Set<REPeriodeAPIViewBean> listePeriodeAPI = vb.getPeriodesAPI();

        // Reprendre la bonne entité de la liste et la supprimer de la base si
        // elle y existe et de la liste
        for (REPeriodeAPIViewBean periodeAPIVB : listePeriodeAPI) {
            if (periodeAPIVB.getIdProvisoire() == vb.getIdProvisoirePeriodeAPI().intValue()) {
                if (!JadeStringUtil.isEmpty(periodeAPIVB.getIdPeriodeAPI())) {
                    REPeriodeAPI periodeAPI = new REPeriodeAPI();
                    periodeAPI.setSession(session);
                    periodeAPI.setIdPeriodeAPI(periodeAPIVB.getIdPeriodeAPI());
                    periodeAPI.retrieve();
                    periodeAPI.delete();
                }
                listePeriodeAPI.remove(periodeAPIVB);
            }
        }

        // Supprimer la période dans la liste
        listePeriodeAPI.remove(vb.getIdProvisoirePeriodeAPI());
        vb.setModifie(true);
        return viewBean;
    }

    public FWViewBeanInterface actionSupprimerPeriodeINV(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean) viewBean;
        Set<REPeriodeInvaliditeViewBean> listePeriodeINV = vb.getPeriodesInvalidite();

        // Reprendre la bonne entité de la liste et la supprimer de la base si
        // elle y existe et de la liste
        for (REPeriodeInvaliditeViewBean periodeINVVB : listePeriodeINV) {
            if (periodeINVVB.getIdProvisoire() == vb.getIdProvisoirePeriodeINV().intValue()) {
                if (!JadeStringUtil.isEmpty(periodeINVVB.getIdPeriodeInvalidite())) {
                    REPeriodeInvalidite periodeINV = new REPeriodeInvalidite();
                    periodeINV.setSession(session);
                    periodeINV.setIdPeriodeInvalidite(periodeINVVB.getIdPeriodeInvalidite());
                    periodeINV.retrieve();
                    periodeINV.delete();
                }
                listePeriodeINV.remove(periodeINVVB);
            }
        }
        vb.setModifie(true);
        return viewBean;
    }

    public FWViewBeanInterface afficherInformationsComplementaires(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        REInfoComplViewBean infoComplViewBean = (REInfoComplViewBean) viewBean;

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(session);
        demandeRente.setIdDemandeRente(infoComplViewBean.getIdDemandeRente());
        demandeRente.retrieve();

        if (!demandeRente.isNew()) {

            if (JadeStringUtil.isBlankOrZero(infoComplViewBean.getIdInfoCompl())) {
                infoComplViewBean.setIdInfoCompl(demandeRente.getIdInfoComplementaire());
            }
            infoComplViewBean.retrieve();

            infoComplViewBean.setCsEtatDemandeRente(demandeRente.getCsEtat());
            infoComplViewBean.setCsTypeCalcul(demandeRente.getCsTypeCalcul());
            infoComplViewBean.setCsTypeDemandeRente(demandeRente.getCsTypeDemandeRente());
            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demandeRente.getCsTypeDemandeRente())
                    && JadeStringUtil.isBlankOrZero(infoComplViewBean.getCsTiersResponsable())) {
                infoComplViewBean.setCsTiersResponsable(IPRInfoCompl.CS_TIERS_RESPONSABLE_NON);
            }
        } else {
            throw new RETechnicalException(FWMessageFormat.format(
                    session.getLabel("JSP_INF_D_ERREUR_DEMANDE_NON_TROUVEE"), infoComplViewBean.getIdDemandeRente()));
        }

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, infoComplViewBean.getIdTiers());
        if (tiers != null) {
            infoComplViewBean.setCsSexe(tiers.getSexe());
            infoComplViewBean.setNss(tiers.getNSS());
        } else {
            throw new RETechnicalException(FWMessageFormat.format("JSP_INF_D_ERREUR_TIERS_NON_TROUVE",
                    infoComplViewBean.getIdTiers()));
        }

        // Check pour voir si on a une rente en état bloqué
        REDemandeRenteJointPrestationAccordeeManager manager = new REDemandeRenteJointPrestationAccordeeManager();
        manager.setSession(session);
        manager.setForIdDemandeRente(infoComplViewBean.getIdDemandeRente());
        manager.find();

        boolean isBloque = false;

        if ((IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(demandeRente.getCsEtat()))
                || (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(demandeRente.getCsEtat()))) {

            for (REDemandeRenteJointPrestationAccordee uneEntree : manager.getContainerAsList()) {
                isBloque = isRenteAccordeeBloque(session, isBloque, uneEntree.getIdRenteAccordee());
                if (isBloque) {
                    break;
                }
            }
        }

        if (isBloque == false) {
            List<RERenteAccordeeFamille> listRenteAccordeeFamille = new ArrayList<RERenteAccordeeFamille>();
            isRenteAccordeePourResteDeLaFamille(session, tiers, listRenteAccordeeFamille);
            for (RERenteAccordeeFamille rentAccFam : listRenteAccordeeFamille) {
                isBloque = isRenteAccordeeBloque(session, isBloque, rentAccFam.getIdRenteAccordee());
                if (isBloque) {
                    break;
                }
            }
        }

        infoComplViewBean.setIsBloque(isBloque);
        return infoComplViewBean;
    }

    private boolean isRenteAccordeeBloque(BSession session, boolean isBloque, String idRenteAccordee) throws Exception {
        REPrestationsAccordees rentePrestationAccordee = new REPrestationsAccordees();
        rentePrestationAccordee.setIdPrestationAccordee(idRenteAccordee);
        rentePrestationAccordee.setSession(session);
        rentePrestationAccordee.retrieve();

        REEnteteBlocage blk = new REEnteteBlocage();
        if (rentePrestationAccordee.getIdEnteteBlocage() != null) {
            blk.setIdEnteteBlocage(rentePrestationAccordee.getIdEnteteBlocage());
            blk.setSession(session);
            blk.retrieve();
            if (REEcheancesUtils.isPrestationBloquee(rentePrestationAccordee, blk)) {
                isBloque = true;
            }
        }
        return isBloque;
    }

    private void isRenteAccordeePourResteDeLaFamille(BSession session, PRTiersWrapper tiers,
            List<RERenteAccordeeFamille> listRenteAccordeeFamille) throws Exception {
        // Recherche les rentes accordés
        RERenteAccordeeFamilleManager renteAccManager = new RERenteAccordeeFamilleManager();
        renteAccManager.setForIdTiersLiant(tiers.getIdTiers());
        renteAccManager.setSession(session);
        renteAccManager.setOrderByCodePrestation();
        renteAccManager.find();

        Set<String> idTiersExConjoints = chargerExConjoints(tiers.getIdTiers(), session);
        Set<PRTiersWrapper> enfants = SFFamilleUtils.getEnfantsDuTiers(session, tiers.getIdTiers());
        for (int i = 0; i < renteAccManager.size(); i++) {
            RERenteAccordeeFamille renteAccFam = (RERenteAccordeeFamille) renteAccManager.get(i);

            if (idTiersExConjoints.contains(renteAccFam.getIdTiersBaseCalcul())
                    || !JadeStringUtil.isBlank(renteAccFam.getDateFinDroit())) {
                continue;
            }

            // BZ 7370 Si c'est une rente principale et qu'elle appartient à un enfant du tiers de la demande
            // on ne la considère pas
            if (isRentePrincipale(renteAccFam)) {
                boolean flag = false;
                for (PRTiersWrapper enfant : enfants) {
                    if (renteAccFam.getIdTiersBeneficiaire().equals(enfant.getIdTiers())) {
                        flag = true;
                    }
                }
                if (flag) {
                    continue;
                }
            }

            // Les rentes n'étant pas dans l'état courant validé ou validé sont ignorées
            if (!IREPrestationAccordee.CS_ETAT_PARTIEL.equals(renteAccFam.getCsEtatRenteAccordee())
                    && !IREPrestationAccordee.CS_ETAT_VALIDE.equals(renteAccFam.getCsEtatRenteAccordee())) {
                continue;
            }

            listRenteAccordeeFamille.add(renteAccFam);
        }
    }

    private Set<String> chargerExConjoints(String idTiers, BSession session) throws Exception {
        Set<String> idTiersExConjoints = new HashSet<String>();

        List<PRTiersWrapper> exConjoints = SFFamilleUtils.getExConjointsTiers(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);

        RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager renteAccManager = new RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager();
        renteAccManager.setSession(session);

        for (PRTiersWrapper unExConjoint : exConjoints) {
            idTiersExConjoints.add(unExConjoint.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

        return idTiersExConjoints;
    }

    private boolean isRentePrincipale(RERenteAccordeeFamille rente) {
        String codePrestation = rente.getCodePrestation();
        return REGenresPrestations.GENRE_50.equals(codePrestation)
                || REGenresPrestations.GENRE_70.equals(codePrestation)
                || REGenresPrestations.GENRE_10.equals(codePrestation)
                || REGenresPrestations.GENRE_20.equals(codePrestation)
                || REGenresPrestations.GENRE_13.equals(codePrestation)
                || REGenresPrestations.GENRE_23.equals(codePrestation);
    }

    public FWViewBeanInterface afficherPrestationTransitoire(FWViewBeanInterface vb, FWAction action, BSession session)
            throws Exception {
        RESaisieDemandeRenteViewBean tmpVB = (RESaisieDemandeRenteViewBean) vb;

        // Dans notre cas, l'id sera un id pour une entité de type REDemandeRenteInvalidite
        String idDemandeRenteInvalidite = tmpVB.getIdDemandeRente();
        if (JadeStringUtil.isBlankOrZero(idDemandeRenteInvalidite)) {
            this.throwException(session,
                    "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_ID_DEMANDE_TYPE_INVALIDITE_DEPUIS_VIEWBEAN");
        }

        REDemandeRenteInvalidite renteInvalidite = new REDemandeRenteInvalidite();
        renteInvalidite.setSession(session);
        renteInvalidite.setIdDemandeRente(idDemandeRenteInvalidite);
        renteInvalidite.retrieve();
        if (renteInvalidite.isNew()) {
            this.throwException(session, "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_DEMANDE_TYPE_INVALIDITE");
        }

        PRDemande demande = new PRDemande();
        demande.setSession(session);
        demande.setIdDemande(renteInvalidite.getIdDemandePrestation());
        demande.retrieve();
        if (demande.isNew()) {
            this.throwException(session, "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_DEMANDE_PRESTATION");
        }

        String idTiersRequerant = demande.getIdTiers();
        if (JadeStringUtil.isBlankOrZero(idTiersRequerant)) {
            this.throwException(session,
                    "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_ID_TIERS_REQUERANT_DEPUIS_DEMANDE_PRESTATION");
        }

        PRTiersWrapper requerant = PRTiersHelper.getTiersParId(session, idTiersRequerant);
        if (requerant == null) {
            this.throwException(session,
                    "PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_REQUERANT_DEPUIS_ID_TIERS_REQUERANT");
        }
        PRTiersWrapper requerantAdresse = PRTiersHelper.getTiersAdresseDomicileParId(session, idTiersRequerant,
                JACalendar.todayJJsMMsAAAA());

        RESaisieDemandeRenteViewBean viewBean = new RESaisieDemandeRenteViewBean();

        // Remplissage du viewBean avec les des données du requérant
        viewBean.setNssRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        viewBean.setNomRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_NOM));
        viewBean.setPrenomRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
        viewBean.setDateNaissanceRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
        viewBean.setCsSexeRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_SEXE));
        viewBean.setCsNationaliteRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE));
        viewBean.setDateDecesRequerant(requerant.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES));
        viewBean.setIdTiers(requerant.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        if (requerantAdresse != null) {
            viewBean.setCsCantonRequerant(requerantAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON));
        }

        // Remplissage du viewBean avec les données de la demande
        viewBean.setIdDemandeRente(idDemandeRenteInvalidite);
        viewBean.setCsAtteinteInv(renteInvalidite.getCsAtteinte());
        viewBean.setCodeOfficeAiInv(renteInvalidite.getCodeOfficeAI());
        viewBean.setCsEtat(renteInvalidite.getCsEtat());
        viewBean.setCsGenrePrononceAiInv(renteInvalidite.getCsGenrePrononceAI());
        viewBean.setCsInfirmiteInv(renteInvalidite.getCsInfirmite());
        viewBean.setCsTypeCalcul(renteInvalidite.getCsTypeCalcul());
        viewBean.setCsTypeDemandeRente(renteInvalidite.getCsTypeDemandeRente());
        viewBean.setDateDebut(renteInvalidite.getDateDebut());
        viewBean.setDateDebutRedNonCollaboration(renteInvalidite.getDateDebutRedNonCollaboration());
        viewBean.setDateDepot(renteInvalidite.getDateDepot());
        viewBean.setDateFin(renteInvalidite.getDateFin());
        viewBean.setDateFinRedNonCollaboration(renteInvalidite.getDateFinRedNonCollaboration());
        viewBean.setDateReception(renteInvalidite.getDateReception());
        viewBean.setDateSurvenanceEvenementAssureInv(renteInvalidite.getDateSuvenanceEvenementAssure());
        viewBean.setDateTraitement(renteInvalidite.getDateTraitement());
        viewBean.setIdGestionnaire(renteInvalidite.getIdGestionnaire());
        viewBean.setIdInfoComplementaire(renteInvalidite.getIdInfoComplementaire());
        viewBean.setNbPagesMotivationInv(renteInvalidite.getNbPageMotivation());
        viewBean.setPourcentageReductionInv(renteInvalidite.getPourcentageReduction());
        viewBean.setPourcentRedFauteGrave(renteInvalidite.getPourcentRedFauteGrave());
        viewBean.setPourcentRedNonCollaboration(renteInvalidite.getPourcentRedNonCollaboration());

        return viewBean;
    }

    public FWViewBeanInterface ajouterInformationsComplementaires(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {
        // Reprise du viewBean
        REInfoComplViewBean infoComplVb = (REInfoComplViewBean) viewBean;
        // Création de la transaction
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) (session).newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            // Faire juste les validations, ne rien stocker dans la base ici...
            validateInfoComp(transaction, infoComplVb, session);
            if (!transaction.hasErrors() && !session.hasErrors()) {
                boolean isUpdate = false;
                if (!JadeStringUtil.isBlankOrZero(infoComplVb.getIdInfoCompl())) {
                    isUpdate = true;
                }
                setUpdateInfoComp(transaction, infoComplVb, session, isUpdate);
            }
            if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            JadeLogger.error(e, e.getMessage());
            throw e;
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
        return infoComplVb;
    }

    /**
     * Finalisation de la demande de prestation transitoire -> Copie des RA accordées à la famille -> Calcul des
     * prestations dues
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface ajouterSaisieDemandePrestationTransitoire(FWViewBeanInterface vb, FWAction action,
            BSession session) throws Exception {

        BITransaction transaction = session.newTransaction();
        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }
        try {

            /**
             * Recherche des informations nécessaires à notre job
             */
            RESaisieDemandeRenteViewBean viewBean = (RESaisieDemandeRenteViewBean) vb;
            if (viewBean == null) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_AUCUN_VIEWBEAN_TRANSMIS");
            }

            // Validation des informations contenues dans le viewBean. On fait une validation max
            validate((BTransaction) transaction, viewBean, session, false);

            // lors de la validation les messages sont mis en session
            if (session.hasErrors() || transaction.hasErrors()) {
                throw new Exception(session.getErrors().toString());
            }

            String idTiersRequerant = viewBean.getIdTiers();
            if (JadeStringUtil.isBlankOrZero(idTiersRequerant)) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_ID_TIERS_REQUERANT_VIDE_VIEWBEAN");
            }

            String idDemandeRenteInvaliditeTiersRequerant = viewBean.getIdDemandeRente();
            if (JadeStringUtil.isBlankOrZero(idDemandeRenteInvaliditeTiersRequerant)) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_ID_DEMANDE_VIDE_VIEWBEAN");
            }

            /**
             * Récupération de la demande de rente, la rente calculée, la base de calcul et la rente invalidité du tiers
             * requérant
             */
            REDemandeRenteInvalidite demandeRenteInvaliditeTiersRequerant = new REDemandeRenteInvalidite();
            demandeRenteInvaliditeTiersRequerant.setSession(session);
            demandeRenteInvaliditeTiersRequerant.setIdDemandeRente(idDemandeRenteInvaliditeTiersRequerant);
            demandeRenteInvaliditeTiersRequerant.retrieve(transaction);
            if (demandeRenteInvaliditeTiersRequerant.isNew()) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_DEMANDE_RENTE");
            }

            Map<String, String> codesCsCodeInfirmite = globaz.prestation.tools.PRCodeSystem.getCUCSinMap(session,
                    globaz.corvus.api.demandes.IREDemandeRente.CS_GROUPE_INFIRMITE);

            String codeInfirmite = "";
            if (JadeStringUtil.isBlankOrZero(viewBean.getCodeCsInfirmiteInv())) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_CODE_INFIRMITE_VIDE");
            }
            if (!codesCsCodeInfirmite.containsKey(viewBean.getCodeCsInfirmiteInv())) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_CODE_INFIRMITE_INVALIDE");
            }
            codeInfirmite = codesCsCodeInfirmite.get(viewBean.getCodeCsInfirmiteInv());

            if (JadeStringUtil.isBlankOrZero(viewBean.getCodeCsInfirmiteInv())) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_CODE_ATTEINTE_VIDE");
            }

            // On met direct la demande à jour avec les données du viewBean
            demandeRenteInvaliditeTiersRequerant.setDateTraitement(REACORParser
                    .retrieveDateTraitement(demandeRenteInvaliditeTiersRequerant));
            demandeRenteInvaliditeTiersRequerant.setDateDepot(viewBean.getDateDepot());
            demandeRenteInvaliditeTiersRequerant.setDateReception(viewBean.getDateReception());

            demandeRenteInvaliditeTiersRequerant.setCodeOfficeAI(viewBean.getCodeOfficeAiInv());
            demandeRenteInvaliditeTiersRequerant.setCsInfirmite(codeInfirmite);
            demandeRenteInvaliditeTiersRequerant.setCsAtteinte(viewBean.getCsAtteinteInv());
            demandeRenteInvaliditeTiersRequerant.setNbPageMotivation(viewBean.getNbPagesMotivationInv());
            demandeRenteInvaliditeTiersRequerant.setDateSuvenanceEvenementAssure(viewBean
                    .getDateSurvenanceEvenementAssureInv());
            demandeRenteInvaliditeTiersRequerant.setPourcentRedFauteGrave(viewBean.getPourcentRedFauteGrave());
            demandeRenteInvaliditeTiersRequerant.setPourcentRedNonCollaboration(viewBean
                    .getPourcentRedNonCollaboration());
            demandeRenteInvaliditeTiersRequerant.setDateDebutRedNonCollaboration(viewBean
                    .getDateDebutRedNonCollaboration());
            demandeRenteInvaliditeTiersRequerant
                    .setDateFinRedNonCollaboration(viewBean.getDateFinRedNonCollaboration());
            demandeRenteInvaliditeTiersRequerant.setIdGestionnaire(viewBean.getIdGestionnaire());
            demandeRenteInvaliditeTiersRequerant.setCsGenrePrononceAI(viewBean.getCsGenrePrononceAiInv());
            // On ne met pas à jour tt de suite en bd car il faut encore renseigner les date de début et fin de période
            // demandeRenteInvaliditeTiersRequerant.update(transaction);

            RERenteCalculee renteCalculeeTiersRequerant = new RERenteCalculee();
            renteCalculeeTiersRequerant.setSession(session);
            renteCalculeeTiersRequerant.setIdRenteCalculee(demandeRenteInvaliditeTiersRequerant.getIdRenteCalculee());
            renteCalculeeTiersRequerant.retrieve(transaction);
            if (renteCalculeeTiersRequerant.isNew()) {
                this.throwException(session,
                        "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_RENTE_CALCULEE");
            }

            String idBaseDeCalcul = null;
            REBasesCalcul baseCalculRenteTiersRequerant = null;
            REBasesCalculManager baseDeCalculManager = new REBasesCalculManager();
            baseDeCalculManager.setSession(session);
            baseDeCalculManager.setForIdRenteCalculee(renteCalculeeTiersRequerant.getIdRenteCalculee());
            baseDeCalculManager.find(transaction, BManager.SIZE_NOLIMIT);
            if (baseDeCalculManager.getContainer().size() == 0) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_AUCUN_BASE_DE_CALCUL_TROUVEE");
            } else if (baseDeCalculManager.getContainer().size() > 1) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_PLUSIEURS_BASE_DE_CALCUL_TROUVEES");
            } else {
                baseCalculRenteTiersRequerant = (REBasesCalcul) baseDeCalculManager.getFirstEntity();
                if ((baseCalculRenteTiersRequerant == null) || baseCalculRenteTiersRequerant.isNew()) {
                    this.throwException(session,
                            "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_BASE_CALCUL");
                }
                idBaseDeCalcul = baseCalculRenteTiersRequerant.getIdBasesCalcul();
            }

            if (JadeStringUtil.isBlankOrZero(idBaseDeCalcul)) {
                this.throwException(session,
                        "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_ID_BASE_CALCUL");
            }

            /**
             * Ici on va créer la période qui ont été définie dans l'écran de saisie de la demande de rente. Il ne doit
             * y en avoir qu'une !!!!
             */

            if (viewBean.getPeriodesInvalidite().size() == 0) {
                this.throwException(session, "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_UNE_PERIODE_DOIT_ETRE_DECLAREE");
            }
            if (viewBean.getPeriodesInvalidite().size() > 1) {
                this.throwException(session,
                        "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_UNE_SEULE_PERIODE_DOIT_ETRE_DECLAREE");
            }

            TreeSet<REPeriodeInvalidite> periodesInvalidite = new TreeSet<REPeriodeInvalidite>(
                    viewBean.getPeriodesInvalidite());

            REPeriodeInvalidite periodeInvListe = periodesInvalidite.first();
            periodeInvListe.setSession(session);
            periodeInvListe.setIdDemandeRente(idDemandeRenteInvaliditeTiersRequerant);
            periodeInvListe.add(transaction);

            String dateDebutPeriode = periodeInvListe.getDateDebutInvalidite().substring(3);

            String dateFinPeriode = "";
            if (!JadeStringUtil.isBlankOrZero(periodeInvListe.getDateFinInvalidite())) {
                dateFinPeriode = periodeInvListe.getDateFinInvalidite().substring(3);
            }

            /**
             * On met à jour la nouvelle base de calcul
             */
            baseCalculRenteTiersRequerant.setCodeOfficeAi(viewBean.getCodeOfficeAiInv());
            baseCalculRenteTiersRequerant.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
            baseCalculRenteTiersRequerant.setDegreInvalidite(periodeInvListe.getDegreInvalidite());
            baseCalculRenteTiersRequerant.update(transaction);

            /**
             * 3-> On va rechercher sa rente 50 (invalidité) original que l'on va copier (si il y en plusieurs, on prend
             * la dernière donc, sans date de fin ou avec la date de fin la plus récente)
             */
            RERenteAccordee rente50DuTiers = REDemandeUtils.getDerniereRenteInvaliditeDuTiers(session, transaction,
                    idTiersRequerant);

            // Voilà, on à récupéré la rente 50 du tiers la plus récente. On s'en assure
            if (rente50DuTiers == null) {
                throw new Exception(translate(session,
                        "PRESTATION_TRANSITOIRE_ERREUR_INTERNE_IMPOSSIBLE_RECUPERER_RENTE_INVALIDITE_LA_PLUS_RECENTE"));
            }

            // On récupère la date de fin de la rente original que l'on va copier ainsi que les info comptable
            // Format MM.AAAA - à confirmer
            String dateDeFin = rente50DuTiers.getDateFinDroit();
            boolean hasDateDeFin = JadeDateUtil.isGlobazDateMonthYear(dateDeFin);

            REInformationsComptabilite reincom = new REInformationsComptabilite();
            reincom.setSession(session);
            reincom.setIdInfoCompta(rente50DuTiers.getIdInfoCompta());
            reincom.retrieve(transaction);

            if (reincom.isNew()) {
                this.throwException(session,
                        "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_INFO_COMPTA_DEPUIS_RENTE_ACCORDEE");
            }
            reincom.setIdInfoCompta(null);
            reincom.add(transaction);

            // Mis à jour de la rente
            rente50DuTiers.setSession(session);
            rente50DuTiers.setIdPrestationAccordee(null);
            rente50DuTiers.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
            rente50DuTiers.setIdBaseCalcul(idBaseDeCalcul);
            rente50DuTiers.setIdInfoCompta(reincom.getIdInfoCompta());
            rente50DuTiers.setDateDebutDroit(dateDebutPeriode);
            rente50DuTiers.setDateFinDroit(dateFinPeriode);
            REDemandeUtils.insertCodeCasSpecial(rente50DuTiers, 84);
            rente50DuTiers.add(transaction);

            // Mis à jour de la demande
            demandeRenteInvaliditeTiersRequerant.setDateDebut(dateDebutPeriode);
            demandeRenteInvaliditeTiersRequerant.setDateFin(dateFinPeriode);
            demandeRenteInvaliditeTiersRequerant.update(transaction);

            /**
             * Maintenant on vas rechercher les rentes de type [50, 54, 55, 56 OU 70, 74, 75, 76] de la famille du tiers
             * requérant. Si la rente accordé du tiers requérant possède une date de fin, on va rechercher les rentes de
             * la famille qui ont une date de fin Si il n'y à pas de date de fin à la rente du tiers requérant on va
             * rechercher les rentes de la famille qui n'ont pas de date de fin
             */

            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);
            if (sf == null) {
                this.throwException(session,
                        "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_DE_RECUPERER_SITUATION_FAMILIALLE_TIERS_REQUERANT");
            }

            ArrayList<String> listesIdsAnciennesRenteFamille = new ArrayList<String>();
            ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(idTiersRequerant);

            if (mf != null) {
                RERenteAccordeeManager renteAccordeeManager = new RERenteAccordeeManager();
                renteAccordeeManager.setSession(session);

                for (ISFMembreFamilleRequerant membres : mf) {
                    // ATTENTION : l'id tiers peut renvoyer 0 ou null.. Dans ce cas il remonte toute la base
                    // ATTENTION : getMembresFamilleRequerant(idTiers) : retourne également le tier passé en paramètres
                    if (!JadeStringUtil.isBlankOrZero(membres.getIdTiers())
                            && !idTiersRequerant.equals(membres.getIdTiers())) {

                        // Si le menbre de la famille est le conjoint on l'exclut car on ne veut copier que les rentes
                        // complémentaire
                        if (membres.getRelationAuRequerant().equals(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT)) {
                            continue;
                        }

                        renteAccordeeManager.setForIdTiersBeneficiaire(membres.getIdTiers());
                        renteAccordeeManager.find(transaction, 100);
                        for (Object o : renteAccordeeManager.getContainer()) {
                            RERenteAccordee ra = (RERenteAccordee) o;
                            // tester l'état rente (validé, courant, diminué)
                            if (IREPrestationAccordee.CS_ETAT_VALIDE.equals(ra.getCsEtat())
                                    || IREPrestationAccordee.CS_ETAT_PARTIEL.equals(ra.getCsEtat())
                                    || IREPrestationAccordee.CS_ETAT_DIMINUE.equals(ra.getCsEtat())) {
                                // La rente est-elle du bon type (Invalidité)
                                if (PRCodePrestationInvalidite.isCodePrestationInvalidite(ra.getCodePrestation())) {
                                    // La rente invalidité du tiers requérant à une date de fin
                                    if (hasDateDeFin) {
                                        // Si la ra à une de date de fin et qu'elle est identique à la date de fin de la
                                        // rente invalidité du tiers requérant on la prend
                                        if (JadeDateUtil.isGlobazDateMonthYear(ra.getDateFinDroit())
                                                && dateDeFin.equals(ra.getDateFinDroit())) {
                                            listesIdsAnciennesRenteFamille.add(ra.getIdPrestationAccordee());
                                        } else {
                                            // On ne fait rien
                                        }
                                    }
                                    // Pas de date de fin sur la rente invalidité du tiers requérant
                                    else {
                                        // Si la ra n'a pas de date de fin on la prend
                                        if (!JadeDateUtil.isGlobazDateMonthYear(ra.getDateFinDroit())) {
                                            listesIdsAnciennesRenteFamille.add(ra.getIdPrestationAccordee());
                                        } else {
                                            // On ne fait rien
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /**
             * On a récupéré tous les ids des rentes accordées à la famille On vas maintenant copier chacune de ces
             * rentes accordées ainsi que les info comptable
             */
            ArrayList<String> listesIdsNouvellesRenteFamille = new ArrayList<String>();
            RERenteAccordee renteAccordee = new RERenteAccordee();
            renteAccordee.setSession(session);

            for (String id : listesIdsAnciennesRenteFamille) {
                renteAccordee.setIdPrestationAccordee(id);
                renteAccordee.retrieve(transaction);
                if (!renteAccordee.isNew()) {

                    REInformationsComptabilite infoCompta = new REInformationsComptabilite();
                    infoCompta.setSession(session);
                    infoCompta.setIdInfoCompta(renteAccordee.getIdInfoCompta());
                    infoCompta.retrieve(transaction);

                    if (infoCompta.isNew()) {
                        this.throwException(session,
                                "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_INFO_COMPTA_DEPUIS_RENTE_ACCORDEE");
                    }
                    infoCompta.setIdInfoCompta(null);
                    infoCompta.add(transaction);

                    renteAccordee.setIdPrestationAccordee(null);
                    renteAccordee.setDateDebutDroit(dateDebutPeriode);
                    renteAccordee.setDateFinDroit(dateFinPeriode);
                    renteAccordee.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                    renteAccordee.setIdBaseCalcul(baseCalculRenteTiersRequerant.getIdBasesCalcul());
                    renteAccordee.setIdInfoCompta(infoCompta.getIdInfoCompta());
                    renteAccordee.add(transaction);
                    listesIdsNouvellesRenteFamille.add(renteAccordee.getIdPrestationAccordee());
                } else {
                    this.throwException(session,
                            "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_UNE_RENTE_ACCORDEE_FAMILLE");
                }
            }

            /**
             * Maintenant on vas insérer le code cas spécial dans les anciennes rente original
             */
            for (String id : listesIdsNouvellesRenteFamille) {
                renteAccordee.setIdPrestationAccordee(id);
                renteAccordee.retrieve(transaction);
                if (!renteAccordee.isNew()) {
                    // Insérer code cas spécial 84 dans la rente original
                    // On catch l'exception uniquement pour renvoyer un message clair et traduit
                    try {
                        REDemandeUtils.insertCodeCasSpecial(renteAccordee, 84);
                    } catch (Exception exception) {
                        this.throwException(session,
                                "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_INSERER_CODE_CAS_SPECIAL_84",
                                exception);
                    }
                    renteAccordee.update(transaction);
                } else {
                    this.throwException(session,
                            "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_UNE_RENTE_ACCORDEE_FAMILLE");
                }
            }

            /**
             * Maintenant on va créer la prestation due au tiers requérant sur la base de sa demande de rente invalidité
             * précédemment copié
             */
            RESaisieManuelleRABCPDHelper helper = new RESaisieManuelleRABCPDHelper();

            // Création des prestations due pour la rente principale du tiers requérant (courant et rétro)
            helper.calculerPrestationDues(session, (BTransaction) transaction, rente50DuTiers,
                    baseCalculRenteTiersRequerant.getRevenuAnnuelMoyen());

            // Création des prestations due pour les rentes liées de la famille (courant et rétro)
            // for (RERenteAccordee newRenteAccordee : listesIdsNouvellesRenteFamille) {
            for (String id : listesIdsNouvellesRenteFamille) {
                renteAccordee.setIdPrestationAccordee(id);
                renteAccordee.retrieve(transaction);
                if (!renteAccordee.isNew()) {
                    helper.calculerPrestationDues(session, (BTransaction) transaction, renteAccordee,
                            baseCalculRenteTiersRequerant.getRevenuAnnuelMoyen());
                } else {
                    this.throwException(session,
                            "AJOUTER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_UNE_RENTE_ACCORDEE_FAMILLE");
                }
            }

            return viewBean;
        } catch (Exception e) {
            transaction.setRollbackOnly();
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage("Error : " + e.toString());
            throw e;
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    public FWViewBeanInterface ajouterSaisieDemandeRente(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        // Reprise du viewBean
        RESaisieDemandeRenteViewBean saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) viewBean;

        // Création de la transaction
        BTransaction transaction = null;
        transaction = (BTransaction) (session).newTransaction();

        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }
        if (saisieDemandeRenteVb.isModifie()) {

            try {

                // Validation du contenu du viewBean
                validate(transaction, saisieDemandeRenteVb, session, Boolean.FALSE);

                // voir si la demande de rente existe déjà, si oui, update,
                // sinon add
                if (JadeStringUtil.isIntegerEmpty(saisieDemandeRenteVb.getIdDemandeRente())) {
                    // Si pas d'erreurs, création de la demande (prestations)
                    if (!saisieDemandeRenteVb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        setDemandePrestation(transaction, saisieDemandeRenteVb, saisieDemandeRenteVb.getIdRequerant(),
                                session, true);
                    }
                    // si toujours pas d'erreurs, création de la demande de
                    // rente
                    if (!saisieDemandeRenteVb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) setDemandeRente(transaction,
                                saisieDemandeRenteVb, session);
                    }

                    if (Boolean.TRUE.equals(saisieDemandeRenteVb.getIsAccuseDeReception())) {
                        REAccuseDeReceptionOO process = new REAccuseDeReceptionOO();
                        process.setSession(session);
                        process.setNssRequerant(saisieDemandeRenteVb.getNssRequerant());
                        process.setNomRequerant(saisieDemandeRenteVb.getNomRequerant());
                        process.setPrenomRequerant(saisieDemandeRenteVb.getPrenomRequerant());
                        process.setIdRequerant(saisieDemandeRenteVb.getIdRequerant());
                        process.setEmailAdresse(session.getUserEMail());
                        process.setDateReceptionDemande(saisieDemandeRenteVb.getDateReception());
                        process.setIdGestionnaire(saisieDemandeRenteVb.getIdGestionnaire());
                        // BZ 7874 Mise en GED du document si celui-ci est paramétré dans le fichier
                        // JadeClientServiceLocator.xml
                        if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ACCUSE_DE_RECEPTION, session)) {
                            process.setIsSendToGed(Boolean.TRUE);
                        } else {
                            process.setIsSendToGed(Boolean.FALSE);
                        }
                        BProcessLauncher.start(process, false);

                    }
                } else {
                    if (!saisieDemandeRenteVb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) updateDemandeRente(transaction,
                                saisieDemandeRenteVb, session);

                        if (Boolean.TRUE.equals(saisieDemandeRenteVb.getIsAccuseDeReception())) {
                            REAccuseDeReceptionOO process = new REAccuseDeReceptionOO();
                            process.setSession(session);
                            process.setNssRequerant(saisieDemandeRenteVb.getNssRequerant());
                            process.setNomRequerant(saisieDemandeRenteVb.getNomRequerant());
                            process.setPrenomRequerant(saisieDemandeRenteVb.getPrenomRequerant());
                            process.setIdRequerant(saisieDemandeRenteVb.getIdRequerant());
                            process.setEmailAdresse(session.getUserEMail());
                            process.setDateReceptionDemande(saisieDemandeRenteVb.getDateReception());
                            process.setIdGestionnaire(saisieDemandeRenteVb.getIdGestionnaire());
                            // BZ 7874 Mise en GED du document si celui-ci est paramétré dans le fichier
                            // JadeClientServiceLocator.xml
                            if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.ACCUSE_DE_RECEPTION, session)) {
                                process.setIsSendToGed(Boolean.TRUE);
                            } else {
                                process.setIsSendToGed(Boolean.FALSE);
                            }
                            BProcessLauncher.start(process, false);

                        }
                    }
                }

            } catch (Exception e) {
                transaction.setRollbackOnly();
                throw e;
            } finally {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        // Voir s'il faut passer dans la situation familiale

        // 1. Voir la situation familiale du requérant
        // 1a. si pas de situation familiale + coche --> Ecran ACOR
        // 2a. si situation familiale + peu importe la coche --> Ecran HERA
        // 3a. si pas de situation familiale + pas coche --> Ecran HERA

        // a) Est-ce que situation familiale existante ?
        // on charge tous les membres de la situation familiale et si le
        // résultat est vide ou null
        // on en déduit qu'il n'y a pas de situation familiale.
        globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, saisieDemandeRenteVb.getIdTiers());
        ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(saisieDemandeRenteVb.getIdTiers());

        boolean isSituationFamiliale = false;
        if (mf == null) {
            isSituationFamiliale = false;
        } else {
            if (mf.length > 1) {
                isSituationFamiliale = true;
            }
        }

        // Si c'est une demande survivant, on va de toute façon dans HERA
        if (saisieDemandeRenteVb.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {
            saisieDemandeRenteVb.setPassageHera(true);
        } else {
            // 1a
            if (!isSituationFamiliale
                    && (saisieDemandeRenteVb.getIsCelibataireSansEnfantsVieillesse().booleanValue() || saisieDemandeRenteVb
                            .getIsCelibataireSansEnfantsInv().booleanValue())) {
                // Ecran ACOR
                saisieDemandeRenteVb.setPassageHera(false);
                // 2a
            } else if (isSituationFamiliale) {
                // Ecran HERA
                saisieDemandeRenteVb.setPassageHera(true);
                // 3a
            } else if (!isSituationFamiliale
                    && (!saisieDemandeRenteVb.getIsCelibataireSansEnfantsVieillesse().booleanValue() && !saisieDemandeRenteVb
                            .getIsCelibataireSansEnfantsInv().booleanValue())) {
                // Ecran HERA
                saisieDemandeRenteVb.setPassageHera(true);
            }
        }

        // MAJ de la période de la demande !!!
        if (!JadeStringUtil.isEmpty(saisieDemandeRenteVb.getIdDemandeRente())) {
            doMAJPeriodeDemandeRente(session, transaction, saisieDemandeRenteVb);
        }

        // bz-6572
        try {
            if (JadeGedFacade.isInstalled()) {

                JadeTarget target = JadeGedFacade.getInstance().getTarget("JadeGedFacade");
                if (globaz.jade.ged.adapter.airs.JadeGedAdapter.class.isAssignableFrom(target.getClass())) {

                    Properties properties = new Properties();

                    TITiersViewBean t = new TITiersViewBean();
                    t.setSession(saisieDemandeRenteVb.getSession());
                    t.setIdTiers(saisieDemandeRenteVb.getIdTiers());
                    t.retrieve();

                    String p1, p2, p3, p4, p5 = "";

                    p1 = JadeStringUtil.removeChar(t.getNumAffilieActuel(), '.');
                    p1 = JadeStringUtil.removeChar(p1, '-');

                    p2 = JadeStringUtil.removeChar(t.getNumAvsActuel(), '.');
                    p3 = t.getDesignation1();
                    p4 = t.getDesignation2();
                    p5 = t.getLocaliteLong();

                    if (JadeStringUtil.isBlankOrZero(p1)) {
                        p1 = "";
                    }

                    if (JadeStringUtil.isBlankOrZero(p2)) {
                        p2 = "";
                    }

                    if (JadeStringUtil.isBlankOrZero(p3)) {
                        p3 = "";
                    }

                    if (JadeStringUtil.isBlankOrZero(p4)) {
                        p4 = "";
                    }

                    if (JadeStringUtil.isBlankOrZero(p5)) {
                        p5 = "";
                    }

                    properties.setProperty(AirsConstants.NAFF, p1);
                    properties.setProperty(AirsConstants.NNSS, p2);
                    properties.setProperty(AirsConstants.NOM, p3);
                    properties.setProperty(AirsConstants.PRENOM, p4);
                    properties.setProperty(AirsConstants.NPA, p5);
                    JadeGedFacade.propagate(properties);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return saisieDemandeRenteVb;

    }

    public FWViewBeanInterface arreterMajPartielleDemande(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        return majEditionPartielle(viewBean, action, session);

    }

    /**
     * Interrompt la création de la demande transitoire. A ce stade, certaine entités ont déjà été clonés par l'action
     * REDemandeRenteJointPrestationAccordeeHelper.actionCopierDemandePourPrestTrans(FWViewBeanInterface, FWAction,
     * BSession) -> REDemandeRenteInvalidite -> RERenteCalculee -> REBaseCalcul de type REBasesCalculNeuviemeRevision OU
     * REBasesCalculDixiemeRevision -> RERenteAccordee la rente invalidité du tiers requérant
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface arreterSaisieDemandePrestationTransitoire(FWViewBeanInterface vb, FWAction action,
            BSession session) throws Exception {
        RESaisieDemandeRenteViewBean viewBean = (RESaisieDemandeRenteViewBean) vb;
        BTransaction transaction = null;
        transaction = (BTransaction) (session).newTransaction();

        try {
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            String idDemandeRenteInvalidite = viewBean.getIdDemandeRente();

            if (idDemandeRenteInvalidite == null) {
                this.throwException(session, "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_TROUVER_ID_DEMANDE");
            }

            REDemandeRenteInvalidite demandeInvalidite = new REDemandeRenteInvalidite();
            demandeInvalidite.setSession(session);
            demandeInvalidite.setIdDemandeRente(idDemandeRenteInvalidite);
            demandeInvalidite.retrieve(transaction);

            if (demandeInvalidite.isNew()) {
                this.throwException(session,
                        "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_TROUVER_DEMANDE_DEPUIS_SON_ID");
            }

            /**
             * On récupère l'idTiers du requérant Il faut passer par la table demande de prestation PRDEMAP
             */
            PRDemande demandeDePrestation = new PRDemande();
            demandeDePrestation.setSession(session);
            demandeDePrestation.setId(demandeInvalidite.getIdDemandePrestation());
            demandeDePrestation.retrieve(transaction);
            if (demandeDePrestation.isNew()) {
                throw new Exception(translate(session,
                        "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RECUPERER_DEMANDE_PRESTATION"));
            }
            String idTiersRequerant = demandeDePrestation.getIdTiers();
            if (JadeStringUtil.isBlankOrZero(idTiersRequerant)) {
                throw new Exception(
                        translate(session,
                                "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_ID_TIERS_REQUERANT_DEPUIS_DEMANDE_PRESTATION"));
            }

            RERenteCalculee renteCalculee = new RERenteCalculee();
            renteCalculee.setSession(session);
            renteCalculee.setIdRenteCalculee(demandeInvalidite.getIdRenteCalculee());
            renteCalculee.retrieve(transaction);

            if (renteCalculee.isNew()) {
                this.throwException(session,
                        "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_RENTE_CALCULEE");
            }

            REBasesCalculManager baseDeCalculManager = new REBasesCalculManager();
            baseDeCalculManager.setSession(session);
            baseDeCalculManager.setForIdRenteCalculee(renteCalculee.getIdRenteCalculee());
            baseDeCalculManager.find(transaction, BManager.SIZE_NOLIMIT);

            REBasesCalcul baseDeCalcul = null;
            String idBaseDeCalcul = "0";
            String versionDuDroit = "0";
            if (baseDeCalculManager.size() == 0) {
                this.throwException(session,
                        "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_BASE_DE_CALCUL");
            } else if (baseDeCalculManager.size() > 1) {
                this.throwException(session,
                        "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_PLUSIEURS_BASE_DE_CALCUL_TROUVEES");
            } else {
                idBaseDeCalcul = ((REBasesCalcul) baseDeCalculManager.getFirstEntity()).getIdBasesCalcul();
                versionDuDroit = ((REBasesCalcul) baseDeCalculManager.getFirstEntity()).getDroitApplique();
                if (JadeStringUtil.isBlankOrZero(idBaseDeCalcul)) {
                    this.throwException(session,
                            "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_ID_BASE_DE_CALCUL");
                }
                if (JadeStringUtil.isBlankOrZero(versionDuDroit)) {
                    this.throwException(session,
                            "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_VERSION_DROIT_BASE_DE_CALCUL");
                } else if (versionDuDroit.equals("9")) {
                    baseDeCalcul = new REBasesCalculNeuviemeRevision();
                } else if (versionDuDroit.equals("10")) {
                    baseDeCalcul = new REBasesCalculDixiemeRevision();
                } else {
                    this.throwException(session,
                            "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_VERSION_DROIT_BASE_DE_CALCUL_EST_INCONNUE");
                }
            }

            baseDeCalcul.setIdBasesCalcul(idBaseDeCalcul);
            baseDeCalcul.setSession(session);
            baseDeCalcul.retrieve(transaction);
            if (baseDeCalcul.isNew()) {
                this.throwException(session,
                        "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_BASE_DE_CALCUL");
            }

            // /**
            // * Recherche des rentes accordées à supprimer
            // */
            // RERenteAccordeeManager renteAccordeeManager = new RERenteAccordeeManager();
            // RERenteAccordee renteAccordeeTiersRequerant = new RERenteAccordee();
            // renteAccordeeManager.setSession(session);
            // renteAccordeeManager.setForIdRenteAccordee(baseDeCalcul.getIdBasesCalcul());
            // renteAccordeeManager.find(transaction, BManager.SIZE_NOLIMIT);
            // // Ce n'est pas normal qu'on ne trouve rien....
            // if (renteAccordeeManager.size() == 0) {
            // this.throwException(session,
            // "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_IMPOSSIBLE_RETROUVER_RENTE_ACCORDEE");
            // }
            // // Ce n'est pas normal qu'on en trouve plusieurs
            // else if (renteAccordeeManager.size() > 1) {
            // this.throwException(session,
            // "ARRETER_SAISIE_PRESTATION_TRANSITOIRE_PLUSIEURS_RENTE_ACCORDEE_PRINCIPALE_TROUVEES");
            // } else {
            // renteAccordeeTiersRequerant = (RERenteAccordee) renteAccordeeManager.getFirstEntity();
            // }

            /**
             * Maintenant on nos entités à supprimer. Ces entités sont crées dans l'action :
             * REDemandeRenteJointPrestationAccordeeAction..actionCopierDemandePourPrestTrans() demandeInvalidite
             * renteCalculee baseDeCalcul renteAccordee (il n'y en à qu'une vu qu'on en à copié qu'une seule lors de la
             * création de la demande de prestation transitoire)
             */
            demandeInvalidite.delete(transaction);
            renteCalculee.delete(transaction);
            baseDeCalcul.delete(transaction);
            // renteAccordeeTiersRequerant.delete(transaction);

            return vb;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            vb.setMessage("Error : " + e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
            return vb;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors()) {
                            vb.setMessage(transaction.getErrors().toString());
                            vb.setMsgType(FWViewBeanInterface.ERROR);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (transaction != null) {
                        transaction.closeTransaction();
                    }
                }
            }
        }
    }

    public FWViewBeanInterface arreterSaisieDemandeRente(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        // Reprise du viewBean
        RESaisieDemandeRenteViewBean saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) viewBean;

        // Création de la transaction
        BTransaction transaction = null;
        transaction = (BTransaction) (session).newTransaction();

        try {
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            // si modif's demande
            if (saisieDemandeRenteVb.isModifie()) {

                // Validation du contenu du viewBean (Validation minimum
                validate(transaction, saisieDemandeRenteVb, session, Boolean.TRUE);

                // voir si la demande de rente existe déjà, si oui, update,
                // sinon add
                if (JadeStringUtil.isIntegerEmpty(saisieDemandeRenteVb.getIdDemandeRente())) {
                    // Si pas d'erreurs, création de la demande (prestations)
                    if (!saisieDemandeRenteVb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        setDemandePrestation(transaction, saisieDemandeRenteVb, saisieDemandeRenteVb.getIdRequerant(),
                                session, true);
                    }
                    // si toujours pas d'erreurs, création de la demande de
                    // rente
                    if (!saisieDemandeRenteVb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) setDemandeRente(transaction,
                                saisieDemandeRenteVb, session);
                    }
                } else {
                    if (!saisieDemandeRenteVb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) updateDemandeRente(transaction,
                                saisieDemandeRenteVb, session);
                    }
                }

            }

            if (Boolean.TRUE.equals(saisieDemandeRenteVb.getIsAccuseDeReception())) {
                REAccuseDeReceptionOO process = new REAccuseDeReceptionOO();
                process.setSession(session);
                process.setNssRequerant(saisieDemandeRenteVb.getNssRequerant());
                process.setNomRequerant(saisieDemandeRenteVb.getNomRequerant());
                process.setPrenomRequerant(saisieDemandeRenteVb.getPrenomRequerant());
                process.setIdRequerant(saisieDemandeRenteVb.getIdRequerant());
                process.setEmailAdresse(session.getUserEMail());
                process.setDateReceptionDemande(saisieDemandeRenteVb.getDateReception());
                process.setIdGestionnaire(saisieDemandeRenteVb.getIdGestionnaire());
                BProcessLauncher.start(process, false);
            }

            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_STANDARD, saisieDemandeRenteVb.getIdTiers());
            sf.addMembreCelibataire(saisieDemandeRenteVb.getIdTiers());

        } catch (Exception e) {
            transaction.setRollbackOnly();
            throw e;
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                transaction.closeTransaction();
            }
        }

        // MAJ de la période de la demande !!!
        if (!JadeStringUtil.isEmpty(saisieDemandeRenteVb.getIdDemandeRente())) {
            doMAJPeriodeDemandeRente(session, transaction, saisieDemandeRenteVb);
        }
        return saisieDemandeRenteVb;

    }

    /**
     * On sauve les info du viewBean.
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface calculerCopie(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        // Reprise du viewBean
        RESaisieDemandeRenteViewBean saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) viewBean;

        // Création de la transaction
        BTransaction transaction = null;
        transaction = (BTransaction) (session).newTransaction();

        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }
        if (saisieDemandeRenteVb.isModifie()) {
            try {

                // Validation du contenu du viewBean
                validate(transaction, saisieDemandeRenteVb, session, Boolean.FALSE);

                if (!saisieDemandeRenteVb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) updateDemandeRente(transaction,
                            saisieDemandeRenteVb, session);
                }

            } catch (Exception e) {
                transaction.setRollbackOnly();
                throw e;
            } finally {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }

        }
        return saisieDemandeRenteVb;

    }

    private FWViewBeanInterface doMAJPeriodeDemandeRente(BSession session, BTransaction transaction,
            RESaisieDemandeRenteViewBean vb) throws Exception {

        RERenteAccJoinTblTiersJoinDemRenteManager managerRenteAcc = new RERenteAccJoinTblTiersJoinDemRenteManager();
        managerRenteAcc.setSession(session);
        managerRenteAcc.setForNoDemandeRente(vb.getIdDemandeRente());
        managerRenteAcc.find(BManager.SIZE_NOLIMIT);

        boolean hasFoundRenteAcc = false;
        JADate dateDebutFinal = null;
        JADate dateFinFinal = null;
        REDemandeRente dem = null;

        JACalendar cal = new JACalendarGregorian();

        /**
         * RECHERCHE DES RENTES ACCORDÉES, SI ON TROUVE AU MOINS 1, ON NE FAIT RIEN D'AUTRE QUE PRENDRE LES DATES
         * LIMITES.
         */
        if (managerRenteAcc.size() > 0) {
            boolean hasFoundDateSansFin = false;
            for (int i = 0; i < managerRenteAcc.size(); i++) {
                RERenteAccJoinTblTiersJoinDemandeRente renteAccordee = (RERenteAccJoinTblTiersJoinDemandeRente) managerRenteAcc
                        .get(i);

                // Gestion de la date de début
                if (!JadeStringUtil.isBlankOrZero(renteAccordee.getDateDebutDroit())) {
                    final JADate dateDebutCurrent = new JADate(renteAccordee.getDateDebutDroit());
                    dateDebutFinal = takeLowerDateDebut(dateDebutFinal, cal, dateDebutCurrent);
                }

                // Gestion de la date de fin, si au moins une fois une date de fin vide alors on la prend comme
                // référence
                if (!JadeStringUtil.isBlankOrZero(renteAccordee.getDateFinDroit()) && !hasFoundDateSansFin) {
                    final JADate datefinCurrent = new JADate(renteAccordee.getDateFinDroit());
                    dateFinFinal = takeUpperDateFin(dateFinFinal, cal, datefinCurrent);
                } else {
                    hasFoundDateSansFin = true;
                    dateFinFinal = null;
                }
            }

            hasFoundRenteAcc = true;
        }

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, vb.getIdTiers());
        String dateDecesTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);

        /**
         * DEMANDE RENTE INVALIDITÉ
         */
        if (isSameTypeDemande(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE, vb.getCsTypeDemandeRente())) {
            dem = new REDemandeRenteInvalidite();
            dem.setSession(session);
            dem.setIdDemandeRente(vb.getIdDemandeRente());
            dem.retrieve(transaction);

            // Si aucune rentes accordées trouvées, on se base sur les périodes d'invalidités pour les dates limites
            if (!hasFoundRenteAcc) {
                REPeriodeInvaliditeManager mgr = new REPeriodeInvaliditeManager();
                mgr.setSession(session);
                mgr.setForIdDemandeRente(dem.getIdDemandeRente());
                mgr.find(transaction, BManager.SIZE_USEDEFAULT);

                boolean hasFoundDateSansFin = false;
                for (int i = 0; i < mgr.size(); i++) {

                    REPeriodeInvalidite periode = (REPeriodeInvalidite) mgr.get(i);

                    // Gestion de la date de début
                    if (!JadeStringUtil.isBlankOrZero(periode.getDateDebutInvalidite())) {
                        final JADate dateDebutCurrent = new JADate(periode.getDateDebutInvalidite());
                        dateDebutFinal = takeLowerDateDebut(dateDebutFinal, cal, dateDebutCurrent);
                    }

                    // Gestion de la date de fin, si au moins une fois une date de fin vide alors on la prend comme
                    // référence
                    if (!JadeStringUtil.isBlankOrZero(periode.getDateFinInvalidite()) && !hasFoundDateSansFin) {
                        final JADate datefinCurrent = new JADate(periode.getDateFinInvalidite());
                        dateFinFinal = takeUpperDateFin(dateFinFinal, cal, datefinCurrent);
                    } else {
                        hasFoundDateSansFin = true;
                        dateFinFinal = null;
                    }

                }

                // La date de décès fait foi
                if (JadeDateUtil.isGlobazDate(dateDecesTiers)) {
                    dateFinFinal = new JADate(JadeDateUtil.getLastDateOfMonth(dateDecesTiers));
                }
            }

            /**
             * DEMANDE RENTE API
             */
        } else if (isSameTypeDemande(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API, vb.getCsTypeDemandeRente())) {
            dem = new REDemandeRenteAPI();
            dem.setSession(session);
            dem.setIdDemandeRente(vb.getIdDemandeRente());
            dem.retrieve(transaction);

            // Si aucune rentes accordées trouvées, on se base sur les périodes API pour les dates limites
            if (!hasFoundRenteAcc) {

                REPeriodeAPIManager mgr = new REPeriodeAPIManager();
                mgr.setSession(session);
                mgr.setForIdDemandeRente(dem.getIdDemandeRente());
                mgr.find(transaction, BManager.SIZE_USEDEFAULT);

                boolean hasFoundDateSansFin = false;
                // Recherche de la limite de la date début la plus petite et la date de fin la plus grande des
                // périodes
                for (int i = 0; i < mgr.size(); i++) {

                    REPeriodeAPI p = (REPeriodeAPI) mgr.get(i);

                    // Gestion de la date de début
                    if (!JadeStringUtil.isBlankOrZero(p.getDateDebutInvalidite())) {
                        final JADate idd = new JADate(p.getDateDebutInvalidite());
                        dateDebutFinal = takeLowerDateDebut(dateDebutFinal, cal, idd);
                    }

                    // Gestion de la date de fin, si au moins une fois une date de fin vide alors on la prend comme
                    // référence
                    if (!JadeStringUtil.isBlankOrZero(p.getDateFinInvalidite()) && !hasFoundDateSansFin) {
                        final JADate idf = new JADate(p.getDateFinInvalidite());
                        dateFinFinal = takeUpperDateFin(dateFinFinal, cal, idf);
                    } else {
                        hasFoundDateSansFin = true;
                        dateFinFinal = null;
                    }
                }

                // La date de décès fait foi
                if (JadeDateUtil.isGlobazDate(dateDecesTiers)) {
                    // On prend le dernier jour du mois de son décès
                    dateFinFinal = new JADate(JadeDateUtil.getLastDateOfMonth(dateDecesTiers));
                }
            }

            /**
             * DEMANDE RENTE SURVIVANT
             */
        } else if (isSameTypeDemande(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT, vb.getCsTypeDemandeRente())) {
            dem = new REDemandeRenteSurvivant();
            dem.setSession(session);
            dem.setIdDemandeRente(vb.getIdDemandeRente());
            dem.retrieve(transaction);

            // Si aucune rentes accordées trouvées, on se base sur la date de décès du requérant
            if (!hasFoundRenteAcc) {

                if (!JadeStringUtil.isBlankOrZero(dateDecesTiers)) {

                    // Prendre le premier jour du mois suivant de la date de décès pour le début de la demande
                    String decesAuPremierDuMoisSuivant = JadeDateUtil.addMonths(dateDecesTiers, 1);
                    decesAuPremierDuMoisSuivant = JadeDateUtil.getFirstDateOfMonth(decesAuPremierDuMoisSuivant);

                    if (!JadeStringUtil.isBlankOrZero(decesAuPremierDuMoisSuivant)) {
                        dateDebutFinal = new JADate(decesAuPremierDuMoisSuivant);
                    }
                }
            }

            /**
             * DEMANDE RENTE VIEILLESSE
             */
        } else if (isSameTypeDemande(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE, vb.getCsTypeDemandeRente())) {
            dem = new REDemandeRenteVieillesse();
            dem.setSession(session);
            dem.setIdDemandeRente(vb.getIdDemandeRente());
            dem.retrieve(transaction);

            // Si aucune rentes accordées trouvées, alors on se base sur le calcul de la date de la retraite du
            // requérant
            if (!hasFoundRenteAcc) {
                // Si la date est non valide selon date globaz, (possible) car c'est un requérant que l'on connait
                // que
                // l'année mais par forcément le jour ou le mois, alors nous lui affectons 01.07.ANNEE
                String dateNaissance = majDateNaissanceIfNoValid(vb.getDateNaissanceRequerant());

                // On calcule la date de la retraite pour le mettre dans la date de début
                final String dateRetraite = calculDateRetraite(vb, new JADate(dateNaissance));

                if (!JadeStringUtil.isBlankOrZero(dateRetraite)) {
                    dateDebutFinal = new JADate(dateRetraite);
                }
            }
        }

        // Si la demande est null, c'est que l'on n'a pas passé par un des 4 types de rentes
        if (dem != null) {
            if (dateDebutFinal != null) {
                dem.setDateDebut(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateDebutFinal.toStrAMJ()));
            }

            if ((dateFinFinal == null)
                    || (cal.compare(dateFinFinal, new JADate("31.12.2099")) == JACalendar.COMPARE_EQUALS)) {
                dem.setDateFin("");
            } else {
                dem.setDateFin(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateFinFinal.toStrAMJ()));
            }

            dem.update(transaction);
        }
        return vb;
    }

    private String majDateNaissanceIfNoValid(final String dateNaissance) {
        String dateNaissanceUpdated = dateNaissance;

        if (!JadeDateUtil.isGlobazDate(dateNaissanceUpdated)) {
            if (dateNaissanceUpdated.length() == 10) {
                String jour = dateNaissanceUpdated.substring(0, 2);
                jour = Long.valueOf(jour) < 1L || Long.valueOf(jour) > 31L ? "01" : jour;

                String mois = dateNaissanceUpdated.substring(3, 5);
                mois = Long.valueOf(mois) < 1L || Long.valueOf(mois) > 12L ? "07" : mois;

                String annee = dateNaissanceUpdated.substring(6, 10);

                dateNaissanceUpdated = jour + "." + mois + "." + annee;
            }
        }

        return dateNaissanceUpdated;
    }

    private JADate takeUpperDateFin(JADate firstDateFin, JACalendar cal, JADate secondDateFin) throws JAException {
        JADate dateToReturn = firstDateFin;
        // On set date fin du droit avec la plus grande date de fin des périodes du droit.
        if ((firstDateFin == null) && (secondDateFin != null)) {
            dateToReturn = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(secondDateFin.toStrAMJ()));
        } else if (firstDateFin != null) {
            if (secondDateFin == null) {
                dateToReturn = new JADate("31.12.2099");
            } else {
                if (cal.compare(firstDateFin, secondDateFin) == JACalendar.COMPARE_SECONDUPPER) {
                    dateToReturn = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(secondDateFin
                            .toStrAMJ()));
                }
            }
        }
        return dateToReturn;
    }

    private JADate takeLowerDateDebut(final JADate firstDateDebut, final JACalendar cal, final JADate secondDateDebut)
            throws JAException {
        JADate dateToReturn = firstDateDebut;

        // On set date debut du droit avec la plus petite date de début des périodes du droit.
        if ((firstDateDebut == null) && (secondDateDebut != null)) {

            dateToReturn = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(secondDateDebut.toStrAMJ()));
        } else if (firstDateDebut != null && secondDateDebut != null) {

            if (cal.compare(firstDateDebut, secondDateDebut) == JACalendar.COMPARE_SECONDLOWER) {
                dateToReturn = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(secondDateDebut.toStrAMJ()));
            }
        }

        return dateToReturn;
    }

    private boolean isSameTypeDemande(final String typeConstante, final String typeDemande) {
        return typeConstante.equals(typeDemande);
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session){
        return deleguerExecute(viewBean, action, session);
    }

    private FWViewBeanInterface majEditionPartielle(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        // Reprise du viewBean
        RESaisieDemandeRenteViewBean saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) viewBean;

        // Création de la transaction
        BTransaction transaction = null;
        transaction = (BTransaction) (session).newTransaction();

        try {
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            REDemandeRente demande = null;
            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(saisieDemandeRenteVb.getCsTypeDemandeRente())) {
                demande = new REDemandeRenteAPI();
            } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(saisieDemandeRenteVb
                    .getCsTypeDemandeRente())) {
                demande = new REDemandeRenteInvalidite();
            } else {
                throw new Exception(
                        "Action impossible pour ce type de demande (valable uniquement pour demande API / Invalidité)");
            }
            demande.setSession(session);
            demande.setIdDemandeRente(saisieDemandeRenteVb.getIdDemandeRente());
            demande.retrieve(transaction);
            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(saisieDemandeRenteVb.getCsTypeDemandeRente())) {
                ((REDemandeRenteAPI) demande).setCsGenrePrononceAI(saisieDemandeRenteVb.getCsGenrePrononceAiApi());
                ((REDemandeRenteAPI) demande).setNbPageMotivation(saisieDemandeRenteVb.getNbPagesMotivationApi());
            } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(saisieDemandeRenteVb
                    .getCsTypeDemandeRente())) {
                ((REDemandeRenteInvalidite) demande).setCsGenrePrononceAI(saisieDemandeRenteVb
                        .getCsGenrePrononceAiInv());
                ((REDemandeRenteInvalidite) demande)
                        .setNbPageMotivation(saisieDemandeRenteVb.getNbPagesMotivationInv());
            }

            demande.update(transaction);
            return saisieDemandeRenteVb;
        } catch (Exception e) {
            transaction.setRollbackOnly();
            throw e;
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                transaction.closeTransaction();
            }
        }

    }

    public FWViewBeanInterface majPartielleDemande(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        return majEditionPartielle(viewBean, action, session);

    }

    private String getTypePrestationAPI(REPeriodeAPI periodeAPI, BSession session) throws Exception {

        boolean isAPIAI = IREDemandeRente.CS_GENRE_DROIT_API_API_AI_PRST.equalsIgnoreCase(periodeAPI
                .getCsGenreDroitApi())
                || IREDemandeRente.CS_GENRE_DROIT_API_API_AI_RENTE.equalsIgnoreCase(periodeAPI.getCsGenreDroitApi());
        boolean isBefore2004 = BSessionUtil.compareDateFirstLower(session, periodeAPI.getDateDebutInvalidite(),
                "01.01.2004");

        // Recherche du type de prestation
        int typePrestation = 0;

        if (isAPIAI && isBefore2004) {
            typePrestation = getTypePrestationAPIAIAvant2004(periodeAPI.getCsDegreImpotence());
        } else if (!isAPIAI && isBefore2004) {
            typePrestation = getTypePrestationAPIAVSAvant2004(periodeAPI.getCsDegreImpotence());
        } else if (isAPIAI && !isBefore2004) {
            typePrestation = Integer.valueOf(
                    getTypePrestationAPIAI(periodeAPI.getCsDegreImpotence(), periodeAPI.getIsResidenceHome(),
                            periodeAPI.getIsAssistancePratique())).intValue();

        } else if (!isAPIAI && !isBefore2004) {

            typePrestation = Integer.valueOf(
                    getTypePrestationAPIAVS(periodeAPI.getCsDegreImpotence(), periodeAPI.getIsResidenceHome(),
                            periodeAPI.getCsGenreDroitApi(), periodeAPI.getDateDebutInvalidite(),
                            periodeAPI.getTypePrestationHistorique())).intValue();
        } else {
            throw new CommonTechnicalException("Not implemented");
        }

        return String.valueOf(typePrestation);

    }

    private int getTypePrestationAPIAVSAvant2004(String csDegreImpotence) throws Exception {

        if (IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equals(csDegreImpotence)) {
            return 95;
        } else if (IREDemandeRente.CS_DEGRE_IMPOTENCE_MOYEN.equals(csDegreImpotence)) {
            return 96;
        } else if (IREDemandeRente.CS_DEGRE_IMPOTENCE_GRAVE.equals(csDegreImpotence)) {
            return 97;
        } else {
            throw new CommonTechnicalException("Not implemented");
        }

    }

    private String getTypePrestationAPIAI(String degreImpotence, boolean isResidenceHome, boolean isAssistancePratique) {

        boolean isDegreImpotenceFaible = IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equalsIgnoreCase(degreImpotence);
        boolean isDegreImpotenceMoyen = IREDemandeRente.CS_DEGRE_IMPOTENCE_MOYEN.equalsIgnoreCase(degreImpotence);
        boolean isDegreImpotenceGrave = IREDemandeRente.CS_DEGRE_IMPOTENCE_GRAVE.equalsIgnoreCase(degreImpotence);

        if (isDegreImpotenceFaible && !isResidenceHome && !isAssistancePratique) {
            return "81";
        } else if (isDegreImpotenceMoyen && !isResidenceHome && !isAssistancePratique) {
            return "82";
        } else if (isDegreImpotenceGrave && !isResidenceHome && !isAssistancePratique) {
            return "83";
        } else if (isDegreImpotenceFaible && isResidenceHome && !isAssistancePratique) {
            return "91";
        } else if (isDegreImpotenceMoyen && isResidenceHome && !isAssistancePratique) {
            return "92";
        } else if (isDegreImpotenceGrave && isResidenceHome && !isAssistancePratique) {
            return "93";
        } else if (isDegreImpotenceFaible && !isResidenceHome && isAssistancePratique) {
            return "84";
        } else if (isDegreImpotenceMoyen && !isResidenceHome && isAssistancePratique) {
            return "88";
        } else {
            throw new CommonTechnicalException("Not implemented");
        }

    }

    private String getTypePrestationAPIAVS(String degreImpotence, boolean isResidenceHome, String genreDroitAPI,
            String dateDebutPeriode, String typePrestationAnterieur) {

        boolean isDegreImpotenceFaible = IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equalsIgnoreCase(degreImpotence);
        boolean isDegreImpotenceMoyen = IREDemandeRente.CS_DEGRE_IMPOTENCE_MOYEN.equalsIgnoreCase(degreImpotence);
        boolean isDegreImpotenceGrave = IREDemandeRente.CS_DEGRE_IMPOTENCE_GRAVE.equalsIgnoreCase(degreImpotence);

        boolean isGenreDroitAPI3 = IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_SUITE_API_AI
                .equalsIgnoreCase(genreDroitAPI);
        boolean isGenreDroitAPI4 = IREDemandeRente.CS_GENRE_DROIT_API_API_AVS_RETRAITE.equalsIgnoreCase(genreDroitAPI);

        boolean isDateDebutPeriodeGreaterEgalJuillet2014 = JadeDateUtil.isDateAfter(dateDebutPeriode, "30.06.2014");

        boolean isAPIAVS86Anterieur = "86".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI81Or84Anterieur = "81".equalsIgnoreCase(typePrestationAnterieur)
                || "84".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI82Or88Anterieur = "82".equalsIgnoreCase(typePrestationAnterieur)
                || "88".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI83Anterieur = "83".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI91Anterieur = "91".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI92Anterieur = "92".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAI93Anterieur = "93".equalsIgnoreCase(typePrestationAnterieur);
        boolean isAPIAVS95Anterieur = "95".equalsIgnoreCase(typePrestationAnterieur);

        if (isDegreImpotenceFaible && !isResidenceHome && isGenreDroitAPI4 && isDateDebutPeriodeGreaterEgalJuillet2014
                && isAPIAVS86Anterieur) {
            return "95";
        } else if (isDegreImpotenceFaible && !isResidenceHome && isGenreDroitAPI4) {
            return "89";
        }

        else if (isDegreImpotenceMoyen && !isResidenceHome && isGenreDroitAPI4) {
            return "96";
        } else if (isDegreImpotenceGrave && !isResidenceHome && isGenreDroitAPI4
                && isDateDebutPeriodeGreaterEgalJuillet2014 && isAPIAVS86Anterieur) {
            return "97";
        } else if (isDegreImpotenceGrave && !isResidenceHome && isGenreDroitAPI4) {
            return "97";
        } else if (isDegreImpotenceMoyen && isResidenceHome && isGenreDroitAPI4) {
            return "96";
        } else if (isDegreImpotenceGrave && isResidenceHome && isGenreDroitAPI4) {
            return "97";
        } else if (isDegreImpotenceFaible && !isResidenceHome && isGenreDroitAPI3 && isAPIAI81Or84Anterieur) {
            return "85";
        } else if (isDegreImpotenceMoyen && !isResidenceHome && isGenreDroitAPI3 && isAPIAI82Or88Anterieur) {
            return "86";
        } else if (isDegreImpotenceGrave && !isResidenceHome && isGenreDroitAPI3 && isAPIAI83Anterieur) {
            return "87";
        } else if (isDegreImpotenceFaible && isResidenceHome && isGenreDroitAPI3
                && isDateDebutPeriodeGreaterEgalJuillet2014 && (isAPIAI91Anterieur || isAPIAVS95Anterieur)) {
            return "94";
        } else if (isDegreImpotenceFaible && isResidenceHome && isGenreDroitAPI3
                && !isDateDebutPeriodeGreaterEgalJuillet2014 && isAPIAI91Anterieur) {
            return "95";
        } else if (isDegreImpotenceFaible && isResidenceHome && isGenreDroitAPI3) {
            return "95";
        } else if (isDegreImpotenceMoyen && isResidenceHome && isGenreDroitAPI3 && isAPIAI92Anterieur) {
            return "96";
        } else if (isDegreImpotenceGrave && isResidenceHome && isGenreDroitAPI3 && isAPIAI93Anterieur) {
            return "97";
        }

        else {
            throw new CommonTechnicalException("Not implemented");
        }

    }

    private int getTypePrestationAPIAIAvant2004(String csDegreImpotence) throws Exception {

        if (IREDemandeRente.CS_DEGRE_IMPOTENCE_FAIBLE.equals(csDegreImpotence)) {
            return 91;
        } else if (IREDemandeRente.CS_DEGRE_IMPOTENCE_MOYEN.equals(csDegreImpotence)) {
            return 92;
        } else if (IREDemandeRente.CS_DEGRE_IMPOTENCE_GRAVE.equals(csDegreImpotence)) {
            return 93;
        } else {
            throw new CommonTechnicalException("Not implemented");
        }
    }

    private String getTypePrestationAPIPrecedent(String idTiersBeneficiaire,
            Set<REPeriodeAPIViewBean> treeSetPeriodesAPI, REPeriodeAPI newPeriodeAPI, BSession session)
            throws Exception {

        if (!JadeStringUtil.isBlankOrZero(newPeriodeAPI.getTypePrestationHistorique())) {
            return newPeriodeAPI.getTypePrestationHistorique();
        }

        String dateInMonthBeforeNouvellePeriode = new JACalendarGregorian().addMonths(
                newPeriodeAPI.getDateDebutInvalidite(), -1);
        String moisAnneePrecedentNouvellePeriode = JACalendar.format(dateInMonthBeforeNouvellePeriode,
                JACalendar.FORMAT_MMsYYYY);

        RERenteAccordeeManager renteAccordeeManager = new RERenteAccordeeManager();
        renteAccordeeManager.setSession(session);
        renteAccordeeManager.setForIdTiersBeneficiaire(idTiersBeneficiaire);
        renteAccordeeManager.setForCodesPrestationsIn("'81','82','83','84','86','88','91','92','93','95'");
        renteAccordeeManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_DIMINUE + ","
                + IREPrestationAccordee.CS_ETAT_PARTIEL + "," + IREPrestationAccordee.CS_ETAT_VALIDE);
        renteAccordeeManager.setForEnCoursAtMois(moisAnneePrecedentNouvellePeriode);
        renteAccordeeManager.find(BManager.SIZE_NOLIMIT);

        if (renteAccordeeManager.getSize() == 1) {
            RERenteAccordee aRenteAccordee = (RERenteAccordee) renteAccordeeManager.getFirstEntity();
            return aRenteAccordee.getCodePrestation();
        }

        for (REPeriodeAPIViewBean aPeriodeAPI : treeSetPeriodesAPI) {

            if (!JadeStringUtil.isBlankOrZero(aPeriodeAPI.getDateFinInvalidite())) {
                String moisAnneeFinPeriode = JACalendar.format(aPeriodeAPI.getDateFinInvalidite(),
                        JACalendar.FORMAT_MMsYYYY);

                if (moisAnneeFinPeriode.equalsIgnoreCase(moisAnneePrecedentNouvellePeriode)) {
                    return aPeriodeAPI.getTypePrestation();
                }
            }
        }

        return "";
    }

    private PRDemande setDemandePrestation(BTransaction transaction, RESaisieDemandeRenteViewBean saisieDemandeRenteVB,
            String idTiers, BSession session, boolean creeSiNecessaire) throws Exception {

        PRDemande retValue = null;
        String typeDemande = IPRDemande.CS_TYPE_RENTE;

        if (PRDemande.ID_TIERS_DEMANDE_BIDON.equals(idTiers)
                && (PRDemande.getDemandeBidon(session, typeDemande) != null)) {

            retValue = PRDemande.getDemandeBidon(session, typeDemande);

        } else {

            PRDemandeManager mgr = new PRDemandeManager();

            mgr.setSession(session);
            mgr.setForIdTiers(JadeStringUtil.isEmpty(idTiers) ? PRDemande.ID_TIERS_DEMANDE_BIDON : idTiers);
            mgr.setForTypeDemande(typeDemande);
            mgr.find(transaction);

            if (mgr.isEmpty() && creeSiNecessaire) {
                retValue = new PRDemande();
                retValue.setIdTiers(idTiers);
                retValue.setEtat(IPRDemande.CS_ETAT_OUVERT);
                retValue.setTypeDemande(typeDemande);
                retValue.setSession(session);
                retValue.add(transaction);
            } else if (!mgr.isEmpty()) {
                retValue = (PRDemande) mgr.get(0);
            }
        }

        if ((retValue != null) && !transaction.hasErrors()) {
            saisieDemandeRenteVB.setIdDemandePrestation(retValue.getIdDemande());
            saisieDemandeRenteVB.setIdRequerant(retValue.getIdTiers());
        }

        return retValue;
    }

    private FWViewBeanInterface setDemandeRente(BTransaction transaction,
            RESaisieDemandeRenteViewBean saisieDemandeRenteVB, BSession session) throws Exception {

        // Savoir quel type de demande c'est et l'insérer dans la base
        if (saisieDemandeRenteVB.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {

            REDemandeRenteVieillesse renteVieillesse = new REDemandeRenteVieillesse();

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(saisieDemandeRenteVB.getCsEtat())) {
                if (JadeStringUtil.isEmpty(saisieDemandeRenteVB.getDateFin())) {

                    // Calcul de la periode de début de droit (age de la retraite du tiers)
                    String dateNaissance = saisieDemandeRenteVB.getDateNaissanceRequerant();

                    String dateRetraite = calculDateRetraite(saisieDemandeRenteVB, new JADate(dateNaissance));
                    renteVieillesse.setDateDebut(dateRetraite);
                }
            }

            renteVieillesse.setIdDemandePrestation(saisieDemandeRenteVB.getIdDemandePrestation());
            renteVieillesse.setIdDemandeRenteParent(saisieDemandeRenteVB.getIdDemandeRenteParent());
            renteVieillesse.setDateTraitement(saisieDemandeRenteVB.getDateTraitement());
            renteVieillesse.setDateDepot(saisieDemandeRenteVB.getDateDepot());
            renteVieillesse.setDateReception(saisieDemandeRenteVB.getDateReception());
            renteVieillesse.setCsEtat(saisieDemandeRenteVB.getCsEtat());
            renteVieillesse.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteVieillesse.setIdInfoComplementaire(saisieDemandeRenteVB.getIdInfoComplementaire());
            renteVieillesse.setCsTypeCalcul(saisieDemandeRenteVB.getCsTypeCalcul());
            renteVieillesse.setCsTypeDemandeRente(saisieDemandeRenteVB.getCsTypeDemandeRente());
            renteVieillesse.setIdGestionnaire(saisieDemandeRenteVB.getIdGestionnaire());

            renteVieillesse.setCsAnneeAnticipation(saisieDemandeRenteVB.getCsAnneeAnticipationVie());
            renteVieillesse.setIsAjournementRequerant(saisieDemandeRenteVB.getIsAjournementRequerantVie());
            renteVieillesse.setDateRevocationRequerant(saisieDemandeRenteVB.getDateRevocationRequerantVie());

            renteVieillesse.setSession(session);
            renteVieillesse.add(transaction);

            if (!session.getCurrentThreadTransaction().hasErrors()) {
                saisieDemandeRenteVB.setIdDemandeRente(renteVieillesse.getIdDemandeRente());
            }

        } else if (saisieDemandeRenteVB.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {

            REDemandeRenteAPI renteAPI = new REDemandeRenteAPI();

            renteAPI.setIdDemandePrestation(saisieDemandeRenteVB.getIdDemandePrestation());
            renteAPI.setIdDemandeRenteParent(saisieDemandeRenteVB.getIdDemandeRenteParent());
            renteAPI.setDateTraitement(saisieDemandeRenteVB.getDateTraitement());
            renteAPI.setDateDepot(saisieDemandeRenteVB.getDateDepot());
            renteAPI.setDateReception(saisieDemandeRenteVB.getDateReception());
            renteAPI.setCsEtat(saisieDemandeRenteVB.getCsEtat());
            renteAPI.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteAPI.setIdInfoComplementaire(saisieDemandeRenteVB.getIdInfoComplementaire());
            renteAPI.setCsTypeCalcul(saisieDemandeRenteVB.getCsTypeCalcul());
            renteAPI.setCsTypeDemandeRente(saisieDemandeRenteVB.getCsTypeDemandeRente());
            renteAPI.setIdGestionnaire(saisieDemandeRenteVB.getIdGestionnaire());
            renteAPI.setNbPageMotivation(saisieDemandeRenteVB.getNbPagesMotivationApi());

            renteAPI.setCsGenrePrononceAI(saisieDemandeRenteVB.getCsGenrePrononceAiApi());
            renteAPI.setCsInfirmite(saisieDemandeRenteVB.getCsInfirmiteApi());
            renteAPI.setCsAtteinte(saisieDemandeRenteVB.getCsAtteinteApi());
            renteAPI.setCodeOfficeAI(saisieDemandeRenteVB.getCodeOfficeAiApi());
            renteAPI.setDateSuvenanceEvenementAssure(saisieDemandeRenteVB.getDateSurvenanceEvenementAssureAPI());

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, saisieDemandeRenteVB.getIdRequerant());
            if (tw == null) {
                throw new Exception("Tiers not found !! idTiers/idDemandeRente : " + saisieDemandeRenteVB.getIdTiers()
                        + "/" + saisieDemandeRenteVB.getIdDemandeRente());
            }

            if (!JadeStringUtil.isBlankOrZero(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES))) {
                renteAPI.setDateFin(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES));
            }

            renteAPI.setSession(session);
            renteAPI.add(transaction);

            // Ajout des périodes API
            for (REPeriodeAPI periodeApi : saisieDemandeRenteVB.getPeriodesAPI()) {

                periodeApi.setSession(session);
                periodeApi.setIdDemandeRente(renteAPI.getIdDemandeRente());
                periodeApi.add(transaction);
            }

            if (!transaction.hasErrors()) {
                saisieDemandeRenteVB.setIdDemandeRente(renteAPI.getIdDemandeRente());
            }

        } else if (saisieDemandeRenteVB.getCsTypeDemandeRente()
                .equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {

            REDemandeRenteInvalidite renteInvalidite = new REDemandeRenteInvalidite();

            renteInvalidite.setIdDemandePrestation(saisieDemandeRenteVB.getIdDemandePrestation());
            renteInvalidite.setIdDemandeRenteParent(saisieDemandeRenteVB.getIdDemandeRenteParent());
            renteInvalidite.setDateTraitement(saisieDemandeRenteVB.getDateTraitement());
            renteInvalidite.setDateDepot(saisieDemandeRenteVB.getDateDepot());
            renteInvalidite.setDateReception(saisieDemandeRenteVB.getDateReception());
            renteInvalidite.setCsEtat(saisieDemandeRenteVB.getCsEtat());
            renteInvalidite.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteInvalidite.setIdInfoComplementaire(saisieDemandeRenteVB.getIdInfoComplementaire());
            renteInvalidite.setCsTypeCalcul(saisieDemandeRenteVB.getCsTypeCalcul());
            renteInvalidite.setCsTypeDemandeRente(saisieDemandeRenteVB.getCsTypeDemandeRente());
            renteInvalidite.setIdGestionnaire(saisieDemandeRenteVB.getIdGestionnaire());
            renteInvalidite.setNbPageMotivation(saisieDemandeRenteVB.getNbPagesMotivationInv());

            renteInvalidite.setCsGenrePrononceAI(saisieDemandeRenteVB.getCsGenrePrononceAiInv());
            renteInvalidite.setCsInfirmite(saisieDemandeRenteVB.getCsInfirmiteInv());
            renteInvalidite.setCsAtteinte(saisieDemandeRenteVB.getCsAtteinteInv());
            renteInvalidite.setCodeOfficeAI(saisieDemandeRenteVB.getCodeOfficeAiInv());
            renteInvalidite.setPourcentageReduction(saisieDemandeRenteVB.getPourcentageReductionInv());
            renteInvalidite.setDateSuvenanceEvenementAssure(saisieDemandeRenteVB.getDateSurvenanceEvenementAssureInv());
            renteInvalidite.setPourcentRedFauteGrave(saisieDemandeRenteVB.getPourcentRedFauteGrave());
            renteInvalidite.setPourcentRedNonCollaboration(saisieDemandeRenteVB.getPourcentRedNonCollaboration());
            renteInvalidite.setDateDebutRedNonCollaboration(saisieDemandeRenteVB.getDateDebutRedNonCollaboration());
            renteInvalidite.setDateFinRedNonCollaboration(saisieDemandeRenteVB.getDateFinRedNonCollaboration());

            renteInvalidite.setSession(session);
            renteInvalidite.add(transaction);

            if (!transaction.hasErrors()) {
                saisieDemandeRenteVB.setIdDemandeRente(renteInvalidite.getIdDemandeRente());
            }

            // Ajouter aussi les périodes Invalidité
            for (REPeriodeInvalidite periodeInv : saisieDemandeRenteVB.getPeriodesInvalidite()) {
                periodeInv.setSession(session);
                periodeInv.setIdDemandeRente(renteInvalidite.getIdDemandeRente());
                periodeInv.add(transaction);
            }

        } else if (saisieDemandeRenteVB.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {

            REDemandeRenteSurvivant renteSurvivant = new REDemandeRenteSurvivant();

            renteSurvivant.setIdDemandePrestation(saisieDemandeRenteVB.getIdDemandePrestation());
            renteSurvivant.setIdDemandeRenteParent(saisieDemandeRenteVB.getIdDemandeRenteParent());
            renteSurvivant.setDateTraitement(saisieDemandeRenteVB.getDateTraitement());
            renteSurvivant.setDateDepot(saisieDemandeRenteVB.getDateDepot());
            renteSurvivant.setDateReception(saisieDemandeRenteVB.getDateReception());
            renteSurvivant.setCsEtat(saisieDemandeRenteVB.getCsEtat());
            renteSurvivant.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteSurvivant.setIdInfoComplementaire(saisieDemandeRenteVB.getIdInfoComplementaire());
            renteSurvivant.setCsTypeCalcul(saisieDemandeRenteVB.getCsTypeCalcul());
            renteSurvivant.setCsTypeDemandeRente(saisieDemandeRenteVB.getCsTypeDemandeRente());
            renteSurvivant.setIdGestionnaire(saisieDemandeRenteVB.getIdGestionnaire());

            renteSurvivant.setPourcentageReduction(saisieDemandeRenteVB.getPourcentageReductionSur());

            // si la damande est de type survivant, creation de l'information
            // complementaire pour stocker "Tiers responsable"
            PRInfoCompl ic = new PRInfoCompl();
            ic.setSession(session);
            ic.setCsTiersResponsable(saisieDemandeRenteVB.getInfoComplCsTiersResponsable());
            ic.add(transaction);

            renteSurvivant.setIdInfoComplementaire(ic.getIdInfoCompl());

            // ajout de la rente survivant
            renteSurvivant.setSession(session);
            renteSurvivant.add(transaction);

            if (!transaction.hasErrors()) {
                saisieDemandeRenteVB.setIdDemandeRente(renteSurvivant.getIdDemandeRente());
                saisieDemandeRenteVB.setIdInfoComplementaire(ic.getIdInfoCompl());
            }

        }

        return saisieDemandeRenteVB;
    }

    private FWViewBeanInterface setUpdateInfoComp(BTransaction transaction, REInfoComplViewBean viewBean,
            BSession session, boolean isUpdate) throws Exception {

        if (isUpdate) {
            viewBean.update(transaction);
        } else {
            viewBean.add(transaction);

            // Modifier également la demande de rente (ajouter l'idInfoCOmpl)
            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(session);
            demandeRente.setIdDemandeRente(viewBean.getIdDemandeRente());
            demandeRente.retrieve(transaction);

            demandeRente.setIdInfoComplementaire(viewBean.getId());
            demandeRente.update(transaction);
        }

        return viewBean;
    }

    public FWViewBeanInterface supprimerSaisieDemandeRente(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        // Reprise du viewBean
        RESaisieDemandeRenteViewBean saisieDemandeRenteVb = (RESaisieDemandeRenteViewBean) viewBean;

        // Création de la transaction
        BTransaction transaction = null;
        transaction = (BTransaction) (session).newTransaction();

        try {
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            // Reprendre la rente selon le type
            if (saisieDemandeRenteVb.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {

                REDemandeRenteVieillesse renteVieillesse = new REDemandeRenteVieillesse();

                // Reprise de la rente
                renteVieillesse.setSession(session);
                renteVieillesse.setIdDemandeRente(saisieDemandeRenteVb.getIdDemandeRente());
                renteVieillesse.retrieve(transaction);

                renteVieillesse.delete(transaction);

            } else if (saisieDemandeRenteVb.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {

                REDemandeRenteAPI renteAPI = new REDemandeRenteAPI();

                // Reprise de la rente
                renteAPI.setSession(session);
                renteAPI.setIdDemandeRente(saisieDemandeRenteVb.getIdDemandeRente());
                renteAPI.retrieve(transaction);

                renteAPI.delete(transaction);

                // Suppression des périodes API
                for (REPeriodeAPI periodeApiListe : saisieDemandeRenteVb.getPeriodesAPI()) {
                    REPeriodeAPI periodeApiBdD = new REPeriodeAPI();
                    periodeApiBdD.setSession(session);
                    periodeApiBdD.setIdPeriodeAPI(periodeApiListe.getIdPeriodeAPI());
                    periodeApiBdD.retrieve(transaction);

                    periodeApiBdD.delete(transaction);
                }

            } else if (saisieDemandeRenteVb.getCsTypeDemandeRente().equals(
                    IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {

                REDemandeRenteInvalidite renteInvalidite = new REDemandeRenteInvalidite();

                // Reprise de la rente
                renteInvalidite.setSession(session);
                renteInvalidite.setIdDemandeRente(saisieDemandeRenteVb.getIdDemandeRente());
                renteInvalidite.retrieve(transaction);

                renteInvalidite.delete(transaction);

                // Suppression des périodes Invalidité
                for (REPeriodeInvalidite periodeInvListe : saisieDemandeRenteVb.getPeriodesInvalidite()) {
                    REPeriodeInvalidite periodeInvBdD = new REPeriodeInvalidite();
                    periodeInvBdD.setSession(session);
                    periodeInvBdD.setIdPeriodeInvalidite(periodeInvListe.getIdPeriodeInvalidite());
                    periodeInvBdD.retrieve(transaction);

                    periodeInvBdD.delete(transaction);
                }

            } else if (saisieDemandeRenteVb.getCsTypeDemandeRente().equals(
                    IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {

                REDemandeRenteSurvivant renteSurvivant = new REDemandeRenteSurvivant();

                // Reprise de la rente
                renteSurvivant.setSession(session);
                renteSurvivant.setIdDemandeRente(saisieDemandeRenteVb.getIdDemandeRente());
                renteSurvivant.retrieve(transaction);

                renteSurvivant.delete(transaction);

            }

        } catch (Exception e) {
            transaction.setRollbackOnly();
            throw e;
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                transaction.closeTransaction();
            }
        }

        return saisieDemandeRenteVb;

    }

    /**
     * BZ 5493, Demande de vieillesse, décès : pouvoir terminer une demande si l'assuré décède avant le début de son
     * droit
     */
    public FWViewBeanInterface terminerDemandeVieillesse(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        REInfoComplViewBean vb = (REInfoComplViewBean) viewBean;

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, vb.getIdTiers());

        if (JadeStringUtil.isBlankOrZero(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES))) {
            throw new Exception(session.getLabel("ERREUR_ASSURANT_VIVANT"));
        }

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(session);
        demandeRente.setIdDemandeRente(vb.getIdDemandeRente());
        demandeRente.retrieve();

        REBasesCalculManager manager = new REBasesCalculManager();
        manager.setSession(session);
        manager.setForIdRenteCalculee(demandeRente.getIdRenteCalculee());
        manager.find();

        if (manager.size() > 0) {
            for (int i = 0; i < manager.size(); i++) {
                REBasesCalcul uneBase = (REBasesCalcul) manager.get(i);
                uneBase.delete();
            }
        }

        demandeRente.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE);
        demandeRente.update();

        return viewBean;
    }

    /**
     * Permet de lancer une exception avec un message traduit
     * 
     * @param session
     *            La session en cours
     * @param labelKey
     *            La clé du texte à récupérer
     * @throws Exception
     *             L'exception lancée
     */
    private void throwException(BSession session, String labelKey) throws Exception {
        throw new Exception(session.getLabel(labelKey));
    }

    /**
     * Permet de lancer une exception avec un message traduit
     * 
     * @param session
     *            La session en cours
     * @param labelKey
     *            La clé du texte à récupérer
     * @param exception
     *            L'exception mère
     * @throws Exception
     *             L'exception lancée
     */
    private void throwException(BSession session, String labelKey, Exception exception) throws Exception {
        throw new Exception(session.getLabel(labelKey), exception);
    }

    /**
     * Retourne le message associé à la clé textKey
     * 
     * @param session
     * @param textKey
     * @return
     */
    private String translate(BSession session, String textKey) {
        return session.getLabel(textKey);
    }

    private FWViewBeanInterface updateDemandeRente(BTransaction transaction,
            RESaisieDemandeRenteViewBean saisieDemandeRenteVB, BSession session) throws Exception {

        // Savoir quel type de demande c'est et modifier la rente dans la base
        if (saisieDemandeRenteVB.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {

            REDemandeRenteVieillesse renteVieillesse = new REDemandeRenteVieillesse();

            // Reprise de la rente
            renteVieillesse.setSession(session);
            renteVieillesse.setIdDemandeRente(saisieDemandeRenteVB.getIdDemandeRente());
            renteVieillesse.retrieve(transaction);

            // Update des champs

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(saisieDemandeRenteVB.getCsEtat())) {
                if (JadeStringUtil.isEmpty(saisieDemandeRenteVB.getDateFin())) {

                    // Calcul de la periode de début de droit (age de la retraite du tiers)
                    String dateNaissance = saisieDemandeRenteVB.getDateNaissanceRequerant();

                    String dateRetraite = calculDateRetraite(saisieDemandeRenteVB, new JADate(dateNaissance));
                    renteVieillesse.setDateDebut(dateRetraite);
                }
            }

            renteVieillesse.setIdDemandePrestation(saisieDemandeRenteVB.getIdDemandePrestation());
            renteVieillesse.setIdDemandeRenteParent(saisieDemandeRenteVB.getIdDemandeRenteParent());
            renteVieillesse.setDateTraitement(saisieDemandeRenteVB.getDateTraitement());
            renteVieillesse.setDateDepot(saisieDemandeRenteVB.getDateDepot());
            renteVieillesse.setDateReception(saisieDemandeRenteVB.getDateReception());
            renteVieillesse.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteVieillesse.setIdInfoComplementaire(saisieDemandeRenteVB.getIdInfoComplementaire());
            renteVieillesse.setCsTypeCalcul(saisieDemandeRenteVB.getCsTypeCalcul());
            renteVieillesse.setCsTypeDemandeRente(saisieDemandeRenteVB.getCsTypeDemandeRente());
            renteVieillesse.setIdGestionnaire(saisieDemandeRenteVB.getIdGestionnaire());

            renteVieillesse.setCsAnneeAnticipation(saisieDemandeRenteVB.getCsAnneeAnticipationVie());
            renteVieillesse.setIsAjournementRequerant(saisieDemandeRenteVB.getIsAjournementRequerantVie());
            renteVieillesse.setDateRevocationRequerant(saisieDemandeRenteVB.getDateRevocationRequerantVie());
            renteVieillesse.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());

            // Comme modification de la demande, remise dans l'état
            // "enregistré", puis delete cascade de tout ce qui suit
            // pour les demandes qui sont pas dans l'état validé.
            // Pour celles qui sont validées, il faut juste mettre l'état de la
            // demandes, des BC, des RA et des PD à "CALCULE"

            if (saisieDemandeRenteVB.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)) {
                renteVieillesse.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);

                // retrieve des BC
                REBasesCalculManager bcMgr = new REBasesCalculManager();
                bcMgr.setSession(session);
                bcMgr.setForIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
                bcMgr.find(transaction);

                if (!bcMgr.isEmpty()) {
                    for (int i = 0; i < bcMgr.size(); i++) {
                        REBasesCalcul bc = (REBasesCalcul) bcMgr.get(i);

                        bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                        bc.update(transaction);

                        // rechercher les ra pour cette bc
                        RERenteAccordeeManager raMgr = new RERenteAccordeeManager();
                        raMgr.setSession(session);
                        raMgr.setForIdBaseCalcul(bc.getIdBasesCalcul());
                        raMgr.find(transaction);

                        if (!raMgr.isEmpty()) {
                            for (int j = 0; j < raMgr.size(); j++) {
                                RERenteAccordee ra = (RERenteAccordee) raMgr.get(j);

                                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                                ra.wantCallValidate(false);
                                ra.update(transaction);

                                // rechercher les pd pour cette ra
                                REPrestationsDuesManager pdMgr = new REPrestationsDuesManager();
                                pdMgr.setSession(session);
                                pdMgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
                                pdMgr.find(transaction);

                                if (!pdMgr.isEmpty()) {
                                    for (int k = 0; k < pdMgr.size(); k++) {
                                        REPrestationDue pd = (REPrestationDue) pdMgr.get(k);

                                        if (pd.getCsType().equals(IREPrestationDue.CS_TYPE_PMT_MENS)) {
                                            pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                                            pd.update(transaction);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {

                // Si la demande est déjà dans l'état enregistré, tout à déjà
                // été effacé.
                if (!saisieDemandeRenteVB.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)) {
                    // retrieve rentecalculee
                    RERenteCalculee rc = new RERenteCalculee();
                    rc.setIdRenteCalculee(renteVieillesse.getIdRenteCalculee());
                    rc.retrieve(transaction);

                    // Si la rente est de type prévisionnel ou dans l'état au
                    // calcul ou enregistre ou transféré,
                    // il n'y aura rien a effacé, la renteCalcul n'existera pas.
                    if (!rc.isNew()) {

                        renteVieillesse.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                        REDeleteCascadeDemandeAPrestationsDues.supprimerRenteCalculeeCascade_noCommit(session,
                                transaction, rc, IREValidationLevel.VALIDATION_LEVEL_NONE);
                    } else {

                        // renteVieillesse.setCsEtat(saisieDemandeRenteVB.getCsEtat());
                        // Modification suite au point BZ 4329, car la demande
                        // ne changeait pas d'état
                        // lors d'une modification si elle n'avait pas de
                        // renteCalculee
                        renteVieillesse.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                    }
                }
            }

            // Mise à jour
            renteVieillesse.setSession(session);
            renteVieillesse.update(transaction);

            saisieDemandeRenteVB.setIdDemandeRente(renteVieillesse.getIdDemandeRente());
        }

        else if (saisieDemandeRenteVB.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {

            REDemandeRenteAPI renteAPI = new REDemandeRenteAPI();

            // Reprise de la rente
            renteAPI.setSession(session);
            renteAPI.setIdDemandeRente(saisieDemandeRenteVB.getIdDemandeRente());
            renteAPI.retrieve(transaction);

            // Update des champs
            renteAPI.setIdDemandePrestation(saisieDemandeRenteVB.getIdDemandePrestation());
            renteAPI.setIdDemandeRenteParent(saisieDemandeRenteVB.getIdDemandeRenteParent());
            renteAPI.setDateTraitement(saisieDemandeRenteVB.getDateTraitement());
            renteAPI.setDateDepot(saisieDemandeRenteVB.getDateDepot());
            renteAPI.setDateReception(saisieDemandeRenteVB.getDateReception());
            renteAPI.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteAPI.setIdInfoComplementaire(saisieDemandeRenteVB.getIdInfoComplementaire());
            renteAPI.setCsTypeCalcul(saisieDemandeRenteVB.getCsTypeCalcul());
            renteAPI.setCsTypeDemandeRente(saisieDemandeRenteVB.getCsTypeDemandeRente());
            renteAPI.setIdGestionnaire(saisieDemandeRenteVB.getIdGestionnaire());
            renteAPI.setNbPageMotivation(saisieDemandeRenteVB.getNbPagesMotivationApi());

            renteAPI.setCsGenrePrononceAI(saisieDemandeRenteVB.getCsGenrePrononceAiApi());
            renteAPI.setCsInfirmite(saisieDemandeRenteVB.getCsInfirmiteApi());
            renteAPI.setCsAtteinte(saisieDemandeRenteVB.getCsAtteinteApi());
            renteAPI.setCodeOfficeAI(saisieDemandeRenteVB.getCodeOfficeAiApi());
            renteAPI.setDateSuvenanceEvenementAssure(saisieDemandeRenteVB.getDateSurvenanceEvenementAssureAPI());
            renteAPI.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());

            // Comme modification de la demande, remise dans l'état
            // "enregistré", puis delete cascade de tout ce qui suit
            // pour les demandes qui sont pas dans l'état validé.
            // Pour celles qui sont validées, il faut juste mettre l'état de la
            // demandes, des BC, des RA et des PD à "CALCULE"

            if (saisieDemandeRenteVB.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)) {
                renteAPI.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);

                // retrieve des BC
                REBasesCalculManager bcMgr = new REBasesCalculManager();
                bcMgr.setSession(session);
                bcMgr.setForIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
                bcMgr.find(transaction);

                if (!bcMgr.isEmpty()) {
                    for (int i = 0; i < bcMgr.size(); i++) {
                        REBasesCalcul bc = (REBasesCalcul) bcMgr.get(i);

                        bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                        bc.update(transaction);

                        // rechercher les ra pour cette bc
                        RERenteAccordeeManager raMgr = new RERenteAccordeeManager();
                        raMgr.setSession(session);
                        raMgr.setForIdBaseCalcul(bc.getIdBasesCalcul());
                        raMgr.find(transaction);

                        if (!raMgr.isEmpty()) {
                            for (int j = 0; j < raMgr.size(); j++) {
                                RERenteAccordee ra = (RERenteAccordee) raMgr.get(j);

                                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                                ra.wantCallValidate(false);
                                ra.update(transaction);

                                // rechercher les pd pour cette ra
                                REPrestationsDuesManager pdMgr = new REPrestationsDuesManager();
                                pdMgr.setSession(session);
                                pdMgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
                                pdMgr.find(transaction);

                                if (!pdMgr.isEmpty()) {
                                    for (int k = 0; k < pdMgr.size(); k++) {
                                        REPrestationDue pd = (REPrestationDue) pdMgr.get(k);

                                        if (pd.getCsType().equals(IREPrestationDue.CS_TYPE_PMT_MENS)) {
                                            pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                                            pd.update(transaction);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {

                // Si la demande est déjà dans l'état enregistré, tout à déjà
                // été effacé.
                if (!saisieDemandeRenteVB.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)) {
                    // retrieve rentecalculee
                    RERenteCalculee rc = new RERenteCalculee();
                    rc.setIdRenteCalculee(renteAPI.getIdRenteCalculee());
                    rc.retrieve(transaction);

                    // Si la rente est de type prévisionnel ou dans l'état au
                    // calcul ou enregistre ou transféré,
                    // il n'y aura rien a effacé, la renteCalcul n'existera pas.
                    if (!rc.isNew()) {

                        renteAPI.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                        REDeleteCascadeDemandeAPrestationsDues.supprimerRenteCalculeeCascade_noCommit(session,
                                transaction, rc, IREValidationLevel.VALIDATION_LEVEL_NONE);
                    } else {
                        // renteAPI.setCsEtat(saisieDemandeRenteVB.getCsEtat());
                        // Modification suite au point BZ 4329, car la demande
                        // ne changeait pas d'état
                        // lors d'une modification si elle n'avait pas de
                        // renteCalculee
                        renteAPI.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                    }
                }

            }

            // Mise à jour
            renteAPI.setSession(session);
            renteAPI.update(transaction);

            // Ajout des périodes API
            for (REPeriodeAPI periodeApiListe : saisieDemandeRenteVB.getPeriodesAPI()) {

                // Voir si la période existe, si oui, update, sinon ajout
                if (JadeStringUtil.isIntegerEmpty(periodeApiListe.getIdPeriodeAPI())) {

                    periodeApiListe.setSession(session);
                    periodeApiListe.setIdDemandeRente(renteAPI.getIdDemandeRente());
                    periodeApiListe.add(transaction);

                } else {

                    REPeriodeAPI periodeApiBdD = new REPeriodeAPI();
                    periodeApiBdD.setSession(session);
                    periodeApiBdD.setIdPeriodeAPI(periodeApiListe.getIdPeriodeAPI());
                    periodeApiBdD.retrieve(transaction);

                    periodeApiBdD.setDateDebutInvalidite(periodeApiListe.getDateDebutInvalidite());
                    periodeApiBdD.setDateFinInvalidite(periodeApiListe.getDateFinInvalidite());
                    periodeApiBdD.setCsDegreImpotence(periodeApiListe.getCsDegreImpotence());
                    periodeApiBdD.setIsResidenceHome(periodeApiListe.getIsResidenceHome());
                    periodeApiBdD.setIsAssistancePratique(periodeApiListe.getIsAssistancePratique());
                    periodeApiBdD.setCsGenreDroitApi(periodeApiListe.getCsGenreDroitApi());
                    periodeApiBdD.setTypePrestation(periodeApiListe.getTypePrestation());
                    periodeApiBdD.setTypePrestationHistorique(periodeApiListe.getTypePrestationHistorique());

                    periodeApiBdD.setSession(session);
                    periodeApiBdD.update(transaction);

                }
            }

            saisieDemandeRenteVB.setIdDemandeRente(renteAPI.getIdDemandeRente());

        } else if (saisieDemandeRenteVB.getCsTypeDemandeRente()
                .equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {

            REDemandeRenteInvalidite renteInvalidite = new REDemandeRenteInvalidite();

            // Reprise de la rente
            renteInvalidite.setSession(session);
            renteInvalidite.setIdDemandeRente(saisieDemandeRenteVB.getIdDemandeRente());
            renteInvalidite.retrieve(transaction);

            // Update des champs
            renteInvalidite.setIdDemandePrestation(saisieDemandeRenteVB.getIdDemandePrestation());
            renteInvalidite.setIdDemandeRenteParent(saisieDemandeRenteVB.getIdDemandeRenteParent());
            renteInvalidite.setDateTraitement(saisieDemandeRenteVB.getDateTraitement());
            renteInvalidite.setDateDepot(saisieDemandeRenteVB.getDateDepot());
            renteInvalidite.setDateReception(saisieDemandeRenteVB.getDateReception());
            renteInvalidite.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteInvalidite.setIdInfoComplementaire(saisieDemandeRenteVB.getIdInfoComplementaire());
            renteInvalidite.setCsTypeCalcul(saisieDemandeRenteVB.getCsTypeCalcul());
            renteInvalidite.setCsTypeDemandeRente(saisieDemandeRenteVB.getCsTypeDemandeRente());
            renteInvalidite.setIdGestionnaire(saisieDemandeRenteVB.getIdGestionnaire());
            renteInvalidite.setNbPageMotivation(saisieDemandeRenteVB.getNbPagesMotivationInv());

            renteInvalidite.setCsGenrePrononceAI(saisieDemandeRenteVB.getCsGenrePrononceAiInv());
            renteInvalidite.setCsInfirmite(saisieDemandeRenteVB.getCsInfirmiteInv());
            renteInvalidite.setCsAtteinte(saisieDemandeRenteVB.getCsAtteinteInv());
            renteInvalidite.setCodeOfficeAI(saisieDemandeRenteVB.getCodeOfficeAiInv());
            renteInvalidite.setPourcentageReduction(saisieDemandeRenteVB.getPourcentageReductionInv());
            renteInvalidite.setDateSuvenanceEvenementAssure(saisieDemandeRenteVB.getDateSurvenanceEvenementAssureInv());
            renteInvalidite.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteInvalidite.setPourcentRedFauteGrave(saisieDemandeRenteVB.getPourcentRedFauteGrave());
            renteInvalidite.setPourcentRedNonCollaboration(saisieDemandeRenteVB.getPourcentRedNonCollaboration());
            renteInvalidite.setDateDebutRedNonCollaboration(saisieDemandeRenteVB.getDateDebutRedNonCollaboration());
            renteInvalidite.setDateFinRedNonCollaboration(saisieDemandeRenteVB.getDateFinRedNonCollaboration());

            // Comme modification de la demande, remise dans l'état
            // "enregistré", puis delete cascade de tout ce qui suit
            // pour les demandes qui sont pas dans l'état validé.
            // Pour celles qui sont validées, il faut juste mettre l'état de la
            // demandes, des BC, des RA et des PD à "CALCULE"

            if (saisieDemandeRenteVB.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)) {
                renteInvalidite.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);

                // retrieve des BC
                REBasesCalculManager bcMgr = new REBasesCalculManager();
                bcMgr.setSession(session);
                bcMgr.setForIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
                bcMgr.find(transaction);

                if (!bcMgr.isEmpty()) {
                    for (int i = 0; i < bcMgr.size(); i++) {
                        REBasesCalcul bc = (REBasesCalcul) bcMgr.get(i);

                        bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                        bc.update(transaction);

                        // rechercher les ra pour cette bc
                        RERenteAccordeeManager raMgr = new RERenteAccordeeManager();
                        raMgr.setSession(session);
                        raMgr.setForIdBaseCalcul(bc.getIdBasesCalcul());
                        raMgr.find(transaction);

                        if (!raMgr.isEmpty()) {
                            for (int j = 0; j < raMgr.size(); j++) {
                                RERenteAccordee ra = (RERenteAccordee) raMgr.get(j);

                                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                                ra.wantCallValidate(false);
                                ra.update(transaction);

                                // rechercher les pd pour cette ra
                                REPrestationsDuesManager pdMgr = new REPrestationsDuesManager();
                                pdMgr.setSession(session);
                                pdMgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
                                pdMgr.find(transaction);

                                if (!pdMgr.isEmpty()) {
                                    for (int k = 0; k < pdMgr.size(); k++) {
                                        REPrestationDue pd = (REPrestationDue) pdMgr.get(k);

                                        if (pd.getCsType().equals(IREPrestationDue.CS_TYPE_PMT_MENS)) {
                                            pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                                            pd.update(transaction);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {

                // Si la demande est déjà dans l'état enregistré, tout à déjà
                // été effacé.
                if (!saisieDemandeRenteVB.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)) {
                    // retrieve rentecalculee
                    RERenteCalculee rc = new RERenteCalculee();
                    rc.setIdRenteCalculee(renteInvalidite.getIdRenteCalculee());
                    rc.retrieve(transaction);

                    // Si la rente est de type prévisionnel ou dans l'état au
                    // calcul ou enregistre ou transféré,
                    // il n'y aura rien a effacé, la renteCalcul n'existera pas.
                    if (!rc.isNew()) {

                        renteInvalidite.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                        REDeleteCascadeDemandeAPrestationsDues.supprimerRenteCalculeeCascade_noCommit(session,
                                transaction, rc, IREValidationLevel.VALIDATION_LEVEL_NONE);
                    } else {
                        // renteInvalidite.setCsEtat(saisieDemandeRenteVB.getCsEtat());
                        // Modification suite au point BZ 4329, car la demande
                        // ne changeait pas d'état
                        // lors d'une modification si elle n'avait pas de
                        // renteCalculee
                        renteInvalidite.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                    }
                }

            }

            // Mise à jour
            renteInvalidite.setSession(session);
            renteInvalidite.update(transaction);

            saisieDemandeRenteVB.setIdDemandeRente(renteInvalidite.getIdDemandeRente());

            // Ajouter aussi les périodes Invalidité
            for (REPeriodeInvalidite periodeInvListe : saisieDemandeRenteVB.getPeriodesInvalidite()) {

                // Voir si la période existe, si oui, update, sinon ajout
                if (JadeStringUtil.isIntegerEmpty(periodeInvListe.getIdPeriodeInvalidite())) {

                    periodeInvListe.setSession(session);
                    periodeInvListe.setIdDemandeRente(renteInvalidite.getIdDemandeRente());
                    periodeInvListe.add(transaction);

                } else {

                    REPeriodeInvalidite periodeInvBdD = new REPeriodeInvalidite();
                    periodeInvBdD.setSession(session);
                    periodeInvBdD.setIdPeriodeInvalidite(periodeInvListe.getIdPeriodeInvalidite());
                    periodeInvBdD.retrieve(transaction);

                    periodeInvBdD.setDateDebutInvalidite(periodeInvListe.getDateDebutInvalidite());
                    periodeInvBdD.setDateFinInvalidite(periodeInvListe.getDateFinInvalidite());
                    periodeInvBdD.setDegreInvalidite(periodeInvListe.getDegreInvalidite());

                    periodeInvBdD.setSession(session);
                    periodeInvBdD.update(transaction);

                }

            }

        } else if (saisieDemandeRenteVB.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {

            REDemandeRenteSurvivant renteSurvivant = new REDemandeRenteSurvivant();

            // Reprise de la rente
            renteSurvivant.setSession(session);
            renteSurvivant.setIdDemandeRente(saisieDemandeRenteVB.getIdDemandeRente());
            renteSurvivant.retrieve(transaction);

            // update de champs
            renteSurvivant.setIdDemandePrestation(saisieDemandeRenteVB.getIdDemandePrestation());
            renteSurvivant.setIdDemandeRenteParent(saisieDemandeRenteVB.getIdDemandeRenteParent());
            renteSurvivant.setDateTraitement(saisieDemandeRenteVB.getDateTraitement());
            renteSurvivant.setDateDepot(saisieDemandeRenteVB.getDateDepot());
            renteSurvivant.setDateReception(saisieDemandeRenteVB.getDateReception());
            renteSurvivant.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteSurvivant.setIdInfoComplementaire(saisieDemandeRenteVB.getIdInfoComplementaire());
            renteSurvivant.setCsTypeCalcul(saisieDemandeRenteVB.getCsTypeCalcul());
            renteSurvivant.setCsTypeDemandeRente(saisieDemandeRenteVB.getCsTypeDemandeRente());
            renteSurvivant.setIdGestionnaire(saisieDemandeRenteVB.getIdGestionnaire());
            renteSurvivant.setIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
            renteSurvivant.setPourcentageReduction(saisieDemandeRenteVB.getPourcentageReductionSur());

            // Comme modification de la demande, remise dans l'état
            // "enregistré", puis delete cascade de tout ce qui suit
            // pour les demandes qui sont pas dans l'état validé.
            // Pour celles qui sont validées, il faut juste mettre l'état de la
            // demandes, des BC, des RA et des PD à "CALCULE"

            if (saisieDemandeRenteVB.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)) {
                renteSurvivant.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);

                // retrieve des BC
                REBasesCalculManager bcMgr = new REBasesCalculManager();
                bcMgr.setSession(session);
                bcMgr.setForIdRenteCalculee(saisieDemandeRenteVB.getIdRenteCalculee());
                bcMgr.find(transaction);

                if (!bcMgr.isEmpty()) {
                    for (int i = 0; i < bcMgr.size(); i++) {
                        REBasesCalcul bc = (REBasesCalcul) bcMgr.get(i);

                        bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                        bc.update(transaction);

                        // rechercher les ra pour cette bc
                        RERenteAccordeeManager raMgr = new RERenteAccordeeManager();
                        raMgr.setSession(session);
                        raMgr.setForIdBaseCalcul(bc.getIdBasesCalcul());
                        raMgr.find(transaction);

                        if (!raMgr.isEmpty()) {
                            for (int j = 0; j < raMgr.size(); j++) {
                                RERenteAccordee ra = (RERenteAccordee) raMgr.get(j);

                                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                                ra.wantCallValidate(false);
                                ra.update(transaction);

                                // rechercher les pd pour cette ra
                                REPrestationsDuesManager pdMgr = new REPrestationsDuesManager();
                                pdMgr.setSession(session);
                                pdMgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
                                pdMgr.find(transaction);

                                if (!pdMgr.isEmpty()) {
                                    for (int k = 0; k < pdMgr.size(); k++) {
                                        REPrestationDue pd = (REPrestationDue) pdMgr.get(k);

                                        if (pd.getCsType().equals(IREPrestationDue.CS_TYPE_PMT_MENS)) {
                                            pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                                            pd.update(transaction);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {

                // Si la demande est déjà dans l'état enregistré, tout à déjà
                // été effacé.
                if (!saisieDemandeRenteVB.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)) {
                    // retrieve rentecalculee
                    RERenteCalculee rc = new RERenteCalculee();
                    rc.setIdRenteCalculee(renteSurvivant.getIdRenteCalculee());
                    rc.retrieve(transaction);

                    // Si la rente est de type prévisionnel ou dans l'état au
                    // calcul ou enregistre ou transféré,
                    // il n'y aura rien a effacé, la renteCalcul n'existera pas.
                    if (!rc.isNew()) {

                        renteSurvivant.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                        REDeleteCascadeDemandeAPrestationsDues.supprimerRenteCalculeeCascade_noCommit(session,
                                transaction, rc, IREValidationLevel.VALIDATION_LEVEL_NONE);
                    } else {
                        // renteSurvivant.setCsEtat(saisieDemandeRenteVB.getCsEtat());
                        // Modification suite au point BZ 4329, car la demande
                        // ne changeait pas d'état
                        // lors d'une modification si elle n'avait pas de
                        // renteCalculee
                        renteSurvivant.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
                    }
                }

            }

            // si la damande est de type survivant, update de l'information
            // complementaire "Tiers responsable"
            PRInfoCompl ic = new PRInfoCompl();
            ic.setSession(session);
            ic.setIdInfoCompl(renteSurvivant.getIdInfoComplementaire());
            ic.retrieve(transaction);

            ic.setCsTiersResponsable(saisieDemandeRenteVB.getInfoComplCsTiersResponsable());

            if (ic.isNew()) {
                ic.add(transaction);
                renteSurvivant.setIdInfoComplementaire(ic.getIdInfoCompl());
            } else {
                ic.update(transaction);
            }

            // Mise à jour
            renteSurvivant.setSession(session);
            renteSurvivant.update(transaction);

            saisieDemandeRenteVB.setIdDemandeRente(renteSurvivant.getIdDemandeRente());

        }

        return saisieDemandeRenteVB;
    }

    private String calculDateRetraite(RESaisieDemandeRenteViewBean saisieDemandeRenteVB, JADate jdate)
            throws JAException {

        jdate.setDay(01);

        JACalendarGregorian jacal = new JACalendarGregorian();

        String dateRetraite = "";

        /** HOMME */
        if (PRACORConst.CS_HOMME.equals(saisieDemandeRenteVB.getCsSexeRequerant())) {
            /** SANS ANTICIPATION */
            if (JadeStringUtil.isEmpty(saisieDemandeRenteVB.getCsAnneeAnticipationVie())
                    || IREDemandeRente.CS_ANNEE_ANTICIPATION_AGE_LEGAL.equals(saisieDemandeRenteVB
                            .getCsAnneeAnticipationVie())
                    || Boolean.TRUE.equals(saisieDemandeRenteVB.getIsAjournementRequerantVie())) {

                jdate = jacal.addYears(jdate, RESaisieDemandeRenteHelper.AGE_AVS_HOMME);
                jdate = jacal.addMonths(jdate, 1);

                dateRetraite = JACalendar.format(jdate, JACalendar.FORMAT_DDsMMsYYYY);

                /** 1 ANNEE ANTICIPATION */
            } else if (IREDemandeRente.CS_ANNEE_ANTICIPATION_1ANNEE.equals(saisieDemandeRenteVB
                    .getCsAnneeAnticipationVie())
                    && Boolean.FALSE.equals(saisieDemandeRenteVB.getIsAjournementRequerantVie())) {

                jdate = jacal.addYears(jdate, RESaisieDemandeRenteHelper.AGE_AVS_HOMME - 1);
                jdate = jacal.addMonths(jdate, 1);

                dateRetraite = JACalendar.format(jdate, JACalendar.FORMAT_DDsMMsYYYY);

                /** 2 ANNEE ANTICIPATION */
            } else if (IREDemandeRente.CS_ANNEE_ANTICIPATION_2ANNEES.equals(saisieDemandeRenteVB
                    .getCsAnneeAnticipationVie())
                    && Boolean.FALSE.equals(saisieDemandeRenteVB.getIsAjournementRequerantVie())) {

                jdate = jacal.addYears(jdate, RESaisieDemandeRenteHelper.AGE_AVS_HOMME - 2);
                jdate = jacal.addMonths(jdate, 1);

                dateRetraite = JACalendar.format(jdate, JACalendar.FORMAT_DDsMMsYYYY);
            }
            /** FEMME */
        } else {
            /** SANS ANTICIPATION */
            if (JadeStringUtil.isEmpty(saisieDemandeRenteVB.getCsAnneeAnticipationVie())
                    || IREDemandeRente.CS_ANNEE_ANTICIPATION_AGE_LEGAL.equals(saisieDemandeRenteVB
                            .getCsAnneeAnticipationVie())
                    || Boolean.TRUE.equals(saisieDemandeRenteVB.getIsAjournementRequerantVie())) {

                jdate = jacal.addYears(jdate, RESaisieDemandeRenteHelper.AGE_AVS_FEMME);
                jdate = jacal.addMonths(jdate, 1);

                dateRetraite = JACalendar.format(jdate, JACalendar.FORMAT_DDsMMsYYYY);

                /** 1 ANNEE ANTICIPATION */
            } else if (IREDemandeRente.CS_ANNEE_ANTICIPATION_1ANNEE.equals(saisieDemandeRenteVB
                    .getCsAnneeAnticipationVie())
                    && Boolean.FALSE.equals(saisieDemandeRenteVB.getIsAjournementRequerantVie())) {

                jdate = jacal.addYears(jdate, RESaisieDemandeRenteHelper.AGE_AVS_FEMME - 1);
                jdate = jacal.addMonths(jdate, 1);

                dateRetraite = JACalendar.format(jdate, JACalendar.FORMAT_DDsMMsYYYY);

                /** 2 ANNEE ANTICIPATION */
            } else if (IREDemandeRente.CS_ANNEE_ANTICIPATION_2ANNEES.equals(saisieDemandeRenteVB
                    .getCsAnneeAnticipationVie())
                    && Boolean.FALSE.equals(saisieDemandeRenteVB.getIsAjournementRequerantVie())) {

                jdate = jacal.addYears(jdate, RESaisieDemandeRenteHelper.AGE_AVS_FEMME - 2);
                jdate = jacal.addMonths(jdate, 1);

                dateRetraite = JACalendar.format(jdate, JACalendar.FORMAT_DDsMMsYYYY);
            }
        }
        return dateRetraite;
    }

    private void validate(BTransaction transaction, RESaisieDemandeRenteViewBean viewBean, BSession session,
            Boolean isValidateMin) throws Exception {

        // Tests généraux indépendant le type de rente

        // Type de rente obligatoire
        if (JadeStringUtil.isEmpty(viewBean.getCsTypeDemandeRente())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            session.addError(session.getLabel("VALID_SDR_H_TYPE_RENTE_OBLIGATOIRE"));
        }

        // Type de calcul obligatoire
        if (JadeStringUtil.isEmpty(viewBean.getCsTypeCalcul())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            session.addError(session.getLabel("VALID_SDR_H_TYPE_CALCUL_OBLIGATOIRE"));
        }

        // Date de dépôt obligatoire
        if (JAUtil.isDateEmpty(viewBean.getDateDepot())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            session.addError(session.getLabel("VALID_SDR_H_DATE_DEPOT_OBLIGATOIRE"));
        }

        // Date de réception obligatoire
        if (JAUtil.isDateEmpty(viewBean.getDateReception())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            session.addError(session.getLabel("VALID_SDR_H_DATE_RECEPTION_OBLIGATOIRE"));
        }

        // Tests selon le type de rente
        if (!isValidateMin.booleanValue()) {

            // API
            if (viewBean.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {

                // Genre prononcé AI obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getCsGenrePrononceAiApi())) {
                    session.addError(session.getLabel("VALID_SDR_H_GENRE_PRONONCE_OBLIGATOIRE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Office AI obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getCodeOfficeAiApi())) {
                    session.addError(session.getLabel("VALID_SDR_H_OFFICE_AI_INCORRECT"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Code infirmité obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getCodeCsInfirmiteApi())) {
                    session.addError(session.getLabel("VALID_SDR_H_CODE_INFIRMITE_OBLIGATOIRE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Code atteinte obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getCodeCsAtteinteApi())) {
                    session.addError(session.getLabel("VALID_SDR_H_CODE_ATTEINTE_OBLIGATOIRE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Vérifier qu'il y aie au moins une période dans la liste
                if (viewBean.getPeriodesAPI().isEmpty()) {
                    session.addError(session.getLabel("VALID_SDR_H_PERIODE_API_OBLIGATOIRE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Invalidité
            } else if (viewBean.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {

                // Genre prononcé AI obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getCsGenrePrononceAiInv())) {
                    session.addError(session.getLabel("VALID_SDR_H_GENRE_PRONONCE_AI_OBLIGATOIRE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Office AI obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getCodeOfficeAiInv())) {
                    session.addError(session.getLabel("VALID_SDR_H_OFFICE_AI_INCORRECT"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Code infirmité obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getCodeCsInfirmiteInv())) {
                    session.addError(session.getLabel("VALID_SDR_H_CODE_INFIRMITE_OBLIGATOIRE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Code atteinte obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getCodeCsAtteinteInv())) {
                    session.addError(session.getLabel("VALID_SDR_H_CODE_ATTEINTE_OBLIGATOIRE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // format de la date de début pour non collaboration
                if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutRedNonCollaboration())) {
                    if (!PRDateValidator.isDateFormat_MMxAAAA(viewBean.getDateDebutRedNonCollaboration())) {
                        session.addError(session.getLabel("VALID_SDR_F_FORMAT_DATES"));
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                }

                // format de la date de fin pour non collaboration
                if (!JadeStringUtil.isBlankOrZero(viewBean.getDateFinRedNonCollaboration())) {
                    if (!PRDateValidator.isDateFormat_MMxAAAA(viewBean.getDateFinRedNonCollaboration())) {
                        session.addError(session.getLabel("VALID_SDR_F_FORMAT_DATES"));
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                }

                // Si pourcent pour non collaboration rempli, dateDebut et date
                // de fin obligatoires
                if (!JadeStringUtil.isBlankOrZero(viewBean.getPourcentRedNonCollaboration())) {
                    if (JadeStringUtil.isBlankOrZero(viewBean.getDateDebutRedNonCollaboration())
                            || JadeStringUtil.isBlankOrZero(viewBean.getDateFinRedNonCollaboration())) {
                        session.addError(session.getLabel("VALID_SDR_H_DATE_DEBUT_DATE_FIN_OBLIGATOIRE"));
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                }

                // Vérifier qu'il y aie au moins une période dans la liste
                if (viewBean.getPeriodesInvalidite().isEmpty()) {
                    session.addError(session.getLabel("VALID_SDR_H_PERIODE_INVALIDITE_OBLIGATOIRE"));
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

                // Vieillesse
            } else if (viewBean.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {

                // Date révocation uniquement si ajournement = OUI
                if (!viewBean.getIsAjournementRequerantVie().booleanValue()) {
                    if (!JadeStringUtil.isEmpty(viewBean.getDateRevocationRequerantVie())) {
                        session.addError(session.getLabel("VALID_SDR_H_DATE_REVOCATION_SI_AJOURNEMENT"));
                    }
                }

                // Survivant
            } else if (viewBean.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {

                // Date de dece obligatoire
                if (JadeStringUtil.isEmpty(viewBean.getDateDecesRequerant())) {
                    session.addError(session.getLabel("VALID_SDR_H_DATE_DECES_OBLIGATOIRE"));
                }

                // tiers responsable obligatoire pour Demande rente survivant
                if (JadeStringUtil.isIntegerEmpty(viewBean.getInfoComplCsTiersResponsable())) {
                    session.addError(session.getLabel("VALID_SDR_H_TIERS_RESPONSABLE_OBLIGATOIRE"));
                }

                // Sinon, c'est une erreur
            } else {
                session.addError(session.getLabel("VALID_SDR_H_TYPE_DEMANDE_INCORRECT"));
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        }

        // Tests sur les données du tiers (comme pas d'insertions pour le
        // moment,
        // il faut juste voir qu'il y ait un numéro AVS et qu'il soit dans les
        // tiers

        if (!JadeStringUtil.isEmpty(viewBean.getNssRequerant())) {

            // est-ce que ce tiers existe ?

            PRTiersWrapper tiers = PRTiersHelper.getTiers(session, viewBean.getNssRequerant());

            if (tiers == null) {
                session.addError(session.getLabel("VALID_SDR_H_REQUERANT_INTROUVABLE"));
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            } else {

                // Voir si tous les champs sont remplis, sinon renvoyer dans les
                // tiers
                if (JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM))) {
                    session.addError(session.getLabel("VALID_SDR_H_NOM_REQUERANT_OBLIGATOIRE"));
                }

                if (JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE))) {
                    session.addError(session.getLabel("VALID_SDR_H_DATE_NAISSANCE_OBLIGATOIRE"));
                }

                if (JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM))) {
                    session.addError(session.getLabel("VALID_SDR_H_PRENOM_OBLIGATOIRE"));
                }

                if (JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
                    session.addError(session.getLabel("VALID_SDR_H_SEXE_OBLIGATOIRE"));
                }

                if (JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))) {
                    session.addError(session.getLabel("VALID_SDR_H_CANTON_OBLIGATOIRE"));
                }

                if (JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))) {
                    session.addError(session.getLabel("VALID_SDR_H_NATIONALITE_OBLIGATOIRE"));
                }

                viewBean.setIdAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                viewBean.setIdRequerant(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            }

        } else {
            session.addError(session.getLabel("VALID_SDR_H_DONNEES_REQUERANT_INCORRECTES"));
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

    }

    private void validateInfoComp(BTransaction transaction, REInfoComplViewBean viewBean, BSession session)
            throws Exception {

        // Tests de validation pour les informations complémentaires

        // IL FAUT QU'IL Y AIT UN ID DEMANDE RENTE
        if (JadeStringUtil.isBlankOrZero(viewBean.getIdDemandeRente())) {
            viewBean.setMsgType(FWMessage.ERREUR);
            viewBean.setMessage("Impossible d'enregistrer des informations complémentaires sur une demande qui n'existe pas encore");
            transaction
                    .addErrors("Impossible d'enregistrer des informations complémentaires sur une demande qui n'existe pas encore");
        }

        // Si transfert de dossier, champs date édition, caisse/Agence sont
        // obligatoires
        if (IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_TRANSFERT_DOSSIER.equals(viewBean.getTypeInfoCompl())) {

            // BZ 5380, séparation des erreurs pour la date d'édition et le N° de caisse/d'agence
            if (JadeStringUtil.isEmpty(viewBean.getDateInfoCompl())
                    || JadeStringUtil.isBlankOrZero(viewBean.getDateInfoCompl())) {
                viewBean.setMsgType(FWMessage.ERREUR);
                viewBean.setMessage(session.getLabel("VALID_SDR_H_SI_TRANSFERT_DOSSIER_DATE_OBLIGATOIRE"));
                transaction.addErrors(session.getLabel("VALID_SDR_H_SI_TRANSFERT_DOSSIER_DATE_OBLIGATOIRE"));
            }
            if (JadeStringUtil.isIntegerEmpty(viewBean.getNoCaisse())
                    && JadeStringUtil.isIntegerEmpty(viewBean.getNoAgence())) {
                viewBean.setMsgType(FWMessage.ERREUR);
                viewBean.setMessage(session.getLabel("VALID_SDR_H_SI_TRANSFERT_DOSSIER_CAISSE_OBLIGATOIRE"));
                transaction.addErrors(session.getLabel("VALID_SDR_H_SI_TRANSFERT_DOSSIER_CAISSE_OBLIGATOIRE"));
            }

            // une caisse de compensation doit exister avec ce code
            String code = null;
            if (JadeStringUtil.isIntegerEmpty(viewBean.getNoAgence())) {
                code = viewBean.getNoCaisse();
            } else {
                code = viewBean.getNoCaisse() + "." + viewBean.getNoAgence();
            }

            if (PRTiersHelper.getCaisseCompensationForCode(session, code) == null) {
                viewBean.setMsgType(FWMessage.ERREUR);
                viewBean.setMessage(session.getLabel("VALID_SDR_H_CAISSE_COMPENSATION_INEXISTANTE"));
                transaction.addErrors(FWMessageFormat.format(
                        session.getLabel("VALID_SDR_H_CAISSE_COMPENSATION_INEXISTANTE"), viewBean.getNoCaisse() + "."
                                + viewBean.getNoAgence()));
            } else {

                PRTiersWrapper[] caisse = PRTiersHelper.getCaisseCompensationForCode(session, code);
                viewBean.setIdTiersCaisse(caisse[0].getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            }
        }

        // tiers responsable obligatoire pour Demande rente survivant
        if (!JadeStringUtil.isBlankOrZero(viewBean.getIdDemandeRente())) {

            REDemandeRenteViewBean d = new REDemandeRenteViewBean();
            d.setSession(session);
            d.setIdDemandeRente(viewBean.getIdDemandeRente());
            d.retrieve(transaction);

            if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(d.getCsTypeDemandeRente())) {
                if (JadeStringUtil.isIntegerEmpty(viewBean.getCsTiersResponsable())) {
                    session.addError(session.getLabel("VALID_SDR_H_TIERS_RESPONSABLE_OBLIGATOIRE"));
                }
            }
        }
    }

}
