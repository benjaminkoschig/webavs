package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaStatus;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCC;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.services.models.annonce.communicationocc.GenerationCommunicationOCCService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.PcaPrecedante;

public class GenerationCommunicationOCCServiceImpl extends PegasusAbstractServiceImpl implements
        GenerationCommunicationOCCService {

    private boolean checkForFonctionCommunicationOCC() throws DecisionException {
        try {
            return EPCProperties.GESTION_COMMUNICATION_OCC.getBooleanValue();
        } catch (Exception e) {
            throw new DecisionException("Couldn't get property gestionCommunicationOCC in Pegasus.properties!", e);
        }
    }

    @Override
    public void genereCommunicationOCCSuppression(DecisionSuppression decisionSuppression) throws PrestationException {
        try {

            SimpleVersionDroit simpleVersionDroit = decisionSuppression.getVersionDroit().getSimpleVersionDroit();

            List<PcaForDecompte> pcas;

            if (JadeStringUtil.isBlankOrZero(decisionSuppression.getVersionDroit().getDemande().getSimpleDemande()
                    .getDateFinInitial())) {
                pcas = PcaPrecedante.findPcaCourrante(simpleVersionDroit.getIdDroit(),
                        simpleVersionDroit.getNoVersion());
            } else {
                pcas = PcaPrecedante.findPcaCourrante(simpleVersionDroit.getIdDroit(),
                        simpleVersionDroit.getNoVersion(), decisionSuppression.getVersionDroit().getDemande()
                                .getSimpleDemande().getDateFinInitial());
            }

            if (pcas.size() == 0) {
                throw new PrestationException(
                        "Impossible de trouver l'ancienne pca accordée liée à la décisions. Id décision suppression: "
                                + decisionSuppression.getId());
            }

            String idsPCa = pcas.get(0).getSimplePCAccordee().getId();
            if (!JadeStringUtil.isBlankOrZero(pcas.get(0).getSimplePCAccordee().getIdPcaParent())) {
                idsPCa = pcas.get(0).getSimplePCAccordee().getIdPcaParent();
            }

            MembreFamilleLoader loader = new MembreFamilleLoader();
            List<PlanDeCalculWitMembreFamille> membreFamilleRetenues = loader.searchMembreFamilleRetenue(idsPCa);

            if (membreFamilleRetenues.size() == 0) {
                throw new PrestationException("Impossible de trouver les membre de famille retenue dans le calcul "
                        + decisionSuppression.getId());
            }

            // Cherche d'abords le requerant
            String idDonneesPersonnellesRequerant = MembreFamilleLoader
                    .resolveIdDonneePersonnelRequerant(membreFamilleRetenues);

            for (PlanDeCalculWitMembreFamille membreFamille : membreFamilleRetenues) {
                this.genereCommunicationOCCSuppression(decisionSuppression, membreFamille,
                        idDonneesPersonnellesRequerant);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrestationException("Service not Available!", e);
        } catch (JadePersistenceException e) {
            throw new PrestationException("An error happened during persistence!", e);
        } catch (DecisionException e) {
            throw new PrestationException("Decisions error", e);
        }
    }

    private void genereCommunicationOCCSuppression(DecisionSuppression decisionSuppression,
            PlanDeCalculWitMembreFamille membreFamille, String idDonneesPersonnellesRequerant)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (!checkForFonctionCommunicationOCC()) {
            return;
        }

        SimpleCommunicationOCC model = new SimpleCommunicationOCC();

        model.setDateRapport(JadeDateUtil.getFormattedDate(new Date()));

        String dateEffet = decisionSuppression.getSimpleDecisionSuppression().getDateSuppression();

        model.setDateEffet(dateEffet);

        model.setIdVersionDroit(decisionSuppression.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit());

        model.setIdTiers(membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers()
                .getIdTiers());

        String idTiersRequerant = decisionSuppression.getVersionDroit().getDemande().getDossier()
                .getDemandePrestation().getDemandePrestation().getIdTiers();

        model.setIdTiersRequerant(idTiersRequerant);

        BSession session = BSessionUtil.getSessionFromThreadContext();

        model.setMotif(session.getCodeLibelle(decisionSuppression.getSimpleDecisionSuppression().getCsMotif()));
        // vu qu'il s'agit d'une suppression, il est dans la même catégorie qu'un refus
        model.setEtatPC(IPCValeursPlanCalcul.STATUS_REFUS);
        model.setIdLocalite(membreFamille.getSimpleDonneesPersonnelles().getIdDernierDomicileLegale());

        model.setIdDonneesPersonnelles(membreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille()
                .getIdDonneesPersonnelles());
        model.setIdDonneesPersonnellesRequerant(idDonneesPersonnellesRequerant);

        PegasusImplServiceLocator.getSimpleCommunicationOCCService().create(model);

    }

    private void saveAnonnces(List<SimpleCommunicationOCC> communications) throws DecisionException {
        for (SimpleCommunicationOCC simpleCommunicationOCC : communications) {
            try {
                PegasusImplServiceLocator.getSimpleCommunicationOCCService().create(simpleCommunicationOCC);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DecisionException("Service not Available!", e);
            } catch (JadePersistenceException e) {
                throw new DecisionException("An error happened during persistence!", e);
            }
        }
    }

    @Override
    public List<SimpleCommunicationOCC> genereCommunicationOCCValidation(List<DecisionApresCalcul> decisions,
            List<PcaForDecompte> listAnciennePca) throws DecisionException {

        if (checkForFonctionCommunicationOCC()) {

            MembreFamilleLoader membreFamilleLoader = new MembreFamilleLoader();
            Map<String, List<PlanDeCalculWitMembreFamille>> membresFamilleNouvellePca = membreFamilleLoader
                    .loadMembreFamilleByDecisionAc(decisions);

            Map<String, List<PlanDeCalculWitMembreFamille>> membresFamilleAnciennePCa = membreFamilleLoader
                    .loadMembreFamilleByPcaDecompte(listAnciennePca);

            List<SimpleCommunicationOCC> communications = generateCommunications(decisions, listAnciennePca,
                    membresFamilleNouvellePca, membresFamilleAnciennePCa);
            return communications;
        } else {
            return new ArrayList<SimpleCommunicationOCC>();
        }
    }

    List<SimpleCommunicationOCC> generateCommunications(List<DecisionApresCalcul> decisions,
            List<PcaForDecompte> listAnciennePca,
            Map<String, List<PlanDeCalculWitMembreFamille>> membresFamilleNouvellePca,
            Map<String, List<PlanDeCalculWitMembreFamille>> membresFamilleAnciennePCa) throws DecisionException {

        List<PeriodeOcc> periodesNouvelle = convertToListPeriodeOccByDecisions(decisions, membresFamilleNouvellePca);
        List<PeriodeOcc> periodesAncienne = new ArrayList<PeriodeOcc>();
        if (!decisions.get(0).getVersionDroit().isInitial()) {
            periodesAncienne = convertToListPeriodeOccByPca(listAnciennePca, membresFamilleAnciennePCa);
        }
        GenerateAnonnceForDecsionsAc generateAnonnceForDecsionsAc = new GenerateAnonnceForDecsionsAc();
        List<SimpleCommunicationOCC> communications = generateAnonnceForDecsionsAc.genereCommunicationOCCValidation(
                periodesNouvelle, periodesAncienne);

        saveAnonnces(communications);

        return communications;
    }

    private List<PeriodeOcc> convertToListPeriodeOccByPca(List<PcaForDecompte> listAnciennePca,
            Map<String, List<PlanDeCalculWitMembreFamille>> membresFamilleNouvellePca) {
        List<PeriodeOcc> periodesAncienne = new ArrayList<PeriodeOcc>();
        for (PcaForDecompte pcaD : listAnciennePca) {
            SimplePCAccordee pca = pcaD.getSimplePCAccordee();

            PcaStatus statusPca = PcaStatus.fromValue(pcaD.getEtatPC());

            List<PlanDeCalculWitMembreFamille> membresFamille = resolveMembreFamille(membresFamilleNouvellePca, pca);

            RoleMembreFamille roleMembreFamille = RoleMembreFamille.fromValue(pca.getCsRoleBeneficiaire());

            PeriodeOcc periodeOcc = new PeriodeOcc(pca.getDateDebut(), pca.getDateFin(), pca.getIdVersionDroit(),
                    statusPca, membresFamille, roleMembreFamille);

            periodesAncienne.add(periodeOcc);
        }
        return periodesAncienne;
    }

    private List<PlanDeCalculWitMembreFamille> resolveMembreFamille(
            Map<String, List<PlanDeCalculWitMembreFamille>> membresFamille, SimplePCAccordee pca) {

        String idPca = JadeStringUtil.isBlankOrZero(pca.getIdPcaParent()) ? pca.getId() : pca.getIdPcaParent();
        List<PlanDeCalculWitMembreFamille> membreFamille = membresFamille.get(idPca);

        if (membreFamille == null) {
            throw new IllegalArgumentException("Aucun membre de famille n'a pu être trouvé avec cette idPca:"
                    + pca.getId());
        }
        return membreFamille;
    }

    private List<PeriodeOcc> convertToListPeriodeOccByDecisions(List<DecisionApresCalcul> decisions,
            Map<String, List<PlanDeCalculWitMembreFamille>> membresFamilleNouvellePca) {
        List<PeriodeOcc> periodesNouvelleePca = new ArrayList<PeriodeOcc>();

        for (DecisionApresCalcul dc : decisions) {
            SimplePCAccordee pca = dc.getPcAccordee().getSimplePCAccordee();

            List<PlanDeCalculWitMembreFamille> membresFamille = resolveMembreFamille(membresFamilleNouvellePca, pca);

            PcaStatus statusPca = PcaStatus.fromValue(dc.getPlanCalcul().getEtatPC());
            String idTiersRequerant = dc.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getDemandePrestation().getIdTiers();

            RoleMembreFamille roleMembreFamille = RoleMembreFamille.fromValue(pca.getCsRoleBeneficiaire());

            PeriodeOcc periodeOcc = new PeriodeOcc(pca.getDateDebut(), pca.getDateFin(), pca.getIdVersionDroit(),
                    statusPca, dc.getSimpleDecisionApresCalcul().getIntroduction(), idTiersRequerant, membresFamille,
                    roleMembreFamille);
            periodesNouvelleePca.add(periodeOcc);
        }
        return periodesNouvelleePca;
    }

}
