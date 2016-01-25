package ch.globaz.pegasus.businessimpl.services.models.blocage;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.prestations.IREPrestations;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.corvus.business.exceptions.CorvusException;
import ch.globaz.corvus.business.models.blocage.SimpleEnteteBlocage;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.businessimpl.services.CorvusImplServiceLocator;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.model.SoldeCompteCourant;
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.constantes.IPCOrdresVersements;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCPresation;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.blocage.Deblocage;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class LibererBlocage {

    private static void checkMontantTotal(Deblocage deblocages) throws BlocageException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeApplicationException {

        SoldeCompteCourant compteCourant = PegasusServiceLocator.getBlocageService().determineLeCompteCouranAUtiliser(
                deblocages.getPcaBloque());

        BigDecimal sumMontantDebloque = deblocages.getSumTotatEtatEnregistre();

        BigDecimal montantAutiliser = new BigDecimal(Math.min(Float.valueOf(compteCourant.getMontant()),
                Float.valueOf(deblocages.getPcaBloque().getMontantBloque()))).abs();

        if (montantAutiliser.compareTo(sumMontantDebloque) == -1) {
            String[] p = new String[3];
            p[0] = new FWCurrency(montantAutiliser.floatValue()).toStringFormat();
            p[1] = new FWCurrency(deblocages.getPcaBloque().getMontantBloque()).toStringFormat();
            JadeThread.logError(LibererBlocage.class.getClass().getName(),
                    "pegasus.deblocage.montantTotalDeblocqueDepasse.integrity", p);
        }
    }

    private static List<SimpleOrdreVersement> createListOvDeblocage(
            Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignesDeblocage, PCAccordee pcAccordee,
            SectionSimpleModel sectionDeblocage) throws BlocageException {
        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();

        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : lignesDeblocage.entrySet()) {
            for (SimpleLigneDeblocage deblocage : entry.getValue()) {
                if (EPCEtatDeblocage.ENREGISTRE.getCsCode().equals(deblocage.getCsEtat())) {
                    SimpleOrdreVersement simpleOrdreVersement = LibererBlocage.newOrdreVersement(deblocage);
                    simpleOrdreVersement.setIdPca(pcAccordee.getId());
                    simpleOrdreVersement.setIdSection(sectionDeblocage.getIdSection());
                    if (EPCTypeDeblocage.CS_CREANCIER.equals(entry.getKey())) {
                        simpleOrdreVersement.setCsType(IREOrdresVersements.CS_TYPE_TIERS);
                        simpleOrdreVersement.setIdDomaineApplication(deblocage.getIdApplicationAdressePaiement());
                        simpleOrdreVersement.setIdTiers(deblocage.getIdTiersCreancier());
                        simpleOrdreVersement.setIdTiersAdressePaiement(deblocage.getIdTiersAdressePaiement());
                    } else if (EPCTypeDeblocage.CS_DETTE_EN_COMPTA.equals(entry.getKey())) {
                        simpleOrdreVersement.setCsType(IREOrdresVersements.CS_TYPE_DETTE);
                        simpleOrdreVersement.setIdSectionDetteEnCompta(deblocage.getIdSectionDetteEnCompta());
                        simpleOrdreVersement.setIdTiersOwnerDetteCreance(pcAccordee.getSimplePrestationsAccordees()
                                .getIdTiersBeneficiaire());
                        // simpleOrdreVersement.setIdRoleCA(deblocage.getIdRoleDetteEnCompta());
                        simpleOrdreVersement.setMontantDetteModifier(deblocage.getMontant());
                        simpleOrdreVersement.setIsCompense(true);
                    } else if (EPCTypeDeblocage.CS_VERSEMENT_BENEFICIAIRE.equals(entry.getKey())) {
                        simpleOrdreVersement.setCsType(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);
                        simpleOrdreVersement.setCsTypeDomaine(LibererBlocage.dettermineCsDomaineOv(pcAccordee
                                .getSimplePCAccordee().getCsTypePC()));
                        simpleOrdreVersement.setIdTiers(pcAccordee.getSimplePrestationsAccordees()
                                .getIdTiersBeneficiaire());
                        simpleOrdreVersement.setIdDomaineApplication(deblocage.getIdApplicationAdressePaiement());
                        simpleOrdreVersement.setIdTiersAdressePaiement(deblocage.getIdTiersAdressePaiement());
                        // la meme pour req et conj pour les DOM2R
                        simpleOrdreVersement.setSousTypeGenrePrestation(pcAccordee.getSimplePrestationsAccordees()
                                .getSousCodePrestation());
                    } else {
                        throw new BlocageException("The type of deblocage is not know :" + entry.getKey().getCsCode());
                    }
                    ovs.add(simpleOrdreVersement);
                }
            }
        }
        return ovs;
    }

    private static SimplePrestation createSimplePrestation(PcaBloque pca, String montanPresation, String idLot,
            CompteAnnexeSimpleModel compteAnnexe) throws JadePersistenceException, JadeApplicationException {

        SimplePrestation simplePrestation = new SimplePrestation();
        simplePrestation.setIdVersionDroit(pca.getIdVersionDroit());
        simplePrestation.setCsEtat(IREPrestations.CS_ETAT_PRE_DEFINITIF);
        simplePrestation.setIdLot(idLot);
        simplePrestation.setMontantTotal(montanPresation);
        simplePrestation.setIdCompteAnnexeRequerant(compteAnnexe.getIdCompteAnnexe());
        // simplePrestation.setIdCompteAnnexeConjoint("");
        simplePrestation.setIdTiersBeneficiaire(pca.getIdTiersBeneficiaire());
        simplePrestation.setCsTypePrestation(IPCPresation.CS_TYPE_DE_PRESTATION_DEBLOCAGE);
        simplePrestation.setDateDebut(pca.getDateDebutPca());

        try {
            PegasusImplServiceLocator.getSimplePrestationService().create(simplePrestation);
        } catch (PrestationException e) {
            throw new DecisionException("Unable to create the presation", e);
        }
        return simplePrestation;
    }

    private static String dettermineCsDomaineOv(String csTypePCA) throws BlocageException {
        String csDomaine = null;
        if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(csTypePCA)) {
            csDomaine = IPCOrdresVersements.CS_DOMAINE_AI;
        } else if (IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(csTypePCA)
                || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(csTypePCA)) {
            csDomaine = IPCOrdresVersements.CS_DOMAINE_AVS;
        } else {
            throw new BlocageException("Unabeld to compute the domaine with this csType: " + csTypePCA);
        }
        return csDomaine;
    }

    private static List<SimpleLigneDeblocage> getLigneEtatEnregistre(
            Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignesDeblocage) {
        List<SimpleLigneDeblocage> list = new ArrayList<SimpleLigneDeblocage>();
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : lignesDeblocage.entrySet()) {
            for (SimpleLigneDeblocage deblocage : entry.getValue()) {
                if (EPCEtatDeblocage.ENREGISTRE.getCsCode().equals(deblocage.getCsEtat())) {
                    list.add(deblocage);
                }
            }
        }
        return list;
    }

    public static void libererBlocage(Deblocage deblocage) throws BlocageException, DecisionException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {

        LibererBlocage.checkMontantTotal(deblocage);
        if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            if (deblocage.getSectionDeblocage().size() == 0) {
                throw new BlocageException("Any one section blocage found for this pcaBloque:"
                        + deblocage.getPcaBloque().toString());
            }

            SimpleLot lot = PegasusServiceLocator.getLotService().findCurrentDeblocageLotOrCreate();
            SimplePrestation simplePrestation = LibererBlocage.persistePresationAndOv(deblocage, lot.getIdLot());

            if (simplePrestation != null) {

                LibererBlocage.updateEtatLigneBlocageToValiderAndAddIdPresation(deblocage.getLingesDeblocages(),
                        simplePrestation.getIdPrestation());

                LibererBlocage.updateMontantDebloque(deblocage.getPcaBloque().getIdEnteteBlocage(),
                        deblocage.getSumTotatEtatEnregistre());
            }
        }
    }

    private static SimpleOrdreVersement newOrdreVersement(SimpleLigneDeblocage deblocage) {
        SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
        simpleOrdreVersement.setIdPrestation(null);
        simpleOrdreVersement.setCsTypeDomaine(null);
        simpleOrdreVersement.setCsType(deblocage.getCsTypeDeblocage());
        simpleOrdreVersement.setIdDomaineApplication(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        simpleOrdreVersement.setMontant(deblocage.getMontant());
        return simpleOrdreVersement;
    }

    private static SimplePrestation persistePresationAndOv(Deblocage deblocage, String idLot)
            throws JadePersistenceException, JadeApplicationException {

        SectionSimpleModel sectionDeblocage = deblocage.getSectionDeblocage().get(0);

        List<SimpleOrdreVersement> ovs = LibererBlocage.createListOvDeblocage(deblocage.getLingesDeblocages(),
                deblocage.getPca(), sectionDeblocage);

        if (ovs.size() > 0) {
            BigDecimal montant = deblocage.getSumTotatEtatEnregistre();

            SimplePrestation simplePrestation = LibererBlocage.createSimplePrestation(deblocage.getPcaBloque(),
                    montant.toString(), idLot, deblocage.getCompteAnnexe());
            for (SimpleOrdreVersement ov : ovs) {
                try {
                    ov.setIdPrestation(simplePrestation.getIdPrestation());
                    PegasusImplServiceLocator.getSimpleOrdreVersementService().create(ov);
                } catch (OrdreVersementException e) {
                    throw new BlocageException("Unable to create ordreVersement", e);
                }
            }
            return simplePrestation;
        } else {
            JadeThread.logError(LibererBlocage.class.getClass().getName(),
                    "pegasus.deblocage.liberation.aucuneLigneTrouve");
            return null;
        }
    }

    private static void updateEtatLigneBlocageToValiderAndAddIdPresation(
            Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lignesDeblocage, String idPrestation)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        for (Entry<EPCTypeDeblocage, List<SimpleLigneDeblocage>> entry : lignesDeblocage.entrySet()) {
            for (SimpleLigneDeblocage deblocage : entry.getValue()) {
                if (EPCEtatDeblocage.ENREGISTRE.getCsCode().equals(deblocage.getCsEtat())) {
                    deblocage.setCsEtat(EPCEtatDeblocage.VALIDE.getCsCode());
                    deblocage.setIdPrestation(idPrestation);
                    PegasusImplServiceLocator.getSimpleLigneDeblocageService().update(deblocage);
                }
            }
        }
    }

    private static void updateMontantDebloque(String idEnteteBlocage, BigDecimal montantDebloque)
            throws CorvusException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleEnteteBlocage model = CorvusImplServiceLocator.getSimpleEnteteBlocageService().read(idEnteteBlocage);
        model.setMontantDebloque(new BigDecimal(model.getMontantDebloque()).add(montantDebloque).toString());
        CorvusImplServiceLocator.getSimpleEnteteBlocageService().update(model);
    }

}
