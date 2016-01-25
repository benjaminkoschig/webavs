package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementTypeResolver;

/**
 * Le But de cette est de créer une liste qui vas contenir des prestions avec toute les
 * opérations(écritures,ordreVersement) qui vont être utilisé dans la comptabilité.
 * 
 * @author dma
 */
public class GeneratePrestationOperations {

    private void addNomPrenomNumAvsIdPrestation(Entry<String, List<OrdreVersementForList>> entry,
            PrestationOperations prestationEcritures) {
        for (OrdreVersementForList ov : entry.getValue()) {
            if (OrdreVersementTypeResolver.isBeneficiarePrincipal(ov.getSimpleOrdreVersement())
                    || OrdreVersementTypeResolver.isRestitution(ov.getSimpleOrdreVersement())) {
                if (ov.getIdTiersRequerant().equals(ov.getSimpleOrdreVersement().getIdTiers())) {
                    prestationEcritures.setNom(ov.getDesignationRequerant1());
                    prestationEcritures.setPrenom(ov.getDesignationRequerant2());
                    prestationEcritures.setNumAvsRequerant(ov.getNumAvs());
                    prestationEcritures.setIdPrestation(entry.getKey());
                    break;
                }
            }
        }
    }

    /**
     * @param ovs
     * @param sections
     * @param comptesAnnexes
     * @param dateForOv
     * @param operation
     * @return
     * @throws JadeApplicationException
     */
    public List<PrestationOperations> generateAllOperationsPrestations(List<OrdreVersementForList> ovs,
            List<SectionSimpleModel> sections, List<CompteAnnexeSimpleModel> comptesAnnexes, String dateForOv,
            PCLotTypeOperationFactory operation) throws JadeApplicationException {

        if (ovs == null) {
            throw new ComptabiliserLotException("Unable to generateAllPrestationsEcriturs the ovs passed is null!");
        }

        if (sections == null) {
            throw new ComptabiliserLotException("Unable to generateAllPrestationsEcriturs the model passed is null!");
        }

        if (comptesAnnexes == null) {
            throw new ComptabiliserLotException(
                    "Unable to generateAllPrestationsEcriturs the model comptesAnnexes is null!");
        }

        if (operation == null) {
            throw new IllegalArgumentException(
                    "Unable to generateAllOperationsPrestations, the opertaion enum is null!");
        }

        List<PrestationOperations> prestationsOperations = new ArrayList<PrestationOperations>();

        try {
            Map<String, List<OrdreVersementForList>> prestationsOvs = groupByPresation(ovs);
            CompteAnnexeResolver.addComptesAnnexes(comptesAnnexes);
            for (Entry<String, List<OrdreVersementForList>> entry : prestationsOvs.entrySet()) {
                prestationsOperations.add(generatePresationOperation(entry, sections, dateForOv, operation));
            }
        } finally {
            CompteAnnexeResolver.clear();
        }
        return prestationsOperations;
    }

    protected Operations generateOperations(List<OrdreVersementForList> ovs, List<SectionSimpleModel> sections,
            String dateForOv, PCLotTypeOperationFactory operation) throws JadeApplicationException {
        return operation.getTreatImplementation().generateAllOperations(ovs, sections, dateForOv);
    }

    private PrestationOperations generatePresationOperation(Entry<String, List<OrdreVersementForList>> entry,
            List<SectionSimpleModel> sections, String dateForOv, PCLotTypeOperationFactory operation)
            throws JadeApplicationException {
        Operations operations = generateOperations(entry.getValue(), sections, dateForOv, operation);
        PrestationOperations prestationEcritures = new PrestationOperations();

        addNomPrenomNumAvsIdPrestation(entry, prestationEcritures);
        prestationEcritures.setControlAmount(operations.getControlAmount());
        prestationEcritures.setEcritures(operations.getEcritures());
        prestationEcritures.setOrdresVersements(operations.getOrdresVersements());
        prestationEcritures.setMontantPresation(new BigDecimal(entry.getValue().get(0).getMontantPresation()));
        return prestationEcritures;
    }

    private Map<String, List<OrdreVersementForList>> groupByPresation(List<OrdreVersementForList> ovs) {
        return JadeListUtil.groupBy(ovs, new Key<OrdreVersementForList>() {
            @Override
            public String exec(OrdreVersementForList e) {
                return e.getSimpleOrdreVersement().getIdPrestation();
            }
        });
    }

    private BigDecimal sumPresation(List<PrestationOperations> prestations) {
        BigDecimal sum = new BigDecimal(0);
        for (PrestationOperations prestation : prestations) {
            sum = sum.add(prestation.getMontantPresation());
        }
        return sum;
    }
}
