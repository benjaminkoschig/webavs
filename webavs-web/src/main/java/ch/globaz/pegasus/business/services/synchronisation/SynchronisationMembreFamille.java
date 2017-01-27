package ch.globaz.pegasus.business.services.synchronisation;

import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.droit.DroitServiceImpl;

public class SynchronisationMembreFamille {

    public MembresFamillesToSynchronise resolveMembresFamilleToSynchroniseByIdDemande(String idDemande)
            throws DonneesPersonnellesException, MembreFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DroitException {
        Droit droit = findDroitByIdDemandeThrowExcptionIfNotFound(idDemande);
        return resolveMembresFamilleToSynchronise(droit);
    }

    public MembresFamillesToSynchronise resolveEnfantToSynchronise(String idDemande)
            throws DonneesPersonnellesException, MembreFamilleException, DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Droit droit = findDroitByIdDemandeThrowExcptionIfNotFound(idDemande);
        MembresFamillesToSynchronise membresFamillesToSynchronise = resolveMembresFamilleToSynchronise(droit);
        List<MembreFamilleVO> list = membresFamillesToSynchronise.getToAdd();
        List<MembreFamilleVO> listFiltred = new ArrayList<MembreFamilleVO>();
        for (MembreFamilleVO membreFamilleVO : list) {
            if (isEnfant(membreFamilleVO, droit)) {
                listFiltred.add(membreFamilleVO);
            }
        }
        return new MembresFamillesToSynchronise(listFiltred, new ArrayList<MembreFamilleVO>());
    }

    private boolean isEnfant(MembreFamilleVO membreFamille, Droit droit) {
        Boolean isRequerant = isMembreFamilleRequerant(droit, membreFamille);
        String csRoleFamillePC = DroitServiceImpl.convertCsRoleFamillePC(membreFamille.getRelationAuRequerant(),
                isRequerant);
        return IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(csRoleFamillePC);
    }

    private MembresFamillesToSynchronise resolveMembresFamilleToSynchronise(Droit droit)
            throws DonneesPersonnellesException, MembreFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DroitException {

        if (droit == null) {
            throw new DonneesPersonnellesException(
                    "Unable to synchroniseMembresFamille droit, the model passed is null!");
        }
        List<MembreFamilleVO> mfToAdd = new ArrayList<MembreFamilleVO>();
        List<MembreFamilleVO> mfToDelete = new ArrayList<MembreFamilleVO>();

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            MembreFamilleVO[] mfDisponibles = loadSituationFamilliale(droit);

            List<MembreFamilleVO> mfFiltre = new ArrayList<MembreFamilleVO>();

            for (MembreFamilleVO membreFamDispo : mfDisponibles) {
                // On filtre les enfant qui on plus de 25ans (25ans révolue).
                Boolean isRequerant = isMembreFamilleRequerant(droit, membreFamDispo);
                String dateMax = JadeDateUtil.addYears(membreFamDispo.getDateNaissance(), 25).substring(3);
                String dateDepot = droit.getDemande().getSimpleDemande().getDateDepot().substring(3);

                // on ne filtre pas le requérant et le conjoint. On ne filtre pas le requérant qui est une fratrie
                if (!isEnfant(membreFamDispo, droit)
                        || (droit.getDemande().getSimpleDemande().getIsFratrie() && isRequerant)
                        || (JadeDateUtil.isDateMonthYearBefore(dateDepot, dateMax) || dateDepot.equals(dateMax))) {
                    mfFiltre.add(membreFamDispo);
                }
            }

            DroitMembreFamilleSearch searchModel = new DroitMembreFamilleSearch();
            searchModel.setForIdDroit(droit.getId());
            searchModel = PegasusImplServiceLocator.getDroitMembreFamilleService().search(searchModel);

            // cherche nouveaux membres famille
            for (MembreFamilleVO membreFamDispo : mfFiltre) {
                boolean membreTrouve = false;
                membreFamDispo.getRelationAuRequerant();
                for (JadeAbstractModel membreFamAbstract : searchModel.getSearchResults()) {
                    DroitMembreFamille membreFamExistant = (DroitMembreFamille) membreFamAbstract;
                    if (membreFamDispo.getIdMembreFamille().equals(membreFamExistant.getMembreFamille().getId())) {
                        membreTrouve = true;
                        break;
                    }
                }
                // si le membre n'est pas trouvé et s'il n'est pas de type conjoint inconnu, l'ajouter
                if (!membreTrouve
                        && !ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(membreFamDispo
                                .getIdMembreFamille())) {
                    mfToAdd.add(membreFamDispo);
                }
            }
            // Cherche les membres à supprimer
            for (JadeAbstractModel membreFamAbstract : searchModel.getSearchResults()) {
                DroitMembreFamille membreFamExistant = (DroitMembreFamille) membreFamAbstract;
                boolean membreTrouve = false;
                for (MembreFamilleVO membreFamDispo : mfFiltre) {
                    if (membreFamDispo.getIdMembreFamille().equals(membreFamExistant.getMembreFamille().getId())) {
                        membreTrouve = true;
                        break;
                    }
                }
                // si le membre de famille n'est pas dans la situation familial.
                if (!membreTrouve) {
                    // mfToDelete.add(membreFamExistant);
                }
            }
        }

        return new MembresFamillesToSynchronise(mfToAdd, mfToDelete);
    }

    private Boolean isMembreFamilleRequerant(Droit droit, MembreFamilleVO membreFamDispo) {
        Boolean isRequerant = droit.getDemande().getDossier().getDemandePrestation().getDemandePrestation()
                .getIdTiers().equals(membreFamDispo.getIdTiers());
        return isRequerant;
    }

    private Droit findDroitByIdDemandeThrowExcptionIfNotFound(String idDemande) throws JadePersistenceException,
            DroitException, JadeApplicationServiceNotAvailableException {
        Checkers.checkNotNull(idDemande, "idDemande");
        Checkers.checkNotEmpty(idDemande, "idDemande");

        DroitSearch search = new DroitSearch();
        search.setForIdDemandePc(idDemande);
        search.setDefinedSearchSize(1);
        search = PegasusServiceLocator.getDroitService().searchDroit(search);

        if (search.getSize() > 0) {
            return (Droit) search.getSearchResults()[0];
        }
        throw new RuntimeException("Any droit found wiht this idDemande: " + idDemande);
    }

    public List<SimpleDroitMembreFamille> addMembreFamilleByIdMembreFamille(List<String> forIdMembreFamilleIn,
            String idDemande) throws MembreFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DonneesPersonnellesException, DroitException {
        List<SimpleDroitMembreFamille> droitMembreFamilles = new ArrayList<SimpleDroitMembreFamille>();

        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR) && forIdMembreFamilleIn != null
                && !forIdMembreFamilleIn.isEmpty()) {
            Droit droit = findDroitByIdDemandeThrowExcptionIfNotFound(idDemande);
            List<MembreFamilleVO> membresFamilles = Arrays.asList(loadSituationFamilliale(droit));

            List<MembreFamilleVO> membresFamillesFiltred = new ArrayList<MembreFamilleVO>();
            for (MembreFamilleVO membreFamille : membresFamilles) {
                if (forIdMembreFamilleIn.contains(membreFamille.getIdMembreFamille())) {
                    membresFamillesFiltred.add(membreFamille);
                }
            }

            for (MembreFamilleVO membreFamilleVO : membresFamillesFiltred) {
                SimpleDonneesPersonnelles donneesPersonnelles = new SimpleDonneesPersonnelles();
                // donneesPersonnelles.setIsEnfant();
                donneesPersonnelles = PegasusImplServiceLocator.getSimpleDonneesPersonnellesService().create(
                        donneesPersonnelles);

                SimpleDroitMembreFamille simpleDroitMembreFamille = new SimpleDroitMembreFamille();
                simpleDroitMembreFamille.setIdDonneesPersonnelles(donneesPersonnelles.getId());

                String csRoleFamillePC = DroitServiceImpl.convertCsRoleFamillePC(
                        membreFamilleVO.getRelationAuRequerant(), false);

                if (csRoleFamillePC != null) {
                    simpleDroitMembreFamille.setCsRoleFamillePC(csRoleFamillePC);
                    simpleDroitMembreFamille.setIdDroit(droit.getId());
                    simpleDroitMembreFamille.setIdMembreFamilleSF(membreFamilleVO.getIdMembreFamille());
                    droitMembreFamilles.add(PegasusImplServiceLocator.getSimpleDroitMembreFamilleService().create(
                            simpleDroitMembreFamille));
                }
            }

        }
        return droitMembreFamilles;
    }

    private MembreFamilleVO[] loadSituationFamilliale(Droit droit) {
        String idTiersRequerant = droit.getDemande().getDossier().getDemandePrestation().getDemandePrestation()
                .getIdTiers();
        // Conversion dernier jour du mois de la date de dépot de la demande, afin d'aller rechercher tous les
        // membres de familles inclus DANS le mois
        String dateRechecheMembre = JadeDateUtil.getLastDateOfMonth(droit.getDemande().getSimpleDemande()
                .getDateDepot());

        try {
            MembreFamilleVO[] searchMembresFamilleDisponibles = HeraServiceLocator.getMembreFamilleService()
                    .searchMembresFamilleRequerantDomaineRentes(idTiersRequerant, dateRechecheMembre);

            return HeraServiceLocator.getMembreFamilleService().filtreMembreFamilleWithDate(
                    searchMembresFamilleDisponibles, dateRechecheMembre);

        } catch (MembreFamilleException e) {
            throw new RuntimeException(e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        }
    }

}
