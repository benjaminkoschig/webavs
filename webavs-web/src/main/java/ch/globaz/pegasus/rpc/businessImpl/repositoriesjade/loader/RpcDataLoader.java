package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.RepositoryJade;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;
import ch.globaz.pegasus.business.domaine.droit.EtatDroit;
import ch.globaz.pegasus.business.domaine.droit.MotifDroit;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.pca.Calcul;
import ch.globaz.pegasus.business.domaine.pca.PcaEtat;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.decision.DecisionRefusSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.donneeFinanciere.DonneeFinanciereLoader;
import ch.globaz.pegasus.businessimpl.services.loader.ParametersLoader;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.MembreFamilleLoader;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.rpc.business.models.AnnonceDecision;
import ch.globaz.pegasus.rpc.business.models.AnnonceDecisionSearch;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMois;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMoisSearch;
import ch.globaz.pegasus.rpc.business.models.RetourAnnonce;
import ch.globaz.pegasus.rpc.business.models.RetourAnnonceSearch;
import ch.globaz.pegasus.rpc.business.models.SimpleLotAnnonce;
import ch.globaz.pegasus.rpc.business.models.SimpleLotAnnonceSearch;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;
import ch.globaz.pegasus.rpc.domaine.RpcAddress;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.converter.PersonneAvsConverter;
import ch.globaz.pyxis.domaine.PaysList;
import ch.globaz.pyxis.loader.PaysLoader;
import com.google.common.base.Preconditions;
import com.google.gson.JsonSyntaxException;

public class RpcDataLoader {

    /**
     * D�lais � retirer � la date du dernier paiement pour <b>assurer le fait que la date obtenue soit dans le mois
     * courrant des annonces RPC</b> � annoncer </br>
     * </br>
     * <i>r�solution de BUG : </i>Les annonces prisent dans le mois sont du m�me mois que le mois d'annonces si la date
     * du denier paiement est comptabilis�e le premier du mois suivant (globalement toujours le cas car le mois
     * comptable ouvert est le mois suivant au moment des paiements mensuels)
     */
    private static final int RENTE_DATE_PAIEMENT_DELAIS = -15;
    
    private static final String ANNONCE_POUR_ENVOIE = "64074005";

    private static final Logger LOG = LoggerFactory.getLogger(RpcDataLoader.class);

    private final String plausiCategoryERROR = "64080004";
    private final InfosRpcDataLoader infos = new InfosRpcDataLoader();
    private final Integer partitionSize;
    private final Integer partitionBlobSize;
    private final Integer limitSize;
    private final Date dateDernierPaiement;
    private final Date dateMoisAnnoncesPrise;
    private final Double toleranceDifferenceAnnonces;
    private Boolean parallel = true;

    public RpcDataLoader() {
        try {
            Map<String, String> map = EPCProperties.RPC_LOAD_PARTION_SIZE.getValueJson();

            partitionSize = Integer.valueOf(map.get("all"));
            partitionBlobSize = Integer.valueOf(map.get("blob"));

            if (map.get("limit") != null) {
                limitSize = Integer.valueOf(map.get("limit"));
            } else {
                limitSize = JadeAbstractSearchModel.SIZE_NOLIMIT;
            }

            if (map.get("parallel") != null) {
                parallel = Boolean.valueOf(map.get("parallel"));
            }

            if (map.get("toleranceDiffAnnonces") != null) {
                toleranceDifferenceAnnonces = Double.valueOf(map.get("toleranceDiffAnnonces"));
            } else {
                toleranceDifferenceAnnonces = 0.2;
            }

            dateDernierPaiement = loadDateDernierPaiement();
            dateMoisAnnoncesPrise = offsetDateToMoisAnnoncesPrise(dateDernierPaiement);
            infos.setDateDernierPaiement(dateDernierPaiement);
            infos.setDateMoisAnnoncesPrise(dateMoisAnnoncesPrise);
            LOG.info("Date d�rnier paiement: {}", dateDernierPaiement.getSwissValue());
            LOG.info("Mois des annonces: {}", dateMoisAnnoncesPrise.getSwissMonthValue());
            LOG.info(
                    "This parmeters will be use to load RPC datas: partitionSize[{}],partitionBlobSize[{}],limitSize[{}],parallel[{}]",
                    partitionSize, partitionBlobSize, limitSize, parallel);
        } catch (PropertiesException e) {
            throw new RuntimeException(e);
        } catch (JsonSyntaxException e) {
            try {
                throw new RuntimeException("Incorrect values [" + EPCProperties.RPC_LOAD_PARTION_SIZE.getValue() + "]"
                        + " for this properties:" + EPCProperties.RPC_LOAD_PARTION_SIZE.getPropertyName(), e);
            } catch (PropertiesException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    public Integer getLimitSize() {
        return limitSize;
    }

    public Double getToleranceDifferenceAnnonces() {
        return toleranceDifferenceAnnonces;
    }

    public InfosRpcDataLoader getInfos() {
        return infos;
    }

    public RpcData loadByIdAnnonce(String idAnnonce) {
        // On n'est pas dans un cadre ou il faut faire de la masse. On charge une seule annonce
        parallel = false;
        Preconditions.checkNotNull(idAnnonce, "idAnnonce is null");
        AnnonceDecisionSearch search = new AnnonceDecisionSearch();
        search.setForIdAnnonce(idAnnonce);
        List<AnnonceDecision> annoncesDecisions = RepositoryJade.searchForAndFetch(search);
        if (annoncesDecisions.isEmpty()) {
            throw new RpcBusinessException("Any annonce found withe this id : {?} ", idAnnonce);
        }
        Set<String> idsDecision = new HashSet<String>();
        for (AnnonceDecision annonceDecision : annoncesDecisions) {
            idsDecision.add(annonceDecision.getSimpleDecisionHeader().getIdDecisionHeader());
        }

        RPCDecionsPriseDansLeMoisSearch searchDecision = new RPCDecionsPriseDansLeMoisSearch();
        searchDecision.setForIdsDecsion(idsDecision);
        List<RPCDecionsPriseDansLeMois> decisions = RepositoryJade.searchForAndFetch(searchDecision);

        List<DecisionRefus> decisionsRefus = loadDecisionsRefusByIdDecision(idsDecision.iterator().next());
        // List<DecisionSuppression> decisionsSuppressions =
        // loadDecisionsRefusByIdDecision(idsDecision.iterator().next());

        RpcDatasFilter filter = new RpcDatasFilter();
        Map<String, List<RPCDecionsPriseDansLeMois>> mapDecision = filter.filtreAndGroupByIdVersionDroit(decisions);
        RpcDatasListConverter rpcDatas = load(mapDecision, decisionsRefus);

        return rpcDatas.read();
    }

    public RpcData loadByIdDecision(String... idDecision) {
        parallel = false;
        Preconditions.checkNotNull(idDecision, "idDecision is null");

        Set<String> idsDecision = new HashSet<String>();
        for(String decision : idDecision) {
            idsDecision.add(decision);
        }

        RPCDecionsPriseDansLeMoisSearch searchDecision = new RPCDecionsPriseDansLeMoisSearch();
        searchDecision.setWhereKey("prevalidation");
        searchDecision.setForIdsDecsion(idsDecision);
        List<RPCDecionsPriseDansLeMois> decisions = RepositoryJade.searchForAndFetch(searchDecision);

        List<DecisionRefus> decisionsRefus = loadDecisionsRefusByIdDecision(idsDecision.iterator().next());

        RpcDatasFilter filter = new RpcDatasFilter();
        Map<String, List<RPCDecionsPriseDansLeMois>> mapDecision = filter.filtreAndGroupByIdVersionDroit(decisions);
        RpcDatasListConverter rpcDatas = load(mapDecision, decisionsRefus);

        return rpcDatas.read();
    }

    public RpcDatasListConverter loadByDernierPaiement() {

        List<DecisionRefus> decisionsRefus = new ArrayList<DecisionRefus>();
        if (limitSize == JadeAbstractSearchModel.SIZE_NOLIMIT) {
            decisionsRefus = loadDecisionsRefus(dateMoisAnnoncesPrise);
            infos.setNbDecisionsRefus(decisionsRefus.size());
            LOG.info("Decisions refus SC loaded: {}", infos.getNbDecisionsRefus());
        }

        List<RPCDecionsPriseDansLeMois> decionsPriseDansLeMois = loadDecisionPriseDansLeMois(dateMoisAnnoncesPrise);
        infos.setNbDecisionAc(decionsPriseDansLeMois.size());
        RPCDecionsPriseDansLeMois choise = null;
        LOG.info("Decisions AC loaded for the month : {}", infos.getNbDecisionAc());

        List<RPCDecionsPriseDansLeMois> decionsMoisSuivantDuMoisPrecedent = loadDecisionMoisSuivantDuMoisPrecendant(dateMoisAnnoncesPrise);
        LOG.info("Decisions de type 'Mois suivant' du mois pr�c�dent : {}", decionsMoisSuivantDuMoisPrecedent.size());
        decionsPriseDansLeMois.addAll(decionsMoisSuivantDuMoisPrecedent);

        /* Control de population: Retours annonce en erreur dans le lot precedent et avec fin de droit */
        List<RPCDecionsPriseDansLeMois> decionsEnErreurMoisPrecedent = loadDecisionEnErreurMoisPrecedent(dateMoisAnnoncesPrise);
        infos.setNbErrorRetoursAnnoncePreviousMonth(decionsEnErreurMoisPrecedent.size());
        LOG.info("Retours decisions en erreur : {}", infos.getNbErrorRetoursAnnoncePreviousMonth());
        decionsPriseDansLeMois.addAll(decionsEnErreurMoisPrecedent);

        Set<String> idsVersionDroitNotIn = new HashSet<String>();
        for (RPCDecionsPriseDansLeMois rpcDecionsPriseDansLeMois : decionsPriseDansLeMois) {
            if (!MotifDroit.ADAPTATION.getValue()
                    .equals(rpcDecionsPriseDansLeMois.getSimpleVersionDroit().getCsMotif())) {
                idsVersionDroitNotIn.add(rpcDecionsPriseDansLeMois.getSimpleVersionDroit().getIdVersionDroit());
            }
        }

        List<RPCDecionsPriseDansLeMois> currentPca = loadPcaCourante(idsVersionDroitNotIn, dateMoisAnnoncesPrise);

        removeDateFin(currentPca);
        infos.setNbPcaCourante(currentPca.size());
        LOG.info("Nb pca current loaded: {}", infos.getNbPcaCourante());

        RpcDatasFilter filter = new RpcDatasFilter();
        decionsPriseDansLeMois.addAll(currentPca);
        Map<String, List<RPCDecionsPriseDansLeMois>> mapDecision = filter
                .filtreAndGroupByIdVersionDroit(decionsPriseDansLeMois);

        infos.setNbDesicionsRestantes(filter.getNbRestant());
        infos.setNbVersionDroitRestant(mapDecision.size());
        LOG.info("Decisions restantes: {}", infos.getNbDesicionsRestantes());
        LOG.info("Version droit restantes: {}", infos.getNbVersionDroitRestant());

        return load(mapDecision, decisionsRefus);
    }

    private void removeDateFin(List<RPCDecionsPriseDansLeMois> currentPca) {
        for (RPCDecionsPriseDansLeMois rpcDecionsPriseDansLeMois : currentPca) {
            rpcDecionsPriseDansLeMois.getSimplePCAccordee().setDateFin(null);
            rpcDecionsPriseDansLeMois.getSimpleDecisionHeader().setDateFinDecision(null);
        }
    }

    private RpcDatasListConverter load(Map<String, List<RPCDecionsPriseDansLeMois>> mapDecision,
            List<DecisionRefus> decisionsRefus) {

        final IdsContainer allIdsContainer = new IdsContainer();
        allIdsContainer.addAllIds(mapDecision);

        infos.setNbIdTiersCourrier(allIdsContainer.getIdsTiersCourrier().size());
        infos.setNbIdTiersDomicile(allIdsContainer.getIdsTiersDomicile().size());
        LOG.info("Nb idTiersDomicile: {}, idTiersCourrier {}", infos.getNbIdTiersDomicile(),
                infos.getNbIdTiersCourrier());

        final PaysLoader paysLoader = new PaysLoader();
        List<IdsContainer> idsContainers = allIdsContainer.partion(partitionSize);
        ThreadLoaderRunner<IdsContainer, MembresFamilleContainer> loaderRunner = new ThreadLoaderRunner<IdsContainer, MembresFamilleContainer>();
        loaderRunner.inputs(idsContainers).transformer(new Transformer<IdsContainer, MembresFamilleContainer>() {
            @Override
            public MembresFamilleContainer transform(IdsContainer idsContainer) {
                MembresFamilleContainer container = new MembresFamilleContainer();
                container.setMapMembresFamilles(loadMembreFamilleWithDonneesFinancieres(idsContainer,
                        paysLoader.getPaysList()));
                container.setIdsTiersDomicile(IdsContainer.resolveIdsTiersMembreFamille(
                        container.getMapMembresFamilles(), idsContainer.getIdsTiersDomicile()));
                container.setIdsTiersCourrier(IdsContainer.resolveIdsTiersMembreFamille(
                        container.getMapMembresFamilles(), idsContainer.getIdsTiersCourrier()));
                return container;
            }
        });
        LOG.info("The membre famille will be load");
        MembresFamilleContainer container = new MembresFamilleContainer(loaderRunner.parallel(parallel).load());

        RpcAdresseLoader adresseLoader = RpcAdresseLoader.build().partitionSize(partitionSize).parallel(parallel)
                .load(container.getAllIdTiersForAdresse());

        Map<String, RpcAddress> mapAdressesDomicile = adresseLoader.resolve(
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_DOMICILE);

        Map<String, RpcAddress> mapAdressesCourrier = adresseLoader.resolve(
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_COURRIER);

        infos.setNbAdresseCourrier(mapAdressesCourrier.size());
        LOG.info("Addresse courrier loaded nb: {}", infos.getNbAdresseCourrier());
        infos.setNbAdresseDomicile(mapAdressesDomicile.size());
        LOG.info("Addresse domicile loaded nb: {}", infos.getNbAdresseDomicile());
        infos.setNbMembreFamille(InfosRpcDataLoader.computElementSizeMapMapList(container.getMapMembresFamilles()));
        LOG.info("MembreFamilles loaded nb: {}", infos.getNbMembreFamille());

        final Map<String, Calcul> mapIdPlanCalculWithCalcul = loadPlanCalcul(allIdsContainer);
        infos.setNbPlanCalcul(mapIdPlanCalculWithCalcul.size());
        LOG.info("PlanCalcul loaded nb: {}", infos.getNbPlanCalcul());

        Parameters parameters = loadParamters(container);
        PersonneAvsConverter personneAvsConverter = personneConverter(paysLoader.getPaysList(), decisionsRefus,
                mapDecision);

        return new RpcDatasListConverter(mapDecision, decisionsRefus, mapAdressesCourrier, mapAdressesDomicile,
                container.getMapMembresFamilles(), mapIdPlanCalculWithCalcul, personneAvsConverter, parameters,
                dateMoisAnnoncesPrise);
    }

    private Parameters loadParamters(MembresFamilleContainer container) {
        ParametersLoader parametersLoader = new ParametersLoader();

        if (container.getMapMembresFamilles().size() < 100) {
            Set<String> idsTypeChambre = IdsContainer.resolveIdsTypeChambreHome(container.getMapMembresFamilles());
            Set<String> idsLocalite = IdsContainer.resolveIdsLocaliteDernierDomicileLegale(container
                    .getMapMembresFamilles());
            parametersLoader = new ParametersLoader(idsTypeChambre, idsLocalite);
        }

        Parameters parameters = parametersLoader.load();
        return parameters;
    }

    private PersonneAvsConverter personneConverter(PaysList paysList, List<DecisionRefus> decisionsRefus,
            Map<String, List<RPCDecionsPriseDansLeMois>> mapDecision) {
        Set<String> csTitres = new HashSet<String>();
        for (DecisionRefus decisionRefus : decisionsRefus) {
            csTitres.add(decisionRefus.getDecisionHeader().getPersonneEtendue().getTiers().getTitreTiers());
        }
        for (List<RPCDecionsPriseDansLeMois> list : mapDecision.values()) {
            for (RPCDecionsPriseDansLeMois rpcDecionsPriseDansLeMois : list) {
                csTitres.add(rpcDecionsPriseDansLeMois.getSimpleTiers().getTitreTiers());
            }
        }
        PersonneAvsConverter personneEtendueToPersonneAvs;
        try {
            personneEtendueToPersonneAvs = new PersonneAvsConverter(paysList,
                    JadeBusinessServiceLocator.getCodeSystemeService());
            personneEtendueToPersonneAvs.loadTitre(csTitres);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CommonTechnicalException(e);
        }
        return personneEtendueToPersonneAvs;
    }

    private Map<String, Calcul> loadPlanCalcul(final IdsContainer idsContainer) {
        final Map<String, Calcul> mapIdPlanCalculWithCalcul = new ConcurrentHashMap<String, Calcul>();
        ThreadLoaderRunner<List<String>, Map<String, Calcul>> loader = new ThreadLoaderRunner<List<String>, Map<String, Calcul>>();
        List<String> ids = new ArrayList<String>(idsContainer.getIdsPlanCal());
        loader.transformer(new Transformer<List<String>, Map<String, Calcul>>() {

            @Override
            public Map<String, Calcul> transform(List<String> ids) {
                mapIdPlanCalculWithCalcul.putAll(loadBlobs(ids));
                return mapIdPlanCalculWithCalcul;
            }
        }).input(ids).partitionList(partitionBlobSize).parallel(parallel).load();

        return mapIdPlanCalculWithCalcul;
    }

    private Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> loadMembreFamilleWithDonneesFinancieres(
            IdsContainer idsContainer, PaysList paysList) {
        Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> map = new HashMap<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>>();

        if (!idsContainer.getIdsPca().isEmpty()) {

            MembreFamilleLoader familleLoader = new MembreFamilleLoader(paysList);
            Map<String, Map<String, MembresFamilles>> mapMembreFamilles = familleLoader
                    .loadMembreFamilleComprisDansLeCalculAndGroupByIdVersionDroit(
                            idsContainer.createMapIdPcaIdVersionDroit(), idsContainer.createMapIdPcaIdpcaOriginale());

            LOG.info("MembresFamille loaded nbVersionDroit : {}", mapMembreFamilles.size());
            LOG.info("Nb idVersionDroit : {}", idsContainer.getIdsVersionDroit().size());

            Map<String, DonneesFinancieresContainer> mapDonneesFinancieres = DonneeFinanciereLoader
                    .loadByIdsVersionDroitAndGroupByIdVersionDroit(idsContainer.getIdsVersionDroit());

            LOG.info("DonneesFinancieres loaded nbVersionDroit : {}", mapDonneesFinancieres.size());

            for (Entry<String, Map<String, MembresFamilles>> entry : mapMembreFamilles.entrySet()) {
                String idVersionDroit = entry.getKey();
                map.put(idVersionDroit, new HashMap<String, List<MembreFamilleWithDonneesFinanciere>>());
                DonneesFinancieresContainer container = mapDonneesFinancieres.get(idVersionDroit);
                if (container == null) {
                    LOG.error("Aucune donn�es fianci�res trouv�es avec cette idVersionDroit: "
                            + idVersionDroit
                            + " NSS "
                            + entry.getValue().entrySet().iterator().next().getValue().getRequerant().getPersonne()
                                    .getNss());
                } else {
                    for (Entry<String, MembresFamilles> mapFam : entry.getValue().entrySet()) {
                        for (MembreFamille membreFamille : mapFam.getValue().getMembresFamilles()) {
                            DonneesFinancieresContainer containerMembre = container
                                    .filtreForMembreFamille(membreFamille);
                            if (!map.get(idVersionDroit).containsKey(mapFam.getKey())) {
                                map.get(idVersionDroit).put(mapFam.getKey(),
                                        new ArrayList<MembreFamilleWithDonneesFinanciere>());
                            }
                            map.get(idVersionDroit).get(mapFam.getKey())
                                    .add(new MembreFamilleWithDonneesFinanciere(membreFamille, containerMembre));
                        }
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Calcul> loadBlobs(Collection<String> idsPlanCal) {
        Map<String, Calcul> mapCalcule = new HashMap<String, Calcul>();
        if (!idsPlanCal.isEmpty()) {
            Map<String, byte[]> map = new BlobLoader().loadBlob(idsPlanCal);
            for (Entry<String, byte[]> entry : map.entrySet()) {
                XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(entry.getValue()));
                TupleDonneeRapport tuple = (TupleDonneeRapport) decoder.readObject();
                mapCalcule.put(entry.getKey(), new Calcul(tuple, entry.getValue().length, entry.getKey()));
            }
        }
        return mapCalcule;
    }

    /**
     * possibility to test in test local/internal use to set <code>search.setForIdDemande("idDem");</code> please take
     * care ton not commit this
     * 
     */
    private List<RPCDecionsPriseDansLeMois> loadDecisionPriseDansLeMois(Date dateMoisAnnoncesPrise) {
        RPCDecionsPriseDansLeMoisSearch search = new RPCDecionsPriseDansLeMoisSearch();
        search.setForDateDecisionMax(dateMoisAnnoncesPrise.getLastDayOfMonth().getSwissValue());
        search.setForDateDecisionMin(dateMoisAnnoncesPrise.getFirstDayOfMonth().getSwissValue());
        search.setForDateDecisionMaxMoins1(dateMoisAnnoncesPrise.addMonth(-1).getLastDayOfMonth().getSwissValue());
        search.setForDateDecisionMinMoins1(dateMoisAnnoncesPrise.addMonth(-1).getFirstDayOfMonth().getSwissValue());
        search.setForDateDecisionMoisAnneMoins1(dateMoisAnnoncesPrise.addMonth(-1).getSwissMonthValue());
        search.setForDebutDecision(dateMoisAnnoncesPrise.getMoisAnneeFormatte());

        LOG.info("requ�te loadDecisionPriseDansLeMois");
        return RepositoryJade.searchForAndFetch(search, limitSize);
    }

    private List<RPCDecionsPriseDansLeMois> loadDecisionMoisSuivantDuMoisPrecendant(Date dateMoisAnnoncesPrise) {
        RPCDecionsPriseDansLeMoisSearch search = new RPCDecionsPriseDansLeMoisSearch();
        search.setWhereKey("decisionMoisSuivantDuMoisPrecendant");
        search.setForDebutDecision(dateMoisAnnoncesPrise.getMoisAnneeFormatte());
        Date dateMoisPrecedent = dateMoisAnnoncesPrise.addMonth(-1);
        search.setForDateDecisionMax(dateMoisPrecedent.getLastDayOfMonth().getSwissValue());
        search.setForDateDecisionMin(dateMoisPrecedent.getFirstDayOfMonth().getSwissValue());
        search.setForDateDecisionMaxMoins1(dateMoisPrecedent.addMonth(-1).getLastDayOfMonth().getSwissValue());
        search.setForDateDecisionMinMoins1(dateMoisPrecedent.addMonth(-1).getFirstDayOfMonth().getSwissValue());
        search.setForDateDecisionMoisAnneMoins1(dateMoisPrecedent.addMonth(-1).getSwissMonthValue());

        LOG.info("requ�te loadDecisionMoisSuivantDuMoisPrecendant");
        return RepositoryJade.searchForAndFetch(search, limitSize);
    }

    // Control de population de retours d'annonces
    private List<RPCDecionsPriseDansLeMois> loadDecisionEnErreurMoisPrecedent(Date dateDernierPaiement) {
        List<String> decisionsEnErreur = new ArrayList<String>();

        String startPeriod = dateDernierPaiement.addMonth(-1).getFirstDayOfMonth().getSwissValue();
        String endPeriod = dateDernierPaiement.addMonth(-1).getLastDayOfMonth().getSwissValue();

        SimpleLotAnnonceSearch lotSearch = new SimpleLotAnnonceSearch();
        lotSearch.setForStartDate(startPeriod);
        lotSearch.setForEndDate(endPeriod);
        lotSearch.setWhereKey("byPeriod");
        List<SimpleLotAnnonce> lots = RepositoryJade.searchForAndFetch(lotSearch, limitSize);
        if (!lots.isEmpty()) {
            RetourAnnonceSearch retourSearch = new RetourAnnonceSearch();
            // RetourAnnonceConverter.toCsCode(RpcPlausiCategory.ERROR= 64080004)
            retourSearch.setForCategoryPlausi(plausiCategoryERROR);
            retourSearch.setForIdLot(lots.get(0).getId());
            List<RetourAnnonce> retoursEnErreur = RepositoryJade.searchForAndFetch(retourSearch, limitSize);
            for (RetourAnnonce enErreur : retoursEnErreur) {
                if(ANNONCE_POUR_ENVOIE.equals(enErreur.getSimpleAnnonce().getCsEtat())) {
                    decisionsEnErreur.add(enErreur.getSimpleDecisionHeader().getIdDecisionHeader());
                }
            }
            if (!decisionsEnErreur.isEmpty()) {
                RPCDecionsPriseDansLeMoisSearch search = new RPCDecionsPriseDansLeMoisSearch();
                search.setWhereKey("avecDateFin");
                search.setForDateDecisionMoisAnneMoins1(dateDernierPaiement.addMonth(-1).getLastDayOfMonth()
                        .getSwissMonthValue());
                search.setForIdsDecsion(decisionsEnErreur);
                LOG.info("requ�te loadDecisionEnErreurMoisPrecedent");
                return RepositoryJade.searchForAndFetch(search, limitSize);
            }
        }
        return new ArrayList<RPCDecionsPriseDansLeMois>();
    }

    private List<DecisionRefus> loadDecisionsRefus(Date dateDernierPaiement) {
        DecisionRefusSearch search = new DecisionRefusSearch();
        search.whereKeyForRpc();
        search.setForDateDecisionMax(dateDernierPaiement.getLastDayOfMonth().getSwissValue());
        search.setForDateDecisionMin(dateDernierPaiement.getFirstDayOfMonth().getSwissValue());
        LOG.info("requ�te loadDecisionsRefus");
        return RepositoryJade.searchForAndFetch(search, limitSize);
    }

    private List<DecisionRefus> loadDecisionsRefusByIdDecision(String id) {
        DecisionRefusSearch search = new DecisionRefusSearch();
        search.setForIdDecisionHeader(id);
        search.whereKeyForRpc();
        LOG.info("requ�te loadDecisionsRefusByIdDecision");
        return RepositoryJade.searchForAndFetch(search);
    }

    private Date loadDateDernierPaiement() {
        Date dateDernierPaiement;
        try {
            dateDernierPaiement = new Date(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
        } catch (PmtMensuelException e) {
            throw new CommonTechnicalException(e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CommonTechnicalException(e);
        }
        return dateDernierPaiement;
    }

    private Date offsetDateToMoisAnnoncesPrise(Date dateDernierPaiement) {
        return dateDernierPaiement.addDays(RENTE_DATE_PAIEMENT_DELAIS).getFirstDayOfMonth();
    }

    /**
     * possibility to test in test local/internal use to set <code>search.setForIdDemande("idDem");</code> please take
     * care ton not commit this
     */
    private List<RPCDecionsPriseDansLeMois> loadPcaCourante(Set<String> idsVersionDroitNotIn, Date dateDernierPaiement) {
        RPCDecionsPriseDansLeMoisSearch search = new RPCDecionsPriseDansLeMoisSearch();
        search.setForCsEtatDemande(IPCDemandes.CS_OCTROYE);
        search.setWhereKey("pcaCourante");
        search.setForCsEtatDroit(EtatDroit.VALIDE.getValue());
        search.setForCsEtatPca(PcaEtat.VALIDE.getValue());
        search.setForDebutDecision(dateDernierPaiement.getMoisAnneeFormatte());

        // Ou date ult�rieur au mois paiement
        search.setForDateFinMoisFutur(dateDernierPaiement.getSwissMonthValue());
        search.getForCsEtatDemandeMoisFutur().add(IPCDemandes.CS_REFUSE);
        // search.getForCsMotifNotIn().add(MotifDroit.ADAPTATION.getValue());
        search.setForIdsVersionDroitNotIn(idsVersionDroitNotIn);
        LOG.info("requ�te loadPcaCourante");
        return RepositoryJade.searchForAndFetch(search, limitSize);
    }
}
