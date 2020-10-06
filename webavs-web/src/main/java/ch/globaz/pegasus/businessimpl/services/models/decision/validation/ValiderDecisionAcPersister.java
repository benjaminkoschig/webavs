package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.external.IntRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class ValiderDecisionAcPersister {

    private ValiderDecisionAcData data;

    public ValiderDecisionAcPersister(ValiderDecisionAcData validerDecisionAcData) {
        data = validerDecisionAcData;
    }

    private void checkIfPcaHasCompteAnnexAndAddCompteAnnexIfNotExist()
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException,
            DecisionException {
        for (PcaForDecompte pca : data.getPcasNew()) {
            if (JadeStringUtil.isBlankOrZero(pca.getSimpleInformationsComptabilite().getIdCompteAnnexe())) {

                // TODO Metre en cache.
                if (isRequerant(pca)) {
                    CompteAnnexeSimpleModel comptAnnexe = findCompteAnnexeAndCreateIfNotExist(data.getRequerant());
                    pca.getSimpleInformationsComptabilite().setIdCompteAnnexe(comptAnnexe.getIdCompteAnnexe());
                    // DOM2R
                    if (JadeStringUtil.isBlankOrZero(pca.getSimpleInformationsComptabiliteConjoint()
                            .getIdCompteAnnexe())) {
                        pca.getSimpleInformationsComptabiliteConjoint().setIdCompteAnnexe(
                                comptAnnexe.getIdCompteAnnexe());
                    }
                } else if (isConjoint(pca)) {
                    CompteAnnexeSimpleModel comptAnnexe = findCompteAnnexeAndCreateIfNotExist(data.getConjoint());
                    pca.getSimpleInformationsComptabilite().setIdCompteAnnexe(comptAnnexe.getIdCompteAnnexe());
                } else {
                    throw new DecisionException("Unable to resolve the type of csRole in the pca "
                            + pca.getSimplePrestationsAccordees().getIdPrestationAccordee());
                }
            }
        }
    }

    private PCAccordee convertToPCAccordee(PcaForDecompte pca) {
        PCAccordee newPca = new PCAccordee();
        newPca.setSimpleInformationsComptabilite(pca.getSimpleInformationsComptabilite());
        newPca.setSimpleInformationsComptabiliteConjoint(pca.getSimpleInformationsComptabiliteConjoint());
        newPca.setSimplePCAccordee(pca.getSimplePCAccordee());
        newPca.setSimplePrestationsAccordees(pca.getSimplePrestationsAccordees());
        newPca.setSimplePrestationsAccordeesConjoint(pca.getSimplePrestationsAccordeesConjoint());
        return newPca;
    }

    private void createOvs() throws OrdreVersementException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        for (SimpleOrdreVersement ov : data.getOvs()) {
            ov.setIdPrestation(data.getSimplePrestation().getIdPrestation());
            PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
        }
    }

    private void createPrestation() throws JadePersistenceException, JadeApplicationServiceNotAvailableException,
            DecisionException {
        try {
            setIdComptesAnnexe();
            data.setSimplePrestation(PegasusImplServiceLocator.getSimplePrestationService().create(
                    data.getSimplePrestation()));
        } catch (PrestationException e) {
            throw new DecisionException("Unable to create the presation", e);
        }
    }

    private CompteAnnexeSimpleModel findCompteAnnexeAndCreateIfNotExist(PersonneEtendueComplexModel personne)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        return CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(null,
                personne.getTiers().getIdTiers(), IntRole.ROLE_RENTIER,
                personne.getPersonneEtendue().getNumAvsActuel(), true);

    }

    private Map<String, String> groupByIdTierSetValueCompteAnnexe(List<PcaForDecompte> pcas,
            List<PcaForDecompte> pcaRepleaced) {
        Map<String, String> map = new HashMap<String, String>();

        List<PcaForDecompte> list = new ArrayList<PcaForDecompte>();
        list.addAll(pcas);
        list.addAll(pcaRepleaced);

        for (PcaForDecompte pca : list) {
            map.put(pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire(), pca
                    .getSimpleInformationsComptabilite().getIdCompteAnnexe());
        }

        return map;
    }

    private boolean isConjoint(PcaForDecompte pca) {
        return IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(pca.getSimplePCAccordee().getCsRoleBeneficiaire());
    }

    private boolean isDroitInitial() {
        return ValiderDecisionUtils.isDroitInitial(data.getNoVersionDroit());
    }

    private boolean isRequerant(PcaForDecompte pca) {
        return IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(pca.getSimplePCAccordee().getCsRoleBeneficiaire());
    }

    public void persist() throws JadePersistenceException, JadeApplicationException {

        checkIfPcaHasCompteAnnexAndAddCompteAnnexIfNotExist();

        createPrestation();
        createOvs();

        updateDemande();
        updatDroitNew();
        updatDroitReplaced();
        updatePcasReplaced();
        updatePcasNew();
        updatePcaCopie();
        updatePcaSupprimer();
        updateDecisionHeader();
        updateCreanciers();

    }



    private void setIdComptesAnnexe() throws DecisionException {

        Map<String, String> map = groupByIdTierSetValueCompteAnnexe(data.getPcasNew(), data.getPcasReplaced());
        if (map.size() > 2) {
            throw new DecisionException("Unable to valide the decision too many compteAnnexe was founded");
        }

        data.getSimplePrestation().setIdCompteAnnexeRequerant(
                map.remove(data.getSimplePrestation().getIdTiersBeneficiaire()));

        if (map.size() == 1) {
            data.getSimplePrestation().setIdCompteAnnexeConjoint(new ArrayList<String>(map.values()).get(0));
        }
    }

    private void updatDroitNew() throws DroitException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        PegasusImplServiceLocator.getSimpleVersionDroitService().update(data.getSimpleVersionDroitNew());
    }

    private void updatDroitReplaced() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (!isDroitInitial()) {
            PegasusImplServiceLocator.getSimpleVersionDroitService().update(data.getSimpleVersionDroitReplaced());
        }
    }

    private void updateDecisionHeader() throws JadePersistenceException, JadeApplicationServiceNotAvailableException,
            DecisionException {
        try {
            for (DecisionApresCalcul dc : data.getDecisionsApresCalcul()) {
                dc.getDecisionHeader().getSimpleDecisionHeader().setIdPrestation(data.getSimplePrestation().getId());
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().updateForValidation(
                        dc.getDecisionHeader().getSimpleDecisionHeader());
            }
        } catch (DecisionException e) {
            throw new DecisionException("Unable to udpate the simpleDecisionHeader", e);
        }
    }
    private void updateCreanciers() throws JadeApplicationServiceNotAvailableException, CreancierException, JadePersistenceException {
        try {
            for (CreanceAccordee ca : data.getCreanciers()) {
                PegasusImplServiceLocator.getSimpleCreancierService().update(ca.getSimpleCreancier());
            }
        } catch (CreancierException e) {
            throw new CreancierException("Unable to udpate the simpleDecisionHeader", e);
        }
    }

    private void updateDemande() throws DemandeException, DossierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PegasusImplServiceLocator.getSimpleDemandeService().update(data.getSimpleDemande());
    }

    private void updatePcaCopie() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        if (!isDroitInitial()) {
            for (PCAccordee pca : data.getPcasCopie()) {
                PegasusServiceLocator.getPCAccordeeService().update(pca);
            }
        }
    }

    private void updatePcas(List<PcaForDecompte> pcas) throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        for (PcaForDecompte pca : pcas) {
            if (JadeStringUtil.isBlankOrZero(pca.getSimpleInformationsComptabilite().getIdCompteAnnexe())) {
                pca.getSimpleInformationsComptabilite();
            }
            if (pca.getSimplePCAccordee().getIsDateFinForce()) {
                String date = "12."
                        + JadeDateUtil.convertDateMonthYear(pca.getSimplePCAccordee().getDateDebut()).substring(3);
                pca.getSimplePCAccordee().setDateFin(date);
                pca.getSimplePrestationsAccordees().setDateFinDroit(date);

                if (!JadeStringUtil
                        .isBlankOrZero(pca.getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee())) {
                    pca.getSimplePrestationsAccordeesConjoint().setDateFinDroit(date);
                }
            }
            PegasusServiceLocator.getPCAccordeeService().update(convertToPCAccordee(pca));
        }
    }

    private void updatePcasNew() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        updatePcas(data.getPcasNew());
    }

    private void updatePcasReplaced() throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        if (!isDroitInitial()) {
            // One modifie seulement les pca et les prestations, car dans certain cas l'infoCompta est partager dans les
            this.updatePcaWithOutUpdateInfoComptat(data.getPcasReplaced());
        }
    }

    private void updatePcaSupprimer() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        for (PCAccordee pca : data.getPcasSupprimee()) {
            this.updatePcaWithOutUpdateInfoComptat(pca);
        }
    }

    private void updatePcaWithOutUpdateInfoComptat(List<PcaForDecompte> pcas) throws PCAccordeeException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        // prestations.
        for (PcaForDecompte pca : pcas) {
            PCAccordee pcaModel = convertToPCAccordee(pca);
            this.updatePcaWithOutUpdateInfoComptat(pcaModel);
        }
    }

    private void updatePcaWithOutUpdateInfoComptat(PCAccordee pca) throws PCAccordeeException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        PegasusImplServiceLocator.getSimplePCAccordeeService().update(pca.getSimplePCAccordee());

        try {
            PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(pca.getSimplePrestationsAccordees());
            // Si conjoint (DOM2Rentes)
            if (!JadeStringUtil.isBlankOrZero(pca.getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee())) {
                PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                        pca.getSimplePrestationsAccordeesConjoint());
            }

        } catch (JadeApplicationException e) {
            throw new PCAccordeeException("Unable to update the prestationsAccordee", e);
        }
    }

}
