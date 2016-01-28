package globaz.campus.process.validationAnnonces;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitMaterniteJointTiersManager;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.PCAWithCalculMembreFamilleAndPrestation;
import ch.globaz.pegasus.business.models.pcaccordee.PCAWithCalculMembreFamilleAndPrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class GECreationAnnoncesRules {

    private static final String WHERE_KEY = "forPrestationByDate";

    /**
     * Point d'entrée permettant de checker si l'étudiant remplit les conditions de non mises à jour de ses informations
     * 
     * @param idTiers
     * 
     * @return l'état de la condition de non mise à jour
     * @throws Exception
     */
    public static Boolean checkIfModifiable(String idTiers, BSession sessionCampus) throws Exception {

        if (null == idTiers) {
            throw new IllegalArgumentException("The idTiers passed in the method can't be null");
        }

        // Si le tiers n'existe pas, il faut pouvoir le créer(modifier)
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            return true;
        }

        Boolean modifiable = Boolean.FALSE;

        // initialisation du thread context et utilisation du contextjdbc
        try {

            JadeThreadContext threadContext = initThreadContext(sessionCampus);
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // définition des règles permettant ou non de définir si les infos du tiers sont modifiables
            modifiable = !hasRentesAccordees(idTiers, sessionCampus) && !hasPcAcoordees(idTiers) && !hasAf(idTiers)
                    && !hasApgOuMaternite(idTiers);

        } catch (Exception ex) {
            throw new Exception(ex);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return modifiable;

    }

    /**
     * Recherche si l'étudiant est au bénéfice d'une rente accordé
     * 
     * @param idTiers l'idTiers de l'étudiant concerné
     * @param sessionCampus, la session initialisé précédemment pèermettant d'effectuer la recherche
     * @return l'état booléen définissant si le tiers est au bénéfice d'une rente
     * @throws Exception si une exception est survenue durant la recherche des résultats
     */
    private static Boolean hasRentesAccordees(String idTiers, BSession sessionCampus) throws Exception {

        REPrestationAccordeeManager prestAccordeesManager = new REPrestationAccordeeManager();
        prestAccordeesManager.setForIdTiersBeneficiaire(idTiers);
        prestAccordeesManager.setForEnCoursAtMois(getActualMonthNumber());
        prestAccordeesManager.setForGenreRentes(IREPrestationAccordee.CS_GENRE_RENTES);
        prestAccordeesManager.setSession(sessionCampus);
        int nbreOfResult = prestAccordeesManager.getCount();

        return nbreOfResult > 0;
    }

    /**
     * Recherche si l'étudiant est au bénéfice d'une rente accordé
     * 
     * @param idTiers l'idTiers de l'étudiant concerné
     * @return l'état booléen définissant si le tiers est au bénéfice d'une PC
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private static Boolean hasPcAcoordees(String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        Boolean hasPcValable = Boolean.FALSE;

        PCAWithCalculMembreFamilleAndPrestationSearch search = new PCAWithCalculMembreFamilleAndPrestationSearch();
        search.setForIdTiersMembreFamille(idTiers);
        search.setForDateValable(getDateNowAsString());
        search.setIsPlanRetenu(Boolean.TRUE);
        search.setForCsEtatPcAccordee(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = PegasusServiceLocator.getPCAccordeeService().search(search);

        if (search.getSearchResults().length > 0) {

            PCAWithCalculMembreFamilleAndPrestation pca = (PCAWithCalculMembreFamilleAndPrestation) search
                    .getSearchResults()[0];

            if (pca.getPlanDeCalculWitMembreFamille().getSimplePlanDeCalcul().getEtatPC()
                    .equals(IPCValeursPlanCalcul.STATUS_OCTROI)
                    || pca.getPlanDeCalculWitMembreFamille().getSimplePlanDeCalcul().getEtatPC()
                            .equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL)) {
                hasPcValable = Boolean.TRUE;
            }
        }

        return hasPcValable;

    }

    /**
     * Définit si le tiers concerné est au bénéfice d'une AF
     * 
     * @param idTiers, l'id du tiers concerné
     * @return l'état booléen définissant si le tiers est au bénéfice d'une AF
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private static Boolean hasAf(String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {
        DossierComplexSearchModel searchDossiers = new DossierComplexSearchModel();
        searchDossiers.setForIdTiersAllocataire(idTiers);
        int nbreFa = ALImplServiceLocator.getDossierBusinessService()
                .countDossiersActifs(getDateNowAsString(), idTiers);

        return nbreFa > 0;

    }

    /**
     * Définit si le tiers concerné eat au bénéfice d'une Assurance Maternité (pas d'APG parce que paiement dans le
     * futur)
     * 
     * @param idTiers, l'id du tiers concerné
     * @return l'état booléen définissant si le tiers est au bénéfice d'une AM
     * @throws Exception
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private static Boolean hasApgOuMaternite(String idTiers) throws Exception {
        APDroitMaterniteJointTiersManager droitMat = new APDroitMaterniteJointTiersManager();
        droitMat.setForIdTiers(idTiers);
        List<String> notInEtat = new ArrayList<String>();
        notInEtat.add(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        notInEtat.add(IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL);
        droitMat.setForEtatDroitNotIn(notInEtat);

        droitMat.find();

        return droitMat.getContainer().size() > 0;

    }

    private static String getActualMonthNumber() {
        return JadeDateUtil.convertDateMonthYear(JadeDateUtil.getGlobazFormattedDate(new Date()));

    }

    private static String getDateNowAsString() {
        String nowAsStr = JadeDateUtil.getFormattedDate(new Date());
        return nowAsStr;

    }

    /**
     * Initialistaion du ThreadContext permettant la recherche via la nouvelle persistance
     * 
     * @param session
     * @return le ThreadContext
     * @throws Exception
     */
    private static JadeThreadContext initThreadContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

}
