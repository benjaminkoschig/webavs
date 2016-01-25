package ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCCreancier;
import ch.globaz.pegasus.business.constantes.IPCOrdresVersements;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseCompteAnnexe;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.services.pca.PeriodePca;

public class GenerateOrdversement {
    private static int RESTITUTION_CONJOINT = 3;
    private static int RESTITUTION_REQUERANT = 4;
    private static int VERSEMENT_CONJOINT = 1;
    private static int VERSEMENT_REQUEERANT = 2;

    public static void addIdPrestationInOv(List<SimpleOrdreVersement> ovs, String idPrestation) {
        for (SimpleOrdreVersement simpleOrdreVersement : ovs) {
            simpleOrdreVersement.setIdPrestation(idPrestation);
        }
    }

    private static BigDecimal computeMontant(List<SimpleOrdreVersement> ovVersements,
            List<SimpleOrdreVersement> ovRestiutions) {
        BigDecimal montantVersement = GenerateOrdversement.sumOv(ovVersements);
        BigDecimal montantResitution = GenerateOrdversement.sumOv(ovRestiutions);
        BigDecimal montant = montantVersement.subtract(montantResitution);
        return montant;
    }

    private static BigDecimal computeMontantDisponible(Map<Integer, List<SimpleOrdreVersement>> map) {
        BigDecimal montantReq = GenerateOrdversement.computeMontant(map.get(GenerateOrdversement.VERSEMENT_REQUEERANT),
                map.get(GenerateOrdversement.RESTITUTION_REQUERANT));

        BigDecimal montantConj = GenerateOrdversement
                .computeMontant(map.get(GenerateOrdversement.VERSEMENT_REQUEERANT),
                        map.get(GenerateOrdversement.RESTITUTION_REQUERANT));

        return montantReq.add(montantConj);
    }

    private static BigDecimal computeMontantOv(PcaDecompte pca, Integer nbMonth) {
        return new BigDecimal(pca.getMontantPCMensuelle()).multiply(new BigDecimal(nbMonth));
    }

    public static String determineTypeOvForCreancier(String csTypeCreance) throws OrdreVersementException {
        if (IPCCreancier.CS_TYPE_CREANCE_IMPOT.equals(csTypeCreance)) {
            return IREOrdresVersements.CS_TYPE_IMPOT_SOURCE;
        } else if (IPCCreancier.CS_TYPE_CREANCE_ASSURANCER_SOCIALE.equals(csTypeCreance)) {
            return IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE;
        } else if (IPCCreancier.CS_TYPE_CREANCE_TIERS.equals(csTypeCreance)) {
            return IREOrdresVersements.CS_TYPE_TIERS;
        } else {
            throw new OrdreVersementException("Unable to find the csType for the ordreVersement (founded:'"
                    + csTypeCreance + "')");
        }
    }

    public static String dettermineCsDomaineOv(String csTypePCA) throws OrdreVersementException {
        String csDomaine = null;
        if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(csTypePCA)) {
            csDomaine = IPCOrdresVersements.CS_DOMAINE_AI;
        } else if (IPCPCAccordee.CS_TYPE_PC_SURVIVANT.equals(csTypePCA)
                || IPCPCAccordee.CS_TYPE_PC_VIELLESSE.equals(csTypePCA)) {
            csDomaine = IPCOrdresVersements.CS_DOMAINE_AVS;
        } else {
            throw new OrdreVersementException("Unabeld to compute the domaine with this csType: " + csTypePCA);
        }
        return csDomaine;
    }

    private static SimpleOrdreVersement generateOv(PcaDecompte pca, String csTypeOv, Integer noGroupPeriode,
            Integer nbMonth) throws OrdreVersementException {
        BigDecimal montant = GenerateOrdversement.computeMontantOv(pca, nbMonth);
        String csDomaine = GenerateOrdversement.dettermineCsDomaineOv(pca.getCsTypePC());
        SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
        simpleOrdreVersement.setCsTypeDomaine(csDomaine);
        simpleOrdreVersement.setCsType(csTypeOv);
        simpleOrdreVersement.setIdTiers(pca.getIdTiersBeneficiaire());
        simpleOrdreVersement.setIdTiersAdressePaiement(pca.getIdTiersAdressePaiement());
        simpleOrdreVersement.setIdTiersAdressePaiementConjoint(pca.getIdTiersAdressePaiementConjoint());
        simpleOrdreVersement.setIdTiersConjoint(pca.getIdTiersConjoint());
        simpleOrdreVersement.setIdDomaineApplication(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        if (!JadeStringUtil.isBlankOrZero(pca.getIdTiersAdressePaiementConjoint())) {
            simpleOrdreVersement
                    .setIdDomaineApplicationConjoint(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);

        }
        simpleOrdreVersement.setIdPca(pca.getIdPCAccordee());
        simpleOrdreVersement.setNoGroupePeriode(noGroupPeriode.toString());
        simpleOrdreVersement.setMontant(montant.toString());
        simpleOrdreVersement.setSousTypeGenrePrestation(pca.getSousCodePresation());
        return simpleOrdreVersement;
    }

    private static SimpleOrdreVersement generateOvCreancier(CreanceAccordee creanceAccordee)
            throws OrdreVersementException {

        SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
        String csTypeCreance = creanceAccordee.getSimpleCreancier().getCsTypeCreance();
        simpleOrdreVersement.setCsType(GenerateOrdversement.determineTypeOvForCreancier(csTypeCreance));
        // TODO reference paiement
        simpleOrdreVersement.setIdTiers(creanceAccordee.getSimpleCreancier().getIdTiers());
        simpleOrdreVersement.setIdTiersOwnerDetteCreance(creanceAccordee.getSimpleCreancier().getIdTiersRegroupement());
        simpleOrdreVersement
                .setIdTiersAdressePaiement(creanceAccordee.getSimpleCreancier().getIdTiersAdressePaiement());
        simpleOrdreVersement.setIdDomaineApplication(creanceAccordee.getSimpleCreancier().getIdDomaineApplicatif());
        simpleOrdreVersement.setMontant(creanceAccordee.getSimpleCreanceAccordee().getMontant());
        // simpleOrdreVersement.setCsTypeDomaine(GenerateOrdversement.dettermineCsDomaineOv(creanceAccordee
        // .getSimplePCAccordee().getCsTypePC()));
        return simpleOrdreVersement;
    }

    public static List<SimpleOrdreVersement> generateOvs(DataForGenerateOvs data) throws PegasusException {

        Map<Integer, List<SimpleOrdreVersement>> map = GenerateOrdversement.generateOvsVersementAndRestiution(data
                .getPeriodesPca());

        List<SimpleOrdreVersement> list = new ArrayList<SimpleOrdreVersement>();

        List<SimpleOrdreVersement> ovsCreancier = GenerateOrdversement.generateOvsCreancier(data.getCreanciers());

        List<SimpleOrdreVersement> ovsDette = GenerateOrdversement.generateOvsDetteCompta(data.getDettes());

        List<SimpleOrdreVersement> ovsAllocationNoell = GenerateOrdversement.generateOvsAllocationNoel(
                data.getAllocationsNoel(), map, data.isUseAllocationNoel());

        List<SimpleOrdreVersement> ovsJoursAppointNew = GenerateOrdversement.generateOvsJourAppoint(
                data.getJoursAppointNew(), map, data.isUseJourAppoints());

        List<SimpleOrdreVersement> ovsJoursAppointReplaced = GenerateOrdversement.generateOvsJourAppoint(
                data.getJoursAppointReplaced(), map, data.isUseJourAppoints());

        list.addAll(ovsAllocationNoell);
        list.addAll(ovsCreancier);
        list.addAll(ovsDette);
        list.addAll(ovsJoursAppointNew);
        list.addAll(ovsJoursAppointReplaced);
        list.addAll(map.get(GenerateOrdversement.VERSEMENT_REQUEERANT));
        list.addAll(map.get(GenerateOrdversement.VERSEMENT_CONJOINT));
        list.addAll(map.get(GenerateOrdversement.RESTITUTION_REQUERANT));
        list.addAll(map.get(GenerateOrdversement.RESTITUTION_CONJOINT));

        return list;
    }

    private static List<SimpleOrdreVersement> generateOvsAllocationNoel(List<SimpleAllocationNoel> allocationsNoel,
            Map<Integer, List<SimpleOrdreVersement>> map, boolean useAllocationNoel) throws OrdreVersementException {
        List<SimpleOrdreVersement> ovsAllocationNoel = new ArrayList<SimpleOrdreVersement>();
        if (useAllocationNoel) {
            Map<String, List<SimpleOrdreVersement>> mapOv = GenerateOrdversement.groupByIdPca(map);
            ovsAllocationNoel = GenerateOrdversement.generateOvsAllocationNoel(allocationsNoel, mapOv);
        }
        return ovsAllocationNoel;
    }

    private static List<SimpleOrdreVersement> generateOvsAllocationNoel(List<SimpleAllocationNoel> allocationsNoel,
            Map<String, List<SimpleOrdreVersement>> mapOvVersement) throws OrdreVersementException {
        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();

        for (SimpleAllocationNoel simpleAllocationNoel : allocationsNoel) {
            if (mapOvVersement.containsKey(simpleAllocationNoel.getIdPCAccordee())) {
                SimpleOrdreVersement ovVersement = mapOvVersement.get(simpleAllocationNoel.getIdPCAccordee()).get(0);

                SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
                simpleOrdreVersement.setCsTypeDomaine(ovVersement.getCsTypeDomaine());
                simpleOrdreVersement.setCsType(IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL);
                simpleOrdreVersement.setIdTiers(ovVersement.getIdTiers());
                simpleOrdreVersement.setIdTiersAdressePaiement(ovVersement.getIdTiersAdressePaiement());
                simpleOrdreVersement.setIdTiersAdressePaiementConjoint(ovVersement.getIdTiersAdressePaiementConjoint());
                simpleOrdreVersement.setIdDomaineApplication(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
                simpleOrdreVersement.setIdPca(simpleAllocationNoel.getIdPCAccordee());
                simpleOrdreVersement.setNoGroupePeriode(ovVersement.getNoGroupePeriode());
                simpleOrdreVersement.setMontant(simpleAllocationNoel.getMontantAllocation());
                ovs.add(simpleOrdreVersement);
                // simpleOrdreVersement.setSousTypeGenrePrestation(simpleAllocationNoel.get);
            } else {
                throw new OrdreVersementException("No PCA was found with this id:"
                        + simpleAllocationNoel.getIdPCAccordee()
                        + " Unable to create order versment type allocationDeNoel");
            }
        }
        return ovs;
    }

    public static List<SimpleOrdreVersement> generateOvsCreancier(List<CreanceAccordee> creanciers)
            throws OrdreVersementException {
        List<SimpleOrdreVersement> list = new ArrayList<SimpleOrdreVersement>();
        // Créer les ordres de versements pour les créanciers
        for (CreanceAccordee creanceAccordee : creanciers) {
            // On créer pas d'ordre de versement si le montant est inférieur a 0;
            if (Float.valueOf(creanceAccordee.getSimpleCreanceAccordee().getMontant()) > 0) {
                SimpleOrdreVersement simpleOrdreVersement = GenerateOrdversement.generateOvCreancier(creanceAccordee);
                list.add(simpleOrdreVersement);
            }
        }
        return list;
    }

    public static List<SimpleOrdreVersement> generateOvsDetteCompta(List<DetteCompenseCompteAnnexe> dettes) {
        BigDecimal zero = new BigDecimal(0);
        List<SimpleOrdreVersement> list = new ArrayList<SimpleOrdreVersement>();
        for (DetteCompenseCompteAnnexe dette : dettes) {

            BigDecimal montantDette = new BigDecimal(JadeStringUtil.isBlankOrZero(dette.getSimpleDetteComptatCompense()
                    .getMontantModifie()) ? dette.getSimpleDetteComptatCompense().getMontant() : dette
                    .getSimpleDetteComptatCompense().getMontantModifie());
            if (montantDette.compareTo(zero) == 1) {
                SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
                simpleOrdreVersement.setCsType(IREOrdresVersements.CS_TYPE_DETTE);

                // simpleOrdreVersement.setIdTiers(dette.geti);
                SectionSimpleModel section;

                // section = CABusinessServiceLocator.getSectionService().readSection(
                // dette.getIdSectionDetteEnCompta());
                //
                // CompteAnnexeSimpleModel compteAnnexe = CABusinessServiceLocator.getCompteAnnexeService().read(
                // section.getIdCompteAnnexe());
                simpleOrdreVersement.setIdDomaineApplication(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
                simpleOrdreVersement.setIdSectionDetteEnCompta(dette.getSimpleDetteComptatCompense()
                        .getIdSectionDetteEnCompta());
                simpleOrdreVersement.setIdTiersOwnerDetteCreance(dette.getIdTiers());
                simpleOrdreVersement.setMontant(dette.getSimpleDetteComptatCompense().getMontant());
                simpleOrdreVersement.setIdDetteComptatCompense(dette.getSimpleDetteComptatCompense()
                        .getIdDetteComptatCompense());
                simpleOrdreVersement.setMontantDetteModifier(dette.getSimpleDetteComptatCompense().getMontantModifie());
                // On paye la dette ou le reste de la dette
                // Si la somme des dette dépasse la prestation on modifie le montant de la dette
                simpleOrdreVersement.setIsCompense(true);
                list.add(simpleOrdreVersement);

            }
        }
        return list;
    }

    private static List<SimpleOrdreVersement> generateOvsJourAppoint(List<SimpleJoursAppoint> joursAppoints,
            Map<Integer, List<SimpleOrdreVersement>> map, boolean useJoursAppoint) throws OrdreVersementException {
        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();
        Map<String, List<SimpleOrdreVersement>> mapOv = GenerateOrdversement.groupByIdPca(map);
        if (useJoursAppoint && (joursAppoints != null)) {
            for (SimpleJoursAppoint jourAppoint : joursAppoints) {
                if (mapOv.containsKey(jourAppoint.getIdPCAccordee())) {
                    SimpleOrdreVersement ovVersement = mapOv.get(jourAppoint.getIdPCAccordee()).get(0);

                    SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
                    simpleOrdreVersement.setCsTypeDomaine(ovVersement.getCsTypeDomaine());
                    simpleOrdreVersement.setCsType(IREOrdresVersements.CS_TYPE_JOURS_APPOINT);
                    simpleOrdreVersement.setIdTiers(ovVersement.getIdTiers());
                    simpleOrdreVersement.setIdTiersAdressePaiement(ovVersement.getIdTiersAdressePaiement());
                    simpleOrdreVersement.setIdTiersAdressePaiementConjoint(ovVersement
                            .getIdTiersAdressePaiementConjoint());
                    simpleOrdreVersement
                            .setIdDomaineApplication(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
                    simpleOrdreVersement.setIdPca(jourAppoint.getIdPCAccordee());
                    simpleOrdreVersement.setNoGroupePeriode(ovVersement.getNoGroupePeriode());
                    simpleOrdreVersement.setMontant(jourAppoint.getMontantTotal());
                    ovs.add(simpleOrdreVersement);
                } else {
                    throw new OrdreVersementException("No PCA was found with this id:" + jourAppoint.getIdPCAccordee()
                            + " Unable to create order versment type jours appoint");
                }
            }
        }
        return ovs;
    }

    public static List<SimpleOrdreVersement> generateOvsStandard(List<PeriodePca> periodesPca) throws PegasusException {

        Map<Integer, List<SimpleOrdreVersement>> map = GenerateOrdversement
                .generateOvsVersementAndRestiution(periodesPca);

        List<SimpleOrdreVersement> list = new ArrayList<SimpleOrdreVersement>();

        list.addAll(map.get(GenerateOrdversement.VERSEMENT_REQUEERANT));
        list.addAll(map.get(GenerateOrdversement.VERSEMENT_CONJOINT));
        list.addAll(map.get(GenerateOrdversement.RESTITUTION_REQUERANT));
        list.addAll(map.get(GenerateOrdversement.RESTITUTION_CONJOINT));
        GenerateOrdversement.sortByNoPeriode(list);
        return list;
    }

    private static Map<Integer, List<SimpleOrdreVersement>> generateOvsVersementAndRestiution(
            List<PeriodePca> periodesPca) throws OrdreVersementException {
        List<SimpleOrdreVersement> versementRequerants = new ArrayList<SimpleOrdreVersement>();
        List<SimpleOrdreVersement> versementConjoints = new ArrayList<SimpleOrdreVersement>();
        List<SimpleOrdreVersement> restitutionsRequerant = new ArrayList<SimpleOrdreVersement>();
        List<SimpleOrdreVersement> restitutionsConjoint = new ArrayList<SimpleOrdreVersement>();

        Integer noPeriode = new Integer(0);
        for (PeriodePca periodePca : periodesPca) {
            noPeriode = noPeriode + 1;
            if (periodePca.getPcaRequerantNew() != null) {
                versementRequerants.add(GenerateOrdversement.generateOv(periodePca.getPcaRequerantNew(),
                        IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, noPeriode, periodePca.getNbMont()));
            }
            if (periodePca.getPcaConjointNew() != null) {
                versementConjoints.add(GenerateOrdversement.generateOv(periodePca.getPcaConjointNew(),
                        IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, noPeriode, periodePca.getNbMont()));
            }
            if (periodePca.getPcaRequeranReplaced() != null) {
                restitutionsRequerant.add(GenerateOrdversement.generateOv(periodePca.getPcaRequeranReplaced(),
                        IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION, noPeriode, periodePca.getNbMont()));
            }
            if (periodePca.getPcaConjointReplaced() != null) {
                restitutionsConjoint.add(GenerateOrdversement.generateOv(periodePca.getPcaConjointReplaced(),
                        IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION, noPeriode, periodePca.getNbMont()));
            }
        }
        Map<Integer, List<SimpleOrdreVersement>> map = new HashMap<Integer, List<SimpleOrdreVersement>>();
        map.put(GenerateOrdversement.VERSEMENT_REQUEERANT, versementRequerants);
        map.put(GenerateOrdversement.VERSEMENT_CONJOINT, versementConjoints);
        map.put(GenerateOrdversement.RESTITUTION_REQUERANT, restitutionsRequerant);
        map.put(GenerateOrdversement.RESTITUTION_CONJOINT, restitutionsConjoint);
        return map;
    }

    private static Map<String, List<SimpleOrdreVersement>> groupByIdPca(List<SimpleOrdreVersement> ovs) {
        return JadeListUtil.groupBy(ovs, new JadeListUtil.Key<SimpleOrdreVersement>() {
            @Override
            public String exec(SimpleOrdreVersement e) {
                return e.getIdPca();
            }
        });
    }

    private static Map<String, List<SimpleOrdreVersement>> groupByIdPca(Map<Integer, List<SimpleOrdreVersement>> map) {
        List<SimpleOrdreVersement> ovsVersement = new ArrayList<SimpleOrdreVersement>();
        ovsVersement.addAll(map.get(GenerateOrdversement.VERSEMENT_REQUEERANT));
        ovsVersement.addAll(map.get(GenerateOrdversement.VERSEMENT_CONJOINT));
        ovsVersement.addAll(map.get(GenerateOrdversement.RESTITUTION_REQUERANT));
        ovsVersement.addAll(map.get(GenerateOrdversement.RESTITUTION_CONJOINT));
        Map<String, List<SimpleOrdreVersement>> mapOv = GenerateOrdversement.groupByIdPca(ovsVersement);
        return mapOv;
    }

    private static void sortByNoPeriode(List<SimpleOrdreVersement> list) {
        Collections.sort(list, new Comparator<SimpleOrdreVersement>() {
            @Override
            public int compare(SimpleOrdreVersement o1, SimpleOrdreVersement o2) {
                return (Integer.valueOf(o1.getNoGroupePeriode()).compareTo(Integer.valueOf(o2.getNoGroupePeriode())));
            }
        });
    }

    private static BigDecimal sumOv(List<SimpleOrdreVersement> ovs) {
        BigDecimal sum = new BigDecimal(0);
        for (SimpleOrdreVersement simpleOrdreVersement : ovs) {
            sum = sum.add(new BigDecimal(simpleOrdreVersement.getMontant()));
        }
        return sum;
    }
}
