package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.hera.domaine.relationconjoint.RelationsConjoints;
import ch.globaz.hera.loader.RelationsConjointsLoader;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.pegasus.business.domaine.membreFamille.DonneesPersonnelles;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.StatusRefugieApatride;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamilleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.adresse.TechnicalExceptionWithTiers;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil.SearchLotExecutor;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.converter.PersonneAvsConverter;
import ch.globaz.pyxis.domaine.PaysList;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class MembreFamilleLoader {

    private static final Logger LOG = LoggerFactory.getLogger(MembreFamilleLoader.class);

    private final PersonneAvsConverter personneConverter;

    public MembreFamilleLoader(PaysList paysList) {
        try {
            personneConverter = new PersonneAvsConverter(paysList, JadeBusinessServiceLocator.getCodeSystemeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    public MembreFamilleLoader() {
        try {
            personneConverter = new PersonneAvsConverter(new PaysList(),
                    JadeBusinessServiceLocator.getCodeSystemeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, MembresFamilles> loadMembreFamilleComprisDansLeCalculAndGroupByIdDroit(Collection<String> idPca) {
        List<PlanDeCalculWitMembreFamille> list = PersistenceUtil.typeSearch(search(idPca, null),
                PlanDeCalculWitMembreFamille.class);
        Map<String, MembresFamilles> map = new HashMap<String, MembresFamilles>();

        for (PlanDeCalculWitMembreFamille planDeCalculWitMembreFamille : list) {
            String idDroit = planDeCalculWitMembreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille()
                    .getIdDroit();
            if (!map.containsKey(idDroit)) {
                map.put(idDroit, new MembresFamilles());
            }

            try {
                map.get(idDroit).add(convert(planDeCalculWitMembreFamille));
            } catch (Exception e) {
                throw new TechnicalExceptionWithTiers(
                        "Impossible de convertir le memerbe de famille idDroitMembreFamille:"
                                + planDeCalculWitMembreFamille.getDroitMembreFamille().getId(),
                        planDeCalculWitMembreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue(), e);
            }
        }
        return map;
    }

    public Map<String, Map<String, MembresFamilles>> loadMembreFamilleComprisDansLeCalculAndGroupByIdVersionDroit(
            Map<String, Set<String>> mapIdPcaIdVersion, Map<String, String> mapIdPcaIdPcaOriginale) {
        List<PlanDeCalculWitMembreFamille> list = PersistenceUtil.typeSearch(search(mapIdPcaIdVersion.keySet(), null),
                PlanDeCalculWitMembreFamille.class);
        Map<String, Map<String, MembresFamilles>> map = new HashMap<String, Map<String, MembresFamilles>>();
        Set<String> idTiers = new HashSet<String>();

        for (PlanDeCalculWitMembreFamille planDeCalculWitMembreFamille : list) {
            if (!planDeCalculWitMembreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue()
                    .getTiers().getIdTiers().trim().isEmpty()) {
                idTiers.add(planDeCalculWitMembreFamille.getDroitMembreFamille().getMembreFamille()
                        .getPersonneEtendue().getTiers().getIdTiers());
            }
        }

        RelationsConjoints relationsConjoints = RelationsConjointsLoader.build().load(idTiers).toListMetiers();
        LOG.info("Nb relation conjoint loaded: {}", relationsConjoints.size());
        personneConverter.setRelationsConjoints(relationsConjoints);

        // personneConverter.setTitre()
        for (PlanDeCalculWitMembreFamille planDeCalculWitMembreFamille : list) {
            String idPca = planDeCalculWitMembreFamille.getSimplePlanDeCalcul().getIdPCAccordee();
            Set<String> idsVersionDroit = mapIdPcaIdVersion.get(idPca);
            for (String idVersionDroit : idsVersionDroit) {
                // on fait cela car si un calcul est une copie on à pas la bonne version de droit
                if (!map.containsKey(idVersionDroit)) {
                    map.put(idVersionDroit, new HashMap<String, MembresFamilles>());
                }
                try {
                    Map<String, MembresFamilles> mapMembreFam = map.get(idVersionDroit);
                    String idPcaOrigin = mapIdPcaIdPcaOriginale.get(idPca + "_" + idVersionDroit);
                    if (!mapMembreFam.containsKey(idPcaOrigin)) {
                        mapMembreFam.put(idPcaOrigin, new MembresFamilles());
                    }
                    mapMembreFam.get(idPcaOrigin).add(convert(planDeCalculWitMembreFamille));

                } catch (Exception e) {
                    throw new TechnicalExceptionWithTiers(
                            "Impossible de convertir le membre de famille idDroitMembreFamille:"
                                    + planDeCalculWitMembreFamille.getDroitMembreFamille().getId(),
                            planDeCalculWitMembreFamille.getDroitMembreFamille().getMembreFamille()
                                    .getPersonneEtendue(), e);
                }
            }

        }
        return map;
    }

    public Map<String, MembresFamilles> loadMembreFamilleComprisDansLeCalculAndGroupByIdPca(List<String> idsPca,
            RoleMembreFamille... roles) {
        Map<String, MembresFamilles> map = new HashMap<String, MembresFamilles>();
        List<PlanDeCalculWitMembreFamille> list = loadMembreFamilleComprisDansLeCalcul(idsPca, Arrays.asList(roles));
        for (PlanDeCalculWitMembreFamille planDeCalculWitMembreFamille : list) {
            String idPca = planDeCalculWitMembreFamille.getSimplePlanDeCalcul().getIdPCAccordee();
            if (!map.containsKey(idPca)) {
                map.put(idPca, new MembresFamilles());
            }

            try {
                map.get(idPca).add(convert(planDeCalculWitMembreFamille));
            } catch (Exception e) {
                throw new TechnicalExceptionWithTiers(
                        "Impossible de convertir le memrbe de famille idDroitMembreFamille:"
                                + planDeCalculWitMembreFamille.getDroitMembreFamille().getId(),
                        planDeCalculWitMembreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue(), e);
            }
        }
        return map;
    }

    private JadeAbstractSearchModel search(Collection<String> idsPca, List<RoleMembreFamille> rolesMembreFamile) {
        PlanDeCalculWitMembreFamilleSearch search = new PlanDeCalculWitMembreFamilleSearch();
        search.setInIdPCAccordee(idsPca);
        search.setForIsPlanRetenu(true);
        search.setForComprisPcal(true);
        if (rolesMembreFamile != null) {
            List<String> csRoleFamille = new ArrayList<String>();
            for (RoleMembreFamille role : rolesMembreFamile) {
                csRoleFamille.add(role.getValue());
            }
            search.setInCsRoleFamille(csRoleFamille);
        }
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            return PegasusServiceLocator.getPCAccordeeService().search(search);
        } catch (PersonneDansPlanCalculException e) {
            throw new RuntimeException(e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        }
    }

    List<PlanDeCalculWitMembreFamille> loadMembreFamilleComprisDansLeCalcul(Collection<String> idPca,
            final List<RoleMembreFamille> rolesMembreFamile) {
        List<PlanDeCalculWitMembreFamille> list = new ArrayList<PlanDeCalculWitMembreFamille>();
        if (!idPca.isEmpty()) {
            try {
                list = PersistenceUtil.searchByLot(idPca, new SearchLotExecutor<PlanDeCalculWitMembreFamille>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        return search(ids, rolesMembreFamile);
                    }
                }, 2000);
            } catch (JadePersistenceException e) {
                throw new RuntimeException(e);
            } catch (JadeApplicationException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    MembreFamille convert(PlanDeCalculWitMembreFamille planDeCalculWitMembreFamille) {
        MembreFamille membreFamille = new MembreFamille();
        membreFamille.setRoleMembreFamille(RoleMembreFamille.fromValue(planDeCalculWitMembreFamille
                .getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC()));
        membreFamille.setId(planDeCalculWitMembreFamille.getDroitMembreFamille().getSimpleDroitMembreFamille()
                .getIdDroitMembreFamille());
        PersonneAVS personne = new PersonneAVS();
        PersonneEtendueComplexModel personneEtendue = planDeCalculWitMembreFamille.getDroitMembreFamille()
                .getMembreFamille().getPersonneEtendue();

        // FIXME: work around beacause personne.setDateDeces !!!!!
        // src\main\java\ch\globaz\pyxis\domaine\Personne.java
        if (personneEtendue.getPersonne().getDateDeces().startsWith("00.00.")) {
            personneEtendue.getPersonne().setDateDeces(
                    personneEtendue.getPersonne().getDateDeces().replace("00.00.", "01.01."));
        }

        if (!JadeStringUtil.isBlank(personneEtendue.getPersonne().getIdTiers())) {
            personne = personneConverter.convertToDomain(personneEtendue);
        }
        membreFamille.setPersonne(personne);
        SimpleDonneesPersonnelles simpleDonneesPersonnelles = planDeCalculWitMembreFamille
                .getSimpleDonneesPersonnelles();
        DonneesPersonnelles donneesPersonnelles = new DonneesPersonnelles();
        donneesPersonnelles.setStatusRefugieApatride(StatusRefugieApatride.fromValue(simpleDonneesPersonnelles
                .getCsStatusRefugieApatride()));
        donneesPersonnelles.setIsRepresentantLegal(simpleDonneesPersonnelles.getIsRepresentantLegal());
        donneesPersonnelles.setNoCaisseAvs(simpleDonneesPersonnelles.getNoCaisseAvs());
        donneesPersonnelles.setIdDernierDomicileLegale(simpleDonneesPersonnelles.getIdDernierDomicileLegale());
        membreFamille.setDonneesPersonnelles(donneesPersonnelles);

        return membreFamille;
    }

}
