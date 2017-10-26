package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.JadePersistencePKProvider;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.DomaineConverterJade;
import ch.globaz.common.persistence.RepositoryJade;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.rpc.business.models.LienAnnonceDecision;
import ch.globaz.pegasus.rpc.business.models.LienAnnonceDecisionSearch;
import ch.globaz.pegasus.rpc.business.models.PersonneAnnonceSearch;
import ch.globaz.pegasus.rpc.business.models.SimpleAnnonce;
import ch.globaz.pegasus.rpc.business.models.SimpleAnnonceSearch;
import ch.globaz.pegasus.rpc.business.models.SimpleLienAnnonceDecision;
import ch.globaz.pegasus.rpc.domaine.AnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.CodeTraitement;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;
import ch.globaz.pegasus.rpc.domaine.LotAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.PersonneAnnonce;
import ch.globaz.pegasus.rpc.domaine.PersonneAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionWithIdPlanCal;
import ch.globaz.pegasus.utils.RpcUtil;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.converter.PersonneAvsConverter;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.loader.PaysLoader;
import com.google.common.base.Preconditions;

public class AnnonceRepositoryJade extends RepositoryJade<AnnonceRpc, SimpleAnnonce> implements AnnonceRpcRepository {

    private JadePersistencePKProvider pkProviderAnnonce;
    private JadePersistencePKProvider pkProviderLienDecision;
    private boolean hasPKProviders = false;

    public void buildPKproviders(long reservedRangeAnnonce, long reservedRangeLienDecision) {
        pkProviderAnnonce = this.buildKeyProviender(reservedRangeAnnonce);
        pkProviderLienDecision = buildKeyProviender(SimpleLienAnnonceDecision.class, reservedRangeLienDecision);
        hasPKProviders = true;
    }

    public void clearPKPorviders() {
        pkProviderAnnonce = null;
        pkProviderLienDecision = null;
        hasPKProviders = false;
    }

    @Override
    public AnnonceRpc create(AnnonceRpc entity, LotAnnonceRpc lotAnnonce) {
        SimpleAnnonce model = convert(entity, lotAnnonce);
        RepositoryJade.create(entity, model);
        return entity;
    }

    public JadePersistencePKProvider buildKeyProviender(long reservedRange) {
        return buildKeyProviender(SimpleAnnonce.class, reservedRange);
    }

    public AnnonceRpc create(AnnonceRpc entity, JadePersistencePKProvider pkProviderAnnonce,
            JadePersistencePKProvider pkProviderLienDecision) {
        AnnonceRpc annonce = super.create(entity, pkProviderAnnonce);
        if (entity.getDecisions() != null) {
            for (RpcDecisionWithIdPlanCal decision : entity.getDecisions()) {
                RpcUtil.deleteSuffixDecisionId(decision.getDecision(), null);
                SimpleLienAnnonceDecision lien = convert(annonce, decision);
                create(lien, pkProviderLienDecision);
            }
        }
        return annonce;
    }

    @Override
    public AnnonceRpc create(AnnonceRpc entity) {
        if (hasPKProviders) {
            return this.create(entity, pkProviderAnnonce, pkProviderLienDecision);
        } else {
            AnnonceRpc annonce = super.create(entity);
            for (RpcDecisionWithIdPlanCal decision : entity.getDecisions()) {
                createLienDecision(annonce, decision);
            }
            return annonce;
        }
    }

    @Override
    public void createLienDecision(AnnonceRpc annonce, RpcDecisionWithIdPlanCal decision) {
        SimpleLienAnnonceDecision lien = convert(annonce, decision);
        try {
            JadePersistenceManager.add(lien);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    private SimpleLienAnnonceDecision convert(AnnonceRpc annonce, RpcDecisionWithIdPlanCal decision) {
        SimpleLienAnnonceDecision lien = new SimpleLienAnnonceDecision();
        lien.setIdAnnonce(annonce.getId());
        lien.setIdDecision(decision.getDecision().getId());
        if (decision.getIdPlanCalcul() != null) {
            lien.setIdPlanCalcul(decision.getIdPlanCalcul());
            lien.setCsRole(decision.getRoleMembreFamille().getValue());
        }
        return lien;
    }

    @Override
    public List<PersonneAnnonceRpc> findPersonneAnnonce(AnnonceSearch annoceSearch) {
        PersonneAnnonceSearch searchModel = convert(annoceSearch);
        searchModel = this.search(searchModel);

        List<PersonneAnnonceRpc> list = new ArrayList<PersonneAnnonceRpc>();
        LotAnnonceConverter lotAnnonceConverter = new LotAnnonceConverter();
        PersonneAvsConverter personneAvsConverter = buildPersonneAvsConverter();

        Set<String> idsAnnonces = new HashSet<String>();
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            ch.globaz.pegasus.rpc.business.models.PersonneAnnonce annonce = (ch.globaz.pegasus.rpc.business.models.PersonneAnnonce) model;
            idsAnnonces.add(annonce.getSimpleAnnonce().getId());
            AnnonceRpc annonceRpc = getConverter().convertToDomain(annonce.getSimpleAnnonce());
            LotAnnonceRpc lotAnnonceRpc = lotAnnonceConverter.convertToDomain(annonce.getSimpleLotAnnonce());
            annonceRpc.setLot(lotAnnonceRpc);
            // FIXME: work around with data deces when day and month = 00
            PersonneSimpleModel personne = annonce.getPersonne().getPersonne();
            if (personne.getDateDeces().startsWith("00.00")) {
                annonce.getPersonne().getPersonne().setDateDeces(personne.getDateDeces().replace("00.00.", "01.01."));
            }

            PersonneAVS personneAVS = personneAvsConverter.convertToDomain(annonce.getPersonne());
            PersonneAnnonce personneAnnonce = new PersonneAnnonce();
            personneAnnonce.setAnnonce(annonceRpc);
            personneAnnonce.setPersonneAVS(personneAVS);
            list.add(personneAnnonce);
        }

        if (!idsAnnonces.isEmpty()) {
            Map<String, List<RpcDecisionWithIdPlanCal>> map = findLiensByIdsAnnonceAndGourpByIdAnnonce(idsAnnonces);
            for (PersonneAnnonceRpc annonce : list) {
                List<RpcDecisionWithIdPlanCal> decisions = map.get(annonce.getId());
                annonce.getAnnonceRpc().setDecisions(decisions);
            }
        }
        return list;
    }

    private Map<String, List<RpcDecisionWithIdPlanCal>> findLiensByIdsAnnonceAndGourpByIdAnnonce(Set<String> idsAnnonces) {
        LienAnnonceDecisionSearch search = new LienAnnonceDecisionSearch();
        search.setForIdsAnnonce(idsAnnonces);
        search = this.search(search);
        Map<String, List<RpcDecisionWithIdPlanCal>> map = new HashMap<String, List<RpcDecisionWithIdPlanCal>>();
        for (JadeAbstractModel model : search.getSearchResults()) {
            LienAnnonceDecision lien = (LienAnnonceDecision) model;
            String id = lien.getLienAnnonceDecision().getIdAnnonce();
            if (!map.containsKey(id)) {
                map.put(id, new ArrayList<RpcDecisionWithIdPlanCal>());
            }

            Decision decision = new Decision();
            decision.setTiersBeneficiaire(new PersonneAVS());
            decision.getTiersBeneficiaire().setId(Long.valueOf(lien.getDecisionHeader().getIdTiersBeneficiaire()));
            map.get(id).add(
                    new RpcDecisionWithIdPlanCal(decision, lien.getLienAnnonceDecision().getIdPlanCalcul(),
                            RoleMembreFamille.fromValue(lien.getLienAnnonceDecision().getCsRole())));
        }
        return map;
    }

    private PersonneAvsConverter buildPersonneAvsConverter() {
        PersonneAvsConverter personneAvsConverter;
        try {
            PaysLoader paysLoader = new PaysLoader();
            personneAvsConverter = new PersonneAvsConverter(paysLoader.getPaysList(),
                    JadeBusinessServiceLocator.getCodeSystemeService());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CommonTechnicalException(e);
        }
        return personneAvsConverter;
    }

    private PersonneAnnonceSearch convert(AnnonceSearch annonceSearch) {
        PersonneAnnonceSearch searchModel = new PersonneAnnonceSearch();
        if (annonceSearch.getAnnonceId() != null && !annonceSearch.getAnnonceId().isEmpty()) {
            searchModel.setForIdAnnonce(annonceSearch.getAnnonceId());
        }
        if (annonceSearch.getCodeTraitement() != null) {
            searchModel.setForCodeTraitement(AnnonceConverter.toCsCode(annonceSearch.getCodeTraitement()));
        }
        if (annonceSearch.getEtat() != null) {
            searchModel.setForEtat(AnnonceConverter.toCsCode(annonceSearch.getEtat()));
        }
        if (annonceSearch.getPeriodeDateDebut() != null) {
            searchModel.setForPeriodeDateDebut(annonceSearch.getPeriodeDateDebut().getSwissValue());
        }
        if (annonceSearch.getPeriodeDateFin() != null) {
            searchModel.setForPeriodeDateFin(annonceSearch.getPeriodeDateFin().getSwissValue());
        }
        if (annonceSearch.isRechercheFamille()) {
            searchModel.whereKeyRechercheFamille();
        }
        if (annonceSearch.getOrder() != null && !annonceSearch.getOrder().isEmpty()) {
            searchModel.setOrderKey(annonceSearch.getOrder());
        }
        searchModel.setLikeNom(annonceSearch.getNom());
        searchModel.setLikeNss(annonceSearch.getNss());
        searchModel.setLikePrenom(annonceSearch.getPrenom());
        return searchModel;
    }

    @Override
    public AnnonceRpc update(AnnonceRpc entity, LotAnnonceRpc lotAnnonce) {
        SimpleAnnonce model = convert(entity, lotAnnonce);
        this.update(entity, model);
        return entity;
    }

    @Override
    public AnnonceRpc update(AnnonceRpc entity) {
        SimpleAnnonce simpleAnnonce = findModelById(entity.getId());
        Preconditions.checkNotNull(simpleAnnonce, "Any one simpleAnnonce was found with this id: " + entity.getId());
        SimpleAnnonce model = converToPersistence(entity);
        model.setIdLot(simpleAnnonce.getIdLot());
        this.update(entity, model);
        return entity;
    }

    private SimpleAnnonce convert(AnnonceRpc entity, LotAnnonceRpc lotAnnonce) {
        SimpleAnnonce model = converToPersistence(entity);
        model.setIdLot(lotAnnonce.getId());
        return model;
    }

    @Override
    public DomaineConverterJade<AnnonceRpc, SimpleAnnonce> getConverter() {
        return new AnnonceConverter();
    }

    public void deleteAnnonceByLot(String lotId) {
        SimpleAnnonceSearch search = new SimpleAnnonceSearch();
        search.setForIdLot(lotId);
        search = this.search(search);
        for (JadeAbstractModel model : search.getSearchResults()) {
            SimpleAnnonce annonce = (SimpleAnnonce) model;
            AnnonceRpc annonceRpc = getConverter().convertToDomain(annonce);
            delete(annonceRpc);
        }
    }

    public void updateCodeTraitement(String idAnnonce, String stringCode) {
        CodeTraitement code = AnnonceConverter.toEnum(stringCode);
        updateCodeTraitement(idAnnonce, code);
    }

    public void updateCodeTraitement(String idAnnonce, CodeTraitement code) {
        final SimpleAnnonce simpleAnnonce = findModelById(idAnnonce);
        updateCodeTraitementAnnonce(simpleAnnonce, code);
    }

    public void updateEtatAnnoncePourRenvoi(String idAnnonce) {
        final SimpleAnnonce simpleAnnonce = findModelById(idAnnonce);
        updateEtatPourEnvoi(simpleAnnonce, EtatAnnonce.POUR_ENVOI, CodeTraitement.RETOUR_CORRIGE);
    }

    private void updateEtatPourEnvoi(SimpleAnnonce model, EtatAnnonce etat, CodeTraitement code) {
        AnnonceRpc annonceRpc = getConverter().convertToDomain(model);
        annonceRpc.setEtat(etat);
        annonceRpc.setCodeTraitement(code);
        new AnnonceRepositoryJade().update(annonceRpc);
    }

    public void updateEtatAnnonce(SimpleAnnonce model, EtatAnnonce etat) {
        AnnonceRpc annonceRpc = getConverter().convertToDomain(model);
        annonceRpc.setEtat(etat);
        new AnnonceRepositoryJade().update(annonceRpc);
    }

    public void updateCodeTraitementAnnonce(SimpleAnnonce model, CodeTraitement code) {
        AnnonceRpc annonceRpc = getConverter().convertToDomain(model);
        annonceRpc.setCodeTraitement(code);
        new AnnonceRepositoryJade().update(annonceRpc);
    }
}
