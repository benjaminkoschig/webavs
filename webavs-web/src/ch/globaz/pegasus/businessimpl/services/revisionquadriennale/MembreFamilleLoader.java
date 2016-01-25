package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamille;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamilleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.adresse.TechnicalExceptionWithTiers;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil.SearchLotExecutor;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Sexe;

public class MembreFamilleLoader {

    private final Map<String, Pays> pays;
    private final JadeCodeSystemeService codeSystemeService;

    public MembreFamilleLoader(Map<String, Pays> pays) {
        this.pays = pays;
        try {
            codeSystemeService = JadeBusinessServiceLocator.getCodeSystemeService();
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    public MembreFamilleLoader() {
        pays = new HashMap<String, Pays>();
        try {
            codeSystemeService = JadeBusinessServiceLocator.getCodeSystemeService();
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, MembresFamilles> loadMembreFamilleComprisDansLeCalculAndGroupByIdDroit(List<String> idPca) {
        Map<String, MembresFamilles> map = new HashMap<String, MembresFamilles>();
        List<PlanDeCalculWitMembreFamille> list = loadMembreFamilleComprisDansLeCalcul(idPca, null);
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
                        "Impossible de convertir le memrbe de famille idDroitMembreFamille:"
                                + planDeCalculWitMembreFamille.getDroitMembreFamille().getId(),
                        planDeCalculWitMembreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue(), e);
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

    List<PlanDeCalculWitMembreFamille> loadMembreFamilleComprisDansLeCalcul(List<String> idPca,
            final List<RoleMembreFamille> rolesMembreFamile) {
        List<PlanDeCalculWitMembreFamille> list = new ArrayList<PlanDeCalculWitMembreFamille>();
        if (!idPca.isEmpty()) {
            try {
                list = PersistenceUtil.searchByLot(idPca, new SearchLotExecutor<PlanDeCalculWitMembreFamille>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        PlanDeCalculWitMembreFamilleSearch search = new PlanDeCalculWitMembreFamilleSearch();
                        search.setInIdPCAccordee(ids);
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
                        return PegasusServiceLocator.getPCAccordeeService().search(search);
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
        PersonneAVS personne = new PersonneAVS();
        PersonneEtendueComplexModel personneEtendue = planDeCalculWitMembreFamille.getDroitMembreFamille()
                .getMembreFamille().getPersonneEtendue();
        if (!JadeStringUtil.isBlank(personneEtendue.getPersonne().getIdTiers())) {

            personne.setId(Long.valueOf(personneEtendue.getPersonne().getIdTiers()));

            personne.setDateDeces(personneEtendue.getPersonne().getDateDeces());
            personne.setDateNaissance(personneEtendue.getPersonne().getDateNaissance());
            personne.setNom(personneEtendue.getTiers().getDesignation1());
            personne.setPrenom(personneEtendue.getTiers().getDesignation2());
            try {
                personne.setNss(new NumeroSecuriteSociale(personneEtendue.getPersonneEtendue().getNumAvsActuel()));
            } catch (IllegalArgumentException e) {

            }
            personne.setSexe(Sexe.parseAllowEmpyOrZeroValue(personneEtendue.getPersonne().getSexe()));
            if (pays != null && !pays.isEmpty()) {
                personne.setPays(pays.get(personneEtendue.getTiers().getIdPays()));
            }
            personne.setTitreParLangue(convertTitre(personneEtendue.getTiers()));
        }
        membreFamille.setPersonne(personne);

        return membreFamille;
    }

    private Map<Langues, String> convertTitre(TiersSimpleModel tiers) {
        JadeCodeSysteme csTitreTiers;
        try {
            Map<Langues, String> titreTiers = new HashMap<Langues, String>();
            csTitreTiers = codeSystemeService.getCodeSysteme(tiers.getTitreTiers());
            if (csTitreTiers != null) {
                for (Langues uneLangue : Langues.values()) {
                    if (csTitreTiers.getTraduction(uneLangue) != null) {
                        titreTiers.put(uneLangue, csTitreTiers.getTraduction(uneLangue));
                    }
                }
            }
            return titreTiers;
        } catch (JadePersistenceException e) {
            throw new RuntimeException("Impossible de convertir le titre(" + tiers.getTitreTiers()
                    + ") pour la personne suivante-> " + tiers.getDesignation1() + " " + tiers.getDesignation2()
                    + " idTiers: " + tiers.getId(), e);
        }

    }
}
