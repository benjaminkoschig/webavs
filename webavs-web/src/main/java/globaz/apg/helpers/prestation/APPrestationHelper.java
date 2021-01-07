package globaz.apg.helpers.prestation;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.ApgServiceLocator;
import globaz.apg.acor.parser.APACORPrestationsParser;
import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.application.APApplication;
import globaz.apg.business.service.APEntityService;
import globaz.apg.business.service.APPlausibilitesApgService;
import globaz.apg.calculateur.APPrestationCalculateurFactory;
import globaz.apg.calculateur.IAPPrestationCalculateur;
import globaz.apg.calculateur.acm.alfa.APCalculateurAcmAlphaDonnesPersistence;
import globaz.apg.calculateur.complement.APCalculateurComplementDonneesPersistence;
import globaz.apg.calculateur.maternite.acm2.ACM2PersistenceInputData;
import globaz.apg.calculateur.pojo.APPrestationCalculeeAPersister;
import globaz.apg.calculateur.pojo.APRepartitionCalculeeAPersister;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.*;
import globaz.apg.enums.APAssuranceTypeAssociation;
import globaz.apg.enums.APTypeCalculPrestation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.helpers.droits.APSituationProfessionnelleHelper;
import globaz.apg.module.calcul.*;
import globaz.apg.module.calcul.constantes.ECanton;
import globaz.apg.module.calcul.constantes.EMontantsMax;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.apg.pojo.APValidationPrestationAPGContainer;
import globaz.apg.pojo.ViolatedRule;
import globaz.apg.properties.APProperties;
import globaz.apg.properties.APPropertyTypeDePrestationAcmValues;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.apg.utils.APCalculAcorUtil;
import globaz.apg.utils.APGUtils;
import globaz.apg.utils.APGenerateurAnnonceRAPG;
import globaz.apg.vb.prestation.APCalculACORViewBean;
import globaz.apg.vb.prestation.APDeterminerTypeCalculPrestationViewBean;
import globaz.apg.vb.prestation.APPrestationViewBean;
import globaz.apg.vb.prestation.APValidationPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.api.IAFAssurance;
import globaz.naos.api.IAFCotisation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceManager;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.prestation.utils.PRDateUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <H1>Description</H1> Créé le 3 juin 05
 *
 * @author vre
 */
@SuppressWarnings("JavadocReference")
public class APPrestationHelper extends PRAbstractHelper {

    public static final String ACTION_CALCULER_PRESTATION_AVEC_ACOR = "calculerPrestationsAvecAcor";
    public static final String ACTION_CALCULER_PRESTATION_AVEC_CALCULATEUR_GLOBAZ = "calculDesPrestationsAvecCalculateurGlobaz";
    public static final String ACTION_CALCULER_PRESTATION_MATCIAB2_AVEC_CALCULATEUR_GLOBAZ = "calculDesPrestationsMATCIAB2AvecCalculateurGlobaz";
    public static final String ACTION_CONTROLE_PRESTATION_CALCULEES = "controllerLesPrestation";
    public static final String ACTION_DETERMINER_TYPE_CALCUL_PRESTATIONS = "determinerLeTypeDeCalculDesPrestations";
    /**
     * Action reçu depuis l'écran de la saisie de la sit. familiale -> maj sit. familiale et redirection vers calcul des
     * prestation
     */
    public static final String CALCULER_TOUTES_LES_PRESTATIONS = "actionCalculerToutesLesPrestations";
    public static final String ERREUR_AUCUNE_PRESTATION_GENEREE = "AUCUNE_PRESTATION_GENEREE";
    public static final String IMPORTER_PRESTATION = "actionImporterPrestationsDepuisACOR";
    public static final String IMPORTER_TOUTES_LES_PRESTATIONS_ACOR = "actionImporterToutesLesPrestationsACOR";
    public static final String RECALCULER_PRESTATIONS = "actionRecalculerPrestations";

    /**
     * Vrais si la situation prof du droit contient au moins un employeur avec la flag ACM a true
     *
     * @param session
     * @return
     * @throws Exception
     */
    public static boolean hasAcmFalgInSitPro(final BISession session, final APDroitLAPG droit) throws Exception {

        if (droit != null && droit.getIdDroit() != null) {
            final APSituationProfessionnelleManager man = new APSituationProfessionnelleManager();
            man.setSession((BSession) session);
            man.setForIdDroit(droit.getIdDroit());
            man.find(BManager.SIZE_NOLIMIT);

            @SuppressWarnings("unchecked") final Iterator<APSituationProfessionnelle> iter = man.iterator();
            while (iter.hasNext()) {
                final APSituationProfessionnelle sitPro = iter.next();
                if (sitPro.getHasAcmAlphaPrestations().booleanValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vrais si la situation prof du droit contient au moins un employeur avec la flag LAMat a true
     *
     * @param session
     * @return
     * @throws Exception
     */
    public static boolean hasLAMatFalgInSitPro(final BISession session, final APDroitLAPG droit) throws Exception {
        final APSituationProfessionnelleManager man = new APSituationProfessionnelleManager();
        man.setSession((BSession) session);
        man.setForIdDroit(droit.getIdDroit());
        man.find();

        @SuppressWarnings("unchecked") final Iterator<APSituationProfessionnelle> iter = man.iterator();
        while (iter.hasNext()) {
            final APSituationProfessionnelle sitPro = iter.next();
            if (sitPro.getHasLaMatPrestations().booleanValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * True si le tiers possède un affiliation a une caisse d'assurance maternite
     *
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static boolean isAffiliationAssuranceMaterniteParIdTiers(final BISession session,
                                                                    final BITransaction transaction, final String idTiers, final APDroitLAPG droit) throws Exception {
        boolean hasOpenedTransaction = false;
        BStatement statement = null;
        APSituationProfessionnelleManager situProfMananger = null;
        try {
            // on cherche l'affiliation du tiers
            final List<String> affiliations = new ArrayList<String>();
            IAFAffiliation affiliation = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
            affiliation.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
            Hashtable<String, String> param = new Hashtable<String, String>();
            param.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
            IAFAffiliation[] afRet = affiliation.findAffiliation(param);

            // le tiers est un affilie
            if (afRet != null) {
                for (int i = 0; i < afRet.length; i++) {
                    affiliations.add(afRet[i].getAffiliationId());
                }
            }

            // on cherche les affiliations une via la situation prof.
            situProfMananger = new APSituationProfessionnelleManager();
            situProfMananger.setSession((BSession) session);
            situProfMananger.setForIdDroit(droit.getIdDroit());
            if (!transaction.isOpened()) {
                transaction.openTransaction();
                hasOpenedTransaction = true;
            }
            statement = situProfMananger.cursorOpen((BTransaction) transaction);
            APSituationProfessionnelle situationProf = null;

            while (null != (situationProf = (APSituationProfessionnelle) situProfMananger.cursorReadNext(statement))) {
                final APEmployeur employeur = new APEmployeur();
                employeur.setSession((BSession) session);
                employeur.setIdEmployeur(situationProf.getIdEmployeur());
                employeur.retrieve();

                affiliation = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
                affiliation.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                param = new Hashtable<String, String>();
                param.put(IAFAffiliation.FIND_FOR_IDTIERS, employeur.getIdTiers());
                afRet = affiliation.findAffiliation(param);
                if (afRet != null) {
                    for (int i = 0; i < afRet.length; i++) {
                        affiliations.add(afRet[i].getAffiliationId());
                    }
                }
            }

            final Iterator<String> iter = affiliations.iterator();

            while (iter.hasNext()) {
                // on cherche toutes les cotisation de l'affiliation
                final IAFCotisation cotisation = (IAFCotisation) session.getAPIFor(IAFCotisation.class);
                cotisation.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                final Hashtable<String, String> paramsAffiliation = new Hashtable<String, String>() {

                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;
                };
                paramsAffiliation.put(IAFCotisation.FIND_FOR_AFFILIATION_ID, iter.next());
                final IAFCotisation[] cotisations = cotisation.findCotisations(paramsAffiliation);

                // pour toutes les cotisation
                for (int i = 0; i < cotisations.length; i++) {

                    // pour chaques cotisation valide au moment du droit
                    if (BSessionUtil.compareDateFirstLowerOrEqual((BSession) session, cotisations[i].getDateDebut(),
                            droit.getDateDebutDroit())
                            && (BSessionUtil.compareDateFirstGreaterOrEqual((BSession) session,
                            cotisations[i].getDateFin(), droit.getDateFinDroit())
                            || JadeStringUtil.isEmpty(cotisations[i].getDateFin()))) {

                        // on cherche l'assurance
                        final IAFAssurance assurance = (IAFAssurance) session.getAPIFor(IAFAssurance.class);
                        assurance
                                .setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                        assurance.setAssuranceId(cotisations[i].getAssuranceId());
                        assurance.retrieve(transaction);

                        // si l'assurance est de type maternite
                        if (IAFAssurance.TYPE_ASS_MATERNITE.equals(assurance.getTypeAssurance())) {
                            return true;
                        }
                    }
                }
            }
        } finally {
            try {
                if (statement != null) {
                    try {
                        situProfMananger.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                if (hasOpenedTransaction && (transaction != null)) {
                    transaction.closeTransaction();
                }
            }
        }
        return false;
    }

    /**
     * Importe les prestations depuis ACOR et effectue le calcul des prestation AMAT et ACM si besoin
     *
     * @param viewBean
     * @param action
     * @param iSession
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionImporterToutesLesPrestationsACOR(final FWViewBeanInterface viewBean,
                                                                      final FWAction action, final BSession session) throws Exception {

        if (!(viewBean instanceof APCalculACORViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type received for the action ["
                    + viewBean.getClass().getName()
                    + ".actionImporterToutesLesPrestationsACOR] The viewBean must be from type APCalculACORViewBean");
        }

        APPrestationViewBean pViewBean = new APPrestationViewBean();
        pViewBean.setISession(session);
        viewBean.setISession(session);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            final APDroitLAPG droit = importerLesPrestations(session, transaction, viewBean);

            // Calcul des prestations standard, LAMat et ACM_ALFA
            final APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
            pViewBean = calculateur.calculPrestationAMAT_ACM(session, transaction, droit, action);

            calculerComplement(session, transaction, droit);


            // Calcul des prestations MATCIAB1 si besoin
            calculerComplementMATCIAB1(session, transaction, droit);
            // Calcul des ACM NE si la propriété TYPE_DE_PRESTATION_ACM vaut ACM_NE
            calculerPrestationsAcmNe(session, transaction, droit);

            // Calcul des prestations ACM 2 si besoin
            calculerPrestationsAcm2(session, transaction, droit);

            // Suppression des prestations standards si l'utilisateur a coché "Exclure l'amat fédéral" dans la situation
            // professionnelle du droit
            calculateur.deletePrestationsStandardsWhenAmatIsExcluded(session, transaction, droit);

            if (!hasErrors(session, transaction)) {
                transaction.commit();
            }
        } catch (final Exception exception) {
            if (transaction != null) {
                transaction.rollback();
                // on s'assure que les prestations standards sont supprimées
                supprimerPrestationsDuDroit(session, pViewBean.getIdDroit());
            }
            pViewBean.setMsgType(FWViewBeanInterface.ERROR);
            pViewBean.setMessage("Unexpected exception throw on commit/rollback of transaction during action ["
                    + action.getActionPart() + "] : " + exception.toString());

        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
        return pViewBean;
    }

    /**
     * Lance le calcul des prestations avec calculateur Globaz
     *
     * @param vb
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public APPrestationViewBean calculDesPrestationsAvecCalculateurGlobaz(final FWViewBeanInterface vb,
                                                                          final FWAction action, final BSession session) throws Exception {

        APPrestationViewBean viewBean = null;
        if (!(vb instanceof APPrestationViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type received for the action ["
                    + vb.getClass().getName() + "]. The viewBean must be from type APPrestationViewBean");
        }
        viewBean = (APPrestationViewBean) vb;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            // TODO: code temporaire, permet de gérer l'ancien calculateur non refactoré. Une fois le refactor effectué,
            // les calculateurs seront lancés par type de prestation
            final IAPPrestationCalculateur calculateur = APPrestationCalculateurFactory
                    .getCalculateurInstance(APTypeDePrestation.STANDARD);

            final APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, transaction,
                    viewBean.getIdDroit());

            final APPrestationStandardLamatAcmAlphaData apPreStaLamAcmAlpDat = new APPrestationStandardLamatAcmAlphaData(
                    droit, getFraisDeGarde(session, droit), getBasesCalcul(session, droit), action, viewBean);

            // TODO: ce calculateur prend en compte les prestations standard, acm alpha et lamat, puis s'occupe de la
            // persistance -> refactor à effectuer
            ((APCalculateurPrestationStandardLamatAcmAlpha) calculateur).calculerPrestation(apPreStaLamAcmAlpDat,
                    session, transaction);

            calculerComplement(session, transaction, droit);


            // Calcul des prestations MATCIAB1 si besoin
            calculerComplementMATCIAB1(session, transaction, droit);
            // Calcul des ACM NE si la propriété TYPE_DE_PRESTATION_ACM vaut ACM_NE
            calculerPrestationsAcmNe(session, transaction, droit);

            // Calcul des prestations ACM 2 si besoin
            calculerPrestationsAcm2(session, transaction, droit);

            // Suppression des prestations standards si l'utilisateur a coché "Exclure l'amat fédéral" dans la situation
            // professionnelle du droit
            ((APCalculateurPrestationStandardLamatAcmAlpha) calculateur)
                    .deletePrestationsStandardsWhenAmatIsExcluded(session, transaction, droit);

        } catch (final Exception exception) {
            JadeLogger.error(this, exception);
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("Unexpected exception throw on commit/rollback of transaction during action ["
                    + action.getActionPart() + "] : " + exception.toString());

        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                        // on s'assure que les prestations standards sont supprimmées
                        supprimerPrestationsDuDroit(session, viewBean.getIdDroit());
                    } else {
                        transaction.commit();
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return (APPrestationViewBean) vb;
    }

    /**
     * Supprime toutes les prestations du droit, et classes liées.
     */
    public static void removePrestationsDroitByType(final BSession session, final BTransaction transaction, final APDroitLAPG droit, APTypeDePrestation typeDePrestation)
            throws Exception {
        final APPrestationManager mgr = new APPrestationManager();
        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgr.getSize(); i++) {
            final APPrestation prestation = (APPrestation) mgr.getEntity(i);
            if (typeDePrestation == null || prestation.getGenre().equals(typeDePrestation.getCodesystemString())) {
                prestation.delete(transaction);
            }
        }
    }

    /**
     * Lance le calcul des prestations MATCIAB2 avec calculateur Globaz
     *
     * @param vb
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public APPrestationViewBean calculDesPrestationsMATCIAB2AvecCalculateurGlobaz(final FWViewBeanInterface vb,
                                                                          final FWAction action, final BSession session) throws Exception {

        APPrestationViewBean viewBean = null;
        if (!(vb instanceof APPrestationViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type received for the action ["
                    + vb.getClass().getName() + "]. The viewBean must be from type APPrestationViewBean");
        }
        viewBean = (APPrestationViewBean) vb;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            final APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, transaction,
                    viewBean.getIdDroit());

            List basesCalcul = getBasesCalcul(session, droit);

            if (!basesCalcul.isEmpty()) {
                final APBaseCalcul bc = (APBaseCalcul) basesCalcul.get(0);
                if (bc != null) {
                    transaction = (BTransaction) session.newTransaction();
                    transaction.openTransaction();

                    // Il faut supprimer toutes les prestations du droit au préalable, avant de pouvoir les recalculer.
                    // Sinon, elles risqueraient d'etre prise en compte pour le calcul des nouvelles prestations et
                    // provoquerait des incohérences.
                    removePrestationsDroitByType(session, transaction, droit, APTypeDePrestation.MATCIAB2);

                }
            }

            // Calcul des prestations MATCIAB2 si besoin
            calculerPrestationsMATCIAB2(session, transaction, droit);

        } catch (final Exception exception) {
            JadeLogger.error(this, exception);
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("Unexpected exception throw on commit/rollback of transaction during action ["
                    + action.getActionPart() + "] : " + exception.toString());

        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                        // on s'assure que les prestations MATCIAB2 sont supprimmées
                        supprimerPrestationsDuDroitParGenre(session, viewBean.getIdDroit(), APTypeDePrestation.MATCIAB2.getCodesystemString());
                    } else {
                        transaction.commit();
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return (APPrestationViewBean) vb;
    }

    /**
     * Calcul des prestations ACM NE si la propriété APProperties.TYPE_DE_PRESTATION_ACM vaut ACM_NE et si nous somme
     * dans les APG</br>
     * Pas de prestastions ACM_NE pour la maternité
     *
     * @param session
     * @param transaction
     * @param viewBean
     * @throws Exception
     * @see{APProperties.PROPERTY_IS_FERCIAB
     * @see{APProperties.TYPE_DE_PRESTATION_ACM
     */
    private void calculerComplement(final BSession session, final BTransaction transaction, final APDroitLAPG droit)
            throws Exception {

        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())
                || APGUtils.isTypeAllocationPandemie(droit.getGenreService())) {
            return;
        }

        String hasComplement = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);

        if (!"true".equals(hasComplement) || APGUtils.isTypeAllocationJourIsole(droit.getGenreService())) {
            return;
        }

        final IAPPrestationCalculateur calculateurComplement = APPrestationCalculateurFactory
                .getCalculateurInstance(APTypeDePrestation.COMPCIAB);

        // Récupération des données depuis la persistence pour le calcul des prestations Complémentaires
        APCalculateurComplementDonneesPersistence donnesPersistencePourCalcul = getDonneesPersistancePourCalculComplementaire(
                droit.getIdDroit(), session, transaction);

        // si versé à l'assuré pas de calcul de complément
        // si l’une des cotisations suivantes existe dans le dans le plan d’affiliation de l’employeur au début de la
        // période APG, alors un complément est calculé
        if (donnesPersistencePourCalcul == null
                || donnesPersistencePourCalcul.getSituationProfessionnelleEmployeur().isEmpty()
                || !isComplement(session, droit.getIdDroit(),
                donnesPersistencePourCalcul.getSituationProfessionnelleEmployeur())) {
            return;
        }

        donnesPersistencePourCalcul.setDroit(droit);

        // Conversion vers des objets métier (domain) pour le calculateur
        final List<Object> entiteesDomainPourCalcul = calculateurComplement
                .persistenceToDomain(donnesPersistencePourCalcul);

        // Calcul des prestations Complémentaires avec le calculateur approprié
        final List<Object> entiteesDomainResultatCalcul = calculateurComplement
                .calculerPrestation(entiteesDomainPourCalcul);

        // Conversion des entités de domain vers des entités de persistance
        final List<APPrestationCalculeeAPersister> resultatCalculAPersister = calculateurComplement
                .domainToPersistence(entiteesDomainResultatCalcul);

        // Sauvegarde des entités de persistance
        persisterResultatCalculPrestation(resultatCalculAPersister, session, transaction, true);
    }

    /**
     * Calcul des prestations MATCIAB1 si la propriété {@link APProperties#PROPERTY_APG_FERCIAB_MATERNITE}
     * possède une date valide et si nous somme dans un droit maternité
     *
     * @param session
     * @param transaction
     * @param droit
     * @throws Exception
     * @see{APProperties.PROPERTY_APG_FERCIAB_MATERNITE
     * @see APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID
     * @see APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID
     * @see APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID
     * @see APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID
     * @see Plage valeur montant max APG MATCIABBEM/MATCIABJUM
     */
    private void calculerComplementMATCIAB1(final BSession session, final BTransaction transaction, final APDroitLAPG droit)
            throws Exception {

        if (!isCalculDisponibleMATCIAB(droit))  {
            return;
        }

        final IAPPrestationCalculateur calculateurMATCIAB1 = APPrestationCalculateurFactory
                .getCalculateurInstance(APTypeDePrestation.MATCIAB1);

        // Récupération des données depuis la persistence pour le calcul des prestations Complémentaires MATCIAB1
        APCalculateurComplementDonneesPersistence donnesPersistencePourCalculMATCIAB1 = getDonneesPersistancePourCalculComplementaireMATCIAB1(
                droit.getIdDroit(), session, transaction);

        // si l’une des cotisations suivantes existe dans le dans le plan d’affiliation de l’employeur au début de la
        // période APG, alors un complément est calculé
        // s'il reste des situations professionelles non filtrées qui cotise aux assurances complémentaires et qui ont isVersementEmployeur à true
        if (donnesPersistencePourCalculMATCIAB1 == null
                || donnesPersistencePourCalculMATCIAB1.getPrestationJointRepartitions() == null
                || donnesPersistencePourCalculMATCIAB1.getPrestationJointRepartitions().isEmpty()
                || donnesPersistencePourCalculMATCIAB1.getListPrestationStandard() == null
                || donnesPersistencePourCalculMATCIAB1.getListPrestationStandard().isEmpty()
                || donnesPersistencePourCalculMATCIAB1.getSituationProfessionnelleEmployeur() == null
                || donnesPersistencePourCalculMATCIAB1.getSituationProfessionnelleEmployeur().isEmpty()) {
            return;
        }

        /*
         * Set les montants max journalier par situations professionelles par canton.
         */
        setMontantMaxSelonCantonSitPro(session, droit.getIdDroit(), donnesPersistencePourCalculMATCIAB1.getSituationProfessionnelleEmployeur(), donnesPersistencePourCalculMATCIAB1.getMapRMD());

        donnesPersistencePourCalculMATCIAB1.setDroit(droit);

        // Conversion vers des objets métier (domain) pour le calculateur
        final List<Object> entiteesDomainPourCalculMATCIAB1 = calculateurMATCIAB1
                .persistenceToDomain(donnesPersistencePourCalculMATCIAB1);

        // Calcul des prestations Complémentaires avec le calculateur approprié
        final List<Object> entiteesDomainResultatCalculMATCIAB1 = calculateurMATCIAB1
                .calculerPrestation(entiteesDomainPourCalculMATCIAB1);

        // Conversion des entités de domain vers des entités de persistance
        final List<APPrestationCalculeeAPersister> resultatCalculAPersisterMATCIAB1 = calculateurMATCIAB1
                .domainToPersistence(entiteesDomainResultatCalculMATCIAB1);

        // Sauvegarde des entités de persistance
        persisterResultatCalculPrestation(resultatCalculAPersisterMATCIAB1, session, transaction, true);
    }

    /**
     * Vérification pour définir si calcul MATCIAB1 ou calcul MATCIAB2
     * peut avoir lieu ou non
     *
     * @param droit
     * @throws Exception
     */
    public static boolean isCalculDisponibleMATCIAB(APDroitLAPG droit) throws Exception {
        if (droit == null) {
            return false;
        }

        if (!(droit instanceof APDroitMaternite)) {
            return false;
        }

        // Contrôle qu'il s'agit d'un droit maternité
        if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
            return false;
        }

        // Contrôle la propriété PROPERTY_APG_FERCIAB_MATERNITE
        String apgFerciabMaternite = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_APG_FERCIAB_MATERNITE);
        if (!JadeDateUtil.isGlobazDate(apgFerciabMaternite)) {
            return false;
        }

        // Contrôle la propriété PROPERTY_IS_FERCIAB
        String isFerciab = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);
        if (!"true".equals(isFerciab)) {
            return false;
        }

        return true;
    }

    /**
     * Check si le nombre de jours définit dans la plage de valeur est valide pour calculer des prestations MATCIAB2
     * et retourne le plus grand nombre de jours si il y a plusieurs employeur dans différent canton.
     *
     * @param session
     * @param idDroit
     * @param listEmployeur
     * @throws Exception
     */
    private Integer getMaxJoursSelonCantonSitPro(BSession session, String idDroit, List<APSitProJointEmployeur> listEmployeur)
            throws Exception {
        Map<IAFAssurance, String> listAssurance;
        Integer maxJours = 0;
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);
        for (APSitProJointEmployeur apSitProJointEmployeur : listEmployeur) {
            listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercherAvecDateDebut(idDroit,
                    apSitProJointEmployeur.getIdAffilie(), session);
            for (Map.Entry<IAFAssurance, String> assurance : listAssurance.entrySet()) {
                if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelBE) || assurance.getKey().getAssuranceId().equals(idAssuranceParitaireBE)) {
                    Integer jour = Integer.valueOf(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", "MATCIABBEJ", "0", "", 0));
                    if (jour > maxJours) {
                        maxJours = jour;
                    }
                } else if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelJU) || assurance.getKey().getAssuranceId().equals(idAssuranceParitaireJU)) {
                    Integer jour = Integer.valueOf(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", "MATCIABJUJ", "0", "", 0));
                    if (jour > maxJours) {
                        maxJours = jour;
                    }
                }
            }
        }
        if (maxJours != 0) {
            return maxJours;
        } else {
            return null;
        }
    }

    /*
     * Set les montants max journalier par situations professionelles par canton.
     *
     * @param session
     * @param idDroit
     * @param listEmployeur
     * @throws Exception
     */
    private void setMontantMaxSelonCantonSitPro(BSession session, String idDroit, List<APSitProJointEmployeur> listEmployeur, Map<String, FWCurrency> mapRMD)
            throws Exception {
        Map<IAFAssurance, String> listAssurance;
        Integer montantMax = 0;
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);
        for (APSitProJointEmployeur apSitProJointEmployeur : listEmployeur) {
            listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercherAvecDateDebut(idDroit,
                    apSitProJointEmployeur.getIdAffilie(), session);
            for (Map.Entry<IAFAssurance, String> assurance : listAssurance.entrySet()) {
                if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelBE) || assurance.getKey().getAssuranceId().equals(idAssuranceParitaireBE)) {
                    Integer montant = Integer.valueOf(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", "MATCIABBEM", "0", "", 0));
                    FWCurrency montant2 = new FWCurrency(JANumberFormatter.format(montant.toString(), 1, 2, JANumberFormatter.SUP));
                    for (Map.Entry<String, FWCurrency> entry : mapRMD.entrySet()) {
                        if (entry.getKey().equals(apSitProJointEmployeur.getIdSitPro())) {
                            if (entry.getValue().compareTo(montant2) > 0) {
                                entry.setValue(montant2);
                            }
                        }
                    }
                } else if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelJU) || assurance.getKey().getAssuranceId().equals(idAssuranceParitaireJU)) {
                    Integer montant = Integer.valueOf(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", "MATCIABJUM", "0", "", 0));
                    FWCurrency montant2 = new FWCurrency(JANumberFormatter.format(montant.toString(), 1, 2, JANumberFormatter.SUP));
                    for (Map.Entry<String, FWCurrency> entry : mapRMD.entrySet()) {
                        if (entry.getKey().equals(apSitProJointEmployeur.getIdSitPro())) {
                            if (entry.getValue().compareTo(montant2) > 0) {
                                entry.setValue(montant2);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isComplement(BSession session, String idDroit, List<APSitProJointEmployeur> listEmployeur)
            throws Exception {
        List<IAFAssurance> listAssurance;
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);
        for (APSitProJointEmployeur employeur : listEmployeur) {
            listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercher(idDroit,
                    employeur.getIdAffilie(), session);
            for (IAFAssurance assurance : listAssurance) {
                if (assurance.getAssuranceId().equals(idAssuranceParitaireJU)
                        || assurance.getAssuranceId().equals(idAssurancePersonnelJU)
                        || assurance.getAssuranceId().equals(idAssuranceParitaireBE)
                        || assurance.getAssuranceId().equals(idAssurancePersonnelBE)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*public static String getGenreAssurance(BSession session, String idDroit, List<APSitProJointEmployeur> listEmployeur)
            throws Exception {
        List<IAFAssurance> listAssurance;
        for (APSitProJointEmployeur employeur : listEmployeur) {
            listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercher(idDroit,
                    employeur.getIdAffilie(), session);
            for (IAFAssurance assurance : listAssurance) {
                if (CodeSystem.GENRE_ASS_PERSONNEL.equals(assurance.getAssuranceGenre())) {
                    return CodeSystem.GENRE_ASS_PERSONNEL;
                } else if (CodeSystem.GENRE_ASS_PARITAIRE.equals(assurance.getAssuranceGenre())) {
                    return CodeSystem.GENRE_ASS_PARITAIRE;
                }
            }
        }
        return "";
    }*/

    /**
     * Calcul des prestations ACM NE si la propriété APProperties.TYPE_DE_PRESTATION_ACM vaut ACM_NE et si nous somme
     * dans les APG</br>
     * Pas de prestastions ACM_NE pour la maternité
     *
     * @param session
     * @param transaction
     * @param viewBean
     * @throws Exception
     * @see{APProperties.TYPE_DE_PRESTATION_ACM
     */
    private void calculerPrestationsAcmNe(final BSession session, final BTransaction transaction,
                                          final APDroitLAPG droit) throws Exception {

        /**
         * calcul des ACM NE si la propriété TYPE_DE_PRESTATION_ACM vaut ACM_NE et si nous somme dans les APG Pas de
         * prestastions ACM_NE pour la maternité
         **/
        if (!JadeStringUtil.isBlankOrZero(droit.getGenreService())) {
            if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())
                    || APGUtils.isTypeAllocationPandemie(droit.getGenreService())) {
                return;
            }
        }
        final String propertyValue = APProperties.TYPE_DE_PRESTATION_ACM.getValue();

        CommonPropertiesUtils.validatePropertyValue(APProperties.TYPE_DE_PRESTATION_ACM, propertyValue,
                APPropertyTypeDePrestationAcmValues.propertyValues());

        final APPropertyTypeDePrestationAcmValues typeDePrestationAcm = APPropertyTypeDePrestationAcmValues
                .valueOf(propertyValue);
        if (typeDePrestationAcm == null) {
            throw new Exception("Unexpected exception : typeDePrestationAcm is null.");
        }

        if (!APPropertyTypeDePrestationAcmValues.ACM_NE.equals(typeDePrestationAcm)) {
            return;
        }

        final IAPPrestationCalculateur calculateurAcmNe = APPrestationCalculateurFactory
                .getCalculateurInstance(APTypeDePrestation.ACM_NE);

        // Récupération des données depuis la persistence pour le calcul des prestations ACM NE
        final APCalculateurAcmAlphaDonnesPersistence donnesPersistencePourCalculAcmNe = getDonneesPersistancePourCalculAcmNE(
                droit.getIdDroit(), session, transaction);

        // Conversion vers des objets métier (domain) pour le calculateur
        final List<Object> entiteesDomainPourCalculAcmNe = calculateurAcmNe
                .persistenceToDomain(donnesPersistencePourCalculAcmNe);

        // Calcul des prestation ACM NE avec le calculateur approprié
        final List<Object> entiteesDomainResultatCalculAcmNe = calculateurAcmNe
                .calculerPrestation(entiteesDomainPourCalculAcmNe);

        // Conversion des entités de domain vers des entités de persistance
        final List<APPrestationCalculeeAPersister> resultatCalculAPersisterAcmNe = calculateurAcmNe
                .domainToPersistence(entiteesDomainResultatCalculAcmNe);

        // Sauvegarde des entités de persistance
        persisterResultatCalculPrestation(resultatCalculAPersisterAcmNe, session, transaction, true);
    }

    /**
     * Calcul des prestations ACM 2 si la propriété {@link APProperties#PRESTATION_ACM_2_ACTIF} ET que l'on soit dans un
     * droit maternité</br>
     * Le calcul sera réalisé pour les situations professionnelle qui le nécéssitent (case à cocher dans l'écran des
     * sit. prof)</br>
     *
     * @param session
     * @param transaction
     * @param droit
     * @throws Exception
     * @see APProperties.PRESTATION_ACM_2_ACTIF
     * @see APProperties#PRESTATION_ACM_2_NOMBRE_JOURS
     */
    private void calculerPrestationsAcm2(final BSession session, final BTransaction transaction,
                                         final APDroitLAPG droitLAPG) throws Exception {

        if (droitLAPG == null) {
            // TODO internationaliser
            throw new Exception("APDroitLAPG is null");
        }

        if (!(droitLAPG instanceof APDroitMaternite)) {
            return;
        }

        APDroitMaternite droit = (APDroitMaternite) droitLAPG;
        /*
         * Est-ce qu'on à a faire à un droit maternité
         */
        if (!JadeStringUtil.isBlankOrZero(droit.getGenreService())) {
            if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
                throw new Exception("GenreService must be CS_ALLOCATION_DE_MATERNITE");
            }
        }

        /*
         * Recherche de la situation familiale maternité pour savoir si c'est une adoption. Si oui, pas de droit aux
         * allocations ACM2
         */
        APSituationFamilialeMatManager situationFamilialeMat = new APSituationFamilialeMatManager();
        situationFamilialeMat.setSession(session);
        situationFamilialeMat.setForIdDroitMaternite(droit.getIdDroit());
        situationFamilialeMat.find(transaction, BManager.SIZE_NOLIMIT);

        boolean isAdoption = true;
        for (int i = 0; i < situationFamilialeMat.getSize(); i++) {
            APSituationFamilialeMat situation = (APSituationFamilialeMat) situationFamilialeMat.get(i);
            if (!situation.getIsAdoption().booleanValue()) {
                isAdoption = false;
            }
        }

        // Pas d'ACM 2 avec que des enfants en cas d'adoption, si un seul n'est pas en cas d'adoption on peut octroyer
        if (isAdoption) {
            return;
        }

        /*
         * Calcul des prestations ACM 2 uniquement si elles ont été activées par la caisse
         */
        if (!APProperties.PRESTATION_ACM_2_ACTIF.getBooleanValue()) {
            return;
        }

        /*
         * Est-ce que le nombre de jours définit dans la propriété est valide pour calculer des prestations ACM 2
         */
        int nombreJoursACM2 = Integer.valueOf(APProperties.PRESTATION_ACM_2_NOMBRE_JOURS.getValue());
        if (nombreJoursACM2 <= 0) {
            throw new Exception("Aucune prestation ACM 2 sera générées car la durée en jours n'est pas correcte ["
                    + APProperties.PRESTATION_ACM_2_NOMBRE_JOURS.getDescription() + "=" + nombreJoursACM2 + "]");
        }

        if (!hasSituationProfAvecACM2(session, droit)) {
            return;
        }

        // OK -> calcul des prestations ACM 2

        final IAPPrestationCalculateur calculateurAcm2 = APPrestationCalculateurFactory
                .getCalculateurInstance(APTypeDePrestation.ACM2_ALFA);

        // Récupération des données depuis la persistence pour le calcul des prestations ACM NE
        final ACM2PersistenceInputData donnesPersistencePourCalculAcm2 = getDonneesPersistancePourCalculAcm2(droit, session, transaction);

        donnesPersistencePourCalculAcm2.setNombreJoursPrestationACM2(nombreJoursACM2);

        // Conversion vers des objets métier (domain) pour le calculateur
        final List<Object> entiteesDomainPourCalculACM2 = calculateurAcm2
                .persistenceToDomain(donnesPersistencePourCalculAcm2);

        // Calcul des prestation ACM NE avec le calculateur approprié
        final List<Object> entiteesDomainResultatCalculACM2 = calculateurAcm2
                .calculerPrestation(entiteesDomainPourCalculACM2);

        // Conversion des entités de domain vers des entités de persistance
        final List<APPrestationCalculeeAPersister> resultatCalculAPersisterACM2 = calculateurAcm2
                .domainToPersistence(entiteesDomainResultatCalculACM2);

        // Sauvegarde des entités de persistance
        persisterResultatCalculPrestation(resultatCalculAPersisterACM2, session, transaction, true);

        // Génération des cotisations ACM 2
        genererLesCotisations(session, resultatCalculAPersisterACM2);
    }

    /**
     * Calcul des prestations MATCIAB2 si la propriété {@link APProperties#PROPERTY_APG_FERCIAB_MATERNITE}
     * possède une date valide et si nous somme dans un droit maternité
     *
     * @param session
     * @param transaction
     * @param droit
     * @throws Exception
     * @see{APProperties.PROPERTY_APG_FERCIAB_MATERNITE
     * @see APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID
     * @see APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID
     * @see APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID
     * @see APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID
     * @see Plage valeur durée max APG MATCIABBEJ/MATCIABJUJ
     * @see Plage valeur montant max APG MATCIABBEM/MATCIABJUM
     *
     */
    private void calculerPrestationsMATCIAB2(final BSession session, final BTransaction transaction,
                                         final APDroitLAPG droitLAPG) throws Exception {

        if (!isCalculDisponibleMATCIAB(droitLAPG))  {
            return;
        }

        APDroitMaternite droit = (APDroitMaternite) droitLAPG;

        final IAPPrestationCalculateur calculateurMATCIAB2 = APPrestationCalculateurFactory
                .getCalculateurInstance(APTypeDePrestation.MATCIAB2);

        // Récupération des données depuis la persistence pour le calcul des prestations MATCIAB2
        final ACM2PersistenceInputData donnesPersistencePourCalculMATCIAB2 = getDonneesPersistancePourCalculMATCIAB2(droit, session, transaction);

        // si l’une des cotisations suivantes existe dans le dans le plan d’affiliation de l’employeur au début de la
        // période APG, alors un complément est calculé
        // s'il reste des situations professionelles non filtrées qui cotise aux assurances complémentaires et qui ont isVersementEmployeur à true
        if (donnesPersistencePourCalculMATCIAB2 == null
                || donnesPersistencePourCalculMATCIAB2.getPrestationJointRepartitions() == null
                || donnesPersistencePourCalculMATCIAB2.getPrestationJointRepartitions().isEmpty()
                || donnesPersistencePourCalculMATCIAB2.getSituationProfessionnelleEmployeur() == null
                || donnesPersistencePourCalculMATCIAB2.getSituationProfessionnelleEmployeur().isEmpty()) {
            return;
        }

        /*
         * Check si le nombre de jours définit dans la plage de valeur est valide pour calculer des prestations MATCIAB2
         * et retourne le plus grand nombre de jours si il y a plusieurs employeur dans différent canton.
         */
        Integer maxJoursMATCIAB2 = getMaxJoursSelonCantonSitPro(session, droit.getIdDroit(), donnesPersistencePourCalculMATCIAB2.getSituationProfessionnelleEmployeur());
        if (maxJoursMATCIAB2 == null || maxJoursMATCIAB2 <= 0) {
            throw new Exception("Aucune prestation MATCIAB2 sera générées car la durée en jours n'est pas correcte [ nombre jours trouvé = " + maxJoursMATCIAB2 + "]");
        } else { // Set le nombre de jours maximum
            donnesPersistencePourCalculMATCIAB2.setNombreJoursPrestationACM2(maxJoursMATCIAB2);
        }

        /*
         * Set les montants max journalier par situations professionelles par canton.
         */
        setMontantMaxSelonCantonSitPro(session, droit.getIdDroit(), donnesPersistencePourCalculMATCIAB2.getSituationProfessionnelleEmployeur(), donnesPersistencePourCalculMATCIAB2.getMapRMD());

        // Conversion vers des objets métier (domain) pour le calculateur
        final List<Object> entiteesDomainPourCalculMATCIAB2 = calculateurMATCIAB2
                .persistenceToDomain(donnesPersistencePourCalculMATCIAB2);

        // Calcul des prestation MATCIAB2 avec le calculateur approprié
        final List<Object> entiteesDomainResultatCalculMATCIAB2 = calculateurMATCIAB2
                .calculerPrestation(entiteesDomainPourCalculMATCIAB2);

        // Conversion des entités de domain vers des entités de persistance
        final List<APPrestationCalculeeAPersister> resultatCalculAPersisterMATCIAB2 = calculateurMATCIAB2
                .domainToPersistence(entiteesDomainResultatCalculMATCIAB2);

        // Sauvegarde des entités de persistance
        persisterResultatCalculPrestation(resultatCalculAPersisterMATCIAB2, session, transaction, false);

        // Génération des cotisations MATCIAB2
        genererLesCotisationsMATCIAB2(session, resultatCalculAPersisterMATCIAB2, droitLAPG);
    }

    private void genererLesCotisationsMATCIAB2(final BSession session,
                                       List<APPrestationCalculeeAPersister> resultatCalculAPersister, APDroitLAPG droit) throws Exception {

        // List des prestations à créer.
        List listDesPrestationsACreer = new ArrayList();

        // Nous allons générer pour chaque répartition de la prestation des cotisations ACM ou MATCIAB
        for (APPrestationCalculeeAPersister prestationCourant : resultatCalculAPersister) {

            // Mapping de la prestation Entity -> Prestation POJO
            APPrestationCalculee prestationCalculee = new APPrestationCalculee();
            prestationCalculee.setDateDebut(new JADate(prestationCourant.getPrestation().getDateDebut()));
            prestationCalculee.setDateFin(new JADate(prestationCourant.getPrestation().getDateFin()));
            prestationCalculee.setNombreJoursSoldes(prestationCourant.getPrestation().getNombreJoursSoldes());
            prestationCalculee.setRevenuDeterminantMoyen(new FWCurrency(prestationCourant.getPrestation().getRevenuMoyenDeterminant()));
            prestationCalculee.setMontantJournalier(new FWCurrency(prestationCourant.getPrestation().getMontantJournalier()));
            prestationCalculee.setFraisGarde(new FWCurrency(prestationCourant.getPrestation().getFraisGarde()));
            prestationCalculee.setCsGenrePrestation(prestationCourant.getPrestation().getGenre());
            prestationCalculee.setEtat(prestationCourant.getPrestation().getEtat());
            prestationCalculee.setIdPrestationApg(prestationCourant.getPrestation().getIdPrestationApg());
            prestationCalculee.setDroitAcquis(new FWCurrency(prestationCourant.getPrestation().getDroitAcquis()));
            prestationCalculee.setIdDroit(prestationCourant.getPrestation().getIdDroit());
            prestationCalculee.setContenuAnnonce(prestationCourant.getPrestation().getContenuAnnonce());
            prestationCalculee.setTypePrestation(prestationCourant.getPrestation().getType());
            APResultatCalcul resultatCalcul = new APResultatCalcul();
            resultatCalcul.setRevision(IAPDroitMaternite.CS_REVISION_MATERNITE_2005);
            prestationCalculee.setResultatCalcul(resultatCalcul);

            for (APRepartitionCalculeeAPersister repartitionCourante : prestationCourant.getRepartitions()) {

                // Récupère la situation professionelle
                APSituationProfessionnelle sitPro = repartitionCourante.getRepartitionPaiements().loadSituationProfessionnelle();

                APResultatCalculSituationProfessionnel apResultatCalculSituationProfessionnel = new APResultatCalculSituationProfessionnel();
                apResultatCalculSituationProfessionnel.setIdAffilie(repartitionCourante.getRepartitionPaiements().getIdAffilie());
                apResultatCalculSituationProfessionnel.setIdTiers(repartitionCourante.getRepartitionPaiements().getIdTiers());
                apResultatCalculSituationProfessionnel.setIndependant(sitPro.getIsIndependant());
                apResultatCalculSituationProfessionnel.setIdSituationProfessionnelle(repartitionCourante.getRepartitionPaiements().getIdSituationProfessionnelle());
                apResultatCalculSituationProfessionnel.setSalaireJournalierNonArrondi(new FWCurrency(String.valueOf(new BigDecimal(repartitionCourante.getRepartitionPaiements().getMontantBrut()).divide(new BigDecimal(prestationCalculee.getNombreJoursSoldes())))));
                apResultatCalculSituationProfessionnel.setSoumisCotisation(!sitPro.getIsNonSoumisCotisation());
                apResultatCalculSituationProfessionnel.setCollaborateurAgricole(sitPro.getIsCollaborateurAgricole());
                apResultatCalculSituationProfessionnel.setTravailleurSansEmployeur(sitPro.getIsTravailleurSansEmploi());
                apResultatCalculSituationProfessionnel.setVersementEmployeur(sitPro.getIsVersementEmployeur());
                apResultatCalculSituationProfessionnel.setMontant(new FWCurrency(repartitionCourante.getRepartitionPaiements().getMontantBrut()));
                apResultatCalculSituationProfessionnel.setTauxProRata(new FWCurrency(String.valueOf(new BigDecimal(repartitionCourante.getRepartitionPaiements().getTauxRJM()).divide(new BigDecimal(100)))));
                apResultatCalculSituationProfessionnel.setNom(repartitionCourante.getRepartitionPaiements().getNom());
                prestationCalculee.getResultatCalcul().addResultatCalculSitProfessionnelle(apResultatCalculSituationProfessionnel);
            }

            listDesPrestationsACreer.add(prestationCalculee);
        }

        final IAPPrestationCalculateur calculateur = APPrestationCalculateurFactory
                .getCalculateurInstance(APTypeDePrestation.STANDARD);
        ((APCalculateurPrestationStandardLamatAcmAlpha) calculateur).creerPrestations(session, session.getCurrentThreadTransaction(), droit, listDesPrestationsACreer);

    }

    private void genererLesCotisations(final BSession session,
                                       List<APPrestationCalculeeAPersister> resultatCalculAPersister) throws Exception {

        // Nous allons générer pour chaque répartition de la prestation des cotisations ACM ou MATCIAB
        for (APPrestationCalculeeAPersister prestationCourant : resultatCalculAPersister) {

            for (APRepartitionCalculeeAPersister repartitionCourante : prestationCourant.getRepartitions()) {

                // Mapping de la prestation Entity -> Prestation POJO
                APPrestationCalculee prestationCalculee = new APPrestationCalculee();
                prestationCalculee.setDateDebut(new JADate(prestationCourant.getPrestation().getDateDebut()));
                prestationCalculee.setDateFin(new JADate(prestationCourant.getPrestation().getDateFin()));
                prestationCalculee.setNombreJoursSoldes(prestationCourant.getPrestation().getNombreJoursSoldes());

                genererCotisationsPourRepartition(session, prestationCalculee,
                        repartitionCourante.getRepartitionPaiements());
            }
        }
    }

    private void genererCotisationsPourRepartition(final BSession session, final APPrestationCalculee prestation,
                                                   final APRepartitionPaiements repartition) throws Exception {

        // Récupère la situation professionelle
        APSituationProfessionnelle sitPro = repartition.loadSituationProfessionnelle();

        // Génération des cotisations pour la répartition de la prestation courante
        APModuleRepartitionPaiements module = new APModuleRepartitionPaiements();
        module.genererCotisationsMATCIAB2(session, prestation, repartition, sitPro.getIsIndependant());

        // Calcul pour avoir le montant NET (montant BRUT + cotisations)
        FWCurrency montantNet = new FWCurrency(JANumberFormatter.format(repartition.getMontantBrut()));

        montantNet.add(getMontantTotalCotisation(session, repartition));

        // Mettre à jour le montant NET de la répartition
        repartition.setMontantNet(montantNet.toString());
        repartition.update(session.getCurrentThreadTransaction());
    }

    private FWCurrency getMontantTotalCotisation(BSession session, APRepartitionPaiements repa) throws Exception {
        FWCurrency result = new FWCurrency(0);
        APCotisationManager mgr = new APCotisationManager();

        mgr.setSession(session);
        mgr.setForIdRepartitionBeneficiairePaiement(repa.getIdRepartitionBeneficiairePaiement());
        mgr.find(BManager.SIZE_NOLIMIT);

        for (Iterator iter = mgr.iterator(); iter.hasNext(); ) {
            APCotisation element = (APCotisation) iter.next();

            result.add(element.getMontant());
        }

        return new FWCurrency(result.toString());
    }

    /**
     * Analyse les situations prof du droit. Retourne <code>true</code> si au moins une des situations prof nécessite un
     * calcul de prestations ACM2
     *
     * @param session
     * @return Retourne <code>true</code> si au moins une des situations prof nécessite un calcul de prestations ACM2
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static boolean hasSituationProfAvecACM2(final BISession session, final APDroitLAPG droit) throws Exception {
        final APSituationProfessionnelleManager man = new APSituationProfessionnelleManager();
        man.setSession((BSession) session);
        man.setForIdDroit(droit.getIdDroit());
        man.find(BManager.SIZE_NOLIMIT);

        final Iterator<APSituationProfessionnelle> iter = man.iterator();
        while (iter.hasNext()) {
            final APSituationProfessionnelle sitPro = iter.next();
            // Case ACM2 cochée dans la sit prof
            if (sitPro.getHasAcm2AlphaPrestations() != null && sitPro.getHasAcm2AlphaPrestations().booleanValue()) {
                // Versement employeur ?
                if (sitPro.getIsVersementEmployeur() != null && sitPro.getIsVersementEmployeur().booleanValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param action
     * @param session
     * @param session
     * @param droit
     * @return
     * @throws Exception
     */

    public APValidationPrestationViewBean controllerLesPrestation(final FWViewBeanInterface vb, final FWAction action,
                                                                  final BSession session) throws Exception {

        APValidationPrestationViewBean viewBean = null;
        if (!(vb instanceof APValidationPrestationViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type received for the action ["
                    + vb.getClass().getName() + "]. The viewBean must be from type APPrestationViewBean");
        }
        viewBean = (APValidationPrestationViewBean) vb;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            final APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, transaction,
                    viewBean.getIdDroit());
            final List<APPrestation> prestations = ApgServiceLocator.getEntityService().getPrestationDuDroit(session,
                    transaction, droit.getIdDroit());
            final PRDemande demande = ApgServiceLocator.getEntityService().getDemandeDuDroit(session, transaction,
                    droit.getIdDroit());

            // On stocke chaque prestation STANDARD ainsi que le droit dans un container de données
            final List<APValidationPrestationAPGContainer> containers = new ArrayList<APValidationPrestationAPGContainer>();
            for (int ctr = 0; ctr < prestations.size(); ctr++) {
                // On évite les prestation ACM et autres
                if (APTypeDePrestation.STANDARD.isCodeSystemEqual(prestations.get(ctr).getGenre())
                        || APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(prestations.get(ctr).getGenre())
                        || APTypeDePrestation.PANDEMIE.isCodeSystemEqual(prestations.get(ctr).getGenre())) {
                    // On évite les prestations de restitutions
                    if (!IAPAnnonce.CS_RESTITUTION.equals(prestations.get(ctr).getContenuAnnonce())) {
                        final APValidationPrestationAPGContainer container = new APValidationPrestationAPGContainer();
                        container.setDroit(droit);
                        container.setPrestation(prestations.get(ctr));
                        containers.add(container);
                    }
                }
            }

            String moisAnneeComptable = JadeDateUtil.getGlobazFormattedDate(new Date());
            moisAnneeComptable = moisAnneeComptable.substring(3);

            // Génération de l'annonce de chaque container (pour chaque prestation).
            final APGenerateurAnnonceRAPG generateurAnnonceRAPG = new APGenerateurAnnonceRAPG();
            for (final APValidationPrestationAPGContainer container : containers) {
                final APAnnonceAPG annonce = generateurAnnonceRAPG.createAnnonceSedex(session,
                        container.getPrestation(), container.getDroit(), moisAnneeComptable);
                container.setAnnonce(annonce);
            }

            // On recherche la date de naissance du tiers pour le contrôle des prestations
            // Si elle est vide, tant pis. Les règles sortiront en erreur et voilà. Au gestionnaire de contrôller
            // ces infos
            final PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, demande.getIdTiers());
            if (tiers == null) {
                String message = session.getLabel("CONTROLER_PRESTATIONS_IMPOSSIBLE_DE_TROUVER_TIERS");
                message = message.replace("{0}", demande.getIdTiers());
                throw new Exception(message);
            }

            // BSessionUtil.initContext(session, this);
            List<String> messageError = checkProperties(session);
            if (!messageError.isEmpty()) {
                viewBean.setMessagePropError(true);
                viewBean.getMessagesError().addAll(messageError);
            }
            // Validation de chaque annonce par le services des plausi
            final APPlausibilitesApgService plausiService = ApgServiceLocator.getPlausibilitesApgService();
            for (final APValidationPrestationAPGContainer container : containers) {
                // Exécution des Rules RAPG
                final List<ViolatedRule> validationErrors = plausiService.checkAnnonce(session, container, tiers);
                container.setValidationErrors(validationErrors);
                for (ViolatedRule vRule : validationErrors) {
                    if (vRule.isPopUp()) {
                        viewBean.setMessagePropError(true);
                        viewBean.getMessagesError().add(vRule.getErrorMessagePopUp());
                    }
                }
            }

            if (APGUtils.isTypeAllocationJourIsole(droit.getGenreService())) {
                viewBean.setErreursValidationsJoursIsoles(
                        plausiService.controllerPrestationsJoursIsoles(session, prestations, droit));
            } else
                // Contrôle qu'une prestation existe pour chaque période. Pas de contrôle pour les droits Mat
                if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
                    final List<APPeriodeAPG> periodesAPG = ApgServiceLocator.getEntityService()
                            .getPeriodesDuDroitAPG(session, transaction, droit.getIdDroit());
                    viewBean.setErreursValidationPeriodes(
                            plausiService.controllerPrestationEnFonctionPeriodes(session, droit, periodesAPG, prestations));
                }

            // BSessionUtil.stopUsingContext(this);

            // Maintenant on va générer l'annonce de chaque prestation et la passer à la validation RAPG
            viewBean.setPrestationValidees(containers);
            viewBean.setIdLangue(session.getIdLangue());
            viewBean.setDateDeDebutDroit(droit.getDateDebutDroit());
            viewBean.setGenreService(droit.getGenreService());
            viewBean.setDetailRequerant(getDetailRequerant(session, demande.getIdTiers()));

            if (!hasErrors(session, transaction)) {
                transaction.commit();
            }

        } catch (final Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            final String message = "Unexpected exception throw on commit/rollback of transaction during action ["
                    + action.getActionPart() + "] : " + exception.toString();
            viewBean.setMessage(message);
            throw new Exception(message);

        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
        return viewBean;
    }

    private List<String> checkProperties(BSession session) throws PropertiesException {
        List<String> listPropertiesEmpty = new ArrayList<>();
        final String prefix = session.getLabel("PROPERTIES_WEBSERVICE_EMPTY") + "<br>";

        if (JadeStringUtil.isBlankOrZero(JadePropertiesService.getInstance().getProperty("apg.rapg.activer.webservice"))) {
            listPropertiesEmpty.add(prefix + "apg.rapg.activer.webservice");
        }
        if (JadeStringUtil.isBlankOrZero(CommonPropertiesUtils.getValueWithoutException(CommonProperties.RAPG_KEYSTORE_PASSWORD))) {
            listPropertiesEmpty.add(prefix + CommonProperties.RAPG_KEYSTORE_PASSWORD.getPropertyName());
        }
        if (JadeStringUtil.isBlankOrZero(CommonPropertiesUtils.getValueWithoutException(CommonProperties.RAPG_KEYSTORE_PATH))) {
            listPropertiesEmpty.add(prefix + CommonProperties.RAPG_KEYSTORE_PATH.getPropertyName());
        }
        if (JadeStringUtil.isBlankOrZero(CommonPropertiesUtils.getValueWithoutException(CommonProperties.RAPG_KEYSTORE_TYPE))) {
            listPropertiesEmpty.add(prefix + CommonProperties.RAPG_KEYSTORE_TYPE.getPropertyName());
        }
        if (JadeStringUtil.isBlankOrZero(CommonPropertiesUtils.getValueWithoutException(CommonProperties.RAPG_WEBSERVICE_WSDL_PATH))) {
            listPropertiesEmpty.add(prefix + CommonProperties.RAPG_WEBSERVICE_WSDL_PATH.getPropertyName());
        }
        if (JadeStringUtil.isBlankOrZero(CommonPropertiesUtils.getValueWithoutException(CommonProperties.RAPG_SSL_CONTEXT_TYPE))) {
            listPropertiesEmpty.add(prefix + CommonProperties.RAPG_SSL_CONTEXT_TYPE.getPropertyName());
        }
        if (JadeStringUtil.isBlankOrZero(CommonPropertiesUtils.getValueWithoutException(CommonProperties.RAPG_WEBSERVICE_NAME))) {
            listPropertiesEmpty.add(prefix + CommonProperties.RAPG_WEBSERVICE_NAME.getPropertyName());
        }
        if (JadeStringUtil.isBlankOrZero(CommonPropertiesUtils.getValueWithoutException(CommonProperties.RAPG_WEBSERVICE_NAMESPACE))) {
            listPropertiesEmpty.add(prefix + CommonProperties.RAPG_WEBSERVICE_NAMESPACE.getPropertyName());
        }
        if (JadeStringUtil.isBlankOrZero(CommonPropertiesUtils.getValueWithoutException(CommonProperties.RAPG_WEBSERVICE_SEDEX_SENDER_ID))) {
            listPropertiesEmpty.add(prefix + CommonProperties.RAPG_WEBSERVICE_SEDEX_SENDER_ID.getPropertyName());
        }

        return listPropertiesEmpty;
    }

    // public APPrestationViewBean calculDesPrestationsAvecCalculateurGlobazOld(final FWViewBeanInterface vb,
    // final FWAction action, final BSession session) throws Exception {
    //
    // APPrestationViewBean viewBean = null;
    // if (!(vb instanceof APPrestationViewBean)) {
    // throw new APWrongViewBeanTypeException("Wrong viewBean type received for the action ["
    // + vb.getClass().getName()
    // + "]. The viewBean must be from type APDeterminerTypeCalculPrestationViewBean");
    // }
    // viewBean = (APPrestationViewBean) vb;
    //
    // BTransaction transaction = null;
    // try {
    // transaction = (BTransaction) session.newTransaction();
    // if (!transaction.isOpened()) {
    // transaction.openTransaction();
    // }
    //
    // final APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, transaction,
    // viewBean.getIdDroit());
    // if (droit.validateBeforeCalcul(transaction)) {
    //
    // final APCalculateurPrestationStandardLamatAcmAlpha calculateur = new
    // APCalculateurPrestationStandardLamatAcmAlpha();
    //
    // calculateur.genererPrestations(session, droit, this.getFraisDeGarde(session, droit),
    // this.getBasesCalcul(session, droit));
    //
    // viewBean = calculateur.calculPrestationAMAT_ACM(session, transaction, droit, action);
    // } else { // Les données ne sont pas valides
    // viewBean.setMsgType(FWViewBeanInterface.ERROR);
    // viewBean.setMessage(transaction.getErrors().toString());
    // return viewBean;
    // }
    // if (!this.hasErrors(session, transaction)) {
    // transaction.commit();
    // }
    // } catch (final Exception exception) {
    // if (transaction != null) {
    // transaction.rollback();
    // }
    // viewBean.setMsgType(FWViewBeanInterface.ERROR);
    // viewBean.setMessage("Unexpected exception throw on commit/rollback of transaction during action ["
    // + action.getActionPart() + "] : " + exception.toString());
    //
    // } finally {
    // if (transaction != null) {
    // transaction.closeTransaction();
    // }
    // }
    // return viewBean;
    // }

    /**
     * Si le Droit est un Droit APG, découpe les périodes si un enfant né en cours de période.
     *
     * @param session
     * @param droit
     * @throws Exception
     * @throws JadeApplicationServiceNotAvailableException
     * @throws APEntityNotFoundException
     */
    private void decoupageDesPeriodesAPGSiBesoin(final BISession session, final BTransaction transaction,
                                                 final APDroitLAPG droit)
            throws Exception, JadeApplicationServiceNotAvailableException, APEntityNotFoundException {
        if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
            final List<APPeriodeAPG> periodesApg = ApgServiceLocator.getEntityService()
                    .getPeriodesDuDroitAPG((BSession) session, transaction, droit.getIdDroit());
            final List<APEnfantAPG> enfantsApg = ApgServiceLocator.getEntityService()
                    .getEnfantsAPGDuDroitAPG((BSession) session, transaction, droit.getIdDroit());
            if (ApgServiceLocator.getDroitAPGService().isDecoupageDesPeriodesAPGNecessaire(periodesApg, enfantsApg)) {
                // Si un découpage d'une période est nécessaire, on les supprime toutes les périodes existantes
                // et on les recrées
                final List<PRPeriode> nouvellePeriodes = ApgServiceLocator.getDroitAPGService()
                        .controlerPrestation(periodesApg, enfantsApg);
                ApgServiceLocator.getEntityService().remplacerPeriodesDroitAPG((BSession) session, transaction,
                        droit.getIdDroit(), nouvellePeriodes);
            }
        }
    }

    /**
     * Determine si les prestations du droit doivent être calculées avec ACOR ou par le calculateur Globaz
     *
     * @param vb
     * @param action
     * @param session
     * @throws Exception
     */
    public APDeterminerTypeCalculPrestationViewBean determinerLeTypeDeCalculDesPrestations(final FWViewBeanInterface vb,
                                                                                           final FWAction action, final BSession session) throws Exception {

        if (!(vb instanceof APDeterminerTypeCalculPrestationViewBean)) {
            throw new APWrongViewBeanTypeException(
                    "Wrong viewBean type received for the action [" + vb.getClass().getName()
                            + "]. The viewBean must be from type APDeterminerTypeCalculPrestationViewBean");
        }
        final APDeterminerTypeCalculPrestationViewBean viewBean = (APDeterminerTypeCalculPrestationViewBean) vb;
        viewBean.setTypeCalculPrestation(null);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            final APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, transaction,
                    viewBean.getIdDroit());

            String apgOuMaternite = null;
            if (droit instanceof APDroitMaternite) {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_MATERNITE;
            } else if (droit instanceof APDroitPandemie) {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_PANDEMIE;
            } else {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_APG;
            }
            if (!Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(droit.getEtat())) {
                if (!isMATCIAB2(viewBean)) {
                    throw new APCalculException(session.getLabel("MODULE_CALCUL_ETAT_DROIT_INVALIDE"));
                }
            }

            if (APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_APG.equals(apgOuMaternite)) {
                decoupageDesPeriodesAPGSiBesoin(session, transaction, droit);
            }

            viewBean.setTypeCalculPrestation(APTypeCalculPrestation.STANDARD);
            // -------------------------------------------------------------------
            // -- Condition stipulant que le calcul doit s'effectuer avec ACOR
            // -------------------------------------------------------------------
            if (APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_MATERNITE.equals(apgOuMaternite)) {
                if (APCalculAcorUtil.grantCalulAcorMaternite(session, (APDroitMaternite) droit)) {
                    final JACalendarGregorian calendar = new JACalendarGregorian();
                    // si une partie du droit se situe entre le 01.07.2001 et le 01.07.2005, le calcul doit se faire
                    // à la main.
                    // On crée des prestations avec des repartitions a zero.
                    if (!((calendar.compare(droit.getDateDebutDroit(), "01.07.2005") == JACalendar.COMPARE_FIRSTLOWER)
                            && (calendar.compare(droit.getDateFinDroit(),
                            "01.07.2001") == JACalendar.COMPARE_FIRSTUPPER)
                            && "true".equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG)
                            .getProperty("isDroitMaterniteCantonale")))) {
                        viewBean.setTypeCalculPrestation(APTypeCalculPrestation.ACOR);
                    }
                }
            } else if (!APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_PANDEMIE.equals(apgOuMaternite)) {
                if (APCalculAcorUtil.grantCalulAcorAPG(session, (APDroitAPG) droit)) {
                    viewBean.setTypeCalculPrestation(APTypeCalculPrestation.ACOR);
                }
            }

            // Cas spécial pour éviter le calcul ACOR quand on calcul des prestations de type MATCIAB2
            if (isMATCIAB2(viewBean)) {
                viewBean.setTypeCalculPrestation(APTypeCalculPrestation.STANDARD);
            }

            if (!hasErrors(session, transaction)) {
                transaction.commit();
            }
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(
                    "Exception thrown during execution of action APPrestationHelper.determinerLeTypeDeCalculDesPrestations");
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
        return viewBean;
    }

    private boolean isMATCIAB2(APDeterminerTypeCalculPrestationViewBean viewBean) {
        return viewBean.getTypePrestation() != null
                && viewBean.getTypePrestation().equals(APTypeDePrestation.MATCIAB2.getNomTypePrestation());
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(final FWViewBeanInterface viewBean, final FWAction action,
                                          final BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * @param session
     * @param droit
     * @return
     * @throws Exception
     */
    private List getBasesCalcul(final BSession session, final APDroitLAPG droit) throws Exception {
        return new APBasesCalculBuilder(session, droit).createBasesCalcul();
    }

    private String getDetailRequerant(final BSession session, final String idTiers) throws Exception {
        final PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, idTiers);
        if (tiers != null) {
            String nationalite = "";
            if (!"999".equals(session.getCode(
                    session.getSystemCode("CIPAYORI", tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = session.getCodeLibelle(
                        session.getSystemCode("CIPAYORI", tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }
            final StringBuilder nomPrenom = new StringBuilder();
            nomPrenom.append(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM));
            nomPrenom.append(" ");
            nomPrenom.append(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            return PRNSSUtil.formatDetailRequerantDetail(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    nomPrenom.toString(), tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    session.getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    private APCalculateurComplementDonneesPersistence getDonneesPersistancePourCalculComplementaire(
            final String idDroit, final BISession iSession, final BITransaction iTransaction) throws Exception {
        final APPrestationManager mgr = new APPrestationManager();
        final BSession session = (BSession) iSession;
        final BTransaction transaction = (BTransaction) iTransaction;
        mgr.setSession(session);
        mgr.setForIdDroit(idDroit);
        mgr.find(BManager.SIZE_NOLIMIT);

        List<APPrestation> list = new ArrayList<>();

        for (int i = 0; i < mgr.getSize(); i++) {
            final APPrestation prestation = (APPrestation) mgr.getEntity(i);
            list.add(prestation);
        }

        final APCalculateurComplementDonneesPersistence donneesPersistence = new APCalculateurComplementDonneesPersistence(
                idDroit);
        donneesPersistence.setListPrestationStandard(list);

        final APEntityService servicePersistance = ApgServiceLocator.getEntityService();
        donneesPersistence.setIdDroit(idDroit);

        // Filtre pour ne garder que les prestations d'allocations
        final List<APRepartitionJointPrestation> apRepJointPrestationsIsAllocation = filtrerApRepJointPrestationsIsAllocation(idDroit, session, transaction, servicePersistance);
        if (apRepJointPrestationsIsAllocation.isEmpty()) return null;

        donneesPersistence.setPrestationJointRepartitions(apRepJointPrestationsIsAllocation);

        APCotisationManager cotisations = new APCotisationManager();

        List<APCotisation> listCotisation = new ArrayList<>();

        for (APRepartitionJointPrestation repartition : apRepJointPrestationsIsAllocation) {
            cotisations.setSession(session);
            cotisations.setForIdRepartitionBeneficiairePaiement(repartition.getId());
            cotisations.find(transaction);
            for (int i = 0; i < cotisations.size(); i++) {
                APCotisation cotisation = (APCotisation) cotisations.get(i);
                listCotisation.add(cotisation);
            }
        }
        donneesPersistence.setListCotisation(listCotisation);

        // Situations professionnelles
        final List<APSitProJointEmployeur> apSitProJointEmployeurs = servicePersistance
                .getSituationProfJointEmployeur(session, transaction, idDroit);

        final String dateDebutPrestationStandard = donneesPersistence.getPrestationJointRepartitions().get(0)
                .getDateDebut();

        // Filtre les situations proffesionelles qui ne cotise pas au complément
        List<APSitProJointEmployeur> apSitProJointEmployeursIsComplement = filterAPSitProJointEmployeursIsComplement(idDroit, session, donneesPersistence, dateDebutPrestationStandard, apSitProJointEmployeurs);

        donneesPersistence.setSituationProfessionnelleEmployeur(apSitProJointEmployeursIsComplement);

        Map<EMontantsMax, BigDecimal> montantsMax = new HashMap<>();
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.COMCIABJUR);
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.COMCIABBER);
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.COMCIABJUA);
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.COMCIABBEA);
        donneesPersistence.setMontantsMax(montantsMax);
        return donneesPersistence;
    }


    private APCalculateurComplementDonneesPersistence getDonneesPersistancePourCalculComplementaireMATCIAB1(
            final String idDroit, final BISession iSession, final BITransaction iTransaction) throws Exception {
        final APPrestationManager mgr = new APPrestationManager();
        final BSession session = (BSession) iSession;
        final BTransaction transaction = (BTransaction) iTransaction;
        mgr.setSession(session);
        mgr.setForIdDroit(idDroit);
        mgr.find(BManager.SIZE_NOLIMIT);

        // Filtre les prestations qui ont une date de début avant la propriété apg.FERCIAB.maternite
        List<APPrestation> apPrestationsIsDateDebutAvantProprieteFerciab = filterAPRepJointPrestationsIsDateDebutAvantProprieteFerciabMATCIAB1(mgr);

        final APCalculateurComplementDonneesPersistence donneesPersistence = new APCalculateurComplementDonneesPersistence(
                idDroit);

        final APEntityService servicePersistance = ApgServiceLocator.getEntityService();
        donneesPersistence.setIdDroit(idDroit);

        // Filtre pour ne garder que les prestations d'allocations
        final List<APRepartitionJointPrestation> apRepJointPrestationsIsAllocation = filtrerApRepJointPrestationsIsAllocation(idDroit, session, transaction, servicePersistance);
        if (apRepJointPrestationsIsAllocation.isEmpty()) return null;

        // Filtre les repartitions qui ne cotise pas au complément
        List<APRepartitionJointPrestation> apRepJointPrestationsIsComplement = filterAPRepJointPrestationsIsComplementMATCIAB1(idDroit, session, apRepJointPrestationsIsAllocation);

        donneesPersistence.setPrestationJointRepartitions(apRepJointPrestationsIsComplement);

        APCotisationManager cotisations = new APCotisationManager();

        List<APCotisation> listCotisation = new ArrayList<>();

        for (APRepartitionJointPrestation apRepartitionJointPrestation : apRepJointPrestationsIsComplement) {
            cotisations.setSession(session);
            cotisations.setForIdRepartitionBeneficiairePaiement(apRepartitionJointPrestation.getId());
            cotisations.find(transaction);
            for (int i = 0; i < cotisations.size(); i++) {
                APCotisation cotisation = (APCotisation) cotisations.get(i);
                listCotisation.add(cotisation);
            }
        }
        donneesPersistence.setListCotisation(listCotisation);

        // Situations professionnelles
        final List<APSitProJointEmployeur> apSitProJointEmployeurs = servicePersistance
                .getSituationProfJointEmployeur(session, transaction, idDroit);

        donneesPersistence.setNombreInitialDeSituationsProfessionelles(apSitProJointEmployeurs.size());

        final String dateDebutPrestationStandard = donneesPersistence.getPrestationJointRepartitions().size() > 0 ? donneesPersistence.getPrestationJointRepartitions().get(0).getDateDebut() : null;
        if (dateDebutPrestationStandard == null) return null;

        // Filtre les situations proffesionelles qui ne cotise pas au complément
        List<APSitProJointEmployeur> apSitProJointEmployeursIsComplement = filterAPSitProJointEmployeursIsComplementMATCIAB1(idDroit, session, donneesPersistence, dateDebutPrestationStandard, apSitProJointEmployeurs);
        if (apSitProJointEmployeursIsComplement.isEmpty()) return null;

        // Filtre les prestations qui débute à une date ou il n'y avait pas encore de cotisations aux assurances complémentaire
        List<APPrestation> apPrestationsIsDebutCotisationComplementaireAvantDebutPrestations = filterAPPrestationsIsCotisationComplementaireAvantDebutPrestations(apPrestationsIsDateDebutAvantProprieteFerciab, apSitProJointEmployeursIsComplement);
        if (apPrestationsIsDebutCotisationComplementaireAvantDebutPrestations.isEmpty()) return null;

        // Filtre les situations proffesionelles qui ne sont pas des versement employeurs
        List<APSitProJointEmployeur> apSitProJointEmployeursIsVersementEmployeur = filterAPSitProJointEmployeursIsVersementEmployeur(apSitProJointEmployeursIsComplement);
        if (apSitProJointEmployeursIsVersementEmployeur.isEmpty()) return null;

        // Filtre les prestations en fonction de la date de fin de contrat des situations proffessionelles
        setDateFinContrat(session, apSitProJointEmployeursIsVersementEmployeur);
        List<APPrestation> apPrestationsIsDateFinContrat = filterAPPrestationsIsDateFinContratMATCIAB1(apSitProJointEmployeursIsVersementEmployeur, apPrestationsIsDebutCotisationComplementaireAvantDebutPrestations);

        donneesPersistence.setListPrestationStandard(apPrestationsIsDateFinContrat);
        donneesPersistence.setSituationProfessionnelleEmployeur(apSitProJointEmployeursIsVersementEmployeur);

        // Add RMD par sitPro
        for (final APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeursIsVersementEmployeur) {
            APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
            sitPro.setSession(session);
            sitPro.setIdSituationProf(apSitProJointEmployeur.getIdSitPro());
            sitPro.retrieve();
            if (sitPro.isNew()) {
                throw new Exception(
                        "Impossible de retrouver la sit pro avec l'id [" + sitPro.getIdSituationProf() + "]");
            }
            // recherche le salaire horaire/mensuel/indépendant/versé
            FWCurrency revenuMoyenDeterminant = APSituationProfessionnelleHelper.getSalaireJournalierVerse(sitPro);
            revenuMoyenDeterminant = new FWCurrency(
                    JANumberFormatter.format(revenuMoyenDeterminant.toString(), 0.05, 2, JANumberFormatter.SUP));
            donneesPersistence.addRMDParEmployeur(sitPro.getIdSituationProf(), revenuMoyenDeterminant);
        }

        Map<EMontantsMax, BigDecimal> montantsMax = new HashMap<>();
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.MATCIABJUM);
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.MATCIABBEM);
        donneesPersistence.setMontantsMax(montantsMax);
        return donneesPersistence;
    }

    private void setDateFinContrat(BSession session, List<APSitProJointEmployeur> apSitProJointEmployeursIsVersementEmployeur) throws Exception {
        for (final APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeursIsVersementEmployeur) {
            APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
            sitPro.setSession(session);
            sitPro.setIdSituationProf(apSitProJointEmployeur.getIdSitPro());
            sitPro.retrieve();
            apSitProJointEmployeur.setDateFin(sitPro.getDateFinContrat());
        }
    }

    private List<APPrestation> filterAPPrestationsIsDateFinContratMATCIAB1(List<APSitProJointEmployeur> apSitProJointEmployeursIsVersementEmployeur, List<APPrestation> apPrestationsIsDebutCotisationComplementaireAvantDebutPrestations) {
        List<APPrestation> apPrestationsIsDateFinContrat = new ArrayList<>();

        APSitProJointEmployeur apSitProJointEmployeurMaxDateFinContrat = apSitProJointEmployeursIsVersementEmployeur.get(0);
        // La situation professionele qui possède une date de fin de contrat la plus éloignée
        for (APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeursIsVersementEmployeur) {
            PRDateUtils.PRDateEquality prestationBeginDateCheck = PRDateUtils.compare(apSitProJointEmployeurMaxDateFinContrat.getDateFin(), apSitProJointEmployeur.getDateFin());
            if (prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.AFTER)) {
                apSitProJointEmployeurMaxDateFinContrat = apSitProJointEmployeur;
            } else if (prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.INCOMPARABLE)) { // dateFin = null est la plus grande date de fin que l'on peut trouver
                apSitProJointEmployeurMaxDateFinContrat = apSitProJointEmployeur;
                break;
            }
        }

        for (APPrestation apPrestation : apPrestationsIsDebutCotisationComplementaireAvantDebutPrestations) {
            // Ne prends en compte que les prestations qui commence avant la date trouvé dans la situations professionelle qui possède une date de fin de contrat la plus éloignée
            PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(apSitProJointEmployeurMaxDateFinContrat.getDateFin(), apPrestation.getDateFin());
            PRDateUtils.PRDateEquality prestationBeginDateCheck = PRDateUtils.compare(apSitProJointEmployeurMaxDateFinContrat.getDateFin(), apPrestation.getDateDebut());
            if (prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.INCOMPARABLE) || prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.EQUALS) || prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.BEFORE)) {
                // Adapte la date de fin de la prestation en fonction de la situation profesionelle qui possède une date de fin de contrat la plus éloignée
                if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.AFTER)) {
                    apPrestation.setDateFin(apSitProJointEmployeurMaxDateFinContrat.getDateFin());
                }
                apPrestationsIsDateFinContrat.add(apPrestation);
            }
        }
        return apPrestationsIsDateFinContrat;
    }

    private List<APRepartitionJointPrestation> filterAPPrestationsIsDateFinContratMATCIAB2(List<APSitProJointEmployeur> apSitProJointEmployeursIsVersementEmployeur, List<APRepartitionJointPrestation> apRepJointPrestationsIsDateDebutAvantProprieteFerciab) {
        List<APRepartitionJointPrestation> apRepJointPrestationIsDateFinContrat = new ArrayList<>();

        for (APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeursIsVersementEmployeur) {

            for (APRepartitionJointPrestation apRepJointPrestation : apRepJointPrestationsIsDateDebutAvantProprieteFerciab) {
                if (apRepJointPrestation.getIdSituationProfessionnelle().equals(apSitProJointEmployeur.getIdSitPro())) {
                    // Ne prends en compte que les prestations qui commence avant la date de fin trouvé dans la situations professionelle
                    PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(apSitProJointEmployeur.getDateFin(), apRepJointPrestation.getDateFin());
                    PRDateUtils.PRDateEquality prestationBeginDateCheck = PRDateUtils.compare(apSitProJointEmployeur.getDateFin(), apRepJointPrestation.getDateDebut());
                    if (prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.INCOMPARABLE) || prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.EQUALS) || prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.BEFORE)) {
                        // Adapte la date de fin de la prestation en fonction de la situation profesionelle qui possède une date de fin de contrat la plus éloignée
                        if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.AFTER)) {
                            apRepJointPrestation.setDateFin(apSitProJointEmployeur.getDateFin());
                        }
                        apRepJointPrestationIsDateFinContrat.add(apRepJointPrestation);
                    }
                }
            }
        }
        return apRepJointPrestationIsDateFinContrat;
    }

    private List<APPrestation> filterAPPrestationsIsCotisationComplementaireAvantDebutPrestations(List<APPrestation> apPrestationsIsDateDebutAvantProprieteFerciab, List<APSitProJointEmployeur> apSitProJointEmployeursIsComplement) {
        List<APPrestation> apPrestationsIsCotisationComplementaireAvantDebutPrestations = new ArrayList<>();

        APSitProJointEmployeur apSitProJointEmployeurMinDateDebut = apSitProJointEmployeursIsComplement.get(0);
        // La situation professionele qui cotise à une assurance complémentaire depuis le plus longtemps
        for (APSitProJointEmployeur apSitProJointEmployeurIsComplement : apSitProJointEmployeursIsComplement) {
            PRDateUtils.PRDateEquality prestationBeginDateCheck = PRDateUtils.compare(apSitProJointEmployeurMinDateDebut.getDateDebut(), apSitProJointEmployeurIsComplement.getDateDebut());
            if (prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.BEFORE)) {
                apSitProJointEmployeurMinDateDebut = apSitProJointEmployeurIsComplement;
            }
        }

        for (APPrestation apPrestation : apPrestationsIsDateDebutAvantProprieteFerciab) {
            // Ne prends en compte que les prestations qui commence après la date trouvé dans la situations professionelle qui cotise à une assurance complémentaire depuis le plus longtemps
            PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(apPrestation.getDateFin(), apSitProJointEmployeurMinDateDebut.getDateDebut());
            PRDateUtils.PRDateEquality prestationBeginDateCheck = PRDateUtils.compare(apPrestation.getDateDebut(), apSitProJointEmployeurMinDateDebut.getDateDebut());
            if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.EQUALS) || prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.BEFORE)) {
                // Adapte la date de début en fonction de la situation profesionelle qui cotise à une assurance complémentaire depuis le plus longtemps
                if (prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.AFTER)) {
                    apPrestation.setDateDebut(apSitProJointEmployeurMinDateDebut.getDateDebut());
                }
                apPrestationsIsCotisationComplementaireAvantDebutPrestations.add(apPrestation);
            }
        }
        return apPrestationsIsCotisationComplementaireAvantDebutPrestations;
    }

    private  List<APPrestation> filterAPRepJointPrestationsIsDateDebutAvantProprieteFerciabMATCIAB1(APPrestationManager mgr) {
        final List<APPrestation> apPrestationsIsDateDebutAvantProprieteFerciab = new ArrayList<>();

        // Contrôle la propriété PROPERTY_APG_FERCIAB_MATERNITE
        String proprieteAPGFerciabMaternite = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_APG_FERCIAB_MATERNITE);

        for (int i = 0; i < mgr.getSize(); i++) {
            final APPrestation prestation = (APPrestation) mgr.getEntity(i);

            // Ne prends en compte que les prestations qui commence après la date trouvé dans la propriété PROPERTY_APG_FERCIAB_MATERNITE
            PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(prestation.getDateFin(), proprieteAPGFerciabMaternite);
            PRDateUtils.PRDateEquality prestationBeginDateCheck = PRDateUtils.compare(prestation.getDateDebut(), proprieteAPGFerciabMaternite);
            if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.EQUALS) || prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.BEFORE)) {
                // Adapte la date de début en fonction de la propriété PROPERTY_APG_FERCIAB_MATERNITE
                if (prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.AFTER)) {
                    prestation.setDateDebut(proprieteAPGFerciabMaternite);
                }
                apPrestationsIsDateDebutAvantProprieteFerciab.add(prestation);
            }

        }
        return apPrestationsIsDateDebutAvantProprieteFerciab;
    }

    private List<APRepartitionJointPrestation> filterAPRepJointPrestationsIsDateDebutAvantProprieteFerciabMATCIAB2(List<APRepartitionJointPrestation> repartitionNonFiltrees) {
        final List<APRepartitionJointPrestation> apRepJointPrestationsIsDateDebutAvantProprieteFerciab = new ArrayList<APRepartitionJointPrestation>();

        // Contrôle la propriété PROPERTY_APG_FERCIAB_MATERNITE
        String apgFerciabMaternite = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_APG_FERCIAB_MATERNITE);

        for (APRepartitionJointPrestation apRepJointPrest : repartitionNonFiltrees) {

            // Ne prends en compte que les prestations qui commence après la date trouvé dans la propriété PROPERTY_APG_FERCIAB_MATERNITE
            String dateFinMoins1Jour = JadeDateUtil.addDays(apRepJointPrest.getDateFin(), 1); // Pour MATCIAB2 il faut comparer la propriété PROPERTY_APG_FERCIAB_MATERNITE à la date de fin moins 1 jours
            PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(dateFinMoins1Jour, apgFerciabMaternite);
            PRDateUtils.PRDateEquality prestationBeginDateCheck = PRDateUtils.compare(apRepJointPrest.getDateDebut(), apgFerciabMaternite);
            if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.EQUALS) || prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.BEFORE)) {
                // Adapte la date de début en fonction de la propriété PROPERTY_APG_FERCIAB_MATERNITE
                if (prestationBeginDateCheck.equals(PRDateUtils.PRDateEquality.AFTER)) {
                    apRepJointPrest.setDateDebut(apgFerciabMaternite);
                }
                apRepJointPrestationsIsDateDebutAvantProprieteFerciab.add(apRepJointPrest);
            }

        }
        return apRepJointPrestationsIsDateDebutAvantProprieteFerciab;
    }

    private List<APSitProJointEmployeur> filterAPSitProJointEmployeursIsComplementMATCIAB1(String idDroit, BSession session, APCalculateurComplementDonneesPersistence donneesPersistence, String dateDebutPrestationStandard, List<APSitProJointEmployeur> apSitProJointEmployeurs) throws Exception {
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

        List<APSitProJointEmployeur> apSitProJointEmployeursIsComplement = new ArrayList<>();

        // Récupération des taux
        for (final APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeurs) {

            String typeAffiliation = getTypeAffiliation(session, apSitProJointEmployeur.getIdAffilie());

            donneesPersistence.getMapTypeAffiliation().put(apSitProJointEmployeur.getIdSitPro(), typeAffiliation);

            // {taux AVS par, taux AC par}>
            final BigDecimal[] taux = new BigDecimal[4];

            taux[0] = getTauxAssurance(APProperties.ASSURANCE_AVS_PAR_ID.getValue(), dateDebutPrestationStandard,
                    session);
            taux[1] = getTauxAssurance(APProperties.ASSURANCE_AC_PAR_ID.getValue(), dateDebutPrestationStandard,
                    session);
            taux[2] = getTauxAssurance(APProperties.ASSURANCE_AVS_PER_ID.getValue(), dateDebutPrestationStandard,
                    session);
            taux[3] = getTauxAssurance(APProperties.ASSURANCE_AC_PER_ID.getValue(), dateDebutPrestationStandard,
                    session);

            donneesPersistence.getTaux().put(apSitProJointEmployeur.getIdSitPro(), taux);

            // list les cantons
            Map<String, ECanton> mCanton = new HashMap<>();
            Map<IAFAssurance, String> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercherAvecDateDebut(idDroit,
                    apSitProJointEmployeur.getIdAffilie(), session);

            String idAssuranceEmployeur = null;

            // recheche d'une assurance dans les propriétés
            for (Map.Entry<IAFAssurance, String> assurance : listAssurance.entrySet()) {
                if (apSitProJointEmployeur.getIndependant()) {
                    if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelBE)) {
                        apSitProJointEmployeur.setDateDebut(assurance.getValue());
                        idAssuranceEmployeur = idAssurancePersonnelBE;
                    } else if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelJU)) {
                        apSitProJointEmployeur.setDateDebut(assurance.getValue());
                        idAssuranceEmployeur = idAssurancePersonnelJU;
                    }
                } else {
                    if (assurance.getKey().getAssuranceId().equals(idAssuranceParitaireBE)) {
                        apSitProJointEmployeur.setDateDebut(assurance.getValue());
                        idAssuranceEmployeur = idAssuranceParitaireBE;
                    } else if (assurance.getKey().getAssuranceId().equals(idAssuranceParitaireJU)) {
                        apSitProJointEmployeur.setDateDebut(assurance.getValue());
                        idAssuranceEmployeur = idAssuranceParitaireJU;
                    }
                }
            }

            // filtre les situations proffessionelles qui ne sont pas assurées à une des complémentaire
            if (idAssuranceEmployeur != null) {
                apSitProJointEmployeursIsComplement.add(apSitProJointEmployeur);
                if (idAssuranceEmployeur.equals(idAssuranceParitaireBE)
                        || idAssuranceEmployeur.equals(idAssurancePersonnelBE)) {
                    mCanton.put(apSitProJointEmployeur.getIdSitPro(), ECanton.BE);
                } else if (idAssuranceEmployeur.equals(idAssuranceParitaireJU)
                        || idAssuranceEmployeur.equals(idAssurancePersonnelJU)) {
                    mCanton.put(apSitProJointEmployeur.getIdSitPro(), ECanton.JU);
                }
                donneesPersistence.setMapCanton(mCanton);
            }
        }
        return apSitProJointEmployeursIsComplement;
    }

    private List<APSitProJointEmployeur> filterAPSitProJointEmployeursIsComplement(String idDroit, BSession session, APCalculateurComplementDonneesPersistence donneesPersistence, String dateDebutPrestationStandard, List<APSitProJointEmployeur> apSitProJointEmployeurs) throws Exception {
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

        List<APSitProJointEmployeur> apSitProJointEmployeursIsComplement = new ArrayList<>();

        // Récupération des taux
        for (final APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeurs) {

            String typeAffiliation = getTypeAffiliation(session, apSitProJointEmployeur.getIdAffilie());

            donneesPersistence.getMapTypeAffiliation().put(apSitProJointEmployeur.getIdSitPro(), typeAffiliation);

            // {taux AVS par, taux AC par}>
            final BigDecimal[] taux = new BigDecimal[4];

            taux[0] = getTauxAssurance(APProperties.ASSURANCE_AVS_PAR_ID.getValue(), dateDebutPrestationStandard,
                    session);
            taux[1] = getTauxAssurance(APProperties.ASSURANCE_AC_PAR_ID.getValue(), dateDebutPrestationStandard,
                    session);
            taux[2] = getTauxAssurance(APProperties.ASSURANCE_AVS_PER_ID.getValue(), dateDebutPrestationStandard,
                    session);
            taux[3] = getTauxAssurance(APProperties.ASSURANCE_AC_PER_ID.getValue(), dateDebutPrestationStandard,
                    session);

            donneesPersistence.getTaux().put(apSitProJointEmployeur.getIdSitPro(), taux);

            // list les cantons
            Map<String, ECanton> mCanton = new HashMap<>();
            List<IAFAssurance> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercher(idDroit,
                    apSitProJointEmployeur.getIdAffilie(), session);
            String idAssuranceEmployeur = null;

            // recheche d'une assurance dans les propriétés
            for (IAFAssurance assurance : listAssurance) {
                if (apSitProJointEmployeur.getIndependant()) {
                    if (assurance.getAssuranceId().equals(idAssurancePersonnelBE)) {
                        idAssuranceEmployeur = idAssurancePersonnelBE;
                    } else if (assurance.getAssuranceId().equals(idAssurancePersonnelJU)) {
                        idAssuranceEmployeur = idAssurancePersonnelJU;
                    }
                } else {
                    if (assurance.getAssuranceId().equals(idAssuranceParitaireBE)) {
                        idAssuranceEmployeur = idAssuranceParitaireBE;
                    } else if (assurance.getAssuranceId().equals(idAssuranceParitaireJU)) {
                        idAssuranceEmployeur = idAssuranceParitaireJU;
                    }
                }
            }

            // filtre les situations proffessionelles qui ne sont pas assurées à une des complémentaire
            if (idAssuranceEmployeur != null) {
                apSitProJointEmployeursIsComplement.add(apSitProJointEmployeur);
                if (idAssuranceEmployeur.equals(idAssuranceParitaireBE)
                        || idAssuranceEmployeur.equals(idAssurancePersonnelBE)) {
                    mCanton.put(apSitProJointEmployeur.getIdSitPro(), ECanton.BE);
                } else if (idAssuranceEmployeur.equals(idAssuranceParitaireJU)
                        || idAssuranceEmployeur.equals(idAssurancePersonnelJU)) {
                    mCanton.put(apSitProJointEmployeur.getIdSitPro(), ECanton.JU);
                }
                donneesPersistence.setMapCanton(mCanton);
            }
        }
        return apSitProJointEmployeursIsComplement;
    }

    private List<APRepartitionJointPrestation> filtrerApRepJointPrestationsIsAllocation(String idDroit, BSession session, BTransaction transaction, APEntityService servicePersistance) throws Exception {
        // Récupération de toutes les restations joint repartitions
        final List<APRepartitionJointPrestation> apRepJointPrestations = servicePersistance
                .getRepartitionJointPrestationDuDroit(session, transaction, idDroit);

        // On filtre car on ne veut pas les restitutions ou autres. Uniquement les prestations d'allocation
        final List<APRepartitionJointPrestation> apRepJointPrestationsIsAllocation = new ArrayList<APRepartitionJointPrestation>();
        for (APRepartitionJointPrestation repJointPrest : apRepJointPrestations) {
            // On prend les prestation allocation et duplicata
            if ((IAPAnnonce.CS_DEMANDE_ALLOCATION.equals(repJointPrest.getContenuAnnonce())
                    || IAPAnnonce.CS_DUPLICATA.equals(repJointPrest.getContenuAnnonce()))
                    && !JadeStringUtil.isBlankOrZero(repJointPrest.getIdSituationProfessionnelle())) {
                apRepJointPrestationsIsAllocation.add(repJointPrest);
            }
        }

        return apRepJointPrestationsIsAllocation;
    }

    private List<APRepartitionJointPrestation> filterAPRepJointPrestationsIsComplementMATCIAB1(String idDroit, BSession session, List<APRepartitionJointPrestation> apRepJointPrestations) throws Exception {
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

        List<APRepartitionJointPrestation> apRepJointPrestationsIsComplement = new ArrayList<>();

        for (APRepartitionJointPrestation apRepJointPrestation : apRepJointPrestations) {

            Map<IAFAssurance, String> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercherAvecDateDebut(idDroit,
                    apRepJointPrestation.getIdAffilie(), session);
            String idAssuranceEmployeur = null;

            // recheche d'une assurance dans les propriétés
            for (Map.Entry<IAFAssurance, String> assurance : listAssurance.entrySet()) {
                if (apRepJointPrestation.loadSituationProfessionnelle().getIsIndependant()) {
                    if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelBE)) {
                        idAssuranceEmployeur = idAssurancePersonnelBE;
                    } else if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelJU)) {
                        idAssuranceEmployeur = idAssurancePersonnelJU;
                    }
                } else {
                    if (assurance.getKey().getAssuranceId().equals(idAssuranceParitaireBE)) {
                        idAssuranceEmployeur = idAssuranceParitaireBE;
                    } else if (assurance.getKey().getAssuranceId().equals(idAssuranceParitaireJU)) {
                        idAssuranceEmployeur = idAssuranceParitaireJU;
                    }
                }
            }

            // filtre les situations proffessionelles qui ne sont pas assurées à une des complémentaire
            if (idAssuranceEmployeur != null) {
                apRepJointPrestationsIsComplement.add(apRepJointPrestation);
            }
        }
        return apRepJointPrestationsIsComplement;
    }

    public static String getTypeAffiliation(BSession session, String idAffiliation) throws Exception {
        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(session);
        affiliationManager.setForAffiliationId(idAffiliation);

        affiliationManager.find(BManager.SIZE_USEDEFAULT);
        if (affiliationManager.size() > 0) {
            // récupération de l'affiliation
            AFAffiliation affiliation = (AFAffiliation) affiliationManager.getFirstEntity();
            return affiliation.getTypeAffiliation();
        }
        return null;
    }

    private void putMontantMax(BSession session, String date, Map<EMontantsMax, BigDecimal> montantsMax,
                               EMontantsMax eMontantMax) throws Exception {
        montantsMax.put(eMontantMax, new BigDecimal(FWFindParameter.findParameter(session.getCurrentThreadTransaction(),
                "1", eMontantMax.name(), date, "", 0)));
    }

    /**
     * @param idDroit
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    private APCalculateurAcmAlphaDonnesPersistence getDonneesPersistancePourCalculAcmNE(final String idDroit,
                                                                                        final BISession iSession, final BITransaction iTransaction) throws Exception {

        final BSession session = (BSession) iSession;
        final BTransaction transaction = (BTransaction) iTransaction;

        final APEntityService servicePersistance = ApgServiceLocator.getEntityService();
        // Bean container des données de persistance
        final APCalculateurAcmAlphaDonnesPersistence donneesPersistence = new APCalculateurAcmAlphaDonnesPersistence();
        donneesPersistence.setIdDroit(idDroit);

        // Récupération de toutes les restations joint repartitions
        final List<APRepartitionJointPrestation> listeTemporaire = servicePersistance
                .getRepartitionJointPrestationDuDroit(session, transaction, idDroit);

        // On filtre car on ne veut pas les restitutions ou autres. Uniquement les prestations d'allocation
        final List<APRepartitionJointPrestation> repartitionJointRepartitionsFiltree = new ArrayList<APRepartitionJointPrestation>();
        for (APRepartitionJointPrestation repJointPrest : listeTemporaire) {
            // On prend les prestation allocation et duplicata
            if (IAPAnnonce.CS_DEMANDE_ALLOCATION.equals(repJointPrest.getContenuAnnonce())) {
                repartitionJointRepartitionsFiltree.add(repJointPrest);
            } else if (IAPAnnonce.CS_DUPLICATA.equals(repJointPrest.getContenuAnnonce())) {
                repartitionJointRepartitionsFiltree.add(repJointPrest);
            }
        }

        donneesPersistence.setPrestations(repartitionJointRepartitionsFiltree);

        // Situations professionnelles
        final List<APSitProJointEmployeur> apSitProJoiEmpList = servicePersistance
                .getSituationProfJointEmployeur(session, transaction, idDroit);
        donneesPersistence.setSituationProfessionnelleEmployeur(apSitProJoiEmpList);

        final String dateDebutPrestationStandard = donneesPersistence.getPrestationJointRepartitions().get(0)
                .getDateDebut();
        // Récupération des taux
        for (final APSitProJointEmployeur apSitProJoiEmp : apSitProJoiEmpList) {
            // {taux AVS par, taux AC par,taux FNE par}>
            final BigDecimal[] taux = new BigDecimal[3];

            taux[0] = getTauxAssurance(APProperties.ASSURANCE_AVS_PAR_ID.getValue(), dateDebutPrestationStandard,
                    session);
            taux[1] = getTauxAssurance(APProperties.ASSURANCE_AC_PAR_ID.getValue(), dateDebutPrestationStandard,
                    session);

            // taux particulier pour l'association FNE
            if (APAssuranceTypeAssociation.FNE.isCodeSystemEqual(apSitProJoiEmp.getCsAssuranceAssociation())) {
                taux[2] = getTauxAssurance(APProperties.ASSURANCE_FNE_ID.getValue(), dateDebutPrestationStandard,
                        session);
            }

            donneesPersistence.getTaux().put(apSitProJoiEmp.getIdSitPro(), taux);
        }

        return donneesPersistence;

    }

    /**
     * @param idDroit
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    private ACM2PersistenceInputData getDonneesPersistancePourCalculAcm2(final APDroitMaternite droit,
                                                                         final BSession session, final BTransaction transaction) throws Exception {

        final APEntityService servicePersistance = ApgServiceLocator.getEntityService();

        // Bean container des données de persistance
        final ACM2PersistenceInputData donneesPersistence = new ACM2PersistenceInputData(droit.getIdDroit());

        // Récupération de toutes les prestations joint repartitions
        final List<APRepartitionJointPrestation> repartitionNonFiltrees = servicePersistance
                .getRepartitionJointPrestationDuDroit(session, transaction, droit.getIdDroit());

        // On filtre car on ne veut pas les restitutions ou autres. Uniquement les prestations d'allocation
        final List<APRepartitionJointPrestation> repartitionJointRepartitionsFiltree = new ArrayList<APRepartitionJointPrestation>();

        for (APRepartitionJointPrestation repJointPrest : repartitionNonFiltrees) {
            // filtrer les prestations qui vont bien !!!
            // FIXME COMMENT ENLEVER LES PRESATIONS DFE RESTITUTION : Pour les prest ACM contenu annonce = 0 !
            repartitionJointRepartitionsFiltree.add(repJointPrest);
            if (!IAPAnnonce.CS_PAIEMENT_RETROACTIF.equals(repJointPrest.getContenuAnnonce())
                    && !IAPAnnonce.CS_RESTITUTION.equals(repJointPrest.getContenuAnnonce())) {
            }
        }

        donneesPersistence.setPrestationJointRepartitions(repartitionJointRepartitionsFiltree);

        // Situations professionnelles
        final List<APSitProJointEmployeur> apSitProJoiEmpList = servicePersistance
                .getSituationProfJointEmployeur(session, transaction, droit.getIdDroit());
        donneesPersistence.setSituationProfessionnelleEmployeur(apSitProJoiEmpList);

        // Add RMD par sitPro
        for (final APSitProJointEmployeur sitProJointEmployeur : apSitProJoiEmpList) {
            APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
            sitPro.setSession(session);
            sitPro.setIdSituationProf(sitProJointEmployeur.getIdSitPro());
            sitPro.retrieve();
            if (sitPro.isNew()) {
                throw new Exception(
                        "Impossible de retrouver la sit pro avec l'id [" + sitPro.getIdSituationProf() + "]");
            }
            // recherche le salaire horaire/mensuel/indépendant/versé
            FWCurrency revenuMoyenDeterminant = APSituationProfessionnelleHelper.getSalaireJournalierVerse(sitPro);
            revenuMoyenDeterminant = new FWCurrency(
                    JANumberFormatter.format(revenuMoyenDeterminant.toString(), 1, 2, JANumberFormatter.SUP));
            donneesPersistence.addRMDParEmployeur(sitPro.getIdSituationProf(), revenuMoyenDeterminant);
        }

        return donneesPersistence;

    }

    /**
     * @param idDroit
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    private ACM2PersistenceInputData getDonneesPersistancePourCalculMATCIAB2(final APDroitMaternite droit,
                                                                         final BSession session, final BTransaction transaction) throws Exception {

        final APEntityService servicePersistance = ApgServiceLocator.getEntityService();

        // Bean container des données de persistance
        final ACM2PersistenceInputData donneesPersistence = new ACM2PersistenceInputData(droit.getIdDroit());

        // Récupération de toutes les prestations joint repartitions
        final List<APRepartitionJointPrestation> repartitionNonFiltrees = servicePersistance
                .getRepartitionJointPrestationDuDroit(session, transaction, droit.getIdDroit());

        // Filtre les prestations qui ont une date de début avant la propriété apg.FERCIAB.maternite
        final List<APRepartitionJointPrestation> apRepJointPrestationsIsDateDebutAvantProprieteFerciab = filterAPRepJointPrestationsIsDateDebutAvantProprieteFerciabMATCIAB2(repartitionNonFiltrees);
        if (apRepJointPrestationsIsDateDebutAvantProprieteFerciab.isEmpty()) return null;

        // Situations professionnelles
        final List<APSitProJointEmployeur> apSitProJointEmployeurs = servicePersistance
                .getSituationProfJointEmployeur(session, transaction, droit.getIdDroit());

        donneesPersistence.setNombreInitialDeSituationsProfessionelles(apSitProJointEmployeurs.size());

        // Filtre les situations proffesionelles qui ne cotise pas au complément
        List<APSitProJointEmployeur> apSitProJointEmployeursIsComplement = filterAPSitProJointEmployeursIsComplementMATCIAB2(droit.getIdDroit(), session, apSitProJointEmployeurs);
        if (apSitProJointEmployeursIsComplement.isEmpty()) return null;

        // Filtre les situations proffesionelles qui ne sont pas des versement employeurs
        List<APSitProJointEmployeur> apSitProJointEmployeursIsVersementEmployeur = filterAPSitProJointEmployeursIsVersementEmployeur(apSitProJointEmployeursIsComplement);
        if (apSitProJointEmployeursIsVersementEmployeur.isEmpty()) return null;

        // Filtre les prestations en fonction de la date de fin de contrat des situations proffessionelles
        setDateFinContrat(session, apSitProJointEmployeursIsVersementEmployeur);
        List<APRepartitionJointPrestation> apRepJointPrestationIsDateFinContrat = filterAPPrestationsIsDateFinContratMATCIAB2(apSitProJointEmployeursIsVersementEmployeur, apRepJointPrestationsIsDateDebutAvantProprieteFerciab);

        // Filtre les situations proffesionelles qui ne sont plus active à la fin de la dernière prestations
        List<APSitProJointEmployeur> apSitProJointEmployeursIsActive = filerAPSitProJointEmployeursIsActive(apRepJointPrestationsIsDateDebutAvantProprieteFerciab, apSitProJointEmployeursIsVersementEmployeur);

        donneesPersistence.setPrestationJointRepartitions(apRepJointPrestationIsDateFinContrat);
        donneesPersistence.setSituationProfessionnelleEmployeur(apSitProJointEmployeursIsActive);

        // Add RMD par sitPro
        for (final APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeursIsActive) {
            APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
            sitPro.setSession(session);
            sitPro.setIdSituationProf(apSitProJointEmployeur.getIdSitPro());
            sitPro.retrieve();
            if (sitPro.isNew()) {
                throw new Exception(
                        "Impossible de retrouver la sit pro avec l'id [" + sitPro.getIdSituationProf() + "]");
            }
            // recherche le salaire horaire/mensuel/indépendant/versé
            FWCurrency revenuMoyenDeterminant = APSituationProfessionnelleHelper.getSalaireJournalierVerse(sitPro);
            revenuMoyenDeterminant = new FWCurrency(
                    JANumberFormatter.format(revenuMoyenDeterminant.toString(), 0.05, 2, JANumberFormatter.SUP));
            donneesPersistence.addRMDParEmployeur(sitPro.getIdSituationProf(), revenuMoyenDeterminant);
        }

        return donneesPersistence;

    }

    private List<APSitProJointEmployeur> filerAPSitProJointEmployeursIsActive(List<APRepartitionJointPrestation> apRepJointPrestationsIsDateDebutAvantProprieteFerciab, List<APSitProJointEmployeur> apSitProJointEmployeursIsVersementEmployeur) {
        List<APSitProJointEmployeur> apSitProJointEmployeursIsActive = new ArrayList<>();

        APRepartitionJointPrestation apRepartitionJointPrestationMaxDateFinContrat = apRepJointPrestationsIsDateDebutAvantProprieteFerciab.get(0);
        for (APRepartitionJointPrestation apRepartitionJointPrestation : apRepJointPrestationsIsDateDebutAvantProprieteFerciab) {
            PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(apRepartitionJointPrestationMaxDateFinContrat.getDateFin(), apRepartitionJointPrestation.getDateFin());
            if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.AFTER)) {
                apRepartitionJointPrestationMaxDateFinContrat = apRepartitionJointPrestation;
            } else if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.INCOMPARABLE)) {
                apRepartitionJointPrestationMaxDateFinContrat = apRepartitionJointPrestation;
                break;
            }
        }

        // Ne garde que les situations professionelles qui sont encore actives au moment de la dernière repartionsJointPrestation
        for (APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeursIsVersementEmployeur) {
            PRDateUtils.PRDateEquality prestationEndDateCheck = PRDateUtils.compare(apRepartitionJointPrestationMaxDateFinContrat.getDateFin(), apSitProJointEmployeur.getDateFin());
            if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.AFTER)) {
                apSitProJointEmployeursIsActive.add(apSitProJointEmployeur);
            } else if (prestationEndDateCheck.equals(PRDateUtils.PRDateEquality.INCOMPARABLE)) {
                apSitProJointEmployeursIsActive.add(apSitProJointEmployeur);
            }
        }

        return apSitProJointEmployeursIsActive;
    }

    private List<APSitProJointEmployeur> filterAPSitProJointEmployeursIsVersementEmployeur(List<APSitProJointEmployeur> apSitProJointEmployeurs) {
        return apSitProJointEmployeurs.stream().filter(APSitProJointEmployeur::getIsVersementEmployeur).collect(Collectors.toList());
    }

    private List<APSitProJointEmployeur> filterAPSitProJointEmployeursIsComplementMATCIAB2(String idDroit, BSession session, List<APSitProJointEmployeur> apSitProJointEmployeurs) throws Exception {
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);
        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

        List<APSitProJointEmployeur> apSitProJointEmployeursIsComplement = new ArrayList<>();

        for (final APSitProJointEmployeur apSitProJointEmployeur : apSitProJointEmployeurs) {

            Map<IAFAssurance, String> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercherAvecDateDebut(idDroit,
                    apSitProJointEmployeur.getIdAffilie(), session);
            String idAssuranceEmployeur = null;

            // recheche d'une assurance dans les propriétés
            for (Map.Entry<IAFAssurance, String> assurance : listAssurance.entrySet()) {
                if (apSitProJointEmployeur.getIndependant()) {
                    if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelBE)) {
                        apSitProJointEmployeur.setDateDebut(assurance.getValue());
                        idAssuranceEmployeur = idAssurancePersonnelBE;
                    } else if (assurance.getKey().getAssuranceId().equals(idAssurancePersonnelJU)) {
                        apSitProJointEmployeur.setDateDebut(assurance.getValue());
                        idAssuranceEmployeur = idAssurancePersonnelJU;
                    }
                } else {
                    if (assurance.getKey().getAssuranceId().equals(idAssuranceParitaireBE)) {
                        apSitProJointEmployeur.setDateDebut(assurance.getValue());
                        idAssuranceEmployeur = idAssuranceParitaireBE;
                    } else if (assurance.getKey().getAssuranceId().equals(idAssuranceParitaireJU)) {
                        apSitProJointEmployeur.setDateDebut(assurance.getValue());
                        idAssuranceEmployeur = idAssuranceParitaireJU;
                    }
                }
            }

            // filtre les situations proffessionelles qui ne sont pas assurées à une des complémentaire
            if (idAssuranceEmployeur != null) {
                apSitProJointEmployeursIsComplement.add(apSitProJointEmployeur);
            }
        }

        return apSitProJointEmployeursIsComplement;
    }

    /**
     * @param session
     * @param droit
     * @return
     * @throws Exception
     */
    private FWCurrency getFraisDeGarde(final BSession session, final APDroitLAPG droit) throws Exception {
        if (droit instanceof APDroitAPG) {
            return new FWCurrency(((APDroitAPG) droit).loadSituationFamilliale().getFraisGarde());
        } else {
            return null;
        }
    }

    private BigDecimal getTauxAssurance(final String idAssurance, final String date, final BISession session)
            throws Exception {

        final AFTauxAssuranceManager afTauAssMgr = new AFTauxAssuranceManager();
        afTauAssMgr.setSession((BSession) session);
        afTauAssMgr.setForIdAssurance(idAssurance);
        afTauAssMgr.setForDate(date);
        afTauAssMgr.setOrderByDebutDescRang();
        afTauAssMgr.changeManagerSize(0);
        afTauAssMgr.find();

        if (afTauAssMgr.size() > 0) {
            final AFTauxAssurance taux = (AFTauxAssurance) afTauAssMgr.getFirstEntity();
            if (null != taux) {
                return new BigDecimal(Double.valueOf(taux.getValeurEmployeur()) / Double.valueOf(taux.getFraction()));
            } else {
                throw new JadePersistenceException("APPrestationHelper.getTauxAssurance() : Taux null");
            }
        } else {
            throw new JadePersistenceException(
                    "APPrestationHelper.getTauxAssurance(): Aucun taux retrouvé pour ce type d'assurance");
        }
    }

    /**
     * @param session
     * @param transaction
     * @return
     */
    private boolean hasErrors(final BSession session, final BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly();
    }

    /**
     * @param viewBean
     * @param bSession
     * @return
     * @throws Exception
     * @throws PRACORException
     */
    private APDroitLAPG importerLesPrestations(final BSession bSession, final BTransaction transaction,
                                               final FWViewBeanInterface viewBean) throws Exception, PRACORException {

        // importer les résultats de ACOR qui nous sont revenus.
        final APCalculACORViewBean caViewBean = (APCalculACORViewBean) viewBean;

        final APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(bSession, transaction,
                caViewBean.getIdDroit());

        final List<APBaseCalcul> basesCalcul = getBasesCalcul(bSession, droit);
        final Collection<APPrestationWrapper> pw = APACORPrestationsParser.parse(droit, basesCalcul, bSession,
                new StringReader(caViewBean.getAnnoncePay()));

        // calculer
        final APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();

        calculateur.reprendreDepuisACOR(bSession, pw, droit, getFraisDeGarde(bSession, droit));
        return droit;
    }

    /**
     * Persiste la liste d'entités fournie en arguments
     *
     * @param apResPreAcmNeEnt
     * @param session
     * @param transaction
     * @param persist
     * @throws Exception
     */
    private void persisterResultatCalculPrestation(final List<APPrestationCalculeeAPersister> resultatCalcul,
                                                   final BSession session, final BTransaction transaction, boolean persist) throws Exception {

        for (final APPrestationCalculeeAPersister prestationCalculee : resultatCalcul) {

            final APPrestation prestation = prestationCalculee.getPrestation();
            prestation.setSession(session);
            if(persist) { prestation.add(transaction); };

            for (final APRepartitionCalculeeAPersister repartitions : prestationCalculee.getRepartitions()) {

                final APRepartitionPaiements repartition = repartitions.getRepartitionPaiements();
                repartition.setSession(session);
                repartition.setIdPrestationApg(prestation.getIdPrestationApg());
                if(persist) { repartition.add(transaction); };

                for (final APCotisation cotisation : repartitions.getCotisations()) {

                    cotisation.setSession(session);
                    cotisation.setIdRepartitionBeneficiairePaiement(repartition.getIdRepartitionBeneficiairePaiement());
                    if(persist) { cotisation.add(transaction); };
                }
            }
        }
    }

    private void supprimerPrestationsDuDroit(final BSession session, String idDroit) throws Exception {
        BITransaction transactionSupPreSta = null;
        try {
            transactionSupPreSta = session.newTransaction();
            transactionSupPreSta.openTransaction();

            ApgServiceLocator.getEntityService().supprimerLesPrestationsDuDroit(session,
                    (BTransaction) transactionSupPreSta, idDroit);

            if (transactionSupPreSta.hasErrors() || transactionSupPreSta.isRollbackOnly()) {
                transactionSupPreSta.rollback();
                throw new Exception(
                        "APPrestationHelper.supprimerPrestationsDuDroit: Impossible de supprimer les prestations");
            } else {
                transactionSupPreSta.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("APPrestationHelper.supprimerPrestationsDuDroit: " + e.getMessage());
        } finally {
            transactionSupPreSta.closeTransaction();
        }
    }

    private void supprimerPrestationsDuDroitParGenre(final BSession session, String idDroit, String genre) throws Exception {
        BITransaction transactionSupPreSta = null;
        try {
            transactionSupPreSta = session.newTransaction();
            transactionSupPreSta.openTransaction();

            ApgServiceLocator.getEntityService().supprimerLesPrestationsDuDroitParGenre(session,
                    (BTransaction) transactionSupPreSta, idDroit, genre);

            if (transactionSupPreSta.hasErrors() || transactionSupPreSta.isRollbackOnly()) {
                transactionSupPreSta.rollback();
                throw new Exception(
                        "APPrestationHelper.supprimerPrestationsDuDroit: Impossible de supprimer les prestations");
            } else {
                transactionSupPreSta.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("APPrestationHelper.supprimerPrestationsDuDroit: " + e.getMessage());
        } finally {
            transactionSupPreSta.closeTransaction();
        }
    }

    public FWViewBeanInterface passerDroitValider(FWViewBeanInterface vb, FWAction action, BSession session)
            throws Exception {
        updateDroitEtat(((APValidationPrestationViewBean) vb).getIdDroit(), session, IAPDroitLAPG.CS_ETAT_DROIT_VALIDE);
        return vb;
    }

    private void updateDroitEtat(String idDroit, BSession session, String etat) throws Exception {
        BTransaction transaction = (BTransaction) session.newTransaction();
        APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, transaction, idDroit);
        droit.setEtat(etat);
        droit.update();
    }
}
