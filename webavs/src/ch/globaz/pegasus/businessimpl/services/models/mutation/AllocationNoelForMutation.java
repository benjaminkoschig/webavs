package ch.globaz.pegasus.businessimpl.services.models.mutation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoelSearch;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimpleAllocationDeNoelService;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class AllocationNoelForMutation {
    private SimpleAllocationDeNoelService service;

    public AllocationNoelForMutation(SimpleAllocationDeNoelService service) {
        this.service = service;
    }

    private List<SimpleAllocationNoel> findAllocationNoelRetro(List<String> idsPca) throws JadePersistenceException,
            MutationException {
        try {
            return PersistenceUtil.searchByLot(idsPca, new PersistenceUtil.SearchLotExecutor<SimpleAllocationNoel>() {
                @Override
                public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                        JadePersistenceException {
                    SimpleAllocationNoelSearch search = new SimpleAllocationNoelSearch();
                    search.setInIdsPcAccordee(ids);
                    search = service.search(search);
                    return search;
                }
            }, 2000);
        } catch (JadeApplicationException e) {
            throw new MutationException("Unable to search allocation de noel with this ids: "
                    + StringUtils.join(idsPca, ","));
        }
    }

    /**
     * Permet de retrouver les allocations de Noël liée ids pcas donnée en paramétrer. Il existe deux sorte
     * d'allocations: Une sorte purement rétro active et l'autre que l'on paye à la fin de l'année pour l'instant on ne
     * traite pas celle de la fin de l'année
     * 
     * @param idsPca
     * @return Un objet avec les 2 sortes d'allocations
     * @throws JadePersistenceException
     * @throws MutationException
     */
    public AlloctionNoelGrouped findAllocationRetro(List<String> idsPca) throws JadePersistenceException,
            MutationException {
        List<SimpleAllocationNoel> list = findAllocationNoelRetro(idsPca);
        return groupByIdDemandeAndSplitRetroAndPresation(list);
    }

    AlloctionNoelGrouped groupByIdDemandeAndSplitRetroAndPresation(List<SimpleAllocationNoel> list) {
        Map<String, SimpleAllocationNoel> mapRetro = new ConcurrentHashMap<String, SimpleAllocationNoel>();
        Map<String, SimpleAllocationNoel> mapPresation = new ConcurrentHashMap<String, SimpleAllocationNoel>();

        for (SimpleAllocationNoel simpleAllocationNoel : list) {
            if (!isRetro(simpleAllocationNoel)) {
                mapRetro.put(simpleAllocationNoel.getIdDemande(), simpleAllocationNoel);
            } else {
                mapPresation.put(simpleAllocationNoel.getIdDemande(), simpleAllocationNoel);
            }
        }
        return new AlloctionNoelGrouped(mapPresation);
    }

    private boolean isRetro(SimpleAllocationNoel simpleAllocationNoel) {
        return JadeStringUtil.isBlankOrZero(simpleAllocationNoel.getIdPrestationAccordee());
    }
}
