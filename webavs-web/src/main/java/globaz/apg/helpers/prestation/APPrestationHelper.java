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
import ch.globaz.common.properties.CommonPropertiesUtils;
import globaz.apg.ApgServiceLocator;
import globaz.apg.acor.parser.APACORPrestationsParser;
import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
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
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APEnfantAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.apg.db.droits.APSituationFamilialeMatManager;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APCotisationManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.enums.APAssuranceTypeAssociation;
import globaz.apg.enums.APTypeCalculPrestation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.helpers.droits.APSituationProfessionnelleHelper;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.module.calcul.APCalculException;
import globaz.apg.module.calcul.APModuleRepartitionPaiements;
import globaz.apg.module.calcul.APPrestationCalculee;
import globaz.apg.module.calcul.APPrestationStandardLamatAcmAlphaData;
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
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
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

/**
 * <H1>Description</H1> Créé le 3 juin 05
 *
 * @author vre
 */
public class APPrestationHelper extends PRAbstractHelper {

    public static final String ACTION_CALCULER_PRESTATION_AVEC_ACOR = "calculerPrestationsAvecAcor";
    public static final String ACTION_CALCULER_PRESTATION_AVEC_CALCULATEUR_GLOBAZ = "calculDesPrestationsAvecCalculateurGlobaz";
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

            @SuppressWarnings("unchecked")
            final Iterator<APSituationProfessionnelle> iter = man.iterator();
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

        @SuppressWarnings("unchecked")
        final Iterator<APSituationProfessionnelle> iter = man.iterator();
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

            // calcul des ACM NE si la propriété TYPE_DE_PRESTATION_ACM vaut ACM_NE
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
     * Lance le calcul des prestations APG
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
     * Calcul des prestations ACM NE si la propriété APProperties.TYPE_DE_PRESTATION_ACM vaut ACM_NE et si nous somme
     * dans les APG</br>
     * Pas de prestastions ACM_NE pour la maternité
     *
     * @see{APProperties.TYPE_DE_PRESTATION_ACM
     * @param session
     * @param transaction
     * @param viewBean
     * @throws Exception
     */
    private void calculerComplement(final BSession session, final BTransaction transaction, final APDroitLAPG droit) throws Exception {

        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
            return;
        }

        String hasComplement = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);

        if (!"true".equals(hasComplement)) {
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

        donnesPersistencePourCalcul.setDroit((APDroitAPG) droit);

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
        persisterResultatCalculPrestation(resultatCalculAPersister, session, transaction);
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
        return true;
    }

    /**
     * Calcul des prestations ACM NE si la propriété APProperties.TYPE_DE_PRESTATION_ACM vaut ACM_NE et si nous somme
     * dans les APG</br>
     * Pas de prestastions ACM_NE pour la maternité
     *
     * @see{APProperties.TYPE_DE_PRESTATION_ACM
     * @param session
     * @param transaction
     * @param viewBean
     * @throws Exception
     */
    private void calculerPrestationsAcmNe(final BSession session, final BTransaction transaction,
            final APDroitLAPG droit) throws Exception {

        /**
         * calcul des ACM NE si la propriété TYPE_DE_PRESTATION_ACM vaut ACM_NE et si nous somme dans les APG Pas de
         * prestastions ACM_NE pour la maternité
         **/
        if (!JadeStringUtil.isBlankOrZero(droit.getGenreService())) {
            if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
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
        final List<Object> entiteesDomainResultatCalcul = calculateurAcmNe
                .calculerPrestation(entiteesDomainPourCalculAcmNe);

        // Conversion des entités de domain vers des entités de persistance
        final List<APPrestationCalculeeAPersister> resultatCalculAPersister = calculateurAcmNe
                .domainToPersistence(entiteesDomainResultatCalcul);

        // Sauvegarde des entités de persistance
        persisterResultatCalculPrestation(resultatCalculAPersister, session, transaction);
    }

    /**
     * Calcul des prestations ACM 2 si la propriété {@link APProperties#PRESTATION_ACM_2_ACTIF} ET que l'on soit dans un
     * droit maternité</br>
     * Le calcul sera réalisé pour les situations professionnelle qui le nécéssitent (case à cocher dans l'écran des
     * sit. prof)</br>
     *
     *
     * @see APProperties.PRESTATION_ACM_2_ACTIF
     * @see APProperties#PRESTATION_ACM_2_NOMBRE_JOURS
     * @param session
     * @param transaction
     * @param droit
     * @throws Exception
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
            // TODO internationaliser
            throw new Exception("Aucune prestation ACM 2 sera générées car la durée en jours n'est pas correcte ["
                    + APProperties.PRESTATION_ACM_2_NOMBRE_JOURS.getDescription() + "=" + nombreJoursACM2 + "]");
        }

        if (!hasSituationProfAvecACM2(session, droit)) {
            return;
        }

        // OK -> calcul des prestations ACM 2

        final IAPPrestationCalculateur calculateurAcm2 = APPrestationCalculateurFactory
                .getCalculateurInstance(APTypeDePrestation.ACM2_ALFA);

        // TODO
        // Récupération des données depuis la persistence pour le calcul des prestations ACM NE
        final ACM2PersistenceInputData donnesPersistencePourCalculAcm2 = getDonneesPersistancePourCalculAcm2(droit,
                nombreJoursACM2, session, transaction);

        // Conversion vers des objets métier (domain) pour le calculateur
        final List<Object> entiteesDomainPourCalculAcmNe = calculateurAcm2
                .persistenceToDomain(donnesPersistencePourCalculAcm2);

        // Calcul des prestation ACM NE avec le calculateur approprié
        final List<Object> entiteesDomainResultatCalcul = calculateurAcm2
                .calculerPrestation(entiteesDomainPourCalculAcmNe);

        // Conversion des entités de domain vers des entités de persistance
        final List<APPrestationCalculeeAPersister> resultatCalculAPersister = calculateurAcm2
                .domainToPersistence(entiteesDomainResultatCalcul);

        // Sauvegarde des entités de persistance
        persisterResultatCalculPrestation(resultatCalculAPersister, session, transaction);

        // Génération des cotisations ACM 2
        genererLesCotisations(session, resultatCalculAPersister);
    }

    private void genererLesCotisations(final BSession session,
            List<APPrestationCalculeeAPersister> resultatCalculAPersister) throws Exception {

        // Nous allons générer pour chaque répartition de la prestation des cotisations ACM
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

        // Génération des cotisations pour la répartition de la prestation courante
        APModuleRepartitionPaiements module = new APModuleRepartitionPaiements();
        module.genererCotisationsACM(session, prestation, repartition);

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

        for (Iterator iter = mgr.iterator(); iter.hasNext();) {
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
                        || APTypeDePrestation.JOUR_ISOLE.isCodeSystemEqual(prestations.get(ctr).getGenre())) {
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

            // Validation de chaque annonce par le services des plausi
            final APPlausibilitesApgService plausiService = ApgServiceLocator.getPlausibilitesApgService();
            for (final APValidationPrestationAPGContainer container : containers) {
                // Exécution des Rules RAPG
                final List<ViolatedRule> validationErrors = plausiService.checkAnnonce(session, container, tiers);
                container.setValidationErrors(validationErrors);
            }

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
            } else {
                apgOuMaternite = APCalculateurPrestationStandardLamatAcmAlpha.PRESTATION_APG;
            }

            if (!IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(droit.getEtat())) {
                throw new APCalculException(session.getLabel("MODULE_CALCUL_ETAT_DROIT_INVALIDE"));
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
            } else {
                if (APCalculAcorUtil.grantCalulAcorAPG(session, (APDroitAPG) droit)) {
                    viewBean.setTypeCalculPrestation(APTypeCalculPrestation.ACOR);
                }
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

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
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

        // Récupération de toutes les restations joint repartitions
        final List<APRepartitionJointPrestation> listeTemporaire = servicePersistance
                .getRepartitionJointPrestationDuDroit(session, transaction, idDroit);

        // On filtre car on ne veut pas les restitutions ou autres. Uniquement les prestations d'allocation
        final List<APRepartitionJointPrestation> repartitionJointRepartitionsFiltree = new ArrayList<APRepartitionJointPrestation>();
        for (APRepartitionJointPrestation repJointPrest : listeTemporaire) {
            // On prend les prestation allocation et duplicata
            if ((IAPAnnonce.CS_DEMANDE_ALLOCATION.equals(repJointPrest.getContenuAnnonce())
                    || IAPAnnonce.CS_DUPLICATA.equals(repJointPrest.getContenuAnnonce()))
                    && !JadeStringUtil.isBlankOrZero(repJointPrest.getIdSituationProfessionnelle())) {
                repartitionJointRepartitionsFiltree.add(repJointPrest);
            }
        }

        if (repartitionJointRepartitionsFiltree.isEmpty()) {
            return null;
        }

        donneesPersistence.setPrestationJointRepartitions(repartitionJointRepartitionsFiltree);

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

            // list les cantons
            Map<String, ECanton> mCanton = new HashMap<>();
            String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
            String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);

            String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
            String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                    .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);
            List<IAFAssurance> listAssurance = APRechercherAssuranceFromDroitCotisationService.rechercher(idDroit,
                    apSitProJoiEmp.getIdAffilie(), session);
            for (IAFAssurance assurance : listAssurance) {
                if (assurance.getAssuranceId().equals(idAssuranceParitaireBE)
                    || assurance.getAssuranceId().equals(idAssurancePersonnelBE)) {
                    mCanton.put(apSitProJoiEmp.getIdSitPro(), ECanton.BE);
                } else if (assurance.getAssuranceId().equals(idAssuranceParitaireJU) ||
                        assurance.getAssuranceId().equals(idAssurancePersonnelJU)) {
                    mCanton.put(apSitProJoiEmp.getIdSitPro(), ECanton.JU);
                }
            }
            donneesPersistence.setMapCanton(mCanton);

        }

        Map<EMontantsMax, BigDecimal> montantsMax = new HashMap<>();
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.COMCIABJUR);
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.COMCIABBER);
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.COMCIABJUA);
        putMontantMax(session, dateDebutPrestationStandard, montantsMax, EMontantsMax.COMCIABBEA);
        donneesPersistence.setMontantsMax(montantsMax);
        return donneesPersistence;
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
            int nombreJoursACM2, final BSession session, final BTransaction transaction) throws Exception {

        final APEntityService servicePersistance = ApgServiceLocator.getEntityService();

        // Bean container des données de persistance
        final ACM2PersistenceInputData donneesPersistence = new ACM2PersistenceInputData(droit.getIdDroit(),
                nombreJoursACM2);

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

        donneesPersistence.setPrestations(repartitionJointRepartitionsFiltree);

        // Situations professionnelles
        final List<APSitProJointEmployeur> apSitProJoiEmpList = servicePersistance
                .getSituationProfJointEmployeur(session, transaction, droit.getIdDroit());
        donneesPersistence.setSituationProfessionnelleEmployeur(apSitProJoiEmpList);

        for (final APSitProJointEmployeur sitProJointEmployeur : apSitProJoiEmpList) {
            APSituationProfessionnelle sitPro = new APSituationProfessionnelle();
            sitPro.setSession(session);
            sitPro.setIdSituationProf(sitProJointEmployeur.getIdSitPro());
            sitPro.retrieve();
            if (sitPro.isNew()) {
                throw new Exception(
                        "Impossible de retrouver la sit pro avec l'id [" + sitPro.getIdSituationProf() + "]");
            }
            FWCurrency revenuMoyenDeterminant = APSituationProfessionnelleHelper.getSalaireJournalierVerse(sitPro);
            revenuMoyenDeterminant = new FWCurrency(
                    JANumberFormatter.format(revenuMoyenDeterminant.toString(), 1, 2, JANumberFormatter.SUP));
            donneesPersistence.addRMDParEmployeur(sitPro.getIdSituationProf(), revenuMoyenDeterminant);
        }

        return donneesPersistence;

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
     * @throws Exception
     */
    private void persisterResultatCalculPrestation(final List<APPrestationCalculeeAPersister> resultatCalcul,
            final BSession session, final BTransaction transaction) throws Exception {

        for (final APPrestationCalculeeAPersister prestationCalculee : resultatCalcul) {

            final APPrestation prestation = prestationCalculee.getPrestation();
            prestation.setSession(session);
            prestation.add(transaction);

            for (final APRepartitionCalculeeAPersister repartitions : prestationCalculee.getRepartitions()) {

                final APRepartitionPaiements repartition = repartitions.getRepartitionPaiements();
                repartition.setSession(session);
                repartition.setIdPrestationApg(prestation.getIdPrestationApg());
                repartition.add(transaction);

                for (final APCotisation cotisation : repartitions.getCotisations()) {

                    cotisation.setSession(session);
                    cotisation.setIdRepartitionBeneficiairePaiement(repartition.getIdRepartitionBeneficiairePaiement());
                    cotisation.add(transaction);
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
}
