package ch.globaz.pegasus.businessimpl.services.models.mutation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.models.mutation.MutationAbstract;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimpleJoursAppointService;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class JoursAppointMerger {

    private SimpleJoursAppointService service;

    public JoursAppointMerger(SimpleJoursAppointService service) {
        this.service = service;
    }

    private List<SimpleJoursAppoint> findJourAppoint(List<? extends MutationAbstract> list) throws MutationException {
        List<String> idsPca = new ArrayList<String>();
        for (MutationAbstract mutation : list) {
            // on filtre les type adaptation car il ne peut pas avoir de jour d'appoint ou d'allocation de Noël sur une
            // adaptation annuel
            if (!IPCDecision.CS_TYPE_ADAPTATION_AC.equals(mutation.getCsTypeDecision())) {
                idsPca.add(mutation.getIdPcaActuel());
            }
        }
        List<SimpleJoursAppoint> joursAppoints = findJoursAppoint(idsPca);
        return joursAppoints;
    }

    private List<SimpleJoursAppoint> findJoursAppoint(List<String> idsPca) throws MutationException {
        List<SimpleJoursAppoint> list = new ArrayList<SimpleJoursAppoint>();

        try {
            list = PersistenceUtil.searchByLot(idsPca, new PersistenceUtil.SearchLotExecutor<SimpleJoursAppoint>() {
                @Override
                public JadeAbstractSearchModel execute(List<String> idsPca) throws JadeApplicationException,
                        JadePersistenceException {
                    SimpleJoursAppointSearch search = new SimpleJoursAppointSearch();
                    search.setInIdsPca(idsPca);
                    // PegasusImplServiceLocator.getSimpleJoursAppointService()
                    service.search(search);
                    return search;
                }
            }, 2500);

        } catch (JadePersistenceException e) {
            throw new MutationException("Unable to search jours appoint");
        } catch (JadeApplicationException e) {
            throw new MutationException("Unable to search jours appoint, the service is not available");
        }

        return list;
    }

    public void mergeJourAppoint(List<? extends MutationAbstract> list) throws MutationException {
        List<SimpleJoursAppoint> joursAppoints = findJourAppoint(list);
        Map<String, MutationAbstract> mapMutation = toMapKeyIdPca(list);

        this.mergeJourAppoint(joursAppoints, mapMutation);
    }

    private void mergeJourAppoint(List<SimpleJoursAppoint> joursAppoints,
            Map<String, ? extends MutationAbstract> mapMutation) throws MutationException {
        for (SimpleJoursAppoint simpleJoursAppoint : joursAppoints) {
            if (!mapMutation.containsKey(simpleJoursAppoint.getIdPCAccordee())) {
                throw new MutationException("No mutation was found for this jour d'appoint this jourAppoint: "
                        + simpleJoursAppoint.toString());
            }
            BigDecimal montant = new BigDecimal(simpleJoursAppoint.getMontantTotal());
            mapMutation.get(simpleJoursAppoint.getIdPCAccordee()).setMontantJourAppoint(montant);
        }
    }

    private Map<String, MutationAbstract> toMapKeyIdPca(List<? extends MutationAbstract> list) {
        Map<String, MutationAbstract> mapMutation = new HashMap<String, MutationAbstract>();
        for (MutationAbstract mutation : list) {
            mapMutation.put(mutation.getIdPcaActuel(), mutation);
        }
        return mapMutation;
    }

}
