package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.CompteAnnexeResolver;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.OrdreVersement;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.PrestationOvDecompte;

/**
 * Le rôle de cette class est de générer un décompte qui contiendra toute les informations utile pour la génération des
 * écritures. Permet de regrouper les ordres de versement par type. On va aussi regrouper les ordres de versements en
 * fonction de leurs périodes est générer un objet de type presationPeriode. Cette objet vas contenir les OVs de type
 * décision et restitution.
 * 
 * @author dma
 * 
 */
public class GeneratePerstationPeriodeDecompte {

    private static void addAllocationNoel(List<OrdreVersementForList> listOv, PrestationOvDecompte decompte) {
        for (OrdreVersementForList ov : listOv) {
            if (OrdreVersementTypeResolver.isAllocationNoel(ov.getSimpleOrdreVersement())) {
                decompte.getAllocationsNoel().add(
                        GeneratePerstationPeriodeDecompte.generateOv(ov.getSimpleOrdreVersement(), decompte));
            }
        }
    }

    private static void addCreancier(List<OrdreVersementForList> listOv, PrestationOvDecompte decompte) {
        for (OrdreVersementForList ov : listOv) {
            if (OrdreVersementTypeResolver.isCreancier(ov.getSimpleOrdreVersement())) {
                decompte.getCreanciers().add(
                        GeneratePerstationPeriodeDecompte.generateOv(ov.getSimpleOrdreVersement(), decompte));
            }
        }
    }

    private static void addDettesInDecompte(List<OrdreVersementForList> listOv, PrestationOvDecompte decompte) {
        for (OrdreVersementForList ov : listOv) {
            if (OrdreVersementTypeResolver.isDette(ov.getSimpleOrdreVersement())) {
                decompte.getDettes().add(
                        GeneratePerstationPeriodeDecompte.generateOv(ov.getSimpleOrdreVersement(), decompte));
            }
        }
    }

    private static void addJoursAppoint(List<OrdreVersementForList> listOv, PrestationOvDecompte decompt) {
        for (OrdreVersementForList ov : listOv) {
            if (OrdreVersementTypeResolver.isJoursAppoint(ov.getSimpleOrdreVersement())) {
                decompt.getJoursAppoint().add(
                        GeneratePerstationPeriodeDecompte.generateOv(ov.getSimpleOrdreVersement(), decompt));
            }
        }
    }

    private static void addMontantDisponibleRequeantConjoint(List<PrestationPeriode> periodes) {
        for (PrestationPeriode periode : periodes) {
            if (periode.getConjoint() != null) {
                periode.setSumConjoint(GeneratePerstationPeriodeDecompte.comptueMontantDisponible(periode.getConjoint()));
            }
            if (periode.getRequerant() != null) {
                periode.setSumRequerant(GeneratePerstationPeriodeDecompte.comptueMontantDisponible(periode
                        .getRequerant()));
            }
        }
    }

    private static void addPresationPeriodeInDecompte(List<OrdreVersementForList> ovs, PrestationOvDecompte decompt)
            throws ComptabiliserLotException {

        List<SimpleOrdreVersement> simpleOvs = new ArrayList<SimpleOrdreVersement>();
        for (OrdreVersementForList ordreVersementForList : ovs) {
            simpleOvs.add(ordreVersementForList.getSimpleOrdreVersement());
        }

        AddAmountJourAppointToOv appointToOv = new AddAmountJourAppointToOv(simpleOvs);
        appointToOv.addAmountJourAppointOnMatchedOv();

        Map<String, List<OrdreVersementForList>> map = GeneratePerstationPeriodeDecompte.groupByNoGroupePeriode(ovs);
        List<PrestationPeriode> periodes = new ArrayList<PrestationPeriode>();
        for (Entry<String, List<OrdreVersementForList>> entry : map.entrySet()) {
            PrestationPeriode prestationPeriode = new PrestationPeriode();
            for (OrdreVersementForList ov : entry.getValue()) {
                GeneratePerstationPeriodeDecompte.fillPeriode(entry.getKey(), prestationPeriode, ov, decompt);
            }
            if (prestationPeriode.getNoGroupePeriode() != null) {
                periodes.add(prestationPeriode);
            }

        }
        GeneratePerstationPeriodeDecompte.addMontantDisponibleRequeantConjoint(periodes);
        decompt.setPrestationsPeriodes(periodes);
    }

    private static BigDecimal comptueMontantDisponible(OrdreVersementPeriode periode) {
        BigDecimal sum = new BigDecimal(0);
        if (periode.getBeneficiaire() != null) {
            sum = sum.add(periode.getBeneficiaire().getMontant());
        }
        if (periode.getRestitution() != null) {
            sum = sum.subtract(periode.getRestitution().getMontant());
        }
        return sum;
    }

    private static OrdreVersementPeriode fillOvPeriodeAndGenerateIfIsNull(OrdreVersementForList ov,
            OrdreVersementPeriode ovPeriode, String idCompteAnnexe, PrestationOvDecompte decompt) {

        if (JadeStringUtil.isBlankOrZero(idCompteAnnexe)) {
            throw new IllegalArgumentException(
                    "Unable to fillOvPeriodeAndGenerateIfIsNull, the idCompteAnnexe is null! OV: " + ov.toString());
        }

        if (ovPeriode == null) {
            ovPeriode = new OrdreVersementPeriode();
            ovPeriode.setIdCompteAnnexe(idCompteAnnexe);
        }

        OrdreVersement ovGen = GeneratePerstationPeriodeDecompte.generateOv(ov.getSimpleOrdreVersement(), decompt);
        if (OrdreVersementTypeResolver.isBeneficiarePrincipal(ov.getSimpleOrdreVersement())) {
            ovPeriode.setBeneficiaire(ovGen);
        } else if (OrdreVersementTypeResolver.isRestitution(ov.getSimpleOrdreVersement())) {
            ovPeriode.setRestitution(ovGen);
        }
        return ovPeriode;
    }

    private static void fillPeriode(String noGroupe, PrestationPeriode prestationPeriode, OrdreVersementForList ov,
            PrestationOvDecompte decompt) {
        SimpleOrdreVersement sOv = ov.getSimpleOrdreVersement();
        if (OrdreVersementTypeResolver.isRestitution(sOv) || OrdreVersementTypeResolver.isBeneficiarePrincipal(sOv)) {
            if (GeneratePerstationPeriodeDecompte.isRequerant(ov)) {
                prestationPeriode.setRequerant(GeneratePerstationPeriodeDecompte.fillOvPeriodeAndGenerateIfIsNull(ov,
                        prestationPeriode.getRequerant(), ov.getIdCompteAnnexeRequerant(), decompt));
            } else {
                prestationPeriode.setConjoint(GeneratePerstationPeriodeDecompte.fillOvPeriodeAndGenerateIfIsNull(ov,
                        prestationPeriode.getConjoint(), ov.getIdCompteAnnexeConjoint(), decompt));
            }
            prestationPeriode.setNoGroupePeriode(Integer.valueOf(noGroupe));
        }
    }

    private static OrdreVersement generateOv(SimpleOrdreVersement ov, PrestationOvDecompte decompte) {

        String montant = null;
        if (JadeStringUtil.isBlankOrZero((ov.getMontantDetteModifier()))) {
            montant = ov.getMontant();
        } else {
            montant = ov.getMontantDetteModifier();
        }

        String refPaiement = decompte.formatDecision();
        return new OrdreVersement(ov.getId(), ov.getCsType(), ov.getCsTypeDomaine(), ov.getIdSectionDetteEnCompta(),
                ov.getIdTiers(), ov.getIdTiersAdressePaiement(), ov.getIdTiersAdressePaiementConjoint(),
                ov.getIdTiersOwnerDetteCreance(), montant, ov.getSousTypeGenrePrestation(),
                ov.getIdDomaineApplication(), ov.getIdDomaineApplicationConjoint(), ov.getIdTiersConjoint(),
                refPaiement, ov.isPartCantonale());
    }

    /**
     * 
     * @param listOv
     * @param decompteInit décompte initialisé (utilisé pour mocker l'objet dans les unitTest)
     * @return
     * @throws ComptabiliserLotException
     */
    public static PrestationOvDecompte generatePersationPeriode(List<OrdreVersementForList> listOv,
            PrestationOvDecompte decompteInit) throws ComptabiliserLotException {
        PrestationOvDecompte decompte;
        if (decompteInit != null) {
            decompte = decompteInit;
        } else {
            decompte = new PrestationOvDecompte();
        }
        // A fair en premier
        GeneratePerstationPeriodeDecompte.setIdTiersAndCompteAnnexeAndInfo(listOv, decompte);

        GeneratePerstationPeriodeDecompte.addDettesInDecompte(listOv, decompte);
        GeneratePerstationPeriodeDecompte.addCreancier(listOv, decompte);

        GeneratePerstationPeriodeDecompte.addPresationPeriodeInDecompte(listOv, decompte);
        // GeneratePerstationPeriodeDecompte.addJoursAppoint(listOv, decompte);
        GeneratePerstationPeriodeDecompte.addAllocationNoel(listOv, decompte);
        GeneratePerstationPeriodeDecompte.sortByNoGroupe(decompte.getPrestationsPeriodes());

        if (listOv.size() > 0) {
            decompte.setPrestationAmount(new BigDecimal(listOv.get(0).getMontantPresation()));
        }
        return decompte;
    }

    public static PrestationOvDecompte generatePersationPeriode(List<OrdreVersementForList> listOv)
            throws ComptabiliserLotException {
        return generatePersationPeriode(listOv, null);
    }

    private static Map<String, List<OrdreVersementForList>> groupByNoGroupePeriode(List<OrdreVersementForList> ovs) {
        return JadeListUtil.groupBy(ovs, new JadeListUtil.Key<OrdreVersementForList>() {
            @Override
            public String exec(OrdreVersementForList e) {
                return e.getSimpleOrdreVersement().getNoGroupePeriode();
            }
        });
    }

    private static boolean isRequerant(OrdreVersementForList ov) {
        return ov.getIdTiersRequerant().equals(ov.getSimpleOrdreVersement().getIdTiers());
    }

    private static void setIdTiersAdressePaiementConjoint(PrestationOvDecompte decompte, String idTiersAddressPaiement,
            String idApplication) {
        if (!JadeStringUtil.isBlankOrZero(idTiersAddressPaiement)
                && JadeStringUtil.isBlankOrZero(decompte.getIdTiersAddressePaiementConjoint())) {
            decompte.setIdTiersAddressePaiementConjoint(idTiersAddressPaiement);
            decompte.setIdDomaineApplicationConjoint(idApplication);
        }
    }

    private static void setIdTiersAndCompteAnnexeAndInfo(List<OrdreVersementForList> listOv,
            PrestationOvDecompte decompte) throws ComptabiliserLotException {
        // On fait un sort pour avoir la l'ordre de versement liée à la pca la plus récente.
        GeneratePerstationPeriodeDecompte.sortByNoGroupeDesc(listOv);
        for (OrdreVersementForList ov : listOv) {
            // On test seulement sur ces deux type d'ov car l'idTiers correspond au conjoint ou au requérant
            if ((OrdreVersementTypeResolver.isBeneficiarePrincipal(ov.getSimpleOrdreVersement()) || OrdreVersementTypeResolver
                    .isRestitution(ov.getSimpleOrdreVersement()))) {
                SimpleOrdreVersement sov = ov.getSimpleOrdreVersement();

                decompte.setDateDecision(ov.getDateDecision());
                decompte.setDateDebut(ov.getDateDebut());
                decompte.setDateFin(ov.getDateFin());
                decompte.setRefPaiement(ov.getRefPaiement());

                if (GeneratePerstationPeriodeDecompte.isRequerant(ov)) {
                    if (JadeStringUtil.isBlankOrZero(decompte.getIdCompteAnnexeRequerant())) {
                        decompte.setIdTiersRequerant(ov.getIdTiersRequerant());
                        decompte.setCompteAnnexeRequerant(CompteAnnexeResolver.resolveByIdCompteAnnexe(ov
                                .getIdCompteAnnexeRequerant()));
                        decompte.setIdTiersAddressePaiementRequerant(sov.getIdTiersAdressePaiement());
                        decompte.setIdDomaineApplicationRequerant(sov.getIdDomaineApplication());
                        decompte.setNomRequerant(ov.getDesignationRequerant1());
                        decompte.setPrenomRequerant(ov.getDesignationRequerant2());
                        decompte.setNssRequerant(ov.getNumAvs());
                    }
                    if (JadeStringUtil.isBlankOrZero(decompte.getIdTiersConjoint())) {
                        decompte.setIdTiersConjoint(sov.getIdTiersConjoint());
                    }
                } else if (JadeStringUtil.isBlankOrZero(decompte.getIdCompteAnnexeConjoint())) {
                    if (!JadeStringUtil.isBlankOrZero(ov.getIdCompteAnnexeConjoint())) {
                        decompte.setCompteAnnexeConjoint(CompteAnnexeResolver.resolveByIdCompteAnnexe(ov
                                .getIdCompteAnnexeConjoint()));
                        decompte.setIdTiersConjoint(sov.getIdTiers());
                        GeneratePerstationPeriodeDecompte.setIdTiersAdressePaiementConjoint(decompte,
                                sov.getIdTiersAdressePaiement(), sov.getIdDomaineApplication());
                    }
                }
                GeneratePerstationPeriodeDecompte.setIdTiersAdressePaiementConjoint(decompte,
                        sov.getIdTiersAdressePaiementConjoint(), sov.getIdDomaineApplicationConjoint());
            }
        }
    }

    private static void sortByNoGroupe(List<PrestationPeriode> prestationsPeriodes) {
        Collections.sort(prestationsPeriodes, new Comparator<PrestationPeriode>() {
            @Override
            public int compare(PrestationPeriode o1, PrestationPeriode o2) {
                return o1.getNoGroupePeriode().compareTo(o2.getNoGroupePeriode());
            }
        });
    }

    private static void sortByNoGroupeDesc(List<OrdreVersementForList> ovs) {
        Collections.sort(ovs, new Comparator<OrdreVersementForList>() {
            @Override
            public int compare(OrdreVersementForList o1, OrdreVersementForList o2) {
                if (o2.getSimpleOrdreVersement().getNoGroupePeriode() == null) {
                    return -1;
                } else if (o2.getSimpleOrdreVersement().getNoGroupePeriode() == null) {
                    return 1;
                }
                return o2.getSimpleOrdreVersement().getNoGroupePeriode()
                        .compareTo(o1.getSimpleOrdreVersement().getNoGroupePeriode());
            }
        });
    }
}
