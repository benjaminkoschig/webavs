package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.util.prestations.MotifVersementUtil;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilation;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.GeneratePerstationPeriodeDecompte;

/**
 * 
 * Le rôle de cette class est de générer toutes les opérations (écritures et ordre versement en comptabilité) liées aux
 * décision après calcule
 * 
 * @author dma
 * 
 */
class GenerateOperationsApresCalcul implements GenerateOperations {
    private Operations operations = new Operations();

    private void computControlAmount(BigDecimal amountPrestation) {
        operations.setControlAmount(operations.getControlAmount().add(amountPrestation));
    }

    @Override
    public Operations generateAllOperations(List<OrdreVersementForList> ovs, List<SectionSimpleModel> sections,
            String dateForOv, String dateEcheance) throws JadeApplicationException {
        return generateAllOperations(ovs, sections, dateForOv, dateEcheance, null);
    }

    @Override
    public Operations generateAllOperations(List<OrdreVersementForList> ovs, List<SectionSimpleModel> sections,
            String dateForOv, String dateEcheance, PrestationOvDecompte decompteIni) throws JadeApplicationException {
        operations = new Operations();
        MontantDispo montantDispo = null;
        PrestationOvDecompte decompte = null;

        ArrayList<String> listIdPca = getListIdPcaFromOvs(ovs);
        HashMap<String, SimpleVentilation> mapSimpleVentilation = getMapSimpleVentilationForListIdPca(listIdPca);

        if (!mapSimpleVentilation.isEmpty()) {
            List<OrdreVersementForList> ovsSplit = changeOvsIfPrestationVentile(mapSimpleVentilation, ovs);
            decompte = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovsSplit, decompteIni);
        } else {
            decompte = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteIni);
        }

        GenerateEcrituresResitutionBeneficiareForDecisionAc ac = gernerateEcrituresStandard(decompte);

        montantDispo = generateEcrituresCompensation(ac, decompte);

        montantDispo = generateEcrituresDettes(sections, decompte, montantDispo);

        montantDispo = generateOperationsCreanciers(decompte, montantDispo, dateForOv);

        generateOvs(montantDispo, decompte);

        generateOperationAllocationNoel(dateForOv, decompte);

        computControlAmount(decompte.getPrestationAmount());

        return operations;
    }

    private ArrayList<String> getListIdPcaFromOvs(List<OrdreVersementForList> ovs) {
        Set<String> setIdPca = new HashSet<String>();
        for (int i = 0; i < ovs.size(); i++) {
            setIdPca.add(ovs.get(i).getSimpleOrdreVersement().getIdPca());
        }

        return new ArrayList<String>(setIdPca);
    }

    protected HashMap<String, SimpleVentilation> getMapSimpleVentilationForListIdPca(ArrayList<String> listIdPca)
            throws JadeApplicationException {
        Map<String, SimpleVentilation> mapStringSimpleVentilations = new HashMap<String, SimpleVentilation>();

        SimplePCAccordeeSearch pcasearch = new SimplePCAccordeeSearch();

        for (int i = 0; i < listIdPca.size(); i++) {
            pcasearch.setForIdPCAccordee(listIdPca.get(i));
            try {
                PegasusServiceLocator.getSimplePcaccordeeService().search(pcasearch);

                if (pcasearch.getSearchResults().length != 0) {
                    SimpleVentilation simpleVentil = CorvusServiceLocator.getSimpleVentilationService()
                            .getMontantVentileFromIdPca(listIdPca.get(i));

                    if (simpleVentil != null) {
                        mapStringSimpleVentilations.put(listIdPca.get(i), simpleVentil);
                    }
                }
            } catch (JadePersistenceException e) {
                JadeLogger.error(GenerateOperationsApresCalcul.class,
                        "Problem in GenerateOperationsApresCalcul, reason : " + e.toString());

                throw new PCAccordeeException("Unable to find the PC accordee", e);
            }
        }
        return (HashMap<String, SimpleVentilation>) mapStringSimpleVentilations;
    }

    private List<OrdreVersementForList> changeOvsIfPrestationVentile(HashMap<String, SimpleVentilation> map,
            List<OrdreVersementForList> ovs) throws JadeApplicationException {
        Set<OrdreVersementForList> ovsModif = new HashSet<OrdreVersementForList>();

        for (OrdreVersementForList ordreVersementForList : ovs) {
            // Si l'id d'une PCA est la même que la map, on doit splitter pour faire la différence avec la part
            // cantonale
            if (map.containsKey(ordreVersementForList.getSimpleOrdreVersement().getIdPca())) {
                SimplePCAccordee pca = retrievePca(ordreVersementForList);
                String dateDeFin = JadeStringUtil.isBlankOrZero(pca.getDateFin()) ? ordreVersementForList.getDateFin()
                        : pca.getDateFin();
                // On récupère le nombre de mois à payer pour la prestation
                int dateDebut = Integer.parseInt(pca.getDateDebut().substring(0, 2));
                int dateFin = Integer.parseInt(dateDeFin.substring(0, 2));

                int nbMois = dateFin + 1 - dateDebut;
                // On créé un nouvel ordre de versement pour la part cantonale
                OrdreVersementForList ordreVersementForListPartCantonale;
                try {
                    ordreVersementForListPartCantonale = (OrdreVersementForList) JadePersistenceUtil
                            .clone(ordreVersementForList);
                } catch (JadeCloneModelException e) {
                    JadeLogger.error(GenerateOperationsApresCalcul.class,
                            "Problem in GenerateOperationsApresCalcul, reason : " + e.toString());

                    throw new RuntimeException(e.toString());
                }

                // On modifie le numéro de groupe période pour que le nouvel OV apparaissent dans les écritures
                int noGroupePeriode = Integer.parseInt(ordreVersementForListPartCantonale.getSimpleOrdreVersement()
                        .getNoGroupePeriode());
                ordreVersementForListPartCantonale.getSimpleOrdreVersement().setNoGroupePeriode(
                        String.valueOf(noGroupePeriode + 1));

                float montantPartCantonalePourOv = Float.parseFloat(map.get(
                        ordreVersementForList.getSimpleOrdreVersement().getIdPca()).getMontantVentile())
                        * nbMois;
                ordreVersementForListPartCantonale.getSimpleOrdreVersement().setMontant(
                        String.valueOf(montantPartCantonalePourOv));

                // Set boolean pour l'écriture plus loin
                ordreVersementForListPartCantonale.getSimpleOrdreVersement().setVentile(true);

                // on supprime la partie part cantonale de l'OV déja présent
                float montantSimpleOVPartFederale = Float.parseFloat(ordreVersementForList.getSimpleOrdreVersement()
                        .getMontant());

                ordreVersementForList.getSimpleOrdreVersement().setMontant(
                        String.valueOf(montantSimpleOVPartFederale - montantPartCantonalePourOv));

                // On ajoute les ov dans la liste qu'on va retourner
                ovsModif.add(ordreVersementForListPartCantonale);
                ovsModif.add(ordreVersementForList);
            } else {
                ovsModif.add(ordreVersementForList);
            }
        }

        return new ArrayList<OrdreVersementForList>(ovsModif);

    }

    private SimplePCAccordee retrievePca(OrdreVersementForList ordreVersementForList) throws PCAccordeeException {
        SimplePCAccordeeSearch simplePCAccordeesSearch = new SimplePCAccordeeSearch();
        simplePCAccordeesSearch.setForIdPCAccordee(ordreVersementForList.getSimpleOrdreVersement().getIdPca());
        try {
            PegasusServiceLocator.getSimplePcaccordeeService().search(simplePCAccordeesSearch);
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(GenerateOperationsApresCalcul.class,
                    "PegasusServiceLocator : simplePcAccordee service not avalable, reason : " + e.toString());
            throw new PCAccordeeException("PegasusServiceLocator : simplePcAccordee service not avalable", e);
        } catch (JadePersistenceException e) {
            JadeLogger.error(GenerateOperationsApresCalcul.class,
                    "Unable to find the PC accordee, reason : " + e.toString());
            throw new PCAccordeeException("Unable to find the PC accordee", e);
        }
        return (SimplePCAccordee) simplePCAccordeesSearch.getSearchResults()[0];
    }

    private MontantDispo generateEcrituresCompensation(GenerateEcrituresResitutionBeneficiareForDecisionAc ac,
            PrestationOvDecompte decompte) throws ComptabiliserLotException {

        GenerateEcrituresCompensationForDecisionAc generateCompensation = new GenerateEcrituresCompensationForDecisionAc(
                ac.getMapEcritures(), decompte.getIdCompteAnnexeRequerant(), decompte.getIdCompteAnnexeConjoint());

        operations.addAllEcritures(generateCompensation.generateEcritures());
        return generateCompensation.getMontantsDisponible();
    }

    private MontantDispo generateEcrituresDettes(List<SectionSimpleModel> sections, PrestationOvDecompte decompte,
            MontantDispo montantDispo) throws JadeApplicationException {
        if (decompte.getDettes().size() > 0) {
            GenerateEcrituresDette generateEcrituresDette = new GenerateEcrituresDette(decompte, montantDispo, sections);
            operations.addAllEcritures(generateEcrituresDette.getEcritures());
            // this.operations.setControlAmount(this.operations.getControlAmount().add(this.sumOv(decompte.getDettes())));
            return generateEcrituresDette.getMontantsDisponible();
        } else {
            return montantDispo;
        }
    }

    private void generateOperationAllocationNoel(String dateForOv, PrestationOvDecompte decompte)
            throws JadeApplicationException {
        if (decompte.getAllocationsNoel().size() > 0) {
            // Périodes
            final String dateFin = JadeStringUtil.isBlankOrZero(decompte.getDateFin()) ? REPmtMensuel
                    .getDateDernierPmt(BSessionUtil.getSessionFromThreadContext()) : decompte.getDateFin();
            final String periode = decompte.getDateDebut() + " - " + dateFin;

            String strDecision = MotifVersementUtil.getTranslatedLabelFromTiers(
                    decompte.getIdTiersAddressePaiementRequerant(), decompte.getIdTiersRequerant(),
                    "PEGASUS_COMPTABILISATION_DECISION_DU", BSessionUtil.getSessionFromThreadContext());
            strDecision += " " + decompte.getDateDecision();

            GenerateOperationsAllocationsNoel allocationsNoel = new GenerateOperationsAllocationsNoel();
            allocationsNoel.generateAllOperation(decompte.getAllocationsNoel(), decompte.getInfosRequerant(),
                    decompte.getInfosConjoint(), periode, strDecision);
            operations.addAllEcritures(allocationsNoel.getEcritures());
            operations.addAllOVs(allocationsNoel.getOrdreVersementCompta());
        }
    }

    private MontantDispo generateOperationsCreanciers(PrestationOvDecompte decompte, MontantDispo montantDispo,
            String dateForOv) throws JadeApplicationException {
        if (decompte.getCreanciers().size() > 0) {
            GenerateOperationsCreancier generateEcrituresCreancier = new GenerateOperationsCreancier(decompte,
                    montantDispo, new GenerateOvComptaAndGroupe());
            operations.addAllEcritures(generateEcrituresCreancier.getEcritures());
            operations.addAllOVs(generateEcrituresCreancier.getOrdresVersementCompta());
            return generateEcrituresCreancier.getMontantsDisponible();
        } else {
            return montantDispo;
        }
    }

    private void generateOvs(final MontantDispo montantDispo, final PrestationOvDecompte decompte) {
        GenerateOvBeneficiaire generateOvBeneficiaire = new GenerateOvBeneficiaire(new GenerateOvComptaAndGroupe());
        operations.addAllOVs(generateOvBeneficiaire.generateOvComptaBeneficiare(montantDispo, decompte));
    }

    private GenerateEcrituresResitutionBeneficiareForDecisionAc gernerateEcrituresStandard(PrestationOvDecompte decompte)
            throws JadeApplicationException {

        GenerateEcrituresResitutionBeneficiareForDecisionAc ac = newEcritureBasic();
        operations.addAllEcritures(ac.generateEcritures(decompte.getPrestationsPeriodes()));
        return ac;
    }

    protected GenerateEcrituresResitutionBeneficiareForDecisionAc newEcritureBasic() {
        GenerateEcrituresResitutionBeneficiareForDecisionAc ac = new GenerateEcrituresResitutionBeneficiareForDecisionAc();
        return ac;
    }
}
