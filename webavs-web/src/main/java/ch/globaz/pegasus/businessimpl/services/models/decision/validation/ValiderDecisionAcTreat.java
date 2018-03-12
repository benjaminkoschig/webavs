package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.prestations.IREPrestations;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCPresation;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.decompte.DecompteException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.DataForGenerateOvs;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.GenerateOrdversement;
import ch.globaz.pegasus.businessimpl.services.pca.DecomptePca;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.RetourAnnonceRepository;

public class ValiderDecisionAcTreat {

    private ValiderDecisionAcData data;

    public ValiderDecisionAcTreat(ValiderDecisionAcData data) {
        this.data = data;
    }

    private void addDateFinToPrestation(PcaForDecompte pca) {
        closePrestationsDateFin(pca.getSimplePrestationsAccordees());
        if ((pca.getSimplePrestationsAccordeesConjoint() != null)
                && !JadeStringUtil.isBlankOrZero(pca.getSimplePrestationsAccordeesConjoint().getId())) {
            closePrestationsDateFin(pca.getSimplePrestationsAccordeesConjoint());
        }
    }

    private String addMonthsToMonth(String dateMonth, int nbMonth) {
        return JadeDateUtil.addMonths("01." + dateMonth, nbMonth).substring(3);
    }

    private void changeEtatAndDateDecision() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DecisionException {
        for (DecisionApresCalcul decisionApresCalcul : data.getDecisionsApresCalcul()) {
            SimpleDecisionHeader decision = decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader();
            decision.setCsEtatDecision(IPCDecision.CS_ETAT_DECISION_VALIDE);
            decision.setValidationPar(data.getCurrentUserId());
            decision.setDateValidation(JACalendar.todayJJsMMsAAAA());
        }
    }

    private void changeEtatDemandeAndAddDateRevision() throws NumberFormatException, DecisionException {

        SimpleDemande simpleDemande = data.getSimpleDemande();

        String csEtat = null;
        // Uniquement lors de la version initial car la date pour la demande n'est pas connue
        if (isDroitInitial()) {
            simpleDemande.setDateDebut(data.getDateDebut());
            // Mise à jour de la date de la prochaine révision si elle est vide
            if (JadeStringUtil.isBlankOrZero(simpleDemande.getDateProchaineRevision())) {
                simpleDemande.setDateProchaineRevision(computeDateProchaineRevision());
            }
            // Date de fin pas vide pour la pca courante octroyé, on clot la demande en suppression
            if (!JadeStringUtil.isBlankOrZero(data.getDateFin())) {
                simpleDemande.setDateFin(data.getDateFin());
            }
        }

        csEtat = EtatDemandeResolver.resolvedEtatDemande(data.getDecisionsApresCalcul(), data.hasDemandeOnlyPcaRefus());
        simpleDemande.setCsEtatDemande(csEtat);
        if (IPCDemandes.CS_REFUSE.equals(csEtat) || IPCDemandes.CS_SUPPRIME.equals(csEtat)) {
            simpleDemande.setDateFin(data.getDateFin());
        }

        if (!JadeStringUtil.isBlankOrZero(data.getDateFinForce())) {
            simpleDemande.setDateFin(data.getDateFinForce());
        }
    }

    private void changeEtatPcaCopie() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {

        // On modifie les copie car elle ne se trouvent pas dans le select des décisions car les copies n'ont pas de
        // décisions liée
        for (PCAccordee pca : data.getPcasCopie()) {
            String idPcaParent = pca.getSimplePCAccordee().getIdPcaParent();
            if (!JadeStringUtil.isBlankOrZero(idPcaParent)) {
                pca.getSimplePCAccordee().setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
                pca.getSimplePrestationsAccordees().setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                if (pca.getSimplePrestationsAccordeesConjoint() != null) {
                    pca.getSimplePrestationsAccordeesConjoint().setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                }
            }
        }
    }

    private void changeEtatPcaNew() throws JadePersistenceException, JadeApplicationException {
        DecisionApresCalcul dc = resolvedLastDecisions();
        for (PcaForDecompte pca : data.getPcasNew()) {
            String csDecision = dc.getSimpleDecisionApresCalcul().getCsTypePreparation();
            String csEtat = resolvedEtatPca(csDecision);
            pca.getSimplePCAccordee().setCsEtatPC(csEtat);
        }
    }

    private void changeEtatPcaReplacedsToHistorisee() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PCAccordeeException {
        for (PcaForDecompte pca : data.getPcasReplaced()) {
            changeEtatPcaReplacedToHistorisee(pca);
        }
    }

    private void changeEtatPcaReplacedToHistorisee(PcaForDecompte pca) {
        pca.getSimplePCAccordee().setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_HISTORISEE);
    }

    private void changeEtatPcaSupprimee() {
        for (PCAccordee pca : data.getPcasSupprimee()) {
            pca.getSimplePCAccordee().setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_HISTORISEE);
            closePrestationsDateFin(pca.getSimplePrestationsAccordees());
            if ((pca.getSimplePrestationsAccordeesConjoint() != null)
                    && !JadeStringUtil.isBlankOrZero(pca.getSimplePrestationsAccordeesConjoint().getId())) {
                closePrestationsDateFin(pca.getSimplePrestationsAccordeesConjoint());
            }
        }
    }

    private void changeEtatPrestationsNewAndDateFin() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        for (PcaForDecompte pca : data.getPcasNew()) {
            // Les refus ne doivent pas être pris dans la recap cet pour cette raison qu l'on met une date de fin
            if (IPCValeursPlanCalcul.STATUS_REFUS.equals(pca.getEtatPC())) {
                pca.getSimplePrestationsAccordees().setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
                pca.getSimplePrestationsAccordeesConjoint().setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
                addDateFinToPrestation(pca);
            } else {
                pca.getSimplePrestationsAccordees().setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                pca.getSimplePrestationsAccordeesConjoint().setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
            }
        }
    }

    private void changeEtatVersionDroitNew() throws DecisionException {

        DecisionApresCalcul lastDecision = resolvedLastDecisions();

        // Si validation partielle droit courant valide
        if (isDecisionPrepartionCourante(lastDecision)) {
            data.getSimpleVersionDroitNew().setCsEtatDroit(IPCDroits.CS_COURANT_VALIDE);
        } else {
            // prep standard et retro (si retro, il y a eu courant avant!!!)
            data.getSimpleVersionDroitNew().setCsEtatDroit(IPCDroits.CS_VALIDE);
        }
    }

    private void changeEtatVersionDroitReplaced() {
        data.getSimpleVersionDroitReplaced().setCsEtatDroit(IPCDroits.CS_HISTORISE);
    }

    private void changePrestationReplaced(PcaForDecompte pca) {
        addDateFinToPrestation(pca);
        pca.getSimplePrestationsAccordees().setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        pca.getSimplePrestationsAccordeesConjoint().setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
    }

    private void changePrestationsReplaced() throws JadePersistenceException, JadeApplicationException {
        for (PcaForDecompte pca : data.getPcasReplaced()) {
            changePrestationReplaced(pca);
        }
    }

    /**
     * Mets à jour les retours de l'ancienne decision si modification droit est lié à des retours d'annonce
     */
    private void changeRetourAnnonce() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DecisionException {
        if (data.getSimpleVersionDroitNew().getCsMotif() != null
                && data.getSimpleVersionDroitNew().getMotif().isRetourRegistre()) {
            DecisionApresCalculSearch search = new DecisionApresCalculSearch();
            search.setForIdVersionDroit(data.getSimpleVersionDroitReplaced().getId());
            PegasusServiceLocator.getDecisionApresCalculService().search(search);
            if (search.getSize() > 0) {
                DecisionApresCalcul decision = (DecisionApresCalcul) search.getSearchResults()[0];
                new RetourAnnonceRepository().updateAnnonceByOldIdDecision(decision.getDecisionHeader().getId());
            }

        }
    }

    private void closePrestationsDateFin(SimplePrestationsAccordees simplePrestationsAccordees) {
        // On fait moins un mois pour les cas qui ont une double validation dans le même mois
        simplePrestationsAccordees
                .setDateFinDroit(addMonthsToMonth(simplePrestationsAccordees.getDateDebutDroit(), -1));
    }

    private String computeDateProchaineRevision() throws NumberFormatException {
        // (date de décision plus le nb de mois dans pegasus.revision.monthsBetween)
        return JadeDateUtil.addMonths(data.getDateDecision(), data.getNbMonthBetween()).substring(3);
        // this.addMonthsToMonth(this.data.getDateDecision(), this.data.getNbMonthBetween());
    }

    public PcaDecompte convertPcaForDecompte(PcaForDecompte pca) {
        PcaDecompte pcaDecompte = new PcaDecompte();
        pcaDecompte.setSimplePCAccordee(pca.getSimplePCAccordee());
        pcaDecompte.setMontantPCMensuelle(pca.getMontantPCMensuelle());
        pcaDecompte.setIdTiersAdressePaiement(pca.getSimpleInformationsComptabilite().getIdTiersAdressePmt());
        pcaDecompte.setIdTiersAdressePaiementConjoint(pca.getSimpleInformationsComptabiliteConjoint()
                .getIdTiersAdressePmt());
        pcaDecompte.setIdTiersBeneficiaire(pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire());
        pcaDecompte.setIdTiersConjoint(pca.getSimplePrestationsAccordeesConjoint().getIdTiersBeneficiaire());
        pcaDecompte.setIdCompteAnnexe(pca.getSimpleInformationsComptabilite().getIdCompteAnnexe());
        pcaDecompte.setIdCompteAnnexeConjoint(pca.getSimpleInformationsComptabiliteConjoint().getIdCompteAnnexe());
        pcaDecompte.setSousCodePresation(pca.getSimplePrestationsAccordees().getSousCodePrestation());
        return pcaDecompte;
    }

    private DecomptePca generateDecomptePca() throws DecompteException, JadeApplicationServiceNotAvailableException {
        List<PcaDecompte> pcasNew = generatePcaForDecompte(data.getPcasNew());

        List<PcaDecompte> pcasReplacedDecompte = generatePcaForDecompte(data.getPcasReplaced());

        DecomptePca decomptePca = PegasusImplServiceLocator.getDecompteService().generateDecomptePca(pcasNew,
                pcasReplacedDecompte, resolvedDateDernierPaiement(), data.getJoursAppointNew(),
                data.getJoursAppointReplaced());

        return decomptePca;
    }

    private List<SimpleOrdreVersement> generateOvs(DecomptePca decomptePca) throws PegasusException,
            PropertiesException {

        DataForGenerateOvs data = new DataForGenerateOvs();
        data.setPeriodesPca(decomptePca.getPeriodesPca());
        data.setAllocationsNoel(this.data.getAllocationsNoel());
        data.setCreanciers(this.data.getCreanciers());
        data.setDettes(this.data.getDettes());
        data.setUseAllocationNoel(this.data.getHasAllocationNoel());
        data.setUseJourAppoints(this.data.getUseJourAppoints());
        data.setJoursAppointNew(this.data.getJoursAppointNew());
        data.setJoursAppointReplaced(this.data.getJoursAppointReplaced());

        return GenerateOrdversement.generateOvs(data);
    }

    public List<PcaDecompte> generatePcaForDecompte(List<PcaForDecompte> pcas) {
        List<PcaDecompte> list = new ArrayList<PcaDecompte>();
        for (PcaForDecompte pca : pcas) {
            PcaDecompte pcaDecompte = convertPcaForDecompte(pca);
            list.add(pcaDecompte);
        }
        return list;
    }

    private SimplePrestation generatePrestation(DecomptePca decomptePca) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        String refPaiement = null;
        Comparator<PcaForDecompte> comparator = new Comparator<PcaForDecompte>() {
            @Override
            public int compare(PcaForDecompte o1, PcaForDecompte o2) {
                Integer date1 = Integer.parseInt(JadePersistenceUtil.parseMonthYearToSql(o1.getSimplePCAccordee()
                        .getDateDebut()));
                Integer date2 = Integer.parseInt(JadePersistenceUtil.parseMonthYearToSql(o2.getSimplePCAccordee()
                        .getDateDebut()));
                return -1 * date1.compareTo(date2);
            }
        };
        Collections.sort(data.getPcasNew(), comparator);

        for (PcaForDecompte pca : data.getPcasNew()) {
            if (refPaiement == null || JadeStringUtil.isBlankOrZero(refPaiement)) {
                refPaiement = pca.getSimplePrestationsAccordees().getReferencePmt();
            }
        }

        SimplePrestation simplePrestation = new SimplePrestation();

        simplePrestation.setRefPaiement(refPaiement);

        simplePrestation.setIdTiersBeneficiaire(decomptePca.getPeriodesPca().get(0).getPcaRequerantNew()
                .getIdTiersBeneficiaire());

        simplePrestation
                .setIdVersionDroit(decomptePca.getPeriodesPca().get(0).getPcaRequerantNew().getIdVersionDroit());

        simplePrestation.setCsEtat(IREPrestations.CS_ETAT_PRE_DEFINITIF);

        simplePrestation.setCsTypePrestation(IPCPresation.CS_TYPE_DE_PRESTATION_DECISION);

        simplePrestation.setDateDebut(decomptePca.getDateDebut());
        if (data.getDateFin() == null) {
            decomptePca.setDateFin(resolvedDateDernierPaiement());
        }

        simplePrestation.setDateFin(decomptePca.getDateFin());

        BigDecimal montantRetro = decomptePca.getMontant();
        BigDecimal sumAllocationNoel = sum(data.getAllocationsNoel());

        // Si on a des allocations de noel il faut ajouter le montant de ces allocation a la prestation
        montantRetro = sumAllocationNoel.setScale(0).add(montantRetro.setScale(0));

        simplePrestation.setMontantTotal(montantRetro.toString());

        simplePrestation.setDateDecision(data.getDateDecision());

        return simplePrestation;
    }

    private boolean isDecisionPrepartionCourante(DecisionApresCalcul lastDecision) {
        return lastDecision.getSimpleDecisionApresCalcul().getCsTypePreparation().equals(IPCDecision.CS_PREP_COURANT);
    }

    private boolean isDroitInitial() {
        return ValiderDecisionUtils.isDroitInitial(data);
    }

    private boolean isEtatDemandeModifialbe() {
        return !(IPCDemandes.CS_SUPPRIME.equals(data.getSimpleDemande().getCsEtatDemande())
                || IPCDemandes.CS_REFUSE.equals(data.getSimpleDemande().getCsEtatDemande()) || IPCDemandes.CS_TRANSFERE
                    .equals(data.getSimpleDemande().getCsEtatDemande()));
    }

    private String resolvedDateDernierPaiement() {
        return addMonthsToMonth(data.getDateProchainPaiement(), -1);
    }

    private String resolvedEtatPca(String csDecision) throws DecisionException {
        String csEtat = null;
        if (IPCDecision.CS_PREP_STANDARD.equals(csDecision)) {
            csEtat = IPCPCAccordee.CS_ETAT_PCA_VALIDE;
        } else if (IPCDecision.CS_PREP_COURANT.equals(csDecision)) {
            csEtat = IPCPCAccordee.ETAT_PCA_COURANT_VALIDE;
        } else if (IPCDecision.CS_PREP_RETRO.equals(csDecision)) {
            csEtat = IPCPCAccordee.CS_ETAT_PCA_VALIDE;
        } else {
            throw new DecisionException("Unable to resolved the state of the pca with this cs decsion: " + csDecision);
        }
        return csEtat;
    }

    private DecisionApresCalcul resolvedLastDecisions() throws DecisionException {
        return EtatDemandeResolver.resolveLastDecsion(data.getDecisionsApresCalcul());
    }

    private BigDecimal sum(List<SimpleAllocationNoel> list) {
        BigDecimal sum = new BigDecimal(0);
        for (SimpleAllocationNoel simpleAllocationNoel : list) {
            sum = sum.add(new BigDecimal(simpleAllocationNoel.getMontantAllocation()));
        }
        return sum;
    }

    private BigDecimal sumJourAppoint(List<SimpleJoursAppoint> list) {
        BigDecimal sum = new BigDecimal(0);
        for (SimpleJoursAppoint simpleJoursAppoint : list) {
            sum = sum.add(new BigDecimal(simpleJoursAppoint.getMontantTotal()));
        }
        return sum;
    }

    public ValiderDecisionAcData treat() throws DecisionException, JadePersistenceException, JadeApplicationException {

        DecomptePca decomptePca = generateDecomptePca();
        List<SimpleOrdreVersement> ovs = generateOvs(decomptePca);
        SimplePrestation simplePrestation = generatePrestation(decomptePca);

        data.setOvs(ovs);
        data.setSimplePrestation(simplePrestation);

        changeEtatPrestationsNewAndDateFin();
        changeEtatPcaNew();

        if (!isDroitInitial()) {
            changePrestationsReplaced();
            changeEtatPcaReplacedsToHistorisee();
            changeEtatVersionDroitReplaced();
        }

        changeEtatPcaCopie();
        changeEtatPcaSupprimee();

        changeEtatDemandeAndAddDateRevision();

        changeEtatVersionDroitNew();

        changeEtatAndDateDecision();

        changeRetourAnnonce();

        return data;
    }
}
