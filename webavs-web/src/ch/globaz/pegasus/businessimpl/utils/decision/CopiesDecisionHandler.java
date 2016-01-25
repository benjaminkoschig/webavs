package ch.globaz.pegasus.businessimpl.utils.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.AutoCopiesFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.decision.IPCAutoCopies;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.business.models.decision.CopiesDecision;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.home.HomeUtil;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class CopiesDecisionHandler {

    private static Map<IPCAutoCopies.TYPE_COPIE, CopiesDecision> copiesAuto = null;
    // private static CreanceAccordeeSearch creancesAccordeesSearch = null;
    private static DecisionApresCalcul decision = null;

    /**
     * Methode qui retourne une CopiesDecision avec les infos pour l'agence communale AVS
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws DecisionException
     */
    private static CopiesDecision dealAgenceCommunalAvsCopie() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException, DecisionException {
        // idTiers du requ�rant, servant a recherche l'agence communale AVS comp�tente
        String idTierReq = CopiesDecisionHandler.getIdTiers(CopiesDecisionHandler.decision,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT);

        String idTierAVS = TIBusinessServiceLocator.getAdministrationService().getAgenceCommunalAVSIdTiers(idTierReq);

        if (idTierAVS == null) {
            throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "DECISION_COPIES_AGENCE_AVS_INTROUVABLE"));
        }

        AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTierAVS, true,
                JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                AdresseService.CS_TYPE_COURRIER, null);

        if ((adresse.getAdresseFormate() == null) || (adresse.getFields() == null)) {
            throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "DECISION_COPIES_AGENCE_AVS_INTROUVABLE"));
        }

        CopiesDecision copieAVS = CopiesDecisionHandler.copiesAuto.get(IPCAutoCopies.TYPE_COPIE.AGENCES_COMMUNALES_AVS);
        copieAVS.setDesignation1(CopiesDecisionHandler.getDesignation1Et2ForAdresse(adresse)[0]);
        copieAVS.setDesignation2(CopiesDecisionHandler.getDesignation1Et2ForAdresse(adresse)[1]);
        copieAVS.getSimpleCopiesDecision().setIdTiersCopie(idTierAVS);

        return copieAVS;
    }

    /**
     * Methode qui retourne une CopiesDecision avec les infos pour les homes
     * 
     * @return
     * @throws Exception
     * @throws DecisionException
     */
    private static CopiesDecision dealHomeCopie() throws JadePersistenceException, JadeApplicationException {
        // on set les infos du tier du home
        Home home = CopiesDecisionHandler.getHomeForDecision(CopiesDecisionHandler.decision);

        if (home == null) {
            throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "DECISION_COPIES_HOME_INTROUVABLE"));
        }
        if (JadeStringUtil.isBlankOrZero(home.getId()) || home.isNew()) {
            throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "DECISION_COPIES_HOME_INTROUVABLE"));
        }

        String idTiers = home.getSimpleHome().getIdTiersHome();

        AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers, true,
                JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                AdresseService.CS_TYPE_COURRIER, null);

        CopiesDecision copie = null;

        // Soit copie plan de calcul soit sans
        if (CopiesDecisionHandler.copiesAuto.get(IPCAutoCopies.TYPE_COPIE.HOME_PCAL) != null) {
            copie = CopiesDecisionHandler.copiesAuto.get(IPCAutoCopies.TYPE_COPIE.HOME_PCAL);
        } else {
            copie = CopiesDecisionHandler.copiesAuto.get(IPCAutoCopies.TYPE_COPIE.HOME_SANS_PCAL);
        }
        copie.getSimpleCopiesDecision().setIdTiersCopie(idTiers);
        copie.setDesignation1(CopiesDecisionHandler.getDesignation1Et2ForAdresse(adresse)[0]);
        copie.setDesignation2(CopiesDecisionHandler.getDesignation1Et2ForAdresse(adresse)[1]);

        return copie;

    }

    /**
     * Methode qui retourne une CopiesDecision avec les infos pour Prosenectute
     * 
     * @throws Exception
     * @throws DecisionException
     */
    private static CopiesDecision dealProSenectuteCopie() throws DecisionException, Exception {
        String idTiersProSenectute = CopiesDecisionHandler.getSession().getApplication()
                .getProperty("pegasus.decision.copie.prosenectute.idtiers");

        if (idTiersProSenectute == null) {
            throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "DECISION_COPIES_PROSENECTUTE_INTROUVABLE"));
        }

        AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiersProSenectute,
                true, JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                AdresseService.CS_TYPE_COURRIER, null);

        CopiesDecision copieProSenectute = CopiesDecisionHandler.copiesAuto
                .get(IPCAutoCopies.TYPE_COPIE.PRO_SENECTUTE_JU);

        copieProSenectute.getSimpleCopiesDecision().setIdTiersCopie(idTiersProSenectute);
        copieProSenectute.setDesignation1(CopiesDecisionHandler.getDesignation1Et2ForAdresse(adresse)[0]);
        copieProSenectute.setDesignation2(CopiesDecisionHandler.getDesignation1Et2ForAdresse(adresse)[1]);

        return copieProSenectute;
    }

    /**
     * Methode qui retourne une CopiesDecision avec les infos pour les services sociaux
     * 
     * @return
     * @throws Exception
     * @throws DecisionException
     */
    private static CopiesDecision dealServicesSociauxCopie() throws DecisionException, Exception {

        CopiesDecision copie = null;

        // Soit copie Page de garde soit sans
        if (CopiesDecisionHandler.copiesAuto.get(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_PG) != null) {
            copie = CopiesDecisionHandler.copiesAuto.get(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_PG);
        } else {
            copie = CopiesDecisionHandler.copiesAuto.get(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_SANS_PG);
        }

        AdresseTiersDetail adresse = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                copie.getSimpleCopiesDecision().getIdTiersCopie(), true, JACalendar.todayJJsMMsAAAA(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_COURRIER, null);

        copie.setDesignation1(CopiesDecisionHandler.getDesignation1Et2ForAdresse(adresse)[0]);
        copie.setDesignation2(CopiesDecisionHandler.getDesignation1Et2ForAdresse(adresse)[1]);

        return copie;
    }

    public static ArrayList<CopiesDecision> getCopiesList(DecisionApresCalcul decision) throws Exception {

        CopiesDecisionHandler.decision = decision;
        CopiesDecisionHandler.copiesAuto = CopiesDecisionHandler.loadCopies();

        ArrayList<CopiesDecision> listeCopies = new ArrayList<CopiesDecision>();

        for (IPCAutoCopies.TYPE_COPIE typeCopie : CopiesDecisionHandler.copiesAuto.keySet()) {
            // ***************** PRO SENECTUTE *****************
            if (typeCopie.equals(IPCAutoCopies.TYPE_COPIE.PRO_SENECTUTE_JU)
                    && !decision.getPcAccordee().getSimplePCAccordee().getCsTypePC()
                            .equals(IPCPCAccordee.CS_TYPE_PC_INVALIDITE)) {
                listeCopies.add(CopiesDecisionHandler.dealProSenectuteCopie());
            }

            // ***************** AGENCE COMMUNALE AVS *****************
            if (typeCopie.equals(IPCAutoCopies.TYPE_COPIE.AGENCES_COMMUNALES_AVS)) {
                listeCopies.add(CopiesDecisionHandler.dealAgenceCommunalAvsCopie());
            }

            // ***************** SERVICE SOCIAUX *****************
            if (typeCopie.equals(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_PG)
                    || typeCopie.equals(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_SANS_PG)) {
                listeCopies.add(CopiesDecisionHandler.dealServicesSociauxCopie());
            }

            // ***************** HOMES *****************
            if (typeCopie.equals(IPCAutoCopies.TYPE_COPIE.HOME_PCAL)
                    || typeCopie.equals(IPCAutoCopies.TYPE_COPIE.HOME_SANS_PCAL)) {
                listeCopies.add(CopiesDecisionHandler.dealHomeCopie());
            }

        }
        return listeCopies;
    }

    /**
     * Retourne un tableau de chaine de caract�re contrenat les desigantions 1 et 2 desd adresses
     * 
     * @param adresse
     * @return
     */
    private static String[] getDesignation1Et2ForAdresse(AdresseTiersDetail adresse) {
        String[] designations = new String[2];
        designations[0] = adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1);
        designations[1] = adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2);
        return designations;

    }

    /**
     * Retourne le home pour la d�cision concern�
     * 
     * @param decision
     * @return home
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws HomeException
     */
    private static Home getHomeForDecision(DecisionApresCalcul decision)
            throws JadeApplicationServiceNotAvailableException, PCAccordeeException, JadePersistenceException,
            HomeException {
        decision.getPcAccordee().getSimplePCAccordee();
        return HomeUtil.readHomeByPlanCacule(decision.getPcAccordee().getId(), decision.getPcAccordee()
                .getSimplePCAccordee().getCsRoleBeneficiaire());
    }

    /**
     * Retourne l'id du tiers demand� concern� par la d�cision et correspondant au r�le pass� en param�tre
     * 
     * @param decision
     * @return id du tiers (requ�rant ou conjoint)
     */
    private static String getIdTiers(DecisionApresCalcul decision, String membre) {
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decision.getPcAccordee().getPersonneEtendue().getPersonneEtendue().getIdTiers();
        } else if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
            return decision.getPcAccordee().getPersonneEtendueConjoint().getPersonneEtendue().getIdTiers();
        } else {
            return null;
        }

    }

    private static Boolean getIfCreanceExistForTiers(String idTiersServiceSocial) throws CreancierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // recherche si creanciers
        CreancierSearch creancesSearch = new CreancierSearch();
        creancesSearch.setForIdDemande(CopiesDecisionHandler.decision.getVersionDroit().getDemande().getId());
        creancesSearch = PegasusServiceLocator.getCreancierService().search(creancesSearch);

        for (JadeAbstractModel creance : creancesSearch.getSearchResults()) {

            Creancier creanceAcc = (Creancier) creance;

            BigDecimal montantRembourser = PegasusServiceLocator.getCreanceAccordeeService()
                    .findTotalCreanceVerseByDemandeForCreancier(
                            CopiesDecisionHandler.decision.getVersionDroit().getDemande().getId(), creanceAcc.getId());
            BigDecimal soldeARembourser = new BigDecimal(creanceAcc.getSimpleCreancier().getMontant())
                    .subtract(montantRembourser);

            // si le tiers correspond(Service social, et que le montant est > 0)
            if ((idTiersServiceSocial.equals(creanceAcc.getSimpleCreancier().getIdTiers()))
                    && (soldeARembourser.intValue() > 0.00f)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne la session pour acc�s au labels
     * 
     * @return BSession session
     * @throws DecisionException
     */
    private static BSession getSession() throws DecisionException {
        BSession session = BSessionUtil.getSessionFromThreadContext();

        if (session == null) {
            throw new DecisionException("Unable to fill Annexes, the session is null");
        }

        return session;

    }

    /**
     * Chargement des copies des d�cision. Toutes doivente �tre d�finis dans le fichier de properties
     * 
     * @return , map des copies
     * @throws DecisionException
     */
    private static Map<IPCAutoCopies.TYPE_COPIE, CopiesDecision> loadCopies() throws DecisionException {

        Map<IPCAutoCopies.TYPE_COPIE, CopiesDecision> returnMap = new HashMap<IPCAutoCopies.TYPE_COPIE, CopiesDecision>();

        BSession session = CopiesDecisionHandler.getSession();

        try {
            // ***************** PRO SENECTUTE *****************
            if (Boolean
                    .parseBoolean(session.getApplication().getProperty("pegasus.decision.copie.prosenectute.defaut"))) {

                CopiesDecision copie = new CopiesDecision();
                copie.setSimpleCopiesDecision(AutoCopiesFactory
                        .getAutoCopies(IPCAutoCopies.TYPE_COPIE.PRO_SENECTUTE_JU));
                returnMap.put(IPCAutoCopies.TYPE_COPIE.PRO_SENECTUTE_JU, copie);
            }

            // ***************** HOMES *****************
            if (Boolean.parseBoolean(session.getApplication().getProperty("pegasus.decision.copie.home.defaut"))
                    && (CopiesDecisionHandler.getHomeForDecision(CopiesDecisionHandler.decision) != null)) {
                // type de copies avec ou sans plandecalcul
                CopiesDecision copie = new CopiesDecision();
                if (Boolean
                        .parseBoolean(session.getApplication().getProperty("pegasus.decision.copie.home.plancalcul"))) {
                    copie.setSimpleCopiesDecision(AutoCopiesFactory.getAutoCopies(IPCAutoCopies.TYPE_COPIE.HOME_PCAL));
                    returnMap.put(IPCAutoCopies.TYPE_COPIE.HOME_PCAL, copie);
                } else {
                    copie.setSimpleCopiesDecision(AutoCopiesFactory
                            .getAutoCopies(IPCAutoCopies.TYPE_COPIE.HOME_SANS_PCAL));
                    returnMap.put(IPCAutoCopies.TYPE_COPIE.HOME_SANS_PCAL, copie);
                }
            }

            // ***************** SERVICE SOCIAUX *****************
            if (Boolean.parseBoolean(session.getApplication().getProperty(
                    EPCProperties.DECISION_COPIE_SERVICES_SOCIAUX_DEFAUT.getPropertyName()))) {

                String idTiersServiceSociaux = CopiesDecisionHandler.getSession().getApplication()
                        .getProperty("pegasus.decision.copie.servicesociaux.idtiers");

                if (idTiersServiceSociaux == null) {
                    throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                            "DECISION_COPIES_SERVICES_SOCIAUX_INTROUVABLE"));
                }

                // il faut qu'il y ait une creance pour le tiers voulue
                if (CopiesDecisionHandler.getIfCreanceExistForTiers(idTiersServiceSociaux)) {
                    CopiesDecision copie = new CopiesDecision();
                    copie.getSimpleCopiesDecision().setIdTiersCopie(idTiersServiceSociaux);

                    if (Boolean.parseBoolean(session.getApplication().getProperty(
                            EPCProperties.DECISION_COPIE_SERVICES_SOCIAUX_PAGEGARDE.getPropertyName()))) {
                        copie.setSimpleCopiesDecision(AutoCopiesFactory
                                .getAutoCopies(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_PG));
                        returnMap.put(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_PG, copie);
                    } else {
                        copie.setSimpleCopiesDecision(AutoCopiesFactory
                                .getAutoCopies(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_SANS_PG));
                        returnMap.put(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_SANS_PG, copie);
                    }
                }

            }

            // ***************** AGENCE COMMUNALE AVS *****************
            if (Boolean.parseBoolean(session.getApplication().getProperty("pegasus.decision.copie.agenceavs"))) {

                CopiesDecision copie = new CopiesDecision();
                copie.setSimpleCopiesDecision(AutoCopiesFactory
                        .getAutoCopies(IPCAutoCopies.TYPE_COPIE.AGENCES_COMMUNALES_AVS));
                returnMap.put(IPCAutoCopies.TYPE_COPIE.AGENCES_COMMUNALES_AVS, copie);
            }
            return returnMap;
        } catch (Exception e) {
            throw new DecisionException("Unable to fill copies, an error occured", e);
        }

    }
}
