package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee.TypeSeparationCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.VariableMetier;

class CalculAllocationNoel {

    private boolean isDroitInitial(Droit droit) {
        return "1".equals(droit.getSimpleVersionDroit().getNoVersion());
    }

    public void calculAndSaveAllocationDeNoel(List<PeriodePCAccordee> listePCAccordes, Droit droit,
            DonneesHorsDroitsProvider containerGlobal, String dateDebutPlageCalcul) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PCAccordeeException,
            AllocationDeNoelException, NumberFormatException, CalculException, PropertiesException {

        // Si gestion alloc noel et calcul retro, on calcul les allocations de noel et persistance
        if (PCproperties.getBoolean(EPCProperties.ALLOCATION_NOEL)) {

            if (isDroitInitial(droit) && isDemandeFirstDemande(droit, dateDebutPlageCalcul)) {

                // Recherche de la pc succeptible de générer une allocation de noel, à savoir, la pc, qui clot
                // l'année
                // précédente
                int anneeCourante = 0;
                int anneeAPrendreEnCompte = 0;
                try {
                    anneeCourante = Integer.parseInt(PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt()
                            .substring(3));
                } catch (PmtMensuelException e) {
                    new CalculException("Unable to fetDateProchainPmt", e);
                }
                anneeAPrendreEnCompte = anneeCourante - 1;

                if (hasDateDepotValide(droit, anneeAPrendreEnCompte)) {

                    // On s'assure qu l'on n'a pas déjà payer une allocation pour cette anne et demande. Couple séparé!!
                    if (!PegasusServiceLocator.getAllocationDeNoelService().hasAlreadyRecivedAllocationNoelForTheYear(
                            anneeAPrendreEnCompte, droit.getDemande().getId())) {

                        // Chargement de la priode pca, une seule période, mais potentiellement 2 pc (séparation par la
                        // maladie)
                        PeriodePCAccordee periodePCA = getPcToDealAllocationDeNoel(anneeAPrendreEnCompte,
                                listePCAccordes);
                        List<PCAccordee> pcasForAllocNoel = null;

                        boolean isCoupleSepareParMaladie = periodePCA.getTypeSeparationCC().equals(
                                TypeSeparationCC.CALCUL_SEPARE_MALADIE);

                        List<String> idsPcaToFind = new ArrayList<String>();
                        idsPcaToFind.add(periodePCA.getIdSimplePcAccordee());

                        if (!JadeStringUtil.isBlank(periodePCA.getIdSimplePcAccordeeConjoint())) {
                            idsPcaToFind.add(periodePCA.getIdSimplePcAccordeeConjoint());
                        }

                        if (periodePCA != null) {
                            pcasForAllocNoel = PegasusImplServiceLocator.getPCAccordeeService().findPcaForPeriode(
                                    idsPcaToFind);
                        }

                        for (PCAccordee pca : pcasForAllocNoel) {
                            pca.loadPlanCalculs();
                            createAllocationDeNoel(droit, containerGlobal, anneeAPrendreEnCompte, pca,
                                    isCoupleSepareParMaladie);
                        }

                    }
                }
            }
        }
    }

    private int getNombrePersonnesPourAllocationNoel(PCAccordee pca, boolean isCoupleSepareParMaladie)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (!pca.getSimplePCAccordee().getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)
                || isCoupleSepareParMaladie) {
            return 1;
        } else {
            return PegasusServiceLocator.getPCAccordeeService().countMembreFamilleForPlanRetenu(
                    pca.getPlanRetenu().getId());
        }

    }

    private void createAllocationDeNoel(Droit droit, DonneesHorsDroitsProvider containerGlobal,
            int anneeAPrendreEnCompte, PCAccordee pcaForAllocNoel, boolean isCoupleSepareParMaladie)
            throws PCAccordeeException, JadePersistenceException, JadeApplicationServiceNotAvailableException,
            AllocationDeNoelException, CalculException {
        // Si pas null, on traite le cas
        if ((pcaForAllocNoel != null) && !isPcaEnRefus(pcaForAllocNoel)) {

            // nbres membres famille
            // int nbreMembres = PegasusServiceLocator.getPCAccordeeService().countMembreFamilleForPlanRetenu(
            // pcaForAllocNoel.getPlanRetenu().getId());
            //
            // if (!pcaForAllocNoel.getSimplePCAccordee().getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
            // nbreMembres = 1;
            // }

            int nbreMembres = getNombrePersonnesPourAllocationNoel(pcaForAllocNoel, isCoupleSepareParMaladie);

            // montant allocation, tableau req, conj
            BigDecimal montantsAllocation = PegasusImplServiceLocator.getSimpleAllocationDeNoelService()
                    .computeAndGetMontantAllocation(nbreMembres, pcaForAllocNoel,
                            getMontantAllocationNoel(containerGlobal));

            // creation alloc de noel
            SimpleAllocationNoel allocationDeNoel = new SimpleAllocationNoel();
            allocationDeNoel.setIdDemande(droit.getDemande().getSimpleDemande().getIdDemande());
            allocationDeNoel.setIdPCAccordee(pcaForAllocNoel.getSimplePCAccordee().getId());

            allocationDeNoel.setMontantAllocation(montantsAllocation.toString());
            // on calcule retroactivement l'allocation pour l'annee precedante
            allocationDeNoel.setAnneeAllocation("" + (anneeAPrendreEnCompte));
            allocationDeNoel.setNbrePersonnes("" + nbreMembres);
            allocationDeNoel = PegasusImplServiceLocator.getSimpleAllocationDeNoelService().create(allocationDeNoel);
        }
    }

    private boolean hasDateDepotValide(Droit droit, int anneeAPrendreEnCompte) {
        return Integer.parseInt(droit.getDemande().getSimpleDemande().getDateDepot().substring(6)) <= anneeAPrendreEnCompte;
    }

    private boolean isDemandeFirstDemande(Droit droit, String dateDebutPlageCalcul) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PegasusImplServiceLocator.getSimpleDemandeService().isDemandeInitial(
                droit.getDemande().getSimpleDemande(), JadeDateUtil.addDays(dateDebutPlageCalcul, -1).substring(3));
    }

    private boolean isPcaEnRefus(PCAccordee pcaForAllocNoel) throws PCAccordeeException {
        return IPCValeursPlanCalcul.STATUS_REFUS.equals(pcaForAllocNoel.getPlanRetenu().getEtatPC());
    }

    private Float getMontantAllocationNoel(DonneesHorsDroitsProvider containerGlobal) throws NumberFormatException,
            CalculException {
        VariableMetier montantAllocaVariable = new VariableMetier();
        montantAllocaVariable.setCsTypeVariableMetier(IPCVariableMetier.CS_ALLOCATION_NOEL);

        Calendar cal = Calendar.getInstance();

        java.util.Date d = cal.getTime();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        List<VariableMetier> listeVarMet = containerGlobal.getListeVariablesMetiers();
        for (VariableMetier varMet : listeVarMet) {
            if (varMet.getCsTypeVariableMetier().equals(IPCVariableMetier.CS_ALLOCATION_NOEL)) {
                return Float.parseFloat(varMet.getValeurDepuisDate(month, year));
            }
        }
        return null;
    }

    private PeriodePCAccordee getPcToDealAllocationDeNoel(int anneeAPrendreEnCompte,
            List<PeriodePCAccordee> listePCAccordes) {

        // on itere sur les pc
        for (PeriodePCAccordee pca : listePCAccordes) {
            int annee = Integer.parseInt(pca.getYearValidite());

            if ((annee == anneeAPrendreEnCompte) && pca.isLastPeriodForYear()) {
                return pca;
            }
        }
        return null;
    }
}
