package ch.globaz.pegasus.process.statistiquesOFAS;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilationSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.VersionDroitPCAPlanDeCacule;
import ch.globaz.pegasus.business.models.pcaccordee.VersionDroitPCAPlanDeCaculeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class PCProcessStatistiqueOFASPopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<PCProcessStatistiquesOFASEnum> {

    private Map<PCProcessStatistiquesOFASEnum, String> data = null;
    private Map<? extends Enum<?>, String> test = null;
    private VersionDroitPCAPlanDeCaculeSearch versionDroitPCAPlanDeCaculeSearch = null;

    public PCProcessStatistiqueOFASPopulation() {
        super();
    }

    @Override
    public String getBusinessKey() {
        return null;
    }

    @Override
    public Class<PCProcessStatistiquesOFASEnum> getEnumForProperties() {
        return PCProcessStatistiquesOFASEnum.class;
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        PCAccordee pca = PegasusServiceLocator.getPCAccordeeService().readDetail(entity.getIdRef());

        String idTiersBeneficiaire = pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire();

        return "idPca=" + entity.getIdRef() + "&idBenef=" + idTiersBeneficiaire;
    }

    private boolean isPcaRequerant(PCAccordee pca) {
        return pca.getSimplePCAccordee().getCsRoleBeneficiaire().equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
    }

    public List<JadeProcessEntity> getPopulation() throws PCAccordeeException, AdaptationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        List<JadeProcessEntity> list = new ArrayList<JadeProcessEntity>();

        List<VersionDroitPCAPlanDeCacule> listPca = PersistenceUtil.typeSearch(versionDroitPCAPlanDeCaculeSearch,
                versionDroitPCAPlanDeCaculeSearch.whichModelClass());
        Map<String, List<VersionDroitPCAPlanDeCacule>> mapPCa = JadeListUtil.groupBy(listPca,
                new Key<VersionDroitPCAPlanDeCacule>() {
                    @Override
                    public String exec(VersionDroitPCAPlanDeCacule e) {
                        return e.getSimpleVersionDroit().getIdVersionDroit();
                    }
                });

        for (Entry<String, List<VersionDroitPCAPlanDeCacule>> entry : mapPCa.entrySet()) {

            if (entry.getValue().size() == 1) {
                if (entry.getValue().get(0).getSimplePCAccordee().getCsGenrePC()
                        .equalsIgnoreCase(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                    peupleListe(list, entry.getValue().get(0), false, "0");
                } else {
                    peupleListe(list, entry.getValue().get(0), false, "0");
                }
            } else if (entry.getValue().size() == 2) {
                if (entry.getValue().get(0).getSimplePCAccordee().getCsGenrePC()
                        .equalsIgnoreCase(IPCPCAccordee.CS_GENRE_PC_HOME)
                        || entry.getValue().get(1).getSimplePCAccordee().getCsGenrePC()
                                .equalsIgnoreCase(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                    peupleListe(list, entry.getValue().get(0), true, entry.getValue().get(1).getSimplePCAccordee()
                            .getCsGenrePC());
                    peupleListe(list, entry.getValue().get(1), true, entry.getValue().get(0).getSimplePCAccordee()
                            .getCsGenrePC());
                } else {
                    throw new PCAccordeeException("Une des 2 pca devrait être de type home");
                }
            } else {
                throw new PCAccordeeException("Plus de 2 pca valables dans une version de droit");
            }
        }

        return list;
    }

    private void peupleListe(List<JadeProcessEntity> list, VersionDroitPCAPlanDeCacule pcAccordee,
            Boolean isCoupleSepareParLaMaladie, String csGenrePcConjoint) throws AdaptationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // On a du sélectionner les pca en refus a cause de couple séparer. Afin de determiner le type de la pca.
        // Mais il ne faut pas faire de statistique sur les pcas en refus
        // S160704_002 : ne pas prendre la part fédérale si refus
        if (!IPCValeursPlanCalcul.STATUS_REFUS.equals(pcAccordee.getSimplePlanDeCalcul().getEtatPC())
                && !isRefusPartFederal(pcAccordee)) {
            JadeProcessEntity entity = new JadeProcessEntity();
            entity.setDescription(pcAccordee.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel() + " "
                    + pcAccordee.getPersonneEtendue().getTiers().getDesignation1() + " "
                    + pcAccordee.getPersonneEtendue().getTiers().getDesignation2());
            entity.setIdRef(pcAccordee.getSimplePCAccordee().getId());
            entity.setValue1(pcAccordee.getSimplePCAccordee().getIdVersionDroit() + "," + isCoupleSepareParLaMaladie
                    + "," + csGenrePcConjoint);
            list.add(entity);
        }
    }

    // S160704_002 - filtre les refus de la part Fédérale
    private boolean isRefusPartFederal(VersionDroitPCAPlanDeCacule pcAccordee) throws JadePersistenceException,
            AdaptationException, JadeApplicationServiceNotAvailableException {
        SimpleVentilationSearch ventilationSearch = new SimpleVentilationSearch();
        ventilationSearch.setForIdPrestationAccordee(pcAccordee.getSimplePCAccordee().getIdPrestationAccordee());
        CorvusServiceLocator.getSimpleVentilationService().search(ventilationSearch);

        // verifie s'il y a une part cantonale
        if (ventilationSearch.getSearchResults().length == 0) {
            return false;
        }

        TupleDonneeRapport tupleRoot;

        // charge le plan de calcul
        SimplePlanDeCalculSearch planSearch = new SimplePlanDeCalculSearch();
        planSearch.setForIdPlanDeCalcul(pcAccordee.getSimplePlanDeCalcul().getIdPlanDeCalcul());
        planSearch = (SimplePlanDeCalculSearch) JadePersistenceManager.search(planSearch, true);
        if (planSearch.getSearchResults().length == 0
                || ((SimplePlanDeCalcul) planSearch.getSearchResults()[0]).getResultatCalcul() == null) {
            return false;
        }
        SimplePlanDeCalcul simplePlanCalcul = (SimplePlanDeCalcul) planSearch.getSearchResults()[0];
        String byteArrayToString = new String(simplePlanCalcul.getResultatCalcul());
        tupleRoot = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(byteArrayToString);

        // récupère le statut de la part Fédérale
        String statutFederal = tupleRoot.getLegendeEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_STATUS_FEDERAL);
        return IPCValeursPlanCalcul.STATUS_REFUS.equals(statutFederal);

    }

    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {

        versionDroitPCAPlanDeCaculeSearch = new VersionDroitPCAPlanDeCaculeSearch();
        versionDroitPCAPlanDeCaculeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        versionDroitPCAPlanDeCaculeSearch.setForIsPlanRetenu(true);
        versionDroitPCAPlanDeCaculeSearch.setForCsEtatPlanDeCalcule(IPCValeursPlanCalcul.STATUS_REFUS);
        versionDroitPCAPlanDeCaculeSearch.setForDateValable(data.get(PCProcessStatistiquesOFASEnum.DATE_STATISTIQUE));
        versionDroitPCAPlanDeCaculeSearch.setForCsEtatPca(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        versionDroitPCAPlanDeCaculeSearch
                .setWhereKey(VersionDroitPCAPlanDeCaculeSearch.FOR_DATE_VALABLE_AND_NOT_CS_ETAT_PLAN_CLACULE);

        PegasusServiceLocator.getPCAccordeeService().search(versionDroitPCAPlanDeCaculeSearch);
        return getPopulation();
    }

    @Override
    public void setProperties(Map<PCProcessStatistiquesOFASEnum, String> hashMap) {
        data = hashMap;
    }

}
