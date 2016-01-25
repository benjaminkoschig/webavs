package ch.globaz.pegasus.process.adaptation.stepAdaptationRente;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import ch.globaz.pegasus.business.constantes.EPCRenteAdaptation;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemande;
import ch.globaz.pegasus.business.models.process.adaptation.RenteToUpdate;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleRenteAdaptation;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.rente.ApiUtil;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import ch.globaz.pegasus.process.adaptation.PCProcessDroitUpdateAbsract;

public class PCProcessAdaptationEntityHandler extends PCProcessDroitUpdateAbsract {

    private Map<String, List<RenteAdapationDemande>> rentes;
    private Map<String, RenteToUpdate> renteToUpdate;

    public PCProcessAdaptationEntityHandler(Map<String, List<RenteAdapationDemande>> mapRente,
            Map<String, RenteToUpdate> renteToUpdate) {
        rentes = mapRente;
        this.renteToUpdate = renteToUpdate;
    }

    private void adaptationDesRentes(Droit droitACalculer, List<RenteAdapationDemande> rentes)
            throws JadePersistenceException, DroitException, JadeApplicationServiceNotAvailableException,
            JadeCloneModelException, RenteAvsAiException, DonneeFinanciereException, AdaptationException,
            AllocationImpotentException {

        Map<String, RenteAdapationDemande> mapRenteAvs = new HashMap<String, RenteAdapationDemande>();
        Map<String, RenteAdapationDemande> mapRenteNewAvs = new HashMap<String, RenteAdapationDemande>();
        Map<String, RenteAdapationDemande> mapRenteApi = new HashMap<String, RenteAdapationDemande>();
        Map<String, RenteAdapationDemande> mapRenteNewApi = new HashMap<String, RenteAdapationDemande>();
        for (RenteAdapationDemande rente : rentes) {

            // On ne fait pas d'adaptation pour les type sans rente
            if (!IPCRenteAvsAi.CS_TYPE_SANS_RENTE.equals(rente.getSimpleRenteAdaptation().getGenre())) {

                if (EPCRenteAdaptation.WAIT.equals(rente.getSimpleRenteAdaptation().getEtat())) {
                    throw new AdaptationException("La rente (" + rente.getSimpleDemandeCentrale().getNss()
                            + ") n'a pas été traité durrant l'étape précendente");
                }

                if (IPCDroits.CS_RENTE_AVS_AI.equals(rente.getSimpleRenteAdaptation().getCsTypdDonneeFinacire())) {
                    // on peut avoir 2 rentes
                    // if (mapRenteAvs.containsKey(rente.getSimpleDemandeCentrale().getReferenceInterne())) {
                    // throw new AdaptationException("An annuity type avs_ai has already been found for this nss: "
                    // + rente.getSimpleDemandeCentrale().getNss());
                    // }

                    if (EPCRenteAdaptation.SYNCHRONIZE.equals(rente.getSimpleRenteAdaptation().getEtat())) {
                        // mapRenteAvs.put(rente.getSimpleDemandeCentrale().getReferenceInterne(), rente);
                        if (renteToUpdate
                                .containsKey(rente.getSimpleRenteAdaptation().getIdDonneeFinanciereHeaderOld())) {
                            updateAvsAi(droitACalculer, rente, renteToUpdate.get(rente.getSimpleRenteAdaptation()
                                    .getIdDonneeFinanciereHeaderOld()), false);
                            mapRenteAvs.put(rente.getSimpleDemandeCentrale().getReferenceInterne(), rente);
                        } else {
                            throw new AdaptationException(
                                    "Aucune rente à adapter avec ce numero de donnée financière: "
                                            + rente.getSimpleRenteAdaptation().getIdDonneeFinanciereHeaderOld()
                                            + " a été trouvé");
                        }
                    } else if (EPCRenteAdaptation.NEW.equals(rente.getSimpleRenteAdaptation().getEtat())) {
                        mapRenteNewAvs.put(rente.getSimpleDemandeCentrale().getReferenceInterne(), rente);
                    } else if (EPCRenteAdaptation.RENTE_CHANGE.equals(rente.getSimpleRenteAdaptation().getEtat())) {
                        updateAvsAi(droitACalculer, rente,
                                renteToUpdate.get(rente.getSimpleRenteAdaptation().getIdDonneeFinanciereHeaderOld()),
                                true);
                    } else if (EPCRenteAdaptation.NOT_RETURN.equals(rente.getSimpleRenteAdaptation().getEtat())) {
                        // on fait rien pour le moment car on a mit un avertissement dans l'étape d'avant
                    } else {
                        throw new AdaptationException("Type du traitement non pris en charge ("
                                + rente.getSimpleRenteAdaptation().getEtat().toString() + ")");
                    }

                } else if (IPCDroits.CS_RENTE_API.equals(rente.getSimpleRenteAdaptation().getCsTypdDonneeFinacire())) {

                    if (EPCRenteAdaptation.SYNCHRONIZE.equals(rente.getSimpleRenteAdaptation().getEtat())) {

                        if (renteToUpdate
                                .containsKey(rente.getSimpleRenteAdaptation().getIdDonneeFinanciereHeaderOld())) {
                            updateRenteAPI(droitACalculer, rente, renteToUpdate.get(rente.getSimpleRenteAdaptation()
                                    .getIdDonneeFinanciereHeaderOld()), false);
                            mapRenteApi.put(rente.getSimpleDemandeCentrale().getReferenceInterne(), rente);
                        }
                    } else if (EPCRenteAdaptation.NEW.equals(rente.getSimpleRenteAdaptation().getEtat())) {
                        mapRenteNewApi.put(rente.getSimpleDemandeCentrale().getReferenceInterne(), rente);
                    } else if (EPCRenteAdaptation.RENTE_CHANGE.equals(rente.getSimpleRenteAdaptation().getEtat())) {
                        updateRenteAPI(droitACalculer, rente,
                                renteToUpdate.get(rente.getSimpleRenteAdaptation().getIdDonneeFinanciereHeaderOld()),
                                true);
                    } else if (EPCRenteAdaptation.NOT_RETURN.equals(rente.getSimpleRenteAdaptation().getEtat())) {
                        // on fait rien pour le moment car on a mit un avertissement dans l'étape d'avant
                    } else {
                        throw new AdaptationException("Type du traitement non pris en charge ("
                                + rente.getSimpleRenteAdaptation().getEtat().toString() + ")");
                    }

                } else {
                    throw new AdaptationException("Type de donnée financière non pris en charge ("
                            + rente.getSimpleRenteAdaptation().getCsTypdDonneeFinacire() + ")");
                }

            }
        }

        if (mapRenteNewAvs.size() > 0) {
            createNewRentesAvs(mapRenteNewAvs, droitACalculer);
        }

        if (mapRenteNewApi.size() > 0) {
            creanteNewAPI(mapRenteNewApi, droitACalculer);
        }
    }

    private void checkAdaptationRente(String csTypeRente, RenteAdapationDemande renteAdapation, String messageTypeRente)
            throws AdaptationException {

        if (renteAdapation == null) {
            throw new AdaptationException("Aucune demande de rente " + messageTypeRente
                    + " trouvée pour ce type de rente: "
                    + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csTypeRente));
        }

        // if (!renteAdapation.getSimpleRenteAdaptation().getAncienMontant().equals(montant)) {
        // throw new AdaptationException("L'ancien montant de la rente  " + messageTypeRente
        // + " ne corresponds pas avec l'ancien montant envoyé à la centrale. Type de rente: "
        // + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(csTypeRente));
        // }

    }

    private void checkNssEquals(Entry<String, RenteAdapationDemande> entry, DroitMembreFamille droitMembreFamille)
            throws AdaptationException {
        String nssAdaptation = entry.getValue().getSimpleDemandeCentrale().getNss();
        String nssDroitMenbrefamille = droitMembreFamille.getMembreFamille().getPersonneEtendue().getPersonneEtendue()
                .getNumAvsActuel();
        this.checkNssEquals(nssAdaptation, nssDroitMenbrefamille);
    }

    private void checkNssEquals(String nssAdaptation, String nssDroitMenbrefamille) throws AdaptationException {

        if (!nssAdaptation.equals(nssDroitMenbrefamille)) {
            throw new AdaptationException("Le nss(" + nssAdaptation
                    + ") trouvé dans l'adaptation ne corresponds pas au NSS(" + nssDroitMenbrefamille
                    + ") trouvé pour augmenter la rente avec le numero d'idDroitMembreFamille(" + nssDroitMenbrefamille
                    + ")");
        }
    }

    private void creanteNewAPI(Map<String, RenteAdapationDemande> mapRenteNewApi, Droit droitACalculer)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            AllocationImpotentException, DonneeFinanciereException, AdaptationException {
        for (Entry<String, RenteAdapationDemande> entry : mapRenteNewApi.entrySet()) {
            RenteAdapationDemande rente = entry.getValue();
            AllocationImpotent API = new AllocationImpotent();
            DroitMembreFamille droitMembreFamille = PegasusServiceLocator.getDroitService().readDroitMembreFamille(
                    entry.getValue().getSimpleDemandeCentrale().getReferenceInterne());

            this.checkNssEquals(entry, droitMembreFamille);

            fillRenteAPI(rente.getSimpleRenteAdaptation(), API);

            PegasusServiceLocator.getRenteIjApiService().createAllocationImpotent(
                    droitACalculer.getSimpleVersionDroit(), droitMembreFamille, API);

        }
    }

    private void createNewRentesAvs(Map<String, RenteAdapationDemande> mapRenteNewAvs, Droit droit)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RenteAvsAiException, DonneeFinanciereException, AdaptationException {
        SimpleRenteAdaptation rente = null;
        for (Entry<String, RenteAdapationDemande> entry : mapRenteNewAvs.entrySet()) {
            RenteAvsAi renteAvsAi = new RenteAvsAi();
            rente = entry.getValue().getSimpleRenteAdaptation();

            DroitMembreFamille droitMembreFamille = PegasusServiceLocator.getDroitService().readDroitMembreFamille(
                    entry.getValue().getSimpleDemandeCentrale().getReferenceInterne());

            this.checkNssEquals(entry, droitMembreFamille);

            fillRenteAvs(rente, renteAvsAi);

            PegasusServiceLocator.getRenteIjApiService().createRenteAvsAi(droit.getSimpleVersionDroit(),
                    droitMembreFamille, renteAvsAi);
        }
    }

    private void fillRenteAPI(SimpleRenteAdaptation rente, AllocationImpotent api)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, AdaptationException {
        api.getSimpleDonneeFinanciereHeader().setDateDebut(properties.get(PCProcessAdapationEnum.DATE_ADAPTATION));
        api.getSimpleAllocationImpotent().setCsTypeRente(rente.getGenre());
        // API.getSimpleAllocationImpotent().setCsDegre(csDegre)
        api.getSimpleAllocationImpotent().setMontant(rente.getNouveauMontant());
        String avsOrAi = ApiUtil.getTypeRenteAvsOrAiByIdCode(rente.getGenre());
        if (ApiUtil.AI.equals(avsOrAi)) {
            api.getSimpleAllocationImpotent().setCsGenre("64015002");
        } else if (ApiUtil.AVS.equals(avsOrAi)) {
            api.getSimpleAllocationImpotent().setCsGenre("64015001");
        } else {
            throw new AdaptationException("Unable to match the code AVS/AI with this type of genre API: "
                    + rente.getGenre());
        }
    }

    private void fillRenteAvs(SimpleRenteAdaptation rente, RenteAvsAi renteAvsAi)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {

        renteAvsAi.getSimpleDonneeFinanciereHeader().setDateDebut(
                properties.get(PCProcessAdapationEnum.DATE_ADAPTATION));
        renteAvsAi.getSimpleRenteAvsAi().setCsTypeRente(rente.getGenre());
        renteAvsAi.getSimpleRenteAvsAi().setDegreInvalidite(rente.getDegreInvalidite());
        renteAvsAi.getSimpleRenteAvsAi().setMontant(rente.getNouveauMontant());
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        if ("true".equalsIgnoreCase(properties.get(PCProcessAdapationEnum.HAS_ADAPTATION_DES_RENTE))) {
            List<RenteAdapationDemande> rentesAnnonce = rentes.get(entity.getIdRef());

            if (rentesAnnonce == null) {
                throw new AdaptationException("Aucune demande de rente trouvé pour cette personne");
            }

            fillDroitToUpdate();
            rentes = null;

            try {
                adaptationDesRentes(droitACalculer, rentesAnnonce);
            } catch (JadeCloneModelException e) {
                JadeProcessCommonUtils.addError(e);
            }
        }
    }

    private void updateAvsAi(Droit droitACalculer, RenteAdapationDemande renteAnnonce, RenteToUpdate renteToUpdate,
            boolean forceClose) throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException, JadeCloneModelException, RenteAvsAiException,
            DonneeFinanciereException, AdaptationException {

        this.checkNssEquals(renteAnnonce.getSimpleDemandeCentrale().getNss(), renteToUpdate.getNss());

        RenteAvsAi oldRenteAvsAi = new RenteAvsAi();
        oldRenteAvsAi.setSimpleDonneeFinanciereHeader(renteToUpdate.getSimpleDonneeFinanciereHeader());
        oldRenteAvsAi.setSimpleRenteAvsAi(renteToUpdate.getSimpleRenteAvsAi());

        RenteAvsAi newRenteAvsAi = (RenteAvsAi) JadePersistenceUtil.clone(oldRenteAvsAi);

        checkAdaptationRente(oldRenteAvsAi.getSimpleRenteAvsAi().getCsTypeRente(), renteAnnonce, "AVS/AI");

        fillRenteAvs(renteAnnonce.getSimpleRenteAdaptation(), newRenteAvsAi);
        if (forceClose) {
            newRenteAvsAi.getSimpleDonneeFinanciereHeader().setDateDebut(
                    oldRenteAvsAi.getSimpleDonneeFinanciereHeader().getDateDebut());
            String dateFin = JadeDateUtil.addMonths("01." + properties.get(PCProcessAdapationEnum.DATE_ADAPTATION), -1)
                    .substring(3);

            newRenteAvsAi.getSimpleDonneeFinanciereHeader().setDateFin(dateFin);
        }
        PegasusServiceLocator.getDroitService().createAndCloseRenteAvsAi(droitACalculer, newRenteAvsAi, oldRenteAvsAi,
                forceClose);

    }

    private void updateRenteAPI(Droit droitACalculer, RenteAdapationDemande renteAnnonce, RenteToUpdate renteToUpdate,
            boolean forceClose) throws JadeApplicationServiceNotAvailableException, DroitException,
            DonneeFinanciereException, JadePersistenceException, JadeCloneModelException, AdaptationException {

        this.checkNssEquals(renteAnnonce.getSimpleDemandeCentrale().getNss(), renteToUpdate.getNss());

        AllocationImpotent oldApi = new AllocationImpotent();
        oldApi.setSimpleDonneeFinanciereHeader(renteToUpdate.getSimpleDonneeFinanciereHeader());
        oldApi.setSimpleAllocationImpotent(renteToUpdate.getSimpleAllocationImpotent());

        AllocationImpotent newApi = (AllocationImpotent) JadePersistenceUtil.clone(oldApi);

        checkAdaptationRente(oldApi.getSimpleAllocationImpotent().getCsTypeRente(), renteAnnonce, "API");

        newApi.getSimpleDonneeFinanciereHeader().setDateDebut(properties.get(PCProcessAdapationEnum.DATE_ADAPTATION));

        newApi.getSimpleAllocationImpotent().setMontant(renteAnnonce.getSimpleRenteAdaptation().getNouveauMontant());

        if (forceClose) {
            newApi.getSimpleDonneeFinanciereHeader()
                    .setDateDebut(oldApi.getSimpleDonneeFinanciereHeader().getDateFin());
            String dateFin = JadeDateUtil.addMonths("01." + properties.get(PCProcessAdapationEnum.DATE_ADAPTATION), -1)
                    .substring(3);
            newApi.getSimpleDonneeFinanciereHeader().setDateFin(dateFin);

            // newApi.getSimpleDonneeFinanciereHeader().setDateFin(
            // PegasusDateUtil.addMonths(this.properties.get(PCProcessAdapationEnum.DATE_ADAPTATION), -1));
        }

        PegasusServiceLocator.getDroitService().createAndCloseAllocationImpotent(droitACalculer, newApi, oldApi,
                forceClose);

    }
}
