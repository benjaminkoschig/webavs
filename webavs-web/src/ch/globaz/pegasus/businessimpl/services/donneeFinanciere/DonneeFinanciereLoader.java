package ch.globaz.pegasus.businessimpl.services.donneeFinanciere;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;
import ch.globaz.pegasus.business.models.revisionquadriennale.DonneeFinanciereComplexModel;
import ch.globaz.pegasus.business.models.revisionquadriennale.DonneeFinanciereSearch;
import ch.globaz.pegasus.businessimpl.services.Loader;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class DonneeFinanciereLoader extends Loader {

    public static Map<String, DonneesFinancieresContainer> LoadByIdsDroitAndGroupByIdDroit(List<String> ids,
            DonneeFinanciereType... donneeFinanciereType) {
        ConvertAllDonneeFinanciere convertAllDonneeFinanciere = new ConvertAllDonneeFinanciere();
        List<DonneeFinanciereComplexModel> donneeFinancieres = loadDonneeFinanciere(ids, donneeFinanciereType);
        Map<String, List<DonneeFinanciereComplexModel>> map = groupByIdDroit(donneeFinancieres);
        Map<String, DonneesFinancieresContainer> mapReturn = new HashMap<String, DonneesFinancieresContainer>();
        for (Entry<String, List<DonneeFinanciereComplexModel>> entry : map.entrySet()) {
            DonneesFinancieresContainer donneesFinancieres = convertAllDonneeFinanciere.convertToDomain(entry
                    .getValue());
            mapReturn.put(entry.getKey(), donneesFinancieres);
        }
        return mapReturn;
    }

    private static List<DonneeFinanciereComplexModel> loadDonneeFinanciere(List<String> listeIdDroitAReviser,
            final DonneeFinanciereType... donneeFinanciereType) {
        // recherche par lot
        List<DonneeFinanciereComplexModel> donneeFinancieres = null;
        try {
            donneeFinancieres = PersistenceUtil.searchByLot(listeIdDroitAReviser,
                    new PersistenceUtil.SearchLotExecutor<DonneeFinanciereComplexModel>() {

                        @Override
                        public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                                JadePersistenceException {
                            DonneeFinanciereSearch donneeSearch = new DonneeFinanciereSearch();
                            donneeSearch.setForIdDroitIn(ids);
                            if (donneeFinanciereType != null) {
                                donneeSearch.setForCsTypeIn(new ArrayList<String>());
                                for (DonneeFinanciereType type : donneeFinanciereType) {
                                    donneeSearch.getForCsTypeIn().add(type.getValue());
                                }
                            }
                            donneeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                            return PegasusImplServiceLocator.getRevisionQuadriennaleService().search(donneeSearch);
                        }
                    }, 2000);
        } catch (JadePersistenceException e) {
            throw new RuntimeException("Erreur lors du chargement des données financières", e);
        } catch (JadeApplicationException e) {
            throw new RuntimeException("Le service pour charger les données financièer n'est pas pret", e);
        }
        return donneeFinancieres;
    }

    private static Map<String, List<DonneeFinanciereComplexModel>> groupByIdDroit(
            List<DonneeFinanciereComplexModel> donneeFinancieres) {
        Map<String, List<DonneeFinanciereComplexModel>> map = JadeListUtil.groupBy(donneeFinancieres,
                new Key<DonneeFinanciereComplexModel>() {
                    @Override
                    public String exec(DonneeFinanciereComplexModel e) {
                        return e.getIdDroit();
                    }
                });
        return map;
    }
}
